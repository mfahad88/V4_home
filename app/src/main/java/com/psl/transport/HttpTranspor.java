package com.psl.transport;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

// Referenced classes of package org.ksoap2.transport:
//            Transport, ServiceConnectionSE, ServiceConnection

public class HttpTranspor extends Transport
{

    public HttpTranspor(String url)
    {
        super(url);
    }

    public void call(String soapAction, SoapEnvelope envelope)
        throws IOException, XmlPullParserException
    {
        if(soapAction == null)
        {
            soapAction = "\"\"";
        }
        byte requestData[] = createRequestData(envelope);
        requestDump = debug ? new String(requestData) : null;
        //requestData = requestDump.replaceAll("n0", "").getBytes();
        responseDump = null;
        ServiceConnection connection = getServiceConnection();
        connection.setRequestProperty("User-Agent", "kSOAP/2.0");
        connection.setRequestProperty("SOAPAction", soapAction);
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("Content-Length", "" + requestData.length);
        connection.setRequestMethod("POST");
        connection.connect();
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
        requestData = null;
        InputStream is;
        try
        {
            connection.connect();
            is = connection.openInputStream();
        }
        catch(IOException e)
        {
            is = connection.getErrorStream();
            if(is == null)
            {
                connection.disconnect();
                throw e;
            }
        }
        if(debug)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[256];
            do
            {
                int rd = is.read(buf, 0, 256);
                if(rd == -1)
                {
                    break;
                }
                bos.write(buf, 0, rd);
            } while(true);
            bos.flush();
            buf = bos.toByteArray();
            responseDump = new String(buf);
            is.close();
            is = new ByteArrayInputStream(buf);
        }
        parseResponse(envelope, is);
    }

    protected ServiceConnection getServiceConnection()
        throws IOException
    {
        return  new ServiceConnecti(url);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

	@Override
	public List call(String arg0, SoapEnvelope arg1, List arg2)
			throws IOException, XmlPullParserException {
		// TODO Auto-generated method stub
		return null;
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

}
