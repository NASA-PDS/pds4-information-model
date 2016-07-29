package gov.nasa.pds.model.plugin;

import java.util.ArrayList;

public class PropertyMapsDefn {
	String rdfIdentifier;					// url
	String identifier;						// unique identifier of this set of property maps
	String title;						    // title for this set of property maps
//	String steward_id;						// steward of the this property map,  e.g. imaging
	String namespace_id;					// namespace id of this property map, e.g. imaging
	String description;						// description of this property map
	String external_property_map_id;		// identifier of an external property map, external to this context

	ArrayList <PropertyMapDefn> propertyMapArr;

	public PropertyMapsDefn (String id) {
		rdfIdentifier = id; 		
		identifier = id; 		
		title = "TBD_title";
//		steward_id = "TBD_steward_id";
		namespace_id = "TBD_namespace_id";
		description = "TBD_descriptiond";
		external_property_map_id = "TBD_external_property_map_id";

		propertyMapArr = new ArrayList <PropertyMapDefn>();
	} 
}
