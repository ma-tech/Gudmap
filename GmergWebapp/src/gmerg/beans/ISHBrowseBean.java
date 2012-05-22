package gmerg.beans;

import gmerg.assemblers.ISHBrowseAssembler;
import gmerg.assemblers.ISHBrowseSubmissionNonRenalAssembler;
import gmerg.entities.Globals;
import gmerg.entities.Globals.PredefinedFilters;
import gmerg.utils.Utility;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableFilter;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.TableUtil;

/**
 * Managed Bean for display of different ISH submissions based on a Generic tables display. Because there 
 * might be multiple ISH browse tables at the same time it extends from MultipleInstanceBean.
 * 
 * @author Mehran Sharghi
 *
 *
*/

public class ISHBrowseBean extends MultipleInstanceBean {

	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public void initInstance (String browseId) {
//		System.out.println("initInstance ISHBrowseBean; browseId="+browseId);
		
//		if (TableBean.isTableViewInSession(browseId)) 
		if (TableUtil.isTableViewInSession()) 
			return;

        if (browseId != null) {
			if ("browseAll".equals(browseId)) 
				TableUtil.saveTableView(populateBrowsAllTableView("browseAll"), getDistinguishingParam(), browseId);
			
			if ("noneRenal".equals(browseId))  
				TableUtil.saveTableView(populateNoneRenalTableView("noneRenal"), getDistinguishingParam(), browseId);
		}
		else  
			System.out.println("Serious warnning! in ISHBrowseBean initInstance: browseId is null");	
	}	

    // ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getDistinguishingParam() {
		return "browseId";
	}
	
	/***************************************************************************************************************************************
	/* BrwseAll																							
	/***************************************************************************************************************************************/
	/********************************************************************************
	 * populates GenericTableView for data table of a BrowseAll page 
	 *
	 */
	private GenericTableView populateBrowsAllTableView(String viewName) {
		OffMemoryTableAssembler assembler = new ISHBrowseAssembler();
	    GenericTable table = assembler.createTable();
		table.getAssembler().setFilter(getDefaultIshFilter());
		GenericTableView tableView = getDefaultIshBrowseTableView(viewName, table);
//		tableView.setNavigationPanelMessage("Totals: ISH(<b>238</b>) &nbsp&nbsp&nbsp Microarray(<b>120</b>)");
		return  tableView;
	}

	/***************************************************************************************************************************************
	/* None Renal																							
	/***************************************************************************************************************************************
	/********************************************************************************
	 * populates GenericTableView for data table of a NoneRenal page 
	 *
	 */
	private GenericTableView populateNoneRenalTableView(String viewName) {
		OffMemoryTableAssembler noneRenalAssembler = new ISHBrowseSubmissionNonRenalAssembler();
	    GenericTable table = noneRenalAssembler.createTable();
		table.getAssembler().setFilter(getDefaultIshFilter());
		GenericTableView tableView = getDefaultIshBrowseTableView(viewName, table);
		return  tableView;
	}

	
	/***************************************************************************************************************************************
	/* Common Methods																							
	/***************************************************************************************************************************************
	/********************************************************************************

	/********************************************************************************
	 * Returns a default GenericTableView for ISH browse table 
	 *
	 */
	public static GenericTableView getDefaultIshBrowseTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 20, 650, table);
//		GenericTableView tableView = new GenericTableView (viewName, 20, table);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(0, 0);
		tableView.addCollection(1, 1);
		tableView.setDisplayTotals(true);
		tableView.setDefaultColVisible(new boolean[]{true, true, true, false, true, false, false, true, false, true, true, true, true});
//		tableView.setColVisible(new boolean[]{true, true, true, false, true, false, false, true, false, true, true, true, true});
//		tableView.setDynamicColumnsLimits(5, 9);
//	    tableView.setColWrap(false);
//		tableView.setHeightUnlimittedFixed();
//		table.getAssembler().setFilter(getDefaultIshFilter());
		
		return tableView;
	}

	public static GenericTableFilter getDefaultIshFilter() { //this is compatible with AdvancedSearchQuery version
		GenericTableFilter filter = new GenericTableFilter();
		filter.addFilter(new FilterItem(1));
		filter.addFilter(2, Globals.getPredefinedFilter(PredefinedFilters.STAGE));
		if(Utility.getProject().equalsIgnoreCase("gudmap")) 
			filter.addFilter(4, Globals.getPredefinedFilter(PredefinedFilters.LAB));
		filter.addFilter(5, Globals.getPredefinedFilter(PredefinedFilters.DATE));
		filter.addFilter(6, Globals.getPredefinedFilter(PredefinedFilters.ASSAY));
		filter.addFilter(7, Globals.getPredefinedFilter(PredefinedFilters.SPECIMEN));
		filter.addFilter(8, Globals.getPredefinedFilter(PredefinedFilters.SEX));
		filter.addFilter(new FilterItem(9));
		return filter;
	}
}