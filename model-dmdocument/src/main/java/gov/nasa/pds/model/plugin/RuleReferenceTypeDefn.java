package gov.nasa.pds.model.plugin; 
public class RuleReferenceTypeDefn {
	String ruleReferenceXPath;
	boolean isLocal;

	public RuleReferenceTypeDefn (String lRuleReferenceXPath, boolean lIsLocal) {
		ruleReferenceXPath = lRuleReferenceXPath; 
		isLocal = lIsLocal;
	} 
}

