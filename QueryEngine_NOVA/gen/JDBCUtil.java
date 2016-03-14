
package gen;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JDBCUtil {
   	private static String usr ="serf";
   	private static String pwd ="serfdb";
   	private static String url ="jdbc:postgresql://localhost:5432/serfdb";
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