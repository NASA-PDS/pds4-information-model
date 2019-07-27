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
import java.util.*;

public class ClassDefn extends Object {

	String rdfIdentifier;							// url, namespace, title -- used for object dictionary (hashmap)
	String identifier; 								// no url, namespace, title (Is used as funcational equivalent of attr.nsTitle)
	String title;  									// no url, no namespace, title
	String versionId;								// the version of this class
	String registrationStatus;						// ISO 11179 item registration status
	String anchorString;							// "class_" + lClass.nameSpaceIdNC + "_" + lClass.title
	String regAuthId;								// registration authority identifier
	String steward;									// steward
	String nameSpaceId;								// namespace id - assigned namespace id with colon
	String nameSpaceIdNC;							// namespace id - assigned namespace id No Colon
//	String role;									// abstract or concrete
	String description;
	
	boolean isUsedInModel;
	boolean isAbstract;
	boolean isFromLDD;									// has been ingested from Ingest_LDD
	
	public ClassDefn (String rdfId) {
		identifier = "TBD_identifier"; 
		rdfIdentifier = rdfId; 
		title = "TBD_title"; 
//		versionId = "TBD_versionId";
		versionId = DMDocument.classVersionIdDefault;
		registrationStatus = "TBD_registrationStatus";
		anchorString = "TBD_anchorString";
		regAuthId = "TBD_registration_authority_identifier";
		steward = "TBD_steward";
		nameSpaceId = "TBD_namespaceid";
		nameSpaceIdNC = "TBD_namespaceidNC";
//		role = "TBD_role";
		description = "TBD_description"; 
		isUsedInModel = false;
		isAbstract = false;
		isFromLDD = false;
	}
} 	
