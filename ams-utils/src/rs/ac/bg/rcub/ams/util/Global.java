package rs.ac.bg.rcub.ams.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Global {

	private static Log log = LogFactory.getLog(Global.class);
	private static Properties props = new Properties();
	static final String APP_PROPS = "conf/props/app.properties";

	public static final String DEFAULT_CATEGORY = "ALL";

	static {
		try {
			initStatic();
		} catch (Exception e) {
			log.error("Config load error - " + e.getMessage());
		}
	}

	public static final long DISPATCH_INTERVAL = Long.parseLong(props.getProperty("dispatch_interval"));
	public static final long CONFIG_UPDATE_INTERVAL = Long.parseLong(props.getProperty("config_update_interval"));
	public static final int CONFIG_UPDATE_ONLY_ONCE = Integer.parseInt(props.getProperty("config_update_only_once", "0"));

	public static final int MAX_RETRY_COUNT = Integer.parseInt(props.getProperty("max_retry_count", "10"));

	private static void initStatic() throws Exception {
		log.info("Reading AMS config");

		URL url = Thread.currentThread().getContextClassLoader().getResource(APP_PROPS);
		if (url == null) {
			log.error("The configuration could not be found: " + APP_PROPS);
		} else {
			InputStream is = url.openStream();
			props.load(is);
			is.close();
		}
	}

	// no instantiation
	private Global() {
	}

	public static String gtalk_user() {
		return props.getProperty("gtalk_user");
	}

	public static String gtalk_pass() {
		return props.getProperty("gtalk_pass");
	}

	public static String email_id() {
		return props.getProperty("email_id");
	}

	public static String config_update_class() {
		return props.getProperty("config_update_class");
	}

}
