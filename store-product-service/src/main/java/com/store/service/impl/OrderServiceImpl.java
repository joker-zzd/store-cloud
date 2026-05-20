package com.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.exception.BusinessException;
import com.store.domain.CartItem;
import com.store.domain.Coupon;
import com.store.domain.MemberAddress;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.OrderOperateHistory;
import com.store.domain.Product;
import com.store.domain.SkuStock;
import com.store.domain.dto.OrderSubmitDTO;
import com.store.domain.vo.OrderSubmitVO;
import com.store.mapper.CartItemMapper;
import com.store.mapper.CouponMapper;
import com.store.mapper.MemberAddressMapper;
import com.store.mapper.OrderItemMapper;
import com.store.mapper.OrderMapper;
import com.store.mapper.OrderOperateHistoryMapper;
import com.store.mapper.ProductMapper;
import com.store.mapper.SkuStockMapper;
import com.store.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单业务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    /**
     * 商品上架状态：1=上架
     */
    private static final int PRODUCT_ON_SALE = 1;

    /**
     * 订单状态：0=待付款
     */
    private static final int ORDER_WAIT_PAY = 0;

    /**
     * 订单类型：0=正常订单
     */
    private static final int NORMAL_ORDER = 0;

    /**
     * 支付方式：0=未选择
     */
    private static final int PAY_TYPE_NONE = 0;

    /**
     * 金额零值
     */
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    private final SkuStockMapper skuStockMapper;
    private final CouponMapper couponMapper;
    private final MemberAddressMapper memberAddressMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderOperateHistoryMapper orderOperateHistoryMapper;

    public OrderServiceImpl(CartItemMapper cartItemMapper,
                            ProductMapper productMapper,
                            SkuStockMapper skuStockMapper,
                            CouponMapper couponMapper,
                            MemberAddressMapper memberAddressMapper,
                            OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            OrderOperateHistoryMapper orderOperateHistoryMapper) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
        this.skuStockMapper = skuStockMapper;
        this.couponMapper = couponMapper;
        this.memberAddressMapper = memberAddressMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderOperateHistoryMapper = orderOperateHistoryMapper;
    }

    /**
     * 提交订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {
        checkSubmitParam(orderSubmitDTO);

        List<CartItem> cartItems = loadCartItems(orderSubmitDTO);
        MemberAddress address = loadAddress(orderSubmitDTO);
        Map<Long, Product> productMap = loadProductMap(cartItems);
        Map<Long, SkuStock> skuMap = loadSkuMap(cartItems);

        BigDecimal totalAmount = calculateTotalAmount(cartItems, productMap, skuMap);
        BigDecimal promotionAmount = calculatePromotionAmount(orderSubmitDTO.getCouponId(), totalAmount);
        BigDecimal freightAmount = ZERO;
        BigDecimal payAmount = totalAmount.add(freightAmount).subtract(promotionAmount);
        if (payAmount.compareTo(ZERO) < 0) {
            payAmount = ZERO;
        }

        deductStock(cartItems);

        String orderSn = generateOrderSn(orderSubmitDTO.getMemberId());
        Order order = buildOrder(orderSubmitDTO, address, orderSn, totalAmount, promotionAmount, freightAmount, payAmount);
        orderMapper.insert(order);

        saveOrderItems(cartItems, skuMap, order);
        saveOperateHistory(order.getId());
        deleteCartItems(orderSubmitDTO);

        return buildSubmitVO(order);
    }

    private void checkSubmitParam(OrderSubmitDTO orderSubmitDTO) {
        if (orderSubmitDTO == null) {
            throw new BusinessException("提交参数不能为空");
        }
        if (orderSubmitDTO.getMemberId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (CollectionUtils.isEmpty(orderSubmitDTO.getCartItemIds())) {
            throw new BusinessException("请选择要结算的商品");
        }
        if (orderSubmitDTO.getAddressId() == null) {
            throw new BusinessException("请选择收货地址");
        }
    }

    private List<CartItem> loadCartItems(OrderSubmitDTO orderSubmitDTO) {
        List<CartItem> cartItems = cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getMemberId, orderSubmitDTO.getMemberId())
                        .in(CartItem::getId, orderSubmitDTO.getCartItemIds())
        );

        if (cartItems.size() != orderSubmitDTO.getCartItemIds().size()) {
            throw new BusinessException("购物车商品不存在或不属于当前用户");
        }
        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                throw new BusinessException("购物车商品数量必须大于0");
            }
        }
        return cartItems;
    }

    private MemberAddress loadAddress(OrderSubmitDTO orderSubmitDTO) {
        MemberAddress address = memberAddressMapper.selectOne(
                new LambdaQueryWrapper<MemberAddress>()
                        .eq(MemberAddress::getId, orderSubmitDTO.getAddressId())
                        .eq(MemberAddress::getMemberId, orderSubmitDTO.getMemberId())
        );
        if (address == null) {
            throw new BusinessException("收货地址不存在或不属于当前用户");
        }
        return address;
    }

    private Map<Long, Product> loadProductMap(List<CartItem> cartItems) {
        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Product> products = productMapper.selectBatchIds(productIds);
        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private Map<Long, SkuStock> loadSkuMap(List<CartItem> cartItems) {
        List<Long> skuIds = cartItems.stream()
                .map(CartItem::getProductSkuId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<SkuStock> skuStocks = skuStockMapper.selectBatchIds(skuIds);
        return skuStocks.stream()
                .collect(Collectors.toMap(SkuStock::getId, Function.identity()));
    }

    private BigDecimal calculateTotalAmount(List<CartItem> cartItems,
                                            Map<Long, Product> productMap,
                                            Map<Long, SkuStock> skuMap) {
        BigDecimal totalAmount = ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = productMap.get(cartItem.getProductId());
            if (product == null) {
                throw new BusinessException("商品不存在");
            }
            if (!Objects.equals(product.getPublishStatus(), PRODUCT_ON_SALE)) {
                throw new BusinessException("商品已下架：" + product.getName());
            }

            SkuStock skuStock = skuMap.get(cartItem.getProductSkuId());
            if (skuStock == null) {
                throw new BusinessException("商品SKU不存在");
            }
            if (skuStock.getStock() == null || skuStock.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("库存不足：" + product.getName());
            }

            BigDecimal price = skuStock.getPrice() == null ? ZERO : skuStock.getPrice();
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePromotionAmount(Long couponId, BigDecimal totalAmount) {
        if (couponId == null) {
            return ZERO;
        }

        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        if (!Objects.equals(coupon.getStatus(), 3)) {
            throw new BusinessException("优惠券不可用");
        }
        if (coupon.getThresholdAmount() != null
                && totalAmount.compareTo(BigDecimal.valueOf(coupon.getThresholdAmount())) < 0) {
            throw new BusinessException("未达到优惠券使用门槛");
        }

        BigDecimal discountValue = BigDecimal.valueOf(coupon.getDiscountValue());
        BigDecimal promotionAmount;

        if (Objects.equals(coupon.getDiscountType(), 1)) {
            promotionAmount = discountValue;
        } else if (Objects.equals(coupon.getDiscountType(), 3)) {
            BigDecimal discountRate = discountValue.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            promotionAmount = totalAmount.multiply(BigDecimal.ONE.subtract(discountRate));
        } else if (Objects.equals(coupon.getDiscountType(), 4)) {
            promotionAmount = discountValue;
        } else {
            promotionAmount = ZERO;
        }

        if (coupon.getMaxDiscountAmount() != null && coupon.getMaxDiscountAmount() > 0) {
            BigDecimal maxDiscount = BigDecimal.valueOf(coupon.getMaxDiscountAmount());
            if (promotionAmount.compareTo(maxDiscount) > 0) {
                promotionAmount = maxDiscount;
            }
        }
        if (promotionAmount.compareTo(totalAmount) > 0) {
            promotionAmount = totalAmount;
        }
        return promotionAmount;
    }

    private void deductStock(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            int rows = skuStockMapper.deductStock(cartItem.getProductSkuId(), cartItem.getQuantity());
            if (rows != 1) {
                throw new BusinessException("库存扣减失败，请重新下单");
            }
        }
    }

    private Order buildOrder(OrderSubmitDTO orderSubmitDTO,
                             MemberAddress address,
                             String orderSn,
                             BigDecimal totalAmount,
                             BigDecimal promotionAmount,
                             BigDecimal freightAmount,
                             BigDecimal payAmount) {
        Order order = new Order();
        order.setMemberId(orderSubmitDTO.getMemberId());
        order.setCouponId(orderSubmitDTO.getCouponId());
        order.setOrderSn(orderSn);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(payAmount);
        order.setFreightAmount(freightAmount);
        order.setPromotionAmount(promotionAmount);
        order.setPayType(PAY_TYPE_NONE);
        order.setStatus(ORDER_WAIT_PAY);
        order.setOrderType(NORMAL_ORDER);
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverDetailAddress(buildFullAddress(address));
        order.setNote(orderSubmitDTO.getNote());
        return order;
    }

    private void saveOrderItems(List<CartItem> cartItems, Map<Long, SkuStock> skuMap, Order order) {
        for (CartItem cartItem : cartItems) {
            SkuStock skuStock = skuMap.get(cartItem.getProductSkuId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductSkuId(cartItem.getProductSkuId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setProductPrice(skuStock.getPrice());
            orderItem.setProductQuantity(cartItem.getQuantity());
            orderItem.setSpData(cartItem.getSpData());
            orderItemMapper.insert(orderItem);
        }
    }

    private void saveOperateHistory(Long orderId) {
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(orderId);
        history.setOperateMan("member");
        history.setOrderStatus(ORDER_WAIT_PAY);
        history.setNote("用户提交订单");
        orderOperateHistoryMapper.insert(history);
    }

    private void deleteCartItems(OrderSubmitDTO orderSubmitDTO) {
        cartItemMapper.delete(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getMemberId, orderSubmitDTO.getMemberId())
                        .in(CartItem::getId, orderSubmitDTO.getCartItemIds())
        );
    }

    private OrderSubmitVO buildSubmitVO(Order order) {
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setOrderId(order.getId());
        orderSubmitVO.setOrderSn(order.getOrderSn());
        orderSubmitVO.setTotalAmount(order.getTotalAmount());
        orderSubmitVO.setPromotionAmount(order.getPromotionAmount());
        orderSubmitVO.setFreightAmount(order.getFreightAmount());
        orderSubmitVO.setPayAmount(order.getPayAmount());
        orderSubmitVO.setPayType(order.getPayType());
        orderSubmitVO.setStatus(order.getStatus());
        return orderSubmitVO;
    }

    private String buildFullAddress(MemberAddress address) {
        return nullToEmpty(address.getProvince())
                + nullToEmpty(address.getCity())
                + nullToEmpty(address.getRegion())
                + nullToEmpty(address.getDetailAddress());
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String generateOrderSn(Long memberId) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return time + memberId;
    }
}
