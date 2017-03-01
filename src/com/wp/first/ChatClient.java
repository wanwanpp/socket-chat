package com.wp.first;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 在线聊天客户端 1、生成图形窗口界面轮廓 2、为轮廓添加关闭事件 3、在轮廓中加入输入区域和内容展示区域 4、为输入区域添加回车事件
 * 5、建立服务端连接并发送数据
 *
 * @author tuzongxun123
 *
 */
public class ChatClient extends Frame {
    // 用户输入区域  
    private TextField tfTxt = new TextField();  
    // 内容展示区域  
    private TextArea tarea = new TextArea();  
    private Socket socket = null;
    // 数据输出流  
    private DataOutputStream dataOutputStream = null;
  
    public static void main(String[] args) {  
        new ChatClient().launcFrame();  
    }  
  
    /** 
     * 建立一个简单的图形化窗口 
     *  
     * @author：tuzongxun 
     * @Title: launcFrame 
     * @param 
     * @return void 
     * @date May 18, 2016 9:57:00 AM 
     * @throws 
     */  
    public void launcFrame() {  
        setLocation(300, 200);  
        this.setSize(200, 400);  
        add(tfTxt, BorderLayout.SOUTH);
        add(tarea, BorderLayout.NORTH);  
        pack();  
        // 监听图形界面窗口的关闭事件  
        this.addWindowListener(new WindowAdapter() {
  
            @Override  
            public void windowClosing(WindowEvent e) {
                System.exit(0);  
                disConnect();  
            }  
        });  
        tfTxt.addActionListener(new TFLister());  
        setVisible(true);  
        connect();  
    }  
  
    /** 
     * 连接服务器 
     *  
     * @author：tuzongxun 
     * @Title: connect 
     * @param 
     * @return void 
     * @date May 18, 2016 9:56:49 AM 
     * @throws 
     */  
    public void connect() {  
        try {  
            // 新建服务端连接  
            socket = new Socket("127.0.0.1", 8888);  
            // 获取客户端输出流  
            dataOutputStream = new DataOutputStream(socket.getOutputStream());  
            System.out.println("连上服务端");  
        } catch (UnknownHostException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 关闭客户端资源 
     *  
     * @author：tuzongxun 
     * @Title: disConnect 
     * @param 
     * @return void 
     * @date May 18, 2016 9:57:46 AM 
     * @throws 
     */  
    public void disConnect() {  
        try {  
            dataOutputStream.close();  
            socket.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 向服务端发送消息 
     *  
     * @author：tuzongxun 
     * @Title: sendMessage 
     * @param @param text 
     * @return void 
     * @date May 18, 2016 9:57:56 AM 
     * @throws 
     */  
    private void sendMessage(String text) {  
        try {  
            dataOutputStream.writeUTF(text);  
            dataOutputStream.flush();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
  
    /** 
     * 图形窗口输入区域监听回车事件 
     *  
     * @author tuzongxun123 
     * 
     */  
    private class TFLister implements ActionListener {
  
        @Override  
        public void actionPerformed(ActionEvent e) {
            String text = tfTxt.getText().trim();  
            tarea.setText(text);  
            tfTxt.setText("");  
            // 回车后发送数据到服务器  
            sendMessage(text);  
        }  
    }  
}  