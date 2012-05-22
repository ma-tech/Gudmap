/**
 * 
 */
package gmerg.db;

import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 * @author xingjun
 *
 */
public class MySQLCollectionDAOImp implements CollectionDAO {
	
    private Connection conn;
    
    // default constructor
    public MySQLCollectionDAOImp() {
    	
    }
    
    // constructor with connection initialisation
    public MySQLCollectionDAOImp(Connection conn) {
    	this.conn = conn;
    }

	/* (non-Javadoc)
	 * @see gmerg.db.CollectionDAO#getCollections(int, int, int, int, boolean, int, int)
	 */
	public ArrayList getCollections(int userId,int collectionType, int status,
			int column, boolean ascending, int offset, int num) {
		// TODO Auto-generated method stub
		
		if (userId < 0 || collectionType < 0 || status < 0) { // if the userId is invalid
			return null;
		}
		
		ResultSet resSet = null;
        ArrayList result = null;
        ParamQuery parQ = null;
        String defaultOrder = DBQuery.COLLECTION_DEFAULT_ORDER_BY_COL;
        
        if (status == 0) { // own collection required
            parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_EXCLUSIVE");
        } else if (status == 1) { // others's shared collection required
        	parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_OTHERS");
        } else if (status == 2) { // both own and others' shared collection required
        	parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_ALL");
        }
        
        String query = parQ.getQuerySQL();
        String queryString =
        	assembleCollectionQueryString(query, defaultOrder, column, ascending, offset, num);
//        System.out.println("collection type: " + collectionType);
//        System.out.println("collection user id: " + userId);
//        System.out.println("collection query string: " + queryString);
		
        PreparedStatement prepStmt = null;
        
        try {
            prepStmt = conn.prepareStatement(queryString);
            
            if (status == 0 || status == 1) { // own or others' shared collections
            	prepStmt.setInt(1, collectionType);
            	prepStmt.setInt(2, userId);
            } else if (status == 2) { // both own and others' shared collections
            	prepStmt.setInt(1, collectionType);
            	prepStmt.setInt(2, userId);
            	prepStmt.setInt(3, collectionType);
            	prepStmt.setInt(4, userId);
            }

            // execute
            resSet = prepStmt.executeQuery();
            result = formatCollectionQueryResultSet(userId, resSet);

            // release database resources
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);

            // reset the static query string to its original value
            parQ.setQuerySQL(query);

        } catch (SQLException se) {
            se.printStackTrace();
        }
		return result;
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
	private String assembleCollectionQueryString(String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
		
		String queryString = query + " ORDER BY ";
		if (columnIndex != -1) {
			
			// translate parameters into database column names
			String column =
				getCollectionBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString += defaultOrder;
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
	 * @param defaultOrder
	 * @return
	 */
	private String getCollectionBrowseOrderByColumn(int columnIndex,
			boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC" : "DESC");
		
		// start to translate
		if (columnIndex == 1) { // name
			column += defaultOrder + order;
		} else if (columnIndex == 2) { // description
			column = "CLN_DESCRIPTION " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) { // owner
			column += "USR_UNAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 5) { // focus group
//			column += "CLN_FOCUS_GROUP " + order + ", " + defaultOrder;
			column += "CLN_FOCUS_GROUP_NAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 6) { // entries
			column += "CLN_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 7) { // status
			column += "CLN_STATUS " + order + ", " + defaultOrder;
		} else if (columnIndex == 8) { // last modified
			column += "CLN_LAST_UPDATED " + order + ", " + defaultOrder;
		} else if (columnIndex == 9) { // type
			column += "CLN_TYPE " + order + ", " + defaultOrder;
		} else { // order by default - name
			column = defaultOrder;
		}
		return column;
	}

	private ArrayList formatCollectionQueryResultSet(int userId, ResultSet resSet) throws SQLException {
		
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();
		
		if (resSet.first()) {
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<String[]>();
			
			while (resSet.next()) {
				String[] columns = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
				}
				results.add(columns);
			}
			return results;
		}
		return null;
	}
	
	/**
	 * @author xingjun
	 * @param userId
	 * @param collectionType
	 * @param status
	 */
	public int getTotalNumberOfCollections(int userId, int collectionType, int status) {

		if (userId < 0 || collectionType < 0 || status < 0) { // if the userId is invalid
			return 0;
		}
		
		ResultSet resSet = null;
        int result = 0;
        ParamQuery parQ = null;
        
        if (status == 0) { // own collection required
            parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_EXCLUSIVE");
        } else if (status == 1) { // others's shared collection required
        	parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_OTHERS");
        } else if (status == 2) { // both own and others' shared collection required
        	parQ = DBQuery.getParamQuery("COLLECTION_BROWSE_ALL");
        }
        
        String query = parQ.getQuerySQL();
//        System.out.println("number of collection query string: " + query);
		
        PreparedStatement prepStmt = null;
        
        try {
            prepStmt = conn.prepareStatement(query);
            
            if (status == 0 || status == 1) { // own or others' shared collections
            	prepStmt.setInt(1, collectionType);
            	prepStmt.setInt(2, userId);
            } else if (status == 2) { // both own and others' shared collections
            	prepStmt.setInt(1, collectionType);
            	prepStmt.setInt(2, userId);
            	prepStmt.setInt(3, collectionType);
            	prepStmt.setInt(4, userId);
            }

            // execute
            resSet = prepStmt.executeQuery();
            if (resSet.first()) {
            	resSet.last();
            	result = resSet.getRow();
            }

            // release database resources
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStmt);

        } catch (SQLException se) {
            se.printStackTrace();
        }
		return result;
	}
	
