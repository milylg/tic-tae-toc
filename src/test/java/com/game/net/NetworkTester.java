package com.game.net;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @user : VIRIYA
 * @create : 2020/11/29 11:32
 */
public class NetworkTester {

    public static void newSocketServer(int port) throws IOException {
        // Socket 服务端
        // 服务器在8888端口上监听
        ServerSocket server = new ServerSocket(8888);
        System.out.println("服务器运行中，等待客户端连接。");
        // 得到连接，程序进入到阻塞状态
        Socket client = server.accept();
        // 打印流输出最方便
        PrintStream out = new PrintStream(client.getOutputStream());
        /**
         * 向客户端输出信息
         * 如果使用println 则在tcp连接中，第一次会传输 hello word！ 第二次发送 \r\d   0x0A 0x0D
         */
        out.print("hello world!");
        out.flush();
        client.close();
        server.close();
        System.out.println("服务器已向客户端发送消息，退出。");
    }


    public static void connect(String ip, int port) {
        Socket client = new Socket();
        InetSocketAddress address = new InetSocketAddress(ip, port);
        try {
            client.connect(address);
            InputStream inputStream = client.getInputStream();
            byte[] bytes = new byte[128];
            inputStream.read(bytes);
            System.out.println(new String(bytes));
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testServer() throws IOException {
        NetworkTester.newSocketServer(8888);
    }

    @Test
    public void testConnect() {
        NetworkTester.connect("localhost", 8888);
    }

}
