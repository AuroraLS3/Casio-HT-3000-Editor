package com.djrapitops.miditest.ui.javafx;

import com.djrapitops.miditest.Main;
import com.djrapitops.miditest.State;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    private Stage primaryStage;
    private State state;

    public Window() {
        state = new State();
    }

    public static void start(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // primaryStage.getIcons().add(new Image(Window.class.getResourceAsStream("/Logo.png")));

        try {
            primaryStage.setTitle("Midi Tester");

            Scene selectDevice = new SelectDeviceScene(state);
            ObservableList<String> stylesheets = selectDevice.getStylesheets();
            stylesheets.addAll(
                    Main.class.getResource("/com/jfoenix/assets/css/jfoenix-fonts.css").toExternalForm(),
                    Main.class.getResource("/com/jfoenix/assets/css/jfoenix-design.css").toExternalForm()
            );
            primaryStage.setScene(selectDevice);
            primaryStage.show();
        } catch (Exception e) {
            primaryStage.setScene(new FatalErrorScene(e));
            primaryStage.show();
        }
    }

    @Override
    public void stop() {
    }
}
