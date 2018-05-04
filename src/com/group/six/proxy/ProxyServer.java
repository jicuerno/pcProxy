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
import net.lightbody.bmp.mitm.RootCertificateGenerator;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class ProxyServer {

	private BrowserMobProxy server;
	private WebDriver webDriver;
	private String script;
	private String user;
	private Tarea tarea;

	public ProxyServer() {

	}

	public void init(Integer puerto, InetAddress direccion) throws Exception {

		String path = new File("").getAbsolutePath();
		RootCertificateGenerator rootCG = RootCertificateGenerator.builder().build();

		rootCG.saveRootCertificateAsPemFile(new File(path + "/certs/certificate.cer"));
		rootCG.savePrivateKeyAsPemFile(new File(path + "/certs/private-key.pem"), "password");
		rootCG.saveRootCertificateAndKey("PKCS12", new File(path + "/certs/keystore.p12"), "privateKeyAlias",
				"password");
		ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder().rootCertificateSource(rootCG).build();

		server = new BrowserMobProxyServer();
		server.setMitmManager(mitmManager);

		script = new Scripts().clickScript();
		System.out.println(script);

		try {
			server.addRequestFilter(new RequestFilter() {

				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					String url = messageInfo.getOriginalUrl().toLowerCase();
					if (url.contains("www.myservice.com")) {
						String messageContents = contents.getTextContents();

						messageContents = messageContents.replace("data=", "").replace("url=", "")
								.replace("session=", "").replace("time=", "").replace("pcIp=", "");

						String[] array = messageContents.split("&");
						try {

							String data = decode(array[0], "UTF-8");
							String uri = decode(array[1], "UTF-8");
							String time = decode(array[2], "UTF-8");
							String  session= decode(array[3], "UTF-8");
							String pcIp = decode(array[4], "UTF-8");

							if (tarea.getUrlFinal().contains(uri)) {
								Linea linea = new Linea(user, tarea.getKeyTarea(), data, uri, session, time, pcIp);
								SQLiteAccess.insertLinea(linea);
							}
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
			System.out.println("PROXY iniciado:" + direccion.getHostAddress() + ":" + puerto);
			server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			setProfileFirefox(puerto, direccion.getHostAddress());

		} catch (Exception e) {
			server.stop();
			throw new Exception();
		}
	}

	public void close() {
		String path = new File("").getAbsolutePath();
		File file = new File(path + "/certs/private-key.pem");
		if (file.exists())
			file.delete();
		file = new File(path + "/certs/keystore.p12");
		if (file.exists())
			file.delete();
		file = new File(path + "/certs/certificate.cer");
		if (file.exists())
			file.delete();
	}

	private void setProfileFirefox(int port, String ip) {

		String path = new File("").getAbsolutePath();

		webDriver = null;

		try {

			String proxyInfo = ip + ":" + port;
			Proxy proxy = new Proxy();
			proxy.setProxyType(Proxy.ProxyType.MANUAL);
			proxy.setHttpProxy(proxyInfo);
			proxy.setSslProxy(proxyInfo);
			proxy.setNoProxy(null);

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--ignore-certificate-errors");
			options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
			options.setAcceptInsecureCerts(true);
			options.addPreference("acceptSslCerts", true);
			options.addPreference("enableNativeEvents", true);
			options.addPreference("security.mixed_content.block_active_content", false);
			options.setProxy(proxy);
			options.setCapability(CapabilityType.PROXY, proxy);
			options.setCapability("marionette", true);
			path += "/driver/geckodriver";
			if (SystemUtils.IS_OS_WINDOWS)
				path += ".exe";
			System.setProperty("webdriver.gecko.driver", path);
			webDriver = new FirefoxDriver(options);

		} catch (Exception e) {
			e.printStackTrace();
			webDriver.quit();
		}
	}

	public void saltoTarea(Tarea tarea) {
		webDriver.get(tarea.getUrlInicio());
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public BrowserMobProxy getServer() {
		return server;
	}

	public void setServer(BrowserMobProxy server) {
		this.server = server;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

}
