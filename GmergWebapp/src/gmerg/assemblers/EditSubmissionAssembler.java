/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import gmerg.db.DBHelper;
import gmerg.db.AnnotationTestDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.StatusNote;
import gmerg.utils.Utility;


/**
 * @author xingjun
 *
 */
public class EditSubmissionAssembler {
	
	public boolean addStatusNotes(String submissionId, StatusNote[] statusNotes, String userName) {

		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** add */
		int counter = 0;
		int len = statusNotes.length;
		for (int i=0;i<len;i++) {
			int addedStatusNoteNumber = ishEditDAO.insertStatusNote(submissionId,
					statusNotes[i].getStatusNote(), userName);
			counter += addedStatusNoteNumber;
		}
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (counter == len) {
			return true;
		}
		return false;
	}
	
	public boolean deleteAllStatusNotes(String submissionId, String userName) {
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		/** delete */
		int deletedStatusNoteNumber =
			ishEditDAO.deleteStatusNotesBySubmissionId(submissionId, userName);
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (deletedStatusNoteNumber != 0) {
			return true;
		}
		return false;
	}
	
	public boolean updateStatusNotes(String submissionId,
			StatusNote[] oldStatusNotes, StatusNote[] newStatusNotes, String userName) {
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		int updatedStatusNoteNumber = 0;
		
		/** update */
		// oldStatusNotes and newStatusNotes are both not null
		int noteNumberInDB = oldStatusNotes.length;
		int noteNumberOnPage = newStatusNotes.length;
		if (noteNumberInDB == noteNumberOnPage) {
			for (int i=0;i<noteNumberInDB;i++) {
				String newStatusNote = newStatusNotes[i].getStatusNote();
				if (!oldStatusNotes[i].getStatusNote().equals(newStatusNote)) {
					// go through the notes, update if different
					int statusNoteId = oldStatusNotes[i].getStatusNoteId();
					updatedStatusNoteNumber =
						ishEditDAO.updateStatusNoteById(statusNoteId, newStatusNote, userName);
				}
			}
		} else if (noteNumberInDB < noteNumberOnPage) {
			// go through the notes, update if different
			for (int i=0;i<noteNumberInDB;i++) {
				String newStatusNote = newStatusNotes[i].getStatusNote();
				if (!oldStatusNotes[i].getStatusNote().equals(newStatusNote)) {
					int statusNoteId = oldStatusNotes[i].getStatusNoteId();
					updatedStatusNoteNumber =
						ishEditDAO.updateStatusNoteById(statusNoteId, newStatusNote, userName);
				}
			}
			// add new status notes
			for (int i=noteNumberInDB;i<noteNumberOnPage;i++) {
				String newStatusNote = newStatusNotes[i].getStatusNote();
				updatedStatusNoteNumber =
					ishEditDAO.insertStatusNote(submissionId, newStatusNote, userName);
			}
		} else { // noteNumberInDB > noteNumberOnPage
			// go through the notes, update if different
			for (int i=0;i<noteNumberOnPage;i++) {
				String newStatusNote = newStatusNotes[i].getStatusNote();
				if (!oldStatusNotes[i].getStatusNote().equals(newStatusNote)) {
					int statusNoteId = oldStatusNotes[i].getStatusNoteId();
					updatedStatusNoteNumber =
						ishEditDAO.updateStatusNoteById(statusNoteId, newStatusNote, userName);
				}
			}
			// delete old status notes
			for (int i=noteNumberOnPage;i<noteNumberInDB;i++) {
				int statusNoteId = oldStatusNotes[i].getStatusNoteId();
				updatedStatusNoteNumber =
					ishEditDAO.deleteStatusNotesByStatusNoteId(statusNoteId, userName);
			}
		}
		
		/** release db resources */
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		/** return */
		if (updatedStatusNoteNumber == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * @author xingjun - 07/08/2008
	 * <p>moved from ISHBrowseSubmissionAssembler</p>
	 * @param selectedSubmissions
	 * @return
	 */
	public boolean deleteSelectedSubmissions(String[] selectedSubmissions) {
		
		boolean submissionDeleted = false;
		
		// no selected submissions
		if (selectedSubmissions == null || selectedSubmissions.length == 0) {
			return submissionDeleted;
		}
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// delete
		submissionDeleted = ishEditDAO.deleteSelectedSubmission(selectedSubmissions);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionDeleted;
	}
	
	/**
	 * @author xingjun - 24/12/2008
	 * xingjun - 23/02/2010 - added extra criteria, namely subState
	 * 
	 */
	public boolean updateSubmissionStatusByLabAndDateAndState (String labId, String subDate, 
			int status, String userName, String subState, boolean set2Public) {
//		System.out.println("EditSubmissionAssembler:updateSubmissionStatusByLabAndDateAndState!!!");
        // passed in parameters are not valid
		if (labId == null || labId.equals("") 
				|| subDate == null || subDate.equals("")
				|| status == 0
				|| userName == null || userName.equals("")
				|| subState == null || subState.equals("")) {
        	return false;
        }
//		System.out.println("EditSubmissionAssembler:labId: " + labId);
//		System.out.println("EditSubmissionAssembler:subDate: " + subDate);
//		System.out.println("EditSubmissionAssembler:status: " + status);
//		System.out.println("EditSubmissionAssembler:userName: " + userName);
//		System.out.println("EditSubmissionAssembler:subState: " + subState);
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		AnnotationTestDAO annotationTestDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
		
		// update
		boolean loggedIn = true;
		int subStateValue = Utility.getSubmissionStatusByName(subState, loggedIn);
		String subDateString = Utility.convertToDatabaseDate(subDate);
//		System.out.println("EditSubmissionAssembler:subDateString: " + subDateString);
		int isPublicValue = set2Public?1:0;
		int updatedRecordNumber = 
			annotationTestDAO.updateSubmissionDbStatusByLabAndSubDateAndState(Integer.parseInt(labId), 
					subDateString, status, userName, subStateValue, isPublicValue);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		annotationTestDAO = null;
		
		// return
		if (updatedRecordNumber == 0) {
			return false;
		}
		return true;
	}

	/**
	 * @author xingjun - 01/07/2011 - overloading version with extra param: archive id
	 */
	public boolean updateSubmissionStatusByLabAndDateAndState (String labId, String subDate, String archiveId,  
			int status, String userName, String subState, boolean set2Public) {
//		System.out.println("EditSubmissionAssembler:updateSubmissionStatusByLabAndDateAndState!!!");
        // passed in parameters are not valid
		if (labId == null || labId.equals("") 
				|| subDate == null || subDate.equals("")
				|| archiveId == null || archiveId.equals("")
				|| status == 0
				|| userName == null || userName.equals("")
				|| subState == null || subState.equals("")) {
        	return false;
        }
//		System.out.println("EditSubmissionAssembler:labId: " + labId);
//		System.out.println("EditSubmissionAssembler:subDate: " + subDate);
//		System.out.println("EditSubmissionAssembler:archiveId: " + archiveId);
//		System.out.println("EditSubmissionAssembler:status: " + status);
//		System.out.println("EditSubmissionAssembler:userName: " + userName);
//		System.out.println("EditSubmissionAssembler:subState: " + subState);
		
		// create dao
		Connection conn = DBHelper.getDBConnection();
		AnnotationTestDAO annotationTestDAO = MySQLDAOFactory.getAnnotationTestDAO(conn);
		
		// update
		boolean loggedIn = true;
		int subStateValue = Utility.getSubmissionStatusByName(subState, loggedIn);
		String subDateString = Utility.convertToDatabaseDate(subDate);
//		System.out.println("EditSubmissionAssembler:subDateString: " + subDateString);
		int isPublicValue = set2Public?1:0;
//		int updatedRecordNumber = 
//			annotationTestDAO.updateSubmissionDbStatusByLabAndSubDateAndState(Integer.parseInt(labId), 
//					subDateString, status, userName, subStateValue, isPublicValue);
		int updatedRecordNumber = 
			annotationTestDAO.updateSubmissionDbStatusByLabAndSubDateAndState(Integer.parseInt(labId), 
					subDateString, archiveId, status, userName, subStateValue, isPublicValue);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		annotationTestDAO = null;
		
		// return
		if (updatedRecordNumber == 0) {
			return false;
		}
		return true;
	}
}