package cn.wjx.placetest;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Condition {
	public static boolean booleanisNumeric1(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	String numberOfGv;
	String attibute;
	String relation;
	String condition;
	int level;
	int dep;
	boolean hasDepCon = false;
	public static boolean hasDepConWhole = false;
	public static Condition[][] depCons = new Condition[5][10]; 
	public Condition[][] getDepCons(){
		return depCons;
	}
	public boolean hasDepConWhole(){
		return hasDepConWhole;
	}
	public String getNumberOfGv() {
		return numberOfGv;
	}

	public void setNumberOfGv(String numberOfGv) {
		this.numberOfGv = numberOfGv;
	}

	public String getAttibute() {
		return attibute;
	}

	public void setAttibute(String attibute) {
		this.attibute = attibute;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Condition(String t) {// 1.cust=cust cust="Bloom" year="1999"
		String[] relations = { ">=", "<=", "<>", "==", "=", ">", "<" };
		for (int i = 0; i < relations.length; i++) {
			String r = relations[i];
			if (t.contains(r)) {

				String[] doubleSides = t.split(r);// cust
													// “Bloom”， year “1990”
				String[] ls = doubleSides[0].split("\\.");

				if (ls.length == 1) {// if the length==1 then it is this kind of case
										// cust=“”，year=“” then the number Of
										// Grouping variable should be 0
					numberOfGv = "0";
					attibute = ls[0];
				} else {// 1.cust
					numberOfGv = ls[0];
					attibute = ls[1];
				}
				relation = r;
				// if it is int(year==1997)
				if (doubleSides[1].contains("'")) {// if it is String
													// cust='Bloom'
					condition = "\""
							+ doubleSides[1].substring(1,
									doubleSides[1].length() - 1) + "\"";
//					break;
				} else if (booleanisNumeric1(doubleSides[1])) {
					condition = doubleSides[1];
				} else {

					String[] operations = { "+", "-", "*", "/" };
					String right = doubleSides[1];
					if (right.contains("+") || right.contains("-")//if this is expression, eg. 1.quant>2_avg_quant*2
							|| right.contains("*") || right.contains("/")) {
						for (int j = 0; j < operations.length; j++) {
							String op = operations[j];
							if (right.contains(op)) {// =month-1, =quan*2
								String[] formula = right.split("\\"+op);
								 String f1 = trans(formula[0]);
								 String f2 = trans(formula[1]);
//								String f1 = formula[0];
														
//								String f2 = formula[1];
								condition = "record.get"
										+ f1.replaceFirst(f1.substring(0, 1),
												f1.substring(0, 1)
														.toUpperCase()) + "()"
										+ op + f2;
								if (t == "=" || t == "==") {
									condition = condition + "+\"\"";
								}
							}
						}
					} else {
							
						String right2 = trans(right);
						condition = "record.get"
								+ right2.replaceFirst(right2.substring(0, 1),
										right2.substring(0, 1).toUpperCase())
								+ "()";
						if (t == "=" || t == "==") {
							condition = condition + "+\"\"";
						}
					}
				}
				Condition c = new Condition(numberOfGv,attibute,relation,condition);
				if(!hasDepCon){
					for (int j = 0; j < depCons[0].length; j++) {
						if(depCons[0][j]==null){
							depCons[0][j]=c;
							break;
						}
					}
				}else{
					depCons[level][dep] = c;
				}
				break;
			}
		}
	}

	public Condition() {
	}

	public Condition(String numberOfGv2, String attibute2, String relation2,
			String condition2) {
		this.attibute = attibute2;
		this.condition = condition2;
		this.relation = relation2;
		this.numberOfGv = numberOfGv2;
		
	}



	public String getHavingConditions(String havingCondition) {//convert the having clause
		String r = "";
		String[] cs = havingCondition.split(" ");
		for (int i = 0; i < cs.length; i++) {

			if (cs[i].contains("_")) {// 1_avg_quant 1_sum_quant
				String[] temp = cs[i].split("_");
				if (temp[1].equals("avg")) {
					cs[i] = "record.sum_" + temp[2] + "_" + temp[0] + "/"
							+ "record.count_" + temp[2] + "_" + temp[0];
				} else {
					cs[i] = "record." + temp[1] + "_" + temp[2] + "_" + temp[0];
				}
			} else if (cs[i].contains("(")) {// avg(quant)
				String[] temp = cs[i].split("\\(");
				if (temp[0].equals("avg")) {
					cs[i] = "record.sum_" + temp[1].split("\\)")[0] + "_0"
							+ "/" + "record.count_" + temp[1].split("\\)")[0]
							+ "_0";
				} else {
					cs[i] = "record." + temp[0] + "_" + temp[1].split("\\)")[0]
							+ "_0";
				}
			} else if (cs[i].equals("and")) {
				cs[i] = "&&";
			} else if (cs[i].equals("or")) {
				cs[i] = "||";
			}

		}

		for (int i = 0; i < cs.length; i++) {
			r += cs[i] + " ";
		}

		return r;
	}
	public String trans(String temp){

		if (temp.contains("(")) {
			String[] ss = temp.split("\\(");
			String attName = ss[1].split("\\)")[0];
			String r = ss[0] + "_" + attName + "_0";
			return r;
		} else if (temp.contains("_")) {// if it is 1_sum_quant this kind of case
										// number（1）， agg-type（sum），
										// corresponding attribute（quant）
			String[] aggAtts = temp.split("_");
			String r = aggAtts[1] + "_" + aggAtts[2] + "_" + aggAtts[0];//sum_quant_1
			
			
			String left = numberOfGv;
			String right = aggAtts[0];
			
			boolean base = true;
			for (int i = 1; i < depCons.length; i++) {//start from 1, 0 position is for 1.cust= cust   1.cust="Bloom"   
				boolean b = false;
				for (int j = 0; j < depCons[i].length; j++) {
					if(depCons[i][j]!=null){
						//record.getAvg_quant_1()
						System.out.println("xx");
						if(left.equals(depCons[i][j].getCondition().split("_")[2].split("\\(")[0]+"")){
//						if(left.equals(j+"")){
							level = i;
							dep = j;
							for(int z = 3;z>=i;z--){//after inserting into this 2D Condition, then the rest of it should move down
								if(depCons[z][j]!=null){
									depCons[z+1][j] = depCons[z][j];
								}
							}
							
							b = true;
							
							hasDepCon = true;
							base = false;
						}
						if(right.equals(depCons[i][j].getNumberOfGv()+"")){
//						if(right.equals(i+"")){
							if(level==0&&dep==0){
								if(depCons[i+1][j]==null){
									level = i+1;
									dep = j;
									
									b = true;
									
//									break;
								}else{
									for(int z = j;z<=5;z++){
										if(depCons[i+1][z]==null){
											level = i+1;
											dep = z;
											
											b = true;
											
//											break;
										}
									}
								}
							}
							
							hasDepCon = true;
							base = false;
						}
						if(b){
							break;
						}
					}
					
					
				}
			}
			if(base){//if there is dependency then the default value is i==1
				for (int j = 0; j < depCons[1].length; j++) {
					if(depCons[1][j]==null){ 
						level = 1;
						dep = j;
//						Condition c = new Condition();
//						
//						depCon[0][j]= new Condition();
						hasDepCon = true;
						hasDepConWhole = true;
						break;
					}
				}
			}
			return r;
		}
		return temp;
	}

	/**
	 * @return the hasDepCon
	 */
	public boolean isHasDepCon() {
		return hasDepCon;
	}

	/**
	 * @param hasDepCon the hasDepCon to set
	 */
	public void setHasDepCon(boolean hasDepCon) {
		this.hasDepCon = hasDepCon;
	}

	/**
	 * @return the hasDepConWhole
	 */
	public static boolean isHasDepConWhole() {
		return hasDepConWhole;
	}

	/**
	 * @param hasDepConWhole the hasDepConWhole to set
	 */
	public static void setHasDepConWhole(boolean hasDepConWhole) {
		Condition.hasDepConWhole = hasDepConWhole;
	}

}
