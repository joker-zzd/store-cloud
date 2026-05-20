package com.store.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.store.common.exception.BusinessException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class OssClientConfig {

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(AliyunOssProperties properties) {
        if (!StringUtils.hasText(properties.getEndpoint())
                || !StringUtils.hasText(properties.getRegion())
                || !StringUtils.hasText(properties.getBucketName())
                || !StringUtils.hasText(properties.getAccessKeyId())
                || !StringUtils.hasText(properties.getAccessKeySecret())) {
            throw new BusinessException("OSS config is incomplete. Please check endpoint, region, bucketName, accessKeyId and accessKeySecret");
        }

        try {
            DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret()
            );

            ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
            configuration.setSignatureVersion(SignVersion.V4);

            return OSSClientBuilder.create()
                    .endpoint(properties.getEndpoint())
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(configuration)
                    .region(properties.getRegion())
                    .build();
        } catch (Exception exception) {
            throw new BusinessException("Failed to initialize OSS client. Please check access key config and OSS settings", exception);
        }
    }
}
