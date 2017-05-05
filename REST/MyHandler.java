import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.io.File;
import com.sun.net.httpserver.*;

class MyHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException{
		InputStream is = t.getRequestBody();
		byte[] b = is.read();
		System.out.println(new String(b));
		String response = "Oi Bro";
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
