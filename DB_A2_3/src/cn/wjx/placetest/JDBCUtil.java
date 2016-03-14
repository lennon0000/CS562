package cn.wjx.placetest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {//to connect to database
	
	private static String usr ="postgres";//user name of the database
	private static String pwd ="admin";//password of the database
	private static String url ="jdbc:postgresql://localhost:5432/serfdb";
	//TODO 111111
	public static Connection getConn(){
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");
			return conn;
		} catch (ClassNotFoundException e) {
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
