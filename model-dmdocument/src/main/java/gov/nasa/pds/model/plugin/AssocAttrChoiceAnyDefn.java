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
