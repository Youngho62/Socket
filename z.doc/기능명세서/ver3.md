# Socket기능명세서

## 1. Client Socket Class

파일을 전송 전 헤더를 보내기 위한 헤더클래스를 생성한다.
```java
//헤더를 클래스로 만들어 관리 
//각 멤버변수들의 getter, toString을 생성
public class Header implements Serializable{
	private long dataSize;
	private DataType dataType;
	
	private String filePath;
	private String fileName;
	private String fileType;
	
	//어떤 데이터를 주고 받을지 정의하는 열거형 상수
	public enum DataType {
	TYPE_STRING,TYPE_OBJECT,TYPE_FILE;
}
}
```

클라이언트의 send 메소드 내부
```java

				...

//사용자에게 입력 받은 파일경로+파일명을 File객체에 넣음
File file = new File(filePath+fileName);

//전송할 파일을 담을 바이트배열
byte[] content = new byte[FILE_BUFFER_SIZE];
//헤더값엔 데이터 크기, 타입, [경로, 이름, 확장자](파일일 경우만)이 저장됨
Header header = new Header(file.length(),DataType.TYPE_FILE,filePath,fileName,fileName.substring(fileName.indexOf(".")+1));

//헤더를 바이트로 변환한뒤 먼저 전송
byte[] byteHeader = convertToBytes(header); 

//헤더 정보 전송
sendMsg.write(byteHeader);
sendMsg.flush();

//파일 전송을 위해 파일을 FileinputStream에 저장
FileInputStream sendFile = new FileInputStream(file);

//보낼 데이터가 0이 되면 전송 종료
while ((readBytes = sendFile.read(content)) > 0) {
	//한번에 FILE_BUFFER_SIZE(=8*1024) 만큼 데이터 전송
	sendMsg.write(content, 0, readBytes);
}

				...

```

## 2. Server Socket Class

서버의 receive() 메소드 내부

```java
			...
//헤더값을 먼저 받아 데어터의 타입, 크기 등이 무엇인지 확인한다.
byte[] byteHeader = new byte[HEADER_SIZE];
receiveMsg.read(byteHeader,0,byteHeader.length);
Header header = (Header)convertFromBytes(byteHeader);

byte[] data = new byte[FILE_BUFFER_SIZE];
//전송받을 파일의 저장 경로를 지정해준다. 
FileOutputStream sendFile = new FileOutputStream(filePath+header.getFileName());

//받을 데이터가 버퍼사이즈 (8Kb) 보다 작아지면 종료
while(true) {
	readBytes = receiveMsg.read(data);
	sendFile.write(data, 0, readBytes);
	if(readBytes<FILE_BUFFER_SIZE) {
		break;
	}
			...
```