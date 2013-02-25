package gmerg.entities.submission;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

public class Gene {

    private String symbol;
    private String name;
    private String synonyms;
    private String xsomeStart;
    private String xsomeEnd;
    private String xsomeName;
    private String genomeBuild;
    private String mgiAccID;
    private String mgiURL;
    private String ensemblURL;
    private String ensemblID;
    private String goURL;
    private String omimURL;
    private String entrezURL;
    private String entrezID;
    private String refSeqID;
    private String refSeqURL;
    private ArrayList ishSubmissions;
    private ArrayList maProbes;
    private ArrayList arraySubmissions;
    private ArrayList ontologyTerms;
    private String geneCardURL;
    private String hgncSearchSymbolURL;
    private String numMicArrays;
    private String ucscURL;

    /**
     * @param value the gene symbol
     */
    public void setSymbol(String value) {
        this.symbol = value;
    }

    /**
     * @return the gene symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param value the name of the gene
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * @return the name of the gene
     */
    public String getName() {
        return name;
    }

    public void setSynonyms(String value) {
        this.synonyms = value;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setXsomeStart(String value) {
        this.xsomeStart = value;
    }

    public String getXsomeStart() {
        return xsomeStart;
    }

    public void setXsomeEnd(String value) {
        this.xsomeEnd = value;
    }

    public String getXsomeEnd() {
        return xsomeEnd;
    }

    public void setXsomeName(String value) {
        this.xsomeName = value;
    }

    public String getXsomeName() {
        return xsomeName;
    }

    public void setGenomeBuild(String value) {
        this.genomeBuild = value;
    }

    public String getGenomeBuild() {
        return genomeBuild;
    }

    /**
     * @param value MGI Accession ID
     */
    public void setMgiAccID(String value) {
        this.mgiAccID = value;
    }

    public String getMgiAccID() {
        return mgiAccID;
    }

    public void setMgiURL(String value) {
        this.mgiURL = value;
    }

    public String getMgiURL() {
        return mgiURL;
    }

    public void setEntrezURL(String value) {
        this.entrezURL = value;
    }

    public String getEntrezURL() {
        return entrezURL;
    }

    public void setEntrezID(String value) {
        this.entrezID = value;
    }

    public String getEntrezID() {
        return entrezID;
    }

    public void setRefSeqURL(String value) {
        this.refSeqURL = value;
    }

    public String getRefSeqURL() {
        return refSeqURL;
    }

    public void setRefSeqID(String value) {
        this.refSeqID = value;
    }

    public String getRefSeqID() {
        return refSeqID;
    }

    public void setEnsemblURL(String value) {
        this.ensemblURL = value;
    }

    public String getEnsemblURL() {
        return ensemblURL;
    }

    public void setEnsemblID(String value) {
        this.ensemblID = value;
    }

    public String getEnsemblID() {
        return ensemblID;
    }

    public void setGoURL(String value) {
        this.goURL = value;
    }

    public String getGoURL() {
        return goURL;
    }

    public void setOmimURL(String value) {
        this.omimURL = value;
    }

    public String getOmimURL() {
        return omimURL;
    }

    public void setIshSubmissions(ArrayList values) {
        this.ishSubmissions = values;
    }

    public ArrayList getIshSubmissions() { 	  	
        return ishSubmissions;
    }

    public void setAssocProbes(ArrayList values) {
        this.maProbes = values;
    }

    public ArrayList getAssocProbes() {
        return maProbes;
    }

    public void setArraySubmissions(ArrayList arraysubs) {
        this.arraySubmissions = arraysubs;
    }

    public ArrayList getArraySubmissions() {
        return arraySubmissions;
    }
    
    public String getGeneCardURL() {
        return geneCardURL;
    }
    
    public void setGeneCardURL(String value){
        geneCardURL = value;
    }
    
    public String getHgncSearchSymbolURL() {
        return hgncSearchSymbolURL;
    }
    
    public void setHgncSearchSymbolURL(String value){
        hgncSearchSymbolURL = value;
    }
    
    public String getNumMicArrays() {
        return numMicArrays;
    }
    
    public void setNumMicArrays(String value){
        numMicArrays = value;
    }
    public ArrayList getOntologyTerms() {
    	return ontologyTerms;
    }
    
    public void setOntologyTerms(ArrayList terms) {
    	ontologyTerms = terms;
    }
    
    public String getUcscURL() {
    	return ucscURL;
    }
    
    public void setUcscURL(String ucscURL) {
    	this.ucscURL = ucscURL;
    }

}
