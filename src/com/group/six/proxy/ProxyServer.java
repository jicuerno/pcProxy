package com.group.six.proxy;

import static java.net.URLDecoder.decode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.group.six.data.Linea;
import com.group.six.data.Tarea;
import com.group.six.utils.SQLiteAccess;
import com.group.six.utils.Scripts;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class ProxyServer {

	public BrowserMobProxy server;
	public WebDriver webDriver;
	private String script;

	public ProxyServer(Integer puerto, InetAddress direccion, String user, Tarea tarea) throws Exception {

		server = new BrowserMobProxyServer();
		// server.setTrustAllServers(true);

		script = new Scripts().clickScript(user, tarea.getKeyTarea());
		System.out.println(script);

		try {
			server.addRequestFilter(new RequestFilter() {

				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					String url = messageInfo.getOriginalUrl().toLowerCase();
					if (url.contains("www.myservice.com")) {
						String messageContents = contents.getTextContents();
						
						messageContents = messageContents.replace("keyUser=", "").replace("keyTarea=", "")
								.replace("event=", "").replace("url=", "").replace("id=", "").replace("time=", "")
								.replace("pcIp=", "");
						
						String[] array = messageContents.split("&");
						try {
							String user = decode(array[0], "UTF-8");
							String tarea = decode(array[1], "UTF-8");
							String event = decode(array[2], "UTF-8");
							String uri = decode(array[3], "UTF-8");
							String elem = decode(array[4], "UTF-8");
							String time = decode(array[5], "UTF-8");
							String pcIp = decode(array[6], "UTF-8");

							Linea linea = new Linea(user,tarea, elem, uri, event, pcIp);
							linea.setTiempo(time);
							SQLiteAccess.insertLinea(linea);
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			});

			server.addResponseFilter(new ResponseFilter() {

				@Override
				public void filterResponse(HttpResponse response, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					String messageContents = contents.getTextContents();

					if (messageContents.contains("</HEAD>") || messageContents.contains("</head>")) {
						// lo injectamos
						String newContents = messageContents.replaceAll("</HEAD>", script);
						if (!newContents.contains("//custom Insert"))
							newContents = messageContents.replaceAll("</head>", script);
						// lo metemos otra vez
						contents.setTextContents(newContents);
						System.out.println("#--> recuperado: " + newContents);
					}
				}
			});

	

			server.start(puerto, direccion);
			System.out.println("PROXY iniciado:" + direccion.getHostAddress() + ":8080");
			server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			setProfileFirefox(puerto, direccion.getHostAddress(), tarea);

		} catch (Exception e) {
			server.stop();
			throw new Exception();
		}
	}

	private void setProfileFirefox(int port, String ip, Tarea tarea) {

		webDriver = null;

		try {
			String path = new File("").getAbsolutePath();
			String proxyInfo = ip + ":" + port;
			Proxy proxy = new Proxy();
			proxy.setProxyType(Proxy.ProxyType.MANUAL);
			proxy.setHttpProxy(proxyInfo);
			// proxy.setSslProxy(proxyInfo);
			proxy.setNoProxy(null);

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--ignore-certificate-errors");
			options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
			options.setAcceptInsecureCerts(true);
			options.addPreference("acceptSslCerts", true);
			options.addPreference("enableNativeEvents", true);
			options.setProxy(proxy);
			options.setCapability(CapabilityType.PROXY, proxy);
			options.setCapability("marionette", true);
			path += "/driver/geckodriver";
			if (SystemUtils.IS_OS_WINDOWS)
				path += ".exe";
			System.setProperty("webdriver.gecko.driver", path);
			webDriver = new FirefoxDriver(options);
			webDriver.get(tarea.getUrlInicio());

		} catch (Exception e) {
			e.printStackTrace();
			webDriver.quit();
		}
	}
}
