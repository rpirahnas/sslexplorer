
<jsp:directive.page import="com.sslexplorer.security.Constants"/><%@ taglib uri="/server/taglibs/core" prefix="core" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<tiles:useAttribute ignore="true" name="resourcePrefix" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="resourceBundle" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="infoImage" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="info" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="messageArea" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="header" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="footer" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute name="content" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="actionLink" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="noBodyStyle" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="pageStyle" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute name="pageHeader" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="displayGlobalWarnings" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="profileSelector" scope="request" classname="java.lang.String"/> 
<tiles:useAttribute ignore="true" name="menu" scope="request" classname="java.lang.String"/>  
<tiles:useAttribute name="rssFeed" scope="request" classname="java.lang.String"/>
<bean:define id="popupLayout">/WEB-INF/<core:themePath/>/popupLayout.jsp</bean:define>
<%
// We place an attribute in the request so all tiles know we are in a popup layout
request.setAttribute(Constants.REQ_ATTR_POPUP, Boolean.TRUE);
 %>
<tiles:insert flush="false" beanName="popupLayout">
	<tiles:put name="pageHeader" beanName="pageHeader"/>
	<tiles:put name="resourcePrefix" beanName="resourcePrefix"/>
	<tiles:put name="resourceBundle" beanName="resourceBundle"/>
	<tiles:put name="infoImage" beanName="infoImage"/>
	<tiles:put name="info" beanName="info"/>
	<tiles:put name="messageArea" beanName="messageArea"/>
	<tiles:put name="header" beanName="header"/>
	<tiles:put name="footer" beanName="footer"/>
	<tiles:put name="content" beanName="content"/>
	<tiles:put name="actionLink" beanName="actionLink"/>
	<tiles:put name="noBodyStyle" beanName="noBodyStyle"/>
	<tiles:put name="pageStyle" beanName="pageStyle"/>
	<tiles:put name="displayGlobalWarnings" beanName="displayGlobalWarnings"/>	
	<tiles:put name="profileSelector" beanName="profileSelector"/>			
	<tiles:put name="menu" beanName="menu"/>			
	<tiles:put name="actionLink" beanName="actionLink"/>			
	<tiles:put name="menuItem" beanName="menuItem"/>		
	<tiles:put name="rssFeed" beanName="rssFeed"/>
</tiles:insert>