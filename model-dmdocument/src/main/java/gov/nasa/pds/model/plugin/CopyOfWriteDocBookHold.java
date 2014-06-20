package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

//class WriteDocBook extends ISO11179MDR{
class CopyOfWriteDocBookHold extends Object {
	
	int docLevel = 1;
	
	String wikiH1, wikiH2, wikiH3;
	String wikiBold;
	String dataTypePageName;
	
	ArrayList <String> adminRecUsedArr = new ArrayList <String> ();
	ArrayList <String> adminRecTitleArr = new ArrayList <String> ();
	
	public CopyOfWriteDocBookHold () {
		wikiH1 = "h4. ";
		wikiH2 = "h5. ";
		wikiH3 = "h6. ";
		wikiBold = "*";
		dataTypePageName = "";
		return;
	}
	
//	print DocBook File
	public void writeDocBook () throws java.io.IOException {
//		PrintWriter prDocBook = new PrintWriter(new FileWriter("DocBook/" + "PDS4IMDocBook.xml", false));
		File targetDir = new File(DMDocument.outputDirPath + "DocBook");
		targetDir.mkdirs();
		PrintWriter prDocBook = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "DocBook/" + "PDS4IMDocBook.xml", false));
		writeDBHdr (docLevel, prDocBook);
		docLevel++;
		writeDBAttrSection (docLevel, prDocBook);
		docLevel--;
		writeDBFtr (docLevel, prDocBook);
		prDocBook.close();
		return;
	}
	
	private void writeDBAttrSection (int docLevel, PrintWriter prDocBook) {
		writeLine (docLevel,"<section>", prDocBook);
		docLevel++;
		writeLine (docLevel,"<title>Attributes)</title>", prDocBook);
		docLevel++;
		writeAttrTbl (docLevel, prDocBook);
		docLevel--;
		docLevel--;
		writeLine (docLevel,"</section>", prDocBook);
	}
	
	/**
	* write the attribute table
	*/
	private void writeAttrTbl (int docLevel, PrintWriter prDocBook) {
		writeLine (docLevel,  "<informaltable frame=\"all\" colsep=\"1\">", prDocBook);
		docLevel++;
		writeLine (docLevel,  "<tgroup cols=\"20\">", prDocBook);
		docLevel++;
		writeLine (docLevel,  "<colspec colname = \"Name\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Namespace Id\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Version Id\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Description\" colwidth=\"2*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Data Type\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Steward\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Class Name\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Class Namespace Id\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Minimum Characters\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Maximum Characters\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Minimum Value\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Maximum Value\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Unit of Measure\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Specified Unit\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Nillable\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Pattern\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Format\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Class Concept\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Data Concept\" colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colname = \"Permissible Values\" colwidth=\"3*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<tbody>", prDocBook);

		ArrayList <AttrDefn> sortedAttrArr = InfoModel.getAttArrByTitleStewardClassSteward ();
		for (Iterator<AttrDefn> i = sortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();			
			if (lAttr.isDataType || lAttr.isUnitOfMeasure) {
				continue;
			}
			if (! (lAttr.isUsedInModel && lAttr.isAttribute)) {
				continue;
			}
			writeAttrRow (docLevel, lAttr, prDocBook);
//			if (lAttr.title.compareTo("author_list") == 0) break;
		}
		
		writeLine (docLevel, "</tbody>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</tgroup>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</informaltable>", prDocBook);
	}

	/**
	* write the attribute row
	*/
	private void writeAttrRow (int docLevel, AttrDefn lAttr, PrintWriter prDocBook) {
		writeLine (docLevel, "<row>", prDocBook);
		docLevel++;
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.title) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.attrNameSpaceIdNC) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.versionIdentifierValue) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.description) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getValueType (true)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.steward) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.className) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.classNameSpaceIdNC) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getMinimumCharacters(true, false)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getMaximumCharacters(true, false)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getMinimumValue(true, false)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getMaximumValue(true, false)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getUnitOfMeasure (true)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getDefaultUnitId (true)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + lAttr.isNilable + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getPattern(true)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.getFormat(true)) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.classConcept) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lAttr.dataConcept) + "</entry>", prDocBook);
		
		// permissible values
		writeLine (docLevel, "<entry>" + "" + "", prDocBook);
		docLevel++;
		
		if ( ! (lAttr.permValueArr == null || lAttr.permValueArr.size() == 0)) {
			writeAttrPVTbl (docLevel, lAttr, prDocBook);
		} else {
			writeLine (docLevel, "" + "None" + "", prDocBook);
		}
		writeLine (docLevel, "" + "" + "</entry>", prDocBook);
		docLevel--;
		docLevel--;
		writeLine (docLevel, "</row>", prDocBook);
	}
	
	
	/**
	* write the permissible values
	*/
	private void writeAttrPVTbl (int docLevel, AttrDefn lAttr, PrintWriter prDocBook) {
		writeLine (docLevel,  "<informaltable frame=\"all\" colsep=\"1\">", prDocBook);
		docLevel++;
		writeLine (docLevel,  "<tgroup cols=\"2\">", prDocBook);
		docLevel++;
		writeLine (docLevel,  "<colspec colwidth=\"1*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<colspec colwidth=\"2*\" align=\"left\"/>", prDocBook);
		writeLine (docLevel,  "<tbody>", prDocBook);
		docLevel++;
		writeAttrPVRowHdr (docLevel, prDocBook);
		docLevel--;
		for (Iterator <PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
			if (lPermValueDefn.value.compareTo("...") == 0) {
				continue;
			} else {

			}
			writeAttrPVRow (docLevel, lPermValueDefn, prDocBook);
		}
		writeLine (docLevel, "</tbody>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</tgroup>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</informaltable>", prDocBook);
	}

	/**
	* write the attribute row header
	*/
	private void writeAttrPVRowHdr (int docLevel, PrintWriter prDocBook) {
		writeLine (docLevel, "<row>", prDocBook);
		docLevel++;
		writeLine (docLevel, "<entry>" + "Value" + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + "Value Meaning" + "</entry>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</row>", prDocBook);
	}
	
	/**
	* write the attribute row
	*/
	private void writeAttrPVRow (int docLevel, PermValueDefn lPermValueDefn, PrintWriter prDocBook) {
		writeLine (docLevel, "<row>", prDocBook);
		docLevel++;
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lPermValueDefn.value) + "</entry>", prDocBook);
		writeLine (docLevel, "<entry>" + InfoModel.escapeXMLChar(lPermValueDefn.value_meaning) + "</entry>", prDocBook);
		docLevel--;
		writeLine (docLevel, "</row>", prDocBook);
	}
		
	
	
	/**
	* Print the ISO Data Element - Wiki
	*/
	private void writeDBAttrxxxy (PrintWriter prDocBook) {
		String lPrintedValue;
			
		ArrayList <AttrDefn> sortedAttrArr = InfoModel.getAttArrByTitleStewardClassSteward ();
		for (Iterator<AttrDefn> i = sortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lDE = (AttrDefn) i.next();			
			
			if (lDE.isDataType || lDE.isUnitOfMeasure) {
//				System.out.println("debug printAttrISODEWiki - lDE.identifier:" + lDE.identifier);
				continue;
			}
		
					

			

			for (Iterator <PermValueDefn> j = lDE.permValueArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
				if (lPermValueDefn.value.compareTo("...") == 0) {

				} else {
					prDocBook.println("  *" + escapeDocBookChar(lPermValueDefn.value) + "*");
					if (lPermValueDefn.value_meaning.indexOf("TBD") != 0) {
						prDocBook.println("      " + escapeDocBookChar(lPermValueDefn.value_meaning));
					}
				}
			}
			if (true) {
				prDocBook.println("  *" + escapeDocBookChar("...") + "*");
				prDocBook.println("    " + escapeDocBookChar("The number of values exceeds the reasonable limit for this document."));
			}
			
			// print the extended permissible values
			if (lDE.permValueExtArr == null || lDE.permValueExtArr.isEmpty()) {
				continue;
			}
			for (Iterator <PermValueExtDefn> j = lDE.permValueExtArr.iterator(); j.hasNext();) {
				PermValueExtDefn lPermValueExt = (PermValueExtDefn) j.next();	
				if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) {
					continue;
				}
				if (lPermValueExt.permValueExtArr.size() > 1) {
					prDocBook.println("#** Extended Permissible Values for: " + lPermValueExt.xpath);						
				} else {
					prDocBook.println("#** Extended Permissible Value for: " + lPermValueExt.xpath);					
				}
				for (Iterator <PermValueDefn> k = lPermValueExt.permValueExtArr.iterator(); k.hasNext();) {
					PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
					if (lPermValueDefn.value.compareTo("...") == 0) {
					} else {
						prDocBook.println("  *" + escapeDocBookChar(lPermValueDefn.value) + "*");
						if (lPermValueDefn.value_meaning.indexOf("TBD") != 0) {
							prDocBook.println("    " + escapeDocBookChar(lPermValueDefn.value_meaning));
						}
					}
				}

			}
		}
	}
		
	/**
	* write the docbook header
	*/
	private void writeDBHdr (int docLevel, PrintWriter prDocBook) {
		prDocBook.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//		prDocBook.println("	<?oxygen RNGSchema=\"http://www.oasis-open.org/docbook/xml/5.0/rng/docbook.rng\" type=\"xml\"?>");
//		prDocBook.println("	<article xmlns=\"http://docbook.org/ns/docbook\" version=\"5.0\">");
		prDocBook.println("<article xmlns=\"http://docbook.org/ns/docbook\" version=\"5.0\" xml:lang=\"en\"");
		prDocBook.println("  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		prDocBook.println("  xsi:schemaLocation=\"http://docbook.org/ns/docbook http://www.docbook.org/xml/5.0/xsd/docbook.xsd\">");
		
		
		prDocBook.println("<title>" + DMDocument.docInfo.title + "</title>");
		prDocBook.println("<subtitle>" + DMDocument.docInfo.subTitle + "</subtitle>");
		prDocBook.println("<info>");
		prDocBook.println("  <pubdate>" +  DMDocument.sTodaysDate 	 + "</pubdate>");
		prDocBook.println("  <author><orgname>" + DMDocument.docInfo.author + "</orgname></author>");
		prDocBook.println("</info>");
	}		
	
	/**
	* write the docbook footer
	*/
	private void writeDBFtr (int docLevel, PrintWriter prDocBook) {
		prDocBook.println("</article>");
		prDocBook.close();
	}	
	
