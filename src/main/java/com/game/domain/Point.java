package com.game.domain;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:08
 */
public class Point {
    private int x;
    private int y;

    public Point() {

    }

    public Point(double x, double y) {
        this.x = (int)x / 100;
        this.y = (int)y / 100;
    }

    public static Point builder() {
        return new Point();
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
