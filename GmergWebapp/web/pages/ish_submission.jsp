<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%-- moved style definition to gudmap_css.css - xingjun - 30/07/2010 --%>
<%-- 
<head>
	<style>
		.width95 {
			width:95%;
		}
		.width5 {
			width:5%;
		}
		td.width5 {
			text-align:right;
			vertical-align:bottom;
		}
		.width85 {
			width:85%;
		}
		.width15 {
			width:15%;
		}		
	</style>
</head>
--%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
  
	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id" rendered="#{!ishSubmissionBean.renderPage}"/>
	
	<h:form id="mainForm" rendered="#{ishSubmissionBean.renderPage}">
<%-- xingjun - 24/11/2009
		<h:panelGrid rendered="#{!userBean.userLoggedIn}" width="100%" columns="1" rowClasses="header-stripey,header-nostripe">
--%>
		<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe">
			<h:outputText styleClass="plaintextbold" value="#{ishSubmissionBean.submission.accID}" />
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

<%-- added by xingjun - 02/06/2008 - display status note if editor logged in - begin --%>
<%-- xingjun - 24/11/2009
		<h:inputHidden id="hiddenSubId" value="#{editSubmissionSupportBean.subAccessionId}" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5}"/>
		<h:panelGrid rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5}" width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        	<h:outputText styleClass="plaintextbold" value="#{ishSubmissionBean.submission.accID}"/>
        	<h:panelGrid columns="1">
        		<h:dataTable id="statusNoteTable" bgcolor="white" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey"
        					value="#{editSubmissionBean.statusNotes}" var="statusNotes">
        			<h:column>
        				<f:facet name="header">
        					<h:outputText id="select" value="Select" styleClass="plaintext"/>
        				</f:facet>
        				<h:selectBooleanCheckbox value="#{statusNotes.selected}"/>
        			</h:column>
        			<h:column>
        				<f:facet name="header">
        					<h:outputText styleClass= "plaintext" value="Status Note" />
        				</f:facet>
        				<h:inputText styleClass= "datatext" size="50" value="#{statusNotes.statusNote}" />
        			</h:column>
        		</h:dataTable>
        		<f:verbatim>&nbsp;</f:verbatim>
        		
        		<h:outputText rendered="#{editSubmissionBean.noStatusNoteCheckedForDeletion}" styleClass="plainred" value="Please make a selection before clicking on the Delete button"/>
        		<h:outputText rendered="#{editSubmissionBean.emptyStatusNoteExists}" styleClass="plainred" value="Status note could not be empty!"/>
        		
        		<h:panelGrid columns="5" cellspacing="5">
        			<h:commandLink id="deleteStatusNoteLink" value="Delete" action="#{editSubmissionBean.deleteStatusNote}" >
        				<f:param name="submissionId" value="#{ishSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<h:commandLink id="addStatusNoteLink" value="Add" action="#{editSubmissionBean.addStatusNote}" >
        				<f:param name="submissionId" value="#{ishSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<h:commandLink id="cancelStatusNoteLink" value="Cancel Modification" action="#{editSubmissionBean.cancelModification}">
        				<f:param name="submissionId" value="#{ishSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<f:verbatim>&nbsp;</f:verbatim>
        			<h:commandButton id="commitButton" value="Commit Change" action="#{editSubmissionBean.commitModification}"/>
        		</h:panelGrid>
        	</h:panelGrid>
        </h:panelGrid>
--%>
<%-- added by xingjun - 02/06/2008 - display status note if editor logged in - end --%>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			<h:outputText value="Gene"/>
			<h:panelGroup rendered="#{ishSubmissionBean.submission.assayType == 'ISH' || ishSubmissionBean.submission.assayType == 'ISH (sense probe)'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
					<h:outputText value="#{ishSubmissionBean.submission.probe.geneSymbol}" />
					<f:param name="gene" value="#{ishSubmissionBean.submission.probe.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.geneName}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{ishSubmissionBean.submission.assayType == 'IHC' || ishSubmissionBean.submission.assayType == 'OPT'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
					<h:outputText value="#{ishSubmissionBean.submission.antibody.geneSymbol}" />
					<f:param name="gene" value="#{ishSubmissionBean.submission.antibody.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.geneName}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{ishSubmissionBean.submission.assayType == 'TG'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
<%-- 
					<h:outputText value="#{ishSubmissionBean.submission.transgenic.promoter}" />
					<f:param name="gene" value="#{ishSubmissionBean.submission.transgenic.promoter}" />
--%>
					<h:outputText value="#{ishSubmissionBean.submission.transgenic.geneSymbol}" />
					<f:param name="gene" value="#{ishSubmissionBean.submission.transgenic.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.transgenic.geneName}" />
			</h:panelGroup>
			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>

			<h:outputText value="Theiler Stage" />
<%-- 
			<h:outputLink styleClass="plaintext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{ishSubmissionBean.submission.stage}" rendered="#{siteSpecies == 'mouse'}">
