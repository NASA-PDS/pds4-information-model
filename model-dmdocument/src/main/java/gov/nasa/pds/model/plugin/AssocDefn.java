package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class AssocDefn {
	String rdfIdentifier;					// url, namespace, name
	String uid;								// unique identifier for rdfIdentifier
	String identifier;
	String localIdentifier;					// local_identifier (the or first local identifiers in the Association)
	                                        // needed only for display
	String classOrder;						// the order of the attribute or association within a class
	String cardMin;
	String cardMax;
	int cardMinI; 
	int cardMaxI;	
	String referenceType;

	boolean isAttribute;					// true->attribute; false->association
	boolean isChoice;						// allows an xs:choice
	boolean isAny;							// allows an xs:any
	
	PDSObjDefn parentClass;					// parent class
	ArrayList <AttrDefn> childAttrArr;		// all child attributes for this association (if necessary for choice)
	ArrayList <PDSObjDefn> childClassArr;	// all child classes for this association (if necessary for choice)
	AttrDefn childAssoc;					// the one association (AttrDefn) equivalent to this association (AssocDefn)
	ArrayList <String> localIdentifierArr;	// local identifier array
	ArrayList <AttrDefn> tempChildAssocArr;
	
	// LDD Attributes
	boolean isExternal;					// the associated attribute or class is external to the LDD
//	String componentLocalIdentifier;	// the external or LDD, attribute or association local identifier
	String enclLocalIdentifier;			// local_identifier of enclosing class
	String minimumOccurrences;
	String maximumOccurrences;
	
//	public AssocDefn (String rdfId) {
	public AssocDefn () {
//		rdfIdentifier = rdfId; 
		rdfIdentifier = "TBD_rdfIdentifier"; 
		uid = "TBD_uid";
		identifier = "TBD_identifier";
		localIdentifier = "TBD_localIdentifier";			// local_identifier from Association
		classOrder = "9999";
		cardMin = "TBD_cardMin";
		cardMax = "TBD_cardMax";
		cardMinI = 0; 
		cardMaxI= 0;
		referenceType = "TBD_referenceType";

		isAttribute = false;
		isChoice = false;
		isAny = false;

		parentClass = null;
		childAttrArr = new ArrayList <AttrDefn> ();				// all child attributes for this association (choice)
		childClassArr = new ArrayList <PDSObjDefn> ();			// all child classes for this association (choice)
		childAssoc = null;										// the one association (AttrDefn) equivalent to this association (AssocDefn)
		localIdentifierArr = new ArrayList <String> ();
		tempChildAssocArr = new ArrayList <AttrDefn> ();			// temporarily for one reference in MasterInfoModel
		
		isExternal = false;
//		componentLocalIdentifier = "TBD_componentLocalIdentifier"; 
		enclLocalIdentifier = "TBD_enclLocalIdentifier";	// local_identifier of enclosing class
		maximumOccurrences = "TBD_maximumOccurrences";
		minimumOccurrences = "TBD_minimumOccurrences";
	} 
}
