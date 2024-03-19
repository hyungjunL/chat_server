package study.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageInfoDTO {
	
	@JsonProperty("to_user_id")
	@JsonInclude(Include.NON_NULL)
	private String toUserId;
	
	@JsonProperty("message")
	@JsonInclude(Include.NON_NULL)
	private String message;
}
