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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Parses an XML document - Local_DD
 */

public class LDDDOMParser extends Object {
	
  // Schema File Definition
  SchemaFileDefn gSchemaFileDefn;

  // initialize the class structures
  static TreeMap<String, DOMClass> classMap = new TreeMap<>();
  static TreeMap<String, DOMClass> classMapLocal = new TreeMap<>();
  static ArrayList<DOMClass> classArr = new ArrayList<>();

  // initialize the attribute structures
  static ArrayList<DOMAttr> attrArr = new ArrayList<>();
  static TreeMap<String, DOMAttr> attrMap = new TreeMap<>();
  static TreeMap<String, DOMAttr> attrMapLocal = new TreeMap<>();

  // initialize the resolved properties (after LDD Attr or Class has been mapped to a class)
  static ArrayList<DOMProp> attrArrResolved = new ArrayList<>();

  // initialize the Property structures
  static ArrayList<DOMProp> LDDDOMPropArr = new ArrayList<>();

  // initialize the Rule structures
  static ArrayList<DOMRule> ruleArr = new ArrayList<>();
  static TreeMap<String, DOMRule> ruleMap = new TreeMap<>();

  // initialize the Reference
  static ArrayList<RuleReferenceTypeDefn> ruleReferenceArr = new ArrayList<>();

  // initialize the Property Map structures
  static ArrayList<PropertyMapsDefn> propertyMapsArr = new ArrayList<>();
  static TreeMap<String, PropertyMapsDefn> propertyMapsMap = new TreeMap<>();
  
  static final String PDSINTERNALREFERENCE = "pds.Internal_Reference";
  static final String PDSLOCALINTERNALREFERENCE = "pds.Local_Internal_Reference";

  PrintWriter prLocalDD, prProtegePont;

  // info, warning, and error messages
  ArrayList<String> lddErrorMsg = new ArrayList<>();

  // class and descriptor structures
  ArrayList<String> classConceptSuf;
  TreeMap<String, String> classConceptNorm;
  ArrayList<String> validConceptArr;

  // map of local to external .titles for LDD valArr update
  TreeMap<String, String> lLDDValArrExtUpdDefnClassMap = new TreeMap<>();

  // No generics
  Document dom;

  String lLDDName;
  String lLDDVersionId;
  String lFullName;
  String lLastModificationDateTime;
  // String lIdentifier;
  String lDescription;
  String lComment = "TBD_lComment";
  String lRegAuthId = DMDocument.registrationAuthorityIdentifierValue;

  String lCardMin;
  int lCardMinI;
  String lCardMax;
  int lCardMaxI;

  public LDDDOMParser() {

	  classMap = new TreeMap<>();
	  classMapLocal = new TreeMap<>();
	  classArr = new ArrayList<>();

	  attrArr = new ArrayList<>();
	  attrMap = new TreeMap<>();
	  attrMapLocal = new TreeMap<>();

	  attrArrResolved = new ArrayList<>();

	  LDDDOMPropArr = new ArrayList<>();

	  ruleArr = new ArrayList<>();
	  ruleMap = new TreeMap<>();
	  ruleReferenceArr = new ArrayList<>();

	  propertyMapsArr = new ArrayList<>();
	  propertyMapsMap = new TreeMap<>();

	  lddErrorMsg = new ArrayList<>();
	  lLDDValArrExtUpdDefnClassMap = new TreeMap<>();

	  lComment = "TBD_lComment";
	  lRegAuthId = DMDocument.registrationAuthorityIdentifierValue;
	  
    // initialize the concept arrays
    initializeConceptArrs();
  }

  public void getLocalDD() throws java.io.IOException {
    // parse the xml file and get the dom object
    parseXmlFile(gSchemaFileDefn);
    Utility.registerMessage("0>info getLocalDD.parseXmlFile() Done");

    // process the dom document for classes, attributes, etc
    parseDocument(gSchemaFileDefn);
    Utility.registerMessage("0>info getLocalDD.parseDocument() Done");
  }

  public void getLocalDDPhase2() {
    // resolve component references (cross namespace DD_Association references)
    resolveComponentsReferences(gSchemaFileDefn);
    Utility.registerMessage("0>info resolveComponentsReferences() Done");

    // validate parsed header
    validateParsedHeader(gSchemaFileDefn);
    Utility.registerMessage("0>info getLocalDD.validateParsedHeader() Done");
  }

  public void getLocalDDPhase3() {
    // add the LDD artifacts to the master
    addLDDtoMaster();
    Utility.registerMessage("0>info getLocalDD.addLDDtoMaster() Done");

    validateNoNestedExposedClasses();
    Utility.registerMessage("0>info getLocalDD.validateNoNestedExposedClasses() Done");

    Utility.registerMessage("0>info getLocalDD Done");
  }

