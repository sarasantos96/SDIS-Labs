import java.io.IOException;
import java.net.*;


public class Client{

	//Multicast
	private String mcast_addr;
	private int mcast_port;
	private InetAddress mcaddr;
	private MulticastSocket mcast_socket;

	//Unicast
	private String host_name;
	private int port_number;
	private InetAddress addr;
	private DatagramSocket socket;

	//Client Request
	private String oper;
	private String opnd;
	private String request; 

	public Client(String [] args) throws UnknownHostException, InterruptedException, IOException{
		//Multicast
		this.mcast_addr = args[0];
		this.mcast_port = Integer.parseInt(args[1]);
		this.mcaddr = InetAddress.getByName(this.mcast_addr);
		this.mcast_socket = new MulticastSocket(this.mcast_port);

		//Request
		this.oper = args[2];
		if(this.oper.equals("register"))
			this.request = this.oper + " " + args[3] + " " + args[4];
		else if(this.oper.equals("lookup"))
			this.request = this.oper + " " + args[3];

		//Unicast
		this.socket = new DatagramSocket();

	}

	public void receiveMulticastMessage() throws UnknownHostException, InterruptedException, IOException{
		mcast_socket.joinGroup(mcaddr);

		byte[] message = new byte[254];
		DatagramPacket packet = new DatagramPacket(message, message.length);
		mcast_socket.receive(packet);

		String msg = new String(packet.getData());
		msg.trim();
		System.out.println("Multicast message: " + msg);
		this.addr = packet.getAddress();
		this.port_number = Integer.parseInt(msg.trim());

		mcast_socket.leaveGroup(mcaddr);
	}

	public void sendMessage() throws IOException{
		byte[] sbuf = this.request.getBytes();
		DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, this.addr, this.port_number);

		this.socket.send(packet);

		//receives Response

		byte[] rbuf = new byte[254];
		packet = new DatagramPacket(rbuf, rbuf.length);
		socket.setSoTimeout(10000);

		try{
			socket.receive(packet);
			String received = new String(packet.getData());
			System.out.println(received);
		}catch(SocketTimeoutException e){
			System.out.println("No response...");
		}
		socket.close();
	}


	//java client <mcast_addr> <mcast_port> <oper> <opnd> * 
	public static void main(String [] args) throws UnknownHostException, InterruptedException, IOException{
		Client client = new Client(args); 

		client.receiveMulticastMessage();
		client.sendMessage();		
	}
}