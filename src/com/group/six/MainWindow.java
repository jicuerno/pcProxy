package com.group.six;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;

public class MainWindow {

	private JFrame frmConfiguracin;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmConfiguracin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConfiguracin = new JFrame();
		frmConfiguracin.setTitle("Configuraci\u00F3n");
		frmConfiguracin.setBounds(100, 100, 450, 300);
		frmConfiguracin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmConfiguracin.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(null);
		textField.setColumns(25);
	}
}
