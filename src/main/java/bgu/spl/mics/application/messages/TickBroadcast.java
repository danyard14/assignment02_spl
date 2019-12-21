package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int currentTime;

    //Constructors
    public TickBroadcast() {
        currentTime = 0;
    }
    public TickBroadcast(int currentTime) {
        this.currentTime = currentTime;
    }

    //Getters
    public int getCurrentTime() {
        return currentTime;
    }
}
