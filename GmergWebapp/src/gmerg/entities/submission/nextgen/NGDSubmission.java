package gmerg.entities.submission.nextgen;

import gmerg.entities.submission.Submission;
import gmerg.entities.submission.nextgen.Protocol;
import gmerg.entities.submission.nextgen.DataProcessing;
import gmerg.entities.submission.nextgen.NGDSupFiles;

import java.util.ArrayList;
import java.util.List;

public class NGDSubmission extends Submission {

    protected NGDSeries series;
    protected Protocol protocol;
    protected NGDSample sample;
    protected DataProcessing[] dataProcessing;
    protected String filesLocation;
    protected List<NGDSupFiles>  rawFile;
    protected List<NGDSupFiles> processedFile;
    protected ArrayList linkedSubmissions;
    protected ArrayList linkedPublications;
    protected String[] acknowledgements;

    public void setSeries(NGDSeries ser) {
        series = ser;
    }

    public NGDSeries getSeries() {
        return series;
    }

    public void setProtocol(Protocol pform) {
    	protocol = pform;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setSample(NGDSample smpl) {
        sample = smpl;
    }

    public NGDSample getSample() {
        return sample;
    }
    
    /*public void setDataProcessing(DataProcessing dataProcessing) {
    	this.dataProcessing = dataProcessing;
    }

    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }*/
    
    public void setDataProcessing(DataProcessing[] dataProcessing) {
    	this.dataProcessing = dataProcessing;
    }

    public DataProcessing[] getDataProcessing() {
        return dataProcessing;
    }

    public void setFilesLocation(String location) {
        filesLocation = location;
    }

    public String getFilesLocation() {
        return filesLocation;
    }

    public void setRawFile(List<NGDSupFiles> file) {
        rawFile = file;
    }

    public List<NGDSupFiles> getRawFile() {
        return rawFile;
    }

    public void setProcessedFile(List<NGDSupFiles> file) {
       processedFile = file;
    }

    public List<NGDSupFiles> getProcessedFile() {
        return processedFile;
    }
    
    
    public void setLinkedSubmissions(ArrayList linkedSubmissions){
    	this.linkedSubmissions = linkedSubmissions;
    }
    
    public ArrayList getLinkedSubmissions(){
    	return linkedSubmissions;
    }
    
    public void setLinkedPublications(ArrayList linkedPublications){
    	this.linkedPublications = linkedPublications;
    }
    
    public ArrayList getLinkedPublications(){
    	return linkedPublications;
    }
    
    public void setAcknowledgements (String[] acknowledgements){
    	this.acknowledgements=acknowledgements;
    }
    
    public String[] getAcknowledgements(){
    	return acknowledgements;
    }
    
}
