package com.saimo.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saimo.yygh.cmn.mapper.Dictmapper;
import com.saimo.yygh.cmn.service.DictService;
import com.saimo.yygh.model.cmn.Dict;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author clearlove
 * @ClassName DictServiceImpl.java
 * @Description
 * @createTime 2021年07月31日 14:30:00
 */
@Service
public class DictServiceImpl extends ServiceImpl<Dictmapper, Dict> implements DictService {

    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dicts = this.baseMapper.selectList(queryWrapper);
        dicts.forEach(dict -> {
            if (isChildren(dict.getId())) {
                dict.setHasChildren(true);
            }
        });

        return dicts;
    }

    /**
     * 判断该结点下是否有子节点
     *
     * @param id
     * @return
     */
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        return baseMapper.selectCount(queryWrapper) > 1;
    }
}
