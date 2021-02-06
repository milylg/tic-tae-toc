package com.game.service;

import com.game.net.Network;

import static org.junit.Assert.assertTrue;

public class NetworkServiceTest {

    /**
     * first start server
     * then start run test
     *
     */
    public static void main(String[] args) {
        Network.getInstance().startListen(8080);
    }

//    @Test
//    public void testConnect() {
//        boolean result = Network.getInstance().connect("localhost" , 8080);
//        assertTrue(result);
//    }
//
//    @Test
//    public void testSendMsg() {
//        boolean isConnect = Network.getInstance().connect("localhost" , 8080);
//        if (isConnect) {
//            Network.getInstance().send("ssssss");
//        }
//    }
}