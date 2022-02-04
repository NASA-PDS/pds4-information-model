// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Main for creating Document
 *
 */ 
public class DMDocument extends Object {
	
	// environment variables
	static String lPARENT_DIR;
	static String lSCRIPT_DIR;
	static String lLIB_DIR;
	static String lUSERNAME;
	
	// specification document info
	static DocDefn docInfo;
	static final String docFileName  = "DMDocument";
	static final String upperModelFileName  = "UpperModel.pont";
	
	// variables for the class %3ACLIPS_TOP_LEVEL_SLOT_CLASS
	static final String TopLevelAttrClassName = "%3ACLIPS_TOP_LEVEL_SLOT_CLASS";
	static final String ANAME = "%3ANAME";
	
	// process state for used flags, files, and directories
	static DMProcessState dmProcessState;
	
	// set by -p flag; needs to remain however is deprecated for any processing; 
	// use mastModelId=PDS4 set in config.properties file
	static boolean PDSOptionalFlag;
	
	// configuration file variables
	static String infoModelVersionId = "0.0.0.0";
	static String schemaLabelVersionId = "0.0";
	static String pds4BuildId = "0a";
	
	static String imSpecDocTitle = "TBD_imSpecDocTitle";
	static String imSpecDocAuthor = "TBD_imSpecDocAuthor";
	static String imSpecDocSubTitle = "TBD_imSpecDocSubTitle";
	static String ddDocTitle = "TBD_ddDocTitle";
	static String ddDocTeam = "TBD_ddDocTeam";
	
	static String dataDirPath  = "TBD_dataDirPath";
	static String outputDirPath = "./";
 
	static String DMDocVersionId  = "0.0.0";
//	static String XMLSchemaLabelBuildNum = "6a";
	static String XMLSchemaLabelBuildNum;
	
	// Version   VID    MOD    Build
	// 1.0.0.0 - 1.0  - 1.0  - Build 3b
	// 1.1.0.0 - 1.1  - 1.1  - Build 4a
	// 1.1.0.1 - 1.2  - 1.2  - Build 4a
	// 1.2.0.0 - 1.3  - 1.3  - Build 4b - Desired
	// 1.2.0.0 - 1.3  - 1.3  - Build x4a - Actual
	// 1.2.0.1 - 1.4  - 1.4  - Build 4b
	// 1.3.0.0 - 1.5  - 1.5  - Build 5a
	// 1.3.0.1 - 1.6  - 1.6  - Build 5a

	// 1.4.0.0 - 1.7  - 1.7  - Build 5b
	// 1.4.0.0 - x1.3 - x1.6 - Build 5b - error
	// 1.4.0.1 - 1.8  - 1.8  - Build 5b - not released
	// 1.4.1.0 - 1.8  - 1.8  - Build 5b
	// 1.5.0.0 - 1.9  - 1.9  - Build 6a 
	// 1.5.1.0 - 1.9  - 1.9  - Build 6b - not released
	// 1.6.0.0 - 1.10 - 1.10 - Build 6b 
	// 1.7.0.0 - 1.11 - 1.11 - Build 7a
	// 1.8.0.0 - 1.12 - 1.12 - Build 7b
	// 1.9.0.0 - 1.13 - 1.13 - Build 8a
	// 1.9.1.0 - 1.14 - 1.14 - Build 8a
	// 1.10.0.0 - 1.15 - 1.15 - Build 8b - 1A00
	// 1.10.1.0 - 1.16 - 1.16 - Build 8b - 1A10
	// 1.11.0.0 - 1.17 - 1.17 - Build 9a - 1B00
	// 1.12.0.0 - 1.18 - 1.18 - Build 9b - 1C00
	// 1.13.0.0 - 1.19 - 1.19 - Build 10a - 1D00
	// 1.14.0.0 - 1.20 - 1.20 - Build 10b - 1E00
	// 1.15.0.0 - 1.21 - 1.21 - Build 11.0 - 1F00
	// 1.16.0.0 - 1.22 - 1.22 - Build 11.1 - 1G00
	// 1.17.0.0 - 1.23 - 1.23 - Build 12.0 - 1H00
	// 1.18.0.0 - 1.24 - 1.24 - Build 12.1 - 1I00
	// 1.19.0.0 - 1.25 - 1.25 - Build 13.0 - 1J00
	
	// x.x.x.x - 1.0 - 1.n - Build nm - first version of product will always be 1.0
	//									Modification history will continue with 1.n
	                         
	static String LDDToolVersionId  = "0.0.0";
	static String buildDate  = "";
	static String buildIMVersionId = "1.17.0.0";
	static String buildIMVersionFolderId = "1H00";
	static String classVersionIdDefault = "1.0.0.0";
	static boolean PDS4MergeFlag  = false;				// create protege output; not currently used
//	static boolean LDDClassElementFlag = false;			// if true, write XML elements for classes
	static boolean LDDAttrElementFlag = false;			// if true, write  XML elements for attributes
	static boolean LDDNuanceFlag = false;				//
	
	// refactor into Protege switches
	static boolean overWriteClass = true;				// use dd11179.pins class disp, isDeprecated, and versionId to overwrite Master DOMClasses, DOMAttrs, and DOMPermvalues
	static boolean useMDPTNConfig = true;				// ProtPontDOMModel; get disposition for the class from MDPTNConfigClassDisp
	static boolean overWriteDeprecated = false;			// use dd11179.pins isDeprecated to overwrite DMDocument.deprecatedObjects2
	
	// alternate IM Version
	// if no option "V" is provided on the command line, then the default is the current IM version.
	static boolean alternateIMVersionFlag = false;
	static String alternateIMVersion = buildIMVersionFolderId;  		// default
	// allowed alternate IM versions
	static ArrayList <String> alternateIMVersionArr;	
	
	// import export file flags
	static boolean exportJSONFileFlag = false;			// LDDTool, set by -J option
	static boolean exportJSONFileAllFlag = false;		// LDDTool, set by -6 option
	static boolean exportSpecFileFlag = false;
	static boolean exportDDFileFlag = false;
	static boolean importJSONAttrFlag = false;			// non PDS processing - not currently used
	static boolean exportOWLFileFlag = false;
	static boolean pds4ModelFlag = false;				// set in config.properties files (read by WriteDOMDocBook to exclude PDS3 from generated DD)
	static boolean printNamespaceFlag = false;			// print the configured namespaces to the log
	static boolean disciplineMissionFlag = false;		//  set by -d; Omit the term "mission" from the namespace of a Mission dictionary
	static int writeDOMCount = 0;						// *** Deprecate *** LDDParser DOM Error write count; if exportDOMFlag=true then DOM code is executed and so error/warning messages are duplicated in log and txt file.
	
	// when true this flag indicates an LDDTool run for a namespace other than pds (i.e., Common)
	static boolean LDDToolFlag;
	
	// misc flags
	static boolean debugFlag = false;
	
	// in an LDDTool run, when true indicates that a mission LDD is being processed
	// this flag was deprecated with the addition of <dictionary_type> to Ingest_LDD IM V1E00
	// however this flag is still needed for LDDTool runs with IM Versions prior to 1E00
	static boolean LDDToolMissionFlag = false;
	
	static boolean LDDToolAnnotateDefinitionFlag;
	static String LDDToolSingletonClassTitle = "USER";
	static DOMClass LDDToolSingletonDOMClass = null;
	
