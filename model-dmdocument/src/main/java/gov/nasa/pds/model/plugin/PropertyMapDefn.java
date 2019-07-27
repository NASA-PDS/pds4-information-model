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

public class PropertyMapDefn {
	String identifier;						// unique identifier of property map
	String title;						    // title for this property map
//	String steward_id;						// steward of this property map - e.g. VICAR or a mission
	String model_object_id;					// unique identifier of the the targeted class, attribute, or value
	String model_object_type;				// type {class, attribute, or value}
	String instance_id;						// unique identifier of a a targeted instance in a label, for example an xpath string
	String external_namespace_id;			// a namespaceid external to this context (me too)
	String description;						// description of this property map

	ArrayList <PropertyMapEntryDefn> propertyMapEntryArr;

	public PropertyMapDefn (String id) {	
		identifier = id; 
		title = "TBD_title";
//		steward_id = "TBD_steward_id";
		model_object_id = "TBD_model_object_id";
		model_object_type = "TBD_model_object_type";
		instance_id = "TBD_instance_id";
		external_namespace_id = "TBD_external_namespace_id";
		description = "TBD_description";

		propertyMapEntryArr = new ArrayList <PropertyMapEntryDefn>();
	} 
}
