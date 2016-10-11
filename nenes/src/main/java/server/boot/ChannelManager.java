package server.boot;

import java.util.HashMap;

import io.netty.channel.Channel;

public class ChannelManager {
	
	//nenea 에이전트가 접속한 세션을 저장함
	public static HashMap<String, Channel> map = new HashMap<>();
	
	//텔넷으로 접속한 세션을 저장함
	public static HashMap<String, Channel> telnetMap = new HashMap<>();

}
