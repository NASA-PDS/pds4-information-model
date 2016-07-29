package gov.nasa.pds.model.plugin;
import java.util.*;

public class Class2Defn extends ClassDefn {

//	String rdfIdentifier;							// url, namespace, title -- used for object dictionary (hashmap)
//	String identifier; 								// no url, namespace, title (Is used as funcational equivalent of attr.nsTitle)
//	String title;  									// no url, no namespace, title
//	String versionId;								// the version of this class
//	String registrationStatus;						// ISO 11179 item registration status
//	String anchorString;							// "class_" + lClass.nameSpaceIdNC + "_" + lClass.title
//	String regAuthId;								// registration authority identifier
//	String steward;									// steward
//	String nameSpaceId;								// namespace id - assigned namespace id with colon
//	String nameSpaceIdNC;							// namespace id - assigned namespace id No Colon
	String section;									// section of the info model specification document for  this class
	String subModelId;								// identifier of submodel within the registration authority's model.
	String role;									// abstract or concrete
//	String description;
	String docSecType;								// class type = title
	String subClassOfTitle;							// title
	String subClassOfIdentifier;					// namespace + title
	String rootClass;								// RDF identifier
	String baseClassName;							// Fundamental structure class title
	String localIdentifier;							// used temporarily for ingest of LDD
	
	int subClassLevel;
	boolean isUSERClass;							// The class of all classes
	boolean isMasterClass;							// will be included in the master class map
	boolean isSchema1Class;							// will have a schema type 1 created
	boolean isRegistryClass;						// will be included in registry configuration file
	boolean isUsedInModel;
	boolean isVacuous;								// a vacuous class is empty and therefore not to be included in schemas
	boolean isUnitOfMeasure;
	boolean isDataType;
	boolean isTDO;
	boolean isAbstract;
	boolean isChoice;								// class requires xs:choice
	boolean isAny;									// class requires xs:any
	boolean includeInThisSchemaFile;
	boolean isFromLDD;									// has been ingested from Ingest_LDD
	boolean isReferencedFromLDD;						// is a class in the master that is referenced from an LDD
	
	TreeMap <String, TermEntryDefn> termEntryMap;
	
	ArrayList <AttrDefn> hasSlot;
	ArrayList <String> subClasses; 
	Class2Defn subClassOfInst;
	ArrayList <Class2Defn> superClass; 
	ArrayList <Class2Defn> subClass; 
	
	ArrayList <String> ownedAttrNSTitle; 
	ArrayList <String> ownedAssocNSTitle; 
	ArrayList <String> inheritedAttrNSTitle; 
	ArrayList <String> inheritedAssocNSTitle; 	
	
	ArrayList <AttrDefn> ownedAttribute; 
	ArrayList <AttrDefn> inheritedAttribute; 
	ArrayList <AttrDefn> ownedAssociation; 
	ArrayList <AttrDefn> inheritedAssociation; 
	
	ArrayList <AttrDefn> allAttrAssocArr; 
	ArrayList <AttrDefn> ownedAttrAssocNOArr;
	ArrayList <AttrDefn> ownedAttrAssocArr;				// each class's owned attribute and associations in sorted order
	ArrayList <AttrDefn> ownedAttrAssocAssertArr;		// all enumerated attributes, from this.class through to all superclasses.
	ArrayList <String> ownedAttrAssocAssertTitleArr;	// all enumerated attributes, required to eliminate duplicates
	
	ArrayList <AssocDefn> PropertyArr;					// Class Properties
	
	public Class2Defn (String rdfId) {
		super (rdfId);
//		identifier = "TBD_identifier"; 
//		rdfIdentifier = rdfId; 
//		title = "TBD_title"; 
//		versionId = DMDocument.classVersionIdDefault;
//		registrationStatus = "TBD_registrationStatus";
//		anchorString = "TBD_anchorString";
//		regAuthId = "TBD_registration_authority_identifier";
//		steward = "TBD_steward";
//		nameSpaceId = "TBD_namespaceid";
//		nameSpaceIdNC = "TBD_namespaceidNC";
		section = "TBD_section";
		subModelId = "TBD_submodel_identifier";
		role = "TBD_role";
//		description = "TBD_description"; 
		docSecType = "TBD_type"; 
		subClassOfTitle = "TBD_subClassOfTitle";
		subClassOfIdentifier = "TBD_subClassOfIdentifier";
		rootClass = "TBD_root_class";
		baseClassName = "TBD_base_class_name";
		localIdentifier = "TBD_localIdentifier";
		subClassLevel = 0;
		isUSERClass = false;
		isMasterClass = false;
		isSchema1Class = false;
		isRegistryClass = false;
		isUsedInModel = false;
		isVacuous = false;
		isUnitOfMeasure = false;
		isDataType = false;
		isTDO = false;
		isAbstract = false;
		isChoice = false;
		isAny = false;
		includeInThisSchemaFile = false;
		isFromLDD = false;
		isReferencedFromLDD = false;
		
		termEntryMap = new TreeMap <String, TermEntryDefn> ();
		hasSlot = new ArrayList <AttrDefn> ();
		subClasses = new ArrayList <String> ();  
		subClassOfInst = null;
		superClass = new ArrayList <Class2Defn> (); 
		subClass = new ArrayList <Class2Defn> (); 
				
		ownedAttrNSTitle = new ArrayList <String> (); 
		ownedAssocNSTitle = new ArrayList <String> (); 
		inheritedAttrNSTitle = new ArrayList <String> (); 
		inheritedAssocNSTitle = new ArrayList <String> (); 
		
		ownedAttribute = new ArrayList <AttrDefn> (); 
		inheritedAttribute = new ArrayList <AttrDefn> (); 
		ownedAssociation = new ArrayList <AttrDefn> (); 
		inheritedAssociation = new ArrayList <AttrDefn> (); 
		
		allAttrAssocArr = new ArrayList <AttrDefn> (); 
		ownedAttrAssocNOArr = new ArrayList <AttrDefn> ();
		ownedAttrAssocArr = new ArrayList <AttrDefn> ();
		ownedAttrAssocAssertArr = new ArrayList <AttrDefn> ();
		ownedAttrAssocAssertTitleArr = new ArrayList <String> ();
		
		PropertyArr = new ArrayList <AssocDefn> ();
	}  	
		
	//	get the name in the indicated language; use the attribute title as a default
	public String nameInLanguage (String lLanguage) {
		if (lLanguage == null) return this.title;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.title;
		return lTermEntry.name;
	}
	
	//	get the name in the indicated language; use the attribute description as a default
	public String definitionInLanguage (String lLanguage) {
		if (lLanguage == null) return this.description;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.description;
		return lTermEntry.definition;
	}
} 	
