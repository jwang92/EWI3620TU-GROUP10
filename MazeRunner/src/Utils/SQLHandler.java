package Utils;

import java.sql.*;

public class SQLHandler {
	
	private Connection c = null;
	private String dbName = "bestegroepjeooit";
	private String dbUser = "johnbeton";
	private String dbPass = "appeltaart";
	
	public SQLHandler(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://sql.ewi.tudelft.nl/"+dbName, dbUser, dbPass);
		      
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	
	/**
	 * ask database for data
	 * @param sql
	 * @return
	 */
	public ResultSet query(String sql){
		
		ResultSet r = null;
		try {
			Statement s = c.createStatement();
			r = s.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
		
	}
	
	/**
	 * writes data to database
	 * @param sql
	 * @return te be written data
	 */
	public boolean updatequery(String sql){
		
		try {
			Statement s = c.createStatement();
			s.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public void close(){
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
