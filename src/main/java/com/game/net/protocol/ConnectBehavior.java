package com.game.net.protocol;

import java.io.IOException;

public class ConnectBehavior extends  Protocol{
    @Override
    public void execute() {
        try {
            network.specifySend(socket, new ConnectResponseBehavior());
        } catch (IOException e) {
            logger.info("response exception:" + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ConnectBehavior{" +
                "socket=" + socket +
                '}';
    }
}
