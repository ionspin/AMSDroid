package rs.ac.bg.rcub.auth.jsf;


public interface User {
	public String getAuthType();

	public String getLoginName();

	public boolean isUserInRole(String roleName);

}
