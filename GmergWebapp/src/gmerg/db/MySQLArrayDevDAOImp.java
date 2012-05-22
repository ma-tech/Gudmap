/**
 * 
 */
package gmerg.db;

import gmerg.entities.submission.array.Series;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

/**
 * @author xingjun 
 *
 */
public class MySQLArrayDevDAOImp implements ArrayDevDAO {
	
	private Connection conn; 

	// default constructor
	public MySQLArrayDevDAOImp() {
		
	}
	
	public MySQLArrayDevDAOImp(Connection conn) {
		this.conn = conn;
	}

	/**
	 * <p>xingjun - 14/12/2009 - changed the default order (refer to the code below)</p>
	 * <p>xingjun - 03/02/2010 - changed the way to append platform criteria into the query string</p>
	 * <p>xingjun - 03/03/2010 - changed the code to assemble sql: used to append clauses at the end of sql. now replace existing one</p>
	 * <p>xingjun - 23/06/2011 - changed the query to be able to pick up multiple PIs</p>
	 */
	public ArrayList getAllSeries(int columnIndex, boolean ascending, int offset, int num, String platform) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
                //find relevant query string from DBQuery
//		ParamQuery parQ = DBQuery.getParamQuery("ALL_SERIES");
		ParamQuery parQ = ArrayDBQuery.getParamQuery("ALL_SERIES");// xingjun - 23/06/2011
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		if(null != platform && !platform.equals("")) {
			String platformString = " and SER_PLATFORM_FK=PLT_OID and PLT_GEO_ID=? ";
			query = query.replaceAll("AND SER_PLATFORM_FK", platformString);
		} else {
			query = query.replaceAll("AND SER_PLATFORM_FK", "");
		}
		
		// xingjun - 14/12/2009 - changed the default order as editors requested
//		String defaultOrder = DBQuery.ORDER_BY_EXPERIMENT_NAME;
		String defaultOrder = DBQuery.ORDER_BY_LAB_AND_EXPERIMENT;
		String queryString = assembleBrowseSerieseQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//		System.out.println("ArrayDevDAO:getAllSeries:sql: " + queryString);
		
		// execute the query
		try {
//			parQ.setPrepStat(conn);
			prepStmt = conn.prepareStatement(queryString);
			if(null != platform && !platform.equals("")) {
				prepStmt.setString(1, platform);
			}
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			DBHelper.closePreparedStatement(prepStmt);
//			parQ.setQuerySQL(query);
			
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;
	}


