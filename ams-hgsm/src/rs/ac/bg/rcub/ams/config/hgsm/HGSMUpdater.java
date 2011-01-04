package rs.ac.bg.rcub.ams.config.hgsm;

import java.util.ArrayList;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.bg.rcub.ams.ejb3.stateless.AdminManagement;
import rs.ac.bg.rcub.ams.ejb3.stateless.config.Updater;

public class HGSMUpdater implements Updater {
	private static Log log = LogFactory.getLog(HGSMUpdater.class);

	private AdminManagement am;

	static final String ADMIN_BEAN = "ams/AdminManagementBean/local";

	private static InitialContext ctx;

	private String url = Config.hgsmUrl();
	private XMLReader reader;

	public HGSMUpdater() throws Exception {
		if (ctx == null)
			ctx = new InitialContext();
		am = (AdminManagement) ctx.lookup(ADMIN_BEAN);

		reader = new XMLReader(url);
	}

	public void update() {
		log.info("Updating configuration...");
		try {
			ArrayList<HGSMSite> sites = reader.readDoc();
			log.info("Total HGSM entries: " + sites.size());
			for (HGSMSite s : sites) {
				try {
					am.createTarget(s.getName(), s.getAffiliation());
				} catch (Exception e) {
					// ignore for now, maybe update existing entries?
					log.error(e, e);
				}
			}

		} catch (Exception e) {
			log.error(e, e);
		}
	}
}
