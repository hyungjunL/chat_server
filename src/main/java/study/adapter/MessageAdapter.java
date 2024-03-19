package study.adapter;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.domain.MessageInfoDTO;
import study.domain.UserInfoDTO;
import study.handler.RequestHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageAdapter {
	
	private final ObjectMapper objectMapper;
	
	public void processMessage(ChannelHandlerContext ctx, String message) {
		log.debug(" ### MESSAGE INFO : [{}] ", message);
		try {
			MessageInfoDTO messageInfoDTO = objectMapper.readValue(message, MessageInfoDTO.class);

			Iterator it = RequestHandler.userChannelMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, UserInfoDTO> entry = (Map.Entry) it.next();
		        if(messageInfoDTO.getToUserId().equals(entry.getValue().getUserId())){
		        	ByteBuf in = Unpooled.wrappedBuffer(message.getBytes());
		        	entry.getValue().getChannel().writeAndFlush(new TextWebSocketFrame(in)).addListener(new ChannelFutureListener() {

		        		@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if(!future.isSuccess()) {
								log.error(" ## sending message is fail... ");
							}
							log.debug(" ## send message : {} , {}", future.isSuccess(), message);
						}
					});
		    	    break;
		        }
		    }
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error(" ## Error occured during Processing message -> [{}], [{}]", e, e.getMessage());
		}
		
	}
	
}
