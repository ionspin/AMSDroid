package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Entity implementation class for Entity: Category
 * 
 */
@Entity
@NamedQueries( { @NamedQuery(name = "category.find", query = "SELECT c FROM Category c WHERE c.deleted = false AND c.name = :name") })
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true, nullable = false)
	private String name;

	private String info;

	private boolean deleted = false;

	public Category() {
		super();
	}

	public int getId() {
		return this.id;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("C:(");
		sb.append(name);
		sb.append(", ");
		sb.append(info);
		sb.append(")");
		return sb.toString();
	}
}
