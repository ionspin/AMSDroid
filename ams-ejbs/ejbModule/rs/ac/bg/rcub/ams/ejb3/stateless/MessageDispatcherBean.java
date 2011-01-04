package rs.ac.bg.rcub.ams.ejb3.stateless;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher.Dispatcher;
import rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher.DispatcherFactory;
import rs.ac.bg.rcub.ams.util.Global;
import rs.ac.bg.rcub.ams.util.enums.MessageStatus;

@Stateless
public class MessageDispatcherBean implements MessageDispatcher {

	private static Log log = LogFactory.getLog(MessageDispatcherBean.class);

	@PersistenceContext(unitName = "ams")
	EntityManager em;

	@Resource
	TimerService timerService;

	public void startTimer() {
		String name = "ams-dispatch-timer-" + String.valueOf((int) ((Math.random() * 1000)));
		long interval = Global.DISPATCH_INTERVAL;
		timerService.createTimer(interval, interval, name);

		log.info("Starting timer: " + name + ", interval = " + interval);
	}

	public void stopTimer() {
		if (timerService.getTimers().size() == 0) {
			log.info("No timers found for this bean.");
			return;
		}

		log.info("Stopping all the timer(s) for this bean...");
		for (Object o : timerService.getTimers()) {
			Timer t = (Timer) o;
			log.info("\tStopping: " + t.getInfo());
			t.cancel();
		}
	}

	@Timeout
	public void timeoutHandler(Timer timer) {
		log.info("Entered timer task: " + timer.getInfo());
		process();
	}

	private void process() {
		List<Message> unsent = findUnsentMessages();
		log.info("Total unsent messages: " + unsent.size());
		for (Message m : unsent) {
			// no need to check for ready with unsent messages
			dispatch(m);
		}

		List<Message> scheduled = findScheduledMessages();
		log.info("Total scheduled messages: " + scheduled.size());
		for (Message m : scheduled) {
			if (checkReady(m)) {
				m.setStatus(MessageStatus.SENDING);
				em.persist(m);
				log.info("Message ready for dispatchment: " + m);
				// try to dispatch immediately
				dispatch(m);
			}
		}

	}

	private boolean processed(Alert a) {
		// maybe called way too often?
		// rework to have alert.messages?
		long num = (Long) em.createQuery(
				"SELECT COUNT(DISTINCT m) FROM Message m WHERE :alert MEMBER OF m.alerts AND m.status = :status").setParameter(
				"alert", a).setParameter("status", MessageStatus.SCHEDULED).getSingleResult();
		return num == 0;
	}

	private boolean dispatch(Message m) {
		boolean dispatched;
		try {
			Dispatcher disp = DispatcherFactory.getDispatcher(m.getSub().getType());
			dispatched = disp.dispatch(m);
		} catch (NamingException e) {
			dispatched = false;
			log.error(e, e);
		}
		if (dispatched) {
			m.setStatus(MessageStatus.DISPATCHED);
			em.persist(m);
			log.info("Successfully dispatched Message (ID): " + m.getId());
			for (Alert a : m.getAlerts()) {
				if (processed(a)) {
					a.setDispatched(true);
					em.persist(a);
					log.info("All messages dispatched for Alert (ID): " + a.getId());
				}
			}
		} else {
			m.setRetryCount(m.getRetryCount() + 1);
			if (m.getRetryCount() == Global.MAX_RETRY_COUNT) {
				m.setStatus(MessageStatus.FAILED);
			}
			em.persist(m);
			log.warn("Message dispatching failed: " + m);
		}
		return dispatched;
	}

	/**
	 * Checks rules to see whether message should be dispatched.
	 * 
	 * @param m
	 * @return
	 */
	private boolean checkReady(Message m) {

		long offset = m.getSub().getDispatchTimeout();
		// check oldest alert only?
		long base = m.getAlerts().get(0).getArrived().getTime();
		int numAlerts = m.getAlerts().size();
		boolean result = false;
		if (System.currentTimeMillis() >= base + offset) {
			// NOTE: messages with offset==0 might not be sent because of delay
			int numErrorAlerts = 0;
			for (Alert a : m.getAlerts()) {
				if (a.isError())
					numErrorAlerts++;
			}
			// a or (not a and b) == (a or not a) and (a or b) == a or b
			if ((!m.getSub().isIgnoreIfLastOK() || m.getAlerts().get(numAlerts - 1).isError())
					&& numErrorAlerts >= m.getSub().getCountThreshold()) {
				result = true;
			} else {
				log.info(String.format(
						"Message (id=%d) didnt meet the dispatch criteria (numErrorAlerts=%d, ignore=%b), ignored!", m.getId(),
						numErrorAlerts, m.getSub().isIgnoreIfLastOK()));
				m.setStatus(MessageStatus.IGNORED);
				em.persist(m);
			}
		}

		return result;
	}

	private List<Message> findScheduledMessages() {
		List<Message> l = em.createQuery("SELECT m FROM Message m WHERE m.status = :status").setParameter("status",
				MessageStatus.SCHEDULED).getResultList();

		return l;
	}

	private List<Message> findUnsentMessages() {
		List<Message> l = em.createQuery("SELECT m FROM Message m WHERE m.status = :status").setParameter("status",
				MessageStatus.SENDING).getResultList();

		return l;
	}

}
