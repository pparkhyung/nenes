/*
 * Copyright 2014 The Netty Project
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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.WebApplicationContext;

import command.OperationCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import nene.event.MessageEvent;
import nene.event.MessageEventData;
import nene.event.MessageEventData.EventType;
import server.boot.ChannelManager;

public class OperationServerHandler extends SimpleChannelInboundHandler<Object> {

	WebApplicationContext applicationContext;

	ApplicationEventPublisher eventPublisher;

	int headerSize;
	String headerBody;
	long dataSize;

	boolean headerRead = false;
	private long offset = 0;
	long startTm;

	String operationCode = "";
	String operationData = "";

	// @Resource(name="wsHandler")
	// WsHandler wsHandler;

	// 에이전트 이름은 nenea가 처음 연결할 떄 보내주는 OP_CODE_JOIN 메시지에 담겨있다
	String agentName = "";

	public OperationServerHandler(WebApplicationContext applicationContext, ApplicationEventPublisher eventPublisher) {
		super();
		this.applicationContext = applicationContext;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {

		//ssl 거부시에도 등록되므로 nenea에서 송신한 join 메시지를 받아야만 채널을 등록함 > 로그만 남기자
		//ChannelManager.map.put(ctx.channel().id().toString(), ctx.channel());
		System.out.println("[channelActive] Client " + ctx.channel().remoteAddress() + " connected");

		// Send greeting for a new connection.
		// ctx.writeAndFlush("HELO: Type the path of the file to retrieve.\n");
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf buf = (ByteBuf) msg;
		System.out.println("입력바이트 : " + (ByteBufUtil.hexDump(buf)));

		startTm = System.currentTimeMillis();

		/*
		 * if (buf.readableBytes() <= 20) {
		 * System.out.println("buf.readableBytes() : " + buf.readableBytes() +
		 * "읽을 바이트가 20도 안됩니다. "); return; }
		 */

		// 1. 헤더 분석
		headerSize = buf.readInt();
		if (headerSize >= 100 || headerSize < 0) {
			System.out.println("fileNameSize : " + headerSize);
			// System.out.println((ByteBufUtil.hexDump(buf)));
			return;
		}

		headerBody = buf.readBytes(headerSize).toString(CharsetUtil.UTF_8);
		dataSize = buf.readLong();
		System.out.println("headerSize : " + headerSize + ", headerBody : " + headerBody + ", dataSize : " + dataSize);

		headerRead = true;

		// 2. 헤더 분류 및 분류에 따른 처리 : operation or file

		if (headerBody.startsWith(OperationCommand.OP_CODE_JOIN)) {
			operationCode = headerBody;
		} else {
			System.err.println("정의한 Header Body가 아닙니다. : " + headerBody);
			return;
		}

		int a = buf.readableBytes();
		int b = (int) Math.min(dataSize - offset, a);
		System.out.println("ReadableBytes : " + a + ", To-Read Bytes : " + b);

		operationData += buf.readBytes(b).toString(CharsetUtil.UTF_8);
		System.out.println("OP Data 저장 중 : " + operationData);

		offset += b;

		if (offset >= dataSize) {
			offset = 0;
			headerRead = false;

			if (headerBody.startsWith(OperationCommand.OP_CODE_JOIN)) {
				// 에이전트 접속시 이름생성
				agentName = operationData + "(" + ctx.channel().id().toString() + ")";

				// WebApplicationContext context =
				// ContextLoader.getCurrentWebApplicationContext();
				// WsHandler wsHandler =
				// (WsHandler)context.getBean("websocket.WsHandler.class");
				// WebApplicationContext wac = BootServer.wac;
				// WsHandler wsHandler = (WsHandler)wac.getBean("wsHandler");
				// WsHandler.sendMessage(agentName + "("
				// +ctx.channel().id().toString() + ")");

			}

			//ChannelManager.map.remove(ctx.channel().id().toString());
			ChannelManager.map.put(agentName, ctx.channel());

			// 이벤트 기반 코드
			publishEvent(EventType.Connected, agentName, "");

			// 강력한 결합 코드
			// WsHandler ws = (WsHandler)
			// applicationContext.getBean("wsHandler");
			// ws.sendMessage(agentName + "(" + ctx.channel().id().toString() +
			// ") is connected");
			// System.out.println(this.getClass() + agentName + "(" +
			// ctx.channel().id().toString() + ") is connected");

			System.out.println("OP Data : " + operationData);
			System.out.println("OP Data 처리시간 : " + ((System.currentTimeMillis() - startTm) / 1000.0f) + "초");

			operationData = ""; // 초기화

			return;
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();

		if (ctx.channel().isActive()) {
			ctx.writeAndFlush("ERR: " + cause.getClass().getSimpleName() + ": " + cause.getMessage() + '\n')
					.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		ChannelManager.map.remove(agentName);
		publishEvent(EventType.Disconnected, agentName, "");
		System.out.println(ctx.channel().id());

	}

	private void publishEvent(EventType eventType, String publisher, String eventMessage) {
		MessageEventData data = new MessageEventData();
		data.setEventType(eventType);
		data.setPublisher(publisher);
		data.setMessage(eventMessage);
		MessageEvent event = new MessageEvent(this, data);
		eventPublisher.publishEvent(event);
	}

}
