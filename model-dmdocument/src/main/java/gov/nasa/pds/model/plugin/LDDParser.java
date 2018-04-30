package gov.nasa.pds.model.plugin; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

public class LDDParser extends Object
{
	// Schema File Definition
	SchemaFileDefn gSchemaFileDefn;
	
	// initialize the class structures
	TreeMap <String, PDSObjDefn> classMap = new TreeMap <String, PDSObjDefn> (); 	
	TreeMap <String, PDSObjDefn> classMapLocal = new TreeMap <String, PDSObjDefn> (); 	
	ArrayList <PDSObjDefn> classArr = new ArrayList <PDSObjDefn> ();	

	// initialize the attribute structures
	ArrayList <AttrDefn> attrArr = new ArrayList <AttrDefn> (); 
	TreeMap <String, AttrDefn> attrMap = new TreeMap <String, AttrDefn> (); 	
	TreeMap <String, AttrDefn> attrMapLocal = new TreeMap <String, AttrDefn> (); 	

	// initialize the resolved attribute structures (after LDD Attr has been mapped to a class)
	ArrayList <AttrDefn> attrArrResolved = new ArrayList <AttrDefn> (); 
	TreeMap <String, AttrDefn> attrMapResolved = new TreeMap <String, AttrDefn> (); 		
	
	// initialize the Property structures
	ArrayList <AssocDefn> LDDMOFPropArr = new ArrayList <AssocDefn> (); 	
		
	// initialize the Rule structures
	ArrayList <RuleDefn> ruleArr = new ArrayList <RuleDefn> (); 	
	TreeMap <String, RuleDefn> ruleMap = new TreeMap <String, RuleDefn> ();


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
//		lComment = lComment.replaceAll("\\s+"," ");
//		lComment = InfoModel.cleanCharString(lComment);  -- removed to allow Mitch's geometry comment to wrap properly.

		
		// get namespace
		String lNameSpaceIdNC = getTextValue(docEle,"namespace_id");
		if (lNameSpaceIdNC == null) lNameSpaceIdNC = "TBD";
		lSchemaFileDefn.setNameSpaceIds(lNameSpaceIdNC);
		
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
				
		validateAttributeUsed();
		if (DMDocument.debugFlag) System.out.println("debug getLocalDD.parseDocument.validateAttributeUsed() Done");
		
