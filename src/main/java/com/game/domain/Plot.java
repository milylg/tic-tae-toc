package com.game.domain;

import java.io.Serializable;

/**
 * @author VIRIYA
 * @create 2020/10/24 1:08
 */
public class Plot implements Serializable {

    private int x;
    private int y;

    public Plot() {

    }

    public Plot(double x, double y) {
        this.x = (int)x / 100;
        this.y = (int)y / 100;
    }

    public static Plot builder() {
        return new Plot();
    }

    public static Plot[][] buildGroup(int[][] groupXY3) {
        Plot[][] coordinates = new Plot[groupXY3.length][3];
        for (int group = 0, len = groupXY3.length; group < len; group ++) {
            for (int i = 0, index = 0, size = groupXY3[group].length; i < size; i = i + 2, index++) {
                coordinates[group][index] = new Plot().setX(groupXY3[group][i]).setY(groupXY3[group][i + 1]);
            }
        }
        return coordinates;
    }

    public Plot setX(int x) {
        this.x = x;
        return this;
    }

    public Plot setY(int y) {
        this.y = y;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
