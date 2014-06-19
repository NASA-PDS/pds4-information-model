package gov.nasa.pds.model.plugin;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

/** Main for creating Document
 *
 */
public class DMDocument extends Object {
	
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
//	static final String TopLevelAttrClassName  = "CLIPS_TOP_LEVEL_SLOT_CLASS";
	static final String TopLevelAttrClassName  = "%3ACLIPS_TOP_LEVEL_SLOT_CLASS";
	
	static boolean PDSOptionalFlag;

	static String dataDirPath  = "TBD_dataDirPath";
	static String outputDirPath  = "./";

	static String DMDocVersionId  = "0.1.5";
	static String LDDToolVersionId  = "0.1.8.5";
	static String classVersionIdDefault = "1.0.0.0";
	static boolean PDS4MergeFlag  = false;
	static boolean LDDClassElementFlag = false;			// if true, write XML elements for classes
	static boolean LDDAttrElementFlag = false;			// if true, write  XML elements for attributes
	static String lLDDToolFileName  = "TBD_lLDDToolFileName.xml";	// original LDDTool input file name
	static String LDDToolFileName  = "TBD_LDDToolFileName.xml";
	static String LDDToolFileNameNE  = "TBD_LDDToolFileName";
	static String LDDToolOutputFileNameNE  = "TBD_LDDToolOutputFileNameNE";
	
	static boolean LDDToolFlag;
	static boolean LDDToolAnnotateDefinitionFlag;
	
	// USER class attributes for access in LDDTool
//	static ArrayList <AttrDefn> topLevelDictArr;
//	static HashMap <String, AttrDefn> topLevelDictMap;
	static TreeMap <String, AttrDefn> userClassAttributesMapId;
	
	
	// Master Model
	static MasterInfoModel masterInfoModel;
	
	// namespaces
	static TreeMap <String, String> masterClassStewardMap;
	static TreeMap <String, String> masterClassStewardSortMap;
	static TreeMap <String, SchemaFileDefn> masterSchemaFileSortMap;
	
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
	
	// uid
	static int masterUId;
	
	// class disposition maps
	static TreeMap <String, DispDefn> masterClassDispoMap2;
	
	// registry classes and attributes
	static ArrayList <String> registryClass;
	static ArrayList <String> registryAttr;
	
	// Omitted classes
	static ArrayList <String> omitClass;
	
	// the pds namespaceid master schema file information
	static SchemaFileDefn masterPDSSchemaFileDefn; 		
		
	// the set of deprecated classes, attributes, and values	
	static ArrayList <DeprecatedDefn> deprecatedObjects2;
	static ArrayList <String> deprecatedAttrValueArr;
	static String Literal_DEPRECATED = " *Deprecated*";
	static boolean deprecatedAdded;

	// need a place to store the LDD schema file definition until it is created.
//	static String LDDToolSchemaFileVersionId;


/**********************************************************************************************************
	main
***********************************************************************************************************/
	