		validateNoDuplicateNames ();
		if (DMDocument.debugFlag) System.out.println("debug parseDocument.validateNoDuplicateNames() Done");
	}
	
	private void printClassDebug (String lLable, String lIdentifier) {
		PDSObjDefn lClass = classMap.get(lIdentifier);
		if (lClass == null) {
			System.out.println("\ndebug label:" + lLable + "  printClassDebug INVALID IDENTIFIER lIdentifier:" + lIdentifier);
			return;
		}
		System.out.println("\ndebug label:" + lLable + "  printClassDebug lClass.identifier:" + lClass.identifier);
		for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			System.out.println("debug printClassDebug lAttr.identifier:" + lAttr.identifier);
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
				String lAttrRdfIdentifier = DMDocument.rdfPrefix + lSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + InfoModel.getNextUId();
//				System.out.println("debug getAttributes INITIAL lAttrRdfIdentifier:" + lAttrRdfIdentifier);
				AttrDefn lAttr = (AttrDefn) attrMap.get(lAttrRdfIdentifier);
				if (lAttr == null) {
					lAttr = new AttrDefn (lAttrRdfIdentifier);
					attrArr.add(lAttr);
					attrMap.put(lAttr.rdfIdentifier, lAttr);
					lAttr.lddLocalIdentifier = lLocalIdentifier;
					attrMapLocal.put(lAttr.lddLocalIdentifier, lAttr);
					lAttr.isFromLDD = true;
					lAttr.versionIdentifierValue = getTextValue(docEle,"version_id");				
					lAttr.title = lTitle;

					// at this point lAttr.className is defaulted to the USER class, however it is updated later in association processing if it is owned by a class.
//					lAttr.setAttrIdentifier ("pds", "USER", localDDSchemaFileDefn.nameSpaceIdNC, lAttr.title);
					lAttr.setAttrIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lSchemaFileDefn.nameSpaceIdNC, lAttr.title);
					
					lAttr.XMLSchemaName = lAttr.title;
					lAttr.attrNameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lAttr.classNameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lAttr.attrNameSpaceId = lAttr.attrNameSpaceIdNC + ":";						
					lAttr.steward = lSchemaFileDefn.stewardId;
					lAttr.submitter = getTextValue(docEle,"submitter_name");
					String lDescription = getTextValue(el,"definition");
//					lDescription = lDescription.replaceAll("\\s+"," ");
					lDescription = InfoModel.cleanCharString(lDescription);
					lAttr.description = lDescription;
					lAttr.regAuthId = lRegAuthId;
					String lNillableFlag = getTextValue(el,"nillable_flag");
					if ((lNillableFlag.compareTo("true") == 0) || (lNillableFlag.compareTo("1") == 0)) lAttr.isNilable = true;
//					lAttr.isUsedInClass = true;
					lAttr.propType = "ATTRIBUTE";
					lAttr.isAttribute = true;
					
					// get the value domain
					getValueDomain (lAttr, el);
					
					// get the terminological entry
					getTermEntry (lAttr, el);
					
					// check if attribute already exists
					String lid = "0001_NASA_PDS_1." + lLocalIdentifier;
//					System.out.println("debug getAttributes -External Attribute-  lid:" + lid);
					AttrDefn lExternAttr = InfoModel.masterMOFAttrIdMap.get(lid);
					if (lExternAttr != null) {
						System.out.println("debug getAttributes -External Attribute - SETTING VALUES-  lExternAttr.identifier:" + lExternAttr.identifier);
					    if (lAttr.description.indexOf("TBD") == 0) lAttr.description = lExternAttr.description;
						if (! lAttr.isNilable) lAttr.isNilable = lExternAttr.isNilable;
						if (!lAttr.isEnumerated) lAttr.isEnumerated = lExternAttr.isEnumerated;
						if (lAttr.minimum_characters.indexOf("TBD") == 0) lAttr.minimum_characters = lExternAttr.minimum_characters;
						if (lAttr.maximum_characters.indexOf("TBD") == 0) lAttr.maximum_characters = lExternAttr.maximum_characters;
						if (lAttr.minimum_value.indexOf("TBD") == 0) lAttr.minimum_value = lExternAttr.minimum_value;
						if (lAttr.maximum_value.indexOf("TBD") == 0) lAttr.maximum_value = lExternAttr.maximum_value;
						if (lAttr.unit_of_measure_type.indexOf("TBD") == 0) lAttr.unit_of_measure_type = lExternAttr.unit_of_measure_type;
						if (lAttr.valueType.indexOf("TBD") == 0) lAttr.valueType = lExternAttr.valueType;
						if (lAttr.dataConcept.indexOf("TBD") == 0) lAttr.dataConcept = lExternAttr.dataConcept;
						if (lAttr.pattern.indexOf("TBD") == 0) lAttr.pattern = lExternAttr.pattern;
					}
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
//						lVal = lVal.replaceAll("\\s+"," ");
						lVal = InfoModel.cleanCharString(lVal);
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
				lValueMeaning= InfoModel.cleanCharString (lValueMeaning);
				
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
		
	private void getClass (SchemaFileDefn lSchemaFileDefn, Element docEle) {	
		//get a nodelist of <DD_Class> elements
		NodeList n2 = docEle.getElementsByTagName("DD_Class");
		if(n2 != null && n2.getLength() > 0) {
			for(int i = 0 ; i < n2.getLength();i++) {
				//get the elements
				Element el = (Element)n2.item(i);
				String lTitle = getTextValue(el,"name");			
				String lClassRdfIdentifier = DMDocument.rdfPrefix + lSchemaFileDefn.nameSpaceIdNC + "." + lTitle + "." + InfoModel.getNextUId();
				String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lSchemaFileDefn.nameSpaceIdNC + "." + lTitle;
				PDSObjDefn lClass = (PDSObjDefn) classMap.get(lClassRdfIdentifier);
				if (lClass == null) {
					lClass = new PDSObjDefn (lClassRdfIdentifier);
					lClass.identifier = lClassIdentifier;
					lClass.title = lTitle;		
					lClass.versionId = DMDocument.classVersionIdDefault;
					classMap.put(lClass.identifier, lClass);
					classArr.add(lClass);					
					lClass.isFromLDD = true;
					lClass.nameSpaceIdNC = lSchemaFileDefn.nameSpaceIdNC;
					lClass.nameSpaceId = lSchemaFileDefn.nameSpaceId;					
					lClass.subModelId = "UpperModel";  // *** this was added to allow the IM Spec to be written					
					lClass.steward = lSchemaFileDefn.stewardId;

					// get disposition
					PDSObjDefn lClassWDisp;
					lClassWDisp = DMDocument.getClassDisposition (lClass, lClass.title, false);
					if (lClassWDisp == null) {
//						System.out.println("debug getClass getLDDClassDisposition FAILED lClass.identifier:" + lClass.identifier);
					}

					String lDescription = getTextValue(el,"definition");
//					lDescription = lDescription.replaceAll("\\s+"," ");
//					lClass.description = lDescription;
					lClass.description = InfoModel.cleanCharString(lDescription);
					
					lClass.regAuthId = lRegAuthId;
					// subClassOF is temporary until true subClassOf is found in main code
					lClass.subClassOfTitle = DMDocument.masterUserClassName;
					lClass.subClassOfIdentifier = InfoModel.getClassIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName);
					lClass.localIdentifier = getTextValue(el,"local_identifier");
					
					String lBooleanStringValue = getTextValue(el,"abstract_flag");
					lClass.isAbstract = false;
					if (lBooleanStringValue != null) {
						if (lBooleanStringValue.compareTo("true") == 0) {
							lClass.isAbstract = true;
						}
					}
					
					lBooleanStringValue = getTextValue(el,"element_flag");
					if (lBooleanStringValue != null) {
						if (lBooleanStringValue.compareTo("true") == 0) {
							lClass.isLDDElement = true;
						}
					}
					
					classMapLocal.put(lClass.localIdentifier, lClass);
					
					// get the terminological entry
					getTermEntry (lClass, el);

					// reset class order
					InfoModel.resetClassOrder();
					
					// get associations for the respective attributes
					getAssociations(lClass, el);
				}
			}
		}
	}	

	private void getAssociations (PDSObjDefn lClass, Element ele) {		
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
				validateAssociationCardinalities (lMinimumOccurrences, lMaximumOccurrences, lLocalIdentifier);
				
				// iterate through the local identifiers for this DD association and set up a property for each
				for (Iterator <String> j = lLocalIdentifierArr.iterator(); j.hasNext();) {
					lLocalIdentifier = (String) j.next();

					// create new association -- Note that lProperty.identifier will not be set until the associated attribute is located in resolveComponentsForAssociation
					AssocDefn lProperty = new AssocDefn ();
					
					// update property arrays
					LDDMOFPropArr.add(lProperty);
					lClass.PropertyArr.add(lProperty);
					
					// test for choice and any
					if (lLocalIdentifier.indexOf("XSChoice#") == 0) {
						isGroupDelimter = true;
						isGroupContent = false;
						lIsChoice = true;
						lGroupName = lLocalIdentifier + InfoModel.getNextGroupNum();  // i.e., XSChoice#26						
						if (! lIsAttribute) {
							String lCompClassNameSpaceIdNC = lClass.nameSpaceIdNC;
							String lCompClassRdfIdentifier = DMDocument.rdfPrefix + lCompClassNameSpaceIdNC + "." + lGroupName + "." + InfoModel.getNextUId();							
							lLocalIdentifier = lGroupName;
							PDSObjDefn lCompClass = new PDSObjDefn (lCompClassRdfIdentifier);
							lCompClass.nameSpaceIdNC = lCompClassNameSpaceIdNC;
							lCompClass.nameSpaceId = lCompClass.nameSpaceIdNC + ":";
							lCompClass.localIdentifier = lGroupName;
							lCompClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lCompClassNameSpaceIdNC + "." + lGroupName;
							lCompClass.subClassOfIdentifier = InfoModel.masterMOFUserClass.identifier;
							lCompClass.title = lGroupName;
							lCompClass.isAbstract = true;
							lCompClass.isChoice = true;
							lCompClass.isFromLDD = true;
							classMap.put(lCompClass.identifier, lCompClass);
							classArr.add(lCompClass);
							classMapLocal.put(lCompClass.localIdentifier, lCompClass);
						}
					}
					if (lLocalIdentifier.compareTo("XSAny#") == 0) {
						isGroupDelimter = true;
						isGroupContent = false;
						lIsAny = true;
						lGroupName = lLocalIdentifier + InfoModel.getNextGroupNum();
						if (! lIsAttribute) {
							String lCompClassNameSpaceIdNC = lClass.nameSpaceIdNC;
							String lCompClassRdfIdentifier = DMDocument.rdfPrefix + lCompClassNameSpaceIdNC + "." + lGroupName + "." + InfoModel.getNextUId();
							lLocalIdentifier = lGroupName;
							PDSObjDefn lCompClass = new PDSObjDefn (lCompClassRdfIdentifier);
							lCompClass.nameSpaceIdNC = lCompClassNameSpaceIdNC;
							lCompClass.nameSpaceId = lCompClass.nameSpaceIdNC + ":";
							lCompClass.localIdentifier = lGroupName;
							lCompClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lCompClassNameSpaceIdNC + "." + lGroupName;
							lCompClass.subClassOfIdentifier = InfoModel.masterMOFUserClass.identifier;
							lCompClass.title = lGroupName;
							lCompClass.isAbstract = true;
							lCompClass.isAny = true;
							lCompClass.isFromLDD = true;
							classMap.put(lCompClass.identifier, lCompClass);
							classArr.add(lCompClass);
							classMapLocal.put(lCompClass.localIdentifier, lCompClass);
						}
					}
					
					// get common attributes
					lProperty.isAttribute = lIsAttribute;
					lProperty.localIdentifier = lLocalIdentifier;
//					lProperty.localIdentifierArr = lLocalIdentifierArr;
					lProperty.referenceType = lReferenceType;
					lProperty.rdfIdentifier = DMDocument.rdfPrefix + lClass.nameSpaceIdNC + "." + lClass.title + "." + lProperty.localIdentifier + "." + lProperty.referenceType + "." + InfoModel.getNextUId();
					lProperty.enclLocalIdentifier = lClass.localIdentifier;			
					lProperty.classOrder = InfoModel.getNextClassOrder();
					lProperty.isChoice = lIsChoice;	
					lProperty.isAny = lIsAny;
					lProperty.maximumOccurrences = lMaximumOccurrences;
					lProperty.minimumOccurrences = lMinimumOccurrences;
					lProperty.groupName = lGroupName;
					lProperty.rdfIdentifier = DMDocument.rdfPrefix + lClass.nameSpaceIdNC + "." + lClass.title + "." + lLocalIdentifier + "." + lProperty.referenceType + "." + InfoModel.getNextUId();
					getAssociationCardinalities(lProperty);
					
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
				RuleDefn lRule = new RuleDefn (lLocalIdentifier);	
				lRule.setRDFIdentifier();	
//				System.out.println("\ndebug getRule lRule.rdfIdentifier:" + lRule.rdfIdentifier);

				RuleDefn lRule2 = (RuleDefn) ruleMap.get(lRule.rdfIdentifier);
				if (lRule2 == null) {
					ruleMap.put(lRule.rdfIdentifier, lRule);
					ruleArr.add(lRule);					
					lRule.attrNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
					lRule.classNameSpaceNC = lSchemaFileDefn.nameSpaceIdNC;
					lRule.classSteward = lSchemaFileDefn.stewardId;
					lValue = getTextValue(el,"rule_context");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lRule.xpath = lValue;
					}
//					System.out.println("debug getRule lRule.xpath:" + lRule.xpath);
					// get the let assign values
					lValueArr = getXMLValueArr ("rule_assign", el);
					if (! (lValueArr == null || lValueArr.isEmpty())) {
						lRule.letAssignArr = lValueArr;
					}
					
					// get the rule statements
//					AssertDefn2 lAssertDefn = new AssertDefn2 ("Rule");
//					lRule.assertArr.add(lAssertDefn);
					ArrayList <Element> lElementStmtArr = getElement ("DD_Rule_Statement", el);
					for (Iterator <Element> j = lElementStmtArr.iterator(); j.hasNext();) {
						Element lElement = (Element) j.next();
						
						AssertDefn2 lAssertDefn = new AssertDefn2 ("Rule");
						lRule.assertArr.add(lAssertDefn);
						
						lValue = getTextValue(lElement,"rule_type");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							if (lValue.compareTo("Assert") == 0) lAssertDefn.assertType = "RAW";
							else if (lValue.compareTo("Assert Every") == 0) lAssertDefn.assertType = "EVERY";
							else if (lValue.compareTo("Assert If") == 0) lAssertDefn.assertType = "IF";
							else if (lValue.compareTo("Report") == 0) lAssertDefn.assertType = "REPORT";
						}
//						System.out.println("debug getRule lAssertDefn.assertType:" + lAssertDefn.assertType);
						
						lValue = getTextValue(lElement,"rule_test");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lAssertDefn.assertStmt = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.assertStmt:" + lAssertDefn.assertStmt);
						
						lValue = getTextValue(lElement,"rule_message");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lAssertDefn.assertMsg = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.assertMsg:" + lAssertDefn.assertMsg);

						lValue = getTextValue(lElement,"rule_description");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lAssertDefn.specMesg = lValue;
						}
//						System.out.println("debug getRule lAssertDefn.specMesg:" + lAssertDefn.specMesg);
							
						// get the statement values
						lValueArr = getXMLValueArr ("rule_value", lElement);
						if (! (lValueArr == null || lValueArr.isEmpty())) {
							lAssertDefn.testValArr = lValueArr;
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
//				System.out.println("\ndebug getPropMap lIdentifier:" + lIdentifier);
				
				PropertyMapsDefn lPropertyMaps = new PropertyMapsDefn (lIdentifier);
				lPropertyMaps.rdfIdentifier = InfoModel.getPropMapRDFIdentifier (lIdentifier);
				PropertyMapsDefn lPropertyMaps2 = (PropertyMapsDefn) propertyMapsMap.get(lPropertyMaps.rdfIdentifier);
				if (lPropertyMaps2 == null) {
					propertyMapsMap.put(lPropertyMaps.rdfIdentifier, lPropertyMaps);
					propertyMapsArr.add(lPropertyMaps);					
					
					// get the title
					lValue = getTextValue(lPropMapsElem,"title");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.title = lValue;
					}
//					System.out.println("debug getPropMap lPropertyMaps.title:" + lPropertyMaps.title);

					// get the steward
//					lValue = getTextValue(lPropMapsElem,"steward_id");
//					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
//						lPropertyMaps.steward_id = lValue;
//					}
//					System.out.println("debug getPropMap lPropertyMaps.steward_id:" + lPropertyMaps.steward_id);

					// get the namespace_id
					lValue = getTextValue(lPropMapsElem,"namespace_id");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.namespace_id = lValue;
					}
//					System.out.println("debug getPropMap lPropertyMaps.namespace_id:" + lPropertyMaps.namespace_id);
					
					// get the description
					lValue = getTextValue(lPropMapsElem,"description");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.description = InfoModel.cleanCharString (lValue);
					}
