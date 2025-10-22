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

import java.util.ArrayList;
import java.util.Arrays;
import gov.nasa.pds.model.plugin.util.Utility;

public class SchemaFileDefn {
  // identifier is the namespace id, without colon, and in caps; it must be unique within the PDS
  String identifier;
  String lddName;
  boolean isActive; // isActive=true indicates that the namespace is found in at least one class,
                    // attribute, see GetDOMModel #040

  // each namespace has a version identifier - model version id
  String versionId;

  // various version identifiers
  String ont_version_id; // 0.1.0.0
  String lab_version_id; // 0100
  String sch_version_id; // 1.0.0
  String ns_version_id; // 01
  String identifier_version_id; // 0.1
  String labelVersionId; // each namespace has a label version identifier - xml product version id

  // steward_id is the primary steward identifier; the steward assigns the namespaceid
  String stewardId;

  // nameSpaceId is the name Space Id; it must be unique within the PDS; NC stands for No Colon
  String nameSpaceIdNC; // no colon, original
  String nameSpaceIdNCLC; // no colon, lower cased
  String nameSpaceIdNCUC; // no colon, upper cased
  String nameSpaceId; // colon, lower cased
  String nameSpaceIdNCDir; // colon, lower cased, underscores replace by slashes, urn:esa:psa:

  // nameSpace URL
  String nameSpaceURL;
  String nameSpaceURLs;
  String urnPrefix;
  String modelShortName;
  String sysBundleName;
  String regAuthId;

  // governance level
  String governanceLevel;

  // isMaster indicates the master schema
  boolean isMaster;

  // isLDD indicates an LDD schema
  boolean isLDD;
  boolean isMasterLDD; 

  // isDiscipline indicates a discipline schema
  boolean isDiscipline;

  // isMission indicates a mission schema
  boolean isMission;

  // relative File Specification Names
  String relativeFileSpecModelSpec_DOM;
  String relativeFileSpecXMLSchema; // base dir path, xml schema dir, base file name, file extension
  String relativeFileSpecSchematron; // base dir path, xml schema dir, base file name, file
                                     // extension
  String relativeFileNameXMLSchema;
  String relativeFileNameXMLSchema2;
  String relativeFileNameSchematron;
  String relativeFileSpecXMLLabel; // base dir path, xml schema dir, base file name, file extension
  String relativeFileSpecDDDocXML;
  String relativeFileSpecDDCSV;
  String relativeFileSpecCCSDSCSV;
  String relativeFileSpecDDProtPins;
  String relativeFileSpecDDProtPinsSN; // short name - used for DD diff comparisons
  String relativeFileSpecDOMModelJSON;
  String relativeFileSpecModelRDF;
  String relativeFileSpecOWLRDF_DOM;
  String relativeFileSpecSKOSTTL_DOM;
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
  String sourceFileName; // complete file name as provided on command line
  String LDDToolInputFileName;
  // String LDDToolInputFileNameNE;
  String LDDToolOutputFileNameNE;

  // comment is from the Ingest_LDD template; one is allowed per namespace.
  String comment;

  // stewarArr is a list of all stewards that share authority over this namespaceid
  ArrayList<String> stewardArr;

