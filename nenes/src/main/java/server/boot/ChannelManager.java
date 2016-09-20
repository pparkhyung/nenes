package server.boot;

import java.util.HashMap;

import io.netty.channel.Channel;

public class ChannelManager {
	
	public static HashMap<String, Channel> map = new HashMap<>();
	
	public static HashMap<String, Channel> telnetMap = new HashMap<>();

}
