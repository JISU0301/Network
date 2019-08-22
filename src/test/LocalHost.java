package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			String hostname = inetAddress.getHostName();
			String hostaddress = inetAddress.getHostAddress();


			System.out.println(hostname);
			System.out.println(hostaddress);

			byte[] ipAddresses = inetAddress.getAddress();
			for(byte ipAddress : ipAddresses) {
				System.out.print(ipAddress & 0x000000ff);//마이너스 나오니깐 
														//부호비트 0만드는 과정
				System.out.print(".");
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
