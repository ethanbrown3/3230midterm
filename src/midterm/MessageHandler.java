package midterm;

import java.io.IOException;
import java.io.ObjectInputStream;

import blackjack.message.ChatMessage;
import blackjack.message.Message;

public class MessageHandler implements Runnable {
	private ObjectInputStream input;
	private ChatWindow window;
	private Message serverMessage;
	private ChatMessage chatMessage;
	
	public MessageHandler(ObjectInputStream ois, ChatWindow window) {
		input = ois;
		this.window = window;
	}

	@Override
	public void run() {
		try {
			serverMessage = (Message) input.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (serverMessage.getType() == Message.MessageType.CHAT) {
			chatMessage = (ChatMessage) serverMessage;
			window.addText(chatMessage.getUsername() + ": " + chatMessage.getText());
		}

	}

}
