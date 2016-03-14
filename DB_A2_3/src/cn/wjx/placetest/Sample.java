package cn.wjx.placetest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
(1)To execute this program, you need to connect to the database postgresql,create table sales,
    and the sql is 'create table sales(cust char(12),prod char(12),day int,mon int,year int,state char(8),quant bigint)'
(2)insert all those 500 records into the table sales, the sql is 'psql -U jwang81 -W jwang81 -f sdap.sql'
(3)then you can run this program
*/

/*
(4)There are two class, class Record and class Avg,and the relationship between Record and Avg is oneToMany
(5)The attributes of class Record are cust(customer), product, avgs(List<Avg>)
(6)Class Avg means the average sale of one quarter 
(7)The attributes of class Avg are quarter, sum, count,years(List<Integer>),mons(List<Integer>),avg,max,quants(List<Integer>),see details in Avg.java 
(8)the schema is (cust,prod,day,mon,year,state,quant)

'*/
public class Sample {
	public static void main(String[] args) throws SQLException {
		Connection conn = JDBCUtil.getConn();// get connection with database

		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sales ");

		ResultSet rs = stmt.executeQuery();// execute query
//		rs.
		
		List<Record> records = new ArrayList<Record>();

		while (rs.next()) {
			String cust = rs.getString("cust");
			String prod = rs.getString("prod");
			int quant = rs.getInt("quant");
			int year = rs.getInt("year");
			int mon = rs.getInt("month");

			Record record = new Record(cust, prod);

			if (records.contains(record)) {// to check if there exist such record in the list of sorted records 
				                                             //whose cust and prod is the same as the new one
				Boolean m = false;                     //if there is one record whose cust and prod are the same as the new one, 
				for (Record r : records) {            //then use the data of the new one to update the original record
					if ((r.getCust()).equals(cust)
							&& (r.getProduct()).equals(prod)) {
																
						List<Avg> avgs = r.getAvgs();
						for (Avg a : avgs) {          //to check whether the quarter of the new record already exist
							if (a.check(mon)) {
								m = true;            //if exist,then Bollean m = true
							}
						}
					}
				}
				if (m) {                             //if the quarter of the new record exist in the list of records
					for (Record r : records) {       
						if ((r.getCust()).equals(cust)
								&& (r.getProduct()).equals(prod)) {//update it when both cust and prod are the same
							r.updateAvg(mon, year, quant);
						}
					}
				} else {// if the quarter of new record not exist in the original record, then add new quarter to it
					for (Record r : records) {
						if ((r.getCust()).equals(cust)
								&& (r.getProduct()).equals(prod)) {
							r.addAvg(mon, year, quant);
						}
					}
				}
			} else {// when there is no same record in the list of sorted record, then add a new record
				record.addAvg(mon, year, quant);
				records.add(record);
			}
		}

		System.out
				.println("  CUSTOMER      PRODUCT      QUARTER    BEFORE_TOT    AFTER_TOT");
		System.out
				.println("  ========      =======      =======    ==========    =========");
		
		for (Record record : records) {//print out each record
			List<Avg> avgs = record.getAvgs();
			if(avgs.size()!=4){//if there are not 4 quarter for the record, then add some quarter for the record by using 0
				record.addavg();
			}
			
			for (Avg a : avgs) {
				
				int i = Integer.parseInt(a.getQuarter().substring(1));
				
				int max = a.getMax();//get the max value of quantity of this quarter
				int avg = a.getAvg();//get the average value of quantity of this quarter
				String beforeCount;
				String afterCount;
				int bc = 0;//to count how many times that the sales from next quarter is between this quarter's average and max value
				int ac = 0;//to count how many times that the sales from previous quarter is between this quarter's average and max value
				for (Avg a2 : avgs) {
					int j = Integer.parseInt(a2.getQuarter().substring(1));
					if (j == i - 1) {
						List<Integer> quants = a2.getQuants();
						for(Integer q : quants){
							if(q>=avg&&q<=max){
								bc++;
							}
						}
					}
					if (j == i + 1) {
						List<Integer> quants = a2.getQuants();
						for(Integer q : quants){
							if(q>=avg&&q<=max){
								ac++;
							}
						}
					}
				}
				if(a.getQuarter().equals("Q1")){//if this quarter is Q1, then next quarter should be null
					beforeCount = "<NULL>";
				}else{
					beforeCount = bc+"";
				}
				if(a.getQuarter().equals("Q4")){//if this quarter is Q4, then previous quarter should be null
					afterCount = "<NULL>";
				}else{
					afterCount = ac+"";
				}
				
				System.out.printf("%14s", record.getCust());
				System.out.printf("%14s", record.getProduct());
				System.out.printf("%3s", a.getQuarter());
				System.out.printf("%19s", beforeCount);
				System.out.printf("%13s", afterCount);
				System.out.println();
			}
		}
	}
}
