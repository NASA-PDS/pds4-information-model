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

/**
 * Parses an XML document - Local_DD
 */

public class LDDDOMParser extends Object
{
	// Schema File Definition
	SchemaFileDefn gSchemaFileDefn;
	
	// initialize the class structures
	TreeMap <String, DOMClass> classMap = new TreeMap <String, DOMClass> (); 	
	TreeMap <String, DOMClass> classMapLocal = new TreeMap <String, DOMClass> (); 	
	ArrayList <DOMClass> classArr = new ArrayList <DOMClass> ();	

	// initialize the attribute structures
	ArrayList <DOMAttr> attrArr = new ArrayList <DOMAttr> (); 
	TreeMap <String, DOMAttr> attrMap = new TreeMap <String, DOMAttr> (); 	
	TreeMap <String, DOMAttr> attrMapLocal = new TreeMap <String, DOMAttr> (); 	

	// initialize the resolved properties (after LDD Attr or Class has been mapped to a class)
	ArrayList <DOMProp> attrArrResolved = new ArrayList <DOMProp> (); 
	
	// initialize the Property structures
//	ArrayList <AssocDefn> LDDMOFPropArr = new ArrayList <AssocDefn> (); 	
//	ArrayList <DOMProp> LDDMOFPropArr = new ArrayList <DOMProp> (); 	
	ArrayList <DOMProp> LDDDOMPropArr = new ArrayList <DOMProp> (); 	
		
	// initialize the Rule structures
	ArrayList <DOMRule> ruleArr = new ArrayList <DOMRule> (); 	
	TreeMap <String, DOMRule> ruleMap = new TreeMap <String, DOMRule> ();

	// initialize the Reference
	ArrayList <RuleReferenceTypeDefn> ruleReferenceArr = new ArrayList <RuleReferenceTypeDefn> ();
	
	// initialize the Property Map structures
	ArrayList <PropertyMapsDefn> propertyMapsArr = new ArrayList <PropertyMapsDefn> (); 	
	TreeMap <String, PropertyMapsDefn> propertyMapsMap = new TreeMap <String, PropertyMapsDefn> ();
	
	PrintWriter prLocalDD, prProtegePont;

	// local_identifier to RDF_Identifier map 
	TreeMap <String, String> lIdToRDFId = new TreeMap <String, String> (); 	
	
	// info, warning, and error messages
	ArrayList <String> lddErrorMsg = new ArrayList <String> ();
	
	// class and descriptor structures
	ArrayList <String> classConceptSuf;
	TreeMap <String, String> classConceptNorm;
	ArrayList <String> validConceptArr;
	
	// map of local to external .titles for LDD valArr update
	TreeMap <String, String> lLDDValArrExtUpdDefnClassMap = new TreeMap <String, String> ();

	//No generics
	Document dom;
	
	String lLDDName;
	String lLDDVersionId;
	String lFullName;				
	String lLastModificationDateTime;
//	String lIdentifier;
	String lDescription;
	String lComment = "TBD_lComment";
	String lRegAuthId = DMDocument.registrationAuthorityIdentifierValue;
	
	String lCardMin;
	int lCardMinI;
	String lCardMax;
	int lCardMaxI;

	public LDDDOMParser () {
		// Order is important especially for similar names - e.g. LOGICAL_IDENTIFIER must preceded IDENTIFIER

		classConceptSuf = new ArrayList <String> ();
		classConceptSuf.add("COUNT");
		classConceptSuf.add("DATE");
		classConceptSuf.add("DESC");
		classConceptSuf.add("DESCRIPTION");
		classConceptSuf.add("DIRECTION");
		classConceptSuf.add("FLAG");
		classConceptSuf.add("FORMAT");
		classConceptSuf.add("GROUP");
//		classConceptSuf.add("PUID");
		classConceptSuf.add("ID");
		classConceptSuf.add("LINK");
//		classConceptSuf.add("LOCAL_IDENTIFIER");
//		classConceptSuf.add("LOGICAL_IDENTIFIER");
		classConceptSuf.add("IDENTIFIER");
		classConceptSuf.add("MASK");
		classConceptSuf.add("NAME");
		classConceptSuf.add("NOTE");
		classConceptSuf.add("NUMBER");
		classConceptSuf.add("QUATERNION");
//		classConceptSuf.add("RANGE");
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
		
		classConceptNorm = new TreeMap <String, String> ();
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
//		classConceptNorm.put("PUID", "PUID");
		classConceptNorm.put("ID", "ID");
		classConceptNorm.put("IDENTIFIER", "ID");
//		classConceptNorm.put("IDENTIFIER", "IDENTIFIER");
		classConceptNorm.put("LINK", "ANYURI");
		classConceptNorm.put("URL", "ANYURI");
//		classConceptNorm.put("LOCAL_IDENTIFIER", "LOCAL_IDENTIFIER");
//		classConceptNorm.put("LOGICAL_IDENTIFIER", "LOGICAL_IDENTIFIER");
		classConceptNorm.put("MASK", "MASK");
		classConceptNorm.put("NAME", "NAME");
		classConceptNorm.put("NOTE", "NOTE");
		classConceptNorm.put("NUMBER", "NUMBER");
		classConceptNorm.put("QUATERNION", "QUATERNION");
//		classConceptNorm.put("RANGE", "RANGE");
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
		
		validConceptArr = new ArrayList <String> ();
        validConceptArr.add("ADDRESS");              
        validConceptArr.add("ANGLE");              
//        validConceptArr.add("ANYURI");              
        validConceptArr.add("ATTRIBUTE");              
        validConceptArr.add("BIT");              
        validConceptArr.add("CHECKSUM");              
        validConceptArr.add("COLLECTION");              
        validConceptArr.add("CONSTANT");              
        validConceptArr.add("COSINE");              
        validConceptArr.add("COUNT");
//        validConceptArr.add("COUNT");              
//        validConceptArr.add("DATE");
//        validConceptArr.add("DATE");              
//        validConceptArr.add("DATE_TIME");              
        validConceptArr.add("DELIMITER");              
        validConceptArr.add("DESC");
        validConceptArr.add("DESCRIPTION");        
        validConceptArr.add("DEVIATION");              
        validConceptArr.add("DIRECTION");              
        validConceptArr.add("DISTANCE");              
        validConceptArr.add("DOI");              
        validConceptArr.add("DURATION");              
//        validConceptArr.add("ENUMERATED");              
        validConceptArr.add("FACTOR");              
        validConceptArr.add("FLAG");              
        validConceptArr.add("FORMAT");             
        validConceptArr.add("GROUP");
        validConceptArr.add("HOME");              
//        validConceptArr.add("ID");        
//        validConceptArr.add("IDENTIFIER");           
        validConceptArr.add("LATITUDE");              
        validConceptArr.add("LENGTH");              
        validConceptArr.add("LIST");              
//        validConceptArr.add("LOCAL_IDENTIFIER");           
        validConceptArr.add("LOCATION");              
        validConceptArr.add("LOGICAL");              
//        validConceptArr.add("LOGICAL_IDENTIFIER");             
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
//        validConceptArr.add("PHYSICAL");              
        validConceptArr.add("PIXEL");              
//        validConceptArr.add("PUID");
        validConceptArr.add("QUATERNION");
        validConceptArr.add("RADIUS");              
//        validConceptArr.add("RANGE");
        validConceptArr.add("RATIO");
        validConceptArr.add("REFERENCE");            
        validConceptArr.add("RESOLUTION");              
//        validConceptArr.add("RIGHTS");              
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
//        validConceptArr.add("TIME");
        validConceptArr.add("TITLE");              
        validConceptArr.add("TYPE");
        validConceptArr.add("TYPE");              
        validConceptArr.add("UNIT");             
//        validConceptArr.add("URL");              
        validConceptArr.add("VALUE"); 
        validConceptArr.add("VECTOR");
	}

	public void getLocalDD() throws java.io.IOException {
		//parse the xml file and get the dom object
		parseXmlFile(gSchemaFileDefn);
		
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseXmlFile() Done");
		
		parseDocument(gSchemaFileDefn);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument() Done");
	
		// validate parsed header
		validateParsedHeader(gSchemaFileDefn);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.validateParsedHeader() Done");
		
		// add the LDD artifacts to the master
		addLDDtoMaster ();
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.addLDDtoMaster() Done");
		
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD Done");
	}		

	private void parseXmlFile(SchemaFileDefn lSchemaFileDefn){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(lSchemaFileDefn.LDDToolInputFileName);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void parseDocument(SchemaFileDefn lSchemaFileDefn){
		//get the root element
		Element docEle = dom.getDocumentElement();		
		//get the basics

		// local DD attributes
		lFullName = getTextValue(docEle,"full_name");	
		lLastModificationDateTime = getTextValue(docEle,"last_modification_date_time");				
		lComment = getTextValue(docEle,"comment");
		if (lComment == null) lComment = "TBD_comment";
		
		// get namespace
		String lNameSpaceIdNC = getTextValue(docEle,"namespace_id");
		if (lNameSpaceIdNC == null) lNameSpaceIdNC = "TBD";
		
		SchemaFileDefn lConfigSchemaFileDefn = DMDocument.masterAllSchemaFileSortMap.get(lNameSpaceIdNC);
		if (lConfigSchemaFileDefn == null) {
			lConfigSchemaFileDefn = DMDocument.masterPDSSchemaFileDefn;
			System.out.println("   WARNING  Init: " + " - Config.Properties Namespace Id Not Found:" + lNameSpaceIdNC);
		} else {
			System.out.println("   INFO     Init: " + " - Config.Properties Namespace Id Found:" + lNameSpaceIdNC);
		}
		System.out.println("   INFO     Init: " + " - Config.Properties Namespace Id Using:" + lConfigSchemaFileDefn.identifier);
		
		lSchemaFileDefn.setNameSpaceIds(lNameSpaceIdNC);
		lSchemaFileDefn.setRegAuthority (lConfigSchemaFileDefn);
		
		// set namespace and governance level
		if (DMDocument.governanceLevel.compareTo("Discipline") == 0) lSchemaFileDefn.isDiscipline = true;
		else lSchemaFileDefn.isMission = true;

		if (! (lComment.indexOf("TBD") == 0)) lSchemaFileDefn.comment = lComment;
		
		lLDDName = getTextValue(docEle,"name");
		if (lLDDName == null || (lLDDName.indexOf("TBD") == 0)) {
			lLDDName = "TBD_LDD_name";
		}
		lSchemaFileDefn.lddName = lLDDName;

		
		lLDDVersionId = getTextValue(docEle,"ldd_version_id");
		if (! (lLDDVersionId == null || (lLDDVersionId.indexOf("TBD") == 0))) {
			lSchemaFileDefn.versionId = lLDDVersionId;
		}
		lSchemaFileDefn.setVersionIds();
		
		String lStewardId = getTextValue(docEle,"steward_id");
		if ( !(lStewardId == null || (lStewardId.indexOf("TBD") == 0))) {
			lStewardId = lStewardId.toLowerCase();
			lSchemaFileDefn.setStewardIds (lStewardId);
		} else {
			lSchemaFileDefn.setStewardIds ("tbd");
		}	

		lDescription = getTextValue(docEle,"comment");
		if (lDescription  == null || (lDescription.indexOf("TBD") == 0)) {
			lDescription  = "TBD_description";
		}
		
		// dump the USER attributes
//		InfoModel.dumpAttrDict();

//		get the LDD attributes
		getAttributes (lSchemaFileDefn, docEle);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.getAttributes() Done");
		
//		get the LDD classes
		getClass (lSchemaFileDefn, docEle);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.getClass() Done");

//		get the LDD rules
		getRule (lSchemaFileDefn, docEle);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.getRule() Done");
		
//		get the LDD property map
		getPropMap (docEle);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.getPropMap() Done");
				
//		get the component for the LDD association 
		resolveComponentsForAssociation (lSchemaFileDefn);
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.resolveComponentsForAssociation() Done");
		
		validateReservedNames();
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.validateReservedNames() Done");
				
		validateAttributeUsed();
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.validateAttributeUsed() Done");
		
		validateNoDuplicateNames ();
		if (DMDocument.debugFlag) System.out.println("debug parseDocument.validateNoDuplicateNames() Done");
	}
	
	private void printClassDebug (String lLable, String lIdentifier) {
		DOMClass lClass = classMap.get(lIdentifier);
		if (lClass == null) {
			System.out.println("\ndebug label:" + lLable + "  printClassDebug INVALID IDENTIFIER lIdentifier:" + lIdentifier);
			return;
		}
		System.out.println("\ndebug label:" + lLable + "  printClassDebug lClass.identifier:" + lClass.identifier);
		for (Iterator <DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
				System.out.println("debug printClassDebug lDOMAttr.identifier:" + lDOMAttr.identifier);
			}
		}
	}

