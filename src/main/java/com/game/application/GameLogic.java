package com.game.application;

import com.game.net.ConnectParams;
import com.game.service.NetworkService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Administrator
 */
public class GameLogic extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(new Scene(root, 300, 330));
        primaryStage.show();

        // for easy test  to config network params
        NetworkService service = NetworkService.getInstance();
        service.startListen(8888);
        ConnectParams message = new ConnectParams();
        message.setIp("127.0.0.1");
        message.setPort("9999");
        service.config(message);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
