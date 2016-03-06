package server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import server.DB.Managment.ClientManager;

public class Service implements Runnable{
	private Socket client;
	private DataOutputStream output;
	private DataInputStream input;
	private boolean active = true;
	private boolean accepted = false;
	private ClientManager manager;
	private int cuenta;
	
	public Service(Socket client) throws IOException{
		this.client = client;
		this.output = new DataOutputStream(this.client.getOutputStream());
		this.input = new DataInputStream(this.client.getInputStream());
		this.manager = new ClientManager();
	}
	
	@Override
	public void run() {
		String structure;
		String petition;
		double cantidad;
		boolean result;
		int TransCuenta;
		
		while(this.active){
			try {
				structure = this.input.readUTF();
				String[] components = structure.split("-");
				petition = components[0];
				
				if(petition.equals("validar")){
					this.cuenta = Integer.parseInt(components[1]);
					this.accepted = this.manager.validate(this.cuenta, components[2]);
				}
				if(this.accepted){
					switch(petition){
						case "consultar":
							double saldo = this.manager.ConsultMoney(cuenta);
							this.output.writeDouble(saldo);
							break;
						case "retirar":
							cantidad = Double.parseDouble(components[1]);
							result = this.manager.getMoneyOut(cuenta, cantidad);
							this.output.writeBoolean(result);
							break;
						case "depositar":
							cantidad = Double.parseDouble(components[1]);
							result = this.manager.putMoneyIn(cuenta, cantidad);
							this.output.writeBoolean(result);
							break;
						case "transferir":
							cantidad = Double.parseDouble(components[1]);
							TransCuenta = Integer.parseInt(components[2]);
							result = this.manager.Transfer(this.cuenta, TransCuenta, cantidad);
							this.output.writeBoolean(result);
							break;
						case "salir":
							this.active = false;
							break;
					}
				}
				this.output.flush();
			} catch (IOException | NumberFormatException | ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
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
