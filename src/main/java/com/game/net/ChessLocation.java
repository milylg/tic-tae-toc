package com.game.net;

import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Plot;

import java.io.Serializable;

/**
 * @user : VIRIYA
 * @create : 2020/11/28 21:17
 */
public class ChessLocation implements Serializable {
    private Plot plot;
    private ChessEnumType chessEnumType;

    private ChessLocation(Plot plot, ChessEnumType chessEnumType) {
        this.chessEnumType = chessEnumType;
        this.plot = plot;
    }

    public static ChessLocation build(Plot plot, ChessEnumType chessEnumType) {
        return new ChessLocation(plot, chessEnumType);
    }

    public Plot getPoint() {
        return plot;
    }

    public ChessEnumType getChessEnumType() {
        return chessEnumType;
    }

    @Override
    public String toString() {
        return "ChessLocation{" +
                "point=" + plot +
                ", chessEnumType=" + chessEnumType +
                '}';
    }
}
