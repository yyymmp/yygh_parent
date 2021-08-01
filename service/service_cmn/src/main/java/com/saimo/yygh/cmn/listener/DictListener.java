package com.saimo.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.saimo.yygh.cmn.mapper.DictMapper;
import com.saimo.yygh.model.cmn.Dict;
import com.saimo.yygh.vo.cmn.DictEeVo;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author clearlove
 * @ClassName DictListener.java
 * @Description
 * @createTime 2021年07月31日 18:38:00
 */
@Slf4j
public class DictListener extends AnalysisEventListener<DictEeVo> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1;
    List<Dict> list = new ArrayList<>();

    private DictMapper dictmapper;

    public DictListener(DictMapper dictmapper) {
        this.dictmapper = dictmapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(dictEeVo));
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictmapper.insert(dict);
        //todo
//        list.add(dict);
//        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (list.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            list.clear();
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        dictmapper.insertBatch(list);
        log.info("存储数据库成功！");
    }
}
