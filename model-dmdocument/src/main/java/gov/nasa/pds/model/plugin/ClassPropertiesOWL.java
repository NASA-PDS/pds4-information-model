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

// class properties
class ClassPropertiesOWL extends ClassPropertiesBase{

	DOMClass subClassOf;
	String subClassOfTitle;
	String subClassOfNameSpaceIdNC;
	String subClassOfID;
	String subClassOfLID;
	String subClassOfLIDVID;
	String subClassOfIRI;
	String subClassOf2Title;
	String subClassOf2NameSpaceIdNC;
							
//	String sequenceId;							
//	DOMClass theClass;
	ClassPropClass theClassSubClassOf = null;
	ArrayList <ClassPropClass> theClassPropClassArr = new ArrayList <ClassPropClass> ();
	ArrayList <ClassPropAttr> theClassPropAttrArr = new ArrayList <ClassPropAttr> ();
	ArrayList <ClassPropertiesOWL> theComponentOfClassArr = new ArrayList <ClassPropertiesOWL> ();
	
	// Constructor - for regular classes
	public ClassPropertiesOWL (DOMClass lClass) {
		
		// set the class attributes including the subclass; specifically subClassOfLIDVID
		identifier = lClass.identifier + "_" + DOMInfoModel.getNextUId();
		versionId = lClass.versionId;
		theClass = lClass;
		theClass.identifier = lClass.identifier;
		rdfIdentifier = lClass.rdfIdentifier;
		nameSpaceIdNC = lClass.nameSpaceIdNC;
		title = formValue(lClass.title);  													
		type = "TBD_type";
		subClassOfTitle = "TBD_subClassOfTitle";
		subClassOfNameSpaceIdNC = "TBD_subClassOfNameSpaceIdNC";
		subClassOfID = "TBD_subClassOfID";
		subClassOfLID = "TBD_subClassOfLID";
		subClassOfLIDVID = "TBD_subClassOfLIDVID";
		subClassOfIRI = "TBD_subClassOfIRI";
		subClassOf2Title = "TBD_subClassOf2Title";	// the title of the subClassOf of the subClassOf
		subClassOf2NameSpaceIdNC = "TBD_subClassO2fNameSpaceIdNC";
		definition = formValue(lClass.definition);
		
		// get the subClassOf
		subClassOf = DOMInfoModel.masterDOMUserClass; // Default
		if (lClass.subClassOf != null) subClassOf = lClass.subClassOf;
		subClassOfTitle = formValue(subClassOf.title);
		subClassOfNameSpaceIdNC = subClassOf.nameSpaceIdNC;
		subClassOfID = subClassOf.identifier;
		
		// get the subClassOf of the subClassOf; needed for subclass LID
		DOMClass lSubClassOf2 = DOMInfoModel.masterDOMUserClass; // Default
		if (subClassOf.subClassOf != null) {
			lSubClassOf2 = subClassOf.subClassOf;
		}
		subClassOf2Title = formValue(lSubClassOf2.title);
		subClassOf2NameSpaceIdNC = lSubClassOf2.nameSpaceIdNC;
		
		// get the subClassOf LID and LIDVID
		String lSubClassOfLID = lidPrefix + prodIdPrefix + subClassOf2NameSpaceIdNC + ":" + subClassOf2Title + ":" + subClassOfNameSpaceIdNC + ":" + subClassOfTitle;
		if (subClassOf2Title.compareTo(subClassOfTitle) == 0) {   // check for user:user:
			lSubClassOfLID = lidPrefix + prodIdPrefix + subClassOfNameSpaceIdNC + ":" + subClassOfTitle;
		} 
		subClassOfLID = formValueLID(lSubClassOfLID);
		subClassOfLIDVID = formValueLID(lSubClassOfLID + "::" + vid);
		subClassOfIRI = formValueIRI(lSubClassOfLID);

		// get the class LID and LIDVID
		lid = formValueLID(lidPrefix + prodIdPrefix + subClassOfNameSpaceIdNC + ":" + subClassOfTitle + ":" + nameSpaceIdNC + ":"+ lClass.title);
		lidvid = formValueLID(lid + "::" + vid);
		iri = formValueIRI(lid);
		return;
	}

