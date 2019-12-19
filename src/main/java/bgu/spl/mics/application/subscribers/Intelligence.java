package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import com.sun.nio.sctp.MessageInfo;

import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private List<MessageInfo> messageInfoList;

	public Intelligence() {
		super("Intelligence" );
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		// TODO Implement this
	}
}
