package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private final boolean debug = true;

    private int currentTime;
    private int duration;
    private Timer timer;

    public TimeService() {
        super("WorldClock");//TODO: change
        currentTime = 0;
        timer = new Timer();
    }
    public TimeService(int duration) {
        super("WorldClock");//TODO: change
        currentTime = 0;
        this.duration = duration;
        timer = new Timer();
    }

    @Override
    protected void initialize() {
        // TODO Implement this
    }

    @Override
    public void run() {
            TimerTask repeatedTask = new TimerTask() {
                public void run() {
                    Broadcast b;
                    if(currentTime == duration) {
                        b = new TickBroadcast(currentTime, true);
                        timer.cancel(); //TODO: terminate all the threads
                    }
                    else
                        b = new TickBroadcast(currentTime, false);
                    if(debug)
                        System.out.println("CurrentTime: " + currentTime);
                    getSimplePublisher().sendBroadcast(b);
                    currentTime++;
                }
            };
            timer.scheduleAtFixedRate(repeatedTask, 1000, 100); //TODO Change to 100 Milliseconds
    }
}
