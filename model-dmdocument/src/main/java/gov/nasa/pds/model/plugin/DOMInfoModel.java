package gov.nasa.pds.model.plugin; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
// import org.apache.commons.lang.WordUtils;

/*
 ** Read RDF XML from a file and write it to standard out
 */
public abstract class DOMInfoModel extends Object {
	
	// print writer
	static PrintWriter prDOMWriter;
	
	// global constants
	static String ont_version_id;						// 0.1.0.0
	static String lab_version_id;						// 0100
	static String identifier_version_id;				// 0.1
	
	// global administrative records
	static ArrayList <AdminDefn> master11179AdminArr;
	static TreeMap <String, AdminDefn> master11179AdminMap;
	static TreeMap <String, AdminDefn> master11179AdminMapId;

	static ArrayList <String> fundamentalStructures;

	// global attributes
	static TreeMap <String, AttrDefn> userClassAttrIdMap;			// user Class Attributes (owned attributes); Id:class changed to USER, Attribute is a master attribute.
	
	// global 11179 data dictionary
	static ArrayList <String> masterMetaAttribute;

	// ==========================================================
	
	// global classes
	static ArrayList <DOMClass> masterDOMClassArr = new ArrayList <DOMClass> ();
	static TreeMap <String, DOMClass> masterDOMClassMap = new TreeMap <String, DOMClass> ();
	static TreeMap <String, DOMClass> masterDOMClassIdMap = new TreeMap <String, DOMClass> ();
	static TreeMap <String, DOMClass> masterDOMClassTitleMap = new TreeMap <String, DOMClass> ();
	static DOMClass masterDOMUserClass;

	// global attributes
	static ArrayList <DOMAttr> masterDOMAttrArr = new ArrayList <DOMAttr> ();
	static TreeMap <String, DOMAttr> masterDOMAttrMap = new TreeMap <String, DOMAttr> ();
	static TreeMap <String, DOMAttr> masterDOMAttrIdMap = new TreeMap <String, DOMAttr> ();
	
	static TreeMap <String, DOMAttr> userDOMClassAttrIdMap = new TreeMap <String, DOMAttr> (); // user Class Attributes (owned attributes); Id:class changed to USER, Attribute is a master attribute.
	static TreeMap <String, DOMAttr> userSingletonDOMClassAttrIdMap = new TreeMap <String, DOMAttr> (); // user Class Attributes (not owned attributes) 

	// global Properties
	static ArrayList <DOMProp> masterDOMPropArr = new ArrayList <DOMProp> ();
	static TreeMap <String, DOMProp> masterDOMPropMap = new TreeMap <String, DOMProp> ();
	static TreeMap <String, DOMProp> masterDOMPropIdMap = new TreeMap <String, DOMProp> ();
	
	// global Rules and Assert statements
	static ArrayList <DOMRule> masterDOMRuleArr = new ArrayList <DOMRule> ();
	static TreeMap <String, DOMRule> masterDOMRuleMap = new TreeMap <String, DOMRule> ();
	static TreeMap <String, DOMRule> masterDOMRuleIdMap = new TreeMap <String, DOMRule> (); // to be deprecated
	
	// new rules
	static ArrayList <DOMRule> masterDOMRuleNewArr;
	static TreeMap <String, DOMRule> masterDOMRuleNewMap;	
	
	// global data types
	static ArrayList <DOMDataType> masterDOMDataTypeArr = new ArrayList <DOMDataType> (); 
	static TreeMap <String, DOMDataType> masterDOMDataTypeMap = new TreeMap <String, DOMDataType> ();
	static TreeMap <String, DOMDataType> masterDOMDataTypeTitleMap = new TreeMap <String, DOMDataType> (); 
	
	// global Units
	static ArrayList <DOMUnit> masterDOMUnitArr = new ArrayList <DOMUnit> ();
	static TreeMap <String, DOMUnit> masterDOMUnitMap = new TreeMap <String, DOMUnit> ();
	static TreeMap <String, DOMUnit> masterDOMUnitTitleMap = new TreeMap <String, DOMUnit> ();
	
	// global property maps
	static TreeMap <String, PropertyMapsDefn> masterPropertyMapsMap = new TreeMap <String, PropertyMapsDefn> ();
	static ArrayList <PropertyMapsDefn> masterPropertyMapsArr = new ArrayList <PropertyMapsDefn> ();
	
	// Array of namespaces that have a class, attribute, etc as a member. Determines what files to write.
	static ArrayList <String> masterNameSpaceHasMemberArr = new ArrayList <String> ();
		
	// All CD and DEC values for the Attributes
	static TreeMap <String, DOMIndexDefn> cdDOMAttrMap = new TreeMap <String, DOMIndexDefn>();
	static TreeMap <String, DOMIndexDefn> decDOMAttrMap = new TreeMap <String, DOMIndexDefn>();
	
	static DOMClass LDDToolSingletonDOMClass; // Class for LDD singleton attributes (Discipline or Mission)
	
	// global science discipline facet map 
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
	// 77775
//	static DOMClass LDDToolSingletonDOMClass; // Class for LDD singleton attributes (Discipline or Mission)
	
	// values and value meaning
	static TreeMap <String, PermValueDefn> masterValueMeaningMap;

	// Attribute Namespace Resolution Map
	static TreeMap <String, String> attrNamespaceResolutionMap;
	
