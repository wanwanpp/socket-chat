package com.wp.webserver.MultiThread;

import java.io.IOException;
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

    private static int number = 1;
    private int port;
    private ServerSocket serverSocket = null;
    private Thread runningThread = null;

    public MultiThreadWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (true) {
            Socket socket = null;
            try {
                socket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO: 2017/3/3 0003 浏览器请求一次,为什么服务器会得到两次请求.
            new Thread(new PostAndGetWorkThread(socket, "wanwanpp server" + MultiThreadWebServer.number++)).start();
            // TODO: 2017/3/3 0003 怎么判断生成了新的线程，什么情况下会生成新的线程？
            //经过下面的延时可以发现， Thread.sleep(1000); 在每次请求后都会产生新的线程，只不过很快会被回收，
            // 若延时时间太短，在visualVM中可能显示不出新线程的信息。

        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port" + this.port, e);
        }
    }


}
