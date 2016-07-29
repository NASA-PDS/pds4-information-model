// import org.apache.commons.lang.WordUtils;
package gov.nasa.pds.model.plugin;

import java.util.*;
/*
 ** Read RDF XML from a file and write it to standard out
 */
public abstract class InfoModel extends Object {
	
	// global constants
	static String ont_version_id;						// 0.1.0.0.a
	static String lab_version_id;						// 0100a
	static String sch_version_id;						// 1.0.0
	static String ns_version_id;						// 01
	static String identifier_version_id;				// 0.1
	
	// global administrative records
	static ArrayList <AdminDefn> master11179AdminArr;
	static TreeMap <String, AdminDefn> master11179AdminMap;
	static TreeMap <String, AdminDefn> master11179AdminMapId;
	
	// global classes
	static PDSObjDefn masterMOFUserClass;
	static ArrayList <PDSObjDefn> masterMOFClassArr;
	static TreeMap <String, PDSObjDefn> masterMOFClassMap;
	static TreeMap <String, PDSObjDefn> masterMOFClassIdMap;
//	static TreeMap <String, PDSObjDefn> masterMOFClassTitleMap;
	
	static ArrayList <String> fundamentalStructures;

	// global attributes
	static TreeMap <String, AttrDefn> masterMOFAttrMap;
	static TreeMap <String, AttrDefn> masterMOFAttrIdMap;
	static ArrayList <AttrDefn> masterMOFAttrArr;
	static TreeMap <String, AttrDefn> userClassAttrIdMap;			// user Class Attributes (owned attributes); Id:class changed to USER, Attribute is a master attribute.
	static TreeMap <String, AttrDefn> userSingletonClassAttrIdMap;	// user Class Attributes (not owned attributes) 

	// global Properties (Currently a duplicate of masterMOFAssocMap)
//	static TreeMap <String, AssocDefn> masterMOFPropMap;
//	static TreeMap <String, AssocDefn> masterMOFPropIdMap;
//	static ArrayList <AssocDefn> masterMOFPropArr;

	// global associations
	static TreeMap <String, AssocDefn> masterMOFAssocMap;
	static TreeMap <String, AssocDefn> masterMOFAssocIdMap;
	static ArrayList <AssocDefn> masterMOFAssocArr;
		
	// global data types
	static TreeMap <String, PDSObjDefn> masterDataTypeMap;
	static ArrayList <PDSObjDefn> masterDataTypesArr;
	static TreeMap <String, DataTypeDefn> masterDataTypeMap2;
	static ArrayList <DataTypeDefn> masterDataTypesArr2;
	
	// global unitOfMeasure
	static TreeMap <String, UnitDefn> masterUnitOfMeasureMap;
	static ArrayList <UnitDefn> masterUnitOfMeasureArr;
	
	// global property maps
	static TreeMap <String, PropertyMapsDefn> masterPropertyMapsMap;
	static ArrayList <PropertyMapsDefn> masterPropertyMapsArr;
	
	// global 11179 data dictionary
	static ArrayList <String> masterMetaAttribute;
	
	// global 11179 data dictionary
	static TreeMap <String, InstDefn> master11179DataDict;

	// global science discipline facet map 
//	static TreeMap <String, InstDefn> masterProtPinsUpperModel;
	static TreeMap <String, SFDisciplineFacetDefn> sfDisciplineFacetDefnMap = new TreeMap <String, SFDisciplineFacetDefn> ();

	// special rdfIdentifiers
	static String protegeRootClassRdfId, protegeSlotClassRdfId;
	
	// All CD and DEC values for the Attributes
	static TreeMap <String, IndexDefn> cdAttrMap = new TreeMap <String, IndexDefn>();
	static TreeMap <String, IndexDefn> decAttrMap = new TreeMap <String, IndexDefn>();
	
	// class concepts - for CD and DEC
	static TreeMap <String, String> classConcept;
	static TreeMap <String, String> dataConcept;
	static TreeMap <String, String> dataTypePDS4ProtegeMap;
//	static HashMap <String, String> rawValueTypeMap;
	static TreeMap <String, String> rawValueTypeMap;
	static TreeMap <String, Integer> metricConceptMap;
	
	// new class concepts - for CD and DEC
	static TreeMap <String, String> dataTypeToConceptMap;
	static TreeMap <String, String> cdID2CDTitleMap;
	static TreeMap <String, String> cdTitle2CDIDMap;
	static TreeMap <String, String> decID2DECTitleMap;
	static TreeMap <String, String> decTitle2DECIDMap;
	
	static PDSObjDefn LDDToolSingletonClass; // Class for LDD singleton attributes (Discipline or Mission)
	
	// values and value meaning
	static TreeMap <String, PermValueDefn> masterValueMeaningMap;

	// Attribute Namespace Resolution Map
	static TreeMap <String, String> attrNamespaceResolutionMap;
	
// 444
	// class assert statements
	static ArrayList <RuleDefn> schematronRuleArr = new ArrayList <RuleDefn> ();
	static TreeMap <String, RuleDefn> schematronRuleMap = new TreeMap <String, RuleDefn> ();
	static TreeMap <String, RuleDefn> schematronRuleIdMap = new TreeMap <String, RuleDefn> (); // to be deprecated
	
	// new rules
	static ArrayList <RuleDefn> schematronRuleNewArr;
	static TreeMap <String, RuleDefn> schematronRuleNewMap;	
	
	// Local Classes	
	ArrayList <PDSObjDefn> objArr;
	HashMap <String, PDSObjDefn> objDict;
	HashMap <String, AttrDefn> attrDict;
	
