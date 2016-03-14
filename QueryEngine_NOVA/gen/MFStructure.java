package gen;

public class MFStructure {

   public String prod;
   public String getProd(){;
        return prod;
   }
    public String cust;
   public String getCust(){;
        return cust;
   }
    public float avg_quant_3;
   public float getAvg_quant_3(){
       return (float)sum_quant_3/count_quant_3;
   }
   public int sum_quant_3;
   public int getSum_quant_3(){
       return sum_quant_3;
   }
   public int count_quant_1;
   public int getCount_quant_1(){
       return count_quant_1;
   }
   public float avg_quant_1;
   public float getAvg_quant_1(){
       return (float)sum_quant_1/count_quant_1;
   }
   public int sum_quant_1;
   public int getSum_quant_1(){
       return sum_quant_1;
   }
   public float avg_quant_2;
   public float getAvg_quant_2(){
       return (float)sum_quant_2/count_quant_2;
   }
   public int sum_quant_2;
   public int getSum_quant_2(){
       return sum_quant_2;
   }
   public int count_quant_2;
   public int getCount_quant_2(){
       return count_quant_2;
   }
   public int count_quant_3;
   public int getCount_quant_3(){
       return count_quant_3;
   }
  public MFStructure() {}
  public MFStructure(String prod,String cust){this.prod = prod;this.cust = cust;}
  @Override
  public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result +((prod == null) ? 0 :prod.hashCode());
      result = prime * result +((cust == null) ? 0 :cust.hashCode());
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
     MFStructure other = (MFStructure) obj;
     if (prod == null) {
     if (other.prod != null) 
       return false; 
     } else if (!prod.equals(other.prod )) 
       return false; 
     if (cust == null) {
     if (other.cust != null) 
       return false; 
     } else if (!cust.equals(other.cust )) 
       return false; 
    return true;

  }
}
