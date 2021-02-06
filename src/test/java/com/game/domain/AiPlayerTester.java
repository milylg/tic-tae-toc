package com.game.domain;

import com.game.domain.value.ChessEnumType;
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
@PrepareForTest(AiPlayer.class)
public class AiPlayerTester {

    private AiPlayer mockAiChessPlayerOn(int[][] chessBoard) {

        AiPlayer aiPlayer = new AiPlayer();
        // mock private field of AiChessPlayer
        int[][] cache = Whitebox.getInternalState(aiPlayer, "cache");

        // status of chess board
        for (int i = 0; i < 3; i ++) {
            cache[i] = Arrays.copyOf(chessBoard[i],3);
        }

        return aiPlayer;
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
        AiPlayer aiPlayer = mockAiChessPlayerOn(chessBoardOfForkWin);

        aiPlayer.setChessType(ChessEnumType.FORK);
        assertFalse(aiPlayer.isWin());

        aiPlayer.setChessType(ChessEnumType.CIRCLE);
        assertTrue(aiPlayer.isWin());
    }

    @Test
    public void testGameResultCircleIsWin() {
        // CIRCLE IS ONE
        int[][] chessBoardOfCircleWin = {
                { 1, -1, -1},
                { 1,  1, -1},
                {-1,  1,  1}
        };
        AiPlayer aiPlayer = mockAiChessPlayerOn(chessBoardOfCircleWin);

        aiPlayer.setChessType(ChessEnumType.CIRCLE);
        assertTrue(aiPlayer.isWin());

        aiPlayer.setChessType(ChessEnumType.FORK);
        assertFalse(aiPlayer.isWin());
    }

    @Test
    public void testGameResultIsDraw() {
        int[][] chessBoardOfDraw = {
                { 1, -1,  1},
                {-1,  -1,  1},
                {-1,  1, -1}
        };
        AiPlayer aiPlayer = mockAiChessPlayerOn(chessBoardOfDraw);
        assertTrue(aiPlayer.isDraw());
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
        AiPlayer aiPlayer = mockAiChessPlayerOn(chessBoardOfWithoutDraw);
        assertFalse(aiPlayer.isDraw());
    }


    @Test
    public void testGameResultForkIsLose() {
        // FORK IS MINUS ONE (NEGATIVE NUMBER)
        int[][] chessBoardOfForkLose = {
                {-1, 1, 1},
                {0, -1, 1},
                {0, -1, 1}
        };
        AiPlayer aiPlayer = mockAiChessPlayerOn(chessBoardOfForkLose);
        aiPlayer.setChessType(ChessEnumType.FORK);
        assertTrue(aiPlayer.isLose());
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

    @Test
    public void testArr() {
        int[][] res = new int[4][3];
        res[3] = new int[]{3,4,3,4,5};
        assert 4 == res.length;
    }
}