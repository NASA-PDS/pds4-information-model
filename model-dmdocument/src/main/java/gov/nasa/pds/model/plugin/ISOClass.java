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

import java.util.TreeMap;

public class ISOClass extends Object {
	String rdfIdentifier;						// url, namespace, title -- used for object dictionary (hashmap)
	String identifier; 							// no url, namespace, title (Is used as functional equivalent of attr.nsTitle)
	String versionId;							// the version of this class
	String sequenceId;							// an sequenced identifier for uniqueness

	String title;  								// no url, no namespace, title
	String nsTitle;								// namespace + title
	String definition;							// a definition

	String registrationStatus;					// ISO 11179 item registration status
	boolean isDeprecated;						// class is deprecated
	boolean isInactive;							// class is inactive, it is hidden from all processing - ignore
	
	TreeMap <String, String> altNameMap;		// names in alternate natural languages
	TreeMap <String, String> altDefnMap;		// descriptions in alternate natural languages
	TreeMap <String, String> modHistoryMap;		// modification history 

	public ISOClass () {
		rdfIdentifier = "TBD_rdfIdentifier"; 
		identifier = "TBD_identifier"; 
		versionId = "0.1";
//		sequenceId = InfoModel.getNextUId();
		sequenceId = "TBD_sequenceId";

		title = "TBD_title";
		nsTitle = "TBD_nsTitle";
		definition = "TBD_definition"; 

// 7777
//		registrationStatus = "Candidate";
		registrationStatus = "TBD_registrationStatus";
		isDeprecated = false;
		isInactive = false;
		
		altNameMap = new TreeMap <String, String> ();
		altDefnMap = new TreeMap <String, String> ();
		modHistoryMap = new TreeMap <String, String> ();
		
		modHistoryMap.put("151231-Sys", "A candidate class was created by request.");
	}
	
	public String getRDFIdentifier() {
		return rdfIdentifier;
	}
	
	public void setRDFIdentifier (String lTitle) {
		this.title = lTitle;
		this.sequenceId = InfoModel.getNextUId();
		this.rdfIdentifier = DMDocument.rdfPrefix + lTitle + "." + this.sequenceId;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String lNameSpaceIdNC, String lTitle) {
		this.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + lNameSpaceIdNC + "." + lTitle;
	}
	
	public void setNSTitle (String lNameSpaceIdNC, String lTitle) {
		this.nsTitle = DOMInfoModel.getAttrNSTitle(lNameSpaceIdNC, lTitle);	
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getVersionId() {
		return versionId;
	}
	
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public String getSequenceId() {
		return sequenceId;
	}
	
//	public void setSequenceId(String sequenceId) {
//		this.sequenceId = sequenceId;
//	}
	
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public boolean getIsDeprecated() {
		return isDeprecated;
	}
	
	public void setIsDeprecated(boolean isDeprecated) {
		this.isDeprecated = isDeprecated;
	}
	
	public String getaltName(String lParameter) {
		String lValue = altNameMap.get(lParameter);
		if (lValue != null) return lValue;
		return "TBD_altName_" + lParameter;
	}
	
	public void setaltNameMap(TreeMap<String, String> altNameMap) {
		this.altNameMap = altNameMap;
	}
	
	public String getaltDefn(String lParameter) {
		String lValue = altDefnMap.get(lParameter);
		if (lValue != null) return lValue;
		return "TBD_altDefn_" + lParameter;
	}
	
	public void setaltDefnMap(TreeMap<String, String> altDefnMap) {
		this.altDefnMap = altDefnMap;
	}
	
	public TreeMap<String, String> getModHistoryMap() {
		return modHistoryMap;
	}
	
	public String getmodHistory(String lParameter) {
		String lValue = modHistoryMap.get(lParameter);
		if (lValue != null) return lValue;
		return "TBD_modHistory_" + lParameter;
	}
	
	public void setmodHistoryMap(TreeMap<String, String> modHistoryMap) {
		this.modHistoryMap = modHistoryMap;
	}
}
