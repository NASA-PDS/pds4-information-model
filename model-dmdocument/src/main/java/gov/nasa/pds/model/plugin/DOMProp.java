package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.TreeMap;

public class DOMProp extends ISOClassOAIS11179 {
	
	String cardMin;
	String cardMax;
	int cardMinI;
	int cardMaxI;
	String classOrder;						// the order of the attribute or association within a class
	
// deprecated to be replaced by hasDOMObject
	ArrayList <ISOClassOAIS11179> hasDOMClass;		// allows more than one object (permissible values)
	DOMClass attrParentClass; 				// class instance that this object is a member of
	ISOClassOAIS11179 hasDOMObject;			// OVERRIDE - allows only one object (class, attribute, permissible value, etc, but no DOMProp
	ArrayList <String> valArr;				// the protege values, either attributes or class titles
	
	String localIdentifier;					// local_identifier (the or first local identifier in the Association set)
    													// needed only for display
//	String className;
	String parentClassTitle;						// class that this attribute is a member of
	String classNameSpaceIdNC;
	String classSteward;							// steward for attribute's class

	String groupName;							// the choice group name
	String referenceType;
	boolean isPDS4;								// true->PDS4 keyword used in Protege
	boolean isAttribute;						// true->attribute; false->association
	boolean isChoice;							// allows an xs:choice
	boolean isAny;								// allows an xs:any
	boolean isSet;								// is a set of either attributes or associations (AttrDefn)
	boolean isRestrictedInSubclass;						// the member attribute/class is owned in this class AND some superclass
	
	// LDD Attributes
	String enclLocalIdentifier;				// local_identifier of enclosing class
	String minimumOccurrences;
	String maximumOccurrences;
	
	public DOMProp () {
		cardMin = "0"; 
		cardMax = "0";
		cardMinI = 0; 
		cardMaxI = 0;
		classOrder = "9999";
		hasDOMClass = new ArrayList <ISOClassOAIS11179> ();
		attrParentClass = null;
		hasDOMObject = null;		
		valArr = new ArrayList <String> (); 
		
		localIdentifier = "TBD_localIdentifier";
		parentClassTitle = "TBD_parentClassTitle";
		classNameSpaceIdNC = "TBD_classNameSpaceIdNC";
		classSteward = "TBD_classSteward";
		groupName = "TBD_groupName";
		referenceType = "TBD_referenceType";
		isPDS4 = false;
		isAttribute = false;
		isChoice = false;
		isAny = false;
		isSet = false;
		isRestrictedInSubclass = false;
		
		enclLocalIdentifier = "TBD_enclLocalIdentifier";
		minimumOccurrences = "TBD_minimumOccurrences";
		maximumOccurrences = "TBD_maximumOccurrences";
		
		return;
	}	
	
	public String getCardMin() {
		return cardMin;
	}
	
	public int getCardMinI() {
		return cardMinI;
	}
	
	public void setCardMinMax(String lCardMin, String lCardMax) {
		if (DMDocument.isInteger(lCardMin)) {
			cardMin = lCardMin;
			cardMinI = new Integer(lCardMin);
		} else {
			System.out.println(">>error    - DomProp " + " - Minimum cardinality is invalid: " + lCardMin);
		}
		if ((lCardMax.compareTo("*") == 0) || (lCardMax.compareTo("unbounded") == 0)) {
			cardMax = "*";
			cardMaxI = 9999999;
		} else if (DMDocument.isInteger(lCardMax)) {
			cardMax = lCardMax;
			cardMaxI = new Integer(lCardMax);
		} else {
			System.out.println(">>error    - DomProp " + " - Maximum cardinality is invalid: " + lCardMax);
		}
		if (cardMaxI < cardMinI) {
			System.out.println(">>error    - DomProp " + " - Maximum cardinality is less than minimum cardinality");
		}
	}
	
	public String getCardMax() {
		return cardMax;
	}
	
	public int getCardMaxI() {
		return cardMaxI;
	}
	
	public String getClassOrder() {
		return classOrder;
	}
	
	public void setClassOrder(String classOrder) {
		this.classOrder = classOrder;
	}
	
