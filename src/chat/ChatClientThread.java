package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatClientThread extends Thread{

	private Socket socket;
	
	public ChatClientThread(Socket socket) {
		this.socket = socket;
	}
	
	
	@Override
	public void run() {

		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			while( true ) {
				String message = br.readLine();
				if( message == null ) {
					break;
				}
				System.out.println( message );
			}
			
		} catch (UnsupportedEncodingException e) {
			ChatClient.log("UnsupportedEncodingException" +e);
		} catch (IOException e) {
			ChatClient.log("IOException" +e);
		}finally {
			System.exit(0);
		}
		
		
	}

	
	
}
