package gov.nasa.pds.model.plugin; 

public class PermValueDefn {
	String identifier;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String searchKey;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String registrationStatus;	// ISO 11179 item registration status
	String value;			// the value
	String value_meaning;	// the meaning of the value
	String value_begin_date;	// the value begin date
	String value_end_date;		// the value end date
	
	public PermValueDefn (String lId, String lValue, String lValueMeaning) {
		identifier = lId; 
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = lValue;
		value_meaning = lValueMeaning;
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
	} 
	
	public PermValueDefn (String lId, String lValue) {
		identifier = lId; 
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = lValue;
		value_meaning = "TBD_value_meaning";
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
	} 	
}