    /**
     * queries database and formats results to be used to build table of 
     * sample data for a particular series
     * @param id - the identifier of the specified series
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return result - an ArrayList of data to be used to build a table fo sample data
     */
    public ArrayList getSamplesForSeries(String id, int columnIndex,
                                         boolean ascending, int offset,
                                         int num) {
        ArrayList result = null;
        ResultSet resSet = null;
        //find relevant query string from db query
        ParamQuery parQ = DBQuery.getParamQuery("SERIES_SAMPLES");
        PreparedStatement prepStat = null;

        String query = parQ.getQuerySQL();
        String defaultOrder = " CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,':')+1) AS UNSIGNED) ";
        String queryString =
            assembleSeriesSamplesQString(query, defaultOrder, columnIndex,
                                         ascending, offset, num);
//        System.out.println("ArrayDevDAO:getSamplesForSeries:sql: " + queryString);

        //try to execute the query
        try {
            prepStat = conn.prepareStatement(queryString);
            prepStat.setString(1, id);

            resSet = prepStat.executeQuery();
            result = formatBrowseSeriesResultSet(resSet);

            DBHelper.closePreparedStatement(prepStat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * @author xingjun - 06/07/2009
     */
    public ArrayList getSamplesBySeriesOid(String oid, int columnIndex,
            boolean ascending, int offset, int num) {
    	ArrayList result = null;
    	ResultSet resSet = null;
    	
    	//find relevant query string from db query
    	ParamQuery parQ = ArrayDBQuery.getParamQuery("SERIES_SAMPLES_BY_OID");
    	PreparedStatement prepStat = null;
    	
    	String query = parQ.getQuerySQL();
    	String defaultOrder = " CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,':')+1) AS UNSIGNED) ";
    	String queryString = assembleSeriesSamplesQString(query, defaultOrder, 
    			columnIndex, ascending, offset, num);
//    	System.out.println("ArrayDevDAO:getSamplesBySeriesOid:sql: " + queryString);
//    	System.out.println("ArrayDevDAO:getSamplesBySeriesOid:seriesOid: " + oid);
    	
    	//try to execute the query
    	try {
    		prepStat = conn.prepareStatement(queryString);
    		prepStat.setInt(1, Integer.parseInt(oid));
    		
    		resSet = prepStat.executeQuery();
    		result = formatBrowseSeriesResultSet(resSet);
    		
    		DBHelper.closePreparedStatement(prepStat);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return result;
    }

    /**
     * assembles the query string used to query the db to find data on samples
     * for a particular series
     * @param query
     * @param defaultOrder
     * @param columnIndex
     * @param ascending
     * @param offset
     * @param num
     * @return queryString - the String used to query the db 
     */
    private String assembleSeriesSamplesQString(String query,
                                                String defaultOrder,
                                                int columnIndex,
                                                boolean ascending, int offset,
                                                int num) {
        StringBuffer queryString = new StringBuffer("");

        //orderBy
        if (columnIndex != -1) {
            //add code here to make table columns sortable
        	queryString.append(query + " ORDER BY ");
        	String column = this.getSeriesSampleOrderByColumn(columnIndex, ascending, defaultOrder);
        	queryString.append(column);
        } else {
            queryString.append(query + " ORDER BY " + defaultOrder);
        }

        queryString.append(" LIMIT " + offset + ", " + num);

        return queryString.toString();
    }
    
    /**
     * <p>xingjun - 11/05/2010 - appended the natural_sort to the sql to make the sorting more 'natural'</p>
     * @param columnIndex
     * @param ascending
     * @param defaultOrder
     * @return
     */
	private String getSeriesSampleOrderByColumn(int columnIndex, boolean ascending, String defaultOrder) {
    	
		String orderByString = new String("");
		String order = (ascending == true ? "ASC": "DESC");
//		String[] seriesSampleColumnList = {"SUB_ACCESSION_ID", "SMP_GEO_ID", "SRM_SAMPLE_ID", "SRM_SAMPLE_DESCRIPTION"};
		
		// start to translate
		if (columnIndex == 1) {
			orderByString = "SMP_GEO_ID " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) {
//			orderByString = "SRM_SAMPLE_ID " + order + ", " + defaultOrder;
			orderByString = "natural_sort(SRM_SAMPLE_ID) " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
//			orderByString = "SRM_SAMPLE_DESCRIPTION " + order + ", " + defaultOrder;
			orderByString = "natural_sort(SRM_SAMPLE_DESCRIPTION) " + order + ", " + defaultOrder;
		} else {
			orderByString = defaultOrder + order;
		}
		return orderByString;
    }
	
	/**
	 * 
	 * @param query
	 * @param defaultOrder
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */	
	private String assembleBrowseSerieseQueryString(String query, String defaultOrder, 
			int columnIndex, boolean ascending, int offset, int num) {

		String queryString = null;
		
		// order by
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			
			// translate parameters into database column names
			String column = getBrowseSeriesOrderByColumn(columnIndex, ascending);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by experiment name and geo id by default
			queryString = query + defaultOrder+ ", NATURAL_SORT(TRIM(SER_GEO_ID))";
		}
		
		// offset and retrieval number
		queryString = queryString + " LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * <p>modified by xingjun - 12/04/2009 - orderByString for lab should be PER_SURNAME, not LAB</p>
	 * @param columnIndex
	 * @param ascending
	 * @return
	 */
	private String getBrowseSeriesOrderByColumn(int columnIndex, boolean ascending) {
		
		String orderByString = new String("");
		String defaultOrder = " NATURAL_SORT(TRIM(SER_TITLE)) ";
		String order = (ascending == true ? "ASC": "DESC");
//		String[] browseSeriesColumnList = {"NATURAL_SORT(TRIM(SER_TITLE))", "SER_TYPE", "NATURAL_SORT(TRIM(SER_GEO_ID))"};
//		String[] browseSeriesColumnList = {"NATURAL_SORT(TRIM(SER_TITLE))", "LAB", 
//				"NATURAL_SORT(TRIM(PLT_GEO_ID))", "NATURAL_SORT(TRIM(SER_GEO_ID))", "SAMPLE_NUMBER"};

		// start to translate
		if (columnIndex == 0) {
			orderByString = defaultOrder + order;
		} else if (columnIndex == 1) {
			orderByString = "NATURAL_SORT(TRIM(SER_GEO_ID)) " + order + ", " + defaultOrder;
		} else if (columnIndex == 2) {
			orderByString = "SAMPLE_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "PER_SURNAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "NATURAL_SORT(TRIM(PLT_GEO_ID)) " + order + ", " + defaultOrder;
		}
		return orderByString;
	}
	
	/**
	 * 
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private ArrayList formatBrowseSeriesResultSet(ResultSet resSet) throws SQLException {
		
   		ArrayList<String[]> results = null;
		ResultSetMetaData resSetMetaData = resSet.getMetaData();
   		int columnCount = resSetMetaData.getColumnCount();

		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			results = new ArrayList<String[]>();
			
			while(resSet.next()) {
				String[] columns = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
		        }
		        results.add(columns);
			}
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see gmerg.db.ArrayDevDAO#getStringArrayFromBatchQuery(java.lang.String[][], java.lang.String[])
	 */
	public String[][] getStringArrayFromBatchQuery(String[][] param, 
			String[] query) {
		// TODO Auto-generated method stub
    	return getStringArrayFromBatchQuery(param, query, "");
	}
	
	/**
	 * based on the specified organ value to append join clause and component related criteria
	 * @author xingjun
	 * @param param
	 * @param query - query name
	 * @param organ 
	 */
	public String[][] getSeriesBrowseTotals(String[][] param,
			String[] query, String organ) {
		String joinClause = "JOIN MIC_SERIES_SAMPLE ON SRM_SERIES_FK = SER_OID " +
	  		"JOIN MIC_SAMPLE ON SRM_SAMPLE_FK= SMP_OID " +
	  		"JOIN ISH_SUBMISSION ON SMP_SUBMISSION_FK = SUB_OID " +
	  		"JOIN ISH_PERSON ON SUB_PI_FK = PER_OID ";
	  
		String componentClause = "";
		if(null != organ && !organ.equals("")) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
			String ids = "";
			if(null != emapids) {
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'" + emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			  componentClause += "JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " +
			  		"AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
			  "from ANA_TIMED_NODE ANCES_ATN, " +
			  "ANAD_RELATIONSHIP_TRANSITIVE, " +
			  "ANA_TIMED_NODE DESCEND_ATN, " +
			  "ANA_NODE, " +
			  "ANAD_PART_OF " +
			  "where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") " +
			  "and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK " +
			  "and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK " +
			  "and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK " +
			  "and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
			  "and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
			  "and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
		}
		return getStringArrayFromBatchQuery(param, query, (joinClause + componentClause));
	}

	
    /**
	 * @param param - string value to specify the criteria of the query therein
	 * @param query - query names
	 * @param endingClause - a string to append at the end of all queries
	 * @return query names and query results stored in a 2-dim array
	 */
    public String[][] getStringArrayFromBatchQuery(String[][] param,
                                                   String[] query, String endingClause) {

        int queryNumber = query.length;
        String[][] result = new String[queryNumber][2];
        String[] queryString = new String[queryNumber];

        // get the query sql based on query name and record query name in result string array
        for (int i = 0; i < queryNumber; i++) {
            ParamQuery parQ = DBQuery.getParamQuery(query[i]);
            queryString[i] = parQ.getQuerySQL() + endingClause; 
//            System.out.println("Q: "  + queryString[i]);
            result[i][0] = query[i];
        }

        // start to execute query
        ResultSet resSet = null;
        PreparedStatement prepStmt = null;
        try {
            for (int i = 0; i < queryNumber; i++) {
                prepStmt = conn.prepareStatement(queryString[i]);
                if (param != null &&
                    param[i] != null) { // set query criteria if it's not null
                    int parameterNumber = param[i].length;
                    for (int j = 0; j < parameterNumber; j++) {
                        prepStmt.setString(j + 1, param[i][j]);
                    }
                }
                resSet = prepStmt.executeQuery();
                result[i][1] = getStringValueFromIntegerResultSet(resSet);
            }
            // release the database resources
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
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
    	} catch (SQLException se) {
    		se.printStackTrace();
    	}
    	return null;
    }
  

	/**
	 * <p>xingjun - 14/04/2010 - changed to use the same sql as the method getAllSeries </p>
	 * <p>xingjun - 23/06/2011 - get the sql from ArrayDBQuery now</p>
	 * <p>xingjun - 22/07/2011 - correct the sql criteria for platform parameter </p>
	 */
	public int getTotalNumberOfSeries(String platform) {
        int totalNumber = 0;
        ResultSet resSet = null;
//        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SERIES");
//        ParamQuery parQ = DBQuery.getParamQuery("ALL_SERIES");
        ParamQuery parQ = ArrayDBQuery.getParamQuery("ALL_SERIES");// xingjun - 23/06/2011
        PreparedStatement prepStmt = null;
        String query = parQ.getQuerySQL();
        
        try {
//            if(null != platform && !platform.equals("")) {
//    			query += " WHERE SER_PLATFORM_FK=PLT_OID and PLT_GEO_ID=? ";
//    		}

            if(null != platform && !platform.equals("")) {
//    			String platformString = " and SER_PLATFORM_FK=PLT_OID and PLT_GEO_ID=? ";
    			String platformString = "AND PLT_GEO_ID = ? "; // xingjun - 22/07/2011
    			query = query.replaceAll("AND SER_PLATFORM_FK", platformString);
    		} else {
    			query = query.replaceAll("AND SER_PLATFORM_FK", "");
    		}
            
            query = "SELECT COUNT(*) FROM (" + query + ") AS S_TABLE";
//    		System.out.println("ArrayDevDAO:getTotalNumberOfSeries:sql: " + query);
            prepStmt = conn.prepareStatement(query);
            if(null != platform && !platform.equals("")) {
				prepStmt.setString(1, platform);
			}
            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
                totalNumber = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            //			DBUtil.closeJDBCConnection(conn);

        } catch (SQLException se) {
            se.printStackTrace();
        }
		return totalNumber;
	}
	
	/**
	 * @author ying
	 * <p>added extra column description into the result</p>
	 * <p>xingjun - 12/08/2010 - added archive id column</p>
	 */
	public Series findSeriesById(String id) {
		if (id == null) {
			return null;
		}
		Series series = null;
		ResultSet resSetSeries = null;
//		ResultSet resSetSampleNumber = null;
		ParamQuery parQSeries = DBQuery.getParamQuery("SERIES_DATA");
		PreparedStatement prepStmtSeries = null;
		try {
			conn.setAutoCommit(false);
			
			// series
			parQSeries.setPrepStat(conn);
			prepStmtSeries = parQSeries.getPrepStat();
			prepStmtSeries.setString(1, id);
			resSetSeries = prepStmtSeries.executeQuery();
			
			// assemble
			if(resSetSeries.first()){
				series = new Series();
				series.setGeoID(resSetSeries.getString(1));
				series.setNumSamples(resSetSeries.getString(2));
				series.setTitle(resSetSeries.getString(3));
				series.setSummary(resSetSeries.getString(4));
				series.setType(resSetSeries.getString(5));
				series.SetDesign(resSetSeries.getString(6));
				series.SetDescription(resSetSeries.getString(7));
				series.setArchiveId(resSetSeries.getInt(8)); // xingjun - 12/08/2010
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmtSeries);
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return series;
	}
	
	/**
	 * @author xingjun - 06/07/2009
	 * <p>modified by xingjun 30/09/2009 - if non-integer param passed in, return null</p>
	 * <p>xingjun  - 04/03/2010 - added property series description into result</p>
	 */
	public Series findSeriesByOid(String oid) {
		if (oid == null) {
			return null;
		}
//		System.out.println("ArrayDevDAO:findSeriesByOid:oid: " + oid);
		int seriesOid = -1;
		try {
			seriesOid = Integer.parseInt(oid);
		} catch (NumberFormatException nfe) {
			seriesOid = 0;
		}
		Series series = null;
		ResultSet resSetSeries = null;
		ParamQuery parQSeries = ArrayDBQuery.getParamQuery("SERIES_DATA_BY_OID");
		PreparedStatement prepStmtSeries = null;
//		System.out.println("ArrayDevDAO:findSeriesByOid:sql: " + parQSeries.getQuerySQL());
//		System.out.println("ArrayDevDAO:findSeriesByOid:seriesOid: " + oid);
		try {
			// series
			parQSeries.setPrepStat(conn);
			prepStmtSeries = parQSeries.getPrepStat();
//			prepStmtSeries.setInt(1, Integer.parseInt(oid));
			prepStmtSeries.setInt(1, seriesOid);
			resSetSeries = prepStmtSeries.executeQuery();
			
			// assemble
			if(resSetSeries.first()){
				series = new Series();
				series.setGeoID(resSetSeries.getString(1));
				series.setNumSamples(resSetSeries.getString(2));
				series.setTitle(resSetSeries.getString(3));
				series.setSummary(resSetSeries.getString(4));
				series.setType(resSetSeries.getString(5));
				series.SetDesign(resSetSeries.getString(6));
				series.SetDescription(resSetSeries.getString(8));
				series.setArchiveId(resSetSeries.getInt(9)); 
			}
			// release the db objects
			DBHelper.closePreparedStatement(prepStmtSeries);
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return series;
	}
	
	public ArrayList findOriginalImagesById(String submissionAccessionId) {
		if (submissionAccessionId == null) {
//			throw new NullPointerException("id parameter");
			return null;
		}
		ArrayList ImageInfo = null;
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("MIC_IMAGES");
		PreparedStatement prepStmt = null;
		try {
			parQ.setPrepStat(conn);
			prepStmt = parQ.getPrepStat();
			prepStmt.setString(1, submissionAccessionId);
			
			// execute
			resSet = prepStmt.executeQuery();
			ImageInfo = formatImageResultSet(resSet);
			
			// close the connection
			DBHelper.closePreparedStatement(prepStmt);
//			DBUtil.closeJDBCConnection(conn);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return ImageInfo;
	}
	
	/**
   	 *
   	 * @param resSetImage
   	 * @return
   	 * @throws SQLException
   	 */
	private ArrayList formatImageResultSet(ResultSet resSetImage) throws SQLException {
		ResultSetMetaData resSetData = resSetImage.getMetaData();
		int columnCount = resSetData.getColumnCount();
		if (resSetImage.first()) {
			resSetImage.beforeFirst();
			int serialNo = 1;
			ArrayList<String[]> results = new ArrayList<String[]>();
			while (resSetImage.next()) {
				String[] columns = new String[columnCount + 1];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSetImage.getString(i + 1);
				}
				columns[columnCount] = String.valueOf(serialNo);
				serialNo++;
//				resSetImage.getString(1).replaceAll("\\W", "");
				results.add(columns);
			}
			return results;
		}
		return null;
	}
	
	/**
	 * @author xingjun - 02/09/2009
	 * @param candidateGudmapIds
	 * @return
	 */
	public ArrayList<String> getRelevantGudmapIds(ArrayList candidateGudmapIds) {
		if (candidateGudmapIds == null || candidateGudmapIds.size() == 0) {
			return null;
		}
		ArrayList<String> relevantGudmapIds = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = InsituDBQuery.getParamQuery("GET_RELEVANT_SUBMISSION_IDS");
		String queryString = parQ.getQuerySQL();
		String gudmapIdString = DBHelper.convertItemsFromArrayListToString(candidateGudmapIds);
		queryString = queryString.replace("SUB_ACCESSION_ID IN", "SUB_ACCESSION_ID IN (" +gudmapIdString + ")");
//		System.out.println("ArrayDevDAO:getRelevantGudmapIds:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantGudmapIds.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return relevantGudmapIds;
	}
	
	/**
	 * @author xingjun - 13/10/2009
	 * get viewable gudmapIds
	 * users with different privileges will be able to see entries in different status
	 */
	public ArrayList<String> getRelevantGudmapIds(ArrayList candidateGudmapIds, int userPrivilege) {
		if (candidateGudmapIds == null || candidateGudmapIds.size() == 0) {
			return null;
		}
		ArrayList<String> relevantGudmapIds = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = InsituDBQuery.getParamQuery("GET_RELEVANT_SUBMISSION_IDS");
		String queryString = parQ.getQuerySQL();
		String gudmapIdString = DBHelper.convertItemsFromArrayListToString(candidateGudmapIds);
		queryString = queryString.replace("SUB_ACCESSION_ID IN", "SUB_ACCESSION_ID IN (" +gudmapIdString + ")");
		int subStatus = DBHelper.getSubStatusByPrivilege(userPrivilege);
//		System.out.println("ArrayDevDAO:getRelevantGudmapIds:status: " + subStatus);
//		System.out.println("ArrayDevDAO:getRelevantGudmapIds:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, subStatus);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantGudmapIds.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
//		for (int i=0;i<relevantGudmapIds.size();i++)
//			System.out.println("ArrayDevDAO:getRelevantGudmapIds:id(" + i + "): " + relevantGudmapIds.get(i));
		return relevantGudmapIds;
	}
	
	
	/**
	 * @author xingjun - 02/09/2009
	 * @param itemList
	 * @return
	 */
//	private String convertItemsFromArrayListToString(ArrayList itemList) {
//		if (itemList == null || itemList.size() == 0) {
//			return null;
//		}
//		int len = itemList.size();
//		String result = "'";
//		for (int i=0;i<len;i++) {
//			result += itemList.get(i) + "', '";
//		}
//		result = result.substring(0, result.length()-3);
//		return result;
//	}

    /**
     * @author xingjun - 02/09/2009
     * <P>xingjun - 16/10/2009 - added one replacement into queryString for GNF_SYMBOL param setup</P>
     */
	public ArrayList<String> getRelevantSymbols(ArrayList candidateSymbols) {
		if (candidateSymbols == null || candidateSymbols.size() == 0) {
			return null;
		}
		ArrayList<String> relevantSymbols = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = InsituDBQuery.getParamQuery("GENE_SYMBOLS_AND_SYNONYMS");
		String queryString = parQ.getQuerySQL();
		String SymbolString = DBHelper.convertItemsFromArrayListToString(candidateSymbols);
		queryString = queryString.replace("RPR_SYMBOL LIKE ?", "RPR_SYMBOL IN (" +SymbolString + ")");
		queryString = queryString.replace("RSY_SYNONYM LIKE ?", "RSY_SYNONYM IN (" +SymbolString + ")");
		queryString = queryString.replace("GNF_SYMBOL LIKE ?", "GNF_SYMBOL IN (" +SymbolString + ")");
		queryString = queryString.replace("ORDER BY NATURAL_SORT(GENE) LIMIT ?", "");
//		System.out.println("ArrayDevDAO:getRelevantSymbols:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantSymbols.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
//		System.out.println("ArrayDevDAO:getRelevantSymbols:relevantSymbols number" + relevantSymbols.size());
		return relevantSymbols;
    }
    
	/**
	 * <p>overloading - xingjun - 29/10/2009 - users with different privileges will see symbols(entries) in different status
	 */
	public ArrayList<String> getRelevantSymbols(ArrayList candidateSymbols, int userPrivilege) {
		if (candidateSymbols == null || candidateSymbols.size() == 0) {
			return null;
		}
		ArrayList<String> relevantSymbols = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = InsituDBQuery.getParamQuery("GENE_SYMBOLS_AND_SYNONYMS");
		String queryString = parQ.getQuerySQL();
		String SymbolString = DBHelper.convertItemsFromArrayListToString(candidateSymbols);
		queryString = queryString.replace("RPR_SYMBOL LIKE ?", "RPR_SYMBOL IN (" +SymbolString + ")");
		queryString = queryString.replace("RSY_SYNONYM LIKE ?", "RSY_SYNONYM IN (" +SymbolString + ")");
		queryString = queryString.replace("GNF_SYMBOL LIKE ?", "GNF_SYMBOL IN (" +SymbolString + ")");
		queryString = queryString.replace("ORDER BY NATURAL_SORT(GENE) LIMIT ?", "");

		int subStatus = DBHelper.getSubStatusByPrivilege(userPrivilege);
		queryString = 
			queryString.replaceAll("AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4", 
					"AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK <= ?");
		
//		System.out.println("ArrayDevDAO:getRelevantSymbols:subStatus: " + subStatus);
//		System.out.println("ArrayDevDAO:getRelevantSymbols:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, subStatus);
			prepStmt.setInt(2, subStatus);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantSymbols.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
//		System.out.println("ArrayDevDAO:getRelevantSymbols:relevantSymbols number" + relevantSymbols.size());
		return relevantSymbols;
    }
    
    /**
     * @author xingjun - 02/09/2009
     */
    public ArrayList<String> getRelevantProbeSetIds(ArrayList candidateProbeSetIds, 
    		String platformId) {
		if (candidateProbeSetIds == null || candidateProbeSetIds.size() == 0) {
			return null;
		}
		ArrayList<String> relevantProbeSetIds = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = ArrayDBQuery.getParamQuery("GET_RELEVANT_PROBE_SET_IDS");
		String queryString = parQ.getQuerySQL();
		String ProbeSetIdString = 
			DBHelper.convertItemsFromArrayListToString(candidateProbeSetIds);
		queryString = queryString.replace("PRS_PROBE_SET_ID IN", "PRS_PROBE_SET_ID IN (" +ProbeSetIdString + ")");
//		System.out.println("ArrayDevDAO:getRelevantProbeSetIds:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, platformId);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantProbeSetIds.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return relevantProbeSetIds;
    }
    
	/**
	 * @author xingjun - 11/12/2009
	 */
    public ArrayList<String> getRelevantImageIds(ArrayList candidateImageIds, int userPrivilege) {
		if (candidateImageIds == null || candidateImageIds.size() == 0) {
			return null;
		}
		ArrayList<String> relevantImageIds = new ArrayList<String>();
		ResultSet resSet = null;
		ParamQuery parQ = InsituDBQuery.getParamQuery("GET_RELEVANT_IMAGE_IDS");
		String queryString = parQ.getQuerySQL();
		String imageIdString = DBHelper.convertItemsFromArrayListToString(candidateImageIds);
		queryString = queryString.replace("CONCAT(SUB_OID, '_', IMG_FILENAME) IN", 
				"CONCAT(SUB_OID, '_', IMG_FILENAME) IN (" +imageIdString + ")");
		int subStatus = DBHelper.getSubStatusByPrivilege(userPrivilege);
//		System.out.println("ArrayDevDAO:getRelevantImageIds:status: " + subStatus);
//		System.out.println("ArrayDevDAO:getRelevantImageIds:SQL: " + queryString);
		
		PreparedStatement prepStmt = null;
		try {
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setInt(1, subStatus);
			resSet = prepStmt.executeQuery();
			if(resSet.first()){
				resSet.beforeFirst();
				while (resSet.next()) {
					relevantImageIds.add(resSet.getString(1));
				}
			}
			
			// release the db objects
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(SQLException se) {
			se.printStackTrace();
		}
//		for (int i=0;i<relevantImageIds.size();i++)
//			System.out.println("ArrayDevDAO:getRelevantImageIds:id(" + i + "): " + relevantImageIds.get(i));
		return relevantImageIds;
	}
    
}