	public void createDOMPropAttrSingletons (AssocDefn lOldProp, AttrDefn lAttr) {
//		rdfIdentifier = lOldProp.rdfIdentifier; 							
//		identifier = lOldProp.identifier; 
		rdfIdentifier = lOldProp.rdfIdentifier; 							
		identifier = lOldProp.identifier;
		versionId = lAttr.versionId;
		sequenceId = lAttr.uid; 
		title = lAttr.title;
		definition =  lAttr.description;
		registrationStatus = lAttr.registrationStatus; 
//		isDeprecated = lAttr.isDeprecated; 
		regAuthId = lAttr.regAuthId; 
		steward = lAttr.steward; 
		nameSpaceId = lOldProp.attrNameSpaceId;
		nameSpaceIdNC = lOldProp.attrNameSpaceIdNC;
		classOrder = lOldProp.classOrder;
		cardMin = lOldProp.cardMin;
		cardMax = lOldProp.cardMax;
		cardMinI = lOldProp.cardMinI; 
		cardMaxI = lOldProp.cardMaxI;
		
		// others from PDS3
		
		localIdentifier = lOldProp.localIdentifier;

		parentClassTitle = lOldProp.className;
		classNameSpaceIdNC = lOldProp.classNameSpaceIdNC;
		groupName = lOldProp.groupName;						

		referenceType = lOldProp.referenceType;
		isAttribute = lOldProp.isAttribute;
		isChoice = lOldProp.isChoice;
		isAny = lOldProp.isAny;
		isSet = lOldProp.isSet;
//		isRestrictedInSubclass = lAttr.isRestrictedInSubclass;
		
		enclLocalIdentifier = lOldProp.enclLocalIdentifier;
		minimumOccurrences = lOldProp.minimumOccurrences;
		maximumOccurrences = lOldProp.maximumOccurrences;
	}
	
	public void initPropParentClass (AttrDefn lOldAttr, TreeMap <String, DOMClass> lDOMClassIdMap) {		
		PDSObjDefn lOldAttrParentClass = lOldAttr.attrParentClass;
		if (lOldAttrParentClass != null) {
			String lIdentifier = lOldAttrParentClass.identifier;
			if (lIdentifier != null && (lIdentifier.indexOf("TBD") != 0) ) {
				attrParentClass = lDOMClassIdMap.get(lIdentifier);
			} else {
				System.out.println(">>warning  - initAttrParentClass  - Failed to get attrParentClass - lOldAttr.attrParentClass.identifier: " + lIdentifier);				
			}
		} else {
			System.out.println(">>warning  - initAttrParentClass  - Null attrParentClass - lOldAttr.identifier: " + lOldAttr.identifier);							
		}
	}
	
	public void createDOMPropClassSingletons (AssocDefn lOldProp, AttrDefn lAttr, PDSObjDefn lOldClass) {
		rdfIdentifier = lOldProp.rdfIdentifier + "." + lOldClass.title; 							
		identifier = lOldProp.identifier + "." + lOldClass.title; 
		versionId = lAttr.versionId;
		sequenceId = lAttr.uid; 
		title = lAttr.title;
		definition =  lAttr.description;
		registrationStatus = lAttr.registrationStatus; 
//		isDeprecated = lAttr.isDeprecated; 
		regAuthId = lAttr.regAuthId; 
		steward = lAttr.steward; 
		nameSpaceId = lOldProp.attrNameSpaceId;
		nameSpaceIdNC = lOldProp.attrNameSpaceIdNC;
		classOrder = lOldProp.classOrder;
		cardMin = lOldProp.cardMin;
		cardMax = lOldProp.cardMax;
		cardMinI = lOldProp.cardMinI; 
		cardMaxI = lOldProp.cardMaxI;
		
		// others from PDS3
		localIdentifier = lOldProp.localIdentifier;
		parentClassTitle = lOldProp.className;
		classNameSpaceIdNC = lOldProp.classNameSpaceIdNC;
		groupName = lOldProp.groupName;						
		referenceType = lOldProp.referenceType;
		isAttribute = lOldProp.isAttribute;
		isChoice = lOldProp.isChoice;
		isAny = lOldProp.isAny;
		isSet = lOldProp.isSet;
//		isRestrictedInSubclass = lAttr.isRestrictedInSubclass;
		
		enclLocalIdentifier = lOldProp.enclLocalIdentifier;
		minimumOccurrences = lOldProp.minimumOccurrences;
		maximumOccurrences = lOldProp.maximumOccurrences;
	}
	
