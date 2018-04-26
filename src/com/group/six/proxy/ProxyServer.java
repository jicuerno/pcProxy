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

import com.group.six.data.LineaDatos;
import com.group.six.data.Tarea;
import com.group.six.utils.MySQLAccess;
import com.group.six.utils.Scripts;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class ProxyServer {

	public BrowserMobProxy server;
	public Integer puerto;
	public InetAddress direccion;
	public WebDriver webDriver;
	private String script;

	public ProxyServer(String port, String ip, String uuid, Tarea tarea) throws Exception {

		KeyStoreFileCertificateSource fileCertificateSource = new KeyStoreFileCertificateSource("PKCS12", new File("/path/to/my/keystore.p12"), "keyAlias", "keystorePassword");

		server = new BrowserMobProxyServer();

		script = new Scripts().clickScript(uuid);
		System.out.println(script);

		try {
			server.addRequestFilter(new RequestFilter() {

				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
					String url = messageInfo.getOriginalUrl().toLowerCase();
					if (url.contains("www.myservice.com")) {
						String messageContents = contents.getTextContents();
						messageContents = messageContents.replace("key=", "").replace("event=", "").replace("url=", "").replace("id=", "").replace("pcIp=", "").replace("time=",
								"");
						String[] array = messageContents.split("&");
						try {
							String uri = decode(array[2], "UTF-8");
							String key = decode(array[0], "UTF-8");
							String pcIp = decode(array[5], "UTF-8");
							LineaDatos linea = new LineaDatos(key, array[1], uri, array[3], array[4], pcIp);
							System.out.println("#--> a Insertar: " + linea);
							// realizarInsercion(linea);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return null;
				}
			});

			server.addResponseFilter(new ResponseFilter() {

				@Override
				public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
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

			if (!port.equals(""))
				puerto = Integer.parseInt(port);
			else
				puerto = 8080;
			if (!ip.equals(""))
				direccion = InetAddress.getByName(ip);
			else
				direccion = InetAddress.getLocalHost();

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
			proxy.setNoProxy(null);

			FirefoxOptions options = new FirefoxOptions();

			options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
			options.setAcceptInsecureCerts(true);
			options.addPreference("acceptSslCerts", true);
			options.addPreference("enableNativeEvents", true);
			options.setProxy(proxy);
			options.setCapability(CapabilityType.PROXY, proxy);
			options.setCapability("marionette", true);
			path += "\\driver\\geckodriver";
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

	private void realizarInsercion(LineaDatos linea) {
		MySQLAccess db = MySQLAccess.getSingletonInstance();
		db.insertDato(linea);
	}
}