	private void getAttributes (SchemaFileDefn lSchemaFileDefn, Element docEle) {			
		//get a nodelist of <DD_Attribute> elements
		NodeList nl = docEle.getElementsByTagName("DD_Attribute");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the elements
				Element el = (Element)nl.item(i);
				String lLocalIdentifier = getTextValue(el,"local_identifier");
				String lTitle = getTextValue(el,"name");
				
				// create the rdfIdentifier; at this time only the LDD local identifier is known; the class is obtained from the association processing later.
				String lAttrRdfIdentifier = DMDocument.rdfPrefix + lSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + DOMInfoModel.getNextUId();
//				System.out.println("debug getAttributes INITIAL lAttrRdfIdentifier:" + lAttrRdfIdentifier);
				DOMAttr lDOMAttr= (DOMAttr) attrMap.get(lAttrRdfIdentifier);
				if (lDOMAttr == null) {
//					lDOMAttr = new DOMAttr (lAttrRdfIdentifier);
					lDOMAttr = new DOMAttr ();
					lDOMAttr.setRDFIdentifier(lAttrRdfIdentifier);
					attrArr.add(lDOMAttr);
					attrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
					lDOMAttr.lddLocalIdentifier = lLocalIdentifier;
					attrMapLocal.put(lDOMAttr.lddLocalIdentifier, lDOMAttr);
					lDOMAttr.isPDS4 = true;
					lDOMAttr.isFromLDD = true;
					lDOMAttr.versionIdentifierValue = getTextValue(docEle,"version_id");				
					lDOMAttr.title = lTitle;

					// at this point lAttr.className is defaulted to the USER class, however it is updated later in association processing if it is owned by a class.
//					lAttr.setIdentifier ("pds", "USER", localDDSchemaFileDefn.nameSpaceIdNC, lAttr.title);
					lDOMAttr.setIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lSchemaFileDefn.nameSpaceIdNC, lDOMAttr.title);
					
					lDOMAttr.XMLSchemaName = lDOMAttr.title;
					lDOMAttr.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lDOMAttr.classNameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lDOMAttr.nameSpaceId = lDOMAttr.nameSpaceIdNC + ":";						
					lDOMAttr.steward = lSchemaFileDefn.stewardId;
					lDOMAttr.submitter = getTextValue(docEle,"submitter_name");
					String lDescription = getTextValue(el,"definition");
//					lDescription = lDescription.replaceAll("\\s+"," ");
					lDescription = DOMInfoModel.cleanCharString(lDescription);
					lDOMAttr.definition = lDescription;
					lDOMAttr.regAuthId = lRegAuthId;
					String lNillableFlag = getTextValue(el,"nillable_flag");
					if ((lNillableFlag.compareTo("true") == 0) || (lNillableFlag.compareTo("1") == 0)) lDOMAttr.isNilable = true;
//					lAttr.isUsedInClass = true;
					lDOMAttr.propType = "ATTRIBUTE";
					lDOMAttr.isAttribute = true;
					
					// get the value domain
					getValueDomain (lDOMAttr, el);
					
					// get the terminological entry
					getTermEntry (lDOMAttr, el);
					
					// *** Not sure that this code is doing anything - check out ***
					// check if attribute already exists
					String lid = DMDocument.registrationAuthorityIdentifierValue + lLocalIdentifier;
//					System.out.println("debug getAttributes -External Attribute-  lid:" + lid);
					DOMAttr lExternAttr = DOMInfoModel.masterDOMAttrIdMap.get(lid);
					if (lExternAttr != null) {
						System.out.println("debug getAttributes -External Attribute - SETTING VALUES-  lExternAttr.identifier:" + lExternAttr.identifier);
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
		}
	}	
	
