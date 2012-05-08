/**
 * 
 */
package gmerg.db;

//import gmerg.entities.submission.array.MicroBrowseAllBean;

//import gmerg.entities.submission.Submission;
//import gmerg.entities.GenelistInfo;
import gmerg.assemblers.AllComponentsGenelistAssembler;
import gmerg.entities.submission.Gene;
import gmerg.entities.submission.array.GeneListBrowseSubmission;
import gmerg.entities.submission.array.MasterTableInfo;
import gmerg.entities.submission.array.Platform;
import gmerg.entities.submission.array.ProcessedGeneListHeader;
import gmerg.entities.submission.array.Sample;
import gmerg.entities.submission.array.SearchLink;
import gmerg.entities.submission.array.Series;
import gmerg.entities.submission.array.SupplementaryFile;
import gmerg.entities.submission.Transgenic;
//import gmerg.entities.submission.TransgenicNote;
import gmerg.entities.HeatmapData;
import gmerg.utils.Utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.ResourceBundle;
//import java.util.ArrayList;
import java.sql.*;

import analysis.DataSet;

/**
 * @author xingjun
 *
 */
public class MySQLArrayDAOImp implements ArrayDAO {
	
    Connection conn;
	
	// default constructor
	public MySQLArrayDAOImp() {
		
	}
	
	// constructor with connection initialisation
	public MySQLArrayDAOImp(Connection conn) {
		this.conn = conn;
	}
	

	public ArrayList getMicroGeneQueryResult(String[][] param) throws SQLException {
		return new ArrayList();
	}
	
	/**
	 * implement the method defined in the interface: ArrayDAO
	 * @param order
	 * @param offset
	 * @param num
	 */
/*	
	public ArrayBrowseSubmission[] getAllSubmissionArray(String[] order, String offset, String num) {
		ResultSet resSet = null;
		ArrayBrowseSubmission[] result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		String defaultOrder = new String(" ORDER BY SUB_SUB_DATE DESC, CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED)");
		String queryString = assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, order, offset, num);
//		System.out.println("offset: " + offset);

//		System.out.println("array browse query: " + queryString);
		parQ.setQuerySQL(queryString);
		
		// execute query and assemble result
		try {
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			
			// execute
			resSet = prepStmt.executeQuery();
			result = formatBrowseResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
//			DBUtil.closeJDBCConnection(conn);
			
			// reset the static query string to its original value
			parQ.setQuerySQL(query);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}
*/        
        /**
         * @return total number of submissions
         */
        public String getTotalNumberOfSubmission() {
            String totalNumber = new String("");
            ResultSet resSet = null;
            ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ARRAY");
            PreparedStatement prepStmt = null;

            try {
            	// if disconnected from db, re-connected
            	conn = DBHelper.reconnect2DB(conn);

                parQ.setPrepStat(conn);
                prepStmt = parQ.getPrepStat();

                // execute
                resSet = prepStmt.executeQuery();

                if (resSet.first()) {
                    totalNumber = Integer.toString(resSet.getInt(1));
                }

                // close the db object
                DBHelper.closePreparedStatement(prepStmt);
                //                  DBUtil.closeJDBCConnection(conn);

            } catch (SQLException se) {
                se.printStackTrace();
            }
            return totalNumber;
        }
	
	/**
	 * 
	 * @param queryType: 1-array browse; 2-gene list item browse
	 * @param query: sql string
	 * @param defaultOrder: if no 'order-by' specified, use it
	 * @param order: a 1-dimension array stores the to-be-ordered item and its order
	 * @param offset: start position of the rows to be retrieved
	 * @param num: number of the rows to be retrieved
	 * @return
	 */
	private String assembleBrowseSubmissionQueryStringArray(int queryType, String query,
			String defaultOrder, String[] order, String offset, String num) {
		String queryString = null;
		
		// order by
		if (order != null) {
			
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = null;
			if (queryType == 1) { // array browse
				column = getArrayBrowseOrderByColumn(order);
			} else if (queryType == 2) { // gene list item browse
				column = getGeneListBrowseOrderByColumn(order);
			}
			queryString += column + " " + order[1] + ",";
			
			// remove the trailing ',' character
			int len = queryString.length();
			queryString = queryString.substring(0, len-1);
			
		} else { // if don't specify order by column, order by submission id ascend by default
//			System.out.println("order is nulll");
			queryString = query + defaultOrder;
//			System.out.println("queryString: " + queryString);
		}
		
		// offset and retrieval number
		if (offset != null) {
			
			if (!offset.equalsIgnoreCase("ALL")) {
				if (isValidInteger(offset)) {
					int os = Integer.parseInt(offset) - 1;
					queryString = queryString + " LIMIT " + Integer.toString(os);
				} else {
					queryString = queryString + " LIMIT 0";
				}
				if (isValidInteger(num)) {
					queryString = queryString + " ," + num;
				} else {
					queryString = queryString + " ," + "100000000";
				}
			}
		}
		
		// return assembled query string
		return queryString;
		
	}
	
	private String assembleBrowseSubmissionQueryStringArray(int queryType, String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
		String queryString = null;
		//System.out.println("QueryType:"+columnIndex+":"+queryType);
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = null;
			if (queryType == 1) { // array browse
				column = getArrayBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
			} else if (queryType == 2) { // gene list item browse
				column = getGeneListBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
			}
			queryString += column;
			
		} else { // if don't specify order by column, order by submission date & stage descending by default
			queryString = query + " ORDER BY " + defaultOrder;
//			System.out.println("queryString: " + queryString);
		}
		
		// offset and retrieval number
//		System.out.println("offset: " + offset);
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;

		// return assembled query string
		return queryString;
	} // end of assembleBrowseSubmissionQueryStringArray

	/**
	 * 
	 * @param order
	 * @return
	 */
	private String getArrayBrowseOrderByColumn(String[] order) {
//		int columnNumber = order.length;
//		String[][] columns = new String[columnNumber][2];
		
		String column = new String("");
//		System.out.println("LAB:"+order[0]+":"+order[1]);
		// start to translate
		if(order[0].equals("byID")) {
			column = "CAST(SUBSTRING(SUB_ACCESSION_ID,8) AS UNSIGNED)"; 
		} else if (order[0].equals("bySample")) {
			column = "SMP_GEO_ID"; 
		} else if (order[0].equals("byTheilerStage")) {
			column = "SMP_THEILER_STAGE"; 
		} else if (order[0].equals("byAge")) {
			column = "SPN_STAGE"; 
		} else if (order[0].equals("byLab")) {
			column = "PER_SURNAME"; 
		} else if (order[0].equals("byDate")) {
			column = "SUB_SUB_DATE"; 
		} else if (order[0].equals("byTissueType")) {
			column = "SMP_SOURCE"; 
		} else if (order[0].equals("byDescription")) {
			column = "SRM_SAMPLE_DESCRIPTION"; 
		} else if (order[0].equals("byTitle")) {
			column = "SMP_TITLE"; 
		} else if (order[0].equals("bySeries")) {
			column = "SER_GEO_ID"; 
		} else {
			column = "SUB_SUB_DATE";
		}
		return column;
	}
	
	private String getArrayBrowseOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC": "DESC");
