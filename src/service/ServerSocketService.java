package service;

//SocketService를 상속 받는 ServerSocketService 인터페이스
public interface ServerSocketService extends SocketService{
	public boolean receive();
	public void send(int num);
}
