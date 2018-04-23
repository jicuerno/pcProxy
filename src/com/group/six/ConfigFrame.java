package com.group.six;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.openqa.selenium.WebDriver;

import com.group.six.utils.ProxyServer;

import net.lightbody.bmp.BrowserMobProxy;

public class ConfigFrame extends JFrame {

	private JTextField tfPort;
	private JTextField pfIp;
	private JLabel lbPort;
	private JLabel lbIp;
	private JLabel lbMesagge;
	private JButton btnInit;
	private JButton btnClose;

	private BrowserMobProxy proxy;
	private WebDriver webDriver;

	public ConfigFrame() {

		GridBagLayout grid = new GridBagLayout();
		JPanel panel = new JPanel(grid);
		GridBagConstraints cs = new GridBagConstraints();
		GridBagConstraints cs2 = new GridBagConstraints();
		GridBagConstraints cs3 = new GridBagConstraints();
		GridBagConstraints cs4 = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;

		lbMesagge = new JLabel(" ");

		lbPort = new JLabel("Puerto: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbPort, cs);

		tfPort = new JTextField(5);
		cs2.gridx = 1;
		cs2.gridy = 0;
		cs2.gridwidth = 2;
		panel.add(tfPort, cs2);

		lbIp = new JLabel("Servidor Ip: ");
		cs3.gridx = 0;
		cs3.gridy = 1;
		cs3.gridwidth = 1;
		panel.add(lbIp, cs3);

		pfIp = new JTextField(15);
		cs4.gridx = 1;
		cs4.gridy = 1;
		cs4.gridwidth = 2;
		panel.add(pfIp, cs4);
		panel.setBorder(new LineBorder(Color.GRAY));

		btnInit = new JButton("Iniciar");
		btnClose = new JButton("Cerrar");

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ProxyServer servidor = new ProxyServer(tfPort.getText(), pfIp.getText());
					tfPort.setText(servidor.puerto.toString());
					pfIp.setText(servidor.direccion.getHostAddress());
					lbMesagge.setText("  Iniciado en :" + servidor.direccion.getHostAddress() + " : " + servidor.puerto);
					proxy = servidor.server;
					webDriver = servidor.webDriver;
				} catch (Exception ex) {
					lbMesagge.setText("error:" + ex.getMessage());
				}
			}
		});

		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (proxy != null)
					proxy.stop();
				if (webDriver != null)
					webDriver.quit();
				dispose();
			}
		});

		JPanel bp = new JPanel();
		bp.setSize(new Dimension(200, 20));
		bp.add(btnInit);
		bp.add(btnClose);

		JPanel bp2 = new JPanel();
		bp2.setSize(new Dimension(200, 20));
		bp2.add(lbMesagge);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.CENTER);
		getContentPane().add(bp2, BorderLayout.CENTER);

		pack();
	}
}
