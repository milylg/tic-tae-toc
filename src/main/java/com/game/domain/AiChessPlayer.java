package com.game.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:02
 */
public class AiChessPlayer extends AbstractPlayer {

    private Random random = new Random();

    @Override
    public Point startPlay() {
        clearCache();
        return randomLocation();
    }

    private Point randomLocation() {
        return new Point()
                .setX(random.nextInt(3))
                .setY(random.nextInt(3));
    }

    /**
     * can't use test case to test it,
     *
     * because is possibility that test case is error
     *
     * @return null if AI is loser
     */
    @Override
    public Point play() {
        List<Point> result = new ArrayList<>(100);
        List<Point> chooseList = searchPossibilityPoint();
        List<Map<Integer, Integer>> track = new ArrayList<>();

        // unuseful
        Point point = Point.builder()
                .setX(random.nextInt(3))
                .setY(random.nextInt(3));

        // MinMaxValue Algo
        // backTrack(result, chooseList, track);

        flushChessBoard(point, chessType);
        return point;
    }

    private void backTrack(List<Point> result, List<Point> choose, List<Map<Integer, Integer>> track) {
        if (isWin(chessType)) {
            /*
             * bounder condition
             *   - is win
             *   - size equals nine
             */
        }
        // choose the one of empty point

        // back track
        backTrack(result, choose, track);
        // undo the choose

    }


    @Override
    public Result gameResult() {
        if (isWin(chessType)) {
            return Result.WIN;
        }
        if (isDraw()) {
            return Result.DRAW;
        }
        if (isLose()) {
            return Result.LOSE;
        }
        return Result.CONTINUE;
    }
}
