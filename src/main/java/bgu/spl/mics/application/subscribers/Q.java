package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Result;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inventory;

	public Q() {
		super("Change_This_Name");
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent event) -> {
			String gadget = event.getGadget();
			Result result = new Result(inventory.getItem(gadget), 0 ); //TODO: deal with time
			complete(event, result);
		});
	}
}
