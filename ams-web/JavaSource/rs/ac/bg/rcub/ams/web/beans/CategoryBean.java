package rs.ac.bg.rcub.ams.web.beans;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.stateless.AdminManagement;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;

public class CategoryBean {

	@EJB
	private AdminManagement admin;

	@EJB
	private UserManagement um;

	private String name;
	private String info;

	private DataModel model;

	public DataModel getAllCategories() {
		// caching
		if (model == null)
			loadData();

		return model;
	}

	private void loadData() {
		model = new ListDataModel(um.listAllCategories());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void add() {
		admin.createCategory(name, info);
		// reload data
		loadData();
	}

	public void edit() {
		Category c = (Category) model.getRowData();
		// maybe use object id to modify?
		if (c != null) {
			name = c.getName();
			info = c.getInfo();
		}
	}

	public void delete() {
		Category c = (Category) model.getRowData();
		if (c != null) {
			admin.deleteCategory(c.getId());
			// reload data
			loadData();
		}
	}

}
