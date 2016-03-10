package server.connection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

import server.DB.Managment.ClientManager;

public class Service implements Runnable{
	private Socket client;
	private DataOutputStream output;
	private BufferedReader input;
	private boolean active = true;
	private boolean accepted = false;
	private ClientManager manager;
	private int cuenta;
	
	public Service(Socket client) throws IOException{
		this.client = client;
		this.output = new DataOutputStream(this.client.getOutputStream());
		this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		this.manager = new ClientManager();
	}
	
	@Override
	public void run() {
		String structure;
		String petition;
		double cantidad;
		boolean result = false;
		int TransCuenta;
		String[] components;
		System.out.println("Client accepted");
		while(this.active){
			try {
				System.out.println("Waiting input");
				structure = this.input.readLine();
				System.out.println(structure);
				 components = structure.split("-");
				petition = components[0];
				
				if(this.accepted){
					switch(petition){
						case "C":
							double saldo = this.manager.ConsultMoney(cuenta);
							this.output.writeBytes(Double.toString(saldo) + "\n");
							System.out.println("Saldo enviado");
							break;
						case "R":
							cantidad = Double.parseDouble(components[1]);
							result = this.manager.getMoneyOut(cuenta, cantidad);
							if(result)
								this.output.writeBytes("pv\n");
							else
								this.output.writeBytes("pi\n");
							break;
						case "D":
							cantidad = Double.parseDouble(components[1]);
							result = this.manager.putMoneyIn(cuenta, cantidad);
							if(result)
								this.output.writeBytes("pv\n");
							else
								this.output.writeBytes("pi\n");
							break;
						case "T":
							cantidad = Double.parseDouble(components[1]);
							TransCuenta = Integer.parseInt(components[2]);
							result = this.manager.Transfer(this.cuenta, TransCuenta, cantidad);
							if(result)
								this.output.writeBytes("pv\n");
							else
								this.output.writeBytes("pi\n");
							break;
						case "I":
							this.active = false;
							this.output.writeBytes("pv\n");
							break;
					}
					
				}
				else if(!this.accepted && petition.equals("V")){
					this.cuenta = Integer.parseInt(components[1]);
					this.accepted = this.manager.validate(this.cuenta, components[2]);
					if(this.accepted)
						this.output.writeBytes("pv\n");
					else
						this.output.writeBytes("pi\n");
				}
				this.output.flush();
			} catch (IOException | NumberFormatException | ClassNotFoundException | SQLException e) {
				this.active = false;
				e.printStackTrace();
			}
		}
		try {
			this.manager.Close();
			this.output.close();
			this.input.close();
			this.client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
