package cn.wjx.placetest;

import java.util.ArrayList;
import java.util.List;

public class Avg {

	private String quarter;// it means the quarter
	private int sum;//it means the sum of the quarter's quantity  
	private int count;//it means the count of different quarters of the record
	private List<Integer> years;
	private List<Integer> mons;
	private int avg;//it means the average quantity of sale for the quarter
	private int max;//it means the maximum quantity of sale for the quarter
	private List<Integer> quants;//it includes the every quantity of this quarter
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quater) {
		this.quarter = quater;
	}

	public Avg() {

	}

	public Avg(String quarter) {
		this.quarter = quarter;
	}

	public Avg(int mon, int year, int quant) {
		if (this.mons == null) {
			this.mons = new ArrayList<Integer>();
		}
		if (this.years == null) {
			this.years = new ArrayList<Integer>();
		}
		if (this.quants == null) {
			this.quants = new ArrayList<Integer>();
		}

		switch ((mon - 1) / 3) {
		case 0://when add one month to the quarter, then initialize the quarter with another two month
			this.quarter = "Q1";
			this.mons.add(1);
			this.mons.add(2);
			this.mons.add(3);
			break;
		case 1:
			this.quarter = "Q2";
			this.mons.add(4);
			this.mons.add(5);
			this.mons.add(6);
			break;
		case 2:
			this.quarter = "Q3";
			this.mons.add(7);
			this.mons.add(8);
			this.mons.add(9);
			break;
		case 3:
			this.quarter = "Q4";
			this.mons.add(10);
			this.mons.add(11);
			this.mons.add(12);
			break;
		}
		this.years.add(year);
		this.setCount();
		this.setSum(quant);
		this.setMax(quant);
		this.quants.add(quant);
	}


	public List<Integer> getMons() {
		return mons;
	}

	public void setMons(List<Integer> mons) {
		this.mons = mons;
	}

	private void setCount() {
		this.count = this.count + 1;

	}

	public int getSum() {
		return sum;
	}

	public void setSum(int quant) {
		this.sum = this.sum + quant;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public int getAvg() {
		if (count != 0) {
			return sum / count;
		} else {
			return 0;
		}
	}

	public void setAvg(int avg) {
		this.avg = avg;
	}

	public void update(int year, int quant) {
//		if (!this.years.contains(years)) {//if this year doesn't exist, then add this year into years and count++
			this.years.add(year);
			this.setCount();
//		}
		this.setSum(quant);
		if(this.max<quant){
			this.max = quant;
		}
		this.quants.add(quant);
	}

	public boolean check(int mon) {//to check if the quarter exist
		if (this.mons.contains(mon)) {
			return true;
		} else {
			return false;
		}
	}

	public List<Integer> getQuants() {
		return quants;
	}

	public void setQuants(List<Integer> quants) {
		this.quants = quants;
	}
}
