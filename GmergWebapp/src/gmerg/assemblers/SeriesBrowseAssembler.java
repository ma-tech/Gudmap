/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.ArrayDevDAO;
import gmerg.db.FocusForAllDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xingjun
 *
 */
public class SeriesBrowseAssembler extends OffMemoryTableAssembler{
	
	String organ;
	String platform;
	
	public SeriesBrowseAssembler() {
		super();
	}
	
	public SeriesBrowseAssembler(HashMap params) {
		super(params);
	}

	public void setParams() {
		super.setParams();
		organ = getParam("organ");
		platform = getParam("platform");
	}
	
	public DataItem[][] retrieveData(int columnIndex, boolean ascending, int offset, int num) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayList seriesList = null;

		if (organ == null) {
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			// get data from database
			seriesList = arrayDevDAO.getAllSeries(columnIndex, ascending, offset, num, platform);
			arrayDevDAO = null;
		}
		else {
			FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			// get data from database
			seriesList = focusForAllDAO.getSeriesList(columnIndex, ascending, offset, num, organ, platform);
			focusForAllDAO = null;
		}
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		
		// return the value object
		return getTableDataFormatFromSeriesList(seriesList);
	}
	
	public int retrieveNumberOfRows() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		int totalNumberOfSeries = 0;
		//System.out.println("ORGAN 0827:"+organ);
		if (organ == null) {
			ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
			// get data from database
			totalNumberOfSeries = arrayDevDAO.getTotalNumberOfSeries(platform);
			arrayDevDAO = null;
		}
		else {
			FocusForAllDAO focusForAllDAO = MySQLDAOFactory.getFocusForAllDAO(conn);
			// get data from database
			totalNumberOfSeries = focusForAllDAO.getNumberOfSeries(organ, platform);
			focusForAllDAO = null;
		}
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		
		// return the value object
		return totalNumberOfSeries;
	}
	
	public int[] retrieveTotals() {

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ArrayDevDAO arrayDevDAO = MySQLDAOFactory.getArrayDevDAO(conn);
		
		// get data from database
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_EXPERIMENT_NAME",
                "TOTAL_NUMBER_OF_SAMPLE_NUMBERS",
                "TOTAL_NUMBER_OF_SERIES_GEO_ID",
                "TOTAL_NUMBER_OF_SERIES_LAB",
                "TOTAL_NUMBER_OF_SERIES_PLATFORM",
                };
		String[][] columnNumbers = arrayDevDAO.getSeriesBrowseTotals(null, allColTotalsQueries, organ);
		
		// convert to interger array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
		}

		return totalNumbers;
	}
	
	public HeaderItem[] createHeader() {
//		 String headerTitles[] = { "Descriptive name of series", "Number of Samples", "GEO ID", "Lab", "Platform" };
//		 boolean headerSortable[] = {false, true, true, true, true };
		 String headerTitles[] = { "Title", "GEO ID", "Number of Samples", "Lab", "Platform", "Component(s) Sampled" };
		 boolean headerSortable[] = {false, true, true, true, true, false};
		 int colNum = headerTitles.length;
		 HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);

		 return tableHeader;
	}
	
	/********************************************************************************
	 * Returns a 2D array of DataItems populated by data in a list of Series submissions 
	 *
	 * modified by xingjun - 06/07/2009 - changed to get display series info by series oid
	 * xingjun - 02/03/2010 - added extra column: list of components 
	 */
	private static DataItem[][] getTableDataFormatFromSeriesList(ArrayList seriesList)
	{
		if (seriesList==null){
			System.out.println("No data is retrieved");
			return null;
		}

		int colNum = ((String[])seriesList.get(0)).length;
		int rowNum = seriesList.size();
		
        //build the data item containing the actual sample data from the ArrayList 
        DataItem[][] tableData = new DataItem[rowNum][colNum];
		for(int i=0; i<rowNum; i++) {
			String[] row = (String[])seriesList.get(i);
//			System.out.println("series oid: " + row[5]);
//			tableData[i][0] = new DataItem(row[0], "Click to view Series Details", "series.html?seriesId="+row[1], 10);   									// Descriptive name (experiment name)
			tableData[i][0] = new DataItem(row[0], "Click to view Series Details", "series.html?seriesId="+row[5], 10);   									// Descriptive name (experiment name)
			tableData[i][1] = new DataItem(row[1], "Click to view Series in GEO", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[1], 9 );// Geo ID
//			tableData[i][2] = new DataItem(row[2], "Click to view Samples page", "series.html?seriesId="+row[1], 10);										// Sample No
			tableData[i][2] = new DataItem(row[2], "Click to view Samples page", "series.html?seriesId="+row[5], 10);										// Sample No
			tableData[i][3] = new DataItem(row[3]);   																										// Lab
			tableData[i][4] = new DataItem(row[4], "Click to view Platform in GEO", "http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc="+row[4], 2);// Platform
			tableData[i][5] = new DataItem(row[6]);   																										// Description (list of components)
		}
			
		return tableData;
	 }	
	
}