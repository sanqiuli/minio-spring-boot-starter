package com.samjoy.minio.template;

import io.minio.errors.MinioException;
import io.minio.messages.Bucket;

import java.util.List;

/**
 * MinioTemplate api接口
 *
 * @author Wenjie Li
 * @date 2022/4/10 16:21
 */
public interface MinioTemplate {

    /**
     * 检查bucket(桶) 是否存在
     *
     * @param bucketName 桶名称
     * @return true：bucket存在，false：bucket不存在
     */
    Boolean bucketExists(String bucketName) throws MinioException;


    /**
     * 列出所有的桶
     *
     * @return 桶的列表
     */
    List<Bucket> listBuckets() throws MinioException;

    /**
     * 创建一个新的桶
     *
     * @param bucketName 桶名称
     * @return true：创建新桶成功，false：创建的桶已经存在存在
     */
    Boolean createBucket(String bucketName) throws MinioException;

}
