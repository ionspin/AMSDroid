package rs.ac.bg.rcub.ams.web.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

/**
 * 
 * @see http://wiki.apache.org/myfaces/Handling_Server_Errors
 * @see http://josephmarques.wordpress.com/2009/04/27/custom-jsf-exception-handling/
 * 
 * @author choppa
 *
 */
public class ErrorInfoBean {

	public String getInfoMessage() {
		return "An unexpected processing error has occurred. Please cut and paste the following information"
				+ " into an email and send it to <b>" + "the admin" + "</b>.";
	}

	public String getStackTrace() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestMap();
		Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");

		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		fillStackTrace(ex, pw);

		return writer.toString();
	}

	private void fillStackTrace(Throwable ex, PrintWriter pw) {
		if (null == ex) {
			return;
		}

		ex.printStackTrace(pw);

		if (ex instanceof ServletException) {
			Throwable cause = ((ServletException) ex).getRootCause();

			if (null != cause) {
				pw.println("Root Cause:");
				fillStackTrace(cause, pw);
			}
		} else {
			Throwable cause = ex.getCause();

			if (null != cause) {
				pw.println("Cause:");
				fillStackTrace(cause, pw);
			}
		}
	}

}
