package com.zhang.service;
//@author ZT 2019/7/18-15:17  

import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<String,Object> getBusinessReport() throws Exception;

    /**
     * 会员性别占比
     * @return
     */
    List<Map<String,Object>> getGenderReport();

    /**
     * 年龄阶段占比
     * @return
     */
    List<Map<String,Object>> getAgeStageReport();
}
