package gov.nasa.pds.model.plugin; 
import java.util.*;

class MasterInfoModel extends InfoModel{ 
	static String attrNameClassWord;
	
	public MasterInfoModel () {
		// initialize the class structures
		InfoModel.masterMOFClassArr = new ArrayList <PDSObjDefn> ();
		InfoModel.masterMOFClassMap = new TreeMap <String, PDSObjDefn> ();
		InfoModel.masterMOFClassIdMap = new TreeMap <String, PDSObjDefn> ();
		InfoModel.masterMOFClassTitleMap = new TreeMap <String, PDSObjDefn> ();
		
		// create the USER class - the root of all classes and all namespaces
		String lTitle = DMDocument.masterUserClassName;
		String lClassNameSpaceIdNC = DMDocument.masterUserClassNamespaceIdNC;
		String lClassRdfIdentifier = DMDocument.rdfPrefix + "." + lClassNameSpaceIdNC + "." + lTitle + "." + getNextUId();
		PDSObjDefn lClass = new PDSObjDefn(lClassRdfIdentifier);
		lClass.identifier = InfoModel.getClassIdentifier(lClassNameSpaceIdNC, lTitle);
		lClass.nameSpaceIdNC = lClassNameSpaceIdNC;
		lClass.nameSpaceId = lClassNameSpaceIdNC + ":";
		lClass.subClassOfTitle = "N/A";
		lClass.subClassOfIdentifier = "N/A";
		lClass.rootClass = "N/A";
		lClass.baseClassName = "N/A";
		lClass.description = "The root class.";
		lClass.steward = DMDocument.masterNameSpaceIdNCLC;
		lClass.title = lTitle;
		lClass.role = "abstract";
		lClass.isAbstract = true;
		lClass.isUSERClass = true;
		lClass.docSecType = lTitle;
		lClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
		lClass.subModelId = "ROOT";
		lClass.docSecType = lTitle;

		InfoModel.masterMOFUserClass = lClass;
		InfoModel.masterMOFClassArr.add(lClass);
		InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
		InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
		InfoModel.masterMOFClassTitleMap.put(lClass.title, lClass);		
		
		// initialize the attribute structures
		InfoModel.masterMOFAttrMap = new TreeMap <String, AttrDefn> (); 
		InfoModel.masterMOFAttrIdMap = new TreeMap <String, AttrDefn> (); 
		InfoModel.masterMOFAttrArr = new ArrayList <AttrDefn> ();
		InfoModel.userClassAttrIdMap = new TreeMap <String, AttrDefn> ();
		InfoModel.userSingletonClassAttrIdMap = new TreeMap <String, AttrDefn> ();
		
		// initialize the Property structures
//		InfoModel.masterMOFPropMap = new TreeMap <String, AssocDefn> (); 
//		InfoModel.masterMOFPropIdMap = new TreeMap <String, AssocDefn> (); 
//		InfoModel.masterMOFPropArr = new ArrayList <AssocDefn> (); 

		// initialize the association structures
		InfoModel.masterMOFAssocMap = new TreeMap <String, AssocDefn> (); 
		InfoModel.masterMOFAssocIdMap = new TreeMap <String, AssocDefn> (); 
		InfoModel.masterMOFAssocArr = new ArrayList <AssocDefn> (); 
				
		// initialize the global data types
		InfoModel.masterDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		InfoModel.masterDataTypeMap2 = new TreeMap <String, DataTypeDefn> ();

		// initialize the global unitOfMeasure
		InfoModel.masterUnitOfMeasureMap = new TreeMap <String, UnitDefn> ();

		// initialize the global property map
		InfoModel.masterPropertyMapsMap = new TreeMap <String, PropertyMapsDefn> ();
		InfoModel.masterPropertyMapsArr = new ArrayList <PropertyMapsDefn> (); 	

		return;
	}
	
/**********************************************************************************************************
	Set the DEC and CD for each attribute.
***********************************************************************************************************/
	
	public void GetMasterDECMaps () {		
		//set the DEC maps
		AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(DMDocument.masterPDSSchemaFileDefn.regAuthId + "." + DMDocument.masterPDSSchemaFileDefn.identifier + "." + "DD_Attribute_Full.pds.attribute_concept");
		
		if (lAttr == null) {
			System.out.println("***error*** system attribute - attribute_concept - MISSING");
			return;
		}
		if (lAttr.valArr == null) {
			System.out.println("***error*** - system attribute - attribute_concept - NO PERMISSIBLE VALUES");
			return;
		}
		if (lAttr.valArr.size() < 1) {
			System.out.println("***error*** - system attribute - attribute_concept - NO PERMISSIBLE VALUES");
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lDECTitle = (String) i.next();
			String lDECId = "DEC_" + lDECTitle;
			InfoModel.cdID2CDTitleMap.put(lDECId, lDECTitle);
			InfoModel.cdTitle2CDIDMap.put(lDECTitle, lDECId);
		}
	}
		
	public void GetMasterCDMaps () {		
		//set the CD maps
		AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(DMDocument.masterPDSSchemaFileDefn.regAuthId + "." + DMDocument.masterPDSSchemaFileDefn.identifier + "." + "DD_Value_Domain_Full.pds.conceptual_domain");

		if (lAttr == null) {
			System.out.println("***error*** system attribute - conceptual_domain - MISSING");
			return;
		}
		if (lAttr.valArr == null) {
			System.out.println("***error*** - system attribute - conceptual_domain - NO PERMISSIBLE VALUES");
			return;
		}
		if (lAttr.valArr.size() < 1) {
			System.out.println("***error*** - system attribute - conceptual_domain - NO PERMISSIBLE VALUES");
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lCDTitle = (String) i.next();
			String lCDId = "CD_" + lCDTitle;
			InfoModel.cdID2CDTitleMap.put(lCDId, lCDTitle);
			InfoModel.cdTitle2CDIDMap.put(lCDTitle, lCDId);
		}
	}
				
/**********************************************************************************************************
		General Update Routines
***********************************************************************************************************/
	
	//	set the attrParentClass (attributes parent class) from the class name (temp fix)
	public void setAttrParentClass (boolean forLDD) {		
//		System.out.println("\n\n\n\ndebug setAttrParentClass");
		// for each PDS4 attribute set the attrParentClass
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("\ndebug setAttrParentClass lAttr.identifier:" + lAttr.identifier);
			if (forLDD && ! lAttr.isFromLDD) continue;
			if (! forLDD && lAttr.isFromLDD) continue;
			String lClassId = InfoModel.getClassIdentifier (lAttr.classNameSpaceIdNC, lAttr.parentClassTitle);
			PDSObjDefn lParentClass = InfoModel.masterMOFClassIdMap.get(lClassId);
			if (lParentClass != null) {
				lAttr.attrParentClass = lParentClass;
//				System.out.println("debug setAttrParentClass FOUND - lAttr.attrParentClass.title:" + lAttr.attrParentClass.title);
			} else {
				boolean classNotFound = true;
				for (Iterator<PDSObjDefn> k = InfoModel.masterMOFClassArr.iterator(); k.hasNext();) {
					lParentClass = (PDSObjDefn) k.next();
					if (lParentClass.title.compareTo(lAttr.parentClassTitle) == 0) {
//						System.out.println("debug getValClassArr FOUND Using Scan - lClassMember.identifier:" + lClassMember.identifier);
						if (classNotFound) {
							classNotFound = false;
							lAttr.attrParentClass = lParentClass;
							System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - using first found - lClassMember.identifier:" + lParentClass.identifier);
						} else {
							System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - also found - lClassMember.identifier:" + lParentClass.identifier);
						}
					}
				}
				if (classNotFound) {
					lAttr.attrParentClass = InfoModel.masterMOFUserClass;
					System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - parent class not found, using USER - lClassMember.identifier:" + lParentClass.identifier);
				}
			}
		}
	}	
	
	//	get the valClassArr from the valArr for each association (AttrDefn)
	public void getValClassArr () {		
		// for each PDS4 attribute set the meta attribute from the data type
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isAttribute) continue;		// ignore, not an association
			if (lAttr.valArr == null || lAttr.valArr.isEmpty()) continue;			
