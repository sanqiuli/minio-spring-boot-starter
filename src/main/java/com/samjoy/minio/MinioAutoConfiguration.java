package com.samjoy.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 自动配置 minio client
 *
 * @author Wenjie Li
 * @date 2022/4/7 19:35
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ConfigMarker.class, MinioClient.class})
public class MinioAutoConfiguration {

    /**
     * slf4j日志
     */
    private static final Logger log = LoggerFactory.getLogger(MinioAutoConfiguration.class);

    @Resource
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        // 使用MinIO服务的url(或称为endpoint)，Access key和Secret key创建一个MinioClient对象
        log.info("开始与Minio服务建立连接，Url为{},Access key为{}", minioProperties.getAccessKey(), minioProperties.getSecretKey());
        MinioClient minioClient = MinioClient.builder().endpoint(minioProperties.getUrl()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();

        // 判断需要连接的桶是否存在
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (bucketExists) {
                log.info("Bucket: {} 已经存在", minioProperties.getBucket());
            } else {
                log.info("Bucket: {} 不存在，开始创建Bucket: {}...", minioProperties.getBucket(), minioProperties.getBucket());
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
                log.info("Bucket: {} 创建成功", minioProperties.getBucket());
            }
        } catch (Exception e) {
            throw new Exception("创建桶失败", e);
        }
        return minioClient;
    }

}
