package it.pagopa.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.pagopa.chat.communication.ClientSideCommunicationManager;
import it.pagopa.chat.communication.CommunicationManager;
import it.pagopa.chat.util.PagoPAChatResourceBundle;

public class PagoPAChatClient extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PagoPAChatClient.class);

	private final Socket connection;
	private final String clientId;
	private final ClientSideCommunicationManager communicationManager; 

	public PagoPAChatClient(String clientId, Socket connection, ClientSideCommunicationManager communicationManager) {
		LOGGER.debug("init");
		
		this.clientId = clientId;
		this.connection = connection;
		this.communicationManager = communicationManager;
	}

	@Override
	public void run() {
		PagoPAChatResourceBundle messageBundle = PagoPAChatResourceBundle.getInstance();
		
		LOGGER.debug(messageBundle.getString(ClientMessageBundleKeys.CLIENT_START, this.clientId));
		
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
			
			while ((line = br.readLine()) != null && !line.equals("exit")) {
				communicationManager.broadcast(this.clientId, line);
			}
			
			communicationManager.broadcast(CommunicationManager.SERVER_ID, messageBundle.getString(ClientMessageBundleKeys.CLIENT_LEAVING, this.clientId));
			communicationManager.unsubscribe(this.clientId);
			this.connection.close();
		} catch (IOException e) {
			LOGGER.error("", e);
		}
		LOGGER.debug(messageBundle.getString(ClientMessageBundleKeys.CLIENT_STOP, this.clientId));
	}

	public final Socket getConnection() {
		return connection;
	}

	public final String getClientId() {
		return clientId;
	}

	public final ClientSideCommunicationManager getCommunicationManager() {
		return communicationManager;
	}

	@Override
	public String toString() {
		return "PagoPAChatClient [connection=" + connection + ", clientId=" + clientId + ", communicationManager="
				+ communicationManager + "]";
	}
	
}
