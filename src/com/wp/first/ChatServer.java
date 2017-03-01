package com.wp.first;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * java使用socket和awt组件简单实现在线聊天功能服务端 可以实现一个客户端连接后不断向服务端发送消息 
 * 但不支持多个客户端同时连接，原因在于代码中获得客户端连接后会一直循环监听客户端输入，造成阻塞 
 * 以至于服务端无法二次监听另外的客户端，如要实现，需要使用异步或者多线程 
 *  
 * @author tuzongxun123 
 * 
 */  
public class ChatServer {  
  
    public static void main(String[] args) {  
        // 是否成功启动服务端  
        boolean isStart = false;  
        // 服务端socket  
        ServerSocket ss = null;
        // 客户端socket  
        Socket socket = null;
        // 服务端读取客户端数据输入流  
        DataInputStream dataInputStream = null;
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
                boolean isConnect = false;  
                // 启动监听  
                socket = ss.accept();  
                System.out.println("one client connect");  
                isConnect = true;  
                while (isConnect) {  
                    // 获取客户端输入流  
                    dataInputStream = new DataInputStream(  
                            socket.getInputStream());  
                    // 读取客户端传递的数据  
                    String message = dataInputStream.readUTF();  
                    System.out.println("客户端说：" + message);  
                }  
  
            }  
        } catch (EOFException e) {
            System.out.println("client closed!");  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭相关资源  
            try {  
                dataInputStream.close();  
                socket.close();  
            } catch (IOException e) {
                e.printStackTrace();  
            }  
        }  
    }  
}  