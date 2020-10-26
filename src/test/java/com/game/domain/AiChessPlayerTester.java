package com.game.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;


import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author VIRIYA
 * @create 2020/10/24 13:16
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AiChessPlayer.class)
public class AiChessPlayerTester {

    private AiChessPlayer mockAiChessPlayerOn(int[][] chessBoard) {

        AiChessPlayer aiChessPlayer = new AiChessPlayer();
        // mock private field of AiChessPlayer
        int[][] cache = Whitebox.getInternalState(aiChessPlayer, "cache");

        // status of chess board
        for (int i = 0; i < 3; i ++) {
            cache[i] = Arrays.copyOf(chessBoard[i],3);
        }

        return aiChessPlayer;
    }

    /**
     * Test Start
     *
     * Here, we only know status of ai player,
     * needn't care for people
     */


    @Test
    public void testGameResultForkIsWin() {
        // FORK IS MINUS ONE (NEGATIVE NUMBER)
        int[][] chessBoardOfForkWin = {
                {-1, 0,-1},
                {0, -1, 1},
                {1,  1, 1}
        };
        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfForkWin);

        aiChessPlayer.setChessType(ChessEnumType.FORK);
        assertFalse(aiChessPlayer.isWin(ChessEnumType.FORK));

        aiChessPlayer.setChessType(ChessEnumType.CIRCLE);
        assertTrue(aiChessPlayer.isWin(ChessEnumType.CIRCLE));
    }

    @Test
    public void testGameResultCircleIsWin() {
        // CIRCLE IS ONE
        int[][] chessBoardOfCircleWin = {
                { 1, -1, -1},
                { 1,  1, -1},
                {-1,  1,  1}
        };
        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfCircleWin);

        aiChessPlayer.setChessType(ChessEnumType.CIRCLE);
        assertTrue(aiChessPlayer.isWin(ChessEnumType.CIRCLE));

        aiChessPlayer.setChessType(ChessEnumType.FORK);
        assertFalse(aiChessPlayer.isWin(ChessEnumType.FORK));
    }

    @Test
    public void testGameResultIsDraw() {
        int[][] chessBoardOfDraw = {
                { 1, -1,  1},
                {-1,  0,  1},
                {-1,  1, -1}
        };
        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfDraw);
        assertTrue(aiChessPlayer.isDraw());
        /**
         * need to pre judge is draw
         * for example :
         *
         *                 { 1, -1,  1},
         *                 { 0,  0,  1},
         *                 {-1,  1, -1}
         */
    }

    @Test
    public void testGameResultIsNotDraw() {
        int[][] chessBoardOfWithoutDraw = {
                { 1, -1,  1},
                { 0,  1, -1},
                {-1,  0,  0}
        };
        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfWithoutDraw);
        assertFalse(aiChessPlayer.isDraw());
    }


    @Test
    public void testGameResultForkIsLose() {
        // FORK IS MINUS ONE (NEGATIVE NUMBER)
        int[][] chessBoardOfForkLose = {
                {-1, 1, 1},
                {0, -1, 1},
                {0, -1, 1}
        };
        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfForkLose);
        aiChessPlayer.setChessType(ChessEnumType.FORK);
        assertTrue(aiChessPlayer.isLose());
    }

//    @Test
//    public void testGameResultCircleIsLose() {
//        // CIRCLE IS ONE
//        int[][] chessBoardOfCircleLose = {
//                {0,  1, -1},
//                {1, -1, -1},
//                {1,  0, -1}
//        };
//        AiChessPlayer aiChessPlayer = mockAiChessPlayerOn(chessBoardOfCircleLose);
//        aiChessPlayer.setChessType(ChessEnumType.CIRCLE);
//        assertTrue(aiChessPlayer.isLose());
//    }
}