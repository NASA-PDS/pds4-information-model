package gov.nasa.pds.model.plugin;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

public class LDDParser extends Object
{
	// initialize the class structures
	TreeMap <String, PDSObjDefn> classMap = new TreeMap <String, PDSObjDefn> (); 	
	TreeMap <String, PDSObjDefn> classMapLocal = new TreeMap <String, PDSObjDefn> (); 	
	ArrayList <PDSObjDefn> classArr = new ArrayList <PDSObjDefn> ();	

	// initialize the attribute structures
	TreeMap <String, AttrDefn> attrMap = new TreeMap <String, AttrDefn> (); 	
	TreeMap <String, AttrDefn> attrMapLocal = new TreeMap <String, AttrDefn> (); 	
	ArrayList <AttrDefn> attrArr = new ArrayList <AttrDefn> (); 
	
	// initialize the association structures
	ArrayList <AssocDefn> assocArr = new ArrayList <AssocDefn> (); 	
	
	PrintWriter prLocalDD, prProtegePont;

	// local_identifier to RDF_Identifier map 
	TreeMap <String, String> lIdToRDFId = new TreeMap <String, String> (); 	
	
	// info, warning, and error messages
	ArrayList <String> lddErrorMsg = new ArrayList <String> ();
	
	// Schema File Definition
	SchemaFileDefn localDDSchemaFileDefn;
	
	// class and descriptor structures
	ArrayList <String> classConceptSuf;
	TreeMap <String, String> classConceptNorm;
	ArrayList <String> validConceptArr;

	//No generics
	Document dom;
	
	String lLDDName;
	String lLDDVersionId;
	String lFullName;				
	String lLastModificationDateTime;
//	String lIdentifier;
	String lDescription;
	String lComment;
	String lRegAuthId = DMDocument.registrationAuthorityIdentifierValue;

	public LDDParser () {
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
//		System.out.println("\ndebug getLocalDD");

		//parse the xml file and get the dom object
		parseXmlFile();
		
		//get each employee element and create a Employee object
		parseDocument();
		
		// validate parsed header
		validateParsedHeader();
		
		// add the LDD artifacts to the master
		addLDDtoMaster ();
	}		

	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(DMDocument.LDDToolFileName);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void parseDocument(){
		//get the root element
		Element docEle = dom.getDocumentElement();		
		//get the basics

		// local DD attributes
		lFullName = getTextValue(docEle,"full_name");				
		lLastModificationDateTime = getTextValue(docEle,"last_modification_date_time");				

		String lNameSpaceIdNC = getTextValue(docEle,"namespace_id");
				
		// get the LDD SchemaFileDefn
		if (! (lNameSpaceIdNC == null || (lNameSpaceIdNC.indexOf("TBD") == 0))) {
			lNameSpaceIdNC = lNameSpaceIdNC.toLowerCase();
			localDDSchemaFileDefn = new SchemaFileDefn(lNameSpaceIdNC);
		} else {
			lNameSpaceIdNC = "tbd";
			localDDSchemaFileDefn = new SchemaFileDefn(lNameSpaceIdNC);
		}
		
		lLDDName = getTextValue(docEle,"name");
		if (lLDDName == null || (lLDDName.indexOf("TBD") == 0)) {
			lLDDName = "TBD_LDD_name";
		}
		
		lLDDVersionId = getTextValue(docEle,"ldd_version_id");
		if (! (lLDDVersionId == null || (lLDDVersionId.indexOf("TBD") == 0))) {
			localDDSchemaFileDefn.versionId = lLDDVersionId;
		}
		localDDSchemaFileDefn.setVersionIds();
		
		String lStewardId = getTextValue(docEle,"steward_id");
		if ( !(lStewardId == null || (lStewardId.indexOf("TBD") == 0))) {
			lStewardId = lStewardId.toLowerCase();
			localDDSchemaFileDefn.setStewardIds (lStewardId);
		} else {
			localDDSchemaFileDefn.setStewardIds ("tbd");
		}	
		
		localDDSchemaFileDefn.directoryPath = DMDocument.LDDToolOutputFileNameNE + "_";
		
		// add to SchemaFileDefn Map and make it the Master
		DMDocument.masterSchemaFileSortMap.put(localDDSchemaFileDefn.identifier, localDDSchemaFileDefn);
		
		lDescription = getTextValue(docEle,"comment");
		if (lDescription  == null || (lDescription.indexOf("TBD") == 0)) {
			lDescription  = "TBD_description";
		}
		
		// get all Owned variables for any class that is a child of the USER class
		DMDocument.userClassAttributesMapId = new TreeMap <String, AttrDefn> ();
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			if (lClass.subClassOfTitle.compareTo("USER") != 0) continue;
			for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
	// future - String lUserAttrIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + "pds" + "." + "USER" + lAttr.attrNameSpaceIdNC + "." + lAttr.title;
				String lUserAttrIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + "pds" + "." + "USER" + "." + lAttr.title;
				DMDocument.userClassAttributesMapId.put(lUserAttrIdentifier, lAttr);
			}
		}
		
		// dump the USER attributes
//		InfoModel.dumpAttrDict();

//		get the LDD attributes
		getAttributes (docEle);
		
//		get the LDD classes
		getClass (docEle);
		
//		get the component for the LDD association 
		resolveComponentsForAssociation ();
		
		validateAttributeUsed();
		
		validateNoDuplicateNames ();
		
