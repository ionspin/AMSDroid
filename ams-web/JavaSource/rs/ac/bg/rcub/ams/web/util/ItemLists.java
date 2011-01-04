package rs.ac.bg.rcub.ams.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import rs.ac.bg.rcub.ams.ejb3.entity.Category;
import rs.ac.bg.rcub.ams.ejb3.entity.UserInfo;
import rs.ac.bg.rcub.ams.ejb3.entity.Target;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;
import rs.ac.bg.rcub.ams.util.enums.MessageType;

public class ItemLists {

	public static final String ANY_CAT = "=Any category=";
	public static final String ANY_TAR = "=Any target=";
	public static final String ANY_SEN = "=Any sender=";
	public static final String ANY_MESS_TYPE = "=Any message type=";

	public enum DateFilter {

		ANY_DATE(0), TODAY(1000 * 3600 * 24), LAST_7_DAYS(TODAY.value * 7), LAST_30_DAYS(TODAY.value * 30), LAST_YEAR(
				TODAY.value * 365);
		private final long value;

		DateFilter(long value) {
			this.value = value;
		}

		public long toLong() {
			return value;
		}
	}

	public static List<SelectItem> getAllTargets(UserManagement um, boolean allowAny) {

		List<Target> list = um.listAllTargets();
		// size+1 if allowAny=true
		List<SelectItem> allTargets = new ArrayList<SelectItem>(list.size());
		if (allowAny)
			allTargets.add(new SelectItem(ANY_TAR));
		for (Target t : list)
			allTargets.add(new SelectItem(t.getName()));

		return allTargets;
	}

	public static List<SelectItem> getAllCategories(UserManagement um, boolean allowAny) {

		List<Category> list = um.listAllCategories();
		List<SelectItem> allCategories = new ArrayList<SelectItem>(list.size());
		if (allowAny)
			allCategories.add(new SelectItem(ANY_CAT));
		for (Category c : list)
			allCategories.add(new SelectItem(c.getName()));

		return allCategories;
	}

	public static List<SelectItem> getAllSenders(UserManagement um, boolean allowAny) {

		List<UserInfo> list = um.listAllSenders();
		List<SelectItem> allSenders = new ArrayList<SelectItem>(list.size());
		if (allowAny)
			allSenders.add(new SelectItem(ANY_SEN));
		for (UserInfo s : list)
			allSenders.add(new SelectItem(s.getName()));

		return allSenders;
	}

	public static List<SelectItem> getMessageTypes(UserManagement um, boolean allowAny) {
		List<SelectItem> typeOptions = new ArrayList<SelectItem>();
		if (allowAny)
			typeOptions.add(new SelectItem(ANY_MESS_TYPE));
		for (MessageType type : MessageType.values())
			typeOptions.add(new SelectItem(type));

		return typeOptions;
	}

	public static List<SelectItem> getAllDates(boolean allowAny) {

		List<SelectItem> allDates = new ArrayList<SelectItem>();
		// if (allowAny)
		// allDates.add(new SelectItem(ANY_DATE));
		// allDates.add(new SelectItem("Today"));
		// allDates.add(new SelectItem("Last 7 days"));
		// allDates.add(new SelectItem("Last 30 days"));
		// allDates.add(new SelectItem("Last year"));

		for (DateFilter filter : DateFilter.values())
			// (a and b) or not a = (a or not a) and (not a or b)
			if (allowAny || filter != DateFilter.ANY_DATE)
				allDates.add(new SelectItem(filter));

		return allDates;
	}

}
