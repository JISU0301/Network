package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatServer {
	private static final int PORT = 9999;
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		String hostAddress;
		
		try {
			//서버소켓 설정.
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			log("연결 기다림 " + hostAddress + ":" + PORT);
			
			while(true) {
				//클라이언트와 연결기다림
				Socket socket = serverSocket.accept();
				log("연결됨" + socket.getRemoteSocketAddress());
				new ChatServerThread(socket).start();
			}
						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static void log(String log) {
		System.out.println("[Server]" + log);
		
	}

}
