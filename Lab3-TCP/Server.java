import java.net.*;
import java.util.*;
import java.io.*;

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
		while(true){
			ServerSocket srvSocket = null;
			Socket echoSocket = null;

			try{
				srvSocket = new ServerSocket(this.port_number);
			}catch(IOException e){
				System.err.println("Could not listen on port: "+this.port_number);
				System.exit(-1);
			}

			try{
				echoSocket = srvSocket.accept();
			}catch(IOException e){
				System.err.println("Accept failed: " + this.port_number);
				System.exit(1);
			}

			PrintWriter out = null;
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			out = new PrintWriter(echoSocket.getOutputStream(),true);

			String request = new String(in.readLine());
			System.out.println("Resquest received: "+ request);

			String message = "ERROR";

			String[] split = request.split(" ");

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

			out.println(message);

			out.close();
			in.close();
			echoSocket.close();
			srvSocket.close();
		}	
	}

	//java Server <srvc_port>
	public static void main(String [] args) throws IOException{
		int port_number = Integer.parseInt(args[0]);
		Server server = new Server(port_number);

		//Initial database
		server.addVehicle("12-A3-56","Fernando");
		server.addVehicle("26-64-XU","Manuel");

		server.receiveMessage();
	}

	
}