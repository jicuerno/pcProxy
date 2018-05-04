package com.group.six;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.Timer;

import com.group.six.data.ArchivoXml;
import com.group.six.data.Datos;
import com.group.six.data.Tarea;
import com.group.six.data.Usuario;
import com.group.six.proxy.ProxyServer;
import com.group.six.utils.ReadXMLFile;
import com.group.six.utils.SQLiteAccess;
import com.group.six.utils.WebServicesUtils;

public class ConfigFrame extends JFrame {

	private static final long serialVersionUID = -7147860617586130063L;
	private JTextField tfPort;
	private JTextField tfIp;
	private JTextField tfEdad;
	private JLabel lbMesagge;
	private JButton btnInit;
	private JButton btnUpload;
	private JButton btnClose;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private Integer port;
	private InetAddress direccion;
	private ProxyServer servidor;
	private Timer timer;

	private int x;
	private Integer tiempo;
	private boolean esperar = true;

	public ConfigFrame() {
		this.setTitle("Formulario Inicial");
		this.setResizable(false);
		this.setBounds(100, 100, 650, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		lbMesagge = new JLabel(" ");
		lbMesagge.setBounds(20, 20, 300, 16);
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

		btnUpload = new JButton("Enviar");
		btnUpload.setBounds(240, 205, 117, 29);
		this.getContentPane().add(btnUpload);

		btnClose = new JButton("Cerrar");
		btnClose.setBounds(90, 245, 267, 29);
		this.getContentPane().add(btnClose);

		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				x += 1000;
				if (x == tiempo) {
					((Timer) e.getSource()).stop();
					esperar= false;
				}
			}
		});

		SpinnerDateModel sm = new SpinnerDateModel(new Date(), null, null, Calendar.SECOND);
		JSpinner spinner = new JSpinner(sm);
		JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, "ss");
		spinner.setBounds(320, 138, 100, 29);
		spinner.setEditor(de);

		this.getContentPane().add(spinner);

		this.getContentPane();

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tfEdad.getText().equals("")) {
					lbMesagge.setText("error: introduce una Edad valida");
				} else {
					InitProxy();
				}
			}

			private void InitProxy() {
				btnUpload.setEnabled(false);

				if (!tfPort.getText().toString().equals(""))
					port = Integer.parseInt(tfPort.getText());
				else {
					port = 9090;
					setTextPort();
				}
				try {
					if (!tfIp.getText().toString().equals(""))
						direccion = InetAddress.getByName(tfIp.getText());
					else {
						direccion = InetAddress.getLocalHost();
						setTextIp();
					}
				} catch (Exception e1) {
					lbMesagge.setText("error:" + e1.getMessage());
				}

				ArchivoXml datosXml = new ReadXMLFile().getDatosXml();
				Usuario user = new Usuario(datosXml.getIdUsuario(), Integer.parseInt(tfEdad.getText()),
						getSelectedButtonText(buttonGroup));

				SQLiteAccess.insertUsuario(user);
				new Thread(ejecutaProxys(datosXml)).start();
				btnUpload.setEnabled(true);
			}
		});

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btnUpload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnClose.setEnabled(false);
				btnInit.setEnabled(false);
				WebServicesUtils ws = new WebServicesUtils();
				String isOk = "";

				try {

					Datos datos = SQLiteAccess.leerUsusarios();
					if (!datos.getUsuarios().isEmpty()) {
						try {
							isOk = ws.invocaWebServiceHttp(datos, "usuarios");
						} catch (Exception ex) {
							isOk = "";
						}
					}

					if ("isOk".equals(isOk)) {
						SQLiteAccess.borrarUsuarios();
						isOk = "";
					}

					datos = SQLiteAccess.leerTareas();

					if (!datos.getTareas().isEmpty()) {
						try {
							isOk = ws.invocaWebServiceHttp(datos, "tareas");
						} catch (Exception ex) {
							isOk = "";
						}
					}

					if ("isOk".equals(isOk)) {
						SQLiteAccess.borrarTareas();
						isOk = "";
					}

					datos = SQLiteAccess.leerLineas();
					if (!datos.getLineas().isEmpty()) {
						try {
							isOk = ws.invocaWebServiceHttp(datos, "lineas");
						} catch (Exception ex) {
							isOk = "";
						}
					}

					if ("isOk".equals(isOk)) {
						SQLiteAccess.borrarLineas();
						System.out.println("Todo Ok");
						lbMesagge.setText("Resulado del envío: Ok");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					lbMesagge.setText("Error: " + e1.getMessage());
				}
				btnClose.setEnabled(true);
				btnInit.setEnabled(true);
			}
		});

		this.setVisible(true);
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

	private void setTextPort() {
		tfPort.setText(port.toString());
		tfPort.setVisible(true);
	}

	private void setTextIp() {
		tfIp.setText(direccion.getHostAddress());
		tfIp.setVisible(true);
	}

	private Runnable ejecutaProxys(ArchivoXml datosXml) {

		return ((Runnable) new Runnable() {
			public void run() {
				try {

					servidor = new ProxyServer();
					servidor.init(port, direccion);

					lbMesagge.setText("  Iniciado en :" + direccion.getHostAddress() + " : " + port);

					for (Tarea tarea : datosXml.getDatos()) {
						x = 0;
						tiempo = Integer.parseInt(tarea.getTiempo()) * 1000;
						SQLiteAccess.insertTarea(tarea);

						servidor.setUser(datosXml.getIdUsuario());
						servidor.setTarea(tarea);
						servidor.saltoTarea(tarea);
						timer.start();
						while (esperar) { /*nop;*/ }
						esperar = true;
					}
					servidor.close();
				} catch (Exception ex) {
					lbMesagge.setText("error:" + ex.getMessage());
				}
			}
		});
	}

}
