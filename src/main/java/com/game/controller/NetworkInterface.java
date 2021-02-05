package com.game.controller;

import com.game.net.ConnectParams;
import com.game.service.NetworkService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author VIRIYA
 * @create 2020/10/24 9:16
 *
 * user need input information
 * - TCP IP and PORT
 * - UDP IP and PORT
 * - first play flag
 */
public class NetworkInterface {

    private static final Logger logger = LoggerFactory.getLogger("com.game.controller");

    @FXML
    public TextField serverIp;

    @FXML
    public TextField textTcpIp;

    @FXML
    public TextField textTcpPort;

    @FXML
    public Label labelListen;

    @FXML
    public Button listenBtn;


    /**
     * try connect partner
     *
     */
    @FXML
    public void clickOk(ActionEvent event) {
        ConnectParams message = new ConnectParams();
        message.setIp(textTcpIp.getText())
                .setPort(textTcpPort.getText());
        if (message.isLegitimate()) {
            NetworkService.getInstance().config(message);
            return;
        }
        showAlertMessage("Connect information is not legitimate.", Alert.AlertType.ERROR);
    }

    /**
     * start server by listen port
     *
     * @param event
     */
    @FXML
    public void listen(ActionEvent event) {
        Pattern pattern = Pattern.compile("^[0-9]{4,5}$");
        Matcher matcher = pattern.matcher(serverIp.getText());
        if (matcher.matches()) {
            NetworkService.getInstance().startListen(Integer.valueOf(serverIp.getText()));
            labelListen.setText("starting");
            listenBtn.setVisible(false);
            logger.info("server listen port : {}", serverIp.getText());
            return;
        }
        showAlertMessage("server listen port is not valid", Alert.AlertType.ERROR);
    }

    /**
     * show tip message on screen
     *
     * @param message
     * @param alertType
     */
    private void showAlertMessage(String message, Alert.AlertType alertType) {
        if (message != null) {
            Dialog<ButtonType> error = new Alert(alertType);
            error.setContentText(message);
            error.show();
        }
    }

}
