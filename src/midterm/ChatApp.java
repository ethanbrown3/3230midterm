/**
 * Chat
 * @author Ethan Brown
 * CS 3230
 * Jan 24, 2017
 */
package midterm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import blackjack.message.LoginMessage;
import blackjack.message.Message;
import blackjack.message.MessageFactory;
import blackjack.message.StatusMessage;

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
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String str;
		ChatWindow window;
		String ipAddress = "137.190.250.174";
		// socket setup
		final int PORT_NUMBER = 8989;
		Socket socket1 = new Socket();

		try {
			socket1.connect(new InetSocketAddress(ipAddress, PORT_NUMBER), 8000);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		ObjectOutputStream outToServer = new ObjectOutputStream(socket1.getOutputStream());
		ObjectInputStream inFromServer = new ObjectInputStream(socket1.getInputStream());
		Message serverMessage;
		System.out.println(InetAddress.getLocalHost());
		window = new ChatWindow(socket1, outToServer);
		
		// login
		LoginMessage login = MessageFactory.getLoginMessage("Ethan");
		outToServer.writeObject(login);
//		serverMessage = (Message) inFromServer.readObject();
//		if (serverMessage.getType() == Message.MessageType.ACK) {
//			window.addText("Login Successful");
//		} else {
//			window.addText("Login Failed");
//		}

	}
}
