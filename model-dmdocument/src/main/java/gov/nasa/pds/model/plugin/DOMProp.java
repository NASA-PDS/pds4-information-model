package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class DOMProp extends ISOClassOAIS11179 {
	
	String cardMin;
	String cardMax;
	int cardMinI;
	int cardMaxI;
	String classOrder;						// the order of the attribute or association within a class
	ArrayList <ISOClassOAIS11179> hasDOMClass;		// allows both PDS4 classes, attributes, etc
	
	String localIdentifier;					// local_identifier (the or first local identifier in the Association set)
    													// needed only for display
	String className;
	String classNameSpaceIdNC;
	String groupName;							// the choice group name
	String referenceType;
	Boolean isAttribute;						// true->attribute; false->association
	Boolean isChoice;							// allows an xs:choice
	Boolean isAny;								// allows an xs:any
	Boolean isSet;								// is a set of either attributes or associations (AttrDefn)

	// LDD Attributes
	String enclLocalIdentifier;				// local_identifier of enclosing class
	String minimumOccurrences;
	String maximumOccurrences;
	
//  PDSObjDefn parentClass;					// parent class // **** deprecate
//  ArrayList <PDSObjDefn> childClassArr;	// all child classes for this association (if necessary for choice)
//  AttrDefn childAssoc;					  // the one association (AttrDefn) equivalent to this association (AssocDefn)
//  ArrayList <String> localIdentifierArr;	// local identifier array

	public DOMProp () {
		cardMin = "0"; 
		cardMax = "0";
		cardMinI = 0; 
		cardMaxI = 0;
		classOrder = "9999";
		hasDOMClass = new ArrayList <ISOClassOAIS11179> ();
		
		localIdentifier = "TBD_localIdentifier";
		className = "TBD_className";
		classNameSpaceIdNC = "TBD_classNameSpaceIdNC";
		groupName = "TBD_groupName";
		referenceType = "TBD_referenceType";
		isAttribute = false;
		isChoice = false;
		isAny = false;
		isSet = false;

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
	
	public void createDOMPropSingletons (AssocDefn lOldProp, AttrDefn lAttr) {
//		System.out.println("debug - createDOMPropSingletons - Phase 4 - lOldProp.rdfIdentifier: " + lOldProp.rdfIdentifier);							
		rdfIdentifier = lOldProp.rdfIdentifier; 							
//		System.out.println("debug - createDOMClassSingletons rdfIdentifier: " + rdfIdentifier);							
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

		className = lOldProp.className;
		classNameSpaceIdNC = lOldProp.classNameSpaceIdNC;
		groupName = lOldProp.groupName;
		referenceType = lOldProp.referenceType;
		isAttribute = lOldProp.isAttribute;
		isChoice = lOldProp.isChoice;
		isAny = lOldProp.isAny;
		isSet = lOldProp.isSet;
		
		enclLocalIdentifier = lOldProp.enclLocalIdentifier;
		minimumOccurrences = lOldProp.minimumOccurrences;
		maximumOccurrences = lOldProp.maximumOccurrences;
			
//		PDSObjDefn parentClass;
//		<PDSObjDefn> childClassArr;
//		AttrDefn childAssoc;
//		ArrayList <String> localIdentifierArr;
		
		public void createDOMPropSingletonsNoAssoc (AttrDefn lAttr) {
			rdfIdentifier = lAttr.rdfIdentifier; 							
//			System.out.println("debug - createDOMClassSingletons rdfIdentifier: " + rdfIdentifier);							
			identifier = lAttr.identifier; 
			versionId = lAttr.versionId;
			sequenceId = lAttr.uid; 

			title = lAttr.title;
			definition =  lAttr.description;
			registrationStatus = lAttr.registrationStatus; 
			regAuthId = lAttr.regAuthId; 
			steward = lAttr.steward; 
			nameSpaceId = lAttr.attrNameSpaceId;
			nameSpaceIdNC = lAttr.attrNameSpaceIdNC;
			
//			classOrder = "9999";
			cardMin = lAttr.cardMin;
			cardMax = lAttr.cardMax;
			cardMinI = lAttr.cardMinI; 
			cardMaxI = lAttr.cardMaxI;
			
			// others from PDS3

			localIdentifier = lAttr.lddLocalIdentifier;

			className = lAttr.parentClassTitle;
			classNameSpaceIdNC = lAttr.classNameSpaceIdNC;
//			groupName = "TBD_groupName";
//			referenceType = "TBD_referenceType";
			isAttribute = lAttr.isAttribute;
			isChoice = lAttr.isChoice;
			isAny = lAttr.isAny;
//			isSet = false;
			
//			enclLocalIdentifier = "TBD_enclLocalIdentifier";
//			minimumOccurrences = "TBD_minimumOccurrences";
//			maximumOccurrences = "TBD_maximumOccurrences";
		}
	}
	
	public void initDOMPermValProp (DOMPermValDefn lDOMPermValDefn) {
//		System.out.println("debug - initDOMPermValProp lDOMPermValDefn.rdfIdentifier: " + lDOMPermValDefn.rdfIdentifier);							
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