--%>
			<h:outputLink styleClass="plaintext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{ishSubmissionBean.submission.stage}definition.html" rendered="#{siteSpecies == 'mouse'}">
				<h:outputText value="#{stageSeriesShort}#{ishSubmissionBean.submission.stage}" />
			</h:outputLink>
			<h:outputLink styleClass="plaintext" value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" rendered="#{siteSpecies == 'Xenopus laevis'}">
				<h:outputText value="#{stageSeriesShort}#{ishSubmissionBean.submission.stage}" />
			</h:outputLink>
			
			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>


			<h:outputText value="Tissue" />
			<h:outputText value="#{ishSubmissionBean.submission.tissue}" /> 


			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
			<h:outputText value="Images" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable columnClasses="text-normal,text-top" value="#{ishSubmissionBean.submission.originalImages}" var="image">
					<h:column>
						<h:outputLink rendered="#{ishSubmissionBean.submission.assayType != 'OPT'}" id="thumbnail" value="#" onclick="openZoomViewer('#{ishSubmissionBean.submission.accID}', '#{image[2]}', '#{image[4]}'); return false;" >
							<h:graphicImage styleClass="icon" value="#{image[0]}" height="50"/>
						</h:outputLink>
						<h:outputLink rendered="#{ishSubmissionBean.submission.assayType == 'OPT'}" id="opt_thumbnail" value="#" onclick="window.open('#{image[0]}','#{image[2]}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,height=500,width=400'); return false;" >
							<h:graphicImage styleClass="icon" value="#{image[3]}" height="50"/>
						</h:outputLink>
					</h:column>
					<h:column>
						<h:outputText styleClass="notetext" value="#{image[1]}"/>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editImage" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
							  onclick="var w=window.open('edit_image.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
    
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe" rendered="#{proj == 'GUDMAP' && (ishSubmissionBean.submission.assayType == 'ISH' || ishSubmissionBean.submission.assayType == 'ISH (sense probe)') && ishSubmissionBean.submission.specimen.assayType == 'wholemount'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="Whole-mount in situ hybridization is subject to technical limitations that may influence accuracy of the data (" />
				<h:outputLink styleClass="plaintext" value="#" onclick="var w=window.open('wish_moreInfo.jsf','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;" >
					<h:outputText value="more info" />
				</h:outputLink>
				<h:outputText styleClass="plaintextbold" value=")." />
			</h:panelGroup>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>
  
		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" >
			<h:outputText value="Submitters" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
					<h:outputText value="Principal Investigator:" />
					<h:panelGroup>
						<h:outputText value="#{ishSubmissionBean.submission.principalInvestigator.name}, " />
						<h:outputText value="#{ishSubmissionBean.submission.principalInvestigator.lab}, " />
						<h:outputText value="#{ishSubmissionBean.submission.principalInvestigator.address}, " />
						<h:outputLink styleClass="datatext" value="mailto:#{ishSubmissionBean.submission.principalInvestigator.email}">
							<h:outputText value="#{ishSubmissionBean.submission.principalInvestigator.email}"/>
						</h:outputLink>
					</h:panelGroup>
					<h:outputText value="Authors:" />
					<h:outputText value="#{ishSubmissionBean.submission.authors}" />
					<h:outputText value="Submitted By:" />
					<h:panelGroup>
						<h:outputText value="#{ishSubmissionBean.submission.submitter.name}, " />
						<h:outputText value="#{ishSubmissionBean.submission.submitter.lab}, " />
						<h:outputText value="#{ishSubmissionBean.submission.submitter.address}, " />
						<h:outputLink styleClass="datatext" value="mailto:#{ishSubmissionBean.submission.submitter.email}">
							<h:outputText value="#{ishSubmissionBean.submission.submitter.email}"/>
						</h:outputLink>
					</h:panelGroup>
					<h:outputText rendered="#{proj == 'GUDMAP'}" value="Archive ID:" />
					<h:outputLink rendered="#{proj == 'GUDMAP'}" styleClass="datatext" value="http://www.gudmap.org/Submission_Archive/index.html##{ishSubmissionBean.submission.archiveId}" >
						<h:outputText value="#{ishSubmissionBean.submission.archiveId}" />
					</h:outputLink>
					<h:outputText rendered="#{proj == 'GUDMAP'}" value="Submission ID:" />
					<h:outputText rendered="#{proj == 'GUDMAP'}" styleClass="datatext" value="#{ishSubmissionBean.submission.labId}" />
				</h:panelGrid>
				
				<h:outputLink id="editSubmitters" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
					          onclick="var w=window.open('edit_person.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
			
			<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			<h:panelGroup>
				<h:outputText value="Expression Mapping <br/><br/>" escape="false"/>
				<h:outputText value="Expression Strengths Key:" styleClass="plaintextbold" rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType != 'list'}">
					<h:graphicImage value="/images/tree/DetectedRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (unspecified strength)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/StrongRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (strong)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/ModerateRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (moderate)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/WeakRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (weak)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/PossibleRound20x20.gif" styleClass="icon" />
					<h:outputText value="Uncertain" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Possible" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/NotDetectedRoundMinus20x20.gif" styleClass="icon" />
					<h:outputText value="Not Detected" styleClass="plaintext" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
				</h:panelGrid>
					
				<h:outputText value="Expression Patterns Key:" styleClass="plaintextbold" rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType != 'list'}">
					<h:graphicImage value="/images/tree/HomogeneousRound20x20.png" styleClass="icon" rendered="#{proj != 'GUDMAP'}" />
					<h:outputText value="Homogeneous" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/GradedRound20x20.png" styleClass="icon" />
					<h:outputText value="Graded" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/RegionalRound20x20.png" styleClass="icon" />
					<h:outputText value="Regional" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/SpottedRound20x20.png" styleClass="icon" />
					<h:outputText value="Spotted" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/UbiquitousRound20x20.png" styleClass="icon" />
					<h:outputText value="Ubiquitous" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/OtherRound20x20.png" styleClass="icon" rendered="#{proj != 'GUDMAP'}" />
					<h:outputText value="Other" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Restricted" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Single cell" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/note.gif" styleClass="icon" />
					<h:outputText value="Contains note" styleClass="plaintext" />
				</h:panelGrid>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="No Expression Mapping Data Available" styleClass="datatext" rendered="#{!ishSubmissionBean.expressionMapped}" />
				
				<h:panelGrid columns="3" columnClasses="table-stripey" bgcolor="white" cellspacing="2" cellpadding="4">
					<h:commandLink action="#{ishSubmissionBean.annotation}" styleClass="plaintext">
						<h:outputText value="#{ishSubmissionBean.annotationDisplayLinkTxt}" />
						<f:param name="annotationDisplay" value="#{ishSubmissionBean.annotationDisplayType}"/>
						<f:param name="displayOfAnnoGps" value="#{ishSubmissionBean.displayOfAnnoGps}"/>
					</h:commandLink>
					<h:commandLink action="#{ishSubmissionBean.displayOfAnnotatedGps}" rendered="#{siteSpecies!='Xenopus laevis' && ishSubmissionBean.annotationDisplayType!='list'}" styleClass="plaintext" >
						<h:outputText value="#{ishSubmissionBean.displayOfAnnotatedGpsTxt}" />
						<f:param name="annotationDisplay" value="#{ishSubmissionBean.annotationDisplayType}"/>
						<f:param name="displayOfAnnoGps" value="#{ishSubmissionBean.displayOfAnnoGps}"/>
					</h:commandLink>
					<f:verbatim>
						<a href="#Link260010Context" name="Link260010Context" id="Link260010Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID260010', 'Gene Query', 'Clicking on these buttons allows you to alter the display format of the annotations. Subsequent visits to this page will use your last selected format options.', 'Link260010Context')"> 
							<img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" />
						</a>
					</f:verbatim>
				</h:panelGrid>
				<h:inputHidden value="#{ishSubmissionBean.submissionID}" />
				<h:panelGroup rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType == 'list'}">
					<h:outputText value="<script type='text/javascript'>SUBMISSION_ID ='#{ishSubmissionBean.submission.accID}'</script>" escape="false"/>
					<h:dataTable cellpadding="2" width="100%" headerClass="align-left" rowClasses="table-nostripe, table-stripey" 
								 columnClasses="align-left" value="#{ishSubmissionBean.submission.annotatedComponents}" var="component" >
						<h:column>
							<f:facet name="header">
								<h:outputText value="ComponentID" styleClass="plaintextbold"/>
							</f:facet>
							<h:outputLink styleClass="datatext" value="javascript:showExprInfo('#{component.componentId}','#{component.componentName}','0')">
								<h:outputText value="#{component.componentId}" />
							</h:outputLink>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Component " styleClass="plaintextbold"/>
							</f:facet>
							<h:outputLink styleClass="datatext" value="javascript:showExprInfo('#{component.componentId}','#{component.componentName}','0')">
								<h:outputText value="#{component.componentName}" />
								<h:graphicImage value="../images/tree/note.gif" rendered="#{component.noteExists}" styleClass="icon" />
							</h:outputLink>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Expression" styleClass="plaintextbold"/>
							</f:facet>
							<h:panelGrid columns="2">
								<h:graphicImage value="#{component.expressionImage}" styleClass="icon" alt="" />
								<h:panelGroup>
									<h:outputText value="#{component.primaryStrength} " styleClass="datatext" />
									<h:outputText rendered="#{component.secondaryStrength != null && component.secondaryStrength != ''}" value="(#{component.secondaryStrength})" styleClass="datatext" />
								</h:panelGroup>
							</h:panelGrid>
						</h:column>
						<h:column rendered="#{proj == 'GUDMAP'}">
							<f:facet name="header">
								<h:outputText value="Patterns/Locations" styleClass="plaintextbold"/>
							</f:facet>
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{component.pattern != null}">
								<h:column>
									<h:panelGrid columns="2">
										<h:graphicImage value="#{pattern.patternImage}" styleClass="icon" alt="" />
										<h:panelGrid columns="2" columnClasses="text-top">
											<h:outputText value="#{pattern.pattern}" styleClass="datatext"/>
											<h:outputText value="#{pattern.locations}" styleClass="datatext"/>
										</h:panelGrid>
									</h:panelGrid>
								</h:column>
							</h:dataTable>
						</h:column>
						<h:column rendered="#{proj == 'EuReGene'}">
							<f:facet name="header">
								<h:outputText value="Pattern" styleClass="plaintextbold"/>
							</f:facet>
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{component.pattern != null}">
								<h:column>
									<h:panelGrid columns="2">
										<h:graphicImage value="#{pattern.patternImage}" styleClass="icon" alt="" />
										<h:outputText value="#{pattern.pattern}" styleClass="datatext"/>
									</h:panelGrid>
								</h:column>
							</h:dataTable>
						</h:column>
					</h:dataTable>
				</h:panelGroup>
				<h:panelGroup rendered="#{ishSubmissionBean.expressionMapped && ishSubmissionBean.annotationDisplayType != 'list'}">
					<h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
						<h:outputText value="Javascript Tree Menu" />
					</h:outputLink>
					<f:verbatim>
						<script type="text/javascript">
							<c:forEach items="${ishSubmissionBean.submission.annotationTree}" var="row">
								<c:out value="${row}" escapeXml="false"/>
							</c:forEach>
						</script>
						<script type="text/javascript">initializeDocument('${ishSubmissionBean.displayOfAnnoGps}')</script>
						<noscript>
							<span class="plaintext">A tree of annotated anatomical components will open here if you enable JavaScript in your browser.</span>
						</noscript>
						&nbsp;
					</f:verbatim>
					<h:panelGroup rendered="#{siteSpecies != 'Xenopus laevis'}">
						<h:outputText styleClass="plaintextbold" value="G " />
						<h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
		
			<h:outputText value="&nbsp" escape="false"/>
			<h:outputText value="&nbsp" escape="false"/>
			
			<h:outputText value="Antibody" rendered="#{ishSubmissionBean.submission.assayType == 'IHC' || ishSubmissionBean.submission.assayType == 'OPT'}" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" rendered ="#{ishSubmissionBean.submission.assayType == 'IHC' || ishSubmissionBean.submission.assayType == 'OPT'}">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
					<h:outputText rendered="#{proj != 'EuReGene' || siteSpecies != 'mouse'}" value="Name:" />
					<h:panelGroup rendered="#{proj != 'EuReGene' || siteSpecies != 'mouse'}">