//	write the Wiki attribute index
	public void printAttributeIndex (PrintWriter prDocBook) {
		String newLineChar = "";
//		prDocBook.println("");
		prDocBook.println("----");
		prDocBook.println("{anchor:Main_Index}");
		prDocBook.println("# " + wikiH2 + "Attribute and Class Indices");
		prDocBook.println("{anchor:Attribute_Index}");
		prDocBook.println("# " + wikiH2 + "Attribute Index");
		prDocBook.println("[*A*|#A] [*B*|#B] [*C*|#C] [*D*|#D] [*E*|#E] [*F*|#F] [*G*|#G] [*H*|#H] [*I*|#I] [*J*|#J] [*K*|#K] [*L*|#L] [*M*|#M] [*N*|#N] [*O*|#O] [*P*|#P] [*Q*|#Q] [*R*|#R] [*S*|#S] [*T*|#T] [*U*|#U] [*V*|#V] [*W*|#W] [*X*|#X] [*Y*|#Y] [*Z*|#Z]");
		String indMarker = " ";
		String indTitle = "";
//		ArrayList <String> DEIdArr = getSortedDEIds();
//		for (Iterator<String> i = DEIdArr.iterator(); i.hasNext();) {
//			String deId = (String) i.next();
//			AttrDefn lDE = (AttrDefn) InfoModel.masterMOFAttrMap.get(deId); // kludge to get to compile

		ArrayList <AttrDefn> sortedAttrArr = InfoModel.getAttArrByTitleStewardClassSteward ();
		for (Iterator<AttrDefn> i = sortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lDE = (AttrDefn) i.next();
			String ltitle = lDE.title;
			String anchorLink = ltitle + "_" + lDE.steward + "_" + lDE.className + "_" + lDE.classSteward;
			String tIndMarker = ltitle.substring(0, 1);
			tIndMarker = tIndMarker.toUpperCase();
			if (tIndMarker.compareTo(indMarker) != 0) {
				indMarker = tIndMarker;
				prDocBook.print(newLineChar + "{anchor:" + escapeDocBookChar(indMarker) + "} \\\\");
				newLineChar = "\n";

			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDocBook.print(", [" + "*" + escapeDocBookChar(ltitle) + "*"  + " in " + escapeDocBookChar(lDE.classSteward) + ":" + escapeDocBookChar(lDE.className) + "|#" + escapeDocBookChar(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDocBook.print("\n#* *" + escapeDocBookChar(indTitle) + "* - [" + "*" + escapeDocBookChar(ltitle) + "*"  + " in " + escapeDocBookChar(lDE.classSteward) + ":" + escapeDocBookChar(lDE.className) + "|#" + escapeDocBookChar(anchorLink) + "]");
			}
		}
//		prDocBook.println(" ");		
	}

//	write the Wiki class index
	public void printClassIndex (PrintWriter prDocBook) {	
		String newLineChar = "";
		prDocBook.println("----");
		prDocBook.println("{anchor:Class_Index}");
		prDocBook.println("# " + wikiH2 + "Class Index");
		prDocBook.println("[*A*|#AC] [*B*|#BC] [*C*|#CC] [*D*|#DC] [*E*|#EC] [*F*|#FC] [*G*|#GC] [*H*|#HC] [*I*|#IC] [*J*|#JC] [*K*|#KC] [*L*|#LC] [*M*|#MC] [*N*|#NC] [*O*|#OC] [*P*|#PC] [*Q*|#QC] [*R*|#RC] [*S*|#SC] [*T*|#TC] [*U*|#UC] [*V*|#VC] [*W*|#WC] [*X*|#XC] [*Y*|#YC] [*Z*|#ZC]");
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
				prDocBook.print(newLineChar + "{anchor:" + escapeDocBookChar(indMarker) + "C} \\\\");
				newLineChar = "\n";
			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDocBook.print(", [*" + escapeDocBookChar(ref) + "*|#" + escapeDocBookChar(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDocBook.print("\n#* [*" + escapeDocBookChar(ref) + "*|#" + escapeDocBookChar(anchorLink) + "]");
			}
		}
	}	
	
//	write the Wiki class index
	public void printProductIndex (PrintWriter prDocBook) {	
		String newLineChar = "";
//		prDocBook.println("");
		prDocBook.println("----");
		prDocBook.println("{anchor:Product_Index}");
		prDocBook.println("# " + wikiH2 + "Product Index");
		prDocBook.println("[*A*|#AC] [*B*|#BC] [*C*|#CC] [*D*|#DC] [*E*|#EC] [*F*|#FC] [*G*|#GC] [*H*|#HC] [*I*|#IC] [*J*|#JC] [*K*|#KC] [*L*|#LC] [*M*|#MC] [*N*|#NC] [*O*|#OC] [*P*|#PC] [*Q*|#QC] [*R*|#RC] [*S*|#SC] [*T*|#TC] [*U*|#UC] [*V*|#VC] [*W*|#WC] [*X*|#XC] [*Y*|#YC] [*Z*|#ZC]");
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
				prDocBook.print(newLineChar + "{anchor:" + escapeDocBookChar(indMarker) + "C} \\\\");
				newLineChar = "\n";
			}
			if (indTitle.compareTo(ltitle) == 0) {
				prDocBook.print(", [*" + escapeDocBookChar(ref) + "*|#" + escapeDocBookChar(anchorLink) + "]");
			} else {
				indTitle = ltitle;
				prDocBook.print("\n#* [*" + escapeDocBookChar(ref) + "*|#" + escapeDocBookChar(anchorLink) + "]");
			}
		}
	}	
	
