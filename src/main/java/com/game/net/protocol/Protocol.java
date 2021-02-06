package com.game.net.protocol;

import com.game.net.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.Socket;

public abstract class Protocol implements Serializable {

    protected static final Logger logger = LoggerFactory.getLogger("com.game.service");

    protected Network network;
    protected Socket socket;

    protected Protocol() {

    }

    public abstract void execute();

    public void needExecutor(Network network) {
        this.network = network;
    }

    public void socket(Socket socket) {
        this.socket = socket;
    }
}
