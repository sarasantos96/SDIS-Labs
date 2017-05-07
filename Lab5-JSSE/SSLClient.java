import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.*;


public class SSLClient{
	private int SOCKET_TIMEOUT = 3000;

	private String host_name;
	private int port_number;
	private InetAddress addr;

	private String oper;
	private String plate_number;
	private String request;

	private SSLSocket clientsocket = null;
	private SSLSocketFactory sf = null;

	private PrintWriter out = null;
	private BufferedReader in = null;
	private String[] cypher_suite;

	public SSLClient(String [] args) throws UnknownHostException, IOException{
		this.host_name = args[0];
		this.port_number = Integer.parseInt(args[1]) ;
		oper =args[2];
		plate_number=args[3];

		System.out.println("Plate number: " + plate_number);

		if(oper.equals("register")){
			String owner_name=args[4];
			request="REGISTER " + plate_number + " " + owner_name;

			cypher_suite = new String[args.length-5];
			for(int i = 5;i<args.length-1;i++) {
				cypher_suite[i-5]=args[i];
			}
		}else if(oper.equals("lookup")){
			System.out.println("oi");
			request="LOOKUP " + plate_number;
			cypher_suite = new String[args.length-4];
			for(int i = 4;i<args.length-1;i++) {
				cypher_suite[i-4]=args[i];
			}
		}else{
			System.out.println("No suitable operation");
			System.exit(1);
		}
		System.out.println("Args length: "+ args.length);
		System.out.println("Cypher: " + cypher_suite.length);
		System.out.println("Request: " + request);

		addr = InetAddress.getByName(host_name);

		sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

		try{
			//System.out.println(cypher_suite[0]);
			clientsocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(addr,port_number);
			if(cypher_suite.length==0){
				SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
				clientsocket.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
			}
			else{
				clientsocket.setEnabledCipherSuites(cypher_suite);
			}

		}catch (IOException e){
			e.printStackTrace();
		}


	}

	public int getPortNumber(){
		return port_number;
	}

	public void sendMessage() throws IOException{
		out = new PrintWriter(clientsocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

		out.println(request);

		String response = new String(in.readLine());
		System.out.println("Response: " + response);

		out.close();
		in.close();

	}

	//java SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*
	public static void main(String [] args) throws IOException{
		if(args.length < 4){
			System.out.println("Usage: java Echo <hostname> <port_number> <oper> <opnd>");
		}

		SSLClient client = new SSLClient(args);

		client.sendMessage();
	}
}
