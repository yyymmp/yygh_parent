package com.saimo.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author clearlove
 * @ClassName FileService.java
 * @Description
 * @createTime 2021年08月15日 23:56:00
 */
public interface FileService {

    String upload(MultipartFile file);
}
