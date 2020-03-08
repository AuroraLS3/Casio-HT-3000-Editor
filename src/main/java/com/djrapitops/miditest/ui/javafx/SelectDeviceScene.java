package com.djrapitops.miditest.ui.javafx;

import com.djrapitops.miditest.State;
import com.jfoenix.controls.JFXComboBox;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import java.util.LinkedHashMap;
import java.util.Map;

public class SelectDeviceScene extends Scene {

    public SelectDeviceScene(State state) {
        super(createComponent(state), 300, 100);
    }

    private static Parent createComponent(State state) {
        VBox container = new VBox();
        container.setPadding(new Insets(10));

        BorderPane select = new BorderPane();

        Map<String, MidiDevice.Info> devices = new LinkedHashMap<>();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            devices.put(info.getName() + " : " + info.getVendor(), info);
        }

        JFXComboBox<Label> options = new JFXComboBox<>();

        for (String deviceName : devices.keySet()) {
            options.getItems().add(new Label(deviceName));
        }

        options.setPromptText("Select MIDI Out");

        options.onActionProperty().addListener((observable, oldValue, newValue) -> {
            Label deviceName = options.getSelectionModel().getSelectedItem();
            MidiDevice.Info device = devices.get(deviceName.getText());
            state.setChosenDevice(device);
        });
        select.setCenter(options);
        container.getChildren().add(select);
        return container;
    }


}
