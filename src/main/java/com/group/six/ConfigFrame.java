package com.group.six;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import com.group.six.data.ArchivoXml;
import com.group.six.data.Datos;
import com.group.six.data.Tarea;
import com.group.six.data.Usuario;
import com.group.six.proxy.ProxyServer;
import com.group.six.utils.ReadXMLFile;
import com.group.six.utils.SQLiteAccess;
import com.group.six.utils.WebServicesUtils;

public class ConfigFrame extends JFrame {

	private static Logger logger = Logger.getLogger(ConfigFrame.class);
	private static final long serialVersionUID = -7147860617586130063L;
	private JTextField tfPort;
	private JTextField tfIp;
	private JTextField tfEdad;
	private JTextField spinner;
	private JTextField ipSender;
	private JTextArea lbMesagge;
	private JButton btnInit;
	private JButton btnUpload;
	private JButton btnClose;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private Integer port;
	private InetAddress direccion;
	private ProxyServer servidor;
	private Timer timer;

	private Integer tiempo;

	private String path;

	private Integer cont;

	private ArchivoXml datosXml;

	public ConfigFrame() {
		this.setTitle("Formulario Inicial");
		this.setResizable(false);
		this.setBounds(100, 100, 650, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

		lbMesagge = new JTextArea();
		lbMesagge.setBounds(30, 60, 606, 60);
		lbMesagge.setBorder(border);
		lbMesagge.setEditable(false);

		JScrollPane scroll = new JScrollPane(lbMesagge);
		scroll.setBounds(30, 60, 606, 60);
		this.getContentPane().add(scroll);

		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setBounds(30, 137, 61, 16);
		this.getContentPane().add(lblEdad);

		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(366, 137, 61, 16);
		this.getContentPane().add(lblSexo);

		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setBounds(30, 175, 61, 16);
		this.getContentPane().add(lblPuerto);

		JLabel lblIp = new JLabel("Servidor Ip:");
		lblIp.setBounds(30, 214, 91, 16);
		this.getContentPane().add(lblIp);

		tfEdad = new JTextField();
		tfEdad.setBounds(123, 133, 78, 26);
		this.getContentPane().add(tfEdad);
		tfEdad.setColumns(10);

		tfPort = new JTextField();
		tfPort.setBounds(123, 171, 78, 26);
		this.getContentPane().add(tfPort);
		tfPort.setColumns(5);

		tfIp = new JTextField();
		tfIp.setBounds(123, 209, 178, 26);
		this.getContentPane().add(tfIp);
		tfIp.setColumns(15);

		JRadioButton rdbtnMasculino = new JRadioButton("Masculino");
		rdbtnMasculino.setSelected(true);
		buttonGroup.add(rdbtnMasculino);
		rdbtnMasculino.setBounds(421, 137, 141, 23);
		this.getContentPane().add(rdbtnMasculino);

		JRadioButton rdbtnFemenino = new JRadioButton("Femenino");
		buttonGroup.add(rdbtnFemenino);
		rdbtnFemenino.setBounds(421, 168, 141, 23);
		this.getContentPane().add(rdbtnFemenino);

		btnInit = new JButton("Iniciar");
		btnInit.setBounds(30, 277, 117, 29);
		this.getContentPane().add(btnInit);

		btnUpload = new JButton("Enviar");
		btnUpload.setBounds(30, 330, 117, 29);
		this.getContentPane().add(btnUpload);

		btnClose = new JButton("Cerrar");
		btnClose.setBounds(159, 277, 267, 29);
		this.getContentPane().add(btnClose);

		JLabel lblTiempo = new JLabel("Tiempo restante:");
		lblTiempo.setBounds(319, 213, 149, 16);
		getContentPane().add(lblTiempo);

		JLabel label = new JLabel("Mensajes:");
		label.setBounds(30, 31, 100, 16);
		getContentPane().add(label);

		spinner = new JTextField("0");
		spinner.setBounds(462, 207, 100, 29);
		this.getContentPane().add(spinner);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(120, 62, 1, 15);
		getContentPane().add(textArea);

		ipSender = new JTextField();
		ipSender.setColumns(15);
		ipSender.setBounds(249, 331, 178, 26);
		getContentPane().add(ipSender);

		JLabel lblIpDelWs = new JLabel("Ip del WS:");
		lblIpDelWs.setBounds(176, 336, 61, 16);
		getContentPane().add(lblIpDelWs);

		btnInit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tfEdad.getText().equals("")) {
					lbMesagge.setText(lbMesagge.getText().toString() + "\nerror: introduce una Edad valida");
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
					lbMesagge.setText(lbMesagge.getText().toString() + "\nerror:" + e1.getMessage());
					logger.error(e1.getMessage());
				}

				datosXml = new ReadXMLFile(path).getDatosXml();
				Usuario user = new Usuario(datosXml.getIdUsuario(), Integer.parseInt(tfEdad.getText()), getSelectedButtonText(buttonGroup));

				SQLiteAccess.insertUsuario(user);
				new Thread(ejecutaProxys()).start();
				btnUpload.setEnabled(true);
			}
		});

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				dispose();
			}
		});

		btnUpload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnClose.setEnabled(false);
				btnInit.setEnabled(false);
				WebServicesUtils ws = new WebServicesUtils(ipSender.getText().toString());
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
							logger.error(ex.getMessage());
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
							logger.error(ex.getMessage());
						}
					}

					if ("isOk".equals(isOk)) {
						SQLiteAccess.borrarLineas();
						System.out.println("Todo Ok");
						lbMesagge.setText(lbMesagge.getText().toString() + "\nResulado del envío: Ok");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					logger.error(e1.getMessage());
					lbMesagge.setText(lbMesagge.getText().toString() + "\nError: " + e1.getMessage());
				}
				btnClose.setEnabled(true);
				btnInit.setEnabled(true);
			}
		});

		this.setVisible(true);
	}

	private Timer initTimer() {
		return new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tiempo -= 1000;
				if (tiempo <= 0 || servidor.isFin()) {
					((Timer) e.getSource()).stop();
					spinner.setText("0");
					setTareaInProxy();
				} else {
					Integer res = tiempo / 1000;
					spinner.setText(res.toString());
				}
			}
		});
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

	private void close() {
		String path = new File("").getAbsolutePath();
		File file = new File(path + "/certs/private-key.pem");
		if (file.exists())
			file.delete();
		file = new File(path + "/certs/keystore.p12");
		if (file.exists())
			file.delete();
		file = new File(path + "/certs/certificate.cer");
		if (file.exists())
			file.delete();
	}

	private void setTextPort() {
		tfPort.setText(port.toString());
		tfPort.setVisible(true);
	}

	private void setTextIp() {
		tfIp.setText(direccion.getHostAddress());
		tfIp.setVisible(true);
	}

	private void setTextMessage(String message) {
		lbMesagge.setText(message);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private Runnable ejecutaProxys() {

		return ((Runnable) new Runnable() {
			public void run() {
				try {
					cont = 0;
					servidor = new ProxyServer();
					servidor.setPath(path);
					servidor.init(port, direccion);
					lbMesagge.setText(lbMesagge.getText().toString() + "\nIniciado en :" + direccion.getHostAddress() + " : " + port);
					setTareaInProxy();
				} catch (Exception ex) {
					logger.error(ex.getMessage());
					lbMesagge.setText(lbMesagge.getText().toString() + "\nerror:" + ex.getMessage());
				}
			}
		});
	}

	private void setTareaInProxy() {
		if (cont < datosXml.getDatos().size()) {
			Tarea tarea = datosXml.getDatos().get(cont++);
			tiempo = Integer.parseInt(tarea.getTiempo()) * 1000;
			timer = initTimer();
			SQLiteAccess.insertTarea(tarea);
			setTextMessage(tarea.getInstrucciones());
			servidor.setUser(datosXml.getIdUsuario());
			servidor.setTarea(tarea);
			servidor.saltoTarea(tarea);
			servidor.setFin(false);
			timer.start();
		} else {
			servidor.close();
		}
	}
}
 