package rs.ac.bg.rcub.ams.config.hgsm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {

	private static Log log = LogFactory.getLog(Config.class);
	private static Properties props = new Properties();
	static final String HGSM_PROPS = "rs/ac/bg/rcub/ams/config/hgsm/hgsm.properties";

	static {
		try {
			initProps();
		} catch (Exception e) {
			log.error("HGSM config load error - " + e.getMessage());
		}
	}

	private static void initProps() throws Exception {
		log.info("Reading HGSMUpdater props file");

		URL url = Thread.currentThread().getContextClassLoader().getResource(HGSM_PROPS);
		if (url == null) {
			log.error("The configuration could not be found: " + HGSM_PROPS);
		} else {
			InputStream is = url.openStream();
			props.load(is);
			is.close();
		}

		if (trustAll())
			trustAllCerts();
	}

	// no instantiation
	private Config() {
	}

	public static String hgsmUrl() {
		return props.getProperty("url");
	}

	private static boolean trustAll() {
		return props.getProperty("TrustServerCerts", "0").equals("1");
	}

	private static void trustAllCerts() throws Exception {
		log.info("All server certs will be trusted now for SSL!");
		
		KeyStore ks = KeyStore.getInstance(System.getProperty("javax.net.ssl.keyStoreType"));
		char[] pass = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
		ks.load(new FileInputStream(System.getProperty("javax.net.ssl.keyStore")), pass);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SUNX509");
		kmf.init(ks, pass);

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(kmf.getKeyManagers(), new TrustManager[] { new FakeX509TrustManager() }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	public static class FakeX509TrustManager implements X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// Do nothing
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// Do nothing
		}
	}

}
