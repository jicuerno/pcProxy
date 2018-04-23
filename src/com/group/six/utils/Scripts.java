package com.group.six.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class Scripts {

	public String clickScript() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n");
		builder.append("<script src=\"data:text/javascript;base64," + obtenerScriptBase64() + "\"></script>\n");
		builder.append("</head>\n");
		return builder.toString();
	}

	private String obtenerScriptBase64() {
		String cadena;
		FileReader f;
		StringBuilder builder = new StringBuilder();
		try {
			f = new FileReader(new File("").getAbsolutePath() + "\\src\\com\\group\\six\\js\\click.js");
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				builder.append(cadena + "\n");
			}
			b.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return MyBase64.encode(builder.toString());
	}
}
