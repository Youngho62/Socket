package service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
//SocketService를 상속 받는 ClientSocketService 인터페이스
public interface ClientSocketService extends SocketService{
	public void receive(InputStream receiveMsg);
	public boolean send(OutputStream sendMsg, Socket client);
}
