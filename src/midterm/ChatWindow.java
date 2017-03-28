/**
 * Chat
 * @author Ethan Brown
 * CS 3230
 * Feb 1, 2017
 */
package midterm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import blackjack.message.MessageFactory;

/**
 * @author Ethan
 *
 */
public class ChatWindow extends JFrame {

	private static final long serialVersionUID = -4248589752453598110L;

	private JTextArea chatArea;
	private JTextArea chatInputArea;
	private ObjectOutputStream output;
	private Socket socket1;
	private ObjectInputStream input;

	/**
	 * @throws HeadlessException
	 * @throws IOException
	 */
	public ChatWindow(Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
			throws HeadlessException, IOException {
		output = oos;
		input = ois;
		socket1 = socket;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(500, 400));
		this.setResizable(true);

		// center panel
		JPanel contentPane = new JPanel(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		JScrollPane chatScroll = new JScrollPane(chatArea);
		chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(chatScroll, "Center");

		// south panel
		JPanel inputPanel = new JPanel(new FlowLayout());
		chatInputArea = new JTextArea(3, 25);
		chatInputArea.setEditable(true);
		chatInputArea.setLineWrap(true);
		chatInputArea.setWrapStyleWord(true);
		JScrollPane inputScroll = new JScrollPane(chatInputArea);
		inputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		inputPanel.add(inputScroll);
		chatInputArea.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == KeyEvent.CTRL_MASK) {
					try {
						sendText();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		// send button setup
		JButton send = new JButton("Send");
		inputPanel.add(send);
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendText();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		contentPane.add(inputPanel, "South");
		getContentPane().add(contentPane);
		contentPane.setVisible(true);
		setVisible(true);
		chatInputArea.requestFocusInWindow();

	}

	/**
	 * transfers text from input box to chat pane. will eventually send text
	 * through the chat client.
	 * 
	 * @throws IOException
	 */
	private void sendText() throws IOException {
		String chat;
		chat = chatInputArea.getText();
		switch (chat) {
		case "quit": {
			logout();
			return;
		}
		case "JOIN": {
			output.writeObject(MessageFactory.getJoinMessage());
			output.flush();
			break;
		}
		case "START": {
			output.writeObject(MessageFactory.getStartMessage());
			output.flush();
			break;
		} }
		chatInputArea.setText("");
		output.writeObject(MessageFactory.getChatMessage(chat));
		output.flush();
	}

	public void addText(String chat) {
		chatArea.append(chat + "\n");
		chatArea.setCaretPosition(chatArea.getDocument().getLength());

	}

	private void logout() {
		try {
			output.close();
			input.close();
			socket1.close();
			addText("logout successful");
			chatInputArea.setText("");
		} catch (IOException e) {
			System.err.println("couldn't close socket");
			e.printStackTrace();
		}
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// new ChatWindow();
	}

}
