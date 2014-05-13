/**
 * this file stores all sqls linked to microarray
 */
package gmerg.db;

import java.util.ResourceBundle;

/**
 * @author xingjun 
 *
 */
public class ArrayDBQuery {
    protected static boolean debug = false;

	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");

	// - rewrote the sql to exclude the table MIC_ANALYSIS_MASTER from the sql
	// - added criteria for sectionId
	final static String name1 = "MASTER_TABLE_EXPRESSION_TITLES";
	final static String query1 = "SELECT AMH_ORDER, 'Master Expression', AMG_DISPLAY_NAME, AMG_DESCRIPTION " +
			"FROM MIC_ANAL_MSTR_GRP_HEADER " +
			"JOIN MIC_ANAL_MASTER_GROUP ON AMH_AM_GROUP_FK = AMG_OID " +
			"WHERE AMH_A_MASTER_FK = ? " +
			"AND AMG_SECTION_FK = ? ";
	
                // the first one is not needed
	final static int ANNOTATION_COLUMN_NUMBER = 6;

	final static String name2 = "MIC_ANALYSIS_ANNOTATION";
    final static String query2 = "SELECT MAN_PLATFORM_ID, MAN_GENE_SYMBOL, "+
	" MAN_PROBE_SEQ_ID, MAN_MGI_ID,  MAN_ENTREZ_GENE_ID, " +
	" MAN_HUMAN_ORTH_SYMBOL, MAN_HUMAN_ORTH_ENTREZ_ID "+
	"FROM MIC_ANAL_ANNOTATION " +
	"WHERE MAN_PROBE_SET_ID " +
	"ORDER BY FIELD(MAN_PROBE_SET_ID, PROBE_SET_ID_ARG) ";

	// find the distribution of theiler stage of the microarray data
	final static String name3 = "GENE_THEILER_STAGES_ARRAY";
	final static String query3 = "SELECT DISTINCT MBC_STG_NAME FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ? ORDER BY NATURAL_SORT(MBC_STG_NAME) ";
	  
	final static String name4 = "TOTAL_NUMBER_OF_SUBMISSION_ARRAY";
	final static String query4 = "SELECT COUNT(DISTINCT MBC_SUB_ACCESSION_ID) " +
			"FROM MIC_BROWSE_CACHE, ANA_TIMED_NODE, ANA_NODE " +
			"WHERE ATN_PUBLIC_ID = MBC_COMPONENT_ID " +
			"AND ATN_NODE_FK = ANO_OID ";
	  
	final static String name5 = "GET_GENE_BY_SYMBOL";
	final static String query5 = "SELECT GNF_SYMBOL, GNF_NAME from REF_GENE_INFO WHERE GNF_SYMBOL = ";
	  
	final static String name6 = "GET_ALL_ANALYSIS_GENELISTS";
	final static String query6 = "SELECT ANG_OID, ANG_TITLE, PER_NAME, ANG_SUMMARY, IF (ANG_CDT_FILENAME = '', URL_URL, '') , ANG_FILEPATH, ANG_FILENAME, ANG_CDT_FILENAME, ANG_SUBMITTER, ANG_ENTRY_DATE, ANG_IS_CLUSTERED, ANG_NUMBER_OF_ENTRIES, ANG_PLATFORM_ID " +
			"FROM MIC_ANALYSIS_GENELIST " +
			"JOIN ISH_PERSON ON ANG_SUBMITTER = PER_OID " +
			"JOIN REF_URL " +
			"WHERE ANG_IS_CLUSTERED = 0 " +
			"AND URL_OID = 37 ";
	  
	final static String name7 = "GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_ID";
	final static String query7 = "SELECT GLE_ENTITY_ID FROM GNL_ENTITIES " +
			"JOIN REF_GENELISTS ON GLE_LIST_ID = GNL_OID " +
			"WHERE GNL_OID = ? " +
			"ORDER BY GLE_ORDER ";
	  
	final static String name25 = "GET_PROBE_SET_ID_BY_ANALYSIS_GENELIST_CLUSTER_ID";
	final static String query25 = "SELECT GCT_PROBE_SET_ID FROM MIC_ANAL_GLST_COLUMN_ITEM  " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
			"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
			"WHERE ANG_OID = ? " +
			"AND AGC_OID = ? " +
			"ORDER BY GCT_OID ";
	  
