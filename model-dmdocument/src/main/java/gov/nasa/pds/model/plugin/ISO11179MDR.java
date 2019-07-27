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
import java.io.*;
import java.util.*;

class ISO11179MDR extends Object {
	TreeMap <String, String> hashCodedPersmissibleValueMap;
	TreeMap <String, String> hashCodedValueMeaningMap;
	TreeMap <String, String> valueChangeMap;

	public ISO11179MDR () {
		hashCodedPersmissibleValueMap = new TreeMap <String, String> ();
		hashCodedValueMeaningMap = new TreeMap <String, String> ();
		valueChangeMap = new TreeMap <String, String> ();
		
		// *** Values to be updated go here ***
		// - changes need to be made to UpperModel.pont
		// - resulting dd11179_Gen.pins needs to be made active
		// - the value meaning text needs to be updated in dd11179.pins (using Protege) if the value is in the text.
		// - comment out the following after testing
		// - this approach is needed since a hash of the value is used as part of the value meaning primary key.
		
/*		valueChangeMap.put("vm.0001_NASA_PDS_1.pds.Telescope.pds.coordinate_source#Aerial survey - North American (1983) datum", "Aerial Survey - North American (1983) Datum");
		valueChangeMap.put("vm.0001_NASA_PDS_1.pds.Checksum_Manifest.pds.record_delimiter#carriage-return line-feed", "Carriage-Return Line-Feed");
		valueChangeMap.put("vm.0001_NASA_PDS_1.pds.Inventory.pds.record_delimiter#carriage-return line-feed", "Carriage-Return Line-Feed"); */
		
		// set up permissible value / value meaning map; need identifier from map
		Set <String> set1 = InfoModel.master11179DataDict.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			InstDefn lInst = (InstDefn) InfoModel.master11179DataDict.get(lId);
			
//			System.out.println("\ndebug ISO11179MDR - lInst.rdfIdentifier:" + lInst.rdfIdentifier);
//			System.out.println("debug ISO11179MDR - lInst.identifier:" + lInst.identifier);
//			System.out.println("debug ISO11179MDR - lId:" + lId);
//			System.out.println("debug ISO11179MDR - lInst.title:" + lInst.title);

			if (lInst.className.compareTo ("PermissibleValue") == 0) {
				
//				System.out.println("\ndebug ISO11179MDR - Instance - lInst.identifier:" + lInst.identifier);			
				// lInst.identifier:dd79.pv.0001_NASA_PDS_1.pds.Array.axis_index_order.-1365745102

				ArrayList <String> lValArr = lInst.genSlotMap.get("value");
				if (lValArr != null) {
					String lValue = lValArr.get(0);
//					System.out.println("debug ISO11179MDR -lValue:" + lValue);	
					// lValue:Last Index Fastest
					
					String lKey = lInst.title;
//					System.out.println("debug ISO11179MDR -lInst.title:" + lInst.title);
					
					// lInst.title:pv.0001_NASA_PDS_1.pds.Array.axis_index_order.-1365745102
					int lOffset = lInst.title.lastIndexOf(".");
					if (lOffset > -1) lKey = lInst.title.substring(0, lOffset);
//					System.out.println("debug ISO11179MDR - lKey:" + lKey);			
					// lKey:pv.0001_NASA_PDS_1.pds.Array.axis_index_order
					
					lValArr = lInst.genSlotMap.get("usedIn");
					if (lValArr != null) {
						String lVMId = lValArr.get(0);
//						System.out.println("debug ISO11179MDR - lVMId:" + lVMId);
						// lVMId:vm.0001_NASA_PDS_1.pds.Array.axis_index_order.-1365745102
						
						String lVMIdExt = "ValueMeaning" + "." + lVMId; 
						// lVMIdExt:ValueMeaning.vm.0001_NASA_PDS_1.pds.Array.axis_index_order.-1365745102
						
						InstDefn lVMInst = (InstDefn) InfoModel.master11179DataDict.get(lVMIdExt);
						if (lVMInst != null) {
//							System.out.println("debug ISO11179MDR - GOT - lVMInst.title:" + lVMInst.title);		
							// lVMInst.title:vm.0001_NASA_PDS_1.pds.Array.axis_index_order.-1365745102
							
							lValArr = lVMInst.genSlotMap.get("description");
							if (lValArr != null) {
								String lDescription = lValArr.get(0);
//								System.out.println("\ndebug ISO11179MDR - lVMId:" + lVMId);

/*								
                                // *** Value Updates Go Here ***
								String lVMUpdId = "";
								lOffset = lVMId.lastIndexOf(".");
								if (lOffset > -1) lVMUpdId = lVMId.substring(0, lOffset);
								lVMUpdId += "#" + lValue;
 								String lValueNew = valueChangeMap.get(lVMUpdId);
								if (lValueNew != null) {
									lValue = lValueNew;
								}
*/		
								lKey += "." + lValue;
								hashCodedValueMeaningMap.put(lKey, lDescription);
							}
						}
					}
				}
			}
		}
		