	// Constructor - for permissible values
	public ClassPropertiesOWL (DOMPermValDefn lDOMPermVal, ClassPropertiesOWL lClassProperties) {
		identifier = lClassProperties.theClass.identifier + "." + lDOMPermVal.value + "_" + DOMInfoModel.getNextUId();
		nameSpaceIdNC = lClassProperties.nameSpaceIdNC;
		type = "TBD_type";
		subClassOfTitle = formValue(lClassProperties.title);
		subClassOfID = lClassProperties.theClass.identifier;
		subClassOfLIDVID = lClassProperties.lidvid;
		title = formValue(lDOMPermVal.value);  													
		definition = formValue(lDOMPermVal.value_meaning);
		String lSubClassName = formValue(lDOMPermVal.value.toLowerCase());
		lSubClassName = DOMInfoModel.replaceString (lSubClassName, " ", "_");
		lid = formValueLID(lClassProperties.lid + lSubClassName);
		lidvid = formValueLID(lid + "::" + vid);
		iri = formValueIRI(lid);
		return;
	}
	
	// get generalization relationship
	public void getSubClassOf (DOMClass lDOMClass) {
		ClassPropClass lClassPropClass;
		if (lDOMClass.subClassOf != null) {
			DOMClass lSuperClass = lDOMClass.subClassOf;
			lClassPropClass = new ClassPropClass (lDOMClass, null, lSuperClass);
		} else {
			lClassPropClass = new ClassPropClass (lDOMClass, null, null);
		}
		theClassSubClassOf = lClassPropClass;
		return;
	}	
	
	// get the class's associated attributes; initialize the Class Property Attribute Array - theClassPropAttrArr
	public void getAssociatedAttributes (DOMClass lDOMClass) {
		// change to allow both owned and inherited associated components to be included
		ArrayList <DOMProp> allAttrArr = new ArrayList <DOMProp> ();
		if (lDOMClass.ownedAttrArr != null && lDOMClass.ownedAttrArr.size() > 0) allAttrArr.addAll(lDOMClass.ownedAttrArr);
		if (lDOMClass.inheritedAttrArr != null && lDOMClass.inheritedAttrArr.size() > 0) allAttrArr.addAll(lDOMClass.inheritedAttrArr);
		if (allAttrArr != null && allAttrArr.size() > 0) {
			for (Iterator<DOMProp> j = allAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
					ClassPropAttr lClassPropClass = new ClassPropAttr (lDOMClass, lDOMProp, lDOMAttr);
					theClassPropAttrArr.add(lClassPropClass);
				}
			}
		}
		return;
	}
	
	// get the class's associated component classes; initialize the Class Property Class Array - theClassPropClassArr
	public void getAssociatedClasses (DOMClass lDOMClass) {
		// change to allow both owned and inherited associated components to be included
		ArrayList <DOMProp> allAssocArr = new ArrayList <DOMProp> ();
		if (lDOMClass.ownedAssocArr != null && lDOMClass.ownedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.ownedAssocArr);
		if (lDOMClass.inheritedAssocArr != null && lDOMClass.inheritedAssocArr.size() > 0) allAssocArr.addAll(lDOMClass.inheritedAssocArr);
		if (allAssocArr != null && allAssocArr.size() > 0) {
			for (Iterator<DOMProp> j = allAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMClass) {
					DOMClass lMethodClass = (DOMClass) lDOMProp.domObject;
					ClassPropClass lClassPropClass = new ClassPropClass (lDOMClass, lDOMProp, lMethodClass);
					theClassPropClassArr.add(lClassPropClass);
				}
			}
		}
		return;
	}
	
	// get the ClassPropClassArr
	public ArrayList <ClassPropClass> getTheClassPropClassArr () {
		return theClassPropClassArr;
	}
	
	// get the ClassPropAttrArr
	public ArrayList <ClassPropAttr> getTheClassPropAttrArr () {
		return theClassPropAttrArr;
	}
	
	// get the ClassPropAttrArr
	public ArrayList <ClassPropertiesOWL> getTheComponentOfClassArr () {
		return theComponentOfClassArr;
	}
}