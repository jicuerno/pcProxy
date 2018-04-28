package com.group.six.data;

import java.sql.Date;

public class Linea {

	private String keyUsuario;
	private String keyTarea;
	private String elemento;
	private String url;
	private String evento;
	private Date tiempo;
	private String pcIp;

	public Linea() {
		super();
	}

	public Linea(String keyUsuario, String keyTarea, String elemento, String url, String evento, Date tiempo,
			String pcIp) {
		super();
		this.keyUsuario = keyUsuario;
		this.keyTarea = keyTarea;
		this.elemento = elemento;
		this.url = url;
		this.evento = evento;
		this.tiempo = tiempo;
		this.pcIp = pcIp;
	}

	public Linea(String keyUsuario, String keyTarea, String elemento, String url, String evento, String pcIp) {
		super();
		this.keyUsuario = keyUsuario;
		this.keyTarea = keyTarea;
		this.elemento = elemento;
		this.url = url;
		this.evento = evento;
		this.pcIp = pcIp;
	}

	public String getKeyUsuario() {
		return keyUsuario;
	}

	public void setKeyUsuario(String keyUsuario) {
		this.keyUsuario = keyUsuario;
	}

	public String getKeyTarea() {
		return keyTarea;
	}

	public void setKeyTarea(String keyTarea) {
		this.keyTarea = keyTarea;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = new Date(Long.parseLong(tiempo));
	}

	public String getPcIp() {
		return pcIp;
	}

	public void setPcIp(String pcIp) {
		this.pcIp = pcIp;
	}

	@Override
	public String toString() {
		return "Linea [keyUsuario=" + keyUsuario + ", keyTarea=" + keyTarea + ", elemento=" + elemento + ", url=" + url
				+ ", evento=" + evento + ", tiempo=" + tiempo + ", pcIp=" + pcIp + "]";
	}

}
