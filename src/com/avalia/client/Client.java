package com.avalia.client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Client extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	public Client(String host) {
		super("Front-end");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}

		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	public void startRunning() {
		try {
			connectTOServer();
			setupStreame();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated Connection.");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeCrap();
		}

	}

	private void connectTOServer() throws IOException {

		showMessage("Attempting to Connection....\n");
		connection = new Socket(InetAddress.getByName(serverIP), 8000);
		showMessage("Connected to : "
				+ connection.getInetAddress().getHostName());

	}

	private void setupStreame() throws IOException {

		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n your streams ready to go.");

	}

	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n I dont know that type of object.");
			}

		} while (!message.equals("Server-END"));

	}

	private void closeCrap() {
		showMessage("\n Closing crap down...!");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();

		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				chatWindow.append(message);
			}
		}

		);
	}

	private void sendMessage(String m) {
		try {
			output.writeObject("Client : " + m);
			output.flush();
			showMessage("\nClient : " + m);
		} catch (IOException exception) {
			chatWindow.append("\n something missed up..");
		}
	}

	private void ableToType(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				userText.setEditable(b);
			}
		}

		);
	}
}
