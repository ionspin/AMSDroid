package rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import rs.ac.bg.rcub.ams.util.enums.MessageType;

public abstract class DispatcherFactory {

	static final String EMAIL_DISPATCHER = "ams/EmailDispatcherBean/local";
	static final String GTALK_DISPATCHER = "ams/GtalkDispatcherBean/local";

	private static InitialContext ctx;

	public static Dispatcher getDispatcher(MessageType type) throws NamingException {

		if (ctx == null)
			ctx = new InitialContext();

		switch (type) {
		case EMAIL:
			return (EmailDispatcher) ctx.lookup(EMAIL_DISPATCHER);
		case GTALK:
			return (GtalkDispatcher) ctx.lookup(GTALK_DISPATCHER);
		case SMS:
			return null;
		}
		return null;
	}

}