	ArrayList <String> texSectionFormats;	
                              
/**********************************************************************************************************
		initialize
***********************************************************************************************************/
	public void initInfoModel () {

		// fundamental structures
		fundamentalStructures = new ArrayList <String> ();
		fundamentalStructures.add("Array_Base");
		fundamentalStructures.add("Table_Base");
		fundamentalStructures.add("Unencoded_Stream_Base");
		fundamentalStructures.add("Encoded_Stream_Base");
		
		// set up special rdfIdentifiers
		protegeRootClassRdfId = DMDocument.rdfPrefix + "USER";
		protegeSlotClassRdfId = DMDocument.rdfPrefix + "%3ASYSTEM-CLASS";
		
		
		// masterMetaAttribute list
		masterMetaAttribute = new ArrayList <String> ();
		masterMetaAttribute.add("class_name");
		masterMetaAttribute.add("data_element_concept");
//		masterMetaAttribute.add("identifier");
		masterMetaAttribute.add("enumeration_flag");
//		masterMetaAttribute.add("name_space_id");
		masterMetaAttribute.add("namespace_id");
		masterMetaAttribute.add("registered_by");
		masterMetaAttribute.add("registration_authority_id");
		masterMetaAttribute.add("steward_id");
//		masterMetaAttribute.add("submitter_id");
		masterMetaAttribute.add("submitter_name");
//		masterMetaAttribute.add("title");
//		masterMetaAttribute.add("version_id");
		masterMetaAttribute.add("definition");
		masterMetaAttribute.add("language");
//		masterMetaAttribute.add("comment");
		masterMetaAttribute.add("conceptual_domain");
		masterMetaAttribute.add("data_type");
//		masterMetaAttribute.add("default_unit_id");
		masterMetaAttribute.add("specified_unit_id");
		masterMetaAttribute.add("formation_rule");
		masterMetaAttribute.add("maximum_characters");
		masterMetaAttribute.add("maximum_value");
		masterMetaAttribute.add("minimum_characters");
		masterMetaAttribute.add("minimum_value");
		masterMetaAttribute.add("pattern");
//		masterMetaAttribute.add("unit_of_measure_name");
		masterMetaAttribute.add("unit_of_measure_type");
		masterMetaAttribute.add("value_begin_date");
		masterMetaAttribute.add("value_end_date");
		masterMetaAttribute.add("value");
		masterMetaAttribute.add("value_meaning");
						
		dataTypePDS4ProtegeMap = new TreeMap<String, String> ();
		dataTypePDS4ProtegeMap.put("Bit", "INTEGER");
		dataTypePDS4ProtegeMap.put("ComplexB16", "INTEGER");
		dataTypePDS4ProtegeMap.put("ComplexB8", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedLSB2", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedLSB4", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedLSB8", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedMSB2", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedMSB4", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedMSB8", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedByte", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedLSB2", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedLSB4", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedMSB2", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedMSB4", "INTEGER");
		dataTypePDS4ProtegeMap.put("IEEE754Double", "FLOAT");
		dataTypePDS4ProtegeMap.put("IEEE754Single", "FLOAT");
		dataTypePDS4ProtegeMap.put("ASCII_File_Name", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Boolean", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Boolean_TF", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_YMD", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Integer", "INTEGER");
		dataTypePDS4ProtegeMap.put("ASCII_Real", "FLOAT");
		dataTypePDS4ProtegeMap.put("ASCII_AnyURI", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_DOY", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_DOY", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_UTC", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_YMD", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_LID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_LIDVID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_MD5_Checksum", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Short_String_Collapsed", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Text_Preserved", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Short_String_Preserved", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Time", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_VID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_DOI", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Numeric_Base2", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Numeric_Base16", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_NonNegative_Integer", "INTEGER");
		dataTypePDS4ProtegeMap.put("ASCII_File_Specification_Name", "STRING");
		dataTypePDS4ProtegeMap.put("SignedLSB16", "INTEGER");
		dataTypePDS4ProtegeMap.put("SignedMSB16", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedLSB8", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedLSB16", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedMSB8", "INTEGER");
		dataTypePDS4ProtegeMap.put("UnsignedMSB16", "INTEGER");

//		rawValueTypeMap = new HashMap<String, String> ();
		rawValueTypeMap = new TreeMap<String, String> ();
		rawValueTypeMap.put("string", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("character", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("identifier", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("integer", "ASCII_Integer");
		rawValueTypeMap.put("time", "ASCII_Date_Time");
		rawValueTypeMap.put("float", "ASCII_Real");
		rawValueTypeMap.put("real", "ASCII_Real");
		rawValueTypeMap.put("boolean", "ASCII_Boolean");
		rawValueTypeMap.put("alphanumeric", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("date", "ASCII_Date_YMD");
		rawValueTypeMap.put("context_dependent", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("non_decimal", "ASCII_Integer_Hex");
		rawValueTypeMap.put("symbol", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("data_set", "INVALID_data_set");
		rawValueTypeMap.put("any", "ASCII_Short_String_Collapsed");
		rawValueTypeMap.put("CLASS", "CLASS");		
		
		classConcept = new TreeMap <String, String> ();
		classConcept.put("COUNT", "A numeric value indicating a current total or tally."); 
		classConcept.put("DATE", "A representation of time in which the smallest unit of measure is a day.");
		classConcept.put("DESCRIPTION", "A free-form, unlimited-length character string that provides a description of the item identified.");
		classConcept.put("DIRECTION", "TBD");
		classConcept.put("FLAG", "A boolean condition indicator, limited to two states.");
		classConcept.put("FORMAT", "A specified or predetermined arrangement of data within a file or on a storage medium.");
		classConcept.put("GROUP", "Names a collection or aggregation of elements. Example: ALT FLAG GROUP");
		classConcept.put("GUID", "A globally unique, immutable, and opaque product identifer.");
		classConcept.put("ID", "A shorthand alphanumeric identifier.");
		classConcept.put("IDENTIFIER", "A shorthand alphanumeric identifier.");
		classConcept.put("LOCAL_IDENTIFIER", "An identifier unique within a product label. When appended to the LID, it provides a global identifier for a component of an product.");
		classConcept.put("LOGICAL_IDENTIFIER", "A unique product identifier without the version.  It identifies the set of all versions of a product.");
		classConcept.put("MASK", "An unsigned numeric value representing the bit positions within a value.");
		classConcept.put("NAME", "A literal value representing the common term used to identify an element.");
		classConcept.put("NOTE", "A textual expression of opinion, an observation, or a criticism; a remark.");
		classConcept.put("NUMBER", "A quantity.");
		classConcept.put("QUATERNION", "TBD");
//		classConcept.put("RANGE", "Numeric values which identify the starting and stopping points of an interval.");
		classConcept.put("RATIO", "The relation between two quantities with respect to the number of times the first contains the second.");
		classConcept.put("SEQUENCE", "1) an arrangement of items in accordance with some criterion that defines their spacewise or timewise succession; 2) an orderly progression of items or operations in accordance with some rule, such as alphabetical or numerical order.");
		classConcept.put("SET", "A collection of items having some feature in common or which bear a certain relation to one another, e.g. all even numbers.");
		classConcept.put("SUMMARY", "An abridged description.");
		classConcept.put("TEXT", "A free-form, unlimited length character string.");
		classConcept.put("DATE_TIME", "A value that measures the point of occurrence of an event expressed in date and time in a standard form.");
		classConcept.put("TYPE", "A literal that indicates membership in a predefined class.");
		classConcept.put("UNIT", "A determinate quantity adopted as a standard of measurement.");
		classConcept.put("VALUE", "The default class word for data element names not terminated with a class word.");
		classConcept.put("VECTOR", "A quantity that has both length and direction which are independent of both the units and of the coordinate system in which each are measured. The vector direction is uniquely defined in terms of an ordered set of components with respect to the particular coordinate system for which those components have been defined.");		

		dataConcept = new TreeMap <String, String> ();
		dataConcept.put("INTEGER", "Mathematically, the infinite ring produced from the additive identity (0) and the multiplicative identity (1) by requiring 0 = 1 and Add(x,1) ? y for any y = x . That is: ..., -2, -1, 0, 1, 2, ... (a denumerably infinite list). - ISO/IEC 11404"); 
		dataConcept.put("REAL", "The value space of the mathematical real type comprises all values which are the limits of convergent sequences of rational numbers. The value space of a computational real datatype shall be a subset of the mathematical real type. - ISO/IEC 11404");
		dataConcept.put("CHARACTER", "The value space of a character datatype comprises exactly the members of the character-sets identified by the repertoire-list. - ISO/IEC 11404");
		dataConcept.put("COMPLEX", "The value space of the mathematical complex type is the field which is the solution space of all polynomial equations having real coefficients. The value space of a computational complex datatype shall be a subset of the mathematical complex type, characterized by two parametric values, radix and factor, which, taken together, describe the precision to which values of the datatype are distinguishable.  - ISO/IEC 11404");
		dataConcept.put("BOOLEAN", "The values true and false, such that true is not equal to false. - ISO/IEC 11404");
		dataConcept.put("SCALED", "The value space of a scaled datatype is that set of values of the rational datatype which are expressible as a value of datatype Integer divided by radix raised to the power factor. - ISO/IEC 11404");
		dataConcept.put("OCTET", "Each value of datatype octet is a code, represented by a non-negative integer value in the range [0, 255]. - ISO/IEC 11404");
		dataConcept.put("TIME", "The value-space of a date-and-time datatype is the denumerably infinite set of all possible points in time with the resolution (time-unit, radix, factor). The time-literal denotes the date-and-time value specified by the characterstring as interpreted under ISO 8601. - ISO/IEC 11404");
		dataConcept.put("VOID", "Conceptually, the value space of the void datatype is empty, but a single nominal value is necessary to perform the �presence required� function. - ISO/IEC 11404");
		dataConcept.put("TIME_INTERVAL", "All values which are integral multiples of one radix ^ (-factor) unit of the specified timeunit. - ISO/IEC 11404");
		dataConcept.put("ENUMERATED", "The value space of an enumerated datatype is the set comprising exactly the named values in the enumerated-value-list, each of which is designated by a unique enumerated-literal. The order of these values is given by the sequence of their occurrence in the enumerated-value-list, which shall be referred to as the naming sequence of the enumerated datatype.  - ISO/IEC 11404");
		
		metricConceptMap = new TreeMap <String, Integer> ();
		
		cdID2CDTitleMap = new TreeMap <String, String> ();
		cdTitle2CDIDMap = new TreeMap <String, String> ();

		decID2DECTitleMap = new TreeMap <String, String> ();
		decTitle2DECIDMap = new TreeMap <String, String> ();

		dataTypeToConceptMap = new TreeMap <String, String> ();
		dataTypeToConceptMap.put("ASCII_AnyURI", "ANYURI");
		dataTypeToConceptMap.put("ASCII_Boolean", "BOOLEAN");
		dataTypeToConceptMap.put("ASCII_Boolean_TF", "BOOLEAN");
		dataTypeToConceptMap.put("ASCII_DOI", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_Date", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_DOY", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_Time", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_Time_DOY", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_Time_UTC", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_Time_YMD", "TIME");
		dataTypeToConceptMap.put("ASCII_Date_YMD", "TIME");
		dataTypeToConceptMap.put("ASCII_Directory_Path_Name", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_File_Name", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_File_Specification_Name", "SHORT_STRING");
//		dataTypeToConceptMap.put("ASCII_Identifier", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_Integer", "INTEGER");
		dataTypeToConceptMap.put("ASCII_LID", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_LIDVID", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_MD5_Checksum", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_NonNegative_Integer", "INTEGER");
		dataTypeToConceptMap.put("ASCII_Numeric_Base16", "NUMERIC");
		dataTypeToConceptMap.put("ASCII_Numeric_Base2", "NUMERIC");
		dataTypeToConceptMap.put("ASCII_Numeric_Base8", "NUMERIC");
		dataTypeToConceptMap.put("ASCII_Real", "REAL");
		dataTypeToConceptMap.put("ASCII_Short_String_Collapsed", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_Short_String_Preserved", "SHORT_STRING");
		dataTypeToConceptMap.put("ASCII_Text_Collapsed", "TEXT");
		dataTypeToConceptMap.put("ASCII_Text_Preserved", "TEXT");
		dataTypeToConceptMap.put("ASCII_Time", "TIME");
		dataTypeToConceptMap.put("ASCII_VID", "SHORT_STRING");
		dataTypeToConceptMap.put("Float3Vector", "VECTOR");
		dataTypeToConceptMap.put("UTF8_Short_String_Collapsed", "SHORT_STRING");
		dataTypeToConceptMap.put("UTF8_Short_String_Preserved", "SHORT_STRING");
		dataTypeToConceptMap.put("UTF8_Text_Preserved", "TEXT");
		
		// initialize the Attribute Namespace Resolution Map 
		// attrs (AttrDefn)
		attrNamespaceResolutionMap = new TreeMap <String, String> ();
		attrNamespaceResolutionMap.put("disp.Color_Display_Settings.disp.comment", "pds");
		attrNamespaceResolutionMap.put("disp.Display_Direction.disp.comment", "pds");
		attrNamespaceResolutionMap.put("disp.Movie_Display_Settings.disp.comment", "pds");
		// assocs (AttrDefn)
		attrNamespaceResolutionMap.put("disp.Display_Settings.disp.local_internal_reference", "pds");			
		
// 999		System.out.println("\n>>info    - Static Schematron Rules Setup");
// 999		System.out.println(">>info    - Rule count for Arr: " + InfoModel.schematronRuleArr.size());
// 999		System.out.println(">>info    - Rule count for Map: " + InfoModel.schematronRuleMap.size());
// 999		System.out.println(">>info    - Rule count for Id Map: " + InfoModel.schematronRuleIdMap.size());
// 999		System.out.println(" ");
		
		masterValueMeaningMap = new TreeMap <String, PermValueDefn> ();
 		GetValueMeanings lGetValueMeanings = new GetValueMeanings ();
		lGetValueMeanings.insertValueMeaning();
		lGetValueMeanings.getSearchKey();

		//	set up the Tex markers
		texSectionFormats = new ArrayList <String> ();
		texSectionFormats.add("\\section");
		texSectionFormats.add("\\subsection");
		texSectionFormats.add("\\subsubsection");
	}

/**********************************************************************************************************
		miscellaneous routines
***********************************************************************************************************/
	
	/**
	*  return a fundamental structure
	*/
	public String checkForFundamentalStructure (String lName) {
	/*		if (InfoModel.fundamentalStructures.contains(lName))
		{
			return lName;
		} */
		for (Iterator <String> i = InfoModel.fundamentalStructures.iterator(); i.hasNext();) {
			String fsName = (String) i.next();
			if (lName.compareTo(fsName) == 0) {
				return fsName;
			}
		}
		return null;
	}
	
	/**
	*  return the next uid
	*/
//	public String getNextUId () {
	static public String getNextUId () {
		DMDocument.masterUId++;
		Integer IUId = new Integer (DMDocument.masterUId);
		String SUId = IUId.toString();
		return SUId;
	}

//	return next property class order
	static public String getNextClassOrder () {
		DMDocument.masterClassOrder += 10;
		if (DMDocument.masterClassOrder > 9990) DMDocument.masterClassOrder = 9999;
		Integer masterClassOrderInt = new Integer (DMDocument.masterClassOrder);
		String masterClassOrderString = masterClassOrderInt.toString();
		return masterClassOrderString;
	}	

	//	return next group (Choice) Number
	static public String getNextGroupNum () {
		DMDocument.masterGroupNum += 1;
		if (DMDocument.masterGroupNum > 99) DMDocument.masterGroupNum = 99;
		Integer masterGroupNumInt = new Integer (DMDocument.masterGroupNum);
		String masterGroupNumString = masterGroupNumInt.toString();
		return masterGroupNumString;
	}	
	
	
//	reset class order
	static public void resetClassOrder () {
		// increment class order
		DMDocument.masterClassOrder = 1000;
		return;
	}
		
	/**
	*  return a class's identifier
	*/
	static public String getClassIdentifier (String lClassNameSpaceIdNC, String lClassTitle) {
		String lIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClassNameSpaceIdNC + "." + lClassTitle;
		return lIdentifier;
	}	
		
	/**
	*  return an attribute's identifier
	*/
	static public String getAttrIdentifier (String lClassNameSpaceIdNC, String lClassTitle, String lAttrNameSpaceIdNC, String lAttrTitle) {
		String lIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClassNameSpaceIdNC + "." + lClassTitle + "." + lAttrNameSpaceIdNC + "." + lAttrTitle;
		return lIdentifier;
	}	
	
	/**
	*  return an attribute's nsTitle
	*/
	static public String getAttrNSTitle (String lAttrNameSpaceIdNC, String lAttrTitle) {
		String lNSTitle = DMDocument.registrationAuthorityIdentifierValue + "." + lAttrNameSpaceIdNC + "." + lAttrTitle;
		return lNSTitle;
	}	
	
	/**
	*  return rules's rdfIdentifier
	*/
	static public String getRuleRDFIdentifier (String lClassNameSpaceIdNC, String lClassTitle, String lAttrNameSpaceIdNC, String lAttrTitle) {
		String lIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClassNameSpaceIdNC + "." + lClassTitle + "." + lAttrNameSpaceIdNC + "." + lAttrTitle;
		return lIdentifier;
	}	
		
	/**
	*  return property map's rdfIdentifier
	*/
	static public String getPropMapRDFIdentifier (String lInputIdentifier) {
//		String lIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lInputIdentifier;
//		String lIdentifier = DMDocument.rdfPrefix + "." + lInputIdentifier + "." + getNextUId();
		String lIdentifier = lInputIdentifier + "." + getNextUId();
		return lIdentifier;
	}	
	
	/**
	*  wrap a text string
	*/
	static public String wrapText (String lString, int beginOffset, int endOffset) {
//		System.out.println("\ndebug wrapText lString:" + lString);
//		System.out.println("debug wrapText -next chunk- beginOffset:" + beginOffset);			
//		System.out.println("debug wrapText -next chunk- endOffset:" + endOffset);	
		
		// return empty array for null text
		if (lString == null) return "";
		
		// return text if length is zero or less
		int wrapWidth = (endOffset - beginOffset) + 1;
		if (wrapWidth <= 0) return "";		

		// return text if less than wrap width
		int lStringLength = lString.length();
		if (lStringLength <= wrapWidth) return lString;
		
		// set buffer offsets
		int currOffset = beginOffset;
		int lInputBuffOffset1 = 0, lInputBuffOffset2 = 0, lInputBuffOffsetPrevBlank = 0;
		int lOutputBuffOffset = 0;
		
//		System.out.println("debug wrapText lStringLength:" + lStringLength);

		// set up string buffer
		StringBuffer lInputStringBuff = new StringBuffer(lString);
		StringBuffer lOutputStringBuff = new StringBuffer();
		
		// setup major loop for transfering the string in the input buffer to a wrapped string in the output buffer 
		int indent = 0;
		char lfcr = '\n';
		boolean isFirst = true;
		while (lInputBuffOffset1 < lStringLength) {
			// insert linefeed (except first time)
			if (isFirst) isFirst = false;
			else lOutputStringBuff.insert(lOutputBuffOffset++, lfcr);
			
//			System.out.println("\ndebug wrapText -insert blanks- lInputBuffOffset1:" + lInputBuffOffset1);
			
			// find next non-blank character in input buffer
			while (lInputBuffOffset1 < lStringLength) {
				if (lInputStringBuff.charAt(lInputBuffOffset1) != ' ') break;
				lInputBuffOffset1++;
			}
			
			// insert prefix blanks in output buffer
			for (int i = 0; i < beginOffset + indent; i++) lOutputStringBuff.insert(lOutputBuffOffset++, ' ');

//			System.out.println("debug wrapText -non blank character- lInputBuffOffset1:" + lInputBuffOffset1);			
			
			// find the next chunk of text by finding the next break before the end of the wrapwidth (delimiter = ' ')
			currOffset = beginOffset + indent; // counter for wrapped string size
			indent = 2;
			lInputBuffOffset2 = lInputBuffOffset1; // ending offset in input buffer; init at start offset
			int lIBO1 = lInputBuffOffset1; // offset in input buffer; init at start offset
			lInputBuffOffsetPrevBlank = -1; // location to backtrack to if last word goes over wrap boundary
			
//			System.out.println("debug wrapText -next chunk- lIBO1:" + lIBO1);			
//			System.out.println("debug wrapText -next chunk- lStringLength:" + lStringLength);			
//			System.out.println("debug wrapText -next chunk- currOffset:" + currOffset);			
//			System.out.println("debug wrapText -next chunk- endOffset:" + endOffset);			
//			System.out.println("debug wrapText -next chunk- lInputBuffOffset1:" + lInputBuffOffset1);			
//			System.out.println("debug wrapText -next chunk1- lInputBuffOffset2:" + lInputBuffOffset2);			
			
			while (lIBO1 < lStringLength && currOffset <= endOffset) {
				if (lInputStringBuff.charAt(lIBO1) == ' ') lInputBuffOffsetPrevBlank = lIBO1;
				lIBO1++; currOffset++;
				lInputBuffOffset2++;
			}
//			System.out.println("debug wrapText -next chunk2- lInputBuffOffset2:" + lInputBuffOffset2);
			
			if (lIBO1 < lStringLength) { // check to see of input buffer is exhausted
				if (lInputBuffOffsetPrevBlank > -1) { // was a blank found
					lInputBuffOffset2 = lInputBuffOffsetPrevBlank;
				}
			}
//			System.out.println("debug wrapText -next chunk2- lInputBuffOffset2:" + lInputBuffOffset2);						
//			System.out.println("debug wrapText -next break- lInputBuffOffset1:" + lInputBuffOffset1);			
//			System.out.println("debug wrapText -next break- lInputBuffOffset2:" + lInputBuffOffset2);			

			// copy in the next chunk of text
			while (lInputBuffOffset1 < lInputBuffOffset2) {
				lOutputStringBuff.insert(lOutputBuffOffset, lInputStringBuff.charAt(lInputBuffOffset1));
				lInputBuffOffset1++;
				lOutputBuffOffset++;
			}

//			System.out.println("debug wrapText -next next chunk- lInputBuffOffset1:" + lInputBuffOffset1);						
		}
		return lOutputStringBuff.toString();
	}
	
//	=======================  Utilities ================================================================
	
	/**
	* escape certain characters - LaTEX
	*/

	static String escapeLaTEXChar (String aString) {
		String lString = aString;
		lString = DMDocument.replaceString (lString, "\\", "\\\\");
		return lString;
	}	
	
//	Test for Protege Escape characters - 4/10/15 - only backslash and quote seem affected
//	Space- -%20, LeftSquareBracket-[-%5B, RightSquareBracket-]-%5D, LeftParen-(-%28, RightParen-)-%29,
//	ForwardSlash-/-%2F, Plus-+-%2B, VerticalBar-|-%7C, LeftCurlyBracket-{-%7B, RightCurlyBracket-}-%7D,
//	Apostrophe-'-%47, BackSlash-\-%5C, Quote-"-%22

//	Space- -%20, LeftSquareBracket-[-%5B, RightSquareBracket-]-%5D, LeftParen-(-%28, RightParen-)-%29,
//	ForwardSlash-/-%2F, Plus-+-%2B, VerticalBar-|-%7C, LeftCurlyBracket-{-%7B, RightCurlyBracket-}-%7D,
//	Apostrophe-'-%47, BackSlash-\\-%5C, Quote-\"-%22"
	
	/**
	* escape certain characters for protege files
	*/
	static String escapeProtegeString (String s1) {
		String ls1 = s1;
		ls1 = replaceString (ls1, "[", "%5B");
		ls1 = replaceString (ls1, "]", "%5D");
		ls1 = replaceString (ls1, "(", "%28");
		ls1 = replaceString (ls1, ")", "%29");
		ls1 = replaceString (ls1, "/", "%2F");
		ls1 = replaceString (ls1, "+", "%2B");		
		ls1 = replaceString (ls1, "|", "%7C");		
		ls1 = replaceString (ls1, "{", "%7B");		
		ls1 = replaceString (ls1, "}", "%7D");		
		ls1 = replaceString (ls1, "\"", "%42");		
		ls1 = replaceString (ls1, "'", "%47");		
		ls1 = replaceString (ls1, "\\", "%5C");		
		return ls1;
	}
	
	/**
	* unescape pattern characters from protege files
	*/
	static String unEscapeProtegeString (String s1) {
		String ls1 = s1;
// 444

		// Protege saves uppermodel.pins with certain characters escaped (e.g. http%3A%2F%2F) even if 
		// if the pins file was written with only quotes escaped (e.g. name=\"lid_num_colons)
		// therefore both sets of escaped characters are unescaped. / jsh 151229
		
		ls1 = replaceString (ls1, "\\\\", "\\"); // order is important
		ls1 = replaceString (ls1, "\\\"", "\"");		

		ls1 = replaceString (ls1, "%5B", "[");
		ls1 = replaceString (ls1, "%5D", "]");
		ls1 = replaceString (ls1, "%28", "(");
		ls1 = replaceString (ls1, "%29", ")");
		ls1 = replaceString (ls1, "%2F", "/");
		ls1 = replaceString (ls1, "%2B", "+");		
		ls1 = replaceString (ls1, "%7C", "|");		
		ls1 = replaceString (ls1, "%7B", "{");		
		ls1 = replaceString (ls1, "%7D", "}");
		ls1 = replaceString (ls1, "%42", "\"");		
		ls1 = replaceString (ls1, "%47", "'");		
		ls1 = replaceString (ls1, "%5C", "\\");	
		
//		ls1 = replaceString (ls1, "%BC", "�");	
		return ls1;
	}
	
	/**
	* escape *** PATTERN only *** characters for protege files 
	*/
	static String escapeProtegePatterns (String s1) {
		String ls1 = s1;
		ls1 = replaceString (ls1, " ", "%20");
		ls1 = replaceString (ls1, "[", "%5B");
		ls1 = replaceString (ls1, "]", "%5D");
		ls1 = replaceString (ls1, "(", "%28");
		ls1 = replaceString (ls1, ")", "%29");
		ls1 = replaceString (ls1, "/", "%2F");
		ls1 = replaceString (ls1, "+", "%2B");		
		ls1 = replaceString (ls1, "|", "%7C");		
		ls1 = replaceString (ls1, "{", "%7B");		
		ls1 = replaceString (ls1, "}", "%7D");		
		ls1 = replaceString (ls1, "'", "%47");		
		ls1 = replaceString (ls1, "\\", "%5C");		
		
		ls1 = replaceString (ls1, "\"", "%22");		
		ls1 = replaceString (ls1, "\r", "%0D");		
		ls1 = replaceString (ls1, "\n", "%0A");	
		
//		ls1 = replaceString (ls1, "�", "%BC");			
		return ls1;
	}
	
	/**
	* unescape *** PATTERN ONLY *** characters from protege files
	*/
	static String unEscapeProtegePatterns (String s1) {
		String ls1 = s1;
		ls1 = replaceString (ls1, "%20", " ");
		ls1 = replaceString (ls1, "%5B", "[");
		ls1 = replaceString (ls1, "%5D", "]");
		ls1 = replaceString (ls1, "%28", "(");
		ls1 = replaceString (ls1, "%29", ")");
		ls1 = replaceString (ls1, "%2F", "/");
		ls1 = replaceString (ls1, "%2B", "+");		
		ls1 = replaceString (ls1, "%7C", "|");		
		ls1 = replaceString (ls1, "%7B", "{");		
		ls1 = replaceString (ls1, "%7D", "}");
		ls1 = replaceString (ls1, "%47", "'");		
		ls1 = replaceString (ls1, "%5C", "\\");	

		ls1 = replaceString (ls1, "%22", "\"");		
		ls1 = replaceString (ls1, "%0D", "\r");		
		ls1 = replaceString (ls1, "%0A", "\n");
		return ls1;
	}
	
	/**
	* escape pattern in local DDs for protege files 
	*/
	static String escapeProtegeLocalDD (String s1) {
		String ls1 = s1;
		ls1 = replaceString (ls1, "[", "%5B");
		ls1 = replaceString (ls1, "]", "%5D");
		ls1 = replaceString (ls1, "(", "%28");
		ls1 = replaceString (ls1, ")", "%29");
		ls1 = replaceString (ls1, "/", "%2F");
		ls1 = replaceString (ls1, "+", "%2B");		
		ls1 = replaceString (ls1, "|", "%7C");		
		ls1 = replaceString (ls1, "{", "%7B");		
		ls1 = replaceString (ls1, "}", "%7D");		
		ls1 = replaceString (ls1, "'", "%47");		
		ls1 = replaceString (ls1, "\\", "%5C");		
		
		ls1 = replaceString (ls1, "\"", "%22");		
		ls1 = replaceString (ls1, "\r", "%0D");		
		ls1 = replaceString (ls1, "\n", "%0A");			
		return ls1;
	}
		
	/**
	* escape certain characters - XML
	*/
	static String escapeXMLChar (String aString) {
		String lString = aString;
// Correct		
// 		True mu:μ GREEK SMALL LETTER MU (U+03BC)
//		Hex:cebc	U+03BC 	\u03BC 	&#956; 	&mu; 	small mu
//		Eclipse - DMDocument- Properties - Set to UTF-8		
// 
// Incorrect		
// 		U+03BC 	\u03BC 	&#956; 	&mu; 	small mu		
//		String lMu = "\u03BC";
//      Windows-1252 or CP-1252 is a character encoding of the Latin alphabet, 
//		used by default in the legacy components of Microsoft Windows in English
//		and some other Western languages		
		
// 777		
//		char lCharMu = '\u03BC';
//		String lStringMu = "\u03BC";
		
//		lString = replaceString (lString, "μ", "&mu;");
		lString = replaceString (lString, "&", "&amp;");
		lString = replaceString (lString, "<", "&lt;");
		lString = replaceString (lString, ">", "&gt;");
		lString = replaceString (lString, "\"", "&quot;");
		lString = replaceString (lString, "'", "&apos;");
		return lString;
	}	
	
	/**
	* escape certain characters - JSON
	*/
	static String escapeJSONChar (String aString) {
		if (aString == null) return "TBD_string";
		String lString = aString;
		lString = replaceString (lString, "\\", "\\\\");  // escape of backslash must be first
		lString = replaceString (lString, "\"", "\\\"");
		lString = replaceString (lString, "/", "\\/");
//		lString = replaceString (lString, "\b", "\\b");
//		lString = replaceString (lString, "\f", "\\f");
//		lString = replaceString (lString, "\n", "\\n");
//		lString = replaceString (lString, "\r", "\\r");
//		lString = replaceString (lString, "\t", "\\t");
		return lString;
	}	
	
	/**
	* escape certain characters for the WIKI
	*/
	static String escapeWiki (String aString) {
		String lString = aString;
//		lString = replaceString (lString, "\\", "\\\\");
//		lString = replaceString (lString, "-", "\\-");
		lString = replaceString (lString, "[", "\\[");
		lString = replaceString (lString, "]", "\\]");
		lString = replaceString (lString, "{", "\\{");
		lString = replaceString (lString, "}", "\\}");
		lString = replaceString (lString, "*", "\\*");
		return lString;
	}
	
	/**
	* remove certain characters from input strings (model - e.g. value meanings should not have <cr><lf>
	*/
	static String cleanCharString (String aString) {
		if (aString == null) return "TBD_string";
		String lString = aString;
		lString = lString.replaceAll("\\s+"," ");
		return lString;
	}	
	
	/**
	* Replace string with string (gleaned from internet)
	*/

	static String replaceString (String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e+pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}		
	
	
	/**
		* Convert String to int
		*/
	public int String2Int (String s1) { 	
		if (s1 == null) {
			return -99999;
		}
		StringBuffer sb1 = new StringBuffer(s1);
		int sb1len = sb1.length();
		for (int p1 = 0; p1 < sb1len; p1++) {
			if (! Character.isDigit(sb1.charAt(p1)))
			{
				return -99999;
			}
		}
		Integer ii = new Integer (s1);
		return ii.intValue();
	}   
	
	/**
	* get a singleton value from a value array, single if multiple value
	*/
	static public String getSingletonAttrValue (ArrayList <String> valarr) {
		if (! (valarr == null || valarr.isEmpty())) {
			int nVal = valarr.size();
			if (nVal == 1) {
				String lVal = (String) valarr.get(0);
				if (lVal.compareTo("") != 0) {
					return lVal;
				}
			} else if (nVal > 1) {
				return "TBD_Multi";
			}
		}
		return null;
	}	
	
	/**
	* get a singleton value from a value array, update only if oVal is TBD
	*/
	static public String getSingletonValueUpdate (ArrayList <String> valarr, String oVal) {
		if (! (valarr == null || valarr.isEmpty())) {
			int nVal = valarr.size();
			if (nVal == 1) {
				String lVal = (String) valarr.get(0);
				if (! ((lVal == null) || ((lVal.compareTo("")) == 0) || ((lVal.indexOf("TBD")) == 0))) {
					if (((oVal == null) || ((oVal.compareTo("")) == 0) || ((oVal.indexOf("TBD")) == 0))) {
						return lVal;
					}
				}
			}
		}
		return null;
	}
	
	/**
	* get multiple values from a value array;
	*/
	static public  ArrayList <String> getMultipleValue (ArrayList <String> valArr) {
		ArrayList <String> lValArr = new ArrayList <String> ();
		if (! (valArr == null || valArr.isEmpty())) {
			int nVal = valArr.size();
			if (nVal > 0) {
				for (Iterator<String> i = valArr.iterator(); i.hasNext();) {
					String lVal = (String) i.next();
					if (! ((lVal == null) || ((lVal.compareTo("")) == 0) || ((lVal.indexOf("TBD")) == 0))) {
						lValArr.add(lVal);
					}
				}
				if (lValArr.size() > 0) {
					return lValArr;
				}
			}
		}
		return null;
	}
	
	/**
	*  Return all attributes in a class - recurse down through all associations.
	*/
	public static ArrayList <AttrDefn> getAllAttrRecurse (ArrayList <AttrDefn> lAttrArr, ArrayList <PDSObjDefn> visitedClass, PDSObjDefn lClass) {
		//	get all local attributes
		for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttrArr.contains(lAttr)) {
				lAttrArr.add(lAttr);
			}
		} 	
		
		//	get all inherited attributes
		for (Iterator<AttrDefn> i = lClass.inheritedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttrArr.contains(lAttr)) {
				lAttrArr.add(lAttr);
			}
		} 	
		
		//get all local associations
		for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) i.next();
			for (Iterator<PDSObjDefn> j = lAssoc.valClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lCompClass = j.next();
				if (! visitedClass.contains(lCompClass)) {
					visitedClass.add(lCompClass);
					getAllAttrRecurse (lAttrArr, visitedClass, lCompClass);
				}
			}
		}
		
		//get all inherited associations
		for (Iterator<AttrDefn> i = lClass.inheritedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) i.next();
			for (Iterator<PDSObjDefn> j = lAssoc.valClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lCompClass = j.next();
				if (! visitedClass.contains(lCompClass)) {
					visitedClass.add(lCompClass);
					getAllAttrRecurse (lAttrArr, visitedClass, lCompClass);
				}
			}
		}
		return lAttrArr;
	}
		
	public static ArrayList <String> getAllRefAssocType (ArrayList <AttrDefn> lAttrArr) {		
		ArrayList <String> lRefTypeArr = new ArrayList <String> ();
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = i.next();
			if (lAttr.isAttribute) {
				if (lAttr.title.indexOf("reference_association_type") == 0) {
					if (! lAttr.valArr.isEmpty()) {
//						System.out.println("debug4 getAllRefAssocType Associations lAttr.title:" + lAttr.title);
						for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
							String lVal = (String) j.next();
							if (lVal.compareTo("") != 0) {
								if (lRefTypeArr.contains(lVal)) {
									System.out.println("\n***Warning***  getAllRefAssocType - found duplicate association -  Association:" + lVal);
//									lVal = lVal + ".DUP";
								} else {
									lRefTypeArr.add(lVal);
								}
							}
						}
					}
				}
			}
		}
		if (lRefTypeArr.isEmpty()) {
			return null;
		}
		return lRefTypeArr;
	}
	
	// get all CD and DEC values for each attribute; used to print a CD or DEC 11179 definition. i.e. all DECS for a CD.
	public void getCDDECIndexes () {
		
		// get Attribute for the VD info
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				
				// update the CD (dataConcept) Index
				String sortKey = lAttr.dataConcept;
				IndexDefn lIndex = cdAttrMap.get(sortKey);
				if (lIndex == null) {
					lIndex = new IndexDefn(sortKey);
					cdAttrMap.put(sortKey, lIndex);
				}
				lIndex.identifier1Map.put(lAttr.identifier, lAttr);
				if (! lIndex.identifier2Arr.contains(lAttr.classConcept)) {
					lIndex.identifier2Arr.add(lAttr.classConcept);
				}

				// update the DEC (classConcept) Index
				sortKey = lAttr.classConcept;
				lIndex = decAttrMap.get(sortKey);
				if (lIndex == null) {
					lIndex = new IndexDefn(sortKey);
					decAttrMap.put(sortKey, lIndex);
				}
				lIndex.identifier1Map.put(lAttr.identifier, lAttr);
				if (! lIndex.identifier2Arr.contains(lAttr.dataConcept)) {
					lIndex.identifier2Arr.add(lAttr.dataConcept);
				}
			}
		}
		return;
	}	
	
	// clone an attribute 
	static public AttrDefn cloneAttr (String lRDFIdentifier, AttrDefn lOrgAttr) {
		AttrDefn lNewAttr = new AttrDefn (lRDFIdentifier);				              					              
		lNewAttr.uid = lOrgAttr.uid;										              
		lNewAttr.identifier = lOrgAttr.identifier; 						              
		lNewAttr.sort_identifier = lOrgAttr.sort_identifier;				              
		lNewAttr.attrAnchorString = lOrgAttr.attrAnchorString;			              
		lNewAttr.title = lOrgAttr.title;  								              
		lNewAttr.versionId = lOrgAttr.versionId;							              
		lNewAttr.registrationStatus = lOrgAttr.registrationStatus;		              
		lNewAttr.XMLSchemaName = lOrgAttr.XMLSchemaName;					              
		lNewAttr.regAuthId = lOrgAttr.regAuthId;							              
		lNewAttr.steward = lOrgAttr.steward;								              
		lNewAttr.classSteward = lOrgAttr.classSteward;					              
		lNewAttr.attrNameSpaceId = lOrgAttr.attrNameSpaceId;                     
		lNewAttr.attrNameSpaceIdNC = lOrgAttr.attrNameSpaceIdNC;                   
		lNewAttr.classNameSpaceIdNC = lOrgAttr.classNameSpaceIdNC;                  
		lNewAttr.submitter = lOrgAttr.submitter;							              
		lNewAttr.subModelId = lOrgAttr.subModelId;						              
		lNewAttr.parentClassTitle = lOrgAttr.parentClassTitle;							              
		lNewAttr.attrParentClass = lOrgAttr.attrParentClass;							              
		lNewAttr.classConcept = lOrgAttr.classConcept;					              
		lNewAttr.dataConcept = lOrgAttr.dataConcept;						              
		lNewAttr.classWord = lOrgAttr.classWord;							              
		lNewAttr.description = lOrgAttr.description;                         
		lNewAttr.lddLocalIdentifier = lOrgAttr.lddLocalIdentifier;		              

		lNewAttr.xmlBaseDataType = lOrgAttr.xmlBaseDataType;				              
		lNewAttr.protValType = lOrgAttr.protValType;						              
		lNewAttr.propType = lOrgAttr.propType;							              
		lNewAttr.valueType = lOrgAttr.valueType;
		lNewAttr.groupName = lOrgAttr.groupName;
		lNewAttr.cardMin = lOrgAttr.cardMin;                             
		lNewAttr.cardMax = lOrgAttr.cardMax;                             
		lNewAttr.cardMinI = lOrgAttr.cardMinI;                            
		lNewAttr.cardMaxI = lOrgAttr.cardMaxI;                            

		lNewAttr.minimum_characters = lOrgAttr.minimum_characters;		              
		lNewAttr.maximum_characters = lOrgAttr.maximum_characters;		              
		lNewAttr.minimum_value = lOrgAttr.minimum_value;			                  
		lNewAttr.maximum_value = lOrgAttr.maximum_value;			                  
		lNewAttr.format = lOrgAttr.format;					                    
		lNewAttr.pattern = lOrgAttr.pattern;					                    
		lNewAttr.unit_of_measure_type = lOrgAttr.unit_of_measure_type;	              
		lNewAttr.default_unit_id = lOrgAttr.default_unit_id;			                
		lNewAttr.unit_of_measure_precision = lOrgAttr.unit_of_measure_precision;	          

//		lNewAttr.type = lOrgAttr.type;                                
		lNewAttr.isAttribute = lOrgAttr.isAttribute;			                    
		lNewAttr.isOwnedAttribute = lOrgAttr.isOwnedAttribute;		                
		lNewAttr.isPDS4 = lOrgAttr.isPDS4;					                    
// 445		lNewAttr.isUnitOfMeasure = lOrgAttr.isUnitOfMeasure;                     
// 445		lNewAttr.isDataType = lOrgAttr.isDataType;                          
		lNewAttr.isEnumerated = lOrgAttr.isEnumerated;                        
		lNewAttr.isUsedInClass = lOrgAttr.isUsedInClass;			                  
		lNewAttr.isRestrictedInSubclass = lOrgAttr.isRestrictedInSubclass;              
		lNewAttr.isMeta = lOrgAttr.isMeta;                              
		lNewAttr.hasAttributeOverride = lOrgAttr.hasAttributeOverride;                
		lNewAttr.isNilable = lOrgAttr.isNilable;                           
		lNewAttr.isChoice = lOrgAttr.isChoice;				                    
		lNewAttr.isAny = lOrgAttr.isAny;				                    
		lNewAttr.isFromLDD = lOrgAttr.isFromLDD;			                      
		lNewAttr.hasRetiredValue = lOrgAttr.hasRetiredValue;                     

		lNewAttr.valArr = lOrgAttr.valArr;                              
		lNewAttr.allowedUnitId = lOrgAttr.allowedUnitId;	                      
		lNewAttr.genAttrMap = lOrgAttr.genAttrMap;                          
		lNewAttr.permValueArr = lOrgAttr.permValueArr;                        
		lNewAttr.permValueExtArr = lOrgAttr.permValueExtArr;                     
		lNewAttr.termEntryMap = lOrgAttr.termEntryMap;                        
		lNewAttr.valueDependencyMap = lOrgAttr.valueDependencyMap;                  
		 	                                            
		lNewAttr.dataIdentifier = lOrgAttr.dataIdentifier; 						          
		lNewAttr.deDataIdentifier = lOrgAttr.deDataIdentifier;					          
		lNewAttr.decDataIdentifier = lOrgAttr.decDataIdentifier;					          
		lNewAttr.ecdDataIdentifier = lOrgAttr.ecdDataIdentifier;					          
		lNewAttr.evdDataIdentifier = lOrgAttr.evdDataIdentifier;					          
		lNewAttr.necdDataIdentifier = lOrgAttr.necdDataIdentifier;				          
		lNewAttr.nevdDataIdentifier = lOrgAttr.nevdDataIdentifier;				          
		lNewAttr.pvDataIdentifier = lOrgAttr.pvDataIdentifier;					          
		lNewAttr.vmDataIdentifier = lOrgAttr.vmDataIdentifier;					          
		 	                                            
		lNewAttr.desDataIdentifier = lOrgAttr.desDataIdentifier;					          
		lNewAttr.defDataIdentifier = lOrgAttr.defDataIdentifier;					          
		lNewAttr.lsDataIdentifier = lOrgAttr.lsDataIdentifier;					          
		lNewAttr.teDataIdentifier = lOrgAttr.teDataIdentifier;					          
		lNewAttr.prDataIdentifier = lOrgAttr.prDataIdentifier;					          
		 	                                            
		lNewAttr.administrationRecordValue = lOrgAttr.administrationRecordValue;           
		lNewAttr.versionIdentifierValue = lOrgAttr.versionIdentifierValue;              
		lNewAttr.registeredByValue = lOrgAttr.registeredByValue;                   
		lNewAttr.registrationAuthorityIdentifierValue = lOrgAttr.registrationAuthorityIdentifierValue;
		 	                                            
		lNewAttr.expressedByArr = lOrgAttr.expressedByArr;                      
		lNewAttr.representing1Arr = lOrgAttr.representing1Arr;                    
		lNewAttr.representedBy1Arr = lOrgAttr.representedBy1Arr;                   
		lNewAttr.representedBy2Arr = lOrgAttr.representedBy2Arr;                   
		lNewAttr.containedIn1Arr = lOrgAttr.containedIn1Arr;                     
		 	                                            
		lNewAttr.genClassArr = lOrgAttr.genClassArr;                         
		lNewAttr.sysClassArr = lOrgAttr.sysClassArr;	                       
		return lNewAttr;
	}
	
	// finish the clone of an attribute 
	static public void finishCloneAttr (AttrDefn lOrgAttr, AttrDefn lNewAttr) {				              					              
//		lNewAttr.uid = lOrgAttr.uid;										              
//		lNewAttr.identifier = lOrgAttr.identifier; 						              
//		lNewAttr.sort_identifier = lOrgAttr.sort_identifier;				              
//		lNewAttr.attrAnchorString = lOrgAttr.attrAnchorString;			              
//		lNewAttr.title = lOrgAttr.title;  								              
//		lNewAttr.versionId = lOrgAttr.versionId;							              
//		lNewAttr.registrationStatus = lOrgAttr.registrationStatus;		              
		lNewAttr.XMLSchemaName = lOrgAttr.XMLSchemaName;					              
//		lNewAttr.regAuthId = lOrgAttr.regAuthId;							              
		lNewAttr.steward = lOrgAttr.steward;								              
//		lNewAttr.classSteward = lOrgAttr.classSteward;					              
//		lNewAttr.attrNameSpaceId = lOrgAttr.attrNameSpaceId;                     
//		lNewAttr.attrNameSpaceIdNC = lOrgAttr.attrNameSpaceIdNC;                   
//		lNewAttr.classNameSpaceIdNC = lOrgAttr.classNameSpaceIdNC;                  
//		lNewAttr.submitter = lOrgAttr.submitter;							              
//		lNewAttr.subModelId = lOrgAttr.subModelId;						              
//		lNewAttr.className = lOrgAttr.className;							              
//		lNewAttr.classConcept = lOrgAttr.classConcept;					              
		lNewAttr.dataConcept = lOrgAttr.dataConcept;						              
//		lNewAttr.classWord = lOrgAttr.classWord;							              
		lNewAttr.description = lOrgAttr.description;                         
		lNewAttr.lddLocalIdentifier = lOrgAttr.lddLocalIdentifier;		              

		lNewAttr.xmlBaseDataType = lOrgAttr.xmlBaseDataType;				              
		lNewAttr.protValType = lOrgAttr.protValType;						              
		lNewAttr.propType = lOrgAttr.propType;							              
		lNewAttr.valueType = lOrgAttr.valueType;	
		lNewAttr.groupName = lOrgAttr.groupName;

//		lNewAttr.cardMin = lOrgAttr.cardMin;                             
//		lNewAttr.cardMax = lOrgAttr.cardMax;                             
//		lNewAttr.cardMinI = lOrgAttr.cardMinI;                            
//		lNewAttr.cardMaxI = lOrgAttr.cardMaxI;                            

		lNewAttr.minimum_characters = lOrgAttr.minimum_characters;		              
		lNewAttr.maximum_characters = lOrgAttr.maximum_characters;		              
		lNewAttr.minimum_value = lOrgAttr.minimum_value;			                  
		lNewAttr.maximum_value = lOrgAttr.maximum_value;			                  
		lNewAttr.format = lOrgAttr.format;					                    
		lNewAttr.pattern = lOrgAttr.pattern;					                    
		lNewAttr.unit_of_measure_type = lOrgAttr.unit_of_measure_type;	              
		lNewAttr.default_unit_id = lOrgAttr.default_unit_id;			                
		lNewAttr.unit_of_measure_precision = lOrgAttr.unit_of_measure_precision;	          

//		lNewAttr.type = lOrgAttr.type;                                
		lNewAttr.isAttribute = lOrgAttr.isAttribute;			                    
		lNewAttr.isOwnedAttribute = lOrgAttr.isOwnedAttribute;		                
		lNewAttr.isPDS4 = lOrgAttr.isPDS4;					                    
//		lNewAttr.isUnitOfMeasure = lOrgAttr.isUnitOfMeasure;                     
//		lNewAttr.isDataType = lOrgAttr.isDataType;                          
		lNewAttr.isEnumerated = lOrgAttr.isEnumerated;                        
		lNewAttr.isUsedInClass = lOrgAttr.isUsedInClass;			                  
		lNewAttr.isRestrictedInSubclass = lOrgAttr.isRestrictedInSubclass;              
		lNewAttr.isMeta = lOrgAttr.isMeta;                              
		lNewAttr.hasAttributeOverride = lOrgAttr.hasAttributeOverride;                
		lNewAttr.isNilable = lOrgAttr.isNilable;                           
		lNewAttr.isChoice = lOrgAttr.isChoice;				                    
		lNewAttr.isAny = lOrgAttr.isAny;				                    
//		lNewAttr.isFromLDD = lOrgAttr.isFromLDD;			                      
		lNewAttr.hasRetiredValue = lOrgAttr.hasRetiredValue;                     

		lNewAttr.valArr = lOrgAttr.valArr;                              
		lNewAttr.allowedUnitId = lOrgAttr.allowedUnitId;	                      
		lNewAttr.genAttrMap = lOrgAttr.genAttrMap;                          
		lNewAttr.permValueArr = lOrgAttr.permValueArr;                        
		lNewAttr.permValueExtArr = lOrgAttr.permValueExtArr;                     
		lNewAttr.termEntryMap = lOrgAttr.termEntryMap;                        
		lNewAttr.valueDependencyMap = lOrgAttr.valueDependencyMap;                  
		 	                                            
//		lNewAttr.dataIdentifier = lOrgAttr.dataIdentifier; 						          
//		lNewAttr.deDataIdentifier = lOrgAttr.deDataIdentifier;					          
//		lNewAttr.decDataIdentifier = lOrgAttr.decDataIdentifier;					          
//		lNewAttr.ecdDataIdentifier = lOrgAttr.ecdDataIdentifier;					          
//		lNewAttr.evdDataIdentifier = lOrgAttr.evdDataIdentifier;					          
//		lNewAttr.necdDataIdentifier = lOrgAttr.necdDataIdentifier;				          
//		lNewAttr.nevdDataIdentifier = lOrgAttr.nevdDataIdentifier;				          
//		lNewAttr.pvDataIdentifier = lOrgAttr.pvDataIdentifier;					          
//		lNewAttr.vmDataIdentifier = lOrgAttr.vmDataIdentifier;					          
		 	                                            
//		lNewAttr.desDataIdentifier = lOrgAttr.desDataIdentifier;					          
//		lNewAttr.defDataIdentifier = lOrgAttr.defDataIdentifier;					          
//		lNewAttr.lsDataIdentifier = lOrgAttr.lsDataIdentifier;					          
//		lNewAttr.teDataIdentifier = lOrgAttr.teDataIdentifier;					          
//		lNewAttr.prDataIdentifier = lOrgAttr.prDataIdentifier;					          
		 	                                            
		lNewAttr.administrationRecordValue = lOrgAttr.administrationRecordValue;           
		lNewAttr.versionIdentifierValue = lOrgAttr.versionIdentifierValue;              
		lNewAttr.registeredByValue = lOrgAttr.registeredByValue;                   
		lNewAttr.registrationAuthorityIdentifierValue = lOrgAttr.registrationAuthorityIdentifierValue;
		 	                                            
		lNewAttr.expressedByArr = lOrgAttr.expressedByArr;                      
		lNewAttr.representing1Arr = lOrgAttr.representing1Arr;                    
		lNewAttr.representedBy1Arr = lOrgAttr.representedBy1Arr;                   
		lNewAttr.representedBy2Arr = lOrgAttr.representedBy2Arr;                   
		lNewAttr.containedIn1Arr = lOrgAttr.containedIn1Arr;                     
		 	                                            
		lNewAttr.genClassArr = lOrgAttr.genClassArr;                         
		lNewAttr.sysClassArr = lOrgAttr.sysClassArr;	                       
		return;
	}
	
//======================= Master Sorts =============================================================================
	
	static public ArrayList <AttrDefn> getSortedAlphaClassAssocAttrArr (PDSObjDefn lClass) {
		TreeMap <String, AttrDefn> lAttrMapOrdered = new TreeMap <String, AttrDefn> ();
		ArrayList<AttrDefn> lAttrArr = new ArrayList<AttrDefn>();
		lAttrArr.addAll(lClass.ownedAttribute);
		lAttrArr.addAll(lClass.inheritedAttribute);
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			lAttrMapOrdered.put(lAttr.title, lAttr);
		}
		ArrayList<AttrDefn> lAttrArrOrdered = new ArrayList<AttrDefn> (lAttrMapOrdered.values()); 
		return lAttrArrOrdered;
	}

