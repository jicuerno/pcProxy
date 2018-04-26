package com.group.six;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.UUID;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.group.six.data.DatosXml;
import com.group.six.data.LineaDatos;
import com.group.six.data.LineaUser;
import com.group.six.data.Tarea;
import com.group.six.proxy.ProxyServer;
import com.group.six.utils.MySQLAccess;
import com.group.six.utils.ReadXMLFile;

import net.lightbody.bmp.BrowserMobProxy;

public class ConfigFrame extends JFrame {


	private static final long serialVersionUID = -7147860617586130063L;
	private JTextField tfPort;
	private JTextField tfIp;
	private JTextField tfEdad;
	private JLabel lbMesagge;
	private JButton btnInit;
	private JButton btnClose;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private BrowserMobProxy proxy;
	private WebDriver webDriver;



	public ConfigFrame() {
		this.setTitle("Formulario Inicial");
		this.setResizable(false);
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		lbMesagge = new JLabel(" ");
		lbMesagge.setBounds(20, 20, 200, 16);
		this.getContentPane().add(lbMesagge);

		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setBounds(45, 50, 61, 16);
		this.getContentPane().add(lblEdad);

		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(245, 50, 61, 16);
		this.getContentPane().add(lblSexo);

		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(45, 90, 61, 16);
		this.getContentPane().add(lblPuerto);

		JLabel lblIp = new JLabel("Servidor Ip:");
		lblIp.setBounds(45, 140, 91, 16);
		this.getContentPane().add(lblIp);

		tfEdad = new JTextField();
		tfEdad.setBounds(128, 48, 78, 26);
		this.getContentPane().add(tfEdad);
		tfEdad.setColumns(10);

		tfPort = new JTextField();
		tfPort.setBounds(128, 88, 78, 26);
		this.getContentPane().add(tfPort);
		tfPort.setColumns(5);

		tfIp = new JTextField();
		tfIp.setBounds(128, 138, 178, 26);
		this.getContentPane().add(tfIp);
		tfIp.setColumns(15);

		JRadioButton rdbtnMasculino = new JRadioButton("Masculino");
		rdbtnMasculino.setSelected(true);
		buttonGroup.add(rdbtnMasculino);
		rdbtnMasculino.setBounds(300, 50, 141, 23);
		this.getContentPane().add(rdbtnMasculino);

		JRadioButton rdbtnFemenino = new JRadioButton("Femenino");
		buttonGroup.add(rdbtnFemenino);
		rdbtnFemenino.setBounds(300, 80, 141, 23);
		this.getContentPane().add(rdbtnFemenino);

		btnInit = new JButton("Iniciar");
		btnInit.setBounds(90, 205, 117, 29);
		this.getContentPane().add(btnInit);

		btnClose = new JButton("Cerrar");
		btnClose.setBounds(240, 205, 117, 29);
		this.getContentPane().add(btnClose);

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					if (tfEdad.getText().equals("")) {
						lbMesagge.setText("error: introduce una Edad valida");
					} else
						initProxys();
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
	}

	private void initProxys() throws Exception {
		DatosXml datosXml = new ReadXMLFile().getDatosXml();
		LineaUser user = new LineaUser(datosXml.getIdUsuario(), tfEdad.getText(), getSelectedButtonText(buttonGroup));
		// realizarInsercion(user);

		for (Tarea tarea : datosXml.getDatos()) {
			ProxyServer servidor = new ProxyServer(tfPort.getText(), tfIp.getText(), datosXml.getIdUsuario(),tarea);
			tfPort.setText(servidor.puerto.toString());
			tfIp.setText(servidor.direccion.getHostAddress());
			lbMesagge.setText("  Iniciado en :" + servidor.direccion.getHostAddress() + " : " + servidor.puerto);
			proxy = servidor.server;
			webDriver = servidor.webDriver;

		}
	}

	private void realizarInsercion(LineaUser linea) {
		MySQLAccess db = MySQLAccess.getSingletonInstance();
		db.insertUser(linea);
	}

	private String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}
}
