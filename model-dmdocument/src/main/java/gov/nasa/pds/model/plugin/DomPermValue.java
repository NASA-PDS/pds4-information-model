package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DomPermValue extends ISOClassOAIS11179 {
	String searchKey;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String value;			// the value
	String value_meaning;	// the meaning of the value
	String value_begin_date;	// the value begin date
	String value_end_date;		// the value end date
	
	public DomPermValue () {
		searchKey = "TBD_searchKey";
		value = "TBD_value";
		value_meaning = "TBD_value_meaning";
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
	}
	
	public String getSearchKey() {
		return searchKey;
	}
	
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue_meaning() {
		return value_meaning;
	}
	
	public void setValue_meaning(String value_meaning) {
		this.value_meaning = value_meaning;
	}
	
	public String getValue_begin_date() {
		return value_begin_date;
	}
	
	public void setValue_begin_date(String value_begin_date) {
		this.value_begin_date = value_begin_date;
	}
	
	public String getValue_end_date() {
		return value_end_date;
	}
	
	public void setValue_end_date(String value_end_date) {
		this.value_end_date = value_end_date;
	}
}
