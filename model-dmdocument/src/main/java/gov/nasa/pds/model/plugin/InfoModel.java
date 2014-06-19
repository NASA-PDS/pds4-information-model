package gov.nasa.pds.model.plugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
	static TreeMap <String, PDSObjDefn> masterMOFClassTitleMap;
	
	static ArrayList <String> fundamentalStructures;

	// global attributes
	static TreeMap <String, AttrDefn> masterMOFAttrMap;
	static TreeMap <String, AttrDefn> masterMOFAttrIdMap;
	static TreeMap <String, AttrDefn> masterMOFAttrTitleMap;
	static ArrayList <AttrDefn> masterMOFAttrArr;

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
	
	// global 11179 data dictionary
	static ArrayList <String> masterMetaAttribute;
	
	// global 11179 data dictionary
	static TreeMap <String, InstDefn> master11179DataDict;

	// global science discipline facet map 
//	static TreeMap <String, InstDefn> masterProtPinsUpperModel;
	static TreeMap <String, SFDisciplineFacetDefn> sfDisciplineFacetDefnMap = new TreeMap <String, SFDisciplineFacetDefn> ();

	// special rdfIdentifiers
	static String protegeRootClassRdfId, protegeSlotClassRdfId;
	
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
	
	static PDSObjDefn user;	// Root of all classes in this model
	
	// values and value meaning
	static TreeMap <String, PermValueDefn> masterValueMeaningMap;
	
	// sorting maps
	static TreeMap <String, Integer> masterAttrSortOrderMap;
	static TreeMap <String, Integer> masterClassSortOrderMap;
	
	static TreeMap <String, Integer> iLabelGroup;
	static TreeMap <String, Integer> classGroup;

	// class assert statements
//	static TreeMap <String, ArrayList<AssertDefn>> classAssertGroupMap;
//	static ArrayList <AssertDefn> classAssertArr;

	static TreeMap <String, RuleDefn> schematronRuleMap;
	
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
		dataTypePDS4ProtegeMap.put("ASCII_Boolean", "BOOLEAN");
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
		dataConcept.put("VOID", "Conceptually, the value space of the void datatype is empty, but a single nominal value is necessary to perform the ‘presence required’ function. - ISO/IEC 11404");
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
		
// =================== Class Sorting Maps ==========================================
		iLabelGroup = new TreeMap <String, Integer> ();
		
		// iLabelGroup is the master class sort order map by default
		masterClassSortOrderMap = iLabelGroup;
		
		iLabelGroup.put("Identification_Area", 1);
		iLabelGroup.put("Alias_List", 3);
		iLabelGroup.put("Citation_Information", 5);
		iLabelGroup.put("Modification_History", 7);
		iLabelGroup.put("Modification_Detail", 9);

		iLabelGroup.put("Observation_Area", 11);
		iLabelGroup.put("Subject_Area", 11);
		iLabelGroup.put("Context_Area", 11);
		iLabelGroup.put("Time_Coordinates", 13);
//		iLabelGroup.put("Primary_Result_Description", 15);
		iLabelGroup.put("Primary_Result_Summary", 15);
		iLabelGroup.put("Investigation_Area", 17);
		iLabelGroup.put("Observing_System", 19);
		iLabelGroup.put("Observing_System_Component", 21);
		iLabelGroup.put("Target_Identification", 23);
		iLabelGroup.put("Geometry", 25);
		iLabelGroup.put("Cartography", 27);
		iLabelGroup.put("Mission_Area", 29);
//		iLabelGroup.put("Node_Area", 31);
		iLabelGroup.put("Discipline_Area", 31);
//		iLabelGroup.put("Document_Format", 34);
//		iLabelGroup.put("Document_Description", 35);
//		iLabelGroup.put("Document_Format_Set", 36);
		
		iLabelGroup.put("Reference_List", 41);
		iLabelGroup.put("Internal_Reference", 42);
		iLabelGroup.put("External_Reference", 43);

		iLabelGroup.put("Collection", 50);
		iLabelGroup.put("Bundle", 50);
		iLabelGroup.put("Archive_Bundle", 50);
		iLabelGroup.put("Instrument", 51);
		iLabelGroup.put("Instrument_Host", 52);
		iLabelGroup.put("Investigation", 53);
		iLabelGroup.put("Target", 59);
		iLabelGroup.put("Document_Format", 51);
//		iLabelGroup.put("Document_Description", 52);
		iLabelGroup.put("Document", 52);
		iLabelGroup.put("Document_Format_Set", 53);
		iLabelGroup.put("Zip", 50);

		
		iLabelGroup.put("File_Area", 51);
		iLabelGroup.put("File_Area_Observational", 51);
		iLabelGroup.put("File_Area_Observational_Supplemental", 52);
		iLabelGroup.put("File_Area_Browse", 51);
		iLabelGroup.put("File_Area_Text", 51);
		iLabelGroup.put("File_Area_Encoded_Image", 51);
		iLabelGroup.put("File_Area_SPICE_Kernel", 51);
		iLabelGroup.put("File_Area_Manifest", 51);
		iLabelGroup.put("File_Area_Inventory", 51);
//		iLabelGroup.put("File_Area_Inventory_LIDVID", 51);
//		iLabelGroup.put("File_Area_Inventory_LID", 51);
		iLabelGroup.put("File_Area_XML_Schema", 51);
		iLabelGroup.put("File", 53);
		iLabelGroup.put("Document_File", 54);

//		iLabelGroup.put("Array_Element", 33);
//		iLabelGroup.put("Array_Axis", 34);
		iLabelGroup.put("Element_Array", 33);
		iLabelGroup.put("Axis_Array", 34);
		iLabelGroup.put("Special_Constants",41);
		iLabelGroup.put("Object_Statistics",42);
		
		iLabelGroup.put("Uniformly_Sampled",31);	
		iLabelGroup.put("Record_Binary",33);	
		iLabelGroup.put("Record_Character",33);	
		
		iLabelGroup.put("Field_Binary",34);	
		iLabelGroup.put("Field_Character",34);	
		iLabelGroup.put("Group_Field_Binary",35);	
		iLabelGroup.put("Group_Field_Character",35);	

		iLabelGroup.put("Software_Format_Set", 17);
		iLabelGroup.put("Software_Desc", 17);	
		iLabelGroup.put("Data_Set", 17);		
		
// =================== Attribute Sorting Maps ==========================================
		
		classGroup = new TreeMap <String, Integer> ();
		
		// classGroup is the master attribute sort order map by default
		masterAttrSortOrderMap = classGroup;

		classGroup.put("logical_identifier", 1);
		classGroup.put("name", 1);
		classGroup.put("version_id", 3);
		classGroup.put("ldd_version_id", 3);
		classGroup.put("revision_id", 3);
		classGroup.put("title", 5);
//		classGroup.put("name", 7);
		classGroup.put("doi", 7);
		classGroup.put("file_name", 7);
		classGroup.put("date_time", 7);				// needed for Update
		classGroup.put("directory_path_name", 8);
		classGroup.put("full_name", 8);
		classGroup.put("document_name", 8);
		classGroup.put("axis_name", 8);
		classGroup.put("lid_reference", 8);
		classGroup.put("lidvid_reference", 8);
		classGroup.put("file_specification_name", 9);
		classGroup.put("alternate_id", 9);
		classGroup.put("alternate_title", 11);
		classGroup.put("alternate_designation", 13);
		classGroup.put("local_identifier", 15);
		classGroup.put("starting_point_identifier", 15);
		
		classGroup.put("type", 17);
		classGroup.put("product_class", 19);
		classGroup.put("reference_type", 21);
		classGroup.put("format_type", 21);
		classGroup.put("bundle_type", 21);
		classGroup.put("collection_type", 21);
		classGroup.put("container_type", 21);

