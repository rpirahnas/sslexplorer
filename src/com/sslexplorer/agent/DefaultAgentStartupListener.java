package com.sslexplorer.agent;

import com.maverick.multiplex.Channel;
import com.maverick.multiplex.MultiplexedConnectionListener;
import com.sslexplorer.security.SessionInfo;

public class DefaultAgentStartupListener implements
		MultiplexedConnectionListener {

	SessionInfo session;
	
	DefaultAgentStartupListener(SessionInfo session) {
		this.session = session;
		
	}
	
	public void onConnectionClose() {

	}

	public void onConnectionOpen() {
	}


}
