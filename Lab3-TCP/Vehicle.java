import java.net.*;
import java.io.IOException;

public class Vehicle{
	private String license_number;
	private String owner_name;

	public Vehicle(String license_number, String owner_name){
		this.license_number = license_number;
		this.owner_name = owner_name;
	}

	public String getLicense(){
		return this.license_number;
	}

	public String getOwner(){
		return owner_name;
	}

	public boolean isVehicle(String license_number){
		if(license_number.equals(this.license_number))
			return true;
		return false;
	}
}