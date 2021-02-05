package com.game.domain;


import com.game.domain.value.Plot;

/**
 * @author VIRIYA
 * @create 2020/10/24 2:15
 */
public class RemotePlayer extends AbstractPlayer {

    /**
     * message of player send to other player
     * let remote player do some choose (a point)
     * then receive for point of remote player.
     *
     * @return a point that current player choose
     */
    @Override
    public Plot play() {
        throw new UnsupportedOperationException("play");
    }

    @Override
    public Plot startPlay() {
        throw new UnsupportedOperationException("startPlay");
    }

}