//			System.out.println("\ndebug getValClassArr - Association - lAttr.identifier:" + lAttr.identifier);
			for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
				String lTitle = (String) j.next();
//				String lClassMemberIdentifier = InfoModel.getClassIdentifier(lAttr.classNameSpaceIdNC, lTitle);
				String lClassMemberIdentifier = InfoModel.getClassIdentifier(lAttr.attrNameSpaceIdNC, lTitle);
//				System.out.println("debug getValClassArr - lClassMemberIdentifier:" + lClassMemberIdentifier);
				PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassIdMap.get(lClassMemberIdentifier);
				if (lClassMember != null) {
					lAttr.valClassArr.add(lClassMember);
//					System.out.println("debug getValClassArr FOUND Using Namespace -" + lAttr.attrNameSpaceIdNC + "- lClassMember.identifier:" + lClassMember.identifier);
				} else {
					PDSObjDefn firstClassFound = null;
					for (Iterator<PDSObjDefn> k = InfoModel.masterMOFClassArr.iterator(); k.hasNext();) {
						lClassMember = (PDSObjDefn) k.next();
						if (lClassMember.title.compareTo(lTitle) == 0) {
//							System.out.println("debug getValClassArr FOUND Using Scan - lClassMember.identifier:" + lClassMember.identifier);
							if (firstClassFound == null) {
								firstClassFound = lClassMember;
								System.out.println(">>warning - get class using attribute value array - lAttr.identifier:" + lAttr.identifier + " - using first found - lClassMember.identifier:" + lClassMember.identifier);
							} else {
								System.out.println(">>warning - get class using attribute value array - lAttr.identifier:" + lAttr.identifier + " - also found - lClassMember.identifier:" + lClassMember.identifier);
							}
						}
					}
					if (firstClassFound != null) lAttr.valClassArr.add(firstClassFound);
				}
			}
		}
	}	

	//	Set Master Attribute XML Base Data Type From the Data Type	
	public void SetMasterAttrXMLBaseDataTypeFromDataType () {		
		// for each PDS4 attribute set the meta attribute from the data type
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttrI = (AttrDefn) i.next();
			if (lAttrI.isPDS4) {
//				System.out.println("\ndebug set other attributes - found PDS4 attribute attrId:" + attrId);
				String lTitle = lAttrI.valueType;					
				String lClassId = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lTitle);
				PDSObjDefn lClass = (PDSObjDefn) masterMOFClassIdMap.get(lClassId);
				if (lClass != null) {
					for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
						AttrDefn lAttr = (AttrDefn) j.next();
						
						// set the xmlBaseDataType
						if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
							String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lAttrI.xmlBaseDataType);
							if (lVal != null) {
								lAttrI.xmlBaseDataType =  lVal;
							}
						}
					}
				}
			}
		}
	}
	
	// general master attribute fixup
	// anchorString; sort_identifier; sorts valArr; get DEC
	// requires final attribute and class namespaces; final valArr;
	public void setMasterAttrGeneral () {
		String lBlanks = "                              ";
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			// set attributes anchor string
			lAttr.attrAnchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.attrNameSpaceIdNC + "_"  + lAttr.title).toLowerCase();

			// set attributes sort identifier
			int lLength = lAttr.title.length();
			if (lLength >= 30) {
				lLength = 30;
			}
			String lPaddedAttrTitle = lAttr.title + lBlanks.substring(0, 30 - lLength);
			
			lLength = lAttr.parentClassTitle.length();
			if (lLength >= 30) {
				lLength = 30;
			}
			String lPaddedClassTitle = lAttr.parentClassTitle + lBlanks.substring(0, 30 - lLength);
			
			lAttr.sort_identifier = lPaddedAttrTitle + "_" + lAttr.steward + "_" + lPaddedClassTitle + "_" + lAttr.classSteward;

			// sort attribute.valarr
			Collections.sort(lAttr.valArr);
		}	
		return;
	}
	
	// get the permissible values from schematron statements (for example reference_type)
	public void getAttributePermValuesExtended () {

		// get an array of the schematron pattern statements
//		ArrayList <RuleDefn> lAssertArrArr = new ArrayList <RuleDefn> (schematronRuleIdMap.values());				
//		for (Iterator<RuleDefn> i = lAssertArrArr.iterator(); i.hasNext();) {
//		for (Iterator<RuleDefn> i = schematronRuleArr.iterator(); i.hasNext();) {
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (schematronRuleMap.values());				
		for (Iterator<RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
//			System.out.println("\ndebug getAttributePermValuesExtended lRule.identifier:" + lRule.identifier);
			// get the affected attribute
			String lAttrId = InfoModel.getAttrIdentifier (lRule.classNameSpaceNC, lRule.classTitle, lRule.attrNameSpaceNC, lRule.attrTitle);			
			AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lAttrId);
			if (lAttr != null) {
			// for each rule get assert statement
				for (Iterator<AssertDefn2> k = lRule.assertArr.iterator(); k.hasNext();) {
					AssertDefn2 lAssert = (AssertDefn2) k.next();
					
					if (lAssert.assertType.compareTo("EVERY") == 0 || lAssert.assertType.compareTo("IF") == 0) {		
						// create a new permissible value entry extended for the attribute
						PermValueExtDefn lPermValueExt = new PermValueExtDefn (lAttr.title);
						lPermValueExt.xpath = lRule.xpath;
						lAttr.permValueExtArr.add(lPermValueExt);
						
						// create a new permissible value entry for each value
						for (Iterator<String> l = lAssert.testValArr.iterator(); l.hasNext();) {
							String lVal = (String) l.next();
							String lValueMeaning = getValueMeaning (lRule, lVal);
							PermValueDefn lPermValue = new PermValueDefn (lAssert.identifier, lVal, lValueMeaning);
							lPermValueExt.permValueExtArr.add(lPermValue);
						}
					}
				}
			}
		}	
		return;
	}	
	
	// get the value meaning
	public String getValueMeaning (RuleDefn lRule, String lVal) {
		String lId = lRule.xpath + "." + lRule.attrTitle + "." + lVal;
//		System.out.println("      getValueMeaning lId:" + lId);
		PermValueDefn lPermVal = GetValueMeanings.masterValueMeaningKeyMap.get(lId);
		if (lPermVal != null) {
			return lPermVal.value_meaning;
		}
		return "TBD_value_meaning";
	}
	
	// set up anchor strings for classes; requires final class namespace.
	public void setMasterClassAnchorString () {
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			lClass.anchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
		}	
		return;
	}				
	
