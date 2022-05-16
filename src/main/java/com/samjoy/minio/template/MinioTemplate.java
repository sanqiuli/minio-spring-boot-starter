package com.samjoy.minio.template;

import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
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
     * @throws MinioException
     */
    Boolean bucketExists(String bucketName) throws MinioException;


    /**
     * 列出所有的桶
     *
     * @return 桶的列表
     * @throws MinioException
     */
    List<Bucket> listBuckets() throws MinioException;

    /**
     * 创建一个新的桶
     *
     * @param bucketName 桶名称
     * @return true：创建新桶成功，false：创建的桶已经存在存在
     * @throws MinioException
     */
    Boolean createBucket(String bucketName) throws MinioException;

    /**
     * 上传文件到bucket
     *
     * @param file 需要上传文件
     * @return 保存的文件名
     * @throws MinioException
     */
    String putFile(File file) throws MinioException;

    /**
     * 上传文件到bucket
     *
     * @param objectName 自定义文件名
     * @param file       需要上传的文件
     * @return 实际保存的文件名
     * @throws MinioException
     */
    String putFile(String objectName, File file) throws MinioException;

    /**
     * 上传MultipartFile文件到bucket
     *
     * @param multipartFile 需要上传的multipartFile
     * @return 实际保存的文件名
     * @throws MinioException
     */
    String putMultipartFile(MultipartFile multipartFile) throws MinioException;

    /**
     * 上传MultipartFile文件到bucket
     *
     * @param objectName    自定义文件名
     * @param multipartFile 需要上传的multipartFile
     * @return 实际保存的文件名
     * @throws MinioException
     */
    String putMultipartFile(String objectName, MultipartFile multipartFile) throws MinioException;

    /**
     * 使用流的方式保存文件名
     *
     * @param objectName  自定义文件名
     * @param inputStream 输入流
     * @param contentType 文件类型
     * @throws MinioException
     */
    void putObject(String objectName, InputStream inputStream, String contentType) throws MinioException;

    /**
     * 根据文件名删除文件
     *
     * @param objectName
     * @throws MinioException
     */
    void removeObject(String objectName) throws MinioException;

    /**
     * 根据文件名获取文件的输出流
     *
     * @param objectName 文件名
     * @throws MinioException
     */
    InputStream getObject(String objectName) throws MinioException;

    /**
     * 根据URL获取文件的输出流
     *
     * @param url 文件的网络路径
     * @throws MinioException
     */
    InputStream getObjectByUrl(String url) throws MinioException;

    /**
     * 根据文件名获取真实的文件名称
     *
     * @param objectName 文件名称
     * @return 真实的文件名称
     */
    String getTrueFileName(String objectName);
}
