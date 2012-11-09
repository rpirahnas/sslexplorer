/**
 * 
 */
package com.sslexplorer.agent;

import javax.swing.JOptionPane;

import com.sslexplorer.boot.RequestHandlerRequest;

class InteractiveAgentCallback extends DummyAgentCallback {
	
	protected boolean ok(RequestHandlerRequest request) {
		return JOptionPane.showConfirmDialog(null,
						"A Agent wants to connect using from "
								+ request.getRemoteAddr()
								+ ". Do you wish to allow this?",
						"Authorize Agent Connection",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}

}