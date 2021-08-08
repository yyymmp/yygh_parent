package com.saimo.yygh.cmn.controller;

import com.saimo.yygh.cmn.service.DictService;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author clearlove
 * @ClassName DictController.java
 * @Description
 * @createTime 2021年07月31日 14:32:00
 */
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;


    @ApiOperation(value = "根据dict_code查询子节点数据")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable("dictCode") String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据id查询子节点数据")
    @GetMapping("/findChildData/{id}")
    public Result<List<Dict>> findChildData(@PathVariable Long id) {
        return Result.ok(dictService.findChildData(id));
    }

    @ApiOperation(value = "exportData")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse httpResponse) {
        dictService.exportData(httpResponse);
    }

    @ApiOperation(value = "importData")
    @GetMapping("/importData")
    public void importData(MultipartFile file) {
        dictService.importData(file);
    }

    @ApiOperation(value = "根据depCode和value获取字典名")
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(
            @PathVariable String dictCode
            , @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    @ApiOperation(value = "根据depCode和value获取字典名")
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value) {
        String dictName = dictService.getDictName("", value);
        return dictName;
    }
}
