import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.io.File;
import com.sun.net.httpserver.*;

public class ServerTest{
	public static void main(String [] args) throws IOException{
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	   	server.createContext("/applications/myapp", new MyHandler());
	   	server.setExecutor(null); // creates a default executor
	   	server.start();
	}
}