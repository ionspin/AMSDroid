<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:subview id="toc">

	<h:panelGroup styleClass="toc" layout="block">
		<h:form id="tocForm">
			<h:panelGrid columns="1" styleClass="tocMenu">
				<f:facet name="header">
					<h:outputText value="Admin options" />
				</f:facet>
				<h:commandLink action="category" value="New category" />
				<h:commandLink action="target" value="New target" />
				<%--			<h:outputLink value="admin/addCategory.jsf">New category</h:outputLink> --%>
				<%--			<h:outputLink value="admin/addTarget.jsf">New target</h:outputLink>--%>
			</h:panelGrid>
			<h:panelGrid columns="1" styleClass="tocMenu">
				<f:facet name="header">
					<h:outputText value="User options" />
				</f:facet>
				<h:commandLink action="listAlerts" value="List alerts" />
				<h:commandLink action="subscribe" value="Subscribe" />
				<%--			<h:outputLink value="listAlerts.jsf">List Alerts</h:outputLink>--%>
				<%--			<h:outputLink value="subscribe.jsf">Subscribe</h:outputLink>--%>
			</h:panelGrid>
		</h:form>
	</h:panelGroup>
</f:subview>
