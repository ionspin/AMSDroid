package rs.ac.bg.rcub.ams.ejb3.service;

import org.jboss.ejb3.annotation.Management;

@Management
public interface AMSManagement {
	void create() throws Exception;

	void start() throws Exception;

	void stop();

	void destroy();
}
