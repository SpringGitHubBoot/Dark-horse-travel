package com.zhang.converter;
//@author ZT 2019/7/12-8:20  

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String, Date> {


    @Override
    public Date convert(String s) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
