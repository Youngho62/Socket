package socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import object.DataType;
import object.FileObj;
import object.Header;
import object.User;
import service.ISocketSend;

public class SendThread extends Thread implements ISocketSend{
	private Socket client;
	private OutputStream sendMsg;
	
	public void setClient(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			sendMsg = client.getOutputStream();
			Scanner scan = new Scanner(System.in);
			
			Header header = new Header(DataType.CONNECTION_START);
			sendMsg.write(convertToBytes(header));
			sendMsg.flush();
			int num;
			do{
				System.out.print("전송할 데이터 타입[1.문자열, 2.(Object)객체, 3.파일, 0.종료]>");
				num = scan.nextInt();
				while(true) {
					if(!send(num)) {
						break;
					};
				}
			}while(num!=0);
			System.out.println("연결해제");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean send(int num) {
		Scanner scan = new Scanner(System.in);
		if(num==1) {
			System.out.print("[나]>");
			String msg = scan.nextLine();
			if(msg.equals("exit")) {
				return false;
			}
			byte[] content = msg.getBytes();
			
			try {
				Header header = new Header(content.length, DataType.TYPE_STRING);
				sendMsg.write(convertToBytes(header));
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
				sendMsg.write(convertToBytes(header));
				sendMsg.flush();

				System.out.print("["+client.getRemoteSocketAddress().toString()+"]> Press Enter");
				String msg = scan.nextLine(); 			
				if(msg.equals("exit")) {
					return false;
				}
				sendMsg.write(content);
				sendMsg.flush();

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
				sendMsg.write(convertToBytes(header));
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
				
				String msg = scan.nextLine(); 
				sendFile.close();
				if (msg.equals("exit")) { 
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
