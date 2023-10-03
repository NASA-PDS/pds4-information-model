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
import java.util.Iterator;
import java.util.TreeMap;

public class ClassAttrPropClassification {
	
	// Class properties
	TreeMap <String, ClassPropertiesOWL> classPropertiesIdOWLMap = new TreeMap <String, ClassPropertiesOWL> ();
	TreeMap <String, String> classTitleAliasMap = new TreeMap <String, String> ();
	ArrayList <String> nameSpaceIdNCArr = new ArrayList <String>  ();
	static String masterNameSpaceIdNC = "TBD_masterNameSpaceIdNC";
	
	ArrayList <ClassPropertiesOWL> classPropertiesOWLDebugArr = new ArrayList <ClassPropertiesOWL> ();
	
	// Relationship Properties
	ArrayList <RelationshipProperties> relationshipPropertiesArr = new ArrayList <RelationshipProperties> ();

	// all selected classes and all selected class identifiers
	ArrayList <String> selectedClassIdArr = new ArrayList <String> ();
	ArrayList <DOMClass> selectedClassArr = new ArrayList <DOMClass> ();
	
	TreeMap <String, String> relNameToClassNameMap  = new TreeMap <String, String> ();

	// Constructor - Initialize ClassAttrPropClassification based on input indicator
	public ClassAttrPropClassification (String lClassificationId) {
			
		if (lClassificationId.compareTo("PDS4.All.Products.Class.Prop") == 0) {
//			System.out.println("\ndebug ClassAttrPropClassification - PDS4.All.Products.Class.Prop");
			// get All Products, Classes, and Properties for OWL/TTL		
//			getAllPropertiesForSelectedClasses ("pds", setPDS4ProductsClassPropTitles_All_Small(), true);
			getAllPropertiesForSelectedClasses ("pds", setPDS4ProductsClassPropTitles_All(), true);
		} else if (lClassificationId.compareTo("PDS4.LDD.All") == 0) {
			// get all class from a stack of LDDs	
			getPDS4LDDAll ();
		} else if (lClassificationId.compareTo("TNDO_Identified") == 0) {
			// get all Tagged Non_Digital Object (Context)	
			getTNDOIdentified ();
		} else if (lClassificationId.compareTo("EIMEntity") == 0) {
			// get EIM Entities 	
//			getEIMEntity ("eim");
			getAllPropertiesForSelectedClasses ("eim", setEIMEnityTitles(), true);
		} else if (lClassificationId.compareTo("OAISIF") == 0) {
//			System.out.println("\ndebug ClassAttrPropClassification - OAISIF");
			// get OAIS-IF entities	
			getAllPropertiesForSelectedClasses ("oais", setOAISIFProductPropTitles (), true);
//			getOAISIF ("oais");
		}
		
		// get relational properties of classes - owned and inherited associations.
// 220518		getRelationships (lClassificationId);   // inits getRelationships -  used by WriteDOMRDFTTLFilePDS4 - duplicate of lClassProperties.getProperties(lSelectedClass)
		
		// set up aliases
		classTitleAliasMap.put("product_data_object", "has_Data_Object");
	}
	
	
	// start of classification types - each has its own selection code
	public boolean getAllPropertiesForSelectedClasses (String nameSpaceId, ArrayList <String> selectedClassesTitleArr, boolean isRecursive) {

		// set the master namespace for the TTL file; used by WriteDOMRDFTTLFile
		masterNameSpaceIdNC = nameSpaceId;
		
		// get the initial selected class titles
		ArrayList <DOMClass> initialDOMClassesArr = getInitialDOMClassesArr(nameSpaceId, selectedClassesTitleArr);

		// from the initial DOMClasses get all DOMClasses - the union of initial as well as member DOMClasses
		selectedClassArr.addAll(initialDOMClassesArr);
		for (DOMClass lDOMClass : initialDOMClassesArr) {
			getMemberOfMembersDOMClassesArr (lDOMClass, 0);
		}
		
		// get the class properties
		for (DOMClass lDOMClass : selectedClassArr) {
//			getClassProperties (lDOMClass, new ArrayList <String> (), 0);
			getClassProperties (lDOMClass);
		}
		
		if (selectedClassArr.size() > 0) return true;
		return false;
	}	

