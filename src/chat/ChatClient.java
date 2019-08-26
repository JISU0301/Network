package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import chat.client.win.ChatWindow;

public class ChatClient {
	private static final String SERVER_IP = "192.168.1.7";
	private static final int SERVER_PORT = 9999;

	public static void main(String[] args) {
		Socket socket = null;
		String name = null;
		Scanner scanner = null;

		try {
	
			scanner = new Scanner(System.in);
		
			socket = new Socket();
			
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8" ), true );
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			System.out.println("닉네임>>");
			String nickname = scanner.nextLine();
			pw.println("join:" + nickname);
			pw.flush();
			
			new ChatClientThread(socket).start();
		
			while( true ) {
				System.out.print(">>");
				String input = scanner.nextLine();

				if ("quit".equals(input) == true ) {
					pw.println("quit:" + nickname);
					break;
				}else {
					pw.println( "MESSAGE:" + input );
				}
			}

		}catch(IOException e) {
			log("error : " + e);
		}finally { 
			try {
				if( scanner != null ) {
					scanner.close();
				}else if( socket != null && socket.isClosed() == false ){
					socket.close();
				}
			}catch(IOException e) {
				
			}
		}
	}

	public static void log(String log) {
		System.out.println("[Server]" + log);
		
	}
}
