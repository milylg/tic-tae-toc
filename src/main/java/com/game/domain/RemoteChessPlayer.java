package com.game.domain;

/**
 * @author VIRIYA
 * @create 2020/10/24 2:15
 */
public class RemoteChessPlayer extends AbstractPlayer {


    /**
     * message of player send to other player
     * let remote player do some choose (a point)
     * then receive for point of remote player.
     *
     * @param pointOfPlayer  ai player or remote player
     * @return a point that current player choose
     */
    @Override
    public Point playByAnalyze(Point pointOfPlayer) {
        if (pointOfPlayer == null) {
            return null;
        }
        // get point by network

        return new Point(1,2);
    }

    @Override
    protected Result isWin() {
        return null;
    }

    @Override
    public Point startPlay() {
        return null;
    }
}
