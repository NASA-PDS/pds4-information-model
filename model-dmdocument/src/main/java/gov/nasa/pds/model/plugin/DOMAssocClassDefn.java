package gov.nasa.pds.model.plugin; 
class DOMAssocClassDefn extends Object{
	String identifier;
	int cardMin; 
	int cardMax;
	DOMClass assocClass;
	
	public DOMAssocClassDefn (String lId, int lCardMin, int lCardMax, DOMClass lAssocClass) {
		identifier = lId;
		cardMin = lCardMin; 
		cardMax = lCardMax;
		assocClass = lAssocClass;
		return;
	}
}
