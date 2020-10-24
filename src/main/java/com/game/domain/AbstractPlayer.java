package com.game.domain;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.awt.*;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:51
 */
public abstract class AbstractPlayer {

    protected int[][] cache;
    protected boolean isUsed;
    private boolean isFirst;
    protected ChessEnumType chessType;

    public AbstractPlayer() {
        cache = new int[3][3];
        isUsed = false;
        isFirst = false;
        chessType = ChessEnumType.CIRCLE;
    }

    public Shape createShape() {
        if (chessType == ChessEnumType.CIRCLE) {
            Shape circle = new Circle(50);
            circle.setVisible(true);
            return circle;
        }
        return new Fork();
    }


    public AbstractPlayer clearCache() {
        for (int i = 0, len = cache.length; i < len; i++) {
            for (int j = 0; j < len; j++) {
                cache[i][j] = 0;
            }
        }
        return this;
    }

    public AbstractPlayer setChoose(boolean used) {
        this.isUsed = used;
        return this;
    }

    public AbstractPlayer setFirst(boolean isFirst) {
        this.isFirst = isFirst;
        return this;
    }

    public AbstractPlayer setChessType(ChessEnumType type) {
        this.chessType = type;
        return this;
    }

    public void flushChessBoard(Point point, ChessEnumType chessType) {
        cache[point.getX()][point.getY()] = chessType.value();
    }

    /**
     * TODO:
     *     Warning:(69, 23) Refactor this method to reduce
     *     its Cognitive Complexity from 17 to the 15 allowed.
     *
     * @param chessType
     * @return
     */
    protected boolean isWin(ChessEnumType chessType) {
        boolean isWin = true;
        for (int x = 0; x <= 2; x ++) {
            isWin &= cache[x][x] == chessType.value();
        }
        if (isWin) {
            return true;
        }else {
            isWin = true;
        }

        for (int x = 0; x <= 2; x ++) {
            isWin &= cache[x][2-x] == chessType.value();
        }
        if (isWin) {
            return true;
        }else {
            isWin = true;
        }

        for (int x = 0; x <= 2; x ++) {
            for (int y = 0; y <= 2; y ++) {
                isWin &= cache[x][y] == chessType.value();
            }
            if (isWin) {
                return true;
            }else {
                isWin = true;
            }
        }

        for (int x = 0; x <= 2; x ++) {
            for (int y = 0; y <= 2; y ++) {
                isWin &= cache[y][x] == chessType.value();
            }
            if (isWin) {
                return true;
            }
        }
        return false;
    }

    protected boolean isDraw() {

        return false;
    }

    protected boolean isLose() {

        return false;
    }


    /**
     * choose a location at chess
     *
     * @return
     */
    public abstract Point play();

    /**
     * judge self is win
     * has three state
     * - win
     * - tie
     * - lost
     *
     * @return
     */
    public abstract Result gameResult();

    /**
     * clear data from cache when start before.
     *
     * @return
     */
    public abstract Point startPlay();

}