  public SchemaFileDefn(String id) {
    identifier = id;
    versionId = "0.0.0.0.n";
    isActive = false;
    lddName = "TBD_lddName";
    labelVersionId = "0.0";
    stewardId = "TBD_stewardId";
    nameSpaceIdNC = id;
    nameSpaceIdNCDir = nameSpaceIdNC;
    nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
    nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
    nameSpaceId = nameSpaceIdNCLC + ":";
    nameSpaceURL = "TBD_nameSpaceURL";
    nameSpaceURLs = "TBD_nameSpaceURLs";
    urnPrefix = "TBD_urnPrefix";
    modelShortName = "TBD_modelShortName";
    sysBundleName = "system_bundle"; // default system bundle name, can be overridden in the
                                     // config.properties file.
    regAuthId = "TBD_regAuthId";

    governanceLevel = "TBD_governanceLevel";
    isMaster = false;
    isLDD = false;
    isMasterLDD = false;
    isDiscipline = false;
    isMission = false;
    relativeFileSpecModelSpec_DOM = "TBD_relativeFileSpecModelSpec_DOM";
    relativeFileSpecXMLSchema = "TBD_relativeFileSpecXMLSchema"; // set after setting version_id;
                                                                 // see below
    relativeFileSpecSchematron = "TBD_relativeFileSpecSchematron";
    relativeFileNameXMLSchema = "TBD_relativeFileNameXMLSchema";
    relativeFileNameXMLSchema2 = "TBD_relativeFileNameXMLSchema2";
    relativeFileNameSchematron = "TBD_relativeFileNameSchematron";
    relativeFileSpecXMLLabel = "TBD_relativeFileSpecXMLLabel";
    relativeFileSpecDDDocXML = "TBD_relativeFileSpecDDDocXML";
    relativeFileSpecDDCSV = "TBD_relativeFileSpecDDCSV";
    relativeFileSpecCCSDSCSV = "TBD_relativeFileSpecCCSDSCSV";
    relativeFileSpecDDProtPins = "relativeFileSpecDDProtPins";
    relativeFileSpecDDProtPinsSN = "relativeFileSpecDDProtPinsSN";
    relativeFileSpecDOMModelJSON = "TBD_relativeFileSpecDOMModelJSON";
    relativeFileSpecModelRDF = "TBD_relativeFileSpecModelRDF";
    relativeFileSpecOWLRDF_DOM = "TBD_relativeFileSpecOWLRDF_DOM";
    relativeFileSpecSKOSTTL_DOM = "TBD_relativeFileSpecSKOSTTL_DOM";
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
    // LDDToolInputFileNameNE = "TBD_LDDToolInputFileNameNE";
    LDDToolOutputFileNameNE = "TBD_LDDToolOutputFileNameNE";
    comment = "This XML schema file has been generated from the Information Model.";
    stewardArr = new ArrayList<>();
  }

  // getters
  
  //get ont_version_id
  public String getOntologyVersionId() {
	  return ont_version_id;
  }

  //get lab_version_id
  public String getLabVersionId() {
	  return lab_version_id;
  }

  //get sch_version_id
  public String getSchemaVersionId() {
	  return sch_version_id;
  }

  //get ns_version_id
  public String getNameSpaceVersionId() {
	  return ns_version_id;
  }
  
  //set ns_version_id
  public void setNameSpaceVersionId(String nsVersionId) {
	  this.ns_version_id = nsVersionId;
  }

  //get identifier_version_id
  public String getIdentiferVersionId() {
	  return identifier_version_id;
  }

  //get labelVersionId
  public String getLabelVersionId() {
	  return labelVersionId;
  }
 
  public void setStewardIds(String lSteward) {
    stewardId = lSteward;
    stewardArr.add(lSteward);
    return;
  }

  public void setNameSpaceIds(String lNameSpaceIdNC) {
    nameSpaceIdNC = lNameSpaceIdNC;
    nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
    nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
    nameSpaceId = nameSpaceIdNCLC + ":";
    nameSpaceIdNCDir = nameSpaceIdNC;
    return;
  }

  public void setRegAuthority(SchemaFileDefn lSchemaFileDefn) {
    nameSpaceURL = lSchemaFileDefn.nameSpaceURL;
    nameSpaceURLs = lSchemaFileDefn.nameSpaceURLs;
    urnPrefix = lSchemaFileDefn.urnPrefix;
    modelShortName = lSchemaFileDefn.modelShortName;
    sysBundleName = lSchemaFileDefn.sysBundleName;
    regAuthId = lSchemaFileDefn.regAuthId;
    if (urnPrefix.compareTo("urn:esa:psa:") == 0) {
      nameSpaceIdNCDir = nameSpaceIdNCDir.replace('_', '/');
    }
    return;
  }

