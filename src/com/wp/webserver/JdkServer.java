package com.wp.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by 王萍 on 2017/3/2 0002.
 */
public class JdkServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000),0);
        server.createContext("/test",new Handle());
        server.setExecutor(null);
        server.start();

    }

    static class Handle implements HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String str = "真的";
//            String str = "haha";
            //注意这里返回的头信息中的长度应该是字节长度。
            httpExchange.sendResponseHeaders(200,str.getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(str.getBytes());
            outputStream.close();
        }
    }
}
