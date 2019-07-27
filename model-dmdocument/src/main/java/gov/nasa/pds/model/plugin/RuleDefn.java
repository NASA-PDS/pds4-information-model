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

public class RuleDefn {
	String rdfIdentifier;
	String identifier;
	String type;
	String roleId;
	String xpath;
	String attrTitle;
	String attrNameSpaceNC;
	String classTitle;
	String classNameSpaceNC;
	String classSteward;
	boolean alwaysInclude;		// the rule is to always be included in the schematron file
	boolean isMissionOnly;		// the rule is to be included in an LDDTool generated .sch file at the mission level
	ArrayList <String> letAssignArr;
	ArrayList <String> letAssignPatternArr;
	ArrayList <AssertDefn2> assertArr;
	
	public RuleDefn (String id) {
		rdfIdentifier = "TBD_rdfIdentifier";
		identifier = id; 
		type = "TBD_type";
//		role = "";
		roleId = "TBD_roleId";
		xpath = "TBD_xpath";
		attrTitle = "TBD_attributeName";
		attrNameSpaceNC = "TBD_attNameSpaceNC";
		classTitle = "TBD_classTitle";
		classNameSpaceNC = "TBD_NameSpaceNC";
		classSteward = "TBD_classSteward";
		alwaysInclude = false;
		isMissionOnly = false;
		
		letAssignArr = new ArrayList <String>();
		letAssignPatternArr = new ArrayList <String>();
		assertArr = new ArrayList <AssertDefn2>();
	} 
	
	public void setRDFIdentifier () {
		rdfIdentifier = DMDocument.rdfPrefix + "." + identifier + "." + InfoModel.getNextUId();
	}
}
