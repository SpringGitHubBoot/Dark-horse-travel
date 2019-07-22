package com.zhang.service;
//@author ZT 2019/7/14-15:49  

import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;

import java.util.Map;

public interface MemberService {

    Result login(String telephone);

    Map getMemberReport() throws Exception;

    Map getTotalMemberReport() throws Exception;

    PageResult getMember(QueryPageBean queryPageBean);
}
