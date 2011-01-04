package rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.Depends;

import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.stateless.AlertServiceBean;
import rs.ac.bg.rcub.ams.ejb3.stateless.dispatcher.EmailDispatcher;
import rs.ac.bg.rcub.ams.util.Global;

@Stateless
public class EmailDispatcherBean implements EmailDispatcher {

	private static Log log = LogFactory.getLog(AlertServiceBean.class);

	@Resource(mappedName = "java:/AMS-Mail")
	@Depends("jboss:service=AMS-Mail")
	private Session session;

	public boolean dispatch(Message m) {
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(Global.email_id()));
			Address[] to = new InternetAddress[] { new InternetAddress(m.getSub().getContactDetail()) };
			msg.setRecipients(javax.mail.Message.RecipientType.TO, to);
			msg.setSubject(m.getAlerts().get(0).getSubject() + ", from: " + m.getAlerts().get(0).getSender().getName());
			Date now = new Date();
			msg.setSentDate(now);
			msg.setContent(m.toString(), "text/plain");

			// Store s = session.getStore();
			// s.connect(); // POP authentication
			Transport.send(msg);
			return true;
		} catch (MessagingException e) {
			log.error(e, e);
			return false;
		}
	}

}
