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
public class DOMPermValDefn extends ISOClassOAIS11179 {
//	String identifier;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String searchKey;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String registrationStatus;	// ISO 11179 item registration status
	String value;			// the value
	String value_meaning;	// the meaning of the value
	String value_begin_date;	// the value begin date
	String value_end_date;		// the value end date
	boolean isPattern;
	
	public DOMPermValDefn () {
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = "TBD_registrationStatus";
		value_meaning = "TBD_value_meaning";
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
		isPattern = false; // pattern special characters (e.g. back slashes) are invalid for OWL RDF identifiers.
	}
	
	public DOMPermValDefn (String lId, String lValue, String lValueMeaning) {
		identifier = lId; 
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = lValue;
		value_meaning = lValueMeaning;
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
	}
	
	public void createDOMPermValSingletons (PermValueDefn lPermValue, AttrDefn lOldAttr) {
		sequenceId = InfoModel.getNextUId();
//		rdfIdentifier = lOldAttr.rdfIdentifier; 
		String lValueId = lPermValue.value;
		if (isPattern) lValueId = "Pattern"; // pattern characters (e.g., back slashes) are invalid for OWL RDF identifiers.
		int lValueLength = lValueId.length();
		if (lValueLength > 20) lValueId = lValueId.substring(0, 20);
		rdfIdentifier = DMDocument.rdfPrefix + lOldAttr.parentClassTitle + "." + lOldAttr.title + "." + lValueId + "." + sequenceId; 
		int lHashCodeI = lPermValue.value.hashCode();
		String lHashCodeS = Integer.toString(lHashCodeI);
		identifier = lOldAttr.identifier + "." + lValueId + "." + lHashCodeS;
		versionId = lOldAttr.versionId; 
		sequenceId = lOldAttr.uid;
		
//		title = lOldAttr.title; 
//		definition = lOldAttr.description;
		
		registrationStatus = lPermValue.registrationStatus; 
//		isDeprecated = lOldProp.isDeprecated; 
		
		regAuthId = lOldAttr.regAuthId; 
		steward = lOldAttr.steward; 
		nameSpaceId = lOldAttr.attrNameSpaceId;
		nameSpaceIdNC = lOldAttr.attrNameSpaceIdNC;
		
		searchKey = lPermValue.searchKey;
		value = lPermValue.value;
		value_meaning = lPermValue.value_meaning;
		value_begin_date = lPermValue.value_begin_date;
		value_end_date = lPermValue.value_end_date;
		return;
	}
	
	public void createDOMPermValSingletonsDOM (String lValue, String lValueMeaning, DOMAttr lAttr) {
		sequenceId = InfoModel.getNextUId();
		String lValueId = lValue;
		if (lAttr.title.compareTo("pattern") == 0) {
			isPattern = true;
			lValueId = "Pattern"; // pattern characters (e.g., back slashes) are invalid for OWL RDF identifiers.
		}
		int lValueLength = lValueId.length();
		if (lValueLength > 20) lValueId = lValueId.substring(0, 20);
		rdfIdentifier = DMDocument.rdfPrefix + lAttr.parentClassTitle + "." + lAttr.title + "." + lValueId + "." + sequenceId; 
		int lHashCodeI = lValue.hashCode();
		String lHashCodeS = Integer.toString(lHashCodeI);
		identifier = lAttr.identifier + "." + lValueId + "." + lHashCodeS;
		versionId = lAttr.versionId; 
		title = identifier;
		registrationStatus = "TBD_registrationStatus"; 
		regAuthId = lAttr.regAuthId; 
		steward = lAttr.steward; 
		nameSpaceId = lAttr.nameSpaceId;
		nameSpaceIdNC = lAttr.nameSpaceIdNC;
		value = lValue;
		value_meaning = lValueMeaning;
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
		return;
	}
}
