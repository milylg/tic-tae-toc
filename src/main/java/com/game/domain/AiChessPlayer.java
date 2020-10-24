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
        return new Point(random.nextInt(3), random.nextInt(3));
    }

    /**
     *
     * @param pointOfPlayer
     * @return null if AI is loser
     */
    @Override
    public Point playByAnalyze(Point pointOfPlayer) {

        return randomLocation();
    }

    @Override
    public Result isWin() {

        return null;
    }
}
