package server.pojos;

public class Client {
	private String nombre;
	private String ApPaterno;
	private double balance;
	
	public Client(String nombre, String appat, double balance){
		this.nombre = nombre;
		this.ApPaterno = appat;
		this.balance = balance;
	}
}
