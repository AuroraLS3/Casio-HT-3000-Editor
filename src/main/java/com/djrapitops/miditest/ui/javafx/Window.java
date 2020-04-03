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
    private View shownView = null;

    public Window() {
        state = new State();
    }

    public static void start(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // primaryStage.getIcons().add(new Image(Window.class.getResourceAsStream("/Logo.png")));

        try {
            primaryStage.setTitle("Midi Tester");

            state.addListener(this::setScene);
            state.setView(View.SELECT_MIDI_OUT);
            primaryStage.show();
        } catch (Exception e) {
            stage.setScene(new FatalErrorScene(state, e));
            stage.show();
        }
    }

    private void setScene(State state) {
        Scene scene;
        View newView = state.getView();
        if (shownView == newView) {
            return;
        }
        switch (newView) {
            case SELECT_MIDI_OUT:
                scene = new SelectDeviceScene(state);
                break;
            case SEND_MIDI_SIGNAL:
                scene = new SendMidiScene(state);
                break;
            default:
                scene = new FatalErrorScene(state, new IllegalStateException("Unsupported view: " + newView.name()));
                break;
        }
        ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(
                Main.class.getResource("/com/jfoenix/assets/css/jfoenix-fonts.css").toExternalForm(),
                Main.class.getResource("/com/jfoenix/assets/css/jfoenix-design.css").toExternalForm()
        );
        shownView = newView;
        primaryStage.setScene(scene);
    }

    @Override
    public void stop() {
    }
}
