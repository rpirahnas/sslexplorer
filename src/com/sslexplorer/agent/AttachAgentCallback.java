/**
 * 
 */
package com.sslexplorer.agent;

import com.sslexplorer.boot.RequestHandlerRequest;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.security.User;

class AttachAgentCallback extends DefaultAgentCallback {
	
	public User authenticate(RequestHandlerRequest request) {
		SessionInfo session = DefaultAgentManager.getInstance()
				.getSessionByAgentId(
						(String) request.getParameters().get("ticket"));

		if (session == null && LogonControllerFactory.getInstance().getActiveSessions().size() > 0) {
			session = (SessionInfo) LogonControllerFactory.getInstance().getActiveSessions().values().iterator().next();
			String ticket = request.getParameters()
				.get("ticket").toString(); 
			LogonControllerFactory.getInstance().registerAuthorizationTicket(ticket, session);
		}
		return super.authenticate(request);
	}
}