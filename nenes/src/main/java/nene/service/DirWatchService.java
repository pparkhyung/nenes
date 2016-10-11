package nene.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import command.NConfiguration;
import nene.event.EventPublisher;
import nene.event.MessageEventData.EventType;

@Service
public class DirWatchService extends Thread{

	/**
	 * 이벤트 알림을 위한 서비스를 얻어옴
	 */
	@Autowired
	private EventPublisher publisher;
	
	@PostConstruct // D.I 완료 후 실행됨... 즉, @Autowired로 객체 주입이 할당되므로 null 발생이 안함
	public void startService() {
		start(); // Thread는 start로 메소드로 실행해야 함. run으로 하면 안됨
	}

	public void run() {
		watch();
	}
	
	/**
	 * 설정에서 저장한 디렉토리를 지정한다.
	 */
	public void watch() {
		watch(NConfiguration.conf.getFileDirectory());
	}

	/**
	 * @param Path
	 *            와치서비스가 수행될 타겟 디렉토리. null이면 설정에서 저장한 디렉토리를 지정한다
	 */
	public void watch(String Path) {

		String targetDir = "";

		if (Path == null) {
			watch(NConfiguration.conf.getFileDirectory());
		} else {
			targetDir = Path;
		}

		System.out.println("[Service] DirWatchService : " + targetDir);

		FileSystem fs = FileSystems.getDefault();
		Path watchPath = fs.getPath(targetDir);

		WatchService watchService = null;

		try {
			watchService = fs.newWatchService();
			watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE
			// StandardWatchEventKinds.ENTRY_MODIFY,
			// StandardWatchEventKinds.ENTRY_DELETE
			);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {

				// 지정된 디렉토리에 변경이되는지 이벤트를 모니터링한다.
				WatchKey changeKey = watchService.take();

				List<WatchEvent<?>> watchEvents = changeKey.pollEvents();

				for (WatchEvent<?> watchEvent : watchEvents) {
					// Ours are all Path type events:
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) watchEvent;

					Path path = pathEvent.context();
					WatchEvent.Kind<Path> eventKind = pathEvent.kind();

					System.out.println(eventKind + " for path: " + path);

					//publishEvent(EventType.FileIn, "DirWatchService", eventKind + " for path: " + path);
					
					publisher.publishEvent(this, EventType.FileIn, "DirWatchService", eventKind + " for path: " + path);
				}

				boolean valid = changeKey.reset(); // Important!

				if (!valid) {
					break;
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
}
