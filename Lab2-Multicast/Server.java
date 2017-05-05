import java.net.*;
import java.io.IOException;
import java.util.*;


public class Server{
	final static String SERVICE_ADDRESS = "127.0.0.0";

	private String mcast_addr;
	private int mcast_port;
	private String host_name = SERVICE_ADDRESS;
	private int port_number;
	private ArrayList<Vehicle> vehicles;

	//Multicast Comunication
	private InetAddress mc_inetAddr;
	private MulticastSocket mcsocket;
	//Unicast Comunication
	private DatagramSocket socket;
	private AdvertiseThread thread;

	public Server(String mcast_addr, int mcast_port, int port_number) throws IOException{
		this.mcast_addr = mcast_addr;
		this.mcast_port = mcast_port;
		this.port_number = port_number;

		this.vehicles = new ArrayList<Vehicle>();

		//Multicast Comunication
		this.mc_inetAddr = InetAddress.getByName(this.mcast_addr);
		this.mcsocket = new MulticastSocket(this.mcast_port);
		this.mcsocket.setTimeToLive(1);
		//Unicast Comunication
		this.socket = new DatagramSocket(this.port_number);
		this.thread = new AdvertiseThread();
		this.thread.start();
	}

	public int addVehicle(String license_number, String owner_name){
		String registed = this.getNameByLicense(license_number);

		if(registed.equals("NOT_FOUND")){
			vehicles.add(new Vehicle(license_number,owner_name));
		}else{
			return -1;
		}

		return vehicles.size();
	}

	public int getNumberOfVehicles(){
		return this.vehicles.size();
	}

	public int getPortNumber(){
		return this.port_number;
	}


	public Vehicle getVehicle(int i){
		return this.vehicles.get(i);
	}

	public String getNameByLicense(String license_number){
		String owner = new String("NOT_FOUND");
		for(int i = 0; i< vehicles.size(); i++){
			String license =  vehicles.get(i).getLicense();

			if(license .equals(license_number.trim())){
				owner = vehicles.get(i).getOwner();
				break;
			}
			
		}

		return owner;
	}

	public void receiveMessage() throws IOException{	
		while(true){
			byte[] rbuf = new byte[254];
			DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);

			socket.receive(packet);

			String received = new String(packet.getData());
			System.out.println("Message: " + received);
			String message = "ERROR";

			String[] split = received.split(" ");

			if(split[0].equals("register")){
				if(split.length != 3){
					//Enviar mensagem de erro para o client
					System.out.println("Invalid Request");
				}else{
					//Adicionar Veiculo à base de dados
					int result = this.addVehicle(split[1],split[2]);
					message = new String("Number of Vehicles: " + Integer.toString(result));
				}
			}else if(split[0].equals("lookup")){
				String result = this.getNameByLicense(split[1]);
				message = new String("Owner: "+ result);
			}

			rbuf = message.getBytes();
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(rbuf, rbuf.length, address, port);

			socket.send(packet);
		}
	}

	//java Server <srvc_port> <mcast_addr> <mcast_port>  
	public static void main(String [] args)  throws UnknownHostException, InterruptedException, IOException{
		Server server = new Server(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[0]));
		//Initial database
		server.addVehicle("12-A3-56","Fernando");
		server.addVehicle("26-64-XU","Manuel");

		server.receiveMessage();
	}

	//Thread
	private class AdvertiseThread extends Thread{
		public void run(){
			while(true){
				String message = new String(Integer.toString(port_number));

				DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), mc_inetAddr, mcast_port);

				try{
					mcsocket.send(packet);
					System.out.println("Multicast message: "+ mcast_addr+" "+mcast_port
						+": "+port_number);
					Thread.sleep(1000);
				}catch(IOException | InterruptedException  e){
					System.out.println("Exception");
				}
			}
		}
	}
}