	static public ArrayList <AssocClassDefn> getSortedAlphaClassAssocClassArr (PDSObjDefn lClass) {
		TreeMap <String, AssocClassDefn> lAssocClassMap = new TreeMap <String, AssocClassDefn> ();
		ArrayList <AttrDefn> lAssocArr = new ArrayList <AttrDefn> ();
		lAssocArr.addAll(lClass.ownedAssociation);
		lAssocArr.addAll(lClass.inheritedAssociation);
		for (Iterator<AttrDefn> i = lAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			for (Iterator<PDSObjDefn> j = lAttr.valClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lClassMember = (PDSObjDefn) j.next();
				AssocClassDefn lAssocClass = new AssocClassDefn (lClassMember.title, lAttr.cardMinI, lAttr.cardMaxI, lClassMember);
				lAssocClassMap.put(lAssocClass.identifier, lAssocClass);
			}
		}
		ArrayList <AssocClassDefn> lAssocClassArr = new ArrayList <AssocClassDefn> (lAssocClassMap.values());
		return lAssocClassArr;
	}	
			
	// sort attributes or associations using MOF properties 
	static public ArrayList <AttrDefn> getSortedAssocAttrArr (ArrayList<AttrDefn> lAttrArr) {
		TreeMap<String, AttrDefn> lSortAttrMap = new TreeMap <String, AttrDefn> ();
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			AssocDefn lAssoc = masterMOFAssocIdMap.get(lAttr.identifier);
			if (lAssoc != null) {
				String sortId = lAssoc.classOrder + "_" + lAttr.identifier;
				lSortAttrMap.put(sortId, lAttr);
			} else {
				System.out.println(">>error   - Association for Attribute is missing - Sort - lAttr.identifier:" + lAttr.identifier);
			}
		}
		ArrayList <AttrDefn> lSortAttrArr = new ArrayList <AttrDefn> (lSortAttrMap.values());
		return lSortAttrArr;
	}
	
	// sort the class associated classes (Future)
	public ArrayList <PDSObjDefn> getSortedAssocClasses_actual_classes_future (ArrayList<AssocDefn> lAssocArr) {
		TreeMap<String, AssocDefn> lSortAssocMap = new TreeMap <String, AssocDefn> ();
		for (Iterator<AssocDefn> i = lAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			if (lAssoc.isAttribute) continue;
			String sortId = lAssoc.classOrder + "_" + lAssoc.identifier;
			lSortAssocMap.put(sortId, lAssoc);			
		}
		ArrayList <AssocDefn> lSortAssocArr = new ArrayList <AssocDefn> (lSortAssocMap.values());
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> ();
		for (Iterator<AssocDefn> i = lSortAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();	
			System.out.println("debug getSortedAssocClasses lAssoc.identifier:" + lAssoc.identifier);
			for (Iterator<PDSObjDefn> j = lAssoc.childClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lClassMember = (PDSObjDefn) j.next();
				lSortClassArr.add(lClassMember);
			}		
		}
		return lSortClassArr;
	}	
		
	static public String getSortId (Integer iseq) {
		String seq = iseq.toString();
		if (seq.length() < 2) {
			seq = "0" + seq;
		} else if (seq.length() > 2) {
//			seq = "99";
			seq = "89";
		}
		return (seq);
	}
	
	// get attribute array sorted by "attribute", namespace, attr title, namespace, class title
	static public ArrayList <AttrDefn> getAttArrByTitleStewardClassSteward () {
		TreeMap <String, AttrDefn> lTreeMap = new TreeMap <String, AttrDefn>();
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			lTreeMap.put(lAttr.sort_identifier, lAttr);
		}
		Collection <AttrDefn> values3 = lTreeMap.values();		
		return (new ArrayList <AttrDefn> ( values3 ));
	}
	
