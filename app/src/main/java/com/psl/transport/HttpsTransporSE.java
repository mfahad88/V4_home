package com.psl.transport;

import org.ksoap2.transport.ServiceConnection;

import java.io.IOException;

//Referenced classes of package org.ksoap2.transport:
//         HttpTransportSE, HttpsServiceConnectionSE, ServiceConnection

public class HttpsTransporSE extends HttpTranspor
{

 static final String PROTOCOL = "https";
 private HttpsServiceConnSE conn;
 private final String host;
 private final int port;
 private final String file;
 private final int timeout;

 public HttpsTransporSE(String host, int port, String file, int timeout)
 {
     //super("https://" + host + ":" + port + file);
	 super(host);
     conn = null;
     this.host = host;
     this.port = port;
     this.file = file;
     this.timeout = timeout;
 }

 public HttpsServiceConnSE getConnection()
 {
     return conn;
 }

 protected ServiceConnection getServiceConnection()
     throws IOException
 {
     conn = new HttpsServiceConnSE(host, port, file, timeout);
     return conn;
 }
}
