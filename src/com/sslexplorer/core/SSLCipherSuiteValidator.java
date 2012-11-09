package com.sslexplorer.core;

import java.util.Properties;

import com.sslexplorer.boot.CodedException;
import com.sslexplorer.boot.PropertyDefinition;
import com.sslexplorer.boot.PropertyValidator;

public class SSLCipherSuiteValidator implements PropertyValidator {

	public void validate(PropertyDefinition definition, String value,
			Properties properties) throws CodedException {
		if(!value.equals("") && value.indexOf("SSL_RSA_WITH_RC4_128_MD5")==-1)
			throw new CoreException(1,"ssl","errors");
	}

}
