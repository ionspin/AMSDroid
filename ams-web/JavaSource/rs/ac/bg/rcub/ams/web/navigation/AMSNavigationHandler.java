package rs.ac.bg.rcub.ams.web.navigation;

import javax.el.ELResolver;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Failed :(
 * 
 * @author choppa
 *
 */
public class AMSNavigationHandler extends NavigationHandler {

	private static Log log = LogFactory.getLog(AMSNavigationHandler.class);

	private NavigationHandler _base;

	public AMSNavigationHandler(NavigationHandler base) {
		super();
		_base = base;
	}

	@Override
	public void handleNavigation(FacesContext context, String fromAction, String outcome) {

		log.info("fromAction=" + fromAction);
		log.info("outcome=" + outcome);
		log.info("view=" + context.getViewRoot().getViewId());
		ELResolver r = context.getApplication().getELResolver();
		NavigationBean navBean = (NavigationBean) r.getValue(context.getELContext(), null, "navigationBean");
		log.info(navBean);

		if (outcome != null && !NavigationBean.EXLUDED_PAGES.contains(outcome)) {
			if (navBean.getCurrentPage().equals(outcome)) {
				log.info("Refresh occured.");
				r.setValue(context.getELContext(), null, "navigationBean", null);
			} else {
				log.info("Normal navigation occured.");
				navBean.setCurrentPage(outcome);
			}
		}

		_base.handleNavigation(context, fromAction, outcome);
	}

}