//====================== Miscellaneous Routines ==============================================================================	
	
	/**
	 * debug one attribute
	 */
	static void printOneAttributeIdentifier (String lIdentifier) {
//		System.out.println("\ndebug printOneAttribute lIdentifier:" + lIdentifier);
		AttrDefn lAttr = masterMOFAttrIdMap.get(lIdentifier);
		if (lAttr != null){
			System.out.println("debug printOneAttribute FOUND lIdentifier:" + lIdentifier);
			printAttr ("printOneAttribute", lAttr);
			
		} else  {
			System.out.println("debug printOneAttribute NOT FOUND lIdentifier:" + lIdentifier);
		}
	}
	
	/**
	 * debug one attribute
	 */
	static void printOneAttributeRDFIdentifier (String lRDFIdentifier) {
		System.out.println("\ndebug printOneAttribute lRDFIdentifier:" + lRDFIdentifier);
		AttrDefn lAttr = masterMOFAttrMap.get(lRDFIdentifier);
		if (lAttr != null){
			System.out.println("debug printOneAttribute FOUND lRDFIdentifier:" + lRDFIdentifier);
			printAttr ("printOneAttribute", lAttr);
			
		} else  {
			System.out.println("debug printOneAttribute NOT FOUND lRDFIdentifier:" + lRDFIdentifier);
		}
	}
	
	/**
	 * print all classes
	 */
	static void printObjectAllDebug (int lInt, ArrayList <PDSObjDefn> classArr) {
		for (Iterator<PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();			
			printObjectDebug(lInt, lClass);
		}
	}
	
	/**
	 * print one class
	 */
	static void printObjectDebug (int lInt, PDSObjDefn objClass) {
        System.out.println("\n==========================  Class  ==============================");
		if (objClass == null) {
		    System.out.println("\ndebug Class Definition - id:" + lInt + " -" + "NOT FOUND");
			return;
		}
	    System.out.println("\ndebug Class Definition - id:" + lInt + " - identifier:" + objClass.identifier);
	    System.out.println("  rdfIdentifier:" + objClass.rdfIdentifier);
		System.out.println("  identifier:" + objClass.identifier);
		System.out.println("  title:" + objClass.title);
		System.out.println("  registrationStatus:" + objClass.registrationStatus);
		System.out.println("  regAuthId:" + objClass.regAuthId);
		System.out.println("  steward:" + objClass.steward);
		System.out.println("  nameSpaceId:" + objClass.nameSpaceId);
		System.out.println("  nameSpaceIdNC:" + objClass.nameSpaceIdNC);
		System.out.println("  section:" + objClass.section);
		System.out.println("  subModelid:" + objClass.subModelId);
		System.out.println("  docSecType:" + objClass.docSecType);
		System.out.println("  subClassOfTitle:" + objClass.subClassOfTitle);
		System.out.println("  subClassOfIdentifier:" + objClass.subClassOfIdentifier);
		System.out.println("  rootClass:" + objClass.rootClass);
		System.out.println("  baseClassName:" + objClass.baseClassName);
		System.out.println("  description:" + objClass.description);
		System.out.println("  subClassLevel:" + objClass.subClassLevel);
	    System.out.println("  isUSERClass:" + objClass.isUSERClass);
	    System.out.println("  isRegistryClass:" + objClass.isRegistryClass);
	    System.out.println("  isMasterClass:" + objClass.isMasterClass);
	    System.out.println("  isUsedInModel:" + objClass.isUsedInModel);
	    System.out.println("  isUnitOfMeasure:" + objClass.isUnitOfMeasure);
	    System.out.println("  isDataType:" + objClass.isDataType);
	    System.out.println("  isFromLDD:" + objClass.isFromLDD);

        System.out.println("    -------------------------  superClass  -------------------------------");
		for (Iterator <PDSObjDefn> i = objClass.superClass.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			System.out.println("    superClass.identifier:" + lClass.identifier);
   		}
        System.out.println("    -------------------------  subClasses  -------------------------------");
		for (Iterator <String> i = objClass.subClasses.iterator(); i.hasNext();) {
			String aname = (String) i.next();
			System.out.println("    subClasses:" + aname);
   		}
        System.out.println("    -------------------------  subClass  -------------------  ------------");
		for (Iterator <PDSObjDefn> i = objClass.subClass.iterator(); i.hasNext();) {
			PDSObjDefn aname = (PDSObjDefn) i.next();
			System.out.println("    subClass:" + aname.title);
   		}
        System.out.println("    -------------------------  ownedAttribute  ---------------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttribute:" + lAttr.identifier);
		}
        System.out.println("    -------------------------  ownedAssociation  -------------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAssociation:" + lAttr.identifier);
		}
        System.out.println("    -------------------------  allAttrAssocArr  --------------------------");
		for (Iterator <AttrDefn> i = objClass.allAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    allAttrAssocArr:" + lAttr.identifier);
		}
        System.out.println("    -------------------------  ownedAttrAssocNOArr  ----------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocNOArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocNOArr:" + lAttr.identifier);
		}
        System.out.println("    -------------------------  ownedAttrAssocArr  ------------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocArr:" + lAttr.identifier);
		}
        System.out.println("    -------------------------  ownedAttrAssocAssertArr  ------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocAssertArr .iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocAssertArr :" + lAttr.identifier);
		}	
		
