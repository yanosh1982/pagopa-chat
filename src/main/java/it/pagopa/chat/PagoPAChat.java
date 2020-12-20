package it.pagopa.chat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.pagopa.chat.communication.SocketBasedCommunicationManager;
import it.pagopa.chat.server.PagoPAChatServer;
import it.pagopa.chat.server.ServerMessageBundleKeys;
import it.pagopa.chat.server.PagoPAChatServer.PagoPAChatServerBuilder;
import it.pagopa.chat.util.PagoPAChatResourceBundle;

public class PagoPAChat {
	private static final Logger LOGGER = LoggerFactory.getLogger(PagoPAChat.class);
	
	public static void main(String[] args) {
		PagoPAChatResourceBundle messageBundle = PagoPAChatResourceBundle.getInstance();
		SocketBasedCommunicationManager communicationManager = new SocketBasedCommunicationManager();
		
		PagoPAChatServerBuilder serverBuilder = new PagoPAChatServerBuilder(communicationManager);
		
		if (args.length > 0) {
			LOGGER.info("Setting up environment..");

			if (args.length >= 1 && args[0] != null) {
				String requestedPort = args[0];
				if (!StringUtils.isBlank(requestedPort)) {
					try {
						int port = Integer.parseInt(requestedPort);
						serverBuilder.withPort(port);
					} catch (NumberFormatException nfe) {
						LOGGER.warn(messageBundle.getString(ServerMessageBundleKeys.INVALID_PORT_FORMAT, requestedPort));
					}

				}
			}

			if (args.length > 1 && args[1] != null) {
				String requestedBacklog = args[1];
				if (!StringUtils.isBlank(requestedBacklog)) {
					try {
						int backlog = Integer.parseInt(requestedBacklog);
						serverBuilder.withBacklog(backlog);
					} catch (NumberFormatException nfe) {
						LOGGER.warn(messageBundle.getString(ServerMessageBundleKeys.INVALID_BACKLOG_FORMAT, requestedBacklog));
					}

				}
			}
		}
		
		PagoPAChatServer server = serverBuilder.build();
		
		Thread serverThread = new Thread(server);
		serverThread.start();
	}

}
