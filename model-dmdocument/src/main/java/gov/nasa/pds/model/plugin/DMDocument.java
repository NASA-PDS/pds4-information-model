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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.model.plugin.util.Utility;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Main for creating Document
 *
 */
public class DMDocument extends Object {
	
  protected static final Logger log = LoggerFactory.getLogger(DMDocument.class);

  // environment variables
  static String parentDir;
  static String lUSERNAME;

  // specification document info
  static DocDefn docInfo;
  static final String docFileName = "DMDocument";
  static final String upperModelFileName = "UpperModel.pont";

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

  static String dataDirPath = "TBD_dataDirPath";
  private static String outputDirPath = "./";

  static String DMDocVersionId = "0.0.0";
  // static String XMLSchemaLabelBuildNum = "6a";
  static String XMLSchemaLabelBuildNum;

  // Version VID MOD Build
  // 1.0.0.0 - 1.0 - Build 3b 
  // 1.1.0.0 - 1.1 - Build 4a 
  // 1.1.0.1 - 1.2 - Build 4a 
  // 1.2.0.0 - 1.3 - Build 4b - Desired 
  // 1.2.0.0 - 1.3 - Build x4a - Actual 
  // 1.2.0.1 - 1.4 - Build 4b 
  // 1.3.0.0 - 1.5 - Build 5a 
  // 1.3.0.1 - 1.6 - Build 5a 
  // 1.4.0.0 - 1.7 - Build 5b 
  // 1.4.0.0 - x1.3 - x1.6 - Build 5b - error 
  // 1.4.0.1 - 1.8 - Build 5b - not released  
  // 1.4.1.0 - 1.8 - Build 5b 
  // 1.5.0.0 - 1.9 - Build 6a 
  // 1.5.1.0 - 1.9 - Build 6b - not released  
  // 1.6.0.0 - 1.10 - Build 6b 
  // 1.7.0.0 - 1.11 - Build 7a 
  // 1.8.0.0 - 1.12 - Build 7b 
  // 1.9.0.0 - 1.13 - Build 8a 
  // 1.9.1.0 - 1.14 - Build 8a 
  // 1.10.0.0 - 1.15 - Build 8b - 1A00 
  // 1.10.1.0 - 1.16 - Build 8b - 1A10 
  // 1.11.0.0 - 1.17 - Build 9a - 1B00 
  // 1.12.0.0 - 1.18 - Build 9b - 1C00 
  // 1.13.0.0 - 1.19 - Build 10a - 1D00  
  // 1.14.0.0 - 1.20 - Build 10b - 1E00  
  // 1.15.0.0 - 1.21 - Build 11.0 - 1F00 
  // 1.16.0.0 - 1.22 - Build 11.1 - 1G00 
  // 1.17.0.0 - 1.23 - Build 12.0 - 1H00 
  // 1.18.0.0 - 1.24 - Build 12.1 - 1I00 - Mar/22
  // 1.19.0.0 - 1.25 - Build 13.0 - 1J00 - Sep/22
  // 1.20.0.0 - 1.26 - Build 13.1 - 1K00 - Mar/23
  // 1.21.0.0 - 1.27 - Build 14.0 - 1L00 - Sep/23
  
  // 1.22.0.0 - 1.0 - Build 14.1 - 1M00 - Mar/24
  // 1.23.0.0 - 1.0 - Build 15.0 - 1N00 - Sep/24
  // 1.24.0.0 - 1.0 - Build 15.1 - 1O00 - Mar/25
  // 1.25.0.0 - 1.0 - Build 16.0 - 1P00 - Sep/25
  // 1.26.0.0 - 1.0 - Build 16.1 - 1Q00 - Mar/26
  // 1.27.0.0 - 1.0 - Build 17.0 - 1R00 - Sep/26
  // 1.28.0.0 - 1.0 - Build 17.1 - 1S00 - Mar/27
  // 1.29.0.0 - 1.0 - Build 18.0 - 1T00 - Sep/27

  
	
  // x.x.x.x - 1.0 - 1.n - Build nm - first version of product will always be 1.0
  // Modification history will continue with 1.n

  static String LDDToolVersionId = "0.0.0";
  static String buildDate = "";
  static String buildIMVersionId = "1.25.0.0";
  static String buildIMVersionFolderId = "1P00";
  final static String versionIdDefault = "1.0" + ".0.0";  // as a single string the literal is a security issue
  static String classVersionIdDefault = versionIdDefault;
  static boolean PDS4MergeFlag = false; // create protege output; not currently used
  // static boolean LDDClassElementFlag = false; // if true, write XML elements for classes
  static boolean LDDAttrElementFlag = false; // if true, write XML elements for attributes
  static boolean LDDNuanceFlag = false;
  static boolean overWriteClass = true; // use dd11179.pins class disp, isDeprecated, and versionId
                                         // to overwrite Master DOMClasses, DOMAttrs, and
                                         // DOMPermvalues
  // alternate IM Version
  // if no option "V" is provided on the command line, then the default is the current IM version.
  static String runIMVersion = buildIMVersionFolderId; // default
  // allowed alternate IM versions
  static ArrayList<String> alternateIMVersionArr;

  // import export file flags
  static boolean exportJSONFileFlag = false; // LDDTool, set by -J option
  static boolean includeAllNamespacesFlag = false; // LDDTool, set by --include-all-namespaces
  static boolean exportSpecFileFlag = false;
  static boolean exportDDFileFlag = false;
  static boolean exportTermMapFileFlag = false;
  static boolean exportOWLRDFTTLFileFlag = false;
  static boolean exportOWLRDFFileFlag = false;
  static boolean exportCustomFileFlag = false;
  
  static boolean importJSONAttrFlag = false; // non PDS processing - not currently used
  static boolean pds4ModelFlag = false; // set in config.properties files (read by WriteDOMDocBook
                                        // to exclude PDS3 from generated DD)
  static boolean printNamespaceFlag = false; // print the configured namespaces to the log
  static boolean disciplineMissionFlag = false; // set by -d; Omit the term "mission" from the
                                                // namespace of a Mission dictionary
  static int writeDOMCount = 0; // *** Deprecate *** LDDParser DOM Error write count; if
                                // exportDOMFlag=true then DOM code is executed and so error/warning
                                // messages are duplicated in log and txt file.

  // when true this flag indicates an LDDTool run for a namespace other than pds (i.e., Common)
  static boolean LDDToolFlag;

