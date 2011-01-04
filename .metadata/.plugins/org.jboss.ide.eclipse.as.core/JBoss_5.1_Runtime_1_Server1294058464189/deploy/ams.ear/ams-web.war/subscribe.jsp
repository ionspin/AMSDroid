<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="styles.css" rel="stylesheet" type="text/css" />

<title>AMS - Subscribe</title>
</head>

<body>

<f:view>
	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:form id="subForm">
					<h:messages showDetail="false" layout="table"
						styleClass="errorMessage" />

					<h:panelGrid columns="2">
						<f:facet name="header">
							<h:outputText value="Subscribe" />
						</f:facet>

						<h:outputText value="Target:" />
						<h:selectOneMenu id="target" value="#{subsBean.target}"
							required="true">
							<f:selectItems value="#{subsBean.allTargets}" />
						</h:selectOneMenu>

						<h:outputText value="Category:" />
						<h:selectOneMenu id="category" value="#{subsBean.category}"
							required="true">
							<f:selectItems value="#{subsBean.allCategories}" />
						</h:selectOneMenu>

						<h:outputText value="Message type:" />
						<h:selectOneMenu id="type" value="#{subsBean.type}"
							required="true">
							<f:selectItems value="#{subsBean.messageTypes}" />
						</h:selectOneMenu>

						<h:outputText value="Contact info:" />
						<h:inputText value="#{subsBean.contactDetail}" required="true"
							id="contactDetail" />

						<h:outputText value="Dispatch timeout (sec):" />
						<h:inputText value="#{subsBean.dispatchTimeout}"
							id="dispatchTimeout" required="true">
							<f:validateLongRange minimum="0" />
						</h:inputText>

						<h:outputText value="Count threshold:" />
						<h:inputText value="#{subsBean.countThreshold}"
							id="countThreshold" required="true">
							<f:validateLongRange minimum="1" />
						</h:inputText>

						<h:outputText
							value="Ignore alerts if last alert was not an error:" />
						<h:selectBooleanCheckbox value="#{subsBean.ignoreIfLastOK}"
							id="ignoreIfLastOK" required="true" />
					</h:panelGrid>

					<h:commandButton action="#{subsBean.subscribe}" value="Subscribe!" />

				</h:form>


				<h:form id="listSubsForm">

					<h:dataTable id="subs" value="#{subsBean.userSubs}"
						summary="UserSubs" var="data" styleClass="subs"
						rowClasses="odd, even">

						<f:facet name="header">
							<h:outputText value="Existing subscriptions" />
						</f:facet>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Target" />
							</f:facet>
							<h:outputText value="#{data.target.name}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Category" />
							</f:facet>
							<h:outputText value="#{data.category.name}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Contact detail" />
							</f:facet>
							<h:outputText value="#{data.type} - #{data.contactDetail}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Rules" />
							</f:facet>
							<h:outputText
								value="TO: #{data.dispatchTimeout}, CT: #{data.countThreshold}, IL: #{data.ignoreIfLastOK}" />
						</h:column>



						<h:column>
							<h:commandLink action="#{subsBean.remove}" value="remove"
								onclick="if(confirm(\"Are you sure you wish to delete this item?\"))return true; else return false;" />
						</h:column>

					</h:dataTable>

				</h:form>

				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
		<jsp:include page="footer.jsp" />
	</h:panelGroup>


</f:view>

</body>
</html>
