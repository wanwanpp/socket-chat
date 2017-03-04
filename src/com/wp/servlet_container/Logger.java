package com.wp.servlet_container;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 王萍 on 2017/3/4 0004.
 */
public class Logger<T> {

    private String className = "";

    public Logger(Class clazz) {
        this.className = clazz.getName();
    }

    public void log(T msg){
        System.out.println(className+"(" +
                (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"):"+msg);
    }
}