//		resolveAssociationsBaseClass ();
	}
	
	private void getAttributes (Element docEle) {			
		//get a nodelist of <DD_Attribute> elements
		NodeList nl = docEle.getElementsByTagName("DD_Attribute");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				//get the elements
				Element el = (Element)nl.item(i);
				String lLocalIdentifier = getTextValue(el,"local_identifier");
				String lTitle = getTextValue(el,"name");
				
				// create the rdfIdentifier; at this time only the title is known; the class is obtained from the association processing later.
				String lAttrRdfIdentifier = DMDocument.rdfPrefix + localDDSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + InfoModel.getNextUId();
				AttrDefn lAttr = (AttrDefn) attrMap.get(lAttrRdfIdentifier);
				if (lAttr == null) {
					lAttr = new AttrDefn (lAttrRdfIdentifier);
					attrMap.put(lAttr.rdfIdentifier, lAttr);
					lAttr.lddLocalIdentifier = lLocalIdentifier;
					attrMapLocal.put(lAttr.lddLocalIdentifier, lAttr);
//					attrMapLocal.put(lLocalIdentifier, lAttr);
					attrArr.add(lAttr);
					lAttr.isFromLDD = true;
					lAttr.versionIdentifierValue = getTextValue(docEle,"version_id");				
					lAttr.title = lTitle;
					// at this point lAttr.className is defaulted to TBD, it is  updated later in association processing
//					lAttr.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + localDDSchemaFileDefn.nameSpaceIdNC + "." + lAttr.className + "." + lAttr.title;
					lAttr.XMLSchemaName = lAttr.title;
					lAttr.attrNameSpaceIdNC = localDDSchemaFileDefn.nameSpaceIdNC;
					lAttr.classNameSpaceIdNC = localDDSchemaFileDefn.nameSpaceIdNC;
					lAttr.attrNameSpaceId = lAttr.attrNameSpaceIdNC + ":";						
					lAttr.steward = localDDSchemaFileDefn.stewardId;
					lAttr.submitter = getTextValue(docEle,"submitter_name");
					String lDescription = getTextValue(el,"definition");
					lDescription = lDescription.replaceAll("\\s+"," ");
					lAttr.description = lDescription;
					lAttr.regAuthId = lRegAuthId;
					String lNillableFlag = getTextValue(el,"nillable_flag");
					if ((lNillableFlag.compareTo("true") == 0) || (lNillableFlag.compareTo("1") == 0)) lAttr.isNilable = true;
					lAttr.isUsedInModel = true;
					lAttr.propType = "ATTRIBUTE";
					lAttr.isAttribute = true;
					
					// get the value domain
					getValueDomain (lAttr, el);
					
					// get the terminological entry
					getTermEntry (lAttr, el);
				}
			}
		}
	}	
	
	private void getValueDomain (AttrDefn lAttr, Element docEle) {	
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
						lAttr.isEnumerated = true; 
					}
				}
				lVal = getTextValue(el,"minimum_characters");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_minimum_characters";
					}
					lAttr.minimum_characters = lVal;
				}
				lVal = getTextValue(el,"maximum_characters");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_maximum_characters";
					}
					lAttr.maximum_characters = lVal;
				}
				lVal = getTextValue(el,"minimum_value");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_minimum_value";
					}
					lAttr.minimum_value = lVal;
				}
				lVal = getTextValue(el,"maximum_value");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_maximum_value";
					}
					lAttr.maximum_value = lVal;
				}
				
				lVal = getTextValue(el,"unit_of_measure_type");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0) || (lVal.compareTo("Units_of_None") == 0)) {
						lVal = "TBD_unit_of_measure_type";
					}
					lAttr.unit_of_measure_type = lVal;
				}				
				lVal = getTextValue(el,"value_data_type");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_value_data_type";
					}
					lAttr.valueType = lVal;
					lAttr.dataConcept = getDataConceptFromDataType (lVal);
				}
				lVal = getTextValue(el,"pattern");
				if (lVal != null) {
					if ((lVal.compareTo("unknown") == 0) || (lVal.compareTo("inapplicable") == 0)) {
						lVal = "TBD_pattern";
					}
					lAttr.pattern = lVal;
				}
				getPermissibleValues (lAttr, el);
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

					if (lObject.getClass().getName().compareTo("AttrDefn") == 0) {
						AttrDefn lAttr = (AttrDefn) lObject;
						lAttr.termEntryMap.put(lTermEntry.language, lTermEntry);
					} else {
						PDSObjDefn lClass = (PDSObjDefn) lObject;
						lClass.termEntryMap.put(lTermEntry.language, lTermEntry);
					}
					
					lVal = getTextValue(el,"name");
					if (lVal != null) lTermEntry.name = lVal;
					
					lVal = getTextValue(el,"definition");
					if (lVal != null) {
						lVal = lVal.replaceAll("\\s+"," ");
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
	
	private void getPermissibleValues (AttrDefn lAttr, Element docEle) {	
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
					lAttr.valArr.add(lValue);
				}
				String lValueMeaning = getTextValue(el,"value_meaning");
				if (lValueMeaning == null) {
					lValue = "TBD_value_meaning";
				}
				String lValueBeginDate = getTextValue(el,"value_begin_date");
				String lValueEndDate = getTextValue(el,"value_end_date");


				PermValueDefn lPermValue = new PermValueDefn (lValue, lValue, lValueMeaning);
				if (lValueBeginDate != null) {
					lPermValue.value_begin_date = lValueBeginDate;
				}
				if (lValueEndDate != null) {
					lPermValue.value_end_date = lValueEndDate;
				}
				lAttr.permValueArr.add(lPermValue);				
			}
		}
	}
		
	private void getClass (Element docEle) {	
		//get a nodelist of <DD_Class> elements
		NodeList n2 = docEle.getElementsByTagName("DD_Class");
		if(n2 != null && n2.getLength() > 0) {
			for(int i = 0 ; i < n2.getLength();i++) {
				//get the elements
				Element el = (Element)n2.item(i);
				String lTitle = getTextValue(el,"name");			
				String lClassRdfIdentifier = DMDocument.rdfPrefix + localDDSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + InfoModel.getNextUId();
				String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + localDDSchemaFileDefn.nameSpaceIdNC + "." + lTitle;
				PDSObjDefn lClass = (PDSObjDefn) classMap.get(lClassRdfIdentifier);
				if (lClass == null) {
					lClass = new PDSObjDefn (lClassRdfIdentifier);
					lClass.identifier = lClassIdentifier;
					lClass.title = lTitle;		
//					lClass.versionId = InfoModel.identifier_version_id;
					lClass.versionId = DMDocument.classVersionIdDefault;
					classMap.put(lClass.identifier, lClass);
					classArr.add(lClass);					
					lClass.isFromLDD = true;
					lClass.nameSpaceIdNC = localDDSchemaFileDefn.nameSpaceIdNC;
					lClass.nameSpaceId = localDDSchemaFileDefn.nameSpaceId;					
					lClass.steward = localDDSchemaFileDefn.stewardId;
					lClass.description = getTextValue(el,"definition");					
					lClass.regAuthId = lRegAuthId;
					lClass.subClassOfTitle = "USER";
					lClass.localIdentifier = getTextValue(el,"local_identifier");
					
					String lBooleanStringValue = getTextValue(el,"abstract_flag");
					lClass.isAbstract = false;
					if (lBooleanStringValue != null) {
						if (lBooleanStringValue.compareTo("true") == 0) {
							lClass.isAbstract = true;
						}
					}
					classMapLocal.put(lClass.localIdentifier, lClass);
					
					// get the terminological entry
					getTermEntry (lClass, el);

					// get associations for the respective attributes
					getAssociations(lClass, el);
				}
			}
		}
	}	

	private void getAssociations (PDSObjDefn lClass, Element ele) {
//		System.out.println("\ndebug getAssociations lClass.rdfIdentifier:" + lClass.rdfIdentifier);
//		System.out.println("debug getAssociations lClass.identifier:" + lClass.identifier);
//		System.out.println("debug getAssociations lClass.localIdentifier:" + lClass.localIdentifier);
		
		// set up order
		int lClassOrder = 1000;
		
		// start processing
		ArrayList <Element> lAssocElemArr = getAssocElemFromClassDefn (ele);
		for (Iterator <Element> i = lAssocElemArr.iterator(); i.hasNext();) {
			Element lAssocElem = (Element) i.next();

			// create a new association
			AssocDefn lLDDAssoc = new AssocDefn ();

			// get common attributes
			lLDDAssoc.referenceType = getTextValue(lAssocElem,"reference_type");;
			lLDDAssoc.enclLocalIdentifier = lClass.localIdentifier;			
			lLDDAssoc.maximumOccurrences = getTextValue(lAssocElem,"maximum_occurrences");	
			lLDDAssoc.minimumOccurrences = getTextValue(lAssocElem,"minimum_occurrences");
			validateAssociationCardinalities(lLDDAssoc);
			
			// increment class order
			lClassOrder += 10;
			if (lClassOrder > 9990) lClassOrder = 9999;
			Integer lClassOrderInt = new Integer (lClassOrder);
			String lClassOrderString = lClassOrderInt.toString();
			lLDDAssoc.classOrder = lClassOrderString;
						
			// get DD_Assocaitions
			if (lAssocElem.getNodeName().compareTo("DD_Association") == 0) { 
				System.out.println("\ndebug getAssociations FOUND DD_Association lLDDAssoc.referenceType:" + lLDDAssoc.referenceType);
				lLDDAssoc.isExternal = false;
				lLDDAssoc.localIdentifierArr =  getXMLValueArr ("local_identifier", lAssocElem);
				if (lLDDAssoc.localIdentifierArr.size() > 0) lLDDAssoc.localIdentifier = lLDDAssoc.localIdentifierArr.get(0);
				lLDDAssoc.rdfIdentifier = DMDocument.rdfPrefix + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAssoc.localIdentifier + "." + lLDDAssoc.referenceType + "." + InfoModel.getNextUId();
				assocArr.add(lLDDAssoc);
				lClass.LDDAssocArr.add(lLDDAssoc);
				if (lLDDAssoc.referenceType.compareTo("attribute_of") == 0) {
					lLDDAssoc.isAttribute = true;
				} else if (lLDDAssoc.referenceType.compareTo("component_of") == 0) {					
					lLDDAssoc.isAttribute = false;
				} else if ((lLDDAssoc.referenceType.compareTo("subclass_of") == 0) || (lLDDAssoc.referenceType.compareTo("extension_of") == 0) || (lLDDAssoc.referenceType.compareTo("restriction_of") == 0)) {
					lLDDAssoc.isAttribute = false;
				} else {
					lddErrorMsg.add("   ERROR    Association: " + lLDDAssoc.localIdentifier + " - Invalid reference type: " + lLDDAssoc.referenceType);
				}
			} else if (lAssocElem.getNodeName().compareTo("DD_Association_External") == 0) {
				System.out.println("\ndebug getAssociations FOUND DD_Association_External lLDDAssoc.referenceType:" + lLDDAssoc.referenceType);
				lLDDAssoc.isExternal = true;
				String lTitle = getTextValue(lAssocElem,"name");
				String lNameSpaceIdNC = getTextValue(lAssocElem,"namespace_id");
				lLDDAssoc.localIdentifier = lNameSpaceIdNC + "." + lTitle;				
				lLDDAssoc.rdfIdentifier = DMDocument.rdfPrefix + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAssoc.localIdentifier + "." + lLDDAssoc.referenceType + "." + InfoModel.getNextUId();
				lLDDAssoc.localIdentifierArr.add(lLDDAssoc.localIdentifier);
				assocArr.add(lLDDAssoc);
				lClass.LDDAssocArr.add(lLDDAssoc);
				if (lLDDAssoc.referenceType.compareTo("attribute_of") == 0) {
					lLDDAssoc.isAttribute = true;
				} else if (lLDDAssoc.referenceType.compareTo("component_of") == 0) {
					lLDDAssoc.isAttribute = false;
				} else if ((lLDDAssoc.referenceType.compareTo("subclass_of") == 0) || (lLDDAssoc.referenceType.compareTo("extension_of") == 0) || (lLDDAssoc.referenceType.compareTo("restriction_of") == 0)) {
					lLDDAssoc.isAttribute = false;
				} else {
					lddErrorMsg.add("   ERROR    Association: " + lLDDAssoc.localIdentifier + " - Invalid Reference Type: " + lLDDAssoc.referenceType);
				}
			}
		}
		return;
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

	// resolve all class associations
	private void resolveComponentsForAssociation () {
//		String testId1 = "0001_NASA_PDS_1.pds.USER.standard_deviation";
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("\ndebug resolveComponentForAssociation lClass.identifier:" + lClass.identifier);

			// for each association in a class, get the attribute (AttrDefn) or the association (AttrDefn with class titles as values)
//			System.out.println("debug resolveComponentForAssociation lClass.LDDAssocArr.size():" + lClass.LDDAssocArr.size());

			// kludge to be able to add new associations to lClass.LDDAssocArr below
			ArrayList <AssocDefn> tempLDDAssocArr = new ArrayList <AssocDefn> ();
			for (Iterator <AssocDefn> j = lClass.LDDAssocArr.iterator(); j.hasNext();) {
				AssocDefn lLDDAssoc = (AssocDefn) j.next();
				tempLDDAssocArr.add(lLDDAssoc);
			}
			for (Iterator <AssocDefn> j = tempLDDAssocArr.iterator(); j.hasNext();) {
				AssocDefn lLDDAssoc = (AssocDefn) j.next();

//				System.out.println("debug resolveComponentForAssociation lLDDAssoc.rdfIdentifier:" + lLDDAssoc.rdfIdentifier);							

				// resolve an attribute association	
				if (lLDDAssoc.isAttribute) {
//					System.out.println("debug ERR resolveComponentForAssociation GETTING lAssocAttrChoiceAnyDefn");
					AssocAttrChoiceAnyDefn lAssocAttrChoiceAnyDefn = getLDDExtAttrArr (lClass, lLDDAssoc.localIdentifierArr);
					if (lAssocAttrChoiceAnyDefn.lddAttrArr != null) {
//						System.out.println("debug resolveComponentForAssociation ADDING ATTRIBUTES lLDDAssoc.localIdentifier:" + lLDDAssoc.localIdentifier);

						if (! lAssocAttrChoiceAnyDefn.isChoice) {
						// fixup Attribute
							AttrDefn lLDDAttr =  lAssocAttrChoiceAnyDefn.lddAttr;
							if (lLDDAttr != null) {
								lLDDAttr.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAttr.title;
								lLDDAttr.className = lClass.title;
								lLDDAttr.cardMinI = lLDDAssoc.cardMinI;
								lLDDAttr.cardMin = lLDDAssoc.cardMin;
								lLDDAttr.cardMaxI = lLDDAssoc.cardMaxI;
								lLDDAttr.cardMax = lLDDAssoc.cardMax;
								lLDDAttr.isChoice = lAssocAttrChoiceAnyDefn.isChoice;
								
								// fixup Class
								lClass.ownedAttribute.add(lLDDAttr);
								lClass.ownedAttrId.add(lLDDAttr.identifier);  
								lClass.ownedAttrTitle.add(lLDDAttr.title); 
								
								// fixup the Association
								lLDDAssoc.identifier = lLDDAttr.identifier;
								lLDDAssoc.childAttrArr = lAssocAttrChoiceAnyDefn.lddAttrArr;
							} else {
								lddErrorMsg.add("   ERROR    Association: " + lLDDAssoc.localIdentifier + " - Could not find referenced LDD attribute - Reference Type: " + lLDDAssoc.referenceType);
							}
							
						} else {  // choice - all attributes (AttrDefn) must have an  AssocDefn
							for (Iterator <AttrDefn> k = lAssocAttrChoiceAnyDefn.lddAttrArr.iterator(); k.hasNext();) {
								AttrDefn lLDDAttrChoice = (AttrDefn) k.next();

//								System.out.println("debug resolveComponentForAssociation lLDDAttrChoice.rdfIdentifier:" + lLDDAttrChoice.rdfIdentifier);							

								// fixup choice Attribute
								lLDDAttrChoice.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAttrChoice.title;
								lLDDAttrChoice.className = lClass.title;
								lLDDAttrChoice.cardMinI = lLDDAssoc.cardMinI;
								lLDDAttrChoice.cardMin = lLDDAssoc.cardMin;
								lLDDAttrChoice.cardMaxI = lLDDAssoc.cardMaxI;
								lLDDAttrChoice.cardMax = lLDDAssoc.cardMax;
								lLDDAttrChoice.isChoice = lAssocAttrChoiceAnyDefn.isChoice;
								
								// fixup Class
								lClass.ownedAttribute.add(lLDDAttrChoice);
								lClass.ownedAttrId.add(lLDDAttrChoice.identifier);  
								lClass.ownedAttrTitle.add(lLDDAttrChoice.title); 

								// create new associations; there will be one duplicate, the original from association processing
								AssocDefn lNewLDDAssoc = new AssocDefn ();
								lNewLDDAssoc.localIdentifier = lLDDAssoc.localIdentifier;
								lNewLDDAssoc.localIdentifierArr = lLDDAssoc.localIdentifierArr;
								lNewLDDAssoc.referenceType = lLDDAssoc.referenceType;
								lNewLDDAssoc.rdfIdentifier = DMDocument.rdfPrefix + lClass.nameSpaceIdNC + "." + lClass.title + "." + lNewLDDAssoc.localIdentifier + "." + lNewLDDAssoc.referenceType + "." + InfoModel.getNextUId();
								lNewLDDAssoc.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAttrChoice.title; 
								lNewLDDAssoc.classOrder = lLDDAssoc.classOrder;
								lNewLDDAssoc.cardMin = lLDDAssoc.cardMin;
								lNewLDDAssoc.cardMax = lLDDAssoc.cardMax;
								lNewLDDAssoc.cardMinI = lLDDAssoc.cardMinI; 
								lNewLDDAssoc.cardMaxI = lLDDAssoc.cardMaxI;	
								lNewLDDAssoc.isChoice = lLDDAssoc.isChoice;	
								lNewLDDAssoc.isAny = lLDDAssoc.isAny;
								
								lNewLDDAssoc.enclLocalIdentifier = lLDDAssoc.enclLocalIdentifier;
								lNewLDDAssoc.maximumOccurrences = lLDDAssoc.maximumOccurrences;
								lNewLDDAssoc.minimumOccurrences = lLDDAssoc.minimumOccurrences;
								lNewLDDAssoc.isExternal = lLDDAssoc.isExternal;
								lNewLDDAssoc.isAttribute = lLDDAssoc.isAttribute;
			
								lNewLDDAssoc.parentClass = lLDDAssoc.parentClass;				
								lNewLDDAssoc.childAttrArr.add(lLDDAttrChoice); // the one attribute;
								lNewLDDAssoc.childClassArr = lLDDAssoc.childClassArr;	
								lNewLDDAssoc.childAssoc = lLDDAssoc.childAssoc;					
								lNewLDDAssoc.tempChildAssocArr = lLDDAssoc.tempChildAssocArr;
													
								assocArr.add(lNewLDDAssoc);
								lClass.LDDAssocArr.add(lNewLDDAssoc);
								InfoModel.masterMOFAssocMap.put(lNewLDDAssoc.rdfIdentifier, lNewLDDAssoc); 
								InfoModel.masterMOFAssocIdMap.put(lNewLDDAssoc.identifier, lNewLDDAssoc); 
								InfoModel.masterMOFAssocArr.add(lNewLDDAssoc); 
							}
						}
					}
					else lddErrorMsg.add("   ERROR    Association: " + lLDDAssoc.localIdentifier + " - Missing Component - Reference Type: " + lLDDAssoc.referenceType);
				} else {
					
					// is an association/class; not an attribute
					if (lLDDAssoc.referenceType.compareTo("component_of") == 0){
						AssocAttrChoiceAnyDefn lAssocAttrChoiceAnyDefn = getLDDExtAssocArr (lLDDAssoc.localIdentifierArr);
						if (lAssocAttrChoiceAnyDefn.lddClassArr != null) {
//							System.out.println("debug resolveComponentForAssociation ADDING FOUND ASSOCIATION lLDDAssoc.localIdentifier:" + lLDDAssoc.localIdentifier);
							lLDDAssoc.childClassArr = lAssocAttrChoiceAnyDefn.lddClassArr;
							lClass.isChoice = lAssocAttrChoiceAnyDefn.isChoice;
							lClass.isAny = lAssocAttrChoiceAnyDefn.isAny;
//							System.out.println("\ndebug resolveComponentForAssociation lClass.identifier:" + lClass.identifier);							
//							System.out.println("debug resolveComponentForAssociation lClass.isChoice:" + lClass.isChoice);							

							// get the assoc (AttrDefn) for the 1..m classes
							lAssocAttrChoiceAnyDefn.createAssoc(lLDDAssoc);
							
							// fixup the assoc (AttrDefn)
							AttrDefn lLDDAttr =  lAssocAttrChoiceAnyDefn.lddAssocAttrDefn;
							lLDDAttr.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLDDAttr.title;
							lLDDAttr.isChoice = lAssocAttrChoiceAnyDefn.isChoice;

							// fixup the Association
							lLDDAssoc.identifier = lLDDAttr.identifier;
							
							// fixup the class
							lClass.ownedAssociation.add(lAssocAttrChoiceAnyDefn.lddAssocAttrDefn);
							lClass.ownedAssocId.add(lAssocAttrChoiceAnyDefn.lddAssocAttrDefn.identifier);  
							lClass.ownedAssocTitle.add(lAssocAttrChoiceAnyDefn.lddAssocAttrDefn.title);

							// copy into the titles of the component classes into the Attr (Assoc)
/*							for (Iterator <PDSObjDefn> k = lAssocAttrChoiceAnyDefn.lddClassArr.iterator(); k.hasNext();) {
								PDSObjDefn lComponentClass = (PDSObjDefn) k.next();
								lAssocAttrChoiceAnyDefn.lddAssocAttrDefn.valArr.add(lComponentClass.title);
							} */
						}
						else lddErrorMsg.add("   ERROR    Association: " + lLDDAssoc.localIdentifier + " - Missing Component - Reference Type: " + lLDDAssoc.referenceType);
					} else {
						// subclassing TBD
					}
				}
			}
		}
		return;
	}
	
	// scan the local identifier array and get the AttrDefn attributes
	// finish setting identifier and title
	private AssocAttrChoiceAnyDefn getLDDExtAttrArr (PDSObjDefn lClass, ArrayList <String> lLocalIdentifierArr) {
		AssocAttrChoiceAnyDefn lAssocAttrChoiceAnyDefn = new AssocAttrChoiceAnyDefn ();
		for (Iterator <String> i = lLocalIdentifierArr.iterator(); i.hasNext();) {
			String lLocalIdentifier = (String) i.next();
//			System.out.println("debug getLDDExtAttrArr - lLocalIdentifier:" + lLocalIdentifier);
			if (lLocalIdentifier.compareTo("XSChoice#") == 0) {
				lAssocAttrChoiceAnyDefn.isChoice = true;
//				System.out.println("debug getLDDAttrAssocArr FOUND CHOICE ATTRIBUTE - lLocalIdentifier:" + lLocalIdentifier);	
				continue;
			}
			if (lLocalIdentifier.compareTo("XSAny#") == 0) {
				lAssocAttrChoiceAnyDefn.isAny = true;
//				System.out.println("debug getLDDAttrAssocArr FOUND ANY - lLocalIdentifier:" + lLocalIdentifier);	
				continue;
			}
			AttrDefn lAttr = attrMapLocal.get(lLocalIdentifier);
			if (lAttr != null) {
//				System.out.println("debug getLDDAttrAssocArr FOUND in LDD Attribute List - lClass.title:" + lClass.title);
				lAssocAttrChoiceAnyDefn.lddAttr = lAttr;
				lAssocAttrChoiceAnyDefn.lddAttrArr.add(lAttr);
			} else {
				// this will fail until USER attribute is referenced. See kludge below.
//				String lAttrIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
				
				// get the attribute title
				String lTempLocalIdentifier = lLocalIdentifier;
				if (lTempLocalIdentifier.indexOf(".") > -1) {
					lTempLocalIdentifier = lLocalIdentifier.substring(lLocalIdentifier.indexOf(".") + 1);
				}								
				String lAttrIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + "pds" + "." + "USER" + "." + lTempLocalIdentifier;
//				System.out.println("debug getLDDAttrAssocArr Created Id For TOP LEVEL SLOT CLASS in Master Attribute List - lAttrIdentifier:" + lAttrIdentifier);
				if ((lAttr = DMDocument.userClassAttributesMapId.get(lAttrIdentifier)) != null) {
//					System.out.println("debug getLDDAttrAssocArr FOUND TOP LEVEL SLOT CLASS in Master Attribute List - lAttrIdentifier:" + lAttrIdentifier);
					lAssocAttrChoiceAnyDefn.lddAttr = lAttr;
					lAssocAttrChoiceAnyDefn.lddAttrArr.add(lAttr);
				} else {
//					System.out.println("debug getLDDAttrAssocArr DID NOT FIND TOP LEVEL SLOT CLASS in Master Attribute List - lAttrIdentifier:" + lAttrIdentifier);
					// kluge - should be use a "USER" attribute.
					AttrDefn lAttrKludge = InfoModel.masterMOFAttrTitleMap.get(lTempLocalIdentifier);
					if (lAttrKludge != null) {
						// what about mins and maxes ??? etc.
						lAssocAttrChoiceAnyDefn.lddAttr = lAttrKludge;
//						System.out.println("debug getAssociations GOT Attr lAssocAttrChoiceAnyDefn.lddAttr.Identifier:" + lAssocAttrChoiceAnyDefn.lddAttr.identifier);
						lAssocAttrChoiceAnyDefn.lddAttrArr.add(lAttrKludge);
					} else {
//						System.out.println("debug getLDDAttrAssocArr NOT FOUND - lLocalIdentifier:" + lLocalIdentifier);	
						lddErrorMsg.add("   ERROR    Attribute: " + lLocalIdentifier + " - Missing Component");
					}
				}
			}
		}
		return lAssocAttrChoiceAnyDefn;
	}
	
	// scan the local identifier array and get the PDSObjDefn (classes)
	private AssocAttrChoiceAnyDefn getLDDExtAssocArr (ArrayList <String> lLocalIdentifierArr) {
		AssocAttrChoiceAnyDefn lAssocAttrChoiceAnyDefn = new AssocAttrChoiceAnyDefn ();

		for (Iterator <String> i = lLocalIdentifierArr.iterator(); i.hasNext();) {
			String lLocalIdentifier = (String) i.next();
			if (lLocalIdentifier.compareTo("XSChoice#") == 0) {
				lAssocAttrChoiceAnyDefn.isChoice = true;
//				System.out.println("debug getLDDAttrAssocArr FOUND CHOICE - lLocalIdentifier:" + lLocalIdentifier);	
				continue;
			}
			if (lLocalIdentifier.compareTo("XSAny#") == 0) {
				lAssocAttrChoiceAnyDefn.isAny = true;
//				System.out.println("debug getLDDAttrAssocArr FOUND ANY - lLocalIdentifier:" + lLocalIdentifier);	
				continue;
			}
			PDSObjDefn lComponentClass = classMapLocal.get(lLocalIdentifier);
			if (lComponentClass  != null) {
//				System.out.println("debug getLDDAttrAssocArr FOUND in LDD Class List - lLocalIdentifier:" + lLocalIdentifier);					
				lAssocAttrChoiceAnyDefn.lddClassArr.add(lComponentClass);
			} else {		
				String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
//				System.out.println("debug getLDDAttrAssocArr - lClassIdentifier:" + lClassIdentifier);
				lComponentClass = InfoModel.masterMOFClassIdMap.get(lClassIdentifier);
				if (lComponentClass != null) {
//					System.out.println("debug getLDDAttrAssocArr FOUND in Master Class List - lClassIdentifier:" + lClassIdentifier);					
					lAssocAttrChoiceAnyDefn.lddClassArr.add(lComponentClass);				
				} else {
//					System.out.println("debug getLDDAttrAssocArr NOT FOUND - lLocalIdentifier:" + lLocalIdentifier);	
					lddErrorMsg.add("   ERROR    Class: " + lLocalIdentifier + " - Missing Component");
				}
			}
		}
		return lAssocAttrChoiceAnyDefn;
	}
	
	private void validateAttributeUsed () {
//		System.out.println("\ndebug validateAttributeUsed");
		
		// get the LDD attributes local_identifiers
		ArrayList <String> lAttrLocalIdentifiersArr = new ArrayList <String> (attrMapLocal.keySet());
		
		// get the ASSOC local_identifiers
		ArrayList <String> lAssocLocalIdentifiersArr = new ArrayList <String> ();
		for (Iterator <AssocDefn> i = assocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			lAssocLocalIdentifiersArr.add(lAssoc.localIdentifier);
		}

		// scan LDD attribute local_identifier and check if used.
		for (Iterator <String> i = lAttrLocalIdentifiersArr.iterator(); i.hasNext();) {
			String lAttrLocalIdentifier = (String) i.next();
			if (lAssocLocalIdentifiersArr.contains(lAttrLocalIdentifier)) continue;
//			lddErrorMsg.add("   WARNING  Attribute not used in Association: " + (attrMapLocal.get(lAttrLocalIdentifier)).title);
			lddErrorMsg.add("   WARNING  Attribute: <" + (attrMapLocal.get(lAttrLocalIdentifier)).title + "> - This local attribute was not used in an Association.");
		}
		return;
	}
	
	private void validateNoDuplicateNames () {
//		System.out.println("\ndebug validateNoDuplicateNames");
		
		// get a list for names
		ArrayList <String> lNameArr = new ArrayList <String> ();
		
		// check the class names
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lNameArr.contains(lClass.title)) {
				lddErrorMsg.add("   WARNING  Class: <" + lClass.title + "> - The class name is duplicated in this local data dictionary.");	
			} else {
				lNameArr.add(lClass.title);
			}
		}

		// check the attribute names
		for (Iterator <AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lNameArr.contains(lAttr.title)) {
				lddErrorMsg.add("   WARNING  Attribute: <" + lAttr.title + "> - The attribute name is duplicated in this local data dictionary.");	
			} else {
				lNameArr.add(lAttr.title);
			}
		}
		return;
	}
	
