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

/**
 * @author clearlove
 * @ClassName DictController.java
 * @Description
 * @createTime 2021年07月31日 14:32:00
 */
@RestController
@RequestMapping("/admin/cmn/doct")
public class DictController {

    @Autowired
    private DictService dictService;

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
}