	static boolean mapToolFlag = false;
	
	// Master DOM Info Model
	static MasterDOMInfoModel masterDOMInfoModel;
	
	// Master and Secondary LDD LDDDOMParsers
	static LDDDOMParser primaryLDDDOMModel;
	static ArrayList <LDDDOMParser> LDDDOMModelArr;
	
	// Schemas, Stewards and Namespaces (SchemaFileDefn)
	
	// *** initialized from the config file - maybe rename, to be used only during initialization of LDDSchemaFileSortMap ***
	static TreeMap <String, SchemaFileDefn> masterAllSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();	// all namespaces in config.properties file.
	
	// *** to be use only for LDDs ***
	static TreeMap <String, SchemaFileDefn> LDDSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();		   
	static ArrayList <SchemaFileDefn> LDDSchemaFileSortArr;
	static ArrayList <String> LDDImportNameSpaceIdNCArr = new ArrayList <String> ();
	
	// *** to be used to detect issues by namespace
	static ArrayList <String> nameSpaceIdExtrnFlagArr = new ArrayList <String> ();	// <element_flag> set to true in each namespace
	
	// Master Schemas, Stewards and Namespaces (SchemaFileDefn)
	static SchemaFileDefn masterPDSSchemaFileDefn;
	static String masterNameSpaceIdNCLC = "TBD_masterNameSpaceIdNCLC";
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

	static String mastModelId;
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
	
	// registry classes and attributes
	static ArrayList <String> registryClass;
	static ArrayList <String> registryAttr;
	
	// reserved Class names
	static ArrayList <String> reservedClassNames;
	
	// reserved Attribute names
	static ArrayList <String> reservedAttrNames;
	
	// Omitted classes
	static ArrayList <String> omitClass;
		
	// the set of deprecated classes, attributes, and values	
	static ArrayList <DeprecatedDefn> deprecatedObjects2;
	static String Literal_DEPRECATED = " *Deprecated*";
	static boolean deprecatedAdded;
	static boolean deprecatedAddedDOM;
	
	// the set of classes and attributes that will be externalized (defined as xs:Element)	
	static ArrayList <String> exposedElementArr;
	
	// info, warning, and error messages
	static int msgOrder = 100000;
	static ArrayList <DOMMsgDefn> mainMsgArr = new ArrayList <DOMMsgDefn> ();
	static DOMMsgDefn masterDOMMsgDefn = new DOMMsgDefn ();
	static TreeMap <String, Integer> messageLevelCountMap = new TreeMap <String, Integer> ();
	static Integer lMessageWarningCount = 0;
	static Integer lMessageErrorCount = 0;
	static Integer lMessageFatalErrorCount = 0;
	
	// Property List for Config.Properties
	static Properties props = new Properties();
	
	static ArrayList <String> propertyMapFileName = new ArrayList <String> ();
	
	// command line argument parser
	static ArgumentParser parser;

/**********************************************************************************************************
	main
***********************************************************************************************************/
	
