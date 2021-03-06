/**
 * 
 */
package gmerg.utils;

import gmerg.beans.UserBean;
import gmerg.entities.Globals;
import gmerg.entities.User;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpSession;
import javax.activation.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


/**
 * @author xingjun
 *
 */


public class Utility {
    public static final String domainUrl="http://www.gudmap.org/";
    public static final String appUrl=domainUrl+"gudmap/";
    public static final String applicationRoot="/gudmap/";
    
    /**
     * modified by xingjun - 27/04/2009 - changed link for several help pages
     * @param queryType
     * @param input
     * @return
     */
    public static String getNoDataMessageForQueryPage(String queryType, String input){
	String noDataMessage = "<p align='center' class='plainred'>Sorry, your query has produced no results.</p>" +
	    "<hr align='center' width='550' noshade='noshade' class='style1' />" +
	    "<p class='plaintext'><a href='"+domainUrl+"Help/Query_Help.html' class='plaintextbold'>GUDMAP database query help</a><br />" +
	    "Contains help on using the database query interface to search by <a href='"+domainUrl+"Help/Query_Help.html#gene'>gene</a>, <a href='"+domainUrl+"Help/Query_Help.html#anatomy'>anatomy</a>, <a href='"+domainUrl+"Help/Query_Help.html#accession'>accession ID</a>, or <a href='"+domainUrl+"Help/Query_Help.html#function'>function</a>." +
	    "</p>" +
	    "<p class='plaintext'><strong><a href='"+domainUrl+"Help/Boolean_Help.html' class='plaintextbold'>Boolean Anatomy Query help</a></strong><br />"+
	    "Search for genes expressed in structure X and/or/not in structure Y.</p>" +
	    "<p class='plaintext'><strong><a href='"+domainUrl+"Help/Browse_Help.html' class='plaintextbold'>Browsing GUDMAP data help</a></strong><br />"+
	    "Help on browsing for microarray data, via series, sample and platform and accessing all ISH &amp; IHC submissions.</p>" +
	    "<p class='plaintext'><strong><a href='"+domainUrl+"Help/Analysis_Help.html' class='plaintextbold'>Analysis Help</a></strong><br />"+
	    "Help on using GUMDAP analysis tools.</p>" +
	    "<p class='plaintext'><strong><a href='"+domainUrl+"Help/Annotation_Tool_Help.html' class='plaintextbold'>Annotation Tool help</a></strong><br />" +
	    "Diagrams and guidance notes in using the annotation tool.</p>" +
	    "<p class='plaintext'><a class='plaintextbold' href='"+domainUrl+"Help/Submission_Help.html'>Submissions help</a><br />" +
	    "Guide to help users submit data.</p>" +
	    "<p class='plaintext'><a class='plaintextbold' href='"+domainUrl+"Help/Download_Help.html'>Downloads help</a><br />" +
	    "Help in downloading data from GUDMAP.</p>" +
	    "<p class='plaintext'><strong><a class='plaintextbold' href='"+domainUrl+"Help/Collection_Help.html'>Collections help</a></strong><br />" +
	    "Help with collections.</p>" +
	    "</p>" +
	    "<hr align='center' width='550' noshade='noshade' class='style1' />";
	return noDataMessage;
    }
    
    //Send a simple email
    public static void sendEmail(String fromEmailAddress, String toEmailAddress, String subject, String content) throws Exception{
	sendEmail(fromEmailAddress, toEmailAddress, subject, content, null, null);
    }
    
