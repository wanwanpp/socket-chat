package com.wp.webserver.httpServerPro;

import java.io.*;

/**
 * Created by 王萍 on 2017/3/3 0003.
 */
public class Response {

    public static String WEB_ROOT = "F:\\IDEA\\socket_chat\\src\\com\\wp\\webserver\\httpServerPro";
    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream out;
    private PrintWriter writer;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(WEB_ROOT, request.getUri());
//        File file = new File("F:\\IDEA\\socket_chat\\src\\com\\wp\\webserver\\httpServerPro\\test.txt");

        //如果下面的false改为true，当writer.print(),writer.println()时，会自动flush()数据。
        writer = new PrintWriter(out, false);
        try {
            fis = new FileInputStream(file);
            int ch = fis.read(buffer, 0, BUFFER_SIZE);
//            out.write(("HTTP/1.1 200 OK\n" +
//                    "Content-Type:text/html;charset=UTF-8\n\n").getBytes());
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type:text/html;charset=UTF-8\n");
            while (ch != -1) {
//                out.write(buffer, 0, BUFFER_SIZE);

                //直接打印字节，应该转换为字符数组char[]进行输出
//                writer.print(buffer);
                writer.print(byteToChar(buffer));

                ch = fis.read(buffer, 0, BUFFER_SIZE);
            }

            //注意这里的flush方法，是将writer缓冲区中的数据冲出去。
            writer.flush();
        } catch (FileNotFoundException e) {
            String html = "<h1>file not found</h1>";
            String errorMsg = "HTTP/1.1 404 file not found\r\n" +
                    "Content-Type:text/html\r\n" +
                    "Content-Length:" + html.getBytes().length + "\r\n" +
                    "\r\n" + html;
            out.write(errorMsg.getBytes());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public char[] byteToChar(byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) bytes[i];
        }
        return chars;
    }
}
