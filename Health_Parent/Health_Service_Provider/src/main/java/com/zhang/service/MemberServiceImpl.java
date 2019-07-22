package com.zhang.service;
//@author ZT 2019/7/14-15:57  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.constance.MessageConstant;
import com.zhang.dao.MemberDao;
import com.zhang.entity.Member;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result login(String telephone) {
        //通过用户手机号查询用户表
        Member member = memberDao.selectMemberByPhoneNumber(telephone);
        //如果查询出用户不为null，说明该用户已经注册了，返回登录成功信息
        if (member != null) {
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }

        //如果查不到用户，说明用户未注册，接着为他注册
        member = new Member();
        member.setPhoneNumber(telephone);
        member.setRegTime(new Date());
        memberDao.add(member);

        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @Override
    //获取最近一年的会员报表，展示每个月的注册会员人数
    public Map getMemberReport() throws Exception {

        ArrayList<String> months = new ArrayList<>();
        ArrayList<Integer> memberCount = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            String string = DateUtils.parseDate2String(date, "yyyy.MM");
            months.add(string);
            String beginDate = string + ".1";
            String endDate = string + ".31";
            Map<Object, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            Integer count = memberDao.selectMemberCountByDate(map);
            memberCount.add(count);
        }
        Map map = new HashMap<>();
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }

    @Override
    //获取最近一年每个月的会员总数报表，用分布查询实现
    public Map getTotalMemberReport() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        ArrayList<String> months = new ArrayList<>();
        ArrayList<Integer> memberCount = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            String string = DateUtils.parseDate2String(date, "yyyy.MM");
            String dateStr = string + ".31";
            Integer totalMemberCount = memberDao.selectTotalMemberCountByDate(dateStr);
            months.add(string);
            memberCount.add(totalMemberCount);
        }
        Map map = new HashMap();
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }



    //会员数量报表指定时间段展示

    public Map getTotalMemberReport2(Map<String, List<String>> map) throws Exception {
        ArrayList<String> months = new ArrayList<>();
        ArrayList<Integer> memberCount = new ArrayList<>();


        List<String> list = map.get("list");

        //获得开始2019-07
        String substring0 = list.get(0).substring(list.get(0).lastIndexOf("-"));
        String[] startMonth = list.get(0).split(substring0);

        //获得结束2019-09
        String substring1 = list.get(1).substring(list.get(1).lastIndexOf("-"));
        String[] engMonth = list.get(1).split(substring1);

        //获得区间

        List<String> monthBetween = DateUtils.getMonthBetween(startMonth[0], engMonth[0], "yyyy-MM");

        //添加到months
        for (String month : monthBetween) {
            Integer totalMemberCount = memberDao.selectTotalMemberCountByDate(month);
            memberCount.add(totalMemberCount);
            months.add(month);
        }


        Map<String,Object> hashMap = new HashMap();
        hashMap.put("months", months);
        hashMap.put("memberCount", memberCount);

        return hashMap;
    }

    @Override
    public PageResult getMember(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        if (queryString == null) {
            queryString = "";
        }
        Page member = memberDao.findMember("%" + queryString + "%");

        return new PageResult(member.getTotal(), member.getResult());

    }
}
