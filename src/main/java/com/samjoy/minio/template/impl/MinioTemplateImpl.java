package com.samjoy.minio.template.impl;

import com.samjoy.minio.template.MinioTemplate;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MinioTemplate 接口实现
 *
 * @author Wenjie Li
 * @date 2022/4/10 16:26
 */
@Component
public class MinioTemplateImpl implements MinioTemplate {

    /**
     * slf4j日志
     */
    private static final Logger log = LoggerFactory.getLogger(MinioTemplateImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Override
    public Boolean bucketExists(String bucketName) throws MinioException {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new MinioException(String.format("检查桶：%s 是否存在失败!", bucketName));
        }
    }

    @Override
    public List<Bucket> listBuckets() throws MinioException {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new MinioException("列出所有桶失败!");
        }
    }

    @Override
    public Boolean createBucket(String bucketName) throws MinioException {
        try {
            // 先判断桶是否存在
            Boolean exists = this.bucketExists(bucketName);
            // 桶存在返回false
            if (Boolean.TRUE.equals(exists)) {
                return false;
            } else {
                // 桶不存在创建桶，并返回true
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                return true;
            }
        } catch (Exception e) {
            throw new MinioException(String.format("创建桶%s失败!", bucketName));
        }
    }


}
