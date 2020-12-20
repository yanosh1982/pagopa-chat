package it.pagopa.chat.communication;

import java.net.Socket;

public interface ServerSideCommunicationManager extends CommunicationManager {

	public String subscribe(Socket clientConnection);
	
	public void broadcast(String message);
	
}