//		String[] arrayBrowseColumnList =
//		{"CAST(SUBSTRING(SUB_ACCESSION_ID,8) AS UNSIGNED)", "SMP_GEO_ID",
//				"SMP_THEILER_STAGE", "SPN_STAGE", "PER_SURNAME", "SUB_SUB_DATE",
//				"SMP_SOURCE", "SRM_SAMPLE_DESCRIPTION", "SMP_TITLE", "SER_GEO_ID"};

		// start to translate
		if(columnIndex == 0) { // gudmap id
			column = "CAST(SUBSTRING(SUB_ACCESSION_ID,8) AS UNSIGNED) " + order + ", " + defaultOrder;
		} else if (columnIndex == 1) { // sample
			column = "SMP_GEO_ID " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) { // stage
			column = "SMP_THEILER_STAGE " + order + ", " + "SUB_SUB_DATE DESC";
		} else if (columnIndex == 3) { // age
//			column = "concat(SPN_STAGE_FORMAT,SPN_STAGE) " + order + ", " + defaultOrder;
			column = "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) { // lab
			column = "PER_SURNAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 5) { // submission date
			column = "SUB_SUB_DATE " + order + ", " + "SMP_THEILER_STAGE DESC";
		} else if (columnIndex == 6) {// tissue
			column = "SMP_SOURCE " + order + ", " + defaultOrder;
		} else if (columnIndex == 7) { // description
			column = "SRM_SAMPLE_DESCRIPTION " + order + ", " + defaultOrder;
		} else if (columnIndex == 8) { // title
			column = "SMP_TITLE " + order + ", " + defaultOrder;
		} else if (columnIndex == 9) { // series
			column = "SER_GEO_ID " + order + ", " + defaultOrder;
		} else if (columnIndex == 10) { // series
			column = "concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') " + order + ", " + defaultOrder;
		} else { // default
			column = defaultOrder;
		}
		return column;
	}

	/**
	 * 
	 * @param personId
	 */
	public String findLabNameByPersonId(String personId) {
		
		String labName = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("LAB_NAME_FROM_PERSON_ID");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, personId);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				labName = resSet.getString(1);
			}
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return labName;
	}
		
	/**
	 * 
	 * @param order
	 * @return
	 */
	private String getGeneListBrowseOrderByColumn(String[] order) {
//		int columnNumber = order.length;
//		String[][] columns = new String[columnNumber][2];
		
		String column = new String("");
		
		// start to translate
		if(order[0].equals("byGene")) {
			column = "MBC_GNF_SYMBOL"; 
		} else if (order[0].equals("byProbeID")) {
			column = "MBC_GLI_PROBE_SET_NAME"; 
		} else if (order[0].equals("bySignal")) {
			column = "MBC_GLI_SIGNAL"; 
		} else if (order[0].equals("byDetection")) {
			column = "MBC_GLI_DETECTION"; 
		} else if (order[0].equals("byPValue")) {
			column = "MBC_GLI_P_VALUE"; 
		}
                else {
                    column = "MBC_GNF_SYMBOL";
                }
		return column;
	}

	// need change when find default order
	/**
	 * @author xingjun
	 * 
	 */
	private String getGeneListBrowseOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC":"DESC");
		
		// start to translate
		if(columnIndex == 0) { // gene
			column = "MBC_GNF_SYMBOL " + order + ", " + defaultOrder; 
		} else if (columnIndex == 1) { // probe id
			column = "MBC_GLI_PROBE_SET_NAME " + order + ", " + defaultOrder; 
		} else if (columnIndex == 2) { // signal
			column = "MBC_GLI_SIGNAL " + order + ", " + defaultOrder; 
		} else if (columnIndex == 3) { // detection
			column = "MBC_GLI_DETECTION " + order + ", " + defaultOrder; 
		} else if (columnIndex == 4) { // p-value
			column = "MBC_GLI_P_VALUE " + order + ", " + defaultOrder; 
		} else {
			column = defaultOrder;
		}
		return column;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private boolean isValidInteger(String value) {
		
		boolean result = true;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			result = false;
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
/*	
	private ArrayBrowseSubmission[] formatBrowseResultSet(ResultSet resSet) throws SQLException {
		
//		ResultSetMetaData resSetMetaData = resSet.getMetaData();
//		int columnCount = resSetMetaData.getColumnCount();
		
		if (resSet.first()) {
			resSet.last();
			int arraySize = resSet.getRow();
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayBrowseSubmission[] results = new ArrayBrowseSubmission[arraySize];
			int i = 0;
			
			while (resSet.next()) {
				 
				// option 1: initialise a ish browse submission object and add it into the array
				// false is the default value of the 'selected'
				ArrayBrowseSubmission arrayBrowseSubmission = new ArrayBrowseSubmission();
				arrayBrowseSubmission.setId(resSet.getString(1));
				arrayBrowseSubmission.setDescription(resSet.getString(2));
				arrayBrowseSubmission.setTitle(resSet.getString(3));
				arrayBrowseSubmission.setSample(resSet.getString(4));
				arrayBrowseSubmission.setStage(resSet.getString(5));
				arrayBrowseSubmission.setLab(resSet.getString(7));
				arrayBrowseSubmission.setSeries(resSet.getString(8));
				arrayBrowseSubmission.setDate(resSet.getString(9));
				arrayBrowseSubmission.setAge(resSet.getString(11));
				arrayBrowseSubmission.setTissue(resSet.getString(13));
				arrayBrowseSubmission.setSelected(false);
				results[i] = arrayBrowseSubmission;
				i++;
				
                // option 2: initialise an array and add it into the arraylist
				// first cell used to indicate checkbox status: false is the default value
//				String[] columns = new String[columnCount + 1];
//				columns[0] = "false";
//				for (int i = 1; i < columnCount; i++) {
//					columns[i] = resSet.getString(i + 1);
//		        }
//		        results.add(columns);
		    }
		    return results;
		}
	    return null;
    }
	private ArrayList formatBrowseResultSet2ArrayList(ResultSet resSet) throws SQLException {

		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			ArrayList<ArrayBrowseSubmission> results = new ArrayList<ArrayBrowseSubmission>();
			int i=0;
			while (resSet.next()) {
				ArrayBrowseSubmission arrayBrowseSubmission = new ArrayBrowseSubmission();
				arrayBrowseSubmission.setId(resSet.getString(1));
				arrayBrowseSubmission.setDescription(resSet.getString(2));
				arrayBrowseSubmission.setTitle(resSet.getString(3));
				arrayBrowseSubmission.setSample(resSet.getString(4));
				arrayBrowseSubmission.setStage(resSet.getString(5));
				arrayBrowseSubmission.setLab(resSet.getString(7));
				arrayBrowseSubmission.setSeries(resSet.getString(8));
				arrayBrowseSubmission.setDate(resSet.getString(9));
				arrayBrowseSubmission.setAge(resSet.getString(11));
				arrayBrowseSubmission.setTissue(resSet.getString(13));
				arrayBrowseSubmission.setSelected(false);
				results.add(arrayBrowseSubmission);
				i++;
		    }
			System.out.println("i: " + i);
		    return results;
		}
	    return null;
    }
*/	

	/**
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query string
	 * @return query name and query result stored in a 2-dim array
	 */
	public String[][] getStringArrayFromBatchQuery(String[][] param, String[] query) {
		int queryNumber = query.length;
		String[][] result = new String[queryNumber][2];
		String[] queryString = new String[queryNumber];
		
		// get the query sql based on query name and record query name in result string array
		for (int i=0;i<queryNumber;i++) {
//			System.out.println("query Name: " + query[i]);
			ParamQuery parQ = DBQuery.getParamQuery(query[i]);
			queryString[i] = parQ.getQuerySQL();
			result[i][0] = query[i];
		}
		
		// start to execute query
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			for (int i=0;i<queryNumber;i++) {
				prepStmt = conn.prepareStatement(queryString[i]);
				if ( param != null && param[i] != null) { // set query criteria if it's not null
					int parameterNumber = param[i].length;
					for (int j=0;j<parameterNumber;j++) {
						prepStmt.setString(j+1, param[i][j]);
					}
				}
				resSet = prepStmt.executeQuery();
				result[i][1] = getStringValueFromIntegerResultSet(resSet);
			}
			// close the database object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 */
	private String getStringValueFromIntegerResultSet(ResultSet resSet) {
		try {
			if (resSet.first()) {
				int integerResult = resSet.getInt(1);
				return Integer.toString(integerResult);
			} else {
				return null;
			}
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param submissionAccessionId
	 * @return SupplementaryFile
	 */
	public SupplementaryFile findSupplementaryFileInfoBySubmissionId(String submissionAccessionId) {
		
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
		
		SupplementaryFile supplementaryFiles = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SUPPLIMENTARY_FILES_ARRAY");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			supplementaryFiles = formatSupplementaryFileResultSet(resSet); 
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return supplementaryFiles;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private SupplementaryFile formatSupplementaryFileResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			SupplementaryFile supplementaryFile = new SupplementaryFile();
			supplementaryFile.setFilesLocation(resSet.getString(1));
			supplementaryFile.setCelFile(resSet.getString(2));
			supplementaryFile.setChpFile(resSet.getString(3));
			supplementaryFile.setRptFile(resSet.getString(4));
			supplementaryFile.setExpFile(resSet.getString(5));
			supplementaryFile.setTxtFile(resSet.getString(6));
			
			return supplementaryFile;
		}
		return null;
	}
	
	/**
	 * <p> modified by xingjun - 18/11/2009 - relevant sample info might be null</p>
	 * @param submissionAccessionId
	 * @return Sample
	 */
	public Sample findSampleBySubmissionId(String submissionAccessionId) {
		
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
//		System.out.println("ArrayDAO:findSampleBySubmissionId:submissionAccessionId: " + submissionAccessionId);
		Sample sample = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_SAMPLE");
		String queryString = parQ.getQuerySQL();
//		System.out.println("ArrayDAO:findSampleBySubmissionId:sql: " + queryString);
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			sample = formatSampleResultSet(resSet); 
			if (sample != null) { // xingjun - 18/11/2009 - in case there's no sample info
				formatSampleAnatomy(sample, submissionAccessionId);
			}
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return sample;
	}
	
	/**
	 * <p>modified by xingjun - 18/06/2008 - we might got more than one source record</p>
	 * <p>xingjun - 18/11/2009 - sampleAnatomy might be empty</p> 
	 * @param sample
	 * @param id
	 */
	public void formatSampleAnatomy(Sample sample, String id) {
		
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("SAMPLE_SOURCE");
		PreparedStatement prepStmt = null;
//		System.out.println("ArrayDAO:formatSampleAnatomy:sql: " + parQ.getQuerySQL());
//		System.out.println("ArrayDAO:formatSampleAnatomy:id: " + id);
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, id);
			
			// execute
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.beforeFirst();
				String anatomySource = "";
				while (resSet.next()) { // it's possible it's expressed in more than one component 
					anatomySource += resSet.getString(1) + "; ";
				}
				anatomySource = anatomySource.substring(0, anatomySource.length()-2);
//				System.out.println("ArrayDAO:formatSampleAnatomy:anatomySource: " + anatomySource);
				if (anatomySource.trim().length() != 0) { // xingjun - 18/11/2009
					sample.setSource(anatomySource);
				}
			}
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
	}	
	
	/**
	 * <p>xingjun - 14/05/2010 
	 * - added 3 extra properties: experimentalDesign, labelProtocol, scanProtocol</p>
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private Sample formatSampleResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			Sample sample = new Sample();
			sample.setGeoID(resSet.getString(1));
			sample.setTitle(resSet.getString(2));
			sample.setSource(resSet.getString(3));
			sample.setOrganism(resSet.getString(4));
			sample.setStrain(resSet.getString(5));
			sample.setMutation(resSet.getString(6));
			sample.setSex(resSet.getString(7));
			sample.setDevAge(resSet.getString(8));
			sample.setTheilerStage(resSet.getString(9));
			sample.setdissectionMethod(resSet.getString(10));
			sample.setMolecule(resSet.getString(11));
			sample.setA_260_280(resSet.getString(12));
			sample.setExtractionProtocol(resSet.getString(13));
			sample.setAmplificationKit(resSet.getString(14));
			sample.setAmplificationProtocol(resSet.getString(15));
			sample.setAmplificationRounds(resSet.getString(16));
			sample.setVolTargHybrid(resSet.getString(17));
			sample.setLabel(resSet.getString(18));
			sample.setWashScanHybProtocol(resSet.getString(19));
			sample.setGcosTgtVal(resSet.getString(20));
			sample.setDataAnalProtocol(resSet.getString(21));
			sample.setReference(resSet.getString(22));
			sample.setDescription(resSet.getString(23));
			sample.setExperimentalDesign(resSet.getString(24));
			sample.setLabelProtocol(resSet.getString(25));
			sample.setScanProtocol(resSet.getString(26));
			sample.setPoolSize(resSet.getString(27));
			sample.setPooledSample(resSet.getString(28));
			sample.setDevelopmentalLandmarks(resSet.getString(29));// Bernie 22/11/2010 - Mantis 505
			
			return sample;
		}
		return null;
	}

	/**
	 * 
	 * @param submissionAccessionId
	 * @return Series
	 */
	public Series findSeriesBySubmissionId(String submissionAccessionId) {
		
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
		
		Series series = null;
		ResultSet resSetSeries = null;
		ResultSet resSetSampleNumber = null;
		ParamQuery parQSeries = DBQuery.getParamQuery("SUBMISSION_SERIES");
		ParamQuery parQSampleNumber = DBQuery.getParamQuery("SAMPLE_NUMBER_OF_SERIES");
		PreparedStatement prepStmtSeries = null;
		PreparedStatement prepStmtSampleNumber = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			conn.setAutoCommit(false);
			
			// series
			parQSeries.setPrepStat(conn);
			prepStmtSeries = parQSeries.getPrepStat();
			prepStmtSeries.setString(1, submissionAccessionId);
			resSetSeries = prepStmtSeries.executeQuery();
			
			// sample number
			parQSampleNumber.setPrepStat(conn);
			prepStmtSampleNumber = parQSampleNumber.getPrepStat();
			prepStmtSampleNumber.setString(1, submissionAccessionId);
			resSetSampleNumber = prepStmtSampleNumber.executeQuery();
			
			// assemble
			series = formatSeriesResultSet(resSetSeries, resSetSampleNumber); 
			
			conn.commit();
			conn.setAutoCommit(true);

			// release the db objects
			DBHelper.closePreparedStatement(prepStmtSeries);
			DBHelper.closePreparedStatement(prepStmtSampleNumber);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return series;
	}
	
	/**
	 * modified by xingjun - 06/07/2009 - added oid into result series
	 * 
	 * @param resSetSeries
	 * @param resSetSampleNumber
	 * @return
	 * @throws SQLException
	 */
	private Series formatSeriesResultSet(ResultSet resSetSeries, ResultSet resSetSampleNumber) throws SQLException {
		if (resSetSeries.first()) {
			// series
			Series series = new Series();
			series.setGeoID(resSetSeries.getString(1));
			series.setTitle(resSetSeries.getString(2));
			series.setSummary(resSetSeries.getString(3));
			series.setType(resSetSeries.getString(4));
			series.SetDesign(resSetSeries.getString(5));
			series.setOid(resSetSeries.getInt(6));
			
			// get the sample number
			if (resSetSampleNumber.first()) {
				series.setNumSamples(Integer.toString(resSetSampleNumber.getInt(1)));
			}
			return series;
		}
		return null;
	}
	
	/**
	 * 
	 * @param submissionAccessionId
	 * @return Platform
	 */
	public Platform findPlatformBySubmissionId(String submissionAccessionId) {
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
		
		Platform platform = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_PLATFORM");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			platform = formatPlatformResultSet(resSet); 
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return platform;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private Platform formatPlatformResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			Platform platform = new Platform();
			platform.setGeoID(resSet.getString(1));
			platform.setTitle(resSet.getString(2));
			platform.setName(resSet.getString(3));
			platform.setDistribution(resSet.getString(4));
			platform.setTechnology(resSet.getString(5));
			platform.setOrganism(resSet.getString(6));
			platform.setManufacturer(resSet.getString(7));
			platform.setManufactureProtocol(resSet.getString(8));
			platform.setCatNo(resSet.getString(9));
			
			return platform;
		}
		return null;
	}

	public GeneListBrowseSubmission[] getGeneListBrowseSubmissionsBySubmissionId(String submissionAccessionId, String[] order, String offset) {
		ResultSet resSet = null;
		GeneListBrowseSubmission[] result = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_GENE_LIST_ITEM");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		
		String defaultOrder = new String(" ORDER BY MBC_GNF_SYMBOL");
		String queryString = assembleBrowseSubmissionQueryStringArray(2, query, defaultOrder, order, offset, "500");
		
//		System.out.println("genelist query: " + queryString);
		parQ.setQuerySQL(queryString);
		
		// execute query and assemble result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			// execute
			resSet = prepStmt.executeQuery();
			result = formatGeneListBrowseResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
//			DBUtil.closeJDBCConnection(conn);
			
			// reset the static query string to its original value
			parQ.setQuerySQL(query);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private GeneListBrowseSubmission[] formatGeneListBrowseResultSet(ResultSet resSet) throws SQLException {
//		ResultSetMetaData resSetMetaData = resSet.getMetaData();
//		int columnCount = resSetMetaData.getColumnCount();
		
		if (resSet.first()) {
			resSet.last();
			int arraySize = resSet.getRow();
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			GeneListBrowseSubmission[] results = new GeneListBrowseSubmission[arraySize];
			int i = 0;
			
			while (resSet.next()) {
				 
				// option 1: initialise a gene list browse submission object and add it into the array
				// false is the default value of the 'selected'
				GeneListBrowseSubmission geneListBrowseSubmission = new GeneListBrowseSubmission();
				geneListBrowseSubmission.setGeneSymbol(resSet.getString(1));
				geneListBrowseSubmission.setSignal(resSet.getString(2));
				geneListBrowseSubmission.setDetection(resSet.getString(3));
				geneListBrowseSubmission.setPvalue(resSet.getString(4));
				geneListBrowseSubmission.setProbeId(resSet.getString(5));
				geneListBrowseSubmission.setSelected(false);
				results[i] = geneListBrowseSubmission;
				i++;
				
                // option 2: initialise an array and add it into the arraylist
				// first cell used to indicate checkbox status: false is the default value
//				String[] columns = new String[columnCount + 1];
//				columns[0] = "false";
//				for (int i = 1; i < columnCount; i++) {
//					columns[i] = resSet.getString(i + 1);
//		        }
//		        results.add(columns);
		    }
		    return results;
		}
	    return null;
	}
	
	/**
	 * 
	 * @param submissionAccessionId
	 * @return String
	 */
	public String getTotalNumberOfGeneListItemsBySubmissionId(String submissionAccessionId) {
		
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return "0";
		}
		
		String totalNumber = new String("");
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_GENE_LIST_ITEM");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				totalNumber = Integer.toString(resSet.getInt(1));
			}
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return totalNumber;
	}
	
	/**
	 * method to query database to return an entire gene list for a single microarray sample and find the location
	 * of a specific gene symbol in the data set
	 * @param accessionId the accession id of the microarray sample
	 * @param geneSymbol the specified gene symbol
	 * @return the row number of the entry matching the geneSymbol string
	 */
	public int getRowNumOf1stOccurrenceOfGeneInArrayGeneList(String accessionId, String geneSymbol) {
		
		if(accessionId == null || geneSymbol == null){
			return -1;
		}
		
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SUBMISSION_GENE_LIST_ITEM");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();
		String defaultOrder = new String(" ORDER BY MBC_GNF_SYMBOL");
		String queryString = assembleBrowseSubmissionQueryStringArray(0, query, defaultOrder, null, null, null);
		parQ.setQuerySQL(queryString);
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, accessionId);
			resSet = prepStmt.executeQuery();
			
			// reset the static query string to its original value
			parQ.setQuerySQL(query);
			if(resSet.first()){
				resSet.beforeFirst();
				boolean notFound = true;
				int rowNum = -1;
				//starting at 0 go through the result set until a match is found
				while(resSet.next() && notFound){
					if(resSet.getString(1).equalsIgnoreCase(geneSymbol)){
						notFound = false;
						rowNum = resSet.getRow();
					}	
				}
				//if a match was found return the row number of the matching entry
				if(!notFound){
					return rowNum;
				}
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
		}
		return -1;
	}

	
	/**
	 * 
	 */
