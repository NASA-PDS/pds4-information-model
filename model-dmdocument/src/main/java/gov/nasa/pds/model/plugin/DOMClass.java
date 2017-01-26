package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class DOMClass extends ISOClassOAIS11179 {
	
	String section;									// section of the info model specification document for  this class
	String subModelId;								// identifier of submodel within the registration authority's model.
	String role;										// abstract or concrete
	String docSecType;								// class type = title
	String rootClass;								// RDF identifier
	String baseClassName;							// Fundamental structure class title
	String localIdentifier;							// used temporarily for ingest of LDD
	
	int subClassLevel;
	boolean isUSERClass;							// The class of all classes
	boolean isMasterClass;						// will be included in the master class map
	boolean isSchema1Class;						// will have a schema type 1 created
	boolean isRegistryClass;					// will be included in registry configuration file
	boolean isUsedInModel;
	boolean isVacuous;								// a vacuous class is empty and therefore not to be included in schemas
	boolean isUnitOfMeasure;
	boolean isDataType;
	boolean isTDO;
	boolean isAbstract;
	boolean isChoice;								// class requires xs:choice
	boolean isAny;									// class requires xs:any
	boolean includeInThisSchemaFile;
	boolean isFromLDD;								// has been ingested from Ingest_LDD
	boolean isReferencedFromLDD;				// is a class in the master that is referenced from an LDD
	
	DOMClass subClassOf; 
	ArrayList <DOMClass> superClassHierArr; 
	ArrayList <DOMClass> subClassHierArr; 
	
	ArrayList <DOMProp> ownedAttrArr; 
	ArrayList <DOMProp> inheritedAttrArr; 
	ArrayList <DOMProp> ownedAssocArr; 
	ArrayList <DOMProp> inheritedAssocArr; 
	
	ArrayList <DOMProp> allAttrAssocArr; 
	ArrayList <DOMProp> ownedAttrAssocNOArr;
	ArrayList <DOMProp> ownedAttrAssocArr;					// each class's owned attribute and associations in sorted order
	ArrayList <DOMAttr> ownedAttrAssocAssertArr;			// all enumerated attributes, from this.class through to all superclasses.
	ArrayList <String> ownedAttrAssocAssertTitleArr;	// all enumerated attributes, required to eliminate duplicates
	
	public DOMClass () {
		section = "TBD_section";
		subModelId = "TBD_submodel_identifier";
		role = "TBD_role";
		docSecType = "TBD_type"; 
		rootClass = "TBD_root_class";
		baseClassName = "TBD_base_class_name";
		localIdentifier = "TBD_localIdentifier";
		subClassLevel = 0;
		isUSERClass = false;
		isMasterClass = false;
		isSchema1Class = false;
		isRegistryClass = false;
		isUsedInModel = false;
		isVacuous = false;
		isUnitOfMeasure = false;
		isDataType = false;
		isTDO = false;
		isAbstract = false;
		isChoice = false;
		isAny = false;
		includeInThisSchemaFile = false;
		isFromLDD = false;
		isReferencedFromLDD = false;
		
		subClassOf = null;
		subClassHierArr = new ArrayList <DOMClass> ();  
		superClassHierArr = new ArrayList <DOMClass> ();  
		
		ownedAttrArr = new ArrayList <DOMProp> (); 
		inheritedAttrArr = new ArrayList <DOMProp> (); 
		ownedAssocArr = new ArrayList <DOMProp> (); 
		inheritedAssocArr = new ArrayList <DOMProp> (); 
		
		allAttrAssocArr = new ArrayList <DOMProp> (); 
		ownedAttrAssocNOArr = new ArrayList <DOMProp> ();
		ownedAttrAssocArr = new ArrayList <DOMProp> ();
		ownedAttrAssocAssertArr = new ArrayList <DOMAttr> ();
		ownedAttrAssocAssertTitleArr = new ArrayList <String> ();
	}
	
	public String getSection() {
		return section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public void createDOMClassSingletons (PDSObjDefn lOldClass) {
		rdfIdentifier = lOldClass.rdfIdentifier; 							
		identifier = lOldClass.identifier; 
		versionId = lOldClass.versionId; 
		sequenceId= lOldClass.uid;
		
		title = lOldClass.title;
		definition = lOldClass.description;
		
		registrationStatus = lOldClass.registrationStatus; 
//		isDeprecated = lOldClass.isDeprecated;
		
		regAuthId = lOldClass.regAuthId; 
		steward = lOldClass.steward; 
		nameSpaceId = lOldClass.nameSpaceId; 
		nameSpaceIdNC = lOldClass.nameSpaceIdNC; 

		anchorString = lOldClass.anchorString; 
		
		section = lOldClass.section;
		subModelId = lOldClass.subModelId;
		role = lOldClass.role;
		docSecType = lOldClass.docSecType;
		rootClass = lOldClass.rootClass;
		baseClassName = lOldClass.baseClassName;
		localIdentifier = lOldClass.localIdentifier;
		subClassLevel = lOldClass.subClassLevel;
		isUSERClass = lOldClass.isUSERClass;
		isMasterClass = lOldClass.isMasterClass;
		isSchema1Class = lOldClass.isSchema1Class;
		isRegistryClass = lOldClass.isRegistryClass;
		isUsedInModel = lOldClass.isUsedInModel;
		isVacuous = lOldClass.isVacuous;
		isUnitOfMeasure = lOldClass.isUnitOfMeasure;
		isDataType = lOldClass.isDataType;
		isTDO = lOldClass.isTDO;
		isAbstract = lOldClass.isAbstract;
		isChoice = lOldClass.isChoice;
		isAny = lOldClass.isAny;
		includeInThisSchemaFile = lOldClass.includeInThisSchemaFile;
		isFromLDD = lOldClass.isFromLDD;
		isReferencedFromLDD = lOldClass.isReferencedFromLDD;
		return;
	}
	
// initialize the class hierarchy arrays	
	public void initDOMClassHierArrs (PDSObjDefn lOldClass, TreeMap <String, DOMClass> lDOMClassMap) {
		InitDOMClassHierArr (subClassHierArr, lOldClass.subClass, lDOMClassMap);
		InitDOMClassHierArr (superClassHierArr, lOldClass.superClass, lDOMClassMap);
		PDSObjDefn lOldClassSubClassOf = lOldClass.subClassOfInst;
		if (lOldClassSubClassOf != null) {
			String lRDFIdentifier = lOldClassSubClassOf.rdfIdentifier;
			if (lRDFIdentifier != null && (lRDFIdentifier.indexOf("TBD") != 0) ) {
				subClassOf = lDOMClassMap.get(lRDFIdentifier);
			} else {
				System.out.println(">>warning  - initDOMClassHierArrs  - Failed to find subClassOfInst 1 - lOldClass.rdfIdentifier: " + lOldClass.rdfIdentifier);				
			}
		} else {
			System.out.println(">>warning  - initDOMClassHierArrs  - Failed to find subClassOfInst 2 - lOldClass.rdfIdentifier: " + lOldClass.rdfIdentifier);							
		}
		return;
	}
	
	// initialize a class hierarchy array
	public void InitDOMClassHierArr (ArrayList <DOMClass> lDOMClassArr, ArrayList <PDSObjDefn> lPDSClassArr, TreeMap <String, DOMClass> lDOMClassMap) {
		for (Iterator <PDSObjDefn> i = lPDSClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lOldClass = (PDSObjDefn) i.next();
			// using the RDFIdentifier of the original class, get the new "equivalent" Dom class.
			DOMClass lDOMClass = lDOMClassMap.get(lOldClass.rdfIdentifier);
			if (lDOMClass != null)
				lDOMClassArr.add(lDOMClass);
			else
				System.out.println(">>error    - InitDOMClassHierArr - Failed to find new DOMClass - lOldClass.rdfIdentifier: " + lOldClass.rdfIdentifier);
		}
	}

/*	
	// update the DOMClass property arrays using the the PDSObjDefn class - AssocDefn:AttrDefn:DOMProp map
	public void initDOMClassAttrArrsxxx (PDSObjDefn lOldClass, TreeMap <String, DOMProp> lDOMPropMap, TreeMap <String, DOMAttr> lDOMAttrMap) {
//		System.out.println("\ndebug - initDOMClassAttrArrs - Phase 5a - lOldClass.rdfIdentifier: " + lOldClass.rdfIdentifier);							
		InitAttrArr (ownedAttrArr, lOldClass.ownedAttribute, lDOMPropMap);
//		dumpCounts (ownedAttrArr);
		InitAttrArr (inheritedAttrArr, lOldClass.inheritedAttribute, lDOMPropMap);
		InitAttrArr (ownedAssocArr, lOldClass.ownedAssociation, lDOMPropMap);
		InitAttrArr (inheritedAssocArr, lOldClass.inheritedAssociation, lDOMPropMap);
		InitAttrArr (allAttrAssocArr, lOldClass.allAttrAssocArr, lDOMPropMap);
		InitAttrArr (ownedAttrAssocNOArr, lOldClass.ownedAttrAssocNOArr, lDOMPropMap);
		InitAttrArr (ownedAttrAssocArr, lOldClass.ownedAttrAssocArr, lDOMPropMap);
		InitAttrArr (ownedAttrAssocAssertArr, lOldClass.ownedAttrAssocAssertArr, lDOMPropMap);
//		InitAttrArr (ownedAttrAssocAssertTitleArr, lOldClass.ownedAttrAssocAssertTitleArr, lDOMAttrMap);
		return;
	} */
	
	// update the DOMClass property arrays using the the PDSObjDefn class - AssocDefn:AttrDefn:DOMProp map
	public void initDOMClassAttrArrs (PDSObjDefn lOldClass, TreeMap <String, DOMProp> lDOMPropMap, TreeMap <String, DOMAttr> lDOMAttrMap) {
		InitPropArrDOMProp (ownedAttrArr, lOldClass.ownedAttribute);
		InitPropArrDOMProp (inheritedAttrArr, lOldClass.inheritedAttribute);
		InitPropArrDOMProp (ownedAssocArr, lOldClass.ownedAssociation);
		InitPropArrDOMProp (inheritedAssocArr, lOldClass.inheritedAssociation);
		InitPropArrDOMProp (allAttrAssocArr, lOldClass.allAttrAssocArr);
		InitPropArrISOClassOAIS11179 (hasDOMObject, lOldClass.allAttrAssocArr);
		InitPropArrDOMProp (ownedAttrAssocNOArr, lOldClass.ownedAttrAssocNOArr);
		InitPropArrDOMProp (ownedAttrAssocArr, lOldClass.ownedAttrAssocArr);
		InitPropArrDOMAttr (ownedAttrAssocAssertArr, lOldClass.ownedAttrAssocAssertArr);
		return;
	}
	
	// update the DOMProp property map with the DOMProp that maps to each AttrDefn 
	public void InitPropArrDOMAttr (ArrayList <DOMAttr> lDOMPropArr, ArrayList <AttrDefn> lOldAttrArr) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			ArrayList <ISOClassOAIS11179> lConvertedDOMPropArr = InfoModel.convertAssocDOMPropIdMap.get(lOldAttr.identifier);
			if (lConvertedDOMPropArr != null) {
				for (Iterator <ISOClassOAIS11179> j = lConvertedDOMPropArr.iterator(); j.hasNext();) {
					DOMProp lConvertedDOMProp = (DOMProp) j.next();
					DOMAttr lConvertedDOMAttr = (DOMAttr) lConvertedDOMProp.hasDOMObject;
					lDOMPropArr.add(lConvertedDOMAttr);
				}
			} else {
				System.out.println(">>error    - DOMClass.InitPropArrDOMAttr - Failed to find created DOMPropArr - lOldAttr.identifier: " + lOldAttr.identifier);				
			}
		}
	}	
		
	// update the DOMProp property map with the DOMProp that maps to each AttrDefn 
	public void InitPropArrDOMProp (ArrayList <DOMProp> lDOMPropArr, ArrayList <AttrDefn> lOldAttrArr) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			ArrayList <ISOClassOAIS11179> lConvertedDOMPropArr = InfoModel.convertAssocDOMPropIdMap.get(lOldAttr.identifier);
			if (lConvertedDOMPropArr != null) {
				for (Iterator <ISOClassOAIS11179> j = lConvertedDOMPropArr.iterator(); j.hasNext();) {
					DOMProp lConvertedDOMProp = (DOMProp) j.next();
					lDOMPropArr.add(lConvertedDOMProp);
				}
			} else {
				System.out.println(">>error    - InitPropArrDOMProp - Failed to find created DOMPropArr - lOldAttr.identifier: " + lOldAttr.identifier);				
			}
		}
	}
		
	// update the DOMProp property map with the DOMProp that maps to each AttrDefn 
	public void InitPropArrISOClassOAIS11179 (ArrayList <ISOClassOAIS11179> lDOMPropArr, ArrayList <AttrDefn> lOldAttrArr) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			ArrayList <ISOClassOAIS11179> lConvertedDOMPropArr = InfoModel.convertAssocDOMPropIdMap.get(lOldAttr.identifier);
			if (lConvertedDOMPropArr != null) {
				for (Iterator <ISOClassOAIS11179> j = lConvertedDOMPropArr.iterator(); j.hasNext();) {
					ISOClassOAIS11179 lConvertedDOMProp = (ISOClassOAIS11179) j.next();
					lDOMPropArr.add(lConvertedDOMProp);
				}
			} else {
				System.out.println(">>error    - InitPropArrISOClassOAIS11179 - Failed to find created DOMPropArr - lOldAttr.identifier: " + lOldAttr.identifier);				
			}
		}
	}
	
	// update the DOMProp property map with the DOMProp that maps to each AttrDefn 
	public void InitAttrArrxxx (ArrayList <DOMProp> lDOMPropArr, ArrayList <AttrDefn> lOldAttrArr, TreeMap <String, DOMProp> lDOMPropMap) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			DOMProp lDOMProp = lDOMPropMap.get(lOldAttr.rdfIdentifier);
			if (lDOMProp != null) {
				if (lOldAttr.isAttribute) {
					lDOMPropArr.add(lDOMProp);
				} else {
					lDOMPropArr.add(lDOMProp);
				}
			} else {
				System.out.println(">>error    - DOMClass.InitAttrArr - Failed to find created DOMProp - lOldAttr.rdfIdentifierr: " + lOldAttr.rdfIdentifier);				
			}
		}
	}
	
	public void dumpCounts (ArrayList <DOMProp> lDOMPropArr) {
		System.out.println("debug - dumpCounts - lDOMPropArr.size(): " + lDOMPropArr.size());							
		for (Iterator <DOMProp> i = lDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			System.out.println("debug - dumpCounts - lDOMProp.hasDOMClass.size(): " + lDOMProp.hasDOMClass.size());							
			for (Iterator <ISOClassOAIS11179> j = lDOMProp.hasDOMClass.iterator(); j.hasNext();) {
				DOMAttr lDOMAttr = (DOMAttr) j.next();
				System.out.println("debug - dumpCounts - lDOMAttr.identifier: " + lDOMAttr.identifier);							
			}
		}
	}	
	
	public void InitAttrArrxxxx (ArrayList <DOMProp> lDOMPropArr, ArrayList <AttrDefn> lOldAttrArr, TreeMap <String, DOMProp> lDOMPropMap, TreeMap <String, DOMAttr> lDOMAttrMap) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			DOMAttr lDOMAttr = lDOMAttrMap.get(lOldAttr.rdfIdentifier);
			if (lDOMAttr != null) {
				DOMProp lDOMProp = lDOMPropMap.get(lOldAttr.rdfIdentifier);
				if (lDOMProp != null) {
					if (lOldAttr.isAttribute) {
						lDOMProp.hasDOMClass.add(lDOMAttr);
						lDOMPropArr.add(lDOMProp);
					} else {
						lDOMProp.hasDOMClass.add(lDOMAttr);
						lDOMPropArr.add(lDOMProp);
					}
				} else {
					System.out.println(">>error    - DOMClass.InitAttrArr - Failed to find created DOMProp - lOldAttr.rdfIdentifierr: " + lOldAttr.rdfIdentifier);				
				}
			} else {
				System.out.println(">>error    - DOMClass.InitAttrArr - Failed to find created DOMAttr - lOldAttr.rdfIdentifierr: " + lOldAttr.rdfIdentifier);				
			}
		}
	}
}
