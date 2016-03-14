package cn.wjx.placetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.postgresql.jdbc2.ArrayAssistantRegistry;

public class FileReader {
	/**
	 * getAttFromFile() by reading from emf-* file，get all the neccessary value, store it into separeted sets
	 * 
	 */

	String numberOfGv;
	Set<String> attsOfMFS = new HashSet<String>();// to store all the attributes, eg. cust，1_max_quant
	Set<String> selConditions = new HashSet<String>();
	
	List<String> selectAtts = new ArrayList<String>();//for output the column_name
	List<String> selectAttsForPr = new ArrayList<String>();//for print (it has "record.get" preffix)
	
	Set<String> groupingAtts = new HashSet<String>();
	Set<String> aggregations = new HashSet<String>();

	String havingCondition = "";
	@SuppressWarnings("rawtypes")
	Map m = new HashMap<String, Object>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map readTxtFile() {

		System.out.println("Please select the .txt file: ");

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt",
				"txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: "
					+ chooser.getSelectedFile().getName());
		}

		File file = chooser.getSelectedFile();

		String rootPath = this.getClass().getResource("/").getFile().toString();
		
//		File file = new File("source/emf-6-.txt");
		System.out.println("You chose to open :" + file.getName());
		try {
			Scanner s = new Scanner(file);
			String att1 = "ATTRIBUTE(S):";
			String att2 = "VARIABLES(n):";
			String att3 = "ATTRIBUTES(V):";
			String att4 = "F-VECT([F]):";
			String att5 = "CONDITION-VECT([σ]):";
			String att6 = "HAVING_CONDITION(G):";
			while (s.hasNext()) {
				String sNext = s.next();
				if (sNext.equals(att1)) {
					while (s.hasNext()) {
						sNext = s.next();
						if (!sNext.equals("NUMBER")) {
							String att = sNext.split(",")[0];
							attsOfMFS.add(att);
							
							selectAtts.add(att);
							selectAttsForPr.add(convert(att));

						} else {
							while (s.hasNext()) {
								if (sNext.equals(att2)) {
									while (s.hasNext()) {
										sNext = s.next();
										if (!sNext.equals("GROUPING")) {
											attsOfMFS.add(sNext.split(",")[0]);
											numberOfGv = sNext;
										} else {
											while (s.hasNext()) {
												if (sNext.equals(att3)) {
													while (s.hasNext()) {
														sNext = s.next();
														if (!sNext.equals("F-VECT([F]):")) {
															attsOfMFS.add(sNext.split(",")[0]);
															groupingAtts.add(sNext.split(",")[0]);
														} else {

															if (sNext
																	.equals(att4)) {
																while (s.hasNext()) {
																	sNext = s
																			.next();
																	if (!sNext
																			.equals("SELECT")) {
																		attsOfMFS
																				.add(sNext
																						.split(",")[0]);
																		aggregations
																				.add(sNext
																						.split(",")[0]);
																	} else {
																		while (s.hasNext()) {
																			if (sNext
																					.equals(att5)) {
																				while (s.hasNext()) {
																					sNext = s
																							.next();
																					if (!sNext
																							.equals("HAVING_CONDITION(G):")) {
																						selConditions
																								.add(sNext);
																					} else {
																						while (s.hasNext()) {
																							havingCondition += s
																									.next()
																									+ " "; 
																						}

																					}
																				}
																			} else {
																				sNext = s
																						.next();
																			}
																		}

																	}
																}

															}
														}
													}
												} else {
													sNext = s.next();
												}
											}

										}
									}
								} else {
									sNext = s.next();
								}
							}

						}
					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		m.put("numberOfGv", numberOfGv);
		m.put("attsOfMFS", attsOfMFS);
		m.put("selConditions", selConditions);
		m.put("selectAtts", selectAtts);
		m.put("groupingAtts", groupingAtts);
		m.put("aggregations", aggregations);
		m.put("havingCondition", havingCondition);
		m.put("selectAttsForPr", selectAttsForPr);

		return m;
	}

	public String convert(String temp) {//将读取到的字符串，比如avg（quant）转换为record.getAvg_quant_0,返回这个集合，在生成主函数的方法中循环遍历这个集合
		
		String[] operations = {"+","-","*","/"};
		String re;
		if(temp.contains("+")||temp.contains("-")||temp.contains("*")||temp.contains("/")){
			for (int i = 0; i < operations.length; i++) {
				String op = operations[i];
				
				
				if(temp.contains(op)){//avg(quant)/sum(quant)
					
					if(op=="/"){
						String[] formula = temp.split(op);
						String f1 = trans(formula[0]);
						String f2 = trans(formula[1]);
						re = "(float)record.get"+f1.replaceFirst(f1.substring(0, 1),f1.substring(0, 1).toUpperCase())+"()"+op+"record.get"+f2.replaceFirst(f2.substring(0, 1),f2.substring(0, 1).toUpperCase())+"()";
						return re;
					}else{
						if(temp.contains("_*")||temp.contains("(*")){
							String  t2 = trans(temp);
							re = "record.get"+t2.replaceFirst(t2.substring(0, 1),t2.substring(0, 1).toUpperCase())+"()";
						}else{
							String[] formula = temp.split("\\"+op);
							String f1 = trans(formula[0]);
							String f2 = trans(formula[1]);
							
							if(booleanisNumeric1(f2)){
								re = "record.get"+f1.replaceFirst(f1.substring(0, 1),f1.substring(0, 1).toUpperCase())+"()"+op+f2;
							}else{
								re = "record.get"+f1.replaceFirst(f1.substring(0, 1),f1.substring(0, 1).toUpperCase())+"()"+op+"record.get"+f2.replaceFirst(f2.substring(0, 1),f2.substring(0, 1).toUpperCase())+"()";
							}
							return re;
						}
						
					}
				}
			}
		}else{
			re = trans(temp);
			re = "record.get"+re.replaceFirst(re.substring(0, 1),
					re.substring(0, 1).toUpperCase())+"()";
			return re;
		}
		
		return temp;
	}
	
	
	
	public String trans(String temp){
//		String result = temp;
		if (temp.contains("(")) {
			String[] ss = temp.split("\\(");
			String attName = ss[1].split("\\)")[0];
			String r = ss[0] + "_" + attName + "_0";
			return r;
		} else if (temp.contains("_")) {// if it is 1_sum_quant this kind of case
										
			String[] aggAtts = temp.split("_");
			String r="";
			
			
				
				
				if(aggAtts.length==3){
					if(aggAtts[2]=="*"){
						r = aggAtts[1] + "_" + aggAtts[0];
					}else{
						r = aggAtts[1] + "_" + aggAtts[2] + "_" + aggAtts[0];
					}
				}else if(aggAtts.length==2){
					r = aggAtts[1] + "_" + aggAtts[0];
				}
				
				
				
			
			
			return r;
		}
		return temp;
	}
	public static boolean booleanisNumeric1(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
