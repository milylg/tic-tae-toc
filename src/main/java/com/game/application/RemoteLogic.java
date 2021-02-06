package com.game.application;

import com.game.net.ConnectParams;
import com.game.net.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * remote client for test
 *
 * @user : VIRIYA
 * @create : 2020/12/5 15:49
 */
public class RemoteLogic extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(new Scene(root, 300, 330));
        primaryStage.show();

        // for easy test to config network params
        Network service = Network.getInstance();
        service.startListen(9999);
        ConnectParams message = new ConnectParams();
        message.setIp("127.0.0.1");
        message.setPort("8888");
        service.config(message);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