	// get the members of members DOMClasses
	public void getMemberOfMembersDOMClassesArr(DOMClass lDOMClass, int count) {
//		if (count < 1) System.out.println("");
//		System.out.println("debug -------START-----count:" + count + "--" + lDOMClass.identifier + "_start");

		for (DOMClass lMemberfDOMClass : getMembersOfDOMClassArr(lDOMClass)) {
			if (lMemberfDOMClass.isInactive) continue;
			if (lMemberfDOMClass.isDeprecated) continue;
			if (! selectedClassIdArr.contains(lMemberfDOMClass.identifier)) {
				selectedClassIdArr.add(lMemberfDOMClass.identifier);
				selectedClassArr.add(lMemberfDOMClass);
				getMemberOfMembersDOMClassesArr(lMemberfDOMClass, count++);
			}
		}
//		System.out.println("debug -------END-----count:" + count + "--" + lDOMClass.identifier + "_end");
		return;
	}

	// get all member DOMClasses from a single DOMClass
	public ArrayList <DOMClass> getMembersOfDOMClassArr (DOMClass lDOMClass) {
		ArrayList <DOMClass> memberDOMClassesArr = new ArrayList <DOMClass> ();
		ArrayList <String> memberDOMClassesIdArr = new ArrayList <String> ();
		ArrayList <DOMProp> allAssocArr = new ArrayList <DOMProp> ();
		if (lDOMClass.ownedAssocArr != null && lDOMClass.ownedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.ownedAssocArr);
		if (lDOMClass.inheritedAssocArr != null && lDOMClass.inheritedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.inheritedAssocArr);
		if (allAssocArr != null && allAssocArr.size() > 0) {
			for (DOMProp lDOMProp : allAssocArr) {
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {					
					DOMClass lDOMMemberClass = (DOMClass) lDOMProp.hasDOMObject;
					if (! memberDOMClassesIdArr.contains(lDOMMemberClass.identifier)) {
						memberDOMClassesIdArr.add(lDOMMemberClass.identifier);
						memberDOMClassesArr.add(lDOMMemberClass);
					}
				}
			}
		}
		return memberDOMClassesArr;
	}
	
	// get the initial DOMClasses from the Class title array
	public ArrayList <DOMClass> getInitialDOMClassesArr (String nameSpaceId, ArrayList <String> lClassesTitleArr) {
		ArrayList <DOMClass> initialDOMClassesArr = new ArrayList <DOMClass> ();
		for (String lClassTitle : lClassesTitleArr) {
			DOMClass lDOMClass = getClassFromClassTitle (nameSpaceId, lClassTitle);
			if (lDOMClass != null)
				initialDOMClassesArr.add(getClassFromClassTitle (nameSpaceId, lClassTitle));
			else
				System.out.println("ERROR - Failed find selected class -ClassAttrPropClassification- lClassTitle:" + lClassTitle);
		}
		return initialDOMClassesArr;
	}
	
	// get the DOMClass from the Class title
	public DOMClass getClassFromClassTitle (String nameSpaceId, String lClassTitle) {
		return getClassFromClassId (DOMInfoModel.getClassIdentifier(nameSpaceId, lClassTitle));
	}	
	
	// get the DOMClass from the Class Identifier
	public DOMClass getClassFromClassId (String lClassId) {

		// get the class from the master DOM CLass Identifier Map
		DOMClass lSelectedClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
		if (lSelectedClass != null) {
			if (lSelectedClass.isInactive) return null;
			if (lSelectedClass.isDeprecated) return null;
			return lSelectedClass;

		} else {
			System.out.println("ERROR - Failed to find selected class -  lClassId:" + lClassId);
		}
		return null;
	}
	
