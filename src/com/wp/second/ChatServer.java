package com.wp.second;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 王萍 on 2017/3/1 0001.
 */
public class ChatServer {

    public static void main(String[] args) {
        new ChatServer().start();
    }

    // 是否成功启动服务端
    private boolean isStart = false;
    // 服务端socket
    private ServerSocket ss = null;
    // 客户端socket
    private Socket socket = null;

    public void start() {
        try {
            // 启动服务器
            ss = new ServerSocket(8888);
        } catch (BindException e) {
            System.out.println("端口已在使用中");
            // 关闭程序
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            isStart = true;
            while (isStart) {
                // 启动监听
                socket = ss.accept();
                System.out.println("one client connect");
                Client client =new Client(socket);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Client implements Runnable {

        public Client(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            isConnect = true;
        }

        Socket socket = null;

        private DataInputStream dataInputStream = null;

        private boolean isConnect = false;

        @Override
        public void run() {
            try {
                while (isConnect) {
                    String message = dataInputStream.readUTF();
                    System.out.println("客户端说："+message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Client is Closed!!!!");
            }finally {
                try {
                    dataInputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}