package it.pagopa.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.pagopa.chat.client.PagoPAChatClient;
import it.pagopa.chat.communication.ClientSideCommunicationManager;
import it.pagopa.chat.communication.ServerSideCommunicationManager;
import it.pagopa.chat.communication.SocketBasedCommunicationManager;
import it.pagopa.chat.util.PagoPAChatResourceBundle;

/**
 * Hello world!
 *
 */
public class PagoPAChatServer implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PagoPAChatServer.class);
	
	private int port = 10000;
	private int backolog = 10;
	private final ServerSideCommunicationManager communicationManager;
	
	private PagoPAChatServer(PagoPAChatServerBuilder builder) {
		this.port = builder.port;
		this.backolog = builder.backlog;
		this.communicationManager = builder.communicationManager;
	}
	
	public final int getPort() {
		return port;
	}

	public final int getBackolog() {
		return backolog;
	}

	public final ServerSideCommunicationManager getCommunicationManager() {
		return communicationManager;
	}
	
	@Override
	public void run() {
		PagoPAChatResourceBundle messageBundle = PagoPAChatResourceBundle.getInstance();
		
		echo(messageBundle.getString(ServerMessageBundleKeys.SERVER_START));
		
		echo(messageBundle.getString(ServerMessageBundleKeys.SERVER_OPENING_CONNECTION, port, backolog));
		try (ServerSocket serverSocket = new ServerSocket(port , backolog)){
			echo(messageBundle.getString(ServerMessageBundleKeys.SERVER_LISTENING));
			while(true)	{
				listen(serverSocket);
			}
		} catch(IOException e) {
			LOGGER.error(messageBundle.getString(ServerMessageBundleKeys.SERVER_START_ERROR), e);
		}
		
	}
	
	public void listen(ServerSocket serverSocket) {
		PagoPAChatResourceBundle messageBundle = PagoPAChatResourceBundle.getInstance();
		Socket clientConnection = null;
		try {	
			clientConnection = serverSocket.accept();
		} catch(IOException e) {
			LOGGER.error(messageBundle.getString(ServerMessageBundleKeys.NEW_CONNECTION_ERROR), e);
		}
		echo(messageBundle.getString(ServerMessageBundleKeys.NEW_CONNECTION, clientConnection.getInetAddress().getHostName(), String.valueOf(clientConnection.getPort())));
		
		String clientId = communicationManager.subscribe(clientConnection);
		
		if(ClientSideCommunicationManager.class.isAssignableFrom(communicationManager.getClass())) {
			new PagoPAChatClient(clientId, clientConnection, (ClientSideCommunicationManager)communicationManager).start();
		} else {
			throw new IllegalArgumentException("unknow client side communication manager type");
		}
		
		communicationManager.broadcast(messageBundle.getString(ServerMessageBundleKeys.NEW_CONNECTION_SUCCESS, clientId));
		
		communicationManager.send(SocketBasedCommunicationManager.SERVER_ID, clientId, messageBundle.getString(ServerMessageBundleKeys.NEW_CONNECTION_WELCOME));
	}
	
	public void echo(String msg) {
		System.out.println(msg);
	}
	
	public static class PagoPAChatServerBuilder {
		private static final Logger LOGGER = LoggerFactory.getLogger(PagoPAChatServerBuilder.class);
		
		ServerSideCommunicationManager communicationManager;
		private int port;
		private int backlog;
		
		public PagoPAChatServerBuilder(ServerSideCommunicationManager communicationManager) {
			this.communicationManager = communicationManager;
			
			//init with default values
			this.port = 10000;
			this.backlog = 5;
		}
		
		public PagoPAChatServerBuilder withPort(int port) {
			this.port = port;
			return this;
		}
		
		public PagoPAChatServerBuilder withBacklog(int backlog) {
			this.backlog = backlog;
			return this;
		}
		
		public PagoPAChatServer build() {
			LOGGER.debug("Building new PagoPAChatServer instance");
			return new PagoPAChatServer(this);
		}
	}

	@Override
	public String toString() {
		return "PagoPAChatServer [port=" + port + ", backolog=" + backolog + ", communicationManager="
				+ communicationManager + "]";
	}

}
