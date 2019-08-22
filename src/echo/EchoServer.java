package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress()
			+ ":" + PORT);

			//3.accept : 클라이언트로 부터 연결 요청 (Connect)을 기다린다.
			Socket socket = serversocket.accept();
			InetSocketAddress inetRemoteSocketAddress = 
					(InetSocketAddress)socket.getRemoteSocketAddress();
			String remoteSocketAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();

			System.out.println("[TCPServer] connected from client [" + 
					remoteSocketAddress + ":" + remoteHostPort + "]");
			try {
				//4.IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				while(true) {

					//5.데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); //blocking
					if(readByteCount == -1) {
						//정상종료 : remote socket이 close()
						//		메소드를 통해서 정상적으로 소켓을 닫은 경우
						System.out.println("[TCPServer] closed by client");
						break;
					}
						
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[TCPServer] received : " + data );

					//6.데이터 쓰기
					os.write(data.getBytes("UTF-8"));

				}
			}catch(SocketException e) {
				System.out.println("[TCPServer] abnormal closed by client");

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
}

