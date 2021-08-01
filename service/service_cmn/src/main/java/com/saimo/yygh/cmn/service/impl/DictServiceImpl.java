package com.saimo.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saimo.yygh.cmn.listener.DictListener;
import com.saimo.yygh.cmn.mapper.DictMapper;
import com.saimo.yygh.cmn.service.DictService;
import com.saimo.yygh.model.cmn.Dict;
import com.saimo.yygh.vo.cmn.DictEeVo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author clearlove
 * @ClassName DictServiceImpl.java
 * @Description
 * @createTime 2021年07月31日 14:30:00
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

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

    @Override
    public void exportData(HttpServletResponse httpResponse) {
        String fileName = "dict";
        //设置下载信息
        httpResponse.setContentType("application/vnd.ms-excel");
        httpResponse.setCharacterEncoding("utf8");
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        httpResponse.setHeader("Pragma", "public");
        httpResponse.setHeader("Cache-Control", "no-store");
        httpResponse.addHeader("Cache-Control", "max-age=0");

        //获取信息
        List<Dict> dicts = baseMapper.selectList(null);
        //dict -> DictEeVo
        List<DictEeVo> dictEeVos = new ArrayList<>(dicts.size());
        dicts.forEach(dict -> {
                    DictEeVo dictEeVo = new DictEeVo();
                    BeanUtils.copyProperties(dict, dictEeVo);
                    dictEeVos.add(dictEeVo);
                }
        );
        //将表格数据写入道输出流
        try {
            EasyExcel.write(httpResponse.getOutputStream(), DictEeVo.class).
                    sheet("dict").
                    doWrite(dictEeVos);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException ioException) {
            log.info("读取excel异常:{}", ioException.getMessage());
        }
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
