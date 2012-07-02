package trabalho.tcp.webServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public final class WebServer
{
	public static void main(String argv[]) throws Exception
	{
		try {
			ServerSocket serverSocket = new ServerSocket(9876);
			while (true) {
				//Espera por resposta
				Socket commSocket = serverSocket.accept();
				//Resposta
				new Thread(new HttpRequest(commSocket)).start();
//				String sentence = new String(receivePacket.getData());
//				System.out.println("RECEIVED: " + sentence);
				
				//Endereço do cliente
//				InetAddress IPAddress = receivePacket.getAddress();
				//Porta do cliente
//				int port = receivePacket.getPort();
				
				//Formatando a resposta
//				String capitalizedSentence = sentence.toUpperCase();
//				sendData = capitalizedSentence.getBytes();
//				
				//Envia resposta pro cliente
//				DatagramPacket sendPacket = new DatagramPacket(sendData,
//						sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}


