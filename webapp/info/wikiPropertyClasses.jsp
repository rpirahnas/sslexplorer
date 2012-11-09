<%@ page contentType="text/plain;charset=UTF-8" language="java" %><jsp:directive.page import="com.sslexplorer.policyframework.PolicyDatabaseFactory"/><jsp:directive.page import="com.sslexplorer.policyframework.ResourceType"/><jsp:directive.page import="java.util.Iterator"/>
<jsp:directive.page import="java.util.Collections"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="com.sslexplorer.boot.PropertyClassManager"/>
<jsp:directive.page import="com.sslexplorer.boot.PropertyClass"/>
<jsp:directive.page import="java.util.Collection"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="com.sslexplorer.boot.SystemProperties"/><%
if(!"true".equals(SystemProperties.get("sslexplorer.enableInfoPages", "false"))) {
		throw new Exception("Access denied");
}
%>|  *Name*  |
<%
	List l = new ArrayList(PropertyClassManager.getInstance().getPropertyClasses());
	Collections.sort(l);
	for(Iterator i = l.iterator(); i.hasNext(); ) {
		PropertyClass propertyClass = (PropertyClass)i.next();
%>| <%= propertyClass.getName() %> |
<% 	} %>