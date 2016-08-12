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
		lFileName += ".csv";
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
				prCSVAttr.write(delmBegin + pIdentifier + delmMid + "Attribute" + delmMid + lAttr.nameInLanguage(lOtherLanguage) + delmMid + "n/a" + delmMid + lAttr.getNameSpaceId () + delmMid + lAttr.definitionInLanguage(lOtherLanguage) + delmMid + lAttr.getSteward () + delmMid + lAttr.valueType + delmMid + lAttr.cardMin + delmMid + lAttr.cardMax + delmMid + pMinVal + delmMid + pMaxVal + delmMid+ pMinChar + delmMid + pMaxChar+ delmMid + lAttr.getUnitOfMeasure (true) + delmMid + lAttr.getDefaultUnitId (true) + delmMid + lAttr.classConcept + delmMid + lAttr.dataConcept + delmEnd + "\r\n");

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
	
//	write the DD Normalized file - CSV
	public void printDDDBFile () throws java.io.IOException  {
		String lFileName = DMDocument.outputDirPath + "PDS4DDDB.csv";
		PrintWriter prCSVAttr = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		TreeMap <String, AttrDefn> AttrMap = new TreeMap <String, AttrDefn> ();
		TreeMap <String, AttrDefn> AssocMap = new TreeMap <String, AttrDefn> ();
//		prCSVAttr.println(delmBegin + "TYPE" + delmMid + "UNIQUE ID" + delmMid + "NAME" + delmMid + "DEFINITION" + delmMid + "DATA TYPE" + delmMid + "MIN VALUE" + delmMid + "MAX VALUE" + delmMid + "MIN CHARACTERS" + delmMid + "MAX CHARACTERS" + delmEnd);		
		prCSVAttr.println(delmBegin + "TYPE" + delmMid + "UNIQUE ID" + delmMid + "NAME" + delmMid + "DEFINITION" + delmMid + "DATA TYPE" + delmEnd);		
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				prCSVAttr.println(delmBegin + "CLASS" + delmMid + lClass.identifier + delmMid + lClass.title + delmMid + lClass.description + delmEnd);
				for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();	
					AttrMap.put(lAttr.identifier, lAttr);
				}
				for (Iterator <AttrDefn> j = lClass.inheritedAttribute.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();	
					AttrMap.put(lAttr.identifier, lAttr);
				}
				for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();	
					AssocMap.put(lAttr.identifier, lAttr);
				}
				for (Iterator <AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();	
					AssocMap.put(lAttr.identifier, lAttr);
				}

			}
		}
		Set <String> set1 = AttrMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			AttrDefn lAttr = (AttrDefn) AttrMap.get(lId);	
			if (lAttr != null) {
//				prCSVAttr.println(delmBegin + "ATTRIBUTE" + delmMid + lAttr.identifier + delmMid + lAttr.title + delmMid + lAttr.description + delmMid + lAttr.valueType + delmMid + lAttr.minimum_value + delmMid + lAttr.maximum_value+ delmMid + lAttr.minimum_characters + delmMid + lAttr.maximum_characters + delmEnd);
				prCSVAttr.println(delmBegin + "ATTRIBUTE" + delmMid + lAttr.identifier + delmMid + lAttr.title + delmMid + lAttr.description + delmMid + lAttr.valueType + delmEnd);
				if ((lAttr.valArr != null) && (! lAttr.valArr.isEmpty())) {
					int lValCnt = 0;
					for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
						String lVal = (String) k.next();
						if (lVal.compareTo ("") != 0) {
							lValCnt++;
							prCSVAttr.println(delmBegin + "ATTR VALUE" + delmMid + lAttr.identifier + "_Value_" + lValCnt + delmMid + lVal + delmMid + "" + delmEnd);
						}
					}
				}
			}
		}
		Set <String> set2 = AssocMap.keySet();
		Iterator <String> iter2 = set2.iterator();
		while(iter2.hasNext()) {
			String lId = (String) iter2.next();
			AttrDefn lAttr = (AttrDefn) AssocMap.get(lId);	
			if (lAttr != null) {
				prCSVAttr.println(delmBegin + "ASSOCIATION" + delmMid + lAttr.identifier + delmMid + lAttr.title + delmMid + lAttr.description + delmEnd);
				if ((lAttr.valArr != null) && (! lAttr.valArr.isEmpty())) {
					int lValCnt = 0;
					for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
						String lVal = (String) k.next();
						if (lVal.compareTo ("") != 0) {
							lValCnt++;
							prCSVAttr.println(delmBegin + "ASSOC VALUE" + delmMid + lAttr.identifier + "_Value_" + lValCnt + delmMid + lVal + delmMid + "" + delmEnd);
						}
					}
				}
			}
		}
		prCSVAttr.close();				
	}
}
