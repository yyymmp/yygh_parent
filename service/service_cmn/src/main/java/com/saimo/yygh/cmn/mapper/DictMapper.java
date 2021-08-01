package com.saimo.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saimo.yygh.model.cmn.Dict;
import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 批量插入
     */
    void insertBatch(List<Dict> list);
}
