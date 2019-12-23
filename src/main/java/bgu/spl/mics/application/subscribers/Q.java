package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.GadgetAvailableResult;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private final boolean debug = true;
	private int currentTime;
	private Inventory inventory;

	public Q() {
		super("Q");
		currentTime = 0;
		inventory = Inventory.getInstance();
	}
	public Q(int id) {
		super("Q" + 1);
		currentTime = 0;
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent event) -> {
			String gadget = event.getGadget();
			GadgetAvailableResult result = new GadgetAvailableResult(currentTime, inventory.getItem(gadget));
			System.out.println("Q received the gadget " + event.getGadget() + ", Q gadget isFind:" + result.isSuccessful() + " At time: " + currentTime);
			complete(event, result);
		});
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast broadcast) -> {
			currentTime = broadcast.getCurrentTime();
			if (broadcast.isTerminated()) {
				terminate();
			}
			if (debug)
				System.out.println(this.getName() + " currentTime update to: " + currentTime);
		});
	}
}