<%-- commented by Xingjun - 16/05/2008 - do not need have the link - antibody submissions are all linked to maprobe
						<h:outputLink styleClass="datatext" rendered="#{ishSubmissionBean.renderPrbNameURL}" value="#{ishSubmissionBean.submission.antibody.url}" target="_blank">
							<h:outputText value="#{ishSubmissionBean.submission.antibody.name}" />
						</h:outputLink>
--%>
						<h:outputText value="#{ishSubmissionBean.submission.antibody.name}" />
						<h:outputText styleClass="datatext" rendered="#{!ishSubmissionBean.renderPrbNameURL}" value="#{ishSubmissionBean.submission.antibody.name}" />
					</h:panelGroup>
			
					<h:outputText value="Accession:" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.accessionId}" rendered="#{proj == 'GUDMAP'}" />
			
					<h:outputText value="Protein:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ishSubmissionBean.submission.antibody.geneSymbol}" />
								<f:param name="gene" value="#{ishSubmissionBean.submission.antibody.geneSymbol}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.geneName} " />
							<h:outputText styleClass="datatext" value="(#{ishSubmissionBean.submission.antibody.geneId})" />
						</h:panelGroup>
					</h:panelGrid>
			
					<h:outputText value="Antigen Sequence:" rendered="#{ishSubmissionBean.submission.antibody.seqStatus != 'Unsequenced'}" />
					<h:panelGrid  rowClasses="text-top" columns="2" border="0" rendered="#{ishSubmissionBean.submission.antibody.seqStatus != 'Unsequenced'}" >
						<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.seqStatus} " />
						<h:panelGroup rendered="#{ishSubmissionBean.renderPrbSeqInfo}" >
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.seqInfo} " />
							<h:outputLink target="_blank" styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.genbankURL}" >
								<h:outputText  value="#{ishSubmissionBean.submission.antibody.genbankID}" />
							</h:outputLink>
						</h:panelGroup>
					</h:panelGrid>
			
					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Supplier: " />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.supplier}" />
			
					<h:outputText value="Catalogue Number: " />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.catalogueNumber}" />
			
					<h:outputText value="Lot Number: " />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.lotNumber}" />
			
					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Antibody Type: " />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.type}" />
			
					<h:outputText value="Production Method: " />
					<h:panelGrid columns="1" cellspacing="5">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="#{ishSubmissionBean.submission.antibody.productionMethod}"
							 			rendered="#{ishSubmissionBean.submission.antibody.type == 'monoclonal' && ishSubmissionBean.submission.antibody.hybridomaValue != null}"/>
							<h:outputText styleClass="plaintext" value="#{ishSubmissionBean.submission.antibody.productionMethod}"
										rendered="#{ishSubmissionBean.submission.antibody.type == 'monoclonal' && ishSubmissionBean.submission.antibody.phageDisplayValue != null}"/>
							<h:panelGroup rendered="#{ishSubmissionBean.submission.antibody.type == 'polyclonal'}">
								<h:outputText styleClass="plaintext" value="Species Immunized - " />
								<h:outputText styleClass="plaintext" value="#{ishSubmissionBean.submission.antibody.productionMethod}" />
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Purification Method:&nbsp;&nbsp;" escape="false"/>
							<h:outputText styleClass="plaintext" value="#{ishSubmissionBean.submission.antibody.purificationMethod}" />
						</h:panelGroup>
					</h:panelGrid>
					
					<h:outputText value="Chain Type:" />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.chainType}" />
			
					<h:outputText value="Immunoglobulin Isotype:" />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.immunoglobulinIsotype}" />
					
					<h:outputText value="Variant Detected:" />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.detectedVariantValue}" />
					
					<h:outputText value="Species Specificity:" />
					<h:outputText value="#{ishSubmissionBean.submission.antibody.speciesSpecificity}" />
					
					<h:outputText value="Visualisation:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Final Label: " />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.finalLabel}" />
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Signal Detection Method: " />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.signalDetectionMethod}" />
						</h:panelGroup>
					</h:panelGrid>
			
					<h:outputText styleClass="plaintext,text-top" value="Notes:" rendered="#{ishSubmissionBean.submission.antibody.notes != null}" />
					<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.antibody.notes}"
								  rendered="#{ishSubmissionBean.submission.antibody.notes != null}" />
				</h:panelGrid>
				
				<h:outputLink id="editAntibody" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}" 
							  onclick="var w=window.open('edit_antibody.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>

		    <h:outputText value="Probe" rendered="#{ishSubmissionBean.submission.assayType == 'ISH' || ishSubmissionBean.submission.assayType == 'ISH (sense probe)'}" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" rendered="#{ishSubmissionBean.submission.assayType == 'ISH'  || ishSubmissionBean.submission.assayType == 'ISH (sense probe)'}">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
					<h:outputText rendered="#{proj != 'EuReGene' || siteSpecies != 'mouse'}" value="Probe ID:" />
					<h:panelGroup rendered="#{proj != 'EuReGene' || siteSpecies != 'mouse'}">
						<h:outputLink styleClass="datatext" rendered="#{ishSubmissionBean.renderPrbNameURL}" value="#{ishSubmissionBean.submission.probe.probeNameURL}" target="_blank">
							<h:outputText value="#{ishSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>
						 <%--
						<h:outputText styleClass="datatext" rendered="#{!ishSubmissionBean.renderPrbNameURL}" value="#{ishSubmissionBean.submission.probe.probeName}" />					
        				<h:outputText styleClass="datatext" rendered="#{ishSubmissionBean.renderPrbNameURL}" value=" (#{ishSubmissionBean.submission.probe.maprobeID})" />
         				--%>
        				<%-- Bernie 27/06/2010 Mantis 558 Task1  --%>
        				<%--Bernie 15/02/2012 Mantis 558 Task A2 --%>
						<h:outputLink styleClass="datatext" rendered="#{!ishSubmissionBean.renderPrbNameURL && ishSubmissionBean.submission.probe.maprobeID != ishSubmissionBean.submission.probe.probeName}" value="#{ishSubmissionBean.submission.probe.probeNameURL}">
							<h:outputText value="#{ishSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>	
						<h:outputLink styleClass="datatext" rendered="#{!ishSubmissionBean.renderPrbNameURL && ishSubmissionBean.submission.probe.maprobeID == ishSubmissionBean.submission.probe.probeName}" value="/gudmap/pages/probe.html" target="_blank">
							<f:param name="probe" value="#{ishSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ishSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" #{ishSubmissionBean.submission.probe.maprobeID}" />
						</h:outputLink>	
						<%--Bernie 15/02/2012 Mantis 558 Task A1,A3 --%>				
						<h:outputLink styleClass="datatext" rendered="#{ishSubmissionBean.submission.probe.maprobeID !='' && ishSubmissionBean.submission.probe.maprobeID != ishSubmissionBean.submission.probe.probeName}" value="/gudmap/pages/probe.html" target="_blank">
							<f:param name="probe" value="#{ishSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ishSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" (#{ishSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>
						
						<%-- Mantis 558 - TaskA1 
						<h:outputLink styleClass="datatext" rendered="#{ishSubmissionBean.submission.probe.maprobeID !=''}" value="/gudmap/pages/probe.html?probe=#{ishSubmissionBean.submission.probe.probeName}" target="_blank">
							<h:outputText value=" (#{ishSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>
						--%>
					</h:panelGroup>
					
					<h:outputText value="Name of cDNA:" rendered="#{siteSpecies == 'mouse'}"/>
					<h:outputText value="#{ishSubmissionBean.submission.probe.cloneName}" rendered="#{siteSpecies == 'mouse'}" />
					
					<h:outputText value="Additional Name of cDNA:" rendered="#{siteSpecies == 'mouse' && ishSubmissionBean.submission.probe.additionalCloneName != null && ishSubmissionBean.submission.probe.additionalCloneName != ''}"/>
					<h:outputText value="#{ishSubmissionBean.submission.probe.additionalCloneName}" rendered="#{siteSpecies == 'mouse' && ishSubmissionBean.submission.probe.additionalCloneName != null && ishSubmissionBean.submission.probe.additionalCloneName != ''}" />
					
					<h:outputText value="Sequence:" />
					<h:panelGrid  rowClasses="text-top" columns="1" border="0" >
						<h:panelGroup>
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.seqStatus} " />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.seqInfo} " rendered="#{ishSubmissionBean.renderPrbSeqInfo}" />
							<h:outputLink target="_blank" styleClass="datatext" value="#{ishSubmissionBean.submission.probe.genbankURL}" rendered="#{ishSubmissionBean.renderPrbSeqInfo}" >
								<h:outputText  value="#{ishSubmissionBean.submission.probe.genbankID}" />
							</h:outputLink>
						</h:panelGroup>
					</h:panelGrid>
					
					<h:outputText value="Gene:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ishSubmissionBean.submission.probe.geneSymbol}" />
								<f:param name="gene" value="#{ishSubmissionBean.submission.probe.geneSymbol}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.geneName} " />
							<h:outputText styleClass="datatext" value="(" rendered="#{ishSubmissionBean.submission.probe.geneIdUrl != null && ishSubmissionBean.submission.probe.geneIdUrl != ''}" />
							<h:outputLink styleClass="datatext" value="#{ishSubmissionBean.submission.probe.geneIdUrl}" >
							<h:outputText value="#{ishSubmissionBean.submission.probe.geneID}" />
							</h:outputLink>
							<h:outputText styleClass="datatext" value=")" rendered="#{ishSubmissionBean.submission.probe.geneIdUrl != null && ishSubmissionBean.submission.probe.geneIdUrl != ''}" />
						</h:panelGroup>
					</h:panelGrid>
					
					<%-- added by xingjun -- 02/05/2007 --- start --%>
					<h:outputText styleClass="plaintext" value="5' primer sequence:" rendered="#{ishSubmissionBean.submission.probe.seq5Primer != null && ishSubmissionBean.submission.probe.seq5Primer != '' && ishSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ishSubmissionBean.submission.probe.seq5Primer}" rendered="#{ishSubmissionBean.submission.probe.seq5Primer != null && ishSubmissionBean.submission.probe.seq5Primer != '' && ishSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="3' primer sequence:" rendered="#{ishSubmissionBean.submission.probe.seq3Primer != null && ishSubmissionBean.submission.probe.seq3Primer != '' && ishSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ishSubmissionBean.submission.probe.seq3Primer}" rendered="#{ishSubmissionBean.submission.probe.seq3Primer != null && ishSubmissionBean.submission.probe.seq3Primer != '' && ishSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="5' primer location:" rendered="#{ishSubmissionBean.submission.probe.seq5Loc != null && ishSubmissionBean.submission.probe.seq5Loc != '' && ishSubmissionBean.submission.probe.seq5Loc != 'n/a' && ishSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.seq5Loc}" rendered="#{ishSubmissionBean.submission.probe.seq5Loc != null && ishSubmissionBean.submission.probe.seq5Loc != '' && ishSubmissionBean.submission.probe.seq5Loc != 'n/a' && ishSubmissionBean.renderPrbSeqInfo}"/>
					
					<h:outputText styleClass="plaintext" value="3' primer location:"  rendered="#{ishSubmissionBean.submission.probe.seq3Loc != null && ishSubmissionBean.submission.probe.seq3Loc != '' && ishSubmissionBean.submission.probe.seq3Loc != 'n/a' && ishSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.seq3Loc}" rendered="#{ishSubmissionBean.submission.probe.seq3Loc != null && ishSubmissionBean.submission.probe.seq3Loc != '' && ishSubmissionBean.submission.probe.seq3Loc != 'n/a' && ishSubmissionBean.renderPrbSeqInfo}" />
					
					<%-- added by ying full sequence -- 04/05/2007 --- start --%>
					<h:outputText value="Template Sequence:" rendered="#{ishSubmissionBean.submission.probe.fullSequence != null}"/>
					<h:outputLink styleClass="datatext" value="#" onclick="var w=window.open('sequence.jsf?id=#{ishSubmissionBean.submission.accID}','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;"  rendered="#{ishSubmissionBean.submission.probe.fullSequence != null}">
						<h:outputText value="(Click to see sequence.)" />
					</h:outputLink>
					<%-- added by ying full sequence -- 04/05/2007 --- end --%>
					
					<%-- added by xingjun -- 02/05/2007 --- end --%>
					
					<h:outputText value="Origin of Clone used to make the Probe:" />
					<h:panelGrid columns="4" border="0">
						<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.source}" />
						<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
						<h:outputText styleClass="plaintext" value="Strain:" />
						<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.strain}" />
					
						<f:verbatim>&nbsp;</f:verbatim>
						<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
						<h:outputText styleClass="plaintext" value="Tissue:" />
						<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.tissue}" />
					</h:panelGrid>
					
					<h:outputText value="Probe Type:" />
					<h:outputText value="#{ishSubmissionBean.submission.probe.type}" />
					
					<h:outputText value="Type:" />
					<h:outputText value="#{ishSubmissionBean.submission.probe.geneType}" />
					
					<h:outputText value="Labelled with:" />
					<h:outputText value="#{ishSubmissionBean.submission.probe.labelProduct}" />
					
					<h:outputText value="Visualisation method:" />
					<h:outputText value="#{ishSubmissionBean.submission.probe.visMethod}" />
					
					<h:outputText value="Lab Probe ID:" rendered="#{ishSubmissionBean.submission.probe.labProbeId != null && ishSubmissionBean.submission.probe.labProbeId != ''}" />
					<h:outputText value="#{ishSubmissionBean.submission.probe.labProbeId}" rendered="#{ishSubmissionBean.submission.probe.labProbeId != null && ishSubmissionBean.submission.probe.labProbeId != ''}" />
					
					<h:outputText styleClass="plaintext,text-top" value="Probe Notes:" rendered="#{ishSubmissionBean.submission.probe.notes != null && ishSubmissionBean.submission.probe.notes != ''}" />
					<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.probe.notes}" rendered="#{ishSubmissionBean.submission.probe.notes != null && ishSubmissionBean.submission.probe.notes != ''}" />

					<%-- moved here from under the Template Sequence field --%>
					<h:outputText value="Curator Notes:" rendered="#{ishSubmissionBean.submission.probe.maprobeNotes != null}" />
					<h:dataTable value="#{ishSubmissionBean.submission.probe.maprobeNotes}" var="maprobeNote" rendered="#{ishSubmissionBean.submission.probe.maprobeNotes != null}">
						<h:column>
							<h:outputText styleClass="datatext" value="#{maprobeNote}" />
						</h:column>
					</h:dataTable>
				</h:panelGrid>
				<h:outputLink id="editProbe" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_probe.html?accessionId=#{ishSubmissionBean.submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
       	</h:panelGrid>
       	
        <%-- added by xingjun - 27/08/2008 - end --%>
		<h:panelGrid width="100%" columns="2" rendered="#{ishSubmissionBean.submission.assayType == 'TG'}" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" >
			<h:outputText value="#{ishSubmissionBean.transgenicTitle}"/>
			<t:dataTable id="transgenicContentTable" width="100%" rowClasses="header-stripey" columnClasses="leftCol,rightCol" headerClass="align-top-stripey"
        	     value="#{ishSubmissionBean.submission.transgenics}" var="transgenics" rowIndexVar="row">
				<t:column>
					<f:verbatim rendered="#{row>0}"> 
						<hr width="100%" align="center"/>
					</f:verbatim>
					<h:outputText value="Allele #{transgenics.serialNo}" styleClass="plaintextbold" rendered="#{ishSubmissionBean.submission.multipleTransgenics}"/>
					<h:panelGrid width="100%" columns="2" columnClasses="width95, width5">
						<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
							<h:outputText value="Gene Reported:" />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{transgenics.geneSymbol}" />
								<f:param name="gene" value="#{transgenics.geneSymbol}" />
							</h:outputLink>
	
							<h:outputText value="Mutated Gene Id:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.geneId}" rendered="#{transgenics.mutantType=='mutant allele'}" />
