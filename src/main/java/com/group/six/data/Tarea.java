package com.group.six.data;

public class Tarea {

	String keyTarea;
	String instrucciones;
	String urlInicio;
	String urlFinal;
	String tiempo;

	public Tarea() {
		super();
	}

	public Tarea(String keyTarea, String instrucciones, String urlInicio, String urlFinal, String tiempo) {
		super();
		this.keyTarea = keyTarea;
		this.instrucciones = instrucciones;
		this.urlInicio = urlInicio;
		this.urlFinal = urlFinal;
		this.tiempo = tiempo;
	}

	public String getKeyTarea() {
		return keyTarea;
	}

	public void setKeyTarea(String keyTarea) {
		this.keyTarea = keyTarea;
	}

	public String getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}

	public String getUrlInicio() {
		return urlInicio;
	}

	public void setUrlInicio(String urlInicio) {
		this.urlInicio = urlInicio;
	}

	public String getUrlFinal() {
		return urlFinal;
	}

	public void setUrlFinal(String urlFinal) {
		this.urlFinal = urlFinal;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	@Override
	public String toString() {
		return "Tarea [keyTarea=" + keyTarea + ", instrucciones=" + instrucciones + ", urlInicio=" + urlInicio
				+ ", urlFinal=" + urlFinal + ", tiempo=" + tiempo + "]";
	}
	
	

}
