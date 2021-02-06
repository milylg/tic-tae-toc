package com.game.net.protocol;

public class AgreeJoinGameBehavior extends Protocol {

    private boolean agree;
    private boolean first;


    /**
     * if (message.equals("AGREE,FIRST:true")) {
     * logger.info("partner already agree to play game");
     * setFirst(false);
     * }
     * if (message.equals("AGREE,FIRST:false")) {
     * logger.info("");
     * setFirst(true);
     * }
     * if (message.equals("DISAGREE")) {
     * setFirst(false);
     * logger.info("partner disagree it, Sorry!");
     * }
     */
    @Override
    public void execute() {
        if ((agree && first) || !agree) {
            network.setFirst(false);
            logger.info("partner already agree to play game ? " + (agree ? " yes" : " no"));
        } else if (agree && !first) {
            network.setFirst(true);
        }
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "AgreeJoinGameBehavior{" +
                "agree=" + agree +
                ", first=" + first +
                ", socket=" + socket +
                '}';
    }
}