//	get the master data type map and array from the master attribute list - protege classes
	public void setMasterDataType () {
		// iterate through the classes and create a sorted array
		// *** cool code
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			String rootClass = lClass.rootClass;
			if (rootClass.indexOf("Data_Type") > -1) {	
				if (rootClass.indexOf("Data_Type_Component") < 0) {	
					if (lClass.subClasses.isEmpty()) {
						InfoModel.masterDataTypeMap.put(lClass.title, lClass);
					}
				}
			}
		}
		masterDataTypesArr = new ArrayList <PDSObjDefn> (InfoModel.masterDataTypeMap.values());
	}	
	
//	get the master data type map and array from the master attribute list - data type definitions
	public void setMasterDataType2 () {
		// iterate through the classes and create a sorted array
		// *** cool code
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("\ndebug setMasterDataType - lClass.title:" + lClass.title);
//			System.out.println("      setMasterDataType - lClass.rootClass:" + lClass.rootClass);
			if (lClass.title.indexOf("Data_Type") > -1 || lClass.rootClass.indexOf("Data_Type") > -1) {
//				lClass.isDataType = true;
//				System.out.println("debug setMasterDataType - FOUND - lClass.title:" + lClass.title);
				if (lClass.subClasses.isEmpty()) {
					DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(lClass.title);
					if (lDataType == null) {
						// the data type does not exist, add it
						lDataType = new DataTypeDefn (lClass.title);
						lDataType.pds4Identifier = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClass.title);
						lDataType.title = lDataType.identifier;
						lDataType.nameSpaceIdNC = lClass.nameSpaceIdNC;
						lDataType.type = lDataType.identifier;
						InfoModel.masterDataTypeMap2.put(lDataType.title, lDataType);						
						// for each attribute of the class
						for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
							AttrDefn lAttr = (AttrDefn) j.next();								
							// set the character_constraint
//							lAttr.isDataType = true;
							if (lAttr.title.compareTo("character_constraint") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.character_constraint);
								if (lVal != null) {
									lDataType.character_constraint =  lVal;
								}
							}
							// set the formation_rule
							if (lAttr.title.compareTo("formation_rule") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.formation_rule);
								if (lVal != null) {
									lDataType.formation_rule =  lVal;
								}
							}
							// set the maximum_characters
							if (lAttr.title.compareTo("maximum_characters") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.maximum_characters);
								if (lVal != null) {
									lDataType.maximum_characters =  lVal;
								}
							}
							// set the maximum_value
							if (lAttr.title.compareTo("maximum_value") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.maximum_value);
								if (lVal != null) {
									lDataType.maximum_value =  lVal;
								}
							}
							// set the minimum_characters
							if (lAttr.title.compareTo("minimum_characters") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.minimum_characters);
								if (lVal != null) {
									lDataType.minimum_characters =  lVal;
								}
							}
							// set the minimum_value
							if (lAttr.title.compareTo("minimum_value") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.minimum_value);
								if (lVal != null) {
									lDataType.minimum_value =  lVal;
								}
							}
							// set the xml_schema_base_type
							if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.xml_schema_base_type);
								if (lVal != null) {
									lDataType.xml_schema_base_type =  lVal;
								}
							}
							// set the character_encoding
							if (lAttr.title.compareTo("character_encoding") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.character_encoding);
								if (lVal != null) {
									lDataType.character_encoding =  lVal;
								}
							}
							// set the pattern
/*								if (lAttr.title.compareTo("pattern") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lDataType.pattern);
								if (lVal != null) {
									lDataType.pattern =  lVal;
								}
							} */
							if (lAttr.title.compareTo("pattern") == 0) {								
								for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
									String lValue = (String) k.next();
									lDataType.pattern.add(lValue);
								}
							}
						}
					}
				}
			}
		}
		masterDataTypesArr2 = new ArrayList <DataTypeDefn> (InfoModel.masterDataTypeMap2.values());
	}
	
//	set Registration Status
	public void setRegistrationStatus () {
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			// is it a value
			if (lDeprecatedDefn.value.compareTo("") != 0) {
				String lId = InfoModel.getAttrIdentifier (lDeprecatedDefn.classNameSpaceIdNC, lDeprecatedDefn.className, lDeprecatedDefn.attrNameSpaceIdNC, lDeprecatedDefn.attrName);
				AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lId);
				if (lAttr != null) {
					lAttr.hasRetiredValue = true;
					for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
						PermValueDefn lPermValue = (PermValueDefn) j.next();
						if (lPermValue.value.compareTo(lDeprecatedDefn.value) == 0) {
							lPermValue.registrationStatus = "Retired";
						}
					}
				}
			} else {
				// is it a class
				PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lDeprecatedDefn.identifier);
				if (lClass != null) {
					lClass.registrationStatus = "Retired";
				} else {
					// is it an attribute
					AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lDeprecatedDefn.identifier);
					if (lAttr != null) {
						lAttr.registrationStatus = "Retired";
					}
				}				
			}
		}
	}	
		
//	set the master unitOfMeasure
	public void setMasterUnitOfMeasure () {
		// iterate through the classes and create a sorted array
		// *** cool code
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.isUnitOfMeasure) {
				if (lClass.role.compareTo("concrete") == 0) {
					UnitDefn lUnit = InfoModel.masterUnitOfMeasureMap.get(lClass.title);
					if (lUnit == null) {
						// the unit does not exist, add it
						lUnit = new UnitDefn (lClass.title);
						lUnit.pds4Identifier = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClass.title);
						lUnit.title = lUnit.identifier;
						lUnit.type = lUnit.identifier;
						InfoModel.masterUnitOfMeasureMap.put(lUnit.title, lUnit);
						// for each attribute of the class
						for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
							AttrDefn lAttr = (AttrDefn) j.next();
							// set the default_unit_id
							if (lAttr.title.compareTo("specified_unit_id") == 0) {
								String lVal = InfoModel.getSingletonValueUpdate(lAttr.valArr, lUnit.default_unit_id);
								if (lVal != null) {
									lUnit.default_unit_id =  lVal;
								}
							}
							// set the unit_id
							if (lAttr.title.compareTo("unit_id") == 0) {
								ArrayList <String> lValArr = InfoModel.getMultipleValue(lAttr.valArr);
								if (lValArr != null) {
									for (Iterator<String> k = lValArr.iterator(); k.hasNext();) {
										String lVal = (String) k.next();
										lUnit.unit_id.add(lVal); 
									}
								}
							}
						}
						InfoModel.masterUnitOfMeasureMap.put(lUnit.title, lUnit);		
					}
				}
			}
		}
		masterUnitOfMeasureArr = new ArrayList <UnitDefn> (InfoModel.masterUnitOfMeasureMap.values());
	}	
	
//	set data type and unit of measure flags in attributes	
	public void setMasterDataTypeAndUnitOfMeasureFlagsAttrxxx () {
		// iterate through the classes
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("\ndebug setMasterDataType - lClass.title:" + lClass.title);
			if (lClass.isDataType) {
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
// 445					lAttr.isDataType = true;
				}
			}
			if (lClass.isUnitOfMeasure) {
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
// 445					lAttr.isUnitOfMeasure = true;
				}
			}
		}
	}

