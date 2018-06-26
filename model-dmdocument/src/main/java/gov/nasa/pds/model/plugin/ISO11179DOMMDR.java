package gov.nasa.pds.model.plugin; 
import java.util.*;

class ISO11179DOMMDR extends Object {
	TreeMap <String, String> hashCodedPersmissibleValueMap;
	TreeMap <String, String> hashCodedValueMeaningMap;
	TreeMap <String, String> valueChangeMap;

	public ISO11179DOMMDR () {
		hashCodedPersmissibleValueMap = new TreeMap <String, String> ();
		hashCodedValueMeaningMap = new TreeMap <String, String> ();
		valueChangeMap = new TreeMap <String, String> ();
				
		// set up permissible value / value meaning map; need identifier from map
		ArrayList <InstDefn> lMaster11179DataDictArr = new ArrayList <InstDefn> (InfoModel.master11179DataDict.values());
		for (Iterator <InstDefn> i = lMaster11179DataDictArr.iterator(); i.hasNext();) {
			InstDefn lInstDefn = (InstDefn) i.next();
			if (lInstDefn.className.compareTo ("PermissibleValue") == 0) {
				ArrayList <String> lValArr = lInstDefn.genSlotMap.get("value");
				if (lValArr != null) {
					String lValue = lValArr.get(0);					
					String lKey = lInstDefn.title;
					int lOffset = lInstDefn.title.lastIndexOf(".");
					if (lOffset > -1) lKey = lInstDefn.title.substring(0, lOffset);
					lValArr = lInstDefn.genSlotMap.get("usedIn");
					if (lValArr != null) {
						String lVMId = lValArr.get(0);
						String lVMIdExt = "ValueMeaning" + "." + lVMId; 
						InstDefn lVMInst = (InstDefn) InfoModel.master11179DataDict.get(lVMIdExt);
						if (lVMInst != null) {
							lValArr = lVMInst.genSlotMap.get("description");
							if (lValArr != null) {
								String lDescription = lValArr.get(0);		
								lKey += "." + lValue;
								hashCodedValueMeaningMap.put(lKey, lDescription);
							}
						}
					}
				}
			}
		}
		return;
	}
		
//	set up the 11179 elements
	public void ISO11179MDRSetup () {
		for (Iterator <DOMAttr> i = DOMInfoModel.getAttArrByTitleStewardClassSteward().iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				getAttrISOAttr (lAttr);
			}	
		}
	}	

	// Get the ISO components of a data element
	private void getAttrISOAttr (DOMAttr lattr) {
	//  add the namespace to the above	
		lattr.administrationRecordValue = DMDocument.administrationRecordValue;
		lattr.versionIdentifierValue = DMDocument.versionIdentifierValue;
		lattr.submitter = DMDocument.submitterValue;
		lattr.registeredByValue = DMDocument.registeredByValue;
		lattr.registrationAuthorityIdentifierValue = DMDocument.registrationAuthorityIdentifierValue;
	}
	
