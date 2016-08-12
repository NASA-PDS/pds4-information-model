package gov.nasa.pds.model.plugin; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

/** Main for creating Document
 *
 */ 
public class DMDocument extends Object {
	
// change markers
// 444 - Rules moved to .pins file
	
	// environment variables
	static String lJAVAHOME;
	static String lPARENT_DIR;
	static String lSCRIPT_DIR;
	static String lLIB_DIR;
	static String lUSERNAME;
	
	// specification document info
	static DocDefn docInfo;
	static final String docFileName  = "DMDocument";
	
	// variables for the class %3ACLIPS_TOP_LEVEL_SLOT_CLASS
	static final String TopLevelAttrClassName  = "%3ACLIPS_TOP_LEVEL_SLOT_CLASS";
	
	static boolean PDSOptionalFlag;
	
	// configuration file variables
	static String infoModelVersionId = "0.0.0.0";
	static String infoModelVersionIdNoDots = "0000";
	static String schemaLabelVersionId = "0.0";
	static String pds4BuildId = "0a";
	
	static String imSpecDocTitle = "TBD_";
	static String imSpecDocAuthor = "TBD_";
	static String imSpecDocSubTitle = "TBD_";
	
	static String dataDirPath  = "TBD_dataDirPath";
	static String outputDirPath = "./";

	static String DMDocVersionId  = "0.1.8";
//	static String XMLSchemaLabelBuildNum = "6a";
	static String XMLSchemaLabelBuildNum;
	
	// Desired   VID   MOD
	// 1.0.0.0 - 1.0  - 1.0  - Build 3b
	// 1.1.0.0 - 1.1  - 1.1  - Build 4a
	// 1.1.0.1 - 1.2  - 1.2  - Build 4a
	// 1.2.0.0 - 1.3  - 1.3  - Build 4b
	// 1.2.0.1 - 1.4  - 1.4  - Build 4b
	// 1.3.0.0 - 1.5  - 1.5  - Build 5a
	// 1.3.0.1 - 1.6  - 1.6  - Build 5a
	// 1.4.0.0 - 1.7  - 1.7  - Build 5b
	// 1.4.0.1 - 1.8  - 1.8  - Build 5b - not released
	// 1.4.1.0 - 1.8  - 1.8  - Build 5b
	// 1.5.0.0 - 1.9  - 1.9  - Build 6a
	// 1.5.1.0 - 1.9  - 1.9  - Build 6b - not released
	// 1.6.0.0 - 1.10 - 1.10 - Build 6b
	// 1.7.0.0 - 1.11 - 1.11 - Build 7a
	
	// Actual    VID    MOD
	// 1.0.0.0 - 1.0  - 1.0  - Build 3b
	// 1.1.0.0 - 1.1  - 1.1  - Build 4a
	// 1.1.0.1 - 1.2  - 1.2  - Build 4a
	// 1.2.0.0 - 1.3  - 1.3  - Build x4a - error
	// 1.2.0.1 - 1.4  - 1.4  - Build 4b
	// 1.3.0.0 - 1.5  - 1.5  - Build 5a
	// 1.3.0.1 - 1.6  - 1.6  - Build 5a
	// 1.4.0.0 - x1.3 - x1.6 - Build 5b - error
	// 1.4.1.0 - 1.8  - 1.8  - Build 5b 
	// 1.5.0.0 - 1.9  - 1.9  - Build 6a 
	// 1.6.0.0 - 1.10 - 1.10 - Build 6b 
	// 1.7.0.0 - 1.11 - 1.11 - Build 7a
	
	// x.x.x.x - 1.0 - 1.n - Build nm - first version of product will always be 1.0
	//									Modification history will continue with 1.n
	                         

//	static String LDDToolVersionId  = "0.1.9.0a2";
//	static String LDDToolVersionId  = "0.2.0.3";
	static String LDDToolVersionId  = "0.2.1.0";
	static String classVersionIdDefault = "1.0.0.0";
//	static String LDDToolGeometry = "Geometry";
	static boolean PDS4MergeFlag  = false;
	static boolean LDDSyncFileNameFlag = false;
	static boolean LDDClassElementFlag = false;			// if true, write XML elements for classes
	static boolean LDDAttrElementFlag = false;			// if true, write  XML elements for attributes
	static boolean LDDNuanceFlag = false;				//
	
	// export file flags
	static boolean exportProdDefnFlag = true;
	static boolean exportJSONFileFlag = false;
	static boolean exportSpecFileFlag = false;
	static boolean exportDDFileFlag = false;

	// when true this flag indicates an LDDTool run for a namespace other than pds (i.e., Common)
	static boolean LDDToolFlag;
	// in an LDDTool run, when true indicates that a mission LDD is being processed, otherwise a discipline LDD is being processed
	static boolean LDDToolMissionGovernanceFlag;
	// governance levels are Common, Discipline, and Mission, consistent with the flags.
	static String governanceLevel = "Common";
	
	static boolean LDDToolAnnotateDefinitionFlag;
	static String LDDToolSingletonClassTitle = "USER";
	static PDSObjDefn LDDToolSingletonClass = null;
	static ArrayList <String> LDDImportNameSpaceIdNCArr = new ArrayList <String> ();
	static TreeMap <String, String> LDDToolSchemaVersionMapDots = new TreeMap <String, String> ();
	static TreeMap <String, String> LDDToolSchemaVersionMapNoDots = new TreeMap <String, String> ();
	static TreeMap <String, String> LDDToolSchemaVersionNSMap = new TreeMap <String, String> ();

	// Master Model
	static MasterInfoModel masterInfoModel;
	
	// Master LDD Model
	static LDDParser primaryLDDModel;
	
	// Secondary LDD Models
	static ArrayList <LDDParser> LDDModelArr;
	
	// Schemas, Stewards and Namespaces (SchemaFileDefn)
	static TreeMap <String, SchemaFileDefn> masterSchemaFileSortMap;
	static ArrayList <SchemaFileDefn> LDDSchemaFileSortArr;
//	static TreeMap <String, String> masterClassStewardSortMap;
	
	// Master Schemas, Stewards and Namespaces (SchemaFileDefn)
	static SchemaFileDefn masterPDSSchemaFileDefn; 		
	static SchemaFileDefn masterLDDSchemaFileDefn; 		
	
	// dates
	static Date rTodaysDate;
	static String sTodaysDate;
	
	static String masterTodaysDate;
	static String masterTodaysDateUTC;
	static String masterTodaysDateTimeUTC;
	static String masterTodaysDateTimeUTCwT;
	static String masterTodaysDateyymmdd;

	// 11179 and RDF global variables
	static String rdfPrefix;
	static String creationDateValue;
	static String beginDatePDS4Value;
	
	static String endDateValue;
	static String futureDateValue;
	static String administrationRecordValue;	
	static String versionIdentifierValue;
	static String stewardValue;
	static String submitterValue;

	static String registeredByValue;
	static String registrationAuthorityIdentifierValue;

	// master user class
	static String masterUserClassName;
	static String masterUserClassNamespaceIdNC;
	
	// master uid
	static int masterUId;
	
	// master class order
	static int masterClassOrder;
	
	// master class order
	static int masterGroupNum;
	
