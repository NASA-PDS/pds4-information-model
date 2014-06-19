package gov.nasa.pds.model.plugin;
public class DeprecatedDefn {
	String identifier;
	String title;
	String nameSpaceIdNC;
	String className;
	String attrName;
	String value;
	String context;
	boolean isAttribute;
	boolean isValue;

	public DeprecatedDefn (String lTitle, String lNameSpaceIdNC, String lClassName, String lAttrName, String lValue) {
		identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lTitle; 
		title = lTitle;
		nameSpaceIdNC = lNameSpaceIdNC;
		className = lClassName;
		attrName = lAttrName;
		value = lValue;
		isValue = false;
		isAttribute = false;
		if (value.compareTo("") != 0) {
			isValue = true;
			isAttribute = true;
//			context = "pds:" + lClassName + "/pds:" + lAttrName;
			context = "pds:" + lClassName;
		} else if (lAttrName.compareTo("") != 0) {
			isAttribute = true;
			context = "pds:" + lClassName + "/pds:" + lAttrName;
		} else {
			context = "pds:" + lClassName;
		}
	} 
}

