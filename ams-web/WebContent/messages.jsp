<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="styles.css" rel="stylesheet" type="text/css" />

<title>AMS - Message Details</title>
</head>

<body>

<f:view>

	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:messages showDetail="false" layout="table"
					styleClass="errorMessage" />

				<h:dataTable id="messages" value="#{listMessagesBean.messages}"
					styleClass="messages" summary="Messages" var="data">

					<f:facet name="header">
						<h:outputText value="#{listMessagesBean.info}" />
					</f:facet>

					<h:column>
						<h:panelGrid columns="2">
							<f:facet name="header">
								<h:outputText value="Message ID: #{data.id}" />
							</f:facet>

							<h:outputText value="Sender:" />
							<h:outputText value="#{listMessagesBean.alert.sender.name}" />
							<h:outputText value="Target:" />
							<h:outputText value="#{listMessagesBean.alert.target.name}" />
							<h:outputText value="Category:" />
							<h:outputText value="#{listMessagesBean.alert.category.name}" />
							<h:outputText value="Arrived:" />
							<h:outputText value="#{listMessagesBean.alert.arrived}" />
							<h:outputText value="Subscriber:" />
							<h:outputText value="#{data.sub}" />
							<h:outputText value="Status:" />
							<h:outputText value="#{data.status}" />
							<h:outputText value="Retry count:" />
							<h:outputText value="#{data.retryCount}" />
							<h:outputText value="Subject:" />
							<h:outputText value="#{listMessagesBean.alert.subject}" />
							<h:outputText value="Body:" />
							<h:inputTextarea value="#{data.body}" readonly="true" cols="60"
								rows="10" />
						</h:panelGrid>
					</h:column>

				</h:dataTable>

				<%-- In case there are no messsages, show just the alert --%>

				<h:panelGrid columns="2" rendered="#{listMessagesBean.rowCount==0}">
					<f:facet name="header">
						<h:outputText value="Alert ID: #{listMessagesBean.alert.id}" />
					</f:facet>

					<h:outputText value="Sender:" />
					<h:outputText value="#{listMessagesBean.alert.sender.name}" />
					<h:outputText value="Target:" />
					<h:outputText value="#{listMessagesBean.alert.target.name}" />
					<h:outputText value="Category:" />
					<h:outputText value="#{listMessagesBean.alert.category.name}" />
					<h:outputText value="Arrived:" />
					<h:outputText value="#{listMessagesBean.alert.arrived}" />
					<h:outputText value="Dispatched:" />
					<h:outputText value="#{listMessagesBean.alert.dispatched}" />
					<h:outputText value="Is Error:" />
					<h:outputText value="#{listMessagesBean.alert.error}" />
					<h:outputText value="Subject:" />
					<h:outputText value="#{listMessagesBean.alert.subject}" />
					<h:outputText value="Body:" />
					<h:inputTextarea value="#{listMessagesBean.alert.body}"
						readonly="true" cols="60" rows="10" />
				</h:panelGrid>
				<%-- ~alert --%>

				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
		<jsp:include page="footer.jsp" />
	</h:panelGroup>

</f:view>

</body>
</html>
