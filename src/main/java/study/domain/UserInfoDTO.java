package study.domain;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class UserInfoDTO {
	
	private String userId;
	private Channel channel;

}
