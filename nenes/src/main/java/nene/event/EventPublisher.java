package nene.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nene.event.MessageEventData.EventType;

@Service
public class EventPublisher {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public void publishEvent(Object object, EventType eventType, String publisher, String eventMessage) {
		MessageEventData data = new MessageEventData();
		data.setEventType(eventType);
		data.setPublisher(publisher);
		data.setMessage(eventMessage);
		MessageEvent event = new MessageEvent(object, data);
		eventPublisher.publishEvent(event);
	}

}
