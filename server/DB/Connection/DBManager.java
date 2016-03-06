package server.DB.Connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	protected Conector conector;
	protected Connection connection;
	protected Statement statement;
	protected ResultSet result;
	
	public void Connect() throws ClassNotFoundException, SQLException{
		this.conector = new Conector();
		this.connection = this.conector.getConnection();
		this.statement = this.connection.createStatement();
	}
	
	public void Close() throws SQLException{
		this.statement.close();
		this.connection.close();
		this.conector.close();
	}
}
