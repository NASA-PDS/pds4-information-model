package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class PropertyMapDefn {
	String identifier;						// unique identifier of property map
	String title;						    // title for this property map
//	String steward_id;						// steward of this property map - e.g. VICAR or a mission
	String model_object_id;					// unique identifier of the the targeted class, attribute, or value
	String model_object_type;				// type {class, attribute, or value}
	String instance_id;						// unique identifier of a a targeted instance in a label, for example an xpath string
	String external_namespace_id;			// a namespaceid external to this context (me too)
	String description;						// description of this property map

	ArrayList <PropertyMapEntryDefn> propertyMapEntryArr;

	public PropertyMapDefn (String id) {	
		identifier = id; 
		title = "TBD_title";
//		steward_id = "TBD_steward_id";
		model_object_id = "TBD_model_object_id";
		model_object_type = "TBD_model_object_type";
		instance_id = "TBD_instance_id";
		external_namespace_id = "TBD_external_namespace_id";
		description = "TBD_description";

		propertyMapEntryArr = new ArrayList <PropertyMapEntryDefn>();
	} 
}
