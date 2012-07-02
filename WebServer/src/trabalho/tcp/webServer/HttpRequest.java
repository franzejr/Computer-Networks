package trabalho.tcp.webServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void processRequest() throws IOException {
		// Get a reference to the socket's input and output streams.
		InputStreamReader in = new InputStreamReader(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		// Set up input stream filters.
		BufferedReader br = new BufferedReader(in);

		// TODO
		// Get the request line of the HTTP request message.
		String requestLine = br.readLine();

		// Display the request line.
		System.out.println();
		System.out.println(requestLine);

		// Get and display the header lines.

		String headerLine = br.readLine();
		while (headerLine.length() != 0) {
			System.out.println(headerLine);
			headerLine = br.readLine();
		}

		// tratar request.
		// Pressupondo que requestLine é um GET, pega-se o caminho o arquivo, e
		// processa a requisição.
		String fileName = "." + requestLine.split(" ")[1];
		processGETRequest(fileName, out);

		// Close streams and socket.
		out.close();
		br.close();
		socket.close();

	}

	private String processGETRequest(String filePath, DataOutputStream out)
			throws IOException {
		String response = "HTTP/1.0 ";
		String statusLine = null;
		String contentTypeLine = "Content-type: ";
		String body = "";
		try {
			FileInputStream fIn = new FileInputStream(filePath);
			statusLine = "200 OK";
			contentTypeLine += new ContentVerifier().verifyContent(filePath);

			response += statusLine + CRLF + contentTypeLine + CRLF;

			// Write header in socket's output stream.
			out.writeBytes(response);

			// Write file in socket's output stream.
			// Construct a 1K buffer to hold bytes on their way to the socket.
			byte[] buffer = new byte[1024];
			int bytes = 0;

			// Copy requested file into the socket's output stream.
			while ((bytes = fIn.read(buffer)) != -1) {
				out.write(buffer, 0, bytes);
			}

		} catch (FileNotFoundException e) {
			// 404.
			e.printStackTrace();
			statusLine = "404 NOT FOUND";
			contentTypeLine += ContentVerifier.HTML;
			body = "<HTML>" + "<HEAD><TITLE>LOL 404</TITLE></HEAD>"
					+ "<BODY>Bad, Bad Server, no Dount for you</BODY></HTML>";
			response += statusLine + CRLF + contentTypeLine + CRLF + CRLF + body;
			out.writeBytes(response);
		}

		return response;
	}
}