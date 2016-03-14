package cn.wjx.placetest;

import java.util.ArrayList;
import java.util.List;


public class Record {

	public String cust;//it stands for customer
	public String product;
	public List<Param> params;//it contains the information of the sale quantity and state 
	
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
	
	public Record(){
		
	}
	public Record(String cust, String product) {
		this.cust = cust;
		this.product = product;
	}
	
	public List<Param> getParams() {
		return params;
	}
	public void setParams(List<Param> params) {
		this.params = params;
	}
	public void addParam(Param param){
		if(this.params==null){
			params = new ArrayList<Param>();
			params.add(new Param("NY"));
			params.add(new Param("NJ"));
			params.add(new Param("CT"));
		}
		for(Param p : params){
			if(p.equals(param)){
				p.setCount(p.getCount()+1);
				p.setQuant(p.getQuant()+param.getQuant());
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
	public boolean equals(Object obj) {
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
	@Override
	public String toString() {
		
		return cust + product + params.toString();
	}
	
}
