package com.zhang.service;
//@author ZT 2019/7/14-15:49  

import com.zhang.pojo.Result;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Result login(String telephone);

    Map getMemberReport() throws Exception;

    Map getTotalMemberReport() throws Exception;

    //获取月份用户
    Map getTotalMemberReport2(Map<String, List<String>> map) throws Exception;
}
