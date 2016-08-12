package gov.nasa.pds.model.plugin; 
import java.util.*;

public class SectionContentDefn extends Object {

	String identifier; 
	String title; 
	String description;
	String modelId;
	String nameSpaceId;
	ArrayList <String> includeClassId;
	ArrayList <String> excludeClassId;
	ArrayList <String> includeClassType;

	public SectionContentDefn (String id) {
		identifier = id; 
		title = id; 
		description = "TBD_description"; 
		modelId = "TBD_model_id"; 
//		nameSpaceId = "TBD_name_space_id";
		nameSpaceId = "TBD_namespace_id";
		includeClassId = new ArrayList <String> ();
		excludeClassId = new ArrayList <String> ();		
		includeClassType = new ArrayList <String>();		
	}  	
} 	
