package cn.wjx.placetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MFStructure {
	// String numberOfGv;
	// Set<String> attsOfMFS = new HashSet<String>();
	Set<Attribute> attsFromTable = new HashSet<Attribute>();
	Set<Attribute> noneAggAtt = new HashSet<Attribute>();// store non-aggregation
	Set<Aggregation> aggs = new HashSet<Aggregation>();// 
	public MFStructure() {
	};

	PrintWriter pWriter;

	/**
	 * 
	 * @param attsFromTable
	 *            all of the columns , eg. cust，prod，quant
	 * @param attsOfMFS2
	 *            all the string value from emf-* file，eg. cust，1_sum_quant
	 *            first generate non-agg-att, eg. cust，prod
	 * 
	 *            generete aggregation , eg.（1_sum_quant,avg(quant)）
	 *            generate those Aggregation object, store it into HashSet
	 * 
	 *            iterate all the aggregations
	 *            
	 * @param groupingAttList
	 * 
	 * return a map，which including aggregation and non-aggregation set
	 */
	public Map genMFStructure(Set<Attribute> attsFromTable,
			Set<String> attsOfMFS2, List<Attribute> groupingAttList) {
		this.attsFromTable.addAll(attsFromTable);
		Map m = new HashMap<String, Set>();
		Properties prop = System.getProperties();
		String filePath = "";
		String osType = prop.getProperty("os.name");
		if (osType.equals("Mac OS X")) {
			String path = this.getClass().getResource("/").getFile().toString();
			String p = path.split("/bin/")[0];
			filePath = p + "/gen/MFStructure.java";
		} else {
			String path = this.getClass().getResource("\\").getFile()
					.toString();
			String p = path.split("\\bin\\")[0];
			filePath = p + "\\gen\\MFStructure.java";
		}

		try {
			pWriter = new PrintWriter(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pWriter.print("package gen;\r\n\r\n");
		pWriter.print("public class MFStructure {\r\n");
		pWriter.print("\r\n");
		/**
		 */
		
		for (Iterator iterator = attsOfMFS2.iterator(); iterator.hasNext();) {
			String attOfMFS = (String) iterator.next();
			for (Iterator iterator2 = attsFromTable.iterator(); iterator2
					.hasNext();) {
				Attribute att = (Attribute) iterator2.next();
				String attNameFromTable = att.attName;
				if (attOfMFS.equals(attNameFromTable)) {
					pWriter.print("   public " + att.getAttType() + " "
							+ attNameFromTable + ";\r\n");
					pWriter.print("   public "
							+ att.getAttType()
							+ " "
							+ "get"
							+ attNameFromTable.replaceFirst(attNameFromTable
									.substring(0, 1), attNameFromTable
									.substring(0, 1).toUpperCase())
							+ "(){;\r\n");
					pWriter.print("        return "
							+ attNameFromTable.replaceFirst(attNameFromTable
									.substring(0, 1), attNameFromTable
									.substring(0, 1).toLowerCase()) + ";\r\n");
					pWriter.print("   }\r\n ");
					noneAggAtt.add(att);
				}
			}
		}
		/**
		 * for aggregation object,like avg(quant),1_sum_quant
		 * convert the String value of agg into Aggregation object
		 */
		for (Iterator iterator = attsOfMFS2.iterator(); iterator.hasNext();) {
			String attOfMFS = (String) iterator.next();
			
			if((attOfMFS.contains("+")||attOfMFS.contains("-")||attOfMFS.contains("*")||attOfMFS.contains("/"))&&!attOfMFS.contains("_*")){
				String[] ops = {"/","*","+","-"};
				for (int i = 0; i < ops.length; i++) {
					if(attOfMFS.contains(ops[i])){
						String[] atts = attOfMFS.split("\\"+ops[i]);
						String left = atts[0];
						String right = atts[1];
						trans(left);
					}
				}
			}else{
				trans(attOfMFS);
			}
			
		}
		/**
		 * iterate those aggregations object, generate MFStructure, including getMethod
		 */
		for (Iterator iterator = aggs.iterator(); iterator.hasNext();) {
			Aggregation agg = (Aggregation) iterator.next();
			if (agg.attribute.matches("\\*")) {
				pWriter.print("   public int " + agg.aggType + "_"
						+ agg.getNumber() + ";\r\n");
				pWriter.print("   public int get"
						+ agg.aggType.replaceFirst(agg.aggType.substring(0, 1),
								agg.aggType.substring(0, 1).toUpperCase())
						+ "_" + agg.getNumber() + "(){\r\n");
				pWriter.print("       return "
						+ agg.aggType.replaceFirst(agg.aggType.substring(0, 1),
								agg.aggType.substring(0, 1).toLowerCase())
						+ "_" + agg.getNumber()
						+ ";\r\n   }\r\n");
			} else {
				if (agg.aggType.equals("avg")) {
					pWriter.print("   public float " + agg.aggType + "_"
							+ agg.attribute + "_" + agg.getNumber() + ";\r\n");
					pWriter.print("   public float get"
							+ agg.aggType.replaceFirst(agg.aggType.substring(0,
									1), agg.aggType.substring(0, 1)
									.toUpperCase()) + "_" + agg.attribute + "_"
							+ agg.getNumber() + "(){\r\n");
					pWriter.print("       return "
							+ "(float)sum_"+ agg.attribute + "_"
							+ agg.getNumber()+"/count_"+ agg.attribute + "_"
							+ agg.getNumber() + ";\r\n   }\r\n");
				} else {
/*
 */
					pWriter.print("   public int " + agg.aggType + "_"
							+ agg.attribute + "_" + agg.getNumber() + ";\r\n");
					pWriter.print("   public int get"
							+ agg.aggType.replaceFirst(agg.aggType.substring(0,
									1), agg.aggType.substring(0, 1)
									.toUpperCase()) + "_" + agg.attribute + "_"
							+ agg.getNumber() + "(){\r\n");

					pWriter.print("       return "
							+ agg.aggType.replaceFirst(agg.aggType
									.substring(0, 1), agg.aggType
									.substring(0, 1).toLowerCase()) + "_"
							+ agg.attribute + "_" + agg.getNumber()
							+ ";\r\n   }\r\n");
				}

			}

		}
		
		pWriter.print("  public MFStructure() {}\r\n");
		
		if(groupingAttList.size()!=0){
			pWriter.print("  public MFStructure(");
			for (Iterator iterator = groupingAttList.iterator(); iterator.hasNext();) {
				Attribute groupingAtt = (Attribute) iterator.next();
				pWriter.print(groupingAtt.getAttType()+" "+groupingAtt.getAttName());
				if(iterator.hasNext()){
					pWriter.print(",");
				}
			}
			pWriter.print("){");
			for (Iterator iterator = groupingAttList.iterator(); iterator.hasNext();) {
				Attribute groupingAtt = (Attribute) iterator.next();
				pWriter.print("this."+groupingAtt.getAttName()+" = "+groupingAtt.getAttName()+";");
				
			}
			pWriter.print("}\r\n");
		}
		
		if(groupingAttList.size()!=0){
			/**
			 * generate hashCode and equals override method
			 */
			pWriter.print("  @Override\r\n");
			pWriter.print("  public int hashCode(){\r\n");
			pWriter.print("      final int prime = 31;\r\n");
			pWriter.print("      int result = 1;\r\n");
			for (Iterator iterator = groupingAttList.iterator(); iterator.hasNext();) {
				Attribute ga = (Attribute) iterator.next();
				
				if(!ga.getAttType().equals("String")){
					pWriter.print("      result = prime * result +" + ga.getAttName()+";\r\n");
				}else{
				
					pWriter.print("      result = prime * result +((" + ga.getAttName()
							+ " == null) ? 0 :" + ga.getAttName() + ".hashCode());\r\n");
				}
				
				
			}
			pWriter.print("      return result;\r\n");
			pWriter.print("  }\r\n");

			pWriter.print("  @Override\r\n");
			pWriter.print("  public boolean equals(Object obj) {\r\n");
			pWriter.print("     if (this == obj)\r\n");
			pWriter.print("       return true;\r\n");
			pWriter.print("     if (obj == null)\r\n");
			pWriter.print("       return false;\r\n");
			pWriter.print("     if (getClass() != obj.getClass())\r\n");
			pWriter.print("       return false;\r\n");
			pWriter.print("     MFStructure other = (MFStructure) obj;\r\n");
			for (Iterator iterator = groupingAttList.iterator(); iterator.hasNext();) {
				Attribute ga = (Attribute) iterator.next();
				if(ga.getAttType().equals("String")){
					pWriter.print("     if (" + ga.getAttName() + " == null) {\r\n");
					pWriter.print("     if (other." + ga.getAttName() + " != null) \r\n");
					pWriter.print("       return false; \r\n");
					pWriter.print("     } else if (!" + ga.getAttName() + ".equals(other." + ga.getAttName()
							+ " )) \r\n");
					pWriter.print("       return false; \r\n");
				}else{
					pWriter.print("     if (" + ga.getAttName() + " !="+"other."+ga.getAttName()+")\r\n");
					pWriter.print("     return false; \r\n");
				}
			}
			pWriter.print("    return true;\r\n");

			pWriter.print("\r\n");
			pWriter.print("  }\r\n");
		}
		
		pWriter.print("}\r\n");
		pWriter.flush();
		System.out.println("The MF-Structure class is generated successfully!");
		System.out.println("The generated MFStructure.java file path is : "
				+ filePath);
		System.out.println();
		m.put("noneAggAtt", noneAggAtt);
		m.put("aggs", aggs);

		return m;
	}

	private void trans(String attOfMFS) {
		if (attOfMFS.contains("(")) {
			String[] ss = attOfMFS.split("\\(");
			String attName = ss[1].split("\\)")[0];

			if (ss[0].equals("avg")) {
				Aggregation a1 = new Aggregation("0", ss[0], attName);
				aggs.add(a1);
				Aggregation a2 = new Aggregation("0", "sum", attName);
				aggs.add(a2);
				Aggregation a3 = new Aggregation("0", "count", attName);
				aggs.add(a3);
				
//				Attribute att = new Attribute(columnName, columnType)
				addAttToNoneAgg(attName);//updata noneAggAtt set
				
			} else {
				Aggregation a = new Aggregation("0", ss[0], attName);
				aggs.add(a);
				addAttToNoneAgg(attName);
				
				
			}
		} else if (attOfMFS.contains("_")) {
			String[] aggAtts = attOfMFS.split("_");
			if(aggAtts.length==3){
				if (aggAtts[1].equals("avg")) {
					Aggregation a1 = new Aggregation(aggAtts[0], "sum",
							aggAtts[2]);
					Aggregation a2 = new Aggregation(aggAtts[0], "count",
							aggAtts[2]);
					aggs.add(a1);
					aggs.add(a2);
					addAttToNoneAgg(aggAtts[2]);
				}
				Aggregation a = new Aggregation(aggAtts[0], aggAtts[1],
						aggAtts[2]);
				aggs.add(a);
				addAttToNoneAgg(aggAtts[2]);
			}else{
			}
		}
	}

	private void addAttToNoneAgg(String attName) {//add the att and attType into the set
		
		for (Iterator iterator2 = attsFromTable.iterator(); iterator2
				.hasNext();) {
			Attribute att = (Attribute) iterator2.next();
			if(att.getAttName().equals(attName)){
				Attribute a = new Attribute();
				a.attName = att.getAttName();
				a.attType = att.getAttType();
				noneAggAtt.add(a);
				break;
			}
			
		}
		
	}
}