	private void getValueDomain (DOMAttr lDOMAttr, Element docEle) {	
		String lVal;
		//get a nodelist of <DD_Value_Domain> elements
		NodeList nl = docEle.getElementsByTagName("DD_Value_Domain");
//		if(nl == null || nl.getLength() < 1) {
//			nl = docEle.getElementsByTagName("DD_Value_Domain2");
//		}
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the elements
				Element el = (Element)nl.item(i);
				lVal = getTextValue(el,"enumeration_flag");
				if (lVal != null) {
					if ((lVal.compareTo("1") == 0) || (lVal.compareTo("true") == 0)) {
						lDOMAttr.isEnumerated = true; 
					}
				}
				lVal = getTextValue(el,"minimum_characters");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_minimum_characters";
					}
					lDOMAttr.minimum_characters = lVal;
				}
				lVal = getTextValue(el,"maximum_characters");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_maximum_characters";
					}
					lDOMAttr.maximum_characters = lVal;
				}
				lVal = getTextValue(el,"minimum_value");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_minimum_value";
					}
					lDOMAttr.minimum_value = lVal;
				}
				lVal = getTextValue(el,"maximum_value");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_maximum_value";
					}
					lDOMAttr.maximum_value = lVal;
				}
				
				lVal = getTextValue(el,"unit_of_measure_type");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0) || (lVal.compareTo("Units_of_None") == 0)) {
						lVal = "TBD_unit_of_measure_type";
					}
					lDOMAttr.unit_of_measure_type = lVal;
				}				
				lVal = getTextValue(el,"value_data_type");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_value_data_type";
					}
					lDOMAttr.valueType = lVal;
					lDOMAttr.dataConcept = getDataConceptFromDataType (lVal);
				}
				lVal = getTextValue(el,"pattern");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_pattern";
					}
					lDOMAttr.pattern = lVal;
				}
				getPermissibleValues (lDOMAttr, el);
			}
		}
	}
	
	private void getTermEntry (Object lObject, Element docEle) {	
		String lVal;
		//get a nodelist of <Terminological_Entry> elements
		NodeList nl = docEle.getElementsByTagName("Terminological_Entry");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the terminological entry
				Element el = (Element)nl.item(i);
				
				lVal = getTextValue(el,"language");
				if (lVal != null) {
					TermEntryDefn lTermEntry = new TermEntryDefn ();
					lTermEntry.language = lVal;

// 3333 - AttrDefn
					if (lObject.getClass().getName().compareTo("AttrDefn") == 0) {
						DOMAttr lAttr = (DOMAttr) lObject;
						lAttr.termEntryMap.put(lTermEntry.language, lTermEntry);
					} else {
						DOMClass lClass = (DOMClass) lObject;
						lClass.termEntryMap.put(lTermEntry.language, lTermEntry);
					}
					
					lVal = getTextValue(el,"name");
					if (lVal != null) lTermEntry.name = lVal;
					
					lVal = getTextValue(el,"definition");
					if (lVal != null) {
//						lVal = lVal.replaceAll("\\s+"," ");
						lVal = DOMInfoModel.cleanCharString(lVal);
						lTermEntry.definition = lVal;
					}
					lVal = getTextValue(el,"preferred_flag");
					if (lVal != null && (lVal.compareTo("1") == 0) || (lVal.compareTo("true") == 0)) lTermEntry.isPreferred = true;
				} else {
					lddErrorMsg.add("   ERROR    Terminological Entry: " + "The <language> attribute is missing.");
				}
			}
		}
	}
	
	private void getPermissibleValues (DOMAttr lDOMAttr, Element docEle) {	
		//get a nodelist of <DD_Permissible_Value> elements
		NodeList nl = docEle.getElementsByTagName("DD_Permissible_Value");
		if(nl == null || nl.getLength() < 1) {
			nl = docEle.getElementsByTagName("DD_Permissible_Value2");
		}
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {			
				//get the elements
				Element el = (Element)nl.item(i);
				String lValue = getTextValue(el,"value");
				if (lValue == null) {
					lValue = "TBD_value";
				} else {
					lDOMAttr.valArr.add(lValue);
				}
				String lValueMeaning = getTextValue(el,"value_meaning");
				if (lValueMeaning == null) {
					lValue = "TBD_value_meaning";
				}
				lValueMeaning= DOMInfoModel.cleanCharString (lValueMeaning);
				
				String lValueBeginDate = getTextValue(el,"value_begin_date");
				String lValueEndDate = getTextValue(el,"value_end_date");

				DOMPermValDefn lDOMPermVal = new DOMPermValDefn (lValue, lValue, lValueMeaning);
				if (lValueBeginDate != null) {
					lDOMPermVal.value_begin_date = lValueBeginDate;
				}
				if (lValueEndDate != null) {
					lDOMPermVal.value_end_date = lValueEndDate;
				}
	            DOMProp lDOMProp = new DOMProp ();
	            lDOMProp.initDOMPermValProp (lDOMPermVal);
				lDOMAttr.domPermValueArr.add(lDOMProp);				
			}
		}
	}
		
	private void getClass (SchemaFileDefn lSchemaFileDefn, Element docEle) {	
		//get a nodelist of <DD_Class> elements
		NodeList n2 = docEle.getElementsByTagName("DD_Class");
		if(n2 != null && n2.getLength() > 0) {
			for(int i = 0 ; i < n2.getLength();i++) {
				//get the elements
				Element el = (Element)n2.item(i);
				String lTitle = getTextValue(el,"name");	
				DOMClass lDOMClass = new DOMClass ();
				lDOMClass.setRDFIdentifier(lTitle);
				if ((DOMClass) classMap.get(lDOMClass.rdfIdentifier) == null) {
					lDOMClass.setIdentifier(lSchemaFileDefn.nameSpaceIdNC, lTitle);
					lDOMClass.title = lTitle;		
					lDOMClass.versionId = DMDocument.classVersionIdDefault;
					classMap.put(lDOMClass.identifier, lDOMClass);
					classArr.add(lDOMClass);					
					lDOMClass.isFromLDD = true;
					lDOMClass.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lDOMClass.nameSpaceId = lSchemaFileDefn.nameSpaceId;					
					lDOMClass.subModelId = "UpperModel";  // *** this was added to allow the IM Spec to be written					
					lDOMClass.steward = lSchemaFileDefn.stewardId;

					// get disposition
//					if (! lDOMClass.getDOMClassDisposition(false)) {
//						System.out.println("debug getClass getLDDClassDisposition FAILED lClass.identifier:" + lDOMClass.identifier);
//					}

					String lDescription = getTextValue(el,"definition");
					lDOMClass.definition = DOMInfoModel.cleanCharString(lDescription);
					lDOMClass.regAuthId = lRegAuthId;
					// subClassOF is temporary until true subClassOf is found in main code
					lDOMClass.subClassOfTitle = DMDocument.masterUserClassName;
					lDOMClass.subClassOfIdentifier = DOMInfoModel.getClassIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName);
					lDOMClass.localIdentifier = getTextValue(el,"local_identifier");
					
					String lBooleanStringValue = getTextValue(el,"abstract_flag");
					lDOMClass.isAbstract = false;
					if (lBooleanStringValue != null) {
						if (lBooleanStringValue.compareTo("true") == 0) {
							lDOMClass.isAbstract = true;
						}
					}
					
					lBooleanStringValue = getTextValue(el,"element_flag");
					if (lBooleanStringValue != null) {
						if (lBooleanStringValue.compareTo("true") == 0) {
							lDOMClass.isExposed = true;
						}
					}
					
					classMapLocal.put(lDOMClass.localIdentifier, lDOMClass);
					
					// get the terminological entry
					getTermEntry (lDOMClass, el);

					// reset class order
					DOMInfoModel.resetClassOrder();
					
					// get associations for the respective attributes
					getAssociations(lDOMClass, el);
				}
			}
		}
	}	

	private void getAssociations (DOMClass lDOMClass, Element ele) {		
		// start processing
		ArrayList <Element> lAssocElemArr = getAssocElemFromClassDefn (ele);
		for (Iterator <Element> i = lAssocElemArr.iterator(); i.hasNext();) {
			Element lAssocElem = (Element) i.next();
			
			// initialize variables
			boolean lIsAttribute = false;
			boolean lIsChoice = false;
			boolean lIsAny = false;
			String lReferenceType = "TBD_referenceType_LDD";
			String lLocalIdentifier = "TBD_localIdentifier_LDD";
			String lMaximumOccurrences = "TBD_maximumOccurrences_LDD";	
			String lMinimumOccurrences = "TBD_minimumOccurrences_LDD";
			ArrayList <String> lLocalIdentifierArr;
			
			// get DD_Associations
			// *** this test can be removed --- it is redundant
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
				lReferenceType = getTextValue(lAssocElem,"reference_type");;
				lMaximumOccurrences = getTextValue(lAssocElem,"maximum_occurrences");	
				lMinimumOccurrences = getTextValue(lAssocElem,"minimum_occurrences");
				
				// get all of the identifiers
				lLocalIdentifierArr =  getXMLValueArr ("identifier_reference", lAssocElem);
				if (lLocalIdentifierArr.size() == 0) {
					lLocalIdentifierArr =  getXMLValueArr ("local_identifier", lAssocElem);
					if (lLocalIdentifierArr.size() == 0) {
						lddErrorMsg.add("   ERROR    Association - Reference_Type: " + lReferenceType + " - No identifiers were provided for this association.");
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
						lddErrorMsg.add("   ERROR    Association: " + lLocalIdentifier + " -  The reference_type 'parent_of' is allowed only one parent");
						continue;
					}
				} else {
					lddErrorMsg.add("   ERROR    Association: " + lLocalIdentifier + " - Invalid reference type: " + lReferenceType);
					continue;
				}
				
				// save the Internal_Reference or Local_Internal_Reference for rule checking
				if (lLocalIdentifier.compareTo("pds.Internal_Reference") == 0 ) {
					String lRuleReferenceXPath = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/" + DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Internal_Reference";
					RuleReferenceTypeDefn lRuleReferenceTypeDefn = new RuleReferenceTypeDefn (lRuleReferenceXPath, false);
					ruleReferenceArr.add(lRuleReferenceTypeDefn);
				} else if (lLocalIdentifier.compareTo("pds.Local_Internal_Reference") == 0) {
					String lRuleReferenceXPath = gSchemaFileDefn.nameSpaceId + lDOMClass.title + "/" + DMDocument.masterPDSSchemaFileDefn.nameSpaceId + "Local_Internal_Reference";
					RuleReferenceTypeDefn lRuleReferenceTypeDefn = new RuleReferenceTypeDefn (lRuleReferenceXPath, true);
					ruleReferenceArr.add(lRuleReferenceTypeDefn);
				}

				validateAssociationCardinalities (lMinimumOccurrences, lMaximumOccurrences, lLocalIdentifier);
				
				// iterate through the local identifiers for this DD association and set up a property for each
				for (Iterator <String> j = lLocalIdentifierArr.iterator(); j.hasNext();) {
					lLocalIdentifier = (String) j.next();

					// create new association -- Note that lProperty.identifier will not be set until the associated attribute is located in resolveComponentsForAssociation
					DOMProp lDOMProp = new DOMProp ();
					
					// test for choice and any
					if (lLocalIdentifier.indexOf("XSChoice#") == 0) {
						isGroupDelimter = true;
						isGroupContent = false;
						lIsChoice = true;
						lGroupName = lLocalIdentifier + DOMInfoModel.getNextGroupNum();  // i.e., XSChoice#26						
					}
					if (lLocalIdentifier.compareTo("XSAny#") == 0) {
						isGroupDelimter = true;
						isGroupContent = false;
						lIsAny = true;
						lGroupName = lLocalIdentifier + DOMInfoModel.getNextGroupNum();
					}
					
					// get common attributes
					lDOMProp.isAttribute = lIsAttribute;
					lDOMProp.localIdentifier = lLocalIdentifier;
//					lProperty.localIdentifierArr = lLocalIdentifierArr;
					lDOMProp.referenceType = lReferenceType;
					lDOMProp.rdfIdentifier = DMDocument.rdfPrefix + lDOMClass.nameSpaceIdNC + "." + lDOMClass.title + "." + lDOMProp.localIdentifier + "." + lDOMProp.referenceType + "." + DOMInfoModel.getNextUId();
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
					if (! isGroupDelimter) {	
					
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
			}
		}
		return;
	}	
		
	private void getRule (SchemaFileDefn lSchemaFileDefn, Element docEle) {	
		String lValue = "";
		ArrayList <String> lValueArr = new ArrayList <String> ();

		//get a nodelist of <DD_Class> elements
		NodeList n2 = docEle.getElementsByTagName("DD_Rule");
		if(n2 != null && n2.getLength() > 0) {
			for(int i = 0 ; i < n2.getLength();i++) {
				//get the elements
				Element el = (Element)n2.item(i);
				lValue = getTextValue(el,"local_identifier");	
				String lLocalIdentifier = "TBD_lLocalIdentifier";	
				if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
					lLocalIdentifier = lValue;
				}				
				DOMRule lDOMRule = new DOMRule (lLocalIdentifier);	
				lDOMRule.setRDFIdentifier();	

				if ((DOMRule) ruleMap.get(lDOMRule.rdfIdentifier) == null) {
					ruleMap.put(lDOMRule.rdfIdentifier, lDOMRule);
					ruleArr.add(lDOMRule);					
					lDOMRule.attrNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
					lDOMRule.classNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
					lDOMRule.classSteward = lSchemaFileDefn.stewardId;
					lValue = getTextValue(el,"rule_context");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lDOMRule.xpath = lValue;
					}
					
					// get the let assign values
					lValueArr = getXMLValueArr ("rule_assign", el);
					if (! (lValueArr == null || lValueArr.isEmpty())) {
						lDOMRule.letAssignArr = lValueArr;
					}
					
					// get the rule statements
					ArrayList <Element> lElementStmtArr = getElement ("DD_Rule_Statement", el);
					for (Iterator <Element> j = lElementStmtArr.iterator(); j.hasNext();) {
						Element lElement = (Element) j.next();
						
						DOMAssert lDOMAssertDefn = new DOMAssert ("Rule");
						lDOMRule.assertArr.add(lDOMAssertDefn);
						
						lValue = getTextValue(lElement,"rule_type");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							if (lValue.compareTo("Assert") == 0) lDOMAssertDefn.assertType = "RAW";
							else if (lValue.compareTo("Assert Every") == 0) lDOMAssertDefn.assertType = "EVERY";
							else if (lValue.compareTo("Assert If") == 0) lDOMAssertDefn.assertType = "IF";
							else if (lValue.compareTo("Report") == 0) lDOMAssertDefn.assertType = "REPORT";
						}
//						System.out.println("debug getRule lAssertDefn.assertType:" + lAssertDefn.assertType);
						
						lValue = getTextValue(lElement,"rule_test");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lDOMAssertDefn.assertStmt = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.assertStmt:" + lAssertDefn.assertStmt);
						
						lValue = getTextValue(lElement,"rule_message");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lDOMAssertDefn.assertMsg = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.assertMsg:" + lAssertDefn.assertMsg);

						lValue = getTextValue(lElement,"rule_description");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lDOMAssertDefn.specMesg = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.specMesg:" + lAssertDefn.specMesg);
							
						// get the statement values
						lValueArr = getXMLValueArr ("rule_value", lElement);
						if (! (lValueArr == null || lValueArr.isEmpty())) {
							lDOMAssertDefn.testValArr = lValueArr;
						}
					}
//					System.out.println("debug getRule lRule.assertArr.size():" + lRule.assertArr.size());
				}
			}
		}
	}
	
	private void getPropMap (Element docEle) {
		//get a nodelist of <Property Maps> elements
		NodeList n2 = docEle.getElementsByTagName("Property_Maps");
		if(n2 != null && n2.getLength() > 0) {
			for(int i = 0 ; i < n2.getLength();i++) {
				//get the elements
				Element lPropMapsElem = (Element)n2.item(i);
				String lValue = getTextValue(lPropMapsElem,"identifier");	
				String lIdentifier = "TBD_lIdentifier";	
				if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
					lIdentifier = lValue;
				}
				
				PropertyMapsDefn lPropertyMaps = new PropertyMapsDefn (lIdentifier);
				lPropertyMaps.rdfIdentifier = DOMInfoModel.getPropMapRDFIdentifier (lIdentifier);
				PropertyMapsDefn lPropertyMaps2 = (PropertyMapsDefn) propertyMapsMap.get(lPropertyMaps.rdfIdentifier);
				if (lPropertyMaps2 == null) {
					propertyMapsMap.put(lPropertyMaps.rdfIdentifier, lPropertyMaps);
					propertyMapsArr.add(lPropertyMaps);					
					
					// get the title
					lValue = getTextValue(lPropMapsElem,"title");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.title = lValue;
					}

					// get the namespace_id
					lValue = getTextValue(lPropMapsElem,"namespace_id");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.namespace_id = lValue;
					}
					
					// get the description
					lValue = getTextValue(lPropMapsElem,"description");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.description = DOMInfoModel.cleanCharString (lValue);
					}
					
					// get the external_property_map_id
					lValue = getTextValue(lPropMapsElem,"external_property_map_id");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.external_property_map_id = lValue;
					}
										
					// get the <Property Map> in this <Property Maps>
					ArrayList <Element> lElementStmtArr = getElement ("Property_Map", lPropMapsElem);
					for (Iterator <Element> j = lElementStmtArr.iterator(); j.hasNext();) {
						Element lPropMapElem = (Element) j.next();
						
						// get the identifier
						lValue = getTextValue(lPropMapElem,"identifier");
						if (lValue == null || (lValue.indexOf("TBD") == 0)) {
							lValue = "TBD_identifier";
						}
						
						// get new property map
						PropertyMapDefn lPropertyMap = new PropertyMapDefn (lValue);
						lPropertyMaps.propertyMapArr.add(lPropertyMap);
						
						// get the title
						lValue = getTextValue(lPropMapElem,"title");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.title = lValue;
						}
						
						// get the external namespace_id
						lValue = getTextValue(lPropMapElem,"external_namespace_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.external_namespace_id = lValue;
						}
						
						// get the model_object_id
						lValue = getTextValue(lPropMapElem,"model_object_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.model_object_id = lValue;
						}
						
						// get the model_object_type
						lValue = getTextValue(lPropMapElem,"model_object_type");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.model_object_type = lValue;
						}
						
						// get the instance_id
						lValue = getTextValue(lPropMapElem,"instance_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.instance_id = lValue;
						}
						
						// get the description
						lValue = getTextValue(lPropMapElem,"description");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.description = DOMInfoModel.cleanCharString (lValue);
						}
					
						// get the property map entries
						ArrayList <Element> lElementStmtArr2 = getElement ("Property_Map_Entry", lPropMapElem);
						for (Iterator <Element> k = lElementStmtArr2.iterator(); k.hasNext();) {
							Element lPropMapEntryElem = (Element) k.next();
							
							// get new property map
							PropertyMapEntryDefn lPropertyMapEntry = new PropertyMapEntryDefn ("Property_Map_Entry");
							lPropertyMap.propertyMapEntryArr.add(lPropertyMapEntry);
							
							// get the property_name
							lValue = getTextValue(lPropMapEntryElem,"property_name");
							if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
								lPropertyMapEntry.property_name = lValue;
							}

							// get the property_value
							lValue = getTextValue(lPropMapEntryElem,"property_value");
							if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
								lPropertyMapEntry.property_value = DOMInfoModel.cleanCharString (lValue);
							}
						}
					}
				}
			}
		}

		// copy this LDDs property maps into the Master
    	Set <String> set1 = propertyMapsMap.keySet();
    	Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			PropertyMapsDefn lPropertyMapsDefn = propertyMapsMap.get(lId);
			DOMInfoModel.masterPropertyMapsMap.put(lId, lPropertyMapsDefn);
		}
		for (Iterator <PropertyMapsDefn> i = propertyMapsArr.iterator(); i.hasNext();) {
			PropertyMapsDefn lPropertyMapsDefn = (PropertyMapsDefn) i.next();
			DOMInfoModel.masterPropertyMapsArr.add(lPropertyMapsDefn);
		}
	}
	
	private ArrayList <Element> getElement (String elemName, Element elem) {
		ArrayList <Element> lElemArr = new ArrayList <Element> ();
		Node assocElement = elem.getFirstChild();
		while (assocElement != null)
		{
			if ((assocElement.getNodeType() == Node.ELEMENT_NODE) && (assocElement.getNodeName().indexOf(elemName) == 0)) {
				lElemArr.add((Element)assocElement);
			}
			assocElement = assocElement.getNextSibling();
		}
		return lElemArr;
	}	
	
	// resolve all class associations
	private void resolveComponentsForAssociation (SchemaFileDefn lSchemaFileDefn) {
		// Structures to capture component classes for a choice block
		ArrayList <String> lBlockCompRDFIdArr = new ArrayList <String> ();
		ArrayList <DOMProp> lBlockCompClassArr = new ArrayList <DOMProp> ();
		TreeMap <String, DOMProp> lBlockCompClassMap = new TreeMap <String, DOMProp> ();
		
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			
			// for each association in a class, get the attribute (AttrDefn) or the association (AttrDefn with class titles as values)
			boolean isChoiceOrAny = false;
			boolean isChoiceOrAnyDelimiter = false;
			ArrayList <DOMClass> lChildClassArr = new ArrayList <DOMClass> ();
			
			// first resolve attribute
			for (Iterator <DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.localIdentifier.indexOf("XSChoice#") == 0) continue; 
				if (lDOMProp.localIdentifier.compareTo("XSAny#") == 0) continue;
				
				DOMAttr lDOMAttr =  getLocalOrExternAttr (lSchemaFileDefn, lDOMClass, lDOMProp);
				if (lDOMAttr != null) {

					lDOMAttr.cardMinI = lDOMProp.cardMinI;
					lDOMAttr.cardMin = lDOMProp.cardMin;
					lDOMAttr.cardMaxI = lDOMProp.cardMaxI;
					lDOMAttr.cardMax = lDOMProp.cardMax;
					lDOMAttr.isChoice = lDOMProp.isChoice;
					lDOMAttr.isAny = lDOMProp.isAny;
					lDOMAttr.groupName = lDOMProp.groupName;
					lDOMProp.nameSpaceIdNC = lDOMAttr.nameSpaceIdNC;
					lDOMProp.title = lDOMAttr.title;
					lDOMProp.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lDOMAttr.nameSpaceIdNC, lDOMAttr.title);
					lDOMProp.nameSpaceIdNC = lDOMAttr.nameSpaceIdNC;
					lDOMProp.title = lDOMAttr.title;
					lDOMProp.parentClassTitle = lDOMClass.title;
					lDOMProp.attrParentClass = lDOMClass;
					lDOMAttr.isUsedInClass = true;
					
					// add the attribute to the property
					lDOMProp.hasDOMObject = lDOMAttr;
						
					// add the association (AttrDefn) to the resolved attribute array
					attrArrResolved.add(lDOMProp);
					
					// fixup Class
					// deprecate			lDOMClass.ownedAttrArr.add(lAttr);
					lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMAttr.nsTitle);
				} else {
					lddErrorMsg.add("   ERROR    Association: " + lDOMProp.localIdentifier + " - Could not find referenced attribute - Reference Type: " + lDOMProp.referenceType);
				}
			}
			for (Iterator <DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				
					// resolve an association/class
					if (lDOMProp.referenceType.compareTo("component_of") == 0) {
						
						// get the associated local or external class
						DOMClass lDOMClassComponent = getLocalOrExtrnCompClass (lSchemaFileDefn, lDOMClass, lDOMProp);
						if (lDOMClassComponent != null) {
														
							// are the following 2 statements useful?
							lDOMClass.isChoice = lDOMProp.isChoice;
							lDOMClass.isAny = lDOMProp.isAny;
							isChoiceOrAny = lDOMProp.isChoice || lDOMProp.isAny;
							isChoiceOrAnyDelimiter = (lDOMProp.localIdentifier.indexOf("XSChoice#") == 0) || (lDOMProp.localIdentifier.compareTo("XSAny#") == 0);
												
							lDOMProp.nameSpaceIdNC = lDOMClassComponent.nameSpaceIdNC;
							lDOMProp.title = lDOMClassComponent.title;
//							lDOMProp.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lDOMProp.nameSpaceIdNC, lDOMProp.title + "_" + lDOMClassComponent.title);
							lDOMProp.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lDOMClassComponent.nameSpaceIdNC, lDOMClassComponent.title);
							lDOMProp.parentClassTitle = lDOMClass.title;
							lDOMProp.attrParentClass = lDOMClass;
//							lAssoc.isChoice = lProperty.isChoice;
//							lAssoc.isAny = lProperty.isAny;
//							lAssoc.groupName = lProperty.groupName;
//							lAssoc.isOwnedAttribute = true;
//							lAssoc.cardMax = lProperty.cardMax;
//							lAssoc.cardMaxI = lProperty.cardMaxI;
//							lAssoc.cardMin = lProperty.cardMin;
//							lAssoc.cardMinI = lProperty.cardMinI;
																					
							// add the associated class
//							if (! isChoiceOrAny) {
							lDOMProp.valArr.add(lDOMClassComponent.title);
							lDOMProp.hasDOMObject = lDOMClassComponent;
							
							// get all block headers and components
							if (lDOMProp.groupName.indexOf("XSChoice#") == 0 || lDOMProp.groupName.indexOf("XSAny#") == 0) {
								if (! lBlockCompRDFIdArr.contains(lDOMProp.rdfIdentifier)) {
									lBlockCompRDFIdArr.add(lDOMProp.rdfIdentifier);
									lBlockCompClassArr.add(lDOMProp);
								}
							}
							
							// add the association (AttrDefn) to the resolved attribute array
							attrArrResolved.add(lDOMProp);

							// following needed to fixup choice and any assoc.valArr(AttrDefn) after all class components are found
							if (isChoiceOrAny && ! isChoiceOrAnyDelimiter) {
								if (! lChildClassArr.contains(lDOMClassComponent)) {
									lChildClassArr.add(lDOMClassComponent);
								}
							}
							
							// fixup the class associations (AttrDefn)
							if (! isChoiceOrAny) {
//								lDOMClass.ownedAssociation.add(lAssoc);
								lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMProp.nsTitle);
							} else if (isChoiceOrAnyDelimiter) {
//								lDOMClass.ownedAssociation.add(lAssoc);
								lDOMClass.ownedAttrAssocNSTitleArr.add(lDOMProp.nsTitle);
							}
						} else lddErrorMsg.add("   ERROR    Association: " + lDOMProp.localIdentifier + " - Missing Component - Reference Type: " + lDOMProp.referenceType);

					
					} else if (lDOMProp.referenceType.compareTo("parent_of") == 0) {
						// add the referenced (LDD) class as the parent of (base_of) this (LDD) Class (lClass)
						DOMClass lDOMParentClass = getLocalOrExtrnParentClass(lDOMClass, lDOMProp);
						if (lDOMParentClass != null) {
							lDOMClass.subClassOfTitle = lDOMParentClass.title;
							lDOMClass.subClassOfIdentifier = lDOMParentClass.identifier;
							lDOMClass.subClassOf = lDOMParentClass;
						} else {
							lddErrorMsg.add("   ERROR    Association: " + lDOMProp.identifier + " - Missing Class: " + lDOMProp.localIdentifier + " - Reference Type: " + lDOMProp.referenceType);	
						}
					}
				}
			}
		
			
		// Fixup the choice block component classes.
		// first get the block headers.
		for (Iterator <DOMProp> j = lBlockCompClassArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp= (DOMProp) j.next();
			if (lDOMProp.title.indexOf("XSChoice#") == 0 || lDOMProp.title.indexOf("XSAny#") == 0) {
				lBlockCompClassMap.put(lDOMProp.groupName, lDOMProp);
			}
		}
			
		// second, get the block members 
		ArrayList <DOMProp> lBlockHeaderArr = new ArrayList <DOMProp> (lBlockCompClassMap.values());
		for (Iterator <DOMProp> j = lBlockCompClassArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			if (lDOMProp.title.indexOf("XSChoice#") == 0 || lDOMProp.title.indexOf("XSAny#") == 0) continue;
			for (Iterator <DOMProp> k = lBlockHeaderArr.iterator(); k.hasNext();) {
				DOMProp lBlockHeader = (DOMProp) k.next();
				if (lDOMProp.groupName.compareTo(lBlockHeader.groupName) == 0) {
					lBlockHeader.valArr.add(lDOMProp.title);
				}
			}					
		}
		
		// update the assoc (AttrDefn) valArr; change 
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			for (Iterator <DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				ArrayList <String> updValArr = new ArrayList <String> ();
				for (Iterator <String> k = lDOMProp.valArr.iterator(); k.hasNext();) {
					String lLDDValue = (String) k.next();
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
	private DOMAttr getLocalOrExternAttr (SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass, DOMProp lDOMProp) {
//		will be looking for something like "0001_NASA_PDS_1.pds.USER.standard_deviation"
		String lLocalIdentifier = lDOMProp.localIdentifier;
		
		// check if attribute is an LDD attribute or an external added in an earlier iteration
		DOMAttr lDOMAttr = attrMapLocal.get(lLocalIdentifier);
		if (lDOMAttr != null) {
//			System.out.println("debug getLocalOrExternAttr - FOUND IN attrMapLocal - lLocalIdentifier:" + lLocalIdentifier);
		} else {
//			System.out.println("debug getLocalOrExternAttr - NOT FOUND IN attrMapLocal - lLocalIdentifier:" + lLocalIdentifier);
			// else get a USER attribute
			int lStringInd = lLocalIdentifier.lastIndexOf(".");
			String lLDDExtTitle = lLocalIdentifier;
			String lLDDExtNS = "xxx";
			if (lStringInd > -1) lLDDExtTitle = lLocalIdentifier.substring(lStringInd + 1);
			if (lStringInd > 0) lLDDExtNS = lLocalIdentifier.substring(0, lStringInd);
			lLDDExtNS = lLDDExtNS.toLowerCase();
			String lAttrIdentifier = DOMInfoModel.getAttrIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lLDDExtNS, lLDDExtTitle);
			lDOMAttr = DOMInfoModel.userDOMClassAttrIdMap.get(lAttrIdentifier);
			if (lDOMAttr != null) {
//				System.out.println("debug getLocalOrExternAttr - FOUND IN USER - lLocalIdentifier:" + lLocalIdentifier);
			} else {
				lddErrorMsg.add("   ERROR    Class:" + lDOMClass.identifier + "  Association:" + lDOMProp.localIdentifier + "  Attribute: " + lLocalIdentifier + " - Missing Attribute");
				return null;
			}
		}
		
		// save the namespace to create an import file
		if ((lDOMAttr.nameSpaceIdNC.compareTo("pds") != 0) && (lDOMAttr.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) && (! DMDocument.LDDImportNameSpaceIdNCArr.contains(lDOMAttr.nameSpaceIdNC))) {
			DMDocument.LDDImportNameSpaceIdNCArr.add(lDOMAttr.nameSpaceIdNC);		
		}
		
		// clone the USER or LDD attribute for use as a Resolved attribute
		// returns rdfIdentifier = "TBD_rdfIdentifier"
		String lRDFIdentifier = DMDocument.rdfPrefix + lDOMAttr.title + "." + DOMInfoModel.getNextUId();
		DOMAttr lNewDOMAttr = new DOMAttr ();
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
		lNewDOMAttr.setIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title, lNewDOMAttr.nameSpaceIdNC, lNewDOMAttr.title);
//		attrArrResolved.add(lNewAttr);
		lNewDOMAttr.lddUserAttribute = lDOMAttr; // actually this could be an LDD attribute
		return lNewDOMAttr;
	}
			
	// return a local or external class
	private DOMClass getLocalOrExtrnCompClass (SchemaFileDefn lSchemaFileDefn, DOMClass lDOMClass, DOMProp lDOMProp) {
//		String lLocalIdentifier = lProperty.localIdentifierArr.get(0);
		DOMClass lComponentDOMClass;
		String lLocalIdentifier = lDOMProp.localIdentifier;
				
		// Is the class local 
		lComponentDOMClass = classMapLocal.get(lLocalIdentifier);
		if (lComponentDOMClass  != null) {
			return lComponentDOMClass;
		}

		// Assume class is external
		String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
		lComponentDOMClass = DOMInfoModel.masterDOMClassIdMap.get(lClassIdentifier);
		if (lComponentDOMClass != null) {
			// save the namespace to create an import file
			if ((lComponentDOMClass.nameSpaceIdNC.compareTo("pds") != 0) && (lComponentDOMClass.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) && (! DMDocument.LDDImportNameSpaceIdNCArr.contains(lComponentDOMClass.nameSpaceIdNC))) {
				DMDocument.LDDImportNameSpaceIdNCArr.add(lComponentDOMClass.nameSpaceIdNC);
			}
			// create the valArr update map - local to external titles.
			lLDDValArrExtUpdDefnClassMap.put(lLocalIdentifier, lComponentDOMClass.title);
			return lComponentDOMClass;
		}
		lddErrorMsg.add("   ERROR    Class:" + lDOMClass.identifier + "  Association:" + lDOMProp.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Component Class");
		return null;
	}	

	// return a local or external class for a parent
	private DOMClass getLocalOrExtrnParentClass (DOMClass lDOMClass, DOMProp lDOMProp) {
		DOMClass lParentDOMClass;
		String lLocalIdentifier = lDOMProp.localIdentifier;
		
		// Is the class local 
		lParentDOMClass = classMapLocal.get(lLocalIdentifier);
		if (lParentDOMClass  != null) {
			return lParentDOMClass;
		}

		// Assume class is external
		String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
		lParentDOMClass = DOMInfoModel.masterDOMClassIdMap.get(lClassIdentifier);
		if (lParentDOMClass != null) {
			return lParentDOMClass;
		}
		lddErrorMsg.add("   ERROR    Class:" + lDOMClass.identifier + "  Association:" + lDOMProp.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Parent Class");
		return null;
	}
	
	private void validateReservedNames () {
//		System.out.println("\ndebug validateReservedNames");
		
		boolean hasAtLeastOneElementDefined = false;
		// scan LDD class titles and determine if a reserved name has been used.
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass= (DOMClass) i.next();
			
			// if the value of the attribute title is a reserved word then this class definition is not allowed.
			// the value of the attribute local_identifier is irrelevant even if it is a reference to a foreign namespace
			if (DMDocument.reservedClassNames.contains(lDOMClass.title)) {
				lddErrorMsg.add("   ERROR    Class: " + " - No local dictionary may define a class called " + lDOMClass.title + ".");
			}
			Character lFirstChar = lDOMClass.title.charAt(0);
			if (! Character.isUpperCase(lFirstChar)) {
				lddErrorMsg.add("   ERROR    Class: " + " - The class " + lDOMClass.title + " must begin with an upper case letter.");
			}
			
			if (lDOMClass.isExposed) hasAtLeastOneElementDefined = true;
		}
		
		if (! hasAtLeastOneElementDefined) {
			lddErrorMsg.add("   ERROR    Class: " + " - At least one class must be defined as an xs:Element. (<element_flag> set to \"true\")");
		}
		
		// scan LDD attributes titles and determine if a reserved name has been used.
		for (Iterator <DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			
			if (DMDocument.reservedAttrNames.contains(lDOMAttr.title)) {
				lddErrorMsg.add("   ERROR    Attribute: " + " - No local dictionary may define an attribute called " + lDOMAttr.title + ".");
			}
			
			Character lFirstChar = lDOMAttr.title.charAt(0);			
			if (! Character.isLowerCase(lFirstChar)) {
				lddErrorMsg.add("   ERROR    Attribute: " + " - The attribute " + lDOMAttr.title + " must begin with a lower case letter.");
			}
		}
		
		// scan LDD rules to check that a value for reference_type has been define for the rule 
		for (Iterator <RuleReferenceTypeDefn> i = ruleReferenceArr.iterator(); i.hasNext();) {
			RuleReferenceTypeDefn lRuleReferenceType = (RuleReferenceTypeDefn) i.next();
			String lRuleXPath = lRuleReferenceType.ruleReferenceXPath;
			boolean foundReferenceTypeDef = false;
			for (Iterator <DOMRule> j = ruleArr.iterator(); j.hasNext();) {
				DOMRule lDOMRule = (DOMRule) j.next();
				String lRuleXpathClean = cleanXPath (lDOMRule.xpath);
				if (lRuleXpathClean.compareTo(lRuleXPath) == 0) {
					for (Iterator <DOMAssert> k = lDOMRule.assertArr.iterator(); k.hasNext();) {
						DOMAssert lAssert = (DOMAssert) k.next();
						if (! lRuleReferenceType.isLocal) {
							if (lAssert.assertStmt.indexOf("pds:reference_type") > -1) {
								foundReferenceTypeDef = true;
							}
						} else {
							if (lAssert.assertStmt.indexOf("pds:local_reference_type") > -1) {
								foundReferenceTypeDef = true;
							}
						}
					}
				}
			}
			if (! foundReferenceTypeDef) 
				lddErrorMsg.add("   ERROR    Class: " + " - At least one value for pds:local_reference_type or pds:reference_type must be defined for " + lRuleXPath + ".");
		}
		return;
	}
	
	private String cleanXPath (String lRuleXpath) {
		StringBuffer lInBuff = new StringBuffer(lRuleXpath);
		StringBuffer lOutBuff = new StringBuffer();
		int inCnt = 0, inLmt = lInBuff.length();
		while (inCnt < inLmt) {
			Character inChar = lInBuff.charAt(inCnt);
			if (! ((inCnt < 2) && (inChar == '/'))) lOutBuff.append(lInBuff.charAt(inCnt));
			inCnt++;
		}
		return lOutBuff.toString();
	}	
	
	private void validateAttributeUsed () {
//		System.out.println("\ndebug validateAttributeUsed");
		
		// get the LDD attributes local_identifiers
		ArrayList <String> lAttrLocalIdentifiersArr = new ArrayList <String> (attrMapLocal.keySet());
		
		// get the ASSOC local_identifiers
		ArrayList <String> lAssocLocalIdentifiersArr = new ArrayList <String> ();

		for (Iterator <DOMProp> i = LDDDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			lAssocLocalIdentifiersArr.add(lDOMProp.localIdentifier);
		}

		// scan LDD attribute local_identifier and check if used.
		for (Iterator <String> i = lAttrLocalIdentifiersArr.iterator(); i.hasNext();) {
			String lAttrLocalIdentifier = (String) i.next();
			if (lAssocLocalIdentifiersArr.contains(lAttrLocalIdentifier)) continue;
			DOMAttr lDOMAttr = attrMapLocal.get(lAttrLocalIdentifier);
			lDOMAttr.parentClassTitle = DMDocument.LDDToolSingletonClassTitle;
			lDOMAttr.attrParentClass = DMDocument.LDDToolSingletonDOMClass;
			lDOMAttr.classNameSpaceIdNC = "pds";
//			lddErrorMsg.add("   WARNING  Attribute: <" + (attrMapLocal.get(lAttrLocalIdentifier)).title + "> - This local attribute was not used in an Association.");
			lddErrorMsg.add("   WARNING  Attribute: <" + lDOMAttr.title + "> - This local attribute was not used in an Association.");
		}
		return;
	}
	
	private void validateNoDuplicateNames () {
//		System.out.println("\ndebug validateNoDuplicateNames");
		
		// get a list for names
		ArrayList <String> lNameArr = new ArrayList <String> ();
		
		// check the class names
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			if (lNameArr.contains(lDOMClass.title)) {
				lddErrorMsg.add("   WARNING  Class: <" + lDOMClass.title + "> - The class name is duplicated in this local data dictionary.");	
			} else {
				lNameArr.add(lDOMClass.title);
			}
		}

		// check the attribute names
		for (Iterator <DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			if (lNameArr.contains(lDOMAttr.title)) {
				lddErrorMsg.add("   WARNING  Attribute: <" + lDOMAttr.title + "> - The attribute name is duplicated in this local data dictionary.");	
			} else {
				lNameArr.add(lDOMAttr.title);
			}
		}
		return;
	}
			
	private ArrayList <Element> getAssocElemFromClassDefn (Element elem) {
//		System.out.println("\ndebug getAssocFromClassDefn");
		ArrayList <Element> lAssocElemArr = new ArrayList <Element> ();

		Node assocElement = elem.getFirstChild();
		while (assocElement != null)
		{
			if ((assocElement.getNodeType() == Node.ELEMENT_NODE) && (assocElement.getNodeName().indexOf("DD_Association") == 0)) {
				lAssocElemArr.add((Element)assocElement);
			}
			assocElement = assocElement.getNextSibling();
		}
		return lAssocElemArr;
	}
	
	private String getTextValue(Element ele, String tagName) {
		String textVal = "TBD_Ingest_LDD";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			if (el != null) {
				Node firstChild = el.getFirstChild();
				if (firstChild != null) {
					textVal = firstChild.getNodeValue();
				}
			}
		}
		return textVal;
	}
	
	private ArrayList <String> getXMLValueArr (String tagName, Element elem) {
		ArrayList <String> lValArr = new ArrayList <String> ();
		Node lNode = elem.getFirstChild();
		while (lNode != null)
		{
			if ((lNode.getNodeType() == Node.ELEMENT_NODE) && (lNode.getNodeName().indexOf(tagName) == 0)) {
				Element lElement = (Element)lNode;
				String lVal = lElement.getFirstChild().getNodeValue();
//				System.out.println("debug getXMLValueArr - local_identifier:" + lVal);
				if (lVal != null && lVal.length() > 0) lValArr.add(lVal);
			}
			lNode = lNode.getNextSibling();
		}
		return lValArr;
	}
	
	// add the LDD artifacts to the Master
	private void addLDDtoMaster () {
		// temporary globals
		ArrayList <DOMRule> tempSchematronRuleArr;

		// save the masters
		tempSchematronRuleArr = new ArrayList <DOMRule> (DOMInfoModel.masterDOMRuleIdMap.values());
		
		// clear the masters
		DOMInfoModel.masterDOMRuleIdMap.clear(); 
		DOMInfoModel.masterDOMRuleMap.clear(); 
		DOMInfoModel.masterDOMRuleArr.clear(); 
		
		// merge the LDD Classes into the Master
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			if (DOMInfoModel.masterDOMClassIdMap.containsKey(lDOMClass.identifier)) {
				// an ldd class is a duplicate of a master class; replace the master with the LDD version
				System.out.println(">>warning - Found duplicate class - lClass.identifier:" + lDOMClass.identifier);
				if (DOMInfoModel.masterDOMClassMap.containsKey(lDOMClass.rdfIdentifier)) {
					DOMInfoModel.masterDOMClassMap.remove(lDOMClass.rdfIdentifier);
					System.out.println(">>warning - Found duplicate class - REPLACED - lClass.rdfIdentifier:" + lDOMClass.rdfIdentifier);
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
		
		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
		for (Iterator<DOMClass> j = lClassArr.iterator(); j.hasNext();) {
			DOMClass lDOMClass = (DOMClass) j.next();
			DOMInfoModel.masterDOMClassIdMap.put(lDOMClass.identifier, lDOMClass);
			DOMInfoModel.masterDOMClassTitleMap.put(lDOMClass.title, lDOMClass);
		}
				
		// build the master array (sorted by identifier)
		DOMInfoModel.masterDOMClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassIdMap.values());		
		
		// merge the LDD attributes into the Master
		for (Iterator <DOMProp> i = attrArrResolved.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
				if (DOMInfoModel.masterDOMAttrIdMap.containsKey(lDOMAttr.identifier)) {
					// an ldd attribute is a duplicate of a master attribute; replace the master with the LDD version
					System.out.println(">>warning - Found duplicate attribute - lDOMAttr.identifier:" + lDOMAttr.identifier);
					if (DOMInfoModel.masterDOMAttrMap.containsKey(lDOMAttr.rdfIdentifier)) {
						DOMInfoModel.masterDOMAttrMap.remove(lDOMAttr.rdfIdentifier);
						System.out.println(">>warning - Found duplicate attribute - REPLACED - lDOMAttr.rdfIdentifier:" + lDOMAttr.rdfIdentifier);
						System.out.println(">>error   - Found duplicate attribute - REPLACED Failed - lDOMAttr.rdfIdentifier:" + lDOMAttr.rdfIdentifier);
					}
					DOMInfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
				} else {
					DOMInfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
				}
			}
		}
		//  build the remaining attribute maps and array
		DOMInfoModel.masterDOMAttrIdMap.clear(); 
		DOMInfoModel.masterDOMAttrArr.clear(); 
		ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> (DOMInfoModel.masterDOMAttrMap.values());
		for (Iterator<DOMAttr> j = lAttrArr.iterator(); j.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) j.next();
			DOMInfoModel.masterDOMAttrIdMap.put(lDOMAttr.identifier, lDOMAttr);
		}
		// build the master array (sorted by identifier)
		DOMInfoModel.masterDOMAttrArr = new ArrayList <DOMAttr> (DOMInfoModel.masterDOMAttrIdMap.values());		
		
		// merge the LDD associations into the Master
		for (Iterator <DOMProp> i = LDDDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp= (DOMProp) i.next();
			if (DOMInfoModel.masterDOMPropIdMap.containsKey(lDOMProp.identifier)) {
				// an ldd association is a duplicate of a master association; replace the master with the LDD version
				System.out.println(">>warning - Found duplicate attribute - lDOMProp.identifier:" + lDOMProp.identifier);
				if (DOMInfoModel.masterDOMPropMap.containsKey(lDOMProp.rdfIdentifier)) {
					DOMInfoModel.masterDOMPropMap.remove(lDOMProp.rdfIdentifier);
					System.out.println(">>warning - Found duplicate attribute - REPLACED - lDOMProp.rdfIdentifier:" + lDOMProp.rdfIdentifier);
				}
				DOMInfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
			} else {
				DOMInfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
			}
		}
		// build the remaining association maps and array
		DOMInfoModel.masterDOMPropIdMap.clear(); 
		DOMInfoModel.masterDOMPropArr.clear(); 
		ArrayList <DOMProp> lAssocArr = new ArrayList <DOMProp> (DOMInfoModel.masterDOMPropMap.values());
		for (Iterator<DOMProp> j = lAssocArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			DOMInfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
		}
		// build the master array (sorted by identifier)
		DOMInfoModel.masterDOMPropArr = new ArrayList <DOMProp> (DOMInfoModel.masterDOMPropIdMap.values());
		
		// copy in the LDD Schematron Rules 
		for (Iterator <DOMRule> i = ruleArr.iterator(); i.hasNext();) {
			DOMRule lRule = (DOMRule) i.next();
//			System.out.println("Debug - Adding to Master - Rule - lRule.identifier:" + lRule.identifier);
			DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
			DOMInfoModel.masterDOMRuleArr.add(lRule);
			lRule.setRDFIdentifier();
			DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
		}		

		// merge in the master rules
		for (Iterator <DOMRule> i = tempSchematronRuleArr.iterator(); i.hasNext();) {
			DOMRule lRule = (DOMRule) i.next();
			if (! DOMInfoModel.masterDOMRuleIdMap.containsKey(lRule.identifier)) {
				DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);	
				DOMInfoModel.masterDOMRuleArr.add(lRule);
				lRule.setRDFIdentifier();
				DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);				
			} else {
				System.out.println(">>warning - Found duplicate attribute - lAttr.identifier:" + lRule.identifier);
			}
		}
	}
	
	public void OverwriteFrom11179DataDict () {
	// iterate through the LDD attribute array
		for (Iterator<DOMProp> i = attrArrResolved.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
			
//				System.out.println("\ndebug LDDDOMParser.OverwriteFrom11179DataDict - Before - lDOMAttr.identifier:" + lDOMAttr.identifier);
//				System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - Before - lDOMAttr.valueType:" + lDOMAttr.valueType);
			
				DOMAttr lLDDUserAttribute = lDOMAttr.lddUserAttribute;
				if (lLDDUserAttribute == null) continue;
				if (lDOMAttr.valueType.indexOf("TBD") != 0) continue;
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

//				System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - After - lDOMAttr.identifier:" + lDOMAttr.identifier);
//				System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - After - lDOMAttr.valueType:" + lDOMAttr.valueType);
			}
		}
	}
	
	public void OverwriteFrom11179DataDictxxx () {
	// iterate through the LDD attribute array
		for (Iterator<DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			
			System.out.println("\ndebug LDDDOMParser.OverwriteFrom11179DataDict - Before - lDOMAttr.identifier:" + lDOMAttr.identifier);
			System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - Before - lDOMAttr.valueType:" + lDOMAttr.valueType);
			
			
			DOMAttr lLDDUserAttribute = lDOMAttr.lddUserAttribute;
			if (lLDDUserAttribute == null) continue;
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
			System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - After - lDOMAttr.identifier:" + lDOMAttr.identifier);
			System.out.println("      LDDDOMParser.OverwriteFrom11179DataDict - After - lDOMAttr.valueType:" + lDOMAttr.valueType);
		}
	}
	
	
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	public void writeLocalDDFiles (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		// print report
		
		if (DMDocument.writeDOMCount == 0) printReport(lSchemaFileDefn);
		DMDocument.writeDOMCount++;
		
		// write the default csv file (English)
		WriteDOMCSVFiles writeDOMCSVFiles = new WriteDOMCSVFiles ();
		writeDOMCSVFiles.writeDOMCSVFile (classArr, lSchemaFileDefn, null);
		
		// write csv for other languages if necessary
		ArrayList <String> lOtherLanguageArr = getOtherLanguage (attrArr);
		if (lOtherLanguageArr != null) {
			for (Iterator <String> i = lOtherLanguageArr.iterator(); i.hasNext();) {
				String lOtherLanguage = (String) i.next();
				writeDOMCSVFiles.writeDOMCSVFile (classArr, lSchemaFileDefn, lOtherLanguage);
			}
		}
		
		// print protege pont file
		if (DMDocument.PDS4MergeFlag) {
			printProtegePontFile(lSchemaFileDefn);
		}		
	}
	
	private ArrayList <String> getOtherLanguage (ArrayList <DOMAttr> attrArr) {
		ArrayList <String> lOtherLanguageArr = new ArrayList <String> ();
		for (Iterator <DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			ArrayList <TermEntryDefn> lTermEntryArr = new ArrayList <TermEntryDefn> (lDOMAttr.termEntryMap.values());
			for (Iterator <TermEntryDefn> j = lTermEntryArr.iterator(); j.hasNext();) {
				TermEntryDefn lTermEntry = (TermEntryDefn) j.next();
				String lLanguage = lTermEntry.language;
				if (lLanguage.compareTo("Russian") != 0) continue;  // add other natural languages as needed; precludes PDS3 nomenclature, etc.
				if (lOtherLanguageArr.contains(lLanguage)) continue;
				lOtherLanguageArr.add(lLanguage);
			}
		}
		if (lOtherLanguageArr.size() < 1) return null;
		return lOtherLanguageArr;
	}
	
	// print report
	private void printReport(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException{
//		String lFileName = lSchemaFileDefn.LDDToolOutputFileNameNE + "_" + lSchemaFileDefn.nameSpaceIdNCUC + "_" + lSchemaFileDefn.lab_version_id +  ".txt";
		String lFileName = lSchemaFileDefn.relativeFileSpecReportTXT;
		prLocalDD = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));	
		
		printDocumentHeader (lSchemaFileDefn);
		printDocumentSummary ();
	
        prLocalDD.println("\nDetailed validation messages");
		for (Iterator <String> i = lddErrorMsg.iterator(); i.hasNext();) {
			String lErrorMsg = (String) i.next();
	        prLocalDD.println(lErrorMsg);
			System.out.println(lErrorMsg);
		}

        prLocalDD.println("\nParsed Input - Header:");
		printParsedHeader(lSchemaFileDefn);
		
		prLocalDD.println ("\nParsed Input - Attributes:");
		for (Iterator <DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			printAttr (lDOMAttr);
		}

		prLocalDD.println ("\nParsed Input - Classes:");
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			printClass (lDOMClass);
		}		
		
        prLocalDD.println("\nEnd of Report");
