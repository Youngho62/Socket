package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import service.ServerSocketService;

public class SocketServer implements ServerSocketService{
	// 버퍼 사이즈 설정
	private final static int BUFFER_SIZE = 1024;

	@Override
	public void run() {
		ServerSocket server;
		try{
			//서버소켓을 생성하고 주소와 포트번호를 바인딩한다.
			server = new ServerSocket();
			InetSocketAddress address = new InetSocketAddress("0.0.0.0",9000);
			server.bind(address);

			// 서버를 시작하자마자 콘솔에 나타나는 메시지
			System.out.println("==================");
			System.out.println("연결 대기중");
			System.out.println("==================");


			while (true) {
				//클라이언트와 서버간에 접속 시도
				Socket client = server.accept();
				//접속이 완료되면 콘솔에 메시지 출력
				System.out.println("클라이언트와 연결 [" + client.getRemoteSocketAddress().toString()+"]");

				// OutputStream과 InputStream를 받는다.
				OutputStream sendMsg = client.getOutputStream();
				InputStream receiveMsg = client.getInputStream();

				//클라이언트에 메시지 전송해주는 메소드 (1=접속 성공시)
				send(1,sendMsg);

				while (true) {
					//클라이언트로부터 메시지를 수신받는 메소드, exit 문자열을 받으면 false 리턴
					if(!receive(receiveMsg, client)) {
						break;
					};

					//클라이언트에 메시지 전송해주는 메소드(2=메시지 수신 완료시)
					send(2,sendMsg);
				}
				//연결이 해제되면 콘솔에 메시지 출력
				System.out.println("클라이언트 연결 해제 =["+client.getRemoteSocketAddress().toString()+"]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//클라이언트에서 받은 바이트 메시지를 String으로 변환해 콘솔에 출력해주는 메소드
	@Override
	public boolean receive(InputStream receiveMsg, Socket client){
		byte[] bytes = new byte[BUFFER_SIZE];
		try {
			receiveMsg.read(bytes, 0, bytes.length);
			String msg = new String(bytes);
			System.out.println("["+client.getRemoteSocketAddress().toString()+"]> "+msg);
			if ("exit".equals(msg)) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	//메시지를 바이트로 변환해 클라이언트에 전송해주는 메소드
	@Override
	public void send(int num, OutputStream sendMsg){
		String msg;
		byte[] bytes;
		try {
			//클라이언트와 연결이 정상적으로 되었을 떄
			if(num==1) {
				msg = "\nServer Connection\n";	
				bytes = msg.getBytes();		
				sendMsg.write(bytes);

				//메시지 수신이 정상적으로 되었을 때
			}else if(num==2) {
				msg = "\nMessage Receive Success\n";	
				bytes = msg.getBytes();		
				sendMsg.write(bytes);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
