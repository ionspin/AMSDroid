package rs.ac.bg.rcub.ams.web.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import rs.ac.bg.rcub.ams.ejb3.entity.Alert;
import rs.ac.bg.rcub.ams.ejb3.stateless.UserManagement;
import rs.ac.bg.rcub.ams.web.util.ItemLists;
import rs.ac.bg.rcub.ams.web.util.ItemLists.DateFilter;

public class ListAlertsBean {

	@EJB
	private UserManagement um;

	private DataModel model;

	private Alert selectedAlert;

	private String filterSender = null;
	private String filterTarget = null;
	private String filterCategory = null;
	private DateFilter filterDate = DateFilter.ANY_DATE;

	private int totalRows;
	private int firstRow = 0;
	private int rowsPerPage = 10;
	private int currentPage = 0;
	private int totalPages;

	public DataModel getAlerts() {
		// caching, test if it works
		if (model == null)
			// model = new ListDataModel(um.listAllAlerts());
			loadData();

		return model;
	}

	private void loadData() {

		// Load list and totalCount.
		model = new ListDataModel(um.listAlerts(firstRow, rowsPerPage, filterSender, filterTarget, filterCategory, filterDate
				.toLong()));
		totalRows = (int) um.totalAlerts(filterSender, filterTarget, filterCategory, filterDate.toLong());

		// Set currentPage, totalPages and pages.
		currentPage = firstRow / rowsPerPage + 1;
		totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
	}

	// Paging actions
	// -----------------------------------------------------------------------------

	public void pageFirst() {
		page(0);
	}

	public void pageNext() {
		int next = firstRow + rowsPerPage;
		int limit = (totalPages - 1) * rowsPerPage;
		limit = totalRows - (totalRows % rowsPerPage == 0 ? rowsPerPage : totalRows % rowsPerPage);
		page(next < limit ? next : limit);
	}

	public void pagePrevious() {
		if (firstRow - rowsPerPage >= 0)
			page(firstRow - rowsPerPage);
		else
			pageFirst();
	}

	public void pageLast() {
		page(totalRows - (totalRows % rowsPerPage == 0 ? rowsPerPage : totalRows % rowsPerPage));
	}

	private void page(int firstRow) {
		this.firstRow = firstRow;
		loadData(); // Load requested page.
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public Alert getSelectedAlert() {
		return selectedAlert;
	}

	public String details() {
		selectedAlert = (Alert) model.getRowData();
		return "details";
		// return NavigationBean.MESSAGES_PAGE;
	}

	// filters

	public String getFilterSender() {
		return filterSender;
	}

	public void setFilterSender(String filterSender) {
		this.filterSender = filterSender.equals(ItemLists.ANY_SEN) ? null : filterSender;
	}

	public String getFilterTarget() {
		return filterTarget;
	}

	public void setFilterTarget(String filterTarget) {
		this.filterTarget = filterTarget.equals(ItemLists.ANY_TAR) ? null : filterTarget;
	}

	public String getFilterCategory() {
		return filterCategory;
	}

	public void setFilterCategory(String filterCategory) {
		this.filterCategory = filterCategory.equals(ItemLists.ANY_CAT) ? null : filterCategory;
	}

	public DateFilter getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(DateFilter filterDate) {
		this.filterDate = filterDate;
	}

	public void applyFilters() {
		page(0);
	}

	// --filters

	// drop down menus

	public List<SelectItem> getAllTargets() {
		return ItemLists.getAllTargets(um, true);
	}

	public List<SelectItem> getAllCategories() {
		return ItemLists.getAllCategories(um, true);
	}

	public List<SelectItem> getMessageTypes() {
		return ItemLists.getMessageTypes(um, true);
	}

	public List<SelectItem> getAllSenders() {
		return ItemLists.getAllSenders(um, true);
	}

	public List<SelectItem> getAllDates() {
		return ItemLists.getAllDates(true);
	}

	// --drop down menus

}
