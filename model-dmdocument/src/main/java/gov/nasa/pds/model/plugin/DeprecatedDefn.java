package gov.nasa.pds.model.plugin;

public class DeprecatedDefn {
	String identifier;
	String title;
	String classNameSpaceIdNC;
	String className;
	String attrNameSpaceIdNC;
	String attrName;
	String value;
	String context;
	boolean isAttribute;
	boolean isValue;
	boolean isUnitId;

	public DeprecatedDefn (String lTitle, String lClassNameSpaceIdNC, String lClassName, String lAttrNameSpaceIdNC, String lAttrName, String lValue, boolean lIsUnitId) {
		title = lTitle;
		classNameSpaceIdNC = lClassNameSpaceIdNC;
		className = lClassName;
		attrNameSpaceIdNC = lAttrNameSpaceIdNC;
		attrName = lAttrName;
		value = lValue;
		isValue = false;
		isAttribute = false;
		isUnitId = lIsUnitId;
		if (value.compareTo("") != 0) {
			identifier = InfoModel.getAttrIdentifier(lClassNameSpaceIdNC, lClassName, lAttrNameSpaceIdNC, lAttrName);
			isValue = true;
			isAttribute = true;
			context = classNameSpaceIdNC + ":" + lClassName;
		} else if (lAttrName.compareTo("") != 0) {
			identifier = InfoModel.getAttrIdentifier(lClassNameSpaceIdNC, lClassName, lAttrNameSpaceIdNC, lAttrName);
			isAttribute = true;
			context = classNameSpaceIdNC + ":" + lClassName + "/" + lAttrNameSpaceIdNC + ":" + lAttrName;
		} else {
			identifier = InfoModel.getClassIdentifier(lClassNameSpaceIdNC, lClassName);
			context = classNameSpaceIdNC + ":" + lClassName;
		}
	} 
}
