package com.avalia.client;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		Client local = new Client("127.0.0.1");
		local.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		local.startRunning();
	}

}
