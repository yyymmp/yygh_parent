package com.saimo.hospital.cmnclient;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author clearlove
 * @ClassName DictFeignClient.java
 * @Description
 * @createTime 2021年08月08日 13:37:00
 */
//远程调用的服务名
@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {

    @ApiOperation(value = "根据depCode和value获取字典名")
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(
            @PathVariable("dictCode") String dictCode
            , @PathVariable("value") String value);

    @ApiOperation(value = "根据depCode和value获取字典名")
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
