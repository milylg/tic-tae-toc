package com.game.domain;

import java.util.Random;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:02
 */
public class AiChessPlayer extends AbstractPlayer {

    private Random random = new Random();
    private ScoreRule scoreRule = new ScoreRule();
    private static final int MAX_DEPTH = 2;

    @Override
    public Point startPlay() {
        clearCache();
        return randomLocation();
    }

    private Point randomLocation() {
        return new Point()
                .setX(random.nextInt(3))
                .setY(random.nextInt(3));
    }

    /**
     * can't use test case to test it,
     *
     * because is possibility that test case is error
     *
     * TODO: Use MaxMinValue algorithm to resolve it
     *
     * @return null if AI is loser
     */
    @Override
    public Point play() {
        Point point = maxMinValue(MAX_DEPTH);
        flushChessBoard(point, chessType);
        return point;
    }

    /**
     * Max value to AI And Min value to human player
     *
     * track back direct depth then return result of possibility
     * - max compare
     * - min compare
     * - score rule
     *
     * @param depth track back depth
     * @return random point in points that let ai player wined game
     */
    private Point maxMinValue(int depth) {
        Point[] bestMove = new Point[9];
        int index = 0;
        int betaValue = ScoreRule.LOSE, alphaValue = ScoreRule.WIN;
        int chessTypeValue = chessType.value();
        int emptyValue = 0;

        for (int row = 0, minValue; row < MAX_ROW; row ++) {
            for (int col = 0; col < MAX_COL; col ++) {
                if (cache[row][col] == 0) {
                    cache[row][col] = chessTypeValue;
                    minValue = min(depth, alphaValue, betaValue);
                    if (betaValue < minValue) {
                        betaValue = minValue;
                        index = 0;
                        bestMove[index] = new Point().setX(col).setY(row);
                    } else if (betaValue == minValue) {
                        index ++;
                        bestMove[index] = new Point().setX(col).setY(row);
                    }
                    cache[row][col] = emptyValue;
                }
            }
        }
        if (index > 1){
            index = (new Random(System.currentTimeMillis()).nextInt() >>> 1) % index;
        }
        return bestMove[index];
    }

    private int min(int depth, int alpha, int bate) {
        int evalValue = scoreRule.getScore();
        if (alpha > bate || depth <= 0) {
            return evalValue;
        }
        boolean gameOver = gameResult() != Result.CONTINUE;
        if (gameOver) {
            return evalValue;
        }

        int bestValue = ScoreRule.WIN ;
        int chessTypeValue = chessType.value() * -1;
        for (int row = 0; row < MAX_ROW; row ++) {
            for (int col = 0; col < MAX_COL; col ++) {
                if (cache[row][col] == 0) {
                    cache[row][col] = chessTypeValue;
                    bestValue = Math.min(bestValue,
                            max(depth - 1, alpha, Math.min(bestValue, bate)));
                    cache[row][col] = 0;
                }
            }
        }
        return bestValue;
    }

    private int max(int depth, int alpha, int beta) {

        int evalValue =  scoreRule.getScore();
        if (beta <= alpha || depth == 0){
            return evalValue;
        }
        boolean isGameOver = gameResult() != Result.CONTINUE;
        if (isGameOver) {
            return evalValue;
        }

        int bestValue = ScoreRule.WIN ;
        int chessTypeValue = chessType.value();

        for (int row = 0; row < MAX_ROW; row ++) {
            for (int col = 0; col < MAX_COL; col ++) {
                if (cache[row][col] == 0) {
                    cache[row][col] = chessTypeValue;
                    bestValue = Math.max(bestValue,
                            min(depth - 1, Math.max(bestValue, alpha), beta));
                    cache[row][col] = 0;
                }
            }
        }
        return bestValue;
    }

    private static final Point[][] WIN_STATUS = Point.buildGroup(new int[][]{
            {0, 0, 0, 1, 0, 2},
            {1, 0, 1, 1, 1, 2},
            {2, 0, 2, 1, 2, 2},
            {0, 0, 1, 0, 2, 0},
            {0, 1, 1, 1, 2, 1},
            {0, 2, 1, 2, 2, 2},
            {0, 0, 1, 1, 2, 2},
            {0, 2, 1, 1, 2, 0}
    });

    private class ScoreRule {
        static final int CONTINUE = 1;
        static final int WIN = 10;
        static final int LOSE = -10;
        static final int DRAW = 0;
        static final int DOUBLE_LINK = 5;

        public int getScore() {
            Result result = gameResult();
            if (result == Result.CONTINUE) {
                return getDoubleLinkValue();
            } else if (result == Result.WIN) {
                return WIN;
            } else if (result == Result.LOSE) {
                return LOSE;
            }
            return DRAW;
        }

        private int getDoubleLinkValue() {
            int[] finds = new int[2];
            for (Point[] p : WIN_STATUS) {
                int chess = 0;
                boolean hasEmpty = false;
                int count = 0;
                for (int i = 0; i < p.length; i++) {
                    if (cache[p[i].getY()][p[i].getX()] == 0) {
                        hasEmpty = true;
                        continue;
                    }
                    if (chess == 0) {
                        chess = cache[p[i].getY()][p[i].getX()];
                    }
                    if (cache[p[i].getY()][p[i].getX()] == chess) {
                        count++;
                    }
                }
                if (hasEmpty && count > 1) {
                    if (chess == -1) {
                        finds[0] ++;
                    } else {
                        finds[1] ++;
                    }
                }
            }
            // check if two in one line
            if (finds[1] > 0) {
                return -DOUBLE_LINK;
            }
            if (finds[0] > 0) {
                return DOUBLE_LINK;
            }
            return CONTINUE;
        }
    }


}