//					System.out.println("debug getPropMap lPropertyMaps.description:" + lPropertyMaps.description);
					
					// get the external_property_map_id
					lValue = getTextValue(lPropMapsElem,"external_property_map_id");
					if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
						lPropertyMaps.external_property_map_id = lValue;
					}
//					System.out.println("debug getPropMap lPropertyMap.external_namespace_id:" + lPropertyMap.namespace_id);
										
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
//						System.out.println("debug getPropMap lPropertyMap.identifier:" + lPropertyMap.identifier);						

						
						// get the title
						lValue = getTextValue(lPropMapElem,"title");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.title = lValue;
						}
//						System.out.println("debug getPropMap lPropertyMap.title:" + lPropertyMap.title);	
						
						// get the external namespace_id
						lValue = getTextValue(lPropMapElem,"external_namespace_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.external_namespace_id = lValue;
						}
//						System.out.println("debug getPropMap lPropertyMap.external_namespace_id:" + lPropertyMap.namespace_id);
						
						// get the model_object_id
						lValue = getTextValue(lPropMapElem,"model_object_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.model_object_id = lValue;
						}
//						System.out.println("debug getPropMap lPropertyMap.model_object_id:" + lPropertyMap.model_object_id);
						
						// get the model_object_type
						lValue = getTextValue(lPropMapElem,"model_object_type");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.model_object_type = lValue;
						}