  public void setDictionaryType(String lDictionaryType) {
    if (lDictionaryType.compareTo("Common") == 0) {
      governanceLevel = "Common";
      isMaster = true;
      isLDD = false;
      isDiscipline = false;
      isMission = false;
    } else if (lDictionaryType.compareTo("Discipline") == 0) {
      governanceLevel = "Discipline";
      isMaster = false;
      isLDD = true;
      isDiscipline = true;
      isMission = false;
    } else if (lDictionaryType.compareTo("Mission") == 0) {
      governanceLevel = "Mission";
      isMaster = false;
      isLDD = true;
      isDiscipline = false;
      isMission = true;
    }
    return;
  }

  // set the various version identifiers
  public void setVersionIds() {
    // cleanup version id; ont_version_id, lab_version_id, identifier_version_id
    getCleanVersionId(versionId);
//    sch_version_id = ont_version_id; // 1.0.0.0
//    ns_version_id = lab_version_id.substring(0, 1); // 1

    String lFileNameIMVersionId = getCleanLabelVersionId(DMDocument.infoModelVersionId);

    // set the relative file spec now that we have a version id
    relativeFileSpecModelSpec_DOM =
        DMDocument.getOutputDirPath() + "index" + "_" + lab_version_id + ".html";
    if (!isLDD) {
      relativeFileSpecXMLSchema = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
          + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
      relativeFileSpecSchematron =
          DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
          + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
      relativeFileNameXMLSchema =
          DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
      relativeFileNameXMLSchema2 = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
          + lFileNameIMVersionId + "_" + lab_version_id + ".xsd";
      relativeFileNameSchematron =
          DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
      relativeFileSpecXMLLabel = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
          + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xml";
      relativeFileSpecDOMModelJSON = DMDocument.getOutputDirPath() + "export/"
          + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".JSON";
      relativeFileSpecDDCSV = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
          + "_" + nameSpaceIdNCUC + "_" + lab_version_id;
      relativeFileSpecCCSDSCSV = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
          + "_" + nameSpaceIdNCUC + "_CCSDS" + "_" + lab_version_id;
    } else {
      relativeFileSpecXMLSchema =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
      relativeFileSpecSchematron =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".sch";
      relativeFileNameXMLSchema = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
          + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
      relativeFileNameXMLSchema2 = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
          + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
      relativeFileNameSchematron = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
          + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".sch";
      relativeFileSpecXMLLabel =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xml";
      relativeFileSpecDOMModelJSON =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".JSON";
      relativeFileSpecLDDPontMerge =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".pont";
      relativeFileSpecReportTXT =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".txt";
      relativeFileSpecDDCSV =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id;
      relativeFileSpecCCSDSCSV =
          DMDocument.getOutputDirPath() + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_"
              + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_CCSDS" + "_" + lab_version_id;
    }

    relativeFileSpecDDDocXML = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "DD" + "_" + lab_version_id + ".xml";
    relativeFileSpecDDProtPins = DMDocument.getOutputDirPath() + "export/" + "dd11179_Gen_"
        + DMDocument.masterTodaysDateyymmdd + ".pins";
    relativeFileSpecDDProtPinsSN =
        DMDocument.getOutputDirPath() + "export/" + "dd11179_Gen" + ".pins";
    relativeFileSpecModelRDF = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "RDF" + "_" + lab_version_id + ".rdf";
    relativeFileSpecOWLRDF_DOM = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "OWL" + "_" + lab_version_id + ".rdf";
    relativeFileSpecSKOSTTL_DOM = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "SKOS" + "_" + lab_version_id + ".ttl";
    relativeFileSpecUMLXMI =
        DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId + "_"
        + nameSpaceIdNCUC + "_" + "XMI" + "_clean" + "_" + lab_version_id + ".xmi";
    relativeFileSpecUMLXMI2 = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "XMI" + "_wNames" + "_" + lab_version_id + ".xmi";
    relativeFileSpecModelPVL = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "PVL" + "_" + lab_version_id + "_";
    relativeFileSpecModelRIM1 = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "RIM1" + "_" + lab_version_id + ".txt";
    relativeFileSpecModelRIM3 = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "RIM3" + "_" + lab_version_id + ".txt";
    relativeFileSpecModelRIM4 = DMDocument.getOutputDirPath() + "export/" + DMDocument.mastModelId
        + "_" + nameSpaceIdNCUC + "_" + "RIM4" + "_" + lab_version_id + ".txt";
    relativeFileSpecAttrDefn = DMDocument.getOutputDirPath() + "export/defnAttr/";
    relativeFileSpecClassDefn = DMDocument.getOutputDirPath() + "export/defnClass/";
    Utility.checkCreateDirectory(relativeFileSpecAttrDefn);
    Utility.checkCreateDirectory(relativeFileSpecClassDefn);
    return;
  }
  