	public static void main (String args[]) throws Throwable {
		
//		boolean PDSOptionalFlag = false;
		PDSOptionalFlag = false;
		LDDToolFlag = false;
		LDDToolAnnotateDefinitionFlag = false;
		
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
//		versionIdentifierValue = InfoModel.identifier_version_id;
		administrationRecordValue = "TBD_administrationRecordValue"; // set in GetModels
		stewardValue = "Steward_PDS";
		submitterValue = "Submitter_PDS";
		
		
		registeredByValue = "RA_0001_NASA_PDS_1"; 
		registrationAuthorityIdentifierValue = "0001_NASA_PDS_1";

		masterUId = 100000000;
		
		masterClassStewardMap = new TreeMap <String, String> ();
		masterClassStewardMap.put("1", "pds");
		masterClassStewardMap.put("2", "atm");
		masterClassStewardMap.put("3", "geo");
		masterClassStewardMap.put("4", "img");
		masterClassStewardMap.put("5", "naif");
		masterClassStewardMap.put("6", "ppi");
		masterClassStewardMap.put("7", "rings");
		masterClassStewardMap.put("8", "rs");
		masterClassStewardMap.put("9", "sbn");
		masterClassStewardMap.put("m", "msn");
		masterClassStewardMap.put("o", "ops");
		masterClassStewardMap.put("^", "meta");

		masterClassStewardSortMap = new TreeMap <String, String> ();
		masterClassStewardSortMap.put("pds"  ,"01");
		masterClassStewardSortMap.put("atm"  ,"02");
		masterClassStewardSortMap.put("geo"  ,"03");
		masterClassStewardSortMap.put("img"  ,"04");
		masterClassStewardSortMap.put("naif" ,"05");
		masterClassStewardSortMap.put("ppi"  ,"06");
		masterClassStewardSortMap.put("rings","07");
		masterClassStewardSortMap.put("rs"   ,"08");
		masterClassStewardSortMap.put("sbn"  ,"09");
		masterClassStewardSortMap.put("msn"  ,"10");
		masterClassStewardSortMap.put("ops"  ,"11");
		masterClassStewardSortMap.put("meta" ,"12");
		
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
//		deprecatedObjects2.add(new DeprecatedDefn ("pds.Display_2D_Image", "pds", "Display_2D_Image", "", ""));	
//		deprecatedObjects2.add(new DeprecatedDefn ("pds.Band_Bin_Set", "pds", "Band_Bin_Set", "", ""));	
		deprecatedObjects2.add(new DeprecatedDefn ("pds.Axis_Array.unit", "pds", "Axis_Array", "unit", ""));
		deprecatedObjects2.add(new DeprecatedDefn ("pds.Instrument_Host", "pds", "Instrument_Host", "type", "Earth Based"));
		deprecatedObjects2.add(new DeprecatedDefn ("pds.Primary_Result_Summary.data_regime", "pds", "Primary_Result_Summary", "data_regime", ""));
		deprecatedObjects2.add(new DeprecatedDefn ("pds.Primary_Result_Summary.type", "pds", "Primary_Result_Summary", "type", ""));
		deprecatedObjects2.add(new DeprecatedDefn ("pds.Primary_Result_Summary.processing_level_id", "pds", "Primary_Result_Summary", "processing_level_id", ""));
		// get ArrayList for  testing
		deprecatedAttrValueArr = new ArrayList <String> ();
		for (Iterator <DeprecatedDefn> i = deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lObject = (DeprecatedDefn) i.next();
			if (lObject.value.compareTo("") != 0) {
				String lIdentifier = lObject.nameSpaceIdNC + "." + lObject.className + "." + lObject.attrName + "." + lObject.value;			
				deprecatedAttrValueArr.add(lIdentifier);
//				System.out.println("debug deprecatedObjects2 Value lIdentifier:" + lIdentifier);
				continue;
			}
			if (lObject.attrName.compareTo("") != 0) {
				String lIdentifier = lObject.nameSpaceIdNC + "." + lObject.className + "." + lObject.attrName;
				deprecatedAttrValueArr.add(lIdentifier);
//				System.out.println("debug deprecatedObjects2 Attr lIdentifier:" + lIdentifier);
				continue;
			}
			String lIdentifier = lObject.nameSpaceIdNC + "." + lObject.className;
			deprecatedAttrValueArr.add(lIdentifier);
//			System.out.println("debug deprecatedObjects2 Class lIdentifier:" + lIdentifier);
		}
				
		// get the environment variables
		getEnvMap();
		dataDirPath = lPARENT_DIR + "/Data/";
		
		// get the command line arguments
		getCommandArgs (args);
				
		// output the context info
		if (! LDDToolFlag) {
			System.out.println(">>info    - DMDoc Version: " + DMDocVersionId);
		} else {
			System.out.println(">>info    - LDDTOOL Version: " + LDDToolVersionId);
//			System.out.println(">>info    - Input File Name: " + LDDToolFileNameNE);
			System.out.println(">>info    - Original Input File Name: " + lLDDToolFileName);
		}
		System.out.println(">>info    - Date: " + sTodaysDate);
		System.out.println(">>info    - JAVAHOME: " + lJAVAHOME);
		System.out.println(">>info    - PARENT_DIR: " + lPARENT_DIR);
		System.out.println(">>info    - SCRIPT_DIR: " + lSCRIPT_DIR);
		System.out.println(">>info    - LIB_DIR: " + lLIB_DIR);
//		System.out.println(">>info    - USERNAME: " + lUSERNAME);
		
		// check the files
//		checkFiles ();
		checkRequiredFiles ();
		if (LDDToolFlag) parseValidateLDDFileName(lLDDToolFileName);
		
		// get the disposition file
		XMLDocParserDomMDPTNConfig lMDPTNConfig = new XMLDocParserDomMDPTNConfig();
		masterClassDispoMap2 = lMDPTNConfig.getXMLTable2(dataDirPath + "MDPTNConfigClassDisp.xml"); 
		
		// set up the Master Schema Information for both normal and LDD processing (dirpath, namespaces, etc)
		masterSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();
		SchemaFileDefn lSchemaFileDefn;
		lSchemaFileDefn = new SchemaFileDefn("pds");
//		lSchemaFileDefn.versionId = "1.1.0.0";
		lSchemaFileDefn.versionId = "1.1.0.1";
//		lSchemaFileDefn.nameSpaceIdNC = "pds";
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
		} 