		// *** New Value Meanings Here ***
/*		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Spectroscopy", "light wavelength/wave number spectra of any and all dimensionalities");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Discipline_Facets.discipline_name.Imaging", "any non-spectroscopic image, of any dimensionality (color, movies, etc.)");
		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet2.facet2.Energetic", "> 30keV"); */
//		hashCodedValueMeaningMap.put("pv.0001_NASA_PDS_1.pds.Group_Facet1.facet1.Spectral Image", "A 2D image of a spectrum, as projected on a focal plane. There may be multiple orders present, and the axes of the spectrum/spectra typically do not align with the edges of the image.");

		return;
	}
		
//	set up the 11179 elements
	public void ISO11179MDRSetup (InfoModel masterInfoModel) {
		for (Iterator <AttrDefn> i = InfoModel.getAttArrByTitleStewardClassSteward().iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("\ndebug getAttrISOAttr1 lattr.rdfIdentifier:" + lAttr.rdfIdentifier);
			if (lAttr.isUsedInClass && lAttr.propType.compareTo("ATTRIBUTE") == 0) {
				getAttrISOAttr (lAttr);
			}	
		}
	}	

	// Get the ISO components of a data element
	private void getAttrISOAttr (AttrDefn lattr) {
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
//		InstDefn lInst;
		String lSuffix;
		String lInstId;
		String lVal;
		boolean isEnumerated = false;
		boolean lDebugFlag = false;
		
		// iterate through the master attribute array
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.propType.compareTo("INSTANCE") == 0) continue;
			if (lAttr.isFromLDD) continue;
			PDSObjDefn lParentClass = lAttr.attrParentClass;			
			if (lParentClass == null) continue;
			if (lParentClass.isFromLDD) continue;
//			System.out.println("\ndebug OverwriteFrom11179DataDict - lAttr.identifier:" + lAttr.identifier);
			lSuffix = InfoModel.getAttrIdentifier (lAttr.classNameSpaceIdNC, lAttr.parentClassTitle, lAttr.attrNameSpaceIdNC, lAttr.title);
			// e.g. 0001_NASA_PDS_1.AAREADME.encoding_type 			
			
			lInstId = "DataElement" + "." + "DE" + "." + lSuffix;
			// e.g. DataElement.DE.0001_NASA_PDS_1.AAREADME.encoding_type 			
