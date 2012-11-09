package com.sslexplorer.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.net.ssl.SSLServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.input.MultiSelectDataSource;
import com.sslexplorer.security.SessionInfo;

public class SSLCipherSuitesDataSource implements MultiSelectDataSource {

	final static Log log = LogFactory.getLog(SSLCipherSuitesDataSource.class);
	private static ArrayList list;
	public Collection<LabelValueBean> getValues(SessionInfo sessionInfo) {
		
		try {
		
			if(list!=null)
				return list;
			
			File f = new File(ContextHolder.getContext().getTempDirectory(), "availableCipherSuites.txt");
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				
				list = new ArrayList();
				String cipher;
				while((cipher = reader.readLine())!=null) {
					list.add(new LabelValueBean(cipher, cipher));
				}
				return list;
			} finally {
				if(reader!=null)
					reader.close();
			}
		} catch(Exception ex) {
			log.error(ex);
			return null;
		}
	}

}
