package com.group.six.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class DatosXml {

	private static volatile SecureRandom numberGenerator = null;
	private static final long MSB = 0x8000000000000000L;

	private String idUsuario;

	private List<Tarea> datos;

	public DatosXml() {
		this.datos = new ArrayList<>();
	}

	public DatosXml(String idUsuario) {
		super();
		this.idUsuario = idUsuario + "|" + generaUuid();
		this.datos = new ArrayList<>();
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario + "|" + generaUuid();
	}

	public List<Tarea> getDatos() {
		return datos;
	}

	public void setDatos(List<Tarea> datos) {
		this.datos = datos;
	}

	public void addDato(Tarea tarea) {
		datos.add(tarea);
	}

	private String generaUuid() {
		SecureRandom ng = numberGenerator;
		if (ng == null)
			numberGenerator = ng = new SecureRandom();
		return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
	}
}