	// get the class properties
	public boolean getClassProperties (DOMClass lDOMClass) {
//		System.out.println("debug getClassProperties lDOMClass.identifier:" + lDOMClass.identifier);

		// create the Class Properties OWL class
		ClassPropertiesOWL lClassPropertiesOWL = new ClassPropertiesOWL (lDOMClass);
		
		// add to the maps; duplicates are overridden
		classPropertiesIdOWLMap.put(lClassPropertiesOWL.identifier, lClassPropertiesOWL);
		
		// collect new namespaces for RDF namespace declarations
		if (! nameSpaceIdNCArr.contains(lClassPropertiesOWL.nameSpaceIdNC))
			nameSpaceIdNCArr.add(lClassPropertiesOWL.nameSpaceIdNC);
				
		// initialize the Class Property Class Array - ClassPropertiesOWL.theClassPropClassArr
		lClassPropertiesOWL.getAssociatedClasses(lDOMClass);
				
		// initialize the Class Property Attribute Array - ClassPropertiesOWL.theClassPropAttrArr
		lClassPropertiesOWL.getAssociatedAttributes(lDOMClass);
				
		// get the class subclass of relationship
		lClassPropertiesOWL.getSubClassOf (lDOMClass);
								
		// initialize the components Of Class Array - ClassPropertiesOWL.componentOfClassArr
		// same as getAssociatedClasses but creates new ClassPropertiesOWL for each component class
		getAssociatedClassesPlus (lClassPropertiesOWL, lDOMClass);

		return true;
	}
	
	// get classes associated with the selected class; create new ClassPropertiesOWL
	public void getAssociatedClassesPlus (ClassPropertiesOWL lSelectedClassPropertiesOWL, DOMClass lSelectedClass) {

		// scan for all associated classes of the selected class
		ArrayList <DOMProp> allAssocArr = new ArrayList <DOMProp> ();
		if (lSelectedClass.ownedAssocArr != null && lSelectedClass.ownedAssocArr.size() > 0) allAssocArr.addAll(lSelectedClass.ownedAssocArr);
		if (lSelectedClass.inheritedAssocArr != null && lSelectedClass.inheritedAssocArr.size() > 0) allAssocArr.addAll(lSelectedClass.inheritedAssocArr);
		if (allAssocArr != null && allAssocArr.size() > 0) {
			for (Iterator <DOMProp> j = allAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {					
					DOMClass lDOMMemberClass = (DOMClass) lDOMProp.hasDOMObject;
					if (lDOMMemberClass.isInactive) continue;
					if (lDOMMemberClass.isDeprecated) continue;
					
					// create the Class Properties OWL class
					ClassPropertiesOWL lClassPropertiesOWL = new ClassPropertiesOWL (lDOMMemberClass);
					lSelectedClassPropertiesOWL.theComponentOfClassArr.add(lClassPropertiesOWL);
					
					// debug
					classPropertiesOWLDebugArr.add(lClassPropertiesOWL);
					
					// add to the maps, not needed since we recurse down the associated member classes in each parent class
					// classPropertiesIdOWLMap.put(lClassPropertiesOWL.identifier, lClassPropertiesOWL);

					// collect new namespaces for RDF namespace declarations
					if (! nameSpaceIdNCArr.contains(lClassPropertiesOWL.nameSpaceIdNC))
						nameSpaceIdNCArr.add(lClassPropertiesOWL.nameSpaceIdNC);
					
					// init the class properties array
					lClassPropertiesOWL.getAssociatedClasses(lDOMMemberClass); //  used by WriteNeo4J - duplicate of getRelationships (lClassificationId);

					// init the class attribute array
					lClassPropertiesOWL.getAssociatedAttributes(lDOMMemberClass);
					
					// get the class subclass of relationship
					lClassPropertiesOWL.getSubClassOf (lDOMMemberClass);
				}
			}
		}
		return;
	}
	
	public ArrayList <String> setPDS4ProductsClassPropTitles_All_Small () {
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("Agency");
		selectedClassesTitleArr.add ("Context_Area");
//		selectedClassesTitleArr.add ("Product_Collection");
		return selectedClassesTitleArr;
	}	
	