  // misc flags
  static boolean debugFlag = false; // should be false

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
  static ArrayList<LDDDOMParser> LDDDOMModelArr;

  // Schemas, Stewards and Namespaces (SchemaFileDefn)

  // *** initialized from the config file - maybe rename, to be used only during initialization of
  // LDDSchemaFileSortMap ***
  static TreeMap<String, SchemaFileDefn> masterAllSchemaFileSortMap = new TreeMap<>(); // all
                                                                                       // namespaces
                                                                                       // in
                                                                                       // config.properties
                                                                                       // file.

  // *** to be use only for LDDs ***
  static TreeMap<String, SchemaFileDefn> LDDSchemaFileSortMap = new TreeMap<>();
  static ArrayList<SchemaFileDefn> LDDSchemaFileSortArr;
  static ArrayList<String> LDDImportNameSpaceIdNCArr = new ArrayList<>();

  // *** to be used to detect issues by namespace
  static ArrayList<String> nameSpaceIdExtrnFlagArr = new ArrayList<>(); // <element_flag> set to
                                                                        // true in each namespace

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
  static String masterTodaysDateTimeUTCFromInstant;
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

  // registry classes and attributes
  static ArrayList<String> registryClass;
  static ArrayList<String> registryAttr;

  // reserved Class names
  static ArrayList<String> reservedClassNames;

  // reserved Attribute names
  static ArrayList<String> reservedAttrNames;

  // Omitted classes
  static ArrayList<String> omitClass;

  // the set of deprecated classes, attributes, and values
  static ArrayList<DeprecatedDefn> deprecatedObjects2;
  static String Literal_DEPRECATED = " *Deprecated*";
  static boolean deprecatedAddedDOM = false;

  // the set of classes and attributes that will be externalized (defined as xs:Element)
  static ArrayList<String> exposedElementArr;
  
  // the set of attributes that will be externalized (defined as xs:Element)
  static ArrayList<String> exposedAttributeArr;
  
  // info, warning, and error messages
  static int msgOrder = 100000;
  public static ArrayList<DOMMsgDefn> mainMsgArr = new ArrayList<>();
  static DOMMsgDefn masterDOMMsgDefn = new DOMMsgDefn();
  static TreeMap<String, Integer> messageLevelCountMap = new TreeMap<>();
  static Integer lMessageWarningCount = 0;
  static Integer lMessageErrorCount = 0;
  static Integer lMessageFatalErrorCount = 0;

  // Property List for Config.Properties
  static Properties configProps = new Properties();
  static Properties nameSpaceProps = new Properties();

  static ArrayList<String> propertyMapFileName = new ArrayList<>();

  // command line argument parser
  static ArgumentParser parser;

  /**
   * main method
   * 
   * @param args
   * @throws Throwable
   */
  public static void main(String[] args) throws Throwable {
	boolean success = run(args);
    
    if (!success) {
      System.out.println("");
      System.out.println(">>  INFO Exit(1)");
      System.exit(1);
    }
    System.out.println("");
    System.out.println(">>  INFO Exit(0)");
    System.exit(0);
  }
  
