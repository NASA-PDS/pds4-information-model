package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DataTypeDefn {
	String identifier;
	String title;
	String nameSpaceIdNC;
	String type;
	String character_constraint;
	String formation_rule;
	String maximum_characters;
	String maximum_value;
	String minimum_characters;
	String minimum_value;
	String xml_schema_base_type;
	String character_encoding; 	 	
	
	ArrayList <String> pattern; 
	
	public DataTypeDefn (String id) {
		identifier = id;
		title = "TBD_title";
		nameSpaceIdNC = "TBD_nameSpaceIdNC";
		type = "TBD_type";
		character_constraint   = "TBD_character_constraint";       
		formation_rule	       = "TBD_formation_rule"; 
		maximum_characters	   = "TBD_maximum_characters"; 
		maximum_value	       = "TBD_maximum_value"; 
		minimum_characters     = "TBD_minimum_characters";  
		minimum_value	       = "TBD_minimum_value"; 
		xml_schema_base_type   = "TBD_xml_schema_base_type"; 
		character_encoding     = "TBD_character_encoding";                                        

		pattern = new ArrayList <String>();
	}
	
	//	get the minimum_characters for printing. Use the data type for a default.
	public String getMinimumCharacters2 (boolean forceBound) {
		String lValue = this.minimum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_characters";
		return lValue;
	}
	
	//	get the maximum_characters for printing. Use the data type for a default.
	public String getMaximumCharacters2 (boolean forceBound) {
		String lValue = this.maximum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_characters";
		return lValue;
	}
	
	//	get the minimum_value for printing. Use the data type for a default.
	public String getMinimumValue2 (boolean forceBound) {
		String lValue = this.minimum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0 || lValue.compareTo("-INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_value";
		return lValue;
	}
	
	//	get the maximum_value for printing. Use the data type for a default.
	public String getMaximumValue2 (boolean forceBound) {
		String lValue = this.maximum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0 || lValue.compareTo("4294967295") == 0 || lValue.compareTo("INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_value";
		return lValue;
	}
}
