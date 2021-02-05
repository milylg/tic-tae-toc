package com.game.controller;

import com.game.domain.*;
import com.game.domain.value.ChessEnumType;
import com.game.domain.value.Fork;
import com.game.domain.value.Plot;
import com.game.domain.value.Result;
import com.game.net.ChessLocation;
import com.game.service.NetworkService;
import javafx.application.Platform;
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

/**
 * @author VIRIYA
 * @create 2020/10/23 20:39
 */
public class GameInterface {

    private static Logger logger = LoggerFactory.getLogger("com.game.controller");

    private static AbstractPlayer chessPlayer;
    private static NetworkService networkService;
    private static boolean isRemotePlayer = false;

    @FXML
    private GridPane box;

    @FunctionalInterface
    public interface FlushBoardCallback {
        /*
         * service invoke it to flush view
         */
        void callbackFlushView(Plot plot, ChessEnumType type);
    }

    @FunctionalInterface
    public interface HitMessageCallback {
        void showHintMessageDialog(Result result);
    }

    @FunctionalInterface
    public  interface FirstPlayCallback {

        default void createRemotePlayer() {
            chessPlayer = PlayerFactory.createRemotePlayer()
                    .setChessType(ChessEnumType.FORK)
                    .clearChessCache();
            isRemotePlayer = true;
        }

        void buildSelectFirstPlayDialog();
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
            networkService.connect();
        });
        logger.info("controller initialize");
    }


    @FXML
    public void clickAiPlayer(ActionEvent event) {
        clearCheeses();
        isRemotePlayer = false;
        chessPlayer = PlayerFactory.createAiPlayer()
                .setChessType(ChessEnumType.FORK)
                .clearChessCache()
                .setStartPlay(true);
        Plot location = chessPlayer.startPlay();
        chessPlayer.flushChessBoard(location, ChessEnumType.FORK);
        flush(chessPlayer.createShape(), location);
    }



    @FXML
    public void clickNet(ActionEvent event) {
        clearCheeses();
        isRemotePlayer = true;
        if (connectPartner(networkService)) {
            return;
        }

        // chess type is fork when first play
        chessPlayer = PlayerFactory.createRemotePlayer()
                .defaultChessType()
                .clearChessCache();

        sendRequestForStartGame();
    }

    static boolean connectPartner(NetworkService networkService) {
        if (!networkService.getConnectResult()) {
            boolean isConnected = networkService.connect();
            if (!isConnected) {
                Dialog<ButtonType> error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("Failed to connect to the another player.");
                error.show();
                return true;
            }
        }
        return false;
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

        logger.info("Now, I can play ?" + (networkService.isFirst() ? " yes" : " no"));

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
        logger.info("Now, I can play ?" + (networkService.isFirst() ? " yes" : " no"));
        if (gameResult == Result.CONTINUE) {
            return ;
        }
        buildWindow(gameResult);
        // as another
        sendGameResultToPartner(gameResult);
    }

    private void sendGameResultToPartner(Result gameResult) {
        switch (gameResult) {
            case WIN:
                networkService.send(Result.LOSE);
                break;
            case LOSE:
                networkService.send(Result.WIN);
                break;
            case DRAW:
                networkService.send(Result.DRAW);
                break;
            default:
                break;
        }
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
