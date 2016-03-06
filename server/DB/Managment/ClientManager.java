package server.DB.Managment;

import java.sql.SQLException;

import server.DB.Connection.DBManager;
import server.pojos.Client;

public class ClientManager extends DBManager{
	
	public synchronized boolean validate(int NoCuenta, String passwd) throws ClassNotFoundException, SQLException{
		String Client_passwd;
		String query = "select clientes.passwd, clientes.nombre, clientes.appat, cuenta.balance "
					 + "from cuenta "
					 + "left join clientes "
					 + "on cuenta.id_cliente = clientes.id "
					 + "where cuenta.id = " + NoCuenta + ";";
		
		super.Connect();
		this.result = this.statement.executeQuery(query);
		
		if(this.result.next()){
			Client cliente = new Client(
					this.result.getString("nombre"),
					this.result.getString("ApPat"),
					this.result.getDouble("balance")
			);
			
			Client_passwd = this.result.getString("passwd");
			
			if(Client_passwd.equals(passwd))
				return true;
		}
		
		return false;
	}
	
	public synchronized double ConsultMoney(int cuenta) throws ClassNotFoundException, SQLException{
		String query = "select balance from cuenta "
					 + "where id = " + cuenta + ";";
		
		super.Connect();
		this.result = this.statement.executeQuery(query);
		
		if(this.result.next()){
			return this.result.getDouble("balance");
		}
		return -1;
	}
	
	public synchronized boolean getMoneyOut(int cuenta, double cantidad) throws ClassNotFoundException, SQLException{
		double saldo_actual = -1;
		String query = "select balance "
					 + "from cuenta "
					 + "where id = " + cuenta + ";";
		
		super.Connect();
		this.result = this.statement.executeQuery(query);
		
		if(this.result.next()){
			saldo_actual = this.result.getDouble("balance");
			if((cantidad > 0) && (cantidad <= saldo_actual)){
				saldo_actual -= cantidad;
				
				query = "update cuenta "
					  + "set balance = " + saldo_actual + " "
					  + "where id = " + cuenta + ";";
				
				this.statement.executeUpdate(query);
				
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean putMoneyIn(int cuenta, double cantidad) throws ClassNotFoundException, SQLException{
		double saldo_actual;
		String query = "select balance from cuenta "
				     + "where id = " + cuenta + ";";
		
		super.Connect();
		this.result = this.statement.executeQuery(query);
		
		if(this.result.next()){
			saldo_actual = this.result.getDouble("balance");
			if(cantidad > 0){
				saldo_actual += cantidad;
			}
			
			query = "update cuenta "
				  + "set balance = " + saldo_actual + " "
				  + "where id = " + cuenta + ";";
			
			this.statement.executeUpdate(query);
			
			return true;
		}
		return false;
	}
	
	public synchronized boolean Transfer(int cuenta, int TransCuenta, double cantidad) throws ClassNotFoundException, SQLException{
		double actual_saldo;
		actual_saldo = this.ConsultMoney(cuenta);
		if((actual_saldo > 0) && (cantidad <= actual_saldo)){
			return this.getMoneyOut(cuenta, cantidad) && this.putMoneyIn(TransCuenta, cantidad); 
		}
		return false;
	}
}
