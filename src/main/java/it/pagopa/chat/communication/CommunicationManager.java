package it.pagopa.chat.communication;

public interface CommunicationManager {
	
	public static final String SERVER_ID = "PagoPAChatServer";
	
	public void send(String from, String to, String message);
	
}
