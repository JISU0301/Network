package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient {
	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 9999;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;

		try {
	
			scanner = new Scanner(System.in);
		
			socket = new Socket();
			
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8" ), true );
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			System.out.println("닉네임>>");
			String nickname = scanner.nextLine();
			pw.println("join:" + nickname);
			pw.flush();
			
			String ack = br.readLine();
			if("join:OK".equals(ack)) {
				System.out.println("입장하였습니다. 즐거운 채팅 되세요");
			}
			
			new ChatClientThread(socket).start();
		
			while( true ) {
				if(scanner.hasNextLine() == false) {
					continue;
				}
				
				String input = scanner.nextLine();

				if ("quit".equals(input)) {
					pw.println("quit:" + nickname);
					break;
				}
				if ("".equals(input) == false) {
					pw.println( "message:" + input );
				}
			}
		}catch(ConnectException e) {
			log("서버[" + SERVER_IP + ":" + SERVER_PORT + "]에 연결할 수 없습니다.");
		}catch(Exception e) {
			log("error : " + e);
		}finally { 
			try {
				if( scanner != null ) {
					scanner.close();
				}else if( socket != null && socket.isClosed() == false ){
					socket.close();
				}
			}catch(IOException e) {
				log("error : " + e);
			}
		}
	}

	public static void log(String log) {
		System.out.println("[Client Server]" + log);
		
	}
}
