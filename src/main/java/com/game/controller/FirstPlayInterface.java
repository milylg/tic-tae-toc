package com.game.controller;

import com.game.net.protocol.AgreeJoinGameBehavior;
import com.game.service.NetworkService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FirstPlayInterface {


    private static Logger logger = LoggerFactory.getLogger("com.game.controller");

    @FXML
    private CheckBox cbxFirst;

    @FXML
    public void play(Event event) {
        NetworkService network = NetworkService.getInstance();

        if (GameInterface.connectPartner(network)) {
            return;
        }
        AgreeJoinGameBehavior agreeJoinGameBehavior = new AgreeJoinGameBehavior();
        agreeJoinGameBehavior.setAgree(true);
        agreeJoinGameBehavior.setFirst(cbxFirst.isSelected());

        network.send(agreeJoinGameBehavior);
        network.setFirst(cbxFirst.isSelected());
        logger.info("agree play game, I am first play ?" + cbxFirst.isSelected());
    }

    @FXML
    public void display(Event event) {
        NetworkService network = NetworkService.getInstance();
        if (GameInterface.connectPartner(network)) {
            return;
        }
        AgreeJoinGameBehavior disagreeJoinGameBehavior = new AgreeJoinGameBehavior();
        disagreeJoinGameBehavior.setAgree(false);
        network.send(disagreeJoinGameBehavior);
        network.setFirst(false);
    }

}
