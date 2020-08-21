package socket;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import object.DataType;
import object.FileObj;
import object.Header;
import object.User;
import service.SocketClientService;
/**
 * 
 * <pre>
 * Class Name : SocketClient
 * Description : 클라이언트를 실행시키는 클래스
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
public class SocketClient implements SocketClientService{
	private Socket client;
	private InetSocketAddress address;
	private OutputStream sendMsg;
	private InputStream receiveMsg;


	@Override
	public void run() {
		//연결할 서버 IP주소와 포트변호를 입력받는다.
		System.out.print("IP주소: ");
		Scanner scan = new Scanner(System.in);
		String hostname = scan.nextLine();
		System.out.print("포트번호: ");
		int port = scan.nextInt();

		try {
			//입력받은 IP와 포트번호를 소켓에 넣고 서버와 연결에 시도한다.
			client = new Socket();
			address = new InetSocketAddress(hostname, port);
			client.connect(address);
			try {
				// OutputStream과 InputStream를 받는다.
				sendMsg = client.getOutputStream(); 
				receiveMsg = client.getInputStream();

				// 서버와 연결에 성공하면 콘솔에 메시지 출력
				System.out.println("서버 연결 성공 =" + client.getRemoteSocketAddress().toString());
				while(true) {
					//서버에서 받은 바이트 메시지를 String으로 변환해 콘솔에 출력해주는 메소드
					//receive();

					while(true) {
						System.out.print("전송할 데이터 타입[1.문자열, 2.(Object)객체, 3.파일]>");
						int num = scan.nextInt();
						//send(num);
						send2();
						//receive();
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
	public void receive() {
		try {
			byte[] bytes = new byte[HEADER_SIZE];
			receiveMsg.read(bytes, 0, bytes.length);
			String msg = new String(bytes);
			System.out.println(msg);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
public void send2() throws IOException, InterruptedException {
	System.out.println("1");
    PrintWriter out = new PrintWriter(client.getOutputStream(),true);
    System.out.println("2");
    File file = new File("C:\\Users\\rladu\\Downloads\\vision-platform-master.zip");
    System.out.println("3");
    //send file length
    out.println(file.length());
    System.out.println("4");
    //read file to buffer
    byte[] buffer = new byte[(int)file.length()];
    System.out.println("5");
    DataInputStream dis = new DataInputStream(new FileInputStream(file));
    System.out.println("6");
    dis.read(buffer, 0, buffer.length);
    System.out.println("7");
    //send file
    BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
    System.out.println("8");
    bos.write(buffer);
    System.out.println("9");
    bos.flush();
    System.out.println("10");
}
	
	//FIle전송 메소드
	public boolean send(int num) {
		Scanner scan = new Scanner(System.in);
		if(num==1) {
			System.out.print("["+client.getRemoteSocketAddress().toString()+"]>");
			String msg = scan.nextLine();
			byte[] content = msg.getBytes();
			
			try {
				Header header = new Header(content.length, DataType.TYPE_STRING);
				byte[] byteHeader = convertToBytes(header);
				sendMsg.write(byteHeader);
				sendMsg.flush();
				

				sendMsg.write(content);
				sendMsg.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(num==2) {
			try { 
				List<Object> list = new ArrayList<>();
				User user = new User("yh",27,"010-1234-1234");
				FileObj file = new FileObj("fileName", 456123, "asdasdasd"); 
				list.add(user); list.add(file);

				byte[] content = convertToBytes(list); 			
				Header header = new Header(content.length, DataType.TYPE_OBJECT);
				byte[] byteHeader = convertToBytes(header);
				sendMsg.write(byteHeader);
				sendMsg.flush();

				System.out.print("["+client.getRemoteSocketAddress().toString()+"]> Press Enter");
				String msg = scan.nextLine(); 			

				sendMsg.write(content);
				sendMsg.flush();



				if ("exit".equals(msg)) { 
					return false; 
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else if(num==3) {
			//전송할 파일 폴더 경로
			System.out.print("파일경로:");
			String filePath = scan.nextLine();

			//파일 이름은 직접 입력 받음
			System.out.print("파일명:");
			String fileName = scan.nextLine();

			File file = new File(filePath+fileName);

			//전송할 파일을 담을 바이트배열
			byte[] content = new byte[FILE_BUFFER_SIZE];
			//헤더값엔 데이터 크기, 타입, 이름,확장자(파일일 경우만)이 저장됨
			Header header = new Header(file.length(),DataType.TYPE_FILE,filePath,fileName,fileName.substring(fileName.indexOf(".")+1));
			//총 전송량, 현재 보내는 값
			long totalReadBytes=0;
			int readBytes;
			try { 
				//헤더를 바이트로 변환한뒤 먼저 전송
				byte[] byteHeader = convertToBytes(header); 
				System.out.println(header.toString()+" : "+byteHeader.length);
				sendMsg.write(byteHeader);
				sendMsg.flush();
				//파일 전송을 위해 파일을 FileinputStream에 저장
				FileInputStream sendFile = new FileInputStream(file);
				//보낼 데이터가 0이 되면 전송 종료
				while ((readBytes = sendFile.read(content)) > 0) {
					//한번에 FILE_BUFFER_SIZE(=1024) 만큼 데이터 전송
					sendMsg.write(content, 0, readBytes);

					//콘솔 출력용
					totalReadBytes += readBytes;
					System.out.println("In progress: " + totalReadBytes/1024 + "/"
							+ file.length()/1024 + " KByte(s) ("
							+ (totalReadBytes * 100 / file.length()) + " %)");
				}

				System.out.println("File transfer completed.");

				System.out.print("["+client.getRemoteSocketAddress().toString()+"]> Press Enter ");
				scan = new Scanner(System.in);
				String msg = scan.nextLine(); 

				if ("exit".equals(msg)) { 
					return false; 
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	//Object를 ByteArray로 바꿔주는 함수
	private byte[] convertToBytes(Object object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			return bos.toByteArray();
		} 
	}
}