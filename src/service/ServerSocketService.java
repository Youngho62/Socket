package service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ServerSocketService extends SocketService{
	public boolean receive(InputStream receiveMsg, Socket client);
	public void send(int num,OutputStream sendMsg);
}
