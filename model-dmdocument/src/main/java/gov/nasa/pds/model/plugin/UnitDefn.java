package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class UnitDefn {
	String identifier;
	String title;
	String type;
	String precision;
	String default_unit_id;
	
	ArrayList <String> unit_id; 
	
	public UnitDefn (String id) {
		identifier = id; 
		
		title = "TBD_title";           
		type = "TBD_type";              
		precision = "TBD precision";                        
		default_unit_id = "TBD_default_unit_id";                                         

		unit_id = new ArrayList <String>();
	} 
}