//		classGroup.put("observing_system_component_type", 23);
		classGroup.put("member_status", 21);
		classGroup.put("information_model_version", 13);

		classGroup.put("author_list", 21);
		classGroup.put("editor_list", 23);
		
		classGroup.put("purpose", 21);
		classGroup.put("data_regime", 23);
//		classGroup.put("reduction_level", 25);
		classGroup.put("processing_level_id", 25);
		
		classGroup.put("local_mean_solar_time", 9);
		classGroup.put("local_true_solar_time", 11);
		classGroup.put("solar_longitude", 13);
				
		classGroup.put("creation_date_time", 31);
		classGroup.put("modification_date",2);
		classGroup.put("publication_year", 33);
		
		classGroup.put("file_size", 33);
		classGroup.put("offset", 33);
		classGroup.put("records", 35);
		classGroup.put("object_length", 36);
		classGroup.put("record_length", 36);
		classGroup.put("record_delimiter", 37);
//		classGroup.put("maximum_record_bytes", 37);
		classGroup.put("maximum_record_length", 37);
		classGroup.put("fields", 38);
		classGroup.put("groups", 39);
		classGroup.put("bit_fields", 38);
		classGroup.put("md5_checksum", 39);
//		classGroup.put("external_standard_id", 41);
		classGroup.put("parsing_standard_id", 41);
		classGroup.put("encoding_standard_id", 41);
		classGroup.put("document_standard_id", 41);
//		classGroup.put("external_standard_version_id", 42);
		classGroup.put("kernel_type", 43);
		classGroup.put("spacecraft_clock_start_count", 44);
		classGroup.put("spacecraft_clock_stop_count", 45);
		classGroup.put("spacecraft_clock_count_partition", 46);
		classGroup.put("encoding_type", 45);	
				
		classGroup.put("repetitions", 31);	
		classGroup.put("group_location", 33);	
		classGroup.put("group_length", 39);	

		classGroup.put("axes", 35);	// must be after offset
		classGroup.put("axis_index_order", 37);
		classGroup.put("axis_storage_order", 39);
		
		classGroup.put("elements", 35);
		classGroup.put("unit", 39);
		
		classGroup.put("field_number", 21);
		classGroup.put("group_number", 21);
		classGroup.put("field_location", 22);
		classGroup.put("start_bit", 21);
		classGroup.put("stop_bit", 22);
		classGroup.put("data_type", 23);
		classGroup.put("field_length", 24);
		classGroup.put("maximum_field_length", 24);
		classGroup.put("bit_mask", 25);
		classGroup.put("field_format", 26);
//		classGroup.put("scaling_factor", 31);
//		classGroup.put("value_offset", 32);

//		classGroup.put("sample_bit_mask", 41);
		classGroup.put("scaling_factor", 43);
		classGroup.put("value_offset", 44);
		
		classGroup.put("display_direction", 31);
		classGroup.put("default_red", 32);
		classGroup.put("default_green", 33);
		classGroup.put("default_blue", 34);
		classGroup.put("frame_rate", 35);
		classGroup.put("continuous_loop_flag", 36);

		classGroup.put("sampling_parameter_name", 31);
		classGroup.put("sampling_parameter_interval", 32);
		classGroup.put("sampling_parameter_unit", 33);
		classGroup.put("first_sampling_parameter_value", 34);
		classGroup.put("last_sampling_parameter_value", 35);
		classGroup.put("sampling_parameter_scale", 36);	
		
		classGroup.put("saturated_constant", 31);
		classGroup.put("missing_constant", 32);
		classGroup.put("error_constant", 33);
		classGroup.put("invalid_constant", 34);
		classGroup.put("unknown_constant", 35);
		classGroup.put("not_applicable_constant", 36);
		
		classGroup.put("valid_maximum", 37);
		classGroup.put("high_instrument_saturation", 38);
		classGroup.put("high_representation_saturation", 39);
		classGroup.put("valid_minimum", 40);
		classGroup.put("low_instrument_saturation", 41);
		classGroup.put("low_representation_saturation", 42);
		
		classGroup.put("maximum", 21);
		classGroup.put("minimum", 22);
		classGroup.put("mean", 23);
		classGroup.put("standard_deviation", 24);
		classGroup.put("median", 25);
		classGroup.put("maximum_scaled_value", 40);
		classGroup.put("minimum_scaled_value", 41);

		classGroup.put("band_number", 11);
		classGroup.put("band_width", 12);
		classGroup.put("center_wavelength", 13);
		classGroup.put("detector_number", 14);
		classGroup.put("filter_number", 15);
		classGroup.put("grating_position", 16);
		classGroup.put("original_band", 17);
		// scaling - 31
		// std dev - 24
		// Value offset - 32
		
		classGroup.put("reference_text", 51);
		classGroup.put("acknowledgement_text", 51);
		classGroup.put("keywords", 53);
		classGroup.put("copyright", 53);
		classGroup.put("comment", 55);
//		classGroup.put("description", 57);
		classGroup.put("description", 97);

/*		classGroup.put("reference_text", 91);
		classGroup.put("acknowledgement_text", 91);
		classGroup.put("keywords", 93);
		classGroup.put("copyright", 93);
		classGroup.put("comment", 95);
		classGroup.put("description", 97); */

		classGroup.put("address", 51);
		classGroup.put("country", 52);

		classGroup.put("aperture", 51);
		classGroup.put("longitude", 52);
		classGroup.put("latitude", 53);
		classGroup.put("telescope_longitude", 52);
		classGroup.put("telescope_latitude", 53);
		classGroup.put("altitude", 54);
		classGroup.put("coordinate_source", 55);
	
		classGroup.put("reference_frame_id", 51);
		classGroup.put("x", 52);
		classGroup.put("y", 53);
		classGroup.put("z", 54);		
		classGroup.put("vector_components", 52);
		
		classGroup.put("data_set_id",3);
		classGroup.put("data_set_name", 4);
		classGroup.put("data_set_release_date", 5);
		classGroup.put("start_date_time", 6);
		classGroup.put("stop_date_time", 7);
		classGroup.put("start_date", 18);
		classGroup.put("stop_date", 19);
		classGroup.put("producer_full_name", 8);
		classGroup.put("citation_text", 9);
		classGroup.put("data_set_terse_desc", 10);
		classGroup.put("abstract_desc", 11);
		classGroup.put("data_set_desc", 12);
		classGroup.put("confidence_level_note", 13);
		classGroup.put("archive_status", 14);
		
		classGroup.put("dataset_name", 1);
		classGroup.put("mission_name", 2);
		classGroup.put("target_name", 3);
		classGroup.put("instrument_name", 4);
		classGroup.put("instrument_host_name", 5);
		classGroup.put("node_name", 6);
		classGroup.put("document_name", 7);
		
		classGroup.put("registration_date", 11);
		classGroup.put("electronic_mail_address", 12);
		classGroup.put("sort_name", 13);
		
		classGroup.put("enumeration_flag", 10);
		classGroup.put("class_name", 12);
		classGroup.put("steward_id", 16);
//		classGroup.put("name_space_id", 18);
		classGroup.put("namespace_id", 18);
		classGroup.put("nillable_flag", 20);
//		classGroup.put("submitter_id", 22);
		classGroup.put("submitter_name", 22);
		classGroup.put("data_element_concept", 24);
		classGroup.put("designation", 26);
		classGroup.put("definition", 28);
		classGroup.put("language", 30);
		classGroup.put("registered_by", 56);
		classGroup.put("registration_authority_id", 57);
		classGroup.put("abstract_flag", 58);
//		classGroup.put("choice_flag", 59);
		
		classGroup.put("local_attribute_id", 1);
