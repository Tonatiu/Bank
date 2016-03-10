package server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread{
	private int port = 3035;
	private ServerSocket server;
	private Socket cliente;
	private boolean active = true;
	
	public Connection() throws IOException{
		this.server = new ServerSocket(port);
	}
	
	public void run(){
		System.out.println("Server ready");
		while(active){
			try {
				this.cliente = this.server.accept();
				Service service = new Service(cliente);
				new Thread(service).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
