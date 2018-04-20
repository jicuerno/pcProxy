package com.group.six;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		final JFrame frame = new ConfigFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 200);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setTitle("Configuracion");
		frame.setLocation(320, 320);

	}
}
