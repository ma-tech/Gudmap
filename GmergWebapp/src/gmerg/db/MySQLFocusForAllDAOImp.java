package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.utils.table.GenericTableFilter;

public class MySQLFocusForAllDAOImp  implements FocusForAllDAO {
    private Connection conn;
    private int MAX_COLUMNS = 12; // changed from 11 by xingjun - 07/12/2009 - added extra column MUT_GENE
    private int MAX_ISH_COLUMNS = 13;

    // default constructor
    public MySQLFocusForAllDAOImp() {  	
    }

    // constructor with connection initialisation

    public MySQLFocusForAllDAOImp(Connection conn) {
        this.conn = conn;
    }
	
	// get total number of ish submissions
	public int findTotalNumberOfSubmissionISH(String[] emapids)
	{
        int result = 0;
        return result;
	}
	
    // get number of public ish sumbissions
	public int findNumberOfPublicSubmissionISH(String[] emapids)
	{
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
        	new ParamQuery("NUM_ISH",AdvancedSearchDBQuery.getPublicISHNumber(emapids));
//      PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
//            parQ.setPrepStat(conn);
//            prepStmt = parQ.getPrepStat();
//            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }

            // close the db object
//            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}
//	 get total number of ihc submissions
	public int findTotalNumberOfSubmissionArray(String[] emapids)
	{
        int result = 0;
        return result;
	}
	
	/**
	 * @author xingjun - 25/09/2009
	 */
	public int findNumberOfPublicSubmissionArray(String[] emapids) {
        int result = 0;
        ResultSet resultSet = null;
//		PreparedStatement prepStmt = null;
		Statement stmt = null;
        ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
        
		// emap ids
		String queryString = parQ.getQuerySQL();
		String ids = "";
		for(int i = 0; i < emapids.length; i++) {
			ids += "'"+emapids[i] + "',";
		}
		if(emapids.length >= 1) {
			ids = ids.substring(0, ids.length()-1);
		}
		queryString += AdvancedSearchDBQuery.findDescendant.replaceAll("COMPONENT_IDS", ids);
		
		// group by clause
		queryString += AdvancedSearchDBQuery.groupBySubmissionArray;
//		System.out.println("FocusForAllDAO:findNumberOfPublicSubmissionArray:sql: " + queryString);
        try {
//            prepStmt = conn.prepareStatement(queryString);
//            resultSet = prepStmt.executeQuery();
            stmt = conn.prepareStatement(queryString);
            resultSet = stmt.executeQuery(queryString);
            
            ArrayList arraySubmissions = this.formatResultSet(resultSet, this.MAX_COLUMNS);
            if (arraySubmissions == null) {
            	result = 0;
            } else {
            	result = arraySubmissions.size();
            }

            // close the db object
//            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}


//	// get number of public ihc sumbissions
//	public int findNumberOfPublicSubmissionArray(String[] emapids)
//	{
//        int result = 0;
//        ResultSet resSet = null;
//        ParamQuery parQ =
//        	new ParamQuery("NUM_MIC",AdvancedSearchDBQuery.getPublicMICNumber(emapids));
////        String query = parQ.getQuerySQL();
////        System.out.println("query of submission number - array: " + query);
//        
//        PreparedStatement prepStmt = null;
//
//        try {
//            parQ.setPrepStat(conn);
//            prepStmt = parQ.getPrepStat();
//            resSet = prepStmt.executeQuery();
//
//            if (resSet.first()) {
//                result = resSet.getInt(1);
//            }
//
//            // close the db object
//            DBHelper.closePreparedStatement(prepStmt);
//
//        } catch (SQLException se) {
//            se.printStackTrace();
//        }
//        return result;
//	}

	// get number of public ihc sumbissions
	public int findNumberOfPublicSubmissionIHC(String[] emapids)
	{
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
        	new ParamQuery("NUM_IHC",AdvancedSearchDBQuery.getPublicIHCNumber(emapids));
//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
//            parQ.setPrepStat(conn);
//            prepStmt = parQ.getPrepStat();
//            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }

            // close the db object
//            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}
	
	/**
	 * @author xingjun - 28/08/2008
	 * @param emapids
	 * @return
	 */
	public int findNumberOfPublicSubmissionTG(String[] emapids) {
        int result = 0;
        ResultSet resSet = null;
        ParamQuery parQ =
        	new ParamQuery("NUM_TG",AdvancedSearchDBQuery.getPublicTransgenicNumber(emapids));
//      PreparedStatement prepStmt = null;
        Statement stmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);
//            parQ.setPrepStat(conn);
//            prepStmt = parQ.getPrepStat();
//            resSet = prepStmt.executeQuery();
            stmt = conn.createStatement();
            resSet = stmt.executeQuery(parQ.getQuerySQL());
            if (resSet.first()) {
                result = resSet.getInt(1);
            }

            // close the db object
//            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeStatement(stmt);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}
	
	/**
	 * @author xingjun - 02/02/2009
	 * <p>xingjun - 22/09/2011 - transgenic data need different sql to get the number</p>
	 */
	public int findNumberOfPublicGenes(String assayType, String[] emapIds) {
        int result = 0;
        ResultSet resSet = null;
        // xingjun - 22/09/2011 - start
//        ParamQuery parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE");
        ParamQuery parQ = null;
        if (assayType.equalsIgnoreCase("TG")) {
        	parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE_TG");
        } else {
        	parQ = InsituDBQuery.getParamQuery("NUMBER_OF_INVOLVED_GENE");
        }
        // xingjun - 22/09/2011 - end
        
//        PreparedStatement prepStmt = null;
        Statement stmt = null;
        String componentString = "AND EXP_COMPONENT_ID IN " + DBHelper.assembleComponentString(emapIds);
        
        // xingjun - 22/09/2011 - start
//        String queryString = 
//        	parQ.getQuerySQL().replace("JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID", 
//        			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
//        			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID ")
//        			+ componentString;
        String queryString = null;
        if (assayType.equalsIgnoreCase("TG")) {
        	queryString = parQ.getQuerySQL().replace("JOIN ISH_SUBMISSION ON MUT_SUBMISSION_FK = SUB_OID", 
        			"JOIN ISH_SUBMISSION ON MUT_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID ")
			+ componentString;
        } else {
        	queryString = parQ.getQuerySQL().replace("JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID", 
        			"JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK = SUB_OID ")
			+ componentString;
        }
        // xingjun - 22/09/2011 - end
        
        queryString = queryString.replace("?", "'"+assayType+"'");
//        System.out.println("findNumberOfPublicGenes query(" + assayType + "): "
//        		+ queryString);
            
          try {
          	// if disconnected from db, re-connected
          	conn = DBHelper.reconnect2DB(conn);
//              prepStmt = conn.prepareStatement(queryString);
//              prepStmt.setString(1, assayType);
//              resSet = prepStmt.executeQuery();
              stmt = conn.createStatement();
              resSet = stmt.executeQuery(queryString);
              
              if (resSet.first()) {
                  result = resSet.getInt(1);
              }

              // close the db object
//              DBHelper.closePreparedStatement(prepStmt);
              DBHelper.closeStatement(stmt);
              DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
	}
	
	/**
	 * <p>xingjun - 20/11/2007: comply to the requirement of order of columns
	 * - for ish, the default order is GENE, STAGE, SEX
	 * - for microarray, the default order is gudmap id</p>
	 * <p>xingjun - 31/08/2009 - changed the statement when orderby = 6 - refer to the code</p>
	 * <p>xingjun - 23/09/2009 - when making sort on SMP_GEO_ID or SER_GEO_ID, use natural_sort</p>
	 * <p>xingjun - 22/10/2009 - changed the rule for concatenating stage format and stage value</p>
	 * <p>xingjun - 07/12/2009 - added extra column MUT_GENE</p>
	 * <p>xingjun - 14/05/2010 - added natural_sort on column concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ')</p>
	 * @param orderby
	 * @param asc
	 * @param query
	 * @return
	 */
	public String orderResult(int orderby, boolean asc, String query) {
		String orderStr = null;
		
		String order = (asc == true ? " ASC ": " DESC ");

			if(query.equals("array")) {
				if(orderby < 0) {
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort(); 
				} else if(0 == orderby) {
//					orderStr = " order by CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort(); 
					orderStr = " order by " + AdvancedSearchDBQuery.getMICDefaultSort() + " " + order; 
				} else if(1 == orderby){
					orderStr = " order by natural_sort(SMP_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(2 == orderby){
//					orderStr = " order by SMP_THEILER_STAGE " + order;
					orderStr = " order by SMP_THEILER_STAGE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(3 == orderby){
//					orderStr = " order by concat(SPN_STAGE_FORMAT,SPN_STAGE) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
					orderStr = " order by TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(4 == orderby){
					orderStr = " order by PER_SURNAME " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(5 == orderby){
					orderStr = " order by SUB_SUB_DATE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(6 == orderby){// modified by xingjun - 31/08/2009 - change to sex column
//					orderStr = " order by SMP_SOURCE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
					orderStr = " order by SMP_SEX " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(7 == orderby){
					orderStr = " order by SRM_SAMPLE_DESCRIPTION " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(8 == orderby){
					orderStr = " order by SMP_TITLE " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				// xingjun - 07/12/2009 - added extra column MUT_GENE
//				}  else if(9 == orderby){
////					orderStr = " order by SER_GEO_ID " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
//					orderStr = " order by natural_sort(SER_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
//				} else if(10 == orderby){
//					orderStr = " order by concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				}  else if(9 == orderby){
					orderStr = " order by natural_sort(MUT_GENE) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} else if(10 == orderby){
//					orderStr = " order by SER_GEO_ID " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
					orderStr = " order by natural_sort(SER_GEO_ID) " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} else if(11 == orderby){
//					orderStr = " order by concat(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
					orderStr = " order by GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') " + order + "," + AdvancedSearchDBQuery.getMICDefaultSort();
				} 
			} else if(query.equals("ish") || query.equals("insitu")) {
				if(orderby < 0) {
					orderStr = " order by " + AdvancedSearchDBQuery.getISHDefaultSort(); 
				} else if(0 == orderby) {
					orderStr = " order by CAST(SUBSTRING(SUB_ACCESSION_ID, INSTR(SUB_ACCESSION_ID,'" + ":" + "')+1) AS UNSIGNED) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort(); 
				} else if(1 == orderby){
//					orderStr = " order by RPR_SYMBOL " + order + "," + AdvancedSearchDBQuery.getISHStageDefaultSort();
					orderStr = " order by NATURAL_SORT(RPR_SYMBOL) " + order + "," + "SUB_EMBRYO_STG, SPN_SEX";
				}  else if(2 == orderby){
//					orderStr = " order by SUB_EMBRYO_STG " + order + "," + AdvancedSearchDBQuery.getISHGeneDefaultSort();
					orderStr = " order by SUB_EMBRYO_STG " + order + "," + "NATURAL_SORT(RPR_SYMBOL), SPN_SEX";
				}  else if(3 == orderby){
//					orderStr = " order by concat(SPN_STAGE, SPN_STAGE_FORMAT) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
					orderStr = " order by TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) " 
						+ order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(4 == orderby){
					orderStr = " order by PER_SURNAME " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(5 == orderby){
					orderStr = " order by SUB_SUB_DATE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(6 == orderby){
					orderStr = " order by SUB_ASSAY_TYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(7 == orderby){
					orderStr = " order by SPN_ASSAY_TYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(8 == orderby){
//					orderStr = " order by SPN_SEX " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
					orderStr = " order by SPN_SEX " + order + "," + "NATURAL_SORT(RPR_SYMBOL), SUB_EMBRYO_STG, SPN_SEX";
				}  else if(9 == orderby){
					orderStr = " order by NATURAL_SORT(TRIM(RPR_JAX_ACC)) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}  else if(10 == orderby){
					orderStr = " order by SPN_WILDTYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}   else if(11 == orderby){
					orderStr = " order by PRB_PROBE_TYPE " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}   else if(12 == orderby){
					orderStr = " order by CONCAT(IMG_URL.URL_URL, I.IMG_FILEPATH, IMG_URL.URL_SUFFIX, I.IMG_FILENAME) " + order + "," + AdvancedSearchDBQuery.getISHDefaultSort();
				}   				
			}
		return orderStr;
    }	

/*
	public ArrayList getFocusBrowseList(String[] organ, int column, boolean ascending, 
			String query, String stage, String offset, String resPerPage) {
//		System.out.println("query type: " + query);
		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		if(query.equals("array")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
//			      " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += orderResult(column, ascending, query);
			} else {
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
			}
			
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_ARRAY_BROWSE",sql);

//			System.out.println("Arraybrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_COLUMNS);
				
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
				
				
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		} else if(query.equals("ish")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ISH");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
//			      " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += orderResult(column, ascending, query);
			} else {
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
			}
			
			
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_ISH_BROWSE",sql);

//			System.out.println("ISHbrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
				
				
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		} else if(query.equals("insitu")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_INSITU");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
//			      " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += orderResult(column, ascending, query);
			} else {
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
			}
			
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_INSITU_BROWSE",sql);

//			System.out.println("INSITUbrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
				
				
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		}
		return result;
	}
*/	
	/**
	 * overloaded version - xingjun - 15/01/2009
	 * added an extra param - gene
	 * <p>modified by xingjun - 25/09/2009 
	 * - due to the introduction of MySQL GROUP_CONCAT function group by clause added into the query</p>
	 * @return
	 */
	public ArrayList getFocusBrowseList(String[] organ, int column, boolean ascending, 
			String query, String stage, String gene, String offset, String resPerPage, GenericTableFilter filter) {
//		System.out.println("FocusForAll:getFocusBrowseList:column: " + column);
//		System.out.println("query type: " + query);
		ResultSet resSet = null;
		ArrayList result = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		if(query.equals("array")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// focus group
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			}
			////////////////// gene -------- to be implemented
			
			// stage group by and order by - xingjun - 25/09/2009 - added group by clause
			if(null == stage || stage.equals("") || stage.equals("null")) {
//				sql += orderResult(column, ascending, query);
				sql += AdvancedSearchDBQuery.groupBySubmissionArray + orderResult(column, ascending, query);
			} else {
//				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + AdvancedSearchDBQuery.groupBySubmissionArray + orderResult(column, ascending, query);
			}
			
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			parQ = null;
			parQ = new ParamQuery("FOCUS_ARRAY_BROWSE",sql);

//			System.out.println("FocusForAllDAO:getFocusBrowseList:Arraybrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_COLUMNS);
				
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
				
				
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		} else if(query.equals("ish")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ISH");
			// assemble the query string
			String sql = parQ.getQuerySQL();
			// focus group
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redundant join to speed up query - 03/09/2009
				sql = sql.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
			}
			// gene --------- to be implemented
			// stage and order
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += orderResult(column, ascending, query);
			} else {
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
			}
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
		
			if(filter!=null) 
				sql = filter.addFilterSql(sql, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
			parQ = null;
			parQ = new ParamQuery("FOCUS_ISH_BROWSE",sql);

//			System.out.println("FocusForAllDAO:ISHbrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		} else if(query.equals("insitu")) {
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_INSITU");
			// assemble the query string
			String sql = parQ.getQuerySQL();	
			// organ
			if(null != organ) {
				String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ[0]);
				String ids = "";
				  for(int i = 0; i < emapids.length; i++) {
					  ids += "'"+emapids[i] + "',";
				  }
				  if(emapids.length >= 1) {
					  ids = ids.substring(0, ids.length()-1);
				  }
				
				sql+=" AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
			    " from ANA_TIMED_NODE ANCES_ATN, "+
			         " ANAD_RELATIONSHIP_TRANSITIVE, "+
			         " ANA_TIMED_NODE DESCEND_ATN, "+
			         " ANA_NODE, "+
			         " ANAD_PART_OF "+
			    " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			      " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
			      " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			      " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			      " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			      " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
			} else { // remove redunant join to speed up query
				sql = sql.replace(" LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
			}
			// gene ---------------- to be implemented
			if (gene != null && !gene.equals("")) {
				sql += " and RPR_SYMBOL = '" + gene + "' ";
			}
			// stage and order
			if(null == stage || stage.equals("") || stage.equals("null")) {
				sql += orderResult(column, ascending, query);
			} else {
				sql += " and SUB_EMBRYO_STG='"+stage+"' " + orderResult(column, ascending, query);
			}
			// offset and limit
			sql+= new String(null == resPerPage?
    	    	    " ":" limit "+offset+","+resPerPage+"  ");
			
			if(filter!=null) 
				sql = filter.addFilterSql(sql, AdvancedSearchDBQuery.getISH_BROWSE_ALL_SQL_COLUMNS());
			parQ = null;
			parQ = new ParamQuery("FOCUS_INSITU_BROWSE",sql);

//			System.out.println("INSITUbrowseall:"+sql);
			try {
				parQ.setPrepStat(conn);
				prepStmt = parQ.getPrepStat();
				// execute
				resSet = prepStmt.executeQuery();
				result = formatResultSet(resSet, MAX_ISH_COLUMNS);
				// close the connection
				DBHelper.closePreparedStatement(prepStmt);
			} catch(SQLException se) {
				se.printStackTrace();
			}			
		}
		return result;
	} // end of getFocusBrowseList()
	
    private ArrayList formatResultSet(ResultSet resSetImage,int length) throws SQLException {     
        if (resSetImage.first()) {
            resSetImage.beforeFirst();           
            ArrayList<String[]> results = new ArrayList<String[]>();
            while (resSetImage.next()) {
                String[] columns = new String[length];
                for(int i = 1; i <= length; i++) {
                	columns[i-1] = resSetImage.getString(i);
                	//System.out.println("SOURCE:"+columns[i-1]);
                }
                if(null != columns)
                results.add(columns);
            }
            return results;
        }
        return null;
    } 	
    
    /**
     * @author xingjun
     * overload version 
     * @return
     */
    public int getQuickNumberOfRows(String query, String[] inputs, String stage, String symbol, GenericTableFilter filter) {
    	ArrayList list = getFocusBrowseList(inputs, 1, true, query, stage, symbol, null, null,filter);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
    }
    
	/**
	 * <p>xingjun - 14/12/2009 - changed the default order (refer to the code below)</p>
	 * <p>xingjun - 03/02/2010 - changed the way to append platform criteria into the query string</p>
	 */
    public ArrayList getSeriesList(int columnIndex, boolean ascending, int offset, int num, String organ, String platform) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_SERIES");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
		if(null != emapids) {
			String ids = "";
			for(int i = 0; i < emapids.length; i++) {
				ids += "'"+emapids[i] + "',";
			}
			if(emapids.length >= 1) {
				ids = ids.substring(0, ids.length()-1);
			}
			String componentString = "AND EXP_COMPONENT_ID IN (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
			" from ANA_TIMED_NODE ANCES_ATN, " +
			" ANAD_RELATIONSHIP_TRANSITIVE, " +
			" ANA_TIMED_NODE DESCEND_ATN, " +
			" ANA_NODE, " +
			" ANAD_PART_OF " +
			" where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
			" and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			//     " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			" and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
			" and ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
			" and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
			" and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";
			query = query.replaceAll("AND EXP_COMPONENT_ID IN", componentString); 
		}
		if (null != platform && !platform.equals("")) {
			String platformString = " and SER_PLATFORM_FK=PLT_OID and PLT_GEO_ID=? ";
			query = query.replaceAll("AND SER_PLATFORM_FK", platformString);
		} else {
			query.replaceAll("AND SER_PLATFORM_FK", "");
		}
		
		// xingjun - 14/12/2009 - changed the default order as editors requested
//		String defaultOrder = AdvancedSearchDBQuery.ORDER_BY_EXPERIMENT_NAME;
		String defaultOrder = DBQuery.ORDER_BY_LAB_AND_EXPERIMENT;
		String queryString = assembleBrowseSerieseQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//	    System.out.println("FocusForAllDAO:getSeriesList:sql: "+queryString);
		// execute the query
		try {
			prepStmt = conn.prepareStatement(queryString);
			if(null != platform && !platform.equals("")) {
				prepStmt.setString(1, platform);
			}
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			DBHelper.closePreparedStatement(prepStmt);		
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;		
	}

	public int getNumberOfSeries(String organ, String platform) {
		ArrayList list = getSeriesList(1, true, 0, 0, organ, platform);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
	}
	
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
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	/**
	 * 
	 * @param columnIndex
	 * @param ascending
	 * @return
	 */
	private String getBrowseSeriesOrderByColumn(int columnIndex, boolean ascending) {
		
		String orderByString = new String("");
		String defaultOrder = " NATURAL_SORT(TRIM(SER_TITLE)) ";
		String order = (ascending == true ? "ASC": "DESC");
		String[] browseSeriesColumnList = {
				"NATURAL_SORT(TRIM(SER_TITLE))", "SAMPLE_NUMBER", "NATURAL_SORT(TRIM(SER_GEO_ID))", 
				"PER_SURNAME", "PLT_GEO_ID"};
		
		// start to translate
		if (columnIndex == 0) {
			orderByString = defaultOrder + order;
		} else if (columnIndex == 2) {
			orderByString = "SAMPLE_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 1) {
			orderByString = "NATURAL_SORT(TRIM(SER_GEO_ID)) " + order + ", " + defaultOrder;
		} else if (columnIndex == 3) {
			orderByString = "PER_SURNAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) {
			orderByString = "PLT_GEO_ID " + order + ", " + defaultOrder;
//		} else if (columnIndex == 5) {
//			orderByString = "ANO_COMPONENT_NAME " + order + ", " + defaultOrder;
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

	/**
	 * modified by xingjun - 18/08/2009 
	 * - use natural sort instead of just normal sort for geo id 
	 * 
	 */
	public ArrayList getPlatformList(int columnIndex, boolean ascending, int offset, int num, String organ) {
		// TODO Auto-generated method stub
		ArrayList result = null;
		ResultSet resSet = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("ALL_PLATFORM");
		PreparedStatement prepStmt = null;
		
		String query = parQ.getQuerySQL();
		if(null != organ && !organ.equals("")) {
			String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
			String ids = "";
			  for(int i = 0; i < emapids.length; i++) {
				  ids += "'"+emapids[i] + "',";
			  }
			  if(emapids.length >= 1) {
				  ids = ids.substring(0, ids.length()-1);
			  }
			query += " AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID "+
		    " from ANA_TIMED_NODE ANCES_ATN, "+
	        " ANAD_RELATIONSHIP_TRANSITIVE, "+
	        " ANA_TIMED_NODE DESCEND_ATN, "+
	        " ANA_NODE, "+
	        " ANAD_PART_OF "+
			   " where ANCES_ATN.ATN_PUBLIC_ID       in ("+ids+") "+
			     " and ANCES_ATN.ATN_NODE_FK   = RTR_ANCESTOR_FK "+
//			     " and RTR_ANCESTOR_FK        <> RTR_DESCENDENT_FK "+ // by xingjun 14/11/2007: should include descendent
			     " and RTR_DESCENDENT_FK       = DESCEND_ATN.ATN_NODE_FK "+
			     " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK "+      
			     " and ANO_OID = DESCEND_ATN.ATN_NODE_FK "+
			     " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) " ;
		}
//		String defaultOrder = " ORDER BY TRIM(PLT_GEO_ID) ";
		String defaultOrder = " ORDER BY natural_sort(TRIM(PLT_GEO_ID)) ";
		String queryString = assemblePlatformQueryString(query, defaultOrder, columnIndex, ascending, offset, num);
//	    System.out.println("FocusPlatform:"+queryString);
		// execute the query
		try {
			prepStmt = conn.prepareStatement(queryString);
			
			resSet = prepStmt.executeQuery();
			result = formatBrowseSeriesResultSet(resSet);
			
			DBHelper.closePreparedStatement(prepStmt);		
		} catch(SQLException se) {
			se.printStackTrace();
		}
		return result;		
	}

	public int getNumberOfPlatform(String organ) {
		ArrayList list = getPlatformList(1, true, 0, 0, organ);
    	if(null == list) {
    		return 0;
    	} else {
    		return list.size();
    	}
	}	

	/**
	 * modified by xingjun - 18/08/2009 
	 * - use natural sort instead of just normal sort for geo id and geo name 
	 * @param query
	 * @param defaultOrder
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @return
	 */
	private String assemblePlatformQueryString(String query, String defaultOrder, 
			int columnIndex, boolean ascending, int offset, int num) {
		String queryString = null;
		String order = (ascending == true ? " ASC ": " DESC ");
		if (columnIndex != -1) {
			queryString = query + " ORDER BY ";
			String column = "";
			if(0 == columnIndex) {
				column = "natural_sort(TRIM(PLT_GEO_ID))" + order;
			} else if(1 == columnIndex) {
				column = "natural_sort(TRIM(PLT_NAME))" + order;
			} else if(2 == columnIndex) {
				column = "PLT_TECHNOLOGY" + order;
			} else if(3 == columnIndex) {
				column = "PLT_MANUFACTURER" + order;
			} else if (columnIndex == 4) {
				column = "SERIES_NUMBER" + order;
			}
			
			queryString += column;
			
		} else { 
			queryString = query + defaultOrder;
		}
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		return queryString;
	}
	
	/**
	 * @author xingjun
	 * <p>modified by xingjun - 28/08/2008
	 * separated query based on assay type - ish, ihc, array, transgenic</p>
	 * @param labId
	 * @param assayType
	 * @param submissionDate
	 * @return
	 */
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate) {
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
//        System.out.println("getNumberOfSubmissionsForLab:assayType: " + assayType);
        if (assayType.equals("ISH") || assayType.equals("IHC")) {
            parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        } else if (assayType.equals("Microarray")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_ARRAY");
        } else if (assayType.equals("TG")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_TG");
        } else if (assayType.equals("insitu")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        }
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }
    	// added by xingjun - 28/08/2008
    	if (assayType.equals("insitu")) {
    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	} else {
    		query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	}
    	
//        System.out.println("getNumberOfSubmissionsForLab:query: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
//            prepStmt.setString(2, assayType);
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(2, submissionDate);
            }

            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
	}
	
	/**
	 * @author xingjun - 01/07/2011 - overloading version
	 * 
	 */
	public int getNumberOfSubmissionsForLab(String labId, String assayType, String submissionDate, String archiveId) {
//      System.out.println("getNumberOfSubmissionsForLab:labId: " + labId);
//      System.out.println("getNumberOfSubmissionsForLab:assayType: " + assayType);
//      System.out.println("getNumberOfSubmissionsForLab:submissionDate: " + submissionDate);
//      System.out.println("getNumberOfSubmissionsForLab:archiveId: " + archiveId);
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        if (assayType.equals("ISH") || assayType.equals("IHC")) {
            parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        } else if (assayType.equals("Microarray")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_ARRAY");
        } else if (assayType.equals("TG")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_TG");
        } else if (assayType.equals("insitu")) {
        	parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSIONS_FOR_SPECIFIC_LAB_INSITU");
        }
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }
    	// added by xingjun - 28/08/2008
    	if (assayType.equals("insitu")) {
    		query += "AND (SUB_ASSAY_TYPE = 'ISH' OR SUB_ASSAY_TYPE = 'IHC') ";
    	} else {
    		query += "AND SUB_ASSAY_TYPE = '" +  assayType + "' ";
    	}
    	
    	// xingjun - 01/07/2011
    	if (archiveId != null && !archiveId.trim().equals("")) {
    		query += "AND SUB_ARCHIVE_ID = ? ";
    	}
    	
//        System.out.println("getNumberOfSubmissionsForLab:query: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
            int paramNum = 2;
            
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(paramNum, submissionDate);
            	paramNum ++;
            }
            
			if (archiveId != null && !archiveId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
			}

            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
	}
	
	
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String isPublic) {
        
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_ANNOTATION_SUBMISSIONS_FOR_SPECIFIC_LAB");
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }
//        System.out.println("number of submission for lab: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
            prepStmt.setString(2, assayType);
            prepStmt.setString(3, isPublic);
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(4, submissionDate);
            }

            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
	}
	
	/**
	 * @author xingjun - 01/07/2011 - overloading version
	 */
	public int getNumberOfSubmissionsForLabForAnnotation(String labId, String assayType, String submissionDate, String archiveId, String isPublic) {
        
		try {
			Integer.parseInt(labId);
		} catch (NumberFormatException nfe) {
			return 0;
		}
		
		int totalNumber = 0;
        ResultSet resSet = null;
        ParamQuery parQ = DBQuery.getParamQuery("TOTAL_NUMBER_OF_ANNOTATION_SUBMISSIONS_FOR_SPECIFIC_LAB");
        String query = parQ.getQuerySQL();
        if (submissionDate != null && !submissionDate.equals("")) {
        	query += "AND SUB_SUB_DATE = ? ";
        }

		if (archiveId != null && !archiveId.trim().equals("")) {
			query += " AND SUB_ARCHIVE_ID = ? ";
		}
		
//        System.out.println("number of submission for lab: " + query);
        PreparedStatement prepStmt = null;

        try {
            prepStmt = conn.prepareStatement(query);
            prepStmt.setInt(1, Integer.parseInt(labId));
            prepStmt.setString(2, assayType);
            prepStmt.setString(3, isPublic);
            int paramNum = 4;
            if (submissionDate != null && !submissionDate.equals("")) {
            	prepStmt.setString(paramNum, submissionDate);
            	paramNum ++;
            }

			if (archiveId != null && !archiveId.trim().equals("")) {
				prepStmt.setInt(paramNum, Integer.parseInt(archiveId));
			}
			
            // execute
            resSet = prepStmt.executeQuery();

            if (resSet.first()) {
            	totalNumber = resSet.getInt(1);
            }

            // close the db object
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return totalNumber;
	}
}