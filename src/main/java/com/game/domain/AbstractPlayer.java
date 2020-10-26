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

    /**
     * TODO:
     *     Warning:(69, 23) Refactor this method to reduce
     *     its Cognitive Complexity from 17 to the 15 allowed.
     *
     * @param chessType
     * @return
     */
    protected boolean isWin(ChessEnumType chessType) {
        int len = cache.length;
        int val = chessType.value() * 3;
        // v
        for (int col = 0; col < len; col++) {
            for (int row = 0,sum = 0; row < len; row++) {
                sum += cache[row][col];
                if (sum == val) {
                    return true;
                }
            }
        }
        // h
        for (int row = 0; row < len; row++) {
            for (int col = 0, sum = 0; col < len; col++) {
                sum += cache[row][col];
                if (sum == val) {
                    return true;
                }
            }
        }

        for (int row = 0,sum = 0; row < len; row++) {
            sum += cache[row][len - row - 1];
            if (sum == val) {
                return true;
            }
        }

        for (int col = 0, sum = 0; col < len; col++) {
            sum += cache[col][col];
            if (sum == val) {
                return true;
            }
        }
        return false;
    }

    protected boolean isDraw() {
        List<Point> emptyPoints = searchPossibilityPoint();
        if (!isWillPlay) {
            return checkIsDrawForCurrentPlayerType(emptyPoints, ChessEnumType.FORK);
        }
        return checkIsDrawForCurrentPlayerType(emptyPoints, ChessEnumType.CIRCLE);
    }

    protected boolean isLose() {
        int len = cache.length;
        // v
        for (int col = 0; col < len; col++) {
            int sum = 0;
            for (int row = 0; row < len; row++) {
                sum += cache[row][col];
            }
            if (sum == 3) {
                return true;
            }
        }
        // h
        for (int row = 0; row < len; row++) {
            int sum = 0;
            for (int col = 0; col < len; col++) {
                sum += cache[row][col];
            }
            if (sum == 3) {
                return true;
            }
        }

        for (int row = 0, sum = 0; row < len; row++) {
            sum += cache[row][len - row - 1];
            if (sum == 3) {
                return true;
            }
        }

        for (int col = 0, sum = 0; col < len; col++) {
            sum += cache[len - col - 1][col];
            if (sum == 3) {
                return true;
            }
        }
        return false;
    }

    protected List<Point> searchPossibilityPoint() {
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

    protected boolean checkIsDrawForCurrentPlayerType(List<Point> emptyPoints, ChessEnumType chessType) {
        int x, y;
        for (Point point : emptyPoints) {
            x = point.getX();
            y = point.getY();
            cache[x][y] = chessType.value();
            for (int col = 0, len = cache.length; col < len; col++) {
                for (int row = 0; row < len; row++) {

                }
            }
            cache[x][y] = 0;
        }
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

    public void setWillPlay(boolean startPlaying) {
        this.isWillPlay = startPlaying;
    }

}
