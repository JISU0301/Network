package thread;

public class MultithreadEX02 {

	public static void main(String[] args) {
		Thread thread1 = new DigitThread();
		Thread thread2 = new AlphabetThread();
		
		thread1.start();
		thread2.start();

	}

}
