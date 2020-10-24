package com.game.domain;


/**
 * @author VIRIYA
 * @create 2020/10/23 23:12
 */
public enum ChessEnumType {
    /**
     * chess type
     */
    CIRCLE(1),

    FORK(-1);

    ChessEnumType(int val) {
        this.value = val;
    }

    private int value;

    public int value() {
        return value;
    }

}