//	write the Wiki administration record reference
	
	public void printAdminRecRef (String administrationRecordValue, String registrationAuthorityIdentifierValue, String registeredByValue, String submitterValue, String stewardValue, String versionIdentifierValue, PrintWriter prDocBook) {
		String adminRecId = administrationRecordValue + registrationAuthorityIdentifierValue + registeredByValue + submitterValue + stewardValue + versionIdentifierValue;
		String adminRecTitle = administrationRecordValue;
		if (! adminRecUsedArr.contains(adminRecId)) {
			adminRecUsedArr.add(adminRecId);
			adminRecTitleArr.add(adminRecTitle);
		}
		prDocBook.println(" - administration_record: " + "[" + "" + escapeDocBookChar(adminRecTitle) + "" + "|#" + escapeDocBookChar(adminRecTitle) + "]");
	}
		
//	write the Wiki Misc
		
	public void printMiscISO (PrintWriter prDocBook) {
		
//		Print the CDs
		Set <String> set1 = InfoModel.dataConcept.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String classWord = (String) iter1.next();
			String classDefn = (String) InfoModel.dataConcept.get(classWord);
			String pClassWordId = "CD_" + classWord;
			String pClassWordTitle = classWord;
			prDocBook.println("{anchor:" + escapeDocBookChar(pClassWordId) + "}");
			prDocBook.println("#* " + wikiH2 + "*" + escapeDocBookChar(pClassWordTitle) + "*");
//			prDocBook.println("designation: " + pClassWord);
			prDocBook.println("version_identifier: *" + escapeDocBookChar(DMDocument.versionIdentifierValue) + "*");
			prDocBook.println("definition: *" + escapeDocBookChar(classDefn) + "*");
//			prDocBook.println("administration_record: " + administrationRecordValue);
		}
			
