package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class AssocDefn {
	String rdfIdentifier;					// url, namespace, name
//	String uid;								// unique identifier for rdfIdentifier
	String identifier;
	String localIdentifier;					// local_identifier (the or first local identifier in the Association set)
	                                        // needed only for display
	String title;
	String className;
	String attrNameSpaceId;
	String attrNameSpaceIdNC;
	String classNameSpaceIdNC;
	
	String classOrder;						// the order of the attribute or association within a class
	String groupName;						// the choice group name
	String cardMin;
	String cardMax;
	int cardMinI; 
	int cardMaxI;	
	String referenceType;

	boolean isAttribute;					// true->attribute; false->association
	boolean isChoice;						// allows an xs:choice
	boolean isAny;							// allows an xs:any
	boolean isSet;							// is a set of either attributes or associations (AttrDefn)
	
	PDSObjDefn parentClass;					// parent class // **** deprecate
	ArrayList <AttrDefn> childAttrArr;		// all child attributes for this association (if necessary for choice)
//	ArrayList <AttrDefn> childAttrArr2;		// for choice grouping in prep for DOMProp
	ArrayList <PDSObjDefn> childClassArr;	// all child classes for this association (if necessary for choice)
	AttrDefn childAssoc;					// the one association (AttrDefn) equivalent to this association (AssocDefn)
	ArrayList <String> localIdentifierArr;	// local identifier array
	
	// LDD Attributes
	String enclLocalIdentifier;				// local_identifier of enclosing class
	String minimumOccurrences;
	String maximumOccurrences;
	
	public AssocDefn () { 
		rdfIdentifier = "TBD_rdfIdentifier"; 
//		uid = "TBD_uid";
		identifier = "TBD_identifier";
		localIdentifier = "TBD_localIdentifier";			// local_identifier from Association
		title = "TBD_title";
		className = "TBD_className";
		
		classNameSpaceIdNC = "TBD_classNameSpaceId";
		attrNameSpaceId = "TBD_attrNameSpaceId";
		attrNameSpaceIdNC = "TBD_attrNameSpaceIdNC";
		
		classOrder = "9999";
		groupName = "TBD_groupName";	
		cardMin = "TBD_cardMin";
		cardMax = "TBD_cardMax";
		cardMinI = 0; 
		cardMaxI= 0;
		referenceType = "TBD_referenceType";

		isAttribute = false;
		isChoice = false;
		isAny = false;
		isSet = false;

		childAttrArr = new ArrayList <AttrDefn> ();				// all child attributes for this association (choice)
//		childAttrArr2 = new ArrayList <AttrDefn> ();				// all child attributes for this association (choice)
		childClassArr = new ArrayList <PDSObjDefn> ();			// all child classes for this association (choice)
		childAssoc = null;										// the one association (AttrDefn) equivalent to this association (AssocDefn)
		localIdentifierArr = new ArrayList <String> ();
		
		enclLocalIdentifier = "TBD_enclLocalIdentifier";		// local_identifier of enclosing class
		maximumOccurrences = "TBD_maximumOccurrences";
		minimumOccurrences = "TBD_minimumOccurrences";
	} 
	
	public void createAssocSingletons (AssocDefn lOldProp) {
//		System.out.println("debug - createDOMPropSingletons - Phase 4 - lOldProp.rdfIdentifier: " + lOldProp.rdfIdentifier);							
		rdfIdentifier = lOldProp.rdfIdentifier; 							
//		System.out.println("debug - createDOMClassSingletons rdfIdentifier: " + rdfIdentifier);							
		identifier = lOldProp.identifier; 
		title = lOldProp.title;
//		isDeprecated = lAttr.isDeprecated; 
		classOrder = lOldProp.classOrder;
		cardMin = lOldProp.cardMin;
		cardMax = lOldProp.cardMax;
		cardMinI = lOldProp.cardMinI; 
		cardMaxI = lOldProp.cardMaxI;
		className = lOldProp.className;
		classNameSpaceIdNC = lOldProp.classNameSpaceIdNC;
		groupName = lOldProp.groupName;
		referenceType = lOldProp.referenceType;
		isAttribute = lOldProp.isAttribute;
		isChoice = lOldProp.isChoice;
		isAny = lOldProp.isAny;
		isSet = lOldProp.isSet;
	}
	
	public void createAssocSingletonsFromAttr (AttrDefn lOldAttr) {
//		System.out.println("debug - createAssocSingletonsFromAttr - Phase 4 - lOldAttr.rdfIdentifier: " + lOldAttr.rdfIdentifier);							
		rdfIdentifier = lOldAttr.rdfIdentifier; 							
//		System.out.println("debug - createDOMClassSincreateAssocSingletonsFromAttrgletons rdfIdentifier: " + rdfIdentifier);							
		identifier = lOldAttr.identifier; 
		title = lOldAttr.title;
//		isDeprecated = lAttr.isDeprecated; 
		cardMin = lOldAttr.cardMin;
		cardMax = lOldAttr.cardMax;
		cardMinI = lOldAttr.cardMinI; 
		cardMaxI = lOldAttr.cardMaxI;
		attrNameSpaceId = lOldAttr.attrNameSpaceId;
		attrNameSpaceIdNC = lOldAttr.attrNameSpaceIdNC;
		className = lOldAttr.parentClassTitle;
		classNameSpaceIdNC = lOldAttr.classNameSpaceIdNC;
		groupName = lOldAttr.groupName;
//		referenceType = lOldAttr.referenceType; //lAssoc.referenceType = "attribute_of";
		isAttribute = lOldAttr.isAttribute;
		isChoice = lOldAttr.isChoice;
		isAny = lOldAttr.isAny;
	}
}
