package com.wp.webserver.MultiThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 王萍 on 2017/3/3 0003.
 */
public class PostAndGetWorkThread implements Runnable {

    private Socket socket = null;

    private String ServerName=null;
    public PostAndGetWorkThread(Socket socket, String serverName) {
        this.socket = socket;
        ServerName = serverName;
    }


    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1000];
            inputStream.read(bytes);
            for (byte a : bytes) {
                System.out.print((char) a);
            }
            System.out.println();
            System.out.println(new String(bytes));
            String method = new String(bytes).substring(0, 3);
            System.out.println(method);
            if (method.equals("GET")) {
                doGet();
            } else {
                doPost();
            }

            inputStream.close();
            System.out.println("响应了请求");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doGet() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(("HTTP/1.1 200 OK\n\nWorkerThread: " +
                    "this is by get").getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doPost() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(("HTTP/1.1 200 OK\n\nWorkerThread: " +
                    "this is by post").getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