/*		for (Iterator <AttrDefn> i = objClass.hasSlot.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
		}
		*/
		
        System.out.println("\n    =========================   Attributes  ===============================");		
        System.out.println("    -------------------------  ownedAttribute  ----------------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
		}
        System.out.println("    -------------------------  ownedAssociation  --------------------------");
		for (Iterator <AttrDefn> i = objClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
		}
		
        System.out.println("\n    =========================  Properties  ===============================");
		for (Iterator <AssocDefn> i = objClass.PropertyArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			printProp("objdump", lAssoc);
		}
	}
	
	/**
	 * print all attributes
	 */
	public void printAttrAll (String note, HashMap <String, AttrDefn> attrDict) {
		Set <String> set = attrDict.keySet();
		Iterator <String> iter = set.iterator();
		while(iter.hasNext()) {
			String attrRDFId = (String) iter.next();
			AttrDefn attr = (AttrDefn) attrDict.get(attrRDFId);
			printAttr(note, attr);
		}
	}
	
	/**
	 * print all named attributes
	 */
	static void printAttrNamed (String note, String lTitle, HashMap <String, AttrDefn> attrDict) {
		Set <String> set = attrDict.keySet();
		Iterator <String> iter = set.iterator();
		while(iter.hasNext()) {
			String attrRDFId = (String) iter.next();
			AttrDefn attr = (AttrDefn) attrDict.get(attrRDFId);
			if (attr.title.compareTo(lTitle) == 0) {
				printAttr(note, attr);
			}
		}
	}

	/**
	 * print one attribute
	 */
	static public void printAttr (String note, AttrDefn attr) {	
        System.out.println("\n    debug Attribute Definition" + " - identifier:" + attr.identifier);
        System.out.println("        attr.rdfIdentifier:" + attr.rdfIdentifier);
		System.out.println("        attr.identifier:" + attr.identifier);
        System.out.println("        attr.nsTitle:" + attr.nsTitle);
        System.out.println("        attr.title:" + attr.title);
		System.out.println("        attr.XMLSchemaName:" + attr.XMLSchemaName);
//		System.out.println("        attr.type:" + attr.type);
        System.out.println("        attr.hasRetiredValue:" + attr.hasRetiredValue);
		System.out.println("        attr.registrationStatus:" + attr.registrationStatus);
		System.out.println("        attr.parentClassTitle:" + attr.parentClassTitle);
		if (attr.attrParentClass != null)
			System.out.println("        attr.attrParentClass.title:" + attr.attrParentClass.title);
		System.out.println("        attr.classNameSpaceIdNC:" + attr.classNameSpaceIdNC);
		System.out.println("        attr.attrNameSpaceId:" + attr.attrNameSpaceId);
		System.out.println("        attr.attrNameSpaceIdNC:" + attr.attrNameSpaceIdNC);
		System.out.println("        attr.deDataIdentifier:" + attr.deDataIdentifier);
		System.out.println("        attr.regAuthId:" + attr.regAuthId);
		System.out.println("        attr.subModelid:" + attr.subModelId);
		System.out.println("        attr.iAttribute:" + attr.isAttribute);
		System.out.println("        attr.isOwnedAttribute:" + attr.isOwnedAttribute);
		System.out.println("        attr.isRestrictedInSubclass:" + attr.isRestrictedInSubclass);
		System.out.println("        attr.isPDS4:" + attr.isPDS4);		
// 445		System.out.println("        attr.isDataType:" + attr.isDataType);		
		System.out.println("        attr.isEnumerated:" + attr.isEnumerated);		
		System.out.println("        attr.isNilable:" + attr.isNilable);		
		System.out.println("        attr.isChoice:" + attr.isChoice);		
		System.out.println("        attr.isAny:" + attr.isAny);		
		System.out.println("        attr.isFromLDD:" + attr.isFromLDD);		
// 445		System.out.println("        attr.isUnitOfMeasure:" + attr.isUnitOfMeasure);
		System.out.println("        attr.valueType:" + attr.valueType);
		System.out.println("        attr.propType:" + attr.propType);
		System.out.println("        attr.groupName:" + attr.groupName);
		System.out.println("        attr.cardMin:" + attr.cardMin);
		System.out.println("        attr.cardMax:" + attr.cardMax);
		System.out.println("        attr.description:" + attr.description);
		System.out.println("        attr.protValueType:" + attr.protValType);
		System.out.println("        attr.format:" + attr.format);
		System.out.println("        attr.minimum_characters:" + attr.minimum_characters);
		System.out.println("        attr.maximum_characters:" + attr.maximum_characters);
		System.out.println("        attr.minimum_value:" + attr.minimum_value);		
		System.out.println("        attr.maximum_value:" + attr.maximum_value);
		System.out.println("        attr.unit_of_measure_type:" + attr.unit_of_measure_type);
		System.out.println("        attr.specified_unit_id:" + attr.default_unit_id);
		System.out.println("        attr.unit_of_measure_precision:" + attr.unit_of_measure_precision);	
		if (attr.lddUserAttribute != null) {
			System.out.println("        attr.lddUserAttribute.identifier:" + attr.lddUserAttribute.identifier);
		} else {
			System.out.println("        attr.lddUserAttribute:" + "null");
		}
		
		if (attr.valArr != null && attr.valArr.size() > 0) {
			System.out.println("        has attr.valArr");

			for (Iterator <String> j = attr.valArr.iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				System.out.println("          val:" + lVal);
			}
		}
		
		if (attr.valClassArr != null && attr.valClassArr.size() > 0) {
			System.out.println("        has attr.valClassArr");

			for (Iterator <PDSObjDefn> j = attr.valClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lValClass = (PDSObjDefn) j.next();
				System.out.println("          class val:" + lValClass.title);
			}
		}
		
		if (attr.permValueArr != null && attr.permValueArr.size() > 0) {
			System.out.println("        has attr.permValueArr");

			for (Iterator <PermValueDefn> j = attr.permValueArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
				System.out.println("          PermValueDefn.value:" + lPermValueDefn.value + "  registrationStatus:" + lPermValueDefn.registrationStatus + "  lPermValueDefn.value_meaning:" + lPermValueDefn.value_meaning);
			}
		}		
	}
	
	/**
	 * print one property
	 */
	static public void printProp (String note, AssocDefn lProp) {	
        System.out.println("\n    debug Property Definition" + " - identifier:" + lProp.identifier);
        System.out.println("        lProp.rdfIdentifier:" + lProp.rdfIdentifier);
		System.out.println("        lProp.identifier:" + lProp.identifier);
        System.out.println("        lProp.title:" + lProp.title);
        System.out.println("        lProp.localIdentifier:" + lProp.localIdentifier);
		System.out.println("        lProp.className:" + lProp.className);
		System.out.println("        lProp.attrNameSpaceId:" + lProp.attrNameSpaceId);
		System.out.println("        lProp.attrNameSpaceIdNC:" + lProp.attrNameSpaceIdNC);
		System.out.println("        lProp.classNameSpaceIdNC:" + lProp.classNameSpaceIdNC);
		System.out.println("        lProp.classOrder:" + lProp.classOrder);
		System.out.println("        lProp.groupName:" + lProp.groupName);
		System.out.println("        lProp.cardMin:" + lProp.cardMin);
		System.out.println("        lProp.cardMax:" + lProp.cardMax);
		System.out.println("        lProp.cardMinI:" + lProp.cardMinI);
		System.out.println("        lProp.cardMaxI:" + lProp.cardMaxI);
		System.out.println("        lProp.referenceType:" + lProp.referenceType);
		System.out.println("        lProp.isAttribute:" + lProp.isAttribute);
		System.out.println("        lProp.isChoice:" + lProp.isChoice);		
		System.out.println("        lProp.isAny:" + lProp.isAny);		
	}
		