	final static String name8 = "GET_ANALYSIS_GENELIST_TITLE";
	final static String query8 = "SELECT GNL_SHORT_NAME FROM REF_GENELISTS WHERE GNL_OID = ? ";
	
	final static String name26 = "GET_ANALYSIS_GENELIST_CLUSTER_TITLE";
	final static String query26 = "SELECT AGC_TITLE FROM MIC_ANAL_GENELIST_COLUMN WHERE AGC_OID = ? ";
	
	final static String name9 = "COMPONENT_EXPRESSION_OF_GIVEN_PROBE_SET_IDS";
	final static String query9 = "SELECT AME_M_HEADER_FK, AME_PROBE_SET_FK, AME_VALUE_RMA FROM MIC_ANAL_MSTR_EXPRESSION " +
	"JOIN MIC_PROBE_SET ON AME_PROBE_SET_FK = PRS_OID " +
	"WHERE PRS_PROBE_SET_ID " +
	"AND AME_M_HEADER_FK = ? " +
	"ORDER BY AME_VALUE_RMA ";
	    
	final static String name10 = "SERIES_DATA_BY_OID";
    final static String query10 = "SELECT SER_GEO_ID, COUNT(distinct SRM_SAMPLE_FK), " +
    		"SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN, SER_OID, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', '), SUB_ARCHIVE_ID " +
    		"FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANA_TIMED_NODE " +
    		"WHERE SER_OID = ? " +
    		"AND SRM_SERIES_FK = SER_OID " +
            "AND SRM_SAMPLE_FK = SMP_OID " +
            "AND SMP_SUBMISSION_FK = SUB_OID " +
            "AND EXP_SUBMISSION_FK=SUB_OID " +
            "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
            "AND ATN_NODE_FK = ANO_OID " +
    		"GROUP BY SER_GEO_ID, SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN ";
                                   
    final static String SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, SMP_GEO_ID, SRM_SAMPLE_ID, " +
    		"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'Wild Type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
    		"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) " +
    		"FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
    		"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND " +
    		"SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE,  SRM_SAMPLE_DESCRIPTION, " +
    		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
    final static String SAMPLE_SERIES_TABS_BY_OID = "FROM MIC_SAMPLE, MIC_SERIES_SAMPLE, MIC_SERIES, ISH_SUBMISSION, ISH_EXPRESSION, ISH_SPECIMEN, ANA_NODE, ANA_TIMED_NODE " + 
                                             "WHERE SER_OID = ? " + 
                                             "AND SER_OID = SRM_SERIES_FK " + 
                                             "AND SRM_SAMPLE_FK = SMP_OID " + 
                                             "AND SMP_SUBMISSION_FK = SUB_OID " +
                                             "AND EXP_SUBMISSION_FK=SUB_OID " +
                                             "AND SPN_SUBMISSION_FK=SUB_OID " +
                                             "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
                                             "AND ATN_NODE_FK = ANO_OID " +
                                             "GROUP BY SMP_OID ";
    
   /* final static String SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, SMP_GEO_ID, SRM_SAMPLE_ID, SRM_SAMPLE_DESCRIPTION, " +
    		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
    final static String SAMPLE_SERIES_TABS_BY_OID = "FROM MIC_SAMPLE, MIC_SERIES_SAMPLE, MIC_SERIES, ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANA_TIMED_NODE " + 
                                             "WHERE SER_OID = ? " + 
                                             "AND SER_OID = SRM_SERIES_FK " + 
                                             "AND SRM_SAMPLE_FK = SMP_OID " + 
                                             "AND SMP_SUBMISSION_FK = SUB_OID " +
                                             "AND EXP_SUBMISSION_FK=SUB_OID " +
                                             "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
                                             "AND ATN_NODE_FK = ANO_OID " +
                                             "GROUP BY SMP_OID ";*/
    
    final static String name11 = "SERIES_SAMPLES_BY_OID";
    final static String query11 = SAMPLE_SERIES_COLS + SAMPLE_SERIES_TABS_BY_OID;

	
	final static String name13 = "MASTER_TABLE_LIST";
	final static String query13 = "SELECT AMT_OID, AMT_PLATFORM_ID, AMT_TITLE, AMT_DESCRIPTION FROM MIC_ANALYSIS_MASTER " +
			"WHERE AMT_STATUS = 1 " +
			"ORDER BY AMC_OID ";
	