//						System.out.println("debug getPropMap lPropertyMap.model_object_type:" + lPropertyMap.model_object_type);
						
						// get the instance_id
						lValue = getTextValue(lPropMapElem,"instance_id");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.instance_id = lValue;
						}
//						System.out.println("debug getPropMap lPropertyMap.instance_id:" + lPropertyMap.instance_id);
						
						// get the description
						lValue = getTextValue(lPropMapElem,"description");
						if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
							lPropertyMap.description = InfoModel.cleanCharString (lValue);
						}
//						System.out.println("debug getPropMap lPropertyMap.description:" + lPropertyMap.description);					
					
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
//							System.out.println("debug getPropMap lPropertyMapEntry.property_name:" + lPropertyMapEntry.property_name);

							// get the property_value
							lValue = getTextValue(lPropMapEntryElem,"property_value");
							if (! (lValue == null || (lValue.indexOf("TBD") == 0))) {
								lPropertyMapEntry.property_value = InfoModel.cleanCharString (lValue);
							}
//							System.out.println("debug getPropMap lPropertyMapEntry.property_value:" + lPropertyMapEntry.property_value);
						}
					}
				}
			}
		}
//		InfoModel.masterPropertyMapsMap = propertyMapsMap;
//		InfoModel.masterPropertyMapsArr = propertyMapsArr;

		// copy this LDDs property maps into the Master
    	Set <String> set1 = propertyMapsMap.keySet();
    	Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			PropertyMapsDefn lPropertyMapsDefn = propertyMapsMap.get(lId);
			InfoModel.masterPropertyMapsMap.put(lId, lPropertyMapsDefn);
		}
		for (Iterator <PropertyMapsDefn> i = propertyMapsArr.iterator(); i.hasNext();) {
			PropertyMapsDefn lPropertyMapsDefn = (PropertyMapsDefn) i.next();
			InfoModel.masterPropertyMapsArr.add(lPropertyMapsDefn);
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
		ArrayList <AttrDefn> lBlockCompClassArr = new ArrayList <AttrDefn> ();
		TreeMap <String, AttrDefn> lBlockCompClassMap = new TreeMap <String, AttrDefn> ();
		
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
			// for each association in a class, get the attribute (AttrDefn) or the association (AttrDefn with class titles as values)
			boolean isChoiceOrAny = false;
			boolean isChoiceOrAnyDelimiter = false;
			ArrayList <PDSObjDefn> lChildClassArr = new ArrayList <PDSObjDefn> ();
			AttrDefn lChoiceOrAnyAssocOld = null;
			
			for (Iterator <AssocDefn> j = lClass.PropertyArr.iterator(); j.hasNext();) {
				AssocDefn lProperty = (AssocDefn) j.next();

				// if an attribute association, resolve it	
				if (lProperty.isAttribute) {
					if (lProperty.localIdentifier.indexOf("XSChoice#") == 0) continue; 
					if (lProperty.localIdentifier.compareTo("XSAny#") == 0) continue;
					AttrDefn lAttr =  getLocalOrExternAttr (lSchemaFileDefn, lClass, lProperty);
					if (lAttr != null) {
						lAttr.cardMinI = lProperty.cardMinI;
						lAttr.cardMin = lProperty.cardMin;
						lAttr.cardMaxI = lProperty.cardMaxI;
						lAttr.cardMax = lProperty.cardMax;
						lAttr.isChoice = lProperty.isChoice;
						lAttr.isAny = lProperty.isAny;
						lAttr.groupName = lProperty.groupName;

						lAttr.isUsedInClass = true;
						
						// add the association (AttrDefn) to the resolved attribute array
						attrArrResolved.add(lAttr);
						attrMapResolved.put(lAttr.rdfIdentifier, lAttr);
						
						// fixup the Property (AssocDefn)
						lProperty.identifier = lAttr.identifier;
						
						// fixup Class
						lClass.ownedAttribute.add(lAttr);
						lClass.ownedAttrNSTitle.add(lAttr.nsTitle); 
						
						// fixup the Property
						lProperty.identifier = lAttr.identifier;
					} else {
						lddErrorMsg.add("   ERROR    Association: " + lProperty.localIdentifier + " - Could not find referenced attribute - Reference Type: " + lProperty.referenceType);
					}
				} else {
					// resolve an association/class
					if (lProperty.referenceType.compareTo("component_of") == 0){
						
						// get the associated local or external class
						PDSObjDefn lClassComponent = getLocalOrExtrnCompClass (lSchemaFileDefn, lClass, lProperty);
						if (lClassComponent != null) {
							
							// are the following 2 statements useful?
							lClass.isChoice = lProperty.isChoice;
							lClass.isAny = lProperty.isAny;
							isChoiceOrAny = lProperty.isChoice || lProperty.isAny;
							isChoiceOrAnyDelimiter = (lProperty.localIdentifier.indexOf("XSChoice#") == 0) || (lProperty.localIdentifier.compareTo("XSAny#") == 0);
							
							// create a new assoc (AttrDefn)
							AttrDefn lAssoc = new AttrDefn (lProperty.rdfIdentifier);
							lAssoc.isAttribute = false;
//							lAssoc.identifier = lProperty.identifier;
							lAssoc.attrNameSpaceIdNC = lClassComponent.nameSpaceIdNC;
							lAssoc.title = lProperty.localIdentifier; 
							lAssoc.setAttrIdentifier (lClass.nameSpaceIdNC, lClass.title, lAssoc.attrNameSpaceIdNC, lAssoc.title);

//							lAssoc.className = lProperty.className;
							lAssoc.parentClassTitle = lClass.title;
							lAssoc.attrParentClass = lClass;
							lAssoc.isChoice = lProperty.isChoice;
							lAssoc.isAny = lProperty.isAny;
							lAssoc.groupName = lProperty.groupName;
							lAssoc.isOwnedAttribute = true;
							lAssoc.cardMax = lProperty.cardMax;
							lAssoc.cardMaxI = lProperty.cardMaxI;
							lAssoc.cardMin = lProperty.cardMin;
							lAssoc.cardMinI = lProperty.cardMinI;
																					
							// add the associated class
							if (! isChoiceOrAny) {
								lAssoc.valArr.add(lClassComponent.title);
								
							}
					
							// get all block headers and components
							if (lAssoc.groupName.indexOf("XSChoice#") == 0 || lAssoc.groupName.indexOf("XSAny#") == 0) {
								if (! lBlockCompRDFIdArr.contains(lAssoc.rdfIdentifier)) {
									lBlockCompRDFIdArr.add(lAssoc.rdfIdentifier);
									lBlockCompClassArr.add(lAssoc);
								}
							}
							
							// add the association (AttrDefn) to the resolved attribute array
							attrArrResolved.add(lAssoc);
							attrMapResolved.put(lAssoc.rdfIdentifier, lAssoc);

							// following needed to fixup choice and any assoc.valArr(AttrDefn) after all class components are found
							if (isChoiceOrAny && ! isChoiceOrAnyDelimiter) {
								if (! lChildClassArr.contains(lClassComponent)) {
									lChildClassArr.add(lClassComponent);
								}
							}							
							
							// fixup the Property (AssocDefn)
							lProperty.identifier = lAssoc.identifier;
							
							// fixup the class associations (AttrDefn)
							if (! isChoiceOrAny) {
								lClass.ownedAssociation.add(lAssoc);
								lClass.ownedAssocNSTitle.add(lAssoc.nsTitle);
							} else if (isChoiceOrAnyDelimiter) {
								lClass.ownedAssociation.add(lAssoc);
								lClass.ownedAssocNSTitle.add(lAssoc.nsTitle);
								lChoiceOrAnyAssocOld = lAssoc;
							}
						} else lddErrorMsg.add("   ERROR    Association: " + lProperty.localIdentifier + " - Missing Component - Reference Type: " + lProperty.referenceType);
					} else if (lProperty.referenceType.compareTo("parent_of") == 0) {
						// add the referenced (LDD) class as the parent of (base_of) this (LDD) Class (lClass)
						PDSObjDefn lParentClass = getLocalOrExtrnParentClass(lClass, lProperty);
						if (lParentClass != null) {
							lClass.subClassOfTitle = lParentClass.title;
							lClass.subClassOfIdentifier = lParentClass.identifier;
							lClass.subClassOfInst = lParentClass;
						} else {
							lddErrorMsg.add("   ERROR    Association: " + lProperty.identifier + " - Missing Class: " + lProperty.localIdentifier + " - Reference Type: " + lProperty.referenceType);	
						}
					}
				}
			}
			
			// copy the titles of the <choice or any> component classes into the old Assoc (AttrDefn)
			if (lChoiceOrAnyAssocOld != null) {
				for (Iterator <PDSObjDefn> k = lChildClassArr.iterator(); k.hasNext();) {
					PDSObjDefn lComponentClass = (PDSObjDefn) k.next();
// 					lChoiceOrAnyAssocOld.valArr.add(lComponentClass.title);
										
//					System.out.println("debug resolveComponentsForAssociation - valArr2 - lComponentClass.rdfIdentifier:" + lComponentClass.rdfIdentifier);							
//					System.out.println("debug resolveComponentsForAssociation - valArr2 - lComponentClass.identifier:" + lComponentClass.identifier);							
//					System.out.println("debug resolveComponentsForAssociation - valArr2 - lComponentClass.title:" + lComponentClass.title);							
//					System.out.println("debug resolveComponentsForAssociation - valArr2 - lClassComponent.rdfIdentifier:" + lChoiceOrAnyAssocOld.rdfIdentifier);							
//					System.out.println("debug resolveComponentsForAssociation - valArr2 - lClassComponent.identifier:" + lChoiceOrAnyAssocOld.identifier);							
					
				}
			}
		}
			
		// Fixup the choice block component classes.
		// first get the block headers.
		for (Iterator <AttrDefn> j = lBlockCompClassArr.iterator(); j.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) j.next();
			if (lAssoc.title.indexOf("XSChoice#") == 0 || lAssoc.title.indexOf("XSAny#") == 0) {
				lBlockCompClassMap.put(lAssoc.groupName, lAssoc);
			}
		}
			
		// second, get the block members 
		ArrayList <AttrDefn> lBlockHeaderArr = new ArrayList <AttrDefn> (lBlockCompClassMap.values());
		for (Iterator <AttrDefn> j = lBlockCompClassArr.iterator(); j.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) j.next();
			if (lAssoc.title.indexOf("XSChoice#") == 0 || lAssoc.title.indexOf("XSAny#") == 0) continue;
			for (Iterator <AttrDefn> k = lBlockHeaderArr.iterator(); k.hasNext();) {
				AttrDefn lBlockHeader = (AttrDefn) k.next();
				if (lAssoc.groupName.compareTo(lBlockHeader.groupName) == 0) {
					lBlockHeader.valArr.add(lAssoc.title);
				}
			}					
		}
		
		// update the assoc (AttrDefn) valArr; change 
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAssoc = (AttrDefn) j.next();
				ArrayList <String> updValArr = new ArrayList <String> ();
				for (Iterator <String> k = lAssoc.valArr.iterator(); k.hasNext();) {
					String lLDDValue = (String) k.next();
					String lExtValue = lLDDValArrExtUpdDefnClassMap.get(lLDDValue);
					if (lExtValue == null) {
						updValArr.add(lLDDValue);
					} else {
						updValArr.add(lExtValue);
					}
				}
				lAssoc.valArr = updValArr;
			}
		}
		
