package gov.nasa.pds.model.plugin; 
public class DOMPermValDefn extends ISOClassOAIS11179 {
//	String identifier;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String searchKey;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String registrationStatus;	// ISO 11179 item registration status
	String value;			// the value
	String value_meaning;	// the meaning of the value
	String value_begin_date;	// the value begin date
	String value_end_date;		// the value end date
	boolean isPattern;
	
	public DOMPermValDefn () {
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = "TBD_registrationStatus";
		value_meaning = "TBD_value_meaning";
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
		isPattern = false; // pattern special characters (e.g. back slashes) are invalid for OWL RDF identifiers.
	}
	
	public void createDOMPermValSingletons (PermValueDefn lPermValue, AttrDefn lOldAttr) {
		sequenceId = InfoModel.getNextUId();
//		rdfIdentifier = lOldAttr.rdfIdentifier; 
		String lValueId = lPermValue.value;
		if (isPattern) lValueId = "Pattern"; // pattern characters (e.g., back slashes) are invalid for OWL RDF identifiers.
		int lValueLength = lValueId.length();
		if (lValueLength > 20) lValueId = lValueId.substring(0, 20);
		rdfIdentifier = DMDocument.rdfPrefix + lOldAttr.parentClassTitle + "." + lOldAttr.title + "." + lValueId + "." + sequenceId; 
		int lHashCodeI = lPermValue.value.hashCode();
		String lHashCodeS = Integer.toString(lHashCodeI);
		identifier = lOldAttr.identifier + "." + lValueId + "." + lHashCodeS;
		versionId = lOldAttr.versionId; 
		sequenceId = lOldAttr.uid;
		
//		title = lOldAttr.title; 
//		definition = lOldAttr.description;
		
		registrationStatus = lPermValue.registrationStatus; 
//		isDeprecated = lOldProp.isDeprecated; 
		
		regAuthId = lOldAttr.regAuthId; 
		steward = lOldAttr.steward; 
		nameSpaceId = lOldAttr.attrNameSpaceId;
		nameSpaceIdNC = lOldAttr.attrNameSpaceIdNC;
		
		searchKey = lPermValue.searchKey;
		value = lPermValue.value;
		value_meaning = lPermValue.value_meaning;
		value_begin_date = lPermValue.value_begin_date;
		value_end_date = lPermValue.value_end_date;
		return;
	}
}
