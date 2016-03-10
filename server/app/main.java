package server.app;

import java.io.IOException;

import server.connection.Connection;

public class main {
	public static void main(String[] args) throws IOException{
		Connection conexion = new Connection();
		new Thread(conexion).run();
	}
}