//		Print the DECs
		set1 = InfoModel.classConcept.keySet();
		iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String classWord = (String) iter1.next();
			String classDefn = (String) InfoModel.classConcept.get(classWord);
			String pClassWordId = "DEC_" + classWord;
			String pClassWordTitle = classWord;
			prDocBook.println("{anchor:" + escapeDocBookChar(pClassWordId) + "}");
			prDocBook.println("#* " + wikiH2 + "*" + escapeDocBookChar(pClassWordTitle) + "*");
//			prDocBook.println("designation: " + pClassWord);
			prDocBook.println("version_identifier: *" + escapeDocBookChar(DMDocument.versionIdentifierValue) + "*");
			prDocBook.println("definition: *" + escapeDocBookChar(classDefn) + "*");
//			prDocBook.println("administration_record: " + administrationRecordValue);
		}
			
		for (Iterator<String> i = adminRecUsedArr.iterator(); i.hasNext();) {
			Iterator<String> j = adminRecTitleArr.iterator(); j.hasNext();
			String adminId = (String) i.next();
			String adminTitle = (String) j.next ();
			prDocBook.println("{anchor:" + escapeDocBookChar(adminTitle) + "}");
			prDocBook.println("#* " + wikiH2 + "Administration Record: *" + escapeDocBookChar(adminTitle) + "*");
			prDocBook.println("administration_record: " + "[*" + escapeDocBookChar(DMDocument.administrationRecordValue) + "*|#" + escapeDocBookChar(DMDocument.administrationRecordValue) + "]");
			prDocBook.println("version_identifier: *" + escapeDocBookChar(DMDocument.versionIdentifierValue) + "*");
			prDocBook.println("steward: " + "[*" + escapeDocBookChar(DMDocument.stewardValue) + "*|#" + escapeDocBookChar(DMDocument.stewardValue) + "]");
			prDocBook.println("submitter: " + "[*" + escapeDocBookChar(DMDocument.submitterValue) + "*|#" + escapeDocBookChar(DMDocument.submitterValue) + "]");
			prDocBook.println("registered_by: " + "[*" + escapeDocBookChar(DMDocument.registeredByValue) + "*|#" + escapeDocBookChar(DMDocument.registeredByValue) + "]");
			prDocBook.println("registration_authority_identifier: " + "[*" + escapeDocBookChar(DMDocument.registrationAuthorityIdentifierValue) + "*|#" + escapeDocBookChar(DMDocument.registrationAuthorityIdentifierValue) + "]");
		}			
		
		prDocBook.println("{anchor:" + escapeDocBookChar(DMDocument.administrationRecordValue) + "}");
		prDocBook.println("#* " + wikiH2 + "*AdministrationRecord*");
		prDocBook.println("identifier: *" + escapeDocBookChar(DMDocument.administrationRecordValue) + "*");
		prDocBook.println("administrative_note: Test load from PDS4 Master Model");
		prDocBook.println("administrative_status: Final");
		prDocBook.println("change_description: *In development.");
		prDocBook.println("creation_date: *" + escapeDocBookChar(DMDocument.creationDateValue) + "*");
		prDocBook.println("effective_date: *" + escapeDocBookChar(DMDocument.creationDateValue) + "*");
		prDocBook.println("last_change_date: *" + escapeDocBookChar(DMDocument.creationDateValue) + "*");
		prDocBook.println("origin: *Planetary Data System*");
		prDocBook.println("registration_status: *Preferred*");
		prDocBook.println("unresolved_issue: *Issues still being determined.*");
		prDocBook.println("until_date: *" + escapeDocBookChar(DMDocument.futureDateValue) + "*");
		prDocBook.println("label: *" + escapeDocBookChar(DMDocument.administrationRecordValue) + "*");
		prDocBook.println("explanatory _comment: This is a test load of a ISO/IEC 11179 Data Dictionary from the PDS4 master model.");
	
		prDocBook.println("{anchor:" + escapeDocBookChar(DMDocument.stewardValue) + "}");
		prDocBook.println("#* " + wikiH2 + "*Steward*");
		prDocBook.println("identifier: " + "[*" + escapeDocBookChar(DMDocument.stewardValue) + "*|#" + escapeDocBookChar(DMDocument.stewardValue) + "]");
		prDocBook.println("label: " + "[*" + escapeDocBookChar(DMDocument.stewardValue) + "*|#" + escapeDocBookChar(DMDocument.stewardValue) + "]");
		prDocBook.println("contact: *Elizabeth_Rye*");
		prDocBook.println("organization: " + "[*" + escapeDocBookChar(DMDocument.registeredByValue) + "*|#" + escapeDocBookChar(DMDocument.registeredByValue) + "]");

		prDocBook.println("{anchor:" + DMDocument.submitterValue + "}");
		prDocBook.println("#* " + wikiH2 + "*Submitter*");
		prDocBook.println("identifier: " + "[*" + escapeDocBookChar(DMDocument.submitterValue) + "*|#" + escapeDocBookChar(DMDocument.submitterValue) + "]");
		prDocBook.println("label: " + "[*" + escapeDocBookChar(DMDocument.submitterValue) + "*|#" + escapeDocBookChar(DMDocument.submitterValue) + "]");
		prDocBook.println("contact: *Elizabeth_Rye*");
		prDocBook.println("organization: " + "[*" + escapeDocBookChar(DMDocument.registeredByValue) + "*|#" + escapeDocBookChar(DMDocument.registeredByValue) + "]");

		prDocBook.println("{anchor:" + DMDocument.registeredByValue + "}");
		prDocBook.println("#* " + wikiH2 + "*RegistrationAuthority*");
		prDocBook.println("Identifier: " + "[*" + escapeDocBookChar(DMDocument.registeredByValue) + "*|#" + escapeDocBookChar(DMDocument.registeredByValue) + "]");
		prDocBook.println("organization_mailing_address: *4800 Oak Grove Drive, Pasadena, CA 91109*");
		prDocBook.println("organization_name: *NASA Planetary Data System*");
		prDocBook.println("label: " + "[*" + escapeDocBookChar(DMDocument.registeredByValue) + "*|#" + escapeDocBookChar(DMDocument.registeredByValue) + "]");
		prDocBook.println("documentation_language_identifier: *LI_English*");
		prDocBook.println("language_used: *LI_English*");
		prDocBook.println("registrar: *PDS_Registrar*");
		prDocBook.println("registration_authority_identifier: " + "[*" + escapeDocBookChar(DMDocument.registrationAuthorityIdentifierValue) + "*|#" + escapeDocBookChar(DMDocument.registrationAuthorityIdentifierValue) + "]");

	}
	
	/**
	* escape certain characters for DocBook
	*/
	 String escapeDocBookChar (String aString) {
		String lString = aString;
//		lString = replaceString (lString, "\\", "\\\\");
		return lString;
	}	
	
		/**
		* write an indented line
		*/
		private void writeLine (int lLevel, String lLine, PrintWriter prDocBook) {
			String lBlanks = "                    ";
			int lIndent = 0;
			if (lLevel > 1) {
				if (lLevel > 10) {
					lIndent = 20;
				} else {
					lIndent = (lLevel - 1) * 2;
				}
			}
			prDocBook.println(lBlanks.substring(0, lIndent) + lLine);
		}
	
}
