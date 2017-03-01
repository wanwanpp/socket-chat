package com.wp.third;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * java使用socket和awt组件以及多线程简单实现在线聊天功能服务端 ：
 * 实现服务端把接收到的客户端信息转发到所有连接的客户端，并且让客户端读取到这些信息并显示在内容显示区域中。
 *
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
    // 保存客户端集合
    List<Client> clients = new ArrayList<Client>();

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
        System.out.println("server is startting");
        try {
            isStart = true;
            while (isStart) {
                // 启动监听
                socket = ss.accept();
                System.out.println("one client connect");
                // 启动客户端线程
                Client client = new Client(socket);

                new Thread(client).start();
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭服务
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 客户端线程
     * 
     * @author tuzongxun123
     *
     */
    private class Client implements Runnable {
        // 客户端socket
        private Socket socket = null;
        // 客户端输入流
        private DataInputStream dataInputStream = null;
        // 客户端输出流
        private DataOutputStream dataOutputStream = null;
        private boolean isConnect = false;

        public Client(Socket socket) {
            this.socket = socket;
            try {
                isConnect = true;
                // 获取客户端输入流
                dataInputStream = new DataInputStream(socket.getInputStream());
                // 获取客户端输出流
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 向客户端群发（转发）数据
         * 
         * @author：tuzongxun
         * @Title: sendMessageToClients
         * @param @param message
         * @return void
         * @date May 18, 2016 11:28:10 AM
         * @throws
         */
        public void sendMessageToClients(String message) {
            try {
                dataOutputStream.writeUTF(message);
            } catch (SocketException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            isConnect = true;
            Client c = null;
            try {
                while (isConnect) {
                    // 读取客户端传递的数据
                    String message = dataInputStream.readUTF();
                    System.out.println("客户端说：" + message);

                    for (Client client:clients){
                        client.sendMessageToClients(message);
                    }

                }
            } catch (EOFException e) {
                System.out.println("client closed!");
            } catch (SocketException e) {
                if (c != null) {
                    clients.remove(c);
                }
                System.out.println("Client is Closed!!!!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭相关资源
                try {
                    if (dataInputStream != null) {
                        dataInputStream.close();
                    }
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
