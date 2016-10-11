package command;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.ssl.SslHandler;
import server.boot.BootServer;
import server.boot.ChannelManager;

@Controller
public class FileCommand {

	// @Resource(name = "bootServer")
	@Autowired
	BootServer bootServer;

	@RequestMapping("/file")
	public String SendCommand(FileMessage fileMessage, Model model) {

		System.out.println("입력파일 : " + fileMessage.getFileName());

		List<String> agents = new ArrayList<String>();

		// 연결된 에이전트 리스트 가져오기
		System.out.println("----------------------channel Status-----------------");
		Iterator iterator = ChannelManager.map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			System.out.println("channel ID: " + entry.getKey() + ", channel Info: " + entry.getValue());
			agents.add(entry.getKey().toString());
		}

		// checkbox를 통해 사용자가 선택한 에이전트 ID 보기
		System.out.println("----------------------Send Message-----------------");
		List<String> agentNames = fileMessage.getAgent();
		if (agentNames != null) {
			Iterator agentName = agentNames.iterator();
			while (agentName.hasNext()) {
				String element = (String) agentName.next();
				// agent에게 파일 보내기
				if (fileMessage.getFileName() != null) {
					try {
						SendFile(fileMessage.getFileName(), element);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		model.addAttribute("agents", agents);
		model.addAttribute("fileMessage", fileMessage);

		return "File";
	}

	void SendFile(final String msg, String element) throws IOException {

		final Channel ch = (Channel) ChannelManager.map.get(element);

		if (ch == null && !ch.isActive()) {
			return;
		}

		// 실제파일이 있는지 검증
		System.out.println("실제파일이 있는지 검증");
		RandomAccessFile raf = null;
		long length = -1;
		try {
			raf = new RandomAccessFile(msg, "r");
			length = raf.length();
		} catch (Exception e) {
			ch.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
			return;
		} finally {
			if (length < 0 && raf != null) {
				raf.close();
			}
		}
		// ch.write("OK: " + raf.length() + '\n');

		// 파일헤더 전송
		final File file = new File(msg);
		ChannelFuture future = ch.writeAndFlush(this.initializeProtocol(file, ch));

		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("header transfer complete. transfer data");

				RandomAccessFile raf = new RandomAccessFile(msg, "r");
				long length = raf.length();

				// 파일 전송
				System.out.println("파일 전송");
				if (ch.pipeline().get(SslHandler.class) == null) {
					// SSL not enabled - can use zero-copy file transfer.
					ch.write(new DefaultFileRegion(raf.getChannel(), 0, length));
				} else {
					// SSL enabled - cannot use zero-copy file transfer.
					// ch.write(new ChunkedFile(raf));
				}
			}
		});

		// ch.writeAndFlush("\n");
		System.out.println("파일 전송 완료");
	}

	private ByteBuf initializeProtocol(File file, Channel ch) {
		System.out.println("header");
		ByteBuf buf = ch.alloc().heapBuffer(file.getName().length() + 12);
		buf.writeInt(file.getName().length());
		buf.writeBytes(file.getName().getBytes());
		buf.writeLong(file.length());
		System.out.println("buffer index : " + buf.writerIndex());
		return buf;
	}
}
