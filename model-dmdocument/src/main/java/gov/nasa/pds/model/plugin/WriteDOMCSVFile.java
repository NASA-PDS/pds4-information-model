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
		
		ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> (DOMInfoModel.userSingletonDOMClassAttrIdMap.values());
		for (Iterator <DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
			
			DOMAttr lAttr = (DOMAttr) i.next();
			if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.nameSpaceIdNC) != 0)) continue;
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
			DOMProp lProp = (DOMProp) j.next();	
			DOMAttr lDOMAttr = (DOMAttr)lProp.hasDOMObject;
				 
		 //	  System.out.println("in WriteDOMCSV DOMAttr.title" + lDOMAttr.getTitle());
			String pMinVal = lDOMAttr.getMinimumValue(true,true);
		        String pMaxVal = lDOMAttr.getMaximumValue(true,true);
		        String pMinChar = lDOMAttr.getMinimumCharacters(true,true);
			String pMaxChar = lDOMAttr.getMaximumCharacters(true,true);
				
			padLength = 30 - lDOMAttr.getTitle().length();
			if (padLength < 0) padLength = 0;
				padding = blanks.substring(0, padLength);
					
			pIdentifier = attrSortField + " " + lDOMAttr.getNameSpaceId() + lDOMAttr.getTitle() + ":1" + padding;
			    	
		        valueSortField = attrSortField + " " + lDOMAttr.getNameSpaceId() + lDOMAttr.getTitle() + ":2" + padding;
			prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Attribute" + DELM_MID +  lDOMAttr.getNameInLanguage(lOtherLanguage) + DELM_MID + "n/a" + DELM_MID + lProp.getNameSpaceIdNC () + DELM_MID +  lDOMAttr.getDefinitionInLanguage(lOtherLanguage) + DELM_MID + lProp.getSteward () + DELM_MID + lDOMAttr.valueType + DELM_MID + lProp.cardMin + DELM_MID + lProp.cardMax + DELM_MID + pMinVal + DELM_MID + pMaxVal + DELM_MID+ pMinChar + DELM_MID + pMaxChar+ DELM_MID + lDOMAttr.getUnitOfMeasure (true) + DELM_MID + lDOMAttr.getDefaultUnitId (true) + DELM_MID + lDOMAttr.classConcept + DELM_MID + lDOMAttr.dataConcept + DELM_END + "\r\n");
			
			ArrayList<ISOClassOAIS11179> lValArr = lDOMAttr.hasDOMObject;
		
			//		System.out.println("getting values - size is "+ lValClassArr.size());
			Iterator <ISOClassOAIS11179> k = lValArr.iterator();		
			String value;
			//	    System.out.println("Attr - "+ lProp.identifier+ "--" + lProp.classNameSpaceIdNC);
			while (k.hasNext()) {					   
				DOMProp lDOMProp = (DOMProp) k.next();					
				if  (lDOMProp.hasDOMObject instanceof DOMPermValDefn) {					  
					 DOMPermValDefn lDOMPermVal  =(DOMPermValDefn) lDOMProp.hasDOMObject; 
					 String lValue = lDOMPermVal.value;
					 if (lValue.length() > 20) lValue = lValue.substring(0,20);
					 pIdentifier = valueSortField + " Value:" + lValue;
					prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Value" + DELM_MID + lDOMPermVal.value + DELM_MID + "" + DELM_MID + "" + DELM_MID + lDOMPermVal.value_meaning + DELM_END + "\r\n");
				}
			}
		}		
		
	}


}
