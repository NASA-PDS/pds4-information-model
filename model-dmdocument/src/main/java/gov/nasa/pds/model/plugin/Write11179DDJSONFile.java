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
import java.io.*;
import java.util.*;

/**
 * Writes the PDS4 DD content to a JSON file 
 *   
 */

class Write11179DDJSONFile extends Object{
	
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
	PrintWriter prDDPins;

	public Write11179DDJSONFile () {
		return;
	}

	// write the JSON file
	public void writeJSONFile (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecModelJSON;
	    prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		printPDDPHdr(lSchemaFileDefn);
		printPDDPBody (lSchemaFileDefn);
		printPDDPFtr(lSchemaFileDefn);
		prDDPins.close();
	}	
	
	// Print the JSON Header
	public void printPDDPHdr (SchemaFileDefn lSchemaFileDefn) {
		prDDPins.println("[");
		prDDPins.println("  {");
		prDDPins.println("    " + formValue("dataDictionary") + ": {");
		prDDPins.println("      " + formValue("Title") + ": " + formValue("PDS4 Data Dictionary") + " ,");
		prDDPins.println("      " + formValue("Version") + ": " +  formValue(DMDocument.masterPDSSchemaFileDefn.ont_version_id) + " ,");
		prDDPins.println("      " + formValue("Date") + ": " +  formValue(DMDocument.sTodaysDate) + " ,");
		prDDPins.println("      " + formValue("Description") + ": " + formValue("This document is a dump of the contents of the PDS4 Data Dictionary") + " ,");
	}
	
	// Format the Boolean String for JSON
	public String formBooleanValue(boolean lBoolean) {
		String rString = "" + lBoolean;
		return formValue(rString);
	}

	// Format the String for JSON
	public String formValue(String lString) {
		String rString = lString;
		if (rString == null) rString = "null";
		if (rString.indexOf("TBD") == 0) rString = "null";
		rString = InfoModel.escapeJSONChar(rString);
		rString = "\"" + rString + "\"";
		return rString;
	}

	// Print the JSON Footer
	public  void printPDDPFtr (SchemaFileDefn lSchemaFileDefn) {
		prDDPins.println("    }");
		prDDPins.println("  }");
		prDDPins.println("]");
	}
	
//	print the JSON body
	public  void printPDDPBody (SchemaFileDefn lSchemaFileDefn) {
		prDDPins.println("      " + formValue("classDictionary") + ": [");
		printClass (lSchemaFileDefn, prDDPins);
		prDDPins.println("      ]");

		prDDPins.println("    , " + formValue("attributeDictionary") + ": [");
		printAttr (lSchemaFileDefn, prDDPins);
		prDDPins.println("      ]");
		
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0) {
			prDDPins.println("    , " + formValue("dataTypeDictionary") + ": [");
			printDataType (prDDPins);
			prDDPins.println("      ]");
			
			prDDPins.println("    , " + formValue("unitDictionary") + ": [");
			printUnits (prDDPins);
			prDDPins.println("      ]");
		}

