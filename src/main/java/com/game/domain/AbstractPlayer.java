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

    /**
     * choose a location at chess
     *
     * @param pointOfPlayer
     * @return
     */
    public abstract Point playByAnalyze(Point pointOfPlayer);

    /**
     * judge self is win
     * has three state
     * - win
     * - tie
     * - lost
     *
     * @return
     */
    protected abstract Result isWin();

    /**
     * clear data from cache when start before.
     *
     * @return
     */
    public abstract Point startPlay();

}
