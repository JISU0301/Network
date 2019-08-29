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
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 9999;
	
	public static void main(String[] args) {
		List<PrintWriter> listPrintWriter = new ArrayList<PrintWriter>();
		ServerSocket serverSocket = null;
				
		try {
			
			serverSocket = new ServerSocket();
			//서버소켓 설정.
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			log("연결 기다림 " + hostAddress + ":" + PORT);
			
			while(true) {
				//클라이언트와 연결기다림
				Socket socket = serverSocket.accept();
				log("연결됨" + socket.getRemoteSocketAddress());
				Thread thread = new ChatServerThread(socket, listPrintWriter); //확인...
				thread.start();
				
			}
						
		}catch (IOException e) {
			log("error : " + e);
		}finally {
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			}catch(IOException e) {
				log("error : " + e);
			}finally {
				try {
					if( serverSocket != null && 
						serverSocket.isClosed() == false ){
						serverSocket.close();
					}
				}catch( IOException ex ) {
					log( "error:" + ex );
				}
			}
		}
		
	}


	public static void log(String log) {
		System.out.println("[Chat Server]" + log);
		
	}

}