	final static String name20 = "MASTER_SECTION_LIST";
	final static String query20 = "SELECT AMT_OID, AMC_OID, AMC_TITLE, AMC_DESCRIPTION, AMT_PLATFORM_ID " +
			"FROM MIC_ANALYSIS_MASTER " +
			"JOIN MIC_ANAL_MASTER_SECTION ON AMC_A_MASTER_FK = AMT_OID " +
			"WHERE AMT_STATUS = 1 " +
			"ORDER BY AMC_OID DESC ";
	  
	final static String name14 = "MASTER_TABLE_PLATFORM_ID";
	final static String query14 = "SELECT AMT_PLATFORM_ID FROM MIC_ANALYSIS_MASTER WHERE AMT_OID = ? ";
		  
	final static String name15 = "GET_ANALYSIS_GENELIST_PLATFORM";
	final static String query15 = "SELECT GNL_PLATFORM_GEO_ID FROM REF_GENELISTS WHERE GNL_OID = ? ";
	  
	final static String name16 = "GET_GENE_SYMBOL_BY_ANALYSIS_GENELIST_ID";
	final static String query16 = "SELECT DISTINCT GNF_SYMBOL " +
			"FROM MIC_ANAL_GLST_COLUMN_ITEM " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
			"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
			"JOIN MIC_PROBE_SET ON GCT_PROBE_SET_ID = PRS_PROBE_SET_ID " +
			"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
			"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
			"WHERE ANG_OID = ? " + // genelist id
			"AND PRS_PLATFORM_ID = ? ";
	  
	final static String name24 = "GET_GENE_SYMBOL_BY_ANALYSIS_GENELIST_CLUSTER_ID";
	final static String query24 = "SELECT DISTINCT GNF_SYMBOL " +
			"FROM MIC_ANAL_GLST_COLUMN_ITEM " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
			"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
			"JOIN MIC_PROBE_SET ON GCT_PROBE_SET_ID = PRS_PROBE_SET_ID " +
			"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
			"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
			"WHERE ANG_OID = ? " +
			"AND PRS_PLATFORM_ID = ? " +
			"AND AGC_OID = ? ";
	  
	
	
	final static String name17 = "GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS";
	final static String query17 = "SELECT AME_M_HEADER_FK, AME_PROBE_SET_FK, AME_VALUE_RMA, AMS_MEDIAN, AMS_STD FROM MIC_ANAL_MSTR_EXPRESSION " +
			"JOIN MIC_ANAL_MSTR_GRP_HEADER ON AME_M_HEADER_FK = AMH_OID " +
			"JOIN MIC_ANALYSIS_MASTER ON AMT_OID = AMH_A_MASTER_FK " +
			"JOIN MIC_PROBE_SET ON AME_PROBE_SET_FK = PRS_OID " +
			"JOIN MIC_ANAL_MASTER_GROUP ON AMG_OID = AMH_AM_GROUP_FK " +
			"JOIN MIC_ANAL_MSTR_EXP_SUMMARY ON AMS_A_MASTER_FK = AMH_A_MASTER_FK AND AMS_PROBE_SET_FK = PRS_OID " +
			"JOIN MIC_ANAL_GLST_COLUMN_ITEM ON PRS_PROBE_SET_ID = GCT_PROBE_SET_ID " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON AGC_OID = GCT_GLST_COLUMN_FK " +
			"JOIN MIC_ANALYSIS_GENELIST ON ANG_OID = AGC_ANAL_GENELIST_FK " +
			"WHERE PRS_PROBE_SET_ID " +
			"AND PRS_PLATFORM_ID = AMT_PLATFORM_ID " +
			"AND AMH_A_MASTER_FK = ? " +
			"AND AMG_SECTION_FK = ? " +
			"AND ANG_OID = ? " +
			"ORDER BY AME_M_HEADER_FK, GCT_OID ";
	  
