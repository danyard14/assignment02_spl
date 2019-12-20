package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.passiveObjects.Result;
import bgu.spl.mics.application.passiveObjects.ResultAgentAvailable;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * There are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	Squad squad;
	int moneypennySerial;

	public Moneypenny() {
		super("Change_This_Name");
		squad = Squad.getInstance();
		//TODO: init moneypenny serial
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent event) -> {
			List<String> agentsSerials = event.getAgents();
			ResultAgentAvailable result = new ResultAgentAvailable();
			result.setTime(0);	//TODO: deal with time
			result.setSerials(squad.getAgentsNames(agentsSerials));
			result.setMoneypenny(this.moneypennySerial);
			result.setNames(squad.getAgentsNames(agentsSerials));
			complete(event, result);
		});

		subscribeEvent(SendAgentsEvent.class, (SendAgentsEvent event) -> {
			Squad.getInstance().sendAgents(event.getAgents(), event.getDuration());
		});
	}

	public int getMoneypennySerial() {
		return moneypennySerial;
	}
}
