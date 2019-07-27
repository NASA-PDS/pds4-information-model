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

public class DataTypeDefn {
	String identifier;
	String pds4Identifier;
	String title;
	String nameSpaceIdNC;
	String type;
	String character_constraint;
	String formation_rule;
	String maximum_characters;
	String maximum_value;
	String minimum_characters;
	String minimum_value;
	String xml_schema_base_type;
	String character_encoding; 	 	
	
	ArrayList <String> pattern; 
	
	public DataTypeDefn (String id) {
		identifier = id;
		pds4Identifier = "TBD_pds4Identifier";
		title = "TBD_title";
		nameSpaceIdNC = "TBD_nameSpaceIdNC";
		type = "TBD_type";
		character_constraint   = "TBD_character_constraint";       
		formation_rule	       = "TBD_formation_rule"; 
		maximum_characters	   = "TBD_maximum_characters"; 
		maximum_value	       = "TBD_maximum_value"; 
		minimum_characters     = "TBD_minimum_characters";  
		minimum_value	       = "TBD_minimum_value"; 
		xml_schema_base_type   = "TBD_xml_schema_base_type"; 
		character_encoding     = "TBD_character_encoding";                                        

		pattern = new ArrayList <String>();
	}
	
	//	get the minimum_characters for printing. Use the data type for a default.
	public String getMinimumCharacters2 (boolean forceBound) {
		String lValue = this.minimum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_characters";
		return lValue;
	}
	
	//	get the maximum_characters for printing. Use the data type for a default.
	public String getMaximumCharacters2 (boolean forceBound) {
		String lValue = this.maximum_characters;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_characters";
		return lValue;
	}
	
	//	get the minimum_value for printing. Use the data type for a default.
	public String getMinimumValue2 (boolean forceBound) {
		String lValue = this.minimum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("-2147483648") == 0 || lValue.compareTo("-INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_minimum_value";
		return lValue;
	}
	
	//	get the maximum_value for printing. Use the data type for a default.
	public String getMaximumValue2 (boolean forceBound) {
		String lValue = this.maximum_value;
		if (forceBound) {
			if (lValue.indexOf("TBD") == 0 || lValue.compareTo("") == 0 || lValue.compareTo("2147483647") == 0 || lValue.compareTo("4294967295") == 0 || lValue.compareTo("INF") == 0) {
				return "Unbounded";
			}
		}
		if (lValue.compareTo("") == 0) return "TBD_maximum_value";
		return lValue;
	}
}
