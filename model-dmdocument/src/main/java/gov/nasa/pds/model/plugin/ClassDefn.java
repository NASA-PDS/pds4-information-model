package gov.nasa.pds.model.plugin; 
import java.util.*;

public class ClassDefn extends Object {

	String rdfIdentifier;							// url, namespace, title -- used for object dictionary (hashmap)
	String identifier; 								// no url, namespace, title (Is used as funcational equivalent of attr.nsTitle)
	String title;  									// no url, no namespace, title
	String versionId;								// the version of this class
	String registrationStatus;						// ISO 11179 item registration status
	String anchorString;							// "class_" + lClass.nameSpaceIdNC + "_" + lClass.title
	String regAuthId;								// registration authority identifier
	String steward;									// steward
	String nameSpaceId;								// namespace id - assigned namespace id with colon
	String nameSpaceIdNC;							// namespace id - assigned namespace id No Colon
//	String role;									// abstract or concrete
	String description;
	
	boolean isUsedInModel;
	boolean isAbstract;
	boolean isFromLDD;									// has been ingested from Ingest_LDD
	
	public ClassDefn (String rdfId) {
		identifier = "TBD_identifier"; 
		rdfIdentifier = rdfId; 
		title = "TBD_title"; 
//		versionId = "TBD_versionId";
		versionId = DMDocument.classVersionIdDefault;
		registrationStatus = "TBD_registrationStatus";
		anchorString = "TBD_anchorString";
		regAuthId = "TBD_registration_authority_identifier";
		steward = "TBD_steward";
		nameSpaceId = "TBD_namespaceid";
		nameSpaceIdNC = "TBD_namespaceidNC";
//		role = "TBD_role";
		description = "TBD_description"; 
		isUsedInModel = false;
		isAbstract = false;
		isFromLDD = false;
	}
} 	
