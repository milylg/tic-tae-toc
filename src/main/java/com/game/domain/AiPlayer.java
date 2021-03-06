package com.game.domain;

import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Plot;
import com.game.domain.value.Result;

import java.util.Random;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:02
 */
public class AiPlayer extends AbstractPlayer {

    private Random random = new Random();
    private ScoreRule scoreRule = new ScoreRule();
    private static final int MAX_DEPTH = 2;

    @Override
    public Plot startPlay() {
        clearChessCache();
        return randomLocation();
    }

    private Plot randomLocation() {
        return Plot.builder()
                .setX(random.nextInt(MAX_ROW))
                .setY(random.nextInt(MAX_COL));
    }

    /**
     * can't use test case to test it,
     *
     * because is possibility that test case is error
     *
     * @return null if AI is loser
     */
    @Override
    public Plot play() {
        Plot plot = maxMinValue(MAX_DEPTH);
        flushChessBoard(plot, chessType);
        return plot;
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
    private Plot maxMinValue(int depth) {
        Plot[] bestMove = new Plot[9];
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
                        bestMove[index] = new Plot().setX(col).setY(row);
                    } else if (betaValue == minValue) {
                        index ++;
                        bestMove[index] = new Plot().setX(col).setY(row);
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

    private static final Plot[][] WIN_STATUS = Plot.buildGroup(new int[][]{
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
            int ai = 0, self = 0;
            for (Plot[] p : WIN_STATUS) {
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
                    if (chess == ChessEnumType.FORK.value()) {
                        ai ++;
                    } else {
                        self ++;
                    }
                }
            }
            // check if two in one line
            if (self > 0) {
                return -DOUBLE_LINK;
            }
            if (ai > 0) {
                return DOUBLE_LINK;
            }
            return CONTINUE;
        }
    }


}
