package gov.nasa.pds.model.plugin; 

import java.io.*;
import java.util.*;



class WriteDOMCSVFiles extends Object {	
	static final String DELM_BEGIN = "\"", DELM_MID = "\",\"", DELM_END = "\"";
	TreeMap <String, TermEntryDefn> termEntryMap;
	
	public WriteDOMCSVFiles () {
		return;
	}
	
//	write the CSV File
	public void writeDOMCSVFile (ArrayList <DOMClass> lClassArr, SchemaFileDefn lSchemaFileDefn, String lOtherLanguage)  throws java.io.IOException {
		String pIdentifier;
		String blanks = "                              ";
		String padding;
		int padLength;
		
//		String lFileName;
		String lFileName = lSchemaFileDefn.relativeFileSpecDDCSV;
		if (lOtherLanguage != null) lFileName = lSchemaFileDefn.relativeFileSpecDDCSV + "_" + lOtherLanguage;				
		lFileName += "_DOM.csv";
                System.out.println("file path ======"+ lFileName);
		FileOutputStream lFileOutputStream = new FileOutputStream(lFileName);
		BufferedWriter prCSVAttr = new BufferedWriter(new OutputStreamWriter(lFileOutputStream,"UTF8"));
		prCSVAttr.write(DELM_BEGIN + "Sort Key" + DELM_MID + "Type" + DELM_MID + "Name" + DELM_MID + "Version" + DELM_MID + "Name Space Id" + DELM_MID + "Description" + DELM_MID + "Steward" + DELM_MID + "Value Type"  + DELM_MID + "Minimum Cardinality"  + DELM_MID + "Maximum Cardinality"  + DELM_MID + "Minimum Value"  + DELM_MID + "Maximum Value" + DELM_MID+ "Minimum Characters"  + DELM_MID + "Maximum Characters" + DELM_MID + "Unit of Measure Type" + DELM_MID + "Specified Unit Id" + DELM_MID + "Attribute Concept" + DELM_MID + "Conceptual Domain" + DELM_END + "\r\n");		
		for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;

			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				padLength = 30 - lClass.title.length();
				if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
				String classSortField = lClass.nameSpaceId + lClass.title + ":1" + padding;
//				String attrSortField = lClass.nameSpaceId + lClass.title + ":2" + padding;
				pIdentifier = classSortField;
				
	//			prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Class" + DELM_MID + nameInLanguage(lOtherLanguage) + DELM_MID + lClass.versionId + DELM_MID + lClass.nameSpaceIdNC + DELM_MID + definitionInLanguage(lOtherLanguage) + DELM_MID + lClass.steward + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID+ "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + ""  + DELM_MID + ""  + DELM_END + "\r\n");
				
				
				prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Class" + DELM_MID + lClass.getNameInLanguage(lOtherLanguage) + DELM_MID + lClass.versionId + DELM_MID + lClass.nameSpaceIdNC + DELM_MID + lClass.getDefinitionInLanguage(lOtherLanguage) + DELM_MID + lClass.steward + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID+ "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + ""  + DELM_MID + ""  + DELM_END + "\r\n");
				
			 ///   System.out.println("in WriteDOMCSV");
				ArrayList <DOMProp> allAttr = new ArrayList <DOMProp> ();
				allAttr.addAll(lClass.ownedAttrArr);
				allAttr.addAll(lClass.inheritedAttrArr);
				writeCSVFileLine (lClass.title, lClass.nameSpaceId, allAttr, lOtherLanguage, prCSVAttr);
				//  System.out.println("in WriteDOMCSV done write FIleLine");
			}
		}
		// write any singleton attributes
		ArrayList <DOMProp> lEnumAttrArr = new ArrayList <DOMProp> ();
		
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.userSingletonClassAttrIdMap.values());
		for (Iterator <AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			
			AttrDefn lAttr = (AttrDefn) i.next();
			if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.attrNameSpaceIdNC) != 0)) continue;
			DOMProp lDOMProp = new DOMProp();
			lDOMProp.createDOMPropSingletonsNoAssoc(lAttr);
			lEnumAttrArr.add(lDOMProp);
			
		}
		if (! lEnumAttrArr.isEmpty()) {
			
//			writeCSVFileLine ("USER", lSchemaFileDefn.nameSpaceIdNC, lEnumAttrArr, lOtherLanguage, prCSVAttr);
			writeCSVFileLine (DMDocument.LDDToolSingletonClassTitle, lSchemaFileDefn.nameSpaceIdNC, lEnumAttrArr, lOtherLanguage, prCSVAttr);
		}

		
		prCSVAttr.close();
		//  System.out.println("in WriteDOMCSV done close");
	}	
	
