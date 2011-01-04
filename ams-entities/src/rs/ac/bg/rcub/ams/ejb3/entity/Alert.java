package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: Alert
 * 
 */
@Entity
public class Alert implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	private UserInfo sender;

	@ManyToOne
	private Target target;

	@ManyToOne
	private Category category;

	@Column(nullable = false)
	private String subject;

	private String body;

	@Column(nullable = false)
	private boolean error = true;

	@Column(nullable = false)
	private Date arrived;

	private boolean dispatched = false;

	public Alert() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserInfo getSender() {
		return sender;
	}

	public void setSender(UserInfo userInfo) {
		this.sender = userInfo;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isError() {
		return error;
	}

	public Date getArrived() {
		return arrived;
	}

	public void setArrived(Date arrived) {
		this.arrived = arrived;
	}

	public boolean isDispatched() {
		return dispatched;
	}

	public void setDispatched(boolean dispatched) {
		this.dispatched = dispatched;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Alert: ");
		sb.append(id);
		sb.append("\n\tError: ");
		sb.append(error);
		sb.append("\n\tArrived: ");
		sb.append(arrived);
		sb.append("\n\tFrom: ");
		sb.append(sender.getInfo());
		sb.append("\n\tTo: ");
		sb.append(target);
		sb.append("\n\tCat: ");
		sb.append(category);
		sb.append("\n\tSubject: ");
		sb.append(subject);
		sb.append("\n\tBody:");
		sb.append("\n---------------------------------------------------------------------\n");
		sb.append(body);
		sb.append("\n---------------------------------------------------------------------\n");
		return sb.toString();
	}

}
