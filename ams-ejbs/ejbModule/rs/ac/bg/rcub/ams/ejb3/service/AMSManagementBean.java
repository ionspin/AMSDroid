package rs.ac.bg.rcub.ams.ejb3.service;

import javax.ejb.EJB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.Depends;
import org.jboss.ejb3.annotation.Service;

import rs.ac.bg.rcub.ams.ejb3.stateless.MessageDispatcher;
import rs.ac.bg.rcub.ams.ejb3.stateless.config.ConfigUpdater;

@Service
public class AMSManagementBean implements AMSManagement {

	private static Log log = LogFactory.getLog(AMSManagementBean.class);

	@EJB
	@Depends("jboss.j2ee:ear=ams.ear,jar=ams-ejbs.jar,name=MessageDispatcherBean,service=EJB3")
	private MessageDispatcher dispatcher;

	@EJB
	@Depends("jboss.j2ee:ear=ams.ear,jar=ams-ejbs.jar,name=ConfigUpdaterBean,service=EJB3")
	private ConfigUpdater updater;

	public void create() throws Exception {
		log.info("========== Deploying AMS service ==========");
	}

	public void destroy() {
		log.info("========== Undeploying AMS service ========");
	}

	public void start() throws Exception {
		log.info("========== Starting AMS service ===========");
		dispatcher.stopTimer();
		dispatcher.startTimer();

		updater.stopTimer();
		updater.startTimer();

	}

	public void stop() {
		log.info("========== Stopping AMS service ===========");
		dispatcher.stopTimer();

		updater.stopTimer();
	}

}
