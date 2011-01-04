package rs.ac.bg.rcub.auth.jboss.security;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.glite.security.SecurityContext;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

/**
 * Relies on glite-security-trustmanager to perform auth. Authz is performed by
 * reading roles from the properties file.
 * <p>
 * Configure auth in ejb.jar/META-INF/jboss.xml or @WebContext(authMethod =
 * "CLIENT-CERT")
 * <p>
 * Turn on SSL in tomcat/server.xml and use glite impl: <br>
 * sSLImplementation=
 * "org.glite.security.trustmanager.tomcat.TMSSLImplementation"
 * 
 * @author choppa
 * 
 */
public class GliteCertRolesLoginModule extends AbstractServerLoginModule {

	/** A principal derived from the certificate */
	private Principal identity;
	/** The client certificate */
	private X509Certificate credential;
	/** The trace level log flag */
	private boolean trace;

	/** The name of the default properties resource containing user/roles */
	private String defaultRolesRsrcName = "defaultRoles.properties";
	/**
	 * The name of the properties resource containing user/roles
	 */
	private String rolesRsrcName = "roles.properties";
	/**
	 * The roles.properties mappings
	 */
	private Properties roles;
	/**
	 * The character used to seperate the role group name from the username
	 * e.g., '.' in jduke.CallerPrincipal=...
	 */
	private char roleGroupSeperator = '.';

	private boolean defaultRoleEnabled = false;
	private String defaultRole = null;