  /**
   * DMDocument Plugin Runner
 * @throws Throwable 
 * @throws IOException 
   */
  public static boolean run(String[] args) throws Throwable {
	  init();

	  // get the command line arguments using argparse4j
	  Namespace argparse4jNamespace = null;
	  try {
		  argparse4jNamespace = getArgumentParserNamespace(args);
	  } catch (Throwable e) {
		  return false;
	  }

	    String nameSpaceDataDirPath = null;

	    // process first set of arguments
	    // this must be done before config file processing
	    // the use of the option "V" (alternate IM version) will change the input file directory (config
	    // included)
	    processArgumentParserNamespacePhase1(argparse4jNamespace);
	    
	    String sysDataHome = System.getProperty("data.home");
	    if (sysDataHome != null) {
	      sysDataHome = sysDataHome.replace('\\', '/');
	      parentDir = sysDataHome + "/";
	      dataDirPath = parentDir;
	      nameSpaceDataDirPath = dataDirPath;
	      // if this is an LDDTool run then an alternate path is allowed (option "V")
	      if (runIMVersion.compareTo(buildIMVersionFolderId) != 0) {
	          dataDirPath = parentDir + runIMVersion + "/";
	      }
	    } else {
	      Utility.registerMessage("0>info - Property data.home is null");   
	      String sysUserDir = System.getProperty("user.dir");
	      if (sysUserDir == null) {
	        Utility.registerMessage("3>error Environment variable sysUserDir is null");
	        printErrorMessages();
	        System.exit(1);
	      }
	      sysUserDir = sysUserDir.replace('\\', '/');
	      parentDir = sysUserDir;
	      String dirExt = "/model-ontology/src/ontology/Data/";
	      dataDirPath = parentDir + dirExt;
	      nameSpaceDataDirPath = dataDirPath;
	      if (debugFlag) {
	          parentDir = System.getProperty("user.home") + "/git/pds4-information-model/model-ontology/src/ontology";
	    	  dataDirPath = System.getProperty("user.home") + "/git/pds4-information-model/model-ontology/src/ontology/Data/";
		      nameSpaceDataDirPath = dataDirPath;
		  }
	      // if this is an LDDTool run then an alternate path is allowed (option "V")
	      if (runIMVersion.compareTo(buildIMVersionFolderId) != 0) {
	    	  dataDirPath = parentDir + "/Data/" + runIMVersion + "/";
	      }
	    }
	    Utility.registerMessage("0>info - Parent Directory:" + parentDir);
	    Utility.registerMessage("0>info - IM Directory Path:" + dataDirPath);
	    Utility.registerMessage("0>info - NS Directory Path:" + nameSpaceDataDirPath);
	    Utility.registerMessage("0>info - IM Versions Available:" + alternateIMVersionArr);
	    
	    // read the configuration file and initialize key attributes; SchemaFileDefn map is initialized
	    // below (setupNameSpaceInfoAll)
	    // "props" are used again below in setupNameSpaceInfoAll)
	    String configInputFileSpecName = dataDirPath + "config.properties";
	    String configInputStr;
      configProps = getConfigProperties(configInputFileSpecName);
      configInputStr = configProps.getProperty("infoModelVersionId");
      if (configInputStr != null) {
        infoModelVersionId = configInputStr;
      }
      configInputStr = configProps.getProperty("schemaLabelVersionId");
      if (configInputStr != null) {
        schemaLabelVersionId = configInputStr;
      }
      configInputStr = configProps.getProperty("pds4BuildId");
      if (configInputStr != null) {
        pds4BuildId = configInputStr;
      }
      configInputStr = configProps.getProperty("imSpecDocTitle");
      if (configInputStr != null) {
        imSpecDocTitle = configInputStr;
      }
      configInputStr = configProps.getProperty("imSpecDocAuthor");
      if (configInputStr != null) {
        imSpecDocAuthor = configInputStr;
      }
      configInputStr = configProps.getProperty("imSpecDocSubTitle");
      if (configInputStr != null) {
        imSpecDocSubTitle = configInputStr;
      }
      configInputStr = configProps.getProperty("ddDocTitle");
      if (configInputStr != null) {
        ddDocTitle = configInputStr;
      }
      configInputStr = configProps.getProperty("debugFlag");
      if (configInputStr != null && configInputStr.compareTo("true") == 0) {
        debugFlag = true;
      }
      // configInputStr= props.getProperty("lSchemaFileDefn.pds.regAuthId");
      configInputStr = configProps.getProperty("mastRegAuthId");
      if (configInputStr != null) {
        registrationAuthorityIdentifierValue = configInputStr;
        registeredByValue = "RA_" + registrationAuthorityIdentifierValue;
      }
      configInputStr = configProps.getProperty("ddDocTeam");
      if (configInputStr != null) {
        ddDocTeam = configInputStr;
      }
      configInputStr = configProps.getProperty("pds4ModelFlag");
      if (configInputStr != null && configInputStr.compareTo("true") == 0) {
        pds4ModelFlag = true;
      }
      configInputStr = configProps.getProperty("mastModelId");
      if (configInputStr != null) {
        mastModelId = configInputStr;
      }

      configInputStr = configProps.getProperty("toolVersionId");
      if (configInputStr != null) {
        DMDocVersionId = LDDToolVersionId = configInputStr;
      }
      configInputStr = configProps.getProperty("buildDate");
      if (configInputStr != null) {
        buildDate = configInputStr;
      }

	    // process second set of arguments; exit if return=false
	    if (! processArgumentParserNamespacePhase2(argparse4jNamespace))
	    	return false;

	    // check the files
	    checkRequiredFiles();

	    if (LDDToolFlag) {
	      for (Iterator<SchemaFileDefn> i = LDDSchemaFileSortArr.iterator(); i.hasNext();) {
	        SchemaFileDefn lSchemaFileDefn = i.next();
	        cleanupLDDInputFileName(lSchemaFileDefn);
	      }
	    }
	    
	    // set up the System Build version
	    XMLSchemaLabelBuildNum = pds4BuildId;

	    // intialize the masterAllSchemaFileSortMap - all namespaces in config.properties file
	    // set up the Master Schema Information for both normal and LDD processing (dirpath, namespaces,
	    // etc)
	    
	    String nsConfigInputFileSpecName = nameSpaceDataDirPath + "config.properties";
	    Properties nameSpaceConfigProps = getConfigProperties(nsConfigInputFileSpecName);	    
	    setupNameSpaceInfoAll(nameSpaceConfigProps);

	    // output the context info
	    if (!LDDToolFlag) {
	      Utility.registerMessage("1>info DMDoc Version: " + DMDocVersionId);
	      Utility.registerMessage("1>info IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);
	      Utility.registerMessage("1>info IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);
	      Utility.registerMessage(
	          "1>info IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);
	    } else {
	      Utility.registerMessage("1>info LDDTOOL Version: " + LDDToolVersionId);
	      Utility.registerMessage("1>info IM Version Id: " + DMDocument.masterPDSSchemaFileDefn.versionId);
	      Utility.registerMessage("1>info IM Namespace Id: " + DMDocument.masterPDSSchemaFileDefn.identifier);
	      Utility.registerMessage(
	          "1>info IM Label Version Id: " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);
	    }

	    Utility.registerMessage("1>info Date: " + sTodaysDate);
	    Utility.registerMessage("1>info PARENT_DIR: " + parentDir);

	    // get the 11179 Attribute Dictionary - .pins file
	    ProtPinsDOM11179DD lProtPinsDOM11179DD = new ProtPinsDOM11179DD();
	    lProtPinsDOM11179DD.getProtPins11179DD(DMDocument.registrationAuthorityIdentifierValue,
	        DMDocument.dataDirPath + "dd11179.pins");

	    // get the models
	    GetDOMModelDoc lGetDOMModelDoc = new GetDOMModelDoc();
	    lGetDOMModelDoc.getModels(docFileName + ".pins");

	    // get the DOM Model
	    GetDOMModel lGetDOMModel = new GetDOMModel();
	    lGetDOMModel.getDOMModel(docFileName + ".pins");
	    if (debugFlag) {
	      DOMInfoModel.domWriter(DOMInfoModel.masterDOMClassArr, "DOMModelListPerm.txt");
	    }
	    
	    // export the models
	    if (DMDocument.LDDToolFlag) {
	      ExportModels lExportModels = new ExportModels();
	      lExportModels.writeLDDArtifacts();
	    } else if (DMDocument.mapToolFlag) {
	      WriteMappingFile writeMappingFile = new WriteMappingFile();
	      writeMappingFile.writeMappingFile(registrationAuthorityIdentifierValue, propertyMapFileName);
	    } else {
	      ExportModels lExportModels = new ExportModels();
	      lExportModels.writeAllArtifacts();
	    }
	    Utility.registerMessage("0>info Next UID: " + DOMInfoModel.getNextUId());
	    printErrorMessages();
	    
	    boolean success = true;
	    if (lMessageErrorCount > 0 || lMessageFatalErrorCount > 0) {
	      success = false;
	    }
	    
	    reset();
	    
	    return success;
  }

  /**********************************************************************************************************
   * local utilities
   * 
   * @throws IOException
   ***********************************************************************************************************/
  
  // get the config.properties file
  static private Properties getConfigProperties (String configInputFileSpecName) {
	  Properties properties = new Properties();
	    File configFile = new File(configInputFileSpecName);
	    FileReader reader = null;
	    try {
	      reader = new FileReader(configFile);
	      properties.load(reader);
	    } catch (FileNotFoundException ex) {
	      // file does not exist
	      Utility.registerMessage("3>error Configuration file does not exist. [config.properties]");
	    } catch (IOException ex) {
	      // I/O error
	      Utility.registerMessage("3>error Configuration file IO Exception. [config.properties]");
	    } finally {
	    	try {
	    		reader.close();
	    	} catch (IOException|NullPointerException e) {
	    		// Do nothing
	    	}
	    }
	  return properties;
  }

  static private void cleanupLDDInputFileName(SchemaFileDefn lSchemaFileDefn) throws IOException {
    String lSourceFileSpec = lSchemaFileDefn.sourceFileName;
    lSourceFileSpec = replaceString(lSourceFileSpec, "\\", "/");
    // String lSourceFileSpecToLower = lSourceFileSpec.toLowerCase();

    lSchemaFileDefn.LDDToolInputFileName = lSourceFileSpec;
    lSchemaFileDefn.LDDToolOutputFileNameNE = null; // *** deprecate ***


    if (!Utility.checkFileName(lSchemaFileDefn.LDDToolInputFileName)) {
      lSchemaFileDefn.LDDToolInputFileName = lSchemaFileDefn.LDDToolInputFileName.toLowerCase();
      if (!Utility.checkFileName(lSchemaFileDefn.LDDToolInputFileName)) {
        lSchemaFileDefn.LDDToolInputFileName = lSchemaFileDefn.LDDToolInputFileName.toUpperCase();
        if (!Utility.checkFileName(lSchemaFileDefn.LDDToolInputFileName)) {
          String msg = "Input file not found: " + lSchemaFileDefn.sourceFileName;
          Utility.registerMessage("3>error " + msg);
          parser.printHelp();
          printErrorMessages();
          throw new IOException(msg);
        }
      }
    }
  }

  static public void checkRequiredFiles() throws IOException {
    // check that all the required data files exist

    File file = new File(dataDirPath + "UpperModel.pont");
    boolean isFound = file.exists();
    String msg = "";
    if (!isFound) {
      msg = "Required data file was not found: " + dataDirPath + "UpperModel.pont";
      Utility.registerMessage("3>error " + msg);
      printErrorMessages();
      throw new IOException(msg);
    }

    file = new File(dataDirPath + "dd11179.pins");
    isFound = file.exists();
    if (!isFound) {
      msg = "Required data file was not found: " + dataDirPath + "dd11179.pins";
      Utility.registerMessage(
          "3>error " + msg);
      printErrorMessages();
      throw new IOException(msg);
    }

    file = new File(dataDirPath + "Glossary.pins");
    isFound = file.exists();
    if (!isFound) {
      msg = "Required data file was not found: " + dataDirPath + "Glossary.pins";
      Utility.registerMessage(
          "3>error " + msg);
      printErrorMessages();
      throw new IOException(msg);
    }

    file = new File(dataDirPath + "DMDocument.pins");
    isFound = file.exists();
    if (!isFound) {
      msg = "Required data file was not found: " + dataDirPath + "DMDocument.pins";
      Utility.registerMessage(
          "3>error " + msg);
      printErrorMessages();
      throw new IOException(msg);
    }
  }
  
  static private void init() {
	  // configuration file variables
	  infoModelVersionId = "0.0.0.0";
	  schemaLabelVersionId = "0.0";
	  pds4BuildId = "0a";

	  imSpecDocTitle = "TBD_imSpecDocTitle";
	  imSpecDocAuthor = "TBD_imSpecDocAuthor";
	  imSpecDocSubTitle = "TBD_imSpecDocSubTitle";
	  ddDocTitle = "TBD_ddDocTitle";
	  ddDocTeam = "TBD_ddDocTeam";

	  dataDirPath = "TBD_dataDirPath";

	  DMDocVersionId = "0.0.0";

	  LDDToolVersionId = "0.0.0";
	  buildDate = "";
	  buildIMVersionId = "1.25.0.0";
	  buildIMVersionFolderId = "1P00";
	  classVersionIdDefault = versionIdDefault;
	  PDS4MergeFlag = false; // create protege output; not currently used
	  LDDAttrElementFlag = false; // if true, write XML elements for attributes
	  LDDNuanceFlag = false;
	  overWriteClass = true; // use dd11179.pins class disp, isDeprecated, and versionId
	                                         // to overwrite Master DOMClasses, DOMAttrs, and
	                                         // DOMPermvalues
	  // alternate IM Version
	  // if no option "V" is provided on the command line, then the default is the current IM version.
	  runIMVersion = buildIMVersionFolderId; // default

	  // import export file flags
	  exportJSONFileFlag = false; // LDDTool, set by -J option
	  includeAllNamespacesFlag = false; // LDDTool, set --include-all-namespaces
	  exportSpecFileFlag = false;
	  exportDDFileFlag = false;
	  exportTermMapFileFlag = false;
	  exportOWLRDFTTLFileFlag = false;
	  exportOWLRDFFileFlag = false;
	  exportCustomFileFlag = false;
	  
	  importJSONAttrFlag = false; // non PDS processing - not currently used
	  pds4ModelFlag = false; // set in config.properties files (read by WriteDOMDocBook
	                                        // to exclude PDS3 from generated DD)
	  printNamespaceFlag = false; // print the configured namespaces to the log
	  disciplineMissionFlag = false; // set by -d; Omit the term "mission" from the
	                                                // namespace of a Mission dictionary
	  writeDOMCount = 0; // *** Deprecate *** LDDParser DOM Error write count; if
	                                // exportDOMFlag=true then DOM code is executed and so error/warning
	                                // messages are duplicated in log and txt file.
	  LDDToolMissionFlag = false;
	  LDDToolSingletonClassTitle = "USER";
	  LDDToolSingletonDOMClass = null;

	  mapToolFlag = false;
	  masterAllSchemaFileSortMap = new TreeMap<>();
	  LDDSchemaFileSortMap = new TreeMap<>();
	  LDDImportNameSpaceIdNCArr = new ArrayList<>();
	  nameSpaceIdExtrnFlagArr = new ArrayList<>();
	  masterNameSpaceIdNCLC = "TBD_masterNameSpaceIdNCLC";
	  Literal_DEPRECATED = " *Deprecated*";
	  deprecatedAddedDOM = false;
	  msgOrder = 100000;
	  mainMsgArr = new ArrayList<>();
	  masterDOMMsgDefn = new DOMMsgDefn();
	  messageLevelCountMap = new TreeMap<>();
	  lMessageWarningCount = 0;
	  lMessageErrorCount = 0;
	  lMessageFatalErrorCount = 0;
	  configProps = new Properties();
	  nameSpaceProps = new Properties();
	  propertyMapFileName = new ArrayList<>();
	  
	// process state for used flags, files, and directories
	    dmProcessState = new DMProcessState();
	    
	    // System.out.println("Debug main 240515");

	    PDSOptionalFlag = false;
	    LDDToolFlag = false;
	    // Secondary LDD Models
	    LDDDOMModelArr = new ArrayList<>();
	    LDDSchemaFileSortArr = new ArrayList<>();
	    LDDToolAnnotateDefinitionFlag = false;

	    // The current version is included to allow for -V currentIMVersion
	    alternateIMVersionArr = new ArrayList<>();
	    alternateIMVersionArr.add("1P00"); // current
	    alternateIMVersionArr.add("1O00");
	    alternateIMVersionArr.add("1N00");
	    alternateIMVersionArr.add("1M00");
	    alternateIMVersionArr.add("1L00");
	    alternateIMVersionArr.add("1K00");
	    alternateIMVersionArr.add("1J00");
	    alternateIMVersionArr.add("1I00");
	    alternateIMVersionArr.add("1H00");
	    alternateIMVersionArr.add("1G00");
	    alternateIMVersionArr.add("1F00");
	    alternateIMVersionArr.add("1E00");
	    alternateIMVersionArr.add("1D00");
	    alternateIMVersionArr.add("1C00");
	    alternateIMVersionArr.add("1B10");
	    alternateIMVersionArr.add("1B00");

	    // message handling
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
	    sTodaysDate = rTodaysDate.toString();
	    masterTodaysDate = sTodaysDate;
	    masterTodaysDateUTC = getUTCDate();
	    masterTodaysDateyymmdd = masterTodaysDateUTC.substring(2, 4)
	        + masterTodaysDateUTC.substring(5, 7) + masterTodaysDateUTC.substring(8, 10);
	    masterTodaysDateTimeUTC = getUTCDateTime();
	    masterTodaysDateTimeUTCwT = replaceString(masterTodaysDateTimeUTC, " ", "T");
	    masterTodaysDateTimeUTCFromInstant = getUTCDateTimeFromInstant();

	    rdfPrefix = "http://pds.nasa.gov/infomodel/pds#";
	    creationDateValue = masterTodaysDateUTC;
	    beginDatePDS4Value = "2009-06-09";

	    endDateValue = "2019-12-31";
	    futureDateValue = "2019-12-31";
	    versionIdentifierValue = "TBD_versionIdentifierValue";
	    administrationRecordValue = "TBD_administrationRecordValue"; // set in GetModels
	    stewardValue = "Steward_PDS";
	    submitterValue = "Submitter_PDS";

	    // registeredByValue = "RA_0001_NASA_PDS_1";
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
	    reservedClassNames = new ArrayList<>();
	    reservedClassNames.add("Internal_Reference");
	    reservedClassNames.add("Local_Internal_Reference");
	    reservedClassNames.add("Reference_Pixel_Regression_Test");

	    // reserved Attribute names
	    reservedAttrNames = new ArrayList<>();
	    reservedAttrNames.add("logical_identifier");
	    reservedAttrNames.add("local_identifier");
	    reservedAttrNames.add("pixel_latitude_Regression_Test");

	    omitClass = new ArrayList<>();
	    omitClass.add("Data_Object");
	    omitClass.add("Digital_Object");
	    omitClass.add("Physical_Object");
	    omitClass.add("Conceptual_Object");

	    // set registryAttr
	    setRegistryAttrFlag();

	    // set exposed elements
	    setexposedElementFlag();
	    
	    // set exposed attributes
	    setexposedAttributeFlag();
  }
  
  static private void reset() {
	  DOMInfoModel.reset();
	  init();
  }

  static void setupNameSpaceInfoAll(Properties prop) {
    SchemaFileDefn lSchemaFileDefn;
    String SCHEMA_LITERAL = "lSchemaFileDefn.";
    String IDENTIFIER = ".identifier";

    Set<Object> keys = prop.keySet();
    for (Object k : keys) {
      String key = (String) k;
      // look for schema entries
      if (key.startsWith(SCHEMA_LITERAL) && key.endsWith(IDENTIFIER)) {
        String nameSpaceId = prop.getProperty(key);
        // registerMessage ("1>info namespace_id:"+ nameSpaceId);
        lSchemaFileDefn = new SchemaFileDefn(nameSpaceId);
        String isMasterKey = SCHEMA_LITERAL + nameSpaceId + ".isMaster";
        String value = prop.getProperty(isMasterKey);
        if (value != null) {
          if (value.equals("true")) {
            lSchemaFileDefn.setDictionaryType("Common");
          }
        } else {
          Utility.registerMessage("3>error Missing schema config item: " + isMasterKey);
        }

        // default to the master values
        // the value of versionId will be reset by Ingest_LDD
        // the value of labelVersionId is the same for all dictionaries, namely the master
        // the value of labelVersionId is always "1.0"; this is the first label for this product

        lSchemaFileDefn.versionId = infoModelVersionId;
        lSchemaFileDefn.labelVersionId = schemaLabelVersionId;

        String isDisciplineKey = SCHEMA_LITERAL + nameSpaceId + ".isDiscipline";
        value = prop.getProperty(isDisciplineKey);
        if (value != null) {
          if (value.equals("true")) {
            lSchemaFileDefn.setDictionaryType("Discipline");
          }
        }

        String isMissionKey = SCHEMA_LITERAL + nameSpaceId + ".isMission";
        value = prop.getProperty(isMissionKey);
        if (value != null) {
          if (value.equals("true")) {
            lSchemaFileDefn.setDictionaryType("Mission");
          }
        }

        String stewardArrKey = SCHEMA_LITERAL + nameSpaceId + ".stewardArr";
        value = prop.getProperty(stewardArrKey);
        if (value != null) {
          String[] stewardArray = value.split(",");
          for (String element : stewardArray) {
            lSchemaFileDefn.stewardArr.add(element);
          }
        } else {
          Utility.registerMessage("3>error Missing schema config item: " + stewardArrKey);
        }

        String commentKey = SCHEMA_LITERAL + nameSpaceId + ".comment";
        value = prop.getProperty(commentKey);
        if (value != null) {
          lSchemaFileDefn.comment = value;
        }

        String lddNameKey = SCHEMA_LITERAL + nameSpaceId + ".lddName";
        value = prop.getProperty(lddNameKey);
        if (value != null) {
          lSchemaFileDefn.lddName = value;
        }

        String sourceFileNameKey = SCHEMA_LITERAL + nameSpaceId + ".sourceFileName";
        value = prop.getProperty(sourceFileNameKey);
        if (value != null) {
          lSchemaFileDefn.sourceFileName = value;
        }

        String nameSpaceURLKey = SCHEMA_LITERAL + nameSpaceId + ".nameSpaceURL";
        value = prop.getProperty(nameSpaceURLKey);
        if (value != null) {
          lSchemaFileDefn.nameSpaceURL = value;
        }

        String nameSpaceURLKeyS = SCHEMA_LITERAL + nameSpaceId + ".nameSpaceURLs";
        value = prop.getProperty(nameSpaceURLKeyS);
        if (value != null) {
          lSchemaFileDefn.nameSpaceURLs = value;
        }

        String urnPrefixKey = SCHEMA_LITERAL + nameSpaceId + ".urnPrefix";
        value = prop.getProperty(urnPrefixKey);
        if (value != null) {
          lSchemaFileDefn.urnPrefix = value;
        }

        String modelShortNameKey = SCHEMA_LITERAL + nameSpaceId + ".modelShortName";
        value = prop.getProperty(modelShortNameKey);
        if (value != null) {
          lSchemaFileDefn.modelShortName = value;
        }

        String sysBundleNameKey = SCHEMA_LITERAL + nameSpaceId + ".sysBundleName";
        value = prop.getProperty(sysBundleNameKey);
        if (value != null) {
          lSchemaFileDefn.sysBundleName = value;
        }

        String regAuthIdKey = SCHEMA_LITERAL + nameSpaceId + ".regAuthId";
        value = prop.getProperty(regAuthIdKey);
        if (value != null) {
          lSchemaFileDefn.regAuthId = value;
        }

        masterAllSchemaFileSortMap.put(lSchemaFileDefn.identifier, lSchemaFileDefn);
        if (lSchemaFileDefn.isMaster) {
          masterPDSSchemaFileDefn = lSchemaFileDefn;
          masterNameSpaceIdNCLC = lSchemaFileDefn.nameSpaceIdNCLC;
          lSchemaFileDefn.isActive = true; // 7777 isActive is set here temporarily until DOM is
                                           // used; isActive is set above for all IngestLDD
          // set lab_version_id for master so that the remaining lab_version_id can be set below.
          masterPDSSchemaFileDefn.setVersionIds();
        }
      }
    }

    // set set VersionIds
    ArrayList<SchemaFileDefn> lSchemaFileDefnArr =
        new ArrayList<>(DMDocument.masterAllSchemaFileSortMap.values());
    ArrayList<String> lNamespaceIdArr = new ArrayList<>();
    for (Iterator<SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
      SchemaFileDefn lSchemaFileDefn2 = i.next();
      lNamespaceIdArr.add(lSchemaFileDefn2.identifier);
      lSchemaFileDefn2.setVersionIds();
    }

    if (printNamespaceFlag || debugFlag) {
      Utility.registerMessage("1>info Configured NameSpaceIds:" + lNamespaceIdArr);
    }
  }

  /**********************************************************************************************************
   * global utilities
   ***********************************************************************************************************/

  /**
   * Replace string with string (gleaned from internet)
   */

  static String replaceString(String str, String pattern, String replace) {
    int s = 0;
    int e = 0;
    StringBuffer result = new StringBuffer();

    while ((e = str.indexOf(pattern, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(replace);
      s = e + pattern.length();
    }
    result.append(str.substring(s));
    return result.toString();
  }

  /**
   * get a UTC Formated Date/Time from Instant 
   */
  static String getUTCDateTimeFromInstant() {
      Instant now = Instant.now();
      now = now.truncatedTo(ChronoUnit.SECONDS);
      String dateTimeUTC = now.toString();
    return dateTimeUTC;
  }   

  /**
   * get a UTC Formated Date/Time from the machine date
   */
  static String getUTCDateTime() {
    String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    String lDateUTC = sdf.format(new Date());
    return lDateUTC;
  }

  /**
   * get a UTC Formated Date from the machine date
   */
  static String getUTCDate() {
    String DATEFORMAT = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
    // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    String lDateUTC = sdf.format(new Date());
    return lDateUTC;
  }

  /**
   * convert from string to hex
   */
  static String stringToHex(String string) {
    StringBuilder buf = new StringBuilder(200);
    for (char ch : string.toCharArray()) {
      if (buf.length() > 0) {
        buf.append(' ');
      }
      buf.append(String.format("%04x", (int) ch));
    }
    return buf.toString();
  }

  /**********************************************************************************************************
   * set object flags
   ***********************************************************************************************************/

  static void setexposedElementFlag() {
    // the set of classes and attributes that will be externalized (defined as xs:Element)
    exposedElementArr = new ArrayList<>();
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
  }
  
  static void setexposedAttributeFlag() {
    // the set of attributes that will be externalized (defined as xs:Element)
    exposedAttributeArr = new ArrayList<>();
// 555 commented out until DDWG addresses impact of using ref:
//    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.comment");
//    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.description");
    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.local_identifier");
    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.logical_identifier");
//    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.name");
//    exposedAttributeArr.add("0001_NASA_PDS_1.all.USER.pds.kernel_type");
  }

  static void setRegistryAttrFlag() {
    registryAttr = new ArrayList<>();
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
    // registryAttr.add("product_subclass");
    registryAttr.add("start_date_time");
    registryAttr.add("stop_date_time");
    registryAttr.add("start_date");
    registryAttr.add("stop_date");
    registryAttr.add("logical_identifier");
    registryAttr.add("version_id");
  }

  static Namespace getArgumentParserNamespace(String args[]) throws Throwable {
    parser = ArgumentParsers.newFor("LDDTool").build().defaultHelp(true).version(LDDToolVersionId)
        .description("LDDTool process control:");

    parser.addArgument("-p", "--pds4").dest("p").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Set the context to PDS4");

    parser.addArgument("-l", "--ldd").dest("l").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue())
        .help("Process one or more local data dictionary input files");

    parser.addArgument("-d", "--discipline").dest("d").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue())
        .help("Omit the term \"mission\" from the namespace of a dictionary");

    parser.addArgument("-D", "--datadict").dest("D").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Write the Data Dictionary DocBook file");

    parser.addArgument("-J", "--json").dest("J").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Write the data dictionary to a JSON formatted file");
    
    parser.addArgument("-a", "--include-all-namespaces").dest("a").type(Boolean.class).nargs(1)
    	.action(Arguments.storeTrue()).help("Include all namespaces in the JSON output (only valid with --json)");

    parser.addArgument("-m", "--merge").dest("m").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue())
        .help("Generate file to merge the local dictionary into the master dictionary");

    parser.addArgument("-M", "--mission").dest("M").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help(
            "This option has no effect starting with PDS4 IM Version 1.14.0.0. See the LDDTool Usage document for more information on how to provide this information.");

    parser.addArgument("-n", "--nuance").dest("n").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue())
        .help("Write nuance property maps to LDD schema annotation in JSON");

    parser.addArgument("-N", "--namespace").dest("N").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Print the list of configured namespaces to the log");

	parser.addArgument("-1", "--im_specification").dest("1").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue())
        .help("Write the Information Model Specification for an LDD");
	
    parser.addArgument("-T", "--termmap").dest("T").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Terminological mapping to alternate names");

    parser.addArgument("-O", "--ttl") .dest("O") .type(Boolean.class) .nargs(1)
        .action(Arguments.storeTrue()) .help("OWL/RDF/TTL output in TTL format (IM Classification)");

    parser.addArgument("-o", "--rdf") .dest("o") .type(Boolean.class) .nargs(1)
        .action(Arguments.storeTrue()) .help("OWL/RDF output in RDF format (IM Export)");

    parser.addArgument("-C", "--custom") .dest("C") .type(Boolean.class) .nargs(1)
        .action(Arguments.storeTrue()) .help("Customized processing and reporting");

    parser.addArgument("-v", "--version").dest("v").type(Boolean.class).nargs(1)
        .action(Arguments.storeTrue()).help("Returns the LDDTool version number");

    parser.addArgument("-V", "--pds4_version").dest("V").type(String.class)
        .choices("1B00", "1B10", "1C00", "1D00", "1E00", "1F00", "1G00", "1H00", "1I00", "1J00", "1K00", "1L00", "1M00", "1N00", "1O00", "1P00")
        .setDefault(buildIMVersionFolderId).help("Set the IM Version");

    parser.addArgument("fileNameArr").dest("fileNameArr").nargs("*")
        .help("Ingest_LDD files to process.");

    Namespace namespace = null;

    try {
      namespace = parser.parseArgs(args);
    } catch (HelpScreenException e) {
      // the library prints the help message internally so no need for parser.printHelp()
      throw e;
    } catch (ArgumentParserException e) {
      System.out.println(">>  ERROR Invalid argument list");
      lMessageFatalErrorCount++;
      parser.printHelp();
      throw e;
    }
    return namespace;
  }

