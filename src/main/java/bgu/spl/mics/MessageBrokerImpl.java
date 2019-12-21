package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Result;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private Map<Subscriber, LinkedBlockingQueue<Message>> subscribersMap; //TODO:change the message type
	private Map<String, ArrayList<Subscriber>> eventsMap;	//TODO:change field name
	private Map<Message, Future> eventFutureMap;

	private static class MessageBrokerHolder{
		private static MessageBroker instance = new MessageBrokerImpl();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}

	private MessageBrokerImpl(){
		subscribersMap = new ConcurrentHashMap<>();
		eventsMap = new ConcurrentHashMap<>();
		eventsMap.put(MissionReceivedEvent.class.getName(), new ArrayList<>());
		eventsMap.put(AgentsAvailableEvent.class.getName(), new ArrayList<>());
		eventsMap.put(GadgetAvailableEvent.class.getName(), new ArrayList<>());
		eventsMap.put(TickBroadcast.class.getName(), new ArrayList<>());
		eventFutureMap = new ConcurrentHashMap<>();
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (eventsMap.get(type.getClass().getName())) { //TODO: need notifyAll?
			if (!eventsMap.get(type.getClass().getName()).contains(m)) //TODO:check if sucsriber can subscribe twice
				eventsMap.get(type.getClass().getName()).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		System.out.println(type.getName());
		Object y = type.getClass().getName();
		Object x = eventsMap.get(type.getName());
		synchronized (eventsMap.get(type.getName())) { //TODO: need notifyAll?
			if (!eventsMap.get(type.getName()).contains(m)) //TODO:check if subscriber can subscribe twice
				eventsMap.get(type.getName()).add(m);
			else
				System.out.println();
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future future = eventFutureMap.get(e);
		future.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		String x = b.getClass().getName();
		ArrayList<Subscriber> list = eventsMap.get(b.getClass().getName());
		for (Subscriber subscriber : list) {
			subscribersMap.get(subscriber).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(eventsMap.get(e.getClass().getName()).isEmpty()) {
			return null;
		}
		ArrayList<Subscriber> list = eventsMap.get(e.getClass().getName());
		Subscriber subscriber = list.remove(0);
		list.add(subscriber);
		subscribersMap.get(subscriber).add(e);
		Future<T> future = new Future<>();
		eventFutureMap.put(e, future);
		return future;
	}

	@Override
	public void register(Subscriber m) {
		subscribersMap.put(m, new LinkedBlockingQueue<>());	//TODO:maybe another stracture
	}

	@Override
	public void unregister(Subscriber m) {	//TODO:maybe have to transfer all the subsriber event in the queue to the other subscribers
		if(subscribersMap.containsKey(m)) {
			subscribersMap.remove(m);
			for(Map.Entry<String, ArrayList<Subscriber>> eventType : eventsMap.entrySet()) {
				synchronized (eventType) {
					if (eventType.getValue().contains(m)) {
						eventType.getValue().remove(m);
					}
				}
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException { //TODO: check the try N catch of this method
//		try {
		//TODO delete comments
		if (subscribersMap.get(m) != null)
			return subscribersMap.get(m).take();;
//			while (subscribersMap.get(m).isEmpty()){
//				try {
//					Thread.currentThread().wait();
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}
//			}
//		} catch(IllegalStateException e) { }
//		return subscribersMap.get(m).poll();
		return null;
	}
}
