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

public class DOMProp extends ISOClassOAIS11179 {
	
	String cardMin;
	String cardMax;
	int cardMinI;
	int cardMaxI;
	String classOrder;						// the order of the attribute or association within a class
	String sortKeyIMSPec;					// sort key for IM Specification Dictionary -  Title/Steward/Class/Steward
	
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
		sortKeyIMSPec = "TBD_sortKeyIMSPec";
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
	
	public void setIdentifier(String lNameSpaceIdNC, String lTitle, String lNameSpaceIdNC2, String lTitle2) {
		this.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lNameSpaceIdNC + "." + lTitle + "." + lNameSpaceIdNC2 + "." + lTitle2;
		this.nsTitle = lNameSpaceIdNC2 + "." + lTitle2;
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
	
	public void setSortKey () {
		String lMemberTitle = "TBD_lMemberTitle";
		String lMemberSteward = "TBD_lMemberSteward";
		if (hasDOMObject != null) {
			if (hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) hasDOMObject;
				lMemberTitle = lDOMAttr.title;
				lMemberSteward = lDOMAttr.steward;
			} else if (hasDOMObject instanceof DOMClass) {
				lMemberTitle = title;
				lMemberSteward = steward;
			} else {
				return;
			}
		}
		String lBlanks = "                              ";
		int lLength = lMemberTitle.length();
		if (lLength >= 30) lLength = 30;
		String lPaddedMemberTitle = lMemberTitle + lBlanks.substring(0, 30 - lLength);
		lLength = attrParentClass.title.length();
		if (lLength >= 30) lLength = 30;
		String lPaddedClassTitle = attrParentClass.title + lBlanks.substring(0, 30 - lLength);
		sortKeyIMSPec = lPaddedMemberTitle + "_" + lMemberSteward + "_" + lPaddedClassTitle + "_" + attrParentClass.steward;
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