  // process -p, -l, and -V to determine IM Version to use
  static void processArgumentParserNamespacePhase1(Namespace ns) {

    // handle processing flags
    Boolean pFlag = ns.getBoolean("p");
    if (pFlag) {
      dmProcessState.setPDSOptionalFlag();
      PDSOptionalFlag = true;
    }

    Boolean lddFlag = ns.getBoolean("l");
    if (lddFlag) {
      dmProcessState.setLDDToolFlag();
      LDDToolFlag = true;
    }

    // set up the alternate versions if any
    String altVersion = ns.getString("V");
    if (altVersion.compareTo(buildIMVersionFolderId) != 0) { // it is not the current version of the
                                                             // IM
      if (alternateIMVersionArr.contains(altVersion)) { // is it an allowed prior version; was
                                                        // validated in argument parser
        runIMVersion = altVersion;
        dmProcessState.setalternateIMVersionFlag();
      }
    }
    return;
  }

  // process remaining arguments, for -v (version) return false to exit application 
  static boolean processArgumentParserNamespacePhase2(Namespace ns) {

    // handle the request for version
    Boolean vFlag = ns.getBoolean("v");
    if (vFlag) {
      dmProcessState.setversionFlag();
      System.out.println(" ");
      System.out.println("LDDTool Version: " + LDDToolVersionId);
      System.out.println("Built with IM Version: " + buildIMVersionId);
      System.out.println("Build Date: " + buildDate);
      System.out.println("Configured IM Versions: " + alternateIMVersionArr);
      System.out.println(" ");
      return false;
    }

    // validate the input arguments
    if (!PDSOptionalFlag) {
      String msg = "The -p option must be used for PDS4 processing";
      Utility.registerMessage("3>error " + msg);
      parser.printHelp();
      System.out.println(">>  INFO  Exit(1)");
      throw new IllegalArgumentException(msg);
    }

    Boolean MFlag = ns.getBoolean("M");
    if (MFlag) {
      dmProcessState.setLDDToolMissionFlag();
      LDDToolMissionFlag = true;
      Utility.registerMessage("1>warning "
          + "The -M flag has been deprecated as of PDS4 IM Version 1.14.0.0. See the LDDTool User's Manual for more information on how to provide this information.");
    }
    Boolean mFlag = ns.getBoolean("m");
    if (mFlag) {
      dmProcessState.setPDS4MergeFlag();
      PDS4MergeFlag = true;
    }
    Boolean nFlag = ns.getBoolean("n");
    if (nFlag) {
      dmProcessState.setLDDNuanceFlag();
      LDDNuanceFlag = true;
    }
    Boolean NFlag = ns.getBoolean("N");
    if (NFlag) {
      dmProcessState.setprintNamespaceFlag();
      printNamespaceFlag = true;
    }

    Boolean dFlag = ns.getBoolean("d");
    if (dFlag) {
      dmProcessState.setdisciplineMissionFlag();
      disciplineMissionFlag = true;
    }
    Boolean DFlag = ns.getBoolean("D");
    if (DFlag) {
      dmProcessState.setexportDDFileFlag();
      exportDDFileFlag = true;
    }
    Boolean JFlag = ns.getBoolean("J");
    if (JFlag) {
      dmProcessState.setexportJSONFileFlag();
      exportJSONFileFlag = true;
    }
    Boolean AFlag = ns.getBoolean("a");
    if (AFlag) {
      dmProcessState.setIncludeAllNamespacesFlag();
      includeAllNamespacesFlag = true;
    }
    Boolean n1Flag = ns.getBoolean("1");
    if (n1Flag) {
      dmProcessState.setexportSpecFileFlag();
      exportSpecFileFlag = true;
    }
    Boolean TFlag = ns.getBoolean("T");
    if (TFlag) {
    	dmProcessState.setExportTermMapFileFlag ();
    	exportTermMapFileFlag = true;
    }
    Boolean OFlag = ns.getBoolean("O");
    if (OFlag) {
    	dmProcessState.setExportOWLRDFTTLFileFlag ();
    	exportOWLRDFTTLFileFlag = true;
   	}
    Boolean oFlag = ns.getBoolean("o");
    if (oFlag) {
    	dmProcessState.setExportOWLRDFFileFlag ();
    	exportOWLRDFFileFlag = true;
   	}
    Boolean CFlag = ns.getBoolean("C");
    if (CFlag) {
    	dmProcessState.setExportCustomFileFlag();
    	exportCustomFileFlag = true;
    }
    
    // init masterLDDSchemaFileDefn with dummy LDD; PDS4_Common_IngestLDD.xml does not exist
    masterLDDSchemaFileDefn = new SchemaFileDefn("PDS4_Common_IngestLDD.xml");
    masterLDDSchemaFileDefn.sourceFileName = "PDS4_Common_IngestLDD.xml";
    masterLDDSchemaFileDefn.isActive = false;
    masterLDDSchemaFileDefn.isLDD = false;
    masterLDDSchemaFileDefn.labelVersionId = versionIdDefault;
    masterLDDSchemaFileDefn.versionId = infoModelVersionId;

    // get the LDDIngest file names
    for (String fileName : ns.<String>getList("fileNameArr")) {
      SchemaFileDefn lLDDSchemaFileDefn = new SchemaFileDefn(fileName);
      lLDDSchemaFileDefn.sourceFileName = fileName;
      lLDDSchemaFileDefn.isActive = true;
      lLDDSchemaFileDefn.isLDD = true;
      lLDDSchemaFileDefn.labelVersionId = schemaLabelVersionId;
      LDDSchemaFileSortArr.add(lLDDSchemaFileDefn);
      masterLDDSchemaFileDefn = lLDDSchemaFileDefn; // the last Ingest_LDD named is the master.
    }
    return true;
  }

