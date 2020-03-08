package com.djrapitops.miditest;

import javax.sound.midi.MidiDevice;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class State {

    private Set<Consumer<State>> listeners;
    private MidiDevice.Info chosenDevice;

    public State() {
        this.listeners = new HashSet<>();
    }

    private void stateChanged() {
        listeners.forEach(consumer -> consumer.accept(this));
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
}
