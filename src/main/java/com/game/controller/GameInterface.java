package com.game.controller;

import com.game.domain.*;
import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Result;
import com.game.net.ChessLocation;
import com.game.service.NetworkService;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

/**
 * @author VIRIYA
 * @create 2020/10/23 20:39
 */
public class GameInterface {

    private static Logger logger = LoggerFactory.getLogger("com.game.controller");

    @FXML
    private Button conf;

    @FXML
    private GridPane box;

    @FXML
    private Button net;

    private static AbstractPlayer chessPlayer;
    private static NetworkService networkService;

    private static boolean isRemotePlayer = false;

    @FunctionalInterface
    public interface FlushBoardCallback {
        /**
         * service invoke it to flush view
         * @param plot
         * @param type
         */
        void call(Plot plot, ChessEnumType type);
    }

    @FunctionalInterface
    public interface HitMessageCallback {
        void call(Result result);
    }

    @FunctionalInterface
    public  interface FirstPlayCallback {
        default void createRemotePlayer() {
            chessPlayer = PlayerFactory.createRemotePlayer()
                    .setChoose(true)
                    .setChessType(ChessEnumType.FORK)
                    .clearCache();
            isRemotePlayer = true;
        }
        void call();
    }

    public GameInterface() {
        networkService = NetworkService.getInstance();
    }

    @FXML
    public void initialize() {

        // call back flush board chess
        networkService.addFlushBoardCallBack(((point, type) -> {

            Platform.runLater(() -> {
                if (type == ChessEnumType.FORK) {
                    flush(new Fork(), point);
                } else {
                    flush(new Circle(50), point);
                }
                if (chessPlayer != null) {
                    chessPlayer.flushChessBoard(point, type);
                }
            });
        }));

        networkService.addHitMessageCallback((result -> {
            Platform.runLater(() -> {
                buildWindow(result);
            });
        }));

        networkService.addFirstCallback(() -> {
            Platform.runLater(() -> {
                // show first play panel
                buildFirstPlayPane();
                clearCheeses();
            });
            // chessPlayer.setWillPlay(networkService.isFirst());
            networkService.connect();
//            // TODO: refactor it
//            networkService.send("FIRST:" + chessPlayer.isFirstPlay());
//            logger.info("I first play? : " + (chessPlayer.isFirstPlay() ? "yes": "no"));
        });
        logger.info("controller initialize");
    }


    @FXML
    public void clickAiPlayer(ActionEvent event) {
        clearCheeses();
        isRemotePlayer = false;
        chessPlayer = PlayerFactory.createAiPlayer()
                .setChessType(ChessEnumType.FORK)
                .clearCache()
                .setChoose(true)
                .setStartPlay(true);
        Plot location = chessPlayer.startPlay();
        chessPlayer.flushChessBoard(location, ChessEnumType.FORK);
        flush(chessPlayer.createShape(), location);
    }



    @FXML
    public void clickNet(ActionEvent event) {
        clearCheeses();
        isRemotePlayer = true;
        if (!networkService.getConnectResult()) {
            boolean isConnected = networkService.connect();
            if (!isConnected) {
                Dialog<ButtonType> error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("Failed to connect to the another player.");
                error.show();
                return;
            }
        }

        // chess type is fork when first play
        chessPlayer = PlayerFactory.createRemotePlayer()
                .setChessType(ChessEnumType.CIRCLE)
                .setChoose(true)
                .clearCache();

        sendRequestForStartGame();
    }


    // TODO: define type of message
    private void sendRequestForStartGame() {
        networkService.send("REQUEST");
    }

    /**
     * col = [0 - 2]
     * row = [0 - 2]
     *
     */
    @FXML
    public void clickGrid(MouseEvent event) {

        if (!isRemotePlayer) {
            actionAiPlayerFor(event);
            logger.info("with ai player");
        } else {
            actionRemotePlayerFor(event);
            logger.info("with remote player");
        }
    }


    private void actionRemotePlayerFor(MouseEvent event) {
        if (!networkService.getConnectResult()) {
            return;
        }

        logger.info("Now, I can play ?" + (networkService.isFirst() ? "yes" : "no"));

        if (!networkService.isFirst()) {
            return;
        }

        // action of current player
        // how choose chess type of player
        Plot plot = new Plot(event.getX(), event.getY());
        ChessLocation location = ChessLocation.build(plot, chessPlayer.getChessType());
        chessPlayer.flushChessBoard(plot, chessPlayer.getChessType());
        // flush chess broad of current player
        flush(chessPlayer.createShape(), plot);

        Result gameResult = chessPlayer.gameResult();
        // notify remote chess board
        networkService.send(location);
        networkService.setFirst(false);
        logger.info("Now, I can play ?" + (networkService.isFirst() ? "yes" : "no"));
        if (gameResult == Result.CONTINUE) {
            return ;
        }
        buildWindow(gameResult);
        // as another
        networkService.send(gameResult);
    }



    private void actionAiPlayerFor(MouseEvent event) {

        if (Optional.ofNullable(chessPlayer).isPresent()) {

            Circle circle = new Circle(50);
            circle.setVisible(true);
            Plot plot = new Plot(event.getX(), event.getY());
            logger.info("people click x = {}, y = {}", plot.getX(), plot.getY());

            if (chessPlayer.checkNotEmptySlot(plot)) {
                return;
            }
            flush(circle, plot);
            chessPlayer.flushChessBoard(plot, ChessEnumType.CIRCLE);
            chessPlayer.setWillPlay(true);
            Result result = chessPlayer.gameResult();
            logger.info("people after game result is {}", result);

            // AI player can play chess
            if (result == Result.CONTINUE) {
                Plot plotOfAi = chessPlayer.play();
                logger.info("AI click x = {}, y = {}", plotOfAi.getX(), plotOfAi.getY());
                chessPlayer.setWillPlay(false);
                flush(chessPlayer.createShape(), plotOfAi);
                chessPlayer.flushChessBoard(plotOfAi, ChessEnumType.FORK);
                Result aiResult = chessPlayer.gameResult();
                buildWindow(aiResult);
                return;
            }
            buildWindow(result);
        }
    }


    @FXML
    private void flush(Shape shape, Plot plot) {
        if (plot != null) {
            box.add(shape, plot.getX(), plot.getY());
        }
    }

    private void clearCheeses() {
        box.getChildren().clear();
        // without effect
        box.setVisible(true);
        box.setHgap(1.0);
        box.setVgap(1.0);
        box.setGridLinesVisible(true);
        box.setVisible(true);
    }

    private void buildConfigNetworkPane() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("network.fxml"));
            stage.setTitle("Network");
            stage.setScene(new Scene(root, 280, 200));
            stage.show();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void buildFirstPlayPane() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("firstplay.fxml"));
            stage.setTitle("who first play?");
            stage.setScene(new Scene(root, 280, 200));
            stage.show();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void buildWindow(Result result) {
        if (result != Result.CONTINUE) {
            Dialog<ButtonType> alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(result.getInformation());
            alert.setTitle("Game Over");
            alert.show();
        }
    }



    @FXML
    public void clickConfig(Event event) {
        // connect direct player util ensure to start
        buildConfigNetworkPane();

        // here is not prefect, so refactor it
    }
}