	// Local Classes	
	ArrayList <PDSObjDefn> objArr;
	HashMap <String, PDSObjDefn> objDict;
	HashMap <String, AttrDefn> attrDict;
	
//  Parsed Classes, Props, and Attributes
	static ArrayList <DOMClass> parsedClassArr;
	static TreeMap <String, DOMClass> parsedClassMap;
	
	static ArrayList <DOMProtAttr> parsedProtAttrArr;
	static TreeMap <String, DOMProtAttr> parsedProtAttrMap;	
		
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
		dataTypePDS4ProtegeMap.put("ASCII_AnyURI", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Boolean", "STRING");
//		dataTypePDS4ProtegeMap.put("ASCII_Boolean_TF", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_DOI", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_DOY", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_DOY", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_DOY_UTC", "STRING");
//		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_UTC", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_YMD", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_Time_YMD_UTC", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Date_YMD", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Directory_Path_Name", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_File_Name", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_File_Specification_Name", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Integer", "INTEGER");
		dataTypePDS4ProtegeMap.put("ASCII_LID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_LIDVID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_LIDVID_LID", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_MD5_Checksum", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_NonNegative_Integer", "INTEGER");
		dataTypePDS4ProtegeMap.put("ASCII_Numeric_Base16", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Numeric_Base2", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Numeric_Base8", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Real", "FLOAT");
		dataTypePDS4ProtegeMap.put("ASCII_Short_String_Collapsed", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Short_String_Preserved", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Text_Collapsed", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Text_Preserved", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_Time", "STRING");
		dataTypePDS4ProtegeMap.put("ASCII_VID", "STRING");
		dataTypePDS4ProtegeMap.put("UTF8_Short_String_Collapsed", "STRING");
		dataTypePDS4ProtegeMap.put("UTF8_Short_String_Preserved", "STRING");
		dataTypePDS4ProtegeMap.put("UTF8_Text_Preserved ", "STRING");

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
		attrNamespaceResolutionMap.put("disp.Color_Display_Settings.disp.comment", DMDocument.masterNameSpaceIdNCLC);
		attrNamespaceResolutionMap.put("disp.Display_Direction.disp.comment", DMDocument.masterNameSpaceIdNCLC);
		attrNamespaceResolutionMap.put("disp.Movie_Display_Settings.disp.comment", DMDocument.masterNameSpaceIdNCLC);
		attrNamespaceResolutionMap.put("rings.Occultation_Supplement.rings.sampling_parameter_name", DMDocument.masterNameSpaceIdNCLC);
		attrNamespaceResolutionMap.put("rings.Occultation_Supplement.rings.sampling_parameter_unit", DMDocument.masterNameSpaceIdNCLC);
		attrNamespaceResolutionMap.put("rings.Occultation_Supplement.rings.sampling_parameter_interval", DMDocument.masterNameSpaceIdNCLC);
		// assocs (AttrDefn)
		attrNamespaceResolutionMap.put("disp.Display_Settings.disp.local_internal_reference", DMDocument.masterNameSpaceIdNCLC);			
		
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
		for (Iterator <String> i = fundamentalStructures.iterator(); i.hasNext();) {
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
//		String lNSTitle = DMDocument.registrationAuthorityIdentifierValue + "." + lAttrNameSpaceIdNC + "." + lAttrTitle;
		String lNSTitle = lAttrNameSpaceIdNC + "." + lAttrTitle;
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
		String lIdentifier = lInputIdentifier + "." + getNextUId();
		return lIdentifier;
	}	
	
	/**
	*  wrap a text string
	*/
	static public String wrapText (String lString, int beginOffset, int endOffset) {
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
		
		// set up string buffer
		StringBuffer lInputStringBuff = new StringBuffer(lString);
		StringBuffer lOutputStringBuff = new StringBuffer();
		
		// setup major loop for transfering the string in the input buffer to a wrapped string in the output buffer 
		int indent = 0;
//		char lfcr = '\n';
		char cr = '\r';
		char lf = '\n';
		boolean isFirst = true;
		while (lInputBuffOffset1 < lStringLength) {
			// insert linefeed (except first time)
			if (isFirst) isFirst = false;
			else { 
				lOutputStringBuff.insert(lOutputBuffOffset++, cr);
				lOutputStringBuff.insert(lOutputBuffOffset++, lf);
			}
			
//			System.out.println("\ndebug wrapText -insert blanks- lInputBuffOffset1:" + lInputBuffOffset1);
			
			// find next non-blank character in input buffer
			while (lInputBuffOffset1 < lStringLength) {
				if (lInputStringBuff.charAt(lInputBuffOffset1) != ' ') break;
				lInputBuffOffset1++;
			}
			
			// insert prefix blanks in output buffer
			for (int i = 0; i < beginOffset + indent; i++) lOutputStringBuff.insert(lOutputBuffOffset++, ' ');

			// find the next chunk of text by finding the next break before the end of the wrapwidth (delimiter = ' ')
			currOffset = beginOffset + indent; // counter for wrapped string size
			indent = 2;
			lInputBuffOffset2 = lInputBuffOffset1; // ending offset in input buffer; init at start offset
			int lIBO1 = lInputBuffOffset1; // offset in input buffer; init at start offset
			lInputBuffOffsetPrevBlank = -1; // location to backtrack to if last word goes over wrap boundary		
			
			while (lIBO1 < lStringLength && currOffset <= endOffset) {
				if (lInputStringBuff.charAt(lIBO1) == ' ') lInputBuffOffsetPrevBlank = lIBO1;
				lIBO1++; currOffset++;
				lInputBuffOffset2++;
			}		
			if (lIBO1 < lStringLength) { // check to see of input buffer is exhausted
				if (lInputBuffOffsetPrevBlank > -1) { // was a blank found
					lInputBuffOffset2 = lInputBuffOffsetPrevBlank;
				}
			}
			// copy in the next chunk of text
			while (lInputBuffOffset1 < lInputBuffOffset2) {
				lOutputStringBuff.insert(lOutputBuffOffset, lInputStringBuff.charAt(lInputBuffOffset1));
				lInputBuffOffset1++;
				lOutputBuffOffset++;
			}
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
						for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
							String lVal = (String) j.next();
							if (lVal.compareTo("") != 0) {
								if (lRefTypeArr.contains(lVal)) {
									System.out.println("\n***Warning***  getAllRefAssocType - found duplicate association -  Association:" + lVal);
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
	
	// clone an attribute 
	static public DOMAttr cloneDOMAttr (String lClassNameSpaceIdNC, String lClassTitle, String lAttrNameSpaceIdNC, String lAttrTitle, DOMAttr lOrgAttr) {
		DOMAttr lNewAttr = new DOMAttr ();				              					              
//		lNewAttr.uid = lOrgAttr.uid;										              
		lNewAttr.setRDFIdentifier (lAttrTitle);								              										              
		lNewAttr.setIdentifier(lClassNameSpaceIdNC, lClassTitle, lAttrNameSpaceIdNC, lAttrTitle); 						              
		lNewAttr.sort_identifier = lOrgAttr.sort_identifier;				              
//		lNewAttr.attrAnchorString = lOrgAttr.attrAnchorString;			              
		lNewAttr.title = lOrgAttr.title;  								              
		lNewAttr.versionId = lOrgAttr.versionId;							              
		lNewAttr.registrationStatus = lOrgAttr.registrationStatus;		              
		lNewAttr.XMLSchemaName = lAttrTitle;					              
		lNewAttr.regAuthId = lOrgAttr.regAuthId;							              
		lNewAttr.steward = lOrgAttr.steward;								              
		lNewAttr.classSteward = lOrgAttr.classSteward;					              
		lNewAttr.nameSpaceId = lOrgAttr.nameSpaceId;                     
		lNewAttr.nameSpaceIdNC = lOrgAttr.nameSpaceIdNC;                   
		lNewAttr.classNameSpaceIdNC = lOrgAttr.classNameSpaceIdNC;                  
		lNewAttr.submitter = lOrgAttr.submitter;							              
		lNewAttr.subModelId = lOrgAttr.subModelId;						              
		lNewAttr.parentClassTitle = lOrgAttr.parentClassTitle;							              
		lNewAttr.attrParentClass = lOrgAttr.attrParentClass;							              
		lNewAttr.classConcept = lOrgAttr.classConcept;					              
		lNewAttr.dataConcept = lOrgAttr.dataConcept;						              
		lNewAttr.classWord = lOrgAttr.classWord;							              
		lNewAttr.definition = lOrgAttr.definition;                         
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
	
	static public ArrayList <DOMAssocClassDefn> getSortedAlphaClassAssocClassArr (DOMClass lClass) {
		TreeMap <String, DOMAssocClassDefn> lAssocClassGroupMap = new TreeMap <String, DOMAssocClassDefn> ();
	
		// get owned and inherited associated classes
		for (Iterator <DOMProp> i = lClass.ownedAssocArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
				DOMClass lAssocClass = (DOMClass) lDOMProp.hasDOMObject;
				DOMAssocClassDefn lAssocClassGroup = new DOMAssocClassDefn (lAssocClass.title, lDOMProp.cardMinI, lDOMProp.cardMaxI, lAssocClass);
				lAssocClassGroupMap.put(lAssocClassGroup.identifier, lAssocClassGroup);
			}
		}
		for (Iterator <DOMProp> i = lClass.inheritedAssocArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
				DOMClass lAssocClass = (DOMClass) lDOMProp.hasDOMObject;
				DOMAssocClassDefn lAssocClassGroup = new DOMAssocClassDefn (lAssocClass.title, lDOMProp.cardMinI, lDOMProp.cardMaxI, lAssocClass);
				lAssocClassGroupMap.put(lAssocClassGroup.identifier, lAssocClassGroup);
			}
		}
		ArrayList <DOMAssocClassDefn> lAssocClassGroupArr = new ArrayList <DOMAssocClassDefn> (lAssocClassGroupMap.values());
		return lAssocClassGroupArr;
	}
	
// 777
	/*
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
			for (Iterator<PDSObjDefn> j = lAssoc.childClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lClassMember = (PDSObjDefn) j.next();
				lSortClassArr.add(lClassMember);
			}		
		}
		return lSortClassArr;
	} */
		
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
	// 7777 deprecate this
	static public ArrayList <DOMAttr> getAttArrByTitleStewardClassSteward () {
		TreeMap <String, DOMAttr> lDOMAttrMap = new TreeMap <String, DOMAttr>();
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			lDOMAttrMap.put(lAttr.sort_identifier, lAttr);
		}
		ArrayList <DOMAttr> lDOMAttrArr = new ArrayList <DOMAttr>(lDOMAttrMap.values());	
		return (lDOMAttrArr);
	}
	
	// for DOM model
	// 7777
	static public ArrayList <DOMProp> getPropArrByTitleStewardClassSteward () {
		String lBlanks = "                              "; // 30 blanks
		TreeMap <String, DOMProp> lDOMPropMap = new TreeMap <String, DOMProp>();
		for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null) {
				if (lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					if (lDOMAttr.attrParentClass != null) {
						DOMClass lDOMClass = lDOMAttr.attrParentClass;
						String lClassTitle = lDOMClass.title;
						String lClassNameSpaceLCNC = lDOMClass.nameSpaceIdNC;
						String lMemberTitle = lDOMAttr.title;
						String lMemberNameSpaceLCNC = lDOMAttr.nameSpaceIdNC;
						String lSortKey = lMemberTitle + "_" + lMemberNameSpaceLCNC;
						int lLength = lSortKey.length();
						if (lLength >= 30) {
							lLength = 30;
						}
						String lPaddedSortKey = lSortKey + lBlanks.substring(0, 30 - lLength) + lClassTitle + "_" + lClassNameSpaceLCNC;
						lDOMPropMap.put(lPaddedSortKey, lDOMProp);
					}
				} else if (lDOMProp.hasDOMObject instanceof DOMClass) {
					DOMClass lDOMMemberClass = (DOMClass) lDOMProp.hasDOMObject;
					if (lDOMProp.attrParentClass != null) {
						DOMClass lDOMClass = lDOMProp.attrParentClass;
						String lClassTitle = lDOMClass.title;
						String lClassNameSpaceLCNC = lDOMClass.nameSpaceIdNC;
						String lMemberTitle = lDOMMemberClass.title;
						String lMemberNameSpaceLCNC = lDOMMemberClass.nameSpaceIdNC;
						String lSortKey = lMemberTitle + "_" + lMemberNameSpaceLCNC;
						int lLength = lSortKey.length();
						if (lLength >= 30) {
							lLength = 30;
						}
						String lPaddedSortKey = lSortKey + lBlanks.substring(0, 30 - lLength) + lClassTitle + "_" + lClassNameSpaceLCNC;
						lDOMPropMap.put(lPaddedSortKey, lDOMProp);
					}
				}
			}
		}
		ArrayList <DOMProp> lDOMPropArr = new ArrayList <DOMProp>(lDOMPropMap.values());	
		return (lDOMPropArr);
	}
	
//====================== Miscellaneous Routines ==============================================================================	
	
	/**
	 * print all classes
	 */
	static void domWriter (ArrayList <DOMClass> classArr, String lFileName)  throws java.io.IOException {
		prDOMWriter = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));	
		prDOMWriter.println("\ndebug - domWriter - classArr.size():" + classArr.size());
		for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();			
			domClassWriter(lClass, prDOMWriter, lFileName);
		}
		
