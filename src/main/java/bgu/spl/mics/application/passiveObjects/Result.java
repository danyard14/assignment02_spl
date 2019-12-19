package bgu.spl.mics.application.passiveObjects;

public class Result {
    private boolean isResolved;
    private int time;

    public Result(){
    }

    public Result(Boolean isResolved, int time){
        this.isResolved = isResolved;
        this.time = time;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}
