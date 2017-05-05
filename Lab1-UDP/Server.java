import java.net.*;
import java.io.IOException;
import java.util.*;


public class Server{

	private int port_number;
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	public Server(int port){
		this.port_number = port;
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
		DatagramSocket socket = new DatagramSocket(port_number);

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

		
	public static void main(String [] args) throws IOException{
		int port_number = Integer.parseInt(args[0]);
		Server server = new Server(port_number);

		//Initial database
		server.addVehicle("12-A3-56","Fernando");
		server.addVehicle("26-64-XU","Manuel");

		server.receiveMessage();
	}

	
}