package rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher;

import rs.ac.bg.rcub.ams.ejb3.entity.Message;

public interface Dispatcher {

	public boolean dispatch(Message m);

}
