package com.game.net.protocol;



public class StartGameRequestBeHavior extends Protocol {

    @Override
    public void execute() {
        network.getFirstPlayCallback().createRemotePlayer();
        network.getFirstPlayCallback().buildSelectFirstPlayDialog();
        logger.info("first play");
    }

    @Override
    public String toString() {
        return "StartGameRequestBeHavior{}";
    }
}