	// master class disposition maps
	static TreeMap <String, DispDefn> masterClassDispoMap2;
	static ArrayList <DispDefn> masterClassDispoArr;
	static ArrayList <String> masterStewardArr;
	static ArrayList <String> masterNameSpaceIDArr;
	static ArrayList <String> masterStewardNameSpaceIDArr;
	
	// registry classes and attributes
	static ArrayList <String> registryClass;
	static ArrayList <String> registryAttr;
	
	// Omitted classes
	static ArrayList <String> omitClass;
		
	// the set of deprecated classes, attributes, and values	
	static ArrayList <DeprecatedDefn> deprecatedObjects2;
	static ArrayList <String> deprecatedAttrValueArr;
	static String Literal_DEPRECATED = " *Deprecated*";
	static boolean deprecatedAdded;
	
	// the set of classes and attributes that will be externalized (defined as xs:Element)	
	static ArrayList <String> exposedElementArr;

	// class version identifiers (only updated classes; v1.0.0.0 is assumed)
	static TreeMap <String, String> classVersionId;
	
	// debug flag	
	static boolean debugFlag = false;
	
	// need a place to store the LDD schema file definition until it is created.
//	static String LDDToolSchemaFileVersionId;


/**********************************************************************************************************
	main
***********************************************************************************************************/
	
	public static void main (String args[]) throws Throwable {
		
//		boolean PDSOptionalFlag = false;
		PDSOptionalFlag = false;
		LDDToolFlag = false;
		// Secondary LDD Models
		LDDModelArr = new ArrayList <LDDParser> ();
		LDDSchemaFileSortArr = new ArrayList <SchemaFileDefn> ();
		LDDToolAnnotateDefinitionFlag = false;
		LDDToolMissionGovernanceFlag = false;
//		LDDToolSchemaVersionMapNoDots = new TreeMap <String, String> ();
//		LDDToolSchemaVersionMapDots = new TreeMap <String, String> ();
//		LDDToolSchemaVersionNSMap = new TreeMap <String, String> ();

		// get dates
		rTodaysDate = new Date();
		sTodaysDate  = rTodaysDate.toString();
		masterTodaysDate = sTodaysDate;
		masterTodaysDateUTC = getUTCDate ();		
		masterTodaysDateyymmdd = masterTodaysDateUTC.substring(2, 4) + masterTodaysDateUTC.substring(5,7) + masterTodaysDateUTC.substring(8, 10);
		masterTodaysDateTimeUTC = getUTCDateTime ();		
		masterTodaysDateTimeUTCwT = replaceString (masterTodaysDateTimeUTC, " ", "T");			
		
		rdfPrefix = "http://pds.nasa.gov/infomodel/pds#";
		creationDateValue = masterTodaysDateUTC;
		beginDatePDS4Value = "2009-06-09";
		
		endDateValue = "2019-12-31";
		futureDateValue = "2019-12-31";
		versionIdentifierValue = "TBD_versionIdentifierValue";
		administrationRecordValue = "TBD_administrationRecordValue"; // set in GetModels
		stewardValue = "Steward_PDS";
		submitterValue = "Submitter_PDS";
		
		registeredByValue = "RA_0001_NASA_PDS_1"; 
		registrationAuthorityIdentifierValue = "0001_NASA_PDS_1";

		// Master User Class Name
		masterUserClassNamespaceIdNC = "all";
		masterUserClassName = "USER";
		
		// master unique sequence number
		masterUId = 100000000;
		
		// master class order
		masterClassOrder = 1000;

		// master group number
		masterClassOrder = 10;
		
		omitClass = new ArrayList <String> ();
		omitClass.add("Data_Object");
		omitClass.add("Digital_Object");
		omitClass.add("Physical_Object");
		omitClass.add("Conceptual_Object");
		
		registryAttr = new ArrayList <String> ();
		registryAttr.add("data_set_name");
		registryAttr.add("full_name");
		registryAttr.add("instrument_host_name");
		registryAttr.add("instrument_name");
		registryAttr.add("investigation_name");
		registryAttr.add("observing_system_name");
		registryAttr.add("target_name");
		registryAttr.add("title");
		registryAttr.add("alternate_title");
		registryAttr.add("alternate_id");
		registryAttr.add("product_class");
//		registryAttr.add("product_subclass");
		registryAttr.add("start_date_time");
		registryAttr.add("stop_date_time");
		registryAttr.add("start_date");
		registryAttr.add("stop_date");
		registryAttr.add("logical_identifier"); 
		registryAttr.add("version_id");
				
		// deprecated objects  *** Inconsistency here to be fixed - Earth base identifier is different ***
		deprecatedAdded = false;
		deprecatedObjects2 = new ArrayList <DeprecatedDefn> ();

		deprecatedObjects2.add(new DeprecatedDefn ("Geometry", "pds", "Geometry", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Display_2D_Image", "pds", "Display_2D_Image", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Band_Bin_Set", "pds", "Band_Bin_Set", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Band_Bin", "pds", "Band_Bin", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Axis_Array.unit", "pds", "Axis_Array", "pds", "unit", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument_Host.type.Earth Based", "pds", "Instrument_Host", "pds", "type", "Earth Based", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Accelerometer", "pds", "Instrument", "pds", "type", "Accelerometer", false));                            
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Alpha Particle Detector", "pds", "Instrument", "pds", "type", "Alpha Particle Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Alpha Particle X-Ray Spectrometer", "pds", "Instrument", "pds", "type", "Alpha Particle X-Ray Spectrometer", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Altimeter", "pds", "Instrument", "pds", "type", "Altimeter", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Anemometer", "pds", "Instrument", "pds", "type", "Anemometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Atomic Force Microscope", "pds", "Instrument", "pds", "type", "Atomic Force Microscope", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Barometer", "pds", "Instrument", "pds", "type", "Barometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Biology Experiments", "pds", "Instrument", "pds", "type", "Biology Experiments", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Bolometer", "pds", "Instrument", "pds", "type", "Bolometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Camera", "pds", "Instrument", "pds", "type", "Camera", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Cosmic Ray Detector", "pds", "Instrument", "pds", "type", "Cosmic Ray Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Drilling Tool", "pds", "Instrument", "pds", "type", "Drilling Tool", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Dust Detector", "pds", "Instrument", "pds", "type", "Dust Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Electrical Probe", "pds", "Instrument", "pds", "type", "Electrical Probe", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Energetic Particle Detector", "pds", "Instrument", "pds", "type", "Energetic Particle Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Gamma Ray Detector", "pds", "Instrument", "pds", "type", "Gamma Ray Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Gas Analyzer", "pds", "Instrument", "pds", "type", "Gas Analyzer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Grinding Tool", "pds", "Instrument", "pds", "type", "Grinding Tool", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Hygrometer", "pds", "Instrument", "pds", "type", "Hygrometer", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Imager", "pds", "Instrument", "pds", "type", "Imager", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Imaging Spectrometer", "pds", "Instrument", "pds", "type", "Imaging Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Inertial Measurement Unit", "pds", "Instrument", "pds", "type", "Inertial Measurement Unit", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Infrared Spectrometer", "pds", "Instrument", "pds", "type", "Infrared Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Laser Induced Breakdown Spectrometer", "pds", "Instrument", "pds", "type", "Laser Induced Breakdown Spectrometer", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Magnetometer", "pds", "Instrument", "pds", "type", "Magnetometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Mass Spectrometer", "pds", "Instrument", "pds", "type", "Mass Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Microwave Spectrometer", "pds", "Instrument", "pds", "type", "Microwave Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Moessbauer Spectrometer", "pds", "Instrument", "pds", "type", "Moessbauer Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Naked Eye", "pds", "Instrument", "pds", "type", "Naked Eye", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Neutral Particle Detector", "pds", "Instrument", "pds", "type", "Neutral Particle Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Neutron Detector", "pds", "Instrument", "pds", "type", "Neutron Detector", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Photometer", "pds", "Instrument", "pds", "type", "Photometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Plasma Analyzer", "pds", "Instrument", "pds", "type", "Plasma Analyzer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Plasma Detector", "pds", "Instrument", "pds", "type", "Plasma Detector", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Plasma Wave Spectrometer", "pds", "Instrument", "pds", "type", "Plasma Wave Spectrometer", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Polarimeter", "pds", "Instrument", "pds", "type", "Polarimeter", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radar", "pds", "Instrument", "pds", "type", "Radar", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radio Science", "pds", "Instrument", "pds", "type", "Radio Science", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radio Spectrometer", "pds", "Instrument", "pds", "type", "Radio Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radio Telescope", "pds", "Instrument", "pds", "type", "Radio Telescope", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radiometer", "pds", "Instrument", "pds", "type", "Radiometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Reflectometer", "pds", "Instrument", "pds", "type", "Reflectometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Robotic Arm", "pds", "Instrument", "pds", "type", "Robotic Arm", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Spectrograph Imager", "pds", "Instrument", "pds", "type", "Spectrograph Imager", false));
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Spectrometer", "pds", "Instrument", "pds", "type", "Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Thermal Imager", "pds", "Instrument", "pds", "type", "Thermal Imager", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Thermal Probe", "pds", "Instrument", "pds", "type", "Thermal Probe", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Thermometer", "pds", "Instrument", "pds", "type", "Thermometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Ultraviolet Spectrometer", "pds", "Instrument", "pds", "type", "Ultraviolet Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Weather Station", "pds", "Instrument", "pds", "type", "Weather Station", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Wet Chemistry Laboratory", "pds", "Instrument", "pds", "type", "Wet Chemistry Laboratory", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.X-ray Detector", "pds", "Instrument", "pds", "type", "X-ray Detector", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.X-ray Diffraction Spectrometer", "pds", "Instrument", "pds", "type", "X-ray Diffraction Spectrometer", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.X-ray Fluorescence Spectrometer", "pds", "Instrument", "pds", "type", "X-ray Fluorescence Spectrometer", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Calibration", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Open Cluster", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Globular Cluster", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Calibration", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Open Cluster", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Globular Cluster", false));
	    
		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.data_regime", "pds", "Primary_Result_Summary", "pds", "data_regime", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.type", "pds", "Primary_Result_Summary", "pds", "type", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.processing_level_id", "pds", "Primary_Result_Summary", "pds", "processing_level_id", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "subclass_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "restriction_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "extension_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External", "pds", "DD_Association_External", "", "", "", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "subclass_of", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "restriction_of", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "extension_of", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Table_Binary.record_delimiter", "pds", "Table_Binary", "pds", "record_delimiter", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.record_delimiter", "pds", "Table_Delimited", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Character.record_delimiter", "pds", "Table_Character", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Stream_Text.record_delimiter", "pds", "Stream_Text", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Checksum_Manifest.record_delimiter", "pds", "Checksum_Manifest", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.record_delimiter", "pds", "Inventory", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Transfer_Manifest.record_delimiter", "pds", "Transfer_Manifest", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Uniformly_Sampled.sampling_parameters", "pds", "Uniformly_Sampled", "pds", "sampling_parameters", "", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "vertical bar", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "vertical bar", false));

