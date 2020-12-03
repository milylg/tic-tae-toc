package com.game.service;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NetworkServiceTest {

    /**
     * first start server
     * then start run test
     *
     */
    public static void main(String[] args) {
        NetworkService.build().startListen(8080);
    }

    @Test
    public void testConnect() {
        boolean result = NetworkService.build().connect("localhost" , 8080);
        assertTrue(result);
    }
}