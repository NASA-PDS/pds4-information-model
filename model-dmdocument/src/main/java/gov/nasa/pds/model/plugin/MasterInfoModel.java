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
		
		// create the USER class - the root of all classes
		String lTitle = "USER";
		String lClassRdfIdentifier = DMDocument.rdfPrefix + lTitle + "." + getNextUId();
		PDSObjDefn lClass = new PDSObjDefn(lClassRdfIdentifier);
		lClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + "pds" + "." + lTitle;
		lClass.isUSERClass = true;
		lClass.title = lTitle;
		lClass.docSecType = lTitle;
		lClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
		lClass.subModelId = "ROOT";
		InfoModel.masterMOFUserClass = lClass;
		InfoModel.masterMOFClassArr.add(lClass);
		InfoModel.masterMOFClassMap.put(lClass.rdfIdentifier, lClass);
		InfoModel.masterMOFClassIdMap.put(lClass.identifier, lClass);
		InfoModel.masterMOFClassTitleMap.put(lClass.title, lClass);		
		
		// initialize the attribute structures
		InfoModel.masterMOFAttrMap = new TreeMap <String, AttrDefn> (); 
		InfoModel.masterMOFAttrIdMap = new TreeMap <String, AttrDefn> (); 
		InfoModel.masterMOFAttrTitleMap = new TreeMap <String, AttrDefn> (); 
		InfoModel.masterMOFAttrArr = new ArrayList <AttrDefn> (); 
		
		// initialize the association structures
		InfoModel.masterMOFAssocMap = new TreeMap <String, AssocDefn> (); 
		InfoModel.masterMOFAssocIdMap = new TreeMap <String, AssocDefn> (); 
		InfoModel.masterMOFAssocArr = new ArrayList <AssocDefn> (); 
		
		// initialize the global data types
		InfoModel.masterDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		InfoModel.masterDataTypeMap2 = new TreeMap <String, DataTypeDefn> ();

		// initialize the global unitOfMeasure
		InfoModel.masterUnitOfMeasureMap = new TreeMap <String, UnitDefn> ();

		return;
	}
	
/**********************************************************************************************************
	Update the master attribute map with the attributes from each individual class.
***********************************************************************************************************/
	
	public void addMasterAttrAssocMap (ProtPontModel lModelxxx, PDSObjDefn lClass) {

	    // get INSTANCE attributes - i.e. associations
		for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			// add associations
			if (! InfoModel.masterMOFAttrMap.containsKey(lAttr.rdfIdentifier)) {
//				System.out.println("debug addMasterAttrAssocMap - ADDING Association lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);				
				InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
				InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
				InfoModel.masterMOFAttrTitleMap.put(lAttr.title, lAttr);
				InfoModel.masterMOFAttrArr.add(lAttr);
			}
			
			// add MOF properties (type association)
			if (masterMOFAssocMap.get(lAttr.rdfIdentifier) == null) {
//				AssocDefn lAssoc = new AssocDefn (lAttr.rdfIdentifier);
				AssocDefn lAssoc = new AssocDefn ();
				lAssoc.rdfIdentifier = lAttr.rdfIdentifier;
				lAssoc.identifier = lAttr.identifier;
//				lAssoc.classOrder = lClassOrderStr;
				lAssoc.isAttribute = lAttr.isAttribute;	// false
				lAssoc.cardMin = lAttr.cardMin;
				lAssoc.cardMax = lAttr.cardMax;
				lAssoc.cardMinI = lAttr.cardMinI; 
				lAssoc.cardMaxI = lAttr.cardMaxI;
				lAssoc.parentClass = lClass;
				lAssoc.referenceType = lAttr.title;
				masterMOFAssocArr.add(lAssoc);
				masterMOFAssocMap.put(lAttr.rdfIdentifier, lAssoc);
				masterMOFAssocIdMap.put(lAttr.identifier, lAssoc);
			}
		}	
		
		//	get non-INSTANCE attributes - i.e. standard attributes
		for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			// add attributes
			if (! InfoModel.masterMOFAttrMap.containsKey(lAttr.rdfIdentifier)) {
//				System.out.println("debug addMasterAttrAssocMap - ADDING Attribute lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
				InfoModel.masterMOFAttrMap.put(lAttr.rdfIdentifier, lAttr);
				InfoModel.masterMOFAttrIdMap.put(lAttr.identifier, lAttr);
				InfoModel.masterMOFAttrTitleMap.put(lAttr.title, lAttr);
				InfoModel.masterMOFAttrArr.add(lAttr);
			}
			
			// add MOF Properties (type attribute)
			if (masterMOFAssocMap.get(lAttr.rdfIdentifier) == null) {
//				AssocDefn lAssoc = new AssocDefn (lAttr.rdfIdentifier);
				AssocDefn lAssoc = new AssocDefn ();
				lAssoc.rdfIdentifier = lAttr.rdfIdentifier;
				lAssoc.identifier = lAttr.identifier;
//				lAssoc.classOrder = lClassOrderStr;
				lAssoc.isAttribute = lAttr.isAttribute; // true
				lAssoc.cardMin = lAttr.cardMin;
				lAssoc.cardMax = lAttr.cardMax;
				lAssoc.cardMinI = lAttr.cardMinI; 
				lAssoc.cardMaxI = lAttr.cardMaxI;
				lAssoc.parentClass = lClass;
				lAssoc.referenceType = "attribute_of";
				masterMOFAssocArr.add(lAssoc);
				masterMOFAssocMap.put(lAttr.rdfIdentifier, lAssoc);
				masterMOFAssocIdMap.put(lAttr.identifier, lAssoc);
			}
		}
	}
		