  // get clean version id
  void getCleanVersionId(String versionId) { 
	  
	// split the version id on "." 
    ArrayList<String> versionIdPartArr = new ArrayList<>(Arrays.asList(versionId.split("\\.")));

    // ensure that there are at least four parts
    for (int i = versionIdPartArr.size(); i < 4; i++) {
    	versionIdPartArr.add("0");
    }	  
	
    // set the version ids
	ont_version_id = getIMVersionId(versionIdPartArr);
	lab_version_id = getFileNameVersionId(versionIdPartArr);	  
	sch_version_id = ont_version_id; // 1.0.0.0
	ns_version_id = versionIdPartArr.get(0);
    identifier_version_id = versionIdPartArr.get(0) + "." + versionIdPartArr.get(1);
    return;
  }

  // get clean label version id
  String getCleanLabelVersionId(String versionId) {
	  
		// split the version id on "." 
	    ArrayList<String> versionIdPartArr = new ArrayList<>(Arrays.asList(versionId.split("\\.")));

	    // ensure that there are at least four parts
	    for (int i = versionIdPartArr.size(); i < 4; i++) {
	    	versionIdPartArr.add("0");
	    }	  
		
	    // set the version ids
	    return getFileNameVersionId(versionIdPartArr);	  
  }
  
  // validate version id (1.12.0.0)
  private String getIMVersionId(ArrayList<String> versionIdPartArr) {

    // get lab_version_id - 1.0.0.0
    String lab_version_id = "";
    String lDel = "";
    for (String versionIdPart: versionIdPartArr) {
    	lab_version_id += lDel + versionIdPart;
      lDel = ".";
    }
    return lab_version_id;
  }
  
  // get filename version id (1C00)
  private String getFileNameVersionId(ArrayList<String> versionIdPartArr) {

    // get lab_version_id - 1X00
    String lab_version_id = "";
    int versionIdPartInt;
    for (String versionIdPart : versionIdPartArr) {
        try {
            versionIdPartInt = Integer.parseInt(versionIdPart);
            // use 0..9 otherwise 10..35 -> A ..Z
    		if (versionIdPartInt > 9) {
    			int digit = versionIdPartInt - 9;
    			if (digit >= 1 && digit <= 26) {
    				char letter = (char) (digit + 64); // ASCII value of 'A' is 65
    				versionIdPart = String.valueOf(letter);
    			} else {
    	        	versionIdPart = "x";	
    			}
    		} else if (versionIdPartInt < 0) {
	        	versionIdPart = "x";
    		}
        } catch (NumberFormatException e) {
        	versionIdPart = "x";
        }
    	lab_version_id += versionIdPart;
    }
    return lab_version_id;
  }
}
