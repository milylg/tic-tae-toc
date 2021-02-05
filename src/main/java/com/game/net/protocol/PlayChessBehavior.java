package com.game.net.protocol;

import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Plot;

public class PlayChessBehavior extends Protocol {

    private Plot location;
    private ChessEnumType type;

    @Override
    public void execute() {
        networkService.getFlushBoardCallback().callbackFlushView(location, type);
        networkService.setFirst(true);
    }

    public void setLocation(Plot location) {
        this.location = location;
    }

    public void setType(ChessEnumType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PlayChessBehavior{" +
                "location=" + location +
                ", type=" + type +
                '}';
    }
}