/*	private void resolveAssociationsBaseClassxxx () {
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("\ndebug resolveAssociationsBaseClass lClass.identifier:" + lClass.identifier);
			if (lClass.lddBaseAssoc != null) {
				AttrDefn lAssoc = lClass.lddBaseAssoc;

				PDSObjDefn lBaseClass = (PDSObjDefn) classMapLocal.get(lAssoc.lddAssocChildClassLocalIdentifier);
				if (lBaseClass != null) {
					lClass.subClassOfTitle = lBaseClass.title;
				} else {
					lddErrorMsg.add("   ERROR    Association: " + lAssoc.lddAssocChildClassLocalIdentifier + " - Missing  Class - Reference Type: " + "component_of");	
				}				
			}
		}
	}
*/		
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
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}
	
	// add the LDD artifacts to the Master
	private void addLDDtoMaster () {

		// temporary globals
		ArrayList <PDSObjDefn> tempMasterMOFClassArr;
		ArrayList <AttrDefn> tempMasterMOFAttrArr;
		ArrayList <AssocDefn> tempMasterMOFAssocArr;

		// save the masters
		tempMasterMOFClassArr = (ArrayList <PDSObjDefn>) InfoModel.masterMOFClassArr.clone();  
		tempMasterMOFAttrArr = (ArrayList <AttrDefn>)InfoModel.masterMOFAttrArr.clone(); 
		tempMasterMOFAssocArr = (ArrayList <AssocDefn>) InfoModel.masterMOFAssocArr.clone();
		
		// clear the masters
		InfoModel.masterMOFClassArr.clear(); 
		InfoModel.masterMOFClassMap.clear(); 
		InfoModel.masterMOFClassIdMap.clear(); 
		InfoModel.masterMOFClassTitleMap.clear(); 
		InfoModel.masterMOFAttrMap.clear(); 
		InfoModel.masterMOFAttrIdMap.clear(); 
		InfoModel.masterMOFAttrTitleMap.clear(); 
		InfoModel.masterMOFAttrArr.clear(); 
		InfoModel.masterMOFAssocMap.clear(); 
		InfoModel.masterMOFAssocIdMap.clear(); 
		InfoModel.masterMOFAssocArr.clear(); 
		
		// copy in the LDD Artifacts 
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();			
//			System.out.println("Debug - Adding to Master - Class - lClass.identifier:" + lClass.identifier);
			InfoModel.masterMOFClassArr.add(lClass);
			InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
			InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
			InfoModel.masterMOFClassTitleMap.put(lClass.title, lClass);	
		}		
		
		for (Iterator <AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("Debug - Adding to Master - Attribute - lAttr.identifier:" + lAttr.identifier);
			InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
			InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
			InfoModel.masterMOFAttrTitleMap.put(lAttr.title, lAttr);
			InfoModel.masterMOFAttrArr.add(lAttr);
		}

		// add MOF properties (attribute and associations) to master
		for (Iterator <AssocDefn> i = assocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
//			System.out.println("Debug - Adding to Master - Association - lAssoc.identifier:" + lAssoc.identifier);
			InfoModel.masterMOFAssocMap.put(lAssoc.rdfIdentifier, lAssoc);
			InfoModel.masterMOFAssocIdMap.put(lAssoc.identifier, lAssoc);
			InfoModel.masterMOFAssocArr.add(lAssoc);
		}
		
		// merge in the Master Artifacts 
		for (Iterator <PDSObjDefn> i = tempMasterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();			
			if (! InfoModel.masterMOFClassIdMap.containsKey(lClass.identifier)) {
//				System.out.println("Debug - Adding to Master - Class - lClass.identifier:" + lClass.identifier);
				InfoModel.masterMOFClassArr.add(lClass);
				InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
				InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
				InfoModel.masterMOFClassTitleMap.put(lClass.title, lClass);	
			} else {
				System.out.println(">>warning - Found duplicate class - Class.identifier:" + lClass.identifier);
			}
		}		
		
		// add attributes to master
		for (Iterator <AttrDefn> i = tempMasterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! InfoModel.masterMOFAttrIdMap.containsKey(lAttr.identifier)) {
//				System.out.println("Debug - Adding to Master - Attribute - lAttr.identifier:" + lAttr.identifier);
				InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
				InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
				InfoModel.masterMOFAttrTitleMap.put(lAttr.title, lAttr);
				InfoModel.masterMOFAttrArr.add(lAttr);
			} else {
				System.out.println(">>warning - Found duplicate attribute - lAttr.identifier:" + lAttr.identifier);
			}
		}

		// add MOF properties (attribute and associations) to master
		for (Iterator <AssocDefn> i = tempMasterMOFAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
//			System.out.println("Debug - Adding to Master - Association - lAssoc.identifier:" + lAssoc.identifier);
			InfoModel.masterMOFAssocMap.put(lAssoc.rdfIdentifier, lAssoc);
			InfoModel.masterMOFAssocIdMap.put(lAssoc.identifier, lAssoc);
			InfoModel.masterMOFAssocArr.add(lAssoc);
		}
	}
	
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	public void writeLocalDDFiles () throws java.io.IOException {
		// print report
		printReport(localDDSchemaFileDefn);	
		
		// write the default csv file (English)
		InfoModel.writeCSVFile (classArr, localDDSchemaFileDefn, null);
		
		// write csv for other languages if necessary
		ArrayList <String> lOtherLanguageArr = getOtherLanguage (attrArr);
		if (lOtherLanguageArr != null) {
			for (Iterator <String> i = lOtherLanguageArr.iterator(); i.hasNext();) {
				String lOtherLanguage = (String) i.next();
				InfoModel.writeCSVFile (classArr, localDDSchemaFileDefn, lOtherLanguage);
			}
		}
		
		// print protege pont file
		if (DMDocument.PDS4MergeFlag) {
			printProtegePontFile(localDDSchemaFileDefn);
		}		
	}
	
	private ArrayList <String> getOtherLanguage (ArrayList <AttrDefn> attrArr) {
		ArrayList <String> lOtherLanguageArr = new ArrayList <String> ();
		for (Iterator <AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			ArrayList <TermEntryDefn> lTermEntryArr = new ArrayList <TermEntryDefn> (lAttr.termEntryMap.values());
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
		String lFileIdUpper = lSchemaFileDefn.identifier;
		prLocalDD = new PrintWriter(new FileWriter(DMDocument.LDDToolOutputFileNameNE + "_" + lFileIdUpper + "_" + lSchemaFileDefn.lab_version_id +  ".txt", false));
		
		printDocumentHeader ();
		printDocumentSummary ();
	
        prLocalDD.println("\nDetailed validation messages");
		for (Iterator <String> i = lddErrorMsg.iterator(); i.hasNext();) {
			String lErrorMsg = (String) i.next();
	        prLocalDD.println(lErrorMsg);
			System.out.println(lErrorMsg);
		}

        prLocalDD.println("\nParsed Input - Header:");
		printParsedHeader();
		
		prLocalDD.println ("\nParsed Input - Attributes:");
		for (Iterator <AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			printAttr (lAttr);
		}

		prLocalDD.println ("\nParsed Input - Classes:");
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			printClass (lClass);
		}		
		
        prLocalDD.println("\nEnd of Report");
//		System.out.println("\nEnd of Run");
		prLocalDD.close();
	}	
	
	// print document header
	public void printDocumentHeader () {	         
        prLocalDD.println("PDS4 Local Data Dictionary Processing Report");
        prLocalDD.println("\nConfiguration:");        
        prLocalDD.println("   Version" + "                " + DMDocument.LDDToolVersionId);
        prLocalDD.println("   Time" + "                   " + DMDocument.sTodaysDate);
        prLocalDD.println("   Core Schemas" + "           " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd" + "]");
        prLocalDD.println("   Core Schematrons" + "       " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch" + "]");
        prLocalDD.println("   Model Version" + "          " + DMDocument.masterPDSSchemaFileDefn.versionId);
        prLocalDD.println("   Object Model" + "           " + "[" + "UpperModel.pont" + "]");
        prLocalDD.println("   Data Dictionary" + "        " + "[" + "dd11179.pins" + "]");
        prLocalDD.println("   Configuration File" + "     " + "[" + "MDPTNConfigClassDisp.xml" + "]");
        prLocalDD.println("   Glossary" + "               " + "[" + "Glossary.pins" + "]");
        prLocalDD.println("   Document Spec" + "          " + "[" + "DMDocument.pins" + "]");
        
        prLocalDD.println("\nParameters:");
        prLocalDD.println("   Input File" + "             " + "[" + DMDocument.LDDToolFileName + "]");
        prLocalDD.println("   PDS Processing" + "         " + DMDocument.PDSOptionalFlag);
        prLocalDD.println("   LDD Processing" + "         " + DMDocument.LDDToolFlag);
        prLocalDD.println("   Merge with Master" + "      " + DMDocument.PDS4MergeFlag);
        prLocalDD.println("   Write Class Elements" + "   " + DMDocument.LDDClassElementFlag);
        prLocalDD.println("   Write Attr Elements" + "    " + DMDocument.LDDAttrElementFlag);
        prLocalDD.println("   Merge with Master" + "      " + DMDocument.PDS4MergeFlag);
	}	
	
	// print document header
	public void printParsedHeader () {	         
        prLocalDD.println("   LDD Name" + "               " + lLDDName);
        prLocalDD.println("   LDD Version" + "            " + lLDDVersionId);
        prLocalDD.println("   Full Name" + "              " + lFullName);
        prLocalDD.println("   Steward" + "                " + localDDSchemaFileDefn.stewardId);
        prLocalDD.println("   Namespace Id " + "          " + localDDSchemaFileDefn.nameSpaceIdNC);
        prLocalDD.println("   Comment" + "                " + lComment);
        prLocalDD.println("   Last Modification Time" + " " + lLastModificationDateTime);
        prLocalDD.println("   PDS4 Merge Flag" + "        " + DMDocument.PDS4MergeFlag);     
	}	
	
	// print document header
	public void printDocumentSummary () {	  
        int totalClasses = classArr.size();
        int totalAttrs = attrArr.size();
        int totalAssocs = assocArr.size();
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
	public void printAttr (AttrDefn attr) {	
		prLocalDD.println("\n   name" + "                   "  + attr.title);
		prLocalDD.println("   version" + "                "  + attr.versionIdentifierValue);
		prLocalDD.println("   value data type" + "        "  + attr.valueType);
		prLocalDD.println("   description" + "            "  + attr.description);
		if (attr.isNilable) {
			prLocalDD.println("   nillable" + "               "  + attr.isNilable);
		}
		if (!(attr.minimum_value.indexOf("TBD") == 0)) {
			prLocalDD.println("   minimum value" + "          "  + attr.minimum_value);
		}
		if (!(attr.maximum_value.indexOf("TBD") == 0)) {
			prLocalDD.println("   maximum value" + "          "  + attr.maximum_value);
		}
		if (!(attr.minimum_characters.indexOf("TBD") == 0)) {
			prLocalDD.println("   minimum characters" + "     "  + attr.minimum_characters);		
		}
		if (!(attr.maximum_characters.indexOf("TBD") == 0)) {
			prLocalDD.println("   maximum characters" + "     "  + attr.maximum_characters);
		}
		if (!(attr.unit_of_measure_type.indexOf("TBD") == 0)) {
			prLocalDD.println("   unit of measure type" + "   "  + attr.unit_of_measure_type);
		}
		if (!(attr.default_unit_id.indexOf("TBD") == 0)) {
			prLocalDD.println("   specified unit id" + "      "  + attr.default_unit_id);
		}
		if (! (attr.valArr == null || attr.valArr.isEmpty())) {
			String lVal = (String) attr.valArr.get(0);
			if (lVal.compareTo("") != 0) {
				prLocalDD.println("   permissible values <value> <value meaning>");
				for (Iterator <String> j = attr.valArr.iterator(); j.hasNext();) {
					String value = (String) j.next();
					prLocalDD.println("      " + value + "   " + "tbd value meaning");
				}
			}
		}
	}

	// print one class
	public void printClass (PDSObjDefn lClass) {	
        prLocalDD.println("\n   name" + "                   " + lClass.title);
        prLocalDD.println("   description" + "            " + lClass.description);
        prLocalDD.println("   is abstract" + "            " + lClass.isAbstract);
        prLocalDD.println("   is choice" + "              " + lClass.isChoice);
        prLocalDD.println("   subclass of" + "            " + lClass.subClassOfTitle);	
        prLocalDD.println("\n   Associations");
		
		// print associations
//		TreeMap <String, AssocDefn> lAssocMap = classAssocMap.get(lClass.localIdentifier);
//		ArrayList <AssocDefn> lLDDAssocArr = new ArrayList <AssocDefn> (lAssocMap.values());

		for (Iterator <AssocDefn> i = lClass.LDDAssocArr.iterator(); i.hasNext();) {
			AssocDefn lLDDAssoc = (AssocDefn) i.next();
	        prLocalDD.println("\n      local identifier" + "      " + lLDDAssoc.localIdentifier);
	        prLocalDD.println("      minimum occurrences" + "   " + lLDDAssoc.minimumOccurrences);
	        prLocalDD.println("      maximum occurrences" + "   " + lLDDAssoc.maximumOccurrences);
	        prLocalDD.println("      reference type" + "        " + lLDDAssoc.referenceType);
		}
	}

	private void validateParsedHeader() {
		if (lRegAuthId.compareTo(DMDocument.registrationAuthorityIdentifierValue) != 0) {
			lddErrorMsg.add("   ERROR    Header: " + " - Invalid Registration Authority: " + lRegAuthId);
		}
		if (localDDSchemaFileDefn.nameSpaceIdNC.compareTo("pds") == 0) {
			lddErrorMsg.add("   ERROR    Header: " + " - Master namespace is not allowed as a local data dictionary namespace:" + localDDSchemaFileDefn.nameSpaceIdNC);
		}

		String lSteward = localDDSchemaFileDefn.stewardId;
		String lNameSpaceIdNC = localDDSchemaFileDefn.nameSpaceIdNC;
		ArrayList <String> lStewardArr = new ArrayList <String> (DMDocument.masterClassStewardSortMap.keySet());
		if (! lStewardArr.contains(lSteward)) {
			lddErrorMsg.add("   WARNING  Header: " + " - New steward has been specified:" + lSteward);
		}
		if (! lStewardArr.contains(lNameSpaceIdNC)) {
			lddErrorMsg.add("   WARNING  Header: " + " - New namespace id has been specified:" + lNameSpaceIdNC);
		}
	}
	
	public void validateLDDAttributes() {
//		System.out.println("\ndebug validateLDDAttributes");
		for (Iterator <AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			validateAttribute(lAttr);
		}
	}
	
	private void validateAttribute(AttrDefn lAttr) {
//		System.out.println("\ndebug validateAttribute lAttr.title:" + lAttr.title);
		int numMatches = 0, maxMatches = 7;
		DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(lAttr.valueType);
		if (lDataType == null) {
			lddErrorMsg.add("   ERROR    Attribute: <" + lAttr.title + "> - Invalid Data Type. Data Type: " + lAttr.valueType);
		} else {
			if (lAttr.minimum_value.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum value provided by the attribute's data type is being overridden with " + lAttr.minimum_value);
			}
			if (lAttr.maximum_value.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default maximum value provided by the attribute's data type is being overridden with " + lAttr.maximum_value);
			}
			if (lAttr.minimum_characters.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default minimum characters provided by the attribute's data type is being overridden with " + lAttr.minimum_characters);
			}
			if (lAttr.maximum_characters.indexOf("TBD") != 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - The default maximum characters provided by the attribute's data type is being overridden with " + lAttr.maximum_characters);
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
		if (!(lAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
			UnitDefn lUnit = InfoModel.masterUnitOfMeasureMap.get(lAttr.unit_of_measure_type);
			if (lUnit == null) {
				lddErrorMsg.add("   WARNING  Attribute2 <: " + lAttr.title + " - Invalid Unit of Measure Type: " + lAttr.unit_of_measure_type);
			}
		}
		// get PDS4 exact match attributes
		boolean isExact = false;
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			if (numMatches >= maxMatches) { break; }
			AttrDefn lMAttr = (AttrDefn) i.next();
//			if (lMAttr.title.compareTo(lAttr.title) == 0) {
			if ((! lMAttr.isFromLDD) && lMAttr.title.compareTo(lAttr.title) == 0) {
				lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - This local attribute has a duplicate in the PDS4 data dictionary.");
				isExact = true;
				numMatches++;
			}
		}
		if (false) {
			getPartialMatches(lAttr, numMatches, maxMatches);
		}
	}
	
	private void validateAssociationCardinalities(AssocDefn lAssoc) {
		if (DMDocument.isInteger(lAssoc.minimumOccurrences)) {
			lAssoc.cardMin = lAssoc.minimumOccurrences;
			lAssoc.cardMinI = new Integer(lAssoc.minimumOccurrences);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lAssoc.localIdentifier + " - Minimum occurrences is invalid: " + lAssoc.minimumOccurrences);
		}
		if ((lAssoc.maximumOccurrences.compareTo("*") == 0) || (lAssoc.maximumOccurrences.compareTo("unbounded") == 0)) {
			lAssoc.cardMax = "*";
			lAssoc.cardMaxI = 9999999;
		} else if (DMDocument.isInteger(lAssoc.maximumOccurrences)) {
			lAssoc.cardMax = lAssoc.maximumOccurrences;
			lAssoc.cardMaxI = new Integer(lAssoc.maximumOccurrences);
		} else {
			lddErrorMsg.add("   ERROR    Association: " + lAssoc.localIdentifier + " - Maximum occurrences is invalid: " + lAssoc.maximumOccurrences);
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
	private void getPartialMatches(AttrDefn lAttr, int numMatches, int maxMatches) {
		
		// Search for partial matches only if there are more than one terms
		int lUSInd = lAttr.title.lastIndexOf('_');
		if (lUSInd > -1) {
			// get class word
			String lClassWord = getClassWord(lAttr.title);
			boolean isValue = false;
			if (lClassWord.compareTo("VALUE") == 0) {
				isValue = true;
			}
			String lDescriptor = getDescriptorWord(isValue, lAttr.title);

			if (lClassWord.compareTo("VALUE") != 0) {
				// we have a non VALUE keyword, first search for the class word
				for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
					AttrDefn lMAttr = (AttrDefn) i.next();
					if (lMAttr.title.toUpperCase().compareTo(lAttr.title.toUpperCase()) == 0) { continue; }
					if (numMatches >= maxMatches) { break; }
					String lTitleUpper = lMAttr.title.toUpperCase();
					if (lTitleUpper.indexOf(lClassWord) > -1) {
						if (lTitleUpper.indexOf(lDescriptor) > -1) {
							lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <" + lMAttr.title + ">");
							numMatches++;
						}
					}
				}
			} else {
				// we have a VALUE keyword, just search for the descriptor word
				for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
					AttrDefn lMAttr = (AttrDefn) i.next();
					if (lMAttr.title.toUpperCase().compareTo(lAttr.title.toUpperCase()) == 0) { continue; }
					if (numMatches >= maxMatches) { break; }
					String lTitleUpper = lMAttr.title.toUpperCase();
					if (lTitleUpper.indexOf(lDescriptor) > -1) {
						lddErrorMsg.add("   INFO     Attribute: <" + lAttr.title + "> - PDS4 data dictionary attribute with similar name. - Matched attribute: <" + lMAttr.title +">");
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
		String lCD = InfoModel.dataTypeToConceptMap.get(lValueType);
		if (lCD != null) {
			return (lCD);
		} else {
			return "SHORT_STRING";
		}
	}	
		
	// print protege pont file
	private void printProtegePontFile(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException{
		//Iterate through the list and print the data
//		String lFileIdUpper = lSchemaFileDefn.identifier.toUpperCase();
		String lFileIdUpper = lSchemaFileDefn.identifier;
		prProtegePont = new PrintWriter(new FileWriter(DMDocument.LDDToolOutputFileNameNE + "_" + lFileIdUpper + "_" + lSchemaFileDefn.lab_version_id + ".pont", false));
		
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			printProtegeClassBegin (lClass.title, lClass.description, "Product_Components");
			for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				printProtegePontAttr (lAttr);
			}
			printProtegeClassEnd ();
		}
		prProtegePont.close();
	}	
	
	// print one protege attribute
	public void printProtegePontAttr (AttrDefn lAttr) {
		if (lAttr.cardMaxI <= 1) {
	        prProtegePont.println("  (single-slot " + InfoModel.escapeProtegeLocalDD(lAttr.title));
	        prProtegePont.println(";+       (comment \"" + InfoModel.escapeProtegeLocalDD(lAttr.description) + "\")");
	        String lValueType = InfoModel.dataTypePDS4ProtegeMap.get(lAttr.valueType);
	        if (lValueType == null) {
	        	lValueType = "string";
	        }
	        prProtegePont.println("    (type " + InfoModel.escapeProtegeLocalDD(lValueType) + ")");
	        prProtegePont.println(";+        (cardinality " + InfoModel.escapeProtegeLocalDD(lAttr.cardMin) + " " + InfoModel.escapeProtegeLocalDD(lAttr.cardMax) + ")");
	        prProtegePont.println("    (create-accessor read-write)");	 	        
	        printProtegePontAttrValues (lAttr);
	        prProtegePont.println("  )");

		} else {
	        prProtegePont.println("  (multislot " + InfoModel.escapeProtegeLocalDD(lAttr.title));
	        prProtegePont.println(";+       (comment \"" + InfoModel.escapeProtegeLocalDD(lAttr.description) + "\")");
	        String lValueType = InfoModel.dataTypePDS4ProtegeMap.get(lAttr.valueType);
	        if (lValueType == null) {
	        	lValueType = "string";
	        }
	        prProtegePont.println("    (type " + lValueType + ")");
	        String lCardMax = "?VARIABLE";
	        if (lAttr.cardMax.compareTo("*") != 0) {
	        	lCardMax = lAttr.cardMax;
	        }
	        prProtegePont.println(";        (cardinality " + InfoModel.escapeProtegeLocalDD(lAttr.cardMin) + " " + InfoModel.escapeProtegeLocalDD(lCardMax) + ")");
	        prProtegePont.println("    (create-accessor read-write)");
	        printProtegePontAttrValues (lAttr);
	        prProtegePont.println("  )");
		}
	}
	
	// print attribute values
	public void printProtegePontAttrValues (AttrDefn lAttr) {
		if (! lAttr.valArr.isEmpty()) {
	        prProtegePont.print(";+		(value ");	
			for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
				String lVal = (String) i.next();
				lVal = DMDocument.replaceString(lVal, " ", "_");
		        prProtegePont.print("\"" + lVal + "\" ");	
			}
	        prProtegePont.println(")");	
		}
	}
	
	
	// print print protege class - begin
	public void printProtegeClassBegin (String lName, String lDefinition, String lSuperClass) {	         
        prProtegePont.println("(defclass " + InfoModel.escapeProtegeLocalDD(lName) + " \"" + InfoModel.escapeProtegeLocalDD(lDefinition) + "\"");
        prProtegePont.println("  (is-a " + InfoModel.escapeProtegeLocalDD(lSuperClass) + ")");
        prProtegePont.println("  (role concrete)");
	}
	
	// print print protege class - end
	public void printProtegeClassEnd () {	         
        prProtegePont.println(")");
	}			
}
				