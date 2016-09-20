package command;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import server.boot.BootServer;
import server.boot.ChannelManager;

@Controller
public class NOperationCommand {

	final static String OP_CODE_SET = "OP_SET"; // agent에게 설정내역을 전송
	final static String OP_CODE_REQUEST = "OP_REQUEST"; // agent에게 정보를 요청
	final static String OP_CODE_SHELL = "OP_SHELL"; // agent에게 shell 실행을 명령
	final public static String OP_CODE_JOIN = "OP_JOIN"; // agent가 server와 연결 생성 후 가입메시지를 보냄

	// @Resource(name = "bootServer")
	@Autowired
	BootServer bootServer;

	@RequestMapping("/noperation")
	public String SendCommand(Message message, Model model) throws UnsupportedEncodingException {

		System.out.println("입력메시지 : " + message.getMsg());

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
		List<String> agentNames = message.getAgent();
		if (agentNames != null) {
			Iterator agentName = agentNames.iterator();
			while (agentName.hasNext()) {
				String element = (String) agentName.next();
				// System.out.println(element);

				// agent에게 메시지 보내기
				if (message.getMsg() != null) {
					Channel ch = (Channel) ChannelManager.map.get(element);
					if (ch != null && ch.isActive()) {

						ch.writeAndFlush(initializeProtocol(message.getMsg(), ch));

						ByteBuf buf = PooledByteBufAllocator.DEFAULT
								.buffer(message.getMsg().getBytes(Charset.forName("utf-8")).length);
						buf.writeBytes(message.getMsg().getBytes(Charset.forName("utf-8")));
						// System.out.println("byte[] a : " +
						// ByteBufUtil.hexDump(a));
						ch.writeAndFlush(buf);

						System.out.println("[Sent to] " + ch);
					}
				}
			}
		}

		model.addAttribute("agents", agents);
		model.addAttribute("message", message);

		return "noperation";
	}

	// Operation Header 생성
	private ByteBuf initializeProtocol(String msg, Channel ch) throws UnsupportedEncodingException {
		System.out.println("Message Header");
		// ByteBuf buf = ch.alloc().heapBuffer(OP_CODE_REQUEST.length() + 12 +
		// msg.getBytes("UTF-8").length);
		ByteBuf buf = ch.alloc().heapBuffer(OP_CODE_REQUEST.length() + 12);

		buf.writeInt(OP_CODE_REQUEST.length());
		buf.writeBytes(OP_CODE_REQUEST.getBytes());
		buf.writeLong(msg.getBytes("UTF-8").length);
		// buf.writeBytes(msg.getBytes("UTF-8"));
		System.out.println("buffer index : " + buf.writerIndex());
		System.out.println("hex : " + ByteBufUtil.hexDump(buf));

		return buf;
	}

}
