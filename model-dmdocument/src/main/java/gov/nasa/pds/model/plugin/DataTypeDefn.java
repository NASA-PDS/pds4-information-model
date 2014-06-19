package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DataTypeDefn {
	String identifier;
	String title;
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
}
