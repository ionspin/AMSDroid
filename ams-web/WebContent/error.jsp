<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="styles.css" rel="stylesheet" type="text/css" />

<title>AMS - Error page</title>
</head>

<body>

<f:view>
	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:form>
					<h:panelGrid columns="1">
						<h:outputText escape="false" value="#{errorInfoBean.infoMessage}" />
						<h:inputTextarea rows="10" readonly="true" cols="60"
							value="#{errorInfoBean.stackTrace}" />
					</h:panelGrid>
				</h:form>

				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
	</h:panelGroup>
</f:view>

</body>
</html>
