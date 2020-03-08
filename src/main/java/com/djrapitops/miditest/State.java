package com.djrapitops.miditest;

import com.djrapitops.miditest.ui.javafx.View;

import javax.sound.midi.MidiDevice;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class State {

    private List<Consumer<State>> listeners;
    private MidiDevice.Info chosenDevice;
    private View view;

    public State() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    private void stateChanged() {
        for (Consumer<State> listener : listeners) {
            listener.accept(this);
        }
    }

    public void addListener(Consumer<State> newStateListener) {
        listeners.add(newStateListener);
    }

    public MidiDevice.Info getChosenDevice() {
        return chosenDevice;
    }

    public void setChosenDevice(MidiDevice.Info chosenDevice) {
        this.chosenDevice = chosenDevice;
        stateChanged();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
        stateChanged();
    }
}
