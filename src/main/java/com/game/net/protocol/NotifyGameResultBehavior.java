package com.game.net.protocol;

import com.game.domain.value.Result;


public class NotifyGameResultBehavior extends Protocol {

    private Result gameResult;


    @Override
    public void execute() {
        networkService.getHitMessageCallback().showHintMessageDialog(gameResult);
    }

    public void setGameResult(Result gameResult) {
        this.gameResult = gameResult;
    }

    @Override
    public String toString() {
        return "NotifyGameResultBehavior{" +
                "gameResult=" + gameResult +
                '}';
    }
}