/*	
	public ArrayBrowseSubmission[] getSubmissionsByLabId(String labId, String submissionDate,
            String[] order, String offset, String num) {
		
		ResultSet resSet = null;
		ArrayBrowseSubmission[] result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();

		if (submissionDate == null || submissionDate.equals("")) {
			query += " AND SUB_PI_FK = ? ";
		} else {
			query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? ";
		}
		
		String defaultOrder = new String(" ORDER BY SUB_SUB_DATE DESC, SMP_THEILER_STAGE DESC");
		String queryString = assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, order, offset, num);
//		System.out.println("array browse query: " + queryString);
		
        int lab = -1;
		try {
        	lab = Integer.parseInt(labId);
        	
        } catch(NumberFormatException nfe) {
        	lab = 0;
        }
        
		// execute query and assemble result
		try {
			
			prepStmt = conn.prepareStatement(queryString);
			
			if (submissionDate == null || submissionDate.equals("")) {
				prepStmt.setInt(1, lab);
			} else {
				prepStmt.setInt(1, Integer.parseInt(labId));
				prepStmt.setString(2, submissionDate);
			}
			
			// execute
			resSet = prepStmt.executeQuery();
			result = formatBrowseResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}
*/	
	/**
	 * @author xingjun
	 */
	public ArrayList getSubmissionsByLabId(String labId, String submissionDate,
            int columnIndex, boolean ascending, int offset, int num) {
//		System.out.println("labId: " + labId);
//		System.out.println("submissionDate: " + submissionDate);
//		System.out.println("labId: " + labId);
		try { // return null value if lab id is not valid
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return null;
		}

		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();

		if (submissionDate == null || submissionDate.equals("")) {
			query += " AND SUB_PI_FK = ? ";
		} else {
			query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? ";
		}
		
		String defaultOrder = new String("SUB_SUB_DATE DESC, SMP_THEILER_STAGE DESC");
		String queryString =
			assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, columnIndex, ascending, offset, num);
//		System.out.println("array browse query: " + queryString);
		
        int lab = -1;
		try {
        	lab = Integer.parseInt(labId);
        	
        } catch(NumberFormatException nfe) {
        	lab = 0;
        }
        
		// execute query and assemble result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			
			if (submissionDate == null || submissionDate.equals("")) {
				prepStmt.setInt(1, lab);
			} else {
				prepStmt.setInt(1, Integer.parseInt(labId));
				prepStmt.setString(2, submissionDate);
			}
			
			// execute
			resSet = prepStmt.executeQuery();
