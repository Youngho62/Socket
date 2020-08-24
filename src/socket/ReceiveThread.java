package socket;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import object.DataType;
import object.Header;
import service.ISocketReceive;

public class ReceiveThread extends Thread implements ISocketReceive{

	private Socket client;
	private InputStream receiveMsg;

	public void setClient(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			receiveMsg = client.getInputStream();
		
			while(true) {
				receive();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receive(){	
		byte[] byteHeader = new byte[HEADER_SIZE];
		try {
			receiveMsg.read(byteHeader,0,byteHeader.length);
			Header header = (Header)convertFromBytes(byteHeader);

			//데이터 타입이 String인 경우
			if(header.getDataType()==DataType.CONNECTION_START) {
			}
			else if(header.getDataType()==DataType.TYPE_STRING) {
				System.out.println("String 데이터");

				byte[] data = new byte[(int)header.getDataSize()];
				receiveMsg.read(data,0,data.length);

				String msg = new String(data);
				System.out.println("[상대방]< "+msg);

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
					System.out.println("현재까지받은KB:["+(total/1024)+"/"+(header.getDataSize()/1024)+" ]"+((long)(total*100)/header.getDataSize())+"%");
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
	}

	private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		} 
	}
}