//	scan through the master attribute list and overwrite with the latest from the 11179 DD database.
	public void OverwriteFrom11179DataDict () {
		HashMap <String, ArrayList<String>> lInstMap = new HashMap <String, ArrayList<String>> ();
		String lSuffix;
		String lInstId;
		String lVal;
		boolean isEnumerated = false;
		boolean lDebugFlag = false;
		
		// iterate through the master attribute array
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (! lAttr.isAttribute) continue;
			if (lAttr.isFromLDD) continue;
			DOMClass lParentClass = lAttr.attrParentClass;			
			if (lParentClass == null) continue;
			if (lParentClass.isFromLDD) continue;
			
			lSuffix = DOMInfoModel.getAttrIdentifier (lAttr.classNameSpaceIdNC, lAttr.parentClassTitle, lAttr.nameSpaceIdNC, lAttr.title);			
			
			lInstId = "DataElement" + "." + "DE" + "." + lSuffix;		
			InstDefn lDEInst = InfoModel.master11179DataDict.get(lInstId);
			if (lDEInst != null) {
				lInstMap = lDEInst.genSlotMap;

				// update steward
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "steward", lAttr.steward);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.steward:" + lAttr.steward + "  - with lVal:" + lVal); }
					lAttr.steward = lVal;
				}
				
				// update classConcept
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "expressedBy", lAttr.classConcept);                        
				if (lVal != null) {
					lVal = lVal.substring(4, lVal.length());
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.classConcept:" + lAttr.classConcept + "  - with lVal:" + lVal); }					
					lAttr.classConcept = lVal;
			}
			
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "isNillable", "false");                        
				if (lVal != null && lVal.compareTo("true") == 0) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.isNilable:" + lAttr.isNilable + "  - with lVal:" + lVal); }
					lAttr.isNilable = true;
				}							

			} else {
				if (! (lAttr.title.compareTo("%3ANAME") == 0)) {
					System.out.println(">>error   - 11179 data dictionary attribute is missing for overwrite - Identifier:" + lInstId);
				}
			}
			boolean hasVD = false;
			isEnumerated = false;
			lInstId = "EnumeratedValueDomain" + "." + "EVD" + "." + lSuffix;
			InstDefn lVDInst = InfoModel.master11179DataDict.get(lInstId);
			if (lAttr.isEnumerated && lVDInst != null) {
				hasVD = true;
				isEnumerated = true;
			} else {
				lInstId = "NonEnumeratedValueDomain" + "." + "NEVD" + "." + lSuffix;
				lVDInst = InfoModel.master11179DataDict.get(lInstId);
				if (lVDInst != null) {
					hasVD = true;
				}
			}
			if (hasVD) {
				lInstMap = lVDInst.genSlotMap;					
				// dataType
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "datatype", lAttr.valueType);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.valueType:" + lAttr.valueType + "  - with lVal:" + lVal); }
					lAttr.valueType = lVal;
				}
				
				// dataConcept
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "representedBy2", lAttr.dataConcept);                        
				if (lVal != null) {
					lVal = lVal.substring(3, lVal.length());
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.dataConcept:" + lAttr.dataConcept + "  - with lVal:" + lVal); }
					lAttr.dataConcept = lVal;	
				}
				
				// Minimum Value
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "minimumValue", lAttr.minimum_value);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_value:" + lAttr.minimum_value + "  - with lVal:" + lVal); }
					lAttr.minimum_value = lVal;
				}				
				
				// Maximum Value
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "maximumValue", lAttr.maximum_value);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.maximum_value:" + lAttr.maximum_value + "  - with lVal:" + lVal); }
					lAttr.maximum_value = lVal;
				}	

				// Minimum Characters
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "minimumCharacterQuantity", lAttr.minimum_characters);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_characters:" + lAttr.minimum_characters + "  - with lVal:" + lVal); }
					lAttr.minimum_characters = lVal;
				}	
				
				// Maximum Characters
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "maximumCharacterQuantity", lAttr.maximum_characters);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.maximum_characters:" + lAttr.maximum_characters + "  - with lVal:" + lVal); }
					lAttr.maximum_characters = lVal;
				}	
				
				// Pattern
				lVal = ProtPins11179DD.getStringValueUpdatePattern (true, lInstMap, "pattern", lAttr.pattern);                        
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.pattern:" + lAttr.pattern + "  - with lVal:" + lVal); }
					lAttr.pattern = lVal;
				}				
				
				// Unit_of_measure_type
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "unitOfMeasure", lAttr.unit_of_measure_type);               
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.unit_of_measure_type:" + lAttr.unit_of_measure_type + "  - with lVal:" + lVal); }
					lAttr.unit_of_measure_type = lVal;
				}				
				
				// default_unit_id
				lVal = ProtPins11179DD.getStringValueUpdate (false, lInstMap, "defaultUnitId", lAttr.default_unit_id);               
				if (lVal != null) {
					if (lDebugFlag) { System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.default_unit_id:" + lAttr.default_unit_id + "  - with lVal:" + lVal); }
					lAttr.default_unit_id = lVal;
				}
				
				// get the permissible value meanings
				if  (lAttr.domPermValueArr != null && lAttr.domPermValueArr.size() > 0) {					
					for (Iterator<DOMProp> j = lAttr.domPermValueArr.iterator(); j.hasNext();) {
						DOMProp lDOMProp = (DOMProp) j.next();
						if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
							DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.hasDOMObject;
//							System.out.println("\ndebug OverwriteFrom11179DataDict - get the permissible value meanings - lDOMPermVal.identifier:" + lDOMPermVal.identifier);
//							System.out.println("debug OverwriteFrom11179DataDict - get the permissible value meanings - lDOMPermVal.value:" + lDOMPermVal.value);
							
							// title:vm.0001_NASA_PDS_1.pds.DD_Class_Full.steward_id.111209
							String lKey = "pv." + InfoModel.getAttrIdentifier(lAttr.classNameSpaceIdNC, lAttr.parentClassTitle, lAttr.nameSpaceIdNC, lAttr.title) + "." + lDOMPermVal.value;
							String lValueMeaning = hashCodedValueMeaningMap.get(lKey);
//							System.out.println("debug OverwriteFrom11179DataDict - get the permissible value meanings - - lKey:" + lKey);
							if (lValueMeaning != null) {
								lDOMPermVal.value_meaning = lValueMeaning;
//								System.out.println("debug OverwriteFrom11179DataDict - get the permissible value meanings - - - lDOMPermVal.value_meaning:" + lDOMPermVal.value_meaning);
							}
						}
					}
				}
			}
		}
	}
	
//	Get class order
	public void getClassOrder () {
		// iterate through the associations
		for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			String lSuffix = InfoModel.getAttrIdentifier (lProp.classNameSpaceIdNC, lProp.parentClassTitle, lProp.nameSpaceIdNC, lProp.title);			
			String lInstId = "Property" + "." + "PR" + "." + lSuffix;	
			InstDefn lPRInst = InfoModel.master11179DataDict.get(lInstId);
			if (lPRInst == null) continue;
			HashMap <String, ArrayList<String>> lInstMap = lPRInst.genSlotMap;
			if (lInstMap == null) continue;
			ArrayList <String> lValArr = lInstMap.get("classOrder");
			if (lValArr == null) continue;
			String lClassOrder = lValArr.get(0);
			if (lClassOrder.compareTo("") == 0) continue;
			lProp.classOrder = lClassOrder;
		}
		return;
	}	
}
