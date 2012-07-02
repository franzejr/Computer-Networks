package trabalho.udp.exemplos;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Thread {

	public Server(String nome) {
		super(nome);
	}

	public void run() {
		DatagramSocket serverSocket;
		byte[] receiveData = new byte[512];
		byte[] sendData = new byte[512];

		try {
			serverSocket = new DatagramSocket(9876);
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				
				//Espera por resposta
				serverSocket.receive(receivePacket);
				//Resposta
				String sentence = new String(receivePacket.getData());
				System.out.println("RECEIVED: " + sentence);
				
				//Endereço do cliente
				InetAddress IPAddress = receivePacket.getAddress();
				//Porta do cliente
				int port = receivePacket.getPort();
				
				//Formatando a resposta
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				
				//Envia resposta pro cliente
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
