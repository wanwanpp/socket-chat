package com.wp.webserver.httpServerPro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 王萍 on 2017/3/3 0003.
 */
public class HttpServer {

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.run();
    }

    boolean shutdown = false;

    public static int port = 8080;

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;
            try {
                socket = serverSocket.accept();
                in = socket.getInputStream();
                out = socket.getOutputStream();

                Request request = new Request(in);
                request.parse();
                String uri = request.getUri();
                if ("/shutdown".equals(uri)){
                    shutdown=true;
                }
                Response response = new Response(out);
                response.setRequest(request);
                response.sendStaticResource();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("正常退出");
    }
}
