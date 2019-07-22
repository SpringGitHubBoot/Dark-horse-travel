package com.zhang.controller;
//@author ZT 2019/7/16-19:56  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.pojo.Result;
import com.zhang.service.MemberService;
import com.zhang.service.ReportService;
import com.zhang.service.SetMealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"report"})
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;

    @RequestMapping(value = {"getMemberReport"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result getMemberReport() throws Exception {

        try {
            Map map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    @RequestMapping(value = {"getTotalMemberReport"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result getTotalMemberReport() throws Exception {
        try {
            Map map = memberService.getTotalMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //获取月份用户getTotalMemberReport2
    @RequestMapping(value = {"getTotalMemberReport2"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result getTotalMemberReport2(@RequestBody Map<String,List<String>> map) throws Exception {
        try {
            if(map ==null || map.size()<=0){
                return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
            }

            Map result =  memberService.getTotalMemberReport2(map);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    public static void main(String[] args) {
        String s = "2019-07-21";
        String substring = s.substring(s.lastIndexOf("-"));
        String[] split = s.split(substring);
        for (String s1 : split) {
            System.out.println(s1);
        }
    }





    @RequestMapping(value = {"getSetMealReport"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result getSetMealReport() throws Exception {

        try {
            Map map = setMealService.getSetMealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping(value = {"getBusinessReportData"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result getBusinessReportData() throws Exception {

        try {
            Map map = reportService.getBusinessReport();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping(value = {"exportBusinessReport"})
    @PreAuthorize(value = "hasAnyAuthority('REPORT_VIEW')")
    public Result exportBusinessReport(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {

        //先获取excel表格需要的数据，之前已经从数据库中查出了
        Map result = reportService.getBusinessReport();

        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        //文件可能运行在不同系统，获取绝对路径的方式 重要！！！
        String templateRealPath = request.getSession().getServletContext().
                getRealPath("template") + File.separator + "report_template.xlsx";
        //加载模板文件，获取输入流
        FileInputStream inputStream = new FileInputStream(new File(templateRealPath));
        //在创建工作空间，也就是excel表时，将输入流作为参数引入，工作空间直接加载已有的表格格式
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //获取excel的第一张sheet表
        XSSFSheet sheet = workbook.getSheetAt(0);
        //通过sheet获取行和列
        XSSFRow row = sheet.getRow(2);
        XSSFCell cell = row.getCell(5);
        //给单元格赋值
        cell.setCellValue(reportDate);

        row = sheet.getRow(4);
        row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
        row.getCell(7).setCellValue(totalMember);//总会员数

        row = sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
        row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

        row = sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
        row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

        row = sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
        row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

        row = sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
        row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

        int rowNum = 12;
        for (Map map : hotSetmeal) {//热门套餐
            String name = (String) map.get("name");
            Long setmeal_count = (Long) map.get("setmeal_count");
            BigDecimal proportion = (BigDecimal) map.get("proportion");
            row = sheet.getRow(rowNum++);
            row.getCell(4).setCellValue(name);//套餐名称
            row.getCell(5).setCellValue(setmeal_count);//预约数量
            row.getCell(6).setCellValue(proportion.doubleValue());//占比
        }

        //通过响应对象获取输出流，将数据输出到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        //支持浏览器以附件的形式进行下载
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
        //内存中的excel将数据写出到浏览器的excel文件中
        workbook.write(outputStream);
        //关闭流
        outputStream.flush();
        outputStream.close();
        workbook.close();
        return null;
    }
}

