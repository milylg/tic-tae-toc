package com.game.controller;

import com.game.domain.*;
import javafx.event.ActionEvent;
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
    private Label info;

    @FXML
    private GridPane box;

    private AbstractPlayer chessPlayer;

    @FXML
    public void clickAiPlayer(ActionEvent event) {
        clearCheeses();
        chessPlayer = PlayerFactory.createAiPlayer()
                .setChessType(ChessEnumType.FORK)
                .clearCache()
                .setChoose(true)
                .setFirst(true);
        Point location = chessPlayer.startPlay();
        chessPlayer.flushChessBoard(location, ChessEnumType.FORK);
        flush(chessPlayer.createShape(), location);
    }

    @FXML
    public void clickNet(ActionEvent event) {
        clearCheeses();
        buildConfigNetworkPane();
        chessPlayer = PlayerFactory.createRemotePlayer()
                .setChessType(ChessEnumType.FORK)
                .setChoose(true)
                .clearCache();
    }

    /**
     * col = [0 - 2]
     * row = [0 - 2]
     *
     * TODO: Event Callback AI player execute progress
     */
    @FXML
    public void clickGrid(MouseEvent event) {
        if (Optional.ofNullable(chessPlayer).isPresent()) {
            Circle circle = new Circle(50);
            circle.setVisible(true);
            Point point = new Point(event.getX(), event.getY());
            logger.info("people click x = {}, y = {}", point.getX(), point.getY());
            if (chessPlayer.checkEmptySlot(point)) {
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
            } // end if
        } // end if
    } // end method

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


}