//		deprecatedObjects2.add(new DeprecatedDefn ("Update.Update_Entry", "pds", "Update_Entry", "", "", "", false));

/*		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Aerial survey - North American (1983) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Adindan datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Australian datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Campo Inchauspe (Argentina) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Cape (South Africa) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Corregio Alegre (Brazil) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - datum unknown", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - European 1979 datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - European datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - GRS 80 datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Hermannskogel datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Indian datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - La Canoa (Venezuela) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - New Zealand datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - North American (1927) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Old Hawaiian datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Ordnance Survey of Great Britain (1936) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Ordnance Survey of Great Britain (SN) 1980 datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Potsdam datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Puerto Rican (1940) datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - South American datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - Tokyo datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Geodetic - WGS 84 datum", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.coordinate_source", "pds", "Telescope", "pds", "coordinate_source", "Satellite determined - datum unknown", false)); */
		
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.altitude", "pds", "Telescope", "pds", "altitude", "", false));

		
		deprecatedObjects2.add(new DeprecatedDefn ("Document_Format.format_type", "pds", "Document_Format", "pds", "format_type", "single file", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Document_Format.format_type", "pds", "Document_Format", "pds", "format_type", "multiple file", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Thermal And Electrical Conductivity Probe", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "X-ray Defraction Spectrometer", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Alpha Particle Xray Spectrometer", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "X-ray Fluorescence", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Grinding And Drilling Tool", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Node.type", "name", "Node", "pds", "name", "Navigation Ancillary Information Facility", false));
		deprecatedObjects2.add(new DeprecatedDefn ("PDS_Affiliate.team_name", "pds", "PDS_Affiliate", "pds", "team_name", "Navigation Ancillary Information Facility", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Encoded_Binary.encoding_standard_id", "pds", "Encoded_Binary", "pds", "encoding_standard_id", "CCSDS Communications Protocols", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Encoded_Binary.encoding_standard_id", "pds", "Encoded_Binary", "pds", "encoding_standard_id", "CCSDS Space Communications Protocols", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Encoded_Image.encoding_standard_id", "pds", "Encoded_Image", "pds", "encoding_standard_id", "J2C", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Software.version_id", "pds", "Software", "pds", "version_id", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument_Host.version_id", "pds", "Instrument_Host", "pds", "version_id", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument_Host.instrument_host_version_id", "pds", "Instrument_Host", "pds", "instrument_host_version_id", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Data_Set_PDS3.start_date_time", "pds", "Data_Set_PDS3", "pds", "start_date_time", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Data_Set_PDS3.stop_date_time", "pds", "Data_Set_PDS3", "pds", "stop_date_time", "", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Document_File.document_standard_id", "pds", "Document_File", "pds", "document_standard_id", "HTML 2.0", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Document_File.document_standard_id", "pds", "Document_File", "pds", "document_standard_id", "HTML 3.2", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Document_File.document_standard_id", "pds", "Document_File", "pds", "document_standard_id", "HTML 4.0", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Document_File.document_standard_id", "pds", "Document_File", "pds", "document_standard_id", "HTML 4.01", false));
		
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Radiance.unit_id", "pds", "Units_of_Radiance", "pds", "unit_id", "W*m**-2*sr**-1", true));

		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Irradiance.unit_id", "pds", "Units_of_Spectral_Irradiance", "pds", "unit_id", "SFU", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Irradiance.unit_id", "pds", "Units_of_Spectral_Irradiance", "pds", "unit_id", "W*m**-2*Hz**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Irradiance.unit_id", "pds", "Units_of_Spectral_Irradiance", "pds", "unit_id", "W*m**-2*nm**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Irradiance.unit_id", "pds", "Units_of_Spectral_Irradiance", "pds", "unit_id", "W*m**-3", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Irradiance.unit_id", "pds", "Units_of_Spectral_Irradiance", "pds", "unit_id", "uW*cm**-2*um**-1", true));

		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Radiance.unit_id", "pds", "Units_of_Spectral_Radiance", "pds", "unit_id", "W*m**-2*sr**-1*Hz**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Radiance.unit_id", "pds", "Units_of_Spectral_Radiance", "pds", "unit_id", "W*m**-2*sr**-1*nm**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Radiance.unit_id", "pds", "Units_of_Spectral_Radiance", "pds", "unit_id", "W*m**-2*sr**-1*um**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Radiance.unit_id", "pds", "Units_of_Spectral_Radiance", "pds", "unit_id", "W*m**-3*sr**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Spectral_Radiance.unit_id", "pds", "Units_of_Spectral_Radiance", "pds", "unit_id", "uW*cm**-2*sr**-1*um**-1", true));

		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Wavenumber.unit_id", "pds", "Units_of_Wavenumber", "pds", "unit_id", "cm**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Wavenumber.unit_id", "pds", "Units_of_Wavenumber", "pds", "unit_id", "m**-1", true));
		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Wavenumber.unit_id", "pds", "Units_of_Wavenumber", "pds", "unit_id", "nm**-1", true));

		deprecatedObjects2.add(new DeprecatedDefn ("Array_1D", "pds", "Array_1D", "", "", "", false));

		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date", "pds", "ASCII_Date", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time", "pds", "ASCII_Date_Time", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time_UTC", "pds", "ASCII_Date_Time_UTC", "", "", "", false));
		
		// get ArrayList for  testing
		deprecatedAttrValueArr = new ArrayList <String> ();
		for (Iterator <DeprecatedDefn> i = deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lObject = (DeprecatedDefn) i.next();
			if (lObject.value.compareTo("") != 0) {
				String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className + "." + lObject.attrName + "." + lObject.value;			
				deprecatedAttrValueArr.add(lIdentifier);
//				System.out.println("debug deprecatedObjects2 Value lIdentifier:" + lIdentifier);
				continue;
			}
			if (lObject.attrName.compareTo("") != 0) {
				String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className + "." + lObject.attrName;
				deprecatedAttrValueArr.add(lIdentifier);
//				System.out.println("debug deprecatedObjects2 Attr lIdentifier:" + lIdentifier);
				continue;
			}
			String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className;
			deprecatedAttrValueArr.add(lIdentifier);
//			System.out.println("debug deprecatedObjects2 Class lIdentifier:" + lIdentifier);
		}
		
		// the set of classes and attributes that will be externalized (defined as xs:Element)
		exposedElementArr = new ArrayList <String> ();
		exposedElementArr.add("Internal_Reference");
		exposedElementArr.add("Local_Internal_Reference");
		exposedElementArr.add("External_Reference");
		
		// class version ids
		classVersionId = new TreeMap <String, String> ();
		classVersionId.put("Array", "2.0.0.0");
		classVersionId.put("Array_2D_Image", "1.1.0.0");
		classVersionId.put("Axis_Array", "1.3.0.0");
		classVersionId.put("Bundle_Member_Entry", "1.1.0.0");
		classVersionId.put("Checksum_Manifest", "1.1.0.0");
		classVersionId.put("Citation_Information", "1.2.0.0");
		classVersionId.put("Data_Set_PDS3", "1.1.0.0");
		classVersionId.put("DD_Association", "1.2.0.0");
		classVersionId.put("DD_Association_External", "1.1.0.0");
		classVersionId.put("DD_Permissible_Value", "1.1.0.0");
		classVersionId.put("Display_Settings", "1.1.0.0");
		classVersionId.put("Document", "2.0.0.0");
		classVersionId.put("Document_File", "1.4.0.0");
		classVersionId.put("Encoded_Binary", "1.1.0.0");
		classVersionId.put("Encoded_Telemetry", "1.1.0.0");
		classVersionId.put("Field", "1.1.0.0");
		classVersionId.put("File", "1.1.0.0");
		classVersionId.put("File_Area_Browse", "1.1.0.0");
		classVersionId.put("File_Area_Ancillary", "1.1.0.0");
		classVersionId.put("File_Area_Browse", "1.2.0.0");
		classVersionId.put("File_Area_Observational", "1.3.0.0");
		classVersionId.put("File_Area_Observational_Supplemental", "1.3.0.0");
		classVersionId.put("File_Area_Update", "1.1.0.0");
		classVersionId.put("Group", "1.1.0.0");
		classVersionId.put("Group_Field_Binary", "1.1.0.0");
		classVersionId.put("Group_Field_Character", "1.1.0.0");
		classVersionId.put("Header", "1.1.0.0");
		classVersionId.put("Identification_Area", "1.4.0.0");
		classVersionId.put("Ingest_LDD", "1.1.0.0");
		classVersionId.put("Instrument", "1.3.0.0");
		classVersionId.put("Instrument_Host", "1.3.0.0");
		classVersionId.put("Internal_Reference", "1.1.0.0");
		classVersionId.put("Inventory", "1.2.0.0");
//		classVersionId.put("NOT Spice_Kernel", "");
		classVersionId.put("Observing_System_Component", "1.1.0.0"); 
		classVersionId.put("Primary_Result_Summary", "2.3.0.0");

		classVersionId.put("Product_Document", "2.1.0.0");
		classVersionId.put("Investigation_Area", "1.1.0.0");        //  <Internal_Reference.reference_type>
		classVersionId.put("Product_Bundle", "1.1.0.0");         		//  <Internal_Reference.reference_type>
		classVersionId.put("Product_Collection", "1.1.0.0");        //  <Internal_Reference.reference_type>
		classVersionId.put("Product_Context", "1.1.0.0");         	//  <Internal_Reference.reference_type>
		classVersionId.put("Product_Observational", "1.7.0.0");      //  <Internal_Reference.reference_type>
		classVersionId.put("Product_Native", "1.2.0.0");
		classVersionId.put("Product_Service", "1.1.0.0");
		classVersionId.put("Product_XML_Schema", "1.2.0.0");
		classVersionId.put("Product_SIP_Deep_Archive", "1.1.0.0");

		classVersionId.put("Record_Character", "1.1.0.0");
		classVersionId.put("Special_Constants", "1.1.0.0");
		classVersionId.put("Spectral_Radiance", "1.1.0.0");
		classVersionId.put("Spectral_Irradiance", "1.1.0.0");
		classVersionId.put("Table_Binary", "1.1.0.0");
		classVersionId.put("Target", "1.3.0.0");
		classVersionId.put("Target_Identification", "1.4.0.0");
		classVersionId.put("Telescope", "1.3.0.0");
		classVersionId.put("Terminological_Entry", "1.1.0.0");
		classVersionId.put("Transfer_Manifest", "1.1.0.0");
		classVersionId.put("Uniformly_Sampled", "1.1.0.0");
		classVersionId.put("Unit_Of_Measure", "1.1.0.0");
		classVersionId.put("Units_of_Current", "1.1.0.0");
		classVersionId.put("Units_of_Radiance", "1.1.0.0");
		classVersionId.put("XML_Schema", "1.2.0.0");

		classVersionId.put("ASCII_Date", "1.1.0.0");
		classVersionId.put("ASCII_DOI", "1.1.0.0");
		classVersionId.put("ASCII_Date_Time", "1.1.0.0");
		classVersionId.put("ASCII_Date_Time_DOY", "1.2.0.0");
		classVersionId.put("ASCII_Date_Time_UTC", "1.3.0.0");
		classVersionId.put("ASCII_Date_Time_YMD", "1.1.0.0");
		classVersionId.put("ASCII_Date_YMD", "1.1.0.0");
		classVersionId.put("ASCII_Integer", "1.1.0.0");
		classVersionId.put("ASCII_MD5_Checksum", "1.1.0.0");
		classVersionId.put("ASCII_Numeric_Base2", "1.2.0.0");
		classVersionId.put("ASCII_Numeric_Base8", "1.2.0.0");
		classVersionId.put("ASCII_Time", "1.1.0.0");
		classVersionId.put("ASCII_VID", "1.1.0.0");
		classVersionId.put("ASCII_Directory_Path_Name", "1.1.0.0");
		classVersionId.put("ASCII_File_Name", "1.1.0.0");
		classVersionId.put("ASCII_File_Specification_Name", "1.1.0.0");
		classVersionId.put("ASCII_LID", "1.1.0.0");
		classVersionId.put("ASCII_LIDVID", "1.1.0.0");
		classVersionId.put("ASCII_LIDVID_LID", "1.1.0.0");
		classVersionId.put("ASCII_MD5_Checksum", "1.1.0.0");
		classVersionId.put("ASCII_Time", "1.1.0.0");
		classVersionId.put("ASCII_VID", "1.1.0.0");
	
		// get the environment variables
		getEnvMap();
		dataDirPath = lPARENT_DIR + "/Data/";
		
		// get the config file
		String configInputFile = dataDirPath + "config.properties";
		String configInputStr;
    	File configFile = new File(configInputFile); 
    	try {
    	    FileReader reader = new FileReader(configFile);
    	    Properties props = new Properties();
    	    props.load(reader);
    	    configInputStr = props.getProperty("infoModelVersionId");
//    	    if (configInputStr != null) infoModelVersionId = configInputStr;
    	    if (configInputStr != null) {
    	    	infoModelVersionId = configInputStr;
    	    	infoModelVersionIdNoDots = replaceString(infoModelVersionId, ".", "");
    			LDDToolSchemaVersionMapNoDots.put ("pds", infoModelVersionIdNoDots);
    			LDDToolSchemaVersionMapNoDots.put ("disp", infoModelVersionIdNoDots);
    			LDDToolSchemaVersionMapDots.put ("pds", infoModelVersionId);
    			LDDToolSchemaVersionMapDots.put ("disp", infoModelVersionId);
    			LDDToolSchemaVersionNSMap.put ("pds", "1");
    			LDDToolSchemaVersionNSMap.put ("disp", "1");
//   	  	   System.out.println("debug dmDocument - infoModelVersionId:" + infoModelVersionId);
//  	  	   System.out.println("debug dmDocument - infoModelVersionIdNoDots:" + infoModelVersionIdNoDots);
//   	  	   System.out.println("debug dmDocument - LDDToolSchemaVersionMapDots.get:" + LDDToolSchemaVersionMapDots.get("pds"));
//   	  	   System.out.println("debug dmDocument - LDDToolSchemaVersionMapNoDots.get:" + LDDToolSchemaVersionMapNoDots.get("pds"));
    	    }
    	    configInputStr = props.getProperty("schemaLabelVersionId");
    	    if (configInputStr != null) schemaLabelVersionId = configInputStr;
//    	    System.out.println("debug dmDocument - schemaLabelVersionId:" + schemaLabelVersionId);
    	    configInputStr= props.getProperty("pds4BuildId");
    	    if (configInputStr != null) pds4BuildId = configInputStr;
//    	    System.out.println("debug dmDocument - pds4BuildId:" + pds4BuildId);
    	    
    	    configInputStr= props.getProperty("imSpecDocTitle");
    	    if (configInputStr != null) imSpecDocTitle = configInputStr;
//    	    System.out.println("debug dmDocument - imSpecDocTitle:" + imSpecDocTitle);
    	    configInputStr= props.getProperty("imSpecDocAuthor");
    	    if (configInputStr != null) imSpecDocAuthor = configInputStr;
//    	    System.out.println("debug dmDocument - imSpecDocAuthor:" + imSpecDocAuthor);
    	    configInputStr= props.getProperty("imSpecDocSubTitle");
    	    if (configInputStr != null) imSpecDocSubTitle = configInputStr;
//    	    System.out.println("debug dmDocument - imSpecDocSubTitle:" + imSpecDocSubTitle);
    	    
    	    configInputStr= props.getProperty("debugFlag");
    	    if (configInputStr != null && configInputStr.compareTo("true") == 0) debugFlag = true;
//    	    System.out.println("debug dmDocument - debugFlag:" + debugFlag);
    	    reader.close();
    	} catch (FileNotFoundException ex) {
    	    // file does not exist
    		System.out.println(">>error    - Configuration file does not exist. [config.properties]");
    	} catch (IOException ex) {
    	    // I/O error
    		System.out.println(">>error    - Configuration file IO Exception. [config.properties]");
    	}
		
		// get the command line arguments
		getCommandArgs (args);
				
		// output the context info
		if (! LDDToolFlag) {
			System.out.println(">>info    - DMDoc Version: " + DMDocVersionId);
		} else {
			System.out.println(">>info    - LDDTOOL Version: " + LDDToolVersionId);
//			System.out.println(">>info    - Input File Name: " + LDDToolFileNameNE);
//1			System.out.println(">>info    - Original Input File Name: " + lLDDToolFileName);
		}
		System.out.println(">>info    - Date: " + sTodaysDate);
		System.out.println(">>info    - JAVAHOME: " + lJAVAHOME);
		System.out.println(">>info    - PARENT_DIR: " + lPARENT_DIR);
		System.out.println(">>info    - SCRIPT_DIR: " + lSCRIPT_DIR);
		System.out.println(">>info    - LIB_DIR: " + lLIB_DIR);
//		System.out.println(">>info    - USERNAME: " + lUSERNAME);
		
		// check the files
		checkRequiredFiles ();

		if (LDDToolFlag) {
			for (Iterator <SchemaFileDefn> i = LDDSchemaFileSortArr.iterator(); i.hasNext();) {
				SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
				cleanupLDDInputFileName(lSchemaFileDefn);
			}
		}
		// get the disposition file, parse out allowed stewards and namespaceids
		XMLDocParserDomMDPTNConfig lMDPTNConfig = new XMLDocParserDomMDPTNConfig();
		masterClassDispoMap2 = lMDPTNConfig.getXMLTable2(dataDirPath + "MDPTNConfigClassDisp.xml"); 
		masterClassDispoArr = new ArrayList <DispDefn> (masterClassDispoMap2.values());
		masterStewardArr = new ArrayList <String> ();
		masterNameSpaceIDArr = new ArrayList <String> ();
		masterStewardNameSpaceIDArr = new ArrayList <String> ();
		
		TreeMap <String, String> lStewardMap = new TreeMap <String, String> ();
		TreeMap <String, String> lNameSpaceIdMap = new TreeMap <String, String> ();
		TreeMap <String, String> lStewardNameSpaceIdMap = new TreeMap <String, String> ();
		
		// get the allowed stewards and namespaces from the configuration file
		for (Iterator <DispDefn> i = masterClassDispoArr.iterator(); i.hasNext();) {
			DispDefn lDispDefn = (DispDefn) i.next();
			String lStewardNameSpaceId = lDispDefn.intSteward + " - " + lDispDefn.intNSId;
			if (! masterStewardNameSpaceIDArr.contains(lStewardNameSpaceId)) {
				masterStewardNameSpaceIDArr.add(lStewardNameSpaceId);
				lStewardNameSpaceIdMap.put(lStewardNameSpaceId, lStewardNameSpaceId);
			}
			if (! masterStewardArr.contains(lDispDefn.intSteward)) {
				masterStewardArr.add(lDispDefn.intSteward);
				lStewardMap.put(lDispDefn.intSteward, lDispDefn.intSteward);
			}
			if (! masterNameSpaceIDArr.contains(lDispDefn.intNSId)) {
				masterNameSpaceIDArr.add(lDispDefn.intNSId);
				lNameSpaceIdMap.put(lDispDefn.intNSId, lDispDefn.intNSId);
			}
		}
		masterStewardArr = new ArrayList <String> (lStewardMap.values());
		masterNameSpaceIDArr = new ArrayList <String> (lNameSpaceIdMap.values());
		masterStewardNameSpaceIDArr = new ArrayList <String> (lStewardNameSpaceIdMap.values());
		
		// print out the stewards - namespaceid pairs
		System.out.println("\n>>info    - Configured Steward/NameSpaceId Pairs");
		for (Iterator <String> i = masterStewardNameSpaceIDArr.iterator(); i.hasNext();) {
			String lStewardNameSpaceId = (String) i.next();
			System.out.println(">>info    - " + lStewardNameSpaceId);
		}
		System.out.println("");
		
		// print out the stewards
//		System.out.println("\n>>info    - Configured Stewards");
//		for (Iterator <String> i = masterStewardArr.iterator(); i.hasNext();) {
//			String lSteward = (String) i.next();
//			System.out.println(">>info    - " + lSteward);
//		}

		// print out the namespaceid
//		System.out.println("\n>>info    - Configured NameSpace Ids");
//		for (Iterator <String> i = masterNameSpaceIDArr.iterator(); i.hasNext();) {
//			String lNameSpaceid = (String) i.next();
//			System.out.println(">>info    - " + lNameSpaceid);
//		}
//		System.out.println("\n");
		
		// set up the System Build version
//		XMLSchemaLabelBuildNum = "6a";
		XMLSchemaLabelBuildNum = pds4BuildId;

		// set up the Master Schema Information for both normal and LDD processing (dirpath, namespaces, etc)
		masterSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();
		SchemaFileDefn lSchemaFileDefn;
		lSchemaFileDefn = new SchemaFileDefn("pds");
		lSchemaFileDefn.versionId = infoModelVersionId;
		lSchemaFileDefn.labelVersionId = schemaLabelVersionId;
		lSchemaFileDefn.lddName = "Common Dictionary";
		lSchemaFileDefn.sourceFileName = "N/A";
		lSchemaFileDefn.isMaster = true;
		lSchemaFileDefn.stewardArr.add("pds");
		lSchemaFileDefn.stewardArr.add("img");
		lSchemaFileDefn.stewardArr.add("ops");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		masterPDSSchemaFileDefn = lSchemaFileDefn;
		
		// set up the remaining Schema Definitions		
		if (! DMDocument.LDDToolFlag) {
			setupNameSpaceInfoAll();
		} else {
			// only output the master LDD schema
			masterSchemaFileSortMap.put(masterLDDSchemaFileDefn.identifier, masterLDDSchemaFileDefn);
		}

		// get the 11179 Attribute Dictionary - .pins file
		ProtPins11179DD protPins11179DD  = new ProtPins11179DD ();
		protPins11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "dd11179.pins");
		
		// get the models
		GetModels lGetModels = new GetModels();
		lGetModels.getModels (PDSOptionalFlag, docFileName + ".pins");
		
//		System.out.println("\ndebug DMDocument - GetDomClasses ");
		if (! DMDocument.LDDToolFlag) {
			GetDomClasses lGetDomClasses = new GetDomClasses ();
			lGetDomClasses.convert();
		}
		
		// export the models
		if (! DMDocument.LDDToolFlag) {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeAllArtifacts ();
		} else {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeLDDArtifacts ();
		}
	}
	
