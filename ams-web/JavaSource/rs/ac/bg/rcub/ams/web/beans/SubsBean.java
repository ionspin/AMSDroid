package rs.ac.bg.rcub.ams.web.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import rs.ac.bg.rcub.ams.ejb3.entity.Subscription;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;
import rs.ac.bg.rcub.ams.util.enums.MessageType;
import rs.ac.bg.rcub.ams.web.util.ItemLists;

public class SubsBean {

	@EJB
	private UserManagement um;

	private MessageType type;

	private String contactDetail;

	private String target;

	private String category;

	private long dispatchTimeout = 0;

	private int countThreshold = 1;

	private boolean ignoreIfLastOK = true;

	// public static final String ANY_CAT = "=Any category=";

	private DataModel model;

	private boolean displaySubs;

	public SubsBean() {
	}

	public DataModel getUserSubs() {
		// caching
		if (model == null)
			loadData();

		return model;
	}

	private void loadData() {
		List<Subscription> subs = um.listSubs();
		displaySubs = subs != null;
		model = new ListDataModel(subs);
	}

	public boolean isDisplaySubs() {
		return displaySubs;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}

	public long getDispatchTimeout() {
		return dispatchTimeout;
	}

	public void setDispatchTimeout(long dispatchTimeout) {
		this.dispatchTimeout = dispatchTimeout;
	}

	public int getCountThreshold() {
		return countThreshold;
	}

	public void setCountThreshold(int countThreshold) {
		this.countThreshold = countThreshold;
	}

	public boolean isIgnoreIfLastOK() {
		return ignoreIfLastOK;
	}

	public void setIgnoreIfLastOK(boolean ignoreIfLastOK) {
		this.ignoreIfLastOK = ignoreIfLastOK;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void subscribe() {
		if (category.equals(ItemLists.ANY_CAT))
			um.addSub(target, null, contactDetail, 1000 * dispatchTimeout, countThreshold, ignoreIfLastOK, type);
		else
			um.addSub(target, category, contactDetail, 1000 * dispatchTimeout, countThreshold, ignoreIfLastOK, type);
	}

	public void remove() {
		Subscription s = (Subscription) model.getRowData();
		if (s != null) {
			um.removeSub(s.getId());
			// reload data
			loadData();
		}
	}

	public List<SelectItem> getAllTargets() {
		return ItemLists.getAllTargets(um, false);
	}

	public List<SelectItem> getAllCategories() {
		return ItemLists.getAllCategories(um, true);
	}

	public List<SelectItem> getMessageTypes() {
		return ItemLists.getMessageTypes(um, false);
	}

}