//		System.out.println("\nEnd of Run");
		prLocalDD.close();
	}
	
	// print document header	         
	public void printDocumentHeader (SchemaFileDefn lSchemaFileDefn) {	         
        prLocalDD.println("PDS4 Local Data Dictionary Processing Report");
        prLocalDD.println("\nConfiguration:");        
        prLocalDD.println("   LDDTool Version" + "        " + DMDocument.LDDToolVersionId);
        prLocalDD.println("   LDD Version Id:" + "        " + lSchemaFileDefn.versionId);
//      prLocalDD.println("   LDD Namespace Id:" + "      " + lSchemaFileDefn.identifier);
        prLocalDD.println("   LDD Label Version Id:" + "  " + lSchemaFileDefn.labelVersionId);
        prLocalDD.println("   LDD Discipline (T/F):" + "  " + lSchemaFileDefn.isDiscipline);
        prLocalDD.println("   LDD Namespace URL:" + "     " + lSchemaFileDefn.nameSpaceURL);
        prLocalDD.println("   LDD URN Prefix:" + "        " + lSchemaFileDefn.urnPrefix);
        prLocalDD.println("   Time" + "                   " + DMDocument.sTodaysDate);
        prLocalDD.println("   Common Schema" + "          " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd" + "]");
        prLocalDD.println("   Common Schematron" + "      " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch" + "]");
        prLocalDD.println("   IM Version Id:" + "         " + DMDocument.masterPDSSchemaFileDefn.versionId);
        prLocalDD.println("   IM Namespace Id:" + "       " + DMDocument.masterPDSSchemaFileDefn.identifier);
        prLocalDD.println("   IM Label Version Id:" + "   " + DMDocument.masterPDSSchemaFileDefn.labelVersionId);        
        prLocalDD.println("   IM Object Model" + "        " + "[" + "UpperModel.pont" + "]");
        prLocalDD.println("   IM Data Dictionary" + "     " + "[" + "dd11179.pins" + "]");
        prLocalDD.println("   IM Configuration File" + "  " + "[" + "MDPTNConfigClassDisp.xml" + "]");
        prLocalDD.println("   IM Glossary" + "            " + "[" + "Glossary.pins" + "]");
        prLocalDD.println("   IM Document Spec" + "       " + "[" + "DMDocument.pins" + "]");
        
        prLocalDD.println("\nParameters:");
        prLocalDD.println("   Input File" + "             " + "[" + lSchemaFileDefn.LDDToolInputFileName + "]");
        prLocalDD.println("   PDS Processing" + "         " + DMDocument.PDSOptionalFlag);
        prLocalDD.println("   LDD Processing" + "         " + DMDocument.LDDToolFlag);
        prLocalDD.println("   Discipline LDD" + "         " + (! DMDocument.LDDToolMissionGovernanceFlag));
        prLocalDD.println("   Mission LDD" + "            " + DMDocument.LDDToolMissionGovernanceFlag);
//		prLocalDD.println("   Write Class Elements" + "   " + DMDocument.LDDClassElementFlag);
        prLocalDD.println("   Write Attr Elements" + "    " + DMDocument.LDDAttrElementFlag);
        prLocalDD.println("   Merge with Master" + "      " + DMDocument.PDS4MergeFlag);
	}
	
	// print document header
	public void printParsedHeader (SchemaFileDefn lSchemaFileDefn) {	         
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
	public void printDocumentSummary () {	  
        int totalClasses = classArr.size();
        int totalAttrs = attrArr.size();
        int totalAssocs = LDDDOMPropArr.size();
        int totalErrors = 0;
        int totalWarnings = 0;
        int totalInfo = 0;
		for (Iterator <String> i = lddErrorMsg.iterator(); i.hasNext();) {
			String lErrorMsg = (String) i.next();
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
	public void printAttr (DOMAttr lDOMAttr) {	
		prLocalDD.println("\n   name" + "                   "  + lDOMAttr.title);
		prLocalDD.println("   version" + "                "  + lDOMAttr.versionIdentifierValue);
		prLocalDD.println("   value data type" + "        "  + lDOMAttr.valueType);
		prLocalDD.println("   description" + "            "  + lDOMAttr.definition);
		if (lDOMAttr.isNilable) {
			prLocalDD.println("   nillable" + "               "  + lDOMAttr.isNilable);
		}
		if (!(lDOMAttr.minimum_value.indexOf("TBD") == 0)) {
			prLocalDD.println("   minimum value" + "          "  + lDOMAttr.minimum_value);
		}
		if (!(lDOMAttr.maximum_value.indexOf("TBD") == 0)) {
			prLocalDD.println("   maximum value" + "          "  + lDOMAttr.maximum_value);
		}
		if (!(lDOMAttr.minimum_characters.indexOf("TBD") == 0)) {
			prLocalDD.println("   minimum characters" + "     "  + lDOMAttr.minimum_characters);		
		}
		if (!(lDOMAttr.maximum_characters.indexOf("TBD") == 0)) {
			prLocalDD.println("   maximum characters" + "     "  + lDOMAttr.maximum_characters);
		}
		if (!(lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
			prLocalDD.println("   unit of measure type" + "   "  + lDOMAttr.unit_of_measure_type);
		}
		if (!(lDOMAttr.default_unit_id.indexOf("TBD") == 0)) {
			prLocalDD.println("   specified unit id" + "      "  + lDOMAttr.default_unit_id);
		}
		
		if (! (lDOMAttr.permValueArr == null || lDOMAttr.permValueArr.isEmpty())) {
			PermValueDefn lPermValueDefn = (PermValueDefn) lDOMAttr.permValueArr.get(0);
			if (lPermValueDefn.value.compareTo("") != 0) {
				prLocalDD.println("   permissible value - value meaning");
				for (Iterator <PermValueDefn> j = lDOMAttr.permValueArr.iterator(); j.hasNext();) {
					lPermValueDefn = (PermValueDefn) j.next();
					prLocalDD.println("      " + lPermValueDefn.value + " - " + lPermValueDefn.value_meaning);
				}
			}
		}
/*		if (! (attr.valArr == null || attr.valArr.isEmpty())) {
			String lVal = (String) attr.valArr.get(0);
			if (lVal.compareTo("") != 0) {
				prLocalDD.println("   permissible values <value> <value meaning>");
				for (Iterator <String> j = attr.valArr.iterator(); j.hasNext();) {
					String value = (String) j.next();
					prLocalDD.println("      " + value + "   " + "tbd value meaning");
				}
			}
		} */
	}

	// print one class
	public void printClass (DOMClass lDOMClass) {	
        prLocalDD.println("\n   name" + "                   " + lDOMClass.title);
        prLocalDD.println("   description" + "            " + lDOMClass.definition);
        prLocalDD.println("   is abstract" + "            " + lDOMClass.isAbstract);
        prLocalDD.println("   is choice" + "              " + lDOMClass.isChoice);
        prLocalDD.println("   subclass of" + "            " + lDOMClass.subClassOfTitle);	
        prLocalDD.println("\n   Associations");
		
		// print associations
//		TreeMap <String, AssocDefn> lAssocMap = classAssocMap.get(lClass.localIdentifier);
//		ArrayList <AssocDefn> lPropertyArr = new ArrayList <AssocDefn> (lAssocMap.values());

		for (Iterator <DOMProp> i = lDOMClass.ownedAttrArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
	        prLocalDD.println("\n      local identifier" + "      " + lDOMProp.localIdentifier);
	        prLocalDD.println("      minimum occurrences" + "   " + lDOMProp.minimumOccurrences);
	        prLocalDD.println("      maximum occurrences" + "   " + lDOMProp.maximumOccurrences);
	        prLocalDD.println("      reference type" + "        " + lDOMProp.referenceType);
		}
		
		for (Iterator <DOMProp> i = lDOMClass.ownedAssocArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
	        prLocalDD.println("\n      local identifier" + "      " + lDOMProp.localIdentifier);
	        prLocalDD.println("      minimum occurrences" + "   " + lDOMProp.minimumOccurrences);
	        prLocalDD.println("      maximum occurrences" + "   " + lDOMProp.maximumOccurrences);
	        prLocalDD.println("      reference type" + "        " + lDOMProp.referenceType);
		}
	}

	private void validateParsedHeader(SchemaFileDefn lSchemaFileDefn) {
		if (lRegAuthId.compareTo(DMDocument.registrationAuthorityIdentifierValue) != 0) {
			lddErrorMsg.add("   ERROR    Header: " + " - Invalid Registration Authority: " + lRegAuthId);
		}
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") == 0) {
			lddErrorMsg.add("   ERROR    Header: " + " - Master namespace is not allowed as a local data dictionary namespace:" + lSchemaFileDefn.nameSpaceIdNC);
		}

		String lSteward = lSchemaFileDefn.stewardId;
		String lNameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
//		ArrayList <String> lStewardArr = new ArrayList <String> (DMDocument.masterClassStewardSortMap.keySet());
		if (! DMDocument.masterStewardArr.contains(lSteward)) {
			lddErrorMsg.add("   WARNING  Header: " + " - New steward has been specified:" + lSteward);
		}
		if (! DMDocument.masterNameSpaceIDArr.contains(lNameSpaceIdNC)) {
			lddErrorMsg.add("   WARNING  Header: " + " - New namespace id has been specified:" + lNameSpaceIdNC);
		}
	}
	
	// finish copying in the values of the USER attribute
	public void finishCloneOfLDDUserAttributes() {
		for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			if (! lDOMAttr.isAttribute) continue;
			if (! lDOMAttr.isFromLDD) continue;
			if (lDOMAttr.lddUserAttribute == null) continue;
			
			lDOMAttr.lddUserAttribute.finishCloneAttr(lDOMAttr);
		}
	}
	
	public void validateLDDAttributes() {
		for (Iterator <DOMAttr> i = attrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			validateAttribute(lDOMAttr);
		}
	}
	
	private void validateAttribute(DOMAttr lDOMAttr) {
		int numMatches = 0, maxMatches = 7;
		DOMDataType lDOMDataType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lDOMAttr.valueType);
		if (lDOMDataType == null) {
			lddErrorMsg.add("   ERROR    Attribute: <" + lDOMAttr.title + "> - Invalid Data Type. Data Type: " + lDOMAttr.valueType);
		} else {
			if (lDOMAttr.minimum_value.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - The default minimum value provided by the attribute's data type is being overridden with " + lDOMAttr.minimum_value);
			}
			if (lDOMAttr.maximum_value.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - The default maximum value provided by the attribute's data type is being overridden with " + lDOMAttr.maximum_value);
			}
			if (lDOMAttr.minimum_characters.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - The default minimum characters provided by the attribute's data type is being overridden with " + lDOMAttr.minimum_characters);
			}
			if (lDOMAttr.maximum_characters.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - The default maximum characters provided by the attribute's data type is being overridden with " + lDOMAttr.maximum_characters);
			}
			
/*			if (lAttr.minimum_value.compareTo("INH") == 0) {
				lAttr.minimum_value = lDataType.minimum_value;
				printLine(lLevel, "  *** info - minimum_value set from data type minimum_value", lAttr.minimum_value);
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum_value provided by the data type is being overridden. minimum_value: " + lAttr.minimum_value);
			}
			if (lAttr.maximum_value.compareTo("INH") == 0) {
				lAttr.maximum_value = lDataType.maximum_value;
				printLine(lLevel, "  *** info - maximum_value set from data type maximum_value", lAttr.maximum_value);
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum_value provided by the data type is being overridden. minimum_value: " + lAttr.minimum_value);
			}
			if (lAttr.minimum_characters.compareTo("INH") == 0) {
				lAttr.minimum_characters = lDataType.minimum_characters;
				printLine(lLevel, "  *** info - minimum_characters set from data type minimum_characters", lAttr.minimum_characters);
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum_value provided by the data type is being overridden. minimum_value: " + lAttr.minimum_value);
			}
			if (lAttr.maximum_characters.compareTo("INH") == 0) {
				lAttr.maximum_characters = lDataType.maximum_characters;
				printLine(lLevel, "  *** info - maximum_characters set from data type maximum_characters", lAttr.maximum_characters);
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum_value provided by the data type is being overridden. minimum_value: " + lAttr.minimum_value);
			} */
		}
		if (!(lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
			DOMUnit lUnit = DOMInfoModel.masterDOMUnitMap.get(lDOMAttr.unit_of_measure_type);
			if (lUnit == null) {
				lddErrorMsg.add("   WARNING  Attribute2 <: " + lDOMAttr.title + " - Invalid Unit of Measure Type: " + lDOMAttr.unit_of_measure_type);
			}
		}
		// get PDS4 exact match attributes
		boolean isExact = false;
		for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			if (numMatches >= maxMatches) { break; }
			DOMAttr lMAttr = (DOMAttr) i.next();
//			if (lMAttr.title.compareTo(lAttr.title) == 0) {
			if ((! lMAttr.isFromLDD) && lMAttr.title.compareTo(lDOMAttr.title) == 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - This local attribute has a duplicate in the PDS4 data dictionary.");
				isExact = true;
				numMatches++;
			}
		}
		if (false) {
			getPartialMatches(lDOMAttr, numMatches, maxMatches);
		}
	}
	
	private void validateAssociationCardinalities (String lMinCard, String lMaxCard, String lLocalIdentifier) {
		if (DMDocument.isInteger(lMinCard)) {
			lCardMin = lMinCard;
			lCardMinI = new Integer(lMinCard);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lLocalIdentifier + " - Minimum occurrences is invalid: " + lMinCard);
		}
		if ((lMaxCard.compareTo("*") == 0) || (lMaxCard.compareTo("unbounded") == 0)) {
			lCardMax = "*";
			lCardMaxI = 9999999;
		} else if (DMDocument.isInteger(lMaxCard)) {
			lCardMax = lMaxCard;
			lCardMaxI = new Integer(lMaxCard);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lLocalIdentifier + " - Maximum occurrences is invalid: " + lMaxCard);
		}
		if (lCardMaxI < lCardMinI) {
			lddErrorMsg.add("   ERROR    Association: " + lLocalIdentifier + " - Maximum occurrences is less than minimum occurrences");
		}
	}
	
	private void validateAssociationCardinalities(AssocDefn lAssoc, String lMinCard, String lMaxCard) {
		if (DMDocument.isInteger(lMinCard)) {
			lAssoc.cardMin = lMinCard;
			lAssoc.cardMinI = new Integer(lMinCard);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lAssoc.localIdentifier + " - Minimum occurrences is invalid: " + lMinCard);
		}
		if ((lMaxCard.compareTo("*") == 0) || (lMaxCard.compareTo("unbounded") == 0)) {
			lAssoc.cardMax = "*";
			lAssoc.cardMaxI = 9999999;
		} else if (DMDocument.isInteger(lMaxCard)) {
			lAssoc.cardMax = lMaxCard;
			lAssoc.cardMaxI = new Integer(lMaxCard);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lAssoc.localIdentifier + " - Maximum occurrences is invalid: " + lMaxCard);
		}
		if (lAssoc.cardMaxI < lAssoc.cardMinI) {
			lddErrorMsg.add("   ERROR    Association: " + lAssoc.localIdentifier + " - Maximum occurrences is less than minimum occurrences");
		}
	}
	
	// print one line
	public void printLine (int lLevel, String lLeftPart, String lRightPart) {	         
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
				for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
					DOMAttr lMAttr = (DOMAttr) i.next();
					if (lMAttr.title.toUpperCase().compareTo(lDOMAttr.title.toUpperCase()) == 0) { continue; }
					if (numMatches >= maxMatches) { break; }
					String lTitleUpper = lMAttr.title.toUpperCase();
					if (lTitleUpper.indexOf(lClassWord) > -1) {
						if (lTitleUpper.indexOf(lDescriptor) > -1) {
							lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <" + lMAttr.title + ">");
							numMatches++;
						}
					}
				}
			} else {
				// we have a VALUE keyword, just search for the descriptor word
				for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
					DOMAttr lMAttr = (DOMAttr) i.next();
					if (lMAttr.title.toUpperCase().compareTo(lDOMAttr.title.toUpperCase()) == 0) { continue; }
					if (numMatches >= maxMatches) { break; }
					String lTitleUpper = lMAttr.title.toUpperCase();
					if (lTitleUpper.indexOf(lDescriptor) > -1) {
						lddErrorMsg.add("   INFO     Attribute: <" + lDOMAttr.title + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <" + lMAttr.title +">");
						numMatches++;
					}
				}
			}
		}
	}	
	
	/**
	* get the Data Element Concept (DEC) - data element side - from the attribute class word
	* - if the data element name is a class word, return it, e.g. NAME
	* - if the data element name ends in a class word, return it, e.g. mission_NAME
	* - if the data element name ends in an 'S', then it is a COUNT.
	* - otherwise the class word is assumed to be VALUE, return the last token, e.g. emission_ANGLE, filtered through valid concept list.
	*/	
	
	public String getClassWord (String deName) {
		String deNameUpper = deName.toUpperCase();
		int offset, cind = 0, deNameUpperLen = deNameUpper.length();			
		for (Iterator<String> i = classConceptSuf.iterator(); i.hasNext();) {		
			// first determine if the deName is a class word
			String classWord = (String) i.next();
			if (deNameUpper.compareTo(classWord) == 0) {
				String lClassWord = (String) classConceptNorm.get(classWord);	
				return lClassWord;				
			}
			// second determine if the class word is contained in the deName
			String classWordAug = "_" + classWord;
			cind = deNameUpper.indexOf(classWordAug);
			if (cind > -1) {
				int classWordAugLen = classWordAug.length();
				offset = deNameUpperLen - classWordAugLen;
				if (offset == cind) {
					String lClassWord = (String) classConceptNorm.get(classWord);	
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
//			if ( ! ((lTerm.compareTo("RADIUS") == 0) || (lTerm.compareTo("STATUS") == 0) || (lTerm.compareTo("NOTES") == 0) || (lTerm.compareTo("ADDRESS") == 0) || (lTerm.compareTo("CLASS") == 0) || (lTerm.compareTo("RIGHTS") == 0))) {
			if ( ! ((lTerm.compareTo("RADIUS") == 0) || (lTerm.compareTo("STATUS") == 0) || (lTerm.compareTo("NOTES") == 0) || (lTerm.compareTo("ADDRESS") == 0) || (lTerm.compareTo("CLASS") == 0))) {
				String lClassWord = "COUNT";
				return lClassWord;
			}
		}
		return lTerm;
	}
	
	public String getDescriptorWord (boolean isValue, String deName) {
		String lDENameUpper = deName.toUpperCase();
//		System.out.println("debug getDescriptorWord lDENameUpper:" + lDENameUpper);
		String lDescriptorWord = lDENameUpper;
		String lTerm = lDENameUpper;
		int lDENameUpperLen = lDENameUpper.length();
		
		// if VALUE then return the last word
		int lUSInd = lDENameUpper.lastIndexOf('_');
		if (lUSInd > -1) { 		// if underscore found then parse
			if (isValue) {		// if implicit VALUE, then return last term
				if (lUSInd <= lDENameUpperLen) {
					int lInd = lUSInd + 1;
					lTerm = lDENameUpper.substring(lInd, lDENameUpperLen);
					lDescriptorWord = lTerm;
				}
			} else {			// return second to last term
				String lDENameUpper2 = lDENameUpper.substring(0, lUSInd);
				int lDENameUpperLen2 = lDENameUpper2.length();
				lDescriptorWord = lDENameUpper2;
				int lUSInd2 = lDENameUpper2.lastIndexOf('_');
				if (lUSInd2 > -1) { 		// if underscore found then parse
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
	public String getDataConceptFromDataType (String lValueType) {
		String lCD = DOMInfoModel.dataTypeToConceptMap.get(lValueType);
		if (lCD != null) {
			return (lCD);
		} else {
			return "SHORT_STRING";
		}
	}	
		
	// print protege pont file
	private void printProtegePontFile(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException{
		//Iterate through the list and print the data
		String lFileName = lSchemaFileDefn.relativeFileSpecLDDPontMerge;
		prProtegePont = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		
		for (Iterator <DOMClass> i = classArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			printProtegeClassBegin (lDOMClass.title, lDOMClass.definition, lDOMClass.subClassOfTitle);
			for (Iterator <DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					printProtegePontAttr (lDOMAttr);
				}
			}
			printProtegeClassEnd ();
		}
		prProtegePont.close();
	}	
	
	// print one protege attribute
	public void printProtegePontAttr (DOMAttr lDOMAttr) {
		if (lDOMAttr.cardMaxI <= 1) {
	        prProtegePont.println("  (single-slot " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.title));
	        prProtegePont.println(";+       (comment \"" + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.definition) + "\")");
	        String lValueType = DOMInfoModel.dataTypePDS4ProtegeMap.get(lDOMAttr.valueType);
	        if (lValueType == null) {
	        	lValueType = "TBD_Protege_Type";
	        }
	        prProtegePont.println("    (type " + DOMInfoModel.escapeProtegeLocalDD(lValueType) + ")");
	        prProtegePont.println(";+        (cardinality " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMin) + " " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMax) + ")");
	        prProtegePont.println("    (create-accessor read-write)");	 	        
	        printProtegePontAttrValues (lDOMAttr);
	        prProtegePont.println("  )");

		} else {
	        prProtegePont.println("  (multislot " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.title));
	        prProtegePont.println(";+       (comment \"" + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.definition) + "\")");
	        String lValueType = DOMInfoModel.dataTypePDS4ProtegeMap.get(lDOMAttr.valueType);
	        if (lValueType == null) {
	        	lValueType = "TBD_Protege_Type";
	        }
	        prProtegePont.println("    (type " + lValueType + ")");
	        String lCardMax = "?VARIABLE";
	        if (lDOMAttr.cardMax.compareTo("*") != 0) {
	        	lCardMax = lDOMAttr.cardMax;
	        }
	        prProtegePont.println(";        (cardinality " + DOMInfoModel.escapeProtegeLocalDD(lDOMAttr.cardMin) + " " + DOMInfoModel.escapeProtegeLocalDD(lCardMax) + ")");
	        prProtegePont.println("    (create-accessor read-write)");
	        printProtegePontAttrValues (lDOMAttr);
	        prProtegePont.println("  )");
		}
	}
	
	// print attribute values
	public void printProtegePontAttrValues (DOMAttr lDOMAttr) {
		if (! lDOMAttr.valArr.isEmpty()) {
	        prProtegePont.print(";+		(value ");	
			for (Iterator <String> i = lDOMAttr.valArr.iterator(); i.hasNext();) {
				String lVal = (String) i.next();
				lVal = DMDocument.replaceString(lVal, " ", "_");
		        prProtegePont.print("\"" + lVal + "\" ");	
			}
	        prProtegePont.println(")");	
		}
	}
	
	// print print protege class - begin
	public void printProtegeClassBegin (String lName, String lDefinition, String lSuperClass) {	         
        prProtegePont.println("(defclass " + DOMInfoModel.escapeProtegeLocalDD(lName) + " \"" + DOMInfoModel.escapeProtegeLocalDD(lDefinition) + "\"");
        prProtegePont.println("  (is-a " + DOMInfoModel.escapeProtegeLocalDD(lSuperClass) + ")");
        prProtegePont.println("  (role concrete)");
	}
	
	// print print protege class - end
	public void printProtegeClassEnd () {	         
        prProtegePont.println(")");
	}			
}
				