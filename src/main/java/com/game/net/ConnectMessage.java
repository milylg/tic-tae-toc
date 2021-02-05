package com.game.net;

/**
 * @user : VIRIYA
 * @create : 2020/11/29 19:03
 */
public class ConnectMessage {

    private String ip;
    private String port;

    public ConnectMessage setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public ConnectMessage setPort(String port) {
        this.port = port;
        return this;
    }

    public String ip() {
        return ip;
    }

    public int port() {
        return Integer.valueOf(port);
    }

    public boolean isLegitimate() {

        return true;
    }
}
