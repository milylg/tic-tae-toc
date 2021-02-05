package com.game.domain;

import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Fork;
import com.game.domain.value.Plot;
import com.game.domain.value.Result;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import jdk.nashorn.internal.ir.annotations.Ignore;


/**
 * @author VIRIYA
 * @create 2020/10/24 1:51
 */
public abstract class AbstractPlayer {

    protected static final int MAX_ROW = 3;
    protected static final int MAX_COL = 3;
    private static final int CHESS_CIRCLE_RADIUS = 50;

    protected int[][] cache;
    protected ChessEnumType chessType;
    protected boolean isWillPlay;


    public AbstractPlayer() {
        cache = new int[3][3];
    }

    public Shape createShape() {
        if (chessType == ChessEnumType.CIRCLE) {
            Shape circle = new Circle(CHESS_CIRCLE_RADIUS);
            circle.setVisible(true);
            return circle;
        }
        return new Fork();
    }


    public AbstractPlayer clearChessCache() {
        for (int i = 0, len = cache.length; i < len; i++) {
            for (int j = 0; j < len; j++) {
                cache[i][j] = 0;
            }
        }
        return this;
    }


    public AbstractPlayer setChessType(ChessEnumType type) {
        this.chessType = type;
        return this;
    }

    public AbstractPlayer defaultChessType() {
        // default type
        chessType = ChessEnumType.CIRCLE;
        return this;
    }


    public ChessEnumType getChessType() {
        return chessType;
    }

    public void flushChessBoard(Plot plot, ChessEnumType chessType) {
        cache[plot.getY()][plot.getX()] = chessType.value();
        // logChessBoardInfo();
    }

    @Ignore
    private void logChessBoardInfo() {
        for (int row = 0; row < 3; row ++) {
            for (int col = 0; col < 3; col ++) {
                System.out.print("  " + cache[row][col]);
            }
            System.out.println();
        }
    }

    protected boolean isWin() {
        return checkPlayerState(false);
    }

    protected boolean isDraw() {
        int len = cache.length;
        for (int[] ints : cache) {
            for (int y = 0; y < len; y++) {
                if (ints[y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true if current player lose
     */
    protected boolean isLose() {
        return checkPlayerState(true);
    }

    /**
     * @param ifLose check weather lose if true for player
     * @return
     *
     * IfLose       Condition    Return Result
     * true           lose         true
     * true           not lose     false
     * false          win          true
     * false          not win      false
     */
    private boolean checkPlayerState(boolean ifLose) {
        int len = cache.length;
        int val = chessType.value() * (ifLose ? -3 : 3);

        for (int col = 0, sumV, sumH; col < len; col++) {
            sumV = 0;
            sumH = 0;
            for (int row = 0; row < len; row++) {
                sumV += cache[row][col];
                sumH += cache[col][row];
            }
            if (sumV == val || sumH == val) {
                return true;
            }
        }

        int sumR = 0, sumL = 0;

        for (int index = 0; index < len; index++) {
            sumR += cache[index][len - index - 1];
            sumL += cache[index][index];
        }
        return sumR == val || sumL == val;
    }


    /**
     * choose a location at chess
     *
     * @return point
     */
    public abstract Plot play();

    /**
     * judge self if wined
     * has three state
     * <p>- win</p>
     * <p>- draw</p>
     * <p>- lost</p>
     *
     * @return state of player in game
     */
    public Result gameResult() {
        if (isWin()) {
            return Result.WIN;
        }
        if (isDraw()) {
            return Result.DRAW;
        }
        if (isLose()) {
            return Result.LOSE;
        }
        return Result.CONTINUE;
    }

    /**
     * clear data from cache when start before.
     *
     * @return point
     */
    public abstract Plot startPlay();

    public void setWillPlay(boolean startPlaying) {
        this.isWillPlay = startPlaying;
    }

    /**
     * check empty point in current chess board
     * @param plot
     * @return true if not empty point
     */
    public boolean checkNotEmptySlot(Plot plot) {
        return cache[plot.getY()][plot.getX()] != 0;
    }

}
