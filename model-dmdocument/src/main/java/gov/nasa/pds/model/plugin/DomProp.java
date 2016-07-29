package gov.nasa.pds.model.plugin;

public class DomProp extends ISOClassOAIS11179 {
	
	String cardMin;
	String cardMax;
	int cardMinI;
	int cardMaxI;
	String classOrder;						// the order of the attribute or association within a class
	DomClass hasDomClass;
	
	public DomProp () {
		cardMin = "0"; 
		cardMax = "0";
		cardMinI = 0; 
		cardMaxI = 0;
		classOrder = "9999";
		hasDomClass = null;
		return;
	}	
	
	public String getCardMin() {
		return cardMin;
	}
	
	public int getCardMinI() {
		return cardMinI;
	}
	
	public void setCardMinMax(String lCardMin, String lCardMax) {
		if (DMDocument.isInteger(lCardMin)) {
			cardMin = lCardMin;
			cardMinI = new Integer(lCardMin);
		} else {
			System.out.println(">>error    - DomProp " + " - Minimum cardinality is invalid: " + lCardMin);
		}
		if ((lCardMax.compareTo("*") == 0) || (lCardMax.compareTo("unbounded") == 0)) {
			cardMax = "*";
			cardMaxI = 9999999;
		} else if (DMDocument.isInteger(lCardMax)) {
			cardMax = lCardMax;
			cardMaxI = new Integer(lCardMax);
		} else {
			System.out.println(">>error    - DomProp " + " - Maximum cardinality is invalid: " + lCardMax);
		}
		if (cardMaxI < cardMinI) {
			System.out.println(">>error    - DomProp " + " - Maximum cardinality is less than minimum cardinality");
		}
	}
	
	public String getCardMax() {
		return cardMax;
	}
	
	public int getCardMaxI() {
		return cardMaxI;
	}
	
	public String getClassOrder() {
		return classOrder;
	}
	
	public void setClassOrder(String classOrder) {
		this.classOrder = classOrder;
	}

}
