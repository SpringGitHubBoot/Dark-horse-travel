package com.zhang.dao;
//@author ZT 2019/7/13-16:25  

import com.zhang.entity.Member;

import java.util.Date;
import java.util.Map;

public interface MemberDao {

    Member selectMemberByPhoneNumber(String telephone);

    void add(Member member);

    Integer selectMemberCountByDate(Map map);

    Integer selectTotalMemberCountByDate(String dateStr);

    Integer getTodayNewMember(String reportDate);

    Integer getTotalMember();

    Integer getThisWeekNewMember(Date firstDayOfWeek);

    Integer getThisMonthNewMember(Date firstDay4ThisMonth);
}
