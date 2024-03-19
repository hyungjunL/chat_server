package study.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.service.ChatWebSocketService;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
	
	
	private final ChatWebSocketService chatWebSocketService;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.debug("[ApplicationReadyListener] APPLICATION READY");
		
		if(event instanceof ApplicationReadyEvent) {
			log.debug("[ApplicationReadyListener]  --> WebSocket Server Start");
			
			//Netty 웹소켓 서비스 시작
			chatWebSocketService.start();
		}
	}
}
