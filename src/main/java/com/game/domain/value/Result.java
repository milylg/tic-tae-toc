package com.game.domain.value;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:56
 */
public enum Result {
    /**
     * game state
     */
    WIN("adversary wined!"),
    DRAW("both draw"),
    LOSE("adversary lose"),
    CONTINUE("");

    private String information;

    Result(String val) {
        this.information = val;
    }

    public String getInformation() {
        return information;
    }
}
