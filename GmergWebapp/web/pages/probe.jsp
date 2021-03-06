<!-- Author: Mehran Sharghi																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

    <table border="0" width="100%" cellpadding="0" cellspacing="0">
      <tr>
        <td><h3>MaProbe Details</h3></td>
        <td align="right"></td>
      </tr>
    </table>
 
 
      <h:outputText value="There are no probes in the database with the supplied probe id." styleClass="plaintext" rendered="#{MaProbeBean.maProbe == null}" />
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe != null}">
        <h:outputText value="Probe ID" />
        <h:panelGroup>
		<h:outputText value="No MGI ID (#{MaProbeBean.maProbe.probeName})" rendered="#{MaProbeBean.maProbe.probeName == MaProbeBean.maProbe.maprobeID}"/>
	    <h:outputLink styleClass="plaintextbold" value="#{MaProbeBean.maProbe.probeNameURL}" rendered="#{MaProbeBean.maProbe.probeNameURL != null && MaProbeBean.maProbe.probeNameURL != ''}">
        	<h:outputText value="#{MaProbeBean.maProbe.probeName}" rendered="#{MaProbeBean.maProbe.probeName != MaProbeBean.maProbe.maprobeID}"/>
        </h:outputLink> 
        <h:outputText styleClass="plaintextbold" value="#{MaProbeBean.maProbe.probeName}" rendered="#{MaProbeBean.maProbe.probeNameURL == null || MaProbeBean.maProbe.probeNameURL == ''}" />
        <h:outputText styleClass="plaintext" value=" (#{MaProbeBean.maProbe.maprobeID})" rendered="#{MaProbeBean.maProbe.probeName != MaProbeBean.maProbe.maprobeID}" />
        </h:panelGroup>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Name of cDNA" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.cloneName}" />
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

        <h:outputText value="Additional Name of cDNA" rendered="#{MaProbeBean.maProbe.additionalCloneName != null && MaProbeBean.maProbe.additionalCloneName != ''}" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.additionalCloneName}" rendered="#{MaProbeBean.maProbe.additionalCloneName != null && MaProbeBean.maProbe.additionalCloneName != ''}" />
        <f:verbatim rendered="#{MaProbeBean.maProbe.additionalCloneName != null && MaProbeBean.maProbe.additionalCloneName != ''}" >&nbsp;</f:verbatim>
        <f:verbatim rendered="#{MaProbeBean.maProbe.additionalCloneName != null && MaProbeBean.maProbe.additionalCloneName != ''}" >&nbsp;</f:verbatim>

		<h:outputText value="Sequence ID:" />
		<h:panelGrid  rowClasses="text-top" columns="1" border="0" >
			<h:panelGroup>
				<h:outputLink target="_blank" styleClass="datatext" value="#{MaProbeBean.maProbe.genbankURL}" rendered="#{MaProbeBean.maProbe.genbankID != null && MaProbeBean.maProbe.genbankID != ''}" >
					<h:outputText  value="#{MaProbeBean.maProbe.genbankID}" />
				</h:outputLink>
				<h:outputText styleClass="datatext" value="Unknown" rendered="#{MaProbeBean.maProbe.genbankID == null || MaProbeBean.maProbe.genbankID == ''}" />
			</h:panelGroup>
		</h:panelGrid>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
       
        <h:outputText value="Gene" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Symbol:" />
          <h:outputLink value="gene.html" styleClass="datatext">
            <f:param name="geneId" value="#{MaProbeBean.maProbe.geneID}" />
            <h:outputText value="#{MaProbeBean.maProbe.geneSymbol}" />
          </h:outputLink>
          <h:outputText value="Name:" />
          <h:panelGroup>
            <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.geneName}" />
            <h:outputLink styleClass="datatext" target="_blank" value="http://www.informatics.jax.org/accession/#{MaProbeBean.maProbe.geneID}" rendered="#{MaProbeBean.maProbe.geneID != null && MaProbeBean.maProbe.geneID != ''}">
              <h:outputText value=" (#{MaProbeBean.maProbe.geneID})" />
            </h:outputLink>
          </h:panelGroup>
        </h:panelGrid>
        
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>
        
	  </h:panelGrid>
	  
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe.seq5Primer != '' && MaProbeBean.maProbe.seq5Primer != null }">        
        <h:outputText value="5' primer sequence" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.seq5Primer}" /> 
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
	  </h:panelGrid>
	  
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe.seq3Primer != '' && MaProbeBean.maProbe.seq5Primer != null }">
        <h:outputText value="3' primer sequence" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.seq3Primer}" />                
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>        
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe.seq5Loc != '' && MaProbeBean.maProbe.seq5Loc != null }">
        <h:outputText value="5' primer location" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.seq5Loc}" />
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>        
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe.seq3Loc != '' && MaProbeBean.maProbe.seq3Loc != null }">
		<h:outputText value="3' primer location" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.seq3Loc}" />        
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>        
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe != null}">

        <h:outputText value="Origin of Clone used to make the Probe" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Strain:" />
          <h:outputText value="#{MaProbeBean.maProbe.strain}" />
          
          <h:outputText value="Tissue:" />
          <h:outputText value="#{MaProbeBean.maProbe.tissue}" />
        </h:panelGrid>
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
      </h:panelGrid>  
      
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe != null}">
         <h:outputText value="Probe Type" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.type}" />
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
      </h:panelGrid>     
            

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe.maprobeNotes != null && MaProbeBean.maProbe.maprobeNotes != ''}">
		<h:outputText value="Curator Notes"/>
		<h:dataTable value="#{MaProbeBean.maProbe.maprobeNotes}" var="maprobeNote">
			<h:column>
				<h:outputText styleClass="datatext" value="#{maprobeNote}"/>
			</h:column>
		</h:dataTable>
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
	  </h:panelGrid> 
	  
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{MaProbeBean.maProbe != null}">
		<h:outputText value="ISH Data" />
		<h:dataTable cellspacing="5" value="#{MaProbeBean.maProbe.ishFilteredSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
			</h:column>
			<h:column>
				<h:outputText value="#{sub[2]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[5]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[8]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[3]}" styleClass="datatext" />
			</h:column>
		</h:dataTable>
        </h:panelGrid>
<%-- 
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Type" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.geneType}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Labelled With" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.labelProduct}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Visualisation Method" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.visMethod}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Probe Source" />
        <h:outputText styleClass="datatext" value="#{MaProbeBean.maProbe.source}" />
--%>                
     
	
	<jsp:include page="/includes/footer.jsp" />
    
</f:view>