	public ArrayList <String> setPDS4ProductsClassPropTitles_All () {
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("Product_Context");
		selectedClassesTitleArr.add ("Agency");
		selectedClassesTitleArr.add ("Airborne");
		selectedClassesTitleArr.add ("Facility");
		selectedClassesTitleArr.add ("Instrument");
		selectedClassesTitleArr.add ("Instrument_Host");
		selectedClassesTitleArr.add ("Investigation");
		selectedClassesTitleArr.add ("Node");
		selectedClassesTitleArr.add ("Other");
		selectedClassesTitleArr.add ("PDS_Affiliate");
		selectedClassesTitleArr.add ("PDS_Guest");
		selectedClassesTitleArr.add ("Resource");
		selectedClassesTitleArr.add ("Target");
		selectedClassesTitleArr.add ("Telescope");
		selectedClassesTitleArr.add ("Observing_System");
//		selectedClassesTitleArr.add ("Service");
//		selectedClassesTitleArr.add ("Product_AIP");
		selectedClassesTitleArr.add ("Product_Ancillary");
//		selectedClassesTitleArr.add ("Product_Attribute_Definition");
		selectedClassesTitleArr.add ("Product_Browse");
		selectedClassesTitleArr.add ("Product_Bundle");
//		selectedClassesTitleArr.add ("Product_Class_Definition");
		selectedClassesTitleArr.add ("Product_Collection");
		selectedClassesTitleArr.add ("Product_Context");
//		selectedClassesTitleArr.add ("Product_DIP");
//		selectedClassesTitleArr.add ("Product_DIP_Deep_Archive");
//		selectedClassesTitleArr.add ("Product_Data_Set_PDS3");
		selectedClassesTitleArr.add ("Product_Document");
		selectedClassesTitleArr.add ("Product_File_Repository");
		selectedClassesTitleArr.add ("Product_File_Text");
//		selectedClassesTitleArr.add ("Product_Instrument_Host_PDS3");
//		selectedClassesTitleArr.add ("Product_Instrument_PDS3");
		selectedClassesTitleArr.add ("Product_Metadata_Supplemental");
//		selectedClassesTitleArr.add ("Product_Mission_PDS3");
//		selectedClassesTitleArr.add ("Product_Native");
		selectedClassesTitleArr.add ("Product_Observational");
//		selectedClassesTitleArr.add ("Product_Proxy_PDS3");
//		selectedClassesTitleArr.add ("Product_SIP");
//		selectedClassesTitleArr.add ("Product_SIP_Deep_Archive");
		selectedClassesTitleArr.add ("Product_SPICE_Kernel");
//		selectedClassesTitleArr.add ("Product_Service");
//		selectedClassesTitleArr.add ("Product_Software");
//		selectedClassesTitleArr.add ("Product_Subscription_PDS3");
//		selectedClassesTitleArr.add ("Product_Target_PDS3");
		selectedClassesTitleArr.add ("Product_Thumbnail");
//		selectedClassesTitleArr.add ("Product_Update");
//		selectedClassesTitleArr.add ("Product_Volume_PDS3");
//		selectedClassesTitleArr.add ("Product_Volume_Set_PDS3");
		selectedClassesTitleArr.add ("Product_XML_Schema");
//		selectedClassesTitleArr.add ("Product_Zipped");
		selectedClassesTitleArr.add ("Ingest_LDD");
		return selectedClassesTitleArr;
	}
	
	public ArrayList <String> setPDS4ProductsClassPropTitles_Allxxx () {
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("Product_Context");
		selectedClassesTitleArr.add ("Agency");
		selectedClassesTitleArr.add ("Airborne");
		selectedClassesTitleArr.add ("Facility");
		selectedClassesTitleArr.add ("Instrument");
		selectedClassesTitleArr.add ("Instrument_Host");
		selectedClassesTitleArr.add ("Investigation");
		selectedClassesTitleArr.add ("Node");
		selectedClassesTitleArr.add ("Other");
		selectedClassesTitleArr.add ("PDS_Affiliate");
		selectedClassesTitleArr.add ("PDS_Guest");
		selectedClassesTitleArr.add ("Resource");
		selectedClassesTitleArr.add ("Target");
		selectedClassesTitleArr.add ("Telescope");
		selectedClassesTitleArr.add ("Observing_System");
		selectedClassesTitleArr.add ("Service");
		selectedClassesTitleArr.add ("Product_AIP");
		selectedClassesTitleArr.add ("Product_Ancillary");
		selectedClassesTitleArr.add ("Product_Attribute_Definition");
		selectedClassesTitleArr.add ("Product_Browse");
		selectedClassesTitleArr.add ("Product_Bundle");
		selectedClassesTitleArr.add ("Product_Class_Definition");
		selectedClassesTitleArr.add ("Product_Collection");
		selectedClassesTitleArr.add ("Product_Context");
		selectedClassesTitleArr.add ("Product_DIP");
		selectedClassesTitleArr.add ("Product_DIP_Deep_Archive");
		selectedClassesTitleArr.add ("Product_Data_Set_PDS3");
		selectedClassesTitleArr.add ("Product_Document");
		selectedClassesTitleArr.add ("Product_File_Repository");
		selectedClassesTitleArr.add ("Product_File_Text");
		selectedClassesTitleArr.add ("Product_Instrument_Host_PDS3");
		selectedClassesTitleArr.add ("Product_Instrument_PDS3");
		selectedClassesTitleArr.add ("Product_Metadata_Supplemental");
		selectedClassesTitleArr.add ("Product_Mission_PDS3");
		selectedClassesTitleArr.add ("Product_Native");
		selectedClassesTitleArr.add ("Product_Observational");
		selectedClassesTitleArr.add ("Product_Proxy_PDS3");
		selectedClassesTitleArr.add ("Product_SIP");
		selectedClassesTitleArr.add ("Product_SIP_Deep_Archive");
		selectedClassesTitleArr.add ("Product_SPICE_Kernel");
		selectedClassesTitleArr.add ("Product_Service");
		selectedClassesTitleArr.add ("Product_Software");
		selectedClassesTitleArr.add ("Product_Subscription_PDS3");
		selectedClassesTitleArr.add ("Product_Target_PDS3");
		selectedClassesTitleArr.add ("Product_Thumbnail");
		selectedClassesTitleArr.add ("Product_Update");
		selectedClassesTitleArr.add ("Product_Volume_PDS3");
		selectedClassesTitleArr.add ("Product_Volume_Set_PDS3");
		selectedClassesTitleArr.add ("Product_XML_Schema");
		selectedClassesTitleArr.add ("Product_Zipped");
		selectedClassesTitleArr.add ("Ingest_LDD");
		return selectedClassesTitleArr;
	}
	
