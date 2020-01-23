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
	static final String upperModelFileName  = "UpperModel.pont";
	
	// variables for the class %3ACLIPS_TOP_LEVEL_SLOT_CLASS
	static final String TopLevelAttrClassName  = "%3ACLIPS_TOP_LEVEL_SLOT_CLASS";
	
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
	// 1.15.0.0 - 1.21 - 1.21 - Build 11a - 1F00
	// 1.16.0.0 - 1.22 - 1.22 - Build 11b - 1G00
	
	// x.x.x.x - 1.0 - 1.n - Build nm - first version of product will always be 1.0
	//									Modification history will continue with 1.n
	                         
	static String LDDToolVersionId  = "0.0.0";
	static String buildIMVersionId = "1.14.0.0";
	static String classVersionIdDefault = "1.0.0.0";
//	static String LDDToolGeometry = "Geometry";
	static boolean PDS4MergeFlag  = false;
//	static boolean LDDClassElementFlag = false;			// if true, write XML elements for classes
	static boolean LDDAttrElementFlag = false;			// if true, write  XML elements for attributes
	static boolean LDDNuanceFlag = false;				//
	
	// import export file flags
	static boolean exportJSONFileFlag = false;			// LDDTool, set by -J option
	static boolean exportSpecFileFlag = false;
	static boolean exportDDFileFlag = false;
	static boolean exportJSONAttrFlag = false;			// non PDS processing - not currently used
	static boolean importJSONAttrFlag = false;			// non PDS processing - not currently used
	static boolean exportDOMFlag = true;				// if false do not write any DOM file; For LDDTool the parse classes from IngestLDD are not in the DOM structures.
	static boolean exportMOFFlag = false;
	static boolean pds4ModelFlag = true;
	static int writeDOMCount = 0;						// LDDParser DOM Error write count; if exportDOMFlag=true then DOM code is executed and so error/warning messages are duplicated in log and txt file.
	
	// when true this flag indicates an LDDTool run for a namespace other than pds (i.e., Common)
	static boolean LDDToolFlag;
	// in an LDDTool run, when true indicates that a mission LDD is being processed, otherwise a discipline LDD is being processed
	static boolean LDDToolMissionGovernanceFlag;
	// governance levels are Common, Discipline, and Mission, consistent with the flags.
	static String governanceLevel = "Common";
	
	static boolean LDDToolAnnotateDefinitionFlag;
	static String LDDToolSingletonClassTitle = "USER";
	static DOMClass LDDToolSingletonDOMClass = null;
	static ArrayList <String> LDDImportNameSpaceIdNCArr = new ArrayList <String> ();
	
	static boolean mapToolFlag = false;
	
	// Master Model
	static MasterDOMInfoModel masterDOMInfoModel;
	
	// Master LDD Model
	static LDDDOMParser primaryLDDDOMModel;
	
	// Secondary LDD Models
	static ArrayList <LDDDOMParser> LDDDOMModelArr;
	
	// Schemas, Stewards and Namespaces (SchemaFileDefn)
	static TreeMap <String, SchemaFileDefn> masterAllSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();	// all namespaces in config.properties file.
	static TreeMap <String, SchemaFileDefn> masterSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();		// namespaces that will be written to XML Schema, etc (*** One only, since LDDs are not ingested anymore ***)
	static ArrayList <SchemaFileDefn> LDDSchemaFileSortArr;
	static TreeMap <String, SchemaFileDefn> LDDSchemaFileSortMap = new TreeMap <String, SchemaFileDefn> ();		   
	
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
	static ArrayList <DispDefn> masterClassDispoArr;
	static ArrayList <String> masterStewardArr;
	static ArrayList <String> masterNameSpaceIDArr;
	static ArrayList <String> masterStewardNameSpaceIDArr;
	
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
	static ArrayList <String> deprecatedAttrValueArr;
	static String Literal_DEPRECATED = " *Deprecated*";
	static boolean deprecatedAdded;
	static boolean deprecatedAddedDOM;
	
	// the set of classes and attributes that will be externalized (defined as xs:Element)	
	static ArrayList <String> exposedElementArr;

	// class version identifiers (only updated classes; v1.0.0.0 is assumed)
	static TreeMap <String, String> classVersionId;
	
	// debug flag	
	static boolean debugFlag = false;
	
	// Property List for Config.Properties
	static Properties props = new Properties();
	
	static ArrayList <String> propertyMapFileName = new ArrayList <String> ();
	
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
		LDDDOMModelArr = new ArrayList <LDDDOMParser> ();
		LDDSchemaFileSortArr = new ArrayList <SchemaFileDefn> ();
		LDDToolAnnotateDefinitionFlag = false;
		LDDToolMissionGovernanceFlag = false;

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
		
		// original location of deprecation code moved to end of file.
		
		// set exposed elements 
		setexposedElementFlag ();
		
		// set classVersionId
		setClassVersionIdFlag ();
		
		// read the configuration file and initialize key attributes; SchemaFileDefn map is initialized below (setupNameSpaceInfoAll) 
		// first get the environment variables
		getEnvMap();
		dataDirPath = lPARENT_DIR + "/Data/";
		
		// next get the configuration file ("props" are used again below in setupNameSpaceInfoAll)
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
		
		// set up the System Build version
		XMLSchemaLabelBuildNum = pds4BuildId;
		
	    // intialize the masterAllSchemaFileSortMap -  all namespaces in config.properties file
		// set up the Master Schema Information for both normal and LDD processing (dirpath, namespaces, etc)
		setupNameSpaceInfoAll(props);
		
		// output the context info
		if (! LDDToolFlag) {
			System.out.println(">>info    - DMDoc Version: " + DMDocVersionId);
			System.out.println(">>info    - IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);	
			System.out.println(">>info    - IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);	
			System.out.println(">>info    - IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);	
		} else {
			System.out.println(">>info    - LDDTOOL Version: " + LDDToolVersionId);
			System.out.println(">>info    - IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);
			System.out.println(">>info    - IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);	
			System.out.println(">>info    - IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);	
		}	
		
		System.out.println(">>info    - Date: " + sTodaysDate);
		System.out.println(">>info    - JAVAHOME: " + lJAVAHOME);
		System.out.println(">>info    - PARENT_DIR: " + lPARENT_DIR);
		System.out.println(">>info    - SCRIPT_DIR: " + lSCRIPT_DIR);
		System.out.println(">>info    - LIB_DIR: " + lLIB_DIR);
		System.out.println("");
		
		// print out the stewards - namespaceid pairs
		if (DMDocument.debugFlag) {
			System.out.println("\n>>info    - Disposition File Steward/NameSpaceId:");
			for (Iterator <String> i = masterStewardNameSpaceIDArr.iterator(); i.hasNext();) {
				String lStewardNameSpaceId = (String) i.next();
				System.out.println(">>info    - " + lStewardNameSpaceId);
			}
			System.out.println(" " );
		}
		
		// set the deprecated flags
		setObjectDeprecatedFlag();
		
		// get the 11179 Attribute Dictionary - .pins file
//		ProtPins11179DD protPins11179DD  = new ProtPins11179DD ();
//		protPins11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "dd11179.pins");
		ProtPinsDOM11179DD lProtPinsDOM11179DD  = new ProtPinsDOM11179DD ();
		lProtPinsDOM11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "dd11179.pins");
		
		// get the models
		GetDOMModelDoc lGetDOMModelDoc = new GetDOMModelDoc();
		lGetDOMModelDoc.getModels (PDSOptionalFlag, docFileName + ".pins");
		
		// get the DOM Model
		if (exportDOMFlag) {
			GetDOMModel lGetDOMModel = new GetDOMModel();
			lGetDOMModel.getDOMModel (PDSOptionalFlag, docFileName + ".pins");
		}
		if (debugFlag) DOMInfoModel.domWriter(DOMInfoModel.masterDOMClassArr, "DOMModelListPerm.txt");
		
		// export the models
		if (DMDocument.LDDToolFlag) {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeLDDArtifacts (exportDOMFlag, exportMOFFlag);
		} else if ( DMDocument.mapToolFlag) {
            WriteMappingFile writeMappingFile = new WriteMappingFile();
            writeMappingFile.writeMappingFile(registrationAuthorityIdentifierValue, propertyMapFileName);
		} else {
			ExportModels lExportModels = new ExportModels ();
			lExportModels.writeAllArtifacts (exportDOMFlag, exportMOFFlag);
		}
		System.out.println(">>info    - Exit");
	}
	
/**********************************************************************************************************
	local utilities
***********************************************************************************************************/

	static private void getEnvMap () {
        Map<String, String> env = System.getenv();

    	lJAVAHOME = env.get("JAVA_HOME");
    	if (lJAVAHOME == null) {
    		System.out.println(">>error    - Environment variable JAVA_HOME is null");
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
				if (lArg.indexOf("map") > -1) {
					System.out.println("Tool processing");
					LDDToolFlag = false;
					mapToolFlag = true;
					PDSOptionalFlag = true;
				}
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
//				if (lArg.indexOf('c') > -1) {
//					LDDClassElementFlag = true;
//				}
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
					System.out.println("LDDTOOL Version: " + LDDToolVersionId + " - Built with IM Version:" + buildIMVersionId);
					System.exit(0);
				}
				if (lArg.indexOf('D') > -1) {
					exportDDFileFlag = true;
				}
				if (lArg.indexOf('J') > -1) {
					exportJSONFileFlag = true;
				}
				if (lArg.indexOf('1') > -1) {
					exportSpecFileFlag = true;
				}
				if (lArg.indexOf('3') > -1) {
					exportJSONAttrFlag = true;
				}
				if (lArg.indexOf('4') > -1) {
					importJSONAttrFlag = true;
				}
				if (lArg.indexOf('f') > -1) {
					aind++;
					while (aind < args.length) { 
						String temFileName = args[aind];                                	                                		
						if (((temFileName.endsWith(".csv")) || (temFileName.endsWith(".CSV"))) && (temFileName.startsWith("PDS4_"))) {
							if ( ! checkFileName (args[aind])) {
								System.out.println(" ");
								System.out.println(">>ERROR: " + "Input file not found: " + temFileName); 
							}
						} else {
							System.out.println(" ");
							System.out.println(">>ERROR: " + "Input file name prefix is \"PDS4_\" and suffix is \".CSV\" " + temFileName); 
						}
						propertyMapFileName.add(temFileName); // accept only valid files
						aind++; 
					}
				}
			} else {
				SchemaFileDefn lLDDSchemaFileDefn = new SchemaFileDefn(lArg);
				lLDDSchemaFileDefn.sourceFileName = lArg;
				lLDDSchemaFileDefn.isActive = true;
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
			if (LDDToolMissionGovernanceFlag) {
				governanceLevel = "Mission";
				LDDToolSingletonClassTitle = "Mission_Area";
			} else {
				governanceLevel = "Discipline";
				LDDToolSingletonClassTitle = "Discipline_Area";
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
		if (file.exists() && (file.isFile())) {
			if (debugFlag) System.out.println (">>info    - Found input file: " + inputFileName);
			return true;
		}
		System.out.println(">>error   - " + "Input file not found: " + inputFileName);
		return false;
	}	
		
	static public void printHelp () {
		if (mapToolFlag) {
			System.out.println("\nUsage: termmap -f  inputFileName.CSV");
		
            System.out.println("  -f \"PropertyMapFile.csv\"  the file name is in the following format: ");
            System.out.println("   PDS4_<namespace_id>_<steward_id>_<value_type>_MAP_<version_id>.CSV");
            System.out.println("	e.g. PDS4_INSIGHT_DEEN_PDS3_MAP_1A00.CSV");
            System.out.println("      <value_types> can be {PDS3, VICR, NUANCE, ....} ");
            System.out.println("  or -f *.csv ");
            System.out.println("   the resulting output file name is: ");
            System.out.println("   PDS4_<namespace_id>_<steward_id>_<value_type>_MAP_<version_id>.XML"); 
            System.out.println("   generated into the map directory \n");
		} else {
			System.out.println("\nUsage: lddtool -pl [OPTION]... FILE1 FILE2 ... ");
			System.out.println("Parse a local data dictionary definition file and generate PDS4 data standard files.");
			
			System.out.println("\nExample: lddtool -pl  inputFileName");
			
			System.out.println("\nProcess control:");
			System.out.println("  -p, --PDS4      Set the context to PDS4");
			System.out.println("  -l, --LDD       Process a local data dictionary input file");
			System.out.println("  -a, --attribute Write definitions for attribute elements.");
			System.out.println("  -D, --DataDict  Write the Data Dictionary DocBook file.");
			System.out.println("  -J, --JSON      Write the master data dictionary to a JSON formatted file.");
			System.out.println("  -m, --merge     Generate file to merge the local dictionary into the master dictionary");
			System.out.println("  -M, --Mission   Indicates mission level governance (includes msn directory specification)");
			System.out.println("  -n, --nuance    Write nuance property maps to LDD schema annotation in JSON");
			System.out.println("  -1, --IM Spec   Write the Information Model Specification with LDD.");
			System.out.println("  -v, --version   Returns the LDDTool version number");
			System.out.println("  -h, --help      Print this message");
			
			System.out.println("\nInput control:");
			System.out.println("  FILE provides the file name of an input file. The file name extension .xml is assumed.");
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

	static void setupNameSpaceInfoAll (Properties prop) {
		SchemaFileDefn lSchemaFileDefn;
		String SCHEMA_LITERAL = "lSchemaFileDefn.";
		String IDENTIFIER = ".identifier";
		
//		System.out.println("\n>>info    - config.properties:");
		
        Set<Object> keys = prop.keySet();	
        for (Object k:keys) {
        	String key = (String)k;
        	// look for schema entries
        	if (key.startsWith(SCHEMA_LITERAL) && key.endsWith(IDENTIFIER)) {
        		String nameSpaceId = prop.getProperty(key);
//        		System.out.println(">>info    - namespace_id:"+ nameSpaceId);
        		lSchemaFileDefn = new SchemaFileDefn(nameSpaceId);
        		String isMasterKey = SCHEMA_LITERAL+nameSpaceId + ".isMaster";
        	    String value = prop.getProperty(isMasterKey);
        		if (value != null){
        			if (value.equals("true")) {
          			   lSchemaFileDefn.isMaster = true;
         			   lSchemaFileDefn.isLDD = false;
        			} else {
        				lSchemaFileDefn.isMaster = false;
        				lSchemaFileDefn.isLDD = true;
        			}
        		} else{
        			System.out.println("Missing schema config item: "+ isMasterKey);
        		}
        		String versionIdKey = SCHEMA_LITERAL+nameSpaceId + ".versionId";
        	
        	    value = prop.getProperty(versionIdKey);
        		if (value != null){
        			lSchemaFileDefn.versionId =  value;
        		} else if (lSchemaFileDefn.isMaster) {
                                lSchemaFileDefn.versionId = infoModelVersionId;
        		} else {
        			System.out.println("Missing schema config item: "+ versionIdKey);
        		}
        		String labelVersionIdKey = SCHEMA_LITERAL+nameSpaceId + ".labelVersionId";
        	    value = prop.getProperty(labelVersionIdKey);
        		if (value != null){
        			lSchemaFileDefn.labelVersionId =  value;
        		} else if (lSchemaFileDefn.isMaster) {
                                lSchemaFileDefn.labelVersionId = schemaLabelVersionId;
        		} else{
        			System.out.println("Missing schema config item: "+ labelVersionIdKey);
        		}
           		String isDisciplineKey = SCHEMA_LITERAL+nameSpaceId + ".isDiscipline";
        	    value = prop.getProperty(isDisciplineKey);
        		if (value != null){
        			if (value.equals("true")) {
        			   lSchemaFileDefn.isDiscipline = true;
        			   lSchemaFileDefn.governanceLevel = "Discipline";
        			} else
        			   lSchemaFileDefn.isDiscipline = false;
        		} 
        		
        		String isMissionKey = SCHEMA_LITERAL+nameSpaceId + ".isMission";
        		value = prop.getProperty(isMissionKey);
            	if (value != null){
            		if (value.equals("true")) {
            			   lSchemaFileDefn.isMission = true;
            			   lSchemaFileDefn.governanceLevel = "Mission";
            		} else
            		       lSchemaFileDefn.isMission = false;
        	    }    	
        		
          		String stewardArrKey = SCHEMA_LITERAL+nameSpaceId + ".stewardArr";
        	    value = prop.getProperty(stewardArrKey);
        		if (value != null){
        		   String[] stewardArray = value.split(",");
        		   for (int i= 0; i < stewardArray.length; i++) {
        			   lSchemaFileDefn.stewardArr.add(stewardArray[i]);
        		   }       	
        		} else{
        			System.out.println("Missing schema config item: "+ stewardArrKey);
        		}
//        		lSchemaFileDefn.setVersionIds();
        		
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
        		
          		String regAuthIdKey = SCHEMA_LITERAL+nameSpaceId + ".regAuthId";
        	    value = prop.getProperty(regAuthIdKey);
        		if (value != null){        		
            			   lSchemaFileDefn.regAuthId = value;
        		}

        		masterAllSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
        		
/*				if (DMDocument.debugFlag) {
	        		System.out.println("\ndebug setupNameSpaceInfoAll lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier);
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
	        		System.out.println("                            lSchemaFileDefn.regAuthId:" + lSchemaFileDefn.regAuthId);
	        		System.out.println("                            lSchemaFileDefn.governanceLevel:" + lSchemaFileDefn.governanceLevel);
	        		System.out.println("                            lSchemaFileDefn.isMaster:" + lSchemaFileDefn.isMaster);
	        		System.out.println("                            lSchemaFileDefn.isLDD:" + lSchemaFileDefn.isLDD);
	        		System.out.println("                            lSchemaFileDefn.isDiscipline:" + lSchemaFileDefn.isDiscipline);
	        		System.out.println("                            lSchemaFileDefn.isMission:" + lSchemaFileDefn.isMission);
				} */
        		
     			if (lSchemaFileDefn.isMaster) {
//           		   System.out.println("debug setupNameSpaceInfoAll - set masterPDSSchemaFileDefn - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier);
           		   masterSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
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
    		if (debugFlag) System.out.println(">>info    - Configured NameSpaceIds:" + lNamespaceIdArr);
    		
    		// update to masterSchemaFileSortMap to process and write the target LDD
    		if (DMDocument.LDDToolFlag) {	
 				masterSchemaFileSortMap.put(masterLDDSchemaFileDefn.identifier, masterLDDSchemaFileDefn);
    		}
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
	static public DOMClass getDOMClassDisposition (DOMClass lClass, String lClassName, boolean isFromProtege) {
		// get disposition identifier - if isFromProtege, then the identifier is set else it is not since it is from an LDD.
		String lDispId = lClass.subModelId + "." + registrationAuthorityIdentifierValue + "." + lClassName;
		if (! isFromProtege) lDispId = "LDD_" + lDispId;
		DispDefn lDispDefn = masterClassDispoMap2.get(lDispId);
		if (lDispDefn != null) {
			lClass.used = lDispDefn.used;
			lClass.section = lDispDefn.section;
			String lDisp = lDispDefn.disposition;
			lClass.steward = lDispDefn.intSteward;
			String lClassNameSpaceIdNC = lDispDefn.intNSId;
			lClass.nameSpaceIdNC = lClassNameSpaceIdNC;
			lClass.nameSpaceId = lClassNameSpaceIdNC + ":";
			
			// if from protege, the identifier needs to be set; if from LDD it cannot be set here.
			if (isFromProtege) lClass.identifier = DOMInfoModel.getClassIdentifier(lClassNameSpaceIdNC, lClassName);
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
			return lClass;
		}
		return null;
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

/*
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
//		Undeprecate Radio Science as per CCB-247
//	    deprecatedObjects2.add(new DeprecatedDefn ("Instrument.type.Radio Science", "pds", "Instrument", "pds", "type", "Radio Science", false));
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
*/

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
		
		deprecatedObjects2.add(new DeprecatedDefn ("Array_1D", "pds", "Array_1D", "", "", "", false));

		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date", "pds", "ASCII_Date", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time", "pds", "ASCII_Date_Time", "", "", "", false));
		deprecatedObjects2.add(new DeprecatedDefn ("ASCII_Date_Time_UTC", "pds", "ASCII_Date_Time_UTC", "", "", "", false));
		
		// get ArrayList for *** testing only ***
		deprecatedAttrValueArr = new ArrayList <String> ();
		for (Iterator <DeprecatedDefn> i = deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lObject = (DeprecatedDefn) i.next();
			if (lObject.value.compareTo("") != 0) {
				String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className + "." + lObject.attrName + "." + lObject.value;			
				deprecatedAttrValueArr.add(lIdentifier);
				continue;
			}
			if (lObject.attrName.compareTo("") != 0) {
				String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className + "." + lObject.attrName;
				deprecatedAttrValueArr.add(lIdentifier);
				continue;
			}
			String lIdentifier = lObject.classNameSpaceIdNC + "." + lObject.className;
			deprecatedAttrValueArr.add(lIdentifier);
		}
	}
	
	static void setexposedElementFlag () {
		// the set of classes and attributes that will be externalized (defined as xs:Element)
		exposedElementArr = new ArrayList <String> ();
		exposedElementArr.add("0001_NASA_PDS_1.pds.Ingest_LDD");
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
	
	static void setClassVersionIdFlag () {
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
	}
}
