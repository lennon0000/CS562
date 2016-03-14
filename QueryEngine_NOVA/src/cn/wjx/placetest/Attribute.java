package cn.wjx.placetest;

import java.lang.invoke.SwitchPoint;

/**
 * 
 * @author wangjingxu att informations from the database, include att name, att
 *         type att name is just the same as it at database, the att type is set
 *         corresponding to the type from database. set the att type by the type
 *         of the att from the database
 * 
 * 
 * 
 *        
 */
public class Attribute {

	String attName;
	String attType;
int id;
	public String getAttName() {
		return attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getAttType() {
		return attType;
	}

	public void setAttType(String attType) {// change the att type into java
											// basic type

		switch (attType) {
		case "bpchar":
			this.attType = "String";
			break;
		case "varchar":
			this.attType = "String";
			break;
		case "int8":
			this.attType = "int";
			break;
		case "int4":
			this.attType = "int";
			break;
		case "float4":
			this.attType = "float";
			break;
		case "int2":
			this.attType = "int";
			break;

		default:
			break;
		}

	}

	public Attribute (String columnName, String columnType) {
		setAttName(columnName);
		setAttType(columnType);
	}

	public Attribute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attName == null) ? 0 : attName.hashCode());
		result = prime * result + ((attType == null) ? 0 : attType.hashCode());
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
		Attribute other = (Attribute) obj;
		if (attName == null) {
			if (other.attName != null)
				return false;
		} else if (!attName.equals(other.attName))
			return false;
		if (attType == null) {
			if (other.attType != null)
				return false;
		} else if (!attType.equals(other.attType))
			return false;
		return true;
	}
}
