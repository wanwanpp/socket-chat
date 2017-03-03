package com.wp.webserver.MultiThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class WorkThread implements Runnable {

        private Socket client = null;
        private String serverName = null;

        public WorkThread(Socket client, String serverName) {
            this.client = client;
            this.serverName = serverName;
        }

        @Override
        public void run() {
            try {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();
                outputStream.write(("HTTP/1.1 200 OK\n\nWorkThread: " +
                        this.serverName).getBytes());
                inputStream.close();
                outputStream.flush();
                System.out.println("已经响应了请求");
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }