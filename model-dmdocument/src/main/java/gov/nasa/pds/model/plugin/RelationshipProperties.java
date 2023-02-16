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
import java.util.TreeMap;

class RelationshipProperties extends ClassPropertiesBase {
		String fromClassID;
		ClassPropertiesOWL fromClassProperties;
		
		String toClassID;
		ClassPropertiesOWL toClassProperties; 
		
		TreeMap <String, String> relNameToClassNameMap  = new TreeMap <String, String> ();
		
		public RelationshipProperties (String referenceType, TreeMap <String, ClassPropertiesOWL> classPropertiesMap) {
			lidvid = formValueLID(lid) + ":1.0";
			
			// initialize the relNameToClassNameMap
			initRelNameToClassNameMap();
			
			// decode a reference_type value into {fromName, to, toName}
			int validCount = 0;
			ArrayList <String> lNameArr = decodeReferenceType (referenceType);
			
			if (lNameArr == null || lNameArr.size() != 3) {
//				if (DMDocument.debugFlag) System.out.println("debug RelationshipProperties - invalid <reference_type> - referenceType:" + referenceType);
				lidvid = null;
				return;
			}
			validCount++;
			
			// handle fromName
			String lName = lNameArr.get(0);
			if (lName != null) {
				String lClassID = relNameToClassNameMap.get(lName);
				if (lClassID != null) {
					ClassPropertiesOWL lClassProperties = classPropertiesMap.get(lClassID);
					if (lClassProperties != null) {						
						fromClassID = lClassID;
						fromClassProperties = lClassProperties;
						title = referenceType;
						validCount++;
					}
				}
			}
			
			// handle toName
			lName = lNameArr.get(2);
			if (lName != null) {
				String lClassID = relNameToClassNameMap.get(lName);
				if (lClassID != null) {
					ClassPropertiesOWL lClassProperties = classPropertiesMap.get(lClassID);
					if (lClassProperties != null) {
						toClassID = lClassID;
						toClassProperties = lClassProperties;
						validCount++;
					}
				}
			}

			if (validCount == 3) {
				lid = fromClassProperties.lid + ":" + title;
				lidvid = formValueLID(lid) + ":1.0";
			} else {
// ***** FIX ****				if (DMDocument.debugFlag) System.out.println("debug RelationshipProperties - create relationship failed - validCount:" + validCount + "   referenceType:" + referenceType);
				lidvid = null;
			}			
		}

		public RelationshipProperties (DOMClass lDOMClass, DOMProp lDOMProp, DOMClass lDOMMemberClass, 	TreeMap <String, ClassPropertiesOWL> classPropertiesIdMap) {
			identifier = "TBD_identifier";
			type = "TBD_type";
			subClassOfTitle = "TBD_subClassOfTitle";
			title = "TBD_title";  													
			definition = "TBD_definition";	
			lid = "TBD_lid";
			lidvid = formValueLID(lid) + ":1.0";
			
			String lClassID = lDOMClass.identifier;
			ClassPropertiesOWL lClassPropertiesFrom = classPropertiesIdMap.get(lClassID);
			if (lClassPropertiesFrom != null) {						
				fromClassID = lClassID;
				fromClassProperties = lClassPropertiesFrom;
				title = lDOMProp.title;
			} else {
				fromClassID = "0001_NASA_PDS_1.pds.Other";
				lClassPropertiesFrom = classPropertiesIdMap.get(fromClassID);
				if (lClassPropertiesFrom == null) { 
					System.out.println("debug RelationshipProperties -FAILED- fromClassID:" + fromClassID);
				}
				fromClassProperties = lClassPropertiesFrom;
				title = "missing";
			}
			
			lClassID = lDOMMemberClass.identifier;
			ClassPropertiesOWL lClassPropertiesTo = classPropertiesIdMap.get(lDOMMemberClass.identifier);
			if (lClassPropertiesTo != null) {
				toClassID = lClassID;
				toClassProperties = lClassPropertiesTo;
			} else {
				toClassID = "0001_NASA_PDS_1.pds.Other";
				lClassPropertiesTo = classPropertiesIdMap.get(toClassID);
				if (lClassPropertiesTo == null) { 
					System.out.println("debug RelationshipProperties -FAILED- toClassID:" + toClassID);
				}
				toClassProperties = lClassPropertiesTo;
			}
			
			lid = fromClassProperties.lid + ":" + title;
			lidvid = formValueLID(lid) + "::1.0";	
			
//			System.out.println("\ndebug RelationshipProperties lDOMClass.identifier:" + lDOMClass.identifier);
//			System.out.println("                             fromClassID:" + fromClassID + "   title:" + title + "   toClassID:" + toClassID);
//			System.out.println("                             fromClassProperties.lidvid:" + fromClassProperties.lidvid + "   lidvid:" + lidvid + "   toClassProperties.lidvid:" + toClassProperties.lidvid);
		}
		
