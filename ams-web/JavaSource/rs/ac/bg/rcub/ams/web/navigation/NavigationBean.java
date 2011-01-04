package rs.ac.bg.rcub.ams.web.navigation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Failed :(
 * 
 * @author choppa
 *
 */
public class NavigationBean {

	public static final String ALERTS_PAGE = "listAlerts.jsp";
	public static final String MESSAGES_PAGE = "messages.jsp";
	public static final String SUBSCRIBE_PAGE = "subscribe.jsp";
	public static final String CATEGORY_PAGE = "admin/addCategory.jsp";
	public static final String TARGET_PAGE = "admin/addTarget.jsp";

	public static final Set<String> EXLUDED_PAGES = new HashSet<String>(Arrays.asList("toc.jsp"));

	private String currentPage = ALERTS_PAGE;

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public String toString() {
		return "currentPage=" + currentPage;
	}
}