		// write DOMRules
		prDOMWriter.println("\ndebug - domWriter - masterDOMRuleArr.size():" + masterDOMRuleArr.size());
		printRulesAllDebug (masterDOMRuleArr, prDOMWriter);
		prDOMWriter.close();
	}
	
	static void domClassWriter (DOMClass objClass, PrintWriter prDOMWriter, String lFileName) {
		prDOMWriter.println("\n==========================  Class  ==============================");
		if (objClass == null) {
		    System.out.println("\ndebug Class Definition - id:" + lFileName + " -" + "NOT FOUND");
			return;
		}
		prDOMWriter.println("\ndebug Class Definition - identifier:" + objClass.identifier);
	    prDOMWriter.println("  rdfIdentifier:" + objClass.rdfIdentifier);
		prDOMWriter.println("  identifier:" + objClass.identifier);
		prDOMWriter.println("  versionId:" + objClass.versionId);
		prDOMWriter.println("  sequenceId:" + objClass.sequenceId);
		prDOMWriter.println("  title:" + objClass.title);
		prDOMWriter.println("  definition:" + objClass.definition);
		prDOMWriter.println("  registrationStatus:" + objClass.registrationStatus);
		prDOMWriter.println("  isDeprecated:" + objClass.isDeprecated);
		
		prDOMWriter.println("  isDigital:" + objClass.isDigital);
		prDOMWriter.println("  isPhysical:" + objClass.isPhysical);
		
		prDOMWriter.println("  regAuthId:" + objClass.regAuthId);
		prDOMWriter.println("  subModelid:" + objClass.subModelId);
		prDOMWriter.println("  steward:" + objClass.steward);
		prDOMWriter.println("  nameSpaceId:" + objClass.nameSpaceId);
		prDOMWriter.println("  nameSpaceIdNC:" + objClass.nameSpaceIdNC);
	    prDOMWriter.println("  isUsedInModel:" + objClass.isUsedInModel);
		prDOMWriter.println("  isAbstract:" + objClass.isAbstract);
	    prDOMWriter.println("  isFromLDD:" + objClass.isFromLDD);
		prDOMWriter.println("  anchorString:" + objClass.anchorString);

		
		prDOMWriter.println("  section:" + objClass.section);
		prDOMWriter.println("  role:" + objClass.role);
		prDOMWriter.println("  xPath:" + objClass.xPath);
		prDOMWriter.println("  docSecType:" + objClass.docSecType);
		prDOMWriter.println("  rootClass:" + objClass.rootClass);
		prDOMWriter.println("  baseClassName:" + objClass.baseClassName);
		prDOMWriter.println("  localIdentifier:" + objClass.localIdentifier);
		prDOMWriter.println("  subClassLevel:" + objClass.subClassLevel);
		prDOMWriter.println("  subClassOfTitle:" + objClass.subClassOfTitle);
		prDOMWriter.println("  subClassOfIdentifier:" + objClass.subClassOfIdentifier);

	    prDOMWriter.println("  isUSERClass:" + objClass.isUSERClass);
		prDOMWriter.println("  isMasterClass:" + objClass.isMasterClass);
		prDOMWriter.println("  isSchema1Class:" + objClass.isSchema1Class);
	    prDOMWriter.println("  isRegistryClass:" + objClass.isRegistryClass);
		prDOMWriter.println("  isUsedInModel:" + objClass.isUsedInModel);
		prDOMWriter.println("  isAnExtension:" + objClass.isAnExtension);
		prDOMWriter.println("  isARestriction:" + objClass.isARestriction);
		prDOMWriter.println("  isVacuous:" + objClass.isVacuous);
	    prDOMWriter.println("  isUnitOfMeasure:" + objClass.isUnitOfMeasure);
	    prDOMWriter.println("  isDataType:" + objClass.isDataType);
		prDOMWriter.println("  isTDO:" + objClass.isTDO);
		prDOMWriter.println("  isChoice:" + objClass.isChoice);
		prDOMWriter.println("  isAny:" + objClass.isAny);
		prDOMWriter.println("  includeInThisSchemaFile:" + objClass.includeInThisSchemaFile);
		prDOMWriter.println("  isFromLDD:" + objClass.isFromLDD);
		prDOMWriter.println("  isReferencedFromLDD:" + objClass.isReferencedFromLDD);
		prDOMWriter.println("  isExposed:" + objClass.isExposed);

    	ArrayList <DOMClass> lSortDOMClassArr;
    	ArrayList <DOMProp> lSortDOMPropArr;
    	ArrayList <DOMAttr> lSortDOMAttrArr;
    	
        prDOMWriter.println("-------------------------  superClass  --------------------------");
        lSortDOMClassArr = sortDOMClass (objClass.superClassHierArr);
        for (Iterator <DOMClass> i = lSortDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			prDOMWriter.println("    superClass.identifier:" + lClass.identifier);
   		}       
           
        prDOMWriter.println("-------------------------  subClass  ----------------------------");
        lSortDOMClassArr = sortDOMClass (objClass.subClassHierArr);
		for (Iterator <DOMClass> i = lSortDOMClassArr.iterator(); i.hasNext();) {
			DOMClass aname = (DOMClass) i.next();
			prDOMWriter.println("    subClass:" + aname.title);
   		}
		
        prDOMWriter.println("-------------------------  ownedAttribute  ----------------------");
        lSortDOMPropArr = sortDOMProp (objClass.ownedAttrArr);
        for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
        	DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    ownedAttribute:" + lAttr.identifier);
   		}
		
        prDOMWriter.println("-------------------------  inheritedAttribute  ----------------------");
        lSortDOMPropArr = sortDOMProp (objClass.inheritedAttrArr);
        for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
        	DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    inheritedAttrArr:" + lAttr.identifier);
   		}
        
        prDOMWriter.println("-------------------------  ownedAssociation  --------------------");
        lSortDOMPropArr = sortDOMProp (objClass.ownedAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    ownedAssociation:" + lAttr.identifier);
		}
	     
        prDOMWriter.println("-------------------------  inheritedAssociation  --------------------");
        lSortDOMPropArr = sortDOMProp (objClass.inheritedAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    inheritedAssocArr:" + lAttr.identifier);
		}

		prDOMWriter.println("-------------------------  allAttrAssocArr  ---------------------");
		lSortDOMPropArr = sortDOMProp (objClass.allAttrAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    allAttrAssocArr:" + lAttr.identifier);
		}

		prDOMWriter.println("-------------------------  ownedAttrAssocNOArr  -----------------");
		lSortDOMPropArr = sortDOMProp (objClass.ownedAttrAssocNOArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    ownedAttrAssocNOArr:" + lAttr.identifier);
		}

		prDOMWriter.println("-------------------------  ownedAttrAssocArr  -------------------");
		lSortDOMPropArr = sortDOMProp (objClass.ownedAttrAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lAttr = (DOMProp) i.next();
			prDOMWriter.println("    ownedAttrAssocArr:" + lAttr.identifier);
		}
		
		prDOMWriter.println("-------------------------  allEnumAttrArr  -------------");
    	lSortDOMAttrArr = sortDOMAttr (objClass.allEnumAttrArr);
		for (Iterator <DOMAttr> i = lSortDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			prDOMWriter.println("    allEnumAttrArr :" + lAttr.identifier);
		}

		prDOMWriter.println("\n=========================  Properties  ==========================");
		lSortDOMPropArr = sortDOMProp (objClass.allAttrAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			DOMPropWriter(lProp, prDOMWriter);
		}

		prDOMWriter.println("\n=========================   Attributes  =========================");		
        
		prDOMWriter.println("-------------------------  ownedAttribute  --------------------------");
		lSortDOMPropArr = sortDOMProp (objClass.ownedAttrArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
				DOMAttrWriter(lDOMAttr, prDOMWriter);
			}
		}
		
		prDOMWriter.println("-------------------------  ownedAssociation (Class Identifiers) -----");
		lSortDOMPropArr = sortDOMProp (objClass.ownedAssocArr);
		for (Iterator <DOMProp> i = lSortDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
				DOMClass lDOMClass = (DOMClass) lDOMProp.hasDOMObject;
				prDOMWriter.println("    ownedAssocArr :" + lDOMClass.identifier);
			}	
		}
	}
	
	/**
	 * print one attribute
	 */
	static public void DOMAttrWriter (DOMAttr attr,  PrintWriter prDOMWriter) {	
        prDOMWriter.println("\n    debug Attribute Definition" + " - identifier:" + attr.identifier);
        prDOMWriter.println("        attr.rdfIdentifier:" + attr.rdfIdentifier);
		prDOMWriter.println("        attr.identifier:" + attr.identifier);
        prDOMWriter.println("        attr.versionId:" + attr.versionId);
        prDOMWriter.println("        attr.sequenceId:" + attr.sequenceId);
        prDOMWriter.println("        attr.title:" + attr.title);
        prDOMWriter.println("        attr.definition:" + attr.definition);
        prDOMWriter.println("        attr.registrationStatus:" + attr.registrationStatus);
        prDOMWriter.println("        attr.isDeprecated:" + attr.isDeprecated);

        prDOMWriter.println("        attr.isDigital:" + attr.isDigital);
        prDOMWriter.println("        attr.isPhysical:" + attr.isPhysical);

		prDOMWriter.println("        attr.regAuthId:" + attr.regAuthId);
		prDOMWriter.println("        attr.subModelid:" + attr.subModelId);
		prDOMWriter.println("        attr.steward:" + attr.steward);
		prDOMWriter.println("        attr.nameSpaceId:" + attr.nameSpaceId);
		prDOMWriter.println("        attr.nameSpaceIdNC:" + attr.nameSpaceIdNC);
		prDOMWriter.println("        attr.isUsedInModel:" + attr.isUsedInModel);
		prDOMWriter.println("        attr.isAbstract:" + attr.isAbstract);
		prDOMWriter.println("        attr.anchorString:" + attr.anchorString);
		
        prDOMWriter.println("        attr.xPath:" + attr.xPath);
        prDOMWriter.println("        attr.nsTitle:" + attr.nsTitle);
        prDOMWriter.println("        attr.sort_identifier:" + attr.sort_identifier); 
		prDOMWriter.println("        attr.XMLSchemaName:" + attr.XMLSchemaName);
		prDOMWriter.println("        attr.classNameSpaceIdNC:" + attr.classNameSpaceIdNC);
		prDOMWriter.println("        attr.classSteward:" + attr.classSteward);
		prDOMWriter.println("        attr.submitter:" + attr.submitter);
		prDOMWriter.println("        attr.parentClassTitle:" + attr.parentClassTitle);
		if (attr.attrParentClass != null)
			prDOMWriter.println("        attr.attrParentClass.identifier:" + attr.attrParentClass.identifier);
		else
			prDOMWriter.println("        attr.attrParentClass.identifier:" + "null");
		prDOMWriter.println("        attr.classConcept:" + attr.classConcept);
		prDOMWriter.println("        attr.dataConcept:" + attr.dataConcept);
		prDOMWriter.println("        attr.lddLocalIdentifier:" + attr.lddLocalIdentifier);
		if (attr.lddUserAttribute != null) prDOMWriter.println("        attr.lddUserAttribute.identifier:" + attr.lddUserAttribute.identifier);
			else prDOMWriter.println("        attr.lddUserAttribute:" + "null");
		
		prDOMWriter.println("        attr.xmlBaseDataType:" + attr.xmlBaseDataType);
		prDOMWriter.println("        attr.protValType:" + attr.protValType);
		prDOMWriter.println("        attr.propType:" + attr.propType);
		prDOMWriter.println("        attr.valueType:" + attr.valueType);
		prDOMWriter.println("        attr.groupName:" + attr.groupName);
		prDOMWriter.println("        attr.cardMin:" + attr.cardMin);
		prDOMWriter.println("        attr.cardMax:" + attr.cardMax);
		prDOMWriter.println("        attr.cardMinI:" + attr.cardMinI);
		prDOMWriter.println("        attr.cardMaxI:" + attr.cardMaxI);
		
		prDOMWriter.println("        attr.minimum_characters:" + attr.minimum_characters);
		prDOMWriter.println("        attr.maximum_characters:" + attr.maximum_characters);
		prDOMWriter.println("        attr.minimum_value:" + attr.minimum_value);		
		prDOMWriter.println("        attr.maximum_value:" + attr.maximum_value);
		prDOMWriter.println("        attr.format:" + attr.format);
		prDOMWriter.println("        attr.pattern:" + attr.pattern);
		prDOMWriter.println("        attr.unit_of_measure_type:" + attr.unit_of_measure_type);
		prDOMWriter.println("        attr.default_unit_id:" + attr.default_unit_id);
		prDOMWriter.println("        attr.unit_of_measure_precision:" + attr.unit_of_measure_precision);
		
		prDOMWriter.println("        attr.isAttribute:" + attr.isAttribute);
		prDOMWriter.println("        attr.isOwnedAttribute:" + attr.isOwnedAttribute);
		prDOMWriter.println("        attr.isPDS4:" + attr.isPDS4);		
		prDOMWriter.println("        attr.isEnumerated:" + attr.isEnumerated);		
		prDOMWriter.println("        attr.isUsedInClass:" + attr.isUsedInClass);
		prDOMWriter.println("        attr.isRestrictedInSubclass:" + attr.isRestrictedInSubclass);
		prDOMWriter.println("        attr.isMeta:" + attr.isMeta);
		prDOMWriter.println("        attr.hasAttributeOverride:" + attr.hasAttributeOverride);
		prDOMWriter.println("        attr.isNilable:" + attr.isNilable);		
		prDOMWriter.println("        attr.isChoice:" + attr.isChoice);		
		prDOMWriter.println("        attr.isAny:" + attr.isAny);		
		prDOMWriter.println("        attr.isFromLDD:" + attr.isFromLDD);
        prDOMWriter.println("        attr.hasRetiredValue:" + attr.hasRetiredValue);
        
        prDOMWriter.println("        attr.classWord:" + attr.classWord);
		prDOMWriter.println("        attr.deDataIdentifier:" + attr.deDataIdentifier);

// 		prDOMWriter.println("        attr.type:" + attr.type);
//		prDOMWriter.println("        attr.isDataType:" + attr.isDataType);		
//		prDOMWriter.println("        attr.isUnitOfMeasure:" + attr.isUnitOfMeasure);
		
// 		Permissible values
		prDOMWriter.println("\n        has attr.domPermValueArr");
		if (attr.domPermValueArr != null && attr.domPermValueArr.size() > 0) {
			for (Iterator <DOMProp> j = attr.domPermValueArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
					DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.hasDOMObject;
					prDOMWriter.println("          lDOMPermVal.value:" + lDOMPermVal.value + "  lDOMPermVal.registrationStatus:" + lDOMPermVal.registrationStatus + "  lDOMPermVal.value_meaning:" + lDOMPermVal.value_meaning);
				}
			}
		} else {
			if (attr.domPermValueArr == null) prDOMWriter.println("            attr.domPermValueArr:" + "null");
			else                              prDOMWriter.println("            attr.domPermValueArr.size():" + attr.domPermValueArr.size());
		}
	}
	
	static public void DOMPropWriter (DOMProp lProp,  PrintWriter prDOMWrite) {	
		prDOMWriter.println("\n    debug Property Definition" + " - identifier:" + lProp.identifier);
		prDOMWriter.println("        lProp.rdfIdentifier:" + lProp.rdfIdentifier);
		prDOMWriter.println("        lProp.identifier:" + lProp.identifier);
		prDOMWriter.println("        lProp.versionId:" + lProp.versionId);
		prDOMWriter.println("        lProp.sequenceId:" + lProp.sequenceId);
		prDOMWriter.println("        lProp.title:" + lProp.title);
		prDOMWriter.println("        lProp.definition:" + lProp.definition);
		prDOMWriter.println("        lProp.registrationStatus:" + lProp.registrationStatus);
		prDOMWriter.println("        lProp.isDeprecated:" + lProp.isDeprecated);
		
		prDOMWriter.println("        lProp.isDigital:" + lProp.isDigital);
		prDOMWriter.println("        lProp.isPhysical:" + lProp.isPhysical);

		prDOMWriter.println("        lProp.regAuthId:" + lProp.regAuthId);
		prDOMWriter.println("        lProp.subModelId:" + lProp.subModelId);
		prDOMWriter.println("        lProp.steward:" + lProp.steward);
		prDOMWriter.println("        lProp.nameSpaceId:" + lProp.nameSpaceId);
		prDOMWriter.println("        lProp.nameSpaceIdNC:" + lProp.nameSpaceIdNC);
		prDOMWriter.println("        lProp.isUsedInModel:" + lProp.isUsedInModel);
		prDOMWriter.println("        lProp.isAbstract:" + lProp.isAbstract);
		prDOMWriter.println("        lProp.isFromLDD:" + lProp.isFromLDD);
		prDOMWriter.println("        lProp.anchorString:" + lProp.anchorString);
		
		prDOMWriter.println("        lProp.cardMin:" + lProp.cardMin);
		prDOMWriter.println("        lProp.cardMax:" + lProp.cardMax);
		prDOMWriter.println("        lProp.cardMinI:" + lProp.cardMinI);
		prDOMWriter.println("        lProp.cardMaxI:" + lProp.cardMaxI);
		prDOMWriter.println("        lProp.classOrder:" + lProp.classOrder);
		
		if (lProp.attrParentClass != null)
			prDOMWriter.println("        lProp.attrParentClass.identifier:" + lProp.attrParentClass.identifier);
		else
			prDOMWriter.println("        lProp.attrParentClass.identifier:" + "null");

		prDOMWriter.println("        lProp.localIdentifier:" + lProp.localIdentifier);
		prDOMWriter.println("        lProp.parentClassTitle:" + lProp.parentClassTitle);
		prDOMWriter.println("        lProp.classNameSpaceIdNC:" + lProp.classNameSpaceIdNC);
		prDOMWriter.println("        lProp.classSteward:" + lProp.classSteward);

		prDOMWriter.println("        lProp.groupName:" + lProp.groupName);
		prDOMWriter.println("        lProp.referenceType:" + lProp.referenceType);
		prDOMWriter.println("        lProp.isPDS4:" + lProp.isPDS4);
		prDOMWriter.println("        lProp.isAttribute:" + lProp.isAttribute);
		prDOMWriter.println("        lProp.isChoice:" + lProp.isChoice);		
		prDOMWriter.println("        lProp.isAny:" + lProp.isAny);		
		prDOMWriter.println("        lProp.isSet:" + lProp.isSet);		
		prDOMWriter.println("        lProp.isRestrictedInSubclass:" + lProp.isRestrictedInSubclass);	
		
		prDOMWriter.println("        lProp.enclLocalIdentifier:" + lProp.enclLocalIdentifier);
		prDOMWriter.println("        lProp.minimumOccurrences:" + lProp.minimumOccurrences);
		prDOMWriter.println("        lProp.maximumOccurrences:" + lProp.maximumOccurrences);
	}
	
	static ArrayList <DOMClass> sortDOMClass (ArrayList <DOMClass> ISOArr) {
		TreeMap <String, DOMClass> lISOMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = ISOArr.iterator(); i.hasNext();) {
			DOMClass lISO = (DOMClass) i.next();
			lISOMap.put(lISO.identifier, lISO);
		}
		ArrayList <DOMClass> lISOArr = new ArrayList <DOMClass> (lISOMap.values());
		return lISOArr;
	}
	
	static ArrayList <DOMAttr> sortDOMAttr (ArrayList <DOMAttr> ISOArr) {
		TreeMap <String, DOMAttr> lISOMap = new TreeMap <String, DOMAttr> ();
		for (Iterator<DOMAttr> i = ISOArr.iterator(); i.hasNext();) {
			DOMAttr lISO = (DOMAttr) i.next();
			lISOMap.put(lISO.identifier, lISO);
		}
		ArrayList <DOMAttr> lISOArr = new ArrayList <DOMAttr> (lISOMap.values());
		return lISOArr;
	}
	
	static ArrayList <DOMProp> sortDOMProp (ArrayList <DOMProp> ISOArr) {
		TreeMap <String, DOMProp> lISOMap = new TreeMap <String, DOMProp> ();
		for (Iterator<DOMProp> i = ISOArr.iterator(); i.hasNext();) {
			DOMProp lISO = (DOMProp) i.next();
			lISOMap.put(lISO.identifier, lISO);
		}
		ArrayList <DOMProp> lISOArr = new ArrayList <DOMProp> (lISOMap.values());
		return lISOArr;
	}
	
	/**
	 * print all rules
	 */
	
