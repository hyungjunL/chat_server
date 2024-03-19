package study.service;


import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.handler.RequestHandler;
import study.handler.ServerHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketService {
	
	private final ServerHandler serverHandler;
	private final RequestHandler requestHandler;
	
	
	// 클라이언트의 접속 요청을 수락해주는 쓰레드 개수
    private static int bossCount=1;
    
	
	// 보스이벤트 루프에서 넘겨주는 이벤트를 처리할 쓰레드 갯수
    private static int workCount=5;
    
	
	// 서버 채널 그룹
	public static final EventLoopGroup bossGroup = new NioEventLoopGroup(bossCount);
	
	
	// 클라이언트 채널 그룹
	public static final EventLoopGroup workerGroup = new NioEventLoopGroup(); //CPU core * 2
	
	public void start() {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new WebSocketInitializer());
		
			//포트에 해당하는 websocket서버 기동
			ChannelFuture future = bootstrap.bind(8888);
			future.syncUninterruptibly();
		} catch (Exception e) {
			log.error(" ## 서버 생성 중 애러 발생 : [{}], [{}] " , e, e.getMessage());
			e.printStackTrace();
		} 
	}
	
	public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpServerCodec());
			pipeline.addLast(new ChunkedWriteHandler());
			pipeline.addLast(new HttpObjectAggregator(65536));
			pipeline.addLast(requestHandler);
			pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
			pipeline.addLast(serverHandler);
			
		}
	}
}
