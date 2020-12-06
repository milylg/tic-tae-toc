package com.game.controller;

import com.game.domain.*;
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

    @FXML
    private Button conf;

    @FXML
    private GridPane box;

    @FXML
    private Button net;

    private AbstractPlayer chessPlayer;
    private NetworkService networkService;

    private boolean isRemotePlayer = false;


    private boolean isSuccessConnect = false;
    private boolean isStartPlay = true;


    @FunctionalInterface
    public interface FlushBoardCallback {
        /**
         * service invoke it to flush view
         * @param point
         * @param type
         */
        void call(Point point, ChessEnumType type);
    }

    @FunctionalInterface
    public interface HitMessageCallback {
        void call(Result result);
    }

    public GameInterface() {
        networkService = NetworkService.getInstance();

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
        Point location = chessPlayer.startPlay();
        chessPlayer.flushChessBoard(location, ChessEnumType.FORK);
        flush(chessPlayer.createShape(), location);
    }



    @FXML
    public void clickNet(ActionEvent event) {
        // operate filter

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
                .setChessType(isStartPlay ? ChessEnumType.FORK : ChessEnumType.CIRCLE)
                .setChoose(true)
                .clearCache();


        // call back flush board chess
        networkService.addFlushBoardCallBack(((point, type) -> {
            logger.info("exec1");
            Platform.runLater(() -> {
                logger.info("exec2");
                if (type == ChessEnumType.FORK) {
                    logger.info("exec3");
                    flush(new Fork(), point);
                } else {
                    logger.info("exec3");
                    flush(new Circle(), point);
                }
                chessPlayer.flushChessBoard(point, type);
            });
        }));

        networkService.addHitMessageCallback((result -> {
            Platform.runLater(() -> {
                buildWindow(result);
            });
        }));
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
        } else {
            actionRemotePlayerFor(event);
        }
    }


    private void actionRemotePlayerFor(MouseEvent event) {
        if (!networkService.getConnectResult()) {
            return;
        }
        // action of current player
        // how choose chess type of player
        Point point = new Point(event.getX(), event.getY());
        ChessLocation location = ChessLocation.build(point, chessPlayer.getChessType());
        chessPlayer.flushChessBoard(point, chessPlayer.getChessType());
        // flush chess broad of current player
        flush(chessPlayer.createShape(), point);

        Result gameResult = chessPlayer.gameResult();
        if (gameResult == Result.CONTINUE) {
            // notify remote chess board
            networkService.send(location);
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
            Point point = new Point(event.getX(), event.getY());
            logger.info("people click x = {}, y = {}", point.getX(), point.getY());

            if (chessPlayer.checkNotEmptySlot(point)) {
                return;
            }
            flush(circle, point);
            chessPlayer.flushChessBoard(point, ChessEnumType.CIRCLE);
            chessPlayer.setWillPlay(true);
            Result result = chessPlayer.gameResult();
            logger.info("people after game result is {}", result);

            // AI or Network player can play chess
            if (result == Result.CONTINUE) {
                Point pointOfAiOrRemote = chessPlayer.play();
                logger.info("AI click x = {}, y = {}", pointOfAiOrRemote.getX(), pointOfAiOrRemote.getY());
                chessPlayer.setWillPlay(false);
                flush(chessPlayer.createShape(), pointOfAiOrRemote);
                chessPlayer.flushChessBoard(pointOfAiOrRemote, ChessEnumType.FORK);
                Result aiResult = chessPlayer.gameResult();
                buildWindow(aiResult);
                return;
            }
            buildWindow(result);
        }
    }



    private void flush(Shape shape, Point point) {
        if (point != null) {
            box.add(shape, point.getX(), point.getY());
        }
    }

    private void clearCheeses() {
        box.getChildren().clear();
        // without effect
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
