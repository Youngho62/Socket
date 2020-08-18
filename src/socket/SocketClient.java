package socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import service.ClientSocketService;

public class SocketClient implements ClientSocketService{
	// 버퍼 사이즈 설정
	private final static int BUFFER_SIZE = 1024;

	@Override
	public void run() {
		//연결할 서버 IP주소와 포트변호를 입력받는다.
		Scanner scan = new Scanner(System.in);
		System.out.print("IP주소: ");
		String hostname = scan.nextLine();

		try {
			//입력받은 IP와 포트번호를 소켓에 넣고 서버와 연결에 시도한다.
			Socket client = new Socket();
			InetSocketAddress address = new InetSocketAddress(hostname, 9000);
			client.connect(address);
			try {
				// OutputStream과 InputStream를 받는다.
				OutputStream sendMsg = client.getOutputStream(); 
				InputStream receiveMsg = client.getInputStream();

				// 서버와 연결에 성공하면 콘솔에 메시지 출력
				System.out.println("서버 연결 성공 =" + client.getRemoteSocketAddress().toString());
				while(true) {
					//서버에서 받은 바이트 메시지를 String으로 변환해 콘솔에 출력해주는 메소드
					receive(receiveMsg);
					//메시지를 바이트로 변환해 서버에 전송해주는 메소드, exit 문자열을 보내면 false 리턴
					if(!send(sendMsg, client)) {
						break;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//서버에서 받은 바이트 메시지를 String으로 변환해 콘솔에 출력해주는 메소드
	@Override
	public void receive(InputStream receiveMsg) {
			try {
				byte[] bytes = new byte[BUFFER_SIZE];
				receiveMsg.read(bytes, 0, bytes.length);
				String msg = new String(bytes);
				System.out.println(msg);

			} catch (Throwable e) {
				e.printStackTrace();
			}
	}

	//메시지를 바이트로 변환해 서버에 전송해주는 메소드
	@Override
	public boolean send(OutputStream sendMsg, Socket client) {
		try {
			Scanner scan = new Scanner(System.in);
			System.out.print("["+client.getRemoteSocketAddress().toString()+"]> ");
			String msg = scan.nextLine();
			byte[] bytes = msg.getBytes();
			sendMsg.write(bytes);
			sendMsg.flush();
			if ("exit".equals(msg)) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}