package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread{
	
	private String nickname = null;
	private Socket socket = null;
	private List<PrintWriter> listWriters= new ArrayList<PrintWriter>() ;
	BufferedReader bufferedReader = null;
	PrintWriter printWriter = null;
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8), true);
			
			while(true) {
				
				String request = bufferedReader.readLine();
				
				if(request == null) {
					ChatServer.log("클라이언트로 부터 연결 끊김");
					break;
				}
				
				String[] tokens = request.split(":");
				
				if("join".equals(tokens[0])) {
					doJoin(tokens[1], printWriter);
				}else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(tokens[1], printWriter);
				}else {
					ChatServer.log("에러 : 알 수 없는 요청(" + tokens[0] + ")");
				}

			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	private void doJoin(String nickname, PrintWriter writer) {
		this.nickname = nickname;

		String data = nickname + "님이 참여하셨습니다.";
		addWriter(writer);
		
		broadcast(data);

		//ack
		//writer.println("join : OK");
		writer.flush();
	}

	private void doMessage(String message) {
		String data = nickname + message;
		broadcast( data );
	}

	private void doQuit(String nickname , PrintWriter writer) {
		removeWriter(writer);

		String data = nickname + "님이 퇴장 하였습니다.";
		broadcast(data);
	} 


	private void broadcast(String data) {
		synchronized(listWriters) {
			for(PrintWriter writer : listWriters) {
				writer.println(data);
				printWriter.flush();
			}
		}
	}

	private void addWriter(PrintWriter writer) {
		synchronized(listWriters) {
			listWriters.add(writer);
		}
	}
	
	private void removeWriter(Writer writer) {
		synchronized( listWriters ) {
			listWriters.remove( writer );
		}
	}
	

}
