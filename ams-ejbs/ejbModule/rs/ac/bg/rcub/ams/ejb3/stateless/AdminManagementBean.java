package rs.ac.bg.rcub.ams.ejb3.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.entity.Target;

@Stateless
public class AdminManagementBean implements AdminManagement {

	private static Log log = LogFactory.getLog(AdminManagementBean.class);

	@PersistenceContext(unitName = "ams")
	private EntityManager em;

	public void createTarget(String name, String info) {
		// em.merge(t);
		// log.info("Merger target: " + t);

		try {
			Target t = (Target) em.createQuery("SELECT t FROM Target t WHERE t.name = :name").setParameter("name", name)
					.getSingleResult();
			t.setInfo(info);
			t.setDeleted(false);
			log.info("Updated target: " + t);
		} catch (NoResultException e) {
			Target t = new Target();
			t.setName(name);
			t.setInfo(info);
			em.persist(t);
			log.info("Added new target: " + t);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void deleteTarget(int targetID) {
		Target t = em.find(Target.class, targetID);
		// em.remove(t);
		t.setDeleted(true);
		log.info("Deleted target: " + t);

		// int n =
		// em.createQuery("DELETE FROM Target t WHERE t.name = :name").setParameter("name",
		// name).executeUpdate();
		// log.info("Deleted target '" + name + "', rows affected " + n);
	}

	public void createCategory(String name, String info) {
		// em.merge(c);
		// log.info("Merged category: " + c);

		try {

			Category c = (Category) em.createQuery("SELECT c FROM Category c WHERE c.name = :name").setParameter("name", name)
					.getSingleResult();
			c.setInfo(info);
			c.setDeleted(false);
			log.info("Updated category: " + c);
		} catch (NoResultException e) {
			Category c = new Category();
			c.setName(name);
			c.setInfo(info);
			em.persist(c);
			log.info("Added new category: " + c);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void deleteCategory(int catID) {
		Category c = em.find(Category.class, catID);
		// em.remove(c);
		c.setDeleted(true);
		log.info("Deleted category: " + c);

		// int n =
		// em.createQuery("DELETE FROM Category c WHERE c.name = :name").setParameter("name",
		// name).executeUpdate();
		// log.info("Deleted category '" + name + "', rows affected " + n);
	}
}
