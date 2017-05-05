import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.*;


public class SSLClient{

	private String host_name;
	private int port_number;

	private String oper;
	private String opnd;
	private String request;

	public SSLClient(String [] args){
		this.host_name = args[0];
		this.port_number = Integer.parseInt(args[1]) ;
		this.oper = args[2];

		if(this.oper.equals("register"))
			this.request = this.oper + " " + args[3] + " " + args[4];
		else if(this.oper.equals("lookup"))
			this.request = this.oper + " " + args[3];
	}

	public int getPortNumber(){
		return port_number;
	}

	/*public void sendMessage() throws IOException{
		InetAddress host = InetAddress.getByName(host_name);
		SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host,port_number);

    PrintWriter out =
        new PrintWriter(sslSocket.getOutputStream(), true);
    BufferedReader in =
        new BufferedReader(
            new InputStreamReader(sslSocket.getInputStream()));
		out.println(request);
		String response = in.readLine();
		System.out.println(response);

		out.close();
		in.close();
		sslSocket.close();
	}*/

	//java SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*
	public static void main(String [] args) throws IOException{
		if(args.length < 4){
			System.out.println("Usage: java Echo <hostname> <port_number> <oper> <opnd>");
		}

		SSLClient client = new SSLClient(args);

		//client.sendMessage();
		int port = client.port_number;
		String host = client.host_name;

		try {
		    SSLSocketFactory sslFact =
		      (SSLSocketFactory)SSLSocketFactory.getDefault();
		    SSLSocket s =
		      (SSLSocket)sslFact.createSocket(host, port);
					 s.startHandshake();

		    OutputStream out = s.getOutputStream();
		    InputStream in = s.getInputStream();

		    // Send messages to the server through
		    // the OutputStream
		    // Receive messages from the server
		    // through the InputStream
				PrintWriter outprinter = new PrintWriter(out,true);
				BufferedReader inprinter =
		        new BufferedReader(
		            new InputStreamReader(in));
				outprinter.println(client.request);

		} catch (IOException e) { }
	}
}