  private void parseXmlFile(SchemaFileDefn lSchemaFileDefn) {
    // get the factory
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      // Using factory get an instance of document builder
      DocumentBuilder db = dbf.newDocumentBuilder();
      // parse using builder to get DOM representation of the XML file
      dom = db.parse(lSchemaFileDefn.LDDToolInputFileName);
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (SAXException se) {
      se.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void parseDocument(SchemaFileDefn lSchemaFileDefn) {
    // get the root element
    Element docEle = dom.getDocumentElement();
    // get the basics

    // local DD attributes
    lFullName = getTextValue(docEle, "full_name");
    lLastModificationDateTime = getTextValue(docEle, "last_modification_date_time");
    lComment = getTextValue(docEle, "comment");
    if (lComment == null) {
      lComment = "TBD_comment";
    } else // escape any user provided values of "TBD"
    if (lComment.indexOf("TBD") == 0) {
      lComment = "_" + lComment;
    }

    // get namespace_id from Ingest_LDD
    String lNameSpaceIdNC = getTextValue(docEle, "namespace_id");
    if (lNameSpaceIdNC == null) {
      lNameSpaceIdNC = "TBD";
    }

    // find namespace_id in "all" schemaFiles (from config.properties)
    SchemaFileDefn lConfigSchemaFileDefn =
        DMDocument.masterAllSchemaFileSortMap.get(lNameSpaceIdNC);
    if (lConfigSchemaFileDefn == null) {
      // default - set settings to the pds configuration setting if the namespace is not found in
      // config file
      lConfigSchemaFileDefn = DMDocument.masterPDSSchemaFileDefn;
      Utility.registerMessage(
          "2>warning Init: " + " - Config.Properties Namespace Id Not Found:" + lNameSpaceIdNC);
    } else {
      Utility.registerMessage(
          "2>info Init: " + " - Config.Properties Namespace Id Found:" + lNameSpaceIdNC);
    }
    Utility.registerMessage("2>info Init: " + " - Config.Properties Namespace Id Using:"
        + lConfigSchemaFileDefn.identifier);

    // finally set namespace id and add this LDD to LDDSchemaFileSortMap
    lSchemaFileDefn.setNameSpaceIds(lNameSpaceIdNC); // all variations
    DMDocument.LDDSchemaFileSortMap.put(lSchemaFileDefn.nameSpaceIdNCLC, lSchemaFileDefn);
    lSchemaFileDefn.setRegAuthority(lConfigSchemaFileDefn);

    // intialize the namespace for import file creation
    if (!DMDocument.LDDImportNameSpaceIdNCArr.contains(lNameSpaceIdNC)) {
      DMDocument.LDDImportNameSpaceIdNCArr.add(lNameSpaceIdNC);
    }

    // get the dictionary type from Ingest_LDD file
    String ldictionaryType = getTextValue(docEle, "dictionary_type");
    if (ldictionaryType.compareTo("TBD_Ingest_LDD") == 0) {
      if (DMDocument.LDDToolMissionFlag) {
        ldictionaryType = "Mission";
      } else {
        ldictionaryType = "TBD_dictionary_type";
      }
    }

    if (ldictionaryType.compareTo("Common") == 0) {
      lSchemaFileDefn.setDictionaryType("Discipline");
      DMDocument.LDDToolSingletonClassTitle = "Discipline_Area";
      Utility.registerMessage("2>warning Init: " + " - LDD Dictionary_Type Found:"
          + ldictionaryType + "  Defaulting to Discipline");
    } else if (ldictionaryType.compareTo("Discipline") == 0) {
      lSchemaFileDefn.setDictionaryType("Discipline");
      DMDocument.LDDToolSingletonClassTitle = "Discipline_Area";
      Utility.registerMessage("2>info Init: " + " - LDD Dictionary_Type is " + ldictionaryType);
    } else if (ldictionaryType.compareTo("Mission") == 0) {
      lSchemaFileDefn.setDictionaryType("Mission");
      DMDocument.LDDToolSingletonClassTitle = "Mission_Area";
      Utility.registerMessage("2>info Init: " + " - LDD Dictionary_Type is " + ldictionaryType);
    } else {
      lSchemaFileDefn.setDictionaryType("Discipline");
      DMDocument.LDDToolSingletonClassTitle = "Discipline_Area";
      Utility.registerMessage("2>error Init: " + " - LDD Dictionary_Type not Found:"
          + ldictionaryType + "  Defaulting to Discipline");
    }

    if (!(lComment.indexOf("TBD") == 0)) {
      lSchemaFileDefn.comment = lComment;
    }

    lLDDName = getTextValue(docEle, "name");
    if (lLDDName == null || (lLDDName.indexOf("TBD") == 0)) {
      lLDDName = "TBD_LDD_name";
    }
    lSchemaFileDefn.lddName = lLDDName;


    lLDDVersionId = getTextValue(docEle, "ldd_version_id");
    if (!(lLDDVersionId == null || (lLDDVersionId.indexOf("TBD") == 0))) {
      lSchemaFileDefn.versionId = lLDDVersionId;
    }

    lSchemaFileDefn.setVersionIds(); // set variations of versions and all filenames

    String lStewardId = getTextValue(docEle, "steward_id");
    if (!(lStewardId == null || (lStewardId.indexOf("TBD") == 0))) {
      lStewardId = lStewardId.toLowerCase();
      lSchemaFileDefn.setStewardIds(lStewardId);
    } else {
      lSchemaFileDefn.setStewardIds("tbd");
    }

    lDescription = getTextValue(docEle, "comment");
    if (lDescription == null || (lDescription.indexOf("TBD") == 0)) {
      lDescription = "TBD_description";
    }

    // dump the USER attributes
    // InfoModel.dumpAttrDict();

    // get the LDD attributes
    getAttributes(lSchemaFileDefn, docEle);
    Utility.registerMessage("0>info getLocalDD.parseDocument.getAttributes() Done");

    //	get the LDD attributes Extended
	getAttributesExtended (lSchemaFileDefn, docEle);
	Utility.registerMessage ("0>info getLocalDD.parseDocument.getAttributesExtended() Done");
	
    // get the LDD classes
    getClass(lSchemaFileDefn, docEle);
    Utility.registerMessage("0>info getLocalDD.parseDocument.getClass() Done");

    // get the LDD rules
    getRule(lSchemaFileDefn, docEle);
    Utility.registerMessage("0>info getLocalDD.parseDocument.getRule() Done");

    // get the LDD property map
    getPropMap(docEle);
    Utility.registerMessage("0>info getLocalDD.parseDocument.getPropMap() Done");
  }

  private void resolveComponentsReferences(SchemaFileDefn lSchemaFileDefn) {

    // get the component for the LDD association
    resolveComponentsForAssociation(lSchemaFileDefn);
    Utility
        .registerMessage("0>info getLocalDD.parseDocument.resolveComponentsForAssociation() Done");

    // scan DD_Rule to determine if any external namespaces are referenced
    scanRulesForExternalNamespaces(lSchemaFileDefn);
    Utility
        .registerMessage("0>info getLocalDD.parseDocument.scanRulesForExternalNamespaces() Done");

    validateReservedNames(lSchemaFileDefn);
    Utility.registerMessage("0>info getLocalDD.parseDocument.validateReservedNames() Done");

    validateAttributeUsed();
    Utility.registerMessage("0>info getLocalDD.parseDocument.validateAttributeUsed() Done");

    validateNoDuplicateNames();
    Utility.registerMessage("0>info parseDocument.validateNoDuplicateNames() Done");

    validateTypeAttributes(lSchemaFileDefn.isMission);
    Utility.registerMessage("0>info parseDocument.validateTypeAttributes() Done");

    validateNoUnitsAttributes();
    Utility.registerMessage("0>info parseDocument.validateNoUnitsAttributes() Done");

    validateNilRequiredAttributes();
    Utility.registerMessage("0>info parseDocument.validateNilRequiredAttributes() Done");

    validateEnumeratedFlags();
    Utility.registerMessage("0>info parseDocument.validateEnumeratedFlags() Done");
  }

  private void printClassDebug(String lLable, String lIdentifier) {
    DOMClass lClass = classMap.get(lIdentifier);
    if (lClass == null) {
      System.out.println("\ndebug label:" + lLable
          + "  printClassDebug INVALID IDENTIFIER lIdentifier:" + lIdentifier);
      return;
    }
    System.out.println(
        "\ndebug label:" + lLable + "  printClassDebug lClass.identifier:" + lClass.identifier);
    for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        System.out.println("debug printClassDebug lDOMAttr.identifier:" + lDOMAttr.identifier);
      }
    }
  }

	
	// get the list of all DD_Attribute nodes
	private void getAttributes (SchemaFileDefn lSchemaFileDefn, Element docEle) {			
		//get a nodelist of <DD_Attribute> elements and iterate
		NodeList nl = docEle.getElementsByTagName("DD_Attribute");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the element
				Element el = (Element)nl.item(i);
				getAttribute_properties (lSchemaFileDefn, docEle, el, false);
			}
		}
	}
	
	// get the list of all DD_Attribute_External nodes
	private void getAttributesExtended (SchemaFileDefn lSchemaFileDefn, Element docEle) {			
		//get a nodelist of <DD_Attribute> elements and iterate
		NodeList nl = docEle.getElementsByTagName("DD_Attribute_Extended");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the element
				Element el = (Element)nl.item(i);	
				getAttribute_properties (lSchemaFileDefn, docEle, el, true);
			}
		}
	}	

	// get the properties of one Attribute 
	private void getAttribute_properties (SchemaFileDefn lSchemaFileDefn, Element docEle, Element el, Boolean isExtendedAttribute) {			

		// get the attribute's local identifier and name
		String lLocalIdentifier = getTextValue(el,"local_identifier");
		String lLocalIdentifierCleaned = getLocalIdentifierNamespacePrefix(lSchemaFileDefn.nameSpaceId, lLocalIdentifier);
		String lTitle = getTextValue(el,"name");
		
		// create the rdfIdentifier; at this time only the LDD local identifier is known; the class is obtained from the association processing later.
		// get the next unique sequence number; create the rdfIdentifier
		String lSequenceId = DOMInfoModel.getNextUId();
		String lAttrRdfIdentifier = DMDocument.rdfPrefix + lSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + lSequenceId;
		
		DOMAttr lDOMAttr= (DOMAttr) attrMap.get(lAttrRdfIdentifier);
		if (lDOMAttr == null) {
			lDOMAttr = new DOMAttr ();
			lDOMAttr.rdfIdentifier = lAttrRdfIdentifier;
			lDOMAttr.sequenceId = lSequenceId;
			attrArr.add(lDOMAttr);
			attrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
			lDOMAttr.lddLocalIdentifier = lLocalIdentifierCleaned;
			attrMapLocal.put(lDOMAttr.lddLocalIdentifier, lDOMAttr);
			lDOMAttr.isPDS4 = true;
			lDOMAttr.isFromLDD = true;
			lDOMAttr.isExtendedAttribute = isExtendedAttribute;
			lDOMAttr.versionIdentifierValue = getTextValue(docEle,"version_id");				
			lDOMAttr.title = lTitle;

			// at this point lAttr.className is defaulted to the USER class, however it is updated later in association processing if it is owned by a class.
			lDOMAttr.setIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lSchemaFileDefn.nameSpaceIdNC, lDOMAttr.title);
			
			lDOMAttr.XMLSchemaName = lDOMAttr.title;
			lDOMAttr.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
			lDOMAttr.classNameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
			lDOMAttr.nameSpaceId = lDOMAttr.nameSpaceIdNC + ":";						
			lDOMAttr.steward = lSchemaFileDefn.stewardId;
			lDOMAttr.submitter = getTextValue(docEle,"submitter_name");
			String lDescription = getTextValue(el,"definition");
			// escape any user provided values of "TBD"
			if (lDescription.indexOf("TBD") == 0) lDescription = "_" + lDescription;
			lDescription = DOMInfoModel.cleanCharString(lDescription);
			lDOMAttr.definition = lDescription;
			lDOMAttr.regAuthId = lRegAuthId;
			String lNillableFlag = getTextValue(el,"nillable_flag");
			if ((lNillableFlag.compareTo("true") == 0) || (lNillableFlag.compareTo("1") == 0)) lDOMAttr.isNilable = true;
			lDOMAttr.propType = "ATTRIBUTE";
			lDOMAttr.isAttribute = true;
			
			// get the value domain
			getValueDomain (lDOMAttr, el);
			
			// get the terminological entry
			getTermEntry (lDOMAttr, el);
			
//			DMDebugUtils.dumpTermEntryMap("0", lDOMAttr);
			
			// check if attribute already exists
			String lid = DMDocument.registrationAuthorityIdentifierValue + lLocalIdentifierCleaned;
			DOMAttr lExternAttr = DOMInfoModel.masterDOMAttrIdMap.get(lid);
			if (lExternAttr != null) {
			    if (lDOMAttr.definition.indexOf("TBD") == 0) lDOMAttr.definition = lExternAttr.definition;
				if (! lDOMAttr.isNilable) lDOMAttr.isNilable = lExternAttr.isNilable;
				if (!lDOMAttr.isEnumerated) lDOMAttr.isEnumerated = lExternAttr.isEnumerated;
				if (lDOMAttr.minimum_characters.indexOf("TBD") == 0) lDOMAttr.minimum_characters = lExternAttr.minimum_characters;
				if (lDOMAttr.maximum_characters.indexOf("TBD") == 0) lDOMAttr.maximum_characters = lExternAttr.maximum_characters;
				if (lDOMAttr.minimum_value.indexOf("TBD") == 0) lDOMAttr.minimum_value = lExternAttr.minimum_value;
				if (lDOMAttr.maximum_value.indexOf("TBD") == 0) lDOMAttr.maximum_value = lExternAttr.maximum_value;
				if (lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0) lDOMAttr.unit_of_measure_type = lExternAttr.unit_of_measure_type;
				if (lDOMAttr.valueType.indexOf("TBD") == 0) lDOMAttr.valueType = lExternAttr.valueType;
				if (lDOMAttr.dataConcept.indexOf("TBD") == 0) lDOMAttr.dataConcept = lExternAttr.dataConcept;
				if (lDOMAttr.pattern.indexOf("TBD") == 0) lDOMAttr.pattern = lExternAttr.pattern;
			}
		}		
	}

  private void getValueDomain(DOMAttr lDOMAttr, Element docEle) {
    String lVal;
    // get a nodelist of <DD_Value_Domain> elements
    NodeList nl = docEle.getElementsByTagName("DD_Value_Domain");
    // if(nl == null || nl.getLength() < 1) {
    // nl = docEle.getElementsByTagName("DD_Value_Domain2");
    // }
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        // get the elements
        Element el = (Element) nl.item(i);
        lVal = getTextValue(el, "enumeration_flag");
        if (lVal != null) {
          if ((lVal.compareTo("1") == 0) || (lVal.compareTo("true") == 0)) {
            lDOMAttr.isEnumerated = true;
          }
        }
        lVal = getTextValue(el, "minimum_characters");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_minimum_characters";
          }
          lDOMAttr.minimum_characters = lVal;
        }
        lVal = getTextValue(el, "maximum_characters");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_maximum_characters";
          }
          lDOMAttr.maximum_characters = lVal;
        }
        lVal = getTextValue(el, "minimum_value");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_minimum_value";
          }
          lDOMAttr.minimum_value = lVal;
        }
        lVal = getTextValue(el, "maximum_value");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_maximum_value";
          }
          lDOMAttr.maximum_value = lVal;
        }

        lVal = getTextValue(el, "unit_of_measure_type");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)
              || (lVal.compareTo("Units_of_None") == 0)) {
            lVal = "TBD_unit_of_measure_type";
          }
          lDOMAttr.unit_of_measure_type = lVal;
        }
        lVal = getTextValue(el, "value_data_type");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_value_data_type";
          }
          lDOMAttr.valueType = lVal;
          lDOMAttr.dataConcept = getDataConceptFromDataType(lVal);
        }
        lVal = getTextValue(el, "pattern");
        if (lVal != null) {
          if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
            lVal = "TBD_pattern";
          }
          lDOMAttr.pattern = lVal;
        }
        getPermissibleValues(lDOMAttr, el);
      }
    }
  }

  private void getTermEntry(Object lObject, Element docEle) {
    String lVal;
	
	// get the instance id
	String fromInstanceId = getTextValue(docEle,"instance_id");
	
	// get the SKOS semantic relation
	String semanticRelation = getTextValue(docEle,"skos_relation_name");
    
    // get a nodelist of <Terminological_Entry> elements
    NodeList nl = docEle.getElementsByTagName("Terminological_Entry");
    if (nl != null && nl.getLength() > 0) {
      String termEntryIdPrefix = "TE_";
      Integer termEntryNum = 1000000;
      for (int i = 0; i < nl.getLength(); i++) {
        // get the terminological entry
        Element el = (Element) nl.item(i);
        termEntryNum++;
        String termEntryId = termEntryIdPrefix + Integer.toString(termEntryNum);

        lVal = getTextValue(el, "language");
        if (lVal != null) {
            TermEntryDefn lTermEntry = new TermEntryDefn();
			String toInstanceId = getTextValue(el,"instance_id");
			lTermEntry.fromInstanceId = DOMInfoModel.cleanCharString(fromInstanceId);
			lTermEntry.toInstanceId = DOMInfoModel.cleanCharString(toInstanceId);
			lTermEntry.semanticRelation = DOMInfoModel.cleanCharString(semanticRelation); 
			lTermEntry.language = lVal;
                         
          if (lObject instanceof DOMAttr) {
            DOMAttr lAttr = (DOMAttr) lObject;
            lAttr.termEntryMap.put(termEntryId, lTermEntry);            
          } else if (lObject instanceof DOMClass) {
            DOMClass lClass = (DOMClass) lObject;
            lClass.termEntryMap.put(termEntryId, lTermEntry);
          }            

          lVal = getTextValue(el, "name");
          if (lVal != null) {
            lTermEntry.name = lVal;
          }

          // get the LDD name and version id
          lTermEntry.lddName = DOMInfoModel.cleanCharString(lLDDName);
          lTermEntry.lddVersion = DOMInfoModel.cleanCharString(lLDDVersionId);
			
          lVal = getTextValue(el, "definition");
          if (lVal != null) {
            lVal = DOMInfoModel.cleanCharString(lVal);
            lTermEntry.definition = lVal;
          }
          lVal = getTextValue(el, "preferred_flag");
          if (lVal != null && (lVal.compareTo("1") == 0) || (lVal.compareTo("true") == 0)) {
            lTermEntry.isPreferred = true;
          }
        } else {
          Utility.registerMessage(
              "2>error Terminological Entry: " + "The <language> attribute is missing.");
        }
      }
    }
  }

  private TreeMap<String, TermEntryQMObject> getTermEntryQueryModel(String lClassName,
      String lAttrName, Element docEle) {
    TreeMap<String, TermEntryQMObject> termEntryQueryModelMap = new TreeMap<>();

    String lVal;
    // get a nodelist of <Terminological_Entry> elements
    NodeList nl = docEle.getElementsByTagName("Terminological_Entry");
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        // get the terminological entry
        Element el = (Element) nl.item(i);
        lVal = getTextValue(el, "language");

        if (lVal != null) {
          TermEntryDefn lTermEntry = new TermEntryDefn();
          lTermEntry.language = lVal;
          lVal = getTextValue(el, "name");
          if (lVal != null) {
            lTermEntry.name = lVal;
          }

          lVal = getTextValue(el, "definition");
          if (lVal != null) {
            lVal = DOMInfoModel.cleanCharString(lVal);
            lTermEntry.definition = lVal;
          }
          lVal = getTextValue(el, "preferred_flag");
          if (lVal != null && (lVal.compareTo("1") == 0) || (lVal.compareTo("true") == 0)) {
            lTermEntry.isPreferred = true;
          }

          lVal = getTextValue(el, "skos_relation_name");
          lTermEntry.semanticRelation = lVal;

          // save term entry
          TermEntryQMObject lTermEntryQMObject =
              new TermEntryQMObject(lClassName, lAttrName, lTermEntry);
          String termEntryId = "TE" + "_" + lTermEntry.name + "_" + DOMInfoModel.getNextUId();
          termEntryQueryModelMap.put(termEntryId, lTermEntryQMObject);

        } else {
          Utility.registerMessage(
              "2>error Terminological Entry: " + "The <language> attribute is missing.");
        }
      }
      return termEntryQueryModelMap;
    }
    return null;
  }

  private void getPermissibleValues(DOMAttr lDOMAttr, Element docEle) {
    // get a nodelist of <DD_Permissible_Value> elements
    NodeList nl = docEle.getElementsByTagName("DD_Permissible_Value");
    if (nl == null || nl.getLength() < 1) {
      nl = docEle.getElementsByTagName("DD_Permissible_Value2");
    }
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        // get the elements
        Element el = (Element) nl.item(i);
        String lValue = getTextValue(el, "value");
        if (lValue == null) {
          lValue = "TBD_value";
        } else {
          lDOMAttr.valArr.add(lValue);
        }
        String lValueMeaning = getTextValue(el, "value_meaning");
        if (lValueMeaning == null) {
          lValueMeaning = "TBD_value_meaning";
        }
        lValueMeaning = DOMInfoModel.cleanCharString(lValueMeaning);

        String lValueBeginDate = getTextValue(el, "value_begin_date");
        String lValueEndDate = getTextValue(el, "value_end_date");

        DOMPermValDefn lDOMPermVal = new DOMPermValDefn(lValue, lValue, lValueMeaning);
        lDOMPermVal.setRDFIdentifier(lValue);
        if (lValueBeginDate != null) {
          lDOMPermVal.value_begin_date = lValueBeginDate;
        }
        if (lValueEndDate != null) {
          lDOMPermVal.value_end_date = lValueEndDate;
        }
        DOMProp lDOMProp = new DOMProp();
        lDOMProp.initForPermValue(lDOMAttr.classNameSpaceIdNC, lDOMAttr.parentClassTitle,
            lDOMAttr.nameSpaceIdNC, lDOMAttr.title, lValue);
        lDOMProp.hasDOMObject = lDOMPermVal;
        lDOMAttr.domPermValueArr.add(lDOMProp);
      }
    }
  }

  private void getClass(SchemaFileDefn lSchemaFileDefn, Element docEle) {
    // get a nodelist of <DD_Class> elements
    NodeList n2 = docEle.getElementsByTagName("DD_Class");
    if (n2 != null && n2.getLength() > 0) {
      for (int i = 0; i < n2.getLength(); i++) {
        // get the elements
        Element el = (Element) n2.item(i);
        String lTitle = getTextValue(el, "name");
        DOMClass lDOMClass = new DOMClass();
        lDOMClass.setRDFIdentifier(lTitle);
        if (classMap.get(lDOMClass.rdfIdentifier) == null) {
          lDOMClass.setIdentifier(lSchemaFileDefn.nameSpaceIdNC, lTitle);
          lDOMClass.title = lTitle;
          lDOMClass.versionId = DMDocument.classVersionIdDefault;
          classMap.put(lDOMClass.identifier, lDOMClass);
          classArr.add(lDOMClass);
          lDOMClass.isFromLDD = true;
          lDOMClass.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
          lDOMClass.nameSpaceId = lSchemaFileDefn.nameSpaceId;
          lDOMClass.subModelId = "UpperModel"; // *** this was added to allow the IM Spec to be
                                               // written
          lDOMClass.steward = lSchemaFileDefn.stewardId;

          String lDescription = getTextValue(el, "definition");
          // escape any user provided values of "TBD"
          if (lDescription.indexOf("TBD") == 0) {
            lDescription = "_" + lDescription;
          }
          lDOMClass.definition = DOMInfoModel.cleanCharString(lDescription);
          lDOMClass.regAuthId = lRegAuthId;
          // subClassOF is temporary until true subClassOf is found in main code
          lDOMClass.subClassOfTitle = DMDocument.masterUserClassName;
          lDOMClass.subClassOfIdentifier = DOMInfoModel.getClassIdentifier(
          DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName);
          String lLocalIdentifier3 = getTextValue(el, "local_identifier");
          String lLocalIdentifierCleaned = getLocalIdentifierNamespacePrefix(lSchemaFileDefn.nameSpaceId, lLocalIdentifier3);
         lDOMClass.localIdentifier = lLocalIdentifierCleaned;

          String lBooleanStringValue = getTextValue(el, "abstract_flag");
          lDOMClass.isAbstract = false;
          if (lBooleanStringValue != null) {
            if (lBooleanStringValue.compareTo("true") == 0) {
              lDOMClass.isAbstract = true;
            }
          }

          lBooleanStringValue = getTextValue(el, "element_flag");
          if (lBooleanStringValue != null) {
            if (lBooleanStringValue.compareTo("true") == 0) {
              lDOMClass.isExposed = true;
            }
          }

          // put both local and cross domain localIdentifiers in classMapLocal for resolution
          classMapLocal.put(lDOMClass.localIdentifier, lDOMClass);
          String crossLocalIdentifier =
              lSchemaFileDefn.nameSpaceIdNCLC + "." + lDOMClass.localIdentifier; // for referencing
                                                                                 // across stacked
                                                                                 // LDD namespaces
          classMapLocal.put(crossLocalIdentifier, lDOMClass);

          // get the terminological entry
          // temporarily commented out for class level TE
          // getTermEntry (lDOMClass, el);

          // reset class order
          DOMInfoModel.resetClassOrder();

          // get associations for the respective attributes
          getAssociations(lSchemaFileDefn, lDOMClass, el);
        }
      }
    }
  }

  private void getAssociations(SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass, Element ele) {
    // start processing
    ArrayList<Element> lAssocElemArr = getAssocElemFromClassDefn(ele);
    for (Iterator<Element> i = lAssocElemArr.iterator(); i.hasNext();) {
      Element lAssocElem = i.next();

      // initialize variables
      boolean lIsAttribute = false;
      boolean lIsChoice = false;
      boolean lIsAny = false;
      String lReferenceType = "TBD_referenceType_LDD";
      String lLocalIdentifier = "TBD_localIdentifier_LDD";
      String lMaximumOccurrences = "TBD_maximumOccurrences_LDD";
      String lMinimumOccurrences = "TBD_minimumOccurrences_LDD";
      ArrayList<String> lLocalIdentifierArr;

      // get DD_Associations
      if (lAssocElem.getNodeName().compareTo("DD_Association") == 0) {
        // initialize
        boolean isGroupDelimter = false;
        boolean isGroupContent = false;
        String lGroupName = "TBD_groupName";
        lCardMin = "";
        lCardMinI = 0;
        lCardMax = "";
        lCardMaxI = 0;

        // get common attributes
        lLocalIdentifier = "TBD_localIdentifier_LDD";
        lReferenceType = getTextValue(lAssocElem, "reference_type");
        lMaximumOccurrences = getTextValue(lAssocElem, "maximum_occurrences");
        lMinimumOccurrences = getTextValue(lAssocElem, "minimum_occurrences");

        // get all of the identifiers
        lLocalIdentifierArr = getXMLValueArr("identifier_reference", lAssocElem);  // standard identifier reference; needs to allow dot notation
        if (lLocalIdentifierArr.size() == 0) {
          lLocalIdentifierArr = getXMLValueArr("local_identifier", lAssocElem);  // old identifier reference
          if (lLocalIdentifierArr.size() == 0) {
            Utility.registerMessage("2>error Association - Reference_Type: " + lReferenceType
                + " - No identifiers were provided for this association.");
            continue;
          }
        }
        lLocalIdentifier = lLocalIdentifierArr.get(0);

        if (lReferenceType.compareTo("attribute_of") == 0) {
          lIsAttribute = true;
        } else if (lReferenceType.compareTo("component_of") == 0) {
          lIsAttribute = false;
        } else if ((lReferenceType.compareTo("parent_of") == 0)) {
          lIsAttribute = false;
          if (lLocalIdentifierArr.size() != 1) {
            Utility.registerMessage("2>error Association: " + lLocalIdentifier
                + " -  The reference_type 'parent_of' is allowed only one parent");
            continue;
          }
        } else {
          Utility.registerMessage("2>error Association: " + lLocalIdentifier
              + " - Invalid reference type: " + lReferenceType);
          continue;
        }

        // save the Internal_Reference or Local_Internal_Reference for rule checking
          if (lLocalIdentifier.compareTo(PDSINTERNALREFERENCE) == 0) {
          String lRuleReferenceXPath = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/"
              + DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Internal_Reference";
          RuleReferenceTypeDefn lRuleReferenceTypeDefn =
              new RuleReferenceTypeDefn(lRuleReferenceXPath, false);
          ruleReferenceArr.add(lRuleReferenceTypeDefn);
        } else if (lLocalIdentifier.compareTo(PDSLOCALINTERNALREFERENCE) == 0) {
          String lRuleReferenceXPath = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/"
              + DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Local_Internal_Reference";
          RuleReferenceTypeDefn lRuleReferenceTypeDefn =
              new RuleReferenceTypeDefn(lRuleReferenceXPath, true);
          ruleReferenceArr.add(lRuleReferenceTypeDefn);
        }

        validateAssociationCardinalities(lMinimumOccurrences, lMaximumOccurrences,
            lLocalIdentifier);

        // iterate through the local identifiers for this DD association and set up a property for
        // each
		for (String lLocalIdentifier2 : lLocalIdentifierArr ) {

          // create new association -- Note that lProperty.identifier will not be set until the
          // associated attribute is located in resolveComponentsForAssociation
          DOMProp lDOMProp = new DOMProp();

          // test for choice and any
          if (lLocalIdentifier2.indexOf("XSChoice#") == 0) {
            isGroupDelimter = true;
            isGroupContent = false;
            lIsChoice = true;
            lGroupName = lLocalIdentifier2 + DOMInfoModel.getNextGroupNum(); // i.e., XSChoice#26
          }
          if (lLocalIdentifier2.compareTo("XSAny#") == 0) {
            isGroupDelimter = true;
            isGroupContent = false;
            lIsAny = true;
            lGroupName = lLocalIdentifier2 + DOMInfoModel.getNextGroupNum();
          }

          // get common attributes
          lDOMProp.isAttribute = lIsAttribute;
          String lLocalIdentifierCleaned = getLocalIdentifierNamespacePrefix(lSchemaFileDefn.nameSpaceId, lLocalIdentifier2);
          lDOMProp.localIdentifier = lLocalIdentifierCleaned;
          lDOMProp.referenceType = lReferenceType;
          lDOMProp.rdfIdentifier = DMDocument.rdfPrefix + lDOMClass.nameSpaceIdNC + "."
              + lDOMClass.title + "." + lDOMProp.localIdentifier + "." + lDOMProp.referenceType
              + "." + DOMInfoModel.getNextUId();
          lDOMProp.enclLocalIdentifier = lDOMClass.localIdentifier;
          lDOMProp.classOrder = DOMInfoModel.getNextClassOrder();
          lDOMProp.isChoice = lIsChoice;
          lDOMProp.isAny = lIsAny;
          lDOMProp.maximumOccurrences = lMaximumOccurrences;
          lDOMProp.minimumOccurrences = lMinimumOccurrences;
          lDOMProp.groupName = lGroupName;
          lDOMProp.cardMin = lCardMin;
          lDOMProp.cardMinI = lCardMinI;
          lDOMProp.cardMax = lCardMax;
          lDOMProp.cardMaxI = lCardMaxI;

          // ignore "XSChoice# and XSAny#
          if (!isGroupDelimter) {

            // update property arrays
            LDDDOMPropArr.add(lDOMProp);
            if (lIsAttribute) {
              lDOMClass.ownedAttrArr.add(lDOMProp);
            } else {
              lDOMClass.ownedAssocArr.add(lDOMProp);
            }
          }

          // validate cardinalities and set card min and max
          if (isGroupDelimter) {
            isGroupDelimter = false;
            isGroupContent = true;
          }
        }

      } else if (lAssocElem.getNodeName().compareTo("DD_Associate_External_Class") == 0) {
        // get the class, attribute, and permissible values associated with an external class, e.g.,
        // pds.Internal_Reference.reference_type = geometry_to_reference_frame
        if (gSchemaFileDefn.nameSpaceIdNC.compareTo("qmsre") != 0) {
          get_Class_Attr_Prop_DD_Associate_External_Class(lDOMClass, lSchemaFileDefn, lAssocElem);
        } else {
          get_Class_Attr_Prop_DD_Associate_External_Class_Query_Model(lDOMClass, lSchemaFileDefn,
              lAssocElem);
        }
      }
    }
    return;
  }

  private void get_Class_Attr_Prop_DD_Associate_External_Class_Query_Model(DOMClass lDOMClassBase,
      SchemaFileDefn lSchemaFileDefn, Element lAssocElem) {
    // initialize
    String lIngestLDDNameSpaceIdNC = "TBD_lIngestLDDNameSpaceIdNC_LDD";
    String lIngestLDDClassName = "TBD_lIngestLDDClassName_LDD";
    String lIngestLDDAttrName = "TBD_lIngestLDDAttrName_LDD";
    String lXpath = "TBD_lXpath_LDD";
    lCardMin = "";
    lCardMinI = 0;
    lCardMax = "";
    lCardMaxI = 0;

    // get common elements
    lIngestLDDNameSpaceIdNC = getTextValue(lAssocElem, "namespace_id"); // e.g. pds
    lIngestLDDClassName = getTextValue(lAssocElem, "class_name"); // e.g., Internal_Reference
    lDOMClassBase.extrnTitleQM = lIngestLDDClassName;

    String lMaximumOccurrences = getTextValue(lAssocElem, "maximum_occurrences");
    String lMinimumOccurrences = getTextValue(lAssocElem, "minimum_occurrences");

    // create a new DOMAttr, the clone of an external attribute
    DOMAttr lDOMAttrExt = new DOMAttr();

    // init
    lDOMAttrExt.isPDS4 = true;
    lDOMAttrExt.isFromLDD = true;
    lDOMAttrExt.nameSpaceIdNC = lDOMClassBase.nameSpaceIdNC; // e.g., geom
    lDOMAttrExt.nameSpaceId = lDOMAttrExt.nameSpaceIdNC + ":";
    lDOMAttrExt.classNameSpaceIdNC = lDOMClassBase.nameSpaceIdNC; // e.g., geom
    lDOMAttrExt.parentClassTitle = lDOMClassBase.title; // e.g., Frame_Identification_Base
    lDOMAttrExt.attrParentClass = lDOMClassBase;
    lDOMAttrExt.steward = lSchemaFileDefn.stewardId; // e.g., geom
    lDOMAttrExt.regAuthId = lRegAuthId;
    lDOMAttrExt.propType = "ATTRIBUTE";
    lDOMAttrExt.isAttribute = true;

    // get the value domain
    // e.g., reference_type = geometry_to_reference_frame
    Node lValueListNode = lAssocElem.getFirstChild();
    while (lValueListNode != null) {
      if ((lValueListNode.getNodeType() == Node.ELEMENT_NODE)
          && (lValueListNode.getNodeName().indexOf("DD_Context_Value_List") == 0)) {
        Element lValueListElement = (Element) lValueListNode;
        lIngestLDDAttrName = getTextValue(lValueListElement, "attribute_name");
        lXpath = getTextValue(lValueListElement, "attribute_relative_xpath");
        int pvCount = 0;
        Node lPermValueListNode = lValueListElement.getFirstChild();
        while (lPermValueListNode != null) {
          if ((lPermValueListNode.getNodeType() == Node.ELEMENT_NODE)
              && (lPermValueListNode.getNodeName().indexOf("DD_Permissible_Value") == 0)) {
            Element lPermValueListElement = (Element) lPermValueListNode;
            String lValue = getTextValue(lPermValueListElement, "value"); // e.g.,
                                                                          // geometry_to_reference_frame
            String lValueMeaning = getTextValue(lPermValueListElement, "value_meaning");
            lDOMAttrExt.valArr.add(lValue);
            DOMPermValDefn lDOMPermValExt = new DOMPermValDefn(lValue, lValue, lValueMeaning);
            lDOMPermValExt.setRDFIdentifier(lValue);
            DOMProp lDOMPropPVExt = new DOMProp();
            lDOMPropPVExt.initForPermValue(lDOMClassBase.nameSpaceIdNC, lDOMClassBase.title,
                lDOMAttrExt.nameSpaceIdNC, lIngestLDDAttrName, lValue);
            lDOMPropPVExt.hasDOMObject = lDOMPermValExt;

            lDOMAttrExt.domPermValueArr.add(lDOMPropPVExt);
            pvCount++;
            TreeMap<String, TermEntryQMObject> lTermEntryQMObjectMap = getTermEntryQueryModel(
                lIngestLDDClassName, lIngestLDDAttrName, lPermValueListElement);
            if (lTermEntryQMObjectMap != null) {
              lDOMClassBase.isQueryModel = true;
              ArrayList<TermEntryQMObject> lTermEntryQMObjectArr =
                  new ArrayList<>(lTermEntryQMObjectMap.values());
              for (Iterator<TermEntryQMObject> j = lTermEntryQMObjectArr.iterator(); j.hasNext();) {
                TermEntryQMObject lTermEntryQMObject = j.next();
                String lTermIdentifier = lTermEntryQMObject.className + "."
                    + lTermEntryQMObject.attrName + "." + lTermEntryQMObject.termEntryDefn.name;
                lDOMPermValExt.termEntryMap.put(lTermIdentifier, lTermEntryQMObject.termEntryDefn);
              }
            }
          }
          lPermValueListNode = lPermValueListNode.getNextSibling();
        }
      }
      lValueListNode = lValueListNode.getNextSibling();
    }

    // copy in values from externally referenced attribute
    String lDOMAttrExternalId = DOMInfoModel.getAttrIdentifier(lIngestLDDNameSpaceIdNC,
        lIngestLDDClassName, lIngestLDDNameSpaceIdNC, lIngestLDDAttrName);
    DOMAttr lDOMAttrExternal = DOMInfoModel.masterDOMAttrIdMap.get(lDOMAttrExternalId);
    if (lDOMAttrExternal != null) {
      DOMProp lDOMPropExternal = DOMInfoModel.masterDOMPropIdMap.get(lDOMAttrExternalId);
      if (lDOMPropExternal != null) {
        finishInitDOMAttr(lDOMAttrExt, lDOMAttrExternal, lDOMPropExternal);
      } else {
        Utility.registerMessage("2>error Association: " + lDOMAttrExternalId
            + " - Could not find referenced property - Query Model 1");
      }
    } else // *** for qmsre process, when referencing external namespaces, e.g., msn, the attributes
           // are not present locally. Need some other solution. ***
    if (lSchemaFileDefn.nameSpaceIdNC.compareTo("qmsre") != 0) {
      Utility.registerMessage("2>error Association: " + lDOMAttrExternalId
          + " - Could not find referenced attribute - Query Model 2");
    }

    // set identification information
    lDOMAttrExt.setRDFIdentifier(lIngestLDDAttrName);
    lDOMAttrExt.lddLocalIdentifier = lDOMClassBase.nameSpaceIdNC + "." + lDOMClassBase.title + "."
        + lIngestLDDNameSpaceIdNC + "." + lIngestLDDAttrName; // e.g.
                                                              // geom.Frame_Identification_Base.pds.reference_type
    lDOMAttrExt.title = lDOMClassBase.title + "_" + lIngestLDDAttrName;
    lDOMAttrExt.lddTitle = lIngestLDDAttrName;
    // e.g., geom.Frame_Identification_Base.geom.reference_type --- i.e., duplicate DOMProp
    // identifier
    lDOMAttrExt.setIdentifier(lDOMClassBase.nameSpaceIdNC, lDOMClassBase.title,
        lDOMAttrExt.nameSpaceIdNC, lDOMAttrExt.title);
    lDOMAttrExt.extrnTitleQM = lIngestLDDAttrName;
    lDOMAttrExt.XMLSchemaName = lDOMAttrExt.title;
    if (lDOMAttrExt.domPermValueArr.size() > 0) {
      lDOMAttrExt.isEnumerated = true;
    }

    // validate min and max occurrences
    validateAssociationCardinalities(lMinimumOccurrences, lMaximumOccurrences,
        lDOMAttrExt.lddLocalIdentifier);

    // create a new DOMProp for lDOMAttrExt
    DOMProp lDOMPropAttrExt = new DOMProp();
    lDOMPropAttrExt.isAttribute = true;
    lDOMPropAttrExt.localIdentifier = lDOMAttrExt.lddLocalIdentifier;
    lDOMPropAttrExt.enclLocalIdentifier = lDOMClassBase.localIdentifier; // ???
    lDOMPropAttrExt.referenceType = "attribute_of";
    lDOMPropAttrExt.setRDFIdentifier(lIngestLDDAttrName);
    // e.g., geom.Frame_Identification_Base.geom.reference_type --- i.e., duplicate DOMAttr
    // identifier
    lDOMPropAttrExt.setIdentifier(lDOMClassBase.nameSpaceIdNC, lDOMClassBase.title,
        lDOMAttrExt.nameSpaceIdNC, lDOMAttrExt.title);
    lDOMPropAttrExt.parentClassTitle = lDOMClassBase.title; // e.g., Frame_Identification_Base
    lDOMPropAttrExt.attrParentClass = lDOMClassBase;
    lDOMPropAttrExt.classOrder = DOMInfoModel.getNextClassOrder();
    lDOMPropAttrExt.isChoice = false;
    lDOMPropAttrExt.isAny = false;
    lDOMPropAttrExt.maximumOccurrences = lMaximumOccurrences;
    lDOMPropAttrExt.minimumOccurrences = lMinimumOccurrences;
    lDOMPropAttrExt.groupName = "TBD_groupName";
    lDOMPropAttrExt.cardMin = lCardMin;
    lDOMPropAttrExt.cardMinI = lCardMinI;
    lDOMPropAttrExt.cardMax = lCardMax;
    lDOMPropAttrExt.cardMaxI = lCardMaxI;

    // add attribute to property
    lDOMPropAttrExt.hasDOMObject = lDOMAttrExt; // e.g.,
                                                // geom.Frame_Identification_Base.geom.reference_type

    // update property arrays with new DOMProp
    LDDDOMPropArr.add(lDOMPropAttrExt);

    // add lDOMAttrExt via lDOMPropAttrExt to base class
    lDOMClassBase.ownedAttrArr.add(lDOMPropAttrExt);
    return;
  }

  private void get_Class_Attr_Prop_DD_Associate_External_Class(DOMClass lDOMClassBase,
      SchemaFileDefn lSchemaFileDefn, Element lAssocElem) {
    // Synopsis:
    // Create one new standalone attribute - e.g.
    // geom:Body_Identification_Base.pds.Internal_Reference.pds.reference_type
    // permissible_value:geometry_to_body
    // used to generate schematron rule; not used elsewhere

    // Creates a Property for pds:Internal_Reference; to be resolved later
    // completes the Base Class - e.g., geom:Body_Identification_Base
    // geom:body_spice_name
    // pds:name
    // ==> pds:Internal_Reference

    // set flag on base class - e.g., geom:Body_Identification_Base
    lDOMClassBase.isAssociatedExternalClass = true;

    // initialize
    ArrayList<String> externalAttributeIdArr = new ArrayList<>();
    String lIngestLDDNameSpaceIdNC = "TBD_lIngestLDDNameSpaceIdNC_LDD";
    String lIngestLDDClassName = "TBD_lIngestLDDClassName_LDD";
    String lIngestLDDAttrName = "TBD_lIngestLDDAttrName_LDD";
    String lXpath = "TBD_lXpath_LDD";
    lCardMin = "";
    lCardMinI = 0;
    lCardMax = "";
    lCardMaxI = 0;

    // get common elements
    lIngestLDDNameSpaceIdNC = getTextValue(lAssocElem, "namespace_id"); // e.g. pds
    lIngestLDDClassName = getTextValue(lAssocElem, "class_name"); // e.g., Internal_Reference
    String lMaximumOccurrences = getTextValue(lAssocElem, "maximum_occurrences"); // not sure that
                                                                                  // cardinalities
                                                                                  // make sense for
                                                                                  // DD_Associate_External_Class
    String lMinimumOccurrences = getTextValue(lAssocElem, "minimum_occurrences"); // why repeat a
                                                                                  // specific class
                                                                                  // definition??

    // do we need more than one attribute. There might be more than one element in the list
    // create a new DOMAttr (e.g. namespaceid:geom -
    // identifier:geom:Body_Identification_Base.pds.Internal_Reference.pds.reference_type); clone
    // existing external attribute
    DOMAttr lDOMAttrExt = new DOMAttr();
    lDOMAttrExt.isPDS4 = true;
    lDOMAttrExt.isFromLDD = true;
    lDOMAttrExt.nameSpaceIdNC = lDOMClassBase.nameSpaceIdNC; // e.g., geom
    lDOMAttrExt.nameSpaceId = lDOMAttrExt.nameSpaceIdNC + ":";
    lDOMAttrExt.classNameSpaceIdNC = lDOMClassBase.nameSpaceIdNC; // e.g., geom
    lDOMAttrExt.parentClassTitle = lDOMClassBase.title; // e.g., Frame_Identification_Base,
                                                        // Body_Identification_Base
    lDOMAttrExt.steward = lSchemaFileDefn.stewardId; // e.g., geom
    lDOMAttrExt.regAuthId = lRegAuthId;
    lDOMAttrExt.propType = "ATTRIBUTE";
    lDOMAttrExt.isAttribute = true;
    lDOMAttrExt.isAssociatedExternalAttr = true;
    lDOMAttrExt.isAbstract = lDOMClassBase.isAbstract; // if base class is abstract then attribute
                                                       // is abstract

    // get the value domain
    // e.g., reference_type = geometry_to_reference_frame
    Node lValueListNode = lAssocElem.getFirstChild();
    while (lValueListNode != null) {
      if ((lValueListNode.getNodeType() == Node.ELEMENT_NODE)
          && (lValueListNode.getNodeName().indexOf("DD_Context_Value_List") == 0)) {
        Element lValueListElement = (Element) lValueListNode;
        lIngestLDDAttrName = getTextValue(lValueListElement, "attribute_name");
        lXpath = getTextValue(lValueListElement, "attribute_relative_xpath");
        int pvCount = 0;
        Node lPermValueListNode = lValueListElement.getFirstChild();
        while (lPermValueListNode != null) {
          if ((lPermValueListNode.getNodeType() == Node.ELEMENT_NODE)
              && (lPermValueListNode.getNodeName().indexOf("DD_Permissible_Value") == 0)) {
            Element lPermValueListElement = (Element) lPermValueListNode;
            String lValue = getTextValue(lPermValueListElement, "value"); // e.g.,
                                                                          // geometry_to_reference_frame
            String lValueMeaning = getTextValue(lPermValueListElement, "value_meaning");
            lDOMAttrExt.valArr.add(lValue);
            DOMPermValDefn lDOMPermValExt = new DOMPermValDefn(lValue, lValue, lValueMeaning);
            lDOMPermValExt.setRDFIdentifier(lValue);
            DOMProp lDOMPropPVExt = new DOMProp();
            lDOMPropPVExt.initForPermValue(lDOMClassBase.nameSpaceIdNC, lDOMClassBase.title,
                lDOMAttrExt.nameSpaceIdNC, lIngestLDDAttrName, lValue);
            lDOMPropPVExt.hasDOMObject = lDOMPermValExt;
            lDOMAttrExt.domPermValueArr.add(lDOMPropPVExt);
            pvCount++;
          }
          lPermValueListNode = lPermValueListNode.getNextSibling();
        }
      }
      lValueListNode = lValueListNode.getNextSibling();
    }

    // now complete the cloning of DOMAttr
    lDOMAttrExt.xPath =
        lDOMClassBase.nameSpaceIdNC + ":" + lDOMClassBase.title + "/" + lIngestLDDNameSpaceIdNC
            + ":" + lIngestLDDClassName + "/" + lIngestLDDNameSpaceIdNC + ":" + lIngestLDDAttrName; // e.g.
                                                                                                    // geom:Frame_Identification_Base/pds:Internal_Reference/pds:reference_type
    lDOMAttrExt.setRDFIdentifier(lIngestLDDAttrName);
    lDOMAttrExt.lddLocalIdentifier =
        lDOMClassBase.nameSpaceIdNC + "." + lDOMClassBase.title + "." + lIngestLDDNameSpaceIdNC
            + "." + lIngestLDDClassName + "." + lIngestLDDNameSpaceIdNC + "." + lIngestLDDAttrName; // e.g.
                                                                                                    // geom.Frame_Identification_Base.pds.Internal_Reference.pds.reference_type
    lDOMAttrExt.title = lDOMClassBase.title + "_" + lIngestLDDClassName + "_" + lIngestLDDAttrName;
    lDOMAttrExt.lddTitle = lIngestLDDAttrName;
    // e.g., geom.Frame_Identification_Base.geom.reference_type --- i.e., duplicate DOMProp
    // identifier
    lDOMAttrExt.setIdentifier(lDOMClassBase.nameSpaceIdNC, lDOMClassBase.title,
        lDOMAttrExt.nameSpaceIdNC, lDOMAttrExt.title);

    lDOMAttrExt.XMLSchemaName = lDOMAttrExt.title;
    if (lDOMAttrExt.domPermValueArr.size() > 0) {
      lDOMAttrExt.isEnumerated = true;
    }

    // get the external attribute
    String lDOMAttrExternalId = DOMInfoModel.getAttrIdentifier(lIngestLDDNameSpaceIdNC,
        lIngestLDDClassName, lIngestLDDNameSpaceIdNC, lIngestLDDAttrName);
    DOMAttr lDOMAttrExternal = DOMInfoModel.masterDOMAttrIdMap.get(lDOMAttrExternalId);
    if (lDOMAttrExternal != null) {
      lDOMAttrExt.lddUserAttribute = lDOMAttrExternal; // save for rule generation; need org
                                                       // namespaceid and title.
      DOMProp lDOMPropExternal = DOMInfoModel.masterDOMPropIdMap.get(lDOMAttrExternalId);
      if (lDOMPropExternal != null) {
        finishInitDOMAttr(lDOMAttrExt, lDOMAttrExternal, lDOMPropExternal);

        // save the identifier
        externalAttributeIdArr.add(lDOMAttrExt.identifier);
      }
    }

    // validate min and max occurrences
    validateAssociationCardinalities(lMinimumOccurrences, lMaximumOccurrences,
        lDOMAttrExt.lddLocalIdentifier);


    // update singleton attribute array;
    DOMInfoModel.userSingletonDOMClassAttrIdMap.put(lDOMAttrExt.identifier, lDOMAttrExt);

    // create a Property for the associated the external class - e.g. pds.Internal_Reference
    String lLocalIdentifier = lIngestLDDNameSpaceIdNC + "." + lIngestLDDClassName;

    // get reference type - component_of for DD_Associate_External_Class
    String lReferenceType = "component_of";

    // save the Internal_Reference or Local_Internal_Reference for rule checking
    /*
     * if (lLocalIdentifier.compareTo("pds.Internal_Reference") == 0 ) { String lRuleReferenceXPath
     * = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/" +
     * DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Internal_Reference"; RuleReferenceTypeDefn
     * lRuleReferenceTypeDefn = new RuleReferenceTypeDefn (lRuleReferenceXPath, false);
     * ruleReferenceArr.add(lRuleReferenceTypeDefn); } else if
     * (lLocalIdentifier.compareTo("pds.Local_Internal_Reference") == 0) { String
     * lRuleReferenceXPath = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/" +
     * DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Local_Internal_Reference";
     * RuleReferenceTypeDefn lRuleReferenceTypeDefn = new RuleReferenceTypeDefn
     * (lRuleReferenceXPath, true); ruleReferenceArr.add(lRuleReferenceTypeDefn); }
     */

    validateAssociationCardinalities(lMinimumOccurrences, lMaximumOccurrences, lLocalIdentifier);

    // set up DOMProp for external class -- Note that lProperty.identifier will not be set until the
    // associated attribute is located in resolveComponentsForAssociation
    DOMProp lDOMProp = new DOMProp();
    lDOMProp.isAttribute = false;
    lDOMProp.localIdentifier = lLocalIdentifier;
    lDOMProp.referenceType = lReferenceType;
    lDOMProp.rdfIdentifier = DMDocument.rdfPrefix + lDOMClassBase.nameSpaceIdNC + "."
        + lDOMClassBase.title + "." + lDOMProp.localIdentifier + "." + lDOMProp.referenceType + "."
        + DOMInfoModel.getNextUId();
    lDOMProp.enclLocalIdentifier = lDOMClassBase.localIdentifier;
    lDOMProp.classOrder = DOMInfoModel.getNextClassOrder();
    lDOMProp.isChoice = false;
    lDOMProp.isAny = false;
    lDOMProp.maximumOccurrences = lMaximumOccurrences;
    lDOMProp.minimumOccurrences = lMinimumOccurrences;
    // lDOMProp.groupName = lGroupName;
    lDOMProp.cardMin = lCardMin;
    lDOMProp.cardMinI = lCardMinI;
    lDOMProp.cardMax = lCardMax;
    lDOMProp.cardMaxI = lCardMaxI;
    lDOMProp.valArr = externalAttributeIdArr; // capture the attribute ids for generating rules
    lDOMClassBase.ownedAssocArr.add(lDOMProp); // e.g. pds.Internal_Reference
    // update property arrays with DOMClass property
    LDDDOMPropArr.add(lDOMProp);

    return;
  }

  private void finishInitDOMAttr(DOMAttr lDOMAttrTo, DOMAttr lDOMAttrFrom, DOMProp lDOMPropFrom) {
    lDOMAttrTo.classConcept = lDOMAttrFrom.classConcept; // copy in the values from common attribute
                                                         // e.g.,
                                                         // pds.Internal_Reference.pds.reference_type
    lDOMAttrTo.dataConcept = lDOMAttrFrom.dataConcept;
    lDOMAttrTo.classWord = lDOMAttrFrom.classWord;
    lDOMAttrTo.definition = lDOMAttrFrom.definition;
    lDOMAttrTo.xmlBaseDataType = lDOMAttrFrom.xmlBaseDataType;
    lDOMAttrTo.protValType = lDOMAttrFrom.protValType;
    lDOMAttrTo.propType = lDOMAttrFrom.propType;
    lDOMAttrTo.valueType = lDOMAttrFrom.valueType;
    lDOMAttrTo.minimum_characters = lDOMAttrFrom.minimum_characters;
    lDOMAttrTo.maximum_characters = lDOMAttrFrom.maximum_characters;
    lDOMAttrTo.minimum_value = lDOMAttrFrom.minimum_value;
    lDOMAttrTo.maximum_value = lDOMAttrFrom.maximum_value;
    lDOMAttrTo.format = lDOMAttrFrom.format;
    lDOMAttrTo.pattern = lDOMAttrFrom.pattern;
    lDOMAttrTo.unit_of_measure_type = lDOMAttrFrom.unit_of_measure_type;
    lDOMAttrTo.default_unit_id = lDOMAttrFrom.default_unit_id;
    lDOMAttrTo.unit_of_measure_precision = lDOMAttrFrom.unit_of_measure_precision;

    lDOMAttrTo.attrParentClass = lDOMAttrFrom.attrParentClass;
    lDOMAttrTo.parentClassTitle = lDOMAttrFrom.parentClassTitle;

    lDOMAttrTo.cardMinI = lDOMPropFrom.cardMinI;
    lDOMAttrTo.cardMin = lDOMPropFrom.cardMin;
    lDOMAttrTo.cardMaxI = lDOMPropFrom.cardMaxI;
    lDOMAttrTo.cardMax = lDOMPropFrom.cardMax;
    lDOMAttrTo.isChoice = lDOMPropFrom.isChoice;
    lDOMAttrTo.isAny = lDOMPropFrom.isAny;
    lDOMAttrTo.groupName = lDOMPropFrom.groupName;
    lDOMAttrTo.isUsedInClass = true;
    lDOMAttrTo.isPDS4 = true;
    lDOMAttrTo.isFromLDD = true;
  }

  private void getRule(SchemaFileDefn lSchemaFileDefn, Element docEle) {
    ArrayList<String> lValueArr = new ArrayList<>();

    // get a nodelist of <DD_Class> elements
    NodeList n2 = docEle.getElementsByTagName("DD_Rule");
    if (n2 != null && n2.getLength() > 0) {
      for (int i = 0; i < n2.getLength(); i++) {
        // get the elements
        Element el = (Element) n2.item(i);
        String lLocalIdentifier = "TBD_lLocalIdentifier";
        String lValue1 = getTextValue(el, "local_identifier");
        if (!(lValue1 == null || (lValue1.indexOf("TBD") == 0))) {
          lLocalIdentifier = lValue1;
        }

        String lContext = "TBD_lContext";
        String lValue2 = getTextValue(el, "rule_context");
        if (!(lValue2 == null || (lValue2.indexOf("TBD") == 0))) {
          lContext = lValue2;
        }

        DOMRule lDOMRule = new DOMRule(lContext);
        lDOMRule.setRDFIdentifier();

        if (ruleMap.get(lDOMRule.rdfIdentifier) == null) {
          ruleMap.put(lDOMRule.rdfIdentifier, lDOMRule);
          ruleArr.add(lDOMRule);
          lDOMRule.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
          lDOMRule.attrNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
          lDOMRule.attrTitle = "Rule";
          lDOMRule.classNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
          lDOMRule.classSteward = lSchemaFileDefn.stewardId;
          lDOMRule.xpath = lContext;

          // get the let assign values
          lValueArr = getXMLValueArr("rule_assign", el);
          if (!(lValueArr == null || lValueArr.isEmpty())) {
            lDOMRule.letAssignArr = lValueArr;
          }

          // get the rule statements
          ArrayList<Element> lElementStmtArr = getElement("DD_Rule_Statement", el);
          for (Iterator<Element> j = lElementStmtArr.iterator(); j.hasNext();) {
            Element lElement = j.next();

            DOMAssert lDOMAssertDefn = new DOMAssert("Rule");
            lDOMRule.assertArr.add(lDOMAssertDefn);

            String lValue3 = getTextValue(lElement, "rule_type");
            if (!(lValue3 == null || (lValue3.indexOf("TBD") == 0))) {
              if (lValue3.compareTo("Assert") == 0) {
                lDOMAssertDefn.assertType = "RAW";
              } else if (lValue3.compareTo("Assert Every") == 0) {
                lDOMAssertDefn.assertType = "EVERY";
              } else if (lValue3.compareTo("Assert If") == 0) {
                lDOMAssertDefn.assertType = "IF";
              } else if (lValue3.compareTo("Report") == 0) {
                lDOMAssertDefn.assertType = "REPORT";
              }
            }

            String lValue4 = getTextValue(lElement, "rule_test");
            if (!(lValue4 == null || (lValue4.indexOf("TBD") == 0))) {
              lDOMAssertDefn.assertStmt = lValue4;
            }

            String lValue5 = getTextValue(lElement, "rule_message");
            if (!(lValue5 == null || (lValue5.indexOf("TBD") == 0))) {
              lDOMAssertDefn.assertMsg = lValue5;
            }

            String lValue6 = getTextValue(lElement, "rule_description");
            if (!(lValue6 == null || (lValue6.indexOf("TBD") == 0))) {
              lDOMAssertDefn.specMesg = lValue6;
            }

            // get the statement values
            lValueArr = getXMLValueArr("rule_value", lElement);
            if (!(lValueArr == null || lValueArr.isEmpty())) {
              lDOMAssertDefn.testValArr = lValueArr;
            }
          }
        }
      }
    }
  }

  private void getPropMap(Element docEle) {
    // get a nodelist of <Property Maps> elements
    NodeList n2 = docEle.getElementsByTagName("Property_Maps");
    if (n2 != null && n2.getLength() > 0) {
      for (int i = 0; i < n2.getLength(); i++) {
        // get the elements
        Element lPropMapsElem = (Element) n2.item(i);
        String lValue = getTextValue(lPropMapsElem, "identifier");
        String lIdentifier = "TBD_lIdentifier";
        if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
          lIdentifier = lValue;
        }

        PropertyMapsDefn lPropertyMaps = new PropertyMapsDefn(lIdentifier);
        lPropertyMaps.rdfIdentifier = DOMInfoModel.getPropMapRDFIdentifier(lIdentifier);
        PropertyMapsDefn lPropertyMaps2 = propertyMapsMap.get(lPropertyMaps.rdfIdentifier);
        if (lPropertyMaps2 == null) {
          propertyMapsMap.put(lPropertyMaps.rdfIdentifier, lPropertyMaps);
          propertyMapsArr.add(lPropertyMaps);

          // get the title
          lValue = getTextValue(lPropMapsElem, "title");
          if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
            lPropertyMaps.title = lValue;
          }

          // get the namespace_id
          lValue = getTextValue(lPropMapsElem, "namespace_id");
          if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
            lPropertyMaps.namespace_id = lValue;
          }

          // get the description
          lValue = getTextValue(lPropMapsElem, "description");
          if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
            lPropertyMaps.description = DOMInfoModel.cleanCharString(lValue);
          }

          // get the external_property_map_id
          lValue = getTextValue(lPropMapsElem, "external_property_map_id");
          if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
            lPropertyMaps.external_property_map_id = lValue;
          }

          // get the <Property Map> in this <Property Maps>
          ArrayList<Element> lElementStmtArr = getElement("Property_Map", lPropMapsElem);
          for (Iterator<Element> j = lElementStmtArr.iterator(); j.hasNext();) {
            Element lPropMapElem = j.next();

            // get the identifier
            lValue = getTextValue(lPropMapElem, "identifier");
            if (lValue == null || (lValue.indexOf("TBD") == 0)) {
              lValue = "TBD_identifier";
            }

            // get new property map
            PropertyMapDefn lPropertyMap = new PropertyMapDefn(lValue);
            lPropertyMaps.propertyMapArr.add(lPropertyMap);

            // get the title
            lValue = getTextValue(lPropMapElem, "title");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.title = lValue;
            }

            // get the external namespace_id
            lValue = getTextValue(lPropMapElem, "external_namespace_id");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.external_namespace_id = lValue;
            }

            // get the model_object_id
            lValue = getTextValue(lPropMapElem, "model_object_id");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.model_object_id = lValue;
            }

            // get the model_object_type
            lValue = getTextValue(lPropMapElem, "model_object_type");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.model_object_type = lValue;
            }

            // get the instance_id
            lValue = getTextValue(lPropMapElem, "instance_id");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.instance_id = lValue;
            }

            // get the description
            lValue = getTextValue(lPropMapElem, "description");
            if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
              lPropertyMap.description = DOMInfoModel.cleanCharString(lValue);
            }

            // get the property map entries
            ArrayList<Element> lElementStmtArr2 = getElement("Property_Map_Entry", lPropMapElem);
            for (Iterator<Element> k = lElementStmtArr2.iterator(); k.hasNext();) {
              Element lPropMapEntryElem = k.next();

              // get new property map
              PropertyMapEntryDefn lPropertyMapEntry =
                  new PropertyMapEntryDefn("Property_Map_Entry");
              lPropertyMap.propertyMapEntryArr.add(lPropertyMapEntry);

              // get the property_name
              lValue = getTextValue(lPropMapEntryElem, "property_name");
              if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
                lPropertyMapEntry.property_name = lValue;
              }

              // get the property_value
              lValue = getTextValue(lPropMapEntryElem, "property_value");
              if (!(lValue == null || (lValue.indexOf("TBD") == 0))) {
                lPropertyMapEntry.property_value = DOMInfoModel.cleanCharString(lValue);
              }
            }
          }
        }
      }
    }

    // copy this LDDs property maps into the Master
    Set<String> set1 = propertyMapsMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String lId = iter1.next();
      PropertyMapsDefn lPropertyMapsDefn = propertyMapsMap.get(lId);
      DOMInfoModel.masterPropertyMapsMap.put(lId, lPropertyMapsDefn);
    }
    for (Iterator<PropertyMapsDefn> i = propertyMapsArr.iterator(); i.hasNext();) {
      PropertyMapsDefn lPropertyMapsDefn = i.next();
      DOMInfoModel.masterPropertyMapsArr.add(lPropertyMapsDefn);
    }
  }

  private ArrayList<Element> getElement(String elemName, Element elem) {
    ArrayList<Element> lElemArr = new ArrayList<>();
    Node assocElement = elem.getFirstChild();
    while (assocElement != null) {
      if ((assocElement.getNodeType() == Node.ELEMENT_NODE)
          && (assocElement.getNodeName().indexOf(elemName) == 0)) {
        lElemArr.add((Element) assocElement);
      }
      assocElement = assocElement.getNextSibling();
    }
    return lElemArr;
  }

  // resolve all class associations
  private void resolveComponentsForAssociation(SchemaFileDefn lSchemaFileDefn) {
    // Structures to capture component classes for a choice block
    ArrayList<String> lBlockCompRDFIdArr = new ArrayList<>();
    ArrayList<DOMProp> lBlockCompClassArr = new ArrayList<>();
    TreeMap<String, DOMProp> lBlockCompClassMap = new TreeMap<>();

    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();

      // if class was defined using DD_Associate_External_Class and it is a query model there is no
      // need to resolve
      if (lDOMClass.isAssociatedExternalClass
          && lSchemaFileDefn.nameSpaceIdNCLC.compareTo("qmsre") == 0) {
        continue;
      }

      // for each association in a class, get the attribute (AttrDefn) or the association (AttrDefn
      // with class titles as values)
      boolean isChoiceOrAny = false;
      boolean isChoiceOrAnyDelimiter = false;
      ArrayList<DOMClass> lChildClassArr = new ArrayList<>();

      // first resolve attribute
      for (Iterator<DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if ((lDOMProp.localIdentifier.indexOf("XSChoice#") == 0)
            || (lDOMProp.localIdentifier.compareTo("XSAny#") == 0)) {
          continue;
        }
        
        DOMAttr lDOMAttr = getLocalOrExternAttr(lSchemaFileDefn, lDOMClass, lDOMProp);
        if (lDOMAttr != null) {

          // standard Ingest_LDD defined attribute
          lDOMAttr.cardMinI = lDOMProp.cardMinI;
          lDOMAttr.cardMin = lDOMProp.cardMin;
          lDOMAttr.cardMaxI = lDOMProp.cardMaxI;
          lDOMAttr.cardMax = lDOMProp.cardMax;
          lDOMAttr.isChoice = lDOMProp.isChoice;
          lDOMAttr.isAny = lDOMProp.isAny;
          lDOMAttr.groupName = lDOMProp.groupName;
          lDOMAttr.isUsedInClass = true;
          lDOMProp.nameSpaceIdNC = lDOMAttr.nameSpaceIdNC;
          lDOMProp.title = lDOMAttr.title;
          lDOMProp.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lDOMAttr.nameSpaceIdNC,
              lDOMAttr.title);
          lDOMProp.nameSpaceIdNC = lDOMAttr.nameSpaceIdNC;
          lDOMProp.title = lDOMAttr.title; // deprecate
          lDOMProp.parentClassTitle = lDOMClass.title;
          lDOMProp.attrParentClass = lDOMClass;

          // add the attribute to the property
          lDOMProp.hasDOMObject = lDOMAttr;

          // add the association (AttrDefn) to the resolved attribute array
          attrArrResolved.add(lDOMProp);

          // fixup Class
          // deprecate lDOMClass.ownedAttrArr.add(lAttr);
          lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMAttr.nsTitle);
        } else // DMDocument.registerMessage ("2>error Association: " + lDOMProp.localIdentifier + "
               // - Could not find referenced attribute - Reference Type: " +
               // lDOMProp.referenceType);
        // *** for qmsre process, when referencing external namespaces, e.g., msn, the attributes
        // are not present locally. Need some other solution. ***
        if (lSchemaFileDefn.nameSpaceIdNC.compareTo("qmsre") != 0) {
          Utility.registerMessage("2>error Association: " + lDOMProp.localIdentifier
              + " - Could not find referenced attribute - Reference Type: "
              + lDOMProp.referenceType);
        }
      }
      
      for (Iterator<DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();

        // resolve an association/class
        if (lDOMProp.referenceType.compareTo("component_of") == 0) {

          // get the associated local or external class
          DOMClass lDOMClassComponent =
              getLocalOrExtrnCompClass(lSchemaFileDefn, lDOMClass, lDOMProp);
          if (lDOMClassComponent != null) {

            // are the following 2 statements useful?
            lDOMClass.isChoice = lDOMProp.isChoice;
            lDOMClass.isAny = lDOMProp.isAny;
            isChoiceOrAny = lDOMProp.isChoice || lDOMProp.isAny;
            isChoiceOrAnyDelimiter = (lDOMProp.localIdentifier.indexOf("XSChoice#") == 0)
                || (lDOMProp.localIdentifier.compareTo("XSAny#") == 0);

            lDOMProp.nameSpaceIdNC = lDOMClassComponent.nameSpaceIdNC;
            lDOMProp.title = lDOMClassComponent.title;
            lDOMProp.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title,
                lDOMClassComponent.nameSpaceIdNC, lDOMClassComponent.title);
            lDOMProp.parentClassTitle = lDOMClass.title;
            lDOMProp.attrParentClass = lDOMClass;

            // add the associated class
            // if (! isChoiceOrAny) {
            lDOMProp.valArr.add(lDOMClassComponent.title);
            lDOMProp.hasDOMObject = lDOMClassComponent;
            lDOMClassComponent.hasDOMPropInverse = lDOMProp;

            // get all block headers and components
            if (lDOMProp.groupName.indexOf("XSChoice#") == 0
                || lDOMProp.groupName.indexOf("XSAny#") == 0) {
              if (!lBlockCompRDFIdArr.contains(lDOMProp.rdfIdentifier)) {
                lBlockCompRDFIdArr.add(lDOMProp.rdfIdentifier);
                lBlockCompClassArr.add(lDOMProp);
              }
            }

            // add the association (AttrDefn) to the resolved attribute array
            attrArrResolved.add(lDOMProp);

            // following needed to fixup choice and any assoc.valArr(AttrDefn) after all class
            // components are found
            if (isChoiceOrAny && !isChoiceOrAnyDelimiter) {
              if (!lChildClassArr.contains(lDOMClassComponent)) {
                lChildClassArr.add(lDOMClassComponent);
              }
            }

            // fixup the class associations (AttrDefn)
            if (!isChoiceOrAny) {
              lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMProp.nsTitle);
            } else if (isChoiceOrAnyDelimiter) {
              lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMProp.nsTitle);
            }
          } else {
            Utility.registerMessage("2>error Association: " + lDOMProp.localIdentifier
                + " - Missing Component - Reference Type: " + lDOMProp.referenceType);
          }


        } else if (lDOMProp.referenceType.compareTo("parent_of") == 0) {
          // add the referenced (LDD) class as the parent of (base_of) this (LDD) Class (lClass)
          DOMClass lDOMParentClass =
              getLocalOrExtrnParentClass(lSchemaFileDefn, lDOMClass, lDOMProp);
          if (lDOMParentClass != null) {
            lDOMClass.subClassOfTitle = lDOMParentClass.title;
            lDOMClass.subClassOfIdentifier = lDOMParentClass.identifier;
            lDOMClass.subClassOf = lDOMParentClass;
          } else {
            Utility.registerMessage(
                "2>error Association: " + lDOMProp.identifier + " - Missing Class: "
                    + lDOMProp.localIdentifier + " - Reference Type: " + lDOMProp.referenceType);
          }
        }
      }
    }


    // Fixup the choice block component classes.
    // first get the block headers.
    for (Iterator<DOMProp> j = lBlockCompClassArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.title.indexOf("XSChoice#") == 0 || lDOMProp.title.indexOf("XSAny#") == 0) {
        lBlockCompClassMap.put(lDOMProp.groupName, lDOMProp);
      }
    }

    // second, get the block members
    ArrayList<DOMProp> lBlockHeaderArr = new ArrayList<>(lBlockCompClassMap.values());
    for (Iterator<DOMProp> j = lBlockCompClassArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.title.indexOf("XSChoice#") == 0 || lDOMProp.title.indexOf("XSAny#") == 0) {
        continue;
      }
      for (Iterator<DOMProp> k = lBlockHeaderArr.iterator(); k.hasNext();) {
        DOMProp lBlockHeader = k.next();
        if (lDOMProp.groupName.compareTo(lBlockHeader.groupName) == 0) {
          lBlockHeader.valArr.add(lDOMProp.title);
        }
      }
    }

    // update the assoc (AttrDefn) valArr; change
    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      for (Iterator<DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        ArrayList<String> updValArr = new ArrayList<>();
        for (Iterator<String> k = lDOMProp.valArr.iterator(); k.hasNext();) {
          String lLDDValue = k.next();
          String lExtValue = lLDDValArrExtUpdDefnClassMap.get(lLDDValue);
          if (lExtValue == null) {
            updValArr.add(lLDDValue);
          } else {
            updValArr.add(lExtValue);
          }
        }
        lDOMProp.valArr = updValArr;
      }
    }
    return;
  }

  // return a local or external attribute
  private DOMAttr getLocalOrExternAttr(SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass,
      DOMProp lDOMProp) {
	  

    // will be looking for something like "0001_NASA_PDS_1.pds.USER.standard_deviation"
    String lLocalIdentifier = lDOMProp.localIdentifier;
        
    // check if attribute is an LDD attribute or an external added in an earlier iteration
    DOMAttr lDOMAttr = attrMapLocal.get(lLocalIdentifier);
    if (lDOMAttr == null) {
      int lStringInd = lLocalIdentifier.lastIndexOf(".");
      String lLDDExtTitle = lLocalIdentifier;
      String lLDDExtNS = "xxx";
      if (lStringInd > -1) {
        lLDDExtTitle = lLocalIdentifier.substring(lStringInd + 1);
      }
      if (lStringInd > 0) {
        lLDDExtNS = lLocalIdentifier.substring(0, lStringInd);
      }
      lLDDExtNS = lLDDExtNS.toLowerCase();
      // e.g., 0001_NASA_PDS_1.all.USER.pds.kernel_type
      String lAttrIdentifier =
          DOMInfoModel.getAttrIdentifier(DMDocument.masterUserClassNamespaceIdNC,
              DMDocument.masterUserClassName, lLDDExtNS, lLDDExtTitle);
      lDOMAttr = DOMInfoModel.userDOMClassAttrIdMap.get(lAttrIdentifier);
      if (lDOMAttr == null) {
        // *** for qmsre process, when referencing external namespaces, e.g., msn, the attributes
        // are not present locally. Need some other solution. ***
        if (lSchemaFileDefn.nameSpaceIdNC.compareTo("qmsre") != 0) {
          Utility.registerMessage(
              "2>error Class:" + lDOMClass.identifier + "  Association:" + lDOMProp.localIdentifier
                  + "  Attribute: " + lLocalIdentifier + " - Missing Attribute");
        }
        return null;
      }
    }

    // save the namespace to create an import file
    if ((lDOMAttr.nameSpaceIdNC.compareTo("pds") != 0)
        && (lDOMAttr.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0)
        && (!DMDocument.LDDImportNameSpaceIdNCArr.contains(lDOMAttr.nameSpaceIdNC))) {
      DMDocument.LDDImportNameSpaceIdNCArr.add(lDOMAttr.nameSpaceIdNC);
    }
    
    // validate that referenced attribute is exposed
	// 555 commented out until DDWG approves impact of the fix
    // if (lDOMAttr.nameSpaceIdNC.compareTo("pds") == 0 && ! lDOMAttr.isExposed) {
    // if (lDOMAttr.isEnumerated) {
    // DMDocument.registerMessage("2>warning Attribute: " + " - The referenced enumerated attribute
    // " + lLocalIdentifier + " is not exposed.");
    // } else {
    // DMDocument.registerMessage("2>warning Attribute: " + " - The referenced attribute " +
    // lLocalIdentifier + " is not exposed.");
    // }
    // }


    // clone the USER or LDD attribute for use as a Resolved attribute
    // returns rdfIdentifier = "TBD_rdfIdentifier"
    String lRDFIdentifier = DMDocument.rdfPrefix + lDOMAttr.title + "." + DOMInfoModel.getNextUId();
    DOMAttr lNewDOMAttr = new DOMAttr();
    lNewDOMAttr.cloneAttr(lDOMAttr);
    // update new attribute
    lNewDOMAttr.rdfIdentifier = lRDFIdentifier;
    lNewDOMAttr.nameSpaceId = lDOMAttr.nameSpaceId;
    lNewDOMAttr.parentClassTitle = lDOMClass.title;
    lNewDOMAttr.attrParentClass = lDOMClass;
    lNewDOMAttr.classNameSpaceIdNC = lDOMClass.nameSpaceIdNC;
    lNewDOMAttr.classSteward = lDOMClass.steward;
    lNewDOMAttr.isFromLDD = true;
    lNewDOMAttr.lddLocalIdentifier = lLocalIdentifier;
    lNewDOMAttr.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lNewDOMAttr.nameSpaceIdNC,
        lNewDOMAttr.title);
    // attrArrResolved.add(lNewAttr);
    lNewDOMAttr.lddUserAttribute = lDOMAttr; // actually this could be an LDD attribute
    return lNewDOMAttr;
  }

  // return a local or external class
  private DOMClass getLocalOrExtrnCompClass(SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass,
      DOMProp lDOMProp) {
    // String lLocalIdentifier = lProperty.localIdentifierArr.get(0);
    DOMClass lComponentDOMClass;
    String lLocalIdentifier = lDOMProp.localIdentifier;

    // Is the class local
    lComponentDOMClass = classMapLocal.get(lLocalIdentifier);
    if (lComponentDOMClass != null) {
      return lComponentDOMClass;
    }
    
    // try with dot converted to colon
	String newLocalIdentifier = DOMInfoModel.replaceString(lLocalIdentifier, ".", ":");

    // Is the class local
    lComponentDOMClass = classMapLocal.get(newLocalIdentifier);
    if (lComponentDOMClass != null) {
      return lComponentDOMClass;
    }

    // Assume class is external
    String lClassIdentifier =
        DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
    lComponentDOMClass = DOMInfoModel.masterDOMClassIdMap.get(lClassIdentifier);
    if (lComponentDOMClass != null) {
      // save the namespace to create an import file
      if ((lComponentDOMClass.nameSpaceIdNC.compareTo("pds") != 0)
          && (lComponentDOMClass.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0)
          && (!DMDocument.LDDImportNameSpaceIdNCArr.contains(lComponentDOMClass.nameSpaceIdNC))) {
        DMDocument.LDDImportNameSpaceIdNCArr.add(lComponentDOMClass.nameSpaceIdNC);
      }
      // create the valArr update map - local to external titles.
      lLDDValArrExtUpdDefnClassMap.put(lLocalIdentifier, lComponentDOMClass.title);
      return lComponentDOMClass;
    }
    Utility.registerMessage("2>error Class:" + lDOMClass.identifier + "  Association:"
        + lDOMProp.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Component Class");
    return null;
  }

  // return a local or external class for a parent
  private DOMClass getLocalOrExtrnParentClass(SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass,
      DOMProp lDOMProp) {
    DOMClass lParentDOMClass;
    String lLocalIdentifier = lDOMProp.localIdentifier;

    // Is the class local
    lParentDOMClass = classMapLocal.get(lLocalIdentifier);
    if (lParentDOMClass != null) {
      return lParentDOMClass;
    }

    // Assume class is external
    String lClassIdentifier =
        DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
    lParentDOMClass = DOMInfoModel.masterDOMClassIdMap.get(lClassIdentifier);
    if (lParentDOMClass != null) {
      // save the namespace to create an import file
      if ((lParentDOMClass.nameSpaceIdNC.compareTo("pds") != 0)
          && (lParentDOMClass.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0)
          && (!DMDocument.LDDImportNameSpaceIdNCArr.contains(lParentDOMClass.nameSpaceIdNC))) {
        DMDocument.LDDImportNameSpaceIdNCArr.add(lParentDOMClass.nameSpaceIdNC);
      }
      return lParentDOMClass;
    }
    Utility.registerMessage("2>error Class:" + lDOMClass.identifier + "  Association:"
        + lDOMProp.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Parent Class");
    return null;
  }

  private void scanRulesForExternalNamespaces(SchemaFileDefn lSchemaFileDefn) {
    for (Iterator<DOMRule> i = ruleArr.iterator(); i.hasNext();) {
      DOMRule lDOMRule = i.next();
      ArrayList<String> namespaceArr = new ArrayList<>();
      namespaceArr = extractNamespaces(lDOMRule.xpath);
      for (Iterator<String> j = namespaceArr.iterator(); j.hasNext();) {
        String lNamespace = j.next();

        // save the namespace to create an import file
        if ((lDOMRule.nameSpaceIdNC.compareTo("pds") != 0)
            && (lDOMRule.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0)
            && (!DMDocument.LDDImportNameSpaceIdNCArr.contains(lDOMRule.nameSpaceIdNC))) {
          DMDocument.LDDImportNameSpaceIdNCArr.add(lDOMRule.nameSpaceIdNC);
        }
      }
    }
  }

  private ArrayList<String> extractNamespaces(String lRuleXpath) {
    // msn:Mission_Information/msn:mission_phase_name
    ArrayList<String> namespaceArr = new ArrayList<>();
    // System.out.println("\ndebug extractNamespace lRuleXpath:" + lRuleXpath);

    int begNamespaceOffset = 0;
    int endNamespaceOffset = 0;
    StringBuffer lInBuff = new StringBuffer(lRuleXpath);
    int inCnt = 0, inLmt = lInBuff.length();
    while (inCnt < inLmt) {
      Character inChar = lInBuff.charAt(inCnt);
      if (inChar == '/') {
        begNamespaceOffset = inCnt + 1;
      }
      if (inChar == ':') {
        endNamespaceOffset = inCnt;
        String lNamespace = lRuleXpath.substring(begNamespaceOffset, endNamespaceOffset);
        namespaceArr.add(lNamespace);
      }
      inCnt++;
    }
    return namespaceArr;
  }

  private void validateReservedNames(SchemaFileDefn lSchemaFileDefn) {
    // System.out.println("\ndebug validateReservedNames");

    boolean hasAtLeastOneElementDefined = false;
    int numClasses = 0;
    // scan LDD class titles and determine if a reserved name has been used.
    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();

      // if the value of the attribute title is a reserved word then this class definition is not
      // allowed.
      // the value of the attribute local_identifier is irrelevant even if it is a reference to a
      // foreign namespace
      if (DMDocument.reservedClassNames.contains(lDOMClass.title)) {
        Utility.registerMessage("2>error Class: "
            + " - No local dictionary may define a class called " + lDOMClass.title + ".");
      }
      Character lFirstChar = lDOMClass.title.charAt(0);
      if (!Character.isUpperCase(lFirstChar)) {
        Utility.registerMessage("2>error Class: " + " - The class " + lDOMClass.title
            + " must begin with an upper case letter.");
      }

      if (lDOMClass.isExposed) {
        hasAtLeastOneElementDefined = true;
      }
      numClasses++;
    }

    if ((!hasAtLeastOneElementDefined) && numClasses > 0) {
      Utility.registerMessage(lSchemaFileDefn.nameSpaceIdNCLC, "2>error NameSpaceId:"
          + lSchemaFileDefn.nameSpaceId
          + " - At least one class must be defined as an xs:Element. (<element_flag> set to \"true\")");
    } else {
      DMDocument.nameSpaceIdExtrnFlagArr.add(lSchemaFileDefn.nameSpaceIdNCLC);
    }

    // scan LDD attributes titles and determine if a reserved name has been used.
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();

      if (DMDocument.reservedAttrNames.contains(lDOMAttr.title)) {
        Utility.registerMessage("2>error Attribute: "
            + " - No local dictionary may define an attribute called " + lDOMAttr.title + ".");
      }

      Character lFirstChar = lDOMAttr.title.charAt(0);
      if (!Character.isLowerCase(lFirstChar)) {
        Utility.registerMessage("2>error Attribute: " + " - The attribute " + lDOMAttr.title
            + " must begin with a lower case letter.");
      }
    }

    // scan LDD rules to check that a value for reference_type has been define for the rule
    for (Iterator<RuleReferenceTypeDefn> i = ruleReferenceArr.iterator(); i.hasNext();) {
      RuleReferenceTypeDefn lRuleReferenceType = i.next();
      String lRuleXPath = lRuleReferenceType.ruleReferenceXPath;
      boolean foundReferenceTypeDef = false;
      for (Iterator<DOMRule> j = ruleArr.iterator(); j.hasNext();) {
        DOMRule lDOMRule = j.next();
        String lRuleXpathClean = cleanXPath(lDOMRule.xpath);
        if (lRuleXpathClean.compareTo(lRuleXPath) == 0) {
          for (Iterator<DOMAssert> k = lDOMRule.assertArr.iterator(); k.hasNext();) {
            DOMAssert lAssert = k.next();
            if (!lRuleReferenceType.isLocal) {
              if (lAssert.assertStmt.indexOf("pds:reference_type") > -1) {
                foundReferenceTypeDef = true;
              }
            } else if (lAssert.assertStmt.indexOf("pds:local_reference_type") > -1) {
              foundReferenceTypeDef = true;
            }
          }
        }
      }
      if (!foundReferenceTypeDef) {
        Utility.registerMessage("2>error Class: "
            + " - At least one value for pds:local_reference_type or pds:reference_type must be defined for "
            + lRuleXPath + ".");
      }
    }
    return;
  }

  private String cleanXPath(String lRuleXpath) {
    StringBuffer lInBuff = new StringBuffer(lRuleXpath);
    StringBuffer lOutBuff = new StringBuffer();
    int inCnt = 0, inLmt = lInBuff.length();
    while (inCnt < inLmt) {
      Character inChar = lInBuff.charAt(inCnt);
      if (!((inCnt < 2) && (inChar == '/'))) {
        lOutBuff.append(lInBuff.charAt(inCnt));
      }
      inCnt++;
    }
    return lOutBuff.toString();
  }

  private void validateAttributeUsed() {
    // get the LDD attributes local_identifiers
    ArrayList<String> lAttrLocalIdentifiersArr = new ArrayList<>(attrMapLocal.keySet());

    // get the ASSOC local_identifiers
    ArrayList<String> lAssocLocalIdentifiersArr = new ArrayList<>();

    for (Iterator<DOMProp> i = LDDDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      lAssocLocalIdentifiersArr.add(lDOMProp.localIdentifier);
    }

    // scan LDD attribute local_identifier and check if used.
    for (Iterator<String> i = lAttrLocalIdentifiersArr.iterator(); i.hasNext();) {
      String lAttrLocalIdentifier = i.next();
      if (lAssocLocalIdentifiersArr.contains(lAttrLocalIdentifier)) {
        continue;
      }
      DOMAttr lDOMAttr = attrMapLocal.get(lAttrLocalIdentifier);
      lDOMAttr.parentClassTitle = DMDocument.LDDToolSingletonClassTitle;
      lDOMAttr.attrParentClass = DMDocument.LDDToolSingletonDOMClass;
      lDOMAttr.classNameSpaceIdNC = "pds";
      // DMDocument.registerMessage ("2>warning Attribute: <" +
      // (attrMapLocal.get(lAttrLocalIdentifier)).title + "> - This local attribute was not used in
      // an Association.");
      Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
          + "> - This local attribute was not used in an Association.");
    }
    return;
  }

  private void validateNoDuplicateNames() {
    // get a list for names
    ArrayList<String> lNameArr = new ArrayList<>();

    // check the class names
    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (lNameArr.contains(lDOMClass.title)) {
        Utility.registerMessage("2>warning Class: <" + lDOMClass.title
            + "> - The class name is duplicated in this local data dictionary.");
      } else {
        lNameArr.add(lDOMClass.title);
      }
    }

    // check the attribute names
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      if (lNameArr.contains(lDOMAttr.title)) {
        Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
            + "> - The attribute name is duplicated in this local data dictionary.");
      } else {
        lNameArr.add(lDOMAttr.title);
      }
    }
    return;
  }

  private void validateTypeAttributes(boolean isMission) {
    // scan for attribute names containing "type"
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      int lTitleLength = lDOMAttr.title.length();
      if (((lTitleLength >= 5) && (lDOMAttr.title.indexOf("_type") == lTitleLength - 5))
          || lDOMAttr.title.compareTo("type") == 0) {
        if (lDOMAttr.domPermValueArr.size() < 1) {
          if (isMission) {
            Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
                + "> - The 'type' attribute must have at least one permissible value.");
          } else {
            // DMDocument.registerMessage ("2>error Attribute: <" + lDOMAttr.title + "> - The 'type'
            // attribute must have at least one permissible value.");
            Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
                + "> - The 'type' attribute must have at least one permissible value.");
          }
        }
      }
    }
    return;
  }

  private void validateEnumeratedFlags() {
    // scan for attributes and validate that permissible values exist if the enumeration_flag is
    // true
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      if (lDOMAttr.isEnumerated) {
        if (lDOMAttr.domPermValueArr.isEmpty()) {
          Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
              + "> - An attribute with the enumeration_flag = true must have at least one permissible value.");
        } else {
          // DMDocument.registerMessage ("2>warning Attribute: <" + lDOMAttr.title + "> - An
          // attribute with the enumeration_flag = true - NO Problem.");
        }
      } else if (lDOMAttr.domPermValueArr.isEmpty()) {
        // DMDocument.registerMessage ("2>warning Attribute: <" + lDOMAttr.title + "> - An attribute
        // with the enumeration_flag = false - NO Problem");
      } else {
        Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
            + "> - An attribute with the enumeration_flag = false must not have any permissible values.");
      }
    }
    return;
  }

  private void validateNoUnitsAttributes() {
    // scan for attribute names containing "unit", "units", "unit_of_measure", or ending with one of
    // those terms.
    boolean foundFlag = false;
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      foundFlag = false;
      int lTitleLength = lDOMAttr.title.length();
      if ((lTitleLength >= 4) && (lDOMAttr.title.indexOf("unit") == lTitleLength - 4)) {
        foundFlag = true;
      } else if ((lTitleLength >= 5) && (lDOMAttr.title.indexOf("units") == lTitleLength - 5)) {
        foundFlag = true;
      } else if ((lTitleLength >= 15)
          && (lDOMAttr.title.indexOf("unit_of_measure") == lTitleLength - 15)) {
        foundFlag = true;
      }
      if (foundFlag) {
        Utility.registerMessage("2>warning Attribute: <" + lDOMAttr.title
            + "> - The terms 'unit', 'units', 'unit_of_measure' should not be used as the rightmost part of an attribute's name.");
      }
    }
    return;
  }

  private void validateNilRequiredAttributes() {
    // Validate that each "nillable" attribute is a required attribute of at least one class

    // first find all nillable attributes in IngestLDD.
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      if (lDOMAttr.isNilable) {

        // for each nillable attribute (title) find properties with same title
        boolean foundFlag = false;
        for (Iterator<DOMProp> j = LDDDOMPropArr.iterator(); j.hasNext();) {
          DOMProp lDOMProp = j.next();
          if (lDOMAttr.title.compareTo(lDOMProp.title) == 0) {
            if (lDOMProp.cardMinI > 0) {
              foundFlag = true;
            }
          }
        }
        if (!foundFlag) {
          Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
              + "> - A 'nillable' attribute was found that is not a required attribute in at least one class.");
        }
      }
    }
    return;
  }

  private void validateNoNestedExposedClasses() {
    // first find an exposed class
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isExposed) {
        // check all component classes
        checkAllSubclasses(lClass);
      }
    }
    return;
  }

  private void checkAllSubclasses(DOMClass lClass) {
    // check all component classes
    ArrayList<DOMProp> allAssocArr = new ArrayList<>();
    allAssocArr.addAll(lClass.ownedAssocArr);
    allAssocArr.addAll(lClass.inheritedAssocArr);
    for (DOMProp lDOMProp : allAssocArr) {
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
        DOMClass lCompClass = (DOMClass) lDOMProp.hasDOMObject;
        if (lCompClass.isExposed) {
          Utility.registerMessage("2>warning Class: <" + lCompClass.title
              + "> - An exposed class was found nested within another exposed class. Nested exposed classes should only be present if there is a specific requirement to expose the additional class.");
        }
        checkAllSubclasses(lCompClass);
      }
    }
    return;
  }

  private ArrayList<Element> getAssocElemFromClassDefn(Element elem) {
    ArrayList<Element> lAssocElemArr = new ArrayList<>();
    Node assocElement = elem.getFirstChild();
    while (assocElement != null) {
      if ((assocElement.getNodeType() == Node.ELEMENT_NODE)
          && ((assocElement.getNodeName().indexOf("DD_Association") == 0)
              || (assocElement.getNodeName().indexOf("DD_Associate_External_Class") == 0))) {
        lAssocElemArr.add((Element) assocElement);
      }
      assocElement = assocElement.getNextSibling();
    }
    return lAssocElemArr;
  }

  private String getTextValue(Element ele, String tagName) {
    String textVal = "TBD_Ingest_LDD";
    NodeList nl = ele.getElementsByTagName(tagName);
    if (nl != null && nl.getLength() > 0) {
      Element el = (Element) nl.item(0);
      if (el != null) {
        Node firstChild = el.getFirstChild();
        if (firstChild != null) {
          textVal = firstChild.getNodeValue();
        }
      }
    }
    return textVal;
  }

  private ArrayList<String> getXMLValueArr(String tagName, Element elem) {
    ArrayList<String> lValArr = new ArrayList<>();
    Node lNode = elem.getFirstChild();
    while (lNode != null) {
      if ((lNode.getNodeType() == Node.ELEMENT_NODE)
          && (lNode.getNodeName().indexOf(tagName) == 0)) {
        Element lElement = (Element) lNode;
        String lVal = lElement.getFirstChild().getNodeValue();
        // System.out.println("debug getXMLValueArr - local_identifier:" + lVal);
        if (lVal != null && lVal.length() > 0) {
          lValArr.add(lVal);
        }
      }
      lNode = lNode.getNextSibling();
    }
    return lValArr;
  }

  // add the LDD artifacts to the Master
  private void addLDDtoMaster() {
    // temporary globals
    ArrayList<DOMRule> tempSchematronRuleArr;

    // save the masters
    tempSchematronRuleArr = new ArrayList<>(DOMInfoModel.masterDOMRuleMap.values());  // use unique rdfIdentifier

    // clear the masters
    DOMInfoModel.masterDOMRuleIdMap.clear();
    DOMInfoModel.masterDOMRuleMap.clear();
    DOMInfoModel.masterDOMRuleArr.clear();

    // *** Class Merge ***
    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (DOMInfoModel.masterDOMClassIdMap.containsKey(lDOMClass.identifier)) {
        // an ldd class is a duplicate of a master class; replace the master with the LDD version
        Utility.registerMessage(
            "2>warning Found duplicate class - lClass.identifier:" + lDOMClass.identifier);
        if (DOMInfoModel.masterDOMClassMap.containsKey(lDOMClass.rdfIdentifier)) {
          DOMInfoModel.masterDOMClassMap.remove(lDOMClass.rdfIdentifier);
          Utility
              .registerMessage("2>warning Found duplicate class - REPLACED - lClass.rdfIdentifier:"
                  + lDOMClass.rdfIdentifier);
        }
        DOMInfoModel.masterDOMClassMap.put(lDOMClass.rdfIdentifier, lDOMClass);
      } else {
        DOMInfoModel.masterDOMClassMap.put(lDOMClass.rdfIdentifier, lDOMClass);
      }
    }

    // build the remaining class maps and array
    DOMInfoModel.masterDOMClassIdMap.clear();
    DOMInfoModel.masterDOMClassArr.clear();
    DOMInfoModel.masterDOMClassTitleMap.clear();

    ArrayList<DOMClass> lClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassMap.values());
    for (Iterator<DOMClass> j = lClassArr.iterator(); j.hasNext();) {
      DOMClass lDOMClass = j.next();
      DOMInfoModel.masterDOMClassIdMap.put(lDOMClass.identifier, lDOMClass);
      DOMInfoModel.masterDOMClassTitleMap.put(lDOMClass.title, lDOMClass);
    }

    // build the master array (sorted by identifier)
    DOMInfoModel.masterDOMClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassIdMap.values());

    // *** Attribute Merge ***
    for (Iterator<DOMProp> i = attrArrResolved.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        if (DOMInfoModel.masterDOMAttrIdMap.containsKey(lDOMAttr.identifier)) {
          // an ldd attribute is a duplicate of a master attribute; replace the master with the LDD
          // version
          Utility.registerMessage(
              "2>warning Found duplicate attribute - lDOMAttr.identifier:" + lDOMAttr.identifier);
          if (DOMInfoModel.masterDOMAttrMap.containsKey(lDOMAttr.rdfIdentifier)) {
            DOMInfoModel.masterDOMAttrMap.remove(lDOMAttr.rdfIdentifier);
            Utility.registerMessage(
                "2>warning Found duplicate attribute - REPLACED - lDOMAttr.rdfIdentifier:"
                    + lDOMAttr.rdfIdentifier);
            Utility.registerMessage(
                "2>error Found duplicate attribute - REPLACED Failed - lDOMAttr.rdfIdentifier:"
                    + lDOMAttr.rdfIdentifier);
          }
          DOMInfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
        } else {
          DOMInfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
        }
      }
    }
    // build the remaining attribute maps and array
    DOMInfoModel.masterDOMAttrIdMap.clear();
    DOMInfoModel.masterDOMAttrArr.clear();
    ArrayList<DOMAttr> lAttrArr = new ArrayList<>(DOMInfoModel.masterDOMAttrMap.values());
    for (Iterator<DOMAttr> j = lAttrArr.iterator(); j.hasNext();) {
      DOMAttr lDOMAttr = j.next();
      DOMInfoModel.masterDOMAttrIdMap.put(lDOMAttr.identifier, lDOMAttr);
    }
    // build the master array (sorted by identifier)
    DOMInfoModel.masterDOMAttrArr = new ArrayList<>(DOMInfoModel.masterDOMAttrIdMap.values());

    // *** Property Merge ***
    for (Iterator<DOMProp> i = LDDDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (DOMInfoModel.masterDOMPropIdMap.containsKey(lDOMProp.identifier)) {
        // an ldd association is a duplicate of a master association; replace the master with the
        // LDD version
        Utility.registerMessage(
            "2>warning Found duplicate attribute - lDOMProp.identifier:" + lDOMProp.identifier);
        if (DOMInfoModel.masterDOMPropMap.containsKey(lDOMProp.rdfIdentifier)) {
          DOMInfoModel.masterDOMPropMap.remove(lDOMProp.rdfIdentifier);
          Utility.registerMessage(
              "2>warning Found duplicate attribute - REPLACED - lDOMProp.rdfIdentifier:"
                  + lDOMProp.rdfIdentifier);
        }
        DOMInfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
      } else {
        DOMInfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
      }
    }
    // build the remaining association maps and array
    DOMInfoModel.masterDOMPropIdMap.clear();
    DOMInfoModel.masterDOMPropArr.clear();
    ArrayList<DOMProp> lAssocArr = new ArrayList<>(DOMInfoModel.masterDOMPropMap.values());
    for (Iterator<DOMProp> j = lAssocArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      DOMInfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
    }
    // build the master array (sorted by identifier)
    DOMInfoModel.masterDOMPropArr = new ArrayList<>(DOMInfoModel.masterDOMPropIdMap.values());

    // display the permissible values, in the owned attributes, in the master classes.
    // for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
    // DOMClass lDOMClass = (DOMClass) i.next();
    // printDebugClass (lDOMClass, "addLDDtoMaster masterDOMClassArr");
    // }

    // copy in the LDD Schematron Rules
    for (Iterator<DOMRule> i = ruleArr.iterator(); i.hasNext();) {
      DOMRule lRule = i.next();
      DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
      DOMInfoModel.masterDOMRuleArr.add(lRule);
      DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
    }

    // merge in the master rules
    for (Iterator<DOMRule> i = tempSchematronRuleArr.iterator(); i.hasNext();) {
      DOMRule lRule = i.next();
      if (!DOMInfoModel.masterDOMRuleIdMap.containsKey(lRule.identifier)) {
        DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
        DOMInfoModel.masterDOMRuleArr.add(lRule);
        lRule.setRDFIdentifier();
        DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
      } else {
        Utility.registerMessage(
            "2>warning Found duplicate attribute - lAttr.identifier:" + lRule.identifier);
      }
    }
  }

  public void OverwriteFrom11179DataDict() {
    // iterate through the LDD attribute array
    for (Iterator<DOMProp> i = attrArrResolved.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        DOMAttr lLDDUserAttribute = lDOMAttr.lddUserAttribute;
        if ((lLDDUserAttribute == null) || (lDOMAttr.valueType.indexOf("TBD") != 0)) {
          continue;
        }
        lDOMAttr.isNilable = lLDDUserAttribute.isNilable;
        lDOMAttr.valueType = lLDDUserAttribute.valueType;
        lDOMAttr.minimum_value = lLDDUserAttribute.minimum_value;
        lDOMAttr.maximum_value = lLDDUserAttribute.maximum_value;
        lDOMAttr.minimum_characters = lLDDUserAttribute.minimum_characters;
        lDOMAttr.maximum_characters = lLDDUserAttribute.maximum_characters;
        lDOMAttr.pattern = lLDDUserAttribute.pattern;
        lDOMAttr.unit_of_measure_type = lLDDUserAttribute.unit_of_measure_type;
        lDOMAttr.default_unit_id = lLDDUserAttribute.default_unit_id;
        lDOMAttr.valArr = lLDDUserAttribute.valArr;
        lDOMAttr.permValueArr = lLDDUserAttribute.permValueArr;

        // System.out.println(" LDDDOMParser.OverwriteFrom11179DataDict - After -
        // lDOMAttr.identifier:" + lDOMAttr.identifier);
        // System.out.println(" LDDDOMParser.OverwriteFrom11179DataDict - After -
        // lDOMAttr.valueType:" + lDOMAttr.valueType);
      }
    }
  }
  
  // append the local identifier with the namespace prefix
  private String getLocalIdentifierNamespacePrefix(String lNamespaceId, String lLocalIdentifier) {
	  String newLocalIdentifier;
	  if (lLocalIdentifier.indexOf(".") > -1) {
//		  newLocalIdentifier = DOMInfoModel.replaceString(lLocalIdentifier, ".", ":");
		  newLocalIdentifier = lLocalIdentifier;
	  } else {
		  newLocalIdentifier = lNamespaceId + lLocalIdentifier;
	  }
	  return newLocalIdentifier;
  }

  private int getIntValue(Element ele, String tagName) {
    // in production application you would catch the exception
    return Integer.parseInt(getTextValue(ele, tagName));
  }

  public void writeLocalDDFiles(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    // print report

    if (DMDocument.writeDOMCount == 0) {
      printReport(lSchemaFileDefn);
    }
    DMDocument.writeDOMCount++;

    // write the default csv file (English)
    WriteDOMCSVFiles writeDOMCSVFiles = new WriteDOMCSVFiles();
    writeDOMCSVFiles.writeDOMCSVFile(classArr, lSchemaFileDefn, null);

    // write csv for other languages if necessary
    ArrayList<String> lOtherLanguageArr = getOtherLanguage(attrArr);
    if (lOtherLanguageArr != null) {
      for (Iterator<String> i = lOtherLanguageArr.iterator(); i.hasNext();) {
        String lOtherLanguage = i.next();
        writeDOMCSVFiles.writeDOMCSVFile(classArr, lSchemaFileDefn, lOtherLanguage);
      }
    }

    // print protege pont file
    if (DMDocument.PDS4MergeFlag) {
      printProtegePontFile(lSchemaFileDefn);
    }
  }

  private ArrayList<String> getOtherLanguage(ArrayList<DOMAttr> attrArr) {
    ArrayList<String> lOtherLanguageArr = new ArrayList<>();
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      ArrayList<TermEntryDefn> lTermEntryArr = new ArrayList<>(lDOMAttr.termEntryMap.values());
      for (Iterator<TermEntryDefn> j = lTermEntryArr.iterator(); j.hasNext();) {
        TermEntryDefn lTermEntry = j.next();
        String lLanguage = lTermEntry.language;
        if ((lLanguage.compareTo("Russian") != 0) || lOtherLanguageArr.contains(lLanguage)) {
          continue;
        }
        lOtherLanguageArr.add(lLanguage);
      }
    }
    if (lOtherLanguageArr.size() < 1) {
      return null;
    }
    return lOtherLanguageArr;
  }

  // print report
  private void printReport(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    String lFileName = lSchemaFileDefn.relativeFileSpecReportTXT;
    prLocalDD =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    printDocumentHeader(lSchemaFileDefn);
    printDocumentSummary();

    prLocalDD.println("\nDetailed validation messages");
    for (Iterator<String> i = lddErrorMsg.iterator(); i.hasNext();) {
      String lErrorMsg = i.next();
      prLocalDD.println(lErrorMsg);
      System.out.println(lErrorMsg);
    }

    prLocalDD.println("\nParsed Input - Header:");
    printParsedHeader(lSchemaFileDefn);

    prLocalDD.println("\nParsed Input - Attributes:");
    for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      printAttr(lDOMAttr);
    }

    prLocalDD.println("\nParsed Input - Classes:");
    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      printClass(lDOMClass);
    }

    prLocalDD.println("\nEnd of Report");
    // System.out.println("\nEnd of Run");
    prLocalDD.close();
  }

  // print document header
  public void printDocumentHeader(SchemaFileDefn lSchemaFileDefn) {
    prLocalDD.println("PDS4 Local Data Dictionary Processing Report");
    prLocalDD.println("\nConfiguration:");
    prLocalDD.println("   LDDTool Version" + "        " + DMDocument.LDDToolVersionId);
    prLocalDD.println("   LDD Version Id:" + "        " + lSchemaFileDefn.versionId);
    // prLocalDD.println(" LDD Namespace Id:" + " " + lSchemaFileDefn.identifier);
    prLocalDD.println("   LDD Label Version Id:" + "  " + lSchemaFileDefn.labelVersionId);
    prLocalDD.println("   LDD Discipline (T/F):" + "  " + lSchemaFileDefn.isDiscipline);
    prLocalDD.println("   LDD Namespace URL:" + "     " + lSchemaFileDefn.nameSpaceURL);
    prLocalDD.println("   LDD URN Prefix:" + "        " + lSchemaFileDefn.urnPrefix);
    prLocalDD.println("   Time" + "                   " + DMDocument.sTodaysDate);
    prLocalDD.println("   Common Schema" + "          " + "[" + "PDS4_"
        + DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_"
        + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd" + "]");
    prLocalDD.println("   Common Schematron" + "      " + "[" + "PDS4_"
        + DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_"
        + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch" + "]");
    prLocalDD
        .println("   IM Version Id:" + "         " + DMDocument.masterPDSSchemaFileDefn.versionId);
    prLocalDD
        .println("   IM Namespace Id:" + "       " + DMDocument.masterPDSSchemaFileDefn.identifier);
    prLocalDD.println(
        "   IM Label Version Id:" + "   " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);
    prLocalDD.println("   IM Object Model" + "        " + "[" + "UpperModel.pont" + "]");
    prLocalDD.println("   IM Data Dictionary" + "     " + "[" + "dd11179.pins" + "]");
    prLocalDD.println("   IM Glossary" + "            " + "[" + "Glossary.pins" + "]");
    prLocalDD.println("   IM Document Spec" + "       " + "[" + "DMDocument.pins" + "]");

    prLocalDD.println("\nParameters:");
    prLocalDD.println(
        "   Input File" + "             " + "[" + lSchemaFileDefn.LDDToolInputFileName + "]");
    prLocalDD.println("   PDS Processing" + "         " + DMDocument.PDSOptionalFlag);
    prLocalDD.println("   LDD Processing" + "         " + DMDocument.LDDToolFlag);
    prLocalDD.println("   Discipline LDD" + "         " + lSchemaFileDefn.isDiscipline);
    prLocalDD.println("   Mission LDD" + "            " + lSchemaFileDefn.isMission);
    // prLocalDD.println(" Write Class Elements" + " " + DMDocument.LDDClassElementFlag);
    prLocalDD.println("   Write Attr Elements" + "    " + DMDocument.LDDAttrElementFlag);
    prLocalDD.println("   Merge with Master" + "      " + DMDocument.PDS4MergeFlag);
  }

  // print document header
  public void printParsedHeader(SchemaFileDefn lSchemaFileDefn) {
    prLocalDD.println("   LDD Name" + "               " + lLDDName);
    prLocalDD.println("   LDD Version" + "            " + lLDDVersionId);
    prLocalDD.println("   Full Name" + "              " + lFullName);
    prLocalDD.println("   Steward" + "                " + lSchemaFileDefn.stewardId);
    prLocalDD.println("   Namespace Id " + "          " + lSchemaFileDefn.nameSpaceIdNC);
    prLocalDD.println("   Comment" + "                " + lComment);
    prLocalDD.println("   Last Modification Time" + " " + lLastModificationDateTime);
    prLocalDD.println("   PDS4 Merge Flag" + "        " + DMDocument.PDS4MergeFlag);
  }

  // print document header
  public void printDocumentSummary() {
    int totalClasses = classArr.size();
    int totalAttrs = attrArr.size();
    int totalAssocs = LDDDOMPropArr.size();
    int totalErrors = 0;
    int totalWarnings = 0;
    int totalInfo = 0;
    for (Iterator<String> i = lddErrorMsg.iterator(); i.hasNext();) {
      String lErrorMsg = i.next();
      if (lErrorMsg.indexOf("ERROR") > -1) {
        totalErrors++;
      } else if (lErrorMsg.indexOf("WARNING") > -1) {
        totalWarnings++;
      } else if (lErrorMsg.indexOf("INFO") > -1) {
        totalInfo++;
      }
    }
    prLocalDD.println("\nSummary:");
    prLocalDD.println("   Classes" + "                " + totalClasses);
    prLocalDD.println("   Attributes" + "             " + totalAttrs);
    prLocalDD.println("   Associations" + "           " + totalAssocs);
    prLocalDD.println("   Error messages" + "         " + totalErrors);
    prLocalDD.println("   Warning messages" + "       " + totalWarnings);
    prLocalDD.println("   Information messages" + "   " + totalInfo);
  }

  // print one attribute
  public void printAttr(DOMAttr lDOMAttr) {
    prLocalDD.println("\n   name" + "                   " + lDOMAttr.title);
    prLocalDD.println("   version" + "                " + lDOMAttr.versionIdentifierValue);
    prLocalDD.println("   value data type" + "        " + lDOMAttr.valueType);
    prLocalDD.println("   description" + "            " + lDOMAttr.definition);
    if (lDOMAttr.isNilable) {
      prLocalDD.println("   nillable" + "               " + lDOMAttr.isNilable);
    }
    if (!(lDOMAttr.minimum_value.indexOf("TBD") == 0)) {
      prLocalDD.println("   minimum value" + "          " + lDOMAttr.minimum_value);
    }
    if (!(lDOMAttr.maximum_value.indexOf("TBD") == 0)) {
      prLocalDD.println("   maximum value" + "          " + lDOMAttr.maximum_value);
    }
    if (!(lDOMAttr.minimum_characters.indexOf("TBD") == 0)) {
      prLocalDD.println("   minimum characters" + "     " + lDOMAttr.minimum_characters);
    }
    if (!(lDOMAttr.maximum_characters.indexOf("TBD") == 0)) {
      prLocalDD.println("   maximum characters" + "     " + lDOMAttr.maximum_characters);
    }
    if (!(lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
      prLocalDD.println("   unit of measure type" + "   " + lDOMAttr.unit_of_measure_type);
    }
    if (!(lDOMAttr.default_unit_id.indexOf("TBD") == 0)) {
      prLocalDD.println("   specified unit id" + "      " + lDOMAttr.default_unit_id);
    }

    if (!(lDOMAttr.permValueArr == null || lDOMAttr.permValueArr.isEmpty())) {
      PermValueDefn lPermValueDefn = lDOMAttr.permValueArr.get(0);
      if (lPermValueDefn.value.compareTo("") != 0) {
        prLocalDD.println("   permissible value - value meaning");
        for (Iterator<PermValueDefn> j = lDOMAttr.permValueArr.iterator(); j.hasNext();) {
          lPermValueDefn = j.next();
          prLocalDD.println("      " + lPermValueDefn.value + " - " + lPermValueDefn.value_meaning);
        }
      }
    }
    /*
     * if (! (attr.valArr == null || attr.valArr.isEmpty())) { String lVal = (String)
     * attr.valArr.get(0); if (lVal.compareTo("") != 0) {
     * prLocalDD.println("   permissible values <value> <value meaning>"); for (Iterator <String> j
     * = attr.valArr.iterator(); j.hasNext();) { String value = (String) j.next();
     * prLocalDD.println("      " + value + "   " + "tbd value meaning"); } } }
     */
  }

  // print one class
  public void printClass(DOMClass lDOMClass) {
    prLocalDD.println("\n   name" + "                   " + lDOMClass.title);
    prLocalDD.println("   description" + "            " + lDOMClass.definition);
    prLocalDD.println("   is abstract" + "            " + lDOMClass.isAbstract);
    prLocalDD.println("   is choice" + "              " + lDOMClass.isChoice);
    prLocalDD.println("   subclass of" + "            " + lDOMClass.subClassOfTitle);
    prLocalDD.println("\n   Associations");

    // print associations
    for (Iterator<DOMProp> i = lDOMClass.ownedAttrArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      prLocalDD.println("\n      local identifier" + "      " + lDOMProp.localIdentifier);
      prLocalDD.println("      minimum occurrences" + "   " + lDOMProp.minimumOccurrences);
      prLocalDD.println("      maximum occurrences" + "   " + lDOMProp.maximumOccurrences);
      prLocalDD.println("      reference type" + "        " + lDOMProp.referenceType);
    }

    for (Iterator<DOMProp> i = lDOMClass.ownedAssocArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      prLocalDD.println("\n      local identifier" + "      " + lDOMProp.localIdentifier);
      prLocalDD.println("      minimum occurrences" + "   " + lDOMProp.minimumOccurrences);
      prLocalDD.println("      maximum occurrences" + "   " + lDOMProp.maximumOccurrences);
      prLocalDD.println("      reference type" + "        " + lDOMProp.referenceType);
    }
  }

  private void validateParsedHeader(SchemaFileDefn lSchemaFileDefn) {
    if (lRegAuthId.compareTo(DMDocument.registrationAuthorityIdentifierValue) != 0) {
      Utility
          .registerMessage("2>error Header: " + " - Invalid Registration Authority: " + lRegAuthId);
    }
    if (lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") == 0) {
      Utility.registerMessage("2>error Header: "
          + " - Master namespace is not allowed as a local data dictionary namespace:"
          + lSchemaFileDefn.nameSpaceIdNC);
    }
  }

  // finish copying in the values of the USER attribute
  public void finishCloneOfLDDUserAttributes() {
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      if (!lDOMAttr.isAttribute || !lDOMAttr.isFromLDD || (lDOMAttr.lddUserAttribute == null)) {
        continue;
      }

      lDOMAttr.lddUserAttribute.finishCloneAttr(lDOMAttr);
    }
  }

  public void validateLDDAttributes() {
    for (DOMAttr lDOMAttr : attrArr) {
      validateAttribute(lDOMAttr);
    }
  }

  private void validateAttribute(DOMAttr lDOMAttr) {
	if (lDOMAttr.isExtendedAttribute) return;
    int numMatches = 0, maxMatches = 7;
    DOMDataType lDOMDataType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lDOMAttr.valueType);
    if (lDOMDataType == null) {
      Utility.registerMessage("2>error Attribute: <" + lDOMAttr.title
          + "> - Invalid Data Type. Data Type: " + lDOMAttr.valueType);
    } else {
      if (lDOMAttr.minimum_value.indexOf("TBD") != 0) {
        Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
            + "> - The default minimum value provided by the attribute's data type is being overridden with "
            + lDOMAttr.minimum_value);
      }
      if (lDOMAttr.maximum_value.indexOf("TBD") != 0) {
        Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
            + "> - The default maximum value provided by the attribute's data type is being overridden with "
            + lDOMAttr.maximum_value);
      }
      if (lDOMAttr.minimum_characters.indexOf("TBD") != 0) {
        Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
            + "> - The default minimum characters provided by the attribute's data type is being overridden with "
            + lDOMAttr.minimum_characters);
      }
      if (lDOMAttr.maximum_characters.indexOf("TBD") != 0) {
        Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
            + "> - The default maximum characters provided by the attribute's data type is being overridden with "
            + lDOMAttr.maximum_characters);
      }

      /*
       * if (lAttr.minimum_value.compareTo("INH") == 0) { lAttr.minimum_value =
       * lDataType.minimum_value; printLine(lLevel,
       * "  *** info - minimum_value set from data type minimum_value", lAttr.minimum_value);
       * DMDocument.registerMessage ("2>info Attribute: <" + lAttr.title +
       * "> - The default minimum_value provided by the data type is being overridden. minimum_value: "
       * + lAttr.minimum_value); } if (lAttr.maximum_value.compareTo("INH") == 0) {
       * lAttr.maximum_value = lDataType.maximum_value; printLine(lLevel,
       * "  *** info - maximum_value set from data type maximum_value", lAttr.maximum_value);
       * DMDocument.registerMessage ("2>info Attribute: <" + lAttr.title +
       * "> - The default minimum_value provided by the data type is being overridden. minimum_value: "
       * + lAttr.minimum_value); } if (lAttr.minimum_characters.compareTo("INH") == 0) {
       * lAttr.minimum_characters = lDataType.minimum_characters; printLine(lLevel,
       * "  *** info - minimum_characters set from data type minimum_characters",
       * lAttr.minimum_characters); DMDocument.registerMessage ("2>info Attribute: <" + lAttr.title
       * +
       * "> - The default minimum_value provided by the data type is being overridden. minimum_value: "
       * + lAttr.minimum_value); } if (lAttr.maximum_characters.compareTo("INH") == 0) {
       * lAttr.maximum_characters = lDataType.maximum_characters; printLine(lLevel,
       * "  *** info - maximum_characters set from data type maximum_characters",
       * lAttr.maximum_characters); DMDocument.registerMessage ("2>info Attribute: <" + lAttr.title
       * +
       * "> - The default minimum_value provided by the data type is being overridden. minimum_value: "
       * + lAttr.minimum_value); }
       */
    }
    if (!(lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
      DOMUnit lUnit = DOMInfoModel.masterDOMUnitMap.get(lDOMAttr.unit_of_measure_type);
      if (lUnit == null) {
        Utility.registerMessage("2>warning Attribute2 <: " + lDOMAttr.title
            + " - Invalid Unit of Measure Type: " + lDOMAttr.unit_of_measure_type);
      }
    }
    // get PDS4 exact match attributes
    boolean isExact = false;
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      if (numMatches >= maxMatches) {
        break;
      }
      DOMAttr lMAttr = i.next();
      // if (lMAttr.title.compareTo(lAttr.title) == 0) {
      if ((!lMAttr.isFromLDD) && lMAttr.title.compareTo(lDOMAttr.title) == 0) {
        Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
            + "> - This local attribute has a duplicate in the PDS4 data dictionary.");
        isExact = true;
        numMatches++;
      }
    }
    if (false) {
      getPartialMatches(lDOMAttr, numMatches, maxMatches);
    }
  }
  
  public void validateAssociationCardinalities(String lMinCard, String lMaxCard,
	      String lLocalIdentifier) {
	    if (lMinCard != null) {
	        try {
	        	lCardMinI = Integer.valueOf(lMinCard);
	        	lCardMin = lMinCard;
	        } catch (NumberFormatException e) {
	        	lCardMinI = 0;
	        	lCardMin = "0";
	        	Utility.registerMessage("2>error Association: " + lLocalIdentifier
	        			+ " - Minimum occurrences is invalid: " + lMinCard);
	        }
	    }

	    if (lMaxCard != null) {
	    	if ((lMaxCard.compareTo("*") == 0) || (lMaxCard.compareTo("unbounded") == 0)) {
	    		lCardMax = "*";
	    		lCardMaxI = 9999999;
	    	} else {
	    		try {
	    			lCardMaxI = Integer.valueOf(lMaxCard);
	    			lCardMax = lMaxCard;
	    		} catch (NumberFormatException e) {
	    			lCardMaxI = 0;
	    			lCardMax = "0";
	    			Utility.registerMessage("2>error Association: " + lLocalIdentifier
	    					+ " - Maximum occurrences is invalid: " + lMaxCard);
	    		}
	    	}
	    	if (lCardMaxI < lCardMinI) {
	    		Utility.registerMessage("2>error Association: " + lLocalIdentifier
	    				+ " - Maximum occurrences is less than minimum occurrences");
	    	}
	    }
}

  // print one line
  public void printLine(int lLevel, String lLeftPart, String lRightPart) {
    String lSpacing = "";
    for (int i = 0; i < lLevel; i++) {
      lSpacing += "  ";
    }
    prLocalDD.println(lSpacing + lLeftPart + ":" + lRightPart);
  }

  // get PDS4 partial match attributes
  private void getPartialMatches(DOMAttr lDOMAttr, int numMatches, int maxMatches) {

    // Search for partial matches only if there are more than one terms
    int lUSInd = lDOMAttr.title.lastIndexOf('_');
    if (lUSInd > -1) {
      // get class word
      String lClassWord = getClassWord(lDOMAttr.title);
      boolean isValue = false;
      if (lClassWord.compareTo("VALUE") == 0) {
        isValue = true;
      }
      String lDescriptor = getDescriptorWord(isValue, lDOMAttr.title);

      if (lClassWord.compareTo("VALUE") != 0) {
        // we have a non VALUE keyword, first search for the class word
        for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
          DOMAttr lMAttr = i.next();
          if (lMAttr.title.toUpperCase().compareTo(lDOMAttr.title.toUpperCase()) == 0) {
            continue;
          }
          if (numMatches >= maxMatches) {
            break;
          }
          String lTitleUpper = lMAttr.title.toUpperCase();
          if (lTitleUpper.indexOf(lClassWord) > -1) {
            if (lTitleUpper.indexOf(lDescriptor) > -1) {
              Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
                  + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <"
                  + lMAttr.title + ">");
              numMatches++;
            }
          }
        }
      } else {
        // we have a VALUE keyword, just search for the descriptor word
        for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
          DOMAttr lMAttr = i.next();
          if (lMAttr.title.toUpperCase().compareTo(lDOMAttr.title.toUpperCase()) == 0) {
            continue;
          }
          if (numMatches >= maxMatches) {
            break;
          }
          String lTitleUpper = lMAttr.title.toUpperCase();
          if (lTitleUpper.indexOf(lDescriptor) > -1) {
            Utility.registerMessage("2>info Attribute: <" + lDOMAttr.title
                + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <"
                + lMAttr.title + ">");
            numMatches++;
          }
        }
      }
    }
  }

  /**
   * get the Data Element Concept (DEC) - data element side - from the attribute class word - if the
   * data element name is a class word, return it, e.g. NAME - if the data element name ends in a
   * class word, return it, e.g. mission_NAME - if the data element name ends in an 'S', then it is
   * a COUNT. - otherwise the class word is assumed to be VALUE, return the last token, e.g.
   * emission_ANGLE, filtered through valid concept list.
   */

  public String getClassWord(String deName) {
    String deNameUpper = deName.toUpperCase();
    int offset, cind = 0, deNameUpperLen = deNameUpper.length();
    for (Iterator<String> i = classConceptSuf.iterator(); i.hasNext();) {
      // first determine if the deName is a class word
      String classWord = i.next();
      if (deNameUpper.compareTo(classWord) == 0) {
        String lClassWord = classConceptNorm.get(classWord);
        return lClassWord;
      }
      // second determine if the class word is contained in the deName
      String classWordAug = "_" + classWord;
      cind = deNameUpper.indexOf(classWordAug);
      if (cind > -1) {
        int classWordAugLen = classWordAug.length();
        offset = deNameUpperLen - classWordAugLen;
        if (offset == cind) {
          String lClassWord = classConceptNorm.get(classWord);
          return lClassWord;
        }
      }
    }
    // get the last term of the deName
    String lTerm = deNameUpper;
    int lInd = deNameUpper.lastIndexOf('_');
    if (lInd > -1) {
      lInd++;
      if (lInd <= deNameUpperLen) {
        lTerm = deNameUpper.substring(lInd, deNameUpperLen);
      } else {
        lTerm = "VALUE";
      }
    }

    // third determine if the data element name ends in an 'S'
    String lChar = deNameUpper.substring(deNameUpperLen - 1, deNameUpperLen);
    if (lChar.compareTo("S") == 0) {
      // if ( ! ((lTerm.compareTo("RADIUS") == 0) || (lTerm.compareTo("STATUS") == 0) ||
      // (lTerm.compareTo("NOTES") == 0) || (lTerm.compareTo("ADDRESS") == 0) ||
      // (lTerm.compareTo("CLASS") == 0) || (lTerm.compareTo("RIGHTS") == 0))) {
      if (!((lTerm.compareTo("RADIUS") == 0) || (lTerm.compareTo("STATUS") == 0)
          || (lTerm.compareTo("NOTES") == 0) || (lTerm.compareTo("ADDRESS") == 0)
          || (lTerm.compareTo("CLASS") == 0))) {
        String lClassWord = "COUNT";
        return lClassWord;
      }
    }
    return lTerm;
  }

  public String getDescriptorWord(boolean isValue, String deName) {
    String lDENameUpper = deName.toUpperCase();
    // System.out.println("debug getDescriptorWord lDENameUpper:" + lDENameUpper);
    String lDescriptorWord = lDENameUpper;
    String lTerm = lDENameUpper;
    int lDENameUpperLen = lDENameUpper.length();

    // if VALUE then return the last word
    int lUSInd = lDENameUpper.lastIndexOf('_');
    if (lUSInd > -1) { // if underscore found then parse
      if (isValue) { // if implicit VALUE, then return last term
        if (lUSInd <= lDENameUpperLen) {
          int lInd = lUSInd + 1;
          lTerm = lDENameUpper.substring(lInd, lDENameUpperLen);
          lDescriptorWord = lTerm;
        }
      } else { // return second to last term
        String lDENameUpper2 = lDENameUpper.substring(0, lUSInd);
        int lDENameUpperLen2 = lDENameUpper2.length();
        lDescriptorWord = lDENameUpper2;
        int lUSInd2 = lDENameUpper2.lastIndexOf('_');
        if (lUSInd2 > -1) { // if underscore found then parse
          if (lUSInd2 <= lDENameUpperLen2) {
            int lInd = lUSInd2 + 1;
            lDescriptorWord = lDENameUpper2.substring(lInd, lDENameUpperLen2);
          }
        }
      }
    }
    return lDescriptorWord;
  }

  /**
   * get the conceptual domain (CD) from the data type
   */
  public String getDataConceptFromDataType(String lValueType) {
    String lCD = DOMInfoModel.dataTypeToConceptMap.get(lValueType);
    if (lCD != null) {
      return (lCD);
    }
    return "SHORT_STRING";
  }

  private void initializeConceptArrs() {
    // Order is important especially for similar names - e.g. LOGICAL_IDENTIFIER must preceded
    // IDENTIFIER
    classConceptSuf = new ArrayList<>();
    classConceptSuf.add("COUNT");
    classConceptSuf.add("DATE");
    classConceptSuf.add("DESC");
    classConceptSuf.add("DESCRIPTION");
    classConceptSuf.add("DIRECTION");
    classConceptSuf.add("FLAG");
    classConceptSuf.add("FORMAT");
    classConceptSuf.add("GROUP");
    // classConceptSuf.add("PUID");
    classConceptSuf.add("ID");
    classConceptSuf.add("LINK");
    // classConceptSuf.add("LOCAL_IDENTIFIER");
    // classConceptSuf.add("LOGICAL_IDENTIFIER");
    classConceptSuf.add("IDENTIFIER");
    classConceptSuf.add("MASK");
    classConceptSuf.add("NAME");
    classConceptSuf.add("NOTE");
    classConceptSuf.add("NUMBER");
    classConceptSuf.add("QUATERNION");
    // classConceptSuf.add("RANGE");
    classConceptSuf.add("RATIO");
    classConceptSuf.add("REFERENCE");
    classConceptSuf.add("SEQUENCE");
    classConceptSuf.add("SET");
    classConceptSuf.add("SUMMARY");
    classConceptSuf.add("TEXT");
    classConceptSuf.add("TIME");
    classConceptSuf.add("TYPE");
    classConceptSuf.add("UNIT");
    classConceptSuf.add("VALUE");
    classConceptSuf.add("VECTOR");

    classConceptNorm = new TreeMap<>();
    classConceptNorm.put("COMMENT", "DESCRIPTION"); // not a class word in PDS3
    classConceptNorm.put("COUNT", "COUNT");
    classConceptNorm.put("DATE", "DATE_TIME");
    classConceptNorm.put("DATE_TIME", "DATE_TIME");
    classConceptNorm.put("DESC", "DESCRIPTION");
    classConceptNorm.put("DEFINITION", "DESCRIPTION");
    classConceptNorm.put("DESCRIPTION", "DESCRIPTION");
    classConceptNorm.put("DIRECTION", "DIRECTION");
    classConceptNorm.put("FLAG", "FLAG");
    classConceptNorm.put("FORMAT", "FORMAT");
    classConceptNorm.put("GROUP", "GROUP");
    // classConceptNorm.put("PUID", "PUID");
    classConceptNorm.put("ID", "ID");
    classConceptNorm.put("IDENTIFIER", "ID");
    // classConceptNorm.put("IDENTIFIER", "IDENTIFIER");
    classConceptNorm.put("LINK", "ANYURI");
    classConceptNorm.put("URL", "ANYURI");
    // classConceptNorm.put("LOCAL_IDENTIFIER", "LOCAL_IDENTIFIER");
    // classConceptNorm.put("LOGICAL_IDENTIFIER", "LOGICAL_IDENTIFIER");
    classConceptNorm.put("MASK", "MASK");
    classConceptNorm.put("NAME", "NAME");
    classConceptNorm.put("NOTE", "NOTE");
    classConceptNorm.put("NUMBER", "NUMBER");
    classConceptNorm.put("QUATERNION", "QUATERNION");
    // classConceptNorm.put("RANGE", "RANGE");
    classConceptNorm.put("RATIO", "RATIO");
    classConceptNorm.put("REFERENCE", "REFERENCE");
    classConceptNorm.put("SEQUENCE", "SEQUENCE");
    classConceptNorm.put("SET", "SET");
    classConceptNorm.put("SUMMARY", "SUMMARY");
    classConceptNorm.put("TEXT", "TEXT");
    classConceptNorm.put("TIME", "DATE_TIME");
    classConceptNorm.put("TITLE", "TITLE");
    classConceptNorm.put("TYPE", "TYPE");
    classConceptNorm.put("UNIT", "UNIT");
    classConceptNorm.put("VALUE", "VALUE");
    classConceptNorm.put("VECTOR", "VECTOR");

    validConceptArr = new ArrayList<>();
    validConceptArr.add("ADDRESS");
    validConceptArr.add("ANGLE");
    // validConceptArr.add("ANYURI");
    validConceptArr.add("ATTRIBUTE");
    validConceptArr.add("BIT");
    validConceptArr.add("CHECKSUM");
    validConceptArr.add("COLLECTION");
    validConceptArr.add("CONSTANT");
    validConceptArr.add("COSINE");
    validConceptArr.add("COUNT");
    // validConceptArr.add("COUNT");
    // validConceptArr.add("DATE");
    // validConceptArr.add("DATE");
    // validConceptArr.add("DATE_TIME");
    validConceptArr.add("DELIMITER");
    validConceptArr.add("DESC");
    validConceptArr.add("DESCRIPTION");
    validConceptArr.add("DEVIATION");
    validConceptArr.add("DIRECTION");
    validConceptArr.add("DISTANCE");
    validConceptArr.add("DOI");
    validConceptArr.add("DURATION");
    // validConceptArr.add("ENUMERATED");
    validConceptArr.add("FACTOR");
    validConceptArr.add("FLAG");
    validConceptArr.add("FORMAT");
    validConceptArr.add("GROUP");
    validConceptArr.add("HOME");
    // validConceptArr.add("ID");
    // validConceptArr.add("IDENTIFIER");
    validConceptArr.add("LATITUDE");
    validConceptArr.add("LENGTH");
    validConceptArr.add("LIST");
    // validConceptArr.add("LOCAL_IDENTIFIER");
    validConceptArr.add("LOCATION");
    validConceptArr.add("LOGICAL");
    // validConceptArr.add("LOGICAL_IDENTIFIER");
    validConceptArr.add("LONGITUDE");
    validConceptArr.add("MASK");
    validConceptArr.add("MAXIMUM");
    validConceptArr.add("MEAN");
    validConceptArr.add("MEDIAN");
    validConceptArr.add("MINIMUM");
    validConceptArr.add("NAME");
    validConceptArr.add("NOTE");
    validConceptArr.add("NUMBER");
    validConceptArr.add("OFFSET");
    validConceptArr.add("ORDER");
    validConceptArr.add("PARALLEL");
    validConceptArr.add("PASSWORD");
    validConceptArr.add("PATH");
    validConceptArr.add("PATTERN");
    // validConceptArr.add("PHYSICAL");
    validConceptArr.add("PIXEL");
    // validConceptArr.add("PUID");
    validConceptArr.add("QUATERNION");
    validConceptArr.add("RADIUS");
    // validConceptArr.add("RANGE");
    validConceptArr.add("RATIO");
    validConceptArr.add("REFERENCE");
    validConceptArr.add("RESOLUTION");
    // validConceptArr.add("RIGHTS");
    validConceptArr.add("ROLE");
    validConceptArr.add("ROTATION");
    validConceptArr.add("SCALE");
    validConceptArr.add("SEQUENCE");
    validConceptArr.add("SET");
    validConceptArr.add("SIPID");
    validConceptArr.add("SIZE");
    validConceptArr.add("STATUS");
    validConceptArr.add("SUMMARY");
    validConceptArr.add("SYNTAX");
    validConceptArr.add("TEMPERATURE");
    validConceptArr.add("TEXT");
    // validConceptArr.add("TIME");
    validConceptArr.add("TITLE");
    validConceptArr.add("TYPE");
    validConceptArr.add("TYPE");
    validConceptArr.add("UNIT");
    // validConceptArr.add("URL");
    validConceptArr.add("VALUE");
    validConceptArr.add("VECTOR");
    return;
  }

  // print protege pont file
  private void printProtegePontFile(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    // Iterate through the list and print the data
    String lFileName = lSchemaFileDefn.relativeFileSpecLDDPontMerge;
    prProtegePont =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    for (Iterator<DOMClass> i = classArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      printProtegeClassBegin(lDOMClass.title, lDOMClass.definition, lDOMClass.subClassOfTitle);
      for (Iterator<DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
          printProtegePontAttr(lDOMAttr);
        }
      }
      printProtegeClassEnd();
    }
    prProtegePont.close();
  }

  // print one protege attribute
  public void printProtegePontAttr(DOMAttr lDOMAttr) {
    if (lDOMAttr.cardMaxI <= 1) {
      prProtegePont.println("  (single-slot " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.title));
      prProtegePont.println(
          ";+       (comment \"" + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.definition) + "\")");
      String lValueType = DOMInfoModel.dataTypePDS4ProtegeMap.get(lDOMAttr.valueType);
      if (lValueType == null) {
        lValueType = "TBD_Protege_Type";
      }
      prProtegePont.println("    (type " + DOMInfoModel.escapeProtegeLocalDD(lValueType) + ")");
      prProtegePont
          .println(";+        (cardinality " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMin)
              + " " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMax) + ")");
      prProtegePont.println("    (create-accessor read-write)");
      printProtegePontAttrValues(lDOMAttr);
      prProtegePont.println("  )");

    } else {
      prProtegePont.println("  (multislot " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.title));
      prProtegePont.println(
          ";+       (comment \"" + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.definition) + "\")");
      String lValueType = DOMInfoModel.dataTypePDS4ProtegeMap.get(lDOMAttr.valueType);
      if (lValueType == null) {
        lValueType = "TBD_Protege_Type";
      }
      prProtegePont.println("    (type " + lValueType + ")");
      String lCardMax = "?VARIABLE";
      if (lDOMAttr.cardMax.compareTo("*") != 0) {
        lCardMax = lDOMAttr.cardMax;
      }
      prProtegePont
          .println(";        (cardinality " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMin)
              + " " + DOMInfoModel.escapeProtegeLocalDD(lCardMax) + ")");
      prProtegePont.println("    (create-accessor read-write)");
      printProtegePontAttrValues(lDOMAttr);
      prProtegePont.println("  )");
    }
  }

  // print attribute values
  public void printProtegePontAttrValues(DOMAttr lDOMAttr) {
    if (!lDOMAttr.valArr.isEmpty()) {
      prProtegePont.print(";+		(value ");
      for (Iterator<String> i = lDOMAttr.valArr.iterator(); i.hasNext();) {
        String lVal = i.next();
        lVal = DMDocument.replaceString(lVal, " ", "_");
        prProtegePont.print("\"" + lVal + "\" ");
      }
      prProtegePont.println(")");
    }
  }

  // print print protege class - begin
  public void printProtegeClassBegin(String lName, String lDefinition, String lSuperClass) {
    prProtegePont.println("(defclass " + DOMInfoModel.escapeProtegeLocalDD(lName) + " \""
        + DOMInfoModel.escapeProtegeLocalDD(lDefinition) + "\"");
    prProtegePont.println("  (is-a " + DOMInfoModel.escapeProtegeLocalDD(lSuperClass) + ")");
    prProtegePont.println("  (role concrete)");
  }

  // print print protege class - end
  public void printProtegeClassEnd() {
    prProtegePont.println(")");
  }

  // print a class
  public void printDebugClass(DOMClass lDOMClass, String note) {
    System.out.println(
        "\n========================================================================================");
    System.out.println("debug  dump - " + note + " - lDOMClass.identifier:" + lDOMClass.identifier);
    printDebugPropertyArr(lDOMClass.ownedAttrArr, "Owned Attr");
    printDebugPropertyArr(lDOMClass.inheritedAttrArr, "Inher Attr");
    printDebugPropertyArr(lDOMClass.ownedAssocArr, "Owned Assoc");
    printDebugPropertyArr(lDOMClass.inheritedAssocArr, "Inher Attr");
  }

  // print an property
  public void printDebugPropertyArr(ArrayList<DOMProp> lDOMPropArr, String note) {
    System.out.println(
        "\n----------------------------------------------------------------------------------------");
    System.out.println("debug  dump - " + note + " - lDOMPropArr.size()" + lDOMPropArr.size());
    for (Iterator<DOMProp> i = lDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      printDebugProperty(lDOMProp, note);
    }
  }

  // print an property
  public void printDebugProperty(DOMProp lDOMProp, String note) {
    System.out.println(
        "\n----------------------------------------------------------------------------------------");
    System.out.println("debug  dump - " + note + " - lDOMProp.identifier" + lDOMProp.identifier);
    if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
      DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
      printDebugAttribute(lDOMAttr, "Attribute");
    }
  }

  // print an attribute
  public void printDebugAttribute(DOMAttr lDOMAttr, String note) {
    System.out.println(
        "\n.........................................................................................");
    System.out.println("debug  dump - " + note + " - lDOMAttr.identifier:" + lDOMAttr.identifier);
    System.out.println("debug  dump - " + note + " - lDOMAttr.domPermValueArr.size():"
        + lDOMAttr.domPermValueArr.size());
    for (Iterator<DOMProp> j = lDOMAttr.domPermValueArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      System.out.println("debug  dump - " + note + " - lDOMProp.identifier:" + lDOMProp.identifier);
      System.out
          .println("debug  dump - " + note + " - lDOMProp.hasDOMObject :" + lDOMProp.hasDOMObject);
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
        DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
        System.out
            .println("debug  dump - " + note + " - lDOMPermValDefn.value:" + lDOMPermValDefn.value);
      }
    }
  }

  public class TermEntryQMObject {
    String className;
    String attrName;
    TermEntryDefn termEntryDefn;

    public TermEntryQMObject(String lClassName, String lAttrName, TermEntryDefn lTermEntryDefn) {
      className = lClassName;
      attrName = lAttrName;
      termEntryDefn = lTermEntryDefn;
    }
  }
}