//	Validate Attribute Data Types
	public void ValidateAttributeDataTypes () {
//		System.out.println("\ndebug ValidatedAttributeDataTypes");
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			if (lAttr.propType.compareTo("ATTRIBUTE") == 0) {
			if (lAttr.isAttribute) {
				if (lAttr.valueType.compareTo("TBD_value_type") == 0) {
//					System.out.println("\ndebug ValidatedAttributeDataTypes lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
//					System.out.println("debug                             lAttr.valueType:" + lAttr.valueType);
					String lProtValType = lAttr.protValType;
//					System.out.println("debug                             lAttr.protValType:" + lAttr.protValType);
					if (! (lAttr.protValType.indexOf("TBD") == 0)) {
						String lValueType = rawValueTypeMap.get(lProtValType);
						if (lValueType == null) {
							lValueType = rawValueTypeMap.get(lAttr.protValType);
							if (lValueType == null) {
								lValueType = "ASCII_Short_String_Collapsed";
							}
						}
						lAttr.valueType = lValueType;
						DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(lValueType);
						if (lDataType != null) {
							lAttr.xmlBaseDataType = lDataType.xml_schema_base_type;
						}
					}
				}

			}
		}
	}	
	
//	CheckDataTypes	
	public void CheckDataTypes () {
		System.out.println("\ndebug CheckDataTypes");
		TreeMap <String, AttrDefn> lTreeMap = new TreeMap <String, AttrDefn>();
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			String rId = lAttr.title + "_" + lAttr.rdfIdentifier;
			lTreeMap.put(rId, lAttr);
		}
		Collection <AttrDefn> values = lTreeMap.values();		
		ArrayList <AttrDefn> lAttrArr = (new ArrayList <AttrDefn> ( values ));
		String pTitle = "", pDataType = "", pRDFId = "";
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			System.out.println("\ndebug CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
			if (lAttr.title.compareTo(pTitle) == 0) {
				if (lAttr.valueType.compareTo(pDataType) == 0) {
					continue;
				} else {
					System.out.println("debug CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
					System.out.println("debug CheckDataTypes data types not equal - pRDFId:" + pRDFId);
					System.out.println("debug CheckDataTypes data types not equal - pDataType:" + pDataType);
					System.out.println("debug CheckDataTypes data types not equal - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
					System.out.println("debug CheckDataTypes data types not equal - lAttr.valueType:" + lAttr.valueType);
				}
			} else {
				pTitle = lAttr.title;
				pDataType = lAttr.valueType;
				pRDFId = lAttr.rdfIdentifier;
			}
		}
	}
		
	//	find attributes that have no overrides; compare to data type
	public void sethasAttributeOverride1 (ArrayList <AttrDefn> lMasterMOFAttrArr) {
		for (Iterator<AttrDefn> i = lMasterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isAttribute) {
//				System.out.println("\ndebug sethasAttributeOverride - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
				DataTypeDefn lValueType = InfoModel.masterDataTypeMap2.get(lAttr.valueType);
								
				if (lValueType == null) {
					System.out.println(">>error   - Could not find a value type for this attribute while checking for attribute overrides - Name:" + lAttr.title + "   Value Type:" + lAttr.valueType);
				} else {
					boolean hasOverride = false;
					if (!((lAttr.minimum_value.indexOf("TBD") == 0) || (lAttr.minimum_value.compareTo(lValueType.minimum_value) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.maximum_value.indexOf("TBD") == 0) || (lAttr.maximum_value.compareTo(lValueType.maximum_value) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.minimum_characters.indexOf("TBD") == 0) || (lAttr.minimum_characters.compareTo(lValueType.minimum_characters) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.maximum_characters.indexOf("TBD") == 0) || (lAttr.maximum_characters.compareTo(lValueType.maximum_characters) == 0))) {
						hasOverride = true;
					}
					if (!(lAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
						hasOverride = true;
					}					
					if (lAttr.pattern.indexOf("TBD") != 0) {
						hasOverride = true;
					}					
					if (hasOverride) {
						lAttr.hasAttributeOverride = true;
					} else {
						// if the attribute does not have an override, then it gets to use it own name in the schema
						lAttr.XMLSchemaName = lAttr.title;
					}
				}
			}
		}
		return;
	}	

	//	for attributes with overrides, the first gets to use its own name in the schema
	//  the others are either equivalent or are forced to use className_attributeName
	public void sethasAttributeOverride2 (ArrayList <AttrDefn> lMasterMOFAttrArr) {
		TreeMap <String, AttrDefn> lSortAttrMap = new TreeMap <String, AttrDefn> ();
		TreeMap <String, ArrayList<AttrDefn>> lTitleAttrsMap = new TreeMap <String, ArrayList<AttrDefn>> ();
		
		// *** Need to add steward to partition attributes ***
		
		// put attributes in sorted order; attr.title_class.title
		for (Iterator<AttrDefn> i = lMasterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if ((lAttr.XMLSchemaName.indexOf("TBD") == 0) && lAttr.isAttribute) {
				
				// all remaining attributes have overrides, i.e. different constraints than those in the data type
//				lAttr.hasAttributeOverride = true;
				lSortAttrMap.put(lAttr.title + "_" + lAttr.parentClassTitle + lAttr.attrNameSpaceId, lAttr);
			}
		}

		// for each attribute title, get the array of all attributes with that title
		ArrayList <String> lTitleArr = new ArrayList <String> ();
		ArrayList <AttrDefn> lSortAttrArr = new ArrayList <AttrDefn> (lSortAttrMap.values());
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> ();
		for (Iterator<AttrDefn> i = lSortAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if(lTitleArr.contains(lAttr.title)) {
				lAttrArr.add(lAttr);
			} else {
				lTitleArr.add(lAttr.title);
				lAttrArr = new ArrayList <AttrDefn> ();
				lAttrArr.add(lAttr);
				lTitleAttrsMap.put(lAttr.title, lAttrArr);
			}
		}

		// iterate through the title/attribute array map
		Set <String> set1 = lTitleAttrsMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lTitle = (String) iter1.next();
			ArrayList <AttrDefn> lAttrArr2 = (ArrayList <AttrDefn>) lTitleAttrsMap.get(lTitle);		

			// iterate through this attribute array 
			boolean isFirst = true;
			for (Iterator<AttrDefn> i = lAttrArr2.iterator(); i.hasNext();) {
				AttrDefn lAttr1 = (AttrDefn) i.next();			

				// if this is  the first attribute, it gets to use its own name
				if (isFirst) {
					lAttr1.XMLSchemaName = lAttr1.title;
					isFirst = false;					
				} else {

					// nested iteration through this attribute array to find the duplicates
					boolean isEquivalentAll = true;
					for (Iterator<AttrDefn> j = lAttrArr2.iterator(); j.hasNext();) {
						AttrDefn lAttr2 = (AttrDefn) j.next();

						// omit checking an attribute against itself
						if (lAttr1.rdfIdentifier.compareTo(lAttr2.rdfIdentifier) == 0) { continue; }
						
						// set one XMLSchemaName at a time 
						if (lAttr2.XMLSchemaName.indexOf("TBD") == 0) {
							break;
						}
						boolean isEquivalent = true;
						if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
							isEquivalent = false;
							System.out.println(">>error   - hasAttributeOverride2 - valueType is not equivalent - attribute identifier:" + lAttr2.identifier);
						}
						if (lAttr1.minimum_value.compareTo(lAttr2.minimum_value) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.maximum_value.compareTo(lAttr2.maximum_value) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.minimum_characters.compareTo(lAttr2.minimum_characters) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.maximum_characters.compareTo(lAttr2.maximum_characters) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.pattern.compareTo(lAttr2.pattern) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.unit_of_measure_type.compareTo(lAttr2.unit_of_measure_type) != 0) {
							isEquivalent = false;
						}
						if (isEquivalent) {
							lAttr1.XMLSchemaName = lAttr2.XMLSchemaName;
							isEquivalentAll = true;
							break;
						} else {
							System.out.println(">>warning - sethasAttributeOverride - attributes are not equivalent:" + lAttr1.identifier + " - " + lAttr2.identifier);
						}
						isEquivalentAll = isEquivalentAll && isEquivalent;
					}
					if (! isEquivalentAll) {
						System.out.println(">>error   - sethasAttributeOverride - attribute is not equivalent - Setting unique name - attribute identifier:" + lAttr1.identifier);
						lAttr1.XMLSchemaName = lAttr1.parentClassTitle + "_" + lAttr1.title;
					}
				}
			}
		}
		return;
	}	
	
