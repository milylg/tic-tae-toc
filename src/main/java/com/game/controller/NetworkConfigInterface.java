package com.game.controller;

import com.game.net.RemoteConnectInfo;
import com.game.service.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * @author VIRIYA
 * @create 2020/10/24 9:16
 */
public class NetworkConfigInterface {

    @FXML
    public TextField textIp;

    @FXML
    public TextField textPort;

    @FXML
    public CheckBox cbxFirst;


    private NetworkService networkService;

    public NetworkConfigInterface() {
         networkService = new NetworkService();
    }

    @FXML
    public void clickOk(ActionEvent event) {
        RemoteConnectInfo remoteConnectMessage = new RemoteConnectInfo();
        remoteConnectMessage.setIp(textIp.getText())
                .setPort(textPort.getText())
                .setFirst(cbxFirst.isWrapText());
        if (remoteConnectMessage.isLegitimate()) {
           boolean isConnected = networkService.connect();
           if (!isConnected) {
               Dialog<ButtonType> error = new Alert(Alert.AlertType.ERROR);
               error.setContentText("Failed to connect to the other player.");
               error.show();
           }
           // here need to close pane
           return;
        }
        Dialog<ButtonType> error = new Alert(Alert.AlertType.ERROR);
        error.setContentText("Connect information is not legitimate.");
        error.show();
    }

}
