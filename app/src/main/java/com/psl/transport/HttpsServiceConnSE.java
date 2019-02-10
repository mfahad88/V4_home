package com.psl.transport;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.ksoap2.transport.ServiceConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// Referenced classes of package org.ksoap2.transport:
//            ServiceConnection

public class HttpsServiceConnSE   implements ServiceConnection
{
    private TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
					}
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
					
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
					
					
				}
				}
			};

    private HttpsURLConnection connection;
    public HttpsServiceConnSE(String host, int port, String file, int timeout)
        throws IOException
    {
        //connection = (HttpsURLConnection)(new URL("https", host, port, file)).openConnection();
        
        try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			}
		catch (Exception e)
		{
			e.getMessage();
			}
		connection = (HttpsURLConnection) new URL(host).openConnection();
		((HttpsURLConnection) connection).setHostnameVerifier(new AllowAllHostnameVerifier());
		updateConnectionParameters(timeout);
    }


    private void updateConnectionParameters(int timeout)
    {
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        
    }

    public void connect()
        throws IOException
    {
        connection.connect();
    }

    public void disconnect()
    {
        connection.disconnect();
    }

    public void setRequestProperty(String key, String value)
    {
        connection.setRequestProperty(key, value);
    }

    public void setRequestMethod(String requestMethod)
        throws IOException
    {
        connection.setRequestMethod(requestMethod);
    }


    public OutputStream openOutputStream()
        throws IOException
    {
        return connection.getOutputStream();
    }

    public InputStream openInputStream()
        throws IOException
    {
        return connection.getInputStream();     
    }

    public InputStream getErrorStream()
    {
        return connection.getErrorStream();
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


	@Override
	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public List getResponseProperties() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	
}

