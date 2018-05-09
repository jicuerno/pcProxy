package com.group.six.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.group.six.data.Datos;
import com.group.six.data.Linea;
import com.group.six.data.Tarea;
import com.group.six.data.Usuario;

public class SQLiteAccess {

	private static Connection connect = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet rs = null;
	private static Statement stmt = null;

	public static void connect() {
		try {
			String path = new File("").getCanonicalPath();
			String url = "jdbc:sqlite:" + path + "/db/internal.db";
			connect = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void disConnect() {
		try {
			if (connect != null)
				connect.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void createTables() {

		try {
			connect();
			preparedStatement = connect.prepareStatement(
					"CREATE TABLE IF NOT EXISTS usuario ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," 
							+ "keyUser TEXT NOT NULL, edad INTEGER NOT NULL, sexo TEXT NOT NULL);");
			preparedStatement.execute();

			preparedStatement = connect.prepareStatement(
					"CREATE TABLE IF NOT EXISTS tarea ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," 
							+ "keyTarea TEXT NOT NULL, instrucciones TEXT, urlInit TEXT NOT NULL,"
							+ "urlFin TEXT NOT NULL, tiempo INTEGER NOT NULL);");
			preparedStatement.execute();

			preparedStatement = connect.prepareStatement(
					"CREATE TABLE IF NOT EXISTS request ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," 
							+ "keyUser TEXT NOT NULL, keyTarea TEXT NOT NULL, element TEXT,"
							+ "url TEXT NOT NULL, event TEXT, time TEXT NOT NULL,"
							+ "pcIp TEXT NOT NULL);");
			preparedStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
	}

	public static void insertUsuario(Usuario usuario) {
		try {
			connect();
			preparedStatement = connect.prepareStatement("INSERT INTO usuario ('keyUser', 'edad', 'sexo') VALUES (?,?,?)");
			preparedStatement.setString(1, usuario.getKeyUsuario());
			preparedStatement.setInt(2, usuario.getEdad());
			preparedStatement.setString(3, usuario.getSexo());
			preparedStatement.executeUpdate();
			System.out.println("insertado: " + usuario.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
	}

	public static void insertTarea(Tarea tarea) {
		try {
			connect();
			preparedStatement = connect.prepareStatement("INSERT INTO tarea ('keyTarea', 'urlInit', 'urlFin', 'tiempo', 'instrucciones') VALUES (?,?,?,?,?)");
			preparedStatement.setString(1, tarea.getKeyTarea());
			preparedStatement.setString(2, tarea.getUrlInicio());
			preparedStatement.setString(3, tarea.getUrlFinal());
			preparedStatement.setString(4, tarea.getTiempo());
			preparedStatement.setString(5, tarea.getInstrucciones());

			preparedStatement.executeUpdate();
			System.out.println("insertado: " + tarea.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
	}

	public static void insertLinea(Linea linea) {
		try {
			connect();
			preparedStatement = connect.prepareStatement("INSERT INTO request ('keyUser', 'keyTarea', 'element', 'url', 'event', 'time', 'pcIp') VALUES (?,?,?,?,?,?,?)");
			preparedStatement.setString(1, linea.getKeyUsuario());
			preparedStatement.setString(2, linea.getKeyTarea());
			preparedStatement.setString(3, linea.getElemento());
			preparedStatement.setString(4, linea.getUrl());
			preparedStatement.setString(5, linea.getEvento());
			preparedStatement.setString(6, linea.getTiempo());
			preparedStatement.setString(7, linea.getPcIp());
			preparedStatement.executeUpdate();
			System.out.println("insertado: " + linea.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
	}

	public static Datos leerLineas() {
		Datos datos = new Datos();
		try {
			connect();
			stmt = connect.createStatement();
			rs = stmt.executeQuery("SELECT keyUser, keyTarea, element, url, event, time, pcIp FROM request;");

			while (rs.next())
				datos.getLineas().add(new Linea(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
		return datos;
	}

	public static Datos leerUsusarios() {
		Datos datos = new Datos();
		try {
			connect();
			stmt = connect.createStatement();
			rs = stmt.executeQuery("SELECT keyUser, edad, sexo  FROM usuario;");

			while (rs.next())
				datos.getUsuarios().add(new Usuario(rs.getString(1), rs.getInt(2), rs.getString(3)));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
		return datos;
	}

	public static Datos leerTareas() {
		Datos datos = new Datos();
		try {
			connect();
			stmt = connect.createStatement();
			rs = stmt.executeQuery("SELECT keyTarea, instrucciones, urlInit, urlFin, tiempo FROM tarea;");

			while (rs.next())
				datos.getTareas().add(new Tarea(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disConnect();
		}
		return datos;
	}

	public static void borrarTareas() {
		try {
			connect();
			preparedStatement = connect.prepareStatement("DELETE FROM tarea");
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	public static void borrarUsuarios() {
		try {
			connect();
			preparedStatement = connect.prepareStatement("DELETE FROM usuario");
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	public static void borrarLineas() {
		try {
			connect();
			preparedStatement = connect.prepareStatement("DELETE FROM request");
			preparedStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}
}
