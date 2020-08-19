# Socket설계서

## 1. Socket Interface

클라이언트와 서버 클래스의 유지와 수정이 쉽게 인터페이스를 상속받아 구현할 수 있게 구성했다.

서버와 클라이언트는 ```run(), send(), receive()``` 공통된 세가지 메소드를 가지고있지만 ```send()와 receive()```메소드는 서버와 클라이언트가 서로 하는 일이 달라 리턴타입과 매개변수가 다르다.

따라서 인터페이스를 설계할때는 Socket 인터페이스를 상속받는 ServerSocketService와 ClientSocketService 인터페이스를 따로 만들어 관리한다.

우선, Socket 인터페이스는 서버와 클래스 간 공통 메소드인 ```run()```을 가지고 있는다.
```java
//SocketService 인터페이스
public interface SocketService {
	public void run();
}
```

ServerSocketService를 구현할 서버클래스는 
receive 메소드에 InputStream을 이용하여 데이터를 받고 받은 데이터가("exit")이면 false를 반환
send 메소드에 OutputStream과 num(1:접속성공메시지, 2:전송성공메시지)를 입력받고 클라이언트에 전송
```java
//SocketService를 상속 받는 ServerSocketService 인터페이스
public interface ServerSocketService extends SocketService{
	public boolean receive(InputStream receiveMsg, Socket client);
	public void send(int num,OutputStream sendMsg);
}
```
ClientSocketService를 구현할 서버클래스는 
send 메소드에 OutputStream을 이용하여 데이터를 받고 받은 데이터가("exit")이면 false를 반환
receive메소드에 InputStream를 받고 서버에서 받은 데이터를 출력
```java
//SocketService를 상속 받는 ClientSocketService 인터페이스
public interface ClientSocketService extends SocketService{
	public void receive(InputStream receiveMsg);
	public boolean send(OutputStream sendMsg, Socket client);
}
```

## 2. Server Socket

서버 소켓은 클라이언트로부터 바이트 데이터를 받고 성공적으로 데이터를 받았으면 성공 메시지를 다시 보낸다.
그리고 언제든 클라이언트와 연결이 가능하게 접속 대기를 한다.


## 3. Client Socket

클라이언트 소켓은 문자열 데이터를 바이트로 변환한 뒤 서버에 전송한다. 또한 전송에 성공하면 서버에서 메시지를 받는다.