//			result = formatBrowseResultSet2ArrayList(resSet);
			result = DBHelper.formatResultSetToArrayList(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}

	/**
	 * @author xingjun - 01/07/2011 - overloading version
	 */
	public ArrayList getSubmissionsByLabId(String labId, String submissionDate, String archiveId,
            int columnIndex, boolean ascending, int offset, int num) {
//		System.out.println("labId: " + labId);
//		System.out.println("submissionDate: " + submissionDate);
//		System.out.println("labId: " + labId);
		try { // return null value if lab id is not valid
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return null;
		}

		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = DBQuery.getParamQuery("ALL_ENTRIES_ARRAY");
		PreparedStatement prepStmt = null;
		
		// assemble the query string
		String query = parQ.getQuerySQL();

		if (submissionDate == null || submissionDate.equals("")) {
			query += " AND SUB_PI_FK = ? ";
		} else {
			query += " AND SUB_PI_FK = ? AND SUB_SUB_DATE = ? ";
		}
		
		if (archiveId != null && !archiveId.trim().equals("")) {
			query += " AND SUB_ARCHIVE_ID = ? ";
		}
		
		String defaultOrder = new String("SUB_SUB_DATE DESC, SMP_THEILER_STAGE DESC");
		String queryString =
			assembleBrowseSubmissionQueryStringArray(1, query, defaultOrder, columnIndex, ascending, offset, num);
//		System.out.println("array browse query: " + queryString);
		
		// execute query and assemble result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			
			prepStmt.setInt(1, Integer.parseInt(labId));
			if (submissionDate != null && !submissionDate.equals("")) {
				prepStmt.setString(2, submissionDate);
			}
			
			if (archiveId != null && !archiveId.trim().equals("")) {
				prepStmt.setInt(3, Integer.parseInt(archiveId));
			}
			
			// execute
			resSet = prepStmt.executeQuery();
			result = DBHelper.formatResultSetToArrayList(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param submissionAccessionId
	 */
	public ArrayList findSamplesInCertainSeriesBySubmissionId(String submissionAccessionId) {
		
		ArrayList relatedSamples = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SERIES_SAMPLE");
		PreparedStatement prepStmt = null;
//		System.out.println("series sample query: " + parQ.getQuerySQL());
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				relatedSamples = DBHelper.formatResultSetToArrayList(resSet);
			}
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return relatedSamples;
	}
	
	/**
	 * 
	 * @param symbol
	 */
	public int findNumberOfSubmissionsByGeneSymbolISH(String symbol) {
		
		int submissionNumber = 0;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("NUMBER_RELATED_SUBMISSIONS_FOR_GENE_ISH");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, symbol);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				submissionNumber = resSet.getInt(1);
			}
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return submissionNumber;
	}
	
	/**
	 * 
	 * @param symbol
	 */
	public int findNumberOfSubmissionsByGeneSymbolArray(String symbol) {

		int submissionNumber = 0;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("NUMBER_RELATED_SUBMISSIONS_FOR_GENE_ARRAY");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, symbol);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				submissionNumber = resSet.getInt(1);
			}
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return submissionNumber;
	}

	// used for analysis
	/**
	 * 
	 * @param seriesId
	 */
	public String[][] findSampleIdsBySeriesId(String seriesId) {
		
		String[][] samplesInfo = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SAMPLE_INFO_BY_SERIES_ID");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, seriesId);
			resSet = prepStmt.executeQuery();
			
			samplesInfo = formatSampleIdResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return samplesInfo;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private String[][] formatSampleIdResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			resSet.last();
			int sampleNumber = resSet.getRow();
			resSet.beforeFirst();
			String[][] samplesInfo = new String[sampleNumber][2];
			int i = 0;
			while (resSet.next()) {
				samplesInfo[i][0] = resSet.getString(1);
				samplesInfo[i++][1] = resSet.getString(2);
			}
			return samplesInfo;
		}
		return null;
	}
	
	/**
	 * this method need to be rewritten: it's not comply to the design!!!!!!!!!!!!!!!!
	 * @param sampleIds
	 * @return
	 */
	public DataSet getAnalysisDataSetBySampleIds(String[][] samplesInfo) {
		
	    double[][] data = null;
	    char[][] mask = null;
	    String[][]geneId = null;
	    int geneNo=0;
	    int sampleNo = samplesInfo.length; 

		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("GENELIST_INFO_RAW");
		PreparedStatement prepStmt = null;
		
		int row;
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			for(int col=0; col<sampleNo; col++) {
				prepStmt.setString(1, samplesInfo[col][0]);
				resSet = prepStmt.executeQuery();
				resSet.last();
				geneNo = resSet.getRow();
				resSet.beforeFirst();
		        if (col==0){
		        	data = new double[geneNo][sampleNo];
		        	mask = new char[geneNo][sampleNo];
		        }
		        while(resSet.next()) {
		        	row = resSet.getInt(1) - 1;
		        	data[row][col] = resSet.getFloat(2);
		        	mask[row][col] = (resSet.getString(3)).charAt(0);
		        }
			}
			
			// retrieve probe names and gene symbols
			geneId = new String[geneNo][2];
			parQ = DBQuery.getParamQuery("GENELIST_INFO_WITH_SYMBOL_RAW");
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, samplesInfo[0][0]);
			resSet = prepStmt.executeQuery();
			
			String prevProbeName = null;
			String geneSymbol = null;
			while (resSet.next()){
				row = resSet.getInt("GLI_SERIAL_NO")-1;
				geneId[row][0] = resSet.getString("GLI_PROBE_SET_NAME");  //Get probe name
				String gs = resSet.getString("GNF_SYMBOL");
				geneSymbol = (gs != null) ? gs : "";
//				System.out.print("row: " + row + "####");
//				if (geneSymbol == null) System.out.print("NULL###");
//				System.out.println("geneSymbol: " + geneSymbol);
				
				// Set gene Symbol in a comma separated list
				if (geneId[row][0].equals(prevProbeName)) {
					geneId[row][1] += ", " + geneSymbol;
				} else {
		            geneId[row][1] = geneSymbol;
				}
				
				prevProbeName = geneId[row][0];
			}
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		
	    String[][] sampleNames = new String[sampleNo][1];
	    for (int i=0; i<sampleNo; i++) {
//			sampleNames[i][0] = "Sample_"+samplesInfo[i][0];
			sampleNames[i][0] = samplesInfo[i][1];
		}

		DataSet dataSet = new DataSet(data, mask, geneNo, sampleNo, geneId, sampleNames);
		String[] arrayHeader = {"Sample ID"};
		String[] geneHeader =  {"ProbeID", "GeneSymbol"};
		boolean[] geneHeadersClickable = {false, true};
		dataSet.setGeneHeaders(geneHeader);
		dataSet.setArrayHeaders(arrayHeader);
		dataSet.setGeneHeadersClickable(geneHeadersClickable);
		
	    return dataSet;
	}
	
	/**
	 * 
	 */
	public ArrayList getAllSeriesGEOIds() {
		
		ArrayList<String> seriesGEOIds = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("SERIES_INFO");
		PreparedStatement prepStmt = null;
		
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				seriesGEOIds = new ArrayList<String>();
				resSet.beforeFirst();
				while(resSet.next()) {
					String seriesGEOId = resSet.getString(1);
					if (seriesGEOId != null && !seriesGEOId.equals("")) {
						seriesGEOIds.add(seriesGEOId);
					}
				}
			}
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return seriesGEOIds;
	}
	
	// used for microarray focus, gene list
	/**
	 * 
	 * @param componentName
	 * @return
	 */
	public ArrayList getComponentListByName(String componentName) {
		
		ArrayList<String> componentNames = new ArrayList<String>();
		componentNames.add(componentName);
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_COMPONENT_NAME");
		
		// add criteria for the quer string
		String queryString = parQ.getQuerySQL();
		queryString += "'%" + componentName + "%'";
//		System.out.println("component list query: " + queryString);
		
		// execute query
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				resSet.beforeFirst();
				while(resSet.next()) {
					String component = resSet.getString(1);
					if (component != null && !component.equals("")) {
						componentNames.add(component);
					}
				}
			}
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return componentNames;
	}
	
	/**
	 * 
	 * @param componentNames
	 * @return
	 */
	public ArrayList getGenelistHeadersByComponentNames(ArrayList componentNames) {
		
		ArrayList genelistHeaders = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_HEADER");
		
		// add where clause for the quer string
		String queryString = parQ.getQuerySQL();
		int componentNumber = componentNames.size();
		String whereClause = "";
		if (componentNumber == 1) {
			whereClause += " WHERE IGL_COMPONENT LIKE '%" + (String)componentNames.get(0) + "%' ";
		} else {
			whereClause += " WHERE (IGL_COMPONENT LIKE '%" + (String)componentNames.get(0) + "%' ";
			for (int i=1;i<componentNumber;i++) {
				whereClause += " OR IGL_COMPONENT LIKE '%" + (String)componentNames.get(i) + "%' ";
			}
			whereClause += ")";
		}
		
		queryString += (whereClause + "AND PER_OID = IGL_LAB_NAME");
//		System.out.println("genelist header by component query: " + queryString);
		
		// execute query and format the result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			genelistHeaders = formatGenelistHeaderResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return genelistHeaders;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList formatGenelistHeaderResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			ArrayList<ProcessedGeneListHeader> geneListHeaders = new ArrayList<ProcessedGeneListHeader>();
			// reset the resultSet cursor
			resSet.beforeFirst();
			while (resSet.next()) {
				ProcessedGeneListHeader geneListHeader = new ProcessedGeneListHeader();
		        geneListHeader.setSurname(resSet.getString("PER_SURNAME"));
		        geneListHeader.setLabName(Integer.toString(resSet.getInt("IGL_LAB_NAME")));
		        geneListHeader.setComponents(resSet.getString("IGL_COMPONENT"));
		        geneListHeader.setStatistics(resSet.getString("IGL_STATISTICS"));
		        geneListHeader.setDataTransformation (resSet.getString("IGL_DATA_TRANSFORMATION"));
		        geneListHeader.setTests(resSet.getInt("IGL_TESTS"));
		        geneListHeader.setSerialNo(resSet.getInt("IGL_SERIAL_NUMBER"));
		        geneListHeader.setUpRegulated(resSet.getInt("IGL_UP_REGULATED"));
		        geneListHeader.setDownRegulated(resSet.getInt("IGL_DOWN_REGULATED"));
		        geneListHeader.setFileName(resSet.getString("IGL_FILE_NAME"));
		        geneListHeader.setId(resSet.getInt("IGL_OID"));
		        geneListHeaders.add(geneListHeader);
			}
//			System.out.println("geneListHeaders size: " + geneListHeaders.size());
			return geneListHeaders;
		}
		return null;
	}
	
	/**
	 * 
	 * @param labId
	 * @return
	 */
	public ArrayList getGenelistHeadersByLabId(String labId) {
		
		ArrayList genelistHeaders = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_HEADER");
		
		// add where clause for the quer string
		String queryString = parQ.getQuerySQL();
		String whereClause = " where IGL_LAB_NAME = " + labId;
		
		queryString += (whereClause + "AND PER_OID = IGL_LAB_NAME");
//		System.out.println("genelist header by lab id query: " + queryString);
		
		// execute query and format the result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			genelistHeaders = formatGenelistHeaderResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return genelistHeaders;
	}

	/**
	 * 
	 */
	public ArrayList<SearchLink> getGenelistExternalLinks() {
		
		ArrayList<SearchLink> externalLinks = null;
		
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_EXTERNAL_LINK");
//		System.out.println("external link query: " + parQ.getQuerySQL());
		
		// execute query and format the result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			resSet = prepStmt.executeQuery();
			externalLinks = formatGenelistExternalLinkResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return externalLinks;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<SearchLink> formatGenelistExternalLinkResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			ArrayList<SearchLink> externalLinks = new ArrayList<SearchLink>();
			
			// reset the resultSet cursor
			resSet.beforeFirst();
			
			while (resSet.next()){
				SearchLink externalLink = new SearchLink();
				externalLink.setUrlPrefix(resSet.getString("URL_URL"));
				externalLink.setUrlSuffix(resSet.getString("URL_SUFFIX"));
				externalLink.setInstitue(resSet.getString("URL_INSTITUTE"));
				externalLink.setName(resSet.getString("URL_SHORT_NAME"));
				externalLinks.add(externalLink);
			}
			return externalLinks;
		}
		return null;
	}
	
	/**
	 * 
	 * @param genelistId
	 * @return
	 */
	public Object[][] getGenelistItemsByGenelistId(int genelistId) {
		
		Object[][] genelistItems = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_ITEM");
		
		// execute query and format the result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setInt(1, genelistId);
			resSet = prepStmt.executeQuery();
			
			genelistItems = formatGenelistItemResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return genelistItems;
	}
	
	/**
	 * 
	 * @param genelistId
	 * @param columnId
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public Object[][] getGenelistItemsByGenelistId(int genelistId, int columnId, boolean ascending, int offset, int num) {
		
		Object[][] genelistItems = null;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = DBQuery.getParamQuery("PROCESSED_GENELIST_ITEM");
		String query = parQ.getQuerySQL();
		String queryString = assembleQueryString(query, columnId, ascending, offset, num);
		
		// execute query and format the result
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, genelistId);
			resSet = prepStmt.executeQuery();
			
			genelistItems = formatGenelistItemResultSet(resSet);
			
			// close the db object
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return genelistItems;
	}
	
	/**
	 * 
	 * @param query
	 * @param columnId
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	private String assembleQueryString(String query, int columnId, boolean ascending, int offset, int num) {
		
		String queryString = null;
		
		// append order by clause is exists
		if (columnId != 0) { // there is an 'order by' criteria
			queryString = query + " ORDER BY ";
			
			// translate parameter into database column name
			String columnName = getGeneListColumnName(columnId);
			
			queryString += columnName + " ";
			
			if (!ascending) {
				queryString += "DESC ";
			}
			
		} else { // there's no 'order by' criteria
			queryString = query + " ";
		}
		
		// append row number restriction clause
		if (offset != 0) {
			queryString += " LIMIT " + Integer.toString(offset-1) + " ," + Integer.toString(num);
		}
		
		return queryString;
	}
	
	/**
	 * 
	 * @param columnId
	 * @return
	 */
	private String getGeneListColumnName(int columnId) {
		
		String columnName = null;

		// start to translate
		if (columnId == 1) {
			columnName = "IGI_GRP1_MEAN";
		} else if (columnId == 2) {
			columnName = "IGI_GRP2_MEAN";
		} else if (columnId == 3) {
			columnName = "IGI_GRP1_SEM";
		} else if (columnId == 4) {
			columnName = "IGI_GRP2_SEM";
		} else if (columnId == 5) {
			columnName = "IGI_RATIO";
		} else if (columnId == 6) {
			columnName = "IGI_DIRECTION";
		} else if (columnId == 7) {
			columnName = "IGI_P_VALUE";
		} else if (columnId == 8) {
			columnName = "IGI_GENE_IDENTIFIER";
		} else if (columnId == 9) {
			columnName = "IGI_OTHER_ID";
		} else if (columnId == 10) {
			columnName = "IGI_GENE_NAME";
		} else if (columnId == 11) {
			columnName = "IGI_UG_CLUSTER";
		} else if (columnId == 12) {
			columnName = "IGI_LOCUSLINK";
		} else if (columnId == 13) {
			columnName = "IGI_GENE_ID";
		} else if (columnId == 14) {
			columnName = "IGI_CHROMOSOME";
		} else if (columnId == 15) {
			columnName = "IGI_ONTOLOGIES";
		} else if (columnId == 16) {
			columnName = "IGI_ISH_ENTRY_COUNT";
		}
		return columnName;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private Object[][] formatGenelistItemResultSet(ResultSet resSet) throws SQLException {
		
		if (resSet.first()) {
			// obtain the column number and row number of the gene list item result
			ResultSetMetaData resSetMetaData = resSet.getMetaData();
			int columnCount = resSetMetaData.getColumnCount();
			resSet.last();
			int rowCount = resSet.getRow();
			
			// reset resultSet cursor and initilise object array to format genelist item result
			resSet.beforeFirst();
			Object[][] genelistItems = new Object[rowCount][columnCount];
			
			// format
			int row = 0;
			while (resSet.next()) {
				for (int col=0;col<columnCount;col++) {
					genelistItems[row][col] = resSet.getObject(col+1);
				}
				row++;
			}
			return genelistItems;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param
	 * @return
	 */
	public int getGenelistsEntryNumber(int genelistId) {
		
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("TOTAL_NUMBER_OF_pROCESSED_GENE_LIST_ITEMS");
        PreparedStatement prepStmt = null;

        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setInt(1,genelistId);
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
                result = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}
	
	
	/**
	 * @author xingjun - 21/11/2008
	 * @param symbol - gene symbol
	 * @param platformId - GEO platform id
	 * @return a list of probe set ids
	 */
	public ArrayList<String> getProbeSetIdBySymbol(String symbol, String platformId) {
		ArrayList<String> probeSetIds = null;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOL");
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, symbol);
			prepStmt.setString(2, platformId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
					probeSetIds.add(resSet.getString(1));
				}
			}
//			probeSetIds = DBHelper.formatResultSetToArrayList(resSet);
			
			// release the db object
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
			
        } catch (SQLException se) {
            se.printStackTrace();
        }
		return probeSetIds;
	}
	
	/**
	 * @author xingjun - 19/03/2009
	 * <p>modified by xingjun - 25/03/2009 - modified sql, added assemble query string method</p>
	 * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
	 * the genelist id and cluster id<p> 
	 */
	public ArrayList<String> getProbeSetIdByAnalysisGenelistId(String genelistId, 
			boolean ascending, int offset, int num) {
		if (genelistId == null || genelistId.equals("")) {
			return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
		
		ArrayList<String> probeSetIds = null;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        
        if (clstId == null) { // only genelist id passed in
            parQ =
                ArrayDBQuery.getParamQuery("GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_ID");
        } else {
            parQ =
                ArrayDBQuery.getParamQuery("GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_CLUSTER_ID");
        }
        String queryString = parQ.getQuerySQL();
//        System.out.println("getProbeSet query raw: " + queryString);
        queryString = this.assembleProbeSetIdByAnaGenelistIdQueryString(queryString, 
        		ascending, offset, num);
//        System.out.println("getProbeSet query extra: " + queryString);
//        System.out.println("getProbeSet genelistId: " + genelistId);
        PreparedStatement prepStmt = null;
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
	        if (clstId == null) {
				prepStmt.setInt(1, Integer.parseInt(glstId));
	        } else {
				prepStmt.setInt(1, Integer.parseInt(glstId));
				prepStmt.setInt(2, Integer.parseInt(clstId));
	        }
			
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
					probeSetIds.add(resSet.getString(1));
				}
			}
			// release the db object
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
		return probeSetIds;
	}
	
	/**
	 * @author xingjun - 25/03/2009
	 * @param query
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	private String assembleProbeSetIdByAnaGenelistIdQueryString(String query, 
			boolean ascending, int offset, int num) {
		String queryString = query;
		
		// append order by clause
		if (!ascending) {
			queryString += " DESC";
		}
		
		// append row number restriction clause
		if (offset != 0) {
			queryString += " LIMIT " + Integer.toString(offset-1) + " ," + Integer.toString(num);
		}
		return queryString;
	}
	
	/**
	 * @author xingjun - 26/03/2009
	 * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
	 * the genelist id and cluster id<p> 
	 */
	public String getAnalysisGenelistTitle(String genelistId) {
		if (genelistId == null || genelistId.equals("")) {
			return null;
		}
		String glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
		String clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);

		String genelistTitle = null;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (clstId == null) { // only genelist id passed in
            parQ =
                ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_TITLE");
        } else {
            parQ =
                ArrayDBQuery.getParamQuery("GET_ANALYSIS_GENELIST_CLUSTER_TITLE");
        }
        
        String queryString = parQ.getQuerySQL();
        PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
	        if (clstId == null) {
				prepStmt.setInt(1, Integer.parseInt(glstId));
	        } else {
				prepStmt.setInt(1, Integer.parseInt(clstId));
	        }
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				genelistTitle = resSet.getString(1);
			}
			// release the db object
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
		return genelistTitle;
	}
	
	/**
	 * @author xingjun - 21/11/2008
	 * <p>modified by xingjun - 11/08/2009
	 * - added master table id into the query</p>
	 * 
	 * <p>modified by xingjun - 26/08/2009 
	 * - added an extra parameter of genelist id
	 * - if the value is provided, the result should be based on the order of 
	 *   probe set id in that genelist; if the value is not provided, ignore it</p>
	 *   
	 * <p>xingjun - 04/02/2010 
	 * - added extra criteria (section). now the masterTableId consisted of masterTableId plus sectionId with underscore in between them
	 * - return median value along with the expression
	 * </p>
	 * <p>xingjun - 30/03/2010 - passed in param will become the combination of 
	 * the genelist id and cluster id<p> 
	 */
	public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
			String masterTableId, String genelistId) {
		// only go to find the expression if probe set ids are provided
		if (probeSetIds != null && probeSetIds.size() != 0 
				&& masterTableId != null && !masterTableId.equals("")) {
			HeatmapData expressions = null;
	        ResultSet resSet = null;
	        PreparedStatement prepStmt = null;
	        
	        // use specific sql based on genelistId parameter
	        ParamQuery parQ = null;
	        boolean genelistIdProvided = false; 
	        String glstId = "";
	        String clstId = "";
	        if (genelistId == null || genelistId.equals("")) {
		        parQ =
		            DBQuery.getParamQuery("ARRAY_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
	        } else {
	        	genelistIdProvided = true;
	    		glstId = AllComponentsGenelistAssembler.getGenelistIdFromClusterId(genelistId);
	    		clstId = AllComponentsGenelistAssembler.getIdFromClusterId(genelistId);
	            if (clstId == null) { // only genelist id passed in
		        	parQ = ArrayDBQuery.getParamQuery("GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
	            } else { // cluster id passed in as well
		        	parQ = ArrayDBQuery.getParamQuery("GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS_CLUSTER");
	            }
	        	
	        }
	        String querySQL = parQ.getQuerySQL();

	        // assembler query string
	        // assemble probe set id string
	        String queryString = 
	        	this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, querySQL);
// 	        System.out.println("getExpressionByGivenProbeSetIds master table id: " + masterTableId);
//	        System.out.println("getExpressionByGivenProbeSetIds genelist id: " + genelistId);
//	        System.out.println("getExpressionByGivenProbeSetIds query: " + queryString);
//	        int probeSetNumber = probeSetIds.size();
//	        String probeSetString = "WHERE PRS_PROBE_SET_ID IN (";
//	        for (int i=0;i<probeSetNumber;i++) {
//	        	probeSetString += "'" + probeSetIds.get(i) + "', ";
//	        }
//	        probeSetString = 
//	        	probeSetString.substring(0, (probeSetString.length()-2)) + ")";
//	        
//	        // assemble sql string
//	        String queryString = 
//	        	parQ.getQuerySQL().replace("WHERE PRS_PROBE_SET_ID IN", probeSetString);
// 	        System.out.println("expressionForProbeSet query:" + queryString);
	        
	        // get data
			try {
	        	// if disconnected from db, re-connected
	        	conn = DBHelper.reconnect2DB(conn);

				prepStmt = conn.prepareStatement(queryString);
				// get master table id and section id
				String[] masterTableAndSectionId = Utility.parseMasterTableId(masterTableId);
				if (genelistIdProvided) {
		            if (clstId == null) { // only genelist id passed in
						prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
						prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
						prepStmt.setInt(3, Integer.parseInt(glstId));
		            } else {
						prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
						prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
						prepStmt.setInt(3, Integer.parseInt(glstId));
						prepStmt.setInt(4, Integer.parseInt(clstId));
		            }
				} else {
					prepStmt.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
					prepStmt.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
				}
				resSet = prepStmt.executeQuery();
				int probeSetNumber = probeSetIds.size();
				expressions = 
					this.formatExpressionByProbeSetIdsResultSet(resSet, probeSetNumber);
				
				// release the db object
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	        return expressions;
		}
		return null;
	} // end of getExpressionByGivenProbeSetIds
	
	/**
	 * @author xingjun - 21/11/2008
	 * @param resSet
	 * @param probeSetNumber
	 * <p>xingjun - 04/02/2010 - added median value along with the expression</p>
	 */
	private HeatmapData formatExpressionByProbeSetIdsResultSet(ResultSet resSet, 
			int probeSetNumber) throws SQLException {
//		System.out.println("probeSetNumber: " + probeSetNumber);
		double[][] expressions = null;
		double[] medianValues = null;
		double[] stdValues = null;
		HeatmapData heatmapData = null;
		if (resSet.first()) {
			// obtain the row number of the expression result
			// and calculate column number for each row
			resSet.last();
			int recordCount = resSet.getRow();
//			System.out.println("recordCount: " + recordCount);
			int columnNumber = recordCount/probeSetNumber;
//			System.out.println("columnNumber: " + columnNumber);

			// reset resultSet cursor and initilise object array 
			// to format expression result
			expressions = new double[probeSetNumber][columnNumber];
			medianValues = new double[probeSetNumber];
			stdValues = new double[probeSetNumber];
			int rowCounter = 0;
			int rowCounterM = 0;
			int columnCounter = 0;
			resSet.beforeFirst();
			// the sequence of putting data into the double array
			// (in the case of 2 probe set ids and 15 expression groups):
			// expressions[0][0] -> expressions[1][0] ->
			// expressions[0][1] -> expressions[1][1] ->
			// ... ...
			// expressions[0][14] -> expressions[1][14]
			while (resSet.next()) {
				// put expression value into the array (start from the 2nd pos)
				double expression = resSet.getDouble(3);
				expressions[rowCounter][columnCounter] = expression;
				if (rowCounter == rowCounterM) {
					double medianVal = resSet.getDouble(4);
					double stdVal = resSet.getDouble(5);
					medianValues[rowCounterM] = medianVal;
					stdValues[rowCounterM] = stdVal;
					rowCounterM++;
				}
//				System.out.println("rowCounter: " + rowCounter);
//				System.out.println("columnCounter: " + columnCounter);
//				System.out.println("expression value: " + expression);
				rowCounter++;
				if (rowCounter == probeSetNumber) {
					columnCounter++;
					rowCounter = 0;
				}
			}
			// put the median value into the expression array
//			for (int i=0;i<probeSetNumber;i++) {
//				expressions[i][0] = medianValues[i];
//			}
			// put the expression, median, and std value into the HeatmapData object
			heatmapData = 
				new HeatmapData(expressions, medianValues, stdValues);
			
//			System.out.println("===== expression =====");
//			for (int i=0;i<expressions.length;i++) {
//				for (int j=0;j<expressions[0].length;j++) {
//					System.out.println(i+":"+j+": "+expressions[i][j]);
//				}
//			}
//			System.out.println("===== median =====");
//			for (int i=0;i<medianValues.length;i++) {
//				System.out.println(i+":"+medianValues[i]);
//			}
//			System.out.println("===== std =====");
//			for (int i=0;i<stdValues.length;i++) {
//				System.out.println(i+":"+stdValues[i]);
//			}
		}
		
		return heatmapData;
	}
	
	/**
	 * @author xingjun - 01/05/2009
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<String> formatExpressionByProbeSetIdsResultSet(ResultSet resSet) throws SQLException {
		ArrayList<String> expressions = null;
		if (resSet.first()) {
			resSet.beforeFirst();
			expressions = new ArrayList<String>();
			while (resSet.next()) {
				String expression = Double.toString(resSet.getDouble(3));
//				System.out.println("expression value: " + expression);
				expressions.add(expression);
			}
		}
		return expressions;
	}
	
	/**
	 * @author xingjun - 05/12/2008
	 * this method is used to retrieve expression value for only one component
	 * @param probeSetIds
	 * @param columnId
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	public ArrayList<String> getExpressionSortedByGivenProbeSetIds(ArrayList probeSetIds, 
			int columnId, boolean ascending, int offset, int num) {
		// only go to find the expression if probe set ids are provided
		if (probeSetIds != null && probeSetIds.size() != 0) {
			ArrayList<String> expressions = null;
	        ResultSet resSet = null;
	        PreparedStatement prepStmt = null;
	        ParamQuery parQ =
	            ArrayDBQuery.getParamQuery("COMPONENT_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
	        String querySQL = parQ.getQuerySQL();

	        // assemble query string
	        // assemble probe set id string
	        String queryString = 
	        	this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, 
	        			querySQL, columnId, ascending, offset, num);
//	        System.out.println("getExpressionSortedByGivenProbeSetIds query: " + queryString);
	        
	        // get data
			try {
            	// if disconnected from db, re-connected
            	conn = DBHelper.reconnect2DB(conn);

				prepStmt = conn.prepareStatement(queryString);
				prepStmt.setInt(1, columnId);
				resSet = prepStmt.executeQuery();
				expressions = 
					this.formatExpressionByProbeSetIdsResultSet(resSet);
				
				// release the db object
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	        return expressions;
		}
		return null;
	} // end of getExpressionByGivenProbeSetIds
	
	// backup
	public HeatmapData getExpressionByGivenProbeSetIds(ArrayList probeSetIds, 
			int columnId, boolean ascending, int offset, int num) {
		// only go to find the expression if probe set ids are provided
		if (probeSetIds != null && probeSetIds.size() != 0) {
			HeatmapData expressions = null;
	        ResultSet resSet = null;
	        PreparedStatement prepStmt = null;
	        ParamQuery parQ =
	            DBQuery.getParamQuery("ARRAY_EXPRESSION_OF_GIVEN_PROBE_SET_IDS");
	        String querySQL = parQ.getQuerySQL();

	        // assemble query string
	        // assemble probe set id string
	        String queryString = 
	        	this.assembleExpressionByGivenProbeSetIdsQueryString(probeSetIds, 
	        			querySQL, columnId, ascending, offset, num);
//	        int probeSetNumber = probeSetIds.size();
//	        String probeSetString = "WHERE PRS_PROBE_SET_ID IN (";
//	        for (int i=0;i<probeSetNumber;i++) {
//	        	probeSetString += "'" + probeSetIds.get(i) + "', ";
//	        }
//	        probeSetString = 
//	        	probeSetString.substring(0, (probeSetString.length()-2)) + ")";
//	        
//	        // assemble sql string
//	        String queryString = 
//	        	parQ.getQuerySQL().replace("WHERE PRS_PROBE_SET_ID IN", probeSetString);
//	        System.out.println("expressionForProbeSet query:" + queryString);
	        
	        // get data
			try {
            	// if disconnected from db, re-connected
            	conn = DBHelper.reconnect2DB(conn);

				prepStmt = conn.prepareStatement(queryString);
				resSet = prepStmt.executeQuery();
				int probeSetNumber = probeSetIds.size();
				expressions = 
					this.formatExpressionByProbeSetIdsResultSet(resSet, probeSetNumber);
				
				// release the db object
				DBHelper.closePreparedStatement(prepStmt);
				DBHelper.closeResultSet(resSet);
				
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	        return expressions;
		}
		return null;
	} // end of getExpressionByGivenProbeSetIds
	
	/**
	 * @author xingjun - 09/12/2008
	 * <p>modified on 01/05/2009 - only retrieve expression for one component</p>
	 * <p>xingjun - 27/10/2009 - modified order clause to make the result shows as the order of given probe set ids</p>
	 * @param probeSetIds
	 * @param querySQL
	 * @param columnId
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	private String assembleExpressionByGivenProbeSetIdsQueryString(ArrayList probeSetIds, String querySQL,
			int columnId, boolean ascending, int offset, int num) {
		String queryString = null;

		// add probe set id criteria
        if (probeSetIds != null && probeSetIds.size() != 0) {
        	String probeSetIdString = "WHERE PRS_PROBE_SET_ID IN (";
        	String probeSetIdList = "";
        	int len = probeSetIds.size();
        	for (int i=0;i<len;i++) {
        		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
        		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
        	}
        	probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
        	probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
//         	System.out.println("MTExpression probeset string: " + probeSetIdString);
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString);
        	queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
        	queryString = queryString.replace(", FIELD(PRS_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
        
        // append ascending condition
        if (!ascending) {
            queryString += " DESC ";
        }
        
		// append row number restriction clause
//		if (offset != 0) {
//			queryString += " LIMIT " + Integer.toString(offset-1) + ", " + Integer.toString(num);
//		}
        
		// return value
//         System.out.println("ArrayDAO:assembleExpressionByGivenProbeSetIdsQueryString:sql: " + queryString);
		return queryString;
	}
	
	/**
	 * @author xingjun - 09/12/2008
	 * <p>overloaded version</p>
	 * <p>xingjun - 27/10/2009 - modified order clause to make the result shows as the order of given probe set ids</p>
	 * @param probeSetIds
	 * @param querySQL
	 * @return
	 */
	private String assembleExpressionByGivenProbeSetIdsQueryString(ArrayList probeSetIds, String querySQL) {
		String queryString = null;

		// add probe set id criteria
        if (probeSetIds != null && probeSetIds.size() != 0) {
        	String probeSetIdString = "WHERE PRS_PROBE_SET_ID IN (";
        	String probeSetIdList = "";
        	int len = probeSetIds.size();
        	for (int i=0;i<len;i++) {
        		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
        		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
        	}
        	probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
        	probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
//        	System.out.println("MTExpression probeset string: " + probeSetIdString);
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", probeSetIdString);
        	queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
        	queryString = querySQL.replace("WHERE PRS_PROBE_SET_ID", "");
        	queryString = queryString.replace(", FIELD(PRS_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }
        
		// return value
//         System.out.println("ArrayDAO:assembleExpressionByGivenProbeSetIdsQueryString:sql: " + queryString);
		return queryString;
	}
	
	
	/**
	 * @author xingjun - 18/03/2009
	 */
	public Gene findGeneInfoBySymbol(ArrayList genes) {
		if (genes == null || genes.size() == 0) {
			return null;
		}
        ResultSet resSet = null;
        ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_GENE_BY_SYMBOL");
        String queryString = parQ.getQuerySQL();
        int len = genes.size();
        String symbolCriteria = "";
        String appendString = " OR GNF_SYMBOL = ";
        for (int i=0;i<len;i++) {
        	symbolCriteria += "'" + genes.get(i) + "'" +  appendString;
        }
        symbolCriteria = symbolCriteria.substring(0, (symbolCriteria.length()-appendString.length()));
        queryString += symbolCriteria;
//        System.out.println("array:findGeneInfoBySymbol query: " + queryString);
        
        PreparedStatement prepStmt = null;
        try {
            prepStmt = conn.prepareStatement(queryString);
            resSet = prepStmt.executeQuery();

            Gene gene = null;
            if (resSet.first()) {
                gene = new Gene();
                gene.setSymbol(resSet.getString(1));  
                gene.setName(resSet.getString(2));
            }
            // release db resources
            DBHelper.closePreparedStatement(prepStmt);
            return gene;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
	}
	
	/**
	 * @author xingjun - 22/07/2009
	 * 
	 */
	public Transgenic getTransgenicInfoBySubmissionId(String submissionId) {
		if (submissionId == null) {
			return null;
		}
		Transgenic transgenicInfo = null;
		ResultSet resSetTransgenic = null;
		ResultSet resSetTransgenicNote = null;
		ParamQuery parQTransgenic = ArrayDBQuery.getParamQuery("TRANSGENIC_INFO_BY_SUBMISSION_ID");
        ParamQuery parQTransgenicNote = DBQuery.getParamQuery("TRANSGENIC_NOTE");
		PreparedStatement prepStmtTransgenic = null;
        PreparedStatement prepStmtTransgenicNote = null;
		try {
			// if disconnected from db, re-connected
			conn = DBHelper.reconnect2DB(conn);
			parQTransgenic.setPrepStat(conn);
			prepStmtTransgenic = parQTransgenic.getPrepStat();
			prepStmtTransgenic.setInt(1, Integer.parseInt(submissionId.substring(7)));
//			System.out.println("ArrayDAO:getTransgenicInfoBySubmissionId:subId: " + submissionId.substring(7));
			resSetTransgenic = prepStmtTransgenic.executeQuery();
			
            // Transgenic note
            parQTransgenicNote.setPrepStat(conn);
            prepStmtTransgenicNote = parQTransgenicNote.getPrepStat();
            prepStmtTransgenicNote.setString(1, submissionId);
            resSetTransgenicNote = prepStmtTransgenicNote.executeQuery();
			transgenicInfo = 
				formatTransgenicResultSet(resSetTransgenic, resSetTransgenicNote);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmtTransgenic);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return transgenicInfo;
	}
	
	/**
	 * @author xingjun - 09/11/2009
	 */
	public Transgenic[] getTransgenicInfoBySubmissionIdBak(String submissionId) {
		if (submissionId == null) {
			return null;
		}
		Transgenic[] transgenicInfo = null;
		ResultSet resSetTransgenic = null;
		ResultSet resSetTransgenicNote = null;
		ParamQuery parQTransgenic = ArrayDBQuery.getParamQuery("TRANSGENIC_INFO_BY_SUBMISSION_ID");
        ParamQuery parQTransgenicNote = ArrayDBQuery.getParamQuery("TRANSGENIC_NOTE");
        String transgenicQueryString = parQTransgenic.getQuerySQL();
        String transgenicNoteQueryString = parQTransgenicNote.getQuerySQL();
        int subId = Integer.parseInt(submissionId.substring(7));
//        System.out.println("ArrayDAO:getTransgenicInfoBySubmissionIdBak:subId: " + subId);
//        System.out.println("ArrayDAO:getTransgenicInfoBySubmissionId:transgenicSql: " + transgenicQueryString);
//        System.out.println("ArrayDAO:getTransgenicInfoBySubmissionId:noteSql: " + transgenicNoteQueryString);
		PreparedStatement prepStmtTransgenic = null;
        PreparedStatement prepStmtTransgenicNote = null;
		try {
			// if disconnected from db, re-connected
			conn = DBHelper.reconnect2DB(conn);
			prepStmtTransgenic = conn.prepareStatement(transgenicQueryString);
			prepStmtTransgenic.setInt(1, subId);
			resSetTransgenic = prepStmtTransgenic.executeQuery();
			
            // Transgenic note
            prepStmtTransgenicNote = conn.prepareStatement(transgenicNoteQueryString);
            prepStmtTransgenicNote.setInt(1, subId);
            resSetTransgenicNote = prepStmtTransgenicNote.executeQuery();
			transgenicInfo = 
				formatTransgenicResultSetBak(resSetTransgenic, resSetTransgenicNote);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmtTransgenic);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return transgenicInfo;
	}
	
	/**
	 * @author xingjun - 22/07/2009
	 * @param resSetTransgenic
	 * @param resSetTransgenicNote
	 * @return
	 * @throws SQLException
	 */
	private Transgenic formatTransgenicResultSet(ResultSet resSetTransgenic,
			ResultSet resSetTransgenicNote) throws SQLException {
		Transgenic transgenic = null;
		if (resSetTransgenic.first()) {
    		transgenic = new Transgenic();
    		transgenic.setMutatedAlleleId(resSetTransgenic.getString(1));
    		transgenic.setMutatedAlleleName(resSetTransgenic.getString(2));
    		transgenic.setLabelProduct(resSetTransgenic.getString(4));
    		transgenic.setVisMethod(resSetTransgenic.getString(5));
    		transgenic.setPromoter(resSetTransgenic.getString(6));
    		
    		// at moment can only deal with notes with only one url embedded
    		if (resSetTransgenicNote.first()) {
                resSetTransgenicNote.beforeFirst();
                String notes = new String("");
                while (resSetTransgenicNote.next()) {
                    notes += resSetTransgenicNote.getString(1) + " ";
                }
//                ArrayList<TransgenicNote> transgenicNotes = this.formatTransgenicNote(notes.trim());
//                transgenic.setTNotes(transgenicNotes);
                transgenic.setNotes(notes);
                String notePrefix = this.getTransgenicNotePrefix(notes);
//                System.out.println("transgenic note prefix: " + notePrefix);
                transgenic.setNotePrefix(notePrefix);
                String noteUrl = this.getTransgenicNoteUrl(notes);
//                System.out.println("transgenic note url: " + notePrefix);
                transgenic.setNoteUrl(noteUrl);
                String noteUrlText = this.getTransgenicNoteUrlText(notes);
//                System.out.println("transgenic note url text: " + notePrefix);
                transgenic.setNoteUrlText(noteUrlText);
                String noteSuffix = this.getTransgenicNoteSuffix(notes);
//                System.out.println("transgenic note suffix: " + notePrefix);
                transgenic.setNoteSuffix(noteSuffix);
    		}
    		return transgenic;
    	}
    	return null;
    }
	
	/**
	 * @author xingjun - 09/11/2009
	 * <p>xingjun - 22/01/2010 - changed code for setting notes</p>
	 */
	private Transgenic[] formatTransgenicResultSetBak(ResultSet resSetTransgenic,
			ResultSet resSetTransgenicNote) throws SQLException {
		ArrayList<Transgenic> transgenicList = null;
		ArrayList<String[]> transgenicNoteList = null;
		Transgenic[] transgenics = null;
		if (resSetTransgenic.first()) {
			transgenicList = new ArrayList<Transgenic>();
			resSetTransgenic.beforeFirst();
			// get transgenic
			int serialNo = 1;
			while (resSetTransgenic.next()) {
				Transgenic transgenic = new Transgenic();
				transgenic.setMutantOid(resSetTransgenic.getInt(1));
				transgenic.setMutantType(resSetTransgenic.getString(2).trim());
				transgenic.setGeneSymbol(resSetTransgenic.getString(3).trim());
//				String geneId = resSetTransgenic.getString(4).trim();
//				if (geneId.indexOf("MGI:") != -1) { // 
//					
//				}
				transgenic.setGeneId(resSetTransgenic.getString(4).trim());
	    		transgenic.setMutatedAlleleId(resSetTransgenic.getString(5).trim());
	    		transgenic.setMutatedAlleleName(resSetTransgenic.getString(6).trim());
	    		transgenic.setLabelProduct(resSetTransgenic.getString(7).trim());
	    		transgenic.setVisMethod(resSetTransgenic.getString(8).trim());
	    		transgenic.setAlleleFirstChrom(resSetTransgenic.getString(9).trim());
	    		transgenic.setAlleleSecondChrom(resSetTransgenic.getString(10).trim());
	    		transgenic.setSerialNo(serialNo);
	    		serialNo++;
	    		transgenicList.add(transgenic);
			}
			
			// get note
    		// at moment can only deal with notes with only one url embedded
			if (resSetTransgenicNote.first()) {
				transgenicNoteList = new ArrayList<String[]>();
//				ResultSetMetaData resSetMetaData = resSetTransgenicNote.getMetaData();
//				int columnNumber = resSetMetaData.getColumnCount();
                resSetTransgenicNote.beforeFirst();
                int tempMutantOid = -1;
                int counter = 0;
            	String[] notes = null;
                while (resSetTransgenicNote.next()) {
                	int mutantOid = resSetTransgenicNote.getInt(1);
//                	System.out.println("mutantOid: " + mutantOid);
//                	System.out.println("tempMutantOid: " + tempMutantOid);
//                	System.out.println("counter: " + counter);
                	if (counter == 0) {
                    	notes = new String[2];
                    	notes[0] = Integer.toString(mutantOid);
                    	notes[1] = resSetTransgenicNote.getString(2);
                    	tempMutantOid = mutantOid;
                	} else if (mutantOid == tempMutantOid) {
                		notes[1] += resSetTransgenicNote.getString(2) + " ";
                	} else {
                		notes[1] = notes[1].trim();
//                		System.out.println("add notes!!!!!");
//                		System.out.println("ArrayDAO:formatTransgenicResultSetBak1:note id: " + notes[0]);
//                		System.out.println("ArrayDAO:formatTransgenicResultSetBak1:note val: " + notes[1] + "#");
                		transgenicNoteList.add(notes);
                		tempMutantOid = mutantOid;
                    	notes = new String[2];
                    	notes[0] = Integer.toString(mutantOid);
                    	notes[1] = resSetTransgenicNote.getString(2);
                	}
                	counter ++;
                }
        		notes[1] = notes[1].trim();
//                System.out.println("add notes!!!!!");
//        		System.out.println("ArrayDAO:formatTransgenicResultSetBak2:note id: " + notes[0]);
//        		System.out.println("ArrayDAO:formatTransgenicResultSetBak2:note val: " + notes[1] + "#");
                transgenicNoteList.add(notes);
//                System.out.println("ArrayDAO:formatTransgenicResultSetBak:note size: " + transgenicNoteList.size());
                
    			// put note info into the transgenic
    			int transgenicLen = transgenicList.size();
    			for (int i=0;i<transgenicLen;i++) {
    				int noteLen = transgenicNoteList.size();
    				int transgenicMutantOid = transgenicList.get(i).getMutantOid();
    				for (int j=0;j<noteLen;j++) {
    					int noteMutantOid = Integer.parseInt(transgenicNoteList.get(j)[0]);
    					String tNote = transgenicNoteList.get(j)[1];
    					if (noteMutantOid == transgenicMutantOid && tNote != null) {
    						transgenicList.get(i).setNotes(tNote);
//    						// modified by xingjun - 22/01/2010
    						// jsf escape attibute of outputText can be used to display url hyperlinks
//    						// - modified to accept note string with more than one url links
//    		                String notePrefix = this.getTransgenicNotePrefix(tNote);
////   		                System.out.println("transgenic note prefix: " + notePrefix);
//    		                transgenicList.get(i).setNotePrefix(notePrefix);
//    						
//    		                String noteUrl = this.getTransgenicNoteUrl(tNote);
////    		           System.out.println("transgenic note url: " + noteUrl);
//   		                transgenicList.get(i).setNoteUrl(noteUrl); 
    						
//    		                String noteUrlText = this.getTransgenicNoteUrlText(tNote);
////    		              System.out.println("transgenic note url text: " + noteUrlText);
//    		                transgenicList.get(i).setNoteUrlText(noteUrlText);
//    		                
//    		                String noteSuffix = this.getTransgenicNoteSuffix(tNote);
////    		           System.out.println("transgenic note suffix: " + noteSuffix);
//    		                transgenicList.get(i).setNoteSuffix(noteSuffix);
    						
    						//Bernie - 23/11/2010 (mantis 336)
    						String pubUrl = getTransgenicPubmedUrl(tNote);
    						if (pubUrl == "")
    							transgenicList.get(i).setNoteUrl(pubUrl);
    						else
    							transgenicList.get(i).setPubUrl(pubUrl);
    					}
    				}
    			}
			} // get note & put note info into transgenic object
    		return transgenicList.toArray(new Transgenic[transgenicList.size()]);
    	} // get transgenic
    	return null;
    } // end of formatTransgenicResultSetBak
	
//	String addUrlIntoAccessionIdString(String str, String url, String accessionPrefix) {
//		if (str.indexOf(accessionPrefix) == -1) {
//			return str;
//		}
//		String[] splitString = str.split(accessionPrefix);
//		
//		return null;
//	}
	
	/**
	 * @author xingjun - 23/07/2009
	 * @param transgenicNote
	 * @return
	 */
	private String getTransgenicNotePrefix(String transgenicNote) {
		if (transgenicNote.indexOf("<a href") == -1) { // no url in the note text - return whole string
			return transgenicNote;
		}
		// find link
		int linkStartPos = transgenicNote.indexOf("<a href");
		if (linkStartPos == 0) { // note start with link: no prefix string
			return "";
		}
		return transgenicNote.substring(0, linkStartPos);
	}
	
	/**
	 * @author xingjun - 23/07/2009
	 * @param transgenicNote
	 * @return
	 */
	private String getTransgenicNoteUrl(String transgenicNote) {
		if (transgenicNote.indexOf("<a href") == -1) { // no url in the note text - return empty string
			return "";
		}
		// find link
		int linkStartPos = transgenicNote.indexOf("<a href");
		
		// find url
		int urlStartPos = linkStartPos + 11;
		int urlEndPos = transgenicNote.indexOf("\">");
		
		return transgenicNote.substring(urlStartPos, urlEndPos);
	}

	// Bernie 25/11/2010 - added method (mantis 336)
	private String getTransgenicPubmedUrl(String transgenicNote) {
		if (transgenicNote.indexOf("PMID") == -1)
		{
			return "";
		}
		
		String numeral = transgenicNote.substring(6);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("http://www.ncbi.nlm.nih.gov/pubmed/");
		strBuf.append(numeral);
		return strBuf.toString();
	}

	/**
	 * @author xingjun - 23/07/2009
	 * @param transgenicNote
	 * @return
	 */
	private String getTransgenicNoteUrlText(String transgenicNote) {
		if (transgenicNote.indexOf("<a href") == -1) { // no url in the note text - return empty string
			return "";
		}
		// find link
		int linkEndPos = transgenicNote.indexOf("</a>");
		
		// find url
		int urlEndPos = transgenicNote.indexOf("\">");
		
		return transgenicNote.substring(urlEndPos+2, linkEndPos);
	}
	
	/**
	 * @author xingjun - 23/07/2009
	 * @param transgenicNote
	 * @return
	 */
	private String getTransgenicNoteSuffix(String transgenicNote) {
		// if there's no url in the note text - 
		// put whole string into note prefix and return empty string for note suffix
		if (transgenicNote.indexOf("<a href") == -1) {
			return "";
		}
		// find link
		int linkEndPos = transgenicNote.indexOf("</a>");
		
		int len = transgenicNote.length();
		
		return transgenicNote.substring(linkEndPos+4, len);
	}
	
	/**
	 * @author xingjun - 22/07/2009
	 * @param notes
	 * @return
	 */
//	private ArrayList<TransgenicNote> formatTransgenicNote(String notes) {
//		if (notes == null || notes.length() == 0) {
//			return null;
//		}
//		ArrayList<TransgenicNote> result = new ArrayList<TransgenicNote>();
//		
//		if (notes.indexOf("<a href") == -1) { // no url in the note text
//			TransgenicNote transgenicNote = new TransgenicNote();
//			transgenicNote.setContentIndex(1);
//			transgenicNote.setContentType("plain_text");
//			transgenicNote.setContentText(notes);
//			result.add(transgenicNote);
//			return result;
//		}
//		String noteString = notes;
//		
//		// parse the note string to separate plain text with url links
//		// assemble it to an arraylist of transgenic note object
//		int indx = 0;
//		while (true) {
//			TransgenicNote transgenicNote = new TransgenicNote();
//			// find link
//			int linkStartPos = noteString.indexOf("<a href");
//			int linkEndPos = noteString.indexOf("</a>");
//			
//			// find url
//			int urlStartPos = linkStartPos + 11;
//			int urlEndPos = noteString.indexOf("\">");
//			
//			if (linkStartPos == 0) { // note string starts with link string: add url and substract the string
//				transgenicNote.setContentIndex(indx);
//				transgenicNote.setContentType("url");
//				transgenicNote.setContentText(noteString.substring(urlEndPos+2, linkEndPos));
//				transgenicNote.setUrlString(noteString.substring(urlStartPos, urlEndPos));
//				result.add(transgenicNote);
//				indx++; // pointer plus 1
//				
//				// subtract string
//				if (linkEndPos+3 < noteString.length()) {
//					noteString = noteString.substring(linkEndPos+4);
//				} else { // reach the end of the string
//					break;
//				}
//			} else { // note string starts with plain text: add plain text and url and substract the string
//				// add plain text
//				transgenicNote.setContentIndex(indx);
//				transgenicNote.setContentType("plain_text");
//				transgenicNote.setContentText(noteString.substring(0, linkStartPos));
//				result.add(transgenicNote);
//				indx++; // pointer plus 1
//				
//				// add link string
//				transgenicNote.setContentIndex(indx);
//				transgenicNote.setContentType("url");
//				transgenicNote.setContentText(noteString.substring(urlEndPos+2, linkEndPos));
//				transgenicNote.setUrlString(noteString.substring(urlStartPos+11, urlEndPos));
//				result.add(transgenicNote);
//				indx++; // pointer plus 1
//				
//				// substract string
//				if (linkEndPos+3 < noteString.length()) {
//					noteString = noteString.substring(linkEndPos+4);
//				} else { // reach the end of the string
//					break;
//				}
//			}
//		}
//		return result;
//	}
	
	
	/**
	 * @author xingjun - 11/08/2009
	 */
	public MasterTableInfo[] getMasterTableList(boolean isMaster) {
		MasterTableInfo[] masterTableList = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_TABLE_LIST");
		PreparedStatement prepStmt = null;
		try {
			// if disconnected from db, re-connected
			conn = DBHelper.reconnect2DB(conn);
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			resSet = prepStmt.executeQuery();
			
			masterTableList = formatMasterTableResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return masterTableList;
	}
	
	/**
	 * @author xingjun - 11/08/2009
	 * <p>xingjun - 03/02/2010 - added an extra property for master table entity</p>
	 */
	private MasterTableInfo[] formatMasterTableResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			resSet.beforeFirst();
			ArrayList<MasterTableInfo> masterTableList = new ArrayList<MasterTableInfo>();
			while (resSet.next()) {
				MasterTableInfo masterTableInfo = new MasterTableInfo();
				masterTableInfo.setId(resSet.getString(1));
				masterTableInfo.setMasterId(resSet.getString(1));// xingjun - 02/03/2010
				masterTableInfo.setPlatform(resSet.getString(2));
				masterTableInfo.setTitle(resSet.getString(3));
				masterTableInfo.setDescription(resSet.getString(4));
				masterTableList.add(masterTableInfo);
			}
			return masterTableList.toArray(new MasterTableInfo[masterTableList.size()]);
		}
		return null;
	}
	
	/**
	 * @author xingjun - 03/02/2010
	 * @return
	 */
	public MasterTableInfo[] getMasterTableList() {
		MasterTableInfo[] masterSectionList = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_SECTION_LIST");
		PreparedStatement prepStmt = null;
		try {
			// if disconnected from db, re-connected
			conn = DBHelper.reconnect2DB(conn);
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			resSet = prepStmt.executeQuery();
			
			masterSectionList = formatMasterSectionResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return masterSectionList;
	}
	
	/**
	 * @author xingjun - 03/02/2010
	 */
	private MasterTableInfo[] formatMasterSectionResultSet(ResultSet resSet) throws SQLException {
		if (resSet.first()) {
			resSet.beforeFirst();
			ArrayList<MasterTableInfo> masterTableList = new ArrayList<MasterTableInfo>();
			while (resSet.next()) {
				MasterTableInfo masterTableInfo = new MasterTableInfo();
				masterTableInfo.setId(resSet.getString(1) + "_" + resSet.getString(2));
				masterTableInfo.setMasterId(resSet.getString(1));
				masterTableInfo.setSectionId(resSet.getString(2));
				masterTableInfo.setTitle(resSet.getString(3));
				masterTableInfo.setDescription(resSet.getString(4));
				masterTableInfo.setPlatform(resSet.getString(5));
				masterTableList.add(masterTableInfo);
			}
			return masterTableList.toArray(new MasterTableInfo[masterTableList.size()]);
		}
		return null;
	}
	
	/**
	 * <p>xingjun - 03/02/2010 
	 * - need parse input masterTableId before proceeding to retrieve platform id</p>
	 */
	public String getMasterTablePlatformId(String masterTableId) {
		String platformId = null;
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("MASTER_TABLE_PLATFORM_ID");
		PreparedStatement prepStmt = null;
		try {
			// if disconnected from db, re-connected
			conn = DBHelper.reconnect2DB(conn);
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			// xingjun - 03/02/2010 - start
			String masterId = Utility.parseMasterTableId(masterTableId)[0];
			prepStmt.setInt(1, Integer.parseInt(masterId));
			// xingjun - 03/02/2010 - finish
			
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				platformId = resSet.getString(1);
			}
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return platformId;
	}
	
	/**
	 * @author xingjun - 24/08/2009
	 */
	public ArrayList<String> getProbeSetIdBySymbols(String[] symbols, String platformId) {
		ArrayList<String> probeSetIds = null;
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        ParamQuery parQ =
            DBQuery.getParamQuery("ARRAY_PROBE_SET_IDS_FOR_GIVEN_SYMBOL");
        String queryString = parQ.getQuerySQL();

        // put symbol criteria into the query string
        String symbolString = "('";
        for (int i=0;i<symbols.length;i++) {
        	symbolString += symbols[i] + "', '";
        }
        symbolString = symbolString.substring(0, symbolString.length()-3) + ")";
//        System.out.println("getProbeSetIdBySymbols:symbolString: " + symbolString);
        queryString = queryString.replaceAll("WHERE GNF_SYMBOL = \\?", "WHERE GNF_SYMBOL IN"+symbolString );
//        System.out.println("getProbeSetIdBySymbols:queryString: " + queryString);
        
		try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, platformId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				resSet.beforeFirst();
				probeSetIds = new ArrayList<String>();
				while (resSet.next()) {
					probeSetIds.add(resSet.getString(1));
				}
			}
			
			// release the db object
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
			
        } catch (SQLException se) {
            se.printStackTrace();
        }
		return probeSetIds;
	}
	
	
	/** --- to be implemented--- */
	public ArrayList getMicroGeneRelatedSubmissions(String symbol) throws SQLException {
		return new ArrayList();
	}
	
	public ArrayList getMicroLabSubmission(int single, String order, String labId, String date) throws SQLException {
		return new ArrayList();
	}
	
	public ArrayList getMicroSubmissionDetails(String id) throws SQLException {
		return new ArrayList();
	}
	
	public ArrayList getMicroSeries(String id) throws SQLException {
		return new ArrayList();
	}
	
	public ArrayList getGeneCollectionDetails(String[] geneCookieList) throws SQLException {
		return new ArrayList();
	}
	
	public ArrayList getMicroGeneList(String order, String id, int pNum, int resPerPage) throws SQLException {
		return new ArrayList();
	}

	// added by Bernie - 23/09/2010
	public String findTissueBySubmissionId(String submissionId)
	{
//		System.out.println("MySQLArrayDAOImp-findTissueBySubmissionId for " + submissionId);
		String tissue = null;
        ResultSet resultSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("GET_SUBMISSION_TISSUE");
        PreparedStatement prepStmt = null;        
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

            parQ.setPrepStat(conn);
            prepStmt = parQ.getPrepStat();
            prepStmt.setString(1, submissionId);
            resultSet = prepStmt.executeQuery();
            if (resultSet.first()) {
                tissue = resultSet.getString(1);
//                System.out.println("tissue: " + tissue);
            }
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            return tissue;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
	}
	
}
