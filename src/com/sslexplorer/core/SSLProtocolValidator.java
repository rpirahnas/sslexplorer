package com.sslexplorer.core;

import java.util.Properties;

import com.sslexplorer.boot.CodedException;
import com.sslexplorer.boot.PropertyDefinition;
import com.sslexplorer.boot.PropertyValidator;

public class SSLProtocolValidator implements PropertyValidator {
	public void validate(PropertyDefinition definition, String value,
			Properties properties) throws CodedException {
		if(!value.equals("") && value.indexOf("SSLv3")==-1)
			throw new CoreException(2,"ssl","errors");
	}
}
