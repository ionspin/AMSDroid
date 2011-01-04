package rs.ac.bg.rcub.ams.ejb3.stateless;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.entity.Subscription;
import rs.ac.bg.rcub.ams.ejb3.entity.Target;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo.Role;
import rs.ac.bg.rcub.ams.util.enums.MessageType;

@Stateless
public class UserManagementBean implements UserManagement {

	private static Log log = LogFactory.getLog(UserManagementBean.class);

	@Resource
	private SessionContext ctx;

	@PersistenceContext(unitName = "ams")
	private EntityManager em;

	private UserInfo userInfo;

	public void addSub(String target, String category, String contact, long dispatchTimeout, int countThreshold,
			boolean ignoreIfLastOK, MessageType type) {
		Target t = (Target) em.createNamedQuery("target.find").setParameter("name", target).getSingleResult();
		Category c = null;
		if (category != null)
			c = (Category) em.createNamedQuery("category.find").setParameter("name", category).getSingleResult();

		Subscription s = new Subscription();
		s.setTarget(t);
		s.setCategory(c);
		s.setDispatchTimeout(dispatchTimeout);
		s.setCountThreshold(countThreshold);
		s.setIgnoreIfLastOK(ignoreIfLastOK);
		s.setType(type);
		s.setContactDetail(contact);
		s.setUser(userInfo);
		em.persist(s);
		log.info("Added new subscription: " + s);
	}

	public void removeSub(int subID) {
		Subscription s = em.find(Subscription.class, subID);
		s.setDeleted(true);
		log.info("Removed subscription: " + s);
	}

	public List<Subscription> listSubs() {
		List<Subscription> result = null;
		if (userInfo != null) {
			result = em.createQuery("SELECT s FROM Subscription s WHERE s.deleted = false AND s.user = :user").setParameter(
					"user", userInfo).getResultList();
			log.info("Total number of subs: " + result.size());
		} else {
			log.warn("No user credentials found, returning null");
		}
		return result;
	}

	public List<Target> listAllTargets() {
		List<Target> result = em.createQuery("SELECT t FROM Target t WHERE t.deleted = false ORDER BY t.name").getResultList();
		log.info("Total number of targets: " + result.size());
		return result;
	}

	public List<Category> listAllCategories() {
		List<Category> result = em.createQuery("SELECT c FROM Category c WHERE c.deleted = false ORDER BY c.name")
				.getResultList();
		log.info("Total number of categories: " + result.size());
		return result;
	}

	public List<UserInfo> listAllSenders() {
		List<UserInfo> result = em.createQuery(
				"SELECT s FROM UserInfo s WHERE s.deleted = false AND s.role = :role ORDER by s.principalName").setParameter(
				"role", Role.sender).getResultList();
		log.info("Total number of senders: " + result.size());
		return result;
	}

	public long totalAlerts() {
		long num = (Long) em.createQuery("SELECT COUNT(a) FROM Alert a").getSingleResult();
		log.info("Total number of alerts: " + num);
		return num;
	}

	public long totalAlerts(String sender, String target, String category, long timeOffset) {

		String whereClause = "";

		UserInfo sen = null;
		Target tar = null;
		Category cat = null;
		Date date = null;

		// Super ugly filter query

		if (sender != null) {
			whereClause += "a.sender = :sen ";
			sen = (UserInfo) em.createNamedQuery("userinfo.find").setParameter("principalName", sender).setParameter("role",
					Role.sender).getSingleResult();
		}
		if (target != null) {
			whereClause += whereClause.equals("") ? "a.target = :tar " : " AND a.target = :tar ";
			tar = (Target) em.createNamedQuery("target.find").setParameter("name", target).getSingleResult();
		}
		if (category != null) {
			whereClause += whereClause.equals("") ? "a.category = :cat " : " AND a.category = :cat ";
			cat = (Category) em.createNamedQuery("category.find").setParameter("name", category).getSingleResult();
		}
		if (timeOffset > 0) {
			whereClause += whereClause.equals("") ? "a.arrived >= :date " : " AND a.arrived >= :date ";
			date = new Date(System.currentTimeMillis() - timeOffset);
		}

		String queryPrefix = "SELECT COUNT(a) FROM Alert a ";
		String query = whereClause.equals("") ? queryPrefix : queryPrefix + "WHERE " + whereClause;
		log.info("Generated query: " + query);
		Query q = em.createQuery(query);
		if (sender != null)
			q.setParameter("sen", sen);
		if (target != null)
			q.setParameter("tar", tar);
		if (category != null)
			q.setParameter("cat", cat);
		if (timeOffset > 0)
			q.setParameter("date", date);

		long num = (Long) q.getSingleResult();
		log.info("Total number of alerts: " + num);
		return num;
	}

