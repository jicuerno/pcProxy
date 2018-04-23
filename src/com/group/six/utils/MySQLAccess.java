package com.group.six.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLAccess {
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private static MySQLAccess mySQLAccess;

	public static MySQLAccess getSingletonInstance() {
		if (mySQLAccess == null) {
			mySQLAccess = new MySQLAccess();
		}
		return mySQLAccess;
	}

	protected boolean insert(LineaDatos linea) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql:dbProxyMob?" + "user=root&password=");
			preparedStatement = connect.prepareStatement("INSERT INTO datosRequest ('key','elemento', 'url', 'evento', 'tiempo') VALUES (?,?,?,?,?)");
			preparedStatement.setString(1, linea.getKey());
			preparedStatement.setString(2, linea.getElement());
			preparedStatement.setString(3, linea.getUrl());
			preparedStatement.setString(5, linea.getEvent());
			preparedStatement.setDate(6, linea.getTime());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
