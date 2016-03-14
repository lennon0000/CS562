package cn.wjx.placetest;



public class Record {

	public String cust;// it means customer
	public String product;
	public QuantAvg custAvg;//it means the customer's average quantity of sales
	public QuantAvg otherAvg;//it means the other customer's average quantity of sales for the same kind of product
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
	
	public QuantAvg getCustAvg() {
		return custAvg;
	}
	public void setCustAvg(QuantAvg custAvg) {
		this.custAvg = custAvg;
	}
	public QuantAvg getOtherAvg() {
		return otherAvg;
	}
	public void setOtherAvg(QuantAvg otherAvg) {
		this.otherAvg = otherAvg;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cust == null) ? 0 : cust.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}
	@Override//override equals function
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
	
	public void updateCustAvg(int quant) {//update the value of average quantity for the customer 
		this.custAvg.setQuant(quant);
		this.custAvg.setCount();
	}
	public void updateOtherAvg(int quant) {//update the value of average quantity for other customer 
		this.otherAvg.setQuant(quant);
		this.otherAvg.setCount();
	}
	public void addAvg(int quant) {//add average information to the record
		this.custAvg = new QuantAvg();
		this.custAvg.setCount();
		this.custAvg.setQuant(quant);
		this.otherAvg = new QuantAvg();
	}
	public void updateOtherAvg(QuantAvg custAvg2) {//update the value of average quantity for other customer
		this.otherAvg.setQuant(custAvg2.getQuant());
		this.otherAvg.setCount(custAvg2.getCount());
	}
	
}
