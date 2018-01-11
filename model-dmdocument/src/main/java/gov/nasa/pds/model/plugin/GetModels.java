package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/** Driver for getting document
 *
 */
public class GetModels extends Object {
	SectionDefn docSection;
	SectionContentDefn docSectionContent;
	ModelDefn docModel;
	ModelDefn lModelInfo;
	
	ISO11179MDR ISO79MDR;

	ArrayList <String> texSectionFormats;
	
	boolean PDSOptionalFlag = false;
	
	public GetModels () {
		
		//	set up the Tex markers *** delete the following. ***
		texSectionFormats = new ArrayList <String>();
		texSectionFormats.add("\\section");
		texSectionFormats.add("\\subsection");
		texSectionFormats.add("\\subsubsection");
	}
	
/**********************************************************************************************************
	  initialize the models (document, information, data dictionaries) and master dictionaries (attribute and class)
***********************************************************************************************************/
	
	public void getModels (boolean oflag, String docFileName)  throws Throwable {

		PDSOptionalFlag = oflag;

		// get the Spec Document Information from the Protege DMDocuemt Pins file
		ProtPins protPinsInst = new ProtPins();
 		protPinsInst.getProtInst("DOC", "doc", DMDocument.dataDirPath + docFileName);
		HashMap <String, InstDefn> instMap = protPinsInst.instDict;
//		printInst(instMap);
		getDocInfo(instMap);
		getSections(instMap);
		getModels2(instMap);
		getSectionContent(instMap);
		
		InfoModel.ont_version_id = DMDocument.docInfo.version; 			// 1.0.0.0[b]
		DMDocument.administrationRecordValue = "DD_" + DMDocument.docInfo.version;
		String lLabVersion = InfoModel.ont_version_id;		
		lLabVersion = replaceString(lLabVersion, ".", "");		// 1000[b]
		if (lLabVersion.length() > 4) {
			InfoModel.lab_version_id = lLabVersion.substring(0,5);	// 1000B from Beta
		} else {
			InfoModel.lab_version_id = lLabVersion;				// 1000[b]	
		}
		InfoModel.ns_version_id = lLabVersion.substring(0,1);	// 1
		String lSchVersion = InfoModel.ont_version_id;	
		InfoModel.sch_version_id = lSchVersion.substring(0,7);	// 1.0.0.0
		InfoModel.identifier_version_id = lSchVersion.substring(0,3);	// 1.0
		DMDocument.versionIdentifierValue = InfoModel.identifier_version_id;
		
		// Initialize the master information model 
		DMDocument.masterInfoModel = new  MasterInfoModel ();
		
		// Create and initialize the 11179 classes
		ISO79MDR = new ISO11179MDR ();
		
		Set <String> set1 = DMDocument.docInfo.modelMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String modelId = (String) iter1.next();
			lModelInfo = (ModelDefn) DMDocument.docInfo.modelMap.get(modelId);
//			System.out.println("debug modelId:" + modelId + "  ModelInfo.type:" + lModelInfo.type + "   ModelInfo.filename:" + lModelInfo.filename);
			if (lModelInfo.type.compareTo("ProtPinsGlossary") == 0) {
				lModelInfo.objectid  = new ProtPinsGlossary ();
				ProtPinsGlossary lobjectid  = (ProtPinsGlossary) lModelInfo.objectid;
				lobjectid.getProtPinsGlossary(modelId, DMDocument.dataDirPath + lModelInfo.filename);
			} else if (lModelInfo.type.compareTo("ProtPontModel") == 0) {
				lModelInfo.objectid  = new ProtPontModel ();
				ProtPontModel lobjectid  = (ProtPontModel) lModelInfo.objectid;
				lobjectid.initInfoModel();
				lobjectid.getProtModel(modelId, DMDocument.dataDirPath + lModelInfo.filename);
			}
		}
				
		// get the master object dictionary
		getMasterObjectDict();
		