//		classGroup.put("name", 2);
		classGroup.put("value_data_type", 32);
		classGroup.put("formation_rule", 34);
		classGroup.put("minimum_characters", 36);
		classGroup.put("maximum_characters", 38);
		classGroup.put("minimum_value", 40);
		classGroup.put("minimum_value_exclusive_flag", 41);
		classGroup.put("maximum_value", 42);
		classGroup.put("maximum_value_exclusive_flag", 43);
		classGroup.put("minimum_occurrences", 40);
		classGroup.put("maximum_occurrences", 42);
		classGroup.put("pattern", 43);
		classGroup.put("unit_of_measure_type", 44);
		classGroup.put("conceptual_domain", 46);
		
		classGroup.put("value", 48);
		classGroup.put("value_meaning", 50);
		classGroup.put("value_begin_date", 52);
		classGroup.put("value_end_date", 54);
		
		// Schematron Patterns, Rules, and Assert statements
		
//		classAssertGroupMap = new TreeMap <String, ArrayList<AssertDefn>> ();
		schematronRuleMap = new TreeMap <String, RuleDefn> ();
		RuleDefn lRule;
		AssertDefn2 lAssert;
		
		/*		lRule = new RuleDefn("pds:Delivery_Manifest/pds:Record_Character/pds:Field_Character[1]");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Delivery_Manifest/pds:Record_Character/pds:Field_Character[1]";
		lRule.attrTitle = "Field_Character";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Record_Character";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("Delivery_Manifest");
		lAssert.assertStmt = "pds:field_number eq '1'";		
		lAssert.assertMsg = "The first field of a Delivery Manifest must have field_number set to 1.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("Delivery_Manifest");
		lAssert.assertStmt = "pds:field_location eq '1'";	
		lAssert.assertMsg = "The first field of a Delivery Manifest must have field_location set to 1.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("Delivery_Manifest");
		lAssert.assertStmt = "pds:data_type eq 'ASCII_MD5_Checksum'";	
		lAssert.assertMsg = "The first field of a Delivery Manifest must have data_type set to ASCII_MD5_Checksum.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("Delivery_Manifest");
		lAssert.assertStmt = "pds:name eq 'MD5_Checksum'";
		lAssert.assertMsg = "The first field of a Delivery Manifest must have name set to MD5_Checksum.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Delivery_Manifest/pds:Record_Character/pds:Field_Character[2]");		
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Delivery_Manifest/pds:Record_Character/pds:Field_Character[2]";
		lRule.attrTitle = "Field_Character";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Record_Character";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:field_number eq '2'";	
		lAssert.assertMsg = "The second field of a Delivery Manifest must have field_number set to 2.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:field_location eq '17'";
		lAssert.assertMsg = "The second field of a Delivery Manifest must have field_location set to 17.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:data_type eq 'ASCII_File_Specification_Name'";
		lAssert.assertMsg = "The second field of a Delivery Manifest must have data_type set to ASCII_File_Specification_Name.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:name eq 'File_Specification_Name'";	
		lAssert.assertMsg = "The second field of a Delivery Manifest must have name set to File_Specification_Name.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Delivery_Manifest");		
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Delivery_Manifest";
		lRule.attrTitle = "offset";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Delivery_Manifest";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:offset eq '0'";		
		lAssert.assertMsg = "The offset for a Delivery Manifest must be set to 0.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "pds:encoding_type eq 'CHARACTER'";
		lAssert.assertMsg = "The encoding_type for a Delivery Manifest must be set to CHARACTER.";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_Delivery_Manifest/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Delivery_Manifest/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("delivery_manifest_to_collection");				
		lAssert.testValArr.add("delivery_manifest_to_bundle");				
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);		
		
*/		
		
	
		lRule = new RuleDefn("pds:XML_Schema");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:XML_Schema";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "XML_Schema";		
		lRule.classNameSpaceNC = "pds";
		lAssert = new AssertDefn2 ("offset");
		lAssert.assertStmt = "pds:offset eq '0'";		
		lAssert.assertMsg = "XML_Schema.offset must have a value of '0'";
		lAssert.specMesg = "XML_Schema.offset must have a value of '0'";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Inventory");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Inventory";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Inventory";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "((pds:reference_type eq 'inventory_has_member_product') and (count(pds:Record_Delimited/pds:Field_Delimited) eq 2))";		
		lAssert.assertMsg = "Inventory.Field_Delimited does not match the expected number of instances";
		lAssert.specMesg = "Inventory.Field_Delimited does not match the expected number of instances";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("offset");
		lAssert.assertStmt = "pds:offset eq '0'";		
		lAssert.assertMsg = "Inventory.offset must have a value of '0'";
		lAssert.specMesg = "Inventory.offset must have a value of '0'";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]";
		lRule.attrTitle = "Field_Delimited";		
