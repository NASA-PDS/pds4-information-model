package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class SchemaFileDefn {
	// identifier is the namespace id, without colon, and in caps; it must be unique within the PDS
	String identifier;
	String lddName;

	// each namespace has a version identifier - model version id
	String versionId;
	
	// each namespace has a label version identifier - xml product version id
	String labelVersionId;

	// steward_id is the primary steward identifier; the steward assigns the namespaceid 
	String stewardId;

	// nameSpaceId is the name Space Id; it must be unique within the PDS; NC stands for No Colon
	String nameSpaceIdNC;		// no colon, original
	String nameSpaceIdNCLC;		// no colon, lower cased
	String nameSpaceIdNCUC;		// no colon, upper cased
	String nameSpaceId;			// colon, lower cased
	
	// governance level
	String governanceLevel;

	// isMaster indicates the master schema
	boolean isMaster;
	
	// isLDD indicates an LDD schema
	boolean isLDD;
	
	// isDiscipline indicates a discipline schema
	boolean isDiscipline;
	
	// isMission indicates a mission schema
	boolean isMission;

	// relative File Specification Names
	String relativeFileSpecModelSpec;
	String relativeFileSpecModelSpec_DOM;
	String relativeFileSpecXMLSchema;		// base dir path, xml schema dir, base file name, file extension
	String relativeFileSpecSchematron;		// base dir path, xml schema dir, base file name, file extension	
	String relativeFileNameXMLSchema;
	String relativeFileNameSchematron;
	String relativeFileSpecXMLLabel;		// base dir path, xml schema dir, base file name, file extension
	String relativeFileSpecDDDocXML;		
	String relativeFileSpecDDCSV;			
	String relativeFileSpecCCSDSCSV;			
	String relativeFileSpecDDProtPins;		
	String relativeFileSpecDDProtPinsSN;	// short name - used for DD diff comparisons
	String relativeFileSpecModelRulePins;		
	String relativeFileSpecModelJSON;	
	String relativeFileSpecDOMModelJSON;	
	String relativeFileSpecModelRDF;
	String relativeFileSpecOWLRDF;			
	String relativeFileSpecSKOSTTL;			
	String relativeFileSpecReportTXT;		
	String relativeFileSpecUMLXMI;
	String relativeFileSpecUMLXMI2;
	String relativeFileSpecModelPVL;
	String relativeFileSpecModelRIM1;
	String relativeFileSpecModelRIM3;
	String relativeFileSpecModelRIM4;
	String relativeFileSpecAttrDefn;
	String relativeFileSpecClassDefn;
	String relativeFileSpecLDDPontMerge;
	
	// LDD File Names
	String sourceFileName;		// complete file name as provided on command line
	String LDDToolInputFileName;			
//	String LDDToolInputFileNameNE;					
	String LDDToolOutputFileNameNE;					
	
	// comment is from the Ingest_LDD template; one is allowed per namespace.
	String comment;
	
	// stewarArr is a list of all stewards that share authority over this namespaceid
	ArrayList <String> stewardArr; 
	
	// various version identifiers
	String ont_version_id;						// 0.1.0.0.a
	String lab_version_id;						// 0100a
	String sch_version_id;						// 1.0.0
	String ns_version_id;						// 01
	String identifier_version_id;				// 0.1
	
	public SchemaFileDefn (String id) {
		identifier = id;
		versionId = "0.0.0.0.n";
		lddName = "TBD_lddName";
		labelVersionId = "0.0";
		stewardId = "TBD_stewardId";
		nameSpaceIdNC = id;
		nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
		nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
		nameSpaceId = nameSpaceIdNCLC + ":";
		governanceLevel = DMDocument.governanceLevel;
		isMaster = false;
		isLDD = false;
		isDiscipline = false;
		isMission = false;
		relativeFileSpecModelSpec = "TBD_relativeFileSpecModelSpec";
		relativeFileSpecXMLSchema = "TBD_relativeFileSpecXMLSchema";  // set after setting version_id; see below
		relativeFileSpecSchematron = "TBD_relativeFileSpecSchematron";
		relativeFileNameXMLSchema = "TBD_relativeFileNameXMLSchema";
		relativeFileNameSchematron = "TBD_relativeFileNameSchematron";
		relativeFileSpecXMLLabel = "TBD_relativeFileSpecXMLLabel";
		relativeFileSpecDDDocXML = "TBD_relativeFileSpecDDDocXML";	
		relativeFileSpecDDCSV = "TBD_relativeFileSpecDDCSV";
		relativeFileSpecCCSDSCSV = "TBD_relativeFileSpecCCSDSCSV";
		relativeFileSpecDDProtPins = "relativeFileSpecDDProtPins";	
		relativeFileSpecDDProtPinsSN = "relativeFileSpecDDProtPinsSN";	
		relativeFileSpecModelRulePins = "TBD_relativeFileSpecModelRulePins";	
		relativeFileSpecModelJSON = "TBD_relativeFileSpecModelJSON";	
		relativeFileSpecDOMModelJSON = "TBD_relativeFileSpecDOMModelJSON";	
		relativeFileSpecModelRDF = "TBD_relativeFileSpecModelRDF";
		relativeFileSpecOWLRDF = "TBD_relativeFileSpecOWLRDF";		
		relativeFileSpecSKOSTTL = "TBD_relativeFileSpecSKOSTTL";		
		relativeFileSpecReportTXT = "TBD_relativeFileSpecReportTXT";
		relativeFileSpecUMLXMI = "TBD_relativeFileSpecUMLXMI";
		relativeFileSpecUMLXMI2 = "TBD_relativeFileSpecUMLXMI2";
		relativeFileSpecModelPVL = "TBD_relativeFileSpecModelPVL";
		relativeFileSpecModelRIM1 = "TBD_relativeFileSpecModelRIM1";
		relativeFileSpecModelRIM3 = "TBD_relativeFileSpecModelRIM3";
		relativeFileSpecModelRIM4 = "TBD_relativeFileSpecModelRIM4";
		relativeFileSpecAttrDefn = "TBD_relativeFileSpecAttrDefn";
		relativeFileSpecClassDefn = "TBD_relativeFileSpecClassDefn";
		relativeFileSpecLDDPontMerge = "TBD_relativeFileSpecLDDPontMerge";

		sourceFileName = "TBD_sourceFileName";
		LDDToolInputFileName = "TBD_LDDToolInputFileName";			
//		LDDToolInputFileNameNE = "TBD_LDDToolInputFileNameNE";					
		LDDToolOutputFileNameNE = "TBD_LDDToolOutputFileNameNE";		
		comment = "This XML schema file has been generated from the PDS4 Information Model.";		
		stewardArr = new ArrayList <String>();
	} 
	
	public void setStewardIds (String lSteward) {
		stewardId = lSteward;
		stewardArr.add(lSteward);
		return;
	}	
	
	public void setNameSpaceIds (String lNameSpaceIdNC) {
		nameSpaceIdNC = lNameSpaceIdNC;
		nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
		nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
		nameSpaceId = nameSpaceIdNCLC + ":";
		return;
	}		
	
	//	set the various version identifiers
	public void setVersionIds () {
		
		// new code; not tested
/*		int lLength = versionId.length();
		if (lLength > 7) lLength = 7;
		String lVersionString = "1.0.0.0";
		String lVersionSuffix = lVersionString.substring(lLength,7);	*/
		
		if (versionId.length() <= 0) versionId  = "1.0.0.0";
		if (versionId.length() <= 1) versionId +=  ".0.0.0";
		if (versionId.length() <= 2) versionId +=   "0.0.0";
		if (versionId.length() <= 3) versionId +=    ".0.0";
		if (versionId.length() <= 4) versionId +=     "0.0";
		if (versionId.length() <= 5) versionId +=      ".0";
		if (versionId.length() <= 6) versionId +=       "0";
		String lVersionId = versionId; 						// 1.0.0.0[b]
		ont_version_id = lVersionId; 						// 1.0.0.0[b]
		sch_version_id = lVersionId.substring(0,7);			// 1.0.0.0
		identifier_version_id = lVersionId.substring(0,3);	// 1.0
		lVersionId = DMDocument.replaceString(lVersionId, ".", "");		// 1000[b]
		if (lVersionId.length() > 4) {
			lab_version_id = lVersionId.substring(0,5);		// 1000B from Beta
		} else {
			lab_version_id = lVersionId;					// 1000[b]	
		}
		ns_version_id = lVersionId.substring(0,1);	// 1

//		System.out.println("\ndebug SchemaFileDefn nameSpaceId:" + nameSpaceId);
//		System.out.println("debug SchemaFileDefn versionId:" + versionId);
//		System.out.println("debug SchemaFileDefn labelVersionId:" + labelVersionId);
//		System.out.println("debug SchemaFileDefn lab_version_id:" + lab_version_id);
		
		// set the relative file spec now that we have a version id
		relativeFileSpecModelSpec = DMDocument.outputDirPath + "index" + "_" + lab_version_id + ".html";
		relativeFileSpecModelSpec_DOM = DMDocument.outputDirPath + "index" + "_" + lab_version_id +"_DOM"+  ".html";
		if (! isLDD) {
			relativeFileSpecXMLSchema = DMDocument.outputDirPath + "SchemaXML4/" + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileSpecSchematron = DMDocument.outputDirPath + "SchemaXML4/" + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileNameXMLSchema = "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileNameSchematron = "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileSpecXMLLabel = DMDocument.outputDirPath + "SchemaXML4/" + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xml";
			relativeFileSpecModelJSON = DMDocument.outputDirPath + "export/JSON/" + "PDS4_" + nameSpaceIdNCUC + "_" + "JSON" + "_" + lab_version_id + ".JSON";	
			relativeFileSpecDOMModelJSON = DMDocument.outputDirPath + "export/JSON/" + "PDS4_" + nameSpaceIdNCUC + "_" + "JSON" + "_" + lab_version_id + "_DOM" + ".JSON";	
			relativeFileSpecDDCSV = DMDocument.outputDirPath + "export/csv/" + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id;			
			relativeFileSpecCCSDSCSV = DMDocument.outputDirPath + "export/csv/" + "PDS4_" + nameSpaceIdNCUC + "_CCSDS"  + "_" + lab_version_id;			
		} else if (! DMDocument.LDDSyncFileNameFlag) {
			relativeFileSpecXMLSchema = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" +  nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileSpecSchematron = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" +  nameSpaceIdNCUC + "_" + lab_version_id + ".sch";			
			relativeFileNameXMLSchema = LDDToolOutputFileNameNE + "_" +  nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileNameSchematron = LDDToolOutputFileNameNE + "_" +  nameSpaceIdNCUC + "_" + lab_version_id + ".sch";			
			relativeFileSpecXMLLabel = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xml";
			relativeFileSpecModelJSON = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".JSON";
			relativeFileSpecDOMModelJSON = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id + "_DOM" + ".JSON";
			relativeFileSpecLDDPontMerge = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".pont";
			relativeFileSpecReportTXT = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".txt";	
			relativeFileSpecDDCSV = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_" + lab_version_id;			
			relativeFileSpecCCSDSCSV = DMDocument.outputDirPath + LDDToolOutputFileNameNE + "_" + nameSpaceIdNCUC + "_CCSDS"  + "_" + lab_version_id;			
		} else {
			versionId = DMDocument.masterPDSSchemaFileDefn.versionId;
			labelVersionId = DMDocument.masterPDSSchemaFileDefn.labelVersionId;
			ont_version_id = DMDocument.masterPDSSchemaFileDefn.ont_version_id;
			lab_version_id = DMDocument.masterPDSSchemaFileDefn.lab_version_id;
			sch_version_id = DMDocument.masterPDSSchemaFileDefn.sch_version_id;
			ns_version_id = DMDocument.masterPDSSchemaFileDefn.ns_version_id;	
			
//			System.out.println("debug SchemaFileDefn new versionId:" + versionId);
//			System.out.println("debug SchemaFileDefn new labelVersionId:" + labelVersionId);
//			System.out.println("debug SchemaFileDefn new lab_version_id:" + lab_version_id);

			relativeFileSpecXMLSchema = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileSpecSchematron = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileNameXMLSchema = "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileNameSchematron = "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileSpecXMLLabel = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xml";
			relativeFileSpecModelJSON = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".JSON";	
			relativeFileSpecLDDPontMerge = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".pont";
			relativeFileSpecReportTXT = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id + ".txt";	
			relativeFileSpecDDCSV = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + lab_version_id;			
			relativeFileSpecCCSDSCSV = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_CCSDS"  + "_" + lab_version_id;			
		}
		relativeFileSpecDDDocXML = DMDocument.outputDirPath + "export/DD/" + "PDS4_" + nameSpaceIdNCUC + "_" + "DD" + "_" + lab_version_id + ".xml";	
		relativeFileSpecDDProtPins = DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen_" + DMDocument.masterTodaysDateyymmdd + ".pins";	
		relativeFileSpecDDProtPinsSN = DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen" + ".pins";	
		relativeFileSpecModelRulePins = DMDocument.outputDirPath + "PDS4_" + nameSpaceIdNCUC + "_" + "MODEL_RULES" + "_" + lab_version_id + ".txt";	
		relativeFileSpecModelRDF = DMDocument.outputDirPath + "export/rdf/" + "PDS4_" + nameSpaceIdNCUC + "_" + "MODEL" + "_" + lab_version_id + ".rdf";		
		relativeFileSpecOWLRDF = DMDocument.outputDirPath + "export/owl/" + "PDS4_" + nameSpaceIdNCUC + "_" + "OWL" + "_" + lab_version_id + ".rdf";		
		relativeFileSpecSKOSTTL = DMDocument.outputDirPath + "export/skos/" + "PDS4_" + nameSpaceIdNCUC + "_" + "SKOS" + "_" + lab_version_id + ".ttl";		
		relativeFileSpecUMLXMI = DMDocument.outputDirPath + "export/xmi/" + "PDS4_" + nameSpaceIdNCUC + "_" + "XMI" + "_clean" + "_" + lab_version_id + ".xmi";	
		relativeFileSpecUMLXMI2 = DMDocument.outputDirPath + "export/xmi/" + "PDS4_" + nameSpaceIdNCUC + "_" + "XMI" + "_wNames" + "_" + lab_version_id + ".xmi";	
		relativeFileSpecModelPVL = DMDocument.outputDirPath + "export/pvl/" + "PDS4_" + nameSpaceIdNCUC + "_" + "PVL" + "_" + lab_version_id + "_";	
		relativeFileSpecModelRIM1 = DMDocument.outputDirPath + "export/rim/" + "PDS4_" + nameSpaceIdNCUC + "_" + "RIM1" + "_" + lab_version_id + ".txt";	
		relativeFileSpecModelRIM3 = DMDocument.outputDirPath + "export/rim/" + "PDS4_" + nameSpaceIdNCUC + "_" + "RIM3" + "_" + lab_version_id + ".txt";	
		relativeFileSpecModelRIM4 = DMDocument.outputDirPath + "export/rim/" + "PDS4_" + nameSpaceIdNCUC + "_" + "RIM4" + "_" + lab_version_id + ".txt";	
		relativeFileSpecAttrDefn = DMDocument.outputDirPath + "export/defnAttr/";	
		relativeFileSpecClassDefn = DMDocument.outputDirPath + "export/defnClass/";	

		return;
	}
}
