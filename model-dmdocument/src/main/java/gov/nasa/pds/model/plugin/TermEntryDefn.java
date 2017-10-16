package gov.nasa.pds.model.plugin; 
public class TermEntryDefn {
	String name;
	String definition;
	String language;
	// from SKOS
	String semanticRelation;
	String mappingProperty;
	String sourceNamespaceId;
	
	boolean isPreferred;
	
	public TermEntryDefn () { 
		name = "TBD_name";
		definition = "TBD_definition";
		language = "TBD_language";
		semanticRelation = "TBD_semanticRelation";
		mappingProperty = "TBD_mappingProperty";
		sourceNamespaceId = "TBD_sourceNamespaceId";
		isPreferred = false;
	} 
}