  static void printErrorMessages() {
    String lPreviousGroupTitle = "";

    // first sort error messages
    TreeMap<String, DOMMsgDefn> lMainMsgMap = new TreeMap<>();
    for (Iterator<DOMMsgDefn> i = mainMsgArr.iterator(); i.hasNext();) {
      DOMMsgDefn lMainMsg = i.next();

      // eliminate certain messages
      if (nameSpaceIdExtrnFlagArr.contains(lMainMsg.getNameSpaceIdNCLC())) {
        continue;
      }

      // if debugFlag is false, skip debug messages
      // 0>info, 0>warning, 0>error
      if (!debugFlag) {
        if ((lMainMsg.getMsgTypeLevel().substring(0, 2)).compareTo("0>") == 0) {
          continue;
        }
      }
      String lMapID = lMainMsg.getMsgTypeLevel() + "." + lMainMsg.getMsgOrder().toString();
      lMainMsgMap.put(lMapID, lMainMsg);
    }

    // using message sorted array, print each message and count message types
    ArrayList<DOMMsgDefn> lMainMsgArr = new ArrayList<>(lMainMsgMap.values());
    for (Iterator<DOMMsgDefn> i = lMainMsgArr.iterator(); i.hasNext();) {
      DOMMsgDefn lMainMsg = i.next();
      Integer lMessageCount = messageLevelCountMap.get(lMainMsg.getMsgTypeLevel());
      if (lMessageCount != null) {
        lMessageCount++;
        messageLevelCountMap.put(lMainMsg.getMsgTypeLevel(), lMessageCount);
      }

      if (lPreviousGroupTitle.compareTo(lMainMsg.getMsgGroupTitle()) != 0) {
        lPreviousGroupTitle = lMainMsg.getMsgGroupTitle();
        System.out.println("");
      }
      System.out.println(lMainMsg.getMsgPrefix() + " " + lMainMsg.getMsgCleanText());
    }

    // print out the message counts
    System.out.println("");
    String lToolName = "IMTool";
    if (LDDToolFlag) {
      lToolName = "LDDTool";
    }
    System.out.println(" -- " + lToolName + " Execution Summary --");

    lMessageWarningCount = messageLevelCountMap.get("1>warning");
    lMessageWarningCount += messageLevelCountMap.get("2>warning");
    lMessageErrorCount = messageLevelCountMap.get("1>error");
    lMessageErrorCount += messageLevelCountMap.get("2>error");
    lMessageFatalErrorCount = messageLevelCountMap.get("3>error");
    lMessageFatalErrorCount += messageLevelCountMap.get("4>error");

    System.out.println("     " + lMessageWarningCount + " warning(s)");
    System.out.println("     " + lMessageErrorCount + " error(s)");
    System.out.println("     " + lMessageFatalErrorCount + " fatal error(s)");

    printFlags();
    printInputFileNames();
    printOutputFileNames();
  }

  static private void printFlags() {
    // print set flags in flag array
    System.out.println("");
    System.out.println("Input:");
    System.out.println("");
    System.out.println("     - IM Version: " + runIMVersion);

    for (String processFlagName : dmProcessState.getSortedProcessFlagNameArr()) {
      System.out.println("     - " + processFlagName + ": true");
    }
  }

  static private void printInputFileNames() {
    // print set flags in flag array
    System.out.println("");
    System.out.print("     - Ingest LDD(s): ");

    if (LDDToolFlag) {
      String del = "";
      for (SchemaFileDefn lLDDSchemaFile : LDDSchemaFileSortArr) {
        System.out.print(del + lLDDSchemaFile.LDDToolInputFileName);
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

  public static String getOutputDirPath() {
    return outputDirPath;
  }
  
  public static SchemaFileDefn getMasterPDSSchemaFileDefn() {
    return masterPDSSchemaFileDefn;
  }
  
  public static String getMasterNameSpaceIdNCLC() {
    return masterNameSpaceIdNCLC;
  }
  
  // masterNameSpaceIdNCLC

  public static void setOutputDirPath(String outputDirPath) {
    DMDocument.outputDirPath = outputDirPath;
  }
}
