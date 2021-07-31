package com.saimo.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.common.result.Result;
import com.saimo.yygh.model.cmn.Dict;
import java.util.List;

public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);
}
