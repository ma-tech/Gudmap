<!-- Microarray analysis & Visualisation jsp interface				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<head>
	<script type="text/javascript">
 		function getSamples()
	  {
			setTimeout("document.getElementById('analysisWaitingForm:progress').src = document.getElementById('analysisWaitingForm:progress').src", 200); 
	    setTimeout("document.getElementById('analysisWaitingForm:getSamples').click()", 100);
    	return false;
		}	
		
	</script>
</head>

<body onload="getSamples()">
<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form id="analysisWaitingForm" >
		<h:panelGrid width="100%" columns="1" columnClasses="table-nostripe" >
			<h:outputText value="&nbsp" escape="false"/>
			<h:outputText value="Loading microarray samples data. This may take over a minute to complete, please wait ..." />
			<h:outputText value="&nbsp" escape="false"/>
   			<h:graphicImage id="progress" value="../images/spinnerBig.gif"  styleClass="icon" />
		</h:panelGrid>			
 		<h:commandButton id="getSamples" action="#{ArrayAnalysisBean.getSamples}" value="" immediate="true" style="visibility:hidden" />
	</h:form>

	<jsp:include page="/includes/footer.jsp" />	
</f:view>
</body>