//	Dump the attribute dictionary (original parsed attributes; includes top class
	static public void dumpAttrDict () {
		
// 	*** Note that the Identifier in the MAP is not the identifier in the Attribute; the class has been set to USER.		

		System.out.println("\ndebug dump attribute dictionary");
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.userClassAttrIdMap.values());
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			System.out.println("debug dump attribute identifiers lAttr.identifier:" + lAttr.identifier);
		}
		return;
	}
	
//	debug - write one attributes permissible values
	static public void writePermissibleValues (String where, AttrDefn lAttr) {
		if (lAttr == null) {
			System.out.println("debug writePermissibleValues -  NULL Attribute");
			return;
		}
		System.out.println("\ndebug writePermissibleValues - " + where + " - lAttr.identifier:" + lAttr.identifier);
		if (lAttr.valArr == null || lAttr.valArr.size() == 0) {
			System.out.println("debug writePermissibleValues -  No ValArr - lAttr.identifier:" + lAttr.identifier);
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lVal = (String) i.next();
			System.out.println("debug writePermissibleValues - lVal:" + lVal);
			System.out.println("debug writePermissibleValues - lVal HEX:" + DMDocument.stringToHex(lVal));
		}
		if (lAttr.permValueArr == null || lAttr.permValueArr.size() == 0) {
			System.out.println("debug writePermissibleValues -  No Permissble Value - lAttr.identifier:" + lAttr.identifier);
			return;
		}
		for (Iterator <PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
			System.out.println("debug writePermissibleValues - lPermValueDefn.value:" + lPermValueDefn.value + "   lPermValueDefn.value_meaning:" + lPermValueDefn.value_meaning);
		}
	}
	
	/**
	 * print all rules
	 */
	
