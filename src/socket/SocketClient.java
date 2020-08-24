package socket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import service.ISocket;
/**
 * 
 * <pre>
 * Class Name : SocketClient
 * Description : Thread로 관리
 * Supplements : Created in 2020. 8. 21
 *
 * Modification Information
 *
 * Date          By               Description
 * ------------- -----------      ----------------------------------------------
 * 2020. 8. 24  Yeongho        First Commit.
 *
 * @since 2020
 * @version v4.0
 * @author Yeongho
 *
 * Copyright (c) ABrain.  All rights reserved.
 * </pre>
 */
public class SocketClient implements ISocket{
	private Socket client;
	private InetSocketAddress address;
	Scanner scan = new Scanner(System.in);
	
	@Override
	public void run() {
		//연결할 서버 IP주소와 포트변호를 입력받는다.
		System.out.print("IP주소: ");
		
		String hostname = scan.nextLine();
		System.out.print("포트번호: ");
		int port = scan.nextInt();
		try {
			//입력받은 IP와 포트번호를 소켓에 넣고 서버와 연결에 시도한다.
			client = new Socket();
			address = new InetSocketAddress(hostname, port);
			client.connect(address);
			try {

				// 서버와 연결에 성공하면 콘솔에 메시지 출력
				System.out.println("서버 연결 성공 =" + client.getRemoteSocketAddress().toString());
				
				ReceiveThread receive = new ReceiveThread();
				receive.setClient(client);
				
				SendThread send = new SendThread();
				send.setClient(client);

				send.start();
				receive.start();
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}