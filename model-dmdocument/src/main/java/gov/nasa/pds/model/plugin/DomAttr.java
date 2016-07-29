package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class DomAttr extends ISOClassOAIS11179 {
	String rdfIdentifier;							// url, namespace, name
	String uid;										// unique identifier for rdfIdentifier
	String identifier; 								// no url, namespace, name (one pair for class, two pair for attribute)
	String nsTitle;									// namespace + title
	String sort_identifier;							// lAttr.title + "_" + lAttr.steward + "_" + lAttr.className + "_" + lAttr.classSteward
	String attrAnchorString;						// "attribute", lAttr.attrNameSpaceIdNC, lAttr.title, lAttr.classNameSpaceIdNC, lAttr.className
	String title;  									// no url, no namespace, name
	String versionId;								// version id
	String registrationStatus;						// ISO 11179 item registration status
	String XMLSchemaName;							// Title or Class_Title
	String regAuthId;								// registration authority identifier
	String steward;									// steward for attribute
	String classSteward;							// steward for attribute's class
	String attrNameSpaceId;
	String attrNameSpaceIdNC;
	String classNameSpaceIdNC;
	String submitter;								// submitter for attribute
	String subModelId;								// identifier of submodel within the registration authority's model.
	String parentClassTitle;						// class that this attribute is a member of
	PDSObjDefn attrParentClass; 					// class instance that this attribute is a member of
	String classConcept;							// for DEC
	String dataConcept;							    // for CD
	String classWord;								// for nomenclature rules
	String description;
	String lddLocalIdentifier;						// LDD local identifier
	AttrDefn lddUserAttribute;						// the USER attribute used to initialize the LDD attribute

	String xmlBaseDataType;							// the XML base data type
	String protValType;								// value type from protege model
	String propType;								// Instance or Attribute
	String valueType;								// Master value type 
	String groupName;								// the choice group name
	String cardMin;
	String cardMax;
	int cardMinI;
	int cardMaxI;

	String minimum_characters;		// minimum number of characters
	String maximum_characters;		// maximum number of characters
	String minimum_value;			// minimum value 
	String maximum_value;			// maximum value 
	String format;					// a template for the structure of the presentation of the Value(s) e.g. YYYY-MM-DD for a date.
	String pattern;					// a regular expression
	String unit_of_measure_type;	//
	String default_unit_id;			//
	String unit_of_measure_precision;	//
		
//	String type;
	boolean isAttribute;			// true->attribute; false->association
	boolean isOwnedAttribute;		// true->attribute is owned by this class, as opposed to inherited
	boolean isPDS4;					// true->PDS4 keyword used in Protege
	boolean isEnumerated;
	boolean isUsedInClass;			// attribute is used in a class
	boolean isRestrictedInSubclass;
	boolean isMeta;
	boolean hasAttributeOverride;
	boolean isNilable;
	boolean isChoice;				// Association or Instance attributes that require a class choice
	boolean isAny;					// Association or Instance attribute that allows a class any
	boolean isFromLDD;				// attribute came from an LDD
	boolean hasRetiredValue;		// at least one permissible value has been retired.
	
	ArrayList <String> valArr;
	ArrayList <PDSObjDefn> valClassArr;	// classes for for assoc (AttrDefn) valArr
	ArrayList <String> allowedUnitId;	// the unit ids allowed from the set of measurement units.
	HashMap <String, ArrayList<String>> genAttrMap; 
	ArrayList <PermValueDefn> permValueArr;
	ArrayList <PermValueExtDefn> permValueExtArr;
	TreeMap <String, TermEntryDefn> termEntryMap;
	TreeMap <String, String> valueDependencyMap;
	
	String dataIdentifier; 						// data identifier
	String deDataIdentifier;					// data element
	String decDataIdentifier;					// data element concept
	String ecdDataIdentifier;					// enumerated conceptual domain 
	String evdDataIdentifier;					// enumerated value domain
	String necdDataIdentifier;					// non enumerated conceptual domain 
	String nevdDataIdentifier;					// non enumerated value domain
	String pvDataIdentifier;					// permissible value
	String vmDataIdentifier;					// value meaning
	
	String desDataIdentifier;					// designation 
	String defDataIdentifier;					// definition
	String lsDataIdentifier;					// language section 
	String teDataIdentifier;					// terminological entry
	String prDataIdentifier;					// property data identifier (Attribute and Association)
	
	String administrationRecordValue;
	String versionIdentifierValue;
	String registeredByValue;
	String registrationAuthorityIdentifierValue;
	
	ArrayList <String> expressedByArr;
	ArrayList <String> representing1Arr;
	ArrayList <String> representedBy1Arr;
	ArrayList <String> representedBy2Arr;
	ArrayList <String> containedIn1Arr; 
	
	ArrayList <String> genClassArr;
	ArrayList <String> sysClassArr;	
	
	public DomAttr () {
		rdfIdentifier = "TBD_identifier"; 
		uid = "TBD_uid";
		identifier = "TBD_identifier"; 
		nsTitle = "TBD_nsTitle"; 
		sort_identifier = "TBD_sort_identifier";
		attrAnchorString = "TBD_attrAnchorString";
		title = "TBD_title";  
		versionId = "TBD_versionId";
		registrationStatus = "TBD_registrationStatus";
		XMLSchemaName = "TBD_XMLSchemaName";
		regAuthId = "TBD_registration_authority_identifier";
		steward = "TBD_steward";
		classSteward = "TBD_classSteward";
		classNameSpaceIdNC = "TBD_classNameSpaceId";
		attrNameSpaceId = "TBD_attrNameSpaceId";
		attrNameSpaceIdNC = "TBD_attrNameSpaceIdNC";
		submitter = "TBD_submitter";
		subModelId = "TBD_submodel_identifier";
		parentClassTitle = "TBD_parentClassTitle";
		attrParentClass = null;
		classConcept = "TBD_classConcept"; 
		dataConcept = "TBD_dataConcept"; 
		classWord = "TBD_classWord"; 
		description = "TBD_description"; 
		lddLocalIdentifier = "TBD_lddLocalIdentifier";
		lddUserAttribute = null;

		xmlBaseDataType = "TBD_XML_Base_Data_Type";
		protValType = "TBD_Protege_Value_type";
		propType = "TBD_slot_type";
		valueType = "TBD_value_type";
		groupName = "TBD_groupName";
		cardMin = "TBD_cardMin";
		cardMax = "TBD_cardMax";
		cardMinI = -99999;
		cardMaxI = -99999;
		
		maximum_characters = "TBD_maximum_characters";
		minimum_characters = "TBD_minimum_characters";
		maximum_value = "TBD_maximum_value";
		minimum_value = "TBD_minimum_value";
		format = "TBD_format";
		pattern = "TBD_pattern";
		unit_of_measure_type = "TBD_unit_of_measure_type";
		default_unit_id = "TBD_default_unit_id";
		unit_of_measure_precision = "TBD_unit_of_measure_precision";
		
		isAttribute = true;
		isOwnedAttribute = false;
		isPDS4 = false;
		isEnumerated = false;
		isUsedInClass = false;
		isRestrictedInSubclass = false;
		isMeta = false;
		hasAttributeOverride = false;
		isNilable = false;
		isChoice = false;
		isAny = false;
		isFromLDD = false;
		hasRetiredValue = false;

		valArr = new ArrayList <String> (); 
		valClassArr = new ArrayList <PDSObjDefn> (); 
		allowedUnitId = new ArrayList <String> ();
		permValueArr = new ArrayList <PermValueDefn> ();
		permValueExtArr = new ArrayList <PermValueExtDefn> ();
		termEntryMap = new TreeMap <String, TermEntryDefn> ();
		valueDependencyMap = new TreeMap <String, String> ();
		
		deDataIdentifier = "TBD_deDataIdentifier";				// data element
		decDataIdentifier = "TBD_decDataIdentifier";			// data element concept
		ecdDataIdentifier = "TBD_ecdDataIdentifier";			// enumerated conceptual domain 
		necdDataIdentifier = "TBD_necdDataIdentifier";			// non enumerated conceptual domain 
		evdDataIdentifier = "TBD_evdDataIdentifier";			// enumerated value domain
		nevdDataIdentifier = "TBD_nevdDataIdentifier";			// non enumerated value domain
		pvDataIdentifier = "TBD_pvDataIdentifier";				// permissible value
		vmDataIdentifier = "TBD_vmDataIdentifier";				// value meaning

		desDataIdentifier = "TBD_desDataIdentifier";			// designation 
		defDataIdentifier = "TBD_defDataIdentifier";			// definition
		lsDataIdentifier = "TBD_lsDataIdentifier";				// language section 
		teDataIdentifier = "TBD_teDataIdentifier";				// terminlogical entry			
		prDataIdentifier = "TBD_prDataIdentifier";				// property			
		
		administrationRecordValue = "TBD_administrationRecordValue";           
		versionIdentifierValue = "TBD_versionIdentifierValue";                                                      
		registeredByValue = "TBD_registeredByValue";                   
		registrationAuthorityIdentifierValue = "TBD_registrationAuthorityIdentifierValue";
		expressedByArr = null;		// DE -> expressedBy -> DEC
		representing1Arr = null;	// DE -> representing -> EVD or NEVD
		representedBy1Arr = new ArrayList <String>();	// VD -> representedBy -> CD;   aka has_CD
		representedBy2Arr = new ArrayList <String>();	// VD -> representedBy -> DE;   aka has_DE
		containedIn1Arr = new ArrayList <String>();	    // VD -> containedIn1 -> VM/PV;   aka has_VM
		genClassArr = null;
		sysClassArr = null;
	}
	
	public void initDomAttr (AttrDefn lOldAttr) {
		initDomAttrSingletons (lOldAttr);
	}
	
	public void initDomAttrSingletons (AttrDefn lOldAttr) {
		rdfIdentifier = lOldAttr.rdfIdentifier; 
		uid = lOldAttr.uid; 
		identifier = lOldAttr.identifier; 
		nsTitle = lOldAttr.nsTitle; 
		sort_identifier = lOldAttr.sort_identifier; 
		attrAnchorString = lOldAttr.attrAnchorString; 
		title = lOldAttr.title; 
		versionId = lOldAttr.versionId; 
		registrationStatus = lOldAttr.registrationStatus; 
		XMLSchemaName = lOldAttr.XMLSchemaName; 
		regAuthId = lOldAttr.regAuthId; 
		steward = lOldAttr.steward; 
		classSteward = lOldAttr.classSteward; 
		attrNameSpaceId = lOldAttr.attrNameSpaceId;
		attrNameSpaceIdNC = lOldAttr.attrNameSpaceIdNC;
		classNameSpaceIdNC = lOldAttr.classNameSpaceIdNC;
		submitter = lOldAttr.submitter; 
		subModelId = lOldAttr.subModelId; 
		parentClassTitle = lOldAttr.parentClassTitle; 
//		 PDSObjDefn attrParentClass = lOldAttr.attrParentClass; 
		classConcept = lOldAttr.classConcept; 
		dataConcept = lOldAttr.dataConcept; 
		classWord = lOldAttr.classWord; 
		description = lOldAttr.description;
		lddLocalIdentifier = lOldAttr.lddLocalIdentifier; 
//		 AttrDefn lddUserAttribute = lOldAttr.lddUserAttribute; 
		 
		xmlBaseDataType = lOldAttr.xmlBaseDataType; 
		protValType = lOldAttr.protValType; 
		propType = lOldAttr.propType; 
		valueType = lOldAttr.valueType; 
		groupName = lOldAttr.groupName; 
		cardMin = lOldAttr.cardMin;
		cardMax = lOldAttr.cardMax;
		cardMinI = lOldAttr.cardMinI;
		cardMaxI = lOldAttr.cardMaxI;
		 
		minimum_characters = lOldAttr.minimum_characters; 
		maximum_characters = lOldAttr.maximum_characters; 
		minimum_value = lOldAttr.minimum_value; 
		maximum_value = lOldAttr.maximum_value; 
		format = lOldAttr.format; 
		pattern = lOldAttr.pattern; 
		unit_of_measure_type = lOldAttr.unit_of_measure_type; 
		default_unit_id = lOldAttr.default_unit_id; 
		unit_of_measure_precision = lOldAttr.unit_of_measure_precision; 
		 
		isAttribute = lOldAttr.isAttribute; 
		isOwnedAttribute = lOldAttr.isOwnedAttribute; 
		isPDS4 = lOldAttr.isPDS4; 
		 
		isEnumerated = lOldAttr.isEnumerated;
		isUsedInClass = lOldAttr.isUsedInClass; 
		isRestrictedInSubclass = lOldAttr.isRestrictedInSubclass;
		isMeta = lOldAttr.isMeta;
		hasAttributeOverride = lOldAttr.hasAttributeOverride;
		isNilable = lOldAttr.isNilable;
		isChoice = lOldAttr.isChoice; 
		isAny = lOldAttr.isAny; 
		isFromLDD = lOldAttr.isFromLDD; 
		hasRetiredValue = lOldAttr.hasRetiredValue; 
		
		InitStringArr (this.valArr, lOldAttr.valArr);
		InitStringArr (this.allowedUnitId, lOldAttr.allowedUnitId);
/*		InitStringArr (this.x, lOldAttr.x);   *** TBD ***
		InitStringArr (this.x, lOldAttr.x);
		InitStringArr (this.x, lOldAttr.x);
		InitStringArr (this.x, lOldAttr.x);
		InitStringArr (this.x, lOldAttr.x);
		InitStringArr (this.x, lOldAttr.x); */

		
/*		
		 ArrayList <String> valArr = lOldAttr.valArr;
		 ArrayList <PDSObjDefn> valClassArr = lOldAttr.valClassArr; 
		 ArrayList <String> allowedUnitId = lOldAttr.allowedUnitId; 
		 HashMap <String, ArrayList<String>> genAttrMap = lOldAttr.genAttrMap; 
		 ArrayList <PermValueDefn> permValueArr = lOldAttr.permValueArr;
		 ArrayList <PermValueExtDefn> permValueExtArr = lOldAttr.permValueExtArr;
		 TreeMap <String, TermEntryDefn> termEntryMap = lOldAttr.termEntryMap;
		 TreeMap <String, String> valueDependencyMap = lOldAttr.valueDependencyMap;
		 */
		 
		dataIdentifier = lOldAttr.dataIdentifier; 
		deDataIdentifier = lOldAttr.deDataIdentifier; 
		decDataIdentifier = lOldAttr.decDataIdentifier; 
		ecdDataIdentifier = lOldAttr.ecdDataIdentifier; 
		evdDataIdentifier = lOldAttr.evdDataIdentifier; 
		necdDataIdentifier = lOldAttr.necdDataIdentifier; 
		nevdDataIdentifier = lOldAttr.nevdDataIdentifier; 
		pvDataIdentifier = lOldAttr.pvDataIdentifier; 
		vmDataIdentifier = lOldAttr.vmDataIdentifier; 
		 
		desDataIdentifier = lOldAttr.desDataIdentifier; 
		defDataIdentifier = lOldAttr.defDataIdentifier; 
		lsDataIdentifier = lOldAttr.lsDataIdentifier; 
		teDataIdentifier = lOldAttr.teDataIdentifier; 
		prDataIdentifier = lOldAttr.prDataIdentifier; 
		 
		administrationRecordValue = lOldAttr.administrationRecordValue;
		versionIdentifierValue = lOldAttr.versionIdentifierValue;
		registeredByValue = lOldAttr.registeredByValue;
		registrationAuthorityIdentifierValue = lOldAttr.registrationAuthorityIdentifierValue;
		
		
		InitStringArr (this.expressedByArr, lOldAttr.expressedByArr);
		InitStringArr (this.representing1Arr, lOldAttr.representing1Arr);
		InitStringArr (this.representedBy1Arr, lOldAttr.representedBy1Arr);
		InitStringArr (this.representedBy2Arr, lOldAttr.representedBy2Arr);
		InitStringArr (this.containedIn1Arr, lOldAttr.containedIn1Arr);
		
		InitStringArr (this.genClassArr, lOldAttr.genClassArr);
		InitStringArr (this.sysClassArr, lOldAttr.sysClassArr);
		return;
	}
	
	// copy a string array
	public void InitStringArr (ArrayList <String> lDomStrArr, ArrayList <String> lPDSStrArr) {
		for (Iterator <String> i = lPDSStrArr.iterator(); i.hasNext();) {
			String lOldStr = (String) i.next();
			if (lOldStr != null)
				lDomStrArr.add(lOldStr);
			else
				System.out.println(">>error    - InitStringArr - Null DomStr");
		}
	}
	
	
}
