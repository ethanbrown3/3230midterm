/**
 * Chat
 * @author Ethan Brown
 * CS 3230
 * Jan 24, 2017
 */
package midterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import blackjack.message.LoginMessage;
import blackjack.message.MessageFactory;

/**
 * The Class ChatApp.
 *
 * @author Ethan
 */
public class ChatApp {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		//String ipAddress = JOptionPane.showInputDialog("Enter Server IP Address");
		String ipAddress = "137.190.250.174";
		// socket setup
		final int PORT_NUMBER = 8989;
		String str;
		Socket socket1 = new Socket();
		ChatWindow window;
		BufferedReader input;
	
		try {
			socket1.connect(new InetSocketAddress(ipAddress, PORT_NUMBER), 8000);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		ObjectOutputStream outToServer = new ObjectOutputStream(socket1.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(socket1.getInputStream());
		LoginMessage login = MessageFactory.getLoginMessage("Ethan");
		

		System.out.println(InetAddress.getLocalHost());
		outToServer.writeObject(login);
		
		try {
			System.out.println(inFromServer.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		while (true) {
//			str = input.readLine();
//			if (str.equals("ACK")) {
//				window.addText("Username Accepted");
//				break;
//			} else {
//				window.addText("Invalid Username, Try again.");
//			}
//			
//		}
//
//		// handle server communication
//		while ((str = input.readLine()) != null) {
//			window.addText(str);
//		}

	}

}
