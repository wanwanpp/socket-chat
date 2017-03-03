package com.wp.webserver.httpServerPro;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 王萍 on 2017/3/3 0003.
 */
public class Request {

    private String uri;

    public String getUri() {
        return uri;
    }

    private InputStream inputStream;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    //根据请求头信息获取uri，如GET /index.html HTTP/1.1 ,可以返回/index.html
    public String parseUri(String request){
        int index1,index2;
        index1 = request.indexOf(" ");
        if (index1!=-1){
            index2 = request.indexOf(" ",index1+1);
            if (index1<index2){
                return  request.substring(index1+1,index2);
            }
        }
        return null;
    }

    //封装字节流
    //根据socket的InputStream读取整个字节流，存储在字节数组中，然后使用内存中的字节数组构建StringBUffer对象
    public void parse(){
        StringBuffer request = new StringBuffer(2048);
        byte[] bytes = new byte[2048];
        int i;
        try {
            i=inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            i=-1;
        }
        for (int j=0;j<i;j++){
            request.append((char)bytes[j]);
        }

        System.out.println("here"+request.toString());
        uri=parseUri(request.toString());
    }
}
