package it.pagopa.chat.communication;

public interface ClientSideCommunicationManager extends CommunicationManager {

	public void broadcast(String senderId, String message);
	
	public void unsubscribe(String connectionId);
	
}
