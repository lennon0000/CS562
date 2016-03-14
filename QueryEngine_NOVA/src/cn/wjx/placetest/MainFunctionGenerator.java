package cn.wjx.placetest;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class MainFunctionGenerator {
	public void generateMain(Condition[][] selectCons,
			Set<String> groupingAtts, Set<Aggregation> aggs,
			Set<Attribute> noneAggAtts, List<String> selectAtts,
			List<String> selectAttsForPr, String numberOfGv,
			String havingCondition) {
		Properties prop = System.getProperties();
		String filePath = "";
		PrintWriter pWriter = null;
		String osType = prop.getProperty("os.name");
		if (osType.equals("Mac OS X")) {
			String path = this.getClass().getResource("/").getFile().toString();
			String p = path.split("/bin/")[0];
			filePath = p + "/gen/Main.java";
		} else {
			String path = this.getClass().getResource("\\").getFile()
					.toString();
			String p = path.split("\\bin\\")[0];
			filePath = p + "\\gen\\Main.java";
		}
		try {
			pWriter = new PrintWriter(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		pWriter.print("package gen;\r\n\r\n");
		pWriter.print("import java.sql.Connection;\r\n");
		pWriter.print("import java.sql.PreparedStatement;\r\n");
		pWriter.print("import java.sql.ResultSet;\r\n");
		pWriter.print("import java.sql.SQLException;\r\n");
		pWriter.print("import java.util.HashSet;\r\n");
		pWriter.print("import java.util.Iterator;\r\n");
		pWriter.print("import java.util.Set;\r\n\r\n");
		pWriter.print("public class Main {\r\n");
		pWriter.print("\r\n");
		pWriter.print("   public static void main(String[] args) throws SQLException {\r\n");
		pWriter.print("      Connection conn = JDBCUtil.getConn();\r\n");
		pWriter.print("      PreparedStatement stmt = conn.prepareStatement(\"SELECT * FROM Sales\");\r\n");
		pWriter.print("      ResultSet rs = stmt.executeQuery();\r\n");
		pWriter.print("      Set<MFStructure> records = new HashSet<MFStructure>();\r\n\r\n");

		pWriter.print("      while (rs.next()) {\r\n\r\n");
		/**
		 * iterate all the aggregation att,eg. cust/quant
		 * 
		 */
		if (noneAggAtts.size() != 0) {
			for (Iterator iterator = noneAggAtts.iterator(); iterator.hasNext();) {//
				Attribute att = (Attribute) iterator.next();
				if (att.attType.equals("String")) {
					pWriter.print("         "
							+ att.attType
							+ " "
							+ att.attName
							+ " = rs.get"
							+ att.attType.replaceFirst(att.attType.substring(0,
									1), att.attType.substring(0, 1)
									.toUpperCase()) + "(\"" + att.attName
							+ "\").trim();\r\n");
				} else {
					pWriter.print("         "
							+ att.attType
							+ " "
							+ att.attName
							+ " = rs.get"
							+ att.attType.replaceFirst(att.attType.substring(0,
									1), att.attType.substring(0, 1)
									.toUpperCase()) + "(\"" + att.attName
							+ "\");\r\n");
				}

			}
		}
		/**
		 * Get the condiction for "where"
		 * 
		 */

		int numOfGv = Integer.parseInt(numberOfGv);
		int a = 0;
		boolean hasIf = false;
		for(int i = 0; i<selectCons[0].length;i++){
			Condition con_0 = selectCons[0][i];
			if(con_0!=null){
				if (con_0.numberOfGv == "0") {
					if (a == 0) {
						pWriter.print("         if( ");
						hasIf = true;
						a++;
					} else {
						pWriter.print(" && ");
					}
					if (con_0.relation.contains("<>")) {
						pWriter.print("!(" + con_0.attibute + "+\"\")" + ".equals("
								+ con_0.condition + "+\"\")");
					} else if (con_0.relation.contains(">")
							|| con_0.relation.contains("<")) {
						pWriter.print(con_0.attibute + " " + con_0.relation + " "
								+ con_0.condition);
					} else {
						pWriter.print("(" + con_0.attibute + "+\"\")" + ".equals("
								+ con_0.condition + "+\"\")");
					}
				}
			}
		}
		if (hasIf) {
			pWriter.print("){\r\n");
		}
		if (groupingAtts.size() != 0) {
			pWriter.print("            MFStructure mfStructure = new MFStructure(");
			for (Iterator iterator2 = groupingAtts.iterator(); iterator2
					.hasNext();) {
				String gAttr = (String) iterator2.next();
				pWriter.print(gAttr);
				if (iterator2.hasNext()) {
					pWriter.print(" , ");
				}
			}
			pWriter.print(");\r\n");

		} else {
			pWriter.print("            MFStructure mfStructure = new MFStructure();\r\n");
		}
		
		pWriter.print("            if (!records.contains(mfStructure)) {\r\n");

		
		for (Iterator selectAtt = selectAtts.iterator(); selectAtt.hasNext();) {
			String att = (String) selectAtt.next();
			if (!att.contains("_") && !att.contains("(")) {
				pWriter.print("               mfStructure." + att + " = " + att
						+ ";\r\n");
			}
		}

		
		for (Iterator iterator2 = aggs.iterator(); iterator2.hasNext();) {
			Aggregation agg = (Aggregation) iterator2.next();

			if (Integer.parseInt(agg.getNumber()) == 0) {
				if (agg.aggType.equals("count")) {
					pWriter.print("               mfStructure.count_"
							+ agg.attribute + "_" + agg.number + " =1;\r\n");
				} else if (!agg.aggType.equals("avg")) {
					pWriter.print("               mfStructure." + agg.aggType
							+ "_" + agg.attribute + "_" + agg.number + " = "
							+ agg.attribute + ";\r\n");
				}
			}
		}

		pWriter.print("               records.add(mfStructure);\r\n");
		pWriter.print("            } \r\n");
		/**
		 * ------------------------------------------------------------------------**************************
		 */
		
		boolean first = true;
		for (Iterator iterator2 = aggs.iterator(); iterator2.hasNext();) {
			Aggregation agg = (Aggregation) iterator2.next();

			if (Integer.parseInt(agg.getNumber()) == 0) {
				
				if(first){
					pWriter.print("              else{\r\n");
					pWriter.print("               for (Iterator iterator = records.iterator(); iterator.hasNext();) {\r\n\r\n");
					pWriter.print("                  MFStructure record = (MFStructure) iterator.next();\r\n\r\n");
					pWriter.print("                  if(record.equals(mfStructure)){\r\n");
				}
				first=false;
				if (!agg.aggType.equals("avg")) {

					if (agg.aggType.equals("max")) {
						pWriter.print("                         if (record."
								+ agg.aggType
								+ "_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ "< "
								+ agg.attribute
								+ "){\r\n");
						pWriter.print("                            record."
								+ agg.aggType
								+ "_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ " = "
								+ agg.attribute
								+ " ;\r\n");
						pWriter.print("                         }\r\n");
					}
					if (agg.aggType.equals("min")) {
						pWriter.print("                         if (record."
								+ agg.aggType
								+ "_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ "> "
								+ agg.attribute
								+ "){\r\n");
						pWriter.print("                            record."
								+ agg.aggType
								+ "_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ " = "
								+ agg.attribute
								+ " ;\r\n");
						pWriter.print("                         }\r\n");

					}
					if (agg.aggType.equals("sum")) {
						pWriter.print("                         record.sum_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ " += "
								+ agg.attribute + ";\r\n");
					}
					if (agg.aggType.equals("count")) {
						pWriter.print("                         record.count_"
								+ agg.attribute
								+ "_"
								+ agg.number
								+ " ++;\r\n");
					}
				}
			
			}
		}
		if(!first){
			pWriter.print("                         break;\r\n");
			
			
			pWriter.print("                  }\r\n");
			
			
			
			
			pWriter.print("               }\r\n");
			/**
			 * ------------------------------------------------------------------------**************************
			 */
			pWriter.print("            } \r\n");//
		}
	
		
		
		
		
		
		if (hasIf) {
			pWriter.print("         }\r\n");
		}

		pWriter.print("      }\r\n\r\n");

		/**
		 * --------------------------------------------------------------------
		 * -------
		 */
		for (int z = 0; z < selectCons.length; z++) {
			if(isNull(selectCons[z])){//when the 2D condition is not null then excute the next part
				pWriter.print("      ResultSet rs"+z+" = stmt.executeQuery();\r\n");
				pWriter.print("      while (rs"+z+".next()) {\r\n\r\n");
				/**
				 * iterate all the non-aggregation att，eg. cust
				 * quant
				 * 
				 */
				if (noneAggAtts.size() != 0) {
					for (Iterator iterator = noneAggAtts.iterator(); iterator.hasNext();) {//
						Attribute att = (Attribute) iterator.next();
						if (att.attType.equals("String")) {
							pWriter.print("         "
									+ att.attType
									+ " "
									+ att.attName
									+ " = rs"+z+".get"
									+ att.attType.replaceFirst(att.attType.substring(0,
											1), att.attType.substring(0, 1)
											.toUpperCase()) + "(\"" + att.attName
									+ "\").trim();\r\n");
						} else {
							pWriter.print("         "
									+ att.attType
									+ " "
									+ att.attName
									+ " = rs"+z+".get"
									+ att.attType.replaceFirst(att.attType.substring(0,
											1), att.attType.substring(0, 1)
											.toUpperCase()) + "(\"" + att.attName
									+ "\");\r\n");
						}

					}
				}
				/**
				 * Get the "Where" condition
				 * 
				 */
				int aa = 0;
				for(int i = 0; i<selectCons[0].length;i++){
					Condition con_0 = selectCons[0][i];
					if(con_0!=null){
						if (con_0.numberOfGv == "0") {
							if (aa == 0) {
								pWriter.print("         if( ");
								hasIf = true;
								aa++;
							} else {
								pWriter.print(" && ");
							}
							if (con_0.relation.contains("<>")) {
								pWriter.print("!(" + con_0.attibute + "+\"\")" + ".equals("
										+ con_0.condition + "+\"\")");
							} else if (con_0.relation.contains(">")
									|| con_0.relation.contains("<")) {
								pWriter.print(con_0.attibute + " " + con_0.relation + " "
										+ con_0.condition);
							} else {
								pWriter.print("(" + con_0.attibute + "+\"\")" + ".equals("
										+ con_0.condition + "+\"\")");
							}
						}
					}
				}
				if (hasIf) {
					pWriter.print("){\r\n");
				}
				
				
				pWriter.print("               for (Iterator iterator = records.iterator(); iterator.hasNext();) {\r\n\r\n");
				pWriter.print("                  MFStructure record = (MFStructure) iterator.next();\r\n\r\n");
			

				for (int j = 1; j <= numOfGv; j++) {// loop by the number of grouping variable
					boolean hasNext = false;
					boolean hasNext2 = false;
					boolean hasNext3 = false;
					
					int b = 0;
					int c = 0;
					/**
					 * this is for 1_cust="Bloom"
					 */
					for(int i = 0; i<selectCons[z].length;i++){
						Condition sc2 = selectCons[z][i];
//						String num = sc2.numberOfGv;
						
						if(sc2!=null){
							if (Integer.parseInt(sc2.numberOfGv) == j) {

								if (sc2.condition.contains("record.get")) {
									if (b == 0) {
										pWriter.print("                     if( ");
										hasNext = true;
										b++;
									} else {
										pWriter.print(" && ");
									}
									if (sc2.relation.contains("<>")) {
										pWriter.print("!(" + sc2.attibute + "+\"\")"
												+ ".equals(" + sc2.condition + "+\"\")");
									} else if (sc2.relation.contains(">")
											|| sc2.relation.contains("<")) {
										pWriter.print(sc2.attibute + " " + sc2.relation
												+ " " + sc2.condition);
									} else {
										pWriter.print("(" + sc2.attibute + "+\"\")"
												+ ".equals(" + sc2.condition + "+\"\")");
									}
								}
							}
						}
					}

							
					if (hasNext) {
						pWriter.print("){\r\n");
					}

					for(int i = 0; i<selectCons[z].length;i++){
						Condition sc2 = selectCons[z][i];
						if(sc2!=null){
							if (Integer.parseInt(sc2.numberOfGv) == j) {

								if (!sc2.condition.contains("record.get")) {
									if (c == 0) {
										pWriter.print("                        if( ");
										hasNext2 = true;
										c++;
									} else {
										pWriter.print(" && ");
									}
									if (sc2.relation.contains("<>")) {
										pWriter.print("!(" + sc2.attibute + "+\"\")"
												+ ".equals(" + sc2.condition + "+\"\")");
									} else if (sc2.relation.contains(">")
											|| sc2.relation.contains("<")) {
										pWriter.print(sc2.attibute + " " + sc2.relation
												+ " " + sc2.condition);
									} else {
										pWriter.print("(" + sc2.attibute + "+\"\")"
												+ ".equals(" + sc2.condition + "+\"\")");
									}
								}
							}
						}
					}
					if (hasNext2) {
						pWriter.print("){\r\n");
					}
					for (Iterator iterator2 = aggs.iterator(); iterator2.hasNext();) {
						Aggregation agg = (Aggregation) iterator2.next();
						                                                     //------------------------------------------------------------------
						if (Integer.parseInt(agg.getNumber()) == j&&hasNext) {//------------------------------------------------------------------
							if (!agg.aggType.equals("avg")) {

								if (agg.aggType.equals("max")) {
									pWriter.print("                           if (record."
											+ agg.aggType
											+ "_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ "< "
											+ agg.attribute
											+ "){\r\n");
									pWriter.print("                              record."
											+ agg.aggType
											+ "_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ " = "
											+ agg.attribute
											+ " ;\r\n");
									pWriter.print("                           }\r\n");
								}
								if (agg.aggType.equals("min")) {
									pWriter.print("                           if (record."
											+ agg.aggType
											+ "_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ "> "
											+ agg.attribute
											+ "){\r\n");
									pWriter.print("                              record."
											+ agg.aggType
											+ "_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ " = "
											+ agg.attribute
											+ " ;\r\n");
									pWriter.print("                           }\r\n");

								}
								if (agg.aggType.equals("sum")) {
									pWriter.print("                           record.sum_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ " += "
											+ agg.attribute + ";\r\n");
								}
								if (agg.aggType.equals("count")) {
									pWriter.print("                           record.count_"
											+ agg.attribute
											+ "_"
											+ agg.number
											+ " ++;\r\n");
								}
							}
						}
					}
					if (hasNext2) {
						pWriter.print("                        }\r\n");
					}
					if (hasNext) {
						pWriter.print("                     }\r\n");
					}

				}

				pWriter.print("               }\r\n");
				if (hasIf) {
					pWriter.print("         }\r\n");
				}
				pWriter.print("      }\r\n");
				
			
			}
				
		}
		
		
		/**
		 * --------------------------------------------------------------------
		 * ------
		 */

		pWriter.print("      System.out.println(\"");
		for (Iterator iterator2 = selectAtts.iterator(); iterator2.hasNext();) {
			String selectAtt = (String) iterator2.next();
			pWriter.printf("%-25s",selectAtt);
		}
		pWriter.print("\");\r\n");
		
		pWriter.print("      System.out.println(\"");
		for (Iterator iterator2 = selectAtts.iterator(); iterator2.hasNext();) {
			String selectAtt = (String) iterator2.next();
			pWriter.printf("%-25s",getIso(selectAtt));
		}
		pWriter.print("\");\r\n");
		pWriter.print("      int i = 0;\r\n");
		
		pWriter.print("      for (MFStructure record : records) {\r\n\r\n");

		if (havingCondition.length() != 1) {
			pWriter.print("         if(" + havingCondition + "){\r\n");
		}

		for (Iterator it = selectAttsForPr.iterator(); it.hasNext();) {
			while (it.hasNext()) {
				String att = (String) it.next();
				pWriter.print("            System.out.printf(\"%-25s\"," + att
						+");\r\n");
			}
		}
		pWriter.print("            System.out.println();\r\n");
		pWriter.print("            i++;\r\n");
		if (havingCondition.length() != 1) {
			pWriter.print("         }\r\n");
		}
		pWriter.print("      }\r\n\r\n");
		   
		pWriter.print("      System.out.println(\"(\"+i+\" rows)\");\r\n");  
		pWriter.print("   }\r\n");
		pWriter.print("}\r\n");

		pWriter.flush();
		System.out.println("The Main class is generated successfully!");
		System.out
				.println("The generated Main.java file path is : " + filePath);
	}
	public boolean isNull(Condition[] selectCons){
		boolean b = false;
		for (int i = 0; i < selectCons.length; i++) {
			if(selectCons[i]!=null){
				 b = true;
			}
		}
		return b;
	}
	public String getIso(String selectAtt){
		String r = "";
		for(int i = 0; i<selectAtt.length();i++){
			r += "=";
		};
		return r;
	}
}
