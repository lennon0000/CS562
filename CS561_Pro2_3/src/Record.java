/**
 * @author Pengpeng GE
 * CWID :10405696
 * 
 * This class is Record class. The class includes 8 variable that customer,product, nyMax,njMAX,ctMin,nyMaxDate,nyMaxDate,ctMinDate.
 * customer means the customers'name of sales; 
 * product means the  products'name of sales;
 * nyMax means the ny's maximum quantity of sales;
 * njMAX means the nj's maximum quantity of sales;
 * ctMin means the ct's maximum quantity of sales;
 * nyMaxDate means the ny's maximum date of sales;
 * njMaxDate means the nj's maximum date of sales;
 * ctMinDate means the ct's maximum date of sales;
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Record {
	static String Q1 = "Q1";
	static String Q2 = "Q2";
	static String Q3 = "Q3";
	static String Q4 = "Q4";

	public Record(String customer, String product) {
		super();
		List<Quarter> quarters = new ArrayList<Quarter>();
		Quarter q1 = new Quarter("Q1");
		Quarter q2 = new Quarter("Q2");
		Quarter q3 = new Quarter("Q3");
		Quarter q4 = new Quarter("Q4");
		quarters.add(q1);
		quarters.add(q2);
		quarters.add(q3);
		quarters.add(q4);
		this.customer = customer;
		this.product = product;

	}

	public String customer;
	public String product;
	List<Quarter> quarters;

	/**
	 * @return the quarters
	 */
	public List<Quarter> getQuarters() {
		return quarters;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @param quarters
	 *            the quarters to set
	 */
	public void setQuarters(List<Quarter> quarters) {
		this.quarters = quarters;
	}

	public void updateQuarter(int month, int quant) {
		List<Quarter> quarters = this.getQuarters();
		if (quarters == null) {
			quarters = new ArrayList<Quarter>();
			Quarter q1 = new Quarter("Q1");
			Quarter q2 = new Quarter("Q2");
			Quarter q3 = new Quarter("Q3");
			Quarter q4 = new Quarter("Q4");
			quarters.add(q1);
			quarters.add(q2);
			quarters.add(q3);
			quarters.add(q4);
		}
		// else{
		for (Quarter q : quarters) {
			int qn = Integer.parseInt(q.getQuarter().substring(1, 2));
			if (month <= 3 && qn == 1) {
				q.setSum(quant);
				q.setCount(1);
				if (q.getMin() == 0) {
					q.setMin(quant);
				}else {
					if (q.getMin() > quant) {
						q.setMin(quant);
					}
				}
				
			}
			if (month <= 6 && month >= 4 && qn == 2) {
				q.setSum(quant);
				q.setCount(1);
				if (q.getMin() == 0) {
					q.setMin(quant);
				}else {
					if (q.getMin() > quant) {
						q.setMin(quant);
					}
				}
			}
			if (month <= 9 && month >= 7 && qn == 3) {
				q.setSum(quant);
				q.setCount(1);
				if (q.getMin() == 0) {
					q.setMin(quant);
				}else {
					if (q.getMin() > quant) {
						q.setMin(quant);
					}
				}
			}
			if (month <= 12 && month >= 10 && qn == 4) {
				q.setSum(quant);
				q.setCount(1);
				if (q.getMin() == 0) {
					q.setMin(quant);
				}else {
					if (q.getMin() > quant) {
						q.setMin(quant);
					}
				}
			}
		}
		this.setQuarters(quarters);
		// }

	}

	void update(int month, int quant) {
		List<Quarter> quarters = this.getQuarters();
		for (Quarter q : quarters) {
			//  1        2         3      4
			// 123      456       789  10/11/12
			//       n*3-2  n*3
			//n*3-5 n*3-3    n*3+1 n*3+3
			int qn = Integer.parseInt(q.getQuarter().substring(1, 2));
			if (month>= qn*3-5 && month <= qn*3-3 && quant<q.getAvg() && quant>q.getMin()) {
				q.setB_count(1);
//				q.setA_count(1);
			}
			if (month>=qn*3+1 && month<=qn*3+3 && quant<q.getAvg() && quant>q.getMin()) {
				q.setA_count(1);
//				q.setB_count(1);
			}
		}
		this.setQuarters(quarters);
	}
}