//	schematronRuleArr	
	static void printRulesAllDebug (int lInt, ArrayList <RuleDefn> lRuleArr) {
        System.out.println("\n\n==========================  Rules  ==============================");
        int cnt = 0;
		for (Iterator<RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();			
			printRuleDebug(lInt, lRule);
			cnt++;
		}
	    System.out.println("\ndebug Total Rules:" + cnt);
        System.out.println("==========================  End Rules  ==============================");
	}
	
	/**
	 * print one rule
	 */
	static void printRuleDebug (int lInt, RuleDefn lRule) {
        System.out.println("\n==========================  Rule  ==============================");
		if (lRule == null) {
		    System.out.println("\ndebug Rule Definition - id:" + lInt + " -" + "NOT FOUND");
			return;
		}

	    System.out.println("\ndebug Rule Definition - id:" + lInt + " - identifier:" + lRule.identifier);
	    System.out.println("  lRule.rdfIdentifier:" + lRule.rdfIdentifier);
		System.out.println("  lRule.identifier:" + lRule.identifier);
		System.out.println("  lRule.type:" + lRule.type);
		System.out.println("  lRule.xpath:" + lRule.xpath);
		System.out.println("  lRule.roleId:" + lRule.roleId);
		System.out.println("  lRule.attrTitle:" + lRule.attrTitle);
		System.out.println("  lRule.attrNameSpaceNC:" + lRule.attrNameSpaceNC);
		System.out.println("  lRule.classTitle:" + lRule.classTitle);
		System.out.println("  lRule.classNameSpaceNC:" + lRule.classNameSpaceNC);
		System.out.println("  lRule.classSteward:" + lRule.classSteward);
		System.out.println("  lRule.alwaysInclude:" + lRule.alwaysInclude);
		System.out.println("  lRule.isMissionOnly:" + lRule.isMissionOnly);

        System.out.println("    -------------------------  Let Assignments - Pattern  -------------------------------");
        if (lRule.letAssignPatternArr != null) {
    		for (Iterator <String> i = lRule.letAssignPatternArr.iterator(); i.hasNext();) {
    			String lLetAssignPattern = (String) i.next();
    			System.out.println("    lLetAssignPattern:" + lLetAssignPattern);
       		}
        }

        System.out.println("    -------------------------  Let Assignments - Rule  -------------------------------");
        if (lRule.letAssignArr != null) {
    		for (Iterator <String> i = lRule.letAssignArr.iterator(); i.hasNext();) {
    			String lLetAssign = (String) i.next();
    			System.out.println("    lLetAssign:" + lLetAssign);
       		}
        }
		
        System.out.println("    -------------------------  Assert Statement  -------------------------------");
        if (lRule.assertArr != null) {
    		for (Iterator <AssertDefn2> i = lRule.assertArr.iterator(); i.hasNext();) {
    			AssertDefn2 lAssert = (AssertDefn2) i.next();
    			System.out.println("    lAssert.identifier:" + lAssert.identifier);
    			System.out.println("    lAssert.attrTitle:" + lAssert.attrTitle);
    			System.out.println("    lAssert.assertType:" + lAssert.assertType);
    			System.out.println("    lAssert.assertMsg:" + lAssert.assertMsg);
    			System.out.println("    lAssert.assertStmt:" + lAssert.assertStmt);
    			System.out.println("    lAssert.specMesg:" + lAssert.specMesg);
    			
    	        System.out.println("    -------------------------  Assert Statement Test Values  -------------------------------");
    	        if (lAssert.testValArr != null) {
        			for (Iterator <String> j = lAssert.testValArr.iterator(); j.hasNext();) {
        				String lTestValue = (String) j.next();
        				System.out.println("       lTestValue:" + lTestValue);
        	   		}	
    	        }
       		}
        }
	}
}
