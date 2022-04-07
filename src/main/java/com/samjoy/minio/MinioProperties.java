package com.samjoy.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wenjie Li
 * @date 2022/4/7 14:47
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * minio服务的网络地址
     */
    private String url;

    /**
     * 认证账户
     */
    private String accessKey;

    /**
     * 账户的密码
     */
    private String secretKey;

    /**
     * 连接的桶的名称
     */
    private String bucket;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
