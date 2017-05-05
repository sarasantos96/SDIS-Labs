import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.*;

public class SSLServer{

	private int port_number;
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	public SSLServer(int port){
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


	/*public void receiveMessage() throws IOException{

		while(true){
			SSLServerSocket s = null;
			SSLServerSocketFactory ssf = null;

			ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

			try {
			    s = (SSLServerSocket) ssf.createServerSocket(port_number);
			}
			catch( IOException e) {
			    System.out.println("Server - Failed to create SSLServerSocket");
			    e.getMessage();
			    return;
			}
			PrintWriter out = null;
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(),true);

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

			srvSocket.close();
		}
	}*/

	//java SSLServer <port> <cypher-suite>*
	public static void main(String [] args) throws IOException{
		int port_number = Integer.parseInt(args[0]);
		//Initial database
		//server.addVehicle("12-A3-56","Fernando");
		//server.addVehicle("26-64-XU","Manuel");


		//server.receiveMessage();
			System.out.println("Starting server...");
			int port = port_number;

			SSLServerSocket s = null;
			SSLServerSocketFactory ssf = null;

			ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

			try {
			    s = (SSLServerSocket) ssf.createServerSocket(port);
					SSLSocket c = (SSLSocket)s.accept();
					System.out.println("oi");
					OutputStream out = c.getOutputStream();
			    InputStream in = c.getInputStream();

			    // Send messages to the client through
			    // the OutputStream
			    // Receive messages from the client
			    // through the InputStream

					PrintWriter outprinter = new PrintWriter(out,true);
					BufferedReader inprinter = new BufferedReader(new InputStreamReader(in));
					String receive  = new String(inprinter.readLine());
					System.out.println(receive);
			}
			catch( IOException e) {
			    System.out.println("Server - Failed to create SSLServerSocket");
			    e.getMessage();
			    return;
			}

	}


}
