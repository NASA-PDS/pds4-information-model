package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

//class WikiDataDictAttrA3 extends ISO11179MDR{
class WikiDataDictAttrA3 extends Object{
	
	// write the Data Dictionary Document Attributes, Data Types, Units, and their indexes
	
	String wikiH1, wikiH2, wikiH3;
	String wikiBold;
	String dataTypePageName;

	ArrayList <String> adminRecUsedArr = new ArrayList <String> ();
	ArrayList <String> adminRecTitleArr = new ArrayList <String> ();
	
	public WikiDataDictAttrA3 () {
		wikiH1 = "h4. ";
		wikiH2 = "h5. ";
		wikiH3 = "h6. ";
		wikiBold = "*";
		dataTypePageName = "";
		return;
	}
	
//	print the ISO 11179 DD Wiki - Abridged
//  This version of the Abridge is close to the unabridged but with Product Classes added and DCE and CD removed.
	public void printISO11179DDWiki (boolean isAbridged) throws java.io.IOException {
		File targetDir = new File(DMDocument.outputDirPath + "SchemaWikiDDA3");
		targetDir.mkdirs();
		PrintWriter prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiAttr.txt", false));
		printAttrISODEWiki (isAbridged, prDDWiki);
		printMiscISO (isAbridged, prDDWiki);
		prDDWiki.close();
		
		prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiAttrInd.txt", false));
		printAttributeIndex (prDDWiki);
		prDDWiki.close();
		
		prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiClassInd.txt", false));
		printClassIndex (prDDWiki);
		prDDWiki.close();
		
		prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiProdInd.txt", false));
		printProductIndex (prDDWiki);
		prDDWiki.close();
		
		prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiAttrIntro.txt", false));
		prDDWiki.println("----");
		prDDWiki.println("{anchor:" + "Attribute_Definitions" + "}");
		prDDWiki.println("# " + wikiH1 + "PDS4 Attribute Definitions" + " - " + InfoModel.escapeWiki(DMDocument.masterTodaysDate));
		prDDWiki.println("Generated from the PDS4 Information Model Version " + InfoModel.escapeWiki(InfoModel.ont_version_id));
		prDDWiki.close();

		prDDWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "PDS4DDWikiClassIntro.txt", false));
		prDDWiki.println("----");
		prDDWiki.println("{anchor:" + "Class_Definitions" + "}");
		prDDWiki.println("# " + wikiH1 + "PDS4 Class Definitions" + " - " + InfoModel.escapeWiki(DMDocument.masterTodaysDate));
		prDDWiki.println("Generated from the PDS4 Information Model Version " + InfoModel.escapeWiki(InfoModel.ont_version_id));
		prDDWiki.close();

		return;
	}
	
	/**
	* Print the ISO Data Element - Wiki
	*/
	private void printAttrISODEWiki (boolean isAbridged, PrintWriter prDDWiki)  throws java.io.IOException {
		String lPrintedValue;
		ArrayList <AttrDefn> sortedAttrArr = InfoModel.getAttArrByTitleStewardClassSteward ();
		for (Iterator<AttrDefn> i = sortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();	
			if (lAttr.isDataType || lAttr.isUnitOfMeasure) continue;
//			if (! (lAttr.isUsedInModel && lAttr.isAttribute)) continue;
			if (! lAttr.isAttribute) continue;
			if (! lAttr.isUsedInModel) continue;
			if (lAttr.title.compareTo("%3ANAME") == 0) continue;
			
			String ltitle = lAttr.title;
			String anchorLink = ltitle + "_" + lAttr.steward + "_" + lAttr.className + "_" + lAttr.classSteward;
			String decId = lAttr.classConcept;
			if (decId == null) {
				decId = "None";
			}			
			prDDWiki.println("{anchor:" + InfoModel.escapeWiki(anchorLink) + "}");
			prDDWiki.println("#* " + wikiH2 + InfoModel.escapeWiki(ltitle) + " in " + InfoModel.escapeWiki(lAttr.className));
			prDDWiki.println("steward: *" + InfoModel.escapeWiki(lAttr.steward) + "*");
			prDDWiki.println("namespace id: *" + InfoModel.escapeWiki(lAttr.attrNameSpaceIdNC) + "*");
			prDDWiki.println("class: *" + InfoModel.escapeWiki(lAttr.className) + "*");
			prDDWiki.println("version: *" + InfoModel.escapeWiki(lAttr.versionIdentifierValue) + "*");
			if (! isAbridged) {
//				prDDWiki.print("#** *data element* - data element concept:[*" + InfoModel.escapeWiki(decId) + "*|#" + InfoModel.escapeWiki(decId) + "]");
				prDDWiki.print("#** *data element* - attribute concept:[*" + InfoModel.escapeWiki(decId) + "*|#" + InfoModel.escapeWiki(decId) + "]");
				printAdminRecRef (lAttr.administrationRecordValue, lAttr.registrationAuthorityIdentifierValue, lAttr.registeredByValue, lAttr.submitter, lAttr.steward, lAttr.versionIdentifierValue, prDDWiki);
			}

			prDDWiki.println("#** definition: *" + InfoModel.escapeWiki(lAttr.description) + "*");
			if (! isAbridged) {
				prDDWiki.print("#** *value domain* - conceptual domain:[*" + InfoModel.escapeWiki(lAttr.dataConcept) + "*|#" + InfoModel.escapeWiki(lAttr.dataConcept) + "]");
				printAdminRecRef (lAttr.administrationRecordValue, lAttr.registrationAuthorityIdentifierValue, lAttr.registeredByValue, lAttr.submitter, lAttr.steward, lAttr.versionIdentifierValue, prDDWiki);
			}
			String pEnumerated = "";
			if (lAttr.isEnumerated) {
				pEnumerated = " - Enumerated";
			}
			
			lPrintedValue = InfoModel.escapeWiki(lAttr.getValueType (true));
			prDDWiki.println("#** value_data_type: [*" + lPrintedValue + "*|" + InfoModel.escapeWiki(dataTypePageName) + "#" + lPrintedValue + "]" + InfoModel.escapeWiki(pEnumerated));

			lPrintedValue = lAttr.getMinimumValue (true, true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** minimum_value: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getMaximumValue (true, true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** maximum_value: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getMinimumCharacters (true, true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** minimum_characters: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getMaximumCharacters (true, true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** maximum_characters: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getUnitOfMeasure (true);
			if (! ((lPrintedValue.indexOf("TBD") == 0) || (lPrintedValue.compareTo("Units_of_None") == 0))) {		
				prDDWiki.println("#** unit_of_measure_type: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}
			lPrintedValue = lAttr.getUnits (false);
			if (lPrintedValue != null) {		
//				prDDWiki.println("#** unit: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
				prDDWiki.println("#** valid units: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getDefaultUnitId (true);
			if (! ((lPrintedValue.indexOf("TBD") == 0) || (lPrintedValue.compareTo("none") == 0))) {		
				prDDWiki.println("#** specified_unit_id: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}			
			lPrintedValue = lAttr.getPattern(true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** pattern: *" + InfoModel.escapeWiki(InfoModel.unEscapeProtegeString(lPrintedValue)) + "*");
			}			
			lPrintedValue = lAttr.getFormat(true);
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** format: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}		
			lPrintedValue = "false";
			if (lAttr.isNilable) {
				lPrintedValue = "true";	
			}
			if (! (lPrintedValue.indexOf("TBD") == 0)) {		
				prDDWiki.println("#** nillable: *" + InfoModel.escapeWiki(lPrintedValue) + "*");
			}
			// print the permissible values
			if (! (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty())) {				
				writePermissibleValues (lAttr, prDDWiki);
			}
						
			// print the extended permissible values
			if (! (lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty())) {			
				writePermissibleValuesExt (lAttr, prDDWiki);
			}

			// print the schematron rules
			printAttrSchematronRuleMsg(lAttr, prDDWiki);
		}
	}

	// print the permissible values
	private void writePermissibleValues (AttrDefn lAttr, PrintWriter prDDWiki)  throws java.io.IOException {
		boolean elipflag = false;

		if (lAttr.permValueArr.size() > 1) {
			prDDWiki.println("#** permissible values");						
		} else {
			prDDWiki.println("#** permissible value");
		}
		for (Iterator <PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
			if (lPermValueDefn.value.compareTo("...") == 0) {
				elipflag = true;
			} else {
				if (lPermValueDefn.value_meaning.indexOf("TBD") == 0) {
					prDDWiki.println("  *" + InfoModel.escapeWiki(lPermValueDefn.value) + "*");
				} else {
					prDDWiki.println("  *" + InfoModel.escapeWiki(lPermValueDefn.value) + "*" + " - " + InfoModel.escapeWiki(lPermValueDefn.value_meaning));					
				}
			}
		}
		if (elipflag) {
			prDDWiki.println("  *" + InfoModel.escapeWiki("...") + "*");
			prDDWiki.println("    " + InfoModel.escapeWiki("The number of values exceeds the reasonable limit for this document."));
		}
	}
	
	// print the extended permissible values
	private void writePermissibleValuesExt (AttrDefn lAttr, PrintWriter prDDWiki)  throws java.io.IOException {
		boolean elipflag = false;
		for (Iterator <PermValueExtDefn> j = lAttr.permValueExtArr.iterator(); j.hasNext();) {
			PermValueExtDefn lPermValueExt = (PermValueExtDefn) j.next();	
			if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) {
				continue;
			}
			if (lPermValueExt.permValueExtArr.size() > 1) {
				prDDWiki.println("#** Extended Permissible Values for: " + lPermValueExt.xpath);						
			} else {
				prDDWiki.println("#** Extended Permissible Value for: " + lPermValueExt.xpath);					
			}
			for (Iterator <PermValueDefn> k = lPermValueExt.permValueExtArr.iterator(); k.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
				if (lPermValueDefn.value.compareTo("...") == 0) {
					elipflag = true;
				} else {
//					prDDWiki.println("  *" + InfoModel.escapeWiki(lPermValueDefn.value) + "*");
//					if (lPermValueDefn.value_meaning.indexOf("TBD") != 0) {
//						prDDWiki.println("    " + InfoModel.escapeWiki(lPermValueDefn.value_meaning));
//					}				
					if (lPermValueDefn.value_meaning.indexOf("TBD") == 0) {
						prDDWiki.println("  *" + InfoModel.escapeWiki(lPermValueDefn.value) + "*");
					} else {
						prDDWiki.println("  *" + InfoModel.escapeWiki(lPermValueDefn.value) + "*" + " - " + InfoModel.escapeWiki(lPermValueDefn.value_meaning));					
					}
				}
			}
			if (elipflag) {
				prDDWiki.println("  *" + InfoModel.escapeWiki("...") + "*");
				prDDWiki.println("    " + InfoModel.escapeWiki("The number of values exceeds the reasonable limit for this document."));
			}
		}
	}
	
	/**
	*  Print schematron rule for the attribute
	*/
	private void printAttrSchematronRuleMsg(AttrDefn lAttr, PrintWriter prDDWiki) {
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			for (Iterator <AssertDefn2> j = lRule.assertArr.iterator(); j.hasNext();) {
				AssertDefn2 lAssert = (AssertDefn2) j.next();
				if (! ((lRule.classTitle.compareTo(lAttr.className) == 0) && lAssert.attrTitle.compareTo(lAttr.title) == 0)) continue;
				if (lAssert.assertMsg.indexOf("TBD") == 0) continue;				
				if (lAssert.specMesg.indexOf("TBD") == 0) continue;				
				if (lAssert.assertType.compareTo("RAW") != 0) continue;
//				prDDWiki.println("#** schematron rule: *" + InfoModel.escapeWiki(lAssert.assertMsg) + "*");
				prDDWiki.println("#** schematron rule: *" + InfoModel.escapeWiki(lAssert.specMesg) + "*");
			}
		} 
	}
	
//	write the Wiki attribute index
	public void printAttributeIndex (PrintWriter prDDWiki) {
		String newLineChar = "";
//		prDDWiki.println("");
		prDDWiki.println("----");
		prDDWiki.println("{anchor:Main_Index}");
		prDDWiki.println("# " + wikiH2 + "Attribute and Class Indices");
		prDDWiki.println("{anchor:Attribute_Index}");
		prDDWiki.println("# " + wikiH2 + "Attribute Index");
		prDDWiki.println("[*A*|#A] [*B*|#B] [*C*|#C] [*D*|#D] [*E*|#E] [*F*|#F] [*G*|#G] [*H*|#H] [*I*|#I] [*J*|#J] [*K*|#K] [*L*|#L] [*M*|#M] [*N*|#N] [*O*|#O] [*P*|#P] [*Q*|#Q] [*R*|#R] [*S*|#S] [*T*|#T] [*U*|#U] [*V*|#V] [*W*|#W] [*X*|#X] [*Y*|#Y] [*Z*|#Z]");
		String indMarker = " ";
		String indTitle = "";
		
		
		ArrayList <AttrDefn> sortedAttrArr = InfoModel.getAttArrByTitleStewardClassSteward ();
		for (Iterator<AttrDefn> i = sortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isDataType || lAttr.isUnitOfMeasure) continue;
//			if (! (lAttr.isUsedInModel && lAttr.isAttribute)) continue;
			if (! lAttr.isAttribute) continue;
			if (! lAttr.isUsedInModel) continue;
			if (lAttr.title.compareTo("%3ANAME") == 0) continue;			
			String ltitle = lAttr.title;
			String anchorLink = ltitle + "_" + lAttr.steward + "_" + lAttr.className + "_" + lAttr.classSteward;
			String tIndMarker = ltitle.substring(0, 1);
			tIndMarker = tIndMarker.toUpperCase();
			if (tIndMarker.compareTo(indMarker) != 0) {
				indMarker = tIndMarker;
				prDDWiki.print(newLineChar + "{anchor:" + InfoModel.escapeWiki(indMarker) + "} \\\\");
				newLineChar = "\n";

			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDDWiki.print(", [" + "*" + InfoModel.escapeWiki(ltitle) + "*"  + " in " + InfoModel.escapeWiki(lAttr.classSteward) + ":" + InfoModel.escapeWiki(lAttr.className) + "|#" + InfoModel.escapeWiki(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDDWiki.print("\n#* *" + InfoModel.escapeWiki(indTitle) + "* - [" + "*" + InfoModel.escapeWiki(ltitle) + "*"  + " in " + InfoModel.escapeWiki(lAttr.classSteward) + ":" + InfoModel.escapeWiki(lAttr.className) + "|#" + InfoModel.escapeWiki(anchorLink) + "]");
			}
		}
//		prDDWiki.println(" ");		
	}

//	write the Wiki class index
	public void printClassIndex (PrintWriter prDDWiki) {	
		String newLineChar = "";
		prDDWiki.println("----");
		prDDWiki.println("{anchor:Class_Index}");
		prDDWiki.println("# " + wikiH2 + "Class Index");
		prDDWiki.println("[*A*|#AC] [*B*|#BC] [*C*|#CC] [*D*|#DC] [*E*|#EC] [*F*|#FC] [*G*|#GC] [*H*|#HC] [*I*|#IC] [*J*|#JC] [*K*|#KC] [*L*|#LC] [*M*|#MC] [*N*|#NC] [*O*|#OC] [*P*|#PC] [*Q*|#QC] [*R*|#RC] [*S*|#SC] [*T*|#TC] [*U*|#UC] [*V*|#VC] [*W*|#WC] [*X*|#XC] [*Y*|#YC] [*Z*|#ZC]");
		String indMarker = " ";
		String indTitle = "";
		Set <String> set1 = InfoModel.masterMOFClassTitleMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lId);
//			if (! (lClass.isSchema1Class || lClass.isSchema2Class)) { continue; };
			if (! (lClass.isSchema1Class)) { continue; };
			String ltitle = lClass.title;
			String anchorLink = ltitle + "_" + lClass.steward;
			String ref = ltitle;
			String tIndMarker = ltitle.substring(0, 1);
			tIndMarker = tIndMarker.toUpperCase();
			if (tIndMarker.compareTo(indMarker) != 0) {
				indMarker = tIndMarker;
				prDDWiki.print(newLineChar + "{anchor:" + InfoModel.escapeWiki(indMarker) + "C} \\\\");
				newLineChar = "\n";
			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDDWiki.print(", [*" + InfoModel.escapeWiki(ref) + "*|#" + InfoModel.escapeWiki(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDDWiki.print("\n#* [*" + InfoModel.escapeWiki(ref) + "*|#" + InfoModel.escapeWiki(anchorLink) + "]");
			}
		}
	}	
	
//	write the Wiki class index
	public void printProductIndex (PrintWriter prDDWiki) {	
		String newLineChar = "";
//		prDDWiki.println("");
		prDDWiki.println("----");
		prDDWiki.println("{anchor:Product_Index}");
		prDDWiki.println("# " + wikiH2 + "Product Index");
		prDDWiki.println("[*A*|#AC] [*B*|#BC] [*C*|#CC] [*D*|#DC] [*E*|#EC] [*F*|#FC] [*G*|#GC] [*H*|#HC] [*I*|#IC] [*J*|#JC] [*K*|#KC] [*L*|#LC] [*M*|#MC] [*N*|#NC] [*O*|#OC] [*P*|#PC] [*Q*|#QC] [*R*|#RC] [*S*|#SC] [*T*|#TC] [*U*|#UC] [*V*|#VC] [*W*|#WC] [*X*|#XC] [*Y*|#YC] [*Z*|#ZC]");
		String indMarker = " ";
		String indTitle = "";
		Set <String> set1 = InfoModel.masterMOFClassTitleMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lId);
//			if (! (lClass.isSchema1Class || lClass.isSchema2Class)) { continue; };
			if (! (lClass.isSchema1Class)) { continue; };
			if (! (lClass.isRegistryClass)) { continue; };
			String ltitle = lClass.title;
			String anchorLink = ltitle + "_" + lClass.steward;
			String ref = ltitle;
			String tIndMarker = ltitle.substring(0, 1);
			tIndMarker = tIndMarker.toUpperCase();
			if (tIndMarker.compareTo(indMarker) != 0) {
				indMarker = tIndMarker;
				prDDWiki.print(newLineChar + "{anchor:" + InfoModel.escapeWiki(indMarker) + "C} \\\\");
				newLineChar = "\n";
			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDDWiki.print(", [*" + InfoModel.escapeWiki(ref) + "*|#" + InfoModel.escapeWiki(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDDWiki.print("\n#* [*" + InfoModel.escapeWiki(ref) + "*|#" + InfoModel.escapeWiki(anchorLink) + "]");
			}
		}
	}	
	
//	write the Wiki administration record reference
	
	public void printAdminRecRef (String administrationRecordValue, String registrationAuthorityIdentifierValue, String registeredByValue, String submitterValue, String stewardValue, String versionIdentifierValue, PrintWriter prDDWiki) {
		String adminRecId = administrationRecordValue + registrationAuthorityIdentifierValue + registeredByValue + submitterValue + stewardValue + versionIdentifierValue;
		String adminRecTitle = administrationRecordValue;
		if (! adminRecUsedArr.contains(adminRecId)) {
			adminRecUsedArr.add(adminRecId);
			adminRecTitleArr.add(adminRecTitle);
		}
		prDDWiki.println(" - administration_record: " + "[" + "" + InfoModel.escapeWiki(adminRecTitle) + "" + "|#" + InfoModel.escapeWiki(adminRecTitle) + "]");
	}
		
//	write the Wiki Misc
		
	public void printMiscISO (boolean isAbridged, PrintWriter prDDWiki) {
		
		if (! isAbridged) {
//			Print the CDs
			Set <String> set1 = InfoModel.dataConcept.keySet();
			Iterator <String> iter1 = set1.iterator();
			while(iter1.hasNext()) {
				String classWord = (String) iter1.next();
				String classDefn = (String) InfoModel.dataConcept.get(classWord);
				String pClassWordId = "CD_" + classWord;
				String pClassWordTitle = classWord;
				prDDWiki.println("{anchor:" + InfoModel.escapeWiki(pClassWordId) + "}");
				prDDWiki.println("#* " + wikiH2 + "*" + InfoModel.escapeWiki(pClassWordTitle) + "*");
				prDDWiki.println("version_identifier: *" + InfoModel.escapeWiki(DMDocument.versionIdentifierValue) + "*");
				prDDWiki.println("definition: *" + InfoModel.escapeWiki(classDefn) + "*");
			}
		}
			
		if (! isAbridged) {
			//		Print the DECs
			Set set1 = InfoModel.classConcept.keySet();
			Iterator iter1 = set1.iterator();
			while(iter1.hasNext()) {
				String classWord = (String) iter1.next();
				String classDefn = (String) InfoModel.classConcept.get(classWord);
				String pClassWordId = "DEC_" + classWord;
				String pClassWordTitle = classWord;
				prDDWiki.println("{anchor:" + InfoModel.escapeWiki(pClassWordId) + "}");
				prDDWiki.println("#* " + wikiH2 + "*" + InfoModel.escapeWiki(pClassWordTitle) + "*");
				prDDWiki.println("version_identifier: *" + InfoModel.escapeWiki(DMDocument.versionIdentifierValue) + "*");
				prDDWiki.println("definition: *" + InfoModel.escapeWiki(classDefn) + "*");
			}
		}
			
		for (Iterator<String> i = adminRecUsedArr.iterator(); i.hasNext();) {
			Iterator<String> j = adminRecTitleArr.iterator(); j.hasNext();
			String adminId = (String) i.next();
			String adminTitle = (String) j.next ();
			prDDWiki.println("{anchor:" + InfoModel.escapeWiki(adminTitle) + "}");
			prDDWiki.println("#* " + wikiH2 + "Administration Record: *" + InfoModel.escapeWiki(adminTitle) + "*");
			prDDWiki.println("administration_record: " + "[*" + InfoModel.escapeWiki(DMDocument.administrationRecordValue) + "*|#" + InfoModel.escapeWiki(DMDocument.administrationRecordValue) + "]");
			prDDWiki.println("version_identifier: *" + InfoModel.escapeWiki(DMDocument.versionIdentifierValue) + "*");
			prDDWiki.println("steward: " + "[*" + InfoModel.escapeWiki(DMDocument.stewardValue) + "*|#" + InfoModel.escapeWiki(DMDocument.stewardValue) + "]");
			prDDWiki.println("submitter: " + "[*" + InfoModel.escapeWiki(DMDocument.submitterValue) + "*|#" + InfoModel.escapeWiki(DMDocument.submitterValue) + "]");
			prDDWiki.println("registered_by: " + "[*" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "]");
			prDDWiki.println("registration_authority_identifier: " + "[*" + InfoModel.escapeWiki(DMDocument.registrationAuthorityIdentifierValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registrationAuthorityIdentifierValue) + "]");
		}			
		
		prDDWiki.println("{anchor:" + InfoModel.escapeWiki(DMDocument.administrationRecordValue) + "}");
		prDDWiki.println("#* " + wikiH2 + "*AdministrationRecord*");
		prDDWiki.println("identifier: *" + InfoModel.escapeWiki(DMDocument.administrationRecordValue) + "*");
		prDDWiki.println("administrative_note: Loaded from PDS4 Information Model");
		prDDWiki.println("administrative_status: Final");
		prDDWiki.println("change_description: *In development.");
		prDDWiki.println("creation_date: *" + InfoModel.escapeWiki(DMDocument.creationDateValue) + "*");
		prDDWiki.println("effective_date: *" + InfoModel.escapeWiki(DMDocument.creationDateValue) + "*");
		prDDWiki.println("last_change_date: *" + InfoModel.escapeWiki(DMDocument.creationDateValue) + "*");
		prDDWiki.println("origin: *Planetary Data System*");
		prDDWiki.println("registration_status: *Preferred*");
		prDDWiki.println("unresolved_issue: *Issues still being determined.*");
		prDDWiki.println("until_date: *" + InfoModel.escapeWiki(DMDocument.futureDateValue) + "*");
		prDDWiki.println("label: *" + InfoModel.escapeWiki(DMDocument.administrationRecordValue) + "*");
		prDDWiki.println("explanatory _comment: This is a test load of a ISO/IEC 11179 Data Dictionary from the PDS4 master model.");
	
		prDDWiki.println("{anchor:" + InfoModel.escapeWiki(DMDocument.stewardValue) + "}");
		prDDWiki.println("#* " + wikiH2 + "*Steward*");
		prDDWiki.println("identifier: " + "[*" + InfoModel.escapeWiki(DMDocument.stewardValue) + "*|#" + InfoModel.escapeWiki(DMDocument.stewardValue) + "]");
		prDDWiki.println("label: " + "[*" + InfoModel.escapeWiki(DMDocument.stewardValue) + "*|#" + InfoModel.escapeWiki(DMDocument.stewardValue) + "]");
		prDDWiki.println("contact: *Elizabeth_Rye*");
		prDDWiki.println("organization: " + "[*" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "]");

		prDDWiki.println("{anchor:" + DMDocument.submitterValue + "}");
		prDDWiki.println("#* " + wikiH2 + "*Submitter*");
		prDDWiki.println("identifier: " + "[*" + InfoModel.escapeWiki(DMDocument.submitterValue) + "*|#" + InfoModel.escapeWiki(DMDocument.submitterValue) + "]");
		prDDWiki.println("label: " + "[*" + InfoModel.escapeWiki(DMDocument.submitterValue) + "*|#" + InfoModel.escapeWiki(DMDocument.submitterValue) + "]");
		prDDWiki.println("contact: *Elizabeth_Rye*");
		prDDWiki.println("organization: " + "[*" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "]");

		prDDWiki.println("{anchor:" + DMDocument.registeredByValue + "}");
		prDDWiki.println("#* " + wikiH2 + "*RegistrationAuthority*");
		prDDWiki.println("Identifier: " + "[*" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "]");
		prDDWiki.println("organization_mailing_address: *4800 Oak Grove Drive, Pasadena, CA 91109*");
		prDDWiki.println("organization_name: *NASA Planetary Data System*");
		prDDWiki.println("label: " + "[*" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registeredByValue) + "]");
		prDDWiki.println("documentation_language_identifier: *LI_English*");
		prDDWiki.println("language_used: *LI_English*");
		prDDWiki.println("registrar: *PDS_Registrar*");
		prDDWiki.println("registration_authority_identifier: " + "[*" + InfoModel.escapeWiki(DMDocument.registrationAuthorityIdentifierValue) + "*|#" + InfoModel.escapeWiki(DMDocument.registrationAuthorityIdentifierValue) + "]");

	}
}
