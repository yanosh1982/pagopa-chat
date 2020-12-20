/**
 * 
 */
package it.pagopa.chat.server;

/**
 * @author VACCARO
 *
 */
public class ServerMessageBundleKeys {

	private ServerMessageBundleKeys() {
		super();
	}
	
	public static final String SERVER_START = "it.aci.pagopa.chat.server.start";

	public static final String SERVER_START_ERROR = "it.aci.pagopa.chat.server.start.error";
	
	public static final String INVALID_PORT_FORMAT = "it.aci.pagopa.chat.server.invalid.port.format";
	
	public static final String INVALID_BACKLOG_FORMAT = "it.aci.pagopa.chat.server.invalid.backlog.format";
	
	public static final String SERVER_OPENING_CONNECTION = "it.aci.pagopa.chat.server.opening.connection";
	
	public static final String SERVER_LISTENING = "it.aci.pagopa.chat.server.listening";
	
	public static final String NEW_CONNECTION = "it.aci.pagopa.chat.server.new.connection";
	
	public static final String NEW_CONNECTION_SUCCESS = "it.aci.pagopa.chat.server.new.connection.success";
	
	public static final String NEW_CONNECTION_WELCOME= "it.aci.pagopa.chat.server.new.connection.welcome";
	
	public static final String NEW_CONNECTION_ERROR = "it.aci.pagopa.chat.server.new.connection.error";
	
}
