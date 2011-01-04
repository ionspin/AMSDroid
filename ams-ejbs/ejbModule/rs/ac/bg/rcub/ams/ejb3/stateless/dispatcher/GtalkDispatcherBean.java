package rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.util.Global;

@Stateless
public class GtalkDispatcherBean implements GtalkDispatcher {

	private static Log log = LogFactory.getLog(GtalkDispatcherBean.class);

	private static XMPPConnection connection = null;

	private void initConn() throws XMPPException {
		if (connection == null || !connection.isConnected() || !connection.isAuthenticated()) {

			// maybe not needed?
			if (connection != null)
				connection.disconnect();

			ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
			connection = new XMPPConnection(connConfig);
			connection.connect();
			// login with username and password
			connection.login(Global.gtalk_user(), Global.gtalk_pass());

			// set presence status info
			// Presence presence = new Presence(Presence.Type.available);
			// connection.sendPacket(presence);
		}
	}

	public boolean dispatch(Message m) {

		try {
			initConn();

			final String contact = m.getSub().getContactDetail();
			Roster man = connection.getRoster();
			RosterEntry e = man.getEntry(contact);
			if (e == null || e.getType() != ItemType.both) {
				if (e != null)
					man.removeEntry(e);
				try {
					man.createEntry(contact, contact, null);
				} catch (XMPPException xe) {
					log.warn("Cant add new entry, perhaps contact doesnt exist: " + xe.getMessage());
				}
				// return without sending
				log.warn("Contact not authorized: " + m.getSub());
				return false;
			}

			org.jivesoftware.smack.packet.Message msg = new org.jivesoftware.smack.packet.Message(contact,
					org.jivesoftware.smack.packet.Message.Type.headline);

			msg.setSubject(m.getAlerts().get(0).getSubject() + ", from: " + m.getAlerts().get(0).getSender().getName());
			msg.setBody(m.toString());
			// msg.setFrom(m.getSender());

			connection.sendPacket(msg);
			return true;
		} catch (XMPPException e) {
			log.error(e, e);
			// connection.disconnect();
			return false;
		}

	}

}
