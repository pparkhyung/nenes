package server.boot;

import org.springframework.beans.factory.InitializingBean;

import io.netty.channel.Channel;
import server.operation.OperationServer;
import server.telnet.TelnetServer;

public class BootServer implements InitializingBean {

	// private ServerBootstrap serverBootstrap;

	private Channel channel;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("BootServer-afterPropertiesSet()");
		// serverBootstrap = new TelnetServer().start();
		// channel = new FileServer().start();
		new OperationServer().start();
		System.out.println("OperationServer.start");
		new TelnetServer().start();
		System.out.println("TelnetServer.start");
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
