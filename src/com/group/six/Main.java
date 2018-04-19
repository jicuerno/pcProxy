package com.group.six;

import com.group.six.utils.ProxyServer;

import net.lightbody.bmp.BrowserMobProxy;

public class Main {

	static BrowserMobProxy server;	
	
	public static void main(String[] args) {
		System.out.println("INICIANDO PROXY");
		ProxyServer proxy =  new ProxyServer();	
		server = proxy.server;
	}

}
