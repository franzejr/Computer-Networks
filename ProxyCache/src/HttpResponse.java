

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpResponse {
    final static String CRLF = "\r\n";
    /** atribui o tamanho do buffer para ler o objeto*/
    final static int BUF_SIZE = 8192;
    /** Tamanho maximo do objeto que esse proxy pode aguentar. Para o momento onde se atribuiu 100 KB*/
    final static int MAX_OBJECT_SIZE = 100000;
    /** Replicar status e cabe�alhos*/
    String version;
    int status;
    String statusLine = "";
    String headers = "";
    /* Corpo da replica��o */
    public byte[] body = new byte[MAX_OBJECT_SIZE];

    /** Ler a resposta do servidor */
    public HttpResponse(DataInputStream fromServer) {
    //public HttpResponse(InputStream is) {
    	/* Comprimento do objeto */
	int length = -1;
	boolean gotStatusLine = false;
	


	/* Primeira linha de status de leitura e cabecalho da resposta */
	try {
	    String line = fromServer.readLine();
	    while (line.length() != 0) {
		if (!gotStatusLine) {
		    statusLine = line;
		    gotStatusLine = true;
		} else {
		    headers += line + CRLF;
		}


		if (line.startsWith("Content-Length:") ||
		    line.startsWith("Content-length:")) {
		    String[] tmp = line.split(" ");
		    length = Integer.parseInt(tmp[1]);
		}
		line = fromServer.readLine();
	    }
	} catch (IOException e) {
	    System.out.println("Error reading headers from server: " + e);
	    return;
	}

	try {
	    int bytesRead = 0;
	    byte buf[] = new byte[BUF_SIZE];
	    boolean loop = false;
	    
	    /* Se nos nao pegar-mos o cabecalho de Content-Lenght, fica num loop ate a conexao ser fechada */
	    if (length == -1) {
		loop = true;
	    }
	    
	 while (bytesRead < length || loop) {
		/* Ler em dados binarios */
		int res = fromServer.read(buf, 0, BUF_SIZE);

		if (res == -1) {
		    break;
		}
		
		/*  Copia os bytes para o corpo. N�o pode exceder o tamanho maximo do objeto*/
		for (int i = 0; 
		     i < res && (i + bytesRead) < MAX_OBJECT_SIZE; 
		     i++) {
		    body[bytesRead + i] = buf[i];
		}
		bytesRead += res;
	    }
 	} catch (IOException e) {
 	    System.out.println("Error reading response body: " + e);
 	    return;
 	}


    }

    /** Converter a resposta numa string para envia-la mais facilmente.
     * Converter somente o cabe�alho*/
    public String toString() {
	String res = "";

	res = statusLine + CRLF;
	res += headers;
	res += CRLF;
	
	return res;
    }
}
