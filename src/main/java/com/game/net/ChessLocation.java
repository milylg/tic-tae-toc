package com.game.net;

import com.game.domain.ChessEnumType;
import com.game.domain.Point;

import java.io.Serializable;

/**
 * @user : VIRIYA
 * @create : 2020/11/28 21:17
 */
public class ChessLocation implements Serializable {
    private Point point;
    private ChessEnumType chessEnumType;

    private ChessLocation(Point point, ChessEnumType chessEnumType) {
        this.chessEnumType = chessEnumType;
        this.point = point;
    }

    public static ChessLocation build(Point point, ChessEnumType chessEnumType) {
        return new ChessLocation(point, chessEnumType);
    }

    public Point getPoint() {
        return point;
    }

    public ChessEnumType getChessEnumType() {
        return chessEnumType;
    }

    @Override
    public String toString() {
        return "ChessLocation{" +
                "point=" + point +
                ", chessEnumType=" + chessEnumType +
                '}';
    }
}
