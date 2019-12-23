package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int currentTime;
    private boolean isTerminated;

    //Constructors
    public TickBroadcast() {
        currentTime = 0;
        isTerminated = false;
    }
    public TickBroadcast(int currentTime, boolean isTerminated) {
        this.currentTime = currentTime;
        this.isTerminated = isTerminated;
    }

    //Getters
    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isTerminated() {
        return isTerminated;
    }
}
