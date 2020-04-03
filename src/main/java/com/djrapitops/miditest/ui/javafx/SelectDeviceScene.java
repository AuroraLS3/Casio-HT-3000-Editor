package com.djrapitops.miditest.ui.javafx;

import com.djrapitops.miditest.State;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
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
        setOptions(state, select);
        container.getChildren().add(select);

        JFXButton refresh = new JFXButton("Refresh list");
        refresh.setOnAction(event -> setOptions(state, select));
        refresh.setStyle(Styles.BG_DARK_CYAN);
        select.setLeft(refresh);

        JFXButton ok = new JFXButton("OK");
        ok.setDisable(true);
        state.addListener(newState -> {
            if (newState.getChosenDevice() != null) {
                ok.setDisable(false);
                select.setBottom(ok);
            }
        });
        ok.setStyle(Styles.BG_LIGHT_GREEN);
        ok.setOnAction(event -> state.setView(View.SEND_MIDI_SIGNAL));
        select.setBottom(ok);

        return container;
    }

    private static void setOptions(State state, BorderPane select) {
        Map<String, MidiDevice.Info> devices = getDevices();

        JFXComboBox<Label> options = new JFXComboBox<>();

        for (String deviceName : devices.keySet()) {
            options.getItems().add(new Label(deviceName));
        }

        options.setPromptText("Select MIDI Out");

        options.addEventHandler(JFXComboBox.ON_HIDDEN, event -> {
            Label deviceName = options.getSelectionModel().getSelectedItem();
            MidiDevice.Info device = deviceName != null ? devices.get(deviceName.getText()) : null;
            state.setChosenDevice(device);
        });
        select.setCenter(options);
    }

    private static Map<String, MidiDevice.Info> getDevices() {
        Map<String, MidiDevice.Info> devices = new LinkedHashMap<>();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try (MidiDevice device = MidiSystem.getMidiDevice(info)) {
                device.open();
                devices.put(info.getName() + " : " + info.getVendor(), info);
            } catch (MidiUnavailableException e) {
//                System.out.println(info.getName() + " errored: " + e.toString());
            }
        }
        return devices;
    }


}
