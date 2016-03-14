/**
 * @author Pengpeng GE
 *CWID :10405696
 *This is the main class, the class include the main function.
 * 
 * Basic:
 * 1.Get the Connection object by using the getConn method defined by Class JDBCUtil.
 * 2.Get the Statement object by Connection object.
 * 3.Execute the sql statement and get the ResultSet. 
 * 4.Retrieve the details of each row by using statement.getString and so on.
 * 
 * Algorithm:
 * 1.initial the Record object by cust and prod.
 * 2.while there is still object for this statement, then retrieve the informations of next object.
 * 3.After getting the details of the new row, check if the combination of this cust and prod exist in the result list.
 * 4.if exist, then update its informations, like maximum value.
 * 5.if not, initial the rest of the informations of that Record object, and save it to the list.
 * 
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class main {
	public static void main(String[] args) throws SQLException {
		JDBCUtil jdbc = new JDBCUtil();
		Connection conn = jdbc.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from sales");
		System.out.println("Successfully connected to the server!");

		List<Record> records = new ArrayList<Record>();

		while (rs.next()) {
			String customer = rs.getString("cust");
			String product = rs.getString("prod");
			int quant = rs.getInt("quant");
			int month = rs.getInt("month");

			Record record = new Record(customer, product);
			if (records.isEmpty()) {
				record.updateQuarter(month, quant);
				records.add(record);
			} else {
				boolean exist = false;
				for (Record r : records) {
					if (r.getCustomer().equals(record.getCustomer())
							&& r.getProduct().equals(record.getProduct())) {
						r.updateQuarter(month, quant);
						exist = true;
					}
				}
				if (!exist) {
					record.updateQuarter(month, quant);
					records.add(record);
				}
			}

		}
		ResultSet rs2 = stmt.executeQuery("select * from sales");
		
		while (rs2.next()) {
			String customer = rs2.getString("cust");
			String product = rs2.getString("prod");
			int quant = rs2.getInt("quant");
			int month = rs2.getInt("month");
			Record record = new Record(customer, product);
			for (Record r:records) {
				if (r.getCustomer().equals(record.getCustomer()) && r.getProduct().equals(record.getProduct())) {
					r.update(month,quant);
				}
			}
		}
		
		int k = 1;
		System.out
				.println("CUSTOMER    PRODUCT    SEASON       BEFORE           AFTER          ");
		System.out
				.println("========    =======    ======       ==========       ==========          ");
		// int i = 1;
		for (Record r : records) {
			List<Quarter> qs = r.getQuarters();
			for (Quarter q : qs) {
				int i = Integer.parseInt(q.getQuarter().substring(1));
//				String before = "<NULL>";
//				String after = "<NULL>";
				int b_count = q.getB_count();
				int a_count = q.getA_count();
//				
				int a = (int)Math.rint(q.getAvg());
				int m = q.getMin();
				System.out.printf("%1d", k);
				k++;
				System.out.printf("%14s", r.getCustomer());
				System.out.printf("%14s", r.getProduct());
//				System.out.printf("%14d", a);
//				System.out.printf("%14d", m);
				System.out.printf("%8s", q.getQuarter());
				if (b_count == 0) {
					System.out.printf("%14s", "<NULL>");
				} else {
					System.out.printf("%14s", b_count);
				}
				if (a_count == 0) {
					System.out.printf("%14s", "<NULL>");
				} else {
					System.out.printf("%13s", a_count);
				}
				System.out.println();

			}

		}

	}

}
