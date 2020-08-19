# Socket기능명세서

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

## 2. Server Socket Class

서버는 byte배열의 크기를 미리 상수로 고정한다. 
```java 
private final static int BUFFER_SIZE = 1024;
```

서버의 메소드는 
1. 서버를 동작하게하는 ```void run()```
2. 클라이언트에 메시지를 전달하는 ```void send(int num,OutputStream sendMsg);```
3. 클라이언트에서 메시지를 받는 ```boolean receive(InputStream receiveMsg, Socket client);```

**run()** 메소드는 ServerSocket클래스를 생성하고 무한루프(while(true))를 통해 메소드가 실행되는 한 계속하여 다른 클라이언트들과 접속을 시도한다.

```java
//ServerSocket 생성
ServerSocket server = new ServerSocket();
InetSocketAddress address = new InetSocketAddress("0.0.0.0",9000);
server.bind(address);
while (true) {
				//클라이언트와 서버간에 접속 시도
				Socket client = server.accept();
                .....
}
```

**send()** 메소드는 매개변수값으로 int와 OutputStream을 받는다.
int의 1의 값이 들어오면 클라이언트와 초기 연결에 성공했다는 의미이고, 2의값은 메시지를 성공적으로 받았다는 의미이다.
OutputStream을 통해 메시지를 전송한다.

```java
//보낼 메시지 String형으로 우선 저장
msg = "\nServer Connection\n";	
//메시지를 byte로 변환
bytes = msg.getBytes();
//byte로 변환된 메시지를 클라이언트에 전송		
sendMsg.write(bytes);
```

**receive()** 메소드는 매개변수값으로 inputStream과 Socket을 받는다.
Socket클래스를 받는 이유는 Socket.getRemoteSocketAddress() 메소드를 이용하여 콘솔창에 클라이언트의 주소를 출력하기 위해서다.
```java
//바이트배열의 크기는 미리 선언된 상수의 크기로 고정된다.
byte[] bytes = new byte[BUFFER_SIZE];
//클라이언트에서 들어 온 값을 byte크기만큼만 읽는다.
receiveMsg.read(bytes, 0, bytes.length);
//byte배열을 String으로 변환한다.
String msg = new String(bytes);
```

## 3. CLinet Socket Class

클라이언트는 byte배열의 크기를 미리 상수로 고정한다. 
```java 
private final static int BUFFER_SIZE = 1024;
```

클라이언트의 메소드는 
1. 클라이언트를 동작하게하는 ```void run()```
2. 서버에 메시지를 전달하는 ```boolean send(OutputStream sendMsg, Socket client)```
3. 서버에서 메시지를 받는 ```void receive(InputStream receiveMsg);```

**run()** 메소드는 Socket클래스를 생성하고 입력받은 IP주소로 서버와 연결에 시도한다.

**send()** 메소드는 매개변수값으로 OutputStream과 Socket을 받는다.
Socket클래스를 받는 이유는 Socket.getRemoteSocketAddress() 메소드를 이용하여 콘솔창에 서버의 주소를 출력하기 위해서다.
OutputStream을 통해 메시지를 전송한다.

```java
//메시지를 입력받아 String형으로 저장한다.
String msg = scan.nextLine();
//String 형으로 저장된 메시지를 byte배열로 변환한다.
byte[] bytes = msg.getBytes();
//변환된 byte를 서버에 전송한다.
sendMsg.write(bytes);
```

**receive()** 메소드는 매개변수값으로 inputStream을 받는다.
```java
//바이트배열의 크기는 미리 선언된 상수의 크기로 고정된다.
byte[] bytes = new byte[BUFFER_SIZE];
//클라이언트에서 들어 온 값을 byte크기만큼만 읽는다.
receiveMsg.read(bytes, 0, bytes.length);
//byte배열을 String으로 변환한다.
String msg = new String(bytes);
```