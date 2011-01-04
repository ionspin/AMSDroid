package rs.ac.bg.rcub.ams.ejb3.stateless;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo;
import rs.ac.bg.rcub.ams.ejb3.entity.Subscription;
import rs.ac.bg.rcub.ams.ejb3.entity.Target;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo.Role;
import rs.ac.bg.rcub.ams.util.Global;
import rs.ac.bg.rcub.ams.util.enums.MessageStatus;

@Stateless
@WebService(endpointInterface = AlertService.CLASS_NAME, serviceName = "AlertService")
@Remote(AlertService.class)
@SecurityDomain("ams")
@RolesAllowed("sender")
// @WebContext(contextRoot = "/ams", urlPattern = "/*", authMethod =
// "CLIENT-CERT", transportGuarantee = "NONE", secureWSDLAccess = false)
public class AlertServiceBean implements AlertService {

	private static Log log = LogFactory.getLog(AlertServiceBean.class);

	@PersistenceContext(unitName = "ams")
	private EntityManager em;

	@Resource
	private SessionContext ctx;

	private UserInfo userInfo = null;

	public boolean sendAlert(String sender, String target, String category, String subject, String body, boolean isError) {
		Alert a = new Alert();

		a.setSender(userInfo);
		Target t = (Target) em.createNamedQuery("target.find").setParameter("name", target).getSingleResult();
		if (category == null) {
			category = Global.DEFAULT_CATEGORY;
			log.info("Category not specified, using default: " + category);
		}
		Category cat = (Category) em.createNamedQuery("category.find").setParameter("name", category).getSingleResult();
		a.setTarget(t);
		a.setCategory(cat);
		a.setSubject(subject);
		a.setArrived(new Date());
		a.setBody(body);
		a.setError(isError);
		em.persist(a); // maybe not needed?

		int newMessages = 0;
		List<Subscription> subs = findSubs(t, cat);
		for (Subscription sub : subs) {
			Message m = existingMessage(a, sub);
			if (m == null) {
				if (isError) {
					m = new Message();
					m.setSub(sub);
					// m.setStatus(MessageStatus.SCHEDULED);
					m.getAlerts().add(a);
					em.persist(m);
					newMessages++;
				} else {
					log.info("Incoming alert is NOT an error alert and no scheduled messages found, IGNORED, ID=" + a.getId());
				}
			} else {
				m.getAlerts().add(a);
			}
		}

		if (subs.size() == 0)
			log.warn("No subscriptions found for: " + t + ", " + cat);
		StringBuilder sb = new StringBuilder();
		sb.append("\n============= Received new alert ===============\n");
		sb.append(a);
		sb.append("\nNumber of generated messages: ");
		sb.append(newMessages);
		sb.append("\n============= Finished alert ===================\n");
		log.info(sb.toString());

		return true;
	}

	private List<Subscription> findSubs(Target target, Category cat) {
		List<Subscription> results = em.createNamedQuery("subs.getSubs").setParameter("target", target).setParameter(
				"category", cat).getResultList();
		log.info("Found a total of full matching subs: " + results.size());

		List<Subscription> nullCatResults = em.createQuery(
				"SELECT s FROM Subscription s WHERE s.deleted = false AND s.target = :target AND s.category IS NULL")
				.setParameter("target", target).getResultList();
		log.info("Found a total of matching subs with no category specified: " + nullCatResults.size());

		for (Subscription nullSub : nullCatResults) {
			boolean found = false;
			for (Subscription sub : results)
				if (nullSub.getTarget() == sub.getTarget() && nullSub.getType() == sub.getType()
						&& nullSub.getContactDetail().equals(sub.getContactDetail())) {
					found = true;
					break;
				}
			if (!found) {
				results.add(nullSub);
				log.info("Added matching subscription with no category specified: " + nullSub);
			}
		}
		return results;
	}

	private Message existingMessage(Alert a, Subscription s) {
		Query q = em.createQuery("SELECT m FROM Message m JOIN m.alerts a WHERE m.status = :status AND m.sub = :sub"
				+ " AND a.sender = :sen AND a.target = :target AND a.subject = :subject ");
		q.setParameter("status", MessageStatus.SCHEDULED);
		q.setParameter("sub", s);
		q.setParameter("sen", a.getSender());
		q.setParameter("target", a.getTarget());
		q.setParameter("subject", a.getSubject());

		try {
			Message m = (Message) q.getSingleResult();
			log.info("Found an already scheduled message: " + m);
			return m;
		} catch (NoResultException e) {
			log.info("No scheduled messages for this alert found.");
			return null;
		}
	}

	@AroundInvoke
	public Object initSender(InvocationContext inctx) throws Exception {
		String username = ctx.getCallerPrincipal().getName();
		String senderName = (String) (inctx.getParameters()[0]);
		log.info("Connected with username '" + username + "' as " + senderName);

		// SecurityContext sc = SecurityContext.getCurrentContext();
		// if (sc != null) {
		// log.info(sc.getClientName());
		// log.info(sc.getClientCert());
		// }

		Query q = em.createNamedQuery("userinfo.find");
		q.setParameter("principalName", username);
		q.setParameter("role", Role.sender);
		try {
			userInfo = (UserInfo) q.getSingleResult();
			// Update info as well?
		} catch (NoResultException e) {
			log.info("User principal '" + username + "' not found, storing it...");
			userInfo = new UserInfo();
			userInfo.setName(username);
			userInfo.setInfo(senderName);
			userInfo.setRole(Role.sender);
			em.persist(userInfo);
		}
		return inctx.proceed();
	}

}
