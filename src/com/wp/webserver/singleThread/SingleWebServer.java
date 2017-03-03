package com.wp.webserver.singleThread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by 王萍 on 2017/3/2 0002.
 */
public class SingleWebServer implements Runnable {

    private int port = 8080;
    private ServerSocket serverSocket = null;
    private Thread thread = null;

    public SingleWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        synchronized (this){
            this.thread  = Thread.currentThread();
        }
        openServerSocket();

        System.out.println("服务器已经启动");

        while (true){
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                processClientRequest(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openServerSocket(){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port"+this.port,e);
        }
    }

    private void processClientRequest(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(("HTTP/1.1 200 OK\n\n<html><body>" +
                "webserver of wanwanpp" +
                "</body></html>").getBytes());

        outputStream.close();
        System.out.println("响应一次请求");
    }

    public static void main(String[] args) {
        SingleWebServer webServer = new SingleWebServer(9000);
        new Thread(webServer).start();
    }
}