	public static void main (String args[]) throws Throwable {
		
		// process state for used flags, files, and directories
		dmProcessState = new DMProcessState ();
		
		PDSOptionalFlag = false;
		LDDToolFlag = false;
		// Secondary LDD Models
		LDDDOMModelArr = new ArrayList <LDDDOMParser> ();
		LDDSchemaFileSortArr = new ArrayList <SchemaFileDefn> ();
		LDDToolAnnotateDefinitionFlag = false;
		
		// The current version is included to allow for -V currentIMVersion
		alternateIMVersionArr = new ArrayList <String> ();
		alternateIMVersionArr.add ("1H00");
		alternateIMVersionArr.add ("1G00");
		alternateIMVersionArr.add ("1F00");
		alternateIMVersionArr.add ("1E00");
		alternateIMVersionArr.add ("1D00");
		alternateIMVersionArr.add ("1C00");
		alternateIMVersionArr.add ("1B10");
		alternateIMVersionArr.add ("1B00");
		
		//	message handling
		DOMMsgDefn.init();
		
		// message level counts
		Integer msgCount0info = 0;
		Integer msgCount0warning = 0;
		Integer msgCount0error = 0;
		Integer msgCount1info = 0;
		Integer msgCount1warning = 0;
		Integer msgCount1error = 0;
		Integer msgCount2info = 0;
		Integer msgCount2warning = 0;
		Integer msgCount2error = 0;
		Integer msgCount3error = 0;
		Integer msgCount4error = 0;
		messageLevelCountMap.put("0>info", msgCount0info);
		messageLevelCountMap.put("0>warning", msgCount0warning);
		messageLevelCountMap.put("0>error", msgCount0error);
		messageLevelCountMap.put("1>info", msgCount1info);
		messageLevelCountMap.put("1>warning", msgCount1warning);
		messageLevelCountMap.put("1>error", msgCount1error);
		messageLevelCountMap.put("2>info", msgCount2info);
		messageLevelCountMap.put("2>warning", msgCount2warning);
		messageLevelCountMap.put("2>error", msgCount2error);
		messageLevelCountMap.put("3>error", msgCount3error);
		messageLevelCountMap.put("4>error", msgCount4error);

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
		
//		registeredByValue = "RA_0001_NASA_PDS_1"; 
		registeredByValue = "TBD_registeredByValue"; 
		registrationAuthorityIdentifierValue = "TBD_registrationAuthorityIdentifierValue";

		// Master User Class Name
		masterUserClassNamespaceIdNC = "all";
		masterUserClassName = "USER";
		
		// master unique sequence number
		masterUId = 100000000;
		
		// master class order
		masterClassOrder = 1000;

		// master group number
		masterGroupNum = 10;
		
		// reserved Class names
		reservedClassNames = new ArrayList <String> ();
		reservedClassNames.add("Internal_Reference");
		reservedClassNames.add("Local_Internal_Reference");
		reservedClassNames.add("Reference_Pixel_Regression_Test");

		// reserved Attribute names
		reservedAttrNames = new ArrayList <String> ();
		reservedAttrNames.add("logical_identifier");
		reservedAttrNames.add("local_identifier");
		reservedAttrNames.add("pixel_latitude_Regression_Test");
		
		omitClass = new ArrayList <String> ();
		omitClass.add("Data_Object");
		omitClass.add("Digital_Object");
		omitClass.add("Physical_Object");
		omitClass.add("Conceptual_Object");
		
		// set registryAttr
		setRegistryAttrFlag ();
		
		// set exposed elements 
		setexposedElementFlag ();
				
		// get the command line arguments using argparse4j
		Namespace argparse4jNamespace = getArgumentParserNamespace(args);
		
		// process first set of arguments
		//    this must be done before config file processing
		//    the use of the option "V" (alternate IM version) will change the input file directory (config included)
		processArgumentParserNamespacePhase1(argparse4jNamespace);
		
		// first get the environment variables
		getEnvMap();
		dataDirPath = lPARENT_DIR + "/Data/";
		
		// if this is an LDDTool run then an alternate path is allowed (option "V")
		if (LDDToolFlag && alternateIMVersionFlag) {
			if (alternateIMVersion.compareTo(buildIMVersionFolderId) != 0)  {
				dataDirPath = lPARENT_DIR + "/Data/" + alternateIMVersion + "/";
			}
		}
		registerMessage ("0>info - IM Directory Path:" + dataDirPath);
		registerMessage ("0>info - IM Versions Available:" + alternateIMVersionArr);

		// read the configuration file and initialize key attributes; SchemaFileDefn map is initialized below (setupNameSpaceInfoAll) 
		// "props" are used again below in setupNameSpaceInfoAll)
		String configInputFile = dataDirPath + "config.properties";
		String configInputStr;
    	File configFile = new File(configInputFile); 
    	try {
    	    FileReader reader = new FileReader(configFile);
//    	    Properties props = new Properties();
    	    props.load(reader);
    	    configInputStr = props.getProperty("infoModelVersionId");
    	    if (configInputStr != null) {
    	    	infoModelVersionId = configInputStr;
    	    }
    	    configInputStr = props.getProperty("schemaLabelVersionId");
    	    if (configInputStr != null) schemaLabelVersionId = configInputStr;
    	    configInputStr= props.getProperty("pds4BuildId");
    	    if (configInputStr != null) pds4BuildId = configInputStr;
    	    configInputStr= props.getProperty("imSpecDocTitle");
    	    if (configInputStr != null) imSpecDocTitle = configInputStr;
    	    configInputStr= props.getProperty("imSpecDocAuthor");
    	    if (configInputStr != null) imSpecDocAuthor = configInputStr;
    	    configInputStr= props.getProperty("imSpecDocSubTitle");
    	    if (configInputStr != null) imSpecDocSubTitle = configInputStr;
    	    configInputStr= props.getProperty("ddDocTitle");
    	    if (configInputStr != null) ddDocTitle = configInputStr;
    	    configInputStr= props.getProperty("debugFlag");
    	    if (configInputStr != null && configInputStr.compareTo("true") == 0) debugFlag = true;
//    	    configInputStr= props.getProperty("lSchemaFileDefn.pds.regAuthId");
    	    configInputStr= props.getProperty("mastRegAuthId");
    	    if (configInputStr != null) {
                registrationAuthorityIdentifierValue = configInputStr;
        		registeredByValue = "RA_" + registrationAuthorityIdentifierValue; 
    	    }
    	    configInputStr= props.getProperty("ddDocTeam");
    	    if (configInputStr != null) ddDocTeam = configInputStr;
    	    configInputStr= props.getProperty("pds4ModelFlag");
    	    if (configInputStr != null && configInputStr.compareTo("true") == 0) pds4ModelFlag = true;
    	    configInputStr= props.getProperty("mastModelId");
    	    if (configInputStr != null) mastModelId = configInputStr;
    	    
    	    configInputStr= props.getProperty("toolVersionId");
            if (configInputStr != null) {
                DMDocVersionId = LDDToolVersionId = configInputStr;
            }
            configInputStr= props.getProperty("buildDate");
            if (configInputStr != null) {
                buildDate = configInputStr;
            }
    	    
    	    reader.close();
    	} catch (FileNotFoundException ex) {
    	    // file does not exist
    		registerMessage ("3>error Configuration file does not exist. [config.properties]");
    	} catch (IOException ex) {
    	    // I/O error
    		registerMessage ("3>error Configuration file IO Exception. [config.properties]");
    	}
		
		// process second set of arguments
		processArgumentParserNamespacePhase2(argparse4jNamespace);    	
		
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
		
		// set up the System Build version
		XMLSchemaLabelBuildNum = pds4BuildId;
		
	    // intialize the masterAllSchemaFileSortMap -  all namespaces in config.properties file
		// set up the Master Schema Information for both normal and LDD processing (dirpath, namespaces, etc)
		setupNameSpaceInfoAll(props);
		
		// output the context info
		if (! LDDToolFlag) {
			registerMessage ("1>info DMDoc Version: " + DMDocVersionId);
			registerMessage ("1>info IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);	
			registerMessage ("1>info IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);	
			registerMessage ("1>info IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);	
		} else {
			registerMessage ("1>info LDDTOOL Version: " + LDDToolVersionId);
			registerMessage ("1>info IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);
			registerMessage ("1>info IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);	
			registerMessage ("1>info IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);	
		}	
		
		registerMessage ("1>info Date: " + sTodaysDate);
		registerMessage ("1>info PARENT_DIR: " + lPARENT_DIR);
		registerMessage ("1>info SCRIPT_DIR: " + lSCRIPT_DIR);
		registerMessage ("1>info LIB_DIR: " + lLIB_DIR);
		
		// set the deprecated flags
		setObjectDeprecatedFlag();
		
		// get the 11179 Attribute Dictionary - .pins file
		ProtPinsDOM11179DD lProtPinsDOM11179DD  = new ProtPinsDOM11179DD ();
		lProtPinsDOM11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "dd11179.pins");
		
		// get the models
		GetDOMModelDoc lGetDOMModelDoc = new GetDOMModelDoc();
		lGetDOMModelDoc.getModels (docFileName + ".pins");
		
		// get the DOM Model
		GetDOMModel lGetDOMModel = new GetDOMModel();
		lGetDOMModel.getDOMModel (docFileName + ".pins");
		if (debugFlag) DOMInfoModel.domWriter(DOMInfoModel.masterDOMClassArr, "DOMModelListPerm.txt");
		
		// export the models
		if (DMDocument.LDDToolFlag) {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeLDDArtifacts ();
		} else if ( DMDocument.mapToolFlag) {
            WriteMappingFile writeMappingFile = new WriteMappingFile();
            writeMappingFile.writeMappingFile(registrationAuthorityIdentifierValue, propertyMapFileName);
		} else {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeAllArtifacts ();
		}
		registerMessage ("0>info Next UID: " + DOMInfoModel.getNextUId());
		printErrorMessages();
		
		if (lMessageErrorCount > 0 || lMessageFatalErrorCount > 0) {
			System.out.println("");
			System.out.println(">>  INFO Exit(1)");
			System.exit(1);
		}
		System.out.println("");
		System.out.println(">>  INFO Exit(0)");
//		System.exit(0);
	}
	
/**********************************************************************************************************
	local utilities
***********************************************************************************************************/

	static private void getEnvMap () {
        Map<String, String> env = System.getenv();
    	
    	lPARENT_DIR = env.get("PARENT_DIR");
    	if (lPARENT_DIR == null) {
    		registerMessage ("3>error Environment variable PARENT_DIR is null");
			printErrorMessages();
			System.exit(1);
    	}
    	lPARENT_DIR =  replaceString (lPARENT_DIR, "\\", "/");
    	
    	lSCRIPT_DIR = env.get("SCRIPT_DIR");
    	if (lSCRIPT_DIR == null) {
    		registerMessage ("3>error Environment variable SCRIPT_DIR is null");
			printErrorMessages();
			System.exit(1);
    	}
    	lSCRIPT_DIR =  replaceString (lSCRIPT_DIR, "\\", "/");
    	
    	lLIB_DIR = env.get("LIB_DIR");
    	if (lLIB_DIR== null) {
    		registerMessage ("3>error Environment variable LIB_DIR is null");
			printErrorMessages();
			System.exit(1);
    	}
    	lLIB_DIR =  replaceString (lLIB_DIR, "\\", "/");	
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
			registerMessage ("3>error " + "Input filename is invalid: " + lSchemaFileDefn.sourceFileName + " - filename[.xml] or fullpath allowed");
			parser.printHelp();
			printErrorMessages();
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
					registerMessage ("3>error " + "Input file not found: " + lSchemaFileDefn.sourceFileName);
					parser.printHelp();
					printErrorMessages();
					System.exit(1);
				}
			}
		}
//		registerMessage ("1>info Input File Name Checked: " + lSchemaFileDefn.LDDToolInputFileName);
	}
	
	static public boolean checkCreateDirectory (String lDirectoryPathName) {
		File file = new File(lDirectoryPathName);
		if (file.exists() && file.isDirectory()) {
//			System.out.println("debug checkCreateDirectory - Directory FOUND - lDirectoryPathName:" + lDirectoryPathName);
			registerMessage ("0>info Found directory: " + lDirectoryPathName);
			return true;
		} else {
			//Create the directory
			boolean bool = file.mkdir();
			if(bool){
//				System.out.println("debug checkCreateDirectory - Directory CREATED - lDirectoryPathName:" + lDirectoryPathName);
				registerMessage ("0>info Created directory: " + lDirectoryPathName);
				return true;
			}else{
//				System.out.println("debug checkCreateDirectory - Directory CREATE FAILED - lDirectoryPathName:" + lDirectoryPathName);
				registerMessage ("1>error Directory create failed: " + lDirectoryPathName);
			}
		}
		return false;
	}
	
	static public boolean checkFileName (String inputFileName) {
		File file=new File (inputFileName);
		if (file.exists() && (file.isFile())) {
			registerMessage ("0>info Found input file: " + inputFileName);
			return true;
		}
		registerMessage ("1>error " + "Input file not found: " + inputFileName);
		return false;
	}
	
	static public void checkRequiredFiles () {
		// check that all the required data files exist
		File file=new File (dataDirPath + "MDPTNConfigClassDisp.xml");
		boolean isFound = file.exists();
		if (!isFound) {
			  registerMessage ("3>error " + "Required data file was not found: " + dataDirPath + "MDPTNConfigClassDisp.xml");
			  printErrorMessages();
			  System.exit(1);
		}
		file=new File (dataDirPath + "UpperModel.pont");
		isFound = file.exists();
		if (!isFound) {
			  registerMessage ("3>error " + "Required data file was not found: " + dataDirPath + "UpperModel.pont");
			  printErrorMessages();
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "dd11179.pins");
		isFound = file.exists();
		if (!isFound) {
			  registerMessage ("3>error " + "Required data file was not found: " + dataDirPath + "dd11179.pins");
			  printErrorMessages();
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "Glossary.pins");
		isFound = file.exists();
		if (!isFound) {
			  registerMessage ("3>error " + "Required data file was not found: " + dataDirPath + "Glossary.pins");
			  printErrorMessages();
			  System.exit(1);
		}
		
		file=new File (dataDirPath + "DMDocument.pins");
		isFound = file.exists();
		if (!isFound) {
			  registerMessage ("3>error " + "Required data file was not found: " + dataDirPath + "DMDocument.pins");
			  printErrorMessages();
			  System.exit(1);
		}
	}

	static void setupNameSpaceInfoAll (Properties prop) {
		SchemaFileDefn lSchemaFileDefn;
		String SCHEMA_LITERAL = "lSchemaFileDefn.";
		String IDENTIFIER = ".identifier";
		
//		System.out.println(" ");
//		registerMessage ("1>info config.properties:");
		
        Set<Object> keys = prop.keySet();	
        for (Object k:keys) {
        	String key = (String)k;
        	// look for schema entries
        	if (key.startsWith(SCHEMA_LITERAL) && key.endsWith(IDENTIFIER)) {
        		String nameSpaceId = prop.getProperty(key);
//        		registerMessage ("1>info namespace_id:"+ nameSpaceId);
        		lSchemaFileDefn = new SchemaFileDefn(nameSpaceId);
        		String isMasterKey = SCHEMA_LITERAL+nameSpaceId + ".isMaster";
        	    String value = prop.getProperty(isMasterKey);
        		if (value != null){
        			if (value.equals("true")) {
        				lSchemaFileDefn.setDictionaryType ("Common");
        			}
        		} else{
        			registerMessage ("3>error Missing schema config item: "+ isMasterKey);
        		}
        		
        		// default to the master values
        		// the value of versionId will be reset by Ingest_LDD 
        		// the value of labelVersionId is the same for all dictionaries, namely the master
        		// the value of labelVersionId is always "1.0"; this is the first label for this product
        		
        		lSchemaFileDefn.versionId = infoModelVersionId;
        		lSchemaFileDefn.labelVersionId = schemaLabelVersionId;

        		String isDisciplineKey = SCHEMA_LITERAL+nameSpaceId + ".isDiscipline";
        	    value = prop.getProperty(isDisciplineKey);
        		if (value != null){
        			if (value.equals("true")) {
        				lSchemaFileDefn.setDictionaryType ("Discipline");
        			}
        		} 
        		
        		String isMissionKey = SCHEMA_LITERAL+nameSpaceId + ".isMission";
        		value = prop.getProperty(isMissionKey);
            	if (value != null){
            		if (value.equals("true")) {
        				lSchemaFileDefn.setDictionaryType ("Mission");
            		}
        	    }    	
        		
          		String stewardArrKey = SCHEMA_LITERAL+nameSpaceId + ".stewardArr";
        	    value = prop.getProperty(stewardArrKey);
        		if (value != null){
        		   String[] stewardArray = value.split(",");
        		   for (int i= 0; i < stewardArray.length; i++) {
        			   lSchemaFileDefn.stewardArr.add(stewardArray[i]);
        		   }       	
        		} else{
        			registerMessage ("3>error Missing schema config item: "+ stewardArrKey);
        		}
        		
          		String commentKey = SCHEMA_LITERAL+nameSpaceId + ".comment";
        	    value = prop.getProperty(commentKey);
        		if (value != null){        		
        			   lSchemaFileDefn.comment = value;        		        	
        		} 
        		
          		String lddNameKey = SCHEMA_LITERAL+nameSpaceId + ".lddName";
        	    value = prop.getProperty(lddNameKey);
        		if (value != null){        		
            			   lSchemaFileDefn.lddName = value;
        		} 
        		
          		String sourceFileNameKey = SCHEMA_LITERAL+nameSpaceId + ".sourceFileName";
        	    value = prop.getProperty(sourceFileNameKey);
        		if (value != null){        		
            			   lSchemaFileDefn.sourceFileName = value;
        		}
        		
          		String nameSpaceURLKey = SCHEMA_LITERAL+nameSpaceId + ".nameSpaceURL";
        	    value = prop.getProperty(nameSpaceURLKey);
        		if (value != null){        		
            			   lSchemaFileDefn.nameSpaceURL = value;
        		}
        		
          		String nameSpaceURLKeyS = SCHEMA_LITERAL+nameSpaceId + ".nameSpaceURLs";
        	    value = prop.getProperty(nameSpaceURLKeyS);
        		if (value != null){        		
            			   lSchemaFileDefn.nameSpaceURLs = value;
        		}
        		
          		String urnPrefixKey = SCHEMA_LITERAL+nameSpaceId + ".urnPrefix";
        	    value = prop.getProperty(urnPrefixKey);
        		if (value != null){        		
            			   lSchemaFileDefn.urnPrefix = value;
        		}
        		
          		String modelShortNameKey = SCHEMA_LITERAL+nameSpaceId + ".modelShortName";
        	    value = prop.getProperty(modelShortNameKey);
        		if (value != null){
            			   lSchemaFileDefn.modelShortName = value;
        		}
        		
          		String sysBundleNameKey = SCHEMA_LITERAL+nameSpaceId + ".sysBundleName";
        	    value = prop.getProperty(sysBundleNameKey);
        		if (value != null){        		
            			   lSchemaFileDefn.sysBundleName = value;
        		}
        		
          		String regAuthIdKey = SCHEMA_LITERAL+nameSpaceId + ".regAuthId";
        	    value = prop.getProperty(regAuthIdKey);
        		if (value != null){        		
            			   lSchemaFileDefn.regAuthId = value;
        		}

        		masterAllSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
        		
/*				if (DMDocument.debugFlag) {
					System.out.println(" ");
	        		System.out.println("debug setupNameSpaceInfoAll lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier);
	        		System.out.println("                            lSchemaFileDefn.lddName:" + lSchemaFileDefn.lddName);
	        		System.out.println("                            lSchemaFileDefn.versionId:" + lSchemaFileDefn.versionId);
	        		System.out.println("                            lSchemaFileDefn.labelVersionId:" + lSchemaFileDefn.labelVersionId);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceIdNC:" + lSchemaFileDefn.nameSpaceIdNC);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceIdNCLC:" + lSchemaFileDefn.nameSpaceIdNCLC);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceIdNCUC:" + lSchemaFileDefn.nameSpaceIdNCUC);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceId:" + lSchemaFileDefn.nameSpaceId);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceURL:" + lSchemaFileDefn.nameSpaceURL);
	        		System.out.println("                            lSchemaFileDefn.nameSpaceURLs:" + lSchemaFileDefn.nameSpaceURLs);
	        		System.out.println("                            lSchemaFileDefn.modelShortName:" + lSchemaFileDefn.modelShortName);
	        		System.out.println("                            lSchemaFileDefn.sysBundleName:" + lSchemaFileDefn.sysBundleName);
	        		System.out.println("                            lSchemaFileDefn.regAuthId:" + lSchemaFileDefn.regAuthId);
	        		System.out.println("                            lSchemaFileDefn.governanceLevel:" + lSchemaFileDefn.governanceLevel);
	        		System.out.println("                            lSchemaFileDefn.isMaster:" + lSchemaFileDefn.isMaster);
	        		System.out.println("                            lSchemaFileDefn.isLDD:" + lSchemaFileDefn.isLDD);
	        		System.out.println("                            lSchemaFileDefn.isDiscipline:" + lSchemaFileDefn.isDiscipline);
	        		System.out.println("                            lSchemaFileDefn.isMission:" + lSchemaFileDefn.isMission);
				} */
        		
     			if (lSchemaFileDefn.isMaster) {
//           		   System.out.println("debug setupNameSpaceInfoAll - set masterPDSSchemaFileDefn - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier);
//           		   masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
          		   masterPDSSchemaFileDefn = lSchemaFileDefn;
           		   masterNameSpaceIdNCLC = lSchemaFileDefn.nameSpaceIdNCLC;
           		   lSchemaFileDefn.isActive = true; // 7777 isActive is set here temporarily until DOM is used; isActive is set above for all IngestLDD
               		// set lab_version_id for master so that the remaining lab_version_id can be set below.
               		masterPDSSchemaFileDefn.setVersionIds();
     			}
        	}
          }
        
        	// set set VersionIds		
    		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterAllSchemaFileSortMap.values());
    		ArrayList <String> lNamespaceIdArr = new ArrayList <String> ();
    		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
    			SchemaFileDefn lSchemaFileDefn2 = (SchemaFileDefn) i.next();
    			lNamespaceIdArr.add(lSchemaFileDefn2.identifier);
    			lSchemaFileDefn2.setVersionIds();
    		}
    		
    		if (printNamespaceFlag || debugFlag) registerMessage ("1>info Configured NameSpaceIds:" + lNamespaceIdArr);
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
	
/**********************************************************************************************************
	set object flags, e.g., deprecated
***********************************************************************************************************/
	
	static void setObjectDeprecatedFlag () {
		// deprecated objects  *** Inconsistency here to be fixed - Earth base identifier is different ***
		deprecatedAdded = false;
		deprecatedAddedDOM = false;
		deprecatedObjects2 = new ArrayList <DeprecatedDefn> ();

		deprecatedObjects2.add(new DeprecatedDefn ("Product_Update", "pds", "Product_Update", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Update", "pds", "Update", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("File_Area_Update", "pds", "File_Area_Update", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Update.update_purpose", "pds", "Update", "pds", "update_purpose", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Information_Package_Component_Deep_Archive.checksum_type", "pds", "Information_Package_Component_Deep_Archive", "pds", "checksum_type", "MD5Deep 4.n", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Airborne", "pds", "Airborne", "", "", "", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Product_Context.Airborne.type", "pds", "Airborne", "pds", "type", "Aircraft", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Product_Context.Airborne.type", "pds", "Airborne", "pds", "type", "Balloon", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Product_Context.Airborne.type", "pds", "Airborne", "pds", "type", "Suborbital Rocket", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Airborne", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Aircraft", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Balloon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Suborbital Rocket", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Computer", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Facility", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Laboratory", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Naked Eye", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Observatory", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Spacecraft", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Observing_System_Component.type", "pds", "Observing_System_Component", "pds", "type", "Artificial Illumination", false));
		
		deprecatedObjects2.add(new DeprecatedDefn ("Internal_Reference.reference_type", "pds", "Internal_Reference", "pds", "reference_type", "is_airborne", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Bundle_Member_Entry.reference_type", "pds", "Bundle_Member_Entry", "pds", "reference_type", "bundle_has_member_collection", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Display_2D_Image", "pds", "Display_2D_Image", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Band_Bin_Set", "pds", "Band_Bin_Set", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Band_Bin", "pds", "Band_Bin", "", "", "", false));	
		deprecatedObjects2.add(new DeprecatedDefn ("Axis_Array.unit", "pds", "Axis_Array", "pds", "unit", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Array.Local_Internal_Reference", "pds", "Array", "pds", "Local_Internal_Reference", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument_Host.type.Earth Based", "pds", "Instrument_Host", "pds", "type", "Earth Based", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Instrument_Host.type.Earth-based", "pds", "Instrument_Host", "pds", "type", "Earth-based", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "", false));                            
	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.subtype", "pds", "Instrument", "pds", "subtype", "", false));                            

	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Calibration", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Open Cluster", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Globular Cluster", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Terrestrial Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Lunar Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Synthetic Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target_Identification.type", "pds", "Target_Identification", "pds", "type", "Meteorite", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Calibration", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Open Cluster", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Globular Cluster", false));

	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Terrestrial Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Lunar Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Synthetic Sample", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Target.type", "pds", "Target", "pds", "type", "Meteorite", false));
	    
	    deprecatedObjects2.add(new DeprecatedDefn ("Node.name.Imaging", "pds", "Node", "pds", "name", "Imaging", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("Node.name.Planetary Rings", "pds", "Node", "pds", "name", "Planetary Rings", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("PDS_Affiliate.team_name.Imaging", "pds", "PDS_Affiliate", "pds", "team_name", "Imaging", false));
	    deprecatedObjects2.add(new DeprecatedDefn ("PDS_Affiliate.team_name.Planetary Rings", "pds", "PDS_Affiliate", "pds", "team_name", "Planetary Rings", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.data_regime", "pds", "Primary_Result_Summary", "pds", "data_regime", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.type", "pds", "Primary_Result_Summary", "pds", "type", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Primary_Result_Summary.processing_level_id", "pds", "Primary_Result_Summary", "pds", "processing_level_id", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "subclass_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "restriction_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.reference_type", "pds", "DD_Association", "pds", "reference_type", "extension_of", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External", "pds", "DD_Association_External", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association.local_identifier", "pds", "DD_Association", "pds", "local_identifier", "", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "subclass_of", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "restriction_of", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("DD_Association_External.reference_type", "pds", "DD_Association_External", "pds", "reference_type", "extension_of", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Table_Binary.record_delimiter", "pds", "Table_Binary", "pds", "record_delimiter", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Field_Bit.start_bit", "pds", "Field_Bit", "pds", "start_bit", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Field_Bit.stop_bit", "pds", "Field_Bit", "pds", "stop_bit", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.record_delimiter", "pds", "Table_Delimited", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_External.record_delimiter", "pds", "Table_Delimited_Source_Product_External", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_Internal.record_delimiter", "pds", "Table_Delimited_Source_Product_Internal", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Character.record_delimiter", "pds", "Table_Character", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Stream_Text.record_delimiter", "pds", "Stream_Text", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Checksum_Manifest.record_delimiter", "pds", "Checksum_Manifest", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.record_delimiter", "pds", "Inventory", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Transfer_Manifest.record_delimiter", "pds", "Transfer_Manifest", "pds", "record_delimiter", "carriage-return line-feed", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Uniformly_Sampled.sampling_parameters", "pds", "Uniformly_Sampled", "pds", "sampling_parameters", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Object_Statistics.bit_mask", "pds", "Object_Statistics", "pds", "bit_mask", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Object_Statistics.md5_checksum", "pds", "Object_Statistics", "pds", "md5_checksum", "", false));
		
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited.field_delimiter", "pds", "Table_Delimited", "pds", "field_delimiter", "vertical bar", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_External.field_delimiter", "pds", "Table_Delimited_Source_Product_External", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_External.field_delimiter", "pds", "Table_Delimited_Source_Product_External", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_External.field_delimiter", "pds", "Table_Delimited_Source_Product_External", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_External.field_delimiter", "pds", "Table_Delimited_Source_Product_External", "pds", "field_delimiter", "vertical bar", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_Internal.field_delimiter", "pds", "Table_Delimited_Source_Product_Internal", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_Internal.field_delimiter", "pds", "Table_Delimited_Source_Product_Internal", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_Internal.field_delimiter", "pds", "Table_Delimited_Source_Product_Internal", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Table_Delimited_Source_Product_Internal.field_delimiter", "pds", "Table_Delimited_Source_Product_Internal", "pds", "field_delimiter", "vertical bar", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "comma", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "horizontal tab", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "semicolon", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Inventory.field_delimiter", "pds", "Inventory", "pds", "field_delimiter", "vertical bar", false));

//		deprecatedObjects2.add(new DeprecatedDefn ("Update.Update_Entry", "pds", "Update_Entry", "", "", "", false));
		
		deprecatedObjects2.add(new DeprecatedDefn ("Telescope.altitude", "pds", "Telescope", "pds", "altitude", "", false));

		
		deprecatedObjects2.add(new DeprecatedDefn ("Document_Format.format_type", "pds", "Document_Format", "pds", "format_type", "single file", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Document_Format.format_type", "pds", "Document_Format", "pds", "format_type", "multiple file", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Thermal And Electrical Conductivity Probe", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "X-ray Defraction Spectrometer", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Alpha Particle Xray Spectrometer", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "X-ray Fluorescence", false));
//		deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type", "pds", "Instrument", "pds", "type", "Grinding And Drilling Tool", false));
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

		deprecatedObjects2.add(new DeprecatedDefn ("Units_of_Map_Scale", "pds", "Units_of_Map_Scale", "", "", "", false));		
		
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date", "pds", "ASCII_Date", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time", "pds", "ASCII_Date_Time", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time_UTC", "pds", "ASCII_Date_Time_UTC", "", "", "", false));

		deprecatedObjects2.add(new DeprecatedDefn ("Vector", "pds", "Vector", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Vector_Component", "pds", "Vector_Component", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Vector_Cartesian_3", "pds", "Vector_Cartesian_3", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Vector_Cartesian_3_Acceleration", "pds", "Vector_Cartesian_3_Acceleration", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Vector_Cartesian_3_Position", "pds", "Vector_Cartesian_3_Position", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("Vector_Cartesian_3_Velocity", "pds", "Vector_Cartesian_3_Velocity", "", "", "", false));
	}
	
	static void Dump333DeprecatedObjects2 (String title, ArrayList <DeprecatedDefn> lDeprecatedObjectsArr) {
		System.out.println("\n debug Dump333DeprecatedObjects2 - " + title);
		for (DeprecatedDefn lDep : lDeprecatedObjectsArr) {
			System.out.println("debug lDep.title:" + lDep.title + " |  lDep.classNameSpaceIdNC:" + lDep.classNameSpaceIdNC + " |  lDep.className:" + lDep.className + " |  lDep.attrNameSpaceIdNC:" + lDep.attrNameSpaceIdNC + " |  lDep.attrName:" + lDep.attrName + " |  lDep.value:" + lDep.value + " |  lDep.isValue:" + lDep.isValue + " |  lDep.isAttribute:" + lDep.isAttribute + " |  lDep.isUnitId:" + lDep.isUnitId);
		}
	}
	
	static void setexposedElementFlag () {
		// the set of classes and attributes that will be externalized (defined as xs:Element)
		exposedElementArr = new ArrayList <String> ();
		exposedElementArr.add("0001_NASA_PDS_1.pds.Ingest_LDD");
//		exposedElementArr.add("0001_NASA_PDS_1.oais.Archival_Information_Package");
//		exposedElementArr.add("0001_NASA_PDS_1.oais.Information_Package");
//		exposedElementArr.add("0001_NASA_PDS_1.oais.Information_Package_Collection");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_AIP");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Ancillary");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Attribute_Definition");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Browse");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Bundle");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Class_Definition");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Collection");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Context");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_DIP");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_DIP_Deep_Archive");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Data_Set_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Document");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_File_Repository");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_File_Text");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Instrument_Host_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Instrument_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Metadata_Supplemental");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Mission_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Native");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Observational");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Proxy_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_SIP");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_SIP_Deep_Archive");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_SPICE_Kernel");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Service");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Software");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Subscription_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Target_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Thumbnail");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Update");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Volume_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Volume_Set_PDS3");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_XML_Schema");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Product_Zipped");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Internal_Reference");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Local_Internal_Reference");
		exposedElementArr.add("0001_NASA_PDS_1.pds.External_Reference");
		exposedElementArr.add("0001_NASA_PDS_1.pds.DD_Class.pds.local_identifier");
		exposedElementArr.add("0001_NASA_PDS_1.pds.Identification_Area.pds.logical_identifier");
	}
	
	static void setRegistryAttrFlag () {
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
	}
	
	static void registerMessage (String lMessage) {
		DOMMsgDefn lMessageDefn = new DOMMsgDefn (lMessage);
		mainMsgArr.add (lMessageDefn);
		return;
	}
	
	static void registerMessage (String lNameSpaceIdNCLC, String lMessage) {
		DOMMsgDefn lMessageDefn = new DOMMsgDefn (lMessage);
		lMessageDefn.nameSpaceIdNCLC = lNameSpaceIdNCLC;
		mainMsgArr.add (lMessageDefn);
		return;
	}

	static Namespace getArgumentParserNamespace(String args[]) {
		parser = ArgumentParsers.newFor("LDDTool").build()
                .defaultHelp(true)
                .version(LDDToolVersionId) 
                .description("LDDTool process control:");
        
        parser.addArgument("-p", "--PDS4 processing")
                .dest("p")
                .type(Boolean.class)
                .nargs(1)
        		.action(Arguments.storeTrue())
                .help("Set the context to PDS4");
        
        parser.addArgument("-l", "--LDD")
                .dest("l")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Process one or more local data dictionary input files");
        
/*        parser.addArgument("-map", "--map")
        		.dest("map")
        		.type(Boolean.class)
        		.nargs(1)
        		.action(Arguments.storeTrue())
        		.setDefault("false")
        		.help("Map Tool Processing");        */
        
        parser.addArgument("-d", "--discipline")
                .dest("d")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Omit the term \"mission\" from the namespace of a dictionary");
        
        parser.addArgument("-D", "--DataDict")
                .dest("D")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Write the Data Dictionary DocBook file");
        
        parser.addArgument("-J", "--JSON")
                .dest("J")
                .type(Boolean.class)
                .nargs(1)
        		.action(Arguments.storeTrue())
                .help("Write the data dictionary to a JSON formatted file");
        
        parser.addArgument("-m", "--merge")
                .dest("m")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Generate file to merge the local dictionary into the master dictionary");
        
        parser.addArgument("-M", "--Mission")
                .dest("M")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("This option has no effect starting with PDS4 IM Version 1.14.0.0. See the LDDTool Usage document for more information on how to provide this information.");
        
        parser.addArgument("-n", "--nuance")
                .dest("n")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Write nuance property maps to LDD schema annotation in JSON");
        
        parser.addArgument("-N", "--Namespace")
                .dest("N")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Print the list of configured namespaces to the log");
        
/*        parser.addArgument("-t", "--Annotate")
                .dest("t")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Annotate Definition"); */
        
/*        parser.addArgument("-a", "--Attr Element")
                .dest("a")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Element Definition (Attribute)"); */
        
        	parser.addArgument("-1", "--IM Spec")
                .dest("1")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Write the Information Model Specification for an LDD");
        
//	The following are hidden and temporarily deprecated        
        
/*        parser.addArgument("-4", "--Import JSON")
                .dest("4")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Import JSON Attribute File"); */
        
/*        parser.addArgument("-5", "--Export OWL")
                .dest("5")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Export OWL File"); */
        
/*        parser.addArgument("-6", "--Export JSON All")
                .dest("6")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Export JSON Attribute File - All"); */
        
/*        parser.addArgument("-f", "--Check")
                .dest("f")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Check File Name"); */
    
        parser.addArgument("-v", "--version")
                .dest("v")
                .type(Boolean.class)
                .nargs(1)
                .action(Arguments.storeTrue())
                .help("Returns the LDDTool version number");
               
        parser.addArgument("-V", "--IM Version")
                .dest("V")
                .type(String.class)
                .choices("1B00", "1B10", "1C00", "1D00", "1E00", "1F00", "1G00", "1H00").setDefault(buildIMVersionFolderId)
                .help("Set the IM Version");
        
        parser.addArgument("fileNameArr")
                .dest("fileNameArr")
                .nargs("*")
                .help("Ingest_LDD files to process.");

        Namespace namespace = null;
        
        try {
        	namespace = parser.parseArgs(args);
        } catch (HelpScreenException e) {
            System.out.println(">>  INFO Exit(0)");
//            parser.handleError(e);
//            e.printStackTrace();
            System.exit(0);
        } catch (ArgumentParserException e) {
            System.out.println(">>  ERROR Invalid argument list");
            parser.printHelp();
            System.out.println(">>  INFO  Exit(1)");
//            parser.handleError(e);
//            e.printStackTrace();
            System.exit(1);
        }
		return namespace;
	}
	
	static void processArgumentParserNamespacePhase1 (Namespace ns) {
        
        // handle processing flags
        Boolean pFlag = ns.getBoolean("p");
		if (pFlag) {
			dmProcessState.setPDSOptionalFlag (); 
			PDSOptionalFlag = true;
		}
		
		Boolean lddFlag = ns.getBoolean("l");
		if (lddFlag) {
			dmProcessState.setLDDToolFlag (); 
			LDDToolFlag = true;
		}

		// set up the alternate versions if any
		String altVersion = ns.getString("V");
		if (altVersion.compareTo(buildIMVersionFolderId) != 0) {  	// it is not the current version of the IM
			if (alternateIMVersionArr.contains(altVersion)) {		// is it an allowed prior version; was validated in argument parser
				alternateIMVersion = altVersion;
				dmProcessState.setalternateIMVersionFlag (); 
				alternateIMVersionFlag = true;
			}
		}
		return;
	}
	
	static void processArgumentParserNamespacePhase2 (Namespace ns) {
		
		// handle the request for version
		Boolean vFlag = ns.getBoolean("v");
		if (vFlag) {
			dmProcessState.setversionFlag (); 
			System.out.println(" ");
			System.out.println("LDDTool Version: " + LDDToolVersionId);
			System.out.println("Built with IM Version: " + buildIMVersionId);
			System.out.println("Build Date: " + buildDate);
			System.out.println("Configured IM Versions: " + alternateIMVersionArr);
			System.out.println(" ");
			System.exit(0);
		}
		
		// validate the input arguments
		if (! PDSOptionalFlag) {
			registerMessage ("3>error " + "The -p option must be used for PDS4 processing");
			parser.printHelp();
            System.out.println(">>  INFO  Exit(1)");			
			System.exit(1);
		}
		
/*        String mapFlag = ns.getBoolean("map");
		if (mapFlag) {
			registerMessage ("1>info Tool processing");
			dmProcessState.setmapToolFlag (); 
			LDDToolFlag = false;
			mapToolFlag = true;
			PDSOptionalFlag = true;
		} */
/*        Boolean tFlag = ns.getBoolean("t");
		if (tFlag) {
			dmProcessState.setLDDToolAnnotateDefinitionFlag (); 
			LDDToolAnnotateDefinitionFlag = true;
		} */
		Boolean MFlag = ns.getBoolean("M");
		if (MFlag) {
			dmProcessState.setLDDToolMissionFlag (); 
			LDDToolMissionFlag = true;
			registerMessage ("1>warning " + "The -M flag has been deprecated as of PDS4 IM Version 1.14.0.0. See the LDDTool User's Manual for more information on how to provide this information.");
		}
		Boolean mFlag = ns.getBoolean("m");
		if (mFlag) {
			dmProcessState.setPDS4MergeFlag (); 
			PDS4MergeFlag = true;
		}
		Boolean nFlag = ns.getBoolean("n");
		if (nFlag) {
			dmProcessState.setLDDNuanceFlag (); 
			LDDNuanceFlag = true;
		}
		Boolean NFlag = ns.getBoolean("N");
		if (NFlag) {
			dmProcessState.setprintNamespaceFlag (); 
			printNamespaceFlag = true;
		}
/*        String aFlag = ns.getBoolean("a");
		if (aFlag) {
			dmProcessState.setLDDAttrElementFlag (); 
//			LDDAttrElementFlag = true;
			LDDAttrElementFlag = false;
		} */
		
		
		Boolean dFlag = ns.getBoolean("d");
		if (dFlag) {
			dmProcessState.setdisciplineMissionFlag (); 
			disciplineMissionFlag = true;
		}
		Boolean DFlag = ns.getBoolean("D");
		if (DFlag) {
			dmProcessState.setexportDDFileFlag (); 
			exportDDFileFlag = true;
		}
		Boolean JFlag = ns.getBoolean("J");
		if (JFlag) {
			dmProcessState.setexportJSONFileFlag (); 
			exportJSONFileFlag = true;
		}
		Boolean n1Flag = ns.getBoolean("1");
		if (n1Flag) {
			dmProcessState.setexportSpecFileFlag (); 
			exportSpecFileFlag = true;
		}
		/*        Boolean n4Flag = ns.getBoolean("4");
		if (n4Flag) {
			dmProcessState.setimportJSONAttrFlag (); 
			importJSONAttrFlag = true;
		} */
		/*        Boolean n5Flag = ns.getBoolean("5");
		if (n5Flag) {
			dmProcessState.setexportOWLFileFlag ();
			exportOWLFileFlag = true;
		} */
		/*        Boolean n6Flag = ns.getBoolean("6");
		if (n6Flag) {
			dmProcessState.setexportJSONFileAllFlag (); 
			exportJSONFileAllFlag = true;
		}  */
/*        Boolean fFlag = ns.getBoolean("f");
		if (fFlag) {
			dmProcessState.setcheckFileNameFlag (); 
		} */
        
		// get the LDDIngest file names
        for (String fileName : ns.<String> getList("fileNameArr")) {
    		SchemaFileDefn lLDDSchemaFileDefn = new SchemaFileDefn(fileName);
    		lLDDSchemaFileDefn.sourceFileName = fileName;
    		lLDDSchemaFileDefn.isActive = true;
    		lLDDSchemaFileDefn.isLDD = true;
    		lLDDSchemaFileDefn.labelVersionId = schemaLabelVersionId;
    		LDDSchemaFileSortArr.add(lLDDSchemaFileDefn);
    		masterLDDSchemaFileDefn = lLDDSchemaFileDefn;  // the last Ingest_LDD named is the master.
        }
		return;
	}
	
	static void printErrorMessages () {
		String lPreviousGroupTitle = "";
		
		// first sort error messages
		TreeMap <String, DOMMsgDefn> lMainMsgMap = new TreeMap <String, DOMMsgDefn> ();
		for (Iterator <DOMMsgDefn> i = mainMsgArr.iterator(); i.hasNext();) {
			DOMMsgDefn lMainMsg = (DOMMsgDefn) i.next();
			
			// eliminate certain messages
			if (nameSpaceIdExtrnFlagArr.contains(lMainMsg.nameSpaceIdNCLC)) continue;
			
			// if debugFlag is false, skip debug messages
			// 0>info, 0>warning, 0>error
			if (! debugFlag) {
				if ((lMainMsg.msgTypeLevel.substring(0, 2)).compareTo("0>") == 0) {
					continue;
				}
			}
			String lMapID = lMainMsg.msgTypeLevel + "." + lMainMsg.msgOrder.toString();
			lMainMsgMap.put(lMapID, lMainMsg);		
//			System.out.println ("debug printErrorMessages lMainMsg.msgTypeLevel:" + lMainMsg.msgTypeLevel + "   lMainMsg.msgOrder.toString():" + lMainMsg.msgOrder.toString());
		}

		// using message sorted array, print each message and count message types
		ArrayList <DOMMsgDefn> lMainMsgArr = new ArrayList <DOMMsgDefn> (lMainMsgMap.values());
		for (Iterator <DOMMsgDefn> i = lMainMsgArr.iterator(); i.hasNext();) {
			DOMMsgDefn lMainMsg = (DOMMsgDefn) i.next();
			Integer lMessageCount = messageLevelCountMap.get(lMainMsg.msgTypeLevel);
			if (lMessageCount != null) {
				lMessageCount++;
				messageLevelCountMap.put(lMainMsg.msgTypeLevel, lMessageCount);
			}
			
			if (lPreviousGroupTitle.compareTo (lMainMsg.msgGroupTitle) != 0) {
				lPreviousGroupTitle = lMainMsg.msgGroupTitle;
				System.out.println ("");
//					System.out.println ("  - " + lMainMsg.msgGroupTitle + " -");
			}
			System.out.println (lMainMsg.msgPrefix + " " + lMainMsg.msgCleanText);
		}
		
	// print out the message counts
		System.out.println ("");
		String lToolName = "IMTool";
		if (LDDToolFlag) lToolName = "LDDTool";
		System.out.println (" -- " + lToolName + " Execution Summary --");
		
		lMessageWarningCount = messageLevelCountMap.get("1>warning");
		lMessageWarningCount += messageLevelCountMap.get("2>warning");
		lMessageErrorCount = messageLevelCountMap.get("1>error");
		lMessageErrorCount += messageLevelCountMap.get("2>error");
		lMessageFatalErrorCount = messageLevelCountMap.get("3>error");
		lMessageFatalErrorCount += messageLevelCountMap.get("4>error");
		
		System.out.println ("     " + lMessageWarningCount + " warning(s)");
		System.out.println ("     " + lMessageErrorCount + " error(s)");
		System.out.println ("     " + lMessageFatalErrorCount + " fatal error(s)");
		
		printFlags ();
		printInputFileNames ();
		printOutputFileNames ();
	}
	
	static private void printFlags() {
		// print set flags in flag array
		System.out.println("");
		System.out.println("Input:");		
		System.out.println("");		
		System.out.println("     - IM Version: " + alternateIMVersion);
		
		for (String processFlagName : dmProcessState.getSortedProcessFlagNameArr()) {
			System.out.println("     - " + processFlagName + ": true");
		}
	}
	
	static private void printInputFileNames() {
		// print set flags in flag array
		System.out.println("");
		System.out.print ("     - Ingest LDD(s): ");		
	
		if (LDDToolFlag) {
			String del = "";
			for (SchemaFileDefn lLDDSchemaFile : LDDSchemaFileSortArr) {
				System.out.print (del + lLDDSchemaFile.LDDToolInputFileName);
				del = ", ";
			}
		}		
		System.out.println("");	
	}
	
	static private void printOutputFileNames() {
		// print filenames used
		System.out.println("");
		System.out.println("Output:");		
		System.out.println("");	
		
		for (String writtenFileName : dmProcessState.getSortedWrittenFileNameArr()) {
			System.out.println("     - " + writtenFileName);
		}	
	}
}
