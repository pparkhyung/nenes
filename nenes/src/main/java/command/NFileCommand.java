package command;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import io.netty.handler.stream.ChunkedFile;
import nene.event.EventPublisher;
import nene.event.MessageEventData.EventType;
import nene.util.FileUtil;
import server.boot.BootServer;
import server.boot.ChannelManager;

@Controller
public class NFileCommand {

	// @Resource(name = "bootServer")
	@Autowired
	BootServer bootServer;

	@Autowired
	FileUtil fileService;
	
	@Autowired
	EventPublisher  publisher;

	//log4j2 test
	private static final Logger logger = LogManager.getLogger("nenesLogger");
	
	@RequestMapping("/nfile")
	public String SendCommand(FileMessage fileMessage, Model model) {
		
		logger.debug("log4j test");

		final String workspace = NConfiguration.conf.getFileDirectory();

		// 입력값 로깅
		// System.out.println("입력파일 : " + fileMessage.getFileName());
		// for (String s : fileMessage.getFileList()) {
		// System.out.println("선택파일 : " + s);
		// }

		// 선택한 파일 이름 및 에이전트 가져오기
		List<String> agentNames = fileMessage.getAgent();
		List<String> fileList = fileMessage.getFileList();

		// checkbox를 통해 사용자가 선택한 에이전트에게 파일 보내기
		System.out.println("----------------------Send File-----------------");

		if (agentNames != null) {
			Iterator agentName = agentNames.iterator();
			while (agentName.hasNext()) {
				String element = (String) agentName.next();
				// 1. agent에게 파일 보내기 : 직접입력한 경우
				
				if (fileMessage.getFileName() != null && !fileMessage.getFileName().equals("") ) {
					try {
						SendFile(fileMessage.getFileName(), element);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 2. agent에게 파일 보내기 : 선택한 경우
				for (String selectedfileName : fileList) {
					try {
						SendFile(workspace + NConfiguration.separator + selectedfileName, element);
						System.out.println(
								"send selectedfile " + workspace + NConfiguration.separator + selectedfileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// workspace의 파일을 조회한다.
		try {
			fileList = fileService.fileList(workspace);
			for (String s : fileList)
				System.out.println("[workspace file list] " + s);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 연결된 에이전트 리스트 가져오기
		System.out.println("----------------------channel Status-----------------");
		List<String> agents = new ArrayList<String>();
		Iterator iterator = ChannelManager.map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			System.out.println("channel ID: " + entry.getKey() + ", channel Info: " + entry.getValue());
			agents.add(entry.getKey().toString());
		}

		model.addAttribute("fileList", fileList);
		model.addAttribute("agents", agents);
		model.addAttribute("fileMessage", fileMessage);

		return "nfile";
	}

	void SendFile(final String msg, String element) throws IOException {

		final Channel ch = (Channel) ChannelManager.map.get(element);

		if (ch == null || !ch.isActive()) {
			return;
		}

		// 실제파일이 있는지 검증
		//System.out.println("실제파일이 있는지 검증");
		RandomAccessFile raf = null;
		long length = -1;
		try {
			System.out.println("[debug]" + msg);
			raf = new RandomAccessFile(msg, "r");
			length = raf.length();
		} catch (Exception e) {
			// ch.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " +
			e.printStackTrace();
			System.err.println("파일 검증 실패");
			return;
		} finally {
			if (length > 0 && raf != null) {
				raf.close();
			}
		}

		// 파일헤더 전송
		ChannelFuture future = ch.writeAndFlush(this.initializeProtocol(msg, ch));

		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("header transfer complete. transfer data");

				// try {
				RandomAccessFile raf = new RandomAccessFile(msg, "r");
				long length = raf.length();

				//System.out.println("파일 전송");
				if (ch.pipeline().get(SslHandler.class) == null) {
					// SSL not enabled - can use zero-copy file transfer.

					ChannelFuture futurefile = ch.writeAndFlush(new DefaultFileRegion(raf.getChannel(), 0, length));

					futurefile.addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							System.out.println("file transfer complete");
							publisher.publishEvent(this, EventType.FileSent, element, new File(msg).getName());
							raf.close();
						}
					});

				} else {
					// SSL enabled - cannot use zero-copy file transfer.
					ChannelFuture futurefile = ch.writeAndFlush(new ChunkedFile(raf));
					
					futurefile.addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							System.out.println("file transfer complete");
							publisher.publishEvent(this, EventType.FileSent, element, new File(msg).getName());
							raf.close();
						}
					});
					
				}
				// } catch (IOException e) {
				// e.printStackTrace();
				// return;
				// } finally {
				// if (length > 0 && raf != null) {
				// raf.close();
				// }
				// }
				// 파일 전송

			}
		});

	}

	private ByteBuf initializeProtocol(String msg, Channel ch) throws UnsupportedEncodingException {
		final File file = new File(msg);
		//System.out.println("header");
		ByteBuf buf = ch.alloc().heapBuffer(file.getName().length() + 12);
		buf.writeInt(file.getName().getBytes("UTF-8").length);
		buf.writeBytes(file.getName().getBytes("UTF-8"));
		buf.writeLong(file.length());
		//System.out.println("buffer index : " + buf.writerIndex());
		return buf;
	}
}
