package gov.nasa.pds.model.plugin;
class AssocClassDefn extends Object{
	String identifier;
	int cardMin; 
	int cardMax;
	PDSObjDefn assocClass;
	
	public AssocClassDefn (String lId, int lCardMin, int lCardMax, PDSObjDefn lAssocClass) {
		identifier = lId;
		cardMin = lCardMin; 
		cardMax = lCardMax;
		assocClass = lAssocClass;
		return;
	}
}
