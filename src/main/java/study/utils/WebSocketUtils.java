package study.utils;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import study.domain.UserInfoDTO;
import study.handler.RequestHandler;

public class WebSocketUtils {
	
	public static void removeClient(ChannelHandlerContext ctx) {
		
		String ipPort = getIpPort(ctx);
		
		//접속이 끊기면 클라이언트의 정보를 정리
		RequestHandler.userChannelMap.get(ipPort).getChannel().close();
		RequestHandler.userChannelMap.remove(ipPort);
    }
	
	public static void addClient(ChannelHandlerContext ctx, String userId) {
		
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setUserId(userId);
		userInfoDTO.setChannel(ctx.channel());
		
		String ipPort = getIpPort(ctx);
		//접속을 성공한 client의 ip, port를 key로 두고 채널을 value로 둔 메모리에 적재한다. => 클라이언트의 접속정보를 관리한다.
		RequestHandler.userChannelMap.put(ipPort, userInfoDTO);
	}
	
	public static String getIpPort(ChannelHandlerContext ctx) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		return inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
	}
}