	private void initOptions(Map<String, ?> options) {
		String option = (String) options.get("rolesProperties");
		if (option != null)
			rolesRsrcName = option;
		option = (String) options.get("defaultRolesProperties");
		if (option != null)
			defaultRolesRsrcName = option;
		option = (String) options.get("roleGroupSeperator");
		if (option != null)
			roleGroupSeperator = option.charAt(0);
		option = (String) options.get("defaultRoleEnabled");
		if (option != null && option.equalsIgnoreCase("true")) {
			option = (String) options.get("defaultRole");
			if (option != null && !option.equals("")) {
				defaultRoleEnabled = true;
				defaultRole = option;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * rs.ac.bg.rcub.auth.jboss.security.auth.spi.AbstractServerLoginModule#
	 * initialize (javax .security.auth.Subject,
	 * javax.security.auth.callback.CallbackHandler, java.util.Map,
	 * java.util.Map)
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		trace = log.isTraceEnabled();
		if (trace)
			log.trace("enter: initialize(Subject, CallbackHandler, Map, Map)");

		try {
			initOptions(options);

			if (trace)
				log.trace(String.format("roles=%s, default=%s, separator=%s, defaultRole=%s", rolesRsrcName,
						defaultRolesRsrcName, roleGroupSeperator, defaultRole));

			loadRolesCustom();

			// Load the properties file that contains the list of users and
			// passwords
			// loadRoles();
		} catch (Exception e) {
			// Note that although this exception isn't passed on, users or roles
			// will be null so that any call to login will throw a
			// LoginException.
			log.error("Failed to load users/passwords/role files", e);
		}

		if (trace)
			log.trace("exit: initialize(Subject, CallbackHandler, Map, Map)");
	}

	/**
	 * Relies on glite-security-trustmanager for auth.
	 */
	public boolean login() throws LoginException {
		if (trace)
			log.trace("enter: login()");

		if (roles == null)
			throw new LoginException("Missing roles properties file");
		super.loginOk = false;

		SecurityContext sc = SecurityContext.getCurrentContext();
		if (sc == null) {
			if (trace)
				log.trace("No security context found");
			throw new FailedLoginException("No security context found");
		}

		credential = sc.getClientCert();
		identity = sc.getClientX500Principal();

		super.loginOk = true;

		if (trace) {
			log.trace("User '" + identity + "' authenticated, loginOk=" + loginOk);
			log.trace("exit: login()");
		}
		return true;
	}

	public boolean commit() throws LoginException {
		boolean ok = super.commit();
		if (ok == true) {
			// Overwrite other credentials with this cert
			if (credential != null) {
				// subject.getPrincipals().clear();
				// subject.getPrincipals().add(identity);
				subject.getPublicCredentials().add(credential);
				// subject.setReadOnly();
			}

			if (trace) {
				log.trace(String.format("principals (total=%d): %s", subject.getPrincipals().size(), subject.getPrincipals()));
				log.trace("public credentials total: " + subject.getPublicCredentials().size());
			}

		}
		return ok;
	}

	protected Principal getIdentity() {
		return identity;
	}

	protected Object getCredentials() {
		return credential;
	}

	protected String getUsername() {
		String username = null;
		if (getIdentity() != null)
			username = getIdentity().getName();
		return username;
	}

	/**
	 * This method is pretty much straight from the UsersRolesLoginModule.
	 * 
	 * @see org.jboss.security.auth.spi.UsersRolesLoginModule#getRoleSets
	 */
	protected Group[] getRoleSets() throws LoginException {
		if (trace)
			log.trace("enter: getRoleSets()");
		String targetUser = getUsername();
		// Group[] roleSets = Util.getRoleSets(targetUser, roles,
		// roleGroupSeperator, this);
		Group[] roleSets = getRoleSets(targetUser, roles, roleGroupSeperator);
		if (trace)
			log.trace("exit: getRoleSets()");
		return roleSets;
	}

	// private void loadRoles() throws IOException {
	// log.trace("enter: loading roles");
	// roles = Util.loadProperties(defaultRolesRsrcName, rolesRsrcName, log);
	// log.trace("exit: loading roles");
	// }

	private void loadRolesCustom() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(rolesRsrcName);
		if (trace)
			log.trace("enter: reading roles");
		if (url == null) {
			log.error("Roles properties cant be found: " + rolesRsrcName);
		} else {
			InputStream is = url.openStream();
			roles = new Properties();
			roles.load(is);
			is.close();
			log.trace("Loaded users: " + roles.keySet());
		}
		if (trace)
			log.trace("exit: reading roles");

	}

	/**
	 * Create the set of roles the user belongs to by parsing the
	 * roles.properties data for username=role1,role2,... and
	 * username.XXX=role1,role2,... patterns.
	 * 
	 * @param targetUser
	 *            - the username to obtain roles for
	 * @param roles
	 *            - the Properties containing the user=roles mappings
	 * @param roleGroupSeperator
	 *            - the character that seperates a username from a group name,
	 *            e.g., targetUser[.GroupName]=roles
	 * @return Group[] containing the sets of roles
	 */
	Group[] getRoleSets(String targetUser, Properties roles, char roleGroupSeperator) {
		boolean trace = log.isTraceEnabled();
		Enumeration<?> users = roles.propertyNames();
		SimpleGroup rolesGroup = new SimpleGroup("Roles");
		ArrayList<Group> groups = new ArrayList<Group>();
		groups.add(rolesGroup);
		while (users.hasMoreElements() && targetUser != null) {
			String user = (String) users.nextElement();
			String value = roles.getProperty(user);
			if (trace)
				log.trace("Checking user: " + user + ", roles string: " + value);
			// See if this entry is of the form targetUser[.GroupName]=roles
			// JBAS-3742 - skip potential '.' in targetUser
			int index = user.indexOf(roleGroupSeperator, targetUser.length());
			boolean isRoleGroup = false;
			boolean userMatch = false;
			if (index > 0 && targetUser.regionMatches(0, user, 0, index) == true)
				isRoleGroup = true;
			else
				userMatch = targetUser.equals(user);

			// Check for username.RoleGroup pattern
			if (isRoleGroup == true) {
				String groupName = user.substring(index + 1);
				if (groupName.equals("Roles")) {
					if (trace)
						log.trace("Adding to Roles: " + value);
					parseGroupMembers(rolesGroup, value);
				} else {
					if (trace)
						log.trace("Adding to " + groupName + ": " + value);
					SimpleGroup group = new SimpleGroup(groupName);
					parseGroupMembers(group, value);
					groups.add(group);
				}
			} else if (userMatch == true) {
				if (trace)
					log.trace("Adding to Roles: " + value);
				// Place these roles into the Default "Roles" group
				parseGroupMembers(rolesGroup, value);
			}
		}
		// Add a default role if enabled & specified
		if (targetUser != null && defaultRoleEnabled) {
			if (trace)
				log.trace("Adding default role: " + defaultRole);
			parseGroupMembers(rolesGroup, defaultRole);
		}
		Group[] roleSets = new Group[groups.size()];
		groups.toArray(roleSets);
		return roleSets;
	}

	/**
	 * Parse the comma delimited roles names given by value and add them to
	 * group. The type of Principal created for each name is determined by the
	 * createIdentity method.
	 * 
	 * @see AbstractServerLoginModule#createIdentity(String)
	 * 
	 * @param group
	 *            - the Group to add the roles to.
	 * @param roles
	 *            - the comma delimited role names.
	 */
	void parseGroupMembers(Group group, String roles) {
		StringTokenizer tokenizer = new StringTokenizer(roles, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			try {
				Principal p = createIdentity(token);
				group.addMember(p);
			} catch (Exception e) {
				log.warn("Failed to create principal for: " + token, e);
			}
		}
	}

}