		private ArrayList <String> decodeReferenceType (String referenceType) {
//			investigation_to_instrument
			ArrayList <String> lNameArr = new ArrayList <String> ();
			
			// check for "to" reference
			int typeInd = referenceType.indexOf("_to_");
			if (typeInd > -1) {
				String lName = referenceType.substring(0, typeInd);
				lNameArr.add(lName);
				lNameArr.add("to");
				lName = referenceType.substring(typeInd + 4, referenceType.length());
				lNameArr.add(lName);
				return lNameArr;
			}
			
			// check for "has" reference
			typeInd = referenceType.indexOf("_has_");
			if (typeInd > -1) {
				String lName = referenceType.substring(0, typeInd);
				lNameArr.add(lName);
				lNameArr.add("has");
				lName = referenceType.substring(typeInd + 5, referenceType.length());
				lNameArr.add(lName);
				return lNameArr;
			}
			
			// check for "by" reference
			typeInd = referenceType.indexOf("_by_");
			if (typeInd > -1) {
				String lName = referenceType.substring(0, typeInd);
				lNameArr.add(lName);
				lNameArr.add("by");
				lName = referenceType.substring(typeInd + 4, referenceType.length());
				lNameArr.add(lName);
				return lNameArr;
			}	
			
			// check for "from" reference
			typeInd = referenceType.indexOf("_from_");
			if (typeInd > -1) {
				String lName = referenceType.substring(0, typeInd);
				lNameArr.add(lName);
				lNameArr.add("from");
				lName = referenceType.substring(typeInd + 6, referenceType.length());
				lNameArr.add(lName);
				return lNameArr;
			}	
			return lNameArr;
		}
		
