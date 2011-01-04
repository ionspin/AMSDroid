package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Entity implementation class for Entity: Target
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "target.find", query = "SELECT t FROM Target t WHERE t.deleted = false AND t.name = :name"),
		@NamedQuery(name = "target.getSubs", query = "SELECT s FROM Target t, Subscription s WHERE t.name = :name AND s.target = :target") })
public class Target implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true, nullable = false)
	private String name;

	private String info;

	private boolean deleted = false;

	public Target() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("T:(");
		sb.append(name);
		sb.append(", ");
		sb.append(info);
		sb.append(")");
		return sb.toString();
	}

}
