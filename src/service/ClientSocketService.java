package service;

//SocketService를 상속 받는 ClientSocketService 인터페이스
public interface ClientSocketService extends SocketService{
	public void receive();
	public boolean send();
}