<%-- 
							<h:outputText value="Allele Id:" />
--%>
							<h:outputText value="Reference for Allele Description:" />
							<h:outputText value="#{transgenics.mutatedAlleleId}" />
	
							<h:outputText value="Allele Name:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="Allele Description:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.mutatedAlleleName}" />
	
							<h:outputText value="Reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="#{transgenics.labelProduct}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
			
							<h:outputText value="Direct method for visualising reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="#{transgenics.visMethod}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
			
							<h:outputText value="Allele First Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.alleleFirstChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />
			
							<h:outputText value="Allele Second Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.alleleSecondChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />
			
							<h:outputText styleClass="plaintext,text-top" value="Notes:" rendered="#{transgenics.notes != null}" />
							<h:outputText styleClass="datatext" value="#{transgenics.notes}" />
						</h:panelGrid>
						<h:outputLink id="editTransgenic" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
									onclick="var w=window.open('edit_probe.html?accessionId=#{ishSubmissionBean.submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
							<h:outputText value="[Edit]" />
						</h:outputLink>
					</h:panelGrid>
				</t:column>
			</t:dataTable>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			<h:outputText value="Specimen" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
					<h:outputLink value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" styleClass="plaintext" rendered="#{siteSpecies == 'Xenopus laevis'}"> 
						<h:outputText value="Theiler Stage:" />
					</h:outputLink>
