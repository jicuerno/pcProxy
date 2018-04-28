package com.group.six.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.six.data.Datos;

public class WebServicesUtils {

	private static Integer sincro = 1800;
	private static String usuario = "";
	private static String passw = "";
	private static String urlWebServiceRest = "https://localhots:............/enviar/";

	public String invocaWebServiceHttps(final Datos datos, String action) throws Exception {
		String result = "";
		urlWebServiceRest += action;

		System.out.println("Sincronizacion : TRAZA - URL (" + urlWebServiceRest + ") segundo timeout (" + sincro + ")");

		final X509TrustManager tm = new X509TrustManager() {

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}

		};
		final SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(null, new TrustManager[] { tm }, null);
		final URL url = new URL(urlWebServiceRest);
		String encoding = MyBase64.encode(usuario + ":" + passw);
		final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(sincro * 1000);
		connection.setReadTimeout(sincro * 1000);
		connection.setSSLSocketFactory(ctx.getSocketFactory());
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return false;
			}

		});
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		connection.setRequestProperty("Content-Type", "application/json");
		final ObjectMapper mapper = new ObjectMapper();

		final String jsonRequest = mapper.writeValueAsString(datos);
		final OutputStream os = connection.getOutputStream();
		os.write(jsonRequest.getBytes());
		os.flush();
		final InputStream content = connection.getInputStream();
		final BufferedReader in = new BufferedReader(new InputStreamReader(content));
		String line;
		while (null != (line = in.readLine())) {
			result += line;
		}
		return result;
	}
}
