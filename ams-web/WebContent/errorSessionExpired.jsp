<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="cache-control" content="no-cache" />
<link href="styles.css" rel="stylesheet" type="text/css" />

<title>AMS - Error - Session Expired</title>
</head>

<body>

<f:view>

	<h:panelGroup styleClass="main" layout="block">
		<jsp:include page="header.jsp" />

		<h:panelGroup styleClass="body" layout="block">

			<jsp:include page="toc.jsp" />

			<h:panelGroup styleClass="page" layout="block">
				<%-- Header ends here --%>

				<h:outputText value="Your session has expired, please login again." />


				<%-- Footer starts here --%>
			</h:panelGroup>
		</h:panelGroup>
		<jsp:include page="footer.jsp" />
	</h:panelGroup>

</f:view>

</body>
</html>