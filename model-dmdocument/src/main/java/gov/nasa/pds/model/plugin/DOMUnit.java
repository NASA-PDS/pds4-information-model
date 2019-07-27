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

public class DOMUnit extends ISOClassOAIS11179 {
	String pds4Identifier;
	String type;
	String precision;
	String default_unit_id;
	
	ArrayList <String> unit_id; 
	
	public DOMUnit () {          
		pds4Identifier = "TBD_pds4Identifier";
		type = "TBD_type";              
		precision = "TBD precision";                        
		default_unit_id = "TBD_default_unit_id";                                         

		unit_id = new ArrayList <String>();
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getPrecision() {
		return precision;
	}
	
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getDefault_unit_id() {
		return default_unit_id;
	}
	
	public void setDefault_unit_id(String default_unit_id) {
		this.default_unit_id = default_unit_id;
	}

	public ArrayList<String> getUnit_id() {
		return unit_id;
	}
	
	public void setUnit_id(ArrayList<String> unit_id) {
		this.unit_id = unit_id;
	}
	
	public void createDOMUnitSingletons (String lId, DOMClass lClass) {
//		rdfIdentifier = "TBD"; 
		identifier = lId;
		versionId = "1.0.0.0";
		pds4Identifier = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClass.title);
		title = this.identifier;
		type = this.identifier;
		nameSpaceIdNC = lClass.nameSpaceIdNC;
		nameSpaceId = lClass.nameSpaceId;
		steward = lClass.steward;
		regAuthId = DMDocument.registrationAuthorityIdentifierValue; 
		definition = lClass.definition;
		registrationStatus = lClass.registrationStatus; 
		isDeprecated = lClass.isDeprecated;
//		precision = lUnitDefn.precision;
//		default_unit_id = lUnitDefn.default_unit_id;
		return;
	}
}