<%-- 
					<h:outputLink value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/MAstaging.html" styleClass="plaintext" rendered="#{siteSpecies == 'mouse'}"> 
--%>
					<h:outputLink value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/stagecriteria.html" styleClass="plaintext" rendered="#{siteSpecies == 'mouse'}"> 
						<h:outputText value="Theiler Stage:" />
					</h:outputLink>
					
					<h:outputLink styleClass="datatext" value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" rendered="#{siteSpecies == 'Xenopus laevis'}">
						<h:outputText value="#{stageSeriesShort}#{ishSubmissionBean.submission.stage}" />
					</h:outputLink>
<%-- 
					<h:outputLink styleClass="datatext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{ishSubmissionBean.submission.stage}" rendered="#{siteSpecies == 'mouse'}">
--%>
					<h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{ishSubmissionBean.submission.stage}definition.html" rendered="#{siteSpecies == 'mouse'}">
						<h:outputText value="#{stageSeriesShort}#{ishSubmissionBean.submission.stage}" />
					</h:outputLink>

					<h:outputText value="Other Staging System:" />
					<h:panelGroup rendered="#{ishSubmissionBean.submission.specimen.stageFormat != 'dpc' && ishSubmissionBean.submission.specimen.stageFormat != 'P'}">
						<h:outputText value="#{ishSubmissionBean.submission.specimen.otherStageValue}" />
						<h:outputText value="#{ishSubmissionBean.submission.specimen.stageFormat} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{ishSubmissionBean.submission.specimen.stageFormat == 'dpc'}">
						<h:outputText value="#{ishSubmissionBean.submission.specimen.otherStageValue}" />
						<h:outputText value="#{ishSubmissionBean.submission.specimen.stageFormat} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{ishSubmissionBean.submission.specimen.stageFormat == 'P'}">
						<h:outputText value="#{ishSubmissionBean.submission.specimen.stageFormat} " />
						<h:outputText value="#{ishSubmissionBean.submission.specimen.otherStageValue}" />
					</h:panelGroup>
								
					<h:outputText value="Tissue" />
					<h:outputText value="#{ishSubmissionBean.submission.tissue}" />
					
					<h:outputText value="Strain:" />
					<h:outputText value="#{ishSubmissionBean.submission.specimen.strain}" />
					
					<h:outputText value="Sex:" />
					<h:outputText value="#{ishSubmissionBean.submission.specimen.sex}" />
					
					<h:outputText value="Genotype:" />
					<h:outputText value="#{ishSubmissionBean.submission.specimen.genotype}" />
					
					<h:outputText value="Specimen Preparation:" />
					<h:panelGrid columns="2" columnClasses="text-top, text-bottom">
						<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.specimen.assayType}" />
						<h:panelGrid columns="3">
							<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
							<h:outputText styleClass="plaintext" value="Fixation Method:" />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.specimen.fixMethod}" />
					
							<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
							<h:outputText styleClass="plaintext" value="Embedding:" />
							<h:outputText styleClass="datatext" value="#{ishSubmissionBean.submission.specimen.embedding}" />
						</h:panelGrid>
					</h:panelGrid>
					
					<h:outputText value="Experiment Notes:" />
					<h:outputText value="#{ishSubmissionBean.submission.specimen.notes}" />
				</h:panelGrid>  
				<h:outputLink id="editSpecimen" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_specimen.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>  
				
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
					
			<h:outputText value="Linked Publications" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable  value="#{ishSubmissionBean.submission.linkedPublications}" var="pub">
					<h:column>
						<h:outputText styleClass="plaintext" value="#{pub[0]} " />
						<h:outputText styleClass="plaintext" value="(#{pub[1]}) " />
						<h:outputText styleClass="plaintextbold" value="#{pub[2]} " />
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[3]}, " rendered="#{pub[3]!=null && pub[3]!=''}" />
                        <h:outputText styleClass="plaintextbold" value="#{pub[4]}" rendered="#{pub[4]!=null && pub[4]!=''}"/>
                        <h:outputText styleClass="plaintext" value=", " rendered="#{pub[4]!=null && pub[4]!=''}" />
                        <h:outputText styleClass="plaintext" value="#{pub[6]}" />
					</h:column>
				</h:dataTable>
				<h:outputLink id="editPublication" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_linked_publication.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
					
			<h:outputText value="Linked Submissions" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{ishSubmissionBean.submission.linkedSubmissions}" var="link">
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
					 
							<h:panelGroup rendered="#{link[2] != null && link[2] != ''}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="plaintext" value="http://www.gudmap.org">
									<h:outputText styleClass="datatext" value="#{link[2]}" />
								</h:outputLink>
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editLinkedSubmission" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_linked_submission.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
    
			<h:outputText value="Acknowledgements" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{ishSubmissionBean.submission.acknowledgements}" var="ack">
					<h:column>
						<h:panelGrid columns="1">
							<h:panelGroup>
								<h:outputText styleClass="plaintext" value="Project: " />
								<h:outputText styleClass="datatext" value="#{ack[0]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[1] != ''}">
								<h:outputText styleClass="plaintext" value="Name(s): " />
								<h:outputText styleClass="datatext" value="#{ack[1]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[2] != ''}">  
								<h:outputText styleClass="plaintext" value="Address: " />
								<h:outputText styleClass="datatext" value="#{ack[2]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[3] != ''}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="datatext" value="#{ack[3]}">
									<h:outputText value="#{ack[3]}" />
								</h:outputLink>
							</h:panelGroup>
							<h:panelGroup>  
								<h:outputText styleClass="plaintext" value="Reason: " />
								<h:outputText styleClass="datatext" value="#{ack[4]}" escape="true" />
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editAcknowledgement" rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=5 && userBean.user.userType!='EXAMINER'}"
					  onclick="var w=window.open('edit_acknowledgement.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
					
	<jsp:include page="/includes/footer.jsp" />
</f:view>