		// intialize
		private void initRelNameToClassNameMap() {
			relNameToClassNameMap.put("agency", "0001_NASA_PDS_1.pds.Agency");          
			relNameToClassNameMap.put("airborne", "0001_NASA_PDS_1.pds.Airborne");
			relNameToClassNameMap.put("facility", "0001_NASA_PDS_1.pds.Facility");
			relNameToClassNameMap.put("instrument", "0001_NASA_PDS_1.pds.Instrument");
			relNameToClassNameMap.put("instrument_host", "0001_NASA_PDS_1.pds.Instrument_Host");
			relNameToClassNameMap.put("investigation", "0001_NASA_PDS_1.pds.Investigation");
			relNameToClassNameMap.put("node", "0001_NASA_PDS_1.pds.Node");
			relNameToClassNameMap.put("observing_system", "0001_NASA_PDS_1.pds.Observing_System");
			relNameToClassNameMap.put("other", "0001_NASA_PDS_1.pds.Other");
//			relNameToClassNameMap.put("PDS_Affiliate", "0001_NASA_PDS_1.pds.PDS_Affiliate");
//			relNameToClassNameMap.put("PDS_Guest", "0001_NASA_PDS_1.pds.PDS_Guest");
			relNameToClassNameMap.put("resource", "0001_NASA_PDS_1.pds.Resource");
			relNameToClassNameMap.put("target", "0001_NASA_PDS_1.pds.Target");
			relNameToClassNameMap.put("telescope", "0001_NASA_PDS_1.pds.Telescope");
//			relNameToClassNameMap.put("Type_List_Area", "0001_NASA_PDS_1.pds.Type_List_Area");
			
			relNameToClassNameMap.put("ancillary", "0001_NASA_PDS_1.pds.Product_Ancillary"); 
			relNameToClassNameMap.put("ancillary_data", "0001_NASA_PDS_1.pds.Product_Ancillary"); 
			relNameToClassNameMap.put("associate", "0001_NASA_PDS_1.pds.Product"); 
			relNameToClassNameMap.put("attribute", "0001_NASA_PDS_1.pds.Product_Attribute_Definition"); 
			relNameToClassNameMap.put("browse", "0001_NASA_PDS_1.pds.Product_Browse"); 
			relNameToClassNameMap.put("bundle", "0001_NASA_PDS_1.pds.Product_Bundle"); 
			relNameToClassNameMap.put("calibrated_product", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("calibration", "0001_NASA_PDS_1.pds.Product_Metadata_Supplemental"); 
			relNameToClassNameMap.put("calibration_document", "0001_NASA_PDS_1.pds.Product_Document"); 
			relNameToClassNameMap.put("calibration_product", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("class", "0001_NASA_PDS_1.pds.Product_Class_Definition"); 
			relNameToClassNameMap.put("collection", "0001_NASA_PDS_1.pds.Product_Collection"); 
			relNameToClassNameMap.put("collection_curated", "0001_NASA_PDS_1.pds.Product_Collection"); 
			relNameToClassNameMap.put("context", "0001_NASA_PDS_1.pds.Product_Context"); 
			relNameToClassNameMap.put("data", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("data_archivist", "0001_NASA_PDS_1.pds.PDS_Affiliate"); 
			relNameToClassNameMap.put("data_curated", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("derived_product", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("document", "0001_NASA_PDS_1.pds.Product_Document"); 
			relNameToClassNameMap.put("errata", "0001_NASA_PDS_1.pds.Product_Document"); 
			relNameToClassNameMap.put("geometry", "0001_NASA_PDS_1.pds.Product_Metadata_Supplemental"); 
			relNameToClassNameMap.put("image", "0001_NASA_PDS_1.pds.Product_Observational"); 
//			relNameToClassNameMap.put("instrument", "0001_NASA_PDS_1.pds.Product_Context"); 
//			relNameToClassNameMap.put("instrument_host", "0001_NASA_PDS_1.pds.Product_Context"); 
//			relNameToClassNameMap.put("investigation", "0001_NASA_PDS_1.pds.Product_Context"); 
			relNameToClassNameMap.put("manager", "0001_NASA_PDS_1.pds.PDS_Affiliate"); 
//			relNameToClassNameMap.put("node", "0001_NASA_PDS_1.pds.Product_Context"); 
			relNameToClassNameMap.put("observatory", "0001_NASA_PDS_1.pds.Product_Context"); 
			relNameToClassNameMap.put("operator", "0001_NASA_PDS_1.pds.PDS_Affiliate"); 
			relNameToClassNameMap.put("package", "0001_NASA_PDS_1.pds.Product_AIP"); 
			relNameToClassNameMap.put("package_compiled", "0001_NASA_PDS_1.pds.Product_AIP"); 
			relNameToClassNameMap.put("personnel", "0001_NASA_PDS_1.pds.PDS_Affiliate"); 
			relNameToClassNameMap.put("product", "0001_NASA_PDS_1.pds.Product_Observational"); 
			relNameToClassNameMap.put("raw_product", "0001_NASA_PDS_1.pds.Product_Observational"); 
//			relNameToClassNameMap.put("resource", "0001_NASA_PDS_1.pds.resource"); 
			relNameToClassNameMap.put("service", "0001_NASA_PDS_1.pds.Product_Service"); 
			relNameToClassNameMap.put("schema", "0001_NASA_PDS_1.pds.Product_XML_Schema"); 
			relNameToClassNameMap.put("spice_kernel", "0001_NASA_PDS_1.pds.Product_SPICE_Kernel"); 
//			relNameToClassNameMap.put("target", "0001_NASA_PDS_1.pds.Product_Context"); 
//			relNameToClassNameMap.put("telescope", "0001_NASA_PDS_1.pds.Product_Context"); 
			relNameToClassNameMap.put("thumbnail", "0001_NASA_PDS_1.pds.Product_Thumbnail"); 
			relNameToClassNameMap.put("update", "0001_NASA_PDS_1.pds.Product_Update"); 
			relNameToClassNameMap.put("zip", "0001_NASA_PDS_1.pds.Product_Zipped"); 
			return;
		}		
	}
