package com.game.domain;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;


/**
 * @author VIRIYA
 * @create 2020/10/24 1:51
 */
public abstract class AbstractPlayer {

    protected int[][] cache;
    protected boolean isUsed;
    private boolean isFirst;
    protected ChessEnumType chessType;
    protected boolean isWillPlay;

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

    protected boolean isWin() {
        return checkPlayerState(false);
    }

    protected boolean isDraw() {
        int len = cache.length;
        for (int x = 0; x < len; x ++) {
            for (int y = 0; y < len; y ++) {
                if (cache[x][y] == 0) {
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
     * TODO: delete it
     *
     * @return
     */
    protected List<Point> searchFreePoint() {
        List<Point> emptyPoints = new ArrayList<>(9);
        for (int col = 0, len = cache.length; col < len; col++) {
            for (int row = 0; row < len; row++) {
                if (cache[col][row] == 0) {
                    Point point = Point.builder()
                            .setX(col)
                            .setY(row);
                    emptyPoints.add(point);
                }
            }
        }
        return emptyPoints;
    }


    /**
     * choose a location at chess
     *
     * @return
     */
    public abstract Point play();

    /**
     * judge self weather wined
     * has three state
     * - win
     * - draw
     * - lost
     *
     * @return state of player in game
     */
    public abstract Result gameResult();

    /**
     * clear data from cache when start before.
     *
     * @return
     */
    public abstract Point startPlay();

    public void setWillPlay(boolean startPlaying) {
        this.isWillPlay = startPlaying;
    }

}
