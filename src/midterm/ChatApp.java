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
import java.net.SocketException;

import blackjack.message.ChatMessage;
import blackjack.message.LoginMessage;
import blackjack.message.Message;
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
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String str;
		ChatWindow window;
		String ipAddress = "52.35.72.251";
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
		
		// login
		LoginMessage login = MessageFactory.getLoginMessage("Ethan1");
		outToServer.writeObject(login);
		outToServer.flush();
		window = new ChatWindow(socket1, outToServer, inFromServer);
		serverMessage = (Message) inFromServer.readObject();
		if (serverMessage.getType() == Message.MessageType.ACK) {
			window.addText("Login Successful");
		} else {
			window.addText("Login Failed");
		}
		while (true) {
			try {
				serverMessage = (Message) inFromServer.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			if (serverMessage.getType() == Message.MessageType.CHAT) {
//				window.addText(((ChatMessage) serverMessage).getUsername() + ": " + ((ChatMessage) serverMessage).getText());
//			}
			switch (serverMessage.getType()) {
			case CHAT: 
				window.addText(((ChatMessage) serverMessage).getUsername() + ": " + ((ChatMessage) serverMessage).getText());
			
			}
		}
//		new Thread(new MessageHandler(inFromServer, window)).start();

	}
}
