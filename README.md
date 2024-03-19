netty라이브러리를 이용한 websocket채팅서버 구축 

1. websocket 서버 기동

2. 클라이언트 2개 기동 (필자는 simpleWebsocketClient와 postman websocket client를 사용함)

3. 클라이언트에서 서버로 접속을 하면 서버에서 클라이언트의 접속 정보를 관리

4. 클라이언트에서 json포맷에 맞게 메시지를 전송하면 수신 받을 클라이언트의 채널을 서버가 찾아서 전달해줌
    
