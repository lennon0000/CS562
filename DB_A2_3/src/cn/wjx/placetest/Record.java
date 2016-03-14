package cn.wjx.placetest;

import java.util.ArrayList;
import java.util.List;

public class Record {

	public String cust;// it means customer
	public String product;
	public List<Avg> avgs;//it stores information of four quarters

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Record() {

	}

	public Record(String cust, String product) {
		this.cust = cust;
		this.product = product;
	}

	public List<Avg> getAvgs() {
		return avgs;
	}

	public void setAvgs(List<Avg> avgs) {
		this.avgs = avgs;
	}

	public void addAvg(int mon, int year, int quant) {
		if (avgs == null) {
			this.avgs = new ArrayList<Avg>();
		}
		avgs.add(new Avg(mon, year, quant));

	}

	public void updateAvg(int mon, int year, int quant) {
		for (Avg a : avgs) {
			if (a.getMons().contains(mon)) {
				a.update(year, quant);
			}
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cust == null) ? 0 : cust.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {//override equals function
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (cust == null) {
			if (other.cust != null)
				return false;
		} else if (!cust.equals(other.cust))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	public void addavg() {//make sure that every record has four quarters
		List<Avg> avgs2 = new ArrayList<Avg>();
		int sum = 0;
		Avg a = new Avg();
		a.setQuarter("Q1");
		a.setSum(0);
		a.setCount(0);
		a.setQuants(new ArrayList<Integer>());
		
		Avg a2 = new Avg();
		a2.setQuarter("Q2");
		a2.setSum(0);
		a2.setCount(0);
		a2.setQuants(new ArrayList<Integer>());
		
		Avg a3 = new Avg();
		a3.setQuarter("Q3");
		a3.setSum(0);
		a3.setCount(0);
		a3.setQuants(new ArrayList<Integer>());
		
		Avg a4 = new Avg();
		a4.setQuarter("Q4");
		a4.setSum(0);
		a4.setCount(0);
		a4.setQuants(new ArrayList<Integer>());
		
		avgs2.add(a);
		avgs2.add(a2);
		avgs2.add(a3);
		avgs2.add(a4);
		
		List<Avg> remove = new ArrayList<Avg>();
		for(Avg ai : avgs){
			for(Avg ao :avgs2){
				if(ai.getQuarter().equals(ao.getQuarter())){
					remove.add(ao);
				}
			}
		}
		avgs2.removeAll(remove);
		this.avgs.addAll(avgs2);
	}
}
