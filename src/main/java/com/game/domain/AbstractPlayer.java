package com.game.domain;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


/**
 * @author VIRIYA
 * @create 2020/10/24 1:51
 */
public abstract class AbstractPlayer {

    protected static final int MAX_ROW = 3;
    protected static final int MAX_COL = 3;
    public static final Shape DEFAULT_SHAPE = new Circle(50);

    protected int[][] cache;
    protected boolean isUsed;
    protected boolean startPlay;
    protected ChessEnumType chessType;
    protected boolean isWillPlay;


    public AbstractPlayer() {
        cache = new int[3][3];
        isUsed = false;
        startPlay = false;
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

    public AbstractPlayer setStartPlay(boolean isFirst) {
        this.startPlay = isFirst;
        return this;
    }

    public boolean isFirstPlay() {
        return startPlay;
    }


    public AbstractPlayer setChessType(ChessEnumType type) {
        this.chessType = type;
        return this;
    }

    public ChessEnumType getChessType() {
        return chessType;
    }

    public void flushChessBoard(Point point, ChessEnumType chessType) {
        cache[point.getY()][point.getX()] = chessType.value();
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
    public abstract Point play();

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
    public abstract Point startPlay();

    public void setWillPlay(boolean startPlaying) {
        this.isWillPlay = startPlaying;
    }

    /**
     * check empty point in current chess board
     * @param point
     * @return true if not empty point
     */
    public boolean checkNotEmptySlot(Point point) {
        return cache[point.getY()][point.getX()] != 0;
    }

}