	public void createDOMPropSingletonsNoAssoc (DOMAttr lAttr) {
		rdfIdentifier = lAttr.rdfIdentifier;
		identifier = lAttr.identifier; 
		versionId = lAttr.versionId;
		sequenceId = lAttr.sequenceId; 
		title = lAttr.title;
		definition =  lAttr.definition;
		registrationStatus = lAttr.registrationStatus; 
		regAuthId = lAttr.regAuthId; 
		steward = lAttr.steward; 
		nameSpaceId = lAttr.nameSpaceId;
		nameSpaceIdNC = lAttr.nameSpaceIdNC;
//		classOrder = "9999";
		cardMin = lAttr.cardMin;
		cardMax = lAttr.cardMax;
		cardMinI = lAttr.cardMinI; 
		cardMaxI = lAttr.cardMaxI;
		
		// others from PDS3
		localIdentifier = lAttr.lddLocalIdentifier;

		parentClassTitle = lAttr.parentClassTitle;
		classNameSpaceIdNC = lAttr.classNameSpaceIdNC;
//		groupName = "TBD_groupName";
//		referenceType = "TBD_referenceType";
		isAttribute = lAttr.isAttribute;
		isChoice = lAttr.isChoice;
		isAny = lAttr.isAny;
//		isRestrictedInSubclass = lAttr.isRestrictedInSubclass;

//		isSet = false;
//		enclLocalIdentifier = "TBD_enclLocalIdentifier";
//		minimumOccurrences = "TBD_minimumOccurrences";
//		maximumOccurrences = "TBD_maximumOccurrences";
	}
	
	public void initDOMPermValProp (DOMPermValDefn lDOMPermValDefn) {
		rdfIdentifier = lDOMPermValDefn.rdfIdentifier; 														
		identifier = lDOMPermValDefn.identifier; 
		versionId = lDOMPermValDefn.versionId;
		sequenceId = lDOMPermValDefn.sequenceId; 

		title = lDOMPermValDefn.title;
		definition =  lDOMPermValDefn.definition;
		
		registrationStatus = lDOMPermValDefn.registrationStatus; 
		isDeprecated = lDOMPermValDefn.isDeprecated; 
		
		regAuthId = lDOMPermValDefn.regAuthId; 
		steward = lDOMPermValDefn.steward; 
		nameSpaceId = lDOMPermValDefn.nameSpaceId;
		nameSpaceIdNC = lDOMPermValDefn.nameSpaceIdNC;
		
//		classOrder = lDOMPermValDefn.classOrder;
//		cardMin = lOldProp.cardMin;
//		cardMax = lOldProp.cardMax;
//		cardMinI = lOldProp.cardMinI; 
//		cardMaxI = lOldProp.cardMaxI;
		
		// others from PDS3

//		localIdentifier = lOldProp.localIdentifier;

//		className = lOldProp.className;
//		classNameSpaceIdNC = lOldProp.classNameSpaceIdNC;
//		groupName = lOldProp.groupName;
//		referenceType = lOldProp.referenceType;
//		isAttribute = lOldProp.isAttribute;
//		isChoice = lOldProp.isChoice;
//		isAny = lOldProp.isAny;
//		isSet = lOldProp.isSet;
	
//		enclLocalIdentifier = lOldProp.enclLocalIdentifier;
//		minimumOccurrences = lOldProp.minimumOccurrences;
//		maximumOccurrences = lOldProp.maximumOccurrences;
			
//		PDSObjDefn parentClass;
//		<PDSObjDefn> childClassArr;
//		AttrDefn childAssoc;
//		ArrayList <String> localIdentifierArr;
	}
}
