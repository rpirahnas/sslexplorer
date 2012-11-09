package com.sslexplorer.extensions;

import com.sslexplorer.boot.AbstractPropertyClass;
import com.sslexplorer.boot.AbstractPropertyKey;

/**
 * A simple property class to support application parameter definitions.
 * 
 * @author brett
 */
public class ApplicationParameters extends AbstractPropertyClass {
	
	/**
	 * Property class name
	 */
	public final static String NAME = "applicationParameters";

	/**
	 * Constructor
	 */
	public ApplicationParameters() {
		super(NAME, true);
	}

	/* (non-Javadoc)
	 * @see com.sslexplorer.boot.AbstractPropertyClass#retrievePropertyImpl(com.sslexplorer.boot.AbstractPropertyKey)
	 */
	protected String retrievePropertyImpl(AbstractPropertyKey key)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet supported.");
	}

	/* (non-Javadoc)
	 * @see com.sslexplorer.boot.AbstractPropertyClass#storePropertyImpl(com.sslexplorer.boot.AbstractPropertyKey, java.lang.String)
	 */
	protected String storePropertyImpl(AbstractPropertyKey key, String value)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet supported.");
	}

}
