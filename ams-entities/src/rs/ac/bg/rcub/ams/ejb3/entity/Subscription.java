package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import rs.ac.bg.rcub.ams.util.enums.MessageType;

/**
 * Entity implementation class for Entity: Subscription
 * 
 * TODO separate tar/cat from contact details
 * 
 */
@Entity
@NamedQueries( { @NamedQuery(name = "subs.getSubs", query = "SELECT s FROM Subscription s WHERE s.deleted = false AND s.target = :target AND s.category = :category") })
public class Subscription implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private MessageType type;

	@Column(nullable = false)
	private String contactDetail;

	// private String rules;

	@Column(nullable = false)
	private long dispatchTimeout = 0;

	@Column(nullable = false)
	private int countThreshold = 1;

	@Column(nullable = false)
	private boolean ignoreIfLastOK = false;

	@ManyToOne(cascade = { CascadeType.REMOVE })
	private Target target;

	@ManyToOne(cascade = { CascadeType.REMOVE })
	private Category category;

	@ManyToOne(cascade = { CascadeType.REMOVE })
	private UserInfo user;

	private boolean deleted = false;

	public Subscription() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setTarget(Target target) {
		this.target = target;
	}

	public Target getTarget() {
		return target;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public UserInfo getUser() {
		return user;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(type);
		sb.append(", ");
		sb.append(contactDetail);
		sb.append(", user=");
		sb.append(user);
		sb.append(", threshold=");
		sb.append(countThreshold);
		sb.append(", timeout=");
		sb.append(dispatchTimeout);
		sb.append(", ignore=");
		sb.append(ignoreIfLastOK);
		sb.append(", ");
		sb.append(target);
		sb.append(", ");
		sb.append(category);
		sb.append(")");
		return sb.toString();
	}

}