/**********************************************************************************************************
	local utilities
***********************************************************************************************************/

	static private void getEnvMap () {
        Map<String, String> env = System.getenv();

    	lJAVAHOME = env.get("JAVA_HOME");
    	if (lJAVAHOME == null) {
    		System.out.println(">>error    - Environment variable JAVAHOME is null");
			System.exit(1);
    	}
    	lJAVAHOME =  replaceString (lJAVAHOME, "\\", "/");
    	
    	lPARENT_DIR = env.get("PARENT_DIR");
    	if (lPARENT_DIR == null) {
    		System.out.println(">>error    - Environment variable PARENT_DIR is null");
			System.exit(1);
    	}
    	lPARENT_DIR =  replaceString (lPARENT_DIR, "\\", "/");
    	
    	lSCRIPT_DIR = env.get("SCRIPT_DIR");
    	if (lSCRIPT_DIR == null) {
    		System.out.println(">>error    - Environment variable SCRIPT_DIR is null");
			System.exit(1);
    	}
    	lSCRIPT_DIR =  replaceString (lSCRIPT_DIR, "\\", "/");
    	
    	lLIB_DIR = env.get("LIB_DIR");
    	if (lLIB_DIR== null) {
    		System.out.println(">>error    - Environment variable LIB_DIR is null");
			System.exit(1);
    	}
    	lLIB_DIR =  replaceString (lLIB_DIR, "\\", "/");	
	}
	
	static private void getCommandArgs (String args[]) {
		for (int aind = 0; aind < args.length; aind++) {
			String lArg = args[aind];
//			System.out.println ("debug lArg:" + lArg);
			if (lArg.indexOf('-') == 0) {
//				System.out.println ("debug lFlag:" + lArg);
				String lFlag = lArg;
				if (lArg.indexOf('l') > -1) {
					LDDToolFlag = true;
//					LDDToolGeometry = "yrtemoeG";  // geometry backwards so that Geometry LDD can be parsed.
				}
				if (lArg.indexOf('d') > -1) {
					LDDToolAnnotateDefinitionFlag = true;
				}
				if (lArg.indexOf('M') > -1) {
					LDDToolMissionGovernanceFlag = true;
				}
				if (lArg.indexOf('m') > -1) {
					PDS4MergeFlag = true;
				}
				if (lArg.indexOf('n') > -1) {
					LDDNuanceFlag = true;
				}
				if (lArg.indexOf('c') > -1) {
					LDDClassElementFlag = true;
				}
				if (lArg.indexOf('a') > -1) {
//					LDDAttrElementFlag = true;
					LDDAttrElementFlag = false;
				}
				if (lArg.indexOf('p') > -1) {
					PDSOptionalFlag = true;
				}
				if (lArg.indexOf('h') > -1) {
					printHelp();
					System.exit(0);
				}
				if (lArg.indexOf('v') > -1) {
//					printHelp();
					System.out.println("LDDTOOL Version: " + LDDToolVersionId);
					System.exit(0);
				}
				if (lArg.indexOf('D') > -1) {
					exportProdDefnFlag = false;
				}
				if (lArg.indexOf('J') > -1) {
					exportJSONFileFlag = true;
				}
				if (lArg.indexOf('s') > -1) {
					LDDSyncFileNameFlag = true;
				}
				if (lArg.indexOf('1') > -1) {
					exportSpecFileFlag = true;
				}
/*				if (lArg.indexOf('2') > -1) {
					exportDDFileFlag = true;
				} */
			} else {
				SchemaFileDefn lLDDSchemaFileDefn = new SchemaFileDefn(lArg);
				lLDDSchemaFileDefn.sourceFileName = lArg;
				lLDDSchemaFileDefn.isLDD = true;
				lLDDSchemaFileDefn.labelVersionId = "1.0";
				LDDSchemaFileSortArr.add(lLDDSchemaFileDefn);
				masterLDDSchemaFileDefn = lLDDSchemaFileDefn;
//				System.out.println("debug getCommandArgs - lSchemaFileDefn.sourceFileName:" + lLDDSchemaFileDefn.sourceFileName);
			}	
		}

		// validate the input arguments
		if (! PDSOptionalFlag) {
			System.out.println(">>error   - " + "The -p option must be used for PDS4 processing");
			printHelp();
			System.exit(1);
		}
		
		if (LDDToolFlag) {
			if (! (lPARENT_DIR.indexOf("LDDTool") > -1)) {
				System.out.println(">>error   - " + "The directory LDDTool needs to be in the path " + lPARENT_DIR);
				printHelp();
				System.exit(1);
			}
			if (LDDToolMissionGovernanceFlag) {
				governanceLevel = "Mission";
				LDDToolSingletonClassTitle = "Mission_Area";
			} else {
				governanceLevel = "Discipline";
				LDDToolSingletonClassTitle = "Discipline_Area";
			}
		} else {
			if ((lPARENT_DIR.indexOf("LDDTool") > -1)) {
				System.out.println(">>error   - " + "The -l argument must be used for LDDTool processing");
				printHelp();
				System.exit(1);
			}
		}
	}

	static private void cleanupLDDInputFileName (SchemaFileDefn lSchemaFileDefn) {
		boolean hasExtension = false;
		boolean isFullPath = false;
		String lSourceFileSpec = lSchemaFileDefn.sourceFileName;
		lSourceFileSpec =  replaceString (lSourceFileSpec, "\\", "/");
		String lSourceFileSpecToLower = lSourceFileSpec.toLowerCase();
		int xmlInd = lSourceFileSpecToLower.indexOf(".xml");
		if (xmlInd > 0) hasExtension = true;
		int firstSlashInd = lSourceFileSpec.indexOf("/");
		if (firstSlashInd == 0) isFullPath = true;
		int lastSlashInd = lSourceFileSpec.lastIndexOf("/");
		if ((!isFullPath) && lastSlashInd > 0) {
			System.out.println(">>error   - " + "Input filename is invalid: " + lSchemaFileDefn.sourceFileName + " - filename[.xml] or fullpath allowed");
			printHelp();
			System.exit(1);
		}
		
		String lSourceFileNameNE = lSourceFileSpec;
		if (hasExtension) lSourceFileNameNE = lSourceFileSpec.substring(0, xmlInd);
		
		if (isFullPath) {
			lSchemaFileDefn.LDDToolInputFileName = lSourceFileSpec;
			if (lastSlashInd > 0) lSchemaFileDefn.LDDToolOutputFileNameNE = lSourceFileNameNE.substring(lastSlashInd, lSourceFileNameNE.length());
		} else {
			if (hasExtension) {
				lSchemaFileDefn.LDDToolInputFileName = lSourceFileSpec;
				lSchemaFileDefn.LDDToolOutputFileNameNE = lSourceFileNameNE;
			} else {
				lSchemaFileDefn.LDDToolInputFileName = lSourceFileSpec + ".xml";
				lSchemaFileDefn.LDDToolOutputFileNameNE = lSourceFileNameNE;
			}
		}

		if (! checkFileName (lSchemaFileDefn.LDDToolInputFileName)) {
			lSchemaFileDefn.LDDToolInputFileName = lSchemaFileDefn.LDDToolInputFileName.toLowerCase();
			if (! checkFileName (lSchemaFileDefn.LDDToolInputFileName)) {
				lSchemaFileDefn.LDDToolInputFileName = lSchemaFileDefn.LDDToolInputFileName.toUpperCase();
				if (! checkFileName (lSchemaFileDefn.LDDToolInputFileName)) {
					System.out.println(" ");
					System.out.println(">>error   - " + "Input file not found: " + lSchemaFileDefn.sourceFileName);
					printHelp();
					System.exit(1);
				}
			}
		}
//		System.out.println(">>info    - Input File Name Checked: " + lSchemaFileDefn.LDDToolInputFileName);
	}
	
	static public boolean checkFileName (String inputFileName) {
		File file=new File (inputFileName);
		if (file.exists()) {
			System.out.println (">>info    - Found input file: " + inputFileName);
			return true;
		}
		System.out.println(">>error   - " + "Input file not found: " + inputFileName);
		return false;
	}	
		
	static public void printHelp () {
		System.out.println("\nUsage: lddtool -pl [OPTION]... FILE1 FILE2 ... ");
		System.out.println("Parse a local data dictionary definition file and generate PDS4 data standard files.");

		System.out.println("\nExample: lddtool -pl  inputFileName");
		
		System.out.println("\nProcess control:");
		System.out.println("  -p, --PDS4      Set the context to PDS4");
		System.out.println("  -l, --LDD       Process a local data dictionary input file");
		System.out.println("  -a, --attribute Write definitions for attribute elements.");
		System.out.println("  -c, --class     Write definitions for class elements.");
		System.out.println("  -J, --JASON     Write the master data dictionary to a JASON formatted file.");
		System.out.println("  -m, --merge     Generate file to merge the local dictionary into the master dictionary");
		System.out.println("  -M, --Mission   Indicates mission level governance (includes msn directory specification)");
		System.out.println("  -n, --nuance    Write nuance property maps to LDD schema annotation in JASON");
		System.out.println("  -s, --sync      Use local namespace + information model version as output file names.");
		System.out.println("  -1, --IM Spec   Write the Information Model Specification with LDD.");
		System.out.println("  -v, --version   Returns the LDDTool version number");
		System.out.println("  -h, --help      Print this message");
		
		System.out.println("\nInput control:");
		System.out.println("  FILEn provides the file name of an input file. The file name extension .xml is assumed.");
		System.out.println("    If there are more than one file, the first files are considered references");
		System.out.println("    for the last file. The last file is considered the primary local data dictionary.");

		System.out.println("\nOutput control:");
		System.out.println("  FILE is used to provide the file name for the output files. The file name extensions are distinct.");
		System.out.println("  .xsd -- XML Schema file");
		System.out.println("  .sch -- schematron file");
		System.out.println("  .xml -- label file");
		System.out.println("  .csv -- data dictionary information in csv formatted file.");
		System.out.println("  .JSON -- dump of model in JSON format.");
		System.out.println("  .txt -- process report in text format");
		System.out.println("  .pont -- ontology file for merge");
		System.out.println(" ");
	}
	
	static public void checkRequiredFiles () {
		// check that all the required data files exist
		File file=new File (dataDirPath + "MDPTNConfigClassDisp.xml");
		boolean isFound = file.exists();
		if (!isFound) {
			  System.out.println(">>error   - " + "Required data file was not found: " + dataDirPath + "MDPTNConfigClassDisp.xml");
			  System.exit(1);
		}
		file=new File (dataDirPath + "UpperModel.pont");
		isFound = file.exists();
		if (!isFound) {
			  System.out.println(">>error   - " + "Required data file was not found: " + dataDirPath + "UpperModel.pont");
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "dd11179.pins");
		isFound = file.exists();
		if (!isFound) {
			  System.out.println(">>error   - " + "Required data file was not found: " + dataDirPath + "dd11179.pins");
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "Glossary.pins");
		isFound = file.exists();
		if (!isFound) {
			  System.out.println(">>error   - " + "Required data file was not found: " + dataDirPath + "Glossary.pins");
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "DMDocument.pins");
		isFound = file.exists();
		if (!isFound) {
			  System.out.println(">>error   - " + "Required data file was not found: " + dataDirPath + "DMDocument.pins");
			  System.exit(1);
		}
	}
	