		// debug print the master list of objects
// 		System.out.println("\n ================================ Master Class Array Dump ========================================");
//		InfoModel.printObjectAllDebug(111, InfoModel.masterMOFClassArr);
		
//		DOMInfoModel.domWriter(InfoModel.masterDOMClassArr, "DOMModelListTemp.txt");
//		DOMInfoModel.domWriter(InfoModel.masterDOMClassArr, "DOMModelListPerm.txt");
		
// 		Display one object
//		InfoModel.printObjectDebug (555, InfoModel.masterMOFClassIdMap.get("0001_NASA_PDS_1.cart.Planar_Coordinate_Information"));

// 444		
		// debug print the master list of rules
//		System.out.println("\n ================================ Master Rule Array Dump ========================================");
//		ArrayList <RuleDefn> dumpRuleDefnArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
//		System.out.println("<<<RuleDump Begin - getModels>>>");
//		InfoModel.printRulesAllDebug (222, dumpRuleDefnArr);
//		System.out.println("<<<RuleDump End - getModels>>>");
		
		MasterInfoModel.checkSameNameOverRide ();
		
		// debug print permissible values
//		InfoModel.writePermissibleValues ("debug - GetModels 1", InfoModel.masterMOFAttrIdMap.get("0001_NASA_PDS_1.pds.Units_of_Spectral_Irradiance.pds.unit_id"));
		
		System.out.println("\n>>info    - Counts");
		System.out.println(">>info    - Classes: " + InfoModel.masterMOFClassArr.size());
		System.out.println(">>info    - Attributes: " + InfoModel.masterMOFAttrMap.size());
		System.out.println(">>info    - Rules: " + InfoModel.schematronRuleArr.size());
		System.out.println(" ");

		
		
		
		if (DMDocument.debugFlag) System.out.println("debug getModels Done");
		
	}
	
/**********************************************************************************************************
	 get the master object dictionary
***********************************************************************************************************/
	
	void getMasterObjectDict () throws Throwable {
		// initialize master object dictionary with classes from the individual protege models
		initMasterObjectDict ();
		
// 999		System.out.println("\n>>info    - Master Attribute Map Sizes - InfoModel.masterMOFAttrMap.size():" + InfoModel.masterMOFAttrMap.size());
// 999		System.out.println(">>info                                 - InfoModel.masterMOFAttrIdMap.size():" + InfoModel.masterMOFAttrIdMap.size());
// 999		System.out.println(">>info                                 - InfoModel.masterMOFAttrArr.size():" + InfoModel.masterMOFAttrArr.size());
// 999		System.out.println(" ");

		// set up the LDDToolSingletonClass - The following classes need to be defined:USER, Discipline_Area, and Mission_Area
		
		if (DMDocument.LDDToolSingletonClassTitle.compareTo("USER") == 0) {
			DMDocument.LDDToolSingletonClass = InfoModel.masterMOFUserClass;
			System.out.println(">>info    - getMasterObjectDict - Set LDDToolSingletonClass - DMDocument.LDDToolSingletonClass.title:" + DMDocument.LDDToolSingletonClass.title);
		} else {
			String lClassId = InfoModel.getClassIdentifier (DMDocument.masterNameSpaceIdNCLC, DMDocument.LDDToolSingletonClassTitle);
			PDSObjDefn lLDDToolSingletonClass = InfoModel.masterMOFClassIdMap.get(lClassId);
			if (lLDDToolSingletonClass != null) {
				DMDocument.LDDToolSingletonClass = lLDDToolSingletonClass;
				System.out.println(">>info    - getMasterObjectDict - Found LDDToolSingletonClass - DMDocument.LDDToolSingletonClass.title:" + DMDocument.LDDToolSingletonClass.title);
			} else {
				System.out.println(">>error   - getMasterObjectDict - Could not find LDDToolSingletonClass - lClassId:" + lClassId);
			}
		}
		
		// set the attrParentClass (attributes parent class) from the class name (temp fix)
		DMDocument.masterInfoModel.setAttrParentClass (false); // master run (LDD run is below)
		
		// update the class orders from the 11179 data dictionary
		ISO79MDR.getClassOrder();
		
		// parse UpperModel.pins file and get Science Facets for example
		ProtPinsModel protPinsUpperModel  = new ProtPinsModel ();
		protPinsUpperModel.getProtPinsModel(DMDocument.registrationAuthorityIdentifierValue, DMDocument.dataDirPath + "UpperModel.pins");

		// get custom rules from UpperModel.pins file
		protPinsUpperModel.getRulesPins ();
		
// 444 copy in parsed rules from uppermodel.pins
		// clear customized rules provide by JAVA code
//		InfoModel.schematronRuleMap.clear();
//		InfoModel.schematronRuleIdMap.clear();
		
		ArrayList <RuleDefn> testRuleDefnArr = new ArrayList <RuleDefn> (protPinsUpperModel.testRuleDefnMap.values());
		for (Iterator <RuleDefn> i = testRuleDefnArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			InfoModel.schematronRuleMap.put(lRule.rdfIdentifier, lRule);
			InfoModel.schematronRuleIdMap.put(lRule.identifier, lRule);
		}
		InfoModel.schematronRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleIdMap.values());		
		
		// get list of USER attributes (owned attributes)
		// This must be done before LDD parsing since it needs the USER attributes
		// The attributes are updated later (data type, etc)
		DMDocument.masterInfoModel.getUserClassAttrIdMap();
		
		// if this is an LDD Tool run, parse the LDD(s)
		if (DMDocument.LDDToolFlag) {
			for (Iterator <SchemaFileDefn> i = DMDocument.LDDSchemaFileSortArr.iterator(); i.hasNext();) {
				SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
				LDDParser lLDDParser = new LDDParser ();
				DMDocument.LDDModelArr.add(lLDDParser);
				DMDocument.primaryLDDModel = lLDDParser; 		// last in array is the primary.
				lLDDParser.gSchemaFileDefn = lSchemaFileDefn;	// the schema definition file for this LDDParser.
				lLDDParser.getLocalDD();
			}
		}		

		// set the attrParentClass (attributes parent class) from the class name (temp fix)
		DMDocument.masterInfoModel.setAttrParentClass (true); // LDD run (master run is above)

		// set up master unitsOfMeasure map
		DMDocument.masterInfoModel.setMasterUnitOfMeasure ();
		
		// get the subClassOf attribute for each class
		DMDocument.masterInfoModel.getSubClassOf ();
		
		// remove the URI (:NAME) attribute from each class
		DMDocument.masterInfoModel.removeURIAttribute ();
		
		// get inherited attributes and associations and get restricted attributes				
		DMDocument.masterInfoModel.fixInheritance ("USER", InfoModel.masterMOFUserClass, new ArrayList <PDSObjDefn> ());
			
		//	iterate through the classes and get all subclasses				
		DMDocument.masterInfoModel.getSubClasses ();	
		
		// get the attribute and associations for each class; also get the owned attribute array (culled of overrides)
		DMDocument.masterInfoModel.getAttrAssocArr ();		
		
		//	fix the namespaces				
