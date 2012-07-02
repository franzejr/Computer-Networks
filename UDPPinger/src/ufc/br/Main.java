package ufc.br;

import java.net.UnknownHostException;

public class Main {
	
	public static void main(String[] args) throws Exception {
		new Thread(new PingServer(Constants.DEFAULT_PORT)).start();
		try {
			new PingClient("localhost", Constants.DEFAULT_PORT, 4).run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}
}

