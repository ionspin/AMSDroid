package rs.ac.bg.rcub.ams.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * http://techieexchange.blogspot.com/2008/02/jsf-session-expiry-timeout-
 * solution.html
 * 
 * @author choppa
 * 
 */
public class SessionTimeoutFilter implements Filter {

	private static Log log = LogFactory.getLog(SessionTimeoutFilter.class);
	private String timeoutPage;

	public void init(FilterConfig filterConfig) throws ServletException {
		timeoutPage = filterConfig.getInitParameter("timeoutPage");
		if (timeoutPage == null)
			log.warn("Missing filter init parameter: 'timeoutPage'");
		else {
			log.info("Session filter initiated, timeoutPage : " + timeoutPage);
		}
	}

	public void destroy() {
		log.info("Session filter destroyed");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		log.info("Processing filter...");
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {

			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			// is session expire control required for this request?
			if (isSessionControlRequiredForThisResource(httpServletRequest)) {
				log.info("Session control required for this resource");
				// is session invalid?
				if (isSessionInvalid(httpServletRequest)) {
					String timeoutUrl = httpServletRequest.getContextPath() + "/" + getTimeoutPage();
					log.info("Session is invalid! redirecting to timeoutPage : " + timeoutUrl);
					httpServletResponse.sendRedirect(timeoutUrl);
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	/*
	 * session shouldn't be checked for some pages. For example: for timeout
	 * page.. Since we're redirecting to timeout page from this filter, if we
	 * don't disable session control for it, filter will again redirect to it
	 * and this will be result with an infinite loop...
	 */

	private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {

		String requestPath = httpServletRequest.getRequestURI();
		boolean controlRequired = !requestPath.contains(getTimeoutPage());
		return controlRequired;
	}

	private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {

		boolean sessionInValid = (httpServletRequest.getRequestedSessionId() != null)
				&& !httpServletRequest.isRequestedSessionIdValid();
		return sessionInValid;
	}

	public String getTimeoutPage() {
		return timeoutPage;
	}

	public void setTimeoutPage(String timeoutPage) {
		this.timeoutPage = timeoutPage;
	}

}
