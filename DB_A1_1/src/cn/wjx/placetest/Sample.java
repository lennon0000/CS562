package cn.wjx.placetest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
	//To execute this program, you need to connect to the database postgresql,create table sales,
	//and the sql is 'create table sales(cust char(12),prod char(12),day int,mon int,year int,state char(8),quant bigint)'
    //insert all those 500 records into the table sales, the sql is 'psql -U jwang81 -W jwang81 -f sdap.sql'
    //then you can run this program

	//There are two class, class Param and class Record,and the relationship between Record and Param is oneToMany
	//The attributes of class Param are name, quant, count
	//The attributes of class Record are cust, product and List of Param 
	//In my local database, the column of attribute customer is 'cust'
	//In my local database, the column of attribute product is 'prod'
	//In my local database, the column of attribute state is 'state'
	//In my local database, the column of attribute quantity is 'quant'
public class Sample {
	public static void main(String[] args) throws SQLException {
		Connection conn = JDBCUtil.getConn();//get connection with database
		
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sales");
		
		ResultSet rs = stmt.executeQuery();//execute query
		Set<Record> records = new HashSet<Record>();
		while (rs.next()) {
			String cust = rs.getString("cust");
			String prod = rs.getString("prod");
			String state = rs.getString("state");
			int quant = rs.getInt("quant");
			Record record = new Record(cust,prod);
			Param param = new Param(state.trim(),quant);
			if(records.contains(record)){//to check if both of customer and product are the same
				for(Record r : records){
					if(r.equals(record)){
						r.addParam(param);//if both customer and product are the same then update it
					}
				}
			}else{
				record.addParam(param);
				records.add(record);
			}
		}

		System.out.println("      CUSTOMER          PRODUCT    NY_AVG   NJ_AVG   CT_AVG");
		System.out.println("      ========          =======    ======   ======   ======");
		for(Record record : records){
			System.out.printf("%18s",record.getCust());
			System.out.printf("%18s",record.getProduct());
			List<Param> ps = record.getParams();
			for(Param p: ps){
				System.out.printf("%5d", Integer.parseInt(p.toString()));
				System.out.print("    ");
			}
			System.out.println();
		}
	}
}