//	schematronRuleArr	
	static void printRulesAllDebug (ArrayList <DOMRule> lRuleArr, PrintWriter prDOMWrite) {

		prDOMWriter.println("\n\n==========================  Rules  ==============================");
		for (Iterator<DOMRule> i = lRuleArr.iterator(); i.hasNext();) {
			DOMRule lRule = (DOMRule) i.next();			
			printRuleDebug(lRule, prDOMWrite);
		}
		prDOMWriter.println("==========================  End Rules  ==============================");
	}
	
	/**
	 * print one rule
	 */
	static void printRuleDebug (DOMRule lRule, PrintWriter prDOMWrite) {
		prDOMWriter.println("\n==========================  Rule  ===================================");
		prDOMWriter.println("  lRule.rdfIdentifier:" + lRule.rdfIdentifier);
		prDOMWriter.println("  lRule.identifier:" + lRule.identifier);
		prDOMWriter.println("  lRule.type:" + lRule.type);
		prDOMWriter.println("  lRule.xpath:" + lRule.xpath);
		prDOMWriter.println("  lRule.roleId:" + lRule.roleId);
		prDOMWriter.println("  lRule.attrTitle:" + lRule.attrTitle);
		prDOMWriter.println("  lRule.attrNameSpaceNC:" + lRule.attrNameSpaceNC);
		prDOMWriter.println("  lRule.classTitle:" + lRule.classTitle);
		prDOMWriter.println("  lRule.classNameSpaceNC:" + lRule.classNameSpaceNC);
		prDOMWriter.println("  lRule.classSteward:" + lRule.classSteward);
		prDOMWriter.println("  lRule.alwaysInclude:" + lRule.alwaysInclude);
		prDOMWriter.println("  lRule.isMissionOnly:" + lRule.isMissionOnly);

		prDOMWriter.println("-------------------------  Let Assignments - Pattern  -----------");
        if (lRule.letAssignPatternArr != null) {
    		for (Iterator <String> i = lRule.letAssignPatternArr.iterator(); i.hasNext();) {
    			String lLetAssignPattern = (String) i.next();
    			prDOMWriter.println("    lLetAssignPattern:" + lLetAssignPattern);
       		}
        }

        prDOMWriter.println("-------------------------  Let Assignments - Rule  --------------");
        if (lRule.letAssignArr != null) {
    		for (Iterator <String> i = lRule.letAssignArr.iterator(); i.hasNext();) {
    			String lLetAssign = (String) i.next();
    			prDOMWriter.println("    lLetAssign:" + lLetAssign);
       		}
        }
		
        prDOMWriter.println("-------------------------  Assert Statement  --------------------");
        if (lRule.assertArr != null) {
    		for (Iterator <DOMAssert> i = lRule.assertArr.iterator(); i.hasNext();) {
    			DOMAssert lAssert = (DOMAssert) i.next();
    			prDOMWriter.println("    lAssert.identifier:" + lAssert.identifier);
    			prDOMWriter.println("    lAssert.attrTitle:" + lAssert.attrTitle);
    			prDOMWriter.println("    lAssert.assertType:" + lAssert.assertType);
    			prDOMWriter.println("    lAssert.assertMsg:" + lAssert.assertMsg);
    			prDOMWriter.println("    lAssert.assertStmt:" + lAssert.assertStmt);
    			prDOMWriter.println("    lAssert.specMesg:" + lAssert.specMesg);
    			
    			prDOMWriter.println("-------------------------  Assert Statement Test Values  --------");
    	        if (lAssert.testValArr != null) {
        			for (Iterator <String> j = lAssert.testValArr.iterator(); j.hasNext();) {
        				String lTestValue = (String) j.next();
        				prDOMWriter.println("       lTestValue:" + lTestValue);
        	   		}	
    	        }
       		}
        }
	}
}
