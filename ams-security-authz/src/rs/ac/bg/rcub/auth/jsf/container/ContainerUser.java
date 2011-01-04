package rs.ac.bg.rcub.auth.jsf.container;

import java.security.Principal;

import javax.faces.context.FacesContext;

import rs.ac.bg.rcub.auth.jsf.User;

class ContainerUser implements User {

	public String getAuthType() {
		return FacesContext.getCurrentInstance().getExternalContext().getAuthType();
	}

	public String getLoginName() {
		Principal p = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		if (p == null)
			return null;
		else
			return p.toString();
	}

	public boolean isUserInRole(String roleName) {
		return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(roleName);
	}

}