//		lRule.attrTitle = "field_number";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Record_Delimited";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("field_number");
		lAssert.assertStmt = "pds:field_number eq '2'";		
		lAssert.assertMsg = "The second field of an Inventory must have field_number set to 2.";
		lAssert.specMesg = "The second field of an Inventory must have field_number set to 2.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("data_type");
		lAssert.assertStmt = "(pds:data_type eq 'ASCII_LIDVID_LID')";	
		lAssert.assertMsg = "The second field of an Inventory must have data_type set to 'ASCII_LIDVID_LID'.";
		lAssert.specMesg = "The second field of an Inventory must have data_type set to 'ASCII_LIDVID_LID'.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("name");
		lAssert.assertStmt = "(pds:name eq 'LIDVID_LID')";	
		lAssert.assertMsg = "The second field of an Inventory must have name set to 'LIDVID_LID'.";
		lAssert.specMesg = "The second field of an Inventory must have name set to 'LIDVID_LID'.";
		lRule.assertArr.add(lAssert);		
		
		lRule = new RuleDefn("pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]";
		lRule.attrTitle = "Field_Delimited";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Record_Delimited";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("field_number");
		lAssert.assertStmt = "pds:field_number eq '1'";	
		lAssert.assertMsg = "The first field of an Inventory must have field_number set to 1.";
		lAssert.specMesg = "The first field of an Inventory must have field_number set to 1.";
		lRule.assertArr.add(lAssert);	
		lAssert = new AssertDefn2 ("maximum_field_length");
		lAssert.assertStmt = "pds:maximum_field_length eq '1'";	
		lAssert.assertMsg = "The first field of an Inventory must have maximum_field_length set to 1.";
		lAssert.specMesg = "The first field of an Inventory must have maximum_field_length set to 1.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("data_type");
		lAssert.assertStmt = "pds:data_type eq 'ASCII_String'";	
		lAssert.assertMsg = "The first field of an Inventory must have data type set to 'ASCII_String'.";
		lAssert.specMesg = "The first field of an Inventory must have data type set to 'ASCII_String'.";
		lRule.assertArr.add(lAssert);	
		lAssert = new AssertDefn2 ("name");
		lAssert.assertStmt = "pds:name eq 'Member_Status'";	
		lAssert.assertMsg = "The first field of an Inventory must have name set to 'Member_Status'.";
		lAssert.specMesg = "The first field of an Inventory must have name set to 'Member_Status'.";
		lRule.assertArr.add(lAssert);	

		lRule = new RuleDefn("pds:Array_2D_Image/pds:Axis_Array[1]");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Array_2D_Image/pds:Axis_Array[1]";
		lRule.attrTitle = "axis_name";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Axis_Array";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("axis_name");
		lAssert.assertStmt = "pds:axis_name = ('Line', 'Sample')";	
		lAssert.assertMsg = "The name of the first axis of an Array_2D_Image must be set to either Line or Sample.";
		lAssert.specMesg = "The name of the first axis of an Array_2D_Image must be set to either Line or Sample.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("sequence_number");
		lAssert.assertStmt = "pds:sequence_number eq '1'";
		lAssert.assertMsg = "The sequence number of the first axis of an Array_2D_Image must be set to 1.";
		lAssert.specMesg = "The sequence number of the first axis of an Array_2D_Image must be set to 1.";
		lRule.assertArr.add(lAssert);	
		
		lRule = new RuleDefn("pds:Array_2D_Image/pds:Axis_Array[2]");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Array_2D_Image/pds:Axis_Array[2]";
		lRule.attrTitle = "axis_name";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Axis_Array";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("axis_name");
		lAssert.assertStmt = "pds:axis_name = ('Line', 'Sample')";	
		lAssert.assertMsg = "The name of the second axis of an Array_2D_Image must be set to either Line or Sample.";
		lAssert.specMesg = "The name of the second axis of an Array_2D_Image must be set to either Line or Sample.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("sequence_number");
		lAssert.assertStmt = "pds:sequence_number eq '2'";		
		lAssert.assertMsg = "The sequence number of the second axis of an Array_2D_Image must be set to 2.";
		lAssert.specMesg = "The sequence number of the second axis of an Array_2D_Image must be set to 2.";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Array_3D_Spectrum/pds:Axis_Array");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Array_3D_Spectrum/pds:Axis_Array";
		lRule.attrTitle = "axis_name";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Axis_Array";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("axis_name");
		lAssert.assertStmt = "(pds:axis_name = 'Band' and pds:Band_Bin_Set) or pds:axis_name != 'Band'";	
		lAssert.assertMsg = "In an Array_3D_Spectrum, if the axis_name is 'Band', then the Band_Bin_Set class must be present.";
		lAssert.specMesg = "In an Array_3D_Spectrum, if the axis_name is 'Band', then the Band_Bin_Set class must be present.";
		lRule.assertArr.add(lAssert);
				
/*		lRule = new RuleDefn("pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert); */

/*			
		lRule = new RuleDefn("pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Internal_Reference";
		lRule.attrTitle = "lidvid_reference";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"lid_num_colons\" value=\"string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lidvid_num_colons\" value=\"string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lid_min_required_colons\" value=\"3\"");
		lRule.letAssignArr.add("name=\"lid_max_required_colons\" value=\"5\"");
		lRule.letAssignArr.add("name=\"lidvid_min_required_colons\" value=\"5\"");
		lRule.letAssignArr.add("name=\"lidvid_max_required_colons\" value=\"7\"");
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then (($lid_num_colons &gt;= $lid_min_required_colons) and ($lid_num_colons &lt;= $lid_max_required_colons)) else true()";					
		lAssert.assertMsg = "The number of colons found in lid_reference: (<sch:value-of select=\"$lid_num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$lid_min_required_colons\"/>:<sch:value-of select=\"$lid_max_required_colons\"/>).";
		lAssert.specMesg = "The number of colons found in lid_reference is validated.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then (($lidvid_num_colons &gt;= $lidvid_min_required_colons) and ($lidvid_num_colons &lt;= $lidvid_max_required_colons)) else true()";					
		lAssert.assertMsg = "The number of colons found in lidvid_reference: (<sch:value-of select=\"$lidvid_num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$lidvid_min_required_colons\"/>:<sch:value-of select=\"$lidvid_max_required_colons\"/>).";
		lAssert.specMesg = "The number of colons found in lidvid_reference is validated.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lAssert.specMesg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lRule.assertArr.add(lAssert);
		
*/
		
		lRule = new RuleDefn("pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Internal_Reference";
		lRule.attrTitle = "lidvid_reference";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"lid_num_colons\" value=\"string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lidvid_num_colons\" value=\"string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lid_min_required_colons\" value=\"3\"");
		lRule.letAssignArr.add("name=\"lid_max_required_colons\" value=\"5\"");
		lRule.letAssignArr.add("name=\"lidvid_min_required_colons\" value=\"5\"");
		lRule.letAssignArr.add("name=\"lidvid_max_required_colons\" value=\"7\"");
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must not include a value that contains '::' followed by version id";
		lAssert.specMesg = "The value of the attribute lid_reference must not include a value that contains '::' followed by version id";
		lRule.assertArr.add(lAssert);		
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then (($lid_num_colons &gt;= $lid_min_required_colons) and ($lid_num_colons &lt;= $lid_max_required_colons)) else true()";					
		lAssert.assertMsg = "The number of colons found in lid_reference: (<sch:value-of select=\"$lid_num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$lid_min_required_colons\"/>:<sch:value-of select=\"$lid_max_required_colons\"/>).";
		lAssert.specMesg = "The number of colons found in lid_reference is validated.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then (($lidvid_num_colons &gt;= $lidvid_min_required_colons) and ($lidvid_num_colons &lt;= $lidvid_max_required_colons)) else true()";					
		lAssert.assertMsg = "The number of colons found in lidvid_reference: (<sch:value-of select=\"$lidvid_num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$lidvid_min_required_colons\"/>:<sch:value-of select=\"$lidvid_max_required_colons\"/>).";
		lAssert.specMesg = "The number of colons found in lidvid_reference is validated.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lAssert.specMesg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_Bundle/pds:Identification_Area/pds:logical_identifier");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Bundle/pds:Identification_Area/pds:logical_identifier";
		lRule.attrTitle = "logical_identifier";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Identification_Area";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"num_colons\" value=\"string-length(.) - string-length(translate(., ':', ''))\"");
		lRule.letAssignArr.add("name=\"required_colons\" value=\"3\"");
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "$num_colons eq $required_colons";					
		lAssert.assertMsg = "In Product_Bundle, the number of colons found: (<sch:value-of select=\"$num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$required_colons\"/>).";
		lAssert.specMesg = "In Product_Bundle the number of colons in logical_identifier is valid.";
		lRule.assertArr.add(lAssert);
		
		
