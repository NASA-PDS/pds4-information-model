package gov.nasa.pds.model.plugin; 
import java.util.*;

public class InstDefn extends Object {

	// note that the master11179DataDict Map key = className + "." title
	String rdfIdentifier;							// e.g. dd79.DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
	String identifier; 								// e.g. dd79.DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
	String title;  									// e.g. DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
	String className;								// ISO 11179 Class Name, e.g. DataElement
	String steward;
	String nameSpaceId;
	String nameSpaceIdNC;
	String description;

	HashMap <String, ArrayList<String>> genSlotMap; 

	public InstDefn (String lRDFIdentifier) { 
		rdfIdentifier = lRDFIdentifier; 
		identifier = "TBD_identifier";
		title = "TBD_title"; 
		className = "TBD_className";
		steward = "TBD_steward";
		nameSpaceId = "TBD_nameSpaceId";
		nameSpaceIdNC = "TBD_nameSpaceIdNC";
		description = "TBD_description";
		
		genSlotMap = new HashMap <String, ArrayList<String>> ();
	}  	
} 	