//			if (lClass.identifier.compareTo("0001_NASA_PDS_1.disp.Display_Direction") == 0) printClassDebug ("4", "0001_NASA_PDS_1.disp.Display_Direction");
//			printClassDebug ("5", "0001_NASA_PDS_1.disp.Display_Direction");
		return;
	}
		
	// return a local or external attribute
	private AttrDefn getLocalOrExternAttr (SchemaFileDefn lSchemaFileDefn, PDSObjDefn lClass, AssocDefn lProperty) {
//		will be looking for something like "0001_NASA_PDS_1.pds.USER.standard_deviation"
		String lLocalIdentifier = lProperty.localIdentifier;
//		System.out.println("\ndebug getLocalOrExternAttr - lClass.identifier:" + lClass.identifier);
//		System.out.println("debug getLocalOrExternAttr - lLocalIdentifier:" + lLocalIdentifier);
		
		// check if attribute is an LDD attribute or an external added in an earlier iteration
		AttrDefn lAttr = attrMapLocal.get(lLocalIdentifier);
		if (lAttr != null) {
//			System.out.println("debug getLocalOrExternAttr - FOUND IN attrMapLocal - lLocalIdentifier:" + lLocalIdentifier);
//			lAttr.setAttrIdentifier (lClass.nameSpaceIdNC, lClass.title, lAttr.attrNameSpaceIdNC, lAttr.title);
		} else {
//			System.out.println("debug getLocalOrExternAttr - NOT FOUND IN attrMapLocal - lLocalIdentifier:" + lLocalIdentifier);
			// else get a USER attribute
			int lStringInd = lLocalIdentifier.lastIndexOf(".");
			String lLDDExtTitle = lLocalIdentifier;
			String lLDDExtNS = "xxx";
			if (lStringInd > -1) lLDDExtTitle = lLocalIdentifier.substring(lStringInd + 1);
			if (lStringInd > 0) lLDDExtNS = lLocalIdentifier.substring(0, lStringInd);
			lLDDExtNS = lLDDExtNS.toLowerCase();
			String lAttrIdentifier = InfoModel.getAttrIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lLDDExtNS, lLDDExtTitle);
			lAttr = InfoModel.userClassAttrIdMap.get(lAttrIdentifier);
			if (lAttr != null) {
//				System.out.println("debug getLocalOrExternAttr - FOUND IN USER - lLocalIdentifier:" + lLocalIdentifier);
			} else {
				lddErrorMsg.add("   ERROR    Class:" + lClass.identifier + "  Association:" + lProperty.localIdentifier + "  Attribute: " + lLocalIdentifier + " - Missing Attribute");
				return null;
			}
		}
		
		// save the namespace to create an import file