//	setup namespace information (for XML Schemas, etc)
	static void setupNameSpaceInfoAll () {
		SchemaFileDefn lSchemaFileDefn;
		
		// *** namespace is set by argument to  constructor ***
		
		// Schema file Definitions by namespaceid
		lSchemaFileDefn = new SchemaFileDefn("atm");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("atm");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("geo");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("geo");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("img");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.17";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("img");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);

		/*
		PDS4_IMG_1300	1.13
		PDS4_IMG_1400	1.14
		PDS4_IMG_1410	1.15
		PDS4_IMG_1500	1.16
		PDS4_IMG_1510	1.17
		*/
		
		lSchemaFileDefn = new SchemaFileDefn("naif");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("naif");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("ppi");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("ppi");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("rings");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("rings");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("rs");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("rs");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("sbn");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("sbn");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
/*		lSchemaFileDefn = new SchemaFileDefn("msn");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = false;
		lSchemaFileDefn.stewardArr.add("msn");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);		*/
		
		lSchemaFileDefn = new SchemaFileDefn("disp");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("img");
		lSchemaFileDefn.setVersionIds();
		lSchemaFileDefn.comment = "This dictionary describes how to display Array data on a display device.";
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);		

		lSchemaFileDefn = new SchemaFileDefn("geom");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("geo");
		lSchemaFileDefn.setVersionIds();
		lSchemaFileDefn.comment = "This dictionary describes geometry.";
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);		
		
		lSchemaFileDefn = new SchemaFileDefn("cart");
		lSchemaFileDefn.versionId = "1.7.0.0";
		lSchemaFileDefn.labelVersionId = "1.11";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.isDiscipline = true;
		lSchemaFileDefn.stewardArr.add("cart");
		lSchemaFileDefn.setVersionIds();
		lSchemaFileDefn.comment = "This dictionary describes cartography.";
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		return;
	}
	
