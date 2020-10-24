package com.game.domain;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:08
 */
public class Point {
    private int x;
    private int y;

    public Point(double x, double y) {
        this.x = (int)x / 100;
        this.y = (int)y / 100;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
