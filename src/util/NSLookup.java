package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {

		try {
			while (true) {
				Scanner scanner = new Scanner(System.in);
				String data = scanner.nextLine();
				
				if(data.equals("exit"))
					break;

				InetAddress[] inetAddresses = InetAddress.getAllByName(data);

				for(int i = 0; i < inetAddresses.length; i++) {
					System.out.println(inetAddresses[i]);
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}



	}

}