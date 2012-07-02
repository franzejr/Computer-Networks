package ufc.br;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PingClient implements Runnable {
	//Attributes from the Client
	private final int PORT;
	private final int ROUNDS;
	private final InetAddress serverAddress;
	
	//Setando os metodos do construtor
	public PingClient(String host, int port, int rounds) throws UnknownHostException{
		this.PORT = port;
		this.ROUNDS = rounds;
		this.serverAddress = InetAddress.getByName(host);
	}
	
	//Method that it will make this class multithread
	@Override
	public void run() {
		DatagramSocket datagramSocket;
		try{
			datagramSocket = new DatagramSocket();
		}catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		for(int i = 1; i < ROUNDS; i++ ){
			byte[] buffer = Constants.MSG_PING_REQUEST.getBytes();
			long currentMillisBefore = System.currentTimeMillis();
			DatagramPacket datagramPacketRequest = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);			
			System.out.println("CLIENT [PING " + i + "] TO " + serverAddress.getHostName() + "...");
			try {
				datagramSocket.send(datagramPacketRequest);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			buffer = new byte[1024];
			DatagramPacket datagramPacketResponse = new DatagramPacket(buffer, buffer.length);
			try {
				datagramSocket.receive(datagramPacketResponse);
				if (new String(buffer).trim().equals(Constants.MSG_PING_RESPONSE)) {
					System.out.println("CLIENT [PING " + i + "] TO " + serverAddress.getHostName() + " OK");
					long currentTimeMillisAfter = System.currentTimeMillis();
					System.out.println("CLIENT [PING " + i + "] Time: " + (currentTimeMillisAfter - currentMillisBefore) + " milliseconds");
				} else {
					System.out.println("CLIENT [PING " + i + "] TO " + serverAddress.getHostName() + " NOT OK");
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		
	}

}
