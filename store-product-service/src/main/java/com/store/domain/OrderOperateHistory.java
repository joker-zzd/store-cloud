package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 订单操作历史表
 */
@TableName("oms_order_operate_history")
@Data
public class OrderOperateHistory {

    /**
     * 操作记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 操作人
     */
    private String operateMan;

    /**
     * 操作后的订单状态
     */
    private Integer orderStatus;

    /**
     * 操作备注
     */
    private String note;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