//	Get User Class Attributes Id Map
//  The attributes are not cloned, they are simply added to the InfoModel.userClassAttrIdMap
//  with the identifier for the map using "attrNameSpaceIdNC.USER" + "attrNameSpaceIdNC.title"
//  e.g. pds.USER.pds.comment or disp.USER.disp.name
//  There is a many-to-one map so only one attribute survives, however all have same definition
	public void getUserClassAttrIdMap () {
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (! lClass.isMasterClass) continue;
			if (lClass.title.indexOf("PDS3") > -1) continue;   // kludge until boolean is set up
			for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();

				// the namespace of the USER class for any attribute is the same as the namespace of the attribute
//				String lUserAttrIdentifier = InfoModel.getAttrIdentifier (lAttr.attrNameSpaceIdNC, "USER", lAttr.attrNameSpaceIdNC, lAttr.title);
				String lUserAttrIdentifier = InfoModel.getAttrIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lAttr.attrNameSpaceIdNC, lAttr.title);
				InfoModel.userClassAttrIdMap.put(lUserAttrIdentifier, lAttr);
			}
		}
		return;
	}
	
//	Get User Class Attributes Id Map (not owned)	
	public void getUserSingletonClassAttrIdMap () {
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttr.isAttribute) continue;
			if (lAttr.isUsedInClass) continue;
			if (lAttr.title.compareTo("%3ANAME") == 0) continue;
			InfoModel.userSingletonClassAttrIdMap.put(lAttr.identifier, lAttr);	
		}
		return;
	}
	
	/**********************************************************************************************************
		Routines for Finalizing the Ontology
	***********************************************************************************************************/ 
			
			// The hierarchy of classes is then processed downward
			// one level at a time, where each class at the level again processes back
			// up the hierarchy, this time capturing inherited attributes.
		
		/**
		*  get the subClassOf identifier and instances for each class, using the title
		*/
		public void getSubClassOf () {
	//		iterate through the classes and get the subClassOf
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();
				if (lClass.isUSERClass) continue;
//				PDSObjDefn lSupClass = (PDSObjDefn) masterMOFClassIdMap.get(lClass.subClassOfIdentifier);
				PDSObjDefn lSupClass = (PDSObjDefn) masterMOFClassTitleMap.get(lClass.subClassOfTitle);
				if (lSupClass != null) {
					lClass.subClassOfInst = lSupClass;
					lClass.subClassOfTitle = lSupClass.title;
					lClass.subClassOfIdentifier = lSupClass.identifier;
				} else {
					if (lClass.subClassOfTitle.indexOf("TBD") != 0) 
						System.out.println("***error*** - missing superClass in master while trying to set subClassOf - lClass.identifier:" + lClass.identifier + " - lClass.subClassOfTitle:" + lClass.subClassOfTitle);					
				}
			}
			return;
		}
		
		/**
		*  Fixup name spaces and base class name - final cleanup
		*/
		public void fixNameSpaces () {
	
	//		iterate through the classes and get the namespace
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();
				if (lClass.baseClassName.indexOf("TBD") == 0) {
					lClass.baseClassName = "none";
				}
			}
			return;
		}	
	
		/**
		*  remove URI Attribute
		*/
		public void removeURIAttribute () {
			// BEWARE of this code to remove the attribute %3ANAME or the protege :NAME meta attribute	
			// iterate through the classes and remove %3ANAME		
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();			
				int ind = 0, targInd = -1;
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					if (lAttr == null) continue;
					if (lAttr.title.compareTo("%3ANAME") == 0) {
						targInd = ind;
						break;
					}
					ind++;
				}
				if (targInd > -1) {
					lClass.ownedAttrNSTitle.remove(targInd);
					lClass.ownedAttribute.remove(targInd);
				}
			} 
		}
		
		/**
		*  fix the inheritance for all child classes
		*/
		public void fixInheritance (String lDocSecType, PDSObjDefn lParentClass,  ArrayList <PDSObjDefn> lSuperClass) {
			if (lParentClass.title.compareTo("USER") != 0) {
				lSuperClass.add(lParentClass);
			}
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lChildClass = (PDSObjDefn) i.next();
				if (lChildClass.subClassOfIdentifier.compareTo(lParentClass.identifier) == 0) {
 
					// if top level class get the class type that indicates the info model spec document section
					if (lParentClass.title.compareTo("USER") == 0) {
						lDocSecType = lChildClass.title;
					}
					if (lParentClass.subClassOfTitle.compareTo("USER") == 0) {
						lDocSecType = lParentClass.title;
					}
					lChildClass.docSecType = lDocSecType;
					
					// get the inherited and restricted attributes
					getInheritedAttributes (lChildClass, lParentClass);
					getRestrictedAttributes (lChildClass, lParentClass);
					getInheritedAssociations (lChildClass, lParentClass);
					getRestrictedAssociations (lChildClass, lParentClass);
					
					// set the super class
					lChildClass.superClass = lSuperClass;
					
					// get the base class
					getBaseClass (lChildClass, lParentClass);
					
					// recurse down
					fixInheritance (lDocSecType, lChildClass, clonePDSObjDefnArrayList (lSuperClass));
				}
			}
		}	
		
		/**
		*  Check whether class is an extension
		*/
		static public boolean isExtendedClass (PDSObjDefn lClass, PDSObjDefn lSuperClass) {
			if (lSuperClass == null) {
				return false;
			}

			//	check if all owned and inherited attributes are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lSuperClass.ownedAttrNSTitle.contains(lAttr.nsTitle) || lSuperClass.inheritedAttrNSTitle.contains(lAttr.nsTitle))) {
					return true;
				}
			}
			
			//	check if all owned and inherited associations are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lSuperClass.ownedAssocNSTitle.contains(lAttr.nsTitle) || lSuperClass.inheritedAssocNSTitle.contains(lAttr.nsTitle))) {
					return true;
				}
			}
			return false;
		}
				
		static public boolean isRestrictedClass (PDSObjDefn lClass, PDSObjDefn lSuperClass) {
			//	check if any owned attributes is in the super class (owned or inherited)
			if (lSuperClass == null) {
				return false;
			}
			for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (isRestrictedAttribute (true, lAttr, (getPossibleRestrictedAttribute (lAttr, lSuperClass)))) {
					return true;
				}
			}
			//	check if all owned and inherited associations are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (isRestrictedAttribute (false, lAttr, (getPossibleRestrictedAssociation (lAttr, lSuperClass)))) {
					return true;
				}
			}
			return false;
		}
		
		static public AttrDefn getPossibleRestrictedAttribute (AttrDefn lAttr, PDSObjDefn lSuperClass) {
			//	find the attribute by title in the super class.

			for (Iterator<AttrDefn> i = lSuperClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			for (Iterator<AttrDefn> i = lSuperClass.inheritedAttribute.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			return null;
		}
		
		static public AttrDefn getPossibleRestrictedAssociation (AttrDefn lAttr, PDSObjDefn lSuperClass) {
			//	find the attribute by title in the super class.

			for (Iterator<AttrDefn> i = lSuperClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			for (Iterator<AttrDefn> i = lSuperClass.inheritedAssociation.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			return null;
		}
		
		/**
		*  Check whether attribute is restricted
		*/
		static public boolean isRestrictedAttribute (Boolean isAttribute, AttrDefn lAttr, AttrDefn lSuperAttr) {
			// check if the attribute has different meta-attributes
			// for the xml schemas, having a different enumerated list does not count as being different
			
			if (lSuperAttr == null) {
				return false;
			}
			
//			System.out.println("\ndebug isRestrictedAttribute lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
//			System.out.println("      isRestrictedAttribute lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
			
			if (lAttr.valueType.compareTo(lSuperAttr.valueType) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.valueType:" + lSuperAttr.valueType + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.cardMin.compareTo(lSuperAttr.cardMin) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.cardMin:" + lSuperAttr.cardMin + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.cardMax.compareTo(lSuperAttr.cardMax) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.cardMax:" + lSuperAttr.cardMax + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.description.compareTo(lSuperAttr.description) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.description:" + lSuperAttr.description + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.minimum_characters.compareTo(lSuperAttr.minimum_characters) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.minimum_characters:" + lSuperAttr.minimum_characters + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.maximum_characters.compareTo(lSuperAttr.maximum_characters) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.maximum_characters:" + lSuperAttr.maximum_characters + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.minimum_value.compareTo(lSuperAttr.minimum_value) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.minimum_value:" + lSuperAttr.minimum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.maximum_value.compareTo(lSuperAttr.maximum_value) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.maximum_value:" + lSuperAttr.maximum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.attrNameSpaceIdNC.compareTo(lSuperAttr.attrNameSpaceIdNC) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.attrNameSpaceIdNC:" + lSuperAttr.attrNameSpaceIdNC + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.pattern.compareTo(lSuperAttr.pattern) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.pattern:" + lSuperAttr.pattern + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.default_unit_id.compareTo(lSuperAttr.default_unit_id) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.default_unit_id:" + lSuperAttr.default_unit_id + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.format.compareTo(lSuperAttr.format) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.format:" + lSuperAttr.format + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (! isAttribute) { // if association we need  to check the standard values.
				if (lAttr.valArr.size() != lSuperAttr.valArr.size()) {
					System.out.println(">>warning - isRestrictedAttribute lAttr.valArr.size():" + lAttr.valArr.size() + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
					return true;
				}
				for (Iterator<String> i = lAttr.valArr.iterator(); i.hasNext();) {
					String lVal = (String) i.next();
					if (! lSuperAttr.valArr.contains(lVal)) {
						System.out.println(">>warning - isRestrictedAttribute lAttr.lVal" + lVal + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
						return true;
					}
				}
			}
			return false;
		}

		/**
		*  get the inherited attributes
		*/
		private void getInheritedAttributes (PDSObjDefn lChildClass, PDSObjDefn lParentClass) {
			//	inherit all owned attributes of the parent class
			for (Iterator<AttrDefn> i = lParentClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lParentAttr = (AttrDefn) i.next();
				if (! lChildClass.ownedAttrNSTitle.contains(lParentAttr.nsTitle)) {				// not already owned
					if (! lChildClass.inheritedAttrNSTitle.contains(lParentAttr.nsTitle)) {		// not in inherited attribute list
						lChildClass.inheritedAttrNSTitle.add(lParentAttr.nsTitle);			// add to inherited
						lChildClass.inheritedAttribute.add(lParentAttr);
					}
				}
			} 
			//	inherit all inherited attributes of the parent class
			for (Iterator<AttrDefn> i = lParentClass.inheritedAttribute.iterator(); i.hasNext();) {
				AttrDefn lParentAttr = (AttrDefn) i.next();
				if (! lChildClass.ownedAttrNSTitle.contains(lParentAttr.nsTitle)) {				// not already owned
					if (! lChildClass.inheritedAttrNSTitle.contains(lParentAttr.nsTitle)) {		// not in inherited attribute list
						lChildClass.inheritedAttrNSTitle.add(lParentAttr.nsTitle);			// add to inherited
						lChildClass.inheritedAttribute.add(lParentAttr);
					}
				}
			} 
		}
		
		/**
		*  get the restricted attributes
		*/
		private void getRestrictedAttributes (PDSObjDefn lChildClass, PDSObjDefn lParentClass) {
			//	check all the owned attributes of the child class against both owned and inherited attributes of parent class
			for (Iterator<AttrDefn> i = lChildClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lChildOwnedAttr = (AttrDefn) i.next();								
				if (lParentClass.ownedAttrNSTitle.contains(lChildOwnedAttr.nsTitle)) {					// restricted attribute
					lChildOwnedAttr.isRestrictedInSubclass = true;
				}	else if (lParentClass.inheritedAttrNSTitle.contains(lChildOwnedAttr.nsTitle)) {		// restricted attribute
					lChildOwnedAttr.isRestrictedInSubclass = true;								// set isRestricted to true
				} 
			} 
		}
		
		/**
		*  get the inherited associations
		*/
		private void getInheritedAssociations (PDSObjDefn lChildClass, PDSObjDefn lParentClass) {
			//	inherit all owned associations of the parent class
			for (Iterator<AttrDefn> i = lParentClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lParentAssoc = (AttrDefn) i.next();								
				if (! lChildClass.ownedAssocNSTitle.contains(lParentAssoc.nsTitle)) {			// not already owned
					if (! lChildClass.inheritedAssocNSTitle.contains(lParentAssoc.nsTitle)) {	// not in inherited association list
						lChildClass.inheritedAssocNSTitle.add(lParentAssoc.nsTitle);			// add to inherited
						lChildClass.inheritedAssociation.add(lParentAssoc);
					}
				}
			} 
			//	inherit all inherited associations of the parent class
			for (Iterator<AttrDefn> i = lParentClass.inheritedAssociation.iterator(); i.hasNext();) {
				AttrDefn lParentAssoc = (AttrDefn) i.next();								
				if (! lChildClass.ownedAssocNSTitle.contains(lParentAssoc.nsTitle)) {			// not already owned
					if (! lChildClass.inheritedAssocNSTitle.contains(lParentAssoc.nsTitle)) {	// not in inherited association list
						lChildClass.inheritedAssocNSTitle.add(lParentAssoc.nsTitle);			// add to inherited
						lChildClass.inheritedAssociation.add(lParentAssoc);
					}
				}
			} 
		}

		/**
		*  get the restricted associations
		*/
		private void getRestrictedAssociations (PDSObjDefn lChildClass, PDSObjDefn lParentClass) {
			//	check all the owned associations of the child class against both owned and inherited associations of parent class
			for (Iterator<AttrDefn> i = lChildClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lChildOwnedAssoc = (AttrDefn) i.next();								
				if (lParentClass.ownedAssocNSTitle.contains(lChildOwnedAssoc.nsTitle)) {			// restricted association
					lChildOwnedAssoc.isRestrictedInSubclass = true;
				}	else if (lParentClass.inheritedAssocNSTitle.contains(lChildOwnedAssoc.nsTitle)) {		// restricted association
					lChildOwnedAssoc.isRestrictedInSubclass = true;								// set isRestricted to true
				} 
			} 
		}
		
		/**
		*  get the Base Class
		*/
		private void getBaseClass (PDSObjDefn lChildClass, PDSObjDefn lParentClass) {
			//	get the base class
			PDSObjDefn lSuperClass = lParentClass.subClassOfInst;
			if (lSuperClass != null) {
				if (lSuperClass.rdfIdentifier.indexOf(protegeRootClassRdfId) != 0) {
					lChildClass.rootClass = lSuperClass.rdfIdentifier ;
					String fundStrucName = checkForFundamentalStructure (lSuperClass.title);
					if (fundStrucName != null) {
						lChildClass.baseClassName = fundStrucName;
					}
				}
			} else {
				lChildClass.rootClass = lParentClass.rdfIdentifier;
				String fundStrucName = checkForFundamentalStructure (lParentClass.title);
				if (fundStrucName != null) {
					lChildClass.baseClassName = fundStrucName;
				}
			}
			return;
		}		
				
		/**
		*  Get all subClasses
		*/
		
		public void getSubClasses () {
			//  foreach object class
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();	
				for (Iterator<PDSObjDefn> j = masterMOFClassArr.iterator(); j.hasNext();) {
					PDSObjDefn lClass2 = (PDSObjDefn) j.next();	
					if (lClass.rdfIdentifier.compareTo(lClass2.rdfIdentifier) != 0) {		// don't check itself
						for (Iterator<PDSObjDefn> k = lClass2.superClass.iterator(); k.hasNext();) {
							PDSObjDefn lClass3 = (PDSObjDefn) k.next();
							if (lClass.rdfIdentifier.compareTo(lClass3.rdfIdentifier) == 0) {
								if (! k.hasNext()) {
									if (! lClass.subClasses.contains(lClass2.rdfIdentifier)) {
										lClass.subClasses.add(lClass2.rdfIdentifier);
										lClass.subClass.add(lClass2);
									}
								}
							}
						}
					}
				}
			}
		}
					
		/**
		*  for each class, get all class attributes and associations from the class and its superclasses, top down
		*/
		public void getAttrAssocArr () {
			//  for each class initialize the class.ownedAttrAssocArr array with sorted and owned attributes and associations
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();

				// sort all owned attributes and associations (AttrDefn)
				ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (lClass.ownedAttribute);
				lAttrArr.addAll(lClass.ownedAssociation);
				ArrayList <AttrDefn> lSortedAttrArr = getSortedAssocAttrArr (lAttrArr);
				lClass.ownedAttrAssocArr.addAll(lSortedAttrArr);
				
				// get the enumerated attributes for asserts
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					if (lAttr.isEnumerated) {
						lClass.ownedAttrAssocAssertArr.add(lAttr);
						lClass.ownedAttrAssocAssertTitleArr.add(lAttr.title);
					}
				}
			}
			// for each class get its superclasses 
			// also capture the enumerated attributes from bottom up through the hierarchy
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				ArrayList <PDSObjDefn> superClassArr = new ArrayList <PDSObjDefn> ();
				PDSObjDefn lClass = (PDSObjDefn) i.next();
				
				// get all the super classes by iterating up the hierarchy
				
				// setup the attr/assoc map for all override attributes in the hierarchy
				ArrayList <String> hierOwnedAttrAssocTitleArr = new ArrayList <String> ();
				TreeMap <String, AttrDefn> hierOwnedAttrAssocTitleMap = new TreeMap <String, AttrDefn> ();
				
				// initialize the attr/assoc map with the the current class's overrides, they are highest priority
				for (Iterator<AttrDefn> j = lClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					hierOwnedAttrAssocTitleMap.put(lAttr.title, lAttr);
					hierOwnedAttrAssocTitleArr.add(lAttr.title);
				}	

				// get the super classes
				// get the titles of attributes with enumerated lists for assertions;
				// get the overridden attributes in the hierarchy, super classes only.
				// also check for cycles in superClass hieararchy
				
				PDSObjDefn lSuperClass = lClass.subClassOfInst;
				while (lSuperClass != null) {
					if (! superClassArr.contains(lSuperClass)) {
						superClassArr.add(lSuperClass);
					} else {
						System.out.println(">>error   - Found cycle in superclass hierarchy - SuperClass.title:" + lSuperClass.title);
						break;
					}
					
					// get the enumerated attributes in bottom up order
					for (Iterator<AttrDefn> j = lSuperClass.ownedAttribute.iterator(); j.hasNext();) {
						AttrDefn lAttr = (AttrDefn) j.next();
						if (lAttr.isEnumerated) {
							if (! lClass.ownedAttrAssocAssertTitleArr.contains(lAttr.title)) {
								lClass.ownedAttrAssocAssertArr.add(lAttr);
								lClass.ownedAttrAssocAssertTitleArr.add(lAttr.title);
							}
						}
					}
					for (Iterator<AttrDefn> j = lSuperClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
						AttrDefn lAttr = (AttrDefn) j.next();
						if (! hierOwnedAttrAssocTitleArr.contains(lAttr.title)) {
							hierOwnedAttrAssocTitleMap.put(lAttr.title, lAttr);
							hierOwnedAttrAssocTitleArr.add(lAttr.title);
						}							
					}
										
					// get next superclass
					lSuperClass = lSuperClass.subClassOfInst;
				}
				
				// reverse order of superclasses; need top down
				Collections.reverse(superClassArr);
				
				// for the current class add all owned attribute and associations from each super class, top down
				ArrayList <String> lClassTitleArr = new ArrayList <String> ();
				for (Iterator<PDSObjDefn> j = superClassArr.iterator(); j.hasNext();) {
					lSuperClass = (PDSObjDefn) j.next();
					
					// for each superclass, add attribute/association if not already present
					for (Iterator<AttrDefn> k = lSuperClass.ownedAttrAssocArr.iterator(); k.hasNext();) {
						AttrDefn lPotentialAttr = (AttrDefn) k.next();
						if (! lClassTitleArr.contains(lPotentialAttr.title)) {
							AttrDefn lHierOwnedAttr = hierOwnedAttrAssocTitleMap.get(lPotentialAttr.title);
							if (lHierOwnedAttr != null) {
								lClass.allAttrAssocArr.add(lHierOwnedAttr);
								lClassTitleArr.add(lHierOwnedAttr.title);
							} else {
								System.out.println(">>error   - getAttrAssocArr - missing potential attribute lPotentialAttr.title:" + lPotentialAttr.title);
							}
						}
					}
				}
				
				// using the original owned attr/assoc array create the owned attr/assoc array with overrides removed
				// and add the local classes attr/assocs
				for (Iterator<AttrDefn> k = lClass.ownedAttrAssocArr.iterator(); k.hasNext();) {
					AttrDefn lAttr = (AttrDefn) k.next();
					if (! lClassTitleArr.contains(lAttr.title)) {
						lClass.ownedAttrAssocNOArr.add(lAttr);
						lClass.allAttrAssocArr.add(lAttr);
						lClassTitleArr.add(lAttr.title);
					}
				}
			}
			
			if (DMDocument.debugFlag) System.out.println("debug getAttrAssocArr Done");
		}	

		/**
		*  set the class version identifiers
		*/
		public void setClassVersionIds () {
			//	set the class version identifiers
			
			Set <String> set = DMDocument.classVersionId.keySet();
			Iterator <String> iter = set.iterator();
			while(iter.hasNext()) {
				String lClassName = (String) iter.next();
				String lClassId = InfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClassName);
//				System.out.println("\ndebug setClassVersionIds lClassName:" + lClassName);
				PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lClassId);
				if (lClass != null) {
//					System.out.println("debug setClassVersionIds FOUND lClass.identifier:" + lClass.identifier);		
					String lClassVersionId = DMDocument.classVersionId.get(lClassName);
//					System.out.println("debug setClassVersionIds SETTING lClassVersionId:" + lClassVersionId);		
					lClass.versionId = lClassVersionId;
				} else {
//					System.out.println("debug setClassVersionIds NOT FOUND lClassId:" + lClassId);						
				}
			}
		}
		
		public void dumpClassVersionIds () {
			//	set the class version identifiers
			 ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());
			for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();
				System.out.println("debug dumpClassVersionIds  lClass.identifier:" + lClass.identifier);		
				System.out.println("debug dumpClassVersionIds  lClass.versionId:" + lClass.versionId);		
			}
		}
		
	/**********************************************************************************************************
		miscellaneous routines
	***********************************************************************************************************/
		
		/**
		*  Check for attributes with the same ns:title but different constraints.
		*  (attributes with the same ns:title can be used in two or more classes
		*   this check ensures that they all have the same values for the meta-attributes
		*   Description alone is allowed to be different
		*   *** Different constraints would cause multiple simpleType definitions ***
		*/		
		static public void checkSameNameOverRide () {
			System.out.println(">>info    - Checking for attribute consistency - checkSameNameOverRide");
		
			// sort the attributes
			TreeMap <String, AttrDefn> lAttrMap = new TreeMap <String, AttrDefn> ();
			for (Iterator<AttrDefn> i = masterMOFAttrArr.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();	
				// *** temporary fix - needs to be thought out ***
				if (lAttr.isFromLDD) continue;
				lAttrMap.put(lAttr.title + "-" + lAttr.identifier, lAttr);
			}
			ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (lAttrMap.values());
			
			// check the attributes against all other attributes
			for (int i = 0; i < lAttrArr.size(); i++) {
				AttrDefn lAttr1 = lAttrArr.get(i);
				if (! lAttr1.isAttribute) continue;
				for (int j = i + 1; j < lAttrArr.size(); j++) {
					AttrDefn lAttr2 = lAttrArr.get(j);
					if (! lAttr2.isAttribute) continue;
					
					// only check attributes with the same titles
//					if (! (lAttr1.title.compareTo(lAttr2.title) == 0)) continue;
					if (! (lAttr1.nsTitle.compareTo(lAttr2.nsTitle) == 0)) continue;
					
					// don't check the meta-attributes
					if (lAttr1.title.compareTo("minimum_characters") == 0) continue;
					if (lAttr1.title.compareTo("maximum_characters") == 0) continue;
					if (lAttr1.title.compareTo("minimum_value") == 0) continue;
					if (lAttr1.title.compareTo("maximum_value") == 0) continue;
					if (lAttr1.title.compareTo("xml_schema_base_type") == 0) continue;
					if (lAttr1.title.compareTo("formation_rule") == 0) continue;
					if (lAttr1.title.compareTo("character_constraint") == 0) continue;
					if (lAttr1.title.compareTo("value") == 0) continue;
					if (lAttr1.title.compareTo("unit_id") == 0) continue;
					if (lAttr1.title.compareTo("pattern") == 0) continue;
					
					// check what is left
					checkForOverRideDetail (lAttr1, lAttr2);
				}
			}	
		}
		
		static public void checkForOverRideDetail (AttrDefn lAttr1, AttrDefn lAttr2) {
			boolean isFound = false;
			
			if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.valueType:" + lAttr1.valueType + "   lAttr2.valueType:" + lAttr2.valueType);
			}
			if (lAttr1.minimum_characters.compareTo(lAttr2.minimum_characters) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.minimum_characters:" + lAttr1.minimum_characters + "   lAttr2.minimum_characters:" + lAttr2.minimum_characters);
			}
			if (lAttr1.maximum_characters.compareTo(lAttr2.maximum_characters) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.maximum_characters:" + lAttr1.maximum_characters + "   lAttr2.maximum_characters:" + lAttr2.maximum_characters);
			}
			if (lAttr1.minimum_value.compareTo(lAttr2.minimum_value) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.minimum_value:" + lAttr1.minimum_value + "   lAttr2.minimum_value:" + lAttr2.minimum_value);
			}
			if (lAttr1.maximum_value.compareTo(lAttr2.maximum_value) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.maximum_value:" + lAttr1.maximum_value + "   lAttr2.maximum_value:" + lAttr2.maximum_value);
			}
			if (lAttr1.pattern.compareTo(lAttr2.pattern) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.pattern:" + lAttr1.pattern + "   lAttr2.pattern:" + lAttr2.pattern);
			}
			if (lAttr1.default_unit_id.compareTo(lAttr2.default_unit_id) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.default_unit_id:" + lAttr1.default_unit_id + "   lAttr2.default_unit_id:" + lAttr2.default_unit_id);
			}
			if (lAttr1.format.compareTo(lAttr2.format) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.format:" + lAttr1.format + "   lAttr2.format:" + lAttr2.format);
			}
			if (isFound) {
				if (DMDocument.debugFlag) System.out.println("debug checkForOverRideDetail lAttr1.identifier:" + lAttr1.identifier);
				if (DMDocument.debugFlag) System.out.println("debug checkForOverRideDetail lAttr2.identifier:" + lAttr2.identifier);
				if (DMDocument.debugFlag) System.out.println(" ");
			}
			return;
		}		
		
		
		// clone an array list
		static public ArrayList <PDSObjDefn> clonePDSObjDefnArrayList (ArrayList <PDSObjDefn> lArrayList) {
			ArrayList <PDSObjDefn> newArrayList = new ArrayList <PDSObjDefn> ();
			newArrayList.addAll(lArrayList);								
			return newArrayList;
		}
	}