		prDDPins.println("    , " + formValue("PropertyMapDictionary") + ": [");
		printPropertyMaps (prDDPins);
		prDDPins.println("      ]");		
	}
	
	// Print the classes
	public  void printClass (SchemaFileDefn lSchemaFileDefn, PrintWriter prDDPins) {
		String delimiter1 = "  ";
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (lClass.title.indexOf("PDS3") > -1) continue;
			prDDPins.println("      " + delimiter1 + "{");				
			delimiter1 = ", ";
			prDDPins.println("          " + formValue("class") + ": {");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lClass.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lClass.title) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lClass.nameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("steward") + ": " + formValue(lClass.steward) + " ,");	
			prDDPins.println("            " + formValue("versionId") + ": " + formValue(lClass.versionId) + " ,");	
			prDDPins.println("            " + formValue("description") + ": " + formValue(lClass.description));		
			printAssoc (lClass, prDDPins);
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}		

	// Print the Associations
	public  void printAssoc (PDSObjDefn lClass, PrintWriter prDDPins) {
		ArrayList <AssocDefn> lClassAssocArr = new ArrayList <AssocDefn> ();
		ArrayList <AssocDefn> lAttrAssocArr = new ArrayList <AssocDefn> ();
		TreeMap <String, AssocDefn> lAssocDefnMap = new TreeMap <String, AssocDefn> ();
		if (lClass.allAttrAssocArr.isEmpty()) return;
		
		// separate the attributes from the associations (classes) 
		for (Iterator<AttrDefn> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lRelAttr = (AttrDefn) i.next();
			AssocDefn lAssoc = InfoModel.masterMOFAssocIdMap.get(lRelAttr.identifier);
			if (lAssoc != null) {
				AssocDefn lTempAssoc = new AssocDefn ();
				lTempAssoc.createAssocSingletons (lAssoc);
				if (lRelAttr.isAttribute) {
					lTempAssoc.childAttrArr.add(lRelAttr);
					String sortTagPre = "";
					if (lTempAssoc.groupName.indexOf("TBD") != 0) sortTagPre = lTempAssoc.groupName;
					String sortTag = sortTagPre +  "-"  + lTempAssoc.identifier;
					lAssocDefnMap.put(sortTag, lTempAssoc);
				} else {
					for (Iterator<PDSObjDefn> j = lRelAttr.valClassArr.iterator(); j.hasNext();) {
						PDSObjDefn lRelClass = (PDSObjDefn) j.next();
						lTempAssoc.childClassArr.add(lRelClass);
					}
					lClassAssocArr.add(lTempAssoc);
				}
			}
		}
		
		// get attribute associations - needed to group "choice" blocks
		ArrayList <AssocDefn> lAssocDefnArr = new ArrayList <AssocDefn> (lAssocDefnMap.values());
		String lPrevSortTag = "FirstOfItsName";
		AssocDefn lPrevAssoc = null;
		for (Iterator<AssocDefn> i = lAssocDefnArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			if (lPrevSortTag.compareTo(lAssoc.groupName) == 0) {
				if (lAssoc.groupName.indexOf("TBD") == 0) {
					lAttrAssocArr.add(lAssoc);
				} else {
					lPrevAssoc.childAttrArr.add(lAssoc.childAttrArr.get(0)); // there is always only one
				}
			} else {
				lPrevSortTag = lAssoc.groupName;
				lPrevAssoc = lAssoc;
				lAttrAssocArr.add(lAssoc);
			}
		}
		
		// sort by classOrder
		TreeMap <String, AssocDefn> lAttrAssocMap = new TreeMap <String, AssocDefn> ();
		for (Iterator<AssocDefn> i = lAttrAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			String lSortOrder = lAssoc.classOrder + "-" + lAssoc.identifier;
			lAttrAssocMap.put(lSortOrder, lAssoc);
		}
		lAttrAssocArr = new ArrayList <AssocDefn> (lAttrAssocMap.values());
		
		// write attribute List
		prDDPins.println("              , " + formValue("associationList") + ": [");	
		String delimiter1 = "  ";
		for (Iterator<AssocDefn> i = lAttrAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");				
			delimiter1 = ", ";
			prDDPins.println("                " + formValue("identifier") + ": " + formValue(lAssoc.identifier) + " ,");	
			prDDPins.println("                " + formValue("title") + ": " + formValue(lAssoc.title) + " ,");	
			prDDPins.println("                " + formValue("assocType") + ": " + formValue("attribute_of") + " ,");	
			prDDPins.println("                " + formValue("isAttribute") + ": " + formBooleanValue(true) + " ,");	
			prDDPins.println("                " + formValue("isChoice") + ": " + formBooleanValue(lAssoc.isChoice) + " ,");	
			prDDPins.println("                " + formValue("isAny") + ": " + formBooleanValue(lAssoc.isAny) + " ,");	
//			prDDPins.println("                " + formValue("groupName") + ": " + formValue(lAssoc.groupName) + " ,");	
			prDDPins.println("                " + formValue("minimumCardinality") + ": " + formValue(lAssoc.cardMin) + " ,");	
			prDDPins.println("                " + formValue("maximumCardinality") + ": " + formValue(lAssoc.cardMax) + " ,");	
			prDDPins.println("                " + formValue("classOrder") + ": " + formValue(lAssoc.classOrder) + " ,");	
			prDDPins.println("                " + formValue("attributeId") + ": [");				
			String delimiter2 = "";
			for (Iterator<AttrDefn> j = lAssoc.childAttrArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(delimiter2);
//				prDDPins.print("                  " + formValue(lAttr.identifier) + delimiter2);	
				prDDPins.print("                  " + formValue(lAttr.identifier));	
				delimiter2 = ",\n";
			}
			prDDPins.print("\n");
			prDDPins.println("                 ]");
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		
		// write the subclass association (if it exists)
		if (lClass.subClassOfInst != null && ! lClass.subClassOfInst.isVacuous && ! lClass.subClassOfInst.isAbstract) {
			prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");				
			delimiter1 = ", ";
			String lIdentifier = InfoModel.getClassIdentifier(lClass.nameSpaceIdNC, lClass.subClassOfTitle);
			prDDPins.println("                " + formValue("identifier") + ": " + formValue(lIdentifier) + " ,");	
			prDDPins.println("                " + formValue("title") + ": " + formValue(lClass.subClassOfTitle) + " ,");	
			prDDPins.println("                " + formValue("assocType") + ": " + formValue("parent_of") + " ,");	
			prDDPins.println("                " + formValue("isAttribute") + ": " + formBooleanValue(false) + " ,");	
			prDDPins.println("                " + formValue("isChoice") + ": " + formBooleanValue(false) + " ,");	
			prDDPins.println("                " + formValue("isAny") + ": " + formBooleanValue(false) + " ,");	
//			prDDPins.println("                " + formValue("groupName") + ": " + formValue(lAssoc.groupName) + " ,");	
			prDDPins.println("                " + formValue("minimumCardinality") + ": " + formValue("1") + " ,");	
			prDDPins.println("                " + formValue("maximumCardinality") + ": " + formValue("1") + " ,");	
			prDDPins.println("                " + formValue("classOrder") + ": " + formValue("1010") + " ,");	
			prDDPins.println("                " + formValue("attributeId") + ": [");				
			prDDPins.println("                  " + formValue(lIdentifier));
			prDDPins.println("                 ]");
			prDDPins.println("              }");
			prDDPins.println("            }");			
		}
		
		// sort by classOrder
		TreeMap <String, AssocDefn> lClassAssocMap = new TreeMap <String, AssocDefn> ();
		for (Iterator<AssocDefn> i = lClassAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			String lSortOrder = lAssoc.classOrder + "-" + lAssoc.identifier;
			lClassAssocMap.put(lSortOrder, lAssoc);
		}
		lClassAssocArr = new ArrayList <AssocDefn> (lClassAssocMap.values());
		
		// write the class list
		for (Iterator<AssocDefn> i = lClassAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");				
			delimiter1 = ", ";		
			prDDPins.println("                " + formValue("identifier") + ": " + formValue(lAssoc.identifier) + " ,");	
			prDDPins.println("                " + formValue("title") + ": " + formValue(lAssoc.title) + " ,");	
			prDDPins.println("                " + formValue("assocType") + ": " + formValue("component_of") + " ,");	
			prDDPins.println("                " + formValue("isAttribute") + ": " + formBooleanValue(false) + " ,");	
			prDDPins.println("                " + formValue("isChoice") + ": " + formBooleanValue(lAssoc.isChoice) + " ,");	
			prDDPins.println("                " + formValue("isAny") + ": " + formBooleanValue(lAssoc.isAny) + " ,");	
//			prDDPins.println("                " + formValue("groupName") + ": " + formValue(lAssoc.groupName) + " ,");	
			prDDPins.println("                " + formValue("minimumCardinality") + ": " + formValue(lAssoc.cardMin) + " ,");	
			prDDPins.println("                " + formValue("maximumCardinality") + ": " + formValue(lAssoc.cardMax) + " ,");	
			prDDPins.println("                " + formValue("classOrder") + ": " + formValue(lAssoc.classOrder) + " ,");	
			prDDPins.println("                " + formValue("classId") + ": [");	
			String delimiter2 = "";
			for (Iterator<PDSObjDefn> j = lAssoc.childClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lRelClass = (PDSObjDefn) j.next();
				prDDPins.print(delimiter2);
				prDDPins.print("                   " + formValue(lRelClass.identifier));
				delimiter2 = ",\n";
			}
			prDDPins.print("\n");
			prDDPins.println("                 ]");
			prDDPins.println("              }");
			prDDPins.println("            }");				
		}
		prDDPins.println("           ]");
	}
	
	// Print the the Protege Pins DE
	public  void printAttr (SchemaFileDefn lSchemaFileDefn, PrintWriter prDDPins) {
		// print the data elements
		String delimiter1 = "  ";
///		ArrayList <AttrDefn> lAttrArr = new ArrayList (InfoModel.masterMOFAttrIdMap.values());
///		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.attrNameSpaceIdNC) != 0) continue;
			if (! (lAttr.isUsedInClass && lAttr.isAttribute)) continue;
			prDDPins.println("      " + delimiter1 + "{");				
			delimiter1 = ", ";
			prDDPins.println("          " + formValue("attribute") + ": {");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lAttr.title) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lAttr.attrNameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("steward") + ": " + formValue(lAttr.steward) + " ,");	
			prDDPins.println("            " + formValue("versionId") + ": " + formValue(lAttr.versionIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("description") + ": " + formValue(lAttr.description) + " ,");	
			prDDPins.println("            " + formValue("isNillable") + ": " + formBooleanValue(lAttr.isNilable) + " ,");	
			prDDPins.println("            " + formValue("isEnumerated") + ": " + formBooleanValue(lAttr.isEnumerated) + " ,");	
			prDDPins.println("            " + formValue("dataType") + ": " + formValue(lAttr.valueType) + " ,");	
			prDDPins.println("            " + formValue("dataTypeId") + ": " + formValue(lAttr.getValueTypeIdentifier()) + " ,");	
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lAttr.getMinimumCharacters2(true,true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lAttr.getMaximumCharacters2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lAttr.getMinimumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lAttr.getMaximumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("pattern") + ": " + formValue(lAttr.getPattern(true)) + " ,");	
			prDDPins.println("            " + formValue("unitOfMeasure") + ": " + formValue(lAttr.getUnitOfMeasure(false)) + " ,");	
			prDDPins.println("            " + formValue("unitOfMeasureId") + ": " + formValue(lAttr.getUnitOfMeasureIdentifier()) + " ,");	
			prDDPins.println("            " + formValue("unitId") + ": " + formValue(lAttr.getUnits(false)) + " ,");	
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lAttr.getDefaultUnitId(false)));	
			printPermValues (lAttr, prDDPins);
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}

	// Print the Permissible Values and Value Meanings
	public  void printPermValues (AttrDefn lAttr, PrintWriter prDDPins) {
		if (lAttr.permValueArr.isEmpty()) return;
		String delimiter1 = "  ";
		prDDPins.println("          , " + formValue("PermissibleValueList") + ": [");
		for (Iterator<PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
//			prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			prDDPins.println("            " + delimiter1 + "{" + formValue("PermissibleValue") + ": {");				
			delimiter1 = ", ";
//			prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lPermValueDefn.value) + " ,");	
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue(lPermValueDefn.value_meaning));	
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");
	}
	
	// Print the Data Types
	public  void printDataType (PrintWriter prDDPins) {
		String delimiter1 = "  ";
		for (Iterator<DataTypeDefn> i = InfoModel.masterDataTypesArr2.iterator(); i.hasNext();) {
			DataTypeDefn lDataType = (DataTypeDefn) i.next();
//			prDDPins.println("      , {" + formValue("DataType") + ": {");			
			prDDPins.println("      " + delimiter1 + "{" + formValue("DataType") + ": {");				
			delimiter1 = ", ";
//			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lDataType.identifier) + " ,");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lDataType.pds4Identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lDataType.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lDataType.nameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lDataType.getMinimumCharacters2(true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lDataType.getMaximumCharacters2(true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lDataType.getMinimumValue2(true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lDataType.getMaximumValue2(true)));	
			printPattern(lDataType, prDDPins);
			prDDPins.println("            }");
			prDDPins.println("         }");
		}
	}	
	
	// Print the data type Pattern
	public  void printPattern (DataTypeDefn lDataType, PrintWriter prDDPins) {
		String delimiter1 = "  ";
		if (lDataType.pattern.isEmpty()) return;
		prDDPins.println("          , " + formValue("patternList") + ": [");	
		for (Iterator<String> i = lDataType.pattern.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
//			prDDPins.println("            , {" + formValue("Pattern") + ": {");	
//			prDDPins.println("            , {" + formValue("") + ": {");					
			prDDPins.println("            " + delimiter1 + "{" + formValue("Pattern") + ": {");				
			delimiter1 = ", ";

			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		prDDPins.println("           ]");
	}

	// Print the Units
	public  void printUnits (PrintWriter prDDPins) {
		String delimiter1 = "  ";
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();
//			prDDPins.println("      , {" + formValue("Unit") + ": {");			
			prDDPins.println("      " + delimiter1 + "{" + formValue("Unit") + ": {");				
			delimiter1 = ", ";
//			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lUnit.identifier) + " ,");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lUnit.pds4Identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lUnit.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(DMDocument.masterNameSpaceIdNCLC) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lUnit.default_unit_id));	
			printUnitId (lUnit, prDDPins); 
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}	

	// Print the Unit Identifier
	public  void printUnitId (UnitDefn lUnit, PrintWriter prDDPins) {
		String delimiter1 = "  ";
		if (lUnit.unit_id.isEmpty()) return;
		prDDPins.println("          , " + formValue("unitIdList") + ": [");	
		for (Iterator<String> i = lUnit.unit_id.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
//			prDDPins.println("            , {" + formValue("UnitId") + ": {");
//			prDDPins.println("            , {" + formValue("UnitId") + ": {");					
			prDDPins.println("            " + delimiter1 + "{" + formValue("UnitId") + ": {");				
			delimiter1 = ", ";

			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");
	}
	
		// Print the Property Maps
		public  void printPropertyMaps (PrintWriter prDDPins) {
			String delimiter1 = "  ";
			for (Iterator<PropertyMapsDefn> i = InfoModel.masterPropertyMapsArr.iterator(); i.hasNext();) {
				PropertyMapsDefn lPropertyMaps = (PropertyMapsDefn) i.next();
//				prDDPins.println("      , {" + formValue("PropertyMaps") + ": {");			
				prDDPins.println("      " + delimiter1 + "{" + formValue("PropertyMaps") + ": {");				
				delimiter1 = ", ";
				prDDPins.println("            " + formValue("identifier") + ": " + formValue(lPropertyMaps.rdfIdentifier) + " ,");		
				prDDPins.println("            " + formValue("title") + ": " + formValue(lPropertyMaps.title) + " ,");		
//				prDDPins.println("            " + formValue("steward_id") + ": " + formValue(lPropertyMaps.steward_id) + " ,");	
				prDDPins.println("            " + formValue("namespace_id") + ": " + formValue(lPropertyMaps.namespace_id) + " ,");			
				prDDPins.println("            " + formValue("description") + ": " + formValue(lPropertyMaps.description) + " ,");	
				prDDPins.println("            " + formValue("external_property_map_id") + ": " + formValue(lPropertyMaps.external_property_map_id));	
				printPropertyMap (lPropertyMaps, prDDPins); 
				prDDPins.println("          }");
				prDDPins.println("        }");
			}
		}	
		
		// Print the Property Map
		public  void printPropertyMap (PropertyMapsDefn lPropertyMaps, PrintWriter prDDPins) {
			String delimiter1 = "  ";
			if (lPropertyMaps.propertyMapArr.isEmpty()) return;
			prDDPins.println("          , " + formValue("propertyMapList") + ": [");	
			for (Iterator<PropertyMapDefn> i = lPropertyMaps.propertyMapArr.iterator(); i.hasNext();) {
				PropertyMapDefn lPropertyMap = (PropertyMapDefn) i.next();
//				prDDPins.println("            , {" + formValue("PropertyMap") + ": {");			
				prDDPins.println("            " + delimiter1 + "{" + formValue("PropertyMap") + ": {");				
				delimiter1 = ", ";
				prDDPins.println("                  " + formValue("identifier") + ": " + formValue(lPropertyMap.identifier) + " ,");		
				prDDPins.println("                  " + formValue("title") + ": " + formValue(lPropertyMap.title) + " ,");			
				prDDPins.println("                  " + formValue("model_object_id") + ": " + formValue(lPropertyMap.model_object_id) + " ,");	
				prDDPins.println("                  " + formValue("model_object_type") + ": " + formValue(lPropertyMap.model_object_type) + " ,");	
				prDDPins.println("                  " + formValue("instance_id") + ": " + formValue(lPropertyMap.instance_id) + " ,");		
				prDDPins.println("                  " + formValue("external_namespace_id") + ": " + formValue(lPropertyMap.external_namespace_id) + " ,");	
				prDDPins.println("                  " + formValue("description") + ": " + formValue(lPropertyMap.description));	
				printPropertyMapEntrys (lPropertyMap, prDDPins); 
				prDDPins.println("                 }");
				prDDPins.println("              }");
			}
			prDDPins.println("            ]");
	}
		
		// Print the Property Map Entrys
		public  void printPropertyMapEntrys (PropertyMapDefn lPropertyMap, PrintWriter prDDPins) {
			String delimiter1 = "  ";
			if (lPropertyMap.propertyMapEntryArr.isEmpty()) return;
			prDDPins.println("                , " + formValue("propertyMapEntryList") + ": [");	
			for (Iterator<PropertyMapEntryDefn> i = lPropertyMap.propertyMapEntryArr.iterator(); i.hasNext();) {
				PropertyMapEntryDefn lPropertyMapEntry = (PropertyMapEntryDefn) i.next();
//				prDDPins.println("                  , {" + formValue("PropertyMapEntry") + ": {");					
				prDDPins.println("                  " + delimiter1 + "{" + formValue("PropertyMapEntry") + ": {");				
				delimiter1 = ", ";
				prDDPins.println("                        " + formValue("property_name") + ": " + formValue(lPropertyMapEntry.property_name) + " ,");		
				prDDPins.println("                        " + formValue("property_value") + ": " + formValue(lPropertyMapEntry.property_value) + " }");		
				prDDPins.println("                    }");
			}
			prDDPins.println("                    ]");	
	}
						
	// Print the the Protege Pins Properties
	public  void printPDDPPR (PrintWriter prDDPins) {
		ArrayList <AssocDefn> lSortedAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());
		for (Iterator<AssocDefn> i = lSortedAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			String prDataIdentifier = "PR." + lAssoc.identifier;
			prDDPins.println("([" + prDataIdentifier + "] of Property");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
			prDDPins.println("  (registeredBy [" + DMDocument.registeredByValue + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");						
			prDDPins.println("  (classOrder \"" + lAssoc.classOrder + "\")");
//			prDDPins.println("  (versionIdentifier \"" + InfoModel.identifier_version_id + "\"))");
			prDDPins.println("  (versionIdentifier \"" + DMDocument.masterPDSSchemaFileDefn.identifier_version_id + "\"))");
		}
	}

	// Print the the Protege Pins CD
	public  void printPDDPCD (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lCDAttrArr = new ArrayList <IndexDefn> (InfoModel.cdAttrMap.values());
		for (Iterator<IndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "CD_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of ConceptualDomain");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (having ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lDEC = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "DEC_" + lDEC + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");

			lfc = "";
			prDDPins.println("  (representing2 ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				if (lAttr.isEnumerated) {
					prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
				} else {
					prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
				}
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
		
			// write terminological entry
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins DEC
	public  void printPDDPDEC (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lDECAttrArr = new ArrayList <IndexDefn> (InfoModel.decAttrMap.values());
		for (Iterator<IndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "DEC_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of DataElementConcept");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (expressing ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");
			lfc = "";
			prDDPins.println("  (specifying ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lCD = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "CD_" + lCD + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
			
			// write the terminological entry
			
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins TE
	public  void printPDDPTE (PrintWriter prDDPins) {
		// print the Terminological Entry
///		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
///		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				// print TE section
				prDDPins.println("([" + lAttr.teDataIdentifier + "] of TerminologicalEntry");
				prDDPins.println("  (administeredItemContext [NASA_PDS])");
				prDDPins.println("  (definition ["  + lAttr.defDataIdentifier + "])");
				prDDPins.println("  (designation [" + lAttr.desDataIdentifier + "])");
				prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");
		
				// print definition section
				prDDPins.println("([" + lAttr.defDataIdentifier + "] of Definition");
				prDDPins.println("  (definitionText \"" + InfoModel.escapeProtegeString(lAttr.description) + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
				
				// print designation section
				prDDPins.println("([" + lAttr.desDataIdentifier + "] of Designation");
				prDDPins.println("  (designationName \"" + lAttr.title + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
			}
		}
	}	

	// Print the the Protege Pins Misc
	public  void printPDDPMISC (PrintWriter prDDPins) {
		// print the Miscellaneous records
		prDDPins.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
		prDDPins.println("	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
		prDDPins.println("	(administrativeStatus Final)");
		prDDPins.println("	(changeDescription \"PSDD content has been merged into the model.\")");
		prDDPins.println("	(creationDate \"2010-03-10\")");
		prDDPins.println("	(effectiveDate \"2010-03-10\")");
		prDDPins.println("	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
		prDDPins.println("	(lastChangeDate \"2010-03-10\")");
		prDDPins.println("	(origin \"Planetary Data System\")");
		prDDPins.println("	(registrationStatus Preferred)");
		prDDPins.println("	(unresolvedIssue \"Issues still being determined.\")");
		prDDPins.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");
		
		prDDPins.println("([" + DMDocument.registrationAuthorityIdentifierValue + "] of RegistrationAuthorityIdentifier");
		prDDPins.println("	(internationalCodeDesignator \"0001\")");
		prDDPins.println("	(opiSource \"1\")");
		prDDPins.println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
		prDDPins.println("	(organizationPartIdentifier \"Planetary Data System\"))");

		prDDPins.println("([" + DMDocument.registeredByValue + "] of RegistrationAuthority");
		prDDPins.println("	(documentationLanguageIdentifier [LI_English])");
		prDDPins.println("	(languageUsed [LI_English])");
		prDDPins.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
		prDDPins.println("	(organizationName \"NASA Planetary Data System\")");
		prDDPins.println("	(registrar [PDS_Registrar])");
		prDDPins.println("	(registrationAuthorityIdentifier_v [" + DMDocument.registrationAuthorityIdentifierValue + "]))");
	
		prDDPins.println("([NASA_PDS] of Context");
		prDDPins.println("	(dataIdentifier  \"NASA_PDS\"))");
		
		prDDPins.println("([PDS_Registrar] of  Registrar");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(registrarIdentifier \"PDS_Registrar\"))");
		
		prDDPins.println("([Steward_PDS] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([pds] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([img] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([rings] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([ops] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([Submitter_PDS] of Submitter");
		prDDPins.println("	(contact [DataDesignWorkingGroup])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
		prDDPins.println("([PDS_Standards_Coordinator] of Contact");
		prDDPins.println("	(contactTitle \"PDS_Standards_Coordinator\")");
		prDDPins.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
		prDDPins.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.6135\")");
		prDDPins.println("	(contactName \"Elizabeth Rye\"))");
		
		prDDPins.println("([DataDesignWorkingGroup] of Contact");
		prDDPins.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.9338\")");
		prDDPins.println("	(contactName \"J. Steven Hughes\"))");
		
		prDDPins.println("([LI_English] of LanguageIdentification");
		prDDPins.println("  (countryIdentifier \"USA\")");
		prDDPins.println("  (languageIdentifier \"English\"))");
			
		// write the unitOfMeasure
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();		
			prDDPins.println("([" + lUnit.title + "] of UnitOfMeasure");
			prDDPins.println("	(measureName \"" + lUnit.title + "\")");
			prDDPins.println("	(defaultUnitId \"" + InfoModel.escapeProtegeString(lUnit.default_unit_id) + "\")");
			prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
			if (! lUnit.unit_id.isEmpty() )
			{
				String lSpace = "";
				prDDPins.print("	(unitId ");
				// set the units
				for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
					String lVal = (String) j.next();
					prDDPins.print(lSpace + "\"" + InfoModel.escapeProtegeString(lVal) + "\"");
					lSpace = " ";
				}
				prDDPins.println("))");
			}
		}
		
		// write the TBD_unitOfMeasure		
		prDDPins.println("([" + "TBD_unit_of_measure_type" + "] of UnitOfMeasure");
		prDDPins.println("	(measureName \"" + "TBD_unit_of_measure_type" + "\")");
		prDDPins.println("	(defaultUnitId \"" + "defaultUnitId" + "\")");
		prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
		prDDPins.println("	(unitId \"" + "TBD_unitId" + "\"))");
		
		// print data types
		for (Iterator<PDSObjDefn> i = InfoModel.masterDataTypesArr.iterator(); i.hasNext();) {
			PDSObjDefn lDataType = (PDSObjDefn) i.next();	
			prDDPins.println("([" + lDataType.title + "] of DataType");
			prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
			prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
}	
