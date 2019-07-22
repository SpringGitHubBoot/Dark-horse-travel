package com.zhang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/setMember")
public class MemberController {

@Reference
 private MemberService memberService;

     @PreAuthorize(value ="hasAuthority('ROLE_QUERY')")
     @RequestMapping("/getMember")
    public PageResult getMember(@RequestBody QueryPageBean queryPageBean){

         return memberService.getMember(queryPageBean);



     }


}