/**********************************************************************************************************
	Set the DEC and CD for each attribute.
***********************************************************************************************************/
	
	public void GetMasterDECMaps () {		
		//set the DEC maps
		AttrDefn lAttr = InfoModel.masterMOFAttrTitleMap.get("attribute_concept");
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
			AttrDefn lAttr = InfoModel.masterMOFAttrTitleMap.get("conceptual_domain");
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
		Set Attributes' attributes from the dataType
***********************************************************************************************************/

		public void SetMasterAttrXMLBaseDataTypeFromDataType () {		
			
			// for each PDS4 attribute set the meta attribute from the data type
//			for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr2.iterator(); i.hasNext();) {
			for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
				AttrDefn lAttrI = (AttrDefn) i.next();
				if (lAttrI.isPDS4) {
//					System.out.println("\ndebug set other attributes - found PDS4 attribute attrId:" + attrId);
					String lTitle = lAttrI.valueType;					
					PDSObjDefn lClass = (PDSObjDefn) masterMOFClassTitleMap.get(lTitle);
					if (lClass != null) {
						// iterate through owned attributes
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
			lAttr.attrAnchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" + lAttr.className + "_" + lAttr.attrNameSpaceIdNC + "_"  + lAttr.title).toLowerCase();

			// set attributes sort identifier
			int lLength = lAttr.title.length();
			if (lLength >= 30) {
				lLength = 30;
			}
			String lPaddedAttrTitle = lAttr.title + lBlanks.substring(0, 30 - lLength);
			
			lLength = lAttr.className.length();
			if (lLength >= 30) {
				lLength = 30;
			}
			String lPaddedClassTitle = lAttr.className + lBlanks.substring(0, 30 - lLength);
			
			lAttr.sort_identifier = lPaddedAttrTitle + "_" + lAttr.steward + "_" + lPaddedClassTitle + "_" + lAttr.classSteward;

			// sort attribute.valarr
			Collections.sort(lAttr.valArr);
		}	
		return;
	}
	
	// get the permissible values from schematron statements (for example reference_type)
	public void getAttributePermValuesExtended () {

		// get an array of the schematron pattern statements
		ArrayList <RuleDefn> lAssertArrArr = new ArrayList <RuleDefn> (schematronRuleMap.values());				
		for (Iterator<RuleDefn> i = lAssertArrArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
//			System.out.println("\ndebug getAttributePermValuesExtended lRule.identifier:" + lRule.identifier);
			// get the affected attribute
			String lAttrId = DMDocument.registrationAuthorityIdentifierValue + "." + lRule.attrNameSpaceNC + "." + lRule.classTitle + "." + lRule.attrTitle;
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
						lDataType.title = lDataType.identifier;
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
//			System.out.println("debug setRegistrationStatus Value Calc lDeprecatedDefn.identifier:" + lDeprecatedDefn.identifier);
			if (lDeprecatedDefn.value.compareTo("") != 0) {
				String lId = DMDocument.registrationAuthorityIdentifierValue + "." + lDeprecatedDefn.nameSpaceIdNC + "." + lDeprecatedDefn.className + "." + lDeprecatedDefn.attrName;
				AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lId);
//				AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lDeprecatedDefn.identifier);
				if (lAttr != null) {
					lAttr.hasRetiredValue = true;
//					System.out.println("debug setRegistrationStatus Object FOUND lDeprecatedDefn.identifier:" + lDeprecatedDefn.identifier);
					for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
						PermValueDefn lPermValue = (PermValueDefn) j.next();
						if (lPermValue.value.compareTo(lDeprecatedDefn.value) == 0) {
							lPermValue.registrationStatus = "Retired";
						}
					}
				} else {
					System.out.println("debug setRegistrationStatus Object NOT FOUND lDeprecatedDefn.value:" + lDeprecatedDefn.value);
				}
			} else {
				// found attribute or object
				PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lDeprecatedDefn.identifier);
				if (lClass != null) {
					lClass.registrationStatus = "Retired";
				} else {
					AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lDeprecatedDefn.identifier);
					if (lAttr != null) {
						lAttr.registrationStatus = "Retired";
					} else {
						System.out.println("debug setRegistrationStatus Object NOT FOUND lDeprecatedDefn.identifier:" + lDeprecatedDefn.identifier);
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
	public void setMasterDataTypeAndUnitOfMeasureFlagsAttr () {
		// iterate through the classes
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("\ndebug setMasterDataType - lClass.title:" + lClass.title);
			if (lClass.isDataType) {
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					lAttr.isDataType = true;
				}
			}
			if (lClass.isUnitOfMeasure) {
				for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					lAttr.isUnitOfMeasure = true;
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

/*
				if (lAttr.isEnumerated) {continue;}
				if (! lAttr.isUsedInModel) {continue;}
				DataTypeDefn lDataType = InfoModel.masterDataTypeMap2.get(lAttr.valueType);
				if (lDataType != null) {
					boolean allEqualflag = true;
					System.out.println("\ndebug  ValidateAttributeDataTypes Checking Attribute lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
					if (lAttr.minimum_value.indexOf("TBD") != 0) {
						if (lAttr.minimum_value.compareTo(lDataType.minimum_value) != 0) {
							allEqualflag = false;
						}
					}
					if (lAttr.maximum_value.indexOf("TBD") != 0) {
						if (lAttr.maximum_value.compareTo(lDataType.maximum_value) != 0) {
							allEqualflag = false;
						}
					}
					if (lAttr.minimum_characters.indexOf("TBD") != 0) {
						if (lAttr.minimum_characters.compareTo(lDataType.minimum_characters) != 0) {
							allEqualflag = false;
						}
					}
					if (lAttr.maximum_characters.indexOf("TBD") != 0) {
						if (lAttr.maximum_characters.compareTo(lDataType.maximum_characters) != 0) {
							allEqualflag = false;
						}
					}
					if (allEqualflag) {
						System.out.println("debug  ValidateAttributeDataTypes FIX ATTRIBUTE lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
						System.out.println("debug ValidatedAttributeDataTypes lAttr.minimum_value:" + lAttr.minimum_value);
						System.out.println("debug ValidatedAttributeDataTypes lAttr.maximum_value:" + lAttr.maximum_value);
						System.out.println("debug ValidatedAttributeDataTypes lAttr.minimum_characters:" + lAttr.minimum_characters);
						System.out.println("debug ValidatedAttributeDataTypes lAttr.maximum_characters:" + lAttr.maximum_characters);
						System.out.println("debug ValidatedAttributeDataTypes lDataType..minimum_value:" + lDataType.minimum_value);
						System.out.println("debug ValidatedAttributeDataTypes lDataType.maximum_value:" + lDataType.maximum_value);
						System.out.println("debug ValidatedAttributeDataTypes lDataType.minimum_characters:" + lDataType.minimum_characters);
						System.out.println("debug ValidatedAttributeDataTypes lDataType.maximum_characters:" + lDataType.maximum_characters);
					}
				}
				*/
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
				lSortAttrMap.put(lAttr.title + "_" + lAttr.className + lAttr.attrNameSpaceId, lAttr);
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
						lAttr1.XMLSchemaName = lAttr1.className + "_" + lAttr1.title;
					}
				}
			}
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
				if (lClass.isUSERClass) {continue;}
//				System.out.println("debug - lClass.identifier:" + lClass.identifier + " - lClass.subClassOfTitle:" + lClass.subClassOfTitle);					
				PDSObjDefn lSupClass = (PDSObjDefn) masterMOFClassTitleMap.get(lClass.subClassOfTitle);
				if (lSupClass != null) {
					lClass.subClassOfInst = lSupClass;
					lClass.subClassOfIdentifier = lSupClass.identifier;
				} else {
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
					lClass.ownedAttribute.remove(targInd);
					lClass.ownedAttrId.remove(targInd);				
				}
			} 
		}
		
		/**
		*  fix the inheritance for all child classes
		*/
		public void fixInheritance (String lDocSecType, PDSObjDefn lParentClass,  ArrayList <PDSObjDefn> lSuperClass) {
//			System.out.println("\ndebug getInheritedAttributes lParentClass.title:" + lParentClass.title);
			if (lParentClass.title.compareTo("USER") != 0) {
				lSuperClass.add(lParentClass);
			}
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lChildClass = (PDSObjDefn) i.next();
				if (lChildClass.subClassOfTitle.compareTo(lParentClass.title) == 0) {
 
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
					
					// get the class hierarchy, i.e. all superclasses
					lChildClass.superClass = lSuperClass;
					for (Iterator<PDSObjDefn> j = lSuperClass.iterator(); j.hasNext();) {
						PDSObjDefn lClass = (PDSObjDefn) j.next();
						lChildClass.superClasses.add(lClass.rdfIdentifier);
					}
//					Collections.reverse(lChildClass.superClasses);
					
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
				if (! (lSuperClass.ownedAttrTitle.contains(lAttr.title) || lSuperClass.inheritedAttrTitle.contains(lAttr.title))) {
					return true;
				}
			}
			
			//	check if all owned and inherited associations are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lSuperClass.ownedAssocTitle.contains(lAttr.title) || lSuperClass.inheritedAssocTitle.contains(lAttr.title))) {
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
				if (! lChildClass.ownedAttrTitle.contains(lParentAttr.title)) {				// not already owned
					if (! lChildClass.inheritedAttrTitle.contains(lParentAttr.title)) {		// not in inherited attribute list
						lChildClass.inheritedAttrTitle.add(lParentAttr.title);				// add to inherited
						lChildClass.inheritedAttrId.add(lParentAttr.identifier);
						lChildClass.inheritedAttribute.add(lParentAttr);
					}
				}
			} 
			//	inherit all inherited attributes of the parent class
			for (Iterator<AttrDefn> i = lParentClass.inheritedAttribute.iterator(); i.hasNext();) {
				AttrDefn lParentAttr = (AttrDefn) i.next();
				if (! lChildClass.ownedAttrTitle.contains(lParentAttr.title)) {				// not already owned
					if (! lChildClass.inheritedAttrTitle.contains(lParentAttr.title)) {		// not in inherited attribute list
						lChildClass.inheritedAttrTitle.add(lParentAttr.title);				// add to inherited
						lChildClass.inheritedAttrId.add(lParentAttr.identifier);
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
				if (lParentClass.ownedAttrTitle.contains(lChildOwnedAttr.title)) {					// restricted attribute
					lChildOwnedAttr.isRestrictedInSubclass = true;
				}	else if (lParentClass.inheritedAttrTitle.contains(lChildOwnedAttr.title)) {		// restricted attribute
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
				if (! lChildClass.ownedAssocTitle.contains(lParentAssoc.title)) {			// not already owned
					if (! lChildClass.inheritedAssocTitle.contains(lParentAssoc.title)) {	// not in inherited association list
						lChildClass.inheritedAssocTitle.add(lParentAssoc.title);			// add to inherited
						lChildClass.inheritedAssocId.add(lParentAssoc.identifier);
						lChildClass.inheritedAssociation.add(lParentAssoc);
					}
				}
			} 
			//	inherit all inherited associations of the parent class
			for (Iterator<AttrDefn> i = lParentClass.inheritedAssociation.iterator(); i.hasNext();) {
				AttrDefn lParentAssoc = (AttrDefn) i.next();								
				if (! lChildClass.ownedAssocTitle.contains(lParentAssoc.title)) {			// not already owned
					if (! lChildClass.inheritedAssocTitle.contains(lParentAssoc.title)) {	// not in inherited association list
						lChildClass.inheritedAssocTitle.add(lParentAssoc.title);			// add to inherited
						lChildClass.inheritedAssocId.add(lParentAssoc.identifier);
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
				if (lParentClass.ownedAssocTitle.contains(lChildOwnedAssoc.title)) {			// restricted association
					lChildOwnedAssoc.isRestrictedInSubclass = true;
				}	else if (lParentClass.inheritedAssocTitle.contains(lChildOwnedAssoc.title)) {		// restricted association
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
//					getSuperClasses (lChildClass, lSuperClass);
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
						for (Iterator<String> k = lClass2.superClasses.iterator(); k.hasNext();) {
							String lId = (String) k.next();
							if (lClass.rdfIdentifier.compareTo(lId) == 0) {
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
								
				ArrayList <AttrDefn> lSortedAttrArr = getSortedAssocAttrArr (lClass.ownedAttribute);
				lClass.ownedAttrAssocArr.addAll(lSortedAttrArr);			
				
				// get the owned associations
				lSortedAttrArr = getSortedAssocAttrArr (lClass.ownedAssociation);
				lClass.ownedAttrAssocArr.addAll(lSortedAttrArr);
				
				// update the associations
				for (Iterator<AttrDefn> j = lClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					AssocDefn lAssoc = masterMOFAssocMap.get(lAttr.rdfIdentifier);
					if (lAssoc == null) continue;
					if (lAssoc.isAttribute) {
						lAssoc.childAttrArr.add(lAttr);
					} else {
						lAssoc.tempChildAssocArr.add(lAttr);
						for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String lTitle = (String) k.next();
							if (lTitle == null) continue;
							PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
							lAssoc.childClassArr.add (lClassMember);
						}
					}
					lClass.allAssocArr.add(lAssoc);
				}
								
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
				PDSObjDefn lSuperClass = lClass.subClassOfInst;
				while (lSuperClass != null) {
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
					superClassArr.add(lSuperClass);
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
		}	
				
		/**
		*  Validate Class Associations
		*/
		public void validateClassAssocs () {
			//  foreach object class
			for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();	
				
				//	get all owned associations
				for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAssoc = (AttrDefn) j.next();
					ArrayList <String> lvalarr = lAssoc.valArr;	
					if (! lvalarr.isEmpty()) {
						
						// get all associated classes
						for (Iterator<String> k = lvalarr.iterator(); k.hasNext();) {
							String lctitle = (String) k.next();
							PDSObjDefn lclass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lctitle);
							if (lclass == null) {
								System.out.println(">>error   - validate associations - missing class lctitle:" + lctitle);
							}
						}
					}
				}
			}
		}
		
	/**********************************************************************************************************
		miscellaneous routines
	***********************************************************************************************************/
		
		// clone an array list
		static public ArrayList <PDSObjDefn> clonePDSObjDefnArrayList (ArrayList <PDSObjDefn> lArrayList) {
			ArrayList <PDSObjDefn> newArrayList = new ArrayList <PDSObjDefn> ();
			newArrayList.addAll(lArrayList);								
			return newArrayList;
		}
	}
