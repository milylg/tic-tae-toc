package com.game.net;

import java.io.Serializable;

/**
 * @author VIRIYA
 * @create 2020/10/24 10:46
 */
public class RemoteConnectInfo implements Serializable {

    private String ip;
    private String port;
    private boolean isFirst;


    public RemoteConnectInfo setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public RemoteConnectInfo setPort(String port) {
        this.port = port;
        return this;
    }

    public RemoteConnectInfo setFirst(boolean isFirst) {
        this.isFirst = isFirst;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLegitimate() {

        return true;
    }
}
