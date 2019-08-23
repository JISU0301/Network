package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static String SERVER_IP = "192.168.1.7";
	private static int SERVER_PORT = 8000;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		
		try {
			//1.스캐너 생성 (표준입력, 키보드연결)
			scanner = new Scanner(System.in);

			//2. 소켓생성
			socket = new Socket();

			//3. 서버연결
			InetSocketAddress inetSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);

			socket.connect(inetSocketAddress);

			log("connected");

			//4. IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);


			/*
			 * //5.쓰기
			 *  String data = "안녕하세요\n"; 
			 * os.write(data.getBytes("UTF-8"));
			 */
			while(true) {
				//5.키보드 입력받기
				System.out.println(">>");
				String line = scanner.nextLine();


				if(line.equals("exit")) {
					break;
				}

				pw.println(line);

				//6.읽기

				String data = br.readLine();
				if(data == null) {
					//정상종료 : remote socket이 close()
					//		메소드를 통해서 정상적으로 소켓을 닫은 경우
					log("closed by client");
					return;
				}

				
				log(" received : " + data);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(scanner != null && socket.isClosed() == false) 
					socket.close();

			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}
	public static void log(String log) {
		System.out.println("[Echo Server]" + log);
	}
}