		// get the 11179 Attribute Dictionary - .pins file
		ProtPins11179DD protPins11179DD  = new ProtPins11179DD ();
		protPins11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "dd11179.pins");
		
		// get the models
		GetModels lGetModels = new GetModels();
		lGetModels.getModels (PDSOptionalFlag, docFileName + ".pins");
		
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

	static public void getEnvMap () {
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
	
	static public void getCommandArgs (String args[]) {
		for (int aind = 0; aind < args.length; aind++) {
			String lArg = args[aind];
//			System.out.println ("debug lArg:" + lArg);
			if (lArg.indexOf('-') == 0) {
//				System.out.println ("debug lFlag:" + lArg);
				String lFlag = lArg;
				if (lArg.indexOf('l') > -1) {
					LDDToolFlag = true;
				}
				if (lArg.indexOf('d') > -1) {
					LDDToolAnnotateDefinitionFlag = true;
				}
				if (lArg.indexOf('m') > -1) {
					PDS4MergeFlag = true;
				}
				if (lArg.indexOf('c') > -1) {
					LDDClassElementFlag = true;
				}
				if (lArg.indexOf('a') > -1) {
					LDDAttrElementFlag = true;
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
			} else {
//				parseLDDFileName(lArg);
				lLDDToolFileName = lArg;
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
		} else {
			if ((lPARENT_DIR.indexOf("LDDTool") > -1)) {
				System.out.println(">>error   - " + "The -l argument must be used for LDDTool processing");
				printHelp();
				System.exit(1);
			}
		}
	}

	static public void parseValidateLDDFileName (String lLDDToolFileName) {
		LDDToolFileName = lLDDToolFileName;
		LDDToolFileName =  replaceString (LDDToolFileName, "\\", "/");
		LDDToolFileNameNE = LDDToolFileName;
		String lInputFileNameToLower = lLDDToolFileName.toLowerCase();
		int xmlInd = lInputFileNameToLower.indexOf(".xml");
		if (xmlInd < 0) {
			LDDToolFileName += ".xml";
		} else {
			LDDToolFileNameNE = LDDToolFileName.substring(0, xmlInd);
		}
		LDDToolOutputFileNameNE = LDDToolFileNameNE;
		if (! checkFileName (LDDToolFileName)) {
			LDDToolFileName = LDDToolFileName.toLowerCase();
			if (! checkFileName (LDDToolFileName)) {
				LDDToolFileName = LDDToolFileName.toUpperCase();
				if (! checkFileName (LDDToolFileName)) {
					System.out.println(" ");
					printHelp();
					System.exit(1);
				}
			}
		}
		System.out.println(">>info    - Input File Name Checked: " + LDDToolFileName);
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
		
	static public void parseLDDFileNamexxx (String inputFileName) {
		LDDToolFileName = inputFileName;
		LDDToolFileName =  replaceString (LDDToolFileName, "\\", "/");
		LDDToolFileNameNE = LDDToolFileName;
		String lInputFileNameToLower = inputFileName.toLowerCase();
		int xmlInd = lInputFileNameToLower.indexOf(".xml");
		if (xmlInd < 0) {
			LDDToolFileName += ".xml";
		} else {
			LDDToolFileNameNE = LDDToolFileName.substring(0, xmlInd);
		}
		LDDToolOutputFileNameNE = LDDToolFileNameNE;
	}
		
	static public void printHelp () {
		System.out.println("\nUsage: lddtool -pl [OPTION]... FILE ");
		System.out.println("Parse a local data dictionary definition file and generate PDS4 data standard files.");

		System.out.println("\nExample: lddtool -pl  inputFileName");
		
		System.out.println("\nProcess control:");
		System.out.println("  -p, --PDS4      Set the context to PDS4");
		System.out.println("  -l, --LDD       Process a local data dictionary input file");
		System.out.println("  -c, --class     Write definitions for class elements.");
		System.out.println("  -a, --attribute Write definitions for attribute elements.");
		System.out.println("  -m, --merge     Generate file to merge the local dictionary into the master dictionary");
		System.out.println("  -h, --help      Print this message");
		
		System.out.println("\nInput control:");
		System.out.println("  FILE provides the file name of the input file. The file name extension .xml is assumed.");

		System.out.println("\nOutput control:");
		System.out.println("  FILE is used to provide the file name for the output files. The file name extensions are distinct.");
		System.out.println("  .xsd -- XML Schema file");
		System.out.println("  .sch -- schematron file");
		System.out.println("  .csv -- data dictionary information in csv formatted file.");
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
		
		// Schema file Definitions by namespaceid
		lSchemaFileDefn = new SchemaFileDefn("atm");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "atm";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("atm");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("geo");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "geo";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("geo");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("img");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "img";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("img");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("naif");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "naif";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("naif");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("ppi");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "ppi";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("ppi");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("rings");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "rings";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("rings");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("rs");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "rs";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("rs");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("sbn");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "sbn";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("sbn");
		lSchemaFileDefn.setVersionIds();
		masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
		
		lSchemaFileDefn = new SchemaFileDefn("msn");
		lSchemaFileDefn.versionId = "1.1.0.0";
//		lSchemaFileDefn.nameSpaceIdNC = "msn";
		lSchemaFileDefn.isMaster = false;
		lSchemaFileDefn.stewardArr.add("msn");
		lSchemaFileDefn.setVersionIds();
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
}
