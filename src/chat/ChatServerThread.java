package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerThread extends Thread{

	private String nickname = null;
	private Socket socket = null;
	private List<PrintWriter> listWriters;


	public ChatServerThread(Socket socket, List<PrintWriter> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			InetSocketAddress isa = (InetSocketAddress)socket.getRemoteSocketAddress();
			ChatServer.log("연결됨 [" + isa.getAddress().getHostAddress() + isa.getPort() + "]");

			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);

			while(true) {
				String request = br.readLine();
				if(request == null) {
					doQuit(pw);
					ChatServer.log("클라이언트로 부터 연결 끊김");
					break;
				}

				String[] tokens = request.split(":");
				if("join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
				}else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}else if("quit".equals(tokens[0])) {
					doQuit(pw);
					break;
				}

			}
		}catch(SocketException e) {
			doQuit(pw);
			ChatServer.log("비정상 끊김");
		}catch(IOException e){
			doQuit(pw);
			ChatServer.log("error : " + e);
		}finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}catch(IOException e) {
				ChatServer.log("error : " + e);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////


	private void doJoin(String nickname, PrintWriter writer) {
		this.nickname = nickname;

		String data = nickname + "님이 참여하셨습니다.";
		
		broadcast(data);
		addWriter(writer);
		//ack
		writer.println("join:OK");
		writer.flush();
	}

	private void doMessage(String message) {
		String data = nickname  + ":" + message;
		broadcast( data );
	}

	private void doQuit(PrintWriter writer) {
		removeWriter(writer);
			String data = nickname + "님이 퇴장 하였습니다.";
			broadcast(data);
		
	}

	private void broadcast(String data) {
		synchronized(listWriters) {
			for(PrintWriter writer : listWriters) {
				writer.println(data);
				writer.flush();
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
