package rs.ac.bg.rcub.ams.ejb3.stateless;

import javax.ejb.Local;

@Local
public interface AdminManagement {

	public void createTarget(String name, String info);

	public void deleteTarget(int targetID);

	public void createCategory(String name, String info);

	public void deleteCategory(int catID);

}
