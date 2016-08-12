package gov.nasa.pds.model.plugin; 
import java.util.*;

public class AttrDefn extends Object {
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
//	String className;								// class that this attribute is a member of
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
// 445	boolean isUnitOfMeasure;
// 445	boolean isDataType;
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
//	ArrayList <VMDefn> containedIn1Arr; 
	ArrayList <String> containedIn1Arr; 
	
	ArrayList <String> genClassArr;
	ArrayList <String> sysClassArr;	
	
/* 	elements in array - possibly 
	String alias_name;
	String available_value_type;
	String bl_name;
	String bl_sql_format;
	String change_date;
	String column_desc;
	String column_name;
	String display_format;
	String general_classification_type;
	String general_data_type;
	String Standard_Value;
	String label_revision_note;
	String maximum_column_value;
	String maximum_length;
	String minimum_column_value;
	String minimum_length;
	String revision_date;
	String source_name;
	String sql_format;
	String standard_default;
	String standard_value_formation_rule;
	String standard_value_output_flag;
	String standard_value_type;
	String status_type;
	String system_classification_id;
	String terse_name;
	String text_flag;
	String unit_id;
	String user_id;	*/

	public AttrDefn (String rdfId) {
		rdfIdentifier = rdfId; 
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
		
//		type = "TBD_type";
		isAttribute = true;
		isOwnedAttribute = false;
		isPDS4 = false;
// 445		isUnitOfMeasure = false;
// 445		isDataType = false;
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
//		containedIn1Arr = new ArrayList <VMDefn>();	    // VD -> containedIn1 -> VM/PV;   aka has_VM
		containedIn1Arr = new ArrayList <String>();	    // VD -> containedIn1 -> VM/PV;   aka has_VM
		genClassArr = null;
		sysClassArr = null;
	}
	
	// set the attribute's identifier
	public void setAttrIdentifier (String lClassNameSpaceIdNC, String lClassTitle, String lAttrNameSpaceIdNC, String lAttrTitle) {
		identifier = InfoModel.getAttrIdentifier(lClassNameSpaceIdNC, lClassTitle, lAttrNameSpaceIdNC, lAttrTitle);	
		nsTitle = InfoModel.getAttrNSTitle(lAttrNameSpaceIdNC, lAttrTitle);	
	}
			
	public void set11179Attr (String lDataIdentifier) {
		// Data Element Identifiers	
		dataIdentifier = lDataIdentifier;
		deDataIdentifier = "DE." + lDataIdentifier;				// data element
		decDataIdentifier = "DEC." + lDataIdentifier;			// data element concept
		ecdDataIdentifier = "ECD." + lDataIdentifier;			// enumerated conceptual domain 
		necdDataIdentifier = "NECD." + lDataIdentifier;			// non enumerated conceptual domain 
		evdDataIdentifier = "EVD." + lDataIdentifier;			// enumerated value domain
		nevdDataIdentifier = "NEVD." + lDataIdentifier;			// non enumerated value domain
		pvDataIdentifier = "PV." + lDataIdentifier;				// permissible value
		vmDataIdentifier = "VM." + lDataIdentifier;				// value meaning

		desDataIdentifier = "DES." + lDataIdentifier;			// designation 
		defDataIdentifier = "DEF." + lDataIdentifier;			// definition
		lsDataIdentifier = "LS." + lDataIdentifier;				// language section 
		teDataIdentifier = "TE." + lDataIdentifier;				// terminological entry		
		prDataIdentifier = "PR." + lDataIdentifier;				// property 	
	}
		
// get attribute routines
	
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
	public String getMinimumCharacters2 (boolean useDataTypeForUNK, boolean forceBound) {
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
	public String getMaximumCharacters2 (boolean useDataTypeForUNK, boolean forceBound) {
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
	public String getMinimumValue2 (boolean useDataTypeForUNK, boolean forceBound) {
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
	public String getMaximumValue2 (boolean useDataTypeForUNK, boolean forceBound) {
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
	
// ========================================================	
	
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
	
	//	get the name space id for printing.
	public String getNameSpaceId () {
		String lValue = this.attrNameSpaceIdNC;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_namespace_id";
	}
	
	//	get the classConcept for printing.
	public String getClassConcept () {
		String lValue = this.classConcept;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_class_concept";
	}
	
	//	get the dataConcept for printing.
	public String dataConcept () {
		String lValue = this.dataConcept;
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.compareTo("") == 0))) {
			return lValue;
		}
		return "TBD_data_concept";
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
