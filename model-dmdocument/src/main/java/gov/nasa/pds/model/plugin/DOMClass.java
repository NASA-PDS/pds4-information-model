package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class DOMClass extends ISOClassOAIS11179 {
	
	String section;									// section of the info model specification document for  this class
	String role;									// abstract or concrete
	String xPath;									// class xpath
	String docSecType;								// the title of the class below USER, for each class's class hierarchy
	String rootClass;								// RDF identifier
	String baseClassName;							// Fundamental structure class title
	String localIdentifier;							// used temporarily for ingest of LDD
	
	int subClassLevel;
	boolean isUSERClass;							// The class of all classes
	boolean isMasterClass;							// will be included in the master class map
	boolean isSchema1Class;							// will have a schema type 1 created
	boolean isRegistryClass;						// will be included in registry configuration file
	boolean isUsedInModel;
	boolean isAnExtension;							// This class is an extension
	boolean isARestriction;							// This class is a restriction
	boolean isVacuous;								// a vacuous class is empty and therefore not to be included in schemas
	boolean isUnitOfMeasure;
	boolean isDataType;
	boolean isTDO;
//	boolean isAbstract;
	boolean isChoice;								// class requires xs:choice
	boolean isAny;									// class requires xs:any
	boolean includeInThisSchemaFile;
	boolean isFromLDD;								// has been ingested from Ingest_LDD
	boolean isReferencedFromLDD;				// is a class in the master that is referenced from an LDD
	boolean isExposed;							// the class is to be exposed in XML Schema - i.e., defined using xs:Element

	DOMProp hasDOMProp;							// associated property - only one allowed
	ArrayList <DOMProtAttr> hasDOMProtAttr;		// the protege attributes to be converted to DOMProp and either DOMAttr or DOMClass
	DOMClass subClassOf; 
	String subClassOfTitle; 					// needed after parsing Protege Pont file to find subClass
	String subClassOfIdentifier; 				// needed after parsing Protege Pont file to find subClass
	ArrayList <DOMClass> superClassHierArr; 
	ArrayList <DOMClass> subClassHierArr; 
	
	ArrayList <DOMProp> ownedAttrArr; 
	ArrayList <DOMProp> inheritedAttrArr; 
	ArrayList <DOMProp> ownedAssocArr; 
	ArrayList <DOMProp> inheritedAssocArr; 
	
	ArrayList <DOMProp> allAttrAssocArr; 
	ArrayList <DOMProp> ownedAttrAssocArr;					// each class's owned attribute and associations in sorted order
	ArrayList <DOMProp> ownedAttrAssocNOArr;
	ArrayList <String> ownedAttrAssocNSTitleArr; 		// needed during attribute/association inheritance processing
	ArrayList <String> ownedTestedAttrAssocNSTitleArr; 		// needed during attribute/association inheritance processing
	TreeMap <String, DOMProp> ownedPropNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass during inheritance processing
	TreeMap <String, DOMAttr> ownedAttrAssocNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass during inheritance processing

	ArrayList <DOMAttr> ownedAttrAssocAssertArr;			// all enumerated attributes, from this.class through to all superclasses.
	ArrayList <String> ownedAttrAssocAssertTitleArr;		// all enumerated attributes, required to eliminate duplicates
	
	public DOMClass () {
		section = "TBD_section";
		role = "TBD_role";
		xPath = "TBD_xPath";
		docSecType = "TBD_type"; 
		rootClass = "TBD_root_class";
		baseClassName = "TBD_base_class_name";
		localIdentifier = "TBD_localIdentifier";
		subClassLevel = 0;
		isUSERClass = false;
//		isUsedInClass = false;
		isMasterClass = false;
		isSchema1Class = false;
		isRegistryClass = false;
		isUsedInModel = false;
		isAnExtension = false;
		isARestriction = false;
		isVacuous = false;
		isUnitOfMeasure = false;
		isDataType = false;
		isTDO = false;
//		isAbstract = false;
		isChoice = false;
		isAny = false;
		includeInThisSchemaFile = false;
		isFromLDD = false;
		isReferencedFromLDD = false;
		
		hasDOMProp = null;
		hasDOMProtAttr = new ArrayList <DOMProtAttr> ();
		
		subClassOf = null;
		subClassOfTitle = "TBD_subClassOfTitle"; 					
		subClassOfIdentifier = "TBD_subClassOfIdentifier";
		
		subClassHierArr = new ArrayList <DOMClass> ();				// all subclasses (children) of this class
		superClassHierArr = new ArrayList <DOMClass> ();  			// the superclass (parent) hierarchy for this class
		
		ownedAttrArr = new ArrayList <DOMProp> (); 
		inheritedAttrArr = new ArrayList <DOMProp> (); 
		ownedAssocArr = new ArrayList <DOMProp> (); 
		inheritedAssocArr = new ArrayList <DOMProp> (); 
		
		allAttrAssocArr = new ArrayList <DOMProp> (); 
		ownedAttrAssocArr = new ArrayList <DOMProp> ();
		ownedAttrAssocNOArr = new ArrayList <DOMProp> ();
		ownedAttrAssocNSTitleArr = new ArrayList <String> (); 
		ownedTestedAttrAssocNSTitleArr = new ArrayList <String> (); 
		ownedPropNSTitleMap = new TreeMap <String, DOMProp> ();
		ownedAttrAssocNSTitleMap = new TreeMap <String, DOMAttr> ();
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
		subClassOfTitle = lOldClass.subClassOfTitle; 
		subClassOfIdentifier = lOldClass.subClassOfIdentifier;
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
				// Setting subClassOfTitle, subClassOfIdentifier are not needed; see createDOMClassSingletons				
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

	//  Check whether this class is an extension or restriction
	public void setisExtentionOrRestrictionClass () {
		DOMClass lSuperClass = this.subClassOf;
		if (lSuperClass == null) return;

		ArrayList <String> lThisIdentifierArr = new ArrayList <String> ();
		ArrayList <String> lSuperIdentifierArr = new ArrayList <String> ();

		// first get the identifiers of the super class's members (attributes and classes)
		for (Iterator<DOMProp> i = lSuperClass.allAttrAssocArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			for (Iterator<ISOClassOAIS11179> j = lProp.hasDOMClass.iterator(); j.hasNext();) {
				ISOClassOAIS11179 lISOClassOAIS11179 = (ISOClassOAIS11179) j.next();
				if (lISOClassOAIS11179 instanceof DOMAttr) {
					DOMAttr lSCPAttr = (DOMAttr) lISOClassOAIS11179;
					lSuperIdentifierArr.add(lSCPAttr.identifier);
				} else {
					DOMClass lSCPClass = (DOMClass) lISOClassOAIS11179;
					lSuperIdentifierArr.add(lSCPClass.identifier);
				}
			}
		}
		
		// second get the identifiers of this class's members (attributes and classes)
		for (Iterator<DOMProp> i = allAttrAssocArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			for (Iterator<ISOClassOAIS11179> j = lProp.hasDOMClass.iterator(); j.hasNext();) {
				ISOClassOAIS11179 lISOClassOAIS11179 = (ISOClassOAIS11179) j.next();
				if (lISOClassOAIS11179 instanceof DOMAttr) {
					DOMAttr lSCPAttr = (DOMAttr) lISOClassOAIS11179;
					lThisIdentifierArr.add(lSCPAttr.identifier);
				} else {
					DOMClass lSCPClass = (DOMClass) lISOClassOAIS11179;
					lThisIdentifierArr.add(lSCPClass.identifier);
				}
			}
		}
		
		boolean foundAllSuperIdentifers = true;
		//	check that all identifiers (attributes and classes) of the super class are in this class
		for (Iterator<String> i = lSuperIdentifierArr.iterator(); i.hasNext();) {
			String lSuperIdentifier = (String) i.next();
			if (! (lThisIdentifierArr.contains(lSuperIdentifier))) foundAllSuperIdentifers = false;
		}
		
		boolean foundAllThisIdentifers = true;
		//	check that all identifiers (attributes and classes) of this class are in the super class
		for (Iterator<String> i = lThisIdentifierArr.iterator(); i.hasNext();) {
			String lThisIdentifier = (String) i.next();
			if (! (lSuperIdentifierArr.contains(lThisIdentifier))) foundAllThisIdentifers = false;
		}
		
		if (foundAllSuperIdentifers) {
			if (foundAllThisIdentifers) {
				isAnExtension = false;
				isARestriction = true;	
			} else {
				isAnExtension = true;
				isARestriction = false;
			}
		}
	}
}
