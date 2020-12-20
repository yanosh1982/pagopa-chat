package it.pagopa.chat;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import it.pagopa.chat.communication.SocketBasedCommunicationManager;

@RunWith(MockitoJUnitRunner.class)
public class TestSocketBasedCommunicationManager {

	private SocketBasedCommunicationManager cut;

	private static final String SENDER = "SENDER";

	private static final String RECEIVER = "RECEIVER";

	private static final String SECOND_RECEIVER = "RECEIVER2";

	private static final String MESSAGE = "MESSAGE";

	@Mock
	private Socket fakeSocket;

	@Mock
	private OutputStream fakeOutputStream;

	@Before
	public void init() {
		this.cut = new SocketBasedCommunicationManager();
	}

	@Test
	public void testSendSuccessfull() {
		try {
			when(fakeSocket.getOutputStream()).thenReturn(System.out);

			this.cut.subscribe(RECEIVER, fakeSocket);
			this.cut.send(SENDER, RECEIVER, MESSAGE);

			verify(this.fakeSocket).getOutputStream();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSendFailure() {
		try {
			when(fakeSocket.getOutputStream()).thenThrow(IOException.class);

			this.cut.subscribe(RECEIVER, fakeSocket);
			this.cut.send(SENDER, RECEIVER, MESSAGE);

			verify(this.fakeSocket).getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSubscribe() {
		this.cut.subscribe(RECEIVER, fakeSocket);

		Map<String, Socket> activeClients = this.cut.getActiveClients();

		assertTrue(RECEIVER + " subscription failed", activeClients.containsKey(RECEIVER));

	}

	@Test
	public void testBroadcastFromClient() {
		try {
			when(fakeSocket.getOutputStream()).thenReturn(System.out);
			this.cut.subscribe(RECEIVER, fakeSocket);
			this.cut.subscribe(SECOND_RECEIVER, fakeSocket);

			this.cut.broadcast(RECEIVER, MESSAGE);

			verify(this.fakeSocket, times(1)).getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBroadcastFromServer() {
		try {
			when(fakeSocket.getOutputStream()).thenReturn(System.out);
			this.cut.subscribe(RECEIVER, fakeSocket);
			this.cut.subscribe(SECOND_RECEIVER, fakeSocket);

			this.cut.broadcast(MESSAGE);

			verify(this.fakeSocket, times(2)).getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUsubscribe() {
		this.cut.subscribe(RECEIVER, fakeSocket);

		Map<String, Socket> activeClients = this.cut.getActiveClients();

		assertTrue(RECEIVER + " subscription failed", activeClients.containsKey(RECEIVER));

		this.cut.unsubscribe(RECEIVER);

		Map<String, Socket> activeClientsAfterUnsubscribe = this.cut.getActiveClients();

		assertTrue(RECEIVER + " unsubscription failed", !activeClientsAfterUnsubscribe.containsKey(RECEIVER));
	}

}
