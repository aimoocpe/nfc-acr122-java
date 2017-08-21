package golf.acr122;

import java.io.IOException;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;


@SuppressWarnings("restriction")
public class TestCard {
	
	public static void main(String[] args) throws CardException, IOException{
		CardTerminal terminal = CardUtils.getTerminalByName("ACR122");
		Thread pollingThread = new Thread(new ScannerCard(terminal));
		pollingThread.setDaemon(false);
		pollingThread.start();
		System.out.println("NFC is ready...  Press ENTER to exit");
		
		System.out.println("=======================");
        System.in.read();
		if (pollingThread != null && pollingThread.isAlive()) {
			pollingThread.interrupt();
			System.out.println("NFC closed...");
		}
	}
}
