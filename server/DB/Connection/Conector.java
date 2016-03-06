package server.DB.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
	private static Connection connection;
	private String server = "jdbc:mysql://localhost/BoxContest";
	private String user = "root";
	private String passwd = "123456";
	private String driver = "com.mysql.jdbc.Driver";
	
	public Conector() throws ClassNotFoundException, SQLException{
		Class.forName(this.driver);
		this.connection = DriverManager.getConnection(server, user, passwd);
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public void close() throws SQLException{
		this.connection.close();
	}
}
