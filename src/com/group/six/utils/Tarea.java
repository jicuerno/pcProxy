package com.group.six.utils;

public class Tarea {
	private String idTarea;
	private String instrucciones;
	private String urlInicio;
	private String urlFinal;
	private String tiempo;

	public String getIdTarea() {
		return idTarea;
	}

	public Tarea() {
	}

	public Tarea(String idTarea, String instrucciones, String urlInicio, String urlFinal, String tiempo) {
		super();
		this.idTarea = idTarea;
		this.instrucciones = instrucciones;
		this.urlInicio = urlInicio;
		this.urlFinal = urlFinal;
		this.tiempo = tiempo;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
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

}
