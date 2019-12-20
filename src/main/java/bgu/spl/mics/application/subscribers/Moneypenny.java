package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.passiveObjects.Result;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;

/**
 * Only this type of Subscriber can access the squad.
 * There are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	Squad squad;

	public Moneypenny() {
		super("Change_This_Name");
		squad = Squad.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent event) -> {
			Result result = new Result(squad.getAgents(event.getAgents()), 0 ); //TODO: deal with time
			complete(event, result);
		});
	}

}
