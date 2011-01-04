<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="../styles.css" rel="stylesheet" type="text/css" />

<title>AMS Admin - Manage Targets</title>
</head>

<body>

<f:view>
	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="../header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="../toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:form id="form">
					<h:messages showDetail="false" layout="table"
						styleClass="errorMessage" />

					<h:panelGrid columns="2">
						<f:facet name="header">
							<h:outputText value="Add target" />
						</f:facet>

						<h:outputText value="Name:" />
						<h:inputText value="#{targetBean.name}" required="true" id="name">
							<f:validateLength maximum="30" minimum="3" />
						</h:inputText>

						<h:outputText value="Info:" />
						<h:inputText value="#{targetBean.info}" id="info">
							<f:validateLength maximum="50" minimum="0" />
						</h:inputText>
					</h:panelGrid>

					<h:commandButton action="#{targetBean.add}" value="Add/Modify" />

				</h:form>

				<h:form id="listTargetsForm">

					<h:dataTable id="targets" value="#{targetBean.allTargets}"
						summary="Targets" var="data" styleClass="target"
						rowClasses="even, odd">

						<f:facet name="header">
							<h:outputText value="Targets" />
						</f:facet>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Name" />
							</f:facet>
							<h:outputText value="#{data.name}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Info" />
							</f:facet>
							<h:outputText value="#{data.info}" />
						</h:column>

						<h:column>
							<h:commandLink action="#{targetBean.edit}" value="edit" />
						</h:column>

						<h:column>
							<h:commandLink action="#{targetBean.delete}" value="delete"
								onclick="if(confirm(\"Are you sure you wish to delete this item?\"))return true; else return false;" />
						</h:column>

					</h:dataTable>

				</h:form>

				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
		<jsp:include page="../footer.jsp" />
	</h:panelGroup>

</f:view>

</body>
</html>
