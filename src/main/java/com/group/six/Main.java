package com.group.six;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.group.six.utils.SQLiteAccess;

public class Main {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class);
		try {
			String path = new File("").getCanonicalPath();
			File folderDB = new File(path + "/db");
			if (!folderDB.exists())
				folderDB.mkdir();
			File folderCert = new File(path + "/certs");
			if (!folderCert.exists())
				folderDB.mkdir();
			File folderDriver = new File(path + "/driver");
			if (!folderDriver.exists())
				folderDB.mkdir();
			
			File folderProper = new File(path + "/properties");
			if (!folderProper.exists())
				folderProper.mkdir();
			
			SQLiteAccess.createTables();

			final ConfigFrame frame = new ConfigFrame();
			frame.setPath(path);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setLocation(320, 320);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
