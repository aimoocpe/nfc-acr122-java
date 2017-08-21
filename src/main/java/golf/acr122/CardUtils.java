package golf.acr122;

import java.nio.ByteBuffer;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.smartcardio.CardChannel;

@SuppressWarnings("restriction")
public class CardUtils {
	public static CardTerminal getTerminalByName(String terminalName) {
		try {
			TerminalFactory terminalFactory = TerminalFactory.getDefault();

			List<CardTerminal> terminals = terminalFactory.terminals().list();
			for (CardTerminal terminal : terminals) {
				if (terminal.getName().contains(terminalName)) {
					return terminal;
				}
			}
		}
		catch (CardException e) {
			throw new RuntimeException(e);
		}

		throw new IllegalArgumentException("no card terminal found, expected: [" + terminalName + "], available: ["
				+ getAvailableTerminals() + "]");

	}
	
	private static String getAvailableTerminals() {
		StringBuilder sb = new StringBuilder();
		TerminalFactory terminalFactory = TerminalFactory.getDefault();

		try {
			List<CardTerminal> terminals = terminalFactory.terminals().list();
			for (CardTerminal terminal : terminals) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				sb.append(terminal.getName());
			}
		}
		catch (CardException e) {
		}
		return sb.toString();
	}
	
	public static String convertBinToASCII(byte[] bin) {
		return convertBinToASCII(bin, 0, bin.length);
	}

	public static String convertBinToASCII(byte[] bin, int offset, int length) {
		StringBuilder sb = new StringBuilder();
		for (int x = offset; x < offset + length; x++) {
			String s = Integer.toHexString(bin[x]);

			if (s.length() == 1)
				sb.append('0');
			else
				s = s.substring(s.length() - 2);
			sb.append(s);
		}
		return sb.toString().toUpperCase();
	}
	
	public static String SendCommand(byte[] cmd, CardChannel channel) 
	 { 
	  String response = ""; 
	  byte[] baResp = new byte[258]; 
	   
	  ByteBuffer bufCmd = ByteBuffer.wrap(cmd); 
	  ByteBuffer bufResp = ByteBuffer.wrap(baResp); 
	   
	  int output = 0; 
	   
	  try{ 
	   output = channel.transmit(bufCmd, bufResp); 
	  } 
	  catch(CardException ex){ 
	   ex.printStackTrace(); 
	  } 
	   
	  for (int i = 0; i < output; i++) { 
	   response += String.format("%02X", baResp[i]); 
	  } 
	  return response;  
	 } 
}
