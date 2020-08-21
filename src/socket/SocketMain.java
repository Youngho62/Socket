package socket;
import java.util.Scanner;
/**
 * 
 * <pre>
 * Class Name : SocketMain
 * Description : 소켓프로그램을 실행시키는 메인 클래스
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
