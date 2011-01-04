package rs.ac.bg.rcub.ams.ejb3.stateless.config;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.bg.rcub.ams.util.Global;

@Stateless
public class ConfigUpdaterBean implements ConfigUpdater {

	private static Log log = LogFactory.getLog(ConfigUpdaterBean.class);

	@Resource
	TimerService timerService;

	@Timeout
	public void timeoutHandler(Timer timer) {
		log.info("Entered timer task: " + timer.getInfo());
		String className = Global.config_update_class();
		if (className == null) {
			log.warn("No config update class defined!");
			return;
		}
		try {
			// Maybe use just one instance?
			Updater u = (Updater) Class.forName(className).newInstance();
			u.update();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public void startTimer() {
		String name = "ams-config-timer-" + String.valueOf((int) ((Math.random() * 1000)));
		long interval = Global.CONFIG_UPDATE_INTERVAL;
		boolean update_once = Global.CONFIG_UPDATE_ONLY_ONCE == 1;

		if (interval > 0) {
			if (update_once) {
				timerService.createTimer(interval, name);
				log.info("Starting timer: " + name + ", one-time only config update, timeout = " + interval);
			} else {
				timerService.createTimer(interval, interval, name);
				log.info("Starting timer: " + name + ", interval = " + interval);
			}
		} else {
			log.info("Update interval not specified, config update is disabled.");
		}
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

}
