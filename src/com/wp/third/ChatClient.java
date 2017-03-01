package com.wp.third;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 用户输入区域  
    private TextField tfTxt = new TextField();
    // 内容展示区域  
    private TextArea tarea = new TextArea();
    private Socket socket = null;
    // 数据输出流  
    private DataOutputStream dataOutputStream = null;
    // 数据输入流  
    private DataInputStream dataInputStream = null;
    private boolean isConnect = false;
    Thread tReceive = new Thread(new ReceiveThread());
    String name = "";

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.createName(3);
        chatClient.launcFrame();

    }

    /**
     * 建立一个简单的图形化窗口
     *
     * @param
     * @return void
     * @throws
     * @author：tuzongxun
     * @Title: launcFrame
     * @date May 18, 2016 9:57:00 AM
     */
    public void launcFrame() {
        setLocation(300, 200);
        this.setSize(200, 400);
        add(tfTxt, BorderLayout.SOUTH);
        add(tarea, BorderLayout.NORTH);
        // 根据窗口里面的布局及组件的preferedSize来确定frame的最佳大小  
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
        // 设置窗口可见  
        setVisible(true);
        connect();
        // 启动接受消息的线程  
        tReceive.start();
    }

    /**
     * 连接服务器
     *
     * @param
     * @return void
     * @throws
     * @author：tuzongxun
     * @Title: connect
     * @date May 18, 2016 9:56:49 AM
     */
    public void connect() {
        try {
            // 新建服务端连接  
            socket = new Socket("123.207.249.95", 8888);
            // 获取客户端输出流  
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            System.out.println("连上服务端");
            isConnect = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 生成随机的客户端名字  
    public void createName(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int n = (int) (Math.random() * str.length());
            char c = str.charAt(n);
            name = name + c;
        }
        System.out.println(name);
        this.setTitle(name);
    }

    /**
     * 关闭客户端资源
     *
     * @param
     * @return void
     * @throws
     * @author：tuzongxun
     * @Title: disConnect
     * @date May 18, 2016 9:57:46 AM
     */
    public void disConnect() {
        try {
            isConnect = false;
            // 停止线程  
            tReceive.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
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

    /**
     * 向服务端发送消息
     *
     * @param @param text
     * @return void
     * @throws
     * @author：tuzongxun
     * @Title: sendMessage
     * @date May 18, 2016 9:57:56 AM
     */
    private void sendMessage(String text) {
        try {
            dataOutputStream.writeUTF(name + ":" + text);
            dataOutputStream.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 图形窗口输入区域监听回车事件
     *
     * @author tuzongxun123
     */
    private class TFLister implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = tfTxt.getText().trim();
            // 清空输入区域信息  
            tfTxt.setText("");
            // 回车后发送数据到服务器  
            sendMessage(text);
        }

    }

    private class ReceiveThread implements Runnable {

        @Override
        public void run() {
            try {
                while (isConnect) {
                    String message = dataInputStream.readUTF();
                    System.out.println(message);
                    String txt = tarea.getText();
                    if (txt != null && !"".equals(txt.trim())) {
                        message = tarea.getText() + "\n" + message;
                    }
                    tarea.setText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}  