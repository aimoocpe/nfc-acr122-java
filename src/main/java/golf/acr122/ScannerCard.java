package golf.acr122;

import java.io.IOException;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

@SuppressWarnings({ "restriction" })
public class ScannerCard implements Runnable{
	private CardTerminal cardTerminal;

	public ScannerCard(CardTerminal cardTerminal) {
		this.cardTerminal = cardTerminal;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				waitForCard(1000);
				while (cardTerminal.isCardPresent()) {
					Thread.sleep(500);
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public boolean waitForCard(int timeout) throws CardException, IOException, InterruptedException {
		if (cardTerminal.waitForCardPresent(timeout)) {
			Card card = cardTerminal.connect("*");
			
			System.out.println("ATR : "+CardUtils.convertBinToASCII(card.getATR().getBytes()));
			
			CardChannel channel = card.getBasicChannel();
			byte[] baReadUID = new byte[5];
			baReadUID = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00}; //FF CA 00 00 00
			System.out.println("UID: " + CardUtils.SendCommand(baReadUID, channel));
			
			return true;
		}
		else
			return false;
	}
}