//		DMDocument.masterInfoModel.fixNameSpaces ();
				
		//	set the attribute isUsedInClass flag				
		setMasterAttrisUsedInClassFlag ();	
		
		// ******* overwrite master attributes from the 11179 DD *******
		ISO79MDR.OverwriteFrom11179DataDict();		
		
		// overwrite any LDD attributes from the cloned USER attributes
		// this is not really needed since the definitions are in the external class
		// the error resulted from checkSameNameOverRide - maybe code needs to added here to 
		// ignore USER clones.
		if (DMDocument.LDDToolFlag) {
			for (Iterator <LDDParser> i = DMDocument.LDDModelArr.iterator(); i.hasNext();) {
				LDDParser lLDDParser = (LDDParser) i.next();
				lLDDParser.OverwriteFrom11179DataDict();
			}
		}
		
		// ******* overwrite completed - start final cleanup *******
		
		// general master attribute fixup
		// anchorString; sort_identifier; sort attribute.valArr
		DMDocument.masterInfoModel.setMasterAttrGeneral ();
		
		// set up anchor strings for classes; requires final class namespaces.
		DMDocument.masterInfoModel.setMasterClassAnchorString ();

		// get the permissible values from schematron statements (for example reference_type)
		DMDocument.masterInfoModel.getAttributePermValuesExtended ();
				
		// set the registration status
		DMDocument.masterInfoModel.setRegistrationStatus ();				

		// set up master data type map and array - just the attributes
		DMDocument.masterInfoModel.setMasterDataType ();

		// set up master data types - the data type map
		DMDocument.masterInfoModel.setMasterDataType2 ();
		
		// set up master Data Element Concept array
		DMDocument.masterInfoModel.GetMasterDECMaps ();

		// set up master conceptual domain array
		DMDocument.masterInfoModel.GetMasterCDMaps ();
						
		// copy in dataType attribute xmlBaseDataType from dataType definitions
		DMDocument.masterInfoModel.SetMasterAttrXMLBaseDataTypeFromDataType ();
		
		// set data type and unit of measure flags
