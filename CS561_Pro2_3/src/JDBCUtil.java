

import java.sql.*;

/**
 * @author Pengpeng GE
 * CWID :10405696
 * 
 * The class is JDBCUtil class. This class include the getConnection function to connect to database.
 * 
 * Users can modify the user name and password to connect to user's database.
 * 
 */
public class JDBCUtil {

	public Connection getConnection() // The getConnection function to connect to database
	{
		// load driver
		
		//The try block contains a block of program statements within which an exception might occur.
		try {
			//Class.forName causes the class named "org.postgresql.Driver" to be loaded.
			Class.forName("org.postgresql.Driver");
			System.out.println("Successfully loaded the driver!");
		} 
		//The catch block contains error handling code
		catch (ClassNotFoundException e) {
			System.out.println("Failed to load the driver!");
			e.printStackTrace();
		}
		
		String url="jdbc:postgresql://localhost:5432/serfdb";
		//user name of the database
		String user="postgres";
		//password of the database
		String password="admin";
		//the class of DriverManager called the getConnection function
		try {
			Connection conn=DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException e) {
			System.out
			.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}
		return null;
	}
	

}
