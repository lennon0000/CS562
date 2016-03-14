package cn.wjx.placetest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
/*
(1)To execute this program, you need to connect to the database postgresql,create table sales,
    and the sql is 'create table sales(cust char(12),prod char(12),day int,mon int,year int,state char(8),quant bigint)'
(2)insert all those 500 records into the table sales, the sql is 'psql -U jwang81 -W jwang81 -f sdap.sql'
(3)then you can run this program
*/

/*
(4)There are two class, class Record and class QuantAvg
(5)The attributes of class Record are cust(customer), product, custAvg(it means the customer's average quantity of sales), otherAvg(it means the other customer's average quantity of sales for the same kind of product).
(6)Class QuantAvg means the average quantity  
(7)The attributes of class QuantAvg are quant,count,avg(it means the average quantity of this product for the customer )
(8)the schema is (cust,prod,day,mon,year,state,quant)

'*/
public class Sample {
	public static void main(String[] args) throws SQLException {
		Connection conn = JDBCUtil.getConn();// get connection with database
		
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sales");
		
		ResultSet rs = stmt.executeQuery();//execute query
		Set<Record> records = new HashSet<Record>();
		while (rs.next()) {
			String cust = rs.getString("cust");
			String prod = rs.getString("prod");
			int quant = rs.getInt("quant");
			
			Record record = new Record(cust,prod);
			if(records.contains(record)){     // to check if there exist such record in the list of sorted records 
				for(Record r : records){     //whose cust and prod is the same as the new one
					if(r.getProduct().equals(prod)){
						if(r.getCust().equals(cust)){//if there is one record whose cust and prod are the same as the new one, 
							r.updateCustAvg(quant);//then use the data of the new one to update the original record
						}
					}
				}
			}else{// when there is no same record in the list of sorted record, then add a new record
				record.addAvg(quant);
				records.add(record);
			}
		}
		
		for(Record r : records){
			for(Record r2: records){
				if(r.getProduct().equals(r2.getProduct())&&(!r.getCust().equals(r2.getCust()))){
					r.updateOtherAvg(r2.getCustAvg());
				}
			}
		}

		System.out.println("      CUSTOMER          PRODUCT      CUST_AVG   OTHER_AVG");
		System.out.println("      ========          =======      ========   =========");
		for(Record record : records){
			System.out.printf("%18s",record.getCust());
			System.out.printf("%18s",record.getProduct());
			System.out.printf("%5d",record.getCustAvg().getAvg());
			System.out.print("      ");
			System.out.printf("%5d",record.getOtherAvg().getAvg());
			System.out.println();
		}
	}
}


