package rs.ac.bg.rcub.ams.ejb3.stateless;

import java.rmi.Remote;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface AlertService extends Remote {

	public static final String CLASS_NAME = "rs.ac.bg.rcub.ams.ejb3.stateless.AlertService";

	@WebMethod
	public boolean sendAlert(@WebParam(name = "sender") String sender, @WebParam(name = "target") String target,
			@WebParam(name = "category") String category, @WebParam(name = "subject") String subject,
			@WebParam(name = "body") String body, @WebParam(name = "isError") boolean isError);

}