//			System.out.println("\ndebug Overwrite with 11179 - attempting to update lInstId:" + lInstId);			
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
//					System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.isNilable:" + lAttr.isNilable + "  - with lVal:" + lVal);
					lAttr.isNilable = true;
				}							

			} else {
//				if (! (lAttr.isFromLDD || lAttr.title.compareTo("%3ANAME") == 0)) {
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
				
				// fix  data types
/*				if (lAttr.title.compareTo("name") == 0) {
					lAttr.valueType = "UTF8_Short_String_Collapsed";
					System.out.println("\ndebug ISO11179MDR - FIXING Data Type - lAttr.identifier:" + lAttr.identifier);		
					System.out.println("debug ISO11179MDR - FIXING Data Type - lAttr.valueType:" + lAttr.valueType);		
				} */
				
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
//					System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId + " - updating lAttr.minimum_characters:" + lAttr.minimum_characters + "  - with lVal:" + lVal);
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
				
//				System.out.println("debug OverwriteFrom11179DataDict -2- lAttr.identifier:" + lAttr.identifier);

				// get the permissible values and value meanings
				if  (lAttr.valArr != null && lAttr.valArr.size() > 0) {
//					System.out.println("\ndebug OverwriteFrom11179DataDict - Value Meanings - lAttr.identifier:" + lAttr.identifier);
					
					ArrayList <String> lValArrSorted = new ArrayList <String> ();
					for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
						String lValue = (String) j.next();
						lValArrSorted.add(lValue);
					}
					Collections.sort(lValArrSorted);
					for (Iterator<String> j = lValArrSorted.iterator(); j.hasNext();) {
						String lValue = (String) j.next();
//						System.out.println("debug OverwriteFrom11179DataDict - Value Meanings - lValue:" + lValue);
						
						String lId = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.parentClassTitle + "." + lAttr.title + "_Value." + lValue;
						PermValueDefn lPermValueDefn = new PermValueDefn (lId, lValue);
						lAttr.permValueArr.add(lPermValueDefn);

						// title:vm.0001_NASA_PDS_1.pds.DD_Class_Full.steward_id.111209
						String lKey = "pv." + InfoModel.getAttrIdentifier(lAttr.classNameSpaceIdNC, lAttr.parentClassTitle, lAttr.attrNameSpaceIdNC, lAttr.title) + "." + lValue;
						
//						System.out.println("debug Overwrite with 11179 - GOT - lKey:" + lKey);
						String lValueMeaning = hashCodedValueMeaningMap.get(lKey);
						if (lValueMeaning != null) {
//							System.out.println("debug Overwrite with 11179 - FOUND - lValueMeaning:" + lValueMeaning);
							lPermValueDefn.value_meaning = lValueMeaning;
						}
					}
				}
			}
		}
	}
	
	
//	Get class order
	public void getClassOrder () {
		// iterate through the associations
		ArrayList <AssocDefn> lAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());		
		for (Iterator<AssocDefn> i = lAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
//			System.out.println("\ndebug getClassOrder lInst.identifier:" + lAssoc.identifier);
			String lSuffix = InfoModel.getAttrIdentifier (lAssoc.classNameSpaceIdNC, lAssoc.className, lAssoc.attrNameSpaceIdNC, lAssoc.title);

			// e.g. 0001_NASA_PDS_1.AAREADME.encoding_type 			
			String lInstId = "Property" + "." + "PR" + "." + lSuffix;
			// e.g. DataElement.DE.0001_NASA_PDS_1.AAREADME.encoding_type 			
//			System.out.println("debug Overwrite with 11179 - attempting to update class order lInstId:" + lInstId);	
			InstDefn lPRInst = InfoModel.master11179DataDict.get(lInstId);
			if (lPRInst == null) continue;
			HashMap <String, ArrayList<String>> lInstMap = lPRInst.genSlotMap;
			if (lInstMap == null) continue;
			ArrayList <String> lValArr = lInstMap.get("classOrder");
			if (lValArr == null) continue;
			String lClassOrder = lValArr.get(0);
			if (lClassOrder.compareTo("") == 0) continue;
//			System.out.println("debug getClassOrder lId:" + lInstId + " - lClassOrder:" + lClassOrder);
			lAssoc.classOrder = lClassOrder;
		}
		return;
	}	
}
