package it.pagopa.chat.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagoPAChatResourceBundle {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PagoPAChatResourceBundle.class);
	
	public static final String MESSAGE_BUNDLE = "it.pagopa.chat.util.messageBundle";

	private ResourceBundle messageBundle;
	
	private static PagoPAChatResourceBundle instance;
	
	public PagoPAChatResourceBundle() {
		super();
		LOGGER.debug("Reading messageBundle: " + MESSAGE_BUNDLE);
		
		this.messageBundle = ResourceBundle.getBundle(MESSAGE_BUNDLE);
		LOGGER.debug("MessageBundle created");
		
	}
	
	public static PagoPAChatResourceBundle getInstance() {
		if (instance == null) {
			LOGGER.debug("Instance is null, invoking constructor");
			instance = new PagoPAChatResourceBundle();
			LOGGER.debug("Instance created");
		}
		
		return instance;

	}
	
	
	public String getString(String key, Object... parameters) {
		LOGGER.trace("Requested message pattern for key: {}", key);
		String messagePattern = this.messageBundle.getString(key);
		LOGGER.trace("Found message pattern {}", messagePattern);
		
		String parametersMessage = "[]";
		if(parameters != null) {
			parametersMessage = Arrays.deepToString(parameters);
		}
		
		LOGGER.trace("Formatting message pattern with params {}", parametersMessage);
		String formattedMessage = MessageFormat.format(messagePattern, parameters);
		LOGGER.trace("Returning formatted message: {}", formattedMessage);
		
		return formattedMessage;
	}

	@Override
	public String toString() {
		return "PagoPAChatResourceBundle [messageBundle=" + messageBundle + "]";
	}
	
	
}
