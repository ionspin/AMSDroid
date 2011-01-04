package rs.ac.bg.rcub.ams.web.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.entity.Message;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;

public class ListMessagesBean {
	@EJB
	private UserManagement um;

	private List<Message> model;

	private Alert selectedAlert;

	public List<Message> getMessages() {
		Alert a = getAlert();
		model = um.listMessages(a);
		return model;
	}

	public Alert getAlert() {
		// caching, test if it works
		if (selectedAlert == null) {
			FacesContext fcontext = FacesContext.getCurrentInstance();
			ELResolver r = fcontext.getApplication().getELResolver();
			ListAlertsBean listAlertsBean = (ListAlertsBean) r.getValue(fcontext.getELContext(), null, "listAlertsBean");
			selectedAlert = listAlertsBean.getSelectedAlert();
			// selectedAlert = (Alert) listAlertsBean.getAlerts().getRowData();
		}
		return selectedAlert;
	}

	public String getInfo() {
		Alert a = getAlert();
		StringBuilder sb = new StringBuilder();
		sb.append(a == null ? "Listing of all messages " : "Listing of all messages for alert: " + a.getId());
		sb.append(" (total=");
		sb.append(model.size());
		sb.append(")");
		return sb.toString();
	}

	public int getRowCount() {
		// return model.getRowCount() > 0 ? model.getRowCount() : 0;
		return model.size();
	}
}