	final static String name27 = "GET_EXPRESSION_OF_GIVEN_PROBE_SET_IDS_CLUSTER";
	final static String query27 = "SELECT AME_M_HEADER_FK, AME_PROBE_SET_FK, AME_VALUE_RMA, AMS_MEDIAN, AMS_STD FROM MIC_ANAL_MSTR_EXPRESSION " +
			"JOIN MIC_ANAL_MSTR_GRP_HEADER ON AME_M_HEADER_FK = AMH_OID " +
			"JOIN MIC_ANALYSIS_MASTER ON AMT_OID = AMH_A_MASTER_FK " +
			"JOIN MIC_PROBE_SET ON AME_PROBE_SET_FK = PRS_OID " +
			"JOIN MIC_ANAL_MASTER_GROUP ON AMG_OID = AMH_AM_GROUP_FK " +
			"JOIN MIC_ANAL_MSTR_EXP_SUMMARY ON AMS_A_MASTER_FK = AMH_A_MASTER_FK AND AMS_PROBE_SET_FK = PRS_OID " +
			"JOIN MIC_ANAL_GLST_COLUMN_ITEM ON PRS_PROBE_SET_ID = GCT_PROBE_SET_ID " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON AGC_OID = GCT_GLST_COLUMN_FK " +
			"JOIN MIC_ANALYSIS_GENELIST ON ANG_OID = AGC_ANAL_GENELIST_FK " +
			"WHERE PRS_PROBE_SET_ID " +
			"AND PRS_PLATFORM_ID = AMT_PLATFORM_ID " +
			"AND AMH_A_MASTER_FK = ? " +
			"AND AMG_SECTION_FK = ? " +
			"AND ANG_OID = ? " +
			"AND AGC_OID = ? " +
			"ORDER BY AME_M_HEADER_FK, GCT_OID ";

	final static String name18 = "GET_RELEVANT_PROBE_SET_IDS";
	final static String query18 = "SELECT PRS_PROBE_SET_ID FROM MIC_PROBE_SET WHERE PRS_PLATFORM_ID = ? AND PRS_PROBE_SET_ID IN ";
	  
	final static String name21 = "ALL_ANALYSIS_GENELISTS_WITH_FOLDER_IDS";
	final static String query21 = "SELECT ANG_OID, ANG_TITLE, PER_NAME, ANG_SUMMARY, URL_URL, ANG_FILEPATH, ANG_FILENAME, ANG_CDT_FILENAME, ANG_SUBMITTER, ANG_ENTRY_DATE, ANG_IS_CLUSTERED, ANG_NUMBER_OF_ENTRIES, AGF_FOLDER_ID, ANG_PLATFORM_ID " +
			"FROM MIC_ANALYSIS_GENELIST " +
			"JOIN MIC_ANAL_GLST_FOLDER ON AGF_A_GLST_FK = ANG_OID " +
			"JOIN ISH_PERSON ON ANG_SUBMITTER = PER_OID " +
			"JOIN REF_URL " +
			"WHERE URL_OID = 37 " +
			"ORDER BY ANG_OID, AGF_FOLDER_ID ";
	  
	final static String name22 = "GET_CLUSTERS_FOR_GIVEN_GENELISTS";
	final static String query22 = "SELECT AGC_OID, AGC_CLUSTER_FILENAME, AGC_ITEM_NUMBER, AGC_TITLE, AGC_DISPLAY_ORDER " +
			"FROM MIC_ANAL_GENELIST_COLUMN " +
			"WHERE AGC_ANAL_GENELIST_FK = ? " +
			"ORDER BY AGC_DISPLAY_ORDER ";
	  
	final static String name23 = "GET_ANALYSIS_GENELIST_PLATFORM_BY_CLUSTER_ID";
	final static String query23 = "SELECT ANG_PLATFORM_ID " +
			"FROM MIC_ANALYSIS_GENELIST " +
			"JOIN MIC_ANAL_GENELIST_COLUMN ON AGC_ANAL_GENELIST_FK = ANG_OID " +
			"WHERE ANG_OID = ? " +
			"AND AGC_OID = ? ";
	  
