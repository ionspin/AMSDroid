package rs.ac.bg.rcub.ams.ejb3.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * Entity implementation class for Entity: UserInfo
 * 
 */
@Entity
@NamedQuery(name = "userinfo.find", query = "SELECT u FROM UserInfo u WHERE u.deleted = false AND u.principalName = :principalName AND u.role = :role")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	private String principalName;

	private String info;

	public enum Role {
		admin, sender, web_user
	}

	private Role role;

	private boolean deleted;

	public UserInfo() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return principalName;
	}

	public void setName(String name) {
		this.principalName = name;
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

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UserInfo:(");
		sb.append(principalName);
		sb.append(", ");
		sb.append(info);
		sb.append(", ");
		sb.append(role);
		sb.append(")");
		return sb.toString();
	}

}
