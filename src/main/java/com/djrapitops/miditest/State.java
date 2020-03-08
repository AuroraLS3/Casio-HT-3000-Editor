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

    private int midiType = -1;
    private short midiData1 = -1;
    private short midiData2 = -1;

    public State() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    private void stateChanged() {
        System.out.println(this);
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

    public int getMidiType() {
        return midiType;
    }

    public void setMidiType(int midiType) {
        this.midiType = midiType;
        stateChanged();
    }

    public short getMidiData1() {
        return midiData1;
    }

    public void setMidiData1(short midiData1) {
        this.midiData1 = midiData1;
        stateChanged();
    }

    public short getMidiData2() {
        return midiData2;
    }

    public void setMidiData2(short midiData2) {
        this.midiData2 = midiData2;
        stateChanged();
    }

    @Override
    public String toString() {
        return "State{" +
                "chosenDevice=" + chosenDevice +
                ", view=" + view +
                ", midiType=" + midiType +
                ", midiData1=" + midiData1 +
                ", midiData2=" + midiData2 +
                '}';
    }
}
