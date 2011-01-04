<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:subview id="header">
	<h:panelGroup styleClass="header" layout="block">
		<h:outputText styleClass="headerUserInfo"
			value="Logged in as: '#{userInfoBean.loginName}' (method=#{userInfoBean.authType})" />
	</h:panelGroup>
</f:subview>


