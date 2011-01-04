package rs.ac.bg.rcub.auth.jsf.container;

import rs.ac.bg.rcub.auth.jsf.User;

public class ContainerUserProvider {
	static ContainerUser user = new ContainerUser();

	public static User getUser() {
		if (user.getLoginName() == null || user.getLoginName().equals("")) {
			return null;
		} else {
			return user;
		}
	}

}
