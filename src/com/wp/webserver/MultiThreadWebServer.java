package com.wp.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 王萍 on 2017/3/3 0003.
 */
public class MultiThreadWebServer implements Runnable {


    public static void main(String[] args) {
        MultiThreadWebServer server = new MultiThreadWebServer(9000);
        new Thread(server).start();
    }
    private static int number=1;
    private int port;
    private ServerSocket serverSocket = null;
    private Thread runningThread = null;

    public MultiThreadWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        openServerSocket();
        while (true){
            Socket socket = null;
            try {
                socket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(new WorkThread(socket,"wanwanpp server"+MultiThreadWebServer.number++)).start();

        }
    }

    private void openServerSocket(){
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port"+this.port,e);
        }
    }

    class WorkThread implements Runnable{

        private Socket client = null;
        private String serverName = null;

        public WorkThread(Socket client, String serverName) {
            this.client = client;
            this.serverName = serverName;
        }

        @Override
        public void run() {
            try {
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
}