	public List<Alert> listAllAlerts() {
		List<Alert> alerts = em.createQuery("SELECT a FROM Alert a ORDER BY a.arrived").getResultList();
		log.info("Alerts returned by query: " + alerts.size());
		return alerts;
	}

	public List<Alert> listAlerts(int start, int offset, String sender, String target, String category, long timeOffset) {
		// id added to order by cause its unique to guarantee that all the rows
		// are properly returned on each page

		String whereClause = "";

		UserInfo sen = null;
		Target tar = null;
		Category cat = null;
		Date date = null;

		// Super ugly filter query

		if (sender != null) {
			whereClause += "a.sender = :sen ";
			sen = (UserInfo) em.createNamedQuery("userinfo.find").setParameter("principalName", sender).setParameter("role",
					Role.sender).getSingleResult();
		}
		if (target != null) {
			whereClause += whereClause.equals("") ? "a.target = :tar " : " AND a.target = :tar ";
			tar = (Target) em.createNamedQuery("target.find").setParameter("name", target).getSingleResult();
		}
		if (category != null) {
			whereClause += whereClause.equals("") ? "a.category = :cat " : " AND a.category = :cat ";
			cat = (Category) em.createNamedQuery("category.find").setParameter("name", category).getSingleResult();
		}
		if (timeOffset > 0) {
			whereClause += whereClause.equals("") ? "a.arrived >= :date " : " AND a.arrived >= :date ";
			date = new Date(System.currentTimeMillis() - timeOffset);
		}

		String queryPrefix = "SELECT a FROM Alert a ";
		String querySufix = " ORDER BY a.arrived, a.id";
		String query = whereClause.equals("") ? queryPrefix + querySufix : queryPrefix + "WHERE " + whereClause + querySufix;
		log.info("Generated query: " + query);
		Query q = em.createQuery(query);
		q.setFirstResult(start).setMaxResults(offset);
		if (sender != null)
			q.setParameter("sen", sen);
		if (target != null)
			q.setParameter("tar", tar);
		if (category != null)
			q.setParameter("cat", cat);
		if (timeOffset > 0)
			q.setParameter("date", date);

		List<Alert> alerts = q.getResultList();
		log.info("Alerts returned by query: " + alerts.size());
		return alerts;
	}

	public List<Alert> listAlerts(int start, int offset) {
		// id added cause its unique to guarantee that all the rows are properly
		// returned on each page
		Query q = em.createQuery("SELECT a FROM Alert a ORDER BY a.arrived, a.id").setFirstResult(start).setMaxResults(offset);
		List<Alert> alerts = q.getResultList();
		log.info("Alerts returned by query: " + alerts.size());
		return alerts;
	}

	public List<Message> listMessages(Alert a) {

		log.info("Connected: " + ctx.getCallerPrincipal().getName());

		// ORDER BY clause missing, so pagination might not work properly
		List<Message> messages;
		if (a == null) {
			log.info("Alert not specified, retrieving all messages!");
			messages = em.createQuery("SELECT m FROM Message m").getResultList();
		} else {
			log.info("Searching messages for Alert: " + a.getId());
			messages = em.createQuery("SELECT DISTINCT m FROM Message m JOIN FETCH m.alerts a WHERE :alert MEMBER OF m.alerts")
					.setParameter("alert", a).getResultList();
		}
		log.info("Messages returned by query: " + messages.size());
		return messages;
	}

	@AroundInvoke
	public Object initUser(InvocationContext inctx) throws Exception {
		String principalName = ctx.getCallerPrincipal().getName();
		if (principalName == null || principalName.equals("")) {
			log.warn("No principal found, unauthenticated user?");
			// reject or proceed?
			return inctx.proceed();
		}

		log.info("Connected as: " + principalName);

		Query q = em.createNamedQuery("userinfo.find");
		q.setParameter("principalName", principalName);
		// just web-user?
		q.setParameter("role", Role.web_user);
		try {
			userInfo = (UserInfo) q.getSingleResult();
			// Update info as well?
		} catch (NoResultException e) {
			log.info("User principal '" + principalName + "' not found, storing it...");
			userInfo = new UserInfo();
			userInfo.setName(principalName);
			// userInfo.setInfo("");
			userInfo.setRole(Role.web_user);
			em.persist(userInfo);
		}
		return inctx.proceed();
	}

}
