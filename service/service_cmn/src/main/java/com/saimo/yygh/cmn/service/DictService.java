package com.saimo.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saimo.yygh.model.cmn.Dict;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);

    void exportData(HttpServletResponse httpResponse);

    void importData(MultipartFile file);

    String getDictName(String depCode, String value);
}
