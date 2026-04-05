package com.store.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.core.Local;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "sotre.file")
public class FileStorageProperties {

    /**
     * 单文件最大大小，单位字节。
     * 这里和 spring.servlet.multipart 配合使用，双重兜底。
     */
    private long maxFileSize = 1024 * 1024 * 10;

    /**
     * 允许上传的扩展名白名单
     */
    private List<String> allowedExtensions = List.of(
            "jpg", "jpeg", "png", "gif", "webp", "pdf", "doc", "docx", "xls", "xlsx"
    );

    /**
     * 允许上传的 MIME 类型白名单。
     */
    private List<String> allowedMimeTypes = List.of(

            "image/jpeg", "image/png", "image/gif", "image/webp", "application/pdf",
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    /**
     * 公开文件访问前缀
     */
    private String publicFilePrefix = "http://localhost:8080/api/file/public";

    /**
     * 私有文件访问前缀
     * 这个地址需要带token访问
     */
    private String privateFilePrefix = "http://localhost:8080/api/file/private";

    private Local local = new Local();

    @Data
    public static class Local {
        /**
         * 本地文件根目录
         */
        private String baseDir = "D:/store-cloud/upload-files";
    }
}
