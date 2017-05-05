import java.net.*;
import java.io.IOException;

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
		DatagramSocket socket = new DatagramSocket();
		byte[] sbuf = this.request.getBytes();
		InetAddress address = InetAddress.getByName(this.host_name);
		DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, address, this.port_number);

		socket.send(packet);

		//receives Response

		byte[] rbuf = new byte[254];
		packet = new DatagramPacket(rbuf, rbuf.length);
		socket.setSoTimeout(500);

		try{
			socket.receive(packet);
			String received = new String(packet.getData());
			System.out.println(received);
		}catch(SocketTimeoutException e){
			System.out.println("No response...");
		}
		socket.close();
	}


	public static void main(String [] args) throws IOException{
		if(args.length < 4){ 	
			System.out.println("Usage: java Echo <hostname> <port_number> <oper> <opnd>");
;		}

		Client client = new Client(args);
		 
		client.sendMessage();		
		
	}
}