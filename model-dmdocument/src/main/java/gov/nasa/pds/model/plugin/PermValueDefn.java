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
public class PermValueDefn {
	String identifier;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String searchKey;		// identifier from raw value meaning files - e.g. 0001_NASA_PDS_1.Table_Delimited.record_delimiter_Value_1
	String registrationStatus;	// ISO 11179 item registration status
	String value;			// the value
	String value_meaning;	// the meaning of the value
	String value_begin_date;	// the value begin date
	String value_end_date;		// the value end date
	boolean isDeprecated;		// class is deprecated
	
	public PermValueDefn (String lId, String lValue, String lValueMeaning) {
		identifier = lId; 
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = lValue;
		value_meaning = lValueMeaning;
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
		isDeprecated = false;
	} 
	
	public PermValueDefn (String lId, String lValue) {
		identifier = lId; 
		searchKey = "TBD_searchKey";
		registrationStatus = "TBD_registrationStatus";
		value = lValue;
		value_meaning = "TBD_value_meaning";
		value_begin_date = "TBD_value_begin_date";
		value_end_date = "TBD_value_end_date";
		isDeprecated = false;
	} 	
}
