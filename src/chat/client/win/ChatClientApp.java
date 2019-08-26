package chat.client.win;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.7";
	private static final int SERVER_PORT = 9999;

	public static void main(String[] args) {
		String name = null;
		Scanner scanner = null;
		Socket socket = null;

		try {
			while( true ) {
				scanner = new Scanner(System.in);
				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				name = scanner.nextLine();

				if (name.isEmpty() == false ) {
					break;
				}

				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}


			//1. create socket
			socket = new Socket();

			//2. connect to server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			//3. create iostream
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			//4. join : 둘리
			pw.println("JOIN:" + name);
			String ack = br.readLine();
			if ("JOIN:OK".equals(ack)) {
				new ChatWindow(name).show();
			}
		}catch(ConnectException e) {
			consoleLog("서버[" + SERVER_IP + ":" + SERVER_PORT + "]에 연결할 수 없습니다.");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(scanner != null) {
				scanner.close();
			}
		}

	}

	public static void consoleLog( String message ) {
		System.out.println( "\n[chat client]" + message );
	}

}
