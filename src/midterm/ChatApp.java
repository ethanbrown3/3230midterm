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
import java.util.ArrayList;

import blackjack.game.Card;
import blackjack.message.CardMessage;
import blackjack.message.ChatMessage;
import blackjack.message.GameStateMessage;
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
		String username = "Ethan6";
		LoginMessage login = MessageFactory.getLoginMessage(username);
		outToServer.writeObject(login);
		outToServer.flush();
		window = new ChatWindow(socket1, outToServer, inFromServer);
		serverMessage = (Message) inFromServer.readObject();
		if (serverMessage.getType() == Message.MessageType.ACK) {
			window.addText("Login Successful");
			socket1.setSoTimeout(0);
		} else {
			window.addText("Login Failed");
		}
		ArrayList<Card> hand = new ArrayList<>();
		int handTotal;
		// input handling
		while (true) {
			try {
				serverMessage = (Message) inFromServer.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			switch (serverMessage.getType()) {
			case LOGIN: {
				window.addText(serverMessage.getUsername() + ":" + "has logged in");
				break;
			}
			case DENY: {
				if (serverMessage.getUsername().equals(username)) {
					window.addText("Server DENY");
				}
				break;
			}
			case CHAT: {
				window.addText(((ChatMessage) serverMessage).getUsername() + ": " + ((ChatMessage) serverMessage).getText());
				break;
			}
			case GAME_STATE: {
				GameStateMessage gameState = (GameStateMessage) serverMessage;
				switch (gameState.getRequestedState()) {
				case START: {
					window.addText("Game Started");
					break;
				}
				case JOIN: {
					window.addText("Would you like to join the game? Type: JOIN (TIMEOUT IN 30s)");
					break;
				}
				default:
					break;
				}
				break;
			}
			case CARD: {
				CardMessage cardMessage = (CardMessage) serverMessage;
				
				if (cardMessage.getUsername().equals(username)) {
					hand.add(cardMessage.getCard());
				
					for (Card c : hand) {
						window.addText("You have: " + c.getValue() + "of " + c.getSuite());
					}
				}
				break;
			}
			default:
				window.addText("unknown message from server" + serverMessage.getType().toString());
				break;
			}
			serverMessage = null;

		}

	}
}
