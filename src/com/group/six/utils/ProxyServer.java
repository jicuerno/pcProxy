package com.group.six.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import static java.net.URLDecoder.decode;

public class ProxyServer {

	public BrowserMobProxy server;

	public ProxyServer() {

		server = new BrowserMobProxyServer();

		try {
			server.addRequestFilter(new RequestFilter() {

				@Override
				public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					String url = messageInfo.getOriginalUrl().toLowerCase();
					if (url.contains("www.myservice.com")) {
						String messageContents = contents.getTextContents();
						messageContents = messageContents.replace("event=", "").replace("url=", "").replace("id=", "")
								.replace("time=", "");
						String[] array = messageContents.split("&");
						System.out.println("#--> enviado: " + messageContents);

						try {
							String uri = decode(array[1], "UTF-8");
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
				public void filterResponse(HttpResponse response, HttpMessageContents contents,
						HttpMessageInfo messageInfo) {
					String messageContents = contents.getTextContents();

					StringBuilder builder = new StringBuilder();
					builder.append(
							"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n");
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

			server.start(8080, InetAddress.getLocalHost());
			System.out.println("PROXY iniciado:" + InetAddress.getLocalHost() +":8080" );
			
			server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
		} catch (Exception e) {
			server.stop();
		}
	}

	private void realizarInsercion(final String element, final String url, final String event, final String time) {

	}

}
