package com.group.six.utils;

import java.sql.Date;

public class LineaDatos {

	private String key;
	private String element;
	private String url;
	private String event;
	private String time;

	public LineaDatos(String key, String element, String url, String event, String time) {
		super();
		this.key = key;
		this.element = element;
		this.url = url;
		this.event = event;
		this.time = time;
	}

	public String getKey() {
		return key;
	}

	public String getElement() {
		return element;
	}

	public String getUrl() {
		return url;
	}

	public String getEvent() {
		return event;
	}

	public Date getTime() {
		return new Date(Long.parseLong(time));
	}

	@Override
	public String toString() {
		return "LineaDatos [key=" + key + ", element=" + element + ", url=" + url + ", event=" + event + ", time=" + time + "]";
	}
}
