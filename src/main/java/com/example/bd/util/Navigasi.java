package com.example.bd.util;

import com.example.bd.HelloApplication;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent; // Import baru
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class Navigasi {

    private static final Duration DURATION = Duration.millis(350);


    public static void switchScene(ActionEvent event, String fxmlFile) {
        animateAndSwitch((Node) event.getSource(), fxmlFile, true);
    }


    public static void goBack(ActionEvent event, String fxmlFile) {
        animateAndSwitch((Node) event.getSource(), fxmlFile, false);
    }


    public static void switchSceneFromMouse(MouseEvent event, String fxmlFile) {
        animateAndSwitch((Node) event.getSource(), fxmlFile, true);
    }


    private static void animateAndSwitch(Node sourceNode, String fxmlFile, boolean dariKanan) {
        try {
            Scene scene = sourceNode.getScene();
            double sceneWidth = scene.getWidth();

            URL resourceUrl = HelloApplication.class.getResource("view/" + fxmlFile);
            Parent newRoot = FXMLLoader.load(resourceUrl);

            newRoot.setTranslateX(dariKanan ? sceneWidth : -sceneWidth);
            scene.setRoot(newRoot);

            TranslateTransition slideIn = new TranslateTransition(DURATION, newRoot);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_OUT);
            slideIn.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}