import java.net.*;
import java.util.*;
import java.io.*;


public class Client{

	private String host_name;
	private int port_number;

	private String oper;
	private String opnd;
	private String request; 

	public Client(String [] args){
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

	public void sendMessage() throws IOException{
		try (
            Socket echoSocket = new Socket(host_name, port_number);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            out.println(request);
            System.out.println(in.readLine());
        } catch (UnknownHostException e) {
            System.err.println("Error Unknown Host " + host_name);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error I/O Exception");
            System.exit(1);
        } 
	}

	//java Client <host_name> <port_number> <oper> <opnd> 
	public static void main(String [] args) throws IOException{
		if(args.length < 4){ 	
			System.out.println("Usage: java Echo <hostname> <port_number> <oper> <opnd>");
;		}

		Client client = new Client(args);
		 
		client.sendMessage();			
	}
}