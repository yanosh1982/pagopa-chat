package it.pagopa.chat.communication;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.pagopa.chat.util.PagoPAChatResourceBundle;

public class SocketBasedCommunicationManager implements ClientSideCommunicationManager, ServerSideCommunicationManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketBasedCommunicationManager.class);

	private Map<String, Socket> activeClients;
	

	public SocketBasedCommunicationManager() {
		super();
		this.activeClients = new ConcurrentHashMap<>();
	}

	public final Map<String, Socket> getActiveClients() {
		return activeClients;
	}

	public String subscribe(Socket clientConnection) {
		String clientId = clientConnection.getInetAddress().getHostName() + String.valueOf(clientConnection.getPort());
		return this.subscribe(clientId, clientConnection);
	}
	
	public String subscribe(String clientId, Socket clientConnection) {
		LOGGER.debug("Storing new subscription");
		this.activeClients.put(clientId, clientConnection);
		LOGGER.debug("subscription stored with id {}", clientId);
		return clientId;
	}

	public void broadcast(String senderId, String message) {
		LOGGER.info("Broadcasting message from {}", senderId);
		for (String currentClientId : activeClients.keySet()) {
			if (currentClientId.equals(senderId)) {
				LOGGER.debug("Skipping receiver as it is the sender");
				continue;
			}

			this.send(senderId, currentClientId, message);
		}
	}
	
	public void broadcast(String message) {
		this.broadcast(SERVER_ID, message);
	}
	
	public void send(String from, String to, String message) {
		LOGGER.debug("Sending message from {}, to {}", from, to);
		Socket clientConnection = this.activeClients.get(to);
		
		try {
			PrintStream out = new PrintStream(clientConnection.getOutputStream());
			out.println(from + ": " + message);
		} catch (IOException e) {
			String error = PagoPAChatResourceBundle.getInstance().getString(CommunicationManagerMessageBundleKeys.CM_SEND_ERROR);
			LOGGER.error(error, e);
		}
	}

	@Override
	public void unsubscribe(String subscriptionId) {
		LOGGER.debug("Removing subscription with ID: {}", subscriptionId);
		this.activeClients.remove(subscriptionId);
		LOGGER.debug("subscription with ID {} successfully removed", subscriptionId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("SocketBasedCommunicationManager [activeClients=");
		if (this.activeClients != null){
			builder.append(activeClients.size());
		}else {
			builder.append(0);
		}
		builder.append("]");
		
		return builder.toString();
	}
	
}
