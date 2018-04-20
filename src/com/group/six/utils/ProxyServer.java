package com.group.six.utils;

import static java.net.URLDecoder.decode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.logging.Level;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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

	public ProxyServer(String port, String ip) throws Exception {

		KeyStoreFileCertificateSource fileCertificateSource = new KeyStoreFileCertificateSource("PKCS12", new File("/path/to/my/keystore.p12"), "keyAlias", "keystorePassword");

		server = new BrowserMobProxyServer();

		try {
			server.addRequestFilter(new RequestFilter() {

				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
					String url = messageInfo.getOriginalUrl().toLowerCase();
					if (url.contains("www.myservice.com")) {
						String messageContents = contents.getTextContents();
						messageContents = messageContents.replace("event=", "").replace("url=", "").replace("id=", "").replace("time=", "");
						String[] array = messageContents.split("&");
						try {
							String uri = decode(array[1], "UTF-8");
							System.out.println("#--> enviado: " + array[0] + "," + uri + "," + array[2] + "," + array[3]);
							realizarInsercion(array[0], uri, array[2], array[3]);
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

					StringBuilder builder = new StringBuilder();
					builder.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n");
					builder.append("<script type=\"text/javascript\">\n");
					builder.append("var \\$j = jQuery.noConflict(true);\n");
					builder.append(new Scripts().clickScript());
					builder.append("</script>\n");
					builder.append("</head>\n");

					if (messageContents.contains("</HEAD>") || messageContents.contains("</head>")) {
						// lo injectamos
						String newContents = messageContents.replaceAll("</HEAD>", builder.toString());
						if (!newContents.contains("//custom Insert"))
							newContents = messageContents.replaceAll("</head>", builder.toString());
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

			setProfileFirefox(puerto, direccion.getHostAddress());

		} catch (Exception e) {
			server.stop();
			throw new Exception();
		}
	}

	private void setProfileFirefox(int port, String ip) {

		WebDriver webDriver = null;
		try {
			String path = new File("").getAbsolutePath();
			// Set log level

			String proxyInfo = ip + ":" + port;
			Proxy proxy = new Proxy();
			proxy.setProxyType(Proxy.ProxyType.MANUAL);
			proxy.setHttpProxy(proxyInfo).setFtpProxy(proxyInfo).setSslProxy(proxyInfo);

			FirefoxOptions options = new FirefoxOptions();

			options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
			options.setAcceptInsecureCerts(true);
			options.addPreference("acceptSslCerts", true);
			options.addPreference("enableNativeEvents", true);
			options.setProxy(proxy);
			options.setCapability(CapabilityType.PROXY, proxy);

			System.setProperty("webdriver.gecko.driver", path + "\\driver\\geckodriver.exe");

			webDriver = new FirefoxDriver(options);

			webDriver.get("https://www.google.com/");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (webDriver != null) {
				webDriver.quit();
			}
		}
	}

	private void realizarInsercion(final String element, final String url, final String event, final String time) {

	}

}