// 445		DMDocument.masterInfoModel.setMasterDataTypeAndUnitOfMeasureFlagsAttr ();
		
		// validate the data types
		DMDocument.masterInfoModel.ValidateAttributeDataTypes ();

		// validate the attribute data types
//		DMDocument.masterInfoModel.CheckDataTypes ();
				
		// set the attribute override flag	
		DMDocument.masterInfoModel.sethasAttributeOverride1 (InfoModel.masterMOFAttrArr);
		DMDocument.masterInfoModel.sethasAttributeOverride2 (InfoModel.masterMOFAttrArr);
		
		// get the valClassArr using valArr; for assocs (AttrDefn)
		DMDocument.masterInfoModel.getValClassArr();

		// set the class isAnExtension and isARestriction flags
		DMDocument.masterInfoModel.setIsAnExtensionAndIsARestrictionClassFlags ();

// 		***********************************************************************		
// 		all updates to class and attributes have been made; extracts can now be done.

		// Get the attribute's CD and DEC values
		DMDocument.masterInfoModel.getCDDECIndexes ();
				
		// Get the attribute's DEC values
//		DMDocument.masterInfoModel.getDECAssocs ();
		
		// get the USER attributes (not owned attributes)
		DMDocument.masterInfoModel.getUserSingletonClassAttrIdMap();
		
		// get the LDDToolSingletonClass, the class for LDD singleton attributes (Discipline or Mission)
		// *** TBD ***
		if (DMDocument.LDDToolFlag) {
			String lClassIdentifier;
			if (DMDocument.LDDToolMissionGovernanceFlag) {
				lClassIdentifier = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, "Mission_Area");
			} else {
				lClassIdentifier = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, "Discipline_Area");
			}
			PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lClassIdentifier);
			if (lClass != null) {
				InfoModel.LDDToolSingletonClass = lClass;
			} else {
				InfoModel.LDDToolSingletonClass = InfoModel.masterMOFUserClass;				
			}
		}
		
		// generate the schematron rules (does not include custom rules)
		GenSchematronRules genSchematronRules = new GenSchematronRules ();
		genSchematronRules.genSchematronRules();
					
		// setup 11179 classes from master attributes
		ISO79MDR.ISO11179MDRSetup(DMDocument.masterInfoModel);					
				
		// if this is an LDD Tool run, validate and write reports for the parsed LDD
		if (DMDocument.LDDToolFlag) {
//			InfoModel.printOneAttributeIdentifier ("0001_NASA_PDS_1.disp.Color_Display_Settings.pds.comment");						
			for (Iterator <LDDParser> i = DMDocument.LDDModelArr.iterator(); i.hasNext();) {
				LDDParser lLDDParser = (LDDParser) i.next();
				lLDDParser.finishCloneOfLDDUserAttributes();
				lLDDParser.validateLDDAttributes();
			}
			DMDocument.primaryLDDModel.writeLocalDDFiles(DMDocument.masterLDDSchemaFileDefn);
		}
		
		// set the class version identifiers (stop gap until class are stored in OWL
		DMDocument.masterInfoModel.setClassVersionIds ();
//		DMDocument.masterInfoModel.dumpClassVersionIds ();
		
		// set exposed flag
		for (Iterator <String> i = DMDocument.exposedElementArr.iterator(); i.hasNext();) {
			String lIdentifier = (String) i.next();
			PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lIdentifier);
			if (lClass != null) lClass.isExposed = true;
		}

		if (DMDocument.debugFlag) System.out.println("debug getMasterObjectDict Done");
	}

/**********************************************************************************************************
	Initialize the Master Class Map
***********************************************************************************************************/
	
	void initMasterObjectDict () {
		// setup the class array and maps
		initMasterClassDict();
		
		// build the remaining class maps and array (sorted by identifier)
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
		for (Iterator<PDSObjDefn> j = lClassArr.iterator(); j.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) j.next();
			InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
			InfoModel.masterMOFClassTitleMap.put(lClass.title, lClass);			
		}
		InfoModel.masterMOFClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());
		
		// setup the association and attribute (AttrDefn) master maps
		// also set up the Property (AssocDefn) combined master map
		initMasterAssocDict();
		initMasterAttrDict();
		
		// build the remaining attribute (attr and assoc) maps and array (sorted by identifier)
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrMap.values());
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
		}
		InfoModel.masterMOFAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
		
		// build the remaining property maps and array (sorted by identifier)
