package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import rs.ac.bg.rcub.ams.util.enums.MessageStatus;

/**
 * Entity implementation class for Entity: Message
 * 
 */
@Entity
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	private Subscription sub;

	@ManyToMany(cascade = { CascadeType.REMOVE })
	private List<Alert> alerts;

	@Column(nullable = false)
	private int retryCount = 0;

	private MessageStatus status = MessageStatus.SCHEDULED;

	public Message() {
		super();
		this.alerts = new ArrayList<Alert>();
	}

	public int getId() {
		return this.id;
	}

	public Subscription getSub() {
		return sub;
	}

	public void setSub(Subscription sub) {
		this.sub = sub;
	}

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		StringBuilder sb = new StringBuilder();
		if (alerts.size() == 1) {
			sb.append("\n");
			sb.append(alerts.get(0).getBody());
		} else {
			for (Alert a : alerts) {
				sb.append("\n\tAlert: ");
				sb.append(a.getId());
				sb.append(", arrived: ");
				sb.append(a.getArrived());
				sb.append("\n");
				sb.append(a.getBody());
			}
		}
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message: ");
		sb.append(id);
		sb.append(", status: ");
		sb.append(status);
		sb.append(", retryCount: ");
		sb.append(retryCount);
		sb.append("\n\tFrom: ");
		sb.append(alerts.get(0).getSender().getInfo());
		sb.append("\n\tTo: ");
		sb.append(sub);
		sb.append("\n\tSubject: ");
		sb.append(alerts.get(0).getSubject());
		sb.append("\n\tTotal alerts: ");
		sb.append(alerts.size());
		sb.append("\n\tBody:");
		sb.append("\n---------------------------------------------------------------------");
		sb.append(getBody());
		sb.append("\n---------------------------------------------------------------------\n");

		return sb.toString();
	}

}
