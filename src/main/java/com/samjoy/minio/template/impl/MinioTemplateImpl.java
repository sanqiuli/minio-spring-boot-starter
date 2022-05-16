package com.samjoy.minio.template.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import com.samjoy.minio.MinioProperties;
import com.samjoy.minio.template.MinioTemplate;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    @Autowired
    private MinioProperties minioProperties;

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
                log.info("创建桶：{} 成功", bucketName);
                return true;
            }
        } catch (Exception e) {
            throw new MinioException(String.format("创建桶%s失败!", bucketName));
        }
    }

    @Override
    public String putFile(File file) throws MinioException {
        return this.putFile(file.getName(), file);
    }

    @Override
    public String putFile(String objectName, File file) throws MinioException {
        String fileName = getFileNameWithSnowflakeId(objectName);
        try {
            this.putObject(fileName, FileUtil.getInputStream(file), new MimetypesFileTypeMap().getContentType(file));
        } catch (Exception e) {
            throw new MinioException("上传失败");
        }
        return fileName;
    }

    @Override
    public String putMultipartFile(MultipartFile multipartFile) throws MinioException {
        return this.putMultipartFile(multipartFile.getName(), multipartFile);
    }

    @Override
    public String putMultipartFile(String objectName, MultipartFile multipartFile) throws MinioException {
        String fileName = getFileNameWithSnowflakeId(objectName);
        try {
            this.putObject(fileName, multipartFile.getInputStream(), multipartFile.getContentType());
        } catch (Exception e) {
            throw new MinioException("上传失败");
        }
        return fileName;
    }

    @Override
    public void putObject(String objectName, InputStream inputStream, String contentType) throws MinioException {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName).stream(
                            inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
            log.info("上传文件 {} 到 桶：{}成功", objectName, minioProperties.getBucket());
        } catch (Exception e) {
            throw new MinioException(String.format("上传文件%s 到 {bucket} %s 失败!", objectName, minioProperties.getBucket()));
        }
    }

    @Override
    public void removeObject(String objectName) throws MinioException {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName).build());
            log.info("删除桶：{}下的 {}成功", minioProperties.getBucket(), objectName);
        } catch (Exception e) {
            throw new MinioException(String.format("删除文件 %s 失败!", objectName));
        }
    }

    @Override
    public InputStream getObject(String objectName) throws MinioException {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName).build());
        } catch (Exception e) {
            throw new MinioException("根据文件名获取流失败!");
        }
    }

    @Override
    public InputStream getObjectByUrl(String url) throws MinioException {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new MinioException("根据URL获取流失败!");
        }
    }

    /**
     * 使用雪花算法，防止文件重名
     *
     * @param fileName 文件名
     * @return 使用雪花算法加上文件后缀名，保证文件名不会重复
     */
    private String getFileNameWithSnowflakeId(String fileName) {
        return FileUtil.getPrefix(fileName) + StrPool.DASHED + IdUtil.getSnowflake().nextIdStr() + StrPool.DOT + FileUtil.getSuffix(fileName);
    }

    @Override
    public String getTrueFileName(String objectName) {
        String prefix = FileUtil.getPrefix(objectName);
        return prefix.substring(0, objectName.length() - 20) + StrPool.DOT + FileUtil.getSuffix(objectName);
    }
}
