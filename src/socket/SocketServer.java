package socket;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import object.DataType;
import object.Header;
import service.SocketServerService;
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
public class SocketServer implements SocketServerService{


	private ServerSocket server;
	private Socket client;
	private InetSocketAddress address;
	private OutputStream sendMsg;
	private InputStream receiveMsg;

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


			while (true) {
				//클라이언트와 서버간에 접속 시도
				client = server.accept();
				//접속이 완료되면 콘솔에 메시지 출력
				System.out.println("클라이언트와 연결 [" + client.getRemoteSocketAddress().toString()+"]");

				// OutputStream과 InputStream를 받는다.
				sendMsg = client.getOutputStream();
				receiveMsg = client.getInputStream();

				//클라이언트에 메시지 전송해주는 메소드 (1=접속 성공시)
				send(1);
				while (true) {
					//클라이언트로부터 메시지를 수신받는 메소드, exit 문자열을 받으면 false 리턴

					if(!receive()) {
						break;
					};

					//클라이언트에 메시지 전송해주는 메소드(2=메시지 수신 완료시)
					send(2);
				}
				//연결이 해제되면 콘솔에 메시지 출력
				System.out.println("클라이언트 연결 해제 =["+client.getRemoteSocketAddress().toString()+"]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//메시지를 바이트로 변환해 클라이언트에 전송해주는 메소드
	@Override
	public void send(int num){
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
	
	//클라이언트에서 받은 바이트 메시지를 String으로 변환해 콘솔에 출력해주는 메소드
	//보낼 데이터에 길이를 추가해서 한번에 보내는 메소드
	public boolean receive(){	
		byte[] byteHeader = new byte[HEADER_SIZE];

		try {
			//먼저 헤더의 값을 가져옴
			receiveMsg.read(byteHeader,0,byteHeader.length);
			Header header = (Header)convertFromBytes(byteHeader);
			System.out.println(header);
			//데이터 타입이 String인 경우
			if(header.getDataType()==DataType.TYPE_STRING) {
				System.out.println("String 데이터");
				
				byte[] data = new byte[(int)header.getDataSize()];
				receiveMsg.read(data,0,data.length);

				String msg = new String(data);
				System.out.println("["+client.getRemoteSocketAddress().toString()+"]< "+msg);
				
				//데이터 타입이 객체(Object)인 경우
			}else if(header.getDataType()==DataType.TYPE_OBJECT) {
				System.out.println("Object 데이터");
				byte[] data = new byte[(int)header.getDataSize()];
				receiveMsg.read(data,0,data.length);
				Object obj = convertFromBytes(data);
				
				System.out.println("["+client.getRemoteSocketAddress().toString()+"]< "+obj);

				//데이터 타입이 파일인 경우
			}else if(header.getDataType()==DataType.TYPE_FILE) {
				
				byte[] data = new byte[FILE_BUFFER_SIZE];	
				System.out.println("File 데이터");
				//빈 파일을 먼저 생성
				FileOutputStream sendFile = new FileOutputStream("D:\\down\\"+header.getFileName());
				int readBytes;
				long total= 0;
				
				//생성된 빈 파일에 데이터를 전송 시킴
				while(true) {
					readBytes = receiveMsg.read(data);
					sendFile.write(data, 0, readBytes);
					total+=readBytes;
					System.out.println("현재까지받은KB:"+(total/1024)+"["+(total/1024)+"/"+(header.getDataSize()/1024)+" ]"+((long)(total*100)/header.getDataSize())+"%");
					if(readBytes<FILE_BUFFER_SIZE) {
						break;
					}
				}
				System.out.println("["+client.getRemoteSocketAddress().toString()+"]< "+header.getFileName()+" Transfer Success!");
			}
		} 
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace(); 
		}
		return true;
	}
	
	private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		} 
	}
}
