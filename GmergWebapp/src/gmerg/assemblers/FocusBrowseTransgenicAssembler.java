/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.TransgenicDAO;
import gmerg.db.ISHDevDAO;
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
public class FocusBrowseTransgenicAssembler extends OffMemoryTableAssembler {
	String[] organs;
	
	public FocusBrowseTransgenicAssembler () {
		super();
	}
	
	public FocusBrowseTransgenicAssembler (HashMap params) {
		super(params);
	}

	public void setParams() {
		super.setParams();
		organs = getParams("organs");
	}
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);

		// get data from database
		ArrayList submissions = transgenicDAO.getAllSubmission(column, ascending, offset, num, organs, filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		transgenicDAO = null;
		
		/** ---return the value object---  */
		return ISHBrowseAssembler.getTableDataFormatFromIshList(submissions);
	}
	
	public int retrieveNumberOfRows() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);

		// get data from database
		int totalNumberOfSubmissions = transgenicDAO.getTotalNumberOfSubmissions(organs, filter);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		transgenicDAO = null;
		
		//* ---return the value---  * /
		return totalNumberOfSubmissions;
	}
	
//	public int[] retrieveTotals() {
//		// create a dao
//		Connection conn = DBHelper.getDBConnection();
//		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
//
//		// get data from database
//		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION",
//                "TOTAL_NUMBER_OF_GENE_SYMBOL",
//                "TOTAL_NUMBER_OF_THEILER_STAGE",
//                "TOTAL_NUMBER_OF_GIVEN_STAGE",
//                "TOTAL_NUMBER_OF_LAB",
//                "TOTAL_NUMBER_OF_SUBMISSION_DATE",
//                "TOTAL_NUMBER_OF_ASSAY_TYPE",
//                "TOTAL_NUMBER_OF_SPECIMEN_TYPE",
//                "TOTAL_NUMBER_OF_SEX",
////                "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL",
//                "TOTAL_NUMBER_OF_PROBE_NAME",
////                "TOTAL_NUMBER_OF_ANTIBODY_NAME",
////                "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL",
//                "TOTAL_NUMBER_OF_GENOTYPE",
//                "TOTAL_NUMBER_OF_PROBE_TYPE",
//                "TOTAL_NUMBER_OF_IMAGE",
//                };
//		String endingClause = " AND (SUB_ASSAY_TYPE = 'TG') ";	// Bernie 16/11/2010 mod to ensure correct totals are returned
//		String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
//		
//		// convert to interger array, each tuple consists of column index and the number
//		int len = columnNumbers.length;
//		int[] totalNumbers = new int[len];
//		for (int i=0;i<len;i++) {
//			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
//		}
//
//		// return result
//		return totalNumbers;
//	}
	
	// xingjun - 08/09/2011 - have a new set of TOTAL_NUMBER_OF sql specifically for TG data
	public int[] retrieveTotals() {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		TransgenicDAO transgenicDAO = MySQLDAOFactory.getTransgenicDAO(conn);
		
		// get data from database
		String [] allColTotalsQueries = {
				"TOTAL_NUMBER_OF_SUBMISSION_TG",
				"TOTAL_NUMBER_OF_GENE_SYMBOL_TG",
				"TOTAL_NUMBER_OF_THEILER_STAGE_TG",
				"TOTAL_NUMBER_OF_GIVEN_STAGE_TG",
				"TOTAL_NUMBER_OF_LAB_TG",
				"TOTAL_NUMBER_OF_SUBMISSION_DATE_TG",
				"TOTAL_NUMBER_OF_ASSAY_TYPE_TG",
				"TOTAL_NUMBER_OF_SPECIMEN_TYPE_TG",
				"TOTAL_NUMBER_OF_SEX_TG",
				"TOTAL_NUMBER_OF_PROBE_NAME_TG",
				"TOTAL_NUMBER_OF_GENOTYPE_TG",
				"TOTAL_NUMBER_OF_PROBE_TYPE_TG",
				"TOTAL_NUMBER_OF_IMAGE_TG"
		};
//		String endingClause = " AND SUB_ASSAY_TYPE = 'TG' ";	// Bernie 16/11/2010 mod to ensure correct totals are returned
//		String[][] columnNumbers = transgenicDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, endingClause, filter);
		String[][] columnNumbers = transgenicDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, "", filter);
		
		// convert to interger array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			if (columnNumbers[i][1] != null) {
				totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
			} else {
				totalNumbers[i] = 0;
			}
		}

		// return result
		return totalNumbers;
	}

	public HeaderItem[] createHeader() {
		return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	}    	

}