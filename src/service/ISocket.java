package service;

/**
 * 
 * <pre>
 * Class Name : SocketService
 * Description : 서버와 클라이언트의 상위 인터페이스
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

public interface ISocket {
	// 버퍼 사이즈 설정
	final static int HEADER_SIZE = 1024; // 1kb
	final static int FILE_BUFFER_SIZE = 8*1024; //8kb 
	
	public void run();
}
