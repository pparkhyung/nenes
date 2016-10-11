package nene.event;

import java.time.LocalTime;

public class MessageEventData {

	// 이벤트 타입 정의
	public static enum EventType {
		Connected, Disconnected, 
		FileIn, FileOut, FileSent, FileReceive
	}

	// 이벤트 메시지 구성요소: 이벤트타입, 이벤트생산자이름, 이벤트메시지
	String publisher, message;

	private EventType eventType;

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// 화면에 보여지는 메시지 포멧을 정의함
	public String notificationFormat() {
		return "[" + LocalTime.now().toString() + "] " + eventType + " " + publisher + " " + message;
	}

}
