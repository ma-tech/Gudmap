/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.ISHDevDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.utils.table.HeaderItem;
import gmerg.utils.table.OffMemoryTableAssembler;
import gmerg.utils.table.DataItem;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class ISHBrowseSubmissionNonRenalAssembler extends OffMemoryTableAssembler{
	
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		ArrayList browseSubmissionsNonRenal = ishDevDAO.getAllSubmissionsNonRenal(column, ascending, offset, num);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		/** ---return the value object---  */
		return ISHBrowseAssembler.getTableDataFormatFromIshList(browseSubmissionsNonRenal);
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] retrieveTotals() {
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		String [] allColTotalsQueries = {"TOTAL_NUMBER_OF_SUBMISSION_NON_RENAL",
                "TOTAL_NUMBER_OF_GENE_SYMBOL_NON_RENAL",
                "TOTAL_NUMBER_OF_THEILER_STAGE_NON_RENAL",
                "TOTAL_NUMBER_OF_GIVEN_STAGE_NON_RENAL",
                "TOTAL_NUMBER_OF_LAB_NON_RENAL",
                "TOTAL_NUMBER_OF_SUBMISSION_DATE_NON_RENAL",
                "TOTAL_NUMBER_OF_ASSAY_TYPE_NON_RENAL",
                "TOTAL_NUMBER_OF_SPECIMEN_TYPE_NON_RENAL",
                "TOTAL_NUMBER_OF_SEX_NON_RENAL",
//                "TOTAL_NUMBER_OF_CONFIDENCE_LEVEL_NON_RENAL",
                "TOTAL_NUMBER_OF_PROBE_NAME_NON_RENAL",
//                "TOTAL_NUMBER_OF_ANTIBODY_NAME_NON_RENAL",
//                "TOTAL_NUMBER_OF_ANTIBODY_GENE_SYMBOL_NON_RENAL",
                "TOTAL_NUMBER_OF_GENOTYPE_NON_RENAL",
                "TOTAL_NUMBER_OF_PROBE_TYPE_NON_RENAL",
                "TOTAL_NUMBER_OF_IMAGE_NON_RENAL",
                };
		String[][] columnNumbers = ishDevDAO.getStringArrayFromBatchQuery(null, allColTotalsQueries, filter);
		
		// convert to interger array, each tuple consists of column index and the number
		int len = columnNumbers.length;
		int[] totalNumbers = new int[len];
		for (int i=0;i<len;i++) {
			totalNumbers[i] = Integer.parseInt(columnNumbers[i][1]);
		}

		// return result
		return totalNumbers;
	}
	
	public int retrieveNumberOfRows() {

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);

		// get data from database
		int totalNumberOfSubmissions = ishDevDAO.getTotalNumberOfNonRenalSubmissions();
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		/** ---return the value---  */
		return totalNumberOfSubmissions;
	}
	
	
	public HeaderItem[] createHeader() {
		return ISHBrowseAssembler.createHeaderForISHBrowseTable();
	}    	

}