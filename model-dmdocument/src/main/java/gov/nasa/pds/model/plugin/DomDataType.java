package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DomDataType extends ISOClassOAIS11179 {
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
	
	public DomDataType () {
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCharacter_constraint() {
		return character_constraint;
	}
	
	public void setCharacter_constraint(String character_constraint) {
		this.character_constraint = character_constraint;
	}
	
	public String getFormation_rule() {
		return formation_rule;
	}
	
	public void setFormation_rule(String formation_rule) {
		this.formation_rule = formation_rule;
	}
	
	public String getMaximum_characters(boolean forceBound) {
		String lValue = this.maximum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_characters";
		return lValue;
	}
	
	public void setMaximum_characters(String maximum_characters) {
		this.maximum_characters = maximum_characters;
	}
	
	public String getMaximum_value(boolean forceBound) {
		String lValue = this.maximum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0 || lValue.compareTo("4294967295") == 0 || lValue.compareTo("INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_value";
		return lValue;
	}
	
	public void setMaximum_value(String maximum_value) {
		this.maximum_value = maximum_value;
	}
	
	public String getMinimum_characters(boolean forceBound) {
		String lValue = this.minimum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_characters";
		return lValue;
	}
	
	public void setMinimum_characters(String minimum_characters) {
		this.minimum_characters = minimum_characters;
	}
	
	public String getMinimum_value(boolean forceBound) {
		String lValue = this.minimum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0 || lValue.compareTo("-INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_value";
		return lValue;
	}
	
	public void setMinimum_value(String minimum_value) {
		this.minimum_value = minimum_value;
	}
	
	public String getXml_schema_base_type() {
		return xml_schema_base_type;
	}
	
	public void setXml_schema_base_type(String xml_schema_base_type) {
		this.xml_schema_base_type = xml_schema_base_type;
	}
	
	public String getCharacter_encoding() {
		return character_encoding;
	}
	
	public void setCharacter_encoding(String character_encoding) {
		this.character_encoding = character_encoding;
	}
	
	public ArrayList<String> getPattern() {
		return pattern;
	}
	
	public void setPattern(ArrayList<String> pattern) {
		this.pattern = pattern;
	}
	
	
		
}