//	write an attribute, one line
	public void writeCSVFileLine (String lClassTitle, String lClassNameSpaceId, ArrayList <DOMProp> allAttr, String lOtherLanguage, BufferedWriter prCSVAttr)  throws java.io.IOException {
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
	


		for (Iterator <DOMProp> j = allAttr.iterator(); j.hasNext();) {
			DOMProp lAttr = (DOMProp) j.next();	
			if(! lAttr.hasDOMClass.isEmpty()) {
				ISOClassOAIS11179 lISOClass = (ISOClassOAIS11179) lAttr.hasDOMClass.get(0);
				if (lISOClass instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lISOClass;
				//	  System.out.println("in WriteDOMCSV DOMAttr.title" + lDOMAttr.getTitle());
				String pMinVal = lDOMAttr.getMinimumValue(true,true);
				String pMaxVal = lDOMAttr.getMaximumValue(true,true);
				String pMinChar = lDOMAttr.getMinimumCharacters(true,true);
				String pMaxChar = lDOMAttr.getMaximumCharacters(true,true);
				
				padLength = 30 - lAttr.title.length();
				if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
				pIdentifier = attrSortField + " " + lClassNameSpaceId + lDOMAttr.getTitle() + ":1" + padding;
				valueSortField = attrSortField + " " + lClassNameSpaceId + lDOMAttr.getTitle() + ":2" + padding;
				prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Attribute" + DELM_MID +  lDOMAttr.getNameInLanguage(lOtherLanguage) + DELM_MID + "n/a" + DELM_MID + lAttr.getNameSpaceId () + DELM_MID +  lDOMAttr.getDefinitionInLanguage(lOtherLanguage) + DELM_MID + lAttr.getSteward () + DELM_MID + lDOMAttr.valueType + DELM_MID + lAttr.cardMin + DELM_MID + lAttr.cardMax + DELM_MID + pMinVal + DELM_MID + pMaxVal + DELM_MID+ pMinChar + DELM_MID + pMaxChar+ DELM_MID + lDOMAttr.getUnitOfMeasure (true) + DELM_MID + lDOMAttr.getDefaultUnitId (true) + DELM_MID + lDOMAttr.classConcept + DELM_MID + lDOMAttr.dataConcept + DELM_END + "\r\n");

				if ( ! (lDOMAttr.domPermValueArr == null || lDOMAttr.domPermValueArr.isEmpty())) {
					for (Iterator <DOMProp> k = lDOMAttr.domPermValueArr.iterator(); k.hasNext();) {
						DOMProp lDOMProp = (DOMProp) k.next();
						DOMPermValDefn lDOMPermVal  =(DOMPermValDefn) lDOMProp.hasDOMClass.get(0); 
						String lValue = lDOMPermVal.value;
						if (lValue.length() > 20) lValue = lValue.substring(0,20);
						pIdentifier = valueSortField + " Value:" + lValue;
						prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Value" + DELM_MID + lDOMPermVal.value + DELM_MID + "" + DELM_MID + "" + DELM_MID + lDOMPermVal.value_meaning + DELM_END + "\r\n");
					}
				}
			}
			}
		
		}
	}

/*	
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
	*/
}
