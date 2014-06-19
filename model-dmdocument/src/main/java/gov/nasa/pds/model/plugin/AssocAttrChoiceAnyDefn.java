package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.Iterator;

public class AssocAttrChoiceAnyDefn {
	boolean isChoice;						// allows an xs:choice
	boolean isAny;							// allows an xs:any
	AttrDefn lddAttr;						// attribute AttrDefn  (the single attribute)
	AttrDefn lddAssocAttrDefn;						// association AttrDefn (the single attribute for 1..m classes)
	ArrayList <AttrDefn> lddAttrArr;		// attribute array for future use 
	ArrayList <PDSObjDefn> lddClassArr;		// class array for future use
	
	public AssocAttrChoiceAnyDefn () {
		isChoice = false;
		isAny = false;
		lddAttr = null;
		lddAssocAttrDefn = null;
		lddAttrArr = new ArrayList <AttrDefn> ();
		lddClassArr = new ArrayList <PDSObjDefn> ();

	}
	
	// init lddAssocAttrDefn - create an AttrDefn for the associations
	public void createAssoc (AssocDefn lAssoc) {
		lddAssocAttrDefn = new AttrDefn (lAssoc.rdfIdentifier);
		lddAssocAttrDefn.identifier = lAssoc.identifier;
		lddAssocAttrDefn.title = lAssoc.localIdentifier; 
//		lddAssocAttrDefn.className = lAssoc.className;
		lddAssocAttrDefn.isAttribute = false;
		lddAssocAttrDefn.isOwnedAttribute = true;
		lddAssocAttrDefn.cardMax = lAssoc.cardMax;
		lddAssocAttrDefn.cardMaxI = lAssoc.cardMaxI;
		lddAssocAttrDefn.cardMin = lAssoc.cardMin;
		lddAssocAttrDefn.cardMinI = lAssoc.cardMinI;
		for (Iterator <PDSObjDefn> i = lddClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			lddAssocAttrDefn.valArr.add(lClass.title);
		}
	}
	
/*	
		lNewAssoc.title = llddAssocAttrDefn.localIdentifier;
		lClass.ownedAssociation.add(lNewAssoc);
		lClass.ownedAssocId.add(lNewAssoc.identifier);  
		lClass.ownedAssocTitle.add(lNewAssoc.title); 
		lNewAssoc.lddAssocAttrDefnChildClassLocalIdentifier = lLDDAssoc.localIdentifier;
		lNewAssoc.className = lClass.title;
	
*/	
}
