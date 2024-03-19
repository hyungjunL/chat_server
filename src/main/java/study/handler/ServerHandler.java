package study.handler;

import java.net.InetSocketAddress;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.adapter.MessageAdapter;
import study.utils.WebSocketUtils;

@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
	
	private final MessageAdapter messageAdapter;
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) {
		
    }
    
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		WebSocketFrame frame = (WebSocketFrame) msg;
		TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
		
		String message = textFrame.text();
		messageAdapter.processMessage(ctx, message);
		
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	log.debug(" ## 채팅 서버 접속 성공!!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	cause.printStackTrace();
    	log.error(" ## 채팅 서버 접속 에러...");
    	//사용자 정보 정리
    	WebSocketUtils.removeClient(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    	log.error(" ## 채팅 서버 접속이 끊어짐...");
    	//사용자 정보 정리
    	WebSocketUtils.removeClient(ctx);
    }
    
}
