package com.djrapitops.miditest.ui.javafx;

import com.djrapitops.miditest.State;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.sound.midi.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SendMidiScene extends Scene {

    public SendMidiScene(State state) {
        super(createComponent(state), 600, 80);
    }

    private static Parent createComponent(State state) {
        VBox container = new VBox();
        container.setPadding(new Insets(10));

        HBox menu = new HBox();
        container.getChildren().add(menu);

        Map<String, Integer> midiTypes = new LinkedHashMap<>();
        midiTypes.put("Control Change", ShortMessage.CONTROL_CHANGE);
        midiTypes.put("Program Change", ShortMessage.PROGRAM_CHANGE);
        midiTypes.put("Stop", ShortMessage.STOP);
        JFXComboBox<Label> options = new JFXComboBox<>();
        for (String key : midiTypes.keySet()) {
            options.getItems().add(new Label(key));
        }
        options.setPromptText("MIDI Type");
        options.addEventHandler(JFXComboBox.ON_HIDDEN, event -> {
            Label midiType = options.getSelectionModel().getSelectedItem();
            Integer num = midiTypes.getOrDefault(midiType.getText(), -1);
            state.setMidiType(num);
        });
        menu.getChildren().add(options);

        JFXTextField data1Field = new JFXTextField();
        data1Field.setStyle(Styles.BUTTON_SQUARE);
        data1Field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                short data1 = Short.parseShort(newValue);
                state.setMidiData1(data1);
                data1Field.setStyle(Styles.BUTTON_SQUARE + Styles.BLACK_FONT);
            } catch (IllegalArgumentException nonInt) {
                data1Field.setStyle(Styles.BUTTON_SQUARE + Styles.RED_FONT);
            }
        });
        menu.getChildren().add(data1Field);

        JFXTextField data2Field = new JFXTextField();
        data2Field.setStyle(Styles.BUTTON_SQUARE);
        data2Field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                short data2 = Short.parseShort(newValue);
                state.setMidiData2(data2);
                data2Field.setStyle(Styles.BUTTON_SQUARE + Styles.BLACK_FONT);
            } catch (IllegalArgumentException nonInt) {
                state.setMidiData2((short) -1);
                data2Field.setStyle(Styles.BUTTON_SQUARE + Styles.RED_FONT);
            }
        });
        menu.getChildren().add(data2Field);

        JFXButton send = new JFXButton("Send");
        send.setDisable(true);
        send.setStyle(Styles.BG_LIGHT_GREEN);
        send.setOnAction(event -> {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(state.getChosenDevice());
                try (Receiver receiver = device.getReceiver()) {
                    ShortMessage myMsg = new ShortMessage();
                    myMsg.setMessage(state.getMidiType(), 0, state.getMidiData1(), state.getMidiData2());
                    receiver.send(myMsg, -1);
                }
            } catch (MidiUnavailableException | InvalidMidiDataException e) {
                throw new IllegalStateException(e);
            }
        });
        state.addListener(newState -> {
            if (newState.getMidiType() == -1 || newState.getMidiData1() == -1 || newState.getMidiData2() == -1) {
                send.setDisable(true);
            } else {
                send.setDisable(false);
            }
        });
        menu.getChildren().add(send);

        return container;
    }
}
