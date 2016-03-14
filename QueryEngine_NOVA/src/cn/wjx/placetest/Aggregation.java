package cn.wjx.placetest;

public class Aggregation {
	String number;
	String aggType;
	String attribute;
	public Aggregation(String num, String aggType, String attName) {
		this.number = num;
		this.aggType = aggType;
		this.attribute = attName;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAggType() {
		return aggType;
	}
	public void setAggType(String aggType) {
		this.aggType = aggType;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aggregation other = (Aggregation) obj;
		
		if(!other.getAggType().equals(aggType)){
			return false;
		}
		if(!other.getAttribute().equals(attribute)){
			return false;
		}
		if(!other.getNumber().equals(number)){
			return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((aggType == null) ? 0 : aggType.hashCode());
		result = prime * result + ((attribute == null) ? 0 : attribute.hashCode());
		return result;
	}
	
}
