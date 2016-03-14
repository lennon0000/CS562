package gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main {

   public static void main(String[] args) throws SQLException {
      Connection conn = JDBCUtil.getConn();
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sales");
      ResultSet rs = stmt.executeQuery();
      Set<MFStructure> records = new HashSet<MFStructure>();

      while (rs.next()) {

         int quant = rs.getInt("quant");
         String prod = rs.getString("prod").trim();
         String cust = rs.getString("cust").trim();
            MFStructure mfStructure = new MFStructure(prod , cust);
            if (!records.contains(mfStructure)) {
               mfStructure.cust = cust;
               mfStructure.prod = prod;
               mfStructure.max = max;
               mfStructure.min = min;
               mfStructure.avg = avg;
               records.add(mfStructure);
            } 
      }

      ResultSet rs0 = stmt.executeQuery();
      while (rs0.next()) {

         int quant = rs0.getInt("quant");
         String prod = rs0.getString("prod").trim();
         String cust = rs0.getString("cust").trim();
               for (Iterator iterator = records.iterator(); iterator.hasNext();) {

                  MFStructure record = (MFStructure) iterator.next();

                     if( (prod+"").equals(record.getProd()+"") && (cust+"").equals(record.getCust()+"")){
                           record.count_quant_1 ++;
                           record.sum_quant_1 += quant;
                     }
                     if( (cust+"").equals(record.getCust()+"")){
                           record.sum_quant_2 += quant;
                           record.count_quant_2 ++;
                     }
               }
      }
      System.out.println("cust                     prod                     max                      min                      avg                      ");
      System.out.println("====                     ====                     ===                      ===                      ===                      ");
      int i = 0;
      for (MFStructure record : records) {

            System.out.printf("%-25s",record.getCust());
            System.out.printf("%-25s",record.getProd());
            System.out.printf("%-25s",record.getMax());
            System.out.printf("%-25s",record.getMin());
            System.out.printf("%-25s",record.getAvg());
            System.out.println();
            i++;
      }

      System.out.println("("+i+" rows)");
   }
}