//=================Bundle_Member_Entry====================
		lRule = new RuleDefn("pds:Bundle_Member_Entry");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Bundle_Member_Entry";
		lRule.attrTitle = "lidvid_reference";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"lid_num_colons\" value=\"string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lidvid_num_colons\" value=\"string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))\"");
		lRule.letAssignArr.add("name=\"lid_required_colons\" value=\"4\"");
		lRule.letAssignArr.add("name=\"lidvid_required_colons\" value=\"6\"");
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then ($lid_num_colons eq $lid_required_colons) else true()";					
		lAssert.assertMsg = "The number of colons found in lid_reference: (<sch:value-of select=\"$lid_num_colons\"/>) is inconsistent with the number expected: <sch:value-of select=\"$lid_required_colons\"/>.";
		lAssert.specMesg = "The number of colons found in the lid_reference is valid.";
		lRule.assertArr.add(lAssert);		
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then ($lidvid_num_colons eq $lidvid_required_colons) else true()";					
		lAssert.assertMsg = "The number of colons found in lidvid_reference: (<sch:value-of select=\"$lidvid_num_colons\"/>) is inconsistent with the number expected: <sch:value-of select=\"$lidvid_required_colons\"/>.";
		lAssert.specMesg = "The number of colons found in the lidvid_reference is valid.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lid_reference");
		lAssert.assertStmt = "if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()";					
		lAssert.assertMsg = "The value of the attribute lid_reference must not include a value that contains '::' followed by version id";
		lAssert.specMesg = "The value of the attribute lid_reference must not include a value that contains '::' followed by version id";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("lidvid_reference");
		lAssert.assertStmt = "if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()";					
		lAssert.assertMsg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lAssert.specMesg = "The value of the attribute lidvid_reference must include a value that contains '::' followed by version id";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_Collection/pds:Identification_Area/pds:logical_identifier");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Collection/pds:Identification_Area/pds:logical_identifier";
		lRule.attrTitle = "logical_identifier";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Identification_Area";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"num_colons\" value=\"string-length(.) - string-length(translate(., ':', ''))\"");
		lRule.letAssignArr.add("name=\"required_colons\" value=\"4\"");
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "$num_colons eq $required_colons";					
		lAssert.assertMsg = "In Product_Collection, the number of colons found: (<sch:value-of select=\"$num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$required_colons\"/>).";
		lAssert.specMesg = "In Product_Collection, the number of colons found in logical identifier is validated.";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_Bundle/pds:Identification_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Bundle/pds:Identification_Area";
		lRule.attrTitle = "description";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Citation_Information";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lAssert = new AssertDefn2 ("description");
		lAssert.assertStmt = "pds:Citation_Information/pds:description";					
		lAssert.assertMsg = "In Product_Bundle a description is required in Citation_Information.";
		lAssert.specMesg = "In Product_Bundle a description is required in Citation_Information.";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_Collection/pds:Identification_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Collection/pds:Identification_Area";
		lRule.attrTitle = "description";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Citation_Information";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lAssert = new AssertDefn2 ("description");
		lAssert.assertStmt = "pds:Citation_Information/pds:description";					
		lAssert.assertMsg = "In Product_Collection a description is required in Citation_Information.";
		lAssert.specMesg = "In Product_Collection a description is required in Citation_Information.";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_Document/pds:Identification_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Document/pds:Identification_Area";
		lRule.attrTitle = "description";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Citation_Information";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lAssert = new AssertDefn2 ("description");
		lAssert.assertStmt = "pds:Citation_Information/pds:description";					
		lAssert.assertMsg = "In Product_Document a description is required in Citation_Information.";
		lAssert.specMesg = "In Product_Document a description is required in Citation_Information.";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_File_Text/pds:Identification_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_File_Text/pds:Identification_Area";
		lRule.attrTitle = "description";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Citation_Information";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lAssert = new AssertDefn2 ("description");
		lAssert.assertStmt = "pds:Citation_Information/pds:description";					
		lAssert.assertMsg = "In Product_File_Text a description is required in Citation_Information.";
		lAssert.specMesg = "In Product_File_Text a description is required in Citation_Information.";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:/Product_Observational/pds:Identification_Area/pds:logical_identifier");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "/*";
		lRule.attrTitle = "logical_identifier";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Identification_Area";		
		lRule.classNameSpaceNC = "pds";
		lRule.alwaysInclude = true;
		lRule.letAssignArr.add("name=\"num_colons\" value=\"string-length(./pds:Identification_Area/pds:logical_identifier) - string-length(translate(./pds:Identification_Area/pds:logical_identifier, ':', ''))\"");
		lRule.letAssignArr.add("name=\"required_colons\" value=\"5\"");
		lRule.letAssignArr.add("name=\"product_name\" value=\"Product_Observational\"");
		lAssert = new AssertDefn2 ("logical_identifier");
//		lAssert.assertStmt = "if ((not (contains(name(), 'Bundle'))) and (not (contains(name(), 'Collection')))) then $num_colons eq $required_colons else true()";					
		lAssert.assertStmt = "if ((not (contains(name(), 'Ingest'))) and (not (contains(name(), 'Bundle'))) and (not (contains(name(), 'Collection')))) then $num_colons eq $required_colons else true()";					
		lAssert.assertMsg = "In <sch:value-of select=\"name()\"/>, the number of colons found: (<sch:value-of select=\"$num_colons\"/>) is inconsistent with the number expected: (<sch:value-of select=\"$required_colons\"/>).";
		lAssert.specMesg = "In the number of colons found in logical_identifier is validated.";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Observing_System_Component/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Observing_System_Component/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_instrument', 'has_instrument_host')";		
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("is_instrument");		
		lAssert.testValArr.add("is_instrument_host");	
		lAssert.testValArr.add("is_other");	
		lAssert.testValArr.add("is_facility");	
		lAssert.testValArr.add("is_telescope");	
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

/*		lRule = new RuleDefn("pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_instrument', 'has_instrument_host')";		
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("data_to_investigation");		
		lAssert.testValArr.add("collection_to_investigation");		
		lAssert.testValArr.add("bundle_to_investigation");
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert); */
		
		lRule = new RuleDefn("pds:Target_Identification/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Target_Identification/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_instrument', 'has_instrument_host')";		
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("data_to_target");		
		lAssert.testValArr.add("collection_to_target");		
		lAssert.testValArr.add("bundle_to_target");
		lAssert.testValArr.add("document_to_target");
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Update_Entry/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Update_Entry/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_instrument', 'has_instrument_host')";		
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("data_to_update");		
		lAssert.testValArr.add("collection_to_update");		
		lAssert.testValArr.add("bundle_to_update");
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		/*
		lRule = new RuleDefn("pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "if (pds:reference_type) then pds:reference_type = 'has_investigation' else true()";		
		lAssert.assertType = "IF";		
		lAssert.testValArr.add("has_investigation");			
		lAssert.assertMsg = " must be set to ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Target_Identification/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Target_Identification/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "if (pds:reference_type) then pds:reference_type = 'has_target' else true()";		
		lAssert.assertType = "IF";		
		lAssert.testValArr.add("has_target");			
		lAssert.assertMsg = " must be set to ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Update_Entry/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Update_Entry/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";	
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "if (pds:reference_type) then pds:reference_type = 'has_update' else true()";		
		lAssert.assertType = "IF";		
		lAssert.testValArr.add("has_update");			
		lAssert.assertMsg = " must be set to ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		*/
	
		lRule = new RuleDefn("pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('bundle_to_errata')";		
		lAssert.assertType = "EVERY";						
		lAssert.testValArr.add("bundle_to_errata");		
		lAssert.testValArr.add("bundle_to_document");		
		lAssert.testValArr.add("bundle_to_investigation");		
		lAssert.testValArr.add("bundle_to_instrument");		
		lAssert.testValArr.add("bundle_to_instrument_host");		
		lAssert.testValArr.add("bundle_to_target");		
		lAssert.testValArr.add("bundle_to_associate");
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
						
		lRule = new RuleDefn("pds:Product_Collection/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Collection/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('data_collection_to_resource', 'data_collection_to_associate', 'data_collection_to_calibration', 'data_collection_to_geometry', 'data_collection_to_spice_kernel', 'data_collection_curated_by_node', 'data_collection_to_document')";		
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("collection_to_resource");		
		lAssert.testValArr.add("collection_to_associate");		
		lAssert.testValArr.add("collection_to_calibration");		
		lAssert.testValArr.add("collection_to_geometry");		
		lAssert.testValArr.add("collection_to_spice_kernel");		
		lAssert.testValArr.add("collection_curated_by_node");			
		lAssert.testValArr.add("collection_to_document");		
		lAssert.testValArr.add("collection_to_browse");		
		lAssert.testValArr.add("collection_to_context");		
		lAssert.testValArr.add("collection_to_data");		
//		lAssert.testValArr.add("collection_to_document");		
		lAssert.testValArr.add("collection_to_schema");		
		lAssert.testValArr.add("collection_to_errata");		
		lAssert.testValArr.add("collection_to_bundle");		
		lAssert.testValArr.add("collection_to_personnel");		
		lAssert.testValArr.add("collection_to_investigation");		
		lAssert.testValArr.add("collection_to_instrument");		
		lAssert.testValArr.add("collection_to_instrument_host");		
		lAssert.testValArr.add("collection_to_target");		
		lAssert.testValArr.add("collection_to_associate");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Observational/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Observational/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('data_to_resource', 'data_to_calibration', 'data_to_geometry', 'data_to_spice_kernel', 'data_to_thumbnail', 'data_to_document', 'data_to_browse', 'bundle_to_document', 'collection_to_document')";						
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("data_to_resource");		
		lAssert.testValArr.add("data_to_calibration_document");	
		lAssert.testValArr.add("data_to_calibration_product");	
		lAssert.testValArr.add("data_to_raw_product");	
		lAssert.testValArr.add("data_to_calibrated_product");		
		lAssert.testValArr.add("data_to_geometry");		
		lAssert.testValArr.add("data_to_spice_kernel");		
		lAssert.testValArr.add("data_to_thumbnail");		
		lAssert.testValArr.add("data_to_document");		
		lAssert.testValArr.add("data_curated_by_node");		
		lAssert.testValArr.add("data_to_browse");		
		lAssert.testValArr.add("data_to_ancillary_data");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_SPICE_Kernel/pds:Context_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_SPICE_Kernel/pds:Context_Area";
		lRule.attrTitle = "TBD_Attribute";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Context_Area";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("x");
		lAssert.assertStmt = "(pds:Time_Coordinates and pds:Investigation_Area and pds:Target_Identification and pds:Observing_System)";							
		lAssert.assertMsg = "In Product_SPICE_Kernel the Time_Coordinates, Investigation_Area, Target_Identification, and Observing_System classes must be present";
		lAssert.specMesg = "In Product_SPICE_Kernel the Time_Coordinates, Investigation_Area, Target_Identification, and Observing_System classes must be present";
		lRule.assertArr.add(lAssert);		
		
		lRule = new RuleDefn("pds:Product_Context/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Context/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_resource', 'has_association', 'has_document', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";
		lAssert.assertType = "EVERY";		
//		lAssert.testValArr.add("context_to_resource");		
		lAssert.testValArr.add("context_to_associate");		
//		lAssert.testValArr.add("context_to_document");		
//		lAssert.testValArr.add("context_to_collection");		
//		lAssert.testValArr.add("context_to_bundle");		
		lAssert.testValArr.add("instrument_host_to_investigation");		
		lAssert.testValArr.add("instrument_host_to_document");		
		lAssert.testValArr.add("instrument_host_to_target");		
		lAssert.testValArr.add("instrument_to_instrument_host");		
		lAssert.testValArr.add("instrument_to_document");		
		lAssert.testValArr.add("investigation_to_target");		
		lAssert.testValArr.add("investigation_to_document");		
		lAssert.testValArr.add("node_to_personnel");		
		lAssert.testValArr.add("node_to_agency");		
		lAssert.testValArr.add("node_to_manager");		
		lAssert.testValArr.add("node_to_operator");		
		lAssert.testValArr.add("node_to_data_archivist");		
		lAssert.testValArr.add("resource_to_instrument");		
		lAssert.testValArr.add("resource_to_instrument_host");		
		lAssert.testValArr.add("resource_to_investigation");		
		lAssert.testValArr.add("resource_to_target");		
		lAssert.testValArr.add("target_to_document");		
		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);	
				
		lRule = new RuleDefn("pds:Product_Document/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Document/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("document_to_associate");		
		lAssert.testValArr.add("document_to_investigation");		
		lAssert.testValArr.add("document_to_instrument_host");		
		lAssert.testValArr.add("document_to_instrument");		
		lAssert.testValArr.add("document_to_target");
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("data_to_investigation");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("collection_to_investigation");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("bundle_to_investigation");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("document_to_investigation");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_association', 'has_investigation', 'has_instrument_host', 'has_instrument', 'has_target')";	
		lAssert.assertType = "EVERY";				
		lAssert.testValArr.add("document_to_target");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
				
		lRule = new RuleDefn("pds:Product_Browse/pds:Reference_List/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Browse/pds:Reference_List/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('has_resource', 'has_association', 'has_document')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("browse_to_data");		
		lAssert.testValArr.add("browse_to_thumbnail");				
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("package_has_collection");				
		lAssert.testValArr.add("package_has_bundle");				
		lAssert.testValArr.add("package_has_product");				
		lAssert.testValArr.add("package_compiled_from_package");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("package_has_collection");				
		lAssert.testValArr.add("package_has_bundle");				
		lAssert.testValArr.add("package_has_product");				
		lAssert.testValArr.add("package_compiled_from_package");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("package_has_collection");				
		lAssert.testValArr.add("package_has_bundle");				
		lAssert.testValArr.add("package_has_product");				
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("package_has_collection");				
		lAssert.testValArr.add("package_has_bundle");				
		lAssert.testValArr.add("package_has_product");		
		lAssert.testValArr.add("package_compiled_from_package");		
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);

		lRule = new RuleDefn("pds:Product_Zipped/pds:Internal_Reference");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Product_Zipped/pds:Internal_Reference";
		lRule.attrTitle = "reference_type";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Internal_Reference";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("reference_type");
		lAssert.assertStmt = "every $ref in (pds:reference_type) satisfies $ref = ('xxx')";			
		lAssert.assertType = "EVERY";		
		lAssert.testValArr.add("zip_to_package");							
		lAssert.assertMsg = " must be set to one of the following values ";
		lAssert.specMesg = "TBD";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Citation_Information/pds:description");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "//pds:Citation_Information/pds:description";
		lRule.attrTitle = "description";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Citation_Information";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("description");
		lAssert.assertStmt = "string-length(translate(., ' ', '')) &gt;= 1 and string-length(translate(., ' ','')) &lt;= 5000";		
		lAssert.assertMsg = "The description in Citation_Information must be greater than 1 and less than 5000 bytes (not counting spaces).";
		lAssert.specMesg = "The description in Citation_Information must be greater than 1 and less than 5000 bytes (not counting spaces).";
		lRule.assertArr.add(lAssert);		
		lRule = new RuleDefn("pds:Identification_Area");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "pds:Identification_Area";
		lRule.attrTitle = "logical_identifier";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Identification_Area";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "pds:product_class = local-name(/*)";	
		lAssert.assertMsg = "The attribute pds:product_class must match parent product class of '<sch:value-of select=\"local-name(/*)\" />'.";
		lAssert.specMesg = "The attribute pds:product_class must match parent product class name.";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "pds:logical_identifier eq lower-case(pds:logical_identifier)";	
		lAssert.assertMsg = "The value of the attribute logical_identifier must only contain lower-case letters'";
		lAssert.specMesg = "The value of the attribute logical_identifier must only contain lower-case letters'";
		lRule.assertArr.add(lAssert);
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "if (pds:logical_identifier) then starts-with(pds:logical_identifier,'urn:nasa:pds:') else true()";	
		lAssert.assertMsg = "The value of the attribute logical_identifier must start with 'urn:nasa:pds:'";
		lAssert.specMesg = "The value of the attribute logical_identifier must start with 'urn:nasa:pds:'";
		lRule.assertArr.add(lAssert);	
		lAssert = new AssertDefn2 ("logical_identifier");
		lAssert.assertStmt = "if (pds:logical_identifier) then not(contains(pds:logical_identifier,'::')) else true()";	
		lAssert.assertMsg = "The value of the attribute logical_identifier must not include a value that contains '::'";
		lAssert.specMesg = "The value of the attribute logical_identifier must not include a value that contains '::'";
		lRule.assertArr.add(lAssert);
		
		lRule = new RuleDefn("pds:Identification_Area/pds:product_class");
		schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = "/*";
		lRule.attrTitle = "product_class";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Identification_Area";		
		lRule.classNameSpaceNC = "pds";		
		lAssert = new AssertDefn2 ("product_class");
		lAssert.assertStmt = "name() = ('Product_Attribute_Definition','Product_Browse', 'Product_Bundle', 'Product_Class_Definition',  'Product_Collection', 'Product_Context', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Observational', 'Product_Service', 'Product_Software', 'Product_SPICE_Kernel', 'Product_Thumbnail', 'Product_Update', 'Product_XML_Schema', 'Product_Zipped','Product_Data_Set_PDS3', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3','Product_Mission_PDS3', 'Product_Proxy_PDS3', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_AIP', 'Product_DIP', 'Product_SIP', 'Product_DIP_Deep_Archive', 'Ingest_LDD')";	
		lAssert.assertMsg = "The ROOT element must be one of the allowed types.";
		lAssert.specMesg = "The ROOT element must be one of the allowed types.";
		lRule.assertArr.add(lAssert);	

// 333
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

//	=======================  Utilities ================================================================
	
	/**
	* escape certain characters - LaTEX
	*/

	static String escapeLaTEXChar (String aString) {
		String lString = aString;
		lString = DMDocument.replaceString (lString, "\\", "\\\\");
		return lString;
	}	
	
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
		ls1 = replaceString (ls1, "'", "%47");		
		ls1 = replaceString (ls1, "\\", "%5C");		
		return ls1;
	}
	
	/**
	* unescape pattern characters from protege files
	*/
	static String unEscapeProtegeString (String s1) {
		String ls1 = s1;
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
		return ls1;
	}
	
	/**
	* escape pattern characters for protege files 
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
		return ls1;
	}
	
	/**
	* unescape pattern characters from protege files
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
		lString = replaceString (lString, "&", "&amp;");
		lString = replaceString (lString, "<", "&lt;");
		lString = replaceString (lString, ">", "&gt;");
		lString = replaceString (lString, "\"", "&quot;");
		lString = replaceString (lString, "'", "&apos;");
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

//		System.out.println("debug getAllAttrRecurse class: lClass.title:" + lClass.title);
		
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
//			if (! lAttrArr.contains(lAssoc)) {
//				lAttrArr.add(lAssoc);
//			}
			ArrayList <String> lAssocClassTitleArr = lAssoc.valArr; 
			for (Iterator<String> j = lAssocClassTitleArr.iterator(); j.hasNext();) {
				String lAssocClassTitle = j.next();
				PDSObjDefn nlClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lAssocClassTitle);
				if (nlClass != null) {
					if (! visitedClass.contains(nlClass)) {
						visitedClass.add(nlClass);
						getAllAttrRecurse (lAttrArr, visitedClass, nlClass);
					}
				}
			}
		}
		
		//get all inherited associations
		for (Iterator<AttrDefn> i = lClass.inheritedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) i.next();
//			if (! lAttrArr.contains(lAssoc)) {
//				lAttrArr.add(lAssoc);
//			}
			ArrayList <String> lAssocClassTitleArr = lAssoc.valArr; 
			for (Iterator<String> j = lAssocClassTitleArr.iterator(); j.hasNext();) {
				String lAssocClassTitle = j.next();
				PDSObjDefn nlClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lAssocClassTitle);
				if (nlClass != null) {
					if (! visitedClass.contains(nlClass)) {
						visitedClass.add(nlClass);
						getAllAttrRecurse (lAttrArr, visitedClass, nlClass);
					}
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
	
//======================= Master Sorts =============================================================================
	
	// 8889
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
		ArrayList <AttrDefn> lAssocArr  = new ArrayList <AttrDefn> ();
		lAssocArr.addAll(lClass.ownedAssociation);
		lAssocArr.addAll(lClass.inheritedAssociation);
		for (Iterator<AttrDefn> i = lAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.valArr.isEmpty()) continue;
			for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
				String lTitle = (String) j.next();
				PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
				if (lClassMember == null) continue; ;
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
//			if (lAssoc == null) continue;
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
	public ArrayList <PDSObjDefn> getSortedAssocClasses_actual_classes (ArrayList<AssocDefn> lAssocArr) {
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
		if (objClass == null) {
		    System.out.println("\ndebug Class Definition - id:" + lInt + " -" + "NOT FOUND");
			return;
		}
	    System.out.println("\ndebug Class Definition - id:" + lInt + " - title:" + objClass.title);
	    System.out.println("  rdfIdentifier:" + objClass.rdfIdentifier);
		System.out.println("  identifier:" + objClass.identifier);
		System.out.println("  title:" + objClass.title);
		System.out.println("  registrationStatus:" + objClass.registrationStatus);
		System.out.println("  regAuthId:" + objClass.regAuthId);
		System.out.println("  steward:" + objClass.steward);
		System.out.println("  nameSpaceId:" + objClass.nameSpaceId);
		System.out.println("  nameSpaceIdNC:" + objClass.nameSpaceIdNC);
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

		for (Iterator <String> i = objClass.superClasses.iterator(); i.hasNext();) {
			String aname = (String) i.next();
			System.out.println("    superClass:" + aname);
   		}
		for (Iterator <PDSObjDefn> i = objClass.superClass.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			System.out.println("    superClass.identifier:" + lClass.identifier);
   		}
		for (Iterator <String> i = objClass.subClasses.iterator(); i.hasNext();) {
			String aname = (String) i.next();
			System.out.println("    subClasses:" + aname);
   		}
		for (Iterator <PDSObjDefn> i = objClass.subClass.iterator(); i.hasNext();) {
			PDSObjDefn aname = (PDSObjDefn) i.next();
			System.out.println("    subClass:" + aname.title);
   		}

		for (Iterator <String> i = objClass.ownedAttrId.iterator(); i.hasNext();) {
			String lId = (String) i.next();
			System.out.println("    ownedAttrId:" + lId);
		}
		for (Iterator <String> i = objClass.inheritedAttrId.iterator(); i.hasNext();) {
			String lId = (String) i.next();
			System.out.println("    inheritedAttrId:" + lId);
		}
		for (Iterator <AttrDefn> i = objClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttribute:" + lAttr.identifier);
		}
		for (Iterator <String> i = objClass.ownedAssocId.iterator(); i.hasNext();) {
			String lId = (String) i.next();
			System.out.println("    ownedAssocId:" + lId);
		}
		for (Iterator <AttrDefn> i = objClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAssociation:" + lAttr.identifier);
		}
		for (Iterator <String> i = objClass.inheritedAssocId.iterator(); i.hasNext();) {
			String lId = (String) i.next();
			System.out.println("    inheritedAssocId:" + lId);
		}
		for (Iterator <AttrDefn> i = objClass.allAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    allAttrAssocArr:" + lAttr.identifier);
		}
		
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocNOArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocNOArr:" + lAttr.identifier);
		}
		
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocArr:" + lAttr.identifier);
		}
		
		for (Iterator <AttrDefn> i = objClass.ownedAttrAssocAssertArr .iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			System.out.println("    ownedAttrAssocAssertArr :" + lAttr.identifier);
		}	
		
/*		for (Iterator <AttrDefn> i = objClass.hasSlot.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
		}
		*/
		
		for (Iterator <AttrDefn> i = objClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
		}
		for (Iterator <AttrDefn> i = objClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn attr = (AttrDefn) i.next();
			printAttr("objdump", attr);
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
//      System.out.println("\n    debug Attribute Definition" + " - " + note);
        System.out.println("\n    debug Attribute Definition" + " - title:" + attr.title);
        System.out.println("        attr.title:" + attr.title);
		System.out.println("        attr.identifier:" + attr.identifier);
		System.out.println("        attr.type:" + attr.type);
        System.out.println("        attr.rdfIdentifier:" + attr.rdfIdentifier);
        System.out.println("        attr.hasRetiredValue:" + attr.hasRetiredValue);
		System.out.println("        attr.registrationStatus:" + attr.registrationStatus);
		System.out.println("        attr.className:" + attr.className);
		System.out.println("        attr.attrNameSpaceId:" + attr.attrNameSpaceId);
		System.out.println("        attr.attrNameSpaceIdNC:" + attr.attrNameSpaceIdNC);
		System.out.println("        attr.deDataIdentifier:" + attr.deDataIdentifier);
		System.out.println("        attr.regAuthId:" + attr.regAuthId);
		System.out.println("        attr.subModelid:" + attr.subModelId);
		System.out.println("        attr.iAttribute:" + attr.isAttribute);
		System.out.println("        attr.isOwnedAttribute:" + attr.isOwnedAttribute);
		System.out.println("        attr.isRestrictedInSubclass:" + attr.isRestrictedInSubclass);
		System.out.println("        attr.isPDS4:" + attr.isPDS4);		
		System.out.println("        attr.isDataType:" + attr.isDataType);		
		System.out.println("        attr.isEnumerated:" + attr.isEnumerated);		
		System.out.println("        attr.isNilable:" + attr.isNilable);		
		System.out.println("        attr.isUnitOfMeasure:" + attr.isUnitOfMeasure);
		System.out.println("        attr.valueType:" + attr.valueType);
		System.out.println("        attr.propType:" + attr.propType);
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
		
		if (attr.permValueArr != null && attr.permValueArr.size() > 0) {
			System.out.println("        has attr.permValueArr");

			for (Iterator <PermValueDefn> j = attr.permValueArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
				System.out.println("          PermValueDefn.value:" + lPermValueDefn.value + "  registrationStatus:" + lPermValueDefn.registrationStatus + "  lPermValueDefn.value_meaning:" + lPermValueDefn.value_meaning);
			}
		}		
	}
	
//	Dump the attribute dictionary (original parsed attributes; includes top class
	static public void dumpAttrDict () {
		
// 	*** Note that the Identifier in the MAP is not the identifier in the Attribute; the class has been set to USER.		

		System.out.println("\ndebug dump attribute dictionary");
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (DMDocument.userClassAttributesMapId.values());
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			System.out.println("debug dump attribute identifiers lAttr.identifier:" + lAttr.identifier);
		}
		return;
	}
	
//	debug - write one attributes permissible values
	static public void writePermissibleValues (String where, AttrDefn lAttr) {
		System.out.println("\ndebug writePermissibleValues - " + where + " - lAttr.identifier:" + lAttr.identifier);
		if (lAttr.valArr == null || lAttr.valArr.size() == 0) {
			System.out.println("debug writePermissibleValues -  No ValArr - lAttr.identifier:" + lAttr.identifier);
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lVal = (String) i.next();
			System.out.println("debug writePermissibleValues - lVal:" + lVal);
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
	
//	write the CSV File
	static public void writeCSVFile (ArrayList <PDSObjDefn> lClassArr, SchemaFileDefn lSchemaFileDefn, String lOtherLanguage)  throws java.io.IOException {
		String pIdentifier;
		String blanks = "                              ";
		String padding;
		int padLength;
		String classSortField, attrSortField, valueSortField;
//		String lFileIdUpper = lSchemaFileDefn.identifier.toUpperCase();
		String lFileIdUpper = lSchemaFileDefn.identifier;
		
		String lFileName;
		if (! DMDocument.LDDToolFlag) {
			lFileName = DMDocument.outputDirPath + "PDS4DD.csv";
		} else {
			if (lOtherLanguage == null) lFileName = DMDocument.LDDToolOutputFileNameNE + "_" + lFileIdUpper + "_" + lSchemaFileDefn.lab_version_id + ".csv";
			else {
				lFileName = DMDocument.LDDToolOutputFileNameNE + "_" + lFileIdUpper + "_" + lSchemaFileDefn.lab_version_id + "_" + lOtherLanguage + ".csv";				
			}
		}
		FileOutputStream lFileOutputStream = new FileOutputStream(lFileName);
		BufferedWriter prCSVAttr = new BufferedWriter(new OutputStreamWriter(lFileOutputStream,"UTF8"));
		String delmBegin = "\"", delmMid = "\",\"", delmEnd = "\"";
		prCSVAttr.write(delmBegin + "Sort Key" + delmMid + "Type" + delmMid + "Name" + delmMid + "Version" + delmMid + "Name Space Id" + delmMid + "Description" + delmMid + "Steward" + delmMid + "Value Type"  + delmMid + "Minimum Cardinality"  + delmMid + "Maximum Cardinality"  + delmMid + "Minimum Value"  + delmMid + "Maximum Value" + delmMid+ "Minimum Characters"  + delmMid + "Maximum Characters" + delmMid + "Unit of Measure Type" + delmMid + "Specified Unit Id" + delmMid + "Attribute Concept" + delmMid + "Conceptual Domain" + delmEnd + "\r\n");		
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;

			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
//				pIdentifier = lClass.title + ":" + "Class";
				padLength = 30 - lClass.title.length();
				if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
				classSortField = lClass.nameSpaceId + lClass.title + ":1" + padding;
				attrSortField = lClass.nameSpaceId + lClass.title + ":2" + padding;
				pIdentifier = classSortField;
				
				prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Class" + delmMid + lClass.nameInLanguage(lOtherLanguage) + delmMid + lClass.versionId + delmMid + lClass.nameSpaceIdNC + delmMid + lClass.definitionInLanguage(lOtherLanguage) + delmMid + lClass.steward + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + ""  + delmMid + ""  + delmEnd + "\r\n");
				ArrayList <AttrDefn> allAttr = new ArrayList <AttrDefn> ();
				allAttr.addAll(lClass.ownedAttribute);
				allAttr.addAll(lClass.inheritedAttribute);

				for (Iterator <AttrDefn> j = allAttr.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();	
					if (lAttr != null) {
						String pMinVal = lAttr.getMinimumValue (true, true);
						if ((pMinVal.indexOf("TBD") == 0) || (pMinVal.indexOf("-INF") == 0)) {
							pMinVal = "Unbounded";
						}
						String pMaxVal = lAttr.getMaximumValue (true, true);
						if ((pMaxVal.indexOf("TBD") == 0) || (pMaxVal.indexOf("INF") == 0)) {
							pMaxVal = "Unbounded";
						}
						String pMinChar = lAttr.getMinimumCharacters (true, true);
						if (pMinChar.indexOf("TBD") == 0) {
							pMinChar = "Unbounded";
						}
						String pMaxChar = lAttr.getMaximumCharacters (true, true);
						if (pMaxChar.indexOf("TBD") == 0) {
							pMaxChar = "Unbounded";
						}
						padLength = 30 - lAttr.title.length();
						if (padLength < 0) padLength = 0;
						padding = blanks.substring(0, padLength);
						pIdentifier = attrSortField + " " + lAttr.attrNameSpaceId + lAttr.title + ":1" + padding;
						valueSortField = attrSortField + " " + lAttr.attrNameSpaceId + lAttr.title + ":2" + padding;
						prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Attribute" + delmMid + lAttr.nameInLanguage(lOtherLanguage) + delmMid + "n/a" + delmMid + lAttr.getNameSpaceId () + delmMid + lAttr.definitionInLanguage(lOtherLanguage) + delmMid + lAttr.getSteward () + delmMid + lAttr.valueType + delmMid + lAttr.cardMin + delmMid + lAttr.cardMax + delmMid + pMinVal + delmMid + pMaxVal + delmMid+ pMinChar + delmMid + pMaxChar+ delmMid + lAttr.getUnitOfMeasure (true) + delmMid + lAttr.getDefaultUnitId (true) + delmMid + lAttr.classConcept + delmMid + lAttr.dataConcept + delmEnd + "\r\n");

						
						if ( ! (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty())) {
							for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
								PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
								String lValue = lPermValueDefn.value;
								if (lValue.length() > 20) lValue = lValue.substring(0,20);
								pIdentifier = valueSortField + " Value:" + lValue;
								prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Value" + delmMid + lPermValueDefn.value + delmMid + "" + delmMid + "" + delmMid + lPermValueDefn.value_meaning + delmEnd + "\r\n");
							}
						}
						
/*						if (! (lAttr.valArr == null || lAttr.valArr.isEmpty())) {
							String lVal = (String) lAttr.valArr.get(0);
							if (lVal.compareTo("") != 0) {
								pIdentifier = lClass.title + ":" + "3";
								for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
									String lValue = (String) k.next();
									prCSVAttr.println(delmBegin + pIdentifier + delmMid + "Value" + delmMid + lValue + delmMid + "" + delmMid + "TBD_value_meaning" + delmEnd);
								}
							}
						} */
					}
				}
			}
		}
		prCSVAttr.close();
	}	
}