	// start of classification types - each has its own selection code
	public ArrayList <String>  setOAISIFProductPropTitles () {
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("Information_Object");
		selectedClassesTitleArr.add ("Access_Rights_Information");
		selectedClassesTitleArr.add ("Content_Information");
		selectedClassesTitleArr.add ("Context_Information");
		selectedClassesTitleArr.add ("Fixity_Information");
		selectedClassesTitleArr.add ("Packaged_Information");
		selectedClassesTitleArr.add ("Provenance_Information");
		selectedClassesTitleArr.add ("Reference_Information");
		selectedClassesTitleArr.add ("Representation_Information");
		selectedClassesTitleArr.add ("Preservation_Description_Information");
		selectedClassesTitleArr.add ("Archival_Information_Package");
		selectedClassesTitleArr.add ("Dissemination_Information_Package");
		selectedClassesTitleArr.add ("Submission_Information_Package");
		selectedClassesTitleArr.add ("Data_Object");
		selectedClassesTitleArr.add ("Content_Data_Object");
		selectedClassesTitleArr.add ("Semantic_Information");
		selectedClassesTitleArr.add ("Structure_Information");
		return selectedClassesTitleArr;
	}	
	
	// start of classification types - each has its own selection code
	public ArrayList <String>  setEIMEnityTitles () {
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("Entity");
		selectedClassesTitleArr.add ("Artifact");
		selectedClassesTitleArr.add ("Authority");
		selectedClassesTitleArr.add ("BillOfMaterials");
		selectedClassesTitleArr.add ("Costs");
		selectedClassesTitleArr.add ("Domain");
		selectedClassesTitleArr.add ("Facility");
		selectedClassesTitleArr.add ("Format");
		selectedClassesTitleArr.add ("Hardware");
		selectedClassesTitleArr.add ("Instrument");
		selectedClassesTitleArr.add ("Instrument_Host");
		selectedClassesTitleArr.add ("Investigation");
		selectedClassesTitleArr.add ("Mission");
		selectedClassesTitleArr.add ("Organization");
		selectedClassesTitleArr.add ("Person");
		selectedClassesTitleArr.add ("Research");
		selectedClassesTitleArr.add ("Role");
		selectedClassesTitleArr.add ("Service");
		selectedClassesTitleArr.add ("Work_Activity");
		selectedClassesTitleArr.add ("Vendor");
		selectedClassesTitleArr.add ("Model");
		selectedClassesTitleArr.add ("Data");
		selectedClassesTitleArr.add ("Document");
		selectedClassesTitleArr.add ("Project");
		selectedClassesTitleArr.add ("Task");
		return selectedClassesTitleArr;
	}
	
	// **********************************************************************************************
	//	Everything below here needs conversion to getAllPropertiesForSelectedClasses
	// **********************************************************************************************
	
