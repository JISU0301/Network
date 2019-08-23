package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 8000;

	public static void main(String[] args) {

		ServerSocket serversocket = null;

		try {
			//1.서버소켓 생성
			serversocket = new ServerSocket();

			//2.binding : socket에 SocketAddress(IPAddress + port)바인딩한다
			InetAddress inetAddress = InetAddress.getLocalHost();
			//String localhostAddress = inetAddress.getHostAddress();
			// => 생략가능
			InetSocketAddress inetsocketaddress = 
					new InetSocketAddress(inetAddress, PORT);
			serversocket.bind(inetsocketaddress);
			log("binding " + inetAddress.getHostAddress()
			+ ":" + PORT);

			//3.accept : 클라이언트로 부터 연결 요청 (Connect)을 기다린다.
			Socket socket = serversocket.accept(); //Blocking
			InetSocketAddress inetRemoteSocketAddress = 
					(InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteSocketAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();

			log("connected from client [" + 
					remoteSocketAddress + ":" + remoteHostPort + "]");
			try {
				//4. I/O Stream 생성								
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);

				while(true) {

					//5.데이터 읽기 (수신)
					String data = br.readLine();

					if(data == null) {
						//정상종료 : remote socket이 close()
						//		메소드를 통해서 정상적으로 소켓을 닫은 경우
						log("closed by client");
						break;
					}

					log("received : " + data );

					//6.데이터 쓰기 (송신)
					pw.println(data);

				}
			}catch(SocketException e) {
				log("abnormal closed by client");

			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				//8. data socket 자원정리
				if(socket != null && socket.isClosed() == false)
					socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();

		}finally {
			//9. server socket 자원정리
			try {
				if(serversocket != null && serversocket.isClosed()) 
					serversocket.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void log(String log) {
		System.out.println("[Echo Server]" + log);
	}
}

