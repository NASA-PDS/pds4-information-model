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

class WriteCSVFiles extends Object {	
	String delmBegin = "\"", delmMid = "\",\"", delmEnd = "\"";
	
	public WriteCSVFiles () {
		return;
	}
	
//	write the CSV File
	public void writeCSVFile (ArrayList <PDSObjDefn> lClassArr, SchemaFileDefn lSchemaFileDefn, String lOtherLanguage)  throws java.io.IOException {
		String pIdentifier;
		String blanks = "                              ";
		String padding;
		int padLength;
		
//		String lFileName;
		String lFileName = lSchemaFileDefn.relativeFileSpecDDCSV;
		if (lOtherLanguage != null) lFileName = lSchemaFileDefn.relativeFileSpecDDCSV + "_" + lOtherLanguage;				
		lFileName += ".csv_MOF";
		FileOutputStream lFileOutputStream = new FileOutputStream(lFileName);
		BufferedWriter prCSVAttr = new BufferedWriter(new OutputStreamWriter(lFileOutputStream,"UTF8"));
		prCSVAttr.write(delmBegin + "Sort Key" + delmMid + "Type" + delmMid + "Name" + delmMid + "Version" + delmMid + "Name Space Id" + delmMid + "Description" + delmMid + "Steward" + delmMid + "Value Type"  + delmMid + "Minimum Cardinality"  + delmMid + "Maximum Cardinality"  + delmMid + "Minimum Value"  + delmMid + "Maximum Value" + delmMid+ "Minimum Characters"  + delmMid + "Maximum Characters" + delmMid + "Unit of Measure Type" + delmMid + "Specified Unit Id" + delmMid + "Attribute Concept" + delmMid + "Conceptual Domain" + delmEnd + "\r\n");		
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;

			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				padLength = 30 - lClass.title.length();
				if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
				String classSortField = lClass.nameSpaceId + lClass.title + ":1" + padding;
//				String attrSortField = lClass.nameSpaceId + lClass.title + ":2" + padding;
				pIdentifier = classSortField;
				
				prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Class" + delmMid + lClass.nameInLanguage(lOtherLanguage) + delmMid + lClass.versionId + delmMid + lClass.nameSpaceIdNC + delmMid + lClass.definitionInLanguage(lOtherLanguage) + delmMid + lClass.steward + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + "" + delmMid + ""  + delmMid + ""  + delmEnd + "\r\n");
				ArrayList <AttrDefn> allAttr = new ArrayList <AttrDefn> ();
				allAttr.addAll(lClass.ownedAttribute);
				allAttr.addAll(lClass.inheritedAttribute);
				writeCSVFileLine (lClass.title, lClass.nameSpaceId, allAttr, lOtherLanguage, prCSVAttr);
			}
		}
		
		// write any singleton attributes
		ArrayList <AttrDefn> lEnumAttrArr = new ArrayList <AttrDefn> ();
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.userSingletonClassAttrIdMap.values());
		for (Iterator <AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.attrNameSpaceIdNC) != 0)) continue;
			lEnumAttrArr.add(lAttr);
		}
		if (! lEnumAttrArr.isEmpty()) {
//			writeCSVFileLine ("USER", lSchemaFileDefn.nameSpaceIdNC, lEnumAttrArr, lOtherLanguage, prCSVAttr);
			writeCSVFileLine (DMDocument.LDDToolSingletonClassTitle, lSchemaFileDefn.nameSpaceIdNC, lEnumAttrArr, lOtherLanguage, prCSVAttr);
		}
		prCSVAttr.close();
	}	
	
//	write an attribute, one line
	public void writeCSVFileLine (String lClassTitle, String lClassNameSpaceId, ArrayList <AttrDefn> allAttr, String lOtherLanguage, BufferedWriter prCSVAttr)  throws java.io.IOException {
		String pIdentifier;
		String blanks = "                              ";
		String padding;
		int padLength;
		String classSortField, attrSortField, valueSortField;

		padLength = 30 - lClassTitle.length();
		if (padLength < 0) padLength = 0;
		padding = blanks.substring(0, padLength);
		classSortField = lClassNameSpaceId + lClassTitle + ":1" + padding;
		attrSortField = lClassNameSpaceId + lClassTitle + ":2" + padding;
		pIdentifier = classSortField;
		
		for (Iterator <AttrDefn> j = allAttr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();	
			if (lAttr != null) {
				String pMinVal = lAttr.getMinimumValue2 (true, true);
				String pMaxVal = lAttr.getMaximumValue2 (true, true);
				String pMinChar = lAttr.getMinimumCharacters2 (true, true);
				String pMaxChar = lAttr.getMaximumCharacters2 (true, true);
				padLength = 30 - lAttr.title.length();
				if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
				pIdentifier = attrSortField + " " + lAttr.attrNameSpaceId + lAttr.title + ":1" + padding;
				valueSortField = attrSortField + " " + lAttr.attrNameSpaceId + lAttr.title + ":2" + padding;
				prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Attribute" + delmMid + lAttr.nameInLanguage(lOtherLanguage) + delmMid + "n/a" + delmMid + lAttr.getNameSpaceIdNC () + delmMid + lAttr.definitionInLanguage(lOtherLanguage) + delmMid + lAttr.getSteward () + delmMid + lAttr.valueType + delmMid + lAttr.cardMin + delmMid + lAttr.cardMax + delmMid + pMinVal + delmMid + pMaxVal + delmMid+ pMinChar + delmMid + pMaxChar+ delmMid + lAttr.getUnitOfMeasure (true) + delmMid + lAttr.getDefaultUnitId (true) + delmMid + lAttr.classConcept + delmMid + lAttr.dataConcept + delmEnd + "\r\n");

				if ( ! (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty())) {
					for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
						PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
						String lValue = lPermValueDefn.value;
						if (lValue.length() > 20) lValue = lValue.substring(0,20);
						pIdentifier = valueSortField + " Value:" + lValue;
						prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Value" + delmMid + lPermValueDefn.value + delmMid + "" + delmMid + "" + delmMid + lPermValueDefn.value_meaning + delmEnd + "\r\n");
					}
				}
			}
		}
	}
}