	// everything
	public boolean getPDS4LDDAll () {
		// classes to be selected are identified.
		int classCount = 0;
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lSelectedClass = (DOMClass) i.next();	
			if (lSelectedClass.isInactive) continue;
			if ((lSelectedClass.isUSERClass || lSelectedClass.isVacuous)) continue;
			if (lSelectedClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if ((lSelectedClass.isUnitOfMeasure || lSelectedClass.isDataType)) continue;
			if (lSelectedClass.isDeprecated) continue;
			
//			System.out.println("debug getPDS4LDDAll lSelectedClass.identifier:" + lSelectedClass.identifier);
			
			// a class selected for processing; create a ClassProperties class
			selectedClassArr.add(lSelectedClass);
//			if (classCount++ > 250) { break; };
		}
		
		if (selectedClassArr.size() > 0) return true;
		return false;
	}
	
	// start of classification types - each has its own selecion code
	public boolean getTNDOIdentified () {
		// classes to be selected are identified by title.
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		selectedClassesTitleArr.add ("TNDO_Identified");
		selectedClassesTitleArr.add ("Campaign");
		selectedClassesTitleArr.add ("Invoice");
		selectedClassesTitleArr.add ("Person");
		selectedClassesTitleArr.add ("Site");
		selectedClassesTitleArr.add ("Treatment");
		selectedClassesTitleArr.add ("Designer");
		selectedClassesTitleArr.add ("Doctor");
		selectedClassesTitleArr.add ("Patient");
		selectedClassesTitleArr.add ("Laboratory");
		selectedClassesTitleArr.add ("Office");

		
		// using the titles get the DOMClass
		for (Iterator<String> i = selectedClassesTitleArr.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("pds", lClassName);
			DOMClass lSelectedClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lSelectedClass != null) {
				if (lSelectedClass.isInactive) continue;
				// get the class properties
				selectedClassArr.add (lSelectedClass);
			}
		}
		if (selectedClassArr.size() > 0) return true;
		return false;
	}	
	
	// start of classification types - each has its own selecion code
	// *** deprecate ***
	public boolean getEIMEntity (String namespaceid) {
		// classes to be selected are identified by title.
		ArrayList <String> selectedClassesTitleArr = new ArrayList <String> ();
		
		  selectedClassesTitleArr.add ("Entity");
		  selectedClassesTitleArr.add ("Artifact");
		  selectedClassesTitleArr.add ("Authority");
		  selectedClassesTitleArr.add ("BillOfMaterials");
		  selectedClassesTitleArr.add ("Costs");
		  selectedClassesTitleArr.add ("Domain");
		  selectedClassesTitleArr.add ("Facility");
		  selectedClassesTitleArr.add ("Format");
		  selectedClassesTitleArr.add ("Hardware");
		  selectedClassesTitleArr.add ("Instrument");
		  selectedClassesTitleArr.add ("Instrument_Host");
		  selectedClassesTitleArr.add ("Investigation");
		  selectedClassesTitleArr.add ("Mission_Objective");
		  selectedClassesTitleArr.add ("Mission_PerformingElement"); 
		  selectedClassesTitleArr.add ("Mission_Product");
		  selectedClassesTitleArr.add ("Organization");
		  selectedClassesTitleArr.add ("People");
		  selectedClassesTitleArr.add ("Research");
		  selectedClassesTitleArr.add ("Role");
		  selectedClassesTitleArr.add ("Service");
		  selectedClassesTitleArr.add ("Work_Activity");
		
		// using the titles get the DOMClass
		for (Iterator<String> i = selectedClassesTitleArr.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
//			String lClassId = DOMInfoModel.getClassIdentifier("eim", lClassName);
			String lClassId = DOMInfoModel.getClassIdentifier(namespaceid, lClassName);
			DOMClass lSelectedClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lSelectedClass != null) {
				if (lSelectedClass.isInactive) continue;
				// get the class properties
				selectedClassArr.add (lSelectedClass);
			}
		}
		if (selectedClassArr.size() > 0) return true;
		return false;
	}
	
	public boolean getPDS4ClassesAll () {
		int classCount = 0;
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();	
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if ((lClass.isUnitOfMeasure || lClass.isDataType)) continue;
			if (lClass.isDeprecated) continue;
			selectedClassArr.add(lClass);
//			if (classCount++ > 250) { break; };
		}
		if (selectedClassArr.size() > 0) return true;
		return false;
	}	
	
	// for Context products, get the values of reference_type for Internal_Reference {Reference_List, ...}
	// *** to be continued ***
	public void getRelationships (String lClassificationId) {
		if (lClassificationId.compareTo("PDS4.All.Products.Class.Prop") == 0) {
			getRelationshipsPDS4AllProductsClassProp();
		} else if (lClassificationId.compareTo("PDS4.LDD.All") == 0) {
			getRelationshipsPDS4AllProductsClassProp();
			getRelationshipsFromOwnedAssoc();
		}  else if (lClassificationId.compareTo("OAISIF") == 0) {
//			getRelationshipsFromOwnedAssoc();
		}
	}	
	
	// the relationships defined using Internal_Reference
	public void getRelationshipsPDS4AllProductsClassProp () {
		for (Iterator <DOMRule> i = DOMInfoModel.masterDOMRuleArr.iterator(); i.hasNext();) {
			DOMRule lDOMRule = (DOMRule) i.next();
			if (lDOMRule.xpath.indexOf("Internal_Reference") < 0) continue;
			for (Iterator <DOMAssert> j = lDOMRule.assertArr.iterator(); j.hasNext();) {
				DOMAssert lAssert = (DOMAssert) j.next();	
				for (Iterator<String> k = lAssert.testValArr.iterator(); k.hasNext();) {
					String lTestValueString = (String) k.next();
					RelationshipProperties lRelationshipProperties = new RelationshipProperties (lTestValueString, classPropertiesIdOWLMap);					
					if (lRelationshipProperties.lidvid != null) {
						relationshipPropertiesArr.add(lRelationshipProperties);

					}
				}
			}
		}
	}		

	// get the relationships defined in the IM (owned associations)
	public void getRelationshipsFromOwnedAssoc () {
	for (Iterator <DOMClass> i = selectedClassArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			
			// change to allow both owned and inherited associated components to be included
			ArrayList <DOMProp> allAssocArr = new ArrayList <DOMProp> ();
			if (lDOMClass.ownedAssocArr != null && lDOMClass.ownedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.ownedAssocArr);
			if (lDOMClass.inheritedAssocArr != null && lDOMClass.inheritedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.inheritedAssocArr);
			if (allAssocArr != null && allAssocArr.size() > 0) {
				for (Iterator <DOMProp> j = allAssocArr.iterator(); j.hasNext();) {
					DOMProp lDOMProp = (DOMProp) j.next();
					if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {					
						DOMClass lDOMMemberClass = (DOMClass) lDOMProp.hasDOMObject;
						RelationshipProperties lRelationshipProperties = new RelationshipProperties (lDOMClass, lDOMProp, lDOMMemberClass, classPropertiesIdOWLMap);					
						if (lRelationshipProperties.lidvid != null) {
							relationshipPropertiesArr.add(lRelationshipProperties);
						}
					}
				}
			}
		}
	}
	
// ======= public procedures =======
	
	
	// get the master namespace
	static public String getMasterNameSpaceIdNC () {
		return masterNameSpaceIdNC;
	}
	
	public ArrayList <DOMClass> getSelectedClassArr () {
		if (selectedClassArr.size() > 0) return selectedClassArr;
		return null;
	}
	
	// get classProperties
	public ArrayList <ClassPropertiesOWL> getClassPropertiesOWL () {
		ArrayList <ClassPropertiesOWL> classPropertiesOWLArr = new ArrayList <ClassPropertiesOWL> (classPropertiesIdOWLMap.values());
		return classPropertiesOWLArr;
	}
	
	
	// get a classProperties
	public ClassPropertiesOWL getClassPropertyOWL (String identifier) {
		ClassPropertiesOWL lClassPropertiesOWL = classPropertiesIdOWLMap.get(identifier);
		return lClassPropertiesOWL;
	}
	
	// get Relationship Properties
	public ArrayList <RelationshipProperties> getRelationshipProperties () {
		return relationshipPropertiesArr;
	}
		
	// get alias
	public String getTitleAlias (String lTitle) {
		String lAlias = classTitleAliasMap.get(lTitle);
		if (lAlias == null) lAlias = lTitle;
		return lAlias;
	}
}
