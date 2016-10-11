package websocket;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import nene.event.MessageEvent;
import nene.event.MessageEventData.EventType;

public class WsHandler extends TextWebSocketHandler implements ApplicationListener<MessageEvent> {

	public Set<WebSocketSession> sessionSet = new HashSet<WebSocketSession>();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private static final Logger logger = LogManager.getLogger("nenes");

	// private HashMap<String, Channel> agentSession = ChannelManager.map;

	public WsHandler() {
		super();
		// System.out.println("create SocketHandler instance!");
	}

	@Override
	public void onApplicationEvent(MessageEvent event) {
		// System.out.println("[debug]onApplicationEvent called");
		/*
		 * try { logger.info("thread speep ~~~~~~~~~~~~~~~~~~ long~");
		 * Thread.sleep(10 * 1000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		sendMessage(event.getMessageEventData().notificationFormat());
		logger.info("sendMessage : " + event.getMessageEventData().toString());

	}

	public void sendMessage(String message) {
		System.out.println("sendMessage called");
		for (WebSocketSession session : sessionSet) {
			if (session.isOpen()) {
				try {
					//System.out.println("session id : " + session.getId());
					session.sendMessage(new TextMessage(message));
				} catch (Exception ignored) {
					System.out.println("fail to send message!");
				}
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionSet.add(session);
		sendMessage("[debug] your ws session id : " + session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessionSet.remove(session);
		System.out.println("remove session!");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		System.out.println("receive message : " + message.toString());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("web socket error!");
	}

	@Override
	public boolean supportsPartialMessages() {
		System.out.println("call method!");
		return super.supportsPartialMessages();
	}

}
