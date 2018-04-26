package com.group.six.utils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLFile {

	private DatosXml datosXml;

	public ReadXMLFile() {
		try {
			datosXml = new DatosXml();
			File fXmlFile = new File("properties.xml");
			
			if (fXmlFile != null) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("idUsuario");
				datosXml.setIdUsuario(nList.item(0).getTextContent());	
				
				
				nList = doc.getElementsByTagName("Tarea");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					System.out.println("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element	eElement = (Element) nNode;

						Tarea tarea = new Tarea();
						tarea.setIdTarea(eElement.getElementsByTagName("idTarea").item(0).getTextContent());
						tarea.setInstrucciones(eElement.getElementsByTagName("instrucciones").item(0).getTextContent());
						tarea.setUrlInicio(eElement.getElementsByTagName("urlInicio").item(0).getTextContent());
						tarea.setUrlFinal(eElement.getElementsByTagName("urlFinal").item(0).getTextContent());
						tarea.setTiempo(eElement.getElementsByTagName("tiempo").item(0).getTextContent());
						datosXml.addDato(tarea);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DatosXml getDatosXml() {
		return datosXml;
	}

}