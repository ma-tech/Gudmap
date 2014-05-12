<!-- Author: Mehran Sharghi			 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  
 	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id (#{NGDSingleSubmissionBean.id})" rendered="#{!NGDSingleSubmissionBean.renderPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as private" rendered="#{!NGDSingleSubmissionBean.submission.released && !NGDSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as deleted" rendered="#{NGDSingleSubmissionBean.submission.deleted && NGDSingleSubmissionBean.submission.released}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as private and deleted" rendered="#{!NGDSingleSubmissionBean.submission.released && NGDSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="<br/><br/>For assistance please contact " rendered="#{!NGDSingleSubmissionBean.submission.released || NGDSingleSubmissionBean.submission.deleted}" escape="false"/>
	<h:outputLink styleClass="text_bottom" value="mailto:GUDMAP-EDITORS@gudmap.org" rendered="#{!NGDSingleSubmissionBean.submission.released || NGDSingleSubmissionBean.submission.deleted}">
		gudmap-editors@gudmap.org
	</h:outputLink>
	
	<h:form id="mainForm" rendered="#{NGDSingleSubmissionBean.renderPage && NGDSingleSubmissionBean.submission.released && !NGDSingleSubmissionBean.submission.deleted}">

		 <h:panelGrid columns="1" width="100%" styleClass="block-stripey">
		           <h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.submission.accID}" />
		 </h:panelGrid>
    
         <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
    	    <h:outputText value="#{stageSeriesLong} Stage" />
    	    <h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{NGDSingleSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{stageSeriesShort}#{NGDSingleSubmissionBean.submission.stage}" />
    	    </h:outputLink>
        </h:panelGrid>
        
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
        	<h:outputText value="External/Internal Links: " />
        	<h:panelGrid columns="1" columnClasses="plaintext, datatext">    
	    	    <h:outputLink styleClass="datatext" value="http://www.gudmap.org/Internal/Consortium/Work_In_Progress/nextgen.html">
	    			<h:outputText value="IGV" />
	    	    </h:outputLink>
	    	    <h:outputLink styleClass="datatext" value="http://www.gudmap.org/">
	    			<h:outputText value="UCSC" />
	    	    </h:outputLink>
    	    </h:panelGrid>
        </h:panelGrid>
<%--         <h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" rendered="#{not empty NGDSingleSubmissionBean.submission.resultNotes}">
 --%>
         <h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="arrayLCol,arrayRCol" rendered="#{not empty NGDSingleSubmissionBean.submission.resultNotes}">
			<h:outputText value="Results Notes" />
			<h:dataTable  columnClasses="text-normal,text-top"  value="#{NGDSingleSubmissionBean.submission.resultNotes}" var="note"  rendered="#{not empty NGDSingleSubmissionBean.submission.resultNotes}">
				<h:column>
					<h:outputText value="#{note}"/>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
        
		<h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{null != NGDSingleSubmissionBean.submission.originalImages}">
			<h:outputText value="Images"  />
			<h:dataTable  columnClasses="text-normal,text-top" value="#{NGDSingleSubmissionBean.submission.originalImages}" var="image">
			        <h:column>
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="mywindow=window.open('#{image.clickFilePath}','#{image.accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image.filePath}" width="80"/>
					</h:outputLink>
			        </h:column>
			        <h:column>
					<h:outputText styleClass="notetext, topAlign" value="#{image.note}"/>
			        </h:column>
			</h:dataTable>
		</h:panelGrid>
		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Supplementary Data Files" />
			<h:panelGrid columns="2" columnClasses="plaintext, datatext">
				<h:outputText value="Raw file:" />
				<h:dataTable  columnClasses="text-normal,text-top"  value="#{NGDSingleSubmissionBean.submission.rawFile}" var="rfile">
					<h:column>
						<h:outputLink styleClass="datatext" value="http://www.gudmap.org/pathtofile/#{rfile}">
							<h:outputText value="#{rfile}"/>
						</h:outputLink>
					</h:column>
				</h:dataTable>	
				<h:outputText value="Processed file:" />
				<h:dataTable  columnClasses="text-normal,text-top"  value="#{NGDSingleSubmissionBean.submission.processedFile}" var="pfile">
					<h:column>
						<h:outputLink styleClass="datatext" value="http://www.gudmap.org/pathtofile/#{pfile}">
							<h:outputText value="#{pfile}"/>
						</h:outputLink>
					</h:column>
				</h:dataTable>
			</h:panelGrid>	
        </h:panelGrid>
 		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{not empty NGDSingleSubmissionBean.submission.archiveId}">
			<h:outputText value="Archive/Batch ID"/>
			<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{NGDSingleSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{NGDSingleSubmissionBean.submission.archiveId != null}">
				<h:outputText value="#{NGDSingleSubmissionBean.submission.archiveId}" />
			</h:outputLink>
        </h:panelGrid>
		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
			<h:outputText value="Principal Investigator(s)" />
			<t:dataList id="piDataList" var="piInfo"
					value="#{NGDSingleSubmissionBean.submission.principalInvestigators}">
					<h:outputLink  title="#{piInfo.fullAddress}"  styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
						<h:outputText value="#{piInfo.name}, " />
					</h:outputLink>
					<h:outputText title="#{piInfo.fullAddress}"  styleClass="datatext" value="#{piInfo.displayAddress}" /><br/>
			</t:dataList>
        </h:panelGrid>
		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
			<h:outputText value="Submitted By" />
			<h:panelGroup>
				<h:outputLink title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{NGDSingleSubmissionBean.submission.submitter.id})">
				       <h:outputText value="#{NGDSingleSubmissionBean.submission.submitter.name}, " />
				</h:outputLink>
				<h:outputText title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.submitter.displayAddress}" />			
			</h:panelGroup>
        </h:panelGrid>
              
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey"  rendered="#{not empty NGDSingleSubmissionBean.submission.authors}">
			<h:outputText value="Contributors" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.authors}"   rendered="#{not empty NGDSingleSubmissionBean.submission.authors}"/>
        </h:panelGrid>
        
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Specimen Details" />
		
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
		
			<h:outputText value="Sample GEO ID:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.geoID}"/>
			<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external"  rendered="#{NGDSingleSubmissionBean.submission.sample.geoID != null}">
				<f:param value="#{NGDSingleSubmissionBean.submission.sample.geoID}" name="acc" />
				<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.geoID}" rendered="#{NGDSingleSubmissionBean.submission.sample.geoID != null}"/>
			</h:outputLink>
			
			<h:outputText value="Sample Description:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.description}" />
			
			<h:outputText value="Sample Name:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.title}" />
			
			<h:outputText value="Component(s) Sampled:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.source}" />
			
			<h:outputText value="Organism:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.organism}" />
			
			<h:outputText value="Strain:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.strain}" />


				<h:outputText value="Genotype:" />
				<h:outputText value="Wild type" rendered="#{null == ISHSingleSubmissionBean.submission.allele}"/>
			    <t:dataTable id="alleleContentTable" value="#{ISHSingleSubmissionBean.submission.allele}" var="allele"  style="margin-left:-5px; " rendered="#{null != ISHSingleSubmissionBean.submission.allele}">
				    <t:column>
				    <h:panelGrid columns="3">
					<h:outputText value="#{allele.title}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />

					<h:outputText value="Gene" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputLink styleClass="datatext" value="gene.html">
						<h:outputText value="#{allele.geneSymbol}" />
						<f:param name="gene" value="#{allele.geneSymbol}" />
					</h:outputLink>

					<h:outputText value="MGI ID"  rendered="#{not empty allele.alleleId}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleId}"/>
					<h:outputText rendered="#{empty allele.alleleIdUrl && not empty allele.alleleId}" value="#{allele.alleleId}" escape="false"/>
					<h:outputLink rendered="#{not empty allele.alleleIdUrl && not empty allele.alleleId}"  styleClass="datatext" value="#{allele.alleleIdUrl}" target="gmerg_external2">
						<h:outputText value="#{allele.alleleId}" />
					</h:outputLink>

					<h:outputText value="MGI Symbol or lab name" rendered="#{not empty allele.alleleName}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1"  rendered="#{not empty allele.alleleName}"/>
					<h:outputText value="#{allele.alleleName}" escape="false"  rendered="#{not empty allele.alleleName}"/>

					<h:outputText value="Type"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{allele.type}"/>
			
					<h:outputText value="Allele First Chromatid" rendered="#{not empty allele.alleleFirstChrom}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleFirstChrom}" />
					<h:outputText value="#{allele.alleleFirstChrom}" rendered="#{not empty allele.alleleFirstChrom}" escape="false"/>
									
					<h:outputText value="Allele Second Chromatid"  rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:outputText value="#{allele.alleleSecondChrom}"  rendered="#{not empty allele.alleleSecondChrom}" escape="false"/>
					
					<h:outputText value="Notes:" rendered="#{not empty allele.notes}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.notes}"/>
					<h:outputText value="#{allele.notes}" rendered="#{not empty allele.notes}" escape="false" />
				         </h:panelGrid>
				    </t:column>
			</t:dataTable>

			<h:outputText value="Sex:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.sex}" />
			
			<h:outputText value="Development Age:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.devAge}" />
			
			<h:outputText value="#{stageSeriesLong} Stage:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.theilerStage}" />

			<h:outputText value="Pooled Sample:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.pooledSample}" />
			
			<h:outputText value="Pool Size:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.poolSize}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.poolSize}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.poolSize}" />			
			
			<h:outputText value="Experiment Design:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.experimentalDesign}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.experimentalDesign}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.experimentalDesign}" />
		
			<h:outputText value="Sample Notes:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.sampleNotes}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.sampleNotes}"  rendered="#{not empty NGDSingleSubmissionBean.submission.sample.sampleNotes}" />
			
		
		</h:panelGrid>
       </h:panelGrid>  
		
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Protocol Details" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Library Strategy:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.libraryStrategy}" />
			
			<h:outputText value="Extracted Molecule:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.extractedMolecule}" />
			
			<h:outputText value="Isolation Method:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.rnaIsolationMethod}" />
			
			<h:outputText value="Library Construction Protocol:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.libraryConProt}" />
			
			<h:outputText value="Labelling Method:"  rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.labelMethod}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.labelMethod}" escape="false"   rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.labelMethod}" />
			
			<h:outputText value="Sequencing Method:"   rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.sequencingMethod}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.sequencingMethod}"   rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.sequencingMethod}" />
			
			<h:outputText value="Single or Paired End:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.pairedEnd}" />
			
			<h:outputText value="Instrument Model:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.instrumentModel}" />
			
			<h:outputText value="Protocol Notes:"  rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.protAdditionalNotes}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.protocol.protAdditionalNotes}"  rendered="#{not empty NGDSingleSubmissionBean.submission.protocol.protAdditionalNotes}"/>
			
			<h:outputText value="Library Pool Size:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.libraryPoolSize}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.libraryPoolSize}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.libraryPoolSize}" />

			<h:outputText value="Library Reads:"  rendered="#{not empty NGDSingleSubmissionBean.submission.sample.libraryReads}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.libraryReads}"  rendered="#{not empty NGDSingleSubmissionBean.submission.sample.libraryReads}" />
			
			<h:outputText value="Read Length:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.readLength}" />
			
			<h:outputText value="Mean Insert Size:"  rendered="#{not empty NGDSingleSubmissionBean.submission.sample.meanInsertSize}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.meanInsertSize}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.meanInsertSize}" />
		</h:panelGrid>
       </h:panelGrid>
       
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Data Processing and Transcript Profile" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Total No of Reads (RAW Only):"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.numberOfReads}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.numberOfReads}"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.numberOfReads}"/>
			
			<h:outputText value="Reads Before Cleanup (RAW Only):"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.beforeCleanUpReads}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.beforeCleanUpReads}" rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.beforeCleanUpReads}" />
			
			<h:outputText value="Single or Paired End:"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.pairedEnd}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.pairedEnd}" rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.pairedEnd}" />
			
			<h:outputText value="Processing Step:" rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.proStep}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.proStep}"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.proStep}"/>
			
			<h:outputText value="Genome Build or Alignment Reference Sequence:"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.build}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.build}" escape="false"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.build}"/>
			
			<h:outputText value="% Aligned to Genome:"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.alignedGenome}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.alignedGenome}"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.alignedGenome}"/>
			
			<h:outputText value="% UnAligned to Genome:"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.unalignedGenome}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.unalignedGenome}"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.unalignedGenome}"/>
			
			<h:outputText value="% RNA Reads:"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.rnaReads}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.rnaReads}"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.rnaReads}"/>
			
			<h:outputText value="Format and Content:" rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.formatContent}" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.dataProcessing.formatContent}" escape="false"  rendered="#{not empty NGDSingleSubmissionBean.submission.dataProcessing.formatContent}"/>
		</h:panelGrid>
       </h:panelGrid>
			
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
       		<h:outputText value="Series Details" />
            <h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol" >
        	   <%-- <h:outputText value="Series GEO ID:" rendered="#{not empty NGDSingleSubmissionBean.submission.series.geoID}"/>
        	   <h:panelGroup>
        		  <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external"   rendered="#{NGDSingleSubmissionBean.submission.series.geoID != null}">
        			 <f:param value="#{NGDSingleSubmissionBean.submission.series.geoID}" name="acc" />
        			 <h:outputText value="#{NGDSingleSubmissionBean.submission.series.geoID}"   rendered="#{NGDSingleSubmissionBean.submission.series.geoID != null}"/>
        		  </h:outputLink>
        	   </h:panelGroup> --%>
        	   
        	   <h:outputText value="Series GEO ID:" rendered="#{not empty NGDSingleSubmissionBean.submission.series.geoID}"/>
        	   	<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external"   rendered="#{NGDSingleSubmissionBean.submission.series.geoID != null}">
        			 <f:param value="#{NGDSingleSubmissionBean.submission.series.geoID}" name="acc" />
        			 <h:outputText value="#{NGDSingleSubmissionBean.submission.series.geoID}"   rendered="#{NGDSingleSubmissionBean.submission.series.geoID != null}"/>
        		</h:outputLink>
        	   
        	
        	   <h:outputText value="Number of Samples:" />
        	   <h:panelGroup>
        		  <h:outputLink styleClass="datatext" value="ngd_series.html">
					<f:param value="#{NGDSingleSubmissionBean.submission.series.oid}" name="seriesId" />
					<h:outputText value="#{NGDSingleSubmissionBean.submission.series.numSamples} samples" />
			      </h:outputLink>
		       </h:panelGroup>
			
		    <h:outputText value="Title:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.title}" />
			
			<h:outputText value="Summary:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.summary}" />
			
			<h:outputText value="Type:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.type}" />
			
			<h:outputText value="Overall Design:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.design}" />
		</h:panelGrid>
        </h:panelGrid>
        
       

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{null != NGDSingleSubmissionBean.submission.linkedSubmissions}">
			<h:outputText value="Linked Submissions" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{NGDSingleSubmissionBean.submission.linkedSubmissions}" var="link">
					<h:column>
						<h:panelGrid columns="1">
							<h:panelGroup>
								<h:outputText styleClass="plaintextbold" value="Database: " />
								<h:outputText styleClass="plaintextbold" value="#{link[0]}" />
							</h:panelGroup>
					
							<h:dataTable styleClass="browseTable" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey"
							               bgcolor="white" cellpadding="5" value="#{link[1]}" var="accessionIDAndTypes">
								<h:column>
									<f:facet name="header">
										<h:outputText value="Accession ID" styleClass="plaintextbold" />
									</f:facet>
									<h:outputLink styleClass="plaintext" id="submissionID" value="ish_submission.html">
										<f:param name="id" value="#{accessionIDAndTypes[0]}" />
									 	<h:outputText value="#{accessionIDAndTypes[0]}"/>
					 				</h:outputLink>
					 			</h:column>
								<h:column>
									<f:facet name="header">
					   					<h:outputText value="Link Type(s)" styleClass="plaintextbold" />
									</f:facet>
									<t:dataList id="linkTypes" styleClass="plaintext" rowIndexVar="index"
					                		    var="linkType" value="#{accessionIDAndTypes[1]}" layout="simple" rowCountVar="count" >
					 					<h:outputText styleClass="plaintext" value="#{linkType}" />
										<f:verbatim>&nbsp;</f:verbatim>
									</t:dataList>
								</h:column>
							</h:dataTable>
					 
							<h:panelGroup rendered="#{not empty link[2]}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="plaintext" value="http://www.gudmap.org">
									<h:outputText styleClass="datatext" value="#{link[2]}" />
								</h:outputLink>
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>
		
		 <h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{not empty NGDSingleSubmissionBean.submission.linkedPublications}">
			<h:outputText value="Linked Publications" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable  value="#{NGDSingleSubmissionBean.submission.linkedPublications}" var="pub">
					<h:column>
						<h:outputText styleClass="plaintext" value="#{pub[0]} " />
						<h:outputText styleClass="plaintext" value="(#{pub[1]}) " />
						<h:outputText styleClass="plaintextbold" value="#{pub[2]} " />
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[3]}, " rendered="#{not empty pub[3]}" />
                        <h:outputText styleClass="plaintextbold" value="#{pub[4]}" rendered="#{not empty pub[4]}"/>
                        <h:outputText styleClass="plaintext" value=", " rendered="#{not empty pub[4]}" />
                        <h:outputText styleClass="plaintext" value="#{pub[6]}" />
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{null != NGDSingleSubmissionBean.submission.acknowledgements}">
			<h:outputText value="Acknowledgements" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{NGDSingleSubmissionBean.submission.acknowledgements}" var="ack">
					<h:column>
						<h:outputText value = "#{ack}"/>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>
        
        

  </h:form>  
  <f:subview id="footer">

    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>