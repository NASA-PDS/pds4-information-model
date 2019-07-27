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

public class SectionDefn extends Object {

	String identifier; 
	String title; 
	String description;
	String type;
	String secType;
	String secSubType;
	boolean secTOCFlag;
	boolean subSecTOCFlag;
	int texFormatInd;
	boolean includeFlag, imageFlag, TOCFlag;
	String imageFileName;
	String imageCaption;
	String selectConstraint;
	ArrayList <String> sectionModelContentId;


	public SectionDefn (String id) {
		identifier = id; 
		title = id; 
		description = "TBD_description"; 
		type = "TBD_type"; 
		secType = "text";
		secSubType = "content";
		secTOCFlag = false;
		subSecTOCFlag = false;
		texFormatInd = 0;
		includeFlag = false;
		imageFlag = false;
		imageFileName = "none";
		imageCaption = "none";
		selectConstraint = "none";
		sectionModelContentId = new ArrayList <String> ();		
		
	}  	
} 	
