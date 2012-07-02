package trabalho.udp.exemplos;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {

	public Client(String nome) {
		super(nome);
	}

	public void run() {
		try {
			DatagramSocket clientSocket = new DatagramSocket();

			//Pega IP do cliente(localhost)
			InetAddress IPAddress = InetAddress.getByName("localhost");
			
			//Pega informação a ser enviada
			String sentence = Thread.currentThread().getName();

			//Envia a informação ao servidor.
			byte[] sendData = new byte[512];
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			
			//Aguarda a resposta do servidor.
			byte[] receiveData = new byte[512];
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			//Trata e imprime a resposta do servidor.
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}