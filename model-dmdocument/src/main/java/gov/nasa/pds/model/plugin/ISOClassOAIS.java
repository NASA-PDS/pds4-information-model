package gov.nasa.pds.model.plugin; 

public class ISOClassOAIS extends ISOClass {
	boolean isDigital = false;
	boolean isPhysical = false;
	
	public ISOClassOAIS () {
	}
	
	public void setOAISClassType(String oaisClassType) {
		if (oaisClassType.compareTo("Digital") == 0) this.isDigital = true;			// false -> non-Digital
		if (oaisClassType.compareTo("Physical") == 0) this.isPhysical = true;			// false -> conceptual
	} 
	
	public boolean getIsDigital() {
		return this.isDigital;
	}
	
	public boolean getisPhysical() {
		return this.isPhysical;
	}
}
