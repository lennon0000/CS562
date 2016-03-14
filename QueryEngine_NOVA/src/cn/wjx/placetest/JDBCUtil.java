package cn.wjx.placetest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 
 * @author wangjingxu
 *	connecting to database
 *	
 *	to test this program, you need to change the user name and password and also the url.
 *	(morever you also need to change those value in another file(/source/JDBCUtil.java) which will out put after running this program)
 */
public class JDBCUtil {
	
	private static String usr ="serf";
	private static String pwd ="serfdb";
	private static String url ="jdbc:postgresql://localhost:5432/serfdb";
	
	public static Connection getConn(){
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Loading the JDBC Driver successfully!");
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Connecting to the JDBC Server successfully!");
			return conn;
		} catch (ClassNotFoundException e) {
			System.out.println("Fail to loading Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
