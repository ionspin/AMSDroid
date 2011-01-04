<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="styles.css" rel="stylesheet" type="text/css" />

<title>AMS - List Alerts</title>
</head>

<body>

<f:view>
	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:form id="listForm">

					<%-- The alert filters --%>

					<h:selectOneMenu id="filterSender"
						value="#{listAlertsBean.filterSender}" required="true">
						<f:selectItems value="#{listAlertsBean.allSenders}" />
					</h:selectOneMenu>

					<h:selectOneMenu id="filterTarget"
						value="#{listAlertsBean.filterTarget}" required="true">
						<f:selectItems value="#{listAlertsBean.allTargets}" />
					</h:selectOneMenu>

					<h:selectOneMenu id="filterCategory"
						value="#{listAlertsBean.filterCategory}" required="true">
						<f:selectItems value="#{listAlertsBean.allCategories}" />
					</h:selectOneMenu>

					<h:selectOneMenu id="filterDate"
						value="#{listAlertsBean.filterDate}" required="true">
						<f:selectItems value="#{listAlertsBean.allDates}" />
					</h:selectOneMenu>

					<h:commandButton value="Apply filters"
						action="#{listAlertsBean.applyFilters}" />


					<%-- Table with alert data --%>

					<h:messages showDetail="false" layout="table"
						styleClass="errorMessage" />

					<h:dataTable id="alerts" value="#{listAlertsBean.alerts}"
						summary="All alerts" var="data" styleClass="alerts"
						rowClasses="odd, even">

						<f:facet name="header">
							<h:outputText value="Listing of all alerts" />
						</f:facet>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Sender" />
							</f:facet>
							<h:outputText value="#{data.sender.name}" />
						</h:column>

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
								<h:outputText value="Arrived" />
							</f:facet>
							<h:outputText value="#{data.arrived}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Subject" />
							</f:facet>
							<h:outputText value="#{data.subject}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Dispatched" />
							</f:facet>
							<h:outputText value="#{data.dispatched}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Is Error?" />
							</f:facet>
							<h:outputText value="#{data.error}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Details" />
							</f:facet>
							<h:commandLink value="#{data.id}"
								action="#{listAlertsBean.details}" />
							<%-- <h:outputLink value="messages.jsf">D</h:outputLink> --%>
						</h:column>

					</h:dataTable>


					<%-- The paging buttons --%>
					<h:commandButton value="first" action="#{listAlertsBean.pageFirst}"
						disabled="#{listAlertsBean.firstRow == 0}" />
					<h:commandButton value="prev"
						action="#{listAlertsBean.pagePrevious}"
						disabled="#{listAlertsBean.firstRow == 0}" />
					<h:commandButton value="next" action="#{listAlertsBean.pageNext}"
						disabled="#{listAlertsBean.firstRow + listAlertsBean.rowsPerPage >= listAlertsBean.totalRows}" />
					<h:commandButton value="last" action="#{listAlertsBean.pageLast}"
						disabled="#{listAlertsBean.firstRow + listAlertsBean.rowsPerPage >= listAlertsBean.totalRows}" />
					<h:outputText
						value="Page #{listAlertsBean.currentPage} / #{listAlertsBean.totalPages}" />
					<br />

					<%-- Set rows per page --%>
					<h:outputLabel for="rowsPerPage" value="Rows per page" />
					<h:inputText id="rowsPerPage" value="#{listAlertsBean.rowsPerPage}"
						size="3" maxlength="3">
						<f:validateLongRange minimum="1" maximum="200" />

					</h:inputText>
					<h:commandButton value="Set" action="#{listAlertsBean.pageFirst}" />
					<%--<h:message for="rowsPerPage" styleClass="errorMessage" />--%>

				</h:form>

				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
		<jsp:include page="footer.jsp" />
	</h:panelGroup>
</f:view>

</body>
</html>
