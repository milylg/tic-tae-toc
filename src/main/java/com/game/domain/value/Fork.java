package com.game.domain.value;

import javafx.scene.effect.Light;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.awt.*;

/**
 * @author VIRIYA
 * @create 2020/10/23 23:16
 */
public class Fork extends Path {

    public Fork() {
        MoveTo moveToStart = new MoveTo(0, 0);
        LineTo downLine = new LineTo(100, 100);

        MoveTo moveToLeft = new MoveTo(0,100);
        LineTo line2 = new LineTo(100,0);

        getElements().add(moveToStart);
        getElements().add(downLine);
        getElements().add(moveToLeft);
        getElements().add(line2);

        setVisible(true);
    }
}
