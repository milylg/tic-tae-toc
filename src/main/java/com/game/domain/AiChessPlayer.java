package com.game.domain;

import java.util.Random;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:02
 */
public class AiChessPlayer extends AbstractPlayer {

    private Random random = new Random();

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
     *
     * @return null if AI is loser
     */
    @Override
    public Point play() {

        return null;
    }


    @Override
    public Result gameResult() {
        if (isWin(chessType)) {
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
}
