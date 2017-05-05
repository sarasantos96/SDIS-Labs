public class Vehicle{
	String plate = new String();
	String owner = new String();

	public Vehicle(){};

	public Vehicle(String plate, String owner){
		this.plate = plate;
		this.owner = owner;
	}

	public String toString(){
		return this.plate + " - " + this.owner;
	}
}
