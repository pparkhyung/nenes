/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package server.operation;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Server that accept the path of a file an echo back its content.
 */
@Service
public final class OperationServer {

	//실행 옵션에 -Dssl 을 추가하면 적용된다. 
	//주의 -Dssl=true, -Dssl=false 와 같이 값을 적용하지 않고 -Dssl 옵션만 있어도 true로 된다.
	//즉, -Dssl=false라고 해도 true로 인식한다)
	static final boolean SSL = System.getProperty("ssl") != null; 
	// Use the same default port with the telnet example so that we can use the
	// telnet client example to access it.
	static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "6443" : "6000"));
	
	@Autowired
	WebApplicationContext applicationContext;
	
	@Autowired
    ApplicationEventPublisher eventPublisher;
	
	public void start() throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
        	//테스트 목적의 빠르지만 안전하지 않은 임시 인증서와 개인키를 생성한다.
        	//따라서, 운영모드에서는 사용하지 말 것.
        	//jvm 종료시 임시파일은 제거된다. (netty api 설명참고)
            //SelfSignedCertificate ssc = new SelfSignedCertificate();
            //sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            
            //File cert = new File("./resource/cert/nene.crt");
        	//File privateKey= new File("./resource/cert/privatekey.pem");
        	
        	//resources/cert 이하 인증서와 개인키 파일을 로드한다.
        	//개인키는 pkcs#8스펙이어야 한다.
        	final File cert = ResourceUtils.getFile("classpath:/cert/nene.crt");
        	final File privateKey= ResourceUtils.getFile("classpath:/cert/nene.pem");
            sslCtx = SslContextBuilder.forServer(cert, privateKey, "tkfkdgo123!").build();
            System.out.println("ssl이 적용됩니다. : " + SSL + ", port : " +PORT);
            
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.DEBUG))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     if (sslCtx != null) {
                         p.addLast(sslCtx.newHandler(ch.alloc()));
                     }
                     p.addLast(
                    		 new LoggingHandler(LogLevel.DEBUG),
                             //new StringEncoder(CharsetUtil.UTF_8),
                             //new LineBasedFrameDecoder(8192),
                             //new StringDecoder(CharsetUtil.UTF_8),
                             new ChunkedWriteHandler(),
                             new OperationServerHandler(applicationContext, eventPublisher));
                 }
             });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            //f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }
}