//		if ((lAttr.attrNameSpaceIdNC.compareTo("pds") != 0) && (! DMDocument.LDDImportNameSpaceIdNCArr.contains(lAttr.attrNameSpaceIdNC))) {
		if ((lAttr.attrNameSpaceIdNC.compareTo("pds") != 0) && (lAttr.attrNameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) && (! DMDocument.LDDImportNameSpaceIdNCArr.contains(lAttr.attrNameSpaceIdNC))) {
			DMDocument.LDDImportNameSpaceIdNCArr.add(lAttr.attrNameSpaceIdNC);		
		}
		
		// clone the USER or LDD attribute for use as a Resolved attribute
		// returns rdfIdentifier = "TBD_rdfIdentifier"
		String lRDFIdentifier = DMDocument.rdfPrefix + lAttr.title + "." + InfoModel.getNextUId();
		AttrDefn lNewAttr = InfoModel.cloneAttr(lRDFIdentifier, lAttr);
		// update new attribute
		lNewAttr.attrNameSpaceId = lAttr.attrNameSpaceId;
		lNewAttr.parentClassTitle = lClass.title;
		lNewAttr.attrParentClass = lClass;
		lNewAttr.classNameSpaceIdNC = lClass.nameSpaceIdNC;
		lNewAttr.classSteward = lClass.steward;
		lNewAttr.isFromLDD = true;
		lNewAttr.lddLocalIdentifier = lLocalIdentifier;
		lNewAttr.setAttrIdentifier(lClass.nameSpaceIdNC, lClass.title, lNewAttr.attrNameSpaceIdNC, lNewAttr.title);
