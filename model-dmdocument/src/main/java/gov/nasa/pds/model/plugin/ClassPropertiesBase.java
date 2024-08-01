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

// class properties
class ClassPropertiesBase extends Object{
	
	static final String lidPrefix = "";
	static final String prodIdPrefix = "0001_NASA_PDS_1:";
	static final String datatypePropertyAttributeOf = "<http://ontology.pds.nasa.gov/pds/0001_nasa_pds_1/attributeof>";
	static final String objectPropertyComponentOf = "<http://ontology.pds.nasa.gov/pds/urn/nasa/pds/0001_nasa_pds_1/componentof>";
	static final String objectPropertyComponentOfP1 = "0001_nasa_pds_1-pds-componentof";
	static final String objectPropertySubclassPrefix = "0001_nasa_pds_1";
	
	String iri;
	String lidvid;
	String lid;
	String vid = "1.x";
	String identifier;		// Class properties identifier not Class identifier; 
	String versionId;
	String rdfIdentifier;
	String nameSpaceIdNC;
	String title;
	String type;
	String definition;
	String subClassOfTitle;	
	DOMClass theClass;
	
	// flag for oaisif (fuseki - no slashs in LIDVID) vs pds4 (graphdb - slashes)
	static boolean isFuseki = false;

	public ClassPropertiesBase () {
		isFuseki = false;
		iri = "TBD_iri";
		identifier = "TBD_identifier";
		versionId = "TBD_versionId";
		rdfIdentifier = "TBD_rdfIdentifier";
		nameSpaceIdNC = "TBD_nameSpaceIdNCr";
		title = "TBD_title";  													
		type = "TBD_type";  													
		definition = "TBD_definition";
		subClassOfTitle = "TBD_subClassOfTitle";
	}
	
	// Format the iri
	static public String formValueIRI(String lString) {
		String stringCleaned = formValue(lString).toLowerCase();
		if (! isFuseki) {
			stringCleaned = DOMInfoModel.replaceString (stringCleaned, ":", "/");
		}		
		stringCleaned = "<http://ontology.pds.nasa.gov/pds/" + stringCleaned + ">";
		return stringCleaned;
	}
	
	// Format the LID and LIDVID
	static public String formValueLID(String lString) {
		String stringLower = formValue(lString).toLowerCase();
		String stringCleaned = DOMInfoModel.replaceString (stringLower, "::", "-v");		
		return stringCleaned;
	}	
	
	// Format the string
	static public String formValue(String lString) {
		String rString = lString;
		if (rString == null) rString = "null";
		if (rString.indexOf("TBD") == 0) rString = "null";
		rString = escapeTTLChar(rString);
		return rString;
	}
	
	static String escapeTTLChar (String aString) {
		if (aString == null) return "TBD_string";
		String lString = aString;
		lString = DOMInfoModel.replaceString (lString, "'", "\\'");  
		lString = DOMInfoModel.replaceString (lString, "\"", "\\\"");  
//		lString = DOMInfoModel.replaceString (lString, "\\", "\\\\");  // escape of backslash must be first
//		lString = DOMInfoModel.replaceString (lString, "\"", "\\\"");
//		lString = DOMInfoModel.replaceString (lString, "/", "\\/");
//		lString = DOMInfoModel.replaceString (lString, "\b", "\\b");
//		lString = DOMInfoModel.replaceString (lString, "\f", "\\f");
//		lString = DOMInfoModel.replaceString (lString, "\n", "\\n");
//		lString = DOMInfoModel.replaceString (lString, "\r", "\\r");
//		lString = DOMInfoModel.replaceString (lString, "\t", "\\t");
		return lString;
	}
}