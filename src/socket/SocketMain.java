package socket;

import java.util.Scanner;

public class SocketMain{
	public static void main(String[] args) {
		int num = choiceSC();
		//서버로 실행
		if(num==1) {
			SocketServer server = new SocketServer();
			server.run();
		//클라이언트로 실행
		}else if(num==2){
			SocketClient client = new SocketClient();
			client.run();
		}
	}
	
	//서버로 실행할 것인지, 클라이언트로 접속할 것인지 선택
	public static int choiceSC() {
		System.out.println("==============================");
		System.out.println("1.서버, 2.클라이언트");
		System.out.println("==============================");
		System.out.print("->");
		Scanner scan = new Scanner(System.in); 
		int num = scan.nextInt();
		return num;
	}
}
