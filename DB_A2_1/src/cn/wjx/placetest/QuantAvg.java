package cn.wjx.placetest;

public class QuantAvg {
	
	private int quant;
	private int count;
	private int avg;
	
	public int getQuant() {
		return quant;
	}
	public void setQuant(int quant) {
		this.quant = this.quant+quant;
	}
	public int getCount() {
		return count;
	}
	public void setCount() {
		this.count = this.count+1;
	}
	
//	public QuantAvg(){
//		
//	}
//	public QuantAvg(String name){
//		this.name = name;
//	}
//	public QuantAvg(String name,int quant) {
//		this.name = name;
//		this.quant = quant;
//	}
	
//	public QuantAvg(int quant) {
//		this.quant = quant;
//	}
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		return result;
//	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		QuantAvg other = (QuantAvg) obj;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		return true;
//	}
	@Override
	public String toString() {
		return String.valueOf(this.count==0?0:this.quant/this.count);
	}
	public int getAvg() {
		if(count!=0){
			return quant/count;
		}else{
			return 0;
		}
		
	}
	public void setAvg(int avg) {
		this.avg = avg;
	}
	public void setCount(int i) {
		this.count = this.count+i;
		
	}
	
}
