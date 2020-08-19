# Socket기능명세서

서버와 클라이언트간에 어떤 객체든 전송이 가능하게 구현

## 1. Socket Interface

ver1과 동일

## 2. Server Socket Class

receive()메소드의 일부를 수정.

```java
//ObjectInputStream을 생성해 생성자에 InputStream을 넣음
ObjectInputStream receiveObj = new ObjectInputStream(receiveMsg);
//readObject()메소드로 Object 객체를 불러들여옴
Object obj = receiveObj.readObject();
//클라이언트에게 받은 객체를 출력
System.out.println("["+client.getRemoteSocketAddress().toString()+"]> "+obj);
```

## 3. Clinet Socket Class

send()메소드의 일부를 수정.
```java
//ObjectOutputStream을 생성해 생성자에 OutputStream을 넣음
ObjectOutputStream sendObj = new ObjectOutputStream(sendMsg);	
//객체 생성	
User user = new User("yh",0,"010-1234-1234");
//writeObject()메소드로 객체를 전송 (Object타입이라 어느 객체든 전송 가능)	
sendObj.writeObject(user);
```