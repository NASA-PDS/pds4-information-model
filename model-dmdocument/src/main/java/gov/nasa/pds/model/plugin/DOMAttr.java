package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class DOMAttr extends ISOClassOAIS11179 {
	String uid;										// unique identifier for rdfIdentifier
	String nsTitle;									// namespace + title
	String sort_identifier;							// lAttr.title + "_" + lAttr.steward + "_" + lAttr.className + "_" + lAttr.classSteward
	String attrAnchorString;						// "attribute", lAttr.attrNameSpaceIdNC, lAttr.title, lAttr.classNameSpaceIdNC, lAttr.className
	String XMLSchemaName;							// Title or Class_Title
	String regAuthId;								// registration authority identifier
	String classSteward;							// steward for attribute's class
	String classNameSpaceIdNC;
	String submitter;								// submitter for attribute
	String subModelId;								// identifier of submodel within the registration authority's model.
	String parentClassTitle;						// class that this attribute is a member of
	DOMClass attrParentClass; 						// class instance that this attribute is a member of
	String classConcept;							// for DEC
	String dataConcept;							    // for CD
	String classWord;								// for nomenclature rules
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
	boolean isRestrictedInSubclass; // *** deprecate *** moved to DOMProp
	boolean isMeta;
	boolean hasAttributeOverride;
	boolean isNilable;
	boolean isChoice;				// Association or Instance attributes that require a class choice
	boolean isAny;					// Association or Instance attribute that allows a class any
	boolean isFromLDD;				// attribute came from an LDD
	boolean hasRetiredValue;		// at least one permissible value has been retired.
	
	ArrayList <DOMProp> domPermValueArr;
	
	ArrayList <String> valArr;
	ArrayList <PDSObjDefn> valClassArr;	// classes for for assoc (AttrDefn) valArr
	ArrayList <String> allowedUnitId;	// the unit ids allowed from the set of measurement units.
	HashMap <String, ArrayList<String>> genAttrMap; 
	ArrayList <PermValueDefn> permValueArr;
	ArrayList <PermValueExtDefn> permValueExtArr;
//	TreeMap <String, TermEntryDefn> termEntryMap;
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
	
	public DOMAttr () {
		uid = "TBD_uid";
		nsTitle = "TBD_nsTitle"; 
		sort_identifier = "TBD_sort_identifier";
		attrAnchorString = "TBD_attrAnchorString";
		XMLSchemaName = "TBD_XMLSchemaName";
		classSteward = "TBD_classSteward";
		classNameSpaceIdNC = "TBD_classNameSpaceId";
		submitter = "TBD_submitter";
		subModelId = "TBD_submodel_identifier";
		parentClassTitle = "TBD_parentClassTitle";
		attrParentClass = null;
		classConcept = "TBD_classConcept"; 
		dataConcept = "TBD_dataConcept"; 
		classWord = "TBD_classWord"; 
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

		domPermValueArr = new ArrayList <DOMProp> ();
		valArr = new ArrayList <String> (); 
		valClassArr = new ArrayList <PDSObjDefn> (); 
		allowedUnitId = new ArrayList <String> ();
		permValueArr = new ArrayList <PermValueDefn> ();
		permValueExtArr = new ArrayList <PermValueExtDefn> ();
//		termEntryMap = new TreeMap <String, TermEntryDefn> ();
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
	
	public void setIdentifier(String lNameSpaceIdNC, String lTitle, String lNameSpaceIdNC2, String lTitle2) {
		this.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lNameSpaceIdNC + "." + lTitle + "." + lNameSpaceIdNC2 + "." + lTitle2;
	}

	public void createDOMAttrSingletons (AttrDefn lOldAttr) {
//		System.out.println("debug - createDOMAttrSingletons - Phase 3 - lOldAttr.rdfIdentifier: " + lOldAttr.rdfIdentifier);							
		rdfIdentifier = lOldAttr.rdfIdentifier; 
		identifier = lOldAttr.identifier; 
		versionId = lOldAttr.versionId; 
		sequenceId = lOldAttr.uid; 
		
		title = lOldAttr.title; 
		definition = lOldAttr.description;
		
		registrationStatus = lOldAttr.registrationStatus; 
//		isDeprecated = lOldAttr.isDeprecated; 
		
		regAuthId = lOldAttr.regAuthId; 
		steward = lOldAttr.steward; 
		nameSpaceId = lOldAttr.attrNameSpaceId;
		nameSpaceIdNC = lOldAttr.attrNameSpaceIdNC;

		uid = lOldAttr.uid; 
		nsTitle = lOldAttr.nsTitle; 
		sort_identifier = lOldAttr.sort_identifier; 
		attrAnchorString = lOldAttr.attrAnchorString; 

		XMLSchemaName = lOldAttr.XMLSchemaName; 
		classSteward = lOldAttr.classSteward; 
		classNameSpaceIdNC = lOldAttr.classNameSpaceIdNC;
		submitter = lOldAttr.submitter; 
		subModelId = lOldAttr.subModelId; 
		parentClassTitle = lOldAttr.parentClassTitle; 
		classConcept = lOldAttr.classConcept; 
		dataConcept = lOldAttr.dataConcept; 
		classWord = lOldAttr.classWord; 

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
	
	public void initAttrParentClass (AttrDefn lOldAttr, TreeMap <String, DOMClass> lDOMClassIdMap) {		
		PDSObjDefn lOldAttrParentClass = lOldAttr.attrParentClass;
		if (lOldAttrParentClass != null) {
			String lIdentifier = lOldAttrParentClass.identifier;
			if (lIdentifier != null && (lIdentifier.indexOf("TBD") != 0) ) {
				attrParentClass = lDOMClassIdMap.get(lIdentifier);
			} else {
				System.out.println(">>warning  - initAttrParentClass  - Failed to get attrParentClass - lOldAttr.attrParentClass.identifier: " + lIdentifier);				
			}
		} else {
			System.out.println(">>warning  - initAttrParentClass  - Null attrParentClass - lOldAttr.identifier: " + lOldAttr.identifier);							
		}
	}
	
	// copy a string array
	public void InitStringArr (ArrayList <String> lDomStrArr, ArrayList <String> lPDSStrArr) {
		if (lPDSStrArr == null) return;
		for (Iterator <String> i = lPDSStrArr.iterator(); i.hasNext();) {
			String lOldStr = (String) i.next();
			if (lOldStr != null)
				lDomStrArr.add(lOldStr);
			else
				System.out.println(">>error    - InitStringArr - Null DomStr");
		}
	}
	
	//	get the value type for printing. 
	public String getValueType (boolean forceBound) {
		String lValue = this.valueType;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		if (forceBound) {
			return "ASCII_Short_String_Collapsed";
		}
		return "TBD_value_type";
	}
	
	
	//	get the identifier for this value type
	public String getValueTypeIdentifier () {
		
		// check if there is a value type
		String lValueType = this.valueType;
		if ((lValueType.indexOf("TBD") == 0) || (lValueType.compareTo("") == 0)) return null;

		// get the data type
		String llValueTypeId = InfoModel.getClassIdentifier ("pds", lValueType);

		PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassIdMap.get(llValueTypeId);
		if (lClass == null) return null;

		return lClass.identifier;
	}	
		
	//	get the minimum_characters for printing. Use the data type for a default.
	public String getMinimumCharacters (boolean useDataTypeForUNK, boolean forceBound) {
		String lValue = this.minimum_characters;
		if (lValue.indexOf("TBD") == 0 && useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType == null) return "TBD_minimum_characters";
			lValue = lDataType.minimum_characters;
		}
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_characters";
		return lValue;
	}
	
	//	get the maximum_characters for printing. Use the data type for a default.
	public String getMaximumCharacters (boolean useDataTypeForUNK, boolean forceBound) {
		String lValue = this.maximum_characters;
		if (lValue.indexOf("TBD") == 0 && useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType == null) return "TBD_maximum_characters";
			lValue = lDataType.maximum_characters;
		}
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_characters";
		return lValue;
	}
	
	//	get the minimum_value for printing. Use the data type for a default.
	public String getMinimumValue (boolean useDataTypeForUNK, boolean forceBound) {
		String lValue = this.minimum_value;
		if (lValue.indexOf("TBD") == 0 && useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType == null) return "TBD_minimum_value";
			lValue = lDataType.minimum_value;
		}
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0 || lValue.compareTo("-INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_value";
		return lValue;
	}
	
	//	get the maximum_value for printing. Use the data type for a default.
	public String getMaximumValue (boolean useDataTypeForUNK, boolean forceBound) {
		String lValue = this.maximum_value;
		if (lValue.indexOf("TBD") == 0 && useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType == null) return "TBD_maximum_value";
			lValue = lDataType.maximum_value;
		}
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0 || lValue.compareTo("4294967295") == 0 || lValue.compareTo("INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_value";
		return lValue;
	}

	//	get the format for printing. Use the data type for a default.
	public String getFormat (boolean useDataTypeForUNK) {
		String lValue = this.format;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		if (useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType != null) {
				lValue = lDataType.formation_rule;
				if (! (lValue.indexOf("TBD") == 0)) {
					return lValue;
				}
			}
		}
		return "TBD_format";
	}
	
	//	get the maximum_value for printing. Use the data type for a default.
	public String getPattern (boolean useDataTypeForUNK) {
		String lValue = this.pattern;
		if (lValue.indexOf("TBD") == 0 && useDataTypeForUNK) {
			DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(this.valueType);
			if (lDataType == null) return "TBD_pattern";
			if (lDataType.pattern.isEmpty()) return "TBD_pattern";
			if (lDataType.pattern.size() > 1) return "TBD_pattern";
			lValue = lDataType.pattern.get(0);
		}
		if (lValue.compareTo("") == 0) return "TBD_pattern";
		return lValue;
	}	

	//	get the unit_of_measure_type for printing.
	public String getUnitOfMeasure (boolean forceBound) {
		String lValue = this.unit_of_measure_type;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return InfoModel.unEscapeProtegeString(lValue);
		}
		if (forceBound) {
			return "Units_of_None";
		}
		return "TBD_unit_of_measure_type";			
	}
	
	//	get the identifier for this Unit Of Measure
	public String getUnitOfMeasureIdentifier () {
		
		// check if there is a UnitOfMeasure
		String lUnitOfMeasure = this.unit_of_measure_type;
		if ((lUnitOfMeasure.indexOf("TBD") == 0) || (lUnitOfMeasure.compareTo("") == 0)) return null;

		// get the unit of measure type
		String lUnitOfMeasureId = InfoModel.getClassIdentifier ("pds", lUnitOfMeasure);

		PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassIdMap.get(lUnitOfMeasureId);
		if (lClass == null) return null;

		return lClass.identifier;
	}

	//	get the units for this unit_of_measure_type.
	public String getUnits (boolean needsQuotes) {
		
		// check if there is a unit of measure type
		String lUnitOfMeasureType = this.unit_of_measure_type;
		if ((lUnitOfMeasureType.indexOf("TBD") == 0) || (lUnitOfMeasureType.compareTo("") == 0)) return null;

		String lUnitsValueString = "";

		// get the unit of measure type
		String lUnitIdId = InfoModel.getAttrIdentifier ("pds", lUnitOfMeasureType, "pds", "unit_id");

		AttrDefn lAttr = (AttrDefn) InfoModel.masterMOFAttrIdMap.get(lUnitIdId);
		if (lAttr == null) return null;

		// check if there are any permissible values
		if ((lAttr.permValueArr == null || lAttr.permValueArr.isEmpty()))  return null;
		
		// create the value string
		String lDel = "";
		for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
			String lValue = lPermValueDefn.value;
			if (needsQuotes) {
				lValue = "'" + lValue + "'";
			}
			lUnitsValueString += lDel + lValue;
			lDel = ", ";
		}
		return lUnitsValueString;
	}
		
	//	get the default_unit_id (specified unit) for printing.
	public String getDefaultUnitId (boolean forceBound) {
		String lValue = this.default_unit_id;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return InfoModel.unEscapeProtegeString(lValue);
		}
		if (forceBound) {
			return "none";
		}
		return "TBD_default_unit_id";
	}	
	
	//	get the steward for printing.
	public String getSteward () {
		String lValue = this.steward;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_steward";
	}	

	/*
	//	get the name space id for printing.
	public String getNameSpaceId () {
		String lValue = this.nameSpaceIdNC;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_namespace_id";
	}
	*/
	
	//	get the classConcept for printing.
	public String getClassConcept () {
		String lValue = this.classConcept;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_class_concept";
	}
	
	//	get the dataConcept for printing.
	public String getDataConcept () {
		String lValue = this.dataConcept;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_data_concept";
	}

	/* moved to ISOClassOAIS11179
	//	get the name in the indicated language; use the attribute title as a default
	public String getNameInLanguage (String lLanguage) {
		if (lLanguage == null) return this.title;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.title;
		return lTermEntry.name;
	}
	
	//	get the name in the indicated language; use the attribute description as a default
	public String getDefinitionInLanguage (String lLanguage) {
		if (lLanguage == null) return this.definition;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.definition;
		return lTermEntry.definition;
	} */	
}