//		ArrayList <AssocDefn> lPropArr = new ArrayList <AssocDefn> (InfoModel.masterMOFPropMap.values());
//		for (Iterator<AssocDefn> j = lPropArr.iterator(); j.hasNext();) {
//			AssocDefn lProp = (AssocDefn) j.next();
//			InfoModel.masterMOFPropIdMap.put(lProp.identifier, lProp);
//		}
//		InfoModel.masterMOFPropArr = new ArrayList <AssocDefn> (InfoModel.masterMOFPropIdMap.values());
		
		// build the remaining association maps and array (sorted by identifier)
		ArrayList <AssocDefn> lAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocMap.values());
		for (Iterator<AssocDefn> j = lAssocArr.iterator(); j.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) j.next();
			InfoModel.masterMOFAssocIdMap.put(lAssoc.identifier, lAssoc);
		}
		InfoModel.masterMOFAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());

	}		

	void initMasterClassDict () {
		//	iterate through the section for section content		
		for (Iterator <String> i = DMDocument.docInfo.sectionArray.iterator(); i.hasNext();) {
			String secId = (String) i.next();
			SectionDefn secInfo = (SectionDefn) DMDocument.docInfo.sectionMap.get(secId);
			if (! secInfo.includeFlag) { continue; }
			if (secInfo.secSubType.compareTo("table") == 0) {
//				System.out.println("      - checking document secInfo.title:" + secInfo.title);					
				//	iterate through the section content for the models
				for (Iterator <String> j = secInfo.sectionModelContentId.iterator(); j.hasNext();) {
					String lContId = (String) j.next();
					SectionContentDefn content = (SectionContentDefn) DMDocument.docInfo.sectionContentMap.get(lContId);
					lModelInfo = (ModelDefn) DMDocument.docInfo.modelMap.get(content.modelId);
					ProtPontModel lmodel = (ProtPontModel) lModelInfo.objectid;

					// iterate through the model for the class definitions
					ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (lmodel.objDict.values());
					for (Iterator <PDSObjDefn> k = lClassArr.iterator(); k.hasNext();) {
						PDSObjDefn lClass = (PDSObjDefn) k.next();
						if (! lClass.isMasterClass) continue;
						if (! InfoModel.masterMOFClassMap.containsKey(lClass.rdfIdentifier)) {
							InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
						} else {
//							System.out.println(">>error    - Duplicate Found - ADDING class lClass.rdfIdentifier:" + lClass.rdfIdentifier);
						}
					}
				}
			}	
		}			
	}	
	
	// init the master attribute and property maps
	public void initMasterAttrDict () {
		// iterate through the classes
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
		
			//	get non-INSTANCE attributes - i.e. standard attributes
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				
				// add attributes
				if (! InfoModel.masterMOFAttrMap.containsKey(lAttr.rdfIdentifier)) {
					InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
				} else {
					System.out.println(">>error    - Duplicate Found - ADDING Attribute lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
				}
								
				// add MOF Properties (type attribute)
				if (InfoModel.masterMOFAssocMap.get(lAttr.rdfIdentifier) == null) {
					AssocDefn lAssoc = new AssocDefn ();
					lAssoc.createAssocSingletonsFromAttr(lAttr);
/*					lAssoc.rdfIdentifier = lAttr.rdfIdentifier;
					lAssoc.identifier = lAttr.identifier;		
					lAssoc.title = lAttr.title;
					lAssoc.className = lAttr.parentClassTitle;
					lAssoc.attrNameSpaceId = lAttr.attrNameSpaceId;
					lAssoc.attrNameSpaceIdNC = lAttr.attrNameSpaceIdNC;
					lAssoc.classNameSpaceIdNC = lAttr.classNameSpaceIdNC;
//					lAssoc.classOrder = lClassOrderStr;
					lAssoc.isAttribute = lAttr.isAttribute; // true
					lAssoc.cardMin = lAttr.cardMin;
					lAssoc.cardMax = lAttr.cardMax;
					lAssoc.cardMinI = lAttr.cardMinI; 
					lAssoc.cardMaxI = lAttr.cardMaxI; */
					lAssoc.referenceType = "attribute_of";
					InfoModel.masterMOFAssocMap.put(lAttr.rdfIdentifier, lAssoc);
				}
			}
		}
	}	
	
	// init the master attribute and property maps
	public void initMasterAssocDict () {
		// iterate through the classes
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
		    // get INSTANCE attributes - i.e. associations
			for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				
				// add associations
				if (! InfoModel.masterMOFAttrMap.containsKey(lAttr.rdfIdentifier)) {
					InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
				}
								
				// add MOF properties (type association)
				if (InfoModel.masterMOFAssocMap.get(lAttr.rdfIdentifier) == null) {
					AssocDefn lAssoc = new AssocDefn ();
					lAssoc.createAssocSingletonsFromAttr(lAttr);
/*					lAssoc.rdfIdentifier = lAttr.rdfIdentifier;
					lAssoc.identifier = lAttr.identifier;			
					lAssoc.title = lAttr.title;
					lAssoc.className = lAttr.parentClassTitle;
					lAssoc.attrNameSpaceId = lAttr.attrNameSpaceId;
					lAssoc.attrNameSpaceIdNC = lAttr.attrNameSpaceIdNC;
					lAssoc.classNameSpaceIdNC = lAttr.classNameSpaceIdNC;
//					lAssoc.classOrder = lClassOrderStr;
					lAssoc.isAttribute = lAttr.isAttribute;	// false
					lAssoc.cardMin = lAttr.cardMin;
					lAssoc.cardMax = lAttr.cardMax;
					lAssoc.cardMinI = lAttr.cardMinI; 
					lAssoc.cardMaxI = lAttr.cardMaxI; */
					lAssoc.referenceType = lAttr.title;
					InfoModel.masterMOFAssocMap.put(lAttr.rdfIdentifier, lAssoc);
					
					// add the child classes
					for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
						String lTitle = (String) k.next();
						if (lTitle == null) continue;
						String lClassMemberIdentifier = InfoModel.getClassIdentifier(lAttr.attrNameSpaceIdNC, lTitle);
						PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassIdMap.get(lClassMemberIdentifier);
						if (lClassMember != null) {
							lAssoc.childClassArr.add (lClassMember);
						} else {
							System.out.println(">>error   - Could not find the class referenced in an association - Name:" + lTitle+ "   Class:" + lClass.title);
						}
					}
				}
			}	
		}
	}	
			
	/**
	*  set the isUsedInClass flag
	*/
	public void setMasterAttrisUsedInClassFlag () {

//		iterate through the classes 			
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				lAttr.isUsedInClass = true;
			}
			for (Iterator <AttrDefn> j = lClass.inheritedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				lAttr.isUsedInClass = true;
			}
			for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				lAttr.isUsedInClass = true;
			}
			for (Iterator <AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				lAttr.isUsedInClass = true;
			}
		}
		
		return;
	}
	
