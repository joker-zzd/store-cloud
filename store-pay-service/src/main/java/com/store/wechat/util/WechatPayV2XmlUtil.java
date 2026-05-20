package com.store.wechat.util;

import com.store.common.exception.BusinessException;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付 V2 XML 与签名工具。
 *
 * <p>V2 签名规则：
 * 1. 去掉 sign 字段。
 * 2. 去掉值为空的字段。
 * 3. 按参数名 ASCII 升序排序。
 * 4. 拼接 key=value&key=value。
 * 5. 末尾追加 &key=APIv2Key。
 * 6. 使用 MD5 或 HMAC-SHA256 计算签名。
 * 7. 签名结果转大写。
 */
public final class WechatPayV2XmlUtil {

    private WechatPayV2XmlUtil() {
    }

    public static String sign(Map<String, String> params, String apiV2Key, String signType) {
        if (!StringUtils.hasText(apiV2Key)) {
            throw new BusinessException("微信 APIv2 密钥未配置");
        }

        String signBase = buildSignBase(params, apiV2Key);
        if ("HMAC-SHA256".equalsIgnoreCase(signType)) {
            return hmacSha256(signBase, apiV2Key).toUpperCase(Locale.ROOT);
        }
        return md5(signBase).toUpperCase(Locale.ROOT);
    }

    public static boolean verifySign(Map<String, String> params, String apiV2Key, String signType) {
        String responseSign = params.get("sign");
        if (!StringUtils.hasText(responseSign)) {
            return false;
        }
        String calculatedSign = sign(params, apiV2Key, signType);
        return responseSign.equalsIgnoreCase(calculatedSign);
    }

    public static String toXml(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!StringUtils.hasText(entry.getValue())) {
                continue;
            }
            builder.append("<").append(entry.getKey()).append(">");
            builder.append("<![CDATA[").append(entry.getValue()).append("]]>");
            builder.append("</").append(entry.getKey()).append(">");
        }
        builder.append("</xml>");
        return builder.toString();
    }

    public static Map<String, String> fromXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // 安全配置：禁止外部实体，避免 XXE。
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            Document document = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            NodeList childNodes = document.getDocumentElement().getChildNodes();

            Map<String, String> result = new HashMap<>();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    result.put(node.getNodeName(), node.getTextContent());
                }
            }
            return result;
        } catch (Exception e) {
            throw new BusinessException("解析微信 XML 失败", e);
        }
    }

    private static String buildSignBase(Map<String, String> params, String apiV2Key) {
        TreeMap<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            if (!StringUtils.hasText(entry.getValue())) {
                continue;
            }
            sortedParams.put(entry.getKey(), entry.getValue());
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        builder.append("&key=").append(apiV2Key);
        return builder.toString();
    }

    private static String md5(String value) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return toHex(md5.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new BusinessException("生成微信 MD5 签名失败", e);
        }
    }

    private static String hmacSha256(String value, String apiV2Key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(apiV2Key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new BusinessException("生成微信 HMAC-SHA256 签名失败", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