	public ArrayList<String> getCollectionItemsByIds(String[] collectionIds) {
		return null;
	}
	
	/**
	 * get detailed info for specified collection
	 * @author xingjun
	 * @param collectionId
	 */
	public CollectionInfo getCollectionInfoById(int collectionId) {
		
//		System.out.println("collection id: " + collectionId);
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("COLLECTION_DETAIL_BY_ID");
		String query = parQ.getQuerySQL();
//		System.out.println("collection info by id query: " + query);
		CollectionInfo collectionInfo = null;
		
		try {
			PreparedStatement prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, collectionId);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				collectionInfo = new CollectionInfo();
				collectionInfo.setId(resSet.getInt(1));
				collectionInfo.setName(resSet.getString(2));
				collectionInfo.setOwner(resSet.getInt(3));
				collectionInfo.setOwnerName(resSet.getString(4));
				collectionInfo.setType(resSet.getInt(5));
				collectionInfo.setStatus(resSet.getInt(6));
				collectionInfo.setDescription(resSet.getString(7));
				collectionInfo.setFocusGroup(resSet.getInt(8));
                java.util.Date lastUpdateDate = resSet.getDate(9);
                if(null != lastUpdateDate) {
                	DateFormat df =
                		DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                	String lud = df.format(lastUpdateDate);
    				collectionInfo.setLastUpdate(lud);
                } else {
                	collectionInfo.setLastUpdate("n/a");
                }
				collectionInfo.setEntries(resSet.getInt(10));
			}
			// release database resources
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return collectionInfo;
	}
	
	/**
	 * get detailed info by collection name and the owner id
	 * @author xingjun
	 * @param collectionName
	 * @param owner
	 */
	public CollectionInfo getCollectionInfoByName(String collectionName, int owner) {

//		System.out.println("collection id: " + collectionName);
//		System.out.println("owner id: " + owner);
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("COLLECTION_DETAIL_BY_NAME");
		String query = parQ.getQuerySQL();
//		System.out.println("collection info by name query: " + query);
		CollectionInfo collectionInfo = null;
		
		try {
			PreparedStatement prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, owner);
			prepStmt.setString(2, collectionName);
			resSet = prepStmt.executeQuery();
			
			if (resSet.first()) {
				collectionInfo = new CollectionInfo();
				collectionInfo.setId(resSet.getInt(1));
				collectionInfo.setName(resSet.getString(2));
				collectionInfo.setOwner(resSet.getInt(3));
				collectionInfo.setOwnerName(resSet.getString(4));
				collectionInfo.setType(resSet.getInt(5));
				collectionInfo.setStatus(resSet.getInt(6));
				collectionInfo.setDescription(resSet.getString(7));
				collectionInfo.setFocusGroup(resSet.getInt(8));
                java.util.Date lastUpdateDate = resSet.getDate(9);
                if(null != lastUpdateDate) {
                	DateFormat df =
                		DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                	String lud = df.format(lastUpdateDate);
    				collectionInfo.setLastUpdate(lud);
                } else {
                	collectionInfo.setLastUpdate("n/a");
                }
				collectionInfo.setEntries(resSet.getInt(10));
			}
			// release database resources
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return collectionInfo;
	}
	
	/**
	 * retrieve collection item list by id
	 * @author xingjun
	 * @param collectionId
	 * 
	 * <p>modified by xingjun - 06/10/2008</p>
	 * <p>need to delete removed submission id(s) if it's a submission collection:
	 * submission entries might be deleted after user create his collection. the deleted submission
	 * entries will not appear in the collection in this situation</p>
	 * <p>modified by xingjun - 26/01/2009 -
	 * last modification caused a problem not working for collections of other than 
	 * submission type. modified the code and it should be able to deal with other types 
	 * of collections</p>
	 */
	public ArrayList<String> getCollectionItemsById(int collectionId) {
		
//		System.out.println("collection id: " + collectionId);
		ResultSet resSet = null;
		ParamQuery parQ = DBQuery.getParamQuery("COLLECTION_ITEM_LIST");
		String queryCollectionItem = parQ.getQuerySQL();
//		System.out.println("CollectionDAO:getCollectionItemsById:query: " + queryCollectionItem);
		parQ = DBQuery.getParamQuery("REMOVED_SUBMISSION_LIST");
		String queryRemovedSubmission = parQ.getQuerySQL();
		
		ArrayList<String> collectionItems = null;
		ArrayList<String> removedSubmissionItems = null;
		
		try {
			// obtain removed submission
			PreparedStatement prepStmt = conn.prepareStatement(queryRemovedSubmission);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				removedSubmissionItems = new ArrayList<String>();
				resSet.beforeFirst();
				while (resSet.next()) {
					String removedSubmission = resSet.getString(1);
					if (removedSubmission != null && removedSubmission != "") {
						removedSubmissionItems.add(removedSubmission);
					}
				}
			}
			
			// obtain collection items - if submission collection, delete removed submission
			prepStmt = conn.prepareStatement(queryCollectionItem);
			prepStmt.setInt(1, collectionId);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				collectionItems = new ArrayList<String>();
				resSet.beforeFirst();
				while (resSet.next()) {
					String collectionItem = resSet.getString(1);
					if (collectionItem != null && collectionItem != "") { // only add meaningful items
						int collectionType = resSet.getInt(2);
						if (collectionType == 0) {// submission collection. need delete removed submission
							if (!removedSubmissionItems.contains(collectionItem)) {
								collectionItems.add(collectionItem);
							}
						} else { // other type of collection
							collectionItems.add(collectionItem);
						}
					}
				}
			}
			// release database resources
			DBHelper.closeResultSet(resSet);
			DBHelper.closePreparedStatement(prepStmt);
			
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return collectionItems;
	}
	
	/**
	 * @author xingjun
	 * @param collectionId
	 */
	public int deleteCollectionById(int collectionId) {
		
//		System.out.println("collection id: " + collectionId);
		ParamQuery parQ = DBUpdateSQL.getParamQuery("DELETE_COLLECTION_BY_ID");
        PreparedStatement prepStmt = null;
        String queryString = parQ.getQuerySQL();
        int deletedRecordNumber = 0;
        
        try {
        	// delete
        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setInt(1, collectionId);
        	deletedRecordNumber = prepStmt.executeUpdate();
//        	System.out.println(deletedRecordNumber + " collection deleted!");
        	
        } catch (SQLException se) {
        	se.printStackTrace();
        }
        // release db resources
        DBHelper.closePreparedStatement(prepStmt);
		return deletedRecordNumber;
	}
	
	/**
	 * @author xingjun - 04/06/2008
	 * <p>modified by xingjun - 01/07/2008 - return meaningful integer values used by assembler</p>
	 * <p>modified by xingjun - 25/07/2008 - last updated column has not been set before which should be</p>
	 * <p>modified by xingjun - 06/10/2009 - added focus group name column</p>
	 */
	public int insertCollection(CollectionInfo collectionInfo, ArrayList<String> items) {
		
		// initialising
		ParamQuery parQCollectionId = DBQuery.getParamQuery("MAX_COLLECTION_ID");
		String queryStringCollectionId = parQCollectionId.getQuerySQL();

		ParamQuery parQCollectionValue = DBUpdateSQL.getParamQuery("INSERT_COLLECTION");
		String queryStringCollectionValue = parQCollectionValue.getQuerySQL();

		ParamQuery parQCollectionItemId = DBQuery.getParamQuery("MAX_COLLECTION_ITEM_ID");
		String queryStringCollectionItemId = parQCollectionItemId.getQuerySQL();

		ParamQuery parQCollectionItemValue = DBUpdateSQL.getParamQuery("INSERT_COLELCTION_ITEM");
		String queryStringCollectionItemValue = parQCollectionItemValue.getQuerySQL();

		PreparedStatement prepStmt = null;
		ResultSet resSet = null;

		int insertedRecordNumber = 0;
		
		// insert collection header
		try {
			conn.setAutoCommit(false);
			
			// get max collection id
			prepStmt = conn.prepareStatement(queryStringCollectionId);
			resSet = prepStmt.executeQuery();
			int maxCollectionId = -1;
			if (resSet.first()) {
				maxCollectionId = resSet.getInt(1);
			}
			
			if (maxCollectionId == -1) { // failed to retrieve max collection id
				conn.rollback();
			}

			// insert collection info
			prepStmt = conn.prepareStatement(queryStringCollectionValue);
			prepStmt.setInt(1, maxCollectionId+1); // id
			prepStmt.setString(2, collectionInfo.getName());
			prepStmt.setInt(3, collectionInfo.getOwner());
			prepStmt.setInt(4, collectionInfo.getType());
			prepStmt.setInt(5, collectionInfo.getStatus());
			///// modified by xingjun - 09/07/2008 description could be empty
			String collectionDesc = collectionInfo.getDescription();
			prepStmt.setString(6, ((collectionDesc ==null) ? "":collectionDesc));
			////////////////////
			// xingjun - 06/10/2009
			int focusGroupId = collectionInfo.getFocusGroup();
			prepStmt.setInt(7, focusGroupId);
			// xingjun - 25/07/2008
			prepStmt.setString(8, collectionInfo.getLastUpdate());
			
			// xingjun - 06/10/2009
			prepStmt.setString(9, Globals.getFocusGroup(focusGroupId));
			
			insertedRecordNumber = prepStmt.executeUpdate();
//			System.out.println(insertedRecordNumber  + " collection record inserted!");
			
			if (insertedRecordNumber == 0) { // insertion failed
				conn.rollback();
			}
			
			// get max collection item id
			prepStmt = conn.prepareStatement(queryStringCollectionItemId);
			resSet = prepStmt.executeQuery();
			int maxCollectionItemId = -1;
			if (resSet.first()) {
				maxCollectionItemId = resSet.getInt(1);
			}
			
			if (maxCollectionItemId == -1) { // failed to retrieve max collection item id
				insertedRecordNumber = 0;
				conn.rollback();
			}
			
			// insert collection item
			int counter = 0;
			for (int i=0;i<items.size();i++) {
//				System.out.println("maxCollectionItemId: " + maxCollectionItemId);
//				System.out.println("collection Id: " + maxCollectionId);
//				System.out.println("item: " + items.get(i).toString());
				prepStmt = conn.prepareStatement(queryStringCollectionItemValue);
				prepStmt.setInt(1, ++maxCollectionItemId); // id
				prepStmt.setInt(2, (maxCollectionId+1)); // collection id
				prepStmt.setString(3, items.get(i).toString()); // item
//				insertedRecordNumber = prepStmt.executeUpdate();
				counter += (insertedRecordNumber = prepStmt.executeUpdate());
			}
//			System.out.println(counter  + " collection items inserted!");
			
			if (counter < items.size()) { // insertion failed
				insertedRecordNumber = 0;
				conn.rollback();
			} else {
				insertedRecordNumber = counter;
				conn.setAutoCommit(true);
			}
			
			// release database resources
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
			
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
        	try {
        		conn.setAutoCommit(true);
        	} catch(SQLException se) {
        		se.printStackTrace();
        	}
		}

		// insertion success
		return insertedRecordNumber;
	} /** end of insertCollection */
	
	/**
	 * @author xingjun - 04/06/2008
	 * <p>modified by xingjun - 06/10/2009 - added focus group name column</p>
	 */
	public int updateCollectionSummary(CollectionInfo collectionInfo) {
		
		// initialising
		ParamQuery parQ = DBUpdateSQL.getParamQuery("UPDATE_COLLECTION_SUMMARY");
		String queryString = parQ.getQuerySQL();
		PreparedStatement prepStmt = null;
		ResultSet resSet = null;
		
		int updatedRecordNumber = 0;
		try {
			// update
			prepStmt = conn.prepareStatement(queryString);
			prepStmt.setString(1, collectionInfo.getName()); // name
			prepStmt.setInt(2, collectionInfo.getStatus()); // status
			///// modified by xingjun - 09/07/2008 description could be empty
			String collectionDesc = collectionInfo.getDescription();
			prepStmt.setString(3, ((collectionDesc ==null) ? "":collectionDesc)); // description
			/////////////////////////////
			// xingjun - 06/10/2009 - start
			int focusGroupId = collectionInfo.getFocusGroup();
			prepStmt.setInt(4, focusGroupId); // focus group
			prepStmt.setString(5, Globals.getFocusGroup(focusGroupId));
			// xingjun - 06/10/2009 - end
			java.util.Date today = new java.util.Date();
			prepStmt.setTimestamp(6, new java.sql.Timestamp(today.getTime())); // last updated
			prepStmt.setInt(7, collectionInfo.getId());
			updatedRecordNumber = prepStmt.executeUpdate();
//			System.out.println(updatedRecordNumber  + " collection items updated!");
			
			// release database resources
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return updatedRecordNumber;
	} /** end of updateCollectionSummary */
	
	/**
	 * @author xingjun - 08/10/2009
	 */
	public int getPublicSubmissionNumberBySubmissionId(ArrayList submissionIds) {
		if (submissionIds == null || submissionIds.size() == 0) {
			return 0;
		}
		int result = 0;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("GET_PUBLIC_SUBMISSION_NUMBER_BY_GIVEN_SUBMISSION_ID");
		String queryString = parQ.getQuerySQL();
		String submissionIdString = DBHelper.convertItemsFromArrayListToString(submissionIds);
		
		queryString = queryString.replaceAll("IN", "IN (" + submissionIdString + ") ");
//		System.out.println("CollectionDAO:getPublicSubmissionNumberBySubmissionId:sql: " + queryString);
		
		try {
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				result = resSet.getInt(1);
			}
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return result;
	}

	/**
	 * @author xingjun - 08/10/2009
	 * @param submissionIds
	 * @return 0: empty submission id list, or invalid submission id(s) exist(s)
	 * 			1: all submission ids are valid
	 */
	public int checkSubmissionId(ArrayList submissionIds) {
		if (submissionIds == null || submissionIds.size() == 0) {
			return 0;
		}
		int numberOfValidSubIds = 0;
		int result = 0;
		ResultSet resSet = null;
		PreparedStatement prepStmt = null;
		ParamQuery parQ = AdvancedSearchDBQuery.getParamQuery("GET_SUBMISSION_NUMBER_BY_GIVEN_SUBMISSION_ID");
		String queryString = parQ.getQuerySQL();
		String submissionIdString = DBHelper.convertItemsFromArrayListToString(submissionIds);
		
		queryString = queryString.replaceAll("IN", "IN (" + submissionIdString + ") ");
//		System.out.println("CollectionDAO:getPublicSubmissionNumberBySubmissionId:sql: " + queryString);
		
		try {
			prepStmt = conn.prepareStatement(queryString);
			resSet = prepStmt.executeQuery();
			if (resSet.first()) {
				numberOfValidSubIds = resSet.getInt(1);
			}
			DBHelper.closePreparedStatement(prepStmt);
			DBHelper.closeResultSet(resSet);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		int numberOfPassedInSubIds = submissionIds.size();
		if (numberOfValidSubIds < numberOfPassedInSubIds) {
			result = 0;
		} else {
			result = 1;
		}
		return result;
	}
	
	/**
	 * @author xingjun - 16/10/2009
	 * @param symbol
	 * @return
	 */
	public ArrayList<String> getInsituSubmissionImageIdByGene(String symbol) {
		if (symbol == null || symbol.equals("")) {
			return null;
		}
        ArrayList<String> imageIds = null;
		ResultSet resSet = null;
        ParamQuery parQ = InsituDBQuery.getParamQuery("INSITU_SUBMISSION_IMAGE_ID_BY_GENE_SYMBOL");
        String queryString = parQ.getQuerySQL();
//        System.out.println("CollectionDAO:getInsituSubmissionImageIdByGene:symbol: " + symbol);
//        System.out.println("CollectionDAO:getInsituSubmissionImageIdByGene:sql: " + queryString);
        PreparedStatement prepStmt = null;
        try {
        	// if disconnected from db, re-connected
        	conn = DBHelper.reconnect2DB(conn);

        	prepStmt = conn.prepareStatement(queryString);
        	prepStmt.setString(1, symbol);
        	resSet = prepStmt.executeQuery();
        	if (resSet.first()) {
        		imageIds = new ArrayList<String>();
        		resSet.beforeFirst();
        		while (resSet.next()) {
        			String imageId = resSet.getString(1);
        			imageIds.add(imageId);
        		}
        	}
//        	System.out.println("CollectionDAO:getInsituSubmissionImageIdByGene:image number: " + images.size());
        	
            // close the db object
            DBHelper.closePreparedStatement(prepStmt);
            DBHelper.closeResultSet(resSet);
        } catch (SQLException se) {
        	se.printStackTrace();
        }
		return imageIds;
	}
}