package service;
/**
 * 
 * <pre>
 * Class Name : SocketClientService
 * Description : SocketService를 상속 받는 ClientSocketService 인터페이스
 * Supplements : Created in 2020. 8. 21
 *
 * Modification Information
 *
 * Date          By               Description
 * ------------- -----------      ----------------------------------------------
 * 2020. 8. 21  Yeongho        First Commit.
 *
 * @since 2020
 * @version v1.0
 * @author Yeongho
 *
 * Copyright (c) ABrain.  All rights reserved.
 * </pre>
 */
public interface SocketClientService extends SocketService{
	public void receive();
	public boolean send(int num);
}
