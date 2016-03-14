package cn.wjx.placetest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * 
 * @author wangjingxu
 * Main function of this program
 * You can test this program by running this main function
 * 
 * Please run this program in MAC OS
 * 
 * Before running this program, you need to change the connection information, for example the user_name and password, you can change it in the JDBCUtil.java
 * Please also change those information in the file /source/JDBCUtil.java(which is used to generate a JDBCUtil for a new project)
 * 
 * The testing input file is store in the file /source/
 * The testing file emf-1 ---emf-6 is the implement of those 6 example in slide Ad-Hoc_OLAP_Query_Processing.pdf, and its sql is in the file /query/
 * 
 * When you run this program, you can chose the testing file by fileChoser
 * After running this program, the generated .java files is in the file /gen/
 * 
 * In order to run this generated .java project, you need to copy those 3 files into the project TestGen which was also attached in this submission

 */
public class Main {

	public static void main(String[] args) throws SQLException {


		Connection conn = JDBCUtil.getConn();// get connection with database
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sales");

		ResultSet rs = stmt.executeQuery();// execute query
		ResultSetMetaData data = rs.getMetaData();// get the information of the
													// column

		Set<Attribute> attsFromTable = new HashSet<Attribute>();
		
		for (int i = 1; i <= data.getColumnCount(); i++) {
			String columnName = data.getColumnName(i);
			String columnType = data.getColumnTypeName(i);
			Attribute att = new Attribute(columnName, columnType);
			attsFromTable.add(att);
		}
		FileReader fr = new FileReader();
		MFStructure mfs = new MFStructure();
		Map m = fr.readTxtFile();
		
		String numberOfGv = (String) m.get("numberOfGv");
		Set<String> attsOfMFS = (Set<String>) m.get("attsOfMFS");
		Set<String> selConditions = (Set<String>) m.get("selConditions");
		
		List<String> selectAtts = (List<String>) m.get("selectAtts");
		
		Set<String> groupingAtts = (Set<String>) m.get("groupingAtts");
		
		
		
		List<String> selectAttsForPr = (List<String>) m.get("selectAttsForPr");	
		
		List<Attribute> groupingAttList = new ArrayList<Attribute>();

		for (Iterator iterator = groupingAtts.iterator(); iterator.hasNext();) {
			String attribute = (String) iterator.next();
			for (Iterator iterator2 = attsFromTable.iterator(); iterator2
					.hasNext();) {
				Attribute att = (Attribute) iterator2.next();
				if(att.getAttName().equals(attribute)){
					groupingAttList.add(att);
					break;
				}
			}
		}
		
		
		String havingCondition = (String) m.get("havingCondition");	
		
		JDBCUtilGenerator jdbc = new JDBCUtilGenerator();
		jdbc.generateJDBCUtil();
		
		Map m2 = mfs.genMFStructure(attsFromTable,attsOfMFS,groupingAttList);
		Set<Attribute> noneAggAtt = (Set<Attribute>) m2.get("noneAggAtt");
		Set<Aggregation> aggs = (Set<Aggregation>) m2.get("aggs");	
		
		
		Condition c = new Condition();
		String havingCons = c.getHavingConditions(havingCondition);
		
		for (Iterator iterator = selConditions.iterator(); iterator.hasNext();) {
			String selCondictionStr = (String) iterator.next();
			Condition selCondiction = new Condition(selCondictionStr);
			
			String att = selCondiction.getAttibute();
			for (Iterator atts = attsFromTable.iterator(); atts
					.hasNext();) {
				Attribute attribute = (Attribute) atts.next();
				if(attribute.getAttName().equals(att)){
					Attribute a = new Attribute();
					a.attName = attribute.getAttName();
					a.attType = attribute.getAttType();
					noneAggAtt.add(a);
					break;
				}
			}
		}
		Condition[][] selectCons = new Condition().getDepCons();
		
		boolean hasdepCon = new Condition().hasDepConWhole;
		if(hasdepCon){
			for(int i = 1; i<selectCons.length;i++){
				for (int j = 0; j < selectCons[i].length; j++) {
					if(selectCons[i][j]!=null){
						String selNum = selectCons[i][j].getNumberOfGv();
						for(int z = 0; z<selectCons[0].length;z++){
							if(selectCons[0][z]!=null&&selectCons[0][z].getNumberOfGv().equals(selNum)){//fetch all the condition for  
								for (int j2 = 0; j2 < selectCons[i].length; j2++) {
									if(selectCons[i][j2]==null){
										selectCons[i][j2] = selectCons[0][z];
										selectCons[0][z] = null;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		MainFunctionGenerator main = new MainFunctionGenerator();
		main.generateMain(selectCons,groupingAtts,aggs,noneAggAtt,selectAtts,selectAttsForPr,numberOfGv,havingCons);
		System.out.println("-------------------");

	}
}
