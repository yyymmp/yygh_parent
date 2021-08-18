package com.saimo.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.saimo.yygh.oss.service.FileService;
import com.saimo.yygh.oss.utils.ConstantOssPropertiesUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author clearlove
 * @ClassName FileService.java
 * @Description
 * @createTime 2021年08月15日 23:55:00
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String url = "";
        //文件夹分类:
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid + file.getOriginalFilename();

        //日期创建文件夹
        String dic = new DateTime().toString("yyyy/MM/dd");
        fileName = dic + "/" + fileName;
// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            PutObjectResult putObjectResult = ossClient.putObject(ConstantOssPropertiesUtils.BUCKET, fileName, inputStream);

            url = "https://" + ConstantOssPropertiesUtils.BUCKET + "." + endpoint + "/" + fileName;
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        return url;

    }
}
