package study.handler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.domain.UserInfoDTO;
import study.utils.WebSocketUtils;

@Slf4j
@Sharable
@Component
public class RequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	// 접속 사용자 정보 관리 메모리
	public static ConcurrentHashMap<String, UserInfoDTO> userChannelMap = new ConcurrentHashMap<String, UserInfoDTO>();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		try {
			// URI에 있는 파라미터 값들은 key : value형태로 변환
			QueryStringDecoder query = new QueryStringDecoder(request.uri());
			Map<String, List<String>> map = query.parameters();
			
			// 접속된 사용자 정보를 메모리에 관리
			String userId = map.get("user_id").get(0);
			WebSocketUtils.addClient(ctx, userId);
			
			// ? 내용을 좀더 공부해야됨 이렇게 fireChannelRead를 안하면 접속을 하다가 말음
			int pos = request.uri().indexOf("?");
			String uri = request.uri().substring(0, pos);

			request.setUri(uri);
			ctx.fireChannelRead(request.retain());
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
