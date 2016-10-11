package server.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import io.netty.channel.Channel;
import server.operation.OperationServer;

@Service
public class BootServer implements InitializingBean {

	// private ServerBootstrap serverBootstrap;

	public static WebApplicationContext wac;

	private Channel channel;

	@Autowired
	OperationServer operationServer;

	Logger log = LoggerFactory.getLogger("nenes");

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("BootServer-afterPropertiesSet()");
		// serverBootstrap = new TelnetServer().start();
		// channel = new FileServer().start();
		operationServer.start();
		//log.error("slf4j log {}", getClass().getName());
		System.out.println("OperationServer.start");
		// new TelnetServer().start();
		// System.out.println("TelnetServer.start");

		wac = ContextLoader.getCurrentWebApplicationContext();

	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