//		attrArrResolved.add(lNewAttr);
//		attrMapResolved.put(lNewAttr.rdfIdentifier, lNewAttr);
		lNewAttr.lddUserAttribute = lAttr; // actually this could be an LDD attribute
		return lNewAttr;
	}
			
	// return a local or external class
	private PDSObjDefn getLocalOrExtrnCompClass (SchemaFileDefn lSchemaFileDefn, PDSObjDefn lClass, AssocDefn lProperty) {
//		String lLocalIdentifier = lProperty.localIdentifierArr.get(0);
		PDSObjDefn lComponentClass;
		String lLocalIdentifier = lProperty.localIdentifier;
				
		// Is the class local 
		lComponentClass = classMapLocal.get(lLocalIdentifier);
		if (lComponentClass  != null) {
			return lComponentClass;
		}

		// Assume class is external
		String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
		lComponentClass = InfoModel.masterMOFClassIdMap.get(lClassIdentifier);
		if (lComponentClass != null) {
			// save the namespace to create an import file
			if ((lComponentClass.nameSpaceIdNC.compareTo("pds") != 0) && (lComponentClass.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) && (! DMDocument.LDDImportNameSpaceIdNCArr.contains(lComponentClass.nameSpaceIdNC))) {
				DMDocument.LDDImportNameSpaceIdNCArr.add(lComponentClass.nameSpaceIdNC);
			}
			// create the valArr update map - local to external titles.
			lLDDValArrExtUpdDefnClassMap.put(lLocalIdentifier, lComponentClass.title);
			return lComponentClass;
		}
		lddErrorMsg.add("   ERROR    Class:" + lClass.identifier + "  Association:" + lProperty.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Component Class");
		return null;
	}	

	// return a local or external class for a parent
	private PDSObjDefn getLocalOrExtrnParentClass (PDSObjDefn lClass, AssocDefn lProperty) {
		PDSObjDefn lParentClass;
		String lLocalIdentifier = lProperty.localIdentifier;
		
		// Is the class local 
		lParentClass = classMapLocal.get(lLocalIdentifier);
		if (lParentClass  != null) {
			return lParentClass;
		}

		// Assume class is external
		String lClassIdentifier = DMDocument.registrationAuthorityIdentifierValue + "." + lLocalIdentifier;
		lParentClass = InfoModel.masterMOFClassIdMap.get(lClassIdentifier);
		if (lParentClass != null) {
			return lParentClass;
		}
		lddErrorMsg.add("   ERROR    Class:" + lClass.identifier + "  Association:" + lProperty.localIdentifier + "  Class:" + lLocalIdentifier + " - Missing Parent Class");
		return null;
	}
	
	private void validateAttributeUsed () {
//		System.out.println("\ndebug validateAttributeUsed");
		
		// get the LDD attributes local_identifiers
		ArrayList <String> lAttrLocalIdentifiersArr = new ArrayList <String> (attrMapLocal.keySet());
		
		// get the ASSOC local_identifiers
		ArrayList <String> lAssocLocalIdentifiersArr = new ArrayList <String> ();

		for (Iterator <AssocDefn> i = LDDMOFPropArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			lAssocLocalIdentifiersArr.add(lAssoc.localIdentifier);
		}

		// scan LDD attribute local_identifier and check if used.
		for (Iterator <String> i = lAttrLocalIdentifiersArr.iterator(); i.hasNext();) {
			String lAttrLocalIdentifier = (String) i.next();
			if (lAssocLocalIdentifiersArr.contains(lAttrLocalIdentifier)) continue;
			AttrDefn lAttr = attrMapLocal.get(lAttrLocalIdentifier);
			lAttr.parentClassTitle = DMDocument.LDDToolSingletonClassTitle;
			lAttr.attrParentClass = DMDocument.LDDToolSingletonClass;
			lAttr.classNameSpaceIdNC = "pds";
//			lddErrorMsg.add("   WARNING  Attribute: <" + (attrMapLocal.get(lAttrLocalIdentifier)).title + "> - This local attribute was not used in an Association.");
			lddErrorMsg.add("   WARNING  Attribute: <" + lAttr.title + "> - This local attribute was not used in an Association.");
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
		ArrayList <RuleDefn> tempSchematronRuleArr;

		// save the masters
		tempSchematronRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleIdMap.values());
		
		// clear the masters
		InfoModel.schematronRuleIdMap.clear(); 
		InfoModel.schematronRuleMap.clear(); 
		InfoModel.schematronRuleArr.clear(); 
		
		// merge the LDD Classes into the Master
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (InfoModel.masterMOFClassIdMap.containsKey(lClass.identifier)) {
				// an ldd class is a duplicate of a master class; replace the master with the LDD version
				System.out.println(">>warning - Found duplicate class - lClass.identifier:" + lClass.identifier);
				if (InfoModel.masterMOFClassMap.containsKey(lClass.rdfIdentifier)) {
					InfoModel.masterMOFClassMap.remove(lClass.rdfIdentifier);
					System.out.println(">>warning - Found duplicate class - REPLACED - lClass.rdfIdentifier:" + lClass.rdfIdentifier);
				}
				InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
			} else {
				InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
			}
		}
		
		// build the remaining class maps and array
		InfoModel.masterMOFClassIdMap.clear(); 
		InfoModel.masterMOFClassArr.clear(); 
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
		for (Iterator<PDSObjDefn> j = lClassArr.iterator(); j.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) j.next();
			InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
		}
				
		// build the master array (sorted by identifier)
		InfoModel.masterMOFClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());		

		// merge the LDD attributes into the Master
		for (Iterator <AttrDefn> i = attrArrResolved.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (InfoModel.masterMOFAttrIdMap.containsKey(lAttr.identifier)) {
				// an ldd attribute is a duplicate of a master attribute; replace the master with the LDD version
				System.out.println(">>warning - Found duplicate attribute - lAttr.identifier:" + lAttr.identifier);
				if (InfoModel.masterMOFAttrMap.containsKey(lAttr.rdfIdentifier)) {
					InfoModel.masterMOFAttrMap.remove(lAttr.rdfIdentifier);
					System.out.println(">>warning - Found duplicate attribute - REPLACED - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
					System.out.println(">>error   - Found duplicate attribute - REPLACED Failed - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
				}
				InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
			} else {
				InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
			}
		}
		//  build the remaining attribute maps and array
		InfoModel.masterMOFAttrIdMap.clear(); 
		InfoModel.masterMOFAttrArr.clear(); 
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrMap.values());
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
		}
		// build the master array (sorted by identifier)
		InfoModel.masterMOFAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());		
		
		// merge the LDD associations into the Master
		for (Iterator <AssocDefn> i = LDDMOFPropArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			if (InfoModel.masterMOFAssocIdMap.containsKey(lAssoc.identifier)) {
				// an ldd association is a duplicate of a master association; replace the master with the LDD version
				System.out.println(">>warning - Found duplicate attribute - lAssoc.identifier:" + lAssoc.identifier);
				if (InfoModel.masterMOFAssocMap.containsKey(lAssoc.rdfIdentifier)) {
					InfoModel.masterMOFAssocMap.remove(lAssoc.rdfIdentifier);
					System.out.println(">>warning - Found duplicate attribute - REPLACED - lAssoc.rdfIdentifier:" + lAssoc.rdfIdentifier);
				}
				InfoModel.masterMOFAssocMap.put(lAssoc.rdfIdentifier, lAssoc);
			} else {
				InfoModel.masterMOFAssocMap.put(lAssoc.rdfIdentifier, lAssoc);
			}
		}
		// build the remaining association maps and array
		InfoModel.masterMOFAssocIdMap.clear(); 
		InfoModel.masterMOFAssocArr.clear(); 
		ArrayList <AssocDefn> lAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocMap.values());
		for (Iterator<AssocDefn> j = lAssocArr.iterator(); j.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) j.next();
			InfoModel.masterMOFAssocIdMap.put(lAssoc.identifier, lAssoc);
		}
		// build the master array (sorted by identifier)
		InfoModel.masterMOFAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());
		
		// copy in the LDD Schematron Rules 
		for (Iterator <RuleDefn> i = ruleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
//			System.out.println("Debug - Adding to Master - Rule - lRule.identifier:" + lRule.identifier);
			InfoModel.schematronRuleIdMap.put(lRule.identifier, lRule);
			InfoModel.schematronRuleArr.add(lRule);
			lRule.setRDFIdentifier();
			InfoModel.schematronRuleMap.put(lRule.rdfIdentifier, lRule);
		}		

		// merge in the master rules
		for (Iterator <RuleDefn> i = tempSchematronRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			if (! InfoModel.schematronRuleIdMap.containsKey(lRule.identifier)) {
				InfoModel.schematronRuleIdMap.put(lRule.identifier, lRule);	
				InfoModel.schematronRuleArr.add(lRule);
				lRule.setRDFIdentifier();
				InfoModel.schematronRuleMap.put(lRule.rdfIdentifier, lRule);				
			} else {
				System.out.println(">>warning - Found duplicate attribute - lAttr.identifier:" + lRule.identifier);
			}
		}
	}
	
	public void OverwriteFrom11179DataDict () {
	// iterate through the LDD attribute array
		for (Iterator<AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			AttrDefn lLDDUserAttribute = lAttr.lddUserAttribute;
			if (lLDDUserAttribute == null) continue;
			lAttr.isNilable = lLDDUserAttribute.isNilable;
			lAttr.valueType = lLDDUserAttribute.valueType;
			lAttr.minimum_value = lLDDUserAttribute.minimum_value;
			lAttr.maximum_value = lLDDUserAttribute.maximum_value;
			lAttr.minimum_characters = lLDDUserAttribute.minimum_characters;
			lAttr.maximum_characters = lLDDUserAttribute.maximum_characters;
			lAttr.pattern = lLDDUserAttribute.pattern;
			lAttr.unit_of_measure_type = lLDDUserAttribute.unit_of_measure_type;
			lAttr.default_unit_id = lLDDUserAttribute.default_unit_id;
			lAttr.valArr = lLDDUserAttribute.valArr;
			lAttr.permValueArr = lLDDUserAttribute.permValueArr;
		}
	}
	
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}

	public void writeLocalDDFiles (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		// print report
		printReport(lSchemaFileDefn);	
		
		// write the default csv file (English)
		WriteCSVFiles writeCSVFiles = new WriteCSVFiles ();
		writeCSVFiles.writeCSVFile (classArr, lSchemaFileDefn, null);
		
		// write csv for other languages if necessary
		ArrayList <String> lOtherLanguageArr = getOtherLanguage (attrArr);
		if (lOtherLanguageArr != null) {
			for (Iterator <String> i = lOtherLanguageArr.iterator(); i.hasNext();) {
				String lOtherLanguage = (String) i.next();
				writeCSVFiles.writeCSVFile (classArr, lSchemaFileDefn, lOtherLanguage);
			}
		}
		
		// print protege pont file
		if (DMDocument.PDS4MergeFlag) {
			printProtegePontFile(lSchemaFileDefn);
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
	public void printDocumentHeader (SchemaFileDefn lSchemaFileDefn) {	         
        prLocalDD.println("PDS4 Local Data Dictionary Processing Report");
        prLocalDD.println("\nConfiguration:");        
        prLocalDD.println("   LDDTool Version" + "        " + DMDocument.LDDToolVersionId);
        prLocalDD.println("   Time" + "                   " + DMDocument.sTodaysDate);
        prLocalDD.println("   Core Schemas" + "           " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd" + "]");
        prLocalDD.println("   Core Schematrons" + "       " + "[" + "PDS4_" +  DMDocument.masterPDSSchemaFileDefn.identifier.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch" + "]");
        prLocalDD.println("   Common Model Version" + "   " + DMDocument.masterPDSSchemaFileDefn.versionId);
        prLocalDD.println("   Object Model" + "           " + "[" + "UpperModel.pont" + "]");
        prLocalDD.println("   Data Dictionary" + "        " + "[" + "dd11179.pins" + "]");
        prLocalDD.println("   Configuration File" + "     " + "[" + "MDPTNConfigClassDisp.xml" + "]");
        prLocalDD.println("   Glossary" + "               " + "[" + "Glossary.pins" + "]");
        prLocalDD.println("   Document Spec" + "          " + "[" + "DMDocument.pins" + "]");
        
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
        int totalAssocs = LDDMOFPropArr.size();
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
		
		if (! (attr.permValueArr == null || attr.permValueArr.isEmpty())) {
			PermValueDefn lPermValueDefn = (PermValueDefn) attr.permValueArr.get(0);
			if (lPermValueDefn.value.compareTo("") != 0) {
				prLocalDD.println("   permissible value - value meaning");
				for (Iterator <PermValueDefn> j = attr.permValueArr.iterator(); j.hasNext();) {
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
	public void printClass (PDSObjDefn lClass) {	
        prLocalDD.println("\n   name" + "                   " + lClass.title);
        prLocalDD.println("   description" + "            " + lClass.description);
        prLocalDD.println("   is abstract" + "            " + lClass.isAbstract);
        prLocalDD.println("   is choice" + "              " + lClass.isChoice);
        prLocalDD.println("   subclass of" + "            " + lClass.subClassOfTitle);	
        prLocalDD.println("\n   Associations");
		
		// print associations
//		TreeMap <String, AssocDefn> lAssocMap = classAssocMap.get(lClass.localIdentifier);
//		ArrayList <AssocDefn> lPropertyArr = new ArrayList <AssocDefn> (lAssocMap.values());

		for (Iterator <AssocDefn> i = lClass.PropertyArr.iterator(); i.hasNext();) {
			AssocDefn lProperty = (AssocDefn) i.next();
	        prLocalDD.println("\n      local identifier" + "      " + lProperty.localIdentifier);
	        prLocalDD.println("      minimum occurrences" + "   " + lProperty.minimumOccurrences);
	        prLocalDD.println("      maximum occurrences" + "   " + lProperty.maximumOccurrences);
	        prLocalDD.println("      reference type" + "        " + lProperty.referenceType);
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
//		System.out.println("\ndebug finishCloneOfLDDUserAttributes");
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttr.isAttribute) continue;
			if (! lAttr.isFromLDD) continue;
//			System.out.println("debug finishCloneOfLDDUserAttributes lAttr.identifier:" + lAttr.identifier);
			if (lAttr.lddUserAttribute == null) continue;
			
//			System.out.println("debug finishCloneOfLDDUserAttributes FINISH lAttr.lddUserAttribute.identifier:" + lAttr.lddUserAttribute.identifier);
			InfoModel.finishCloneAttr(lAttr, lAttr.lddUserAttribute);
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
	
	private void getAssociationCardinalities(AssocDefn lAssoc) {
		lAssoc.cardMin = lCardMin;
		lAssoc.cardMinI = lCardMinI;
		lAssoc.cardMax = lCardMax;
		lAssoc.cardMaxI = lCardMaxI;
		return;
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
		String lFileName = lSchemaFileDefn.relativeFileSpecLDDPontMerge;
		prProtegePont = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		
		for (Iterator <PDSObjDefn> i = classArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			printProtegeClassBegin (lClass.title, lClass.description, lClass.subClassOfTitle);
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
	        	lValueType = "TBD_Protege_Type";
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
	        	lValueType = "TBD_Protege_Type";
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
				