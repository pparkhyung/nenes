package nene.event;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6586869616351303325L;

	MessageEventData messageEventData;

	public MessageEvent(Object source, MessageEventData data) {
		super(source);
		messageEventData = data;
	}

	public MessageEventData getMessageEventData() {
		return messageEventData;
	}

	public void setMessageEventData(MessageEventData messageEventData) {
		this.messageEventData = messageEventData;
	}

}
