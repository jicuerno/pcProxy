package com.group.six.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scripts {

	public String clickScript() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n <!-- Insercion -->\n");
		builder.append("<script src=\"data:text/javascript;base64," + obtenerScriptBase64("jQuery") + "\"></script>\n");
		builder.append("<script src=\"data:text/javascript;base64," + obtenerScriptBase64("click") + "\"></script>\n");
		builder.append("</head>\n");
		return builder.toString();
	}

	private String obtenerScriptBase64(String name) {
		String cadena;
		FileReader f;
		StringBuilder builder = new StringBuilder();
		try {
			f = new FileReader(new File("").getAbsolutePath() + "/src/main/java/com/group/six/js/" + name + ".js");
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
