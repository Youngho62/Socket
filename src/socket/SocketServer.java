package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import service.ISocket;
/**
 * 
 * <pre>
 * Class Name : SocketSever
 * Description : 서버를 실행시키는 클래스
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
public class SocketServer implements ISocket{


	private ServerSocket server;
	private Socket client;
	private InetSocketAddress address;

	Scanner scan = new Scanner(System.in);
	@Override
	public void run() {
		System.out.print("포트번호: ");
		int port = scan.nextInt();
		try{
			//서버소켓을 생성하고 주소와 포트번호를 바인딩한다.
			server = new ServerSocket();
			address = new InetSocketAddress("0.0.0.0",port);
			server.bind(address);

			// 서버를 시작하자마자 콘솔에 나타나는 메시지
			System.out.println("==================");
			System.out.println("연결 대기중");
			System.out.println("==================");


				//클라이언트와 서버간에 접속 시도
				client = server.accept();
				//접속이 완료되면 콘솔에 메시지 출력
				System.out.println("클라이언트와 연결 [" + client.getRemoteSocketAddress().toString()+"]");

				ReceiveThread receive = new ReceiveThread();
				receive.setClient(client);
				
				SendThread send = new SendThread();
				send.setClient(client);
				
				send.start();
				receive.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
