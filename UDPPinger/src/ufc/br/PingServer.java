package ufc.br;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PingServer implements Runnable {

	private final int PORT;

	public PingServer(int port) {
		this.PORT = port;
	}

	// Method that will start the thread
	@Override
	public void run() {

		DatagramSocket datagramSocket;
		try {
			datagramSocket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}

		// Processing loop
		while (true) {
			byte[] buffer = new byte[1024];
			// Create a datagram packet to hold incomming UDP packet.
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			InetAddress clientAddress;
			try {
				datagramSocket.receive(request);
				clientAddress = request.getAddress();

				// Doing some checks
				if (new String(buffer).trim()
						.equals(Constants.MSG_PING_REQUEST)) {
					System.out.println("OK --> FROM:"
							+ clientAddress.getHostName());
				} else {
					System.out.println("NOT OK --> FROM:" + clientAddress.getHostName());
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, request.getPort());			
			response.setData(Constants.MSG_PING_RESPONSE.getBytes());
			try {
				datagramSocket.send(response);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}