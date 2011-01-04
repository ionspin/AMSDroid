package rs.ac.bg.rcub.ams.web.beans;

import rs.ac.bg.rcub.auth.jsf.User;
import rs.ac.bg.rcub.auth.jsf.container.ContainerUserProvider;

public class UserInfoBean {

	private User user = ContainerUserProvider.getUser();

	public UserInfoBean() {

	}

	public String getLoginName() {
		if (user != null)
			return user.getLoginName();
		else
			return "UNKNOWN";
	}

	public String getAuthType() {
		if (user != null)
			return user.getAuthType();
		else
			return "UNKNOWN";
	}

}