/**********************************************************************************************************
		get the document info
***********************************************************************************************************/
	
	public void getDocInfo (HashMap <String, InstDefn> instMap) throws Throwable {
		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			HashMap <String, ArrayList<String>> slotMap = (HashMap<String, ArrayList<String>>) localInst.genSlotMap;
			if (localInst.className.compareTo("Document") == 0) {
				String docId = getSlotMapValue (slotMap.get("identifier"));
				DMDocument.docInfo = new DocDefn(docId);
				DMDocument.docInfo.title = getSlotMapValue (slotMap.get("title"));
//				ArrayList valarr = (ArrayList) localInst.genSlotMap.get("subtitle");
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("subtitle");
				if (valarr != null) {
					DMDocument.docInfo.subTitle = (String) valarr.get(0);
					// blank seems more appropriate
					if (DMDocument.docInfo.subTitle.compareTo("Final") == 0) DMDocument.docInfo.subTitle = "";
				} else {
					DMDocument.docInfo.subTitle = "DRAFT";
				}
				DMDocument.docInfo.description = getSlotMapValue (slotMap.get("description"));
				DMDocument.docInfo.author = getSlotMapValue (slotMap.get("author"));
				DMDocument.docInfo.version = getSlotMapValue (slotMap.get("version"));
			}
		}
	}
	