/**********************************************************************************************************
	global utilities 
***********************************************************************************************************/
	
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
	*  check to see if string is numeric
	*/
	static public boolean isInteger (String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i <  sb.length (); i++) {
			if (! Character.isDigit(sb.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	*  get a UTC Formated Date/Time from the machine date
	*/
	static String getUTCDateTime () {
		String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		String lDateUTC = sdf.format(new Date());
		return lDateUTC;
	}
	
	/**
	*  get a UTC Formated Date from the machine date
	*/
	static String getUTCDate () {
		String DATEFORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
//		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		String lDateUTC = sdf.format(new Date());
		return lDateUTC;
	}	

	/**
	*  convert from string to hex
	*/
	static String stringToHex(String string) {
		  StringBuilder buf = new StringBuilder(200);
		  for (char ch: string.toCharArray()) {
		    if (buf.length() > 0)
		      buf.append(' ');
		    buf.append(String.format("%04x", (int) ch));
		  }
		  return buf.toString();
		}
	
	/**
	 *   get the disposition of a class (from Protege)
	 */
	static public PDSObjDefn getClassDisposition (PDSObjDefn lClass, String lClassName, boolean isFromProtege) {
		// get disposition identifier - if isFromProtege, then the identifier is set else it is not since it is from an LDD.
		String lDispId = lClass.subModelId + "." + registrationAuthorityIdentifierValue + "." + lClassName;
//		System.out.println("\ndebug getClassDisposition1 lDispId:" + lDispId);
		if (! isFromProtege) lDispId = "LDD_" + lDispId;
//		System.out.println("debug getClassDisposition2 lDispId:" + lDispId);
		DispDefn lDispDefn = masterClassDispoMap2.get(lDispId);
		if (lDispDefn != null) {
//			System.out.println("debug getClassDisposition3 lDispId:" + lDispId);
			lClass.section = lDispDefn.section;
			String lDisp = lDispDefn.disposition;
//			String lStewardInd = lDisp.substring(0, 1);
//			String lSteward = DMDocument.masterClassStewardMap.get(lStewardInd);
//			lClass.steward = lSteward;
			lClass.steward = lDispDefn.intSteward;
			String lClassNameSpaceIdNC = lDispDefn.intNSId;
			lClass.nameSpaceIdNC = lClassNameSpaceIdNC;
			lClass.nameSpaceId = lClassNameSpaceIdNC + ":";
			
			// if from protege, the identifier needs to be set; if from LDD it cannot be set here.
			if (isFromProtege) lClass.identifier = InfoModel.getClassIdentifier(lClassNameSpaceIdNC, lClassName);
			lClass.isMasterClass = true;
			if (lDisp.indexOf("V") > -1) {
				lClass.isVacuous = true;
			}
			if (lDisp.indexOf("S") > -1) {
				lClass.isSchema1Class = true;
			}
			if (lDisp.indexOf("R") > -1) {
				lClass.isRegistryClass = true;
			}
			if (lDisp.indexOf("T") > -1) {
				lClass.isTDO = true;
			}
			if (lDisp.indexOf("d") > -1) {
				lClass.isDataType = true;
			}
			if (lDisp.indexOf("u") > -1) {
				lClass.isUnitOfMeasure = true;
			}
/*			if (lDisp.indexOf("E") > -1) {
				lClass.versionId = "1.1.0.0";
			} else if (lDisp.indexOf("D") > -1) {
				lClass.versionId = "1.2.0.0";
			} else if (lDisp.indexOf("F") > -1) {
				lClass.versionId = "1.n.0.0";
			} */
			return lClass;
		}
		return null;
	}	
}
