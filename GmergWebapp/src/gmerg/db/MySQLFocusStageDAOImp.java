package gmerg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLFocusStageDAOImp implements FocusStageDAO{
    private Connection conn;

    // default constructor
    public MySQLFocusStageDAOImp() {  	
    }

    // constructor with connection initialisation

    public MySQLFocusStageDAOImp(Connection conn) {
        this.conn = conn;
    }    
    
	/**
	 * modified by xingjun - 20/11/2007: find in situ result instead of only ish result
	 */
    public ArrayList[][] getStageList(String[] stage){
		// TODO Auto-generated method stub
		ArrayList[][] result = new ArrayList[stage.length][2];
		ResultSet resSet = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		if(null != stage) {
//			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ISH");
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_INSITU");
			// assemble the query string
			String sql = parQ.getQuerySQL();
			parQ = AdvancedSearchDBQuery.getParamQuery("ALL_ENTRIES_ARRAY_FOCUS");
			// assemble the query string
			String sql2 = parQ.getQuerySQL();
			
			String queryString = null;
			try {
				for(int i = 0; i < stage.length; i++) {
					queryString = sql + " and SUB_EMBRYO_STG='"+stage[i]+"' ";
					//System.out.println("FocusStageArray:"+queryString);
					prepStmt = conn.prepareStatement(queryString);
					
					resSet = prepStmt.executeQuery();
					result[i][0] = formatBrowseSeriesResultSet(resSet);
					
					DBHelper.closePreparedStatement(prepStmt);	
					
					queryString = sql2 + " and SUB_EMBRYO_STG='"+stage[i]+"' ";
//					System.out.println("FocusStageISH:"+queryString);
					prepStmt = null;
					prepStmt = conn.prepareStatement(queryString);
					resSet = null;
					resSet = prepStmt.executeQuery();
					result[i][1] = formatBrowseSeriesResultSet(resSet);
					
					DBHelper.closePreparedStatement(prepStmt);
				}
			} catch(Exception se) {
				se.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * @author xingjun - 11-09-2007
	 * <p>return submission summary information (in situ & microarray) categorised by stages</p>
	 */
	public String[][] getStageList(String[] stage, String organ) {
		
		String[][] result = new String[stage.length][2];
		ResultSet resSet = null;
		ParamQuery parQ = null;
		ParamQuery parQ2 = null;
		PreparedStatement prepStmt = null;
		if(null != stage) {
			parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_IN_SITU");
			String sql = parQ.getQuerySQL();
			parQ2 = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ARRAY");
			String sql2 = parQ2.getQuerySQL();
			
			String queryString = null;
			try {
				for(int i = 0; i < stage.length; i++) {
                    // append stage criteria
					queryString = sql + " and SUB_EMBRYO_STG='"+stage[i]+"' ";
					
					// append component criteria
					if(null != organ) {
						String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
						String ids = "";
						  for(int j = 0; j < emapids.length; j++) {
							  ids += "'"+emapids[j] + "',";
						  }
						  if(emapids.length >= 1) {
							  ids = ids.substring(0, ids.length()-1);
						  }
						
						  queryString += 
							  " AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
							  " from ANA_TIMED_NODE ANCES_ATN, " +
							  " ANAD_RELATIONSHIP_TRANSITIVE, " +
							  " ANA_TIMED_NODE DESCEND_ATN, " +
							  " ANA_NODE, " +
							  " ANAD_PART_OF " +
							  " where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
							  " and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
//							  " and RTR_ANCESTOR_FK <> RTR_DESCENDENT_FK " + // by xingjun 14/11/2007: should include descendent
							  " and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
							  " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
							  " and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
							  " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";
					} else { // remove redundant join to speed up query
						queryString = queryString.replace("LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK", "");
					}
//					System.out.println("FocusStageInsitu:"+queryString);
					prepStmt = conn.prepareStatement(queryString);
					resSet = prepStmt.executeQuery();
					if (resSet.first()) {
						result[i][0] = resSet.getString(1);
					}
					DBHelper.closePreparedStatement(prepStmt);	
					
					queryString = sql2 + " and SUB_EMBRYO_STG='"+stage[i]+"' ";

					// append component criteria
					if(null != organ) {
						String[] emapids = (String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
						String ids = "";
						  for(int j = 0; j < emapids.length; j++) {
							  ids += "'"+emapids[j] + "',";
//							  System.out.println("emapId: " + emapids[j]);
						  }
						  if(emapids.length >= 1) {
							  ids = ids.substring(0, ids.length()-1);
						  }
						
						  queryString += 
							  " AND EXP_COMPONENT_ID in (select distinct DESCEND_ATN.ATN_PUBLIC_ID " +
							  " from ANA_TIMED_NODE ANCES_ATN, " +
							  " ANAD_RELATIONSHIP_TRANSITIVE, " +
							  " ANA_TIMED_NODE DESCEND_ATN, " +
							  " ANA_NODE, " +
							  " ANAD_PART_OF " +
							  " where ANCES_ATN.ATN_PUBLIC_ID in ("+ids+") " +
							  " and ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
//							  " and RTR_ANCESTOR_FK <> RTR_DESCENDENT_FK " + // by xingjun 14/11/2007: should include descendent
							  " and RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
							  " and ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
							  " and ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
							  " and APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true) ";
					}
// 					System.out.println("FocusStageArray:"+queryString);
					prepStmt = null;
					prepStmt = conn.prepareStatement(queryString);
					resSet = null;
					resSet = prepStmt.executeQuery();
					if (resSet.first()) {
						result[i][1] = resSet.getString(1);
					}
					DBHelper.closePreparedStatement(prepStmt);
				}
			} catch(Exception se) {
				se.printStackTrace();
			}
		}
		return result;
	}
	

	/**
	 * @author xingjun - 28/01/2009
	 * different version of last one: accept more parameters and return only stage list for
	 * one type of assay
	 * @param assayType
	 * @param stage
	 * @param organ
	 * @param symbol
	 * @return
	 */
	public String[] getStageList(String assayType, String[] stage, String organ, String symbol) {
		String[] result = new String[stage.length];
		ResultSet resSet = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		String stageString = null;
		String geneString = "";
		String componentString = null;
		if(null != stage) {
			if (assayType.equals("insitu")) {
				parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_IN_SITU");
				stageString = " AND SUB_EMBRYO_STG = '";
				if (symbol != null && !symbol.equals("")) {
					geneString += " AND RPR_SYMBOL = '" + symbol + "'";
				}
				componentString = " AND EXP_COMPONENT_ID IN ";
			} else if (assayType.equals("Microarray")) {
				// if gene criteria is not provided, use alternative query and much faster
				if (symbol != null && !symbol.equals("")) {
					parQ = ArrayDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ARRAY");
					stageString = " AND MBC_SUB_EMBRYO_STG = '";
					geneString += " AND MBC_GNF_SYMBOL = '" + symbol + "'";
					componentString = " AND MBC_COMPONENT_ID IN ";
				} else {
					parQ = AdvancedSearchDBQuery.getParamQuery("TOTAL_NUMBER_OF_SUBMISSION_ARRAY");
					stageString = " AND SUB_EMBRYO_STG = '";
					componentString = " AND EXP_COMPONENT_ID IN ";
				}
			}
			String querySQL = parQ.getQuerySQL();;
			String queryString = "";
			
			for(int i = 0; i < stage.length; i++) {
                // append stage criteria
				queryString = querySQL + stageString + stage[i] + "' ";
				
				// append gene criteria
				queryString += geneString;
				
				// append component criteria
				if(organ != null && !organ.equals("")) {
					String[] emapids = 
						(String[])AdvancedSearchDBQuery.getEMAPID().get(organ);
					String ids = "";
					  for(int j = 0; j < emapids.length; j++) {
						  ids += "'"+emapids[j] + "',";
					  }
					  if(emapids.length >= 1) {
						  ids = ids.substring(0, ids.length()-1);
					  }
					  queryString += componentString + 
						  " (SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
						  " FROM ANA_TIMED_NODE ANCES_ATN, " +
						  " ANAD_RELATIONSHIP_TRANSITIVE, " +
						  " ANA_TIMED_NODE DESCEND_ATN, " +
						  " ANA_NODE, " +
						  " ANAD_PART_OF " +
						  " WHERE ANCES_ATN.ATN_PUBLIC_ID IN (" + ids + ") " +
						  " AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
						  " AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
						  " AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
						  " AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
						  " AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = TRUE) ";
				}
//				System.out.println("FocusStageSummaryQuery:" + assayType + ": " +queryString);
				// get the data
				try {
					prepStmt = conn.prepareStatement(queryString);
					resSet = prepStmt.executeQuery();
					if (resSet.first()) {
						result[i] = resSet.getString(1);
					}
					DBHelper.closePreparedStatement(prepStmt);
					DBHelper.closeResultSet(resSet);
				} catch(Exception se) {
					se.printStackTrace();
				}
				queryString = "";
			}
		}
		return result;
	} // end of getStageList(assayType, stage, organ, symbol)

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
	
	public Object[][] getGeneIndex(String prefix, String organ) {
		// TODO Auto-generated method stub
		
		ArrayList list = new ArrayList();
		ResultSet resSet = null;
		ParamQuery parQ = null;
		Object[][] result = null;
		PreparedStatement prepStmt = null;
		if(null != prefix && !prefix.equals("") && !prefix.equals("null")) {
			parQ = new ParamQuery("GENE_INDEX", AdvancedSearchDBQuery.getISHGeneIndex(prefix, organ));
			String sql = parQ.getQuerySQL();
// 			System.out.println("gene_index sql: " + sql);
			try {
				prepStmt = conn.prepareStatement(sql);
				resSet = prepStmt.executeQuery();
				list = formatBrowseSeriesResultSet(resSet);
				DBHelper.closePreparedStatement(prepStmt);
				if(null != list) {
					result = new Object[list.size()][];
					for(int i = 0; i < list.size(); i++) {
						result[i] = (Object[])list.get(i);						
					}				
				} 
			} catch(Exception se) {
				se.printStackTrace();
				DBHelper.closePreparedStatement(prepStmt);
			}
			/*parQ = new ParamQuery("GENE_INDEX", AdvancedSearchDBQuery.getISHGeneIndex(prefix, organ));			
			// assemble the query string
			String sql = parQ.getQuerySQL();			
			try {	
						//select QSC_RPR_SYMBOL col1, count(distinct QSC_SUB_ACCESSION_ID) col2, count(distinct MBC_SUB_ACCESSION_ID) col3 
						//from QSC_ISH_CACHE,MIC_BROWSE_CACHE where QSC_RPR_SYMBOL like 'A%'
						//and MBC_GNF_SYMBOL=QSC_RPR_SYMBOL  and QSC_EXP_STRENGTH='present' 
						//and MBC_GLI_DETECTION='P' group by col1 order by col1					
					
					
					//System.out.println("FocusStageArray:"+queryString);
					prepStmt = conn.prepareStatement(sql);
					conn.setAutoCommit(false);
					resSet = prepStmt.executeQuery();
					list = formatBrowseSeriesResultSet(resSet);
					DBHelper.closePreparedStatement(prepStmt);
						
					if(null != list) {
						result = new Object[list.size()][7];
						String sql2 = null;
						String sql3 = null;
						for(int i = 0; i < list.size(); i++) {
							result[i][0] = ((String[])list.get(i))[0];
							//parQ = new ParamQuery("GENE_ISH", AdvancedSearchDBQuery.getISHGeneExpresssion(((String[])list.get(i))[0]));
							parQ = new ParamQuery("GENE_ISH", AdvancedSearchDBQuery.getISHGeneExpresssion1(((String[])list.get(i))[0], organ));
							sql2 = parQ.getQuerySQL();
							
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql2);
							resSet = null;	
							//System.out.println("ish1:"+sql2);
							resSet = prepStmt.executeQuery();
							result[i][1] = formatGeneResultSet(resSet);
							DBHelper.closePreparedStatement(prepStmt);
							parQ = null;
							parQ = new ParamQuery("GENE_ISH", AdvancedSearchDBQuery.getISHGeneExpresssion2(((String[])list.get(i))[0], organ));
							sql2 = parQ.getQuerySQL();
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql2);
							resSet = null;	
							//System.out.println("ish2:"+sql2);
							resSet = prepStmt.executeQuery();
							result[i][2] = formatGeneResultSet(resSet);
							DBHelper.closePreparedStatement(prepStmt);
							parQ = null;
							parQ = new ParamQuery("GENE_ISH", AdvancedSearchDBQuery.getISHGeneExpresssion3(((String[])list.get(i))[0], organ));
							sql2 = parQ.getQuerySQL();
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql2);
							resSet = null;				
							//System.out.println("ish3:"+sql2);
							resSet = prepStmt.executeQuery();
							result[i][3] = formatGeneResultSet(resSet);
							DBHelper.closePreparedStatement(prepStmt);
							
							//parQ = new ParamQuery("GENE_MIC", AdvancedSearchDBQuery.getMicGeneExpresssion(((String[])list.get(i))[0]));
							parQ = null;
							parQ = new ParamQuery("GENE_MIC", AdvancedSearchDBQuery.getMicGeneExpresssion1(((String[])list.get(i))[0], organ));
							sql3 = parQ.getQuerySQL();
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql3);
							resSet = null;		
							//System.out.println("mic1:"+sql3);
							resSet = prepStmt.executeQuery();
							result[i][4] = formatGeneResultSet(resSet);	
							DBHelper.closePreparedStatement(prepStmt);
							parQ = null;
							parQ = new ParamQuery("GENE_MIC", AdvancedSearchDBQuery.getMicGeneExpresssion2(((String[])list.get(i))[0], organ));
							sql3 = parQ.getQuerySQL();
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql3);
							resSet = null;					
							//System.out.println("mic2:"+sql3);
							resSet = prepStmt.executeQuery();
							result[i][5] = formatGeneResultSet(resSet);		
							DBHelper.closePreparedStatement(prepStmt);
							parQ = null;
							parQ = new ParamQuery("GENE_MIC", AdvancedSearchDBQuery.getMicGeneExpresssion3(((String[])list.get(i))[0], organ));
							sql3 = parQ.getQuerySQL();
							prepStmt = null;
							prepStmt = conn.prepareStatement(sql3);
							resSet = null;			
							//System.out.println("mic3:"+sql3);
							resSet = prepStmt.executeQuery();
							result[i][6] = formatGeneResultSet(resSet);		
							DBHelper.closePreparedStatement(prepStmt);
					    }
					}
					
					conn.commit();
		            conn.setAutoCommit(true);
		            DBHelper.closePreparedStatement(prepStmt);
			} catch(Exception se) {
				se.printStackTrace();
				DBHelper.closePreparedStatement(prepStmt);
			}*/
		}
		return result;
	}

	private String formatGeneResultSet(ResultSet resSet) throws SQLException {
		String results = "";
		
		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			results = new String();
			
			if(resSet.next()) {				
				results = resSet.getString(1);
			}
		}
		return results;
	}
	
	/**
	 * @author xingjun - 30/01/2009
	 */
	public String getDpcStageValue(String theilerStage) {
		String dpcStageValue = null;
		ResultSet resSet = null;
		ParamQuery parQ = null;
		PreparedStatement prepStmt = null;
		parQ = InsituDBQuery.getParamQuery("EQUIVALENT_DPC_STAGE_FOR_THEILER_STAGE");
		String queryString = parQ.getQuerySQL();;
//		System.out.println("getDpcValueQuery: " + queryString);
		try {
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, theilerStage);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				dpcStageValue = resSet.getString(1) + " " + resSet.getString(2);
			} else {
				dpcStageValue = "";
			}
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch(Exception se) {
			se.printStackTrace();
		}
		
		return dpcStageValue;
	}
}