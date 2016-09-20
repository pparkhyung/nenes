package command;

import java.io.File;
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
import server.boot.BootServer;
import server.boot.ChannelManager;

@Controller
public class NCommand {

	// @Resource(name = "bootServer")
	@Autowired
	BootServer bootServer;

	@RequestMapping("/ncommand")
	public String SendCommand(Message message, Model model) {

		System.out.println("입력메시지 : " + message.getMsg());

		List<String> agents = new ArrayList<String>();

		// 연결된 에이전트 리스트 가져오기
		System.out.println("----------------------channel Status-----------------");
		Iterator iterator = ChannelManager.telnetMap.entrySet().iterator();
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
					Channel ch = (Channel) ChannelManager.telnetMap.get(element);
					if (ch != null && ch.isActive()) {
						ch.writeAndFlush(message.getMsg() + "\r\n");
						System.out.println("[Sent to] " + ch);
					}
				}
			}
		}

		model.addAttribute("agents", agents);
		model.addAttribute("message", message);

		return "ncommand";
	}

	@Deprecated
	public String SendCommandTest(Message message, Model model) {

		System.out.println("Message.msg : " + message.getMsg());

		List<String> agents = new ArrayList<String>();

		// 연결된 에이전트 리스트 가져오기
		Iterator iterator = ChannelManager.map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());

			agents.add(entry.getKey().toString());

			// System.out.println(ch.isWritable());
			// System.out.println(ch.isActive());
			// System.out.println(ch.isOpen());
		}

		// checkbox로 선택한 에이전트 ID 보기
		List<String> agentName = message.getAgent();

		if (agentName != null) {

			Iterator ii = agentName.iterator();
			while (ii.hasNext()) {
				String element = (String) ii.next();
				System.out.println(element);

				// agent에게 메시지 보내기
				if (message.getMsg() != null) {
					Channel ch = (Channel) ChannelManager.telnetMap.get(element);
					ch.writeAndFlush(message.getMsg() + "\r\n");
					System.out.println("Sent to" + ch);
				}
			}
		}

		// Map referenceData = new HashMap();
		// Map<String, List<String>> a = new HashMap<>();
		// referenceData.put("agents", agents);

		/*
		 * List<String> courses = new ArrayList<String>(); courses.add("Yoga");
		 * courses.add("Stretching"); courses.add("Pilates");
		 * courses.add("Aerobic"); courses.add("Oriental"); //
		 * Arrays.asList("111", "222", "33")
		 * 
		 * Map bbsPropOption = new HashMap(); bbsPropOption.put("notice","공지");
		 * bbsPropOption.put("secret","비밀글");
		 */

		model.addAttribute("agents", agents);
		model.addAttribute("message", message);

		/*
		 * ModelAndView mav = new ModelAndView("Command");
		 * mav.addObject("agents", agents); mav.addObject("message", message);
		 */

		return "Command";
		// return mav;
	}

}