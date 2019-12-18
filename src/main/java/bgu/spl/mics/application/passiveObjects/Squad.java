package bgu.spl.mics.application.passiveObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	//TODO: check: 1.maybe change map to synchronized map
	//				2. maybe initialize map in the declaration.


	private Map<String, Agent> agents;

	private static class SquadHolder {
		private static Squad instance = new Squad();
	}

	// initialization
	private Squad(){
		agents = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(Agent agent : agents){
			this.agents.put(agent.getSerialNumber(), agent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for(String serial : serials){
			if(this.agents.containsKey(serial)){
				this.agents.get(serial).release();
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		for(String serial: serials ){
			agents.get(serial).acquire();
		}
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		for(String serial: serials ){
			agents.get(serial).release();
		}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		for(String serial: serials){
			if(!this.agents.containsKey(serial)) {
				return false;
			}
			while (!agents.get(serial).isAvailable()){
				try {
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			this.agents.get(serial).acquire();
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		ArrayList<String> toReturn = new ArrayList<>();
    	for(String serial: serials){
    		if(agents.containsKey(serial)){
				toReturn.add(agents.get(serial).getName());
			}
		}
	    return toReturn;
    }

}