	final static String name28 = "GET_SUBMISSION_TISSUE";
	final static String query28 = "SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE " +
	"JOIN ANA_TIMED_NODE ON ATN_NODE_FK = ANO_OID " +
	"JOIN ISH_SP_TISSUE ON IST_COMPONENT  = ATN_PUBLIC_ID " +
	"WHERE IST_SUBMISSION_FK = SUBSTR(?,8) ";
	final static String name29 = "ALL_SERIES";
	final static String query29 = "SELECT DISTINCT SER_TITLE, SER_GEO_ID, (SELECT COUNT(distinct SRM_SAMPLE_FK) FROM MIC_SERIES_SAMPLE WHERE SRM_SERIES_FK = SER_OID) SAMPLE_NUMBER, " +
			"SUB_SOURCE, " +
			"PLT_GEO_ID, SER_OID, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') " +
			"FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, MIC_PLATFORM, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +
			"WHERE SRM_SERIES_FK = SER_OID " +
			"AND SRM_SAMPLE_FK = SMP_OID " +
			"AND SMP_SUBMISSION_FK = SUB_OID " +
			"AND EXP_SUBMISSION_FK=SUB_OID " +
			"AND SUB_PI_FK = PER_OID " +
			"AND PLT_OID = SER_PLATFORM_FK " +
			"AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
			"AND ATN_NODE_FK = ANO_OID " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
			"AND SER_PLATFORM_FK " + 
			"GROUP BY SER_GEO_ID ";

	final static String name30 = "GET_SAMPLE_LIST";
	final static String query30 = "SELECT GNL_OID,GNL_SHORT_NAME FROM REF_GENELISTS " +
			"WHERE GNL_MA_DATASET = ? " +
			"AND GNL_STAGE = ? " +
			"AND GNL_KEY_SAMPLE = ? ";

	final static String name31 = "GET_ANALYSIS_GENELIST";
	final static String query31 = "SELECT DISTINCT MAN_HUMAN_ORTH_SYMBOL FROM MIC_ANAL_ANNOTATION, GNL_ENTITIES " +
			"WHERE MAN_PROBE_SET_ID = GLE_ENTITY_ID " +
			"AND GLE_LIST_ID = ? " +
			"AND MAN_HUMAN_ORTH_SYMBOL != ''";

	final static String name32 = "GET_ALL_REF_GENELISTS";
	final static String query32 = "SELECT GNL_OID,GNL_UID,GNL_SHORT_NAME,GNL_DESCRIPTION,GNL_SERIES_PLATFORM," +
			"GNL_PLATFORM_GEO_ID,GNL_KEY_SAMPLE,GNL_EMAP_ID,GNL_MA_DATASET,GNL_MA_DATASET_ID,GNL_GEN_METHOD,GNL_ENTITY_TYPE," +
			"GNL_TOTAL_ENTITIES,GNL_TOTAL_GENES,GNL_AUTHOR,GNL_DATE,GNL_VERSION,GNL_REFERENCE,GNL_IS_PUBLISHED,GNL_OTHER_REFS," +
			"GNL_STAGE,GNL_GENELIST_TYPE,GNL_SEX,GNL_SUBSET_1,GNL_SUBSET_2,GNL_SUBSET_3,GNL_AMG_OID_FK,LPU_REF " +
			"FROM REF_GENELISTS LEFT JOIN ISH_LINKED_PUBLICATION ON LPU_OID = GNL_REFERENCE";
	
	final static String NGD_SAMPLE_SERIES_COLS = "SELECT DISTINCT SUB_ACCESSION_ID, NGS_GEO_ID, NGS_SAMPLE_NAME, NGP_LIBRARY_STRATEGY, "+
			"CASE NGS_GENOTYPE WHEN 'true' THEN 'Wild Type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS GENOTYPE, NGS_DESCRIPTION, " +
    		"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ')  ";
    final static String NGD_SAMPLE_SERIES_TABS_BY_OID = "FROM NGD_SAMPLE, NGD_SAMPLE_SERIES, NGD_SERIES, NGD_PROTOCOL, ISH_SUBMISSION, ISH_SP_TISSUE, ANA_NODE, ANA_TIMED_NODE " + 
                                             "WHERE NGR_OID = ? " + 
                                             "AND NGL_SERIES_FK = NGR_OID " + 
                                             "AND NGL_SAMPLE_FK = NGS_OID " + 
                                             "AND NGS_PROTOCOL_FK=NGP_OID " +
                                             "AND NGS_SUBMISSION_FK = SUB_OID " +
                                             "AND IST_SUBMISSION_FK=SUB_OID " +
                                             "AND ATN_PUBLIC_ID = IST_COMPONENT " +
                                             "AND ATN_NODE_FK = ANO_OID " +
                                             "GROUP BY NGS_OID ";
                                             
