package trabalho.tcp.webServer;

public class ContentVerifier {

	public static final String HTML = "text/html";
	public static final String JPEG = "image/jpeg";
	public static final String GIF = "image/gif";

	public String verifyContent(String fileName) {
		if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return HTML;
		}
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			return JPEG;
		}
		if (fileName.endsWith(".gif")) {
			return GIF;
		}

		return null;
	}

}
