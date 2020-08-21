package service;
/**
 * 
 * <pre>
 * Class Name : SocketServerService
 * Description : SocketService를 상속 받는 ServerSocketService 인터페이스
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

public interface SocketServerService extends SocketService{
	public boolean receive();
	public void send(int num);
}