    //Send an email with a text file attachment
    public static void sendEmail(String fromEmailAddress, String toEmailAddress, String subject, String content, 
				 String attachment, String attachmentName) throws Exception {
	Properties props = new Properties();
	props.put("mail.smtp.host", Globals.getMailServer());
	Session session = Session.getDefaultInstance(props);
	Message emailMsg = new MimeMessage(session);
	try {
	    if (attachment==null) 
		emailMsg.setText(content);
	    else {
		BodyPart emailBody = new MimeBodyPart();
		emailBody.setText(content);
		BodyPart emailAttachment = new MimeBodyPart();
		String fileName = attachmentName;
		if(!attachment.toLowerCase().endsWith(".txt"))
		    fileName += ".txt";
		emailAttachment.setFileName(fileName);
		DataHandler fileData = new DataHandler(attachment, "text/plain");
		emailAttachment.setDataHandler(fileData);
		Multipart emailContent = new MimeMultipart();
		emailContent.addBodyPart(emailBody);
		emailContent.addBodyPart(emailAttachment);
		emailMsg.setContent(emailContent);	
	    }
	    
	    emailMsg.setFrom(new InternetAddress(fromEmailAddress));
	    emailMsg.setSubject(subject);
	    emailMsg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toEmailAddress));
	    Transport.send(emailMsg);
	}
	catch (Exception e) {
	    throw e;
	}
    }
    
    public static ResourceBundle getConfigBundle() {
	return ResourceBundle.getBundle("configuration");
    }
    
    public static String getProject() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("project");
    }
    
    public static String getStageSeriesMed() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("stage_series_medium");
    }
    
    public static String getSpecies() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("species");
    }
    
    public static String getPerspective() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("perspective");
    }
    
    public static String getAnatomyIdPrefix() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("anatomy_id_prefix");
    }
    
    
    public static String getValue(String value, String defaultValue) {
	if (value==null || value=="")
	    return defaultValue;
	return value;
    }
    
    public static int getIntValue(String value, int defaultValue) {
	if (value==null || value=="")
	    return defaultValue;
	try {
	    return Integer.parseInt(value);
	}
	catch(Exception e) {
	    return defaultValue;
	}
    }
    
    public static int stringArraySearch(String[] a, String value, boolean ignoreCase) {
	if (a == null || value == null)
	    return -1;
	
	for(int i=0; i<a.length; i++)
	    if (ignoreCase) {
		if (value.equalsIgnoreCase(a[i]))
		    return i;
	    }
	    else
		if (value.equals(a[i]))
		    return i;
	return -1;
    }
    
    public static int intArraySearch(int[] a, int value) {
	if (a == null)
	    return -1;
	
	for(int i=0; i<a.length; i++)
	    if (value == a[i])
		return i;
	return -1;
    }
    
    public static boolean matchesGudmapIdFormat(String value) {
	String id = value.toLowerCase();
	return id.matches("gudmap:\\d+");
    }
    
    public static User getUser() {
	UserBean userBean = (UserBean)FacesUtil.getSessionValue("UserBean");

	//!!! it is important to have this test
	// because there may not be UserBean
	if (null == userBean)
	    return null;
	return userBean.getUser();
    }
    
    public static boolean isUserLoggedIn() {
	UserBean userBean = (UserBean)FacesUtil.getSessionValue("UserBean");

	//!!! it is important to have this test
	// because there may not be UserBean
	if (null == userBean)
	    return false;
	return userBean.isUserLoggedIn();
    }
    
    public static int getUserPriviledges() {
	User user = getUser();
	if (user == null)
	    return -1;
	return user.getUserPrivilege(); 
    }
    
    /**
     * @author xingjun - 04/08/2008
     * @return
     */
    public static String getLockingSwitchOn() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("locking_switch_on");
    }
    
    public static String getLockTimeOut() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("lock_time_out");
    }
    
    public static String getLockedTextDisplayStyle() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("locked_text_display_style");
    }
    
    public static String getUnlockedTextDisplayStyle() {
	ResourceBundle bundle = getConfigBundle();
	return bundle.getString("unlocked_text_display_style");
    }
    
    
    public static Color getHeatmapExpressedColor() {
	return new Color(255, 0, 0);
    }
    
    public static Color getHeatmapNotExpressedColor() {
	return new Color(0, 0, 255);
    }
    
    // ********************************** type/format converters ****************************************************
    public static String convertToDatabaseDate(Date date) {
	SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    return dbDateFormat.format(date);
	} catch (Exception e) {
	    return date.toString();
	    //			e.printStackTrace();
	}
    }
    
    public static String convertToDisplayDate(Date date) {
	SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	try {
	    return dbDateFormat.format(date);
	} catch (Exception e) {
	    return date.toString();
	    //			e.printStackTrace();
	}
    }
    
    public static String convertToDatabaseDate(String date) {
	SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
	SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    date = df2.format(df1.parse(date));
	} catch (Exception e1) {
	    df1 = new SimpleDateFormat("dd-MM-yyyy");
	    try {
		date = df2.format(df1.parse(date));
		return date;
	    } catch (Exception e2) {
		return date;
	    }
	}
	return date;
    }
    
    public static String convertToDisplayDate(String date) {
	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy");
	try {
	    date = df2.format(df1.parse(date));
	} catch (Exception e) {
	    return date;
	    //			e.printStackTrace();
	}
	return date;
    }
    
    public static int compareDates(String date1, String date2) {
	SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
	try {
	    Date d1 = df.parse(date1);
	    Date d2 = df.parse(date2);
	    return d1.compareTo(d2);
	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	}
    }
    
    public static String captalizeFirst(String s) {
	if (s==null || s=="")
	    return s;
	return s.substring(0,1).toUpperCase()+s.substring(1);
    }
    
    public static String[] toArray(String s, String separator) {
	String[] array = null;
	if (s != null) 
	    array = s.split(separator);
	return array;
	
    }
    public static ArrayList<String> toArrayList(String s, String separator) {
	ArrayList<String> arrayList = new ArrayList<String>();
	String[] array = toArray(s, separator);
	if (array != null) 
	    for (int i=0; i<array.length; i++) 
		arrayList.add(array[i]);
	return arrayList;
    }
    
    public static ArrayList<String> toArrayList(String[] array) {
	ArrayList<String> arrayList = new ArrayList<String>();
	if (array != null) 
	    for (int i=0; i<array.length; i++) 
		arrayList.add(array[i]);
	return arrayList;
    }
    
    public static String toString(ArrayList<String> arrayList, String separator) {
	if (arrayList == null)
	    return null;
	String s = "";
	for (String e: arrayList)
	    s += e + separator;
	if (s.equals(""))
	    return null;
	return s;
    }
    
    /**
     * @author xingjun - 14/07/2009
     * replace apostrophe (') with (\') : avoid confusing sql parser of database
     * @param inputString
     * @return
     */
    public static String normaliseApostrophe(String inputString) {
	return inputString.replaceAll("\'", "\\"+"\\\'");
    }
    
    public static String javaTreeViewAppletCode(HashMap<String, String> params) {
	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	String contextPath = context.getRequestContextPath();
	HttpSession session = (HttpSession)(context.getSession(true));
	String code = "<applet id='treeviewApplet' code='edu/stanford/genetics/treeview/applet/GudmapApplet.class' MAYSCRIPT ";
	code += "archive='" + 	contextPath+"/TreeView.jar," + contextPath+"/nanoxml-2.2.2.jar,"+ contextPath+"/analysis.jar' "; 
	code += "width='1' height='1' alt='Your browser is not running the visualisation applet!' >\n";
	Set<String> keySet = params.keySet();
	for (String key : keySet) {
	    String value = params.get(key);
	    if (value != null)
		code += "<param name='" + key + "' value='" + value + "'/>";
	}
	code += "<param name='sessionId' value='"+ session.getId() + "'/>";
	code += "</applet>";
	return code;
    }
    
    //*****************************************************************************************
	public static HashSet<String> getDifference(ArrayList<String> a, ArrayList<String> b, boolean ignoreCase) {	// returns a - b
	if (a == null || a.size() == 0)
	    return null;
	
	if (b == null || b.size()==0) 
	    return new HashSet<String>(a);
	
	HashSet<String> resultHashSet = new HashSet<String>();
	for (String itemId : a)  
	    if (ignoreCase) {
		boolean found = false;
		for (String id : b) {
		    if (itemId.equalsIgnoreCase(id)) {
			found = true;
			break;
		    }
		}
		if (!found)
		    resultHashSet.add(itemId.toLowerCase());
	    }
	    else
		if (!b.contains(itemId)) 
		    resultHashSet.add(itemId);
	
	return resultHashSet;	// Ids that are not found (or not visible for this user) in the database
    }
    
    /**
     * @author xingjun - 04/02/2010
     * masterId = masterTableId + "_" + sectionId
     * or
     * masterId = masterTableId
     */
    public static String[] parseMasterTableId(String masterId) {
	String[] result = new String[2];
	if (masterId.indexOf("_") == -1) {
	    result[0] = masterId.substring(0, 1);
	    result[1] = "0";
	} else {
	    int delimiterPos = masterId.indexOf("_");
	    result[0] = masterId.substring(0, delimiterPos);
	    result[1] = masterId.substring(delimiterPos+1, masterId.length());;
	}
	return result;
    }
    
    /**
     * @author xingjun - 23/02/2010
     * <p>xingjun - 23/04/2010 - change 'Editing in Progress' into 'Not Public'</p>
     * @param subStatus
     * @return
     */
    public static int getSubmissionStatusByName(String subStatus, boolean loggedIn) {
	if (subStatus == null || subStatus.trim().equals("")) {
	    return 0;
	}
	if (loggedIn) {
	    if (subStatus.equals("Public")) {
		return 4;
	    } else if (subStatus.equals("Awaiting Annotation")) {
		return 5;
	    } else if (subStatus.equals("Partially Annotated by Annotator")) {
		return 19;
	    } else if (subStatus.equals("Completely Annotated by Annotator")) {
		return 20;
	    } else if (subStatus.equals("Partially Annotated by Sr. Annotator")) {
		return 21;
	    } else if (subStatus.equals("Awaiting Editor QA")) {
		return 22;
	    } else if (subStatus.equals("Partially Annotated by Editor")) {
		return 23;
	    } else if (subStatus.equals("Completely Annotated by Editor")) {
		return 24;
	    } else if (subStatus.equals("Partially Annotated by Sr. Editor")) {
		return 25;
	    }
	} else {
	    if (subStatus.equals("Not Public")) {
		return 0;
	    } else if (subStatus.equals("Available")) {
		return 1;
	    }
	}
	return 0;
    }
    
    public static String checkAccessionInput(String accessionInput)
    {
	// check Numeric
        if (accessionInput.matches("(?i)[0-9]+$")) {
	    String gudmap = "GUDMAP:" + accessionInput;
	    String maprobe = "maprobe:" + accessionInput;
	    String mgi = "MGI:" + accessionInput;
	    String mtf = "MTF#" + accessionInput;
	    String ensmusg = "ENSMUSG" + accessionInput;
	    /*
	      String tub = "TUB" + accessionInput;
	      String msr = "MSR" + accessionInput;
	      String gpl = "GPL" + accessionInput;
	      String gse = "GSE" + accessionInput;
	      String gsm = "GSM" + accessionInput;
	      String ensmust = "ENSMUST" + accessionInput;
	      String ensmusp = "ENSMUSP" + accessionInput;
	    */
	    return gudmap + "; " + maprobe + "; " + mgi  + "; " + ensmusg + "; " + mtf ;
	}
	
	// check GUDMAP
        if (accessionInput.matches("(?i)^gudmap:[0-9]+$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gudmap[\\W]*[\\w]*[0-9]+$"))
	    return "GUDMAP:" + getDigits(accessionInput, true);
	
        // check TUB
        if (accessionInput.matches("(?i)^tub[0-9]{3,3}[1-9]$"))
	    return accessionInput;
        if (accessionInput.matches("(?i)^tub[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "TUB" + getDigits(accessionInput, true);
	
        // check MSR
        if (accessionInput.matches("(?i)^msr[0-9]{3,3}[1-9]$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^msr[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "MSR" + getDigits(accessionInput, true);
	
        // check maprobe
        if (accessionInput.matches("(?i)^maprobe:[1-9]{4,4}$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^maprobe[\\W]*[\\w]*[1-9]{4,4}$"))
	    return "maprobe:" + getDigits(accessionInput, true);
	
        // check GEO - GPL
        if (accessionInput.matches("(?i)^gpl[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gpl[\\W]*[1-9][0-9]*$"))
	    return "GPL" + getDigits(accessionInput, false);
	
        // check GEO - GSE
        if (accessionInput.matches("(?i)^gse[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gse[\\W]*[1-9][0-9]*$"))
	    return "GSE" + getDigits(accessionInput, false);
	
        // check GEO - GSM
        if (accessionInput.matches("(?i)^gsm[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gsm[\\W]*[1-9][0-9]*$"))
	    return "GSM" + getDigits(accessionInput, false);
	
        // check MGI
        if (accessionInput.matches("(?i)^mgi:[1-9][1-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^mgi[\\W]*[1-9][0-9]*$"))
	    return "MGI:" + getDigits(accessionInput, false);
	
        // check MTF
        if (accessionInput.matches("(?i)^mtf#[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^mtf[\\W]*[1-9][0-9]*$"))
	    return "MTF#" + getDigits(accessionInput, false);
	
        // check ENSEMBLE
        if (accessionInput.matches("(?i)^ensmus[gtp][0-9]{11,11}$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^ensmus[gtp][\\W]*[0-9]+$"))
	    return getEnsemble(accessionInput);
	
	return accessionInput;
    }
    
    public static String getDigits(String str, Boolean allowLeadingZeros)
    {
        String digits = "";
        Boolean digitFound = false;
	
        for (int i=0; i < str.length(); i++)
	    {
		if (Character.isDigit(str.charAt(i)))
		    {
			if (!allowLeadingZeros)
			    {
				if (str.charAt(i) != '0')
				    digitFound = true;
			    }
			
			if (allowLeadingZeros || digitFound)
			    {
				digits += str.charAt(i);
			    }
		    }
	    }
        return digits;
    }
    
    public static String getEnsemble(String ensembleString)
    {
        String digits = "";
	
        // extract the digit part of the string 'str'
        for (int i=0; i < ensembleString.length(); i++)
	    {
		if (Character.isDigit(ensembleString.charAt(i)))
		    {
			digits += ensembleString.charAt(i);
		    }
	    }
	
        if (digits.length() == 0)
            return ensembleString;
	
        // pad the string with leading zeros if required
        if (digits.length() < 11)
	    {
		digits = padZero(digits);
	    }
	
        // rebuild the ensemble string
        if (ensembleString.matches("(?i)^ensmusg[\\p{Print}]*"))
            return "ensmusg" + digits;
	
        else if (ensembleString.matches("(?i)^ensmust[\\p{Print}]*"))
            return "ensmust" + digits;
	
        else if (ensembleString.matches("(?i)^ensmusp[\\p{Print}]*"))
            return "ensmusp" + digits;
	
        else return ensembleString;
    }

    public static String padZero(String str)
    {
        if (str.length() < 11)
        {
            String newStr = "0" + str;
            return padZero(newStr);
        }
        return str;
    }

    public static String netTrim(String input) {
	String ret = input;
	if (null != ret) {
	    ret = ret.trim();
	    if (ret.equals(""))
		ret = null;
	}
	
	return ret;
    }
    
    public static String superscriptAllele(String input) {
    	String alleleName = input;
		if (null != alleleName &&
		    -1 != alleleName.indexOf("<")) {
			alleleName = alleleName.replace("<", "##1");
			alleleName = alleleName.replace(">", "##2");
			alleleName = alleleName.replace("##1", "<sup>");
			alleleName = alleleName.replace("##2", "</sup>");
			
		}
		return alleleName;
	} 
    
    public static ArrayList formatResultSet(ResultSet resSet) throws SQLException {
		
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
    
  //QUERYROW=THE POSITION OF THE GENOTYPE COLUMN IN THE RESULT SET	
    public static ArrayList formatGenotypeResultSet(ArrayList series, ArrayList genotype, int queryRow) throws SQLException {
    		
       		ArrayList<String[]> results = series;
       		if(genotype==null)
       			return results;
       		int seriesColNum = ((String[])series.get(0)).length;
    		int seriesRowNum = series.size();
    		int genotypeColNum = ((String[])genotype.get(0)).length;
    		int genotypeRowNum = genotype.size();
    		
    		
    		for(int i=0; i<seriesRowNum; i++) {
    			String[] row = (String[])series.get(i); 
    			
    			if(!row[queryRow].equalsIgnoreCase("wild type"))
    			{
    				row[queryRow]="";
    				for(int j=0; j<genotypeRowNum; j++) {
    					String[] row2 = (String[])genotype.get(j);
    					//if SUB_ACCESSION_ID's match
    					if(row2[0].equals(row[0]))
    					{
    						if(!row[queryRow].contains(row2[1])) {
    							if(!row[queryRow].equals(""))
    								row[queryRow]+=", "+row2[1];
    							else
    								row[queryRow]+=row2[1];
    						}
    					}
    				}
    			}
    		
    		}
    		return results;
    	}
	
}