    final static String name33 = "NGD_SERIES_SAMPLES_BY_OID";
    final static String query33 = NGD_SAMPLE_SERIES_COLS + NGD_SAMPLE_SERIES_TABS_BY_OID;
    
    final static String name34 = "NGD_SERIES_DATA_BY_OID";
    final static String query34 = "SELECT NGR_GEO_ID, SUB_ARCHIVE_ID, COUNT(distinct NGL_SAMPLE_FK), NGR_TITLE, NGR_SUMMARY, NGR_OVERALL_DESIGN " + 
            "FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, ISH_SUBMISSION " + 
            "WHERE NGR_OID = ? " + 
            "AND NGL_SERIES_FK = NGR_OID " +
            "AND NGL_SAMPLE_FK = NGS_OID " +
            "AND NGS_SUBMISSION_FK = SUB_OID " +
            "GROUP BY NGR_GEO_ID, NGR_TITLE, NGR_SUMMARY,NGR_OVERALL_DESIGN ";
    
    final static String name35 = "ALL_NGD_SERIES";
	final static String query35 = "SELECT DISTINCT NGR_TITLE, NGR_GEO_ID, SUB_SOURCE, (SELECT COUNT(distinct NGL_SAMPLE_FK) FROM NGD_SAMPLE_SERIES WHERE NGL_SERIES_FK = NGR_OID) SAMPLE_NUMBER, " +
			"NGP_LIBRARY_STRATEGY, " +
			"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', '), NGR_OID " +
			"FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_PROTOCOL, NGD_SAMPLE, ISH_SUBMISSION, ISH_SP_TISSUE, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +
			"WHERE NGL_SERIES_FK = NGR_OID " +
			"AND NGL_SAMPLE_FK = NGS_OID " +
			"AND NGS_SUBMISSION_FK = SUB_OID " +
			"AND NGS_PROTOCOL_FK=NGP_OID " +
			"AND IST_SUBMISSION_FK=SUB_OID " +
			"AND SUB_PI_FK = PER_OID " +
			"AND ATN_PUBLIC_ID = IST_COMPONENT " +
			"AND ATN_NODE_FK = ANO_OID " +
			"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
			"GROUP BY NGR_OID ";
	
	final static String name = "";
	final static String query = "";
	  
	static ParamQuery pqList[] = {
		new ParamQuery(name1, query1),
		new ParamQuery(name2, query2),
		new ParamQuery(name3, query3),
		new ParamQuery(name4, query4),
		new ParamQuery(name5, query5),
		new ParamQuery(name6, query6),
		new ParamQuery(name7, query7),
		new ParamQuery(name8, query8),
		new ParamQuery(name9, query9),
		new ParamQuery(name10, query10),
		new ParamQuery(name11, query11),
		new ParamQuery(name13, query13),
		new ParamQuery(name14, query14),
		new ParamQuery(name15, query15),
		new ParamQuery(name16, query16),
		new ParamQuery(name17, query17),
		new ParamQuery(name18, query18),
		new ParamQuery(name20, query20),
		new ParamQuery(name21, query21),
		new ParamQuery(name22, query22),
		new ParamQuery(name23, query23),
		new ParamQuery(name24, query24),
		new ParamQuery(name25, query25),
		new ParamQuery(name26, query26),
		new ParamQuery(name27, query27),
		new ParamQuery(name28, query28),
		new ParamQuery(name29, query29),
		new ParamQuery(name30, query30),
		new ParamQuery(name31, query31),
		new ParamQuery(name32, query32),
		new ParamQuery(name33, query33),
		new ParamQuery(name34, query34),
		new ParamQuery(name35, query35)
	};
	
	// finds ParamQuery object by name and returns
	public static ParamQuery getParamQuery(String name) {

	    ParamQuery ret = null;

	    for (int i = 0; i < pqList.length; i++) {
		if (pqList[i].getQueryName().equals(name)) {
		    ret = pqList[i];

		    if (debug)
			System.out.println("sql = "+  ret.getQuerySQL().toLowerCase());
		    
		    break;
		}
	    }

	    return ret;
	}	  
}
