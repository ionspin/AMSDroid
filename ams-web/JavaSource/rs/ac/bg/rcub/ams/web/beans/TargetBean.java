package rs.ac.bg.rcub.ams.web.beans;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import rs.ac.bg.rcub.ams.ejb3.entity.Target;
import rs.ac.bg.rcub.ams.ejb3.stateless.AdminManagement;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;

public class TargetBean {

	@EJB
	private AdminManagement admin;

	private String name;
	private String info;

	@EJB
	private UserManagement um;

	private DataModel model;

	public DataModel getAllTargets() {
		// caching
		if (model == null)
			loadData();

		return model;
	}

	private void loadData() {
		model = new ListDataModel(um.listAllTargets());
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
		admin.createTarget(name, info);
		// reload data
		loadData();
	}

	public void edit() {
		Target t = (Target) model.getRowData();
		// maybe use object id to modify?
		if (t != null) {
			name = t.getName();
			info = t.getInfo();
		}
	}

	public void delete() {
		Target t = (Target) model.getRowData();
		if (t != null) {
			admin.deleteTarget(t.getId());
			// reload data
			loadData();
		}
	}

}
