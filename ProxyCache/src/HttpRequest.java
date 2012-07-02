
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest {
    /** Variaveis auxiliares */
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;
    /** colocar os parametros da requisicao */
    String method;
    String URI;
    String version;
    String headers = "";
    /** criando variaveis servidor e porta */
    private String host;
    private int port;

    /** Criando a requisicao http atraves da leitura do socket do cliente */
    public HttpRequest(BufferedReader from) {
	String firstLine = "";
	try {
	    firstLine = from.readLine();
	} catch (IOException e) {
	    System.out.println("Error reading request line: " + e);
	}

	String[] tmp = firstLine.split(" ");
	method = tmp[0];
	URI = tmp[1];
	version = tmp[2];

	System.out.println("URI is: " + URI);

	if (!method.equals("GET")) {
	    System.out.println("Error: Method not GET");
	}
	try {
	    String line = from.readLine();
	    while (line.length() != 0) {
		headers += line + CRLF;
		
		/* Caso a requisicao URI nao esteja completa, precisa-se achar o cabecalho do host e assim saber com qual servidor deve se conectar */
		if (line.startsWith("Host:")) {
		    tmp = line.split(" ");
		    if (tmp[1].indexOf(':') > 0) {
			String[] tmp2 = tmp[1].split(":");
			host = tmp2[0];
			port = Integer.parseInt(tmp2[1]);
		    } else {
			host = tmp[1];
			port = HTTP_PORT;
		    }
		}
		line = from.readLine();
	    }
	} catch (IOException e) {
	    System.out.println("Error reading from socket: " + e);
	    return;
	}
	System.out.println("Host to contact is: " + host + " at port " + port);
    }

    /**  retornar o host para o qual essa requisi��o � intensionada*/
    /** Return host for which this request is intended */
    public String getHost() {
	return host;
    }

    /**  Retorna porta para o servidor*/
    public int getPort() {
	return port;
    }

    /**  Convertendo a requisi��o numa string para re-envia-la mais facilmente*/
    public String toString() {
	String req = "";

	req = method + " " + URI + " " + version + CRLF;
	req += headers;
	/* This proxy does not support persistent connections */
	req += "Connection: close" + CRLF;
	req += CRLF;
	
	return req;
    }
}

