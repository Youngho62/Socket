package service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ClientSocketService extends SocketService{
	public void receive(InputStream receiveMsg);
	public boolean send(OutputStream sendMsg, Socket client);
}
