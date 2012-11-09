<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@page import="java.io.PrintWriter"%>
<jsp:directive.page import="com.sslexplorer.security.SessionInfo"/>
<jsp:directive.page import="com.sslexplorer.core.CoreServlet"/>
<jsp:directive.page import="java.util.Iterator"/>
<jsp:directive.page import="com.sslexplorer.security.Constants"/>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="com.sslexplorer.boot.PropertyDefinition"/>
<jsp:directive.page import="com.sslexplorer.boot.DefaultPropertyDefinition"/>
<jsp:directive.page import="com.sslexplorer.boot.PropertyClass"/>
<jsp:directive.page import="com.sslexplorer.boot.PropertyClassManager"/>
<jsp:directive.page import="com.sslexplorer.boot.SystemProperties"/>

<definitions><% 
	if(!"true".equals(SystemProperties.get("sslexplorer.enableInfoPages", "false"))) {
		throw new Exception("Access denied");
	}
	for(Iterator j = PropertyClassManager.getInstance().getPropertyClasses().iterator(); j.hasNext(); ) {
		PropertyClass propertyClass = (PropertyClass)j.next();
		%>
<propertyClass name="<%= propertyClass.getName() %>">
		<%
		for(Iterator i = propertyClass.getDefinitions().iterator(); i.hasNext(); ) {
			DefaultPropertyDefinition def = (DefaultPropertyDefinition)i.next();
	%>
    <definition type="<%= String.valueOf(def.getType()) %>"
                name="<%= def.getName() %>" 
                typeMeta="<%= def.getTypeMeta() %>" 
                category="<%= String.valueOf(def.getCategory()) %>"
                defaultValue="<%= def.getDefaultValue() %>" 
                sortOrder="<%= String.valueOf(def.getSortOrder()) %>"
                validation="<%= def.getValidationString() == null ? "" : def.getValidationString() %>"
                messageResourcesKey="<%= def.getMessageResourcesKey() %>" /><% 
        } %>
</propertyClass><% } %>
                
</definitions>