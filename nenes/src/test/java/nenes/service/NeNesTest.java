package nenes.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.ssl.SslHandler;

public class NeNesTest {

	@Test
	public void testCase6() throws Exception {

		RandomAccessFile raf = new RandomAccessFile("D:\\nene_data\\1474786121994aaaaaa.zip", "r");
		long length = raf.length();

		// 파일 전송
		System.out.println("파일 전송");
		new DefaultFileRegion(raf.getChannel(), 0, length);

		System.out.println("file transfer complete");
		raf.close();
		System.out.println("랜덤액세스파일 종료");

		Thread.sleep(210000000);
	}

	/**
	 * 디렉토리에서 파일리스트 가져오기
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCase5() throws Exception {

		String directory = "C:\\Users\\72495\\.nenes\\workspace";

		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path path : directoryStream) {
				fileNames.add(path.toString());
				System.out.println(path.toString());
				System.out.println(path.getFileName());
			}
		} catch (IOException ex) {
		}

	}

	@Test
	public void testCase1() throws Exception {
		System.out.println("testCase1");

		File file = new File("./test.txt");

		FileWriter w = new FileWriter(file);

		w.write("맘마");
		w.close();

		System.out.println(file.getAbsolutePath());

		// ResourceUtils.getFile("classpath:conf/conf.json");

	}

	@Test
	public void testCase2() throws Exception {

		System.out.println("Working Directory : " + System.getProperty("user.dir"));
		System.out.println("Working Directory : " + System.getProperty("user.home"));

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is : " + s);
	}

	@Test
	public void testCase3() throws Exception {
		FileSystem fs = FileSystems.getDefault();

		Path watchPath = fs.getPath("d:\\nene_data");
		WatchService watchService = fs.newWatchService();

		watchPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE);

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
				}

				changeKey.reset(); // Important!

				System.out.println("watchservice called");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
