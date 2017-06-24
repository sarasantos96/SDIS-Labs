import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.*;

public class SSLServer{

	private int port_number;
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	String [] cypher_suite;
	SSLServerSocket listener = null;
	SSLServerSocketFactory ssf = null;

	public SSLServer(String [] args) throws IOException{

		this.port_number = Integer.parseInt(args[0]);

		int numCyphers=args.length-1;
		cypher_suite=new String[numCyphers];
		for(int i=0;i<numCyphers;i++){
			int j=i+1;
			cypher_suite[i]=args[j];
		}

		ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try{
			listener = (SSLServerSocket) ssf.createServerSocket(port_number);
			listener.setNeedClientAuth(true);
			if(cypher_suite.length==0){
				listener.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
			}
			else{
				listener.setEnabledCipherSuites(cypher_suite);
			}


		}catch(IOException e){
			e.printStackTrace();
		}
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
			SSLSocket socket = (SSLSocket) listener.accept();

			try{
				BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

				String request = new String(in.readLine().trim());
				System.out.println("Request received: " + request);

				String message = "ERROR";

				String[] split = request.split(" ");

				if(split[0].equals("REGISTER")){
					if(split.length != 3){
						//Enviar mensagem de erro para o client
						System.out.println("Invalid Request");
					}else{
						//Adicionar Veiculo à base de dados
						int result = this.addVehicle(split[1],split[2]);
						message = new String("Number of Vehicles: " + Integer.toString(result));
					}
				}else if(split[0].equals("LOOKUP")){
					String result = this.getNameByLicense(split[1]);
					message = new String("Owner: "+ result);
				}

				out.println(message);

				out.close();
				in.close();


			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	//java SSLServer <port> <cypher-suite>*
	public static void main(String [] args) throws IOException{
		SSLServer server = new SSLServer(args);
		//Initial database
		server.addVehicle("12-A3-56","Fernando");
		server.addVehicle("26-64-XU","Manuel");

		System.out.println("Starting server...");

		server.receiveMessage();


	}


}
