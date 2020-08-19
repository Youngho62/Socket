package service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
//SocketService를 상속 받는 ServerSocketService 인터페이스
public interface ServerSocketService extends SocketService{
	public boolean receive(InputStream receiveMsg, Socket client);
	public void send(int num,OutputStream sendMsg);
}