/**********************************************************************************************************
		get the document sections
***********************************************************************************************************/
	
	public void getSections (HashMap <String, InstDefn> instMap) throws Throwable {

		String tflag;

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			if (localInst.className.compareTo("Section") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier");
				String sectionId = (String) valarr.get(0);
				docSection = new SectionDefn(sectionId);
				DMDocument.docInfo.sectionMap.put(sectionId, docSection);
				DMDocument.docInfo.sectionArray.add(sectionId);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("title"); docSection.title = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("description"); docSection.description  = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secType"); docSection.secType = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secSubType"); docSection.secSubType = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("selectConstraint"); docSection.selectConstraint = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("latexFormat"); Integer Iind = new Integer ((String) valarr.get(0));
				int ind = Iind.intValue();
				if (ind == 0) {
					docSection.texFormatInd = 0;
				} else {
					ind = ind - 1;
					if (ind < 0 || ind >= texSectionFormats.size()) {
						docSection.texFormatInd = 0;
					} else {
						docSection.texFormatInd = ind;
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secTOCFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.secTOCFlag = true;
				} else {
					docSection.secTOCFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("subSecTOCFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.subSecTOCFlag = true;
				} else {
					docSection.subSecTOCFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("includeFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.includeFlag = true;
				} else {
					docSection.includeFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("imageFileName"); docSection.imageFileName = (String) valarr.get(0);
				if (docSection.imageFileName.compareTo("none") == 0) {
					docSection.imageFlag = false;
				} else {
					docSection.imageFlag = true;
					valarr = (ArrayList <String>) localInst.genSlotMap.get("imageCaption"); docSection.imageCaption = (String) valarr.get(0);
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Section_Model_Content_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSection.sectionModelContentId.add(val);
					}
				}
			}
		}
	}

/**********************************************************************************************************
		get the document models
***********************************************************************************************************/
	
	public void getModels2 (HashMap <String, InstDefn> instMap) throws Throwable {

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			if (localInst.className.compareTo("Model") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier");
				String modelId = (String) valarr.get(0);
				docModel = new ModelDefn(modelId);
				DMDocument.docInfo.modelMap.put(modelId, docModel);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("type"); docModel.type = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("filename"); docModel.filename  = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("DDIncludeFlag"); String tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docModel.ddincludeflag = true;
				} else {
					docModel.ddincludeflag = false;
				}
			}
		}
	}	

/**********************************************************************************************************
		get the document section contents
***********************************************************************************************************/
	
	public void getSectionContent (HashMap <String, InstDefn> instMap) throws Throwable {

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();

//			System.out.println("debug getSectionContent instRDFId:" + instRDFId);
			if (localInst.className.compareTo("Section_Model_Content") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier"); 
				String contentId = (String) valarr.get(0);
				docSectionContent = new SectionContentDefn(contentId);
				DMDocument.docInfo.sectionContentMap.put(contentId, docSectionContent);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Model_Identifier"); docSectionContent.modelId = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Include_Class_Type");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.includeClassType.add(val);
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Include_Class_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.includeClassId.add(val);
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Exclude_Class_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.excludeClassId.add(val);
					}
				}
			}
		}
	}
				
/**********************************************************************************************************
		miscellaneous routines
***********************************************************************************************************/
		
	/**
	*  Get Slot Value
	*/
	public String getSlotMapValue (ArrayList <String> valarr) {
		if (! (valarr == null || valarr.isEmpty())) {
			return (String) valarr.get(0);
		}
		return null;
	}

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
	
	public void printInst (HashMap <String, InstDefn> instMap) throws Throwable {
		Set <String> set1 = instMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String instRDFId = (String) iter1.next();
			System.out.println("\ndebug instRDFId:" + instRDFId);
			InstDefn localInst = (InstDefn) instMap.get(instRDFId);
			System.out.println("      rdfIdentifier:" + localInst.rdfIdentifier);
			System.out.println("      identifier:" + localInst.identifier);
			System.out.println("      title:" + localInst.title);
//			System.out.println("      classifiedIdentifier:" + localInst.classifiedIdentifier);
			System.out.println("      className:" + localInst.className);
			System.out.println("      description:" + localInst.description);
			Set <String> set2 = localInst.genSlotMap.keySet();
			Iterator <String> iter2 = set2.iterator();
			while(iter2.hasNext()) {
				String aname = (String) iter2.next();
				System.out.println("      attribute:" + aname);
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get(aname);
				if (valarr != null) {
					for (Iterator <String> i = valarr.iterator(); i.hasNext();) {
						String val = (String) i.next();
						System.out.println("        val:" + val);
					}
				}
			}
		}
	}	
}
