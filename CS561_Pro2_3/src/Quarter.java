
public class Quarter {
	int count;
	double sum;
	double avg;
	String quarter;
	int b_count;
	int a_count;
	/**
	 * @return the b_count
	 */
	public int getB_count() {
		return b_count;
	}
	/**
	 * @param b_count the b_count to set
	 */
	public void setB_count(int b_count) {
		this.b_count += b_count;
	}
	/**
	 * @return the a_count
	 */
	public int getA_count() {
		return a_count;
	}
	/**
	 * @param a_count the a_count to set
	 */
	public void setA_count(int a_count) {
		this.a_count += a_count;
	}
	int min;
	/** 
	 * @return the min
	 */
	public int getMin() {
		return min;
	}
	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}
	public Quarter(String quarter) {
		// TODO Auto-generated constructor stub
		this.quarter = quarter;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count += count;
	}
	/**
	 * @return the sum
	 */
	public double getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(double sum) {
		this.sum += sum;
	}
	/**
	 * @return the avg
	 */
	public double getAvg() {
		double r = 0;
		if (this.count !=0) {
			r = this.sum/this.count;
					
		}
		return r;
	}
	/**
	 * @param avg the avg to set
	 */
	public void setAvg(double avg) {
		this.avg = avg;
	}
	/**
	 * @return the quarter
	 */
	public String getQuarter() {
		return quarter;
	}
	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	
	
}
