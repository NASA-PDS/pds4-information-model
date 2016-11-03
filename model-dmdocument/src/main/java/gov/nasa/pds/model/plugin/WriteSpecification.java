package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/** Driver for getting document
 *
 */
public class WriteSpecification extends Object {
	
	DocDefn docInfo;

	ArrayList <String> begSectionFormats;
	ArrayList <String> endSectionFormats;
	ArrayList <String> emwfHTML;
	
	boolean includeAllAttrFlag = false;
	boolean PDSOptionalFlag = false;
	boolean lDebugPrint = false;
	int figNum;
	Date rTodaysDate;
	String sTodaysDate;
	ArrayList <String> itemNum;
	
	PrintWriter prhtml;
	
	public WriteSpecification (DocDefn lDocInfo, boolean lflag) {
		docInfo = lDocInfo;
		PDSOptionalFlag = lflag;
		
		//	set up the HTML Headers
		begSectionFormats = new ArrayList <String>();
		begSectionFormats.add("<H2>");
		begSectionFormats.add("<H3>");
		begSectionFormats.add("<H4>");

		endSectionFormats = new ArrayList <String>();
		endSectionFormats.add("</H2>");
		endSectionFormats.add("</H3>");
		endSectionFormats.add("</H4>");
				
		emwfHTML = new ArrayList <String>();
		emwfHTML.add("<center> <i><b>i<sub>x</sub>A</b>cos(&omega;t-<b>kr</b>-&phi;</i>) </center>");
		emwfHTML.add("<i><b>i<sub>x</sub></b></i>");
		emwfHTML.add("<i>A</i>");
		emwfHTML.add("<i>&omega;</i>");
		emwfHTML.add("<i><b>k</b></i>");
		emwfHTML.add("<i>&phi;</i>");
				
		figNum = 0;
		rTodaysDate = new Date();
		sTodaysDate  = rTodaysDate.toString();
		itemNum = new ArrayList <String> ();
	}
	
/**********************************************************************************************************
		print the various artifacts
***********************************************************************************************************/

	public void printArtifacts () throws java.io.IOException {
		String phtitle;
		
//		FileWriter fw = new FileWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//		To specify these values yourself, construct an OutputStreamWriter on a FileOutputStream.	    

	    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelSpec;
	    prhtml = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
	    
//		Start writing the html document                
//		System.out.println("debug - writing html document");
	    
//	    String documentTitle = "PDS4 Information Model Specification";
//	    String documentAuthor = "PDS4 Information Model Specification Team";
//	    String documentSubTitle = "";
		
		prhtml.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");
		prhtml.println("<html>");
		prhtml.println("<head>");
//		prhtml.println("<title>" + documentTitle + "</title>");
		prhtml.println("<title>" + DMDocument.imSpecDocTitle + "</title>");
		prhtml.println("<p align=center>");
//		prhtml.println("<h1 align=center>" + documentTitle + "</h1><br><br>");
		prhtml.println("<h1 align=center>" + DMDocument.imSpecDocTitle + "</h1><br><br>");
//		prhtml.println("<h2 align=center>" + documentAuthor + "</h2><br>");
		prhtml.println("<h2 align=center>" + DMDocument.imSpecDocAuthor + "</h2><br>");
		prhtml.println("<h2 align=center>" + sTodaysDate + "</h2><br><br><br>");
//		prhtml.println("<h2 align=center>" + documentSubTitle + "</h2><br>");
		prhtml.println("<h2 align=center>" + DMDocument.imSpecDocSubTitle + "</h2><br>");
		prhtml.println("<h2 align=center>" + "Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</h2><br>");
		prhtml.println("</p>");
		prhtml.println("</head>");
		prhtml.println("<body bgcolor=#FFFFF0>");

//	Print the *** TABLE OF CONTENTS *** for the HTML file
		prhtml.println("<p><h2>" + "Table of Contents" + "</h2></p>");
		prhtml.println("<ul>");
		
// 		get each section		
		Collections.sort(docInfo.sectionArray);
		itemNumAdd(itemNum);
		for (Iterator <String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
			String secId = (String) i.next();
			SectionDefn secInfo = (SectionDefn) docInfo.sectionMap.get(secId);
			if (! secInfo.includeFlag) { continue; }
			if (secInfo.secTOCFlag) {
				itemNumPlus(itemNum);			
			}
			if (secInfo.secType.compareTo("text") == 0) {
				if (secInfo.secTOCFlag) {
					phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "</a>";
					prhtml.println("<b>"+ phtitle + "</b><br>");
				}
			} else if (secInfo.secType.compareTo("model") == 0) {
				if (secInfo.secTOCFlag) {
					phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "</a>";
					prhtml.println("<b>"+ phtitle + "</b><br>");
				}
				if (secInfo.secSubType.compareTo("table") == 0) {
					if (secInfo.subSecTOCFlag) {
						printModelTOC(getPrintClassArr(secInfo), secInfo);
					}
				}
			}
		}
		itemNumRemove(itemNum);
		
//	Print the *** Document Contents ***		
//	iterate through sections and print them 	
		itemNum.clear();
		itemNumAdd(itemNum);
		for (Iterator <String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
			String secId = (String) i.next();
			SectionDefn secInfo = (SectionDefn) docInfo.sectionMap.get(secId);
			if (! secInfo.includeFlag) { continue; }
			if (secInfo.secTOCFlag) {
				itemNumPlus(itemNum);
			}
			if (secInfo.secType.compareTo("text") == 0) {
				printStandardSection (secInfo, itemNum);
			} else if (secInfo.secType.compareTo("model") == 0) {
				printStandardSection (secInfo, itemNum);
				if (secInfo.secSubType.compareTo("table") == 0) {
					printModelTable(getPrintClassArr(secInfo), secInfo, itemNum);
				} else if (secInfo.secSubType.compareTo("hierarchy") == 0) {
					printClassHierarchySection (getPrintClassArr(secInfo), secInfo);
				} else if (secInfo.secSubType.compareTo("datadictionary") == 0) {
					printDataDict();
				} else if (secInfo.secSubType.compareTo("glossary") == 0) {
					printGlossary(secInfo);
				}
			}
		}					
		itemNumRemove(itemNum);		
		prhtml.println("</body>");
		prhtml.println("</html>");
		prhtml.close();
	}
	
/**********************************************************************************************************
		routines for printing the model
***********************************************************************************************************/

	/**
		*  Print standard section
		*/
	public void printStandardSection (SectionDefn imsec, ArrayList <String> itemNum) {
// 		print the html		
		if (imsec.secTOCFlag) {
			String phtitle = "<a name=\"" + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "</a>";
			prhtml.println(begSectionFormats.get(imsec.texFormatInd) + phtitle + endSectionFormats.get(imsec.texFormatInd) );
		}
		String phdesc = replaceString (imsec.description, "\\newline", "<p>");			
		prhtml.println("<p>" + insertEMWF(phdesc, emwfHTML) + "</p>");

//		insert the graphic diagram if one exists
		if (imsec.imageFlag) {
			prhtml.println("<p><img align=bottom src=\"" + imsec.imageFileName + ".jpg\">");
			figNum++;
			prhtml.println("<p><b>Figure " + figNum + "&nbsp;&nbsp;" + imsec.imageCaption + "</b></p>");
		}
	}
		
	/**
		*  Print Class Hierarchy
		*/
	
	public void printClassHierarchySection (ArrayList <PDSObjDefn> printClassArr, SectionDefn secInfo) {
		String concatClassName;
		int numclass = 0;

		HashMap <String, String> objHierArray = new HashMap <String, String> ();
		HashMap <String, Integer> classParentCountMap = new HashMap <String, Integer> ();
		ArrayList <String> concatNameArray = new ArrayList <String> ();
		ArrayList <String> accumClassNameArr;
		for (Iterator <PDSObjDefn> i = printClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();	
			accumClassNameArr = new ArrayList <String> ();
			for (Iterator <PDSObjDefn> j = lClass.superClass.iterator(); j.hasNext();) {
				PDSObjDefn locClass2 = (PDSObjDefn) j.next();
				accumClassNameArr.add(locClass2.title);
			}
			accumClassNameArr.add(lClass.title);
			Integer numSupClass = new Integer (lClass.superClass.size());
			classParentCountMap.put(lClass.rdfIdentifier, numSupClass);
			concatClassName = "";
			for (Iterator <String> j = accumClassNameArr.iterator(); j.hasNext();) {
				String name = (String) j.next();
				concatClassName += name;
			}
			objHierArray.put(concatClassName, lClass.rdfIdentifier);
			concatNameArray.add(concatClassName);
		}
		Collections.sort(concatNameArray);
		for (Iterator <String> i = concatNameArray.iterator(); i.hasNext();) {
			concatClassName = (String) i.next();
			String lClassId = (String) objHierArray.get(concatClassName);
			numclass++;
			String indent = "+ ";
			Integer numSupClass = (Integer) classParentCountMap.get(lClassId);
			int jlmt = numSupClass.intValue();
			for (int j = 0; j < jlmt; j++) {
			  indent += "+ ";
			}

// 			get lClassAnchorString
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lClassId);
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
			prhtml.println("<p>" + indent + phtitle + "</p>");
		}
		prhtml.println("<p>" + "The class hierarchy above includes " + numclass + " unique classes." + "</p>");	
	}
	
	/**
		*  Get the classes to be printed
	*/
	public ArrayList <PDSObjDefn> getPrintClassArr (SectionDefn secInfo) {
		TreeMap <String, PDSObjDefn> lClassSortMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator <String> i = secInfo.sectionModelContentId.iterator(); i.hasNext();) {
			String cid = (String) i.next();
			SectionContentDefn content = (SectionContentDefn) docInfo.sectionContentMap.get(cid);
			ModelDefn lModelInfo = (ModelDefn) docInfo.modelMap.get(content.modelId);
			ProtPontModel lmodel = (ProtPontModel) lModelInfo.objectid;	
//			ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (lmodel.objDict.values());
			for (Iterator <PDSObjDefn> j = InfoModel.masterMOFClassArr.iterator(); j.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) j.next();			
				if (content.includeClassId.contains(lClass.section)) {					
					lClassSortMap.put(lClass.title, lClass);
				}
			}
		}
		return new ArrayList <PDSObjDefn> (lClassSortMap.values());
	}	
		
	/**
		*  Print a section of the table of contents
		*/
	
	public void printModelTOC (ArrayList <PDSObjDefn> printClassArr, SectionDefn secInfo) {
		itemNumAdd(itemNum);
		for (Iterator <PDSObjDefn> i = printClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
				
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			itemNumPlus(itemNum);
			String phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClassAnchorString + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClass.title + "</a>";
			prhtml.println("<b>&nbsp;&nbsp;" + phtitle + "</b><br>");
		}
		itemNumRemove(itemNum);
	}	

	/**
		*  Print a model in table format as a subsection (added values and fixed card)
		*/
	public void printModelTable (ArrayList <PDSObjDefn> printClassArr, SectionDefn secInfo, ArrayList <String> itemNum) {
		int levelind = secInfo.texFormatInd;
		itemNumAdd(itemNum);
		for (Iterator <PDSObjDefn> i = printClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			itemNumPlus(itemNum);
			printClass (lClass.rdfIdentifier, levelind, itemNum);
		}
		itemNumRemove(itemNum);
	}
	
	/**
		*  Print out a class
		*/
	
	private void printClass (String lClassId, int levelind, ArrayList <String> itemNum) {				
		PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lClassId);
		String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();		
//		System.out.println("\ndebug class title:" + lClass.title);
		if ((levelind + 1) < begSectionFormats.size()) {
			levelind += 1;
		}
		String lRegistrationStatus = "";
		if (lClass.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = DMDocument.Literal_DEPRECATED;
		
		String prole = "Abstract";
		if (lClass.role.compareTo("concrete") == 0) {
			prole = "Concrete";
		}
		prhtml.println("<a name=\"" + lClassAnchorString + "\">" + "&nbsp;&nbsp;" + "</a>"); // needed to link from within objects
		prhtml.println(begSectionFormats.get(levelind) + "<a name=\"" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClassAnchorString + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClass.title + lRegistrationStatus + "</a>" + endSectionFormats.get(levelind));
		prhtml.println("<p><i><b>Root Class:</b></i>" + lClass.docSecType + "<br>");
		prhtml.println("<i><b>Role:</b></i>" + prole + "<br>");
		String phdesc = lClass.description;
		int s = phdesc.indexOf("[CHG:");
		if (s >= 0) {
			int e = phdesc.indexOf(']', s);
			String change = phdesc.substring(s+5, e);
			String phchange = "<a href=\"#" + change + "\">" + change + "</a>";
			phdesc = replaceString (phdesc, change, phchange);
		}	
		s = phdesc.indexOf("[ANO:");
		if (s >= 0) {
			int e = phdesc.indexOf(']', s);
			String change = phdesc.substring(s+5, e);
			String phchange = "<a href=\"#" + change + "\">" + change + "</a>";
			phdesc = replaceString (phdesc, change, phchange);
		}	
		prhtml.println("<i><b>Class Description:</b></i>" + phdesc + "<br>");
		prhtml.println("<i><b>Steward:</b></i>" + lClass.steward + "<br>");
		prhtml.println("<i><b>Namespace Id:</b></i>" + lClass.nameSpaceIdNC + "<br>");
		prhtml.println("<i><b>Version Id:</b></i>" + lClass.versionId + "<br>");
		String lUnitsOf = getClassUnits (lClass);
		if (lUnitsOf == null) {
			prhtml.println("</p>");
		} else {
			prhtml.println("<i><b>Unit of Measure Type:</b></i>" + lUnitsOf + "</p>");
		}
		printHTMLTableHdr();

//print attributes for this object
 		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (lClass.superClass);
 		lClassArr.add(lClass);
		PrintOneClassHierarchy (lClassArr, "Hierarchy");
		PrintSubclasses (lClass.subClasses, "Subclass");					
		printTableRow2 (lClass.ownedAttribute, "Attribute");
		printTableRow2 (lClass.inheritedAttribute, "Inherited Attribute");
		printTableRow2 (lClass.ownedAssociation, "Association");
		printTableRow2 (lClass.inheritedAssociation, "Inherited Association");		
		
		//	find and print reference for this class
		ArrayList <PDSObjDefn> refClassIds = getClassReferences (lClass.identifier);
		printSimpleTableRow (refClassIds, "Referenced from", true);

		prhtml.println("</table>");
	}
	
	public String getClassUnits (PDSObjDefn lClass)  {
		if (lClass.title.compareTo("Vector_Cartesian_3_Acceleration") == 0) {
			return "Units_of_Acceleration";
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Position") == 0) {
			return"Units_of_Length";
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Velocity") == 0) {
			return"Units_of_Velocity";
		}
		return null;
	}		
	
	/**
		*  Print hierarchy
		*/
	private void PrintOneClassHierarchy (ArrayList <PDSObjDefn> lClassArr, String relation) {
		String phRelation = relation;
		boolean firstflag = true;
		String indent = "";

		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();			
			
			String pltitle = lClass.title;
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>" ;
			printHTMLTableRow (phRelation, indent + phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			indent = indent + ". ";
			if (firstflag) {
				firstflag = false;
				phRelation = "&nbsp;";
			}
		}
		if (firstflag) {
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
	}
	
	/**
		*  Print subclasses
		*/
	private void PrintSubclasses (ArrayList <String> classIds, String relation) {
		String phRelation = relation;
		boolean firstflag = true;

		Collections.sort(classIds);
		for (Iterator <String> i = classIds.iterator(); i.hasNext();) {
			String locId = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(locId);
			String pltitle = lClass.title;
			
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>" ;
			printHTMLTableRow (phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			if (firstflag) {
				firstflag = false;
				phRelation = "&nbsp;";
			}
   	}
		if (firstflag) {
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
	}

	/**
	*  Print a table row
	*
	*/
	private void printTableRow2 (ArrayList <AttrDefn> lAttrArr, String relation) {
		TreeMap  <String, AttrDefn> lAttrSortMap = new TreeMap <String, AttrDefn>();
		String phRelation = relation;
		String phtitle;
		String phcard;
		String phvalue;		
		String phindicator = "&nbsp;";		
		boolean firstflag = true;
		
		// sort the local attributes
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();	
			lAttrSortMap.put(lAttr.rdfIdentifier, lAttr);			
		}
		
		// process the local attributes
		ArrayList <AttrDefn> lSortAttrArr = new ArrayList <AttrDefn> (lAttrSortMap.values());
		for (Iterator <AttrDefn> i = lSortAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			String lRegistrationStatus = "";
			if (lAttr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = DMDocument.Literal_DEPRECATED;
			phtitle = "<a href=\"#" + lAttr.attrAnchorString + "\">" + lAttr.title + lRegistrationStatus + "</a>";
			String cmin = lAttr.cardMin;																// get min card
			String cmax = lAttr.cardMax;																// get max card
			String cardval = cmin + ".." + cmax;
			if (cardval.compareTo("1..0") == 0) { 		
				cardval = "none";
			} else if (PDSOptionalFlag) {															// Needed for PDS info model - Optional Attributes
				if (myIsInteger(cmin) && myIsInteger(cmax)) {			// if mincard > maxcard then we have an optional attribute
					Integer iccmin = new Integer (cmin);									//    if so calc mincard = mincard-maxcard - 1 
					int icmin = iccmin.intValue();												//    e.g. 7,3 ->  3,3; 4,3 -> 0,3
					Integer iccmax = new Integer (cmax);
					int icmax = iccmax.intValue();
					if (icmin > icmax) {
						icmin = (icmin - icmax) - 1;
						iccmin = new Integer (icmin);
						cmin = iccmin.toString();
						cardval = cmin + ".." + cmax;
					}
				} else {
					phindicator = "&nbsp;";
				}
			}
			if (lAttr.isRestrictedInSubclass) {		// attribute is restricted in a subclass as opposed to restricted relative to the attribute in the "USER" class
				phindicator += "R";
			}
			if (cmin.compareTo(cmax) == 0) {
				cardval = cmin;
			}
			phcard = cardval;
			phvalue = "";
			if (lAttr.genAttrMap != null) {															// get value list from dd
				ArrayList stdvalarr = (ArrayList) lAttr.genAttrMap.get("standard_value");
				if (stdvalarr != null) {
					phvalue = "DD";
				}
			}			
			
			ArrayList <String> tarr = lAttr.valArr;																// get attr value list
			if (tarr.isEmpty()) {
				tarr.add("");
			}
			ArrayList <PDSObjDefn> lValClassArr = lAttr.valClassArr;
			if (lValClassArr.isEmpty()) {
				String lClassRdfIdentifier = DMDocument.rdfPrefix + "." + "UNK" + "." + "DUMMY";
				PDSObjDefn lDummyClass = new PDSObjDefn(lClassRdfIdentifier);
				lDummyClass.title = "dummy";
				lValClassArr.add(lDummyClass);
			}			
			
			Iterator <PDSObjDefn> k = lValClassArr.iterator();
			Iterator <String> j = tarr.iterator();
			String value;
			PDSObjDefn lValClass = null;
			while (j.hasNext()) {
				value = (String) j.next();
				if (k.hasNext()) lValClass = (PDSObjDefn) k.next();
				if (value.compareTo("") == 0) {
					if (phvalue.compareTo("DD") != 0) {
						phvalue = "&nbsp;";
					}
				} else {
					String lAnchorString = lAttr.attrAnchorString;
					if (lAttr.isAttribute) {  // attribute
						// check for data types, unit of measure, etc
						if (lAttr.title.compareTo("data_type") == 0 || lAttr.title.compareTo("value_data_type") == 0 || lAttr.title.compareTo("unit_of_measure_type") == 0 || lAttr.title.compareTo("product_class") == 0) {
							String lClassId = InfoModel.getClassIdentifier ("pds", value);
							PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lClassId);
							if (lClass != null) {
								lAnchorString = lClass.anchorString;	
							} else {
								// error
								System.out.println(">>error   - printTableRow2 - Component Class is missing - lClassId:" + lClassId);							
							}
						} else {
							lAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + value).toLowerCase();				
						}
					} else {  // association (AttrDefn)
						lAnchorString = lValClass.anchorString;
					}
					
					String lValue = replaceString (value, "μ", "&mu;");
					phvalue = "<a href=\"#" + lAnchorString + "\">" + lValue + "</a>";
				}
				printHTMLTableRow (phRelation, phtitle, phcard, phvalue, phindicator);
				firstflag = false;
				phRelation = "&nbsp;";
				phtitle = "&nbsp;";
				phcard = "&nbsp;";
				phindicator = "&nbsp;";
			}
		}
		if (firstflag) {
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
	}
   		
	/**
		*  Print HTML table Header
		*/
	private void printHTMLTableHdr () {
		if (PDSOptionalFlag) {
			prhtml.println("<table border=1>");
			prhtml.println("<tr> <th>&nbsp;</th> <th>Entity</th> <th>Card</th> <th>Value/Class</th> <th>Ind</th> </tr>");
		} else {
			prhtml.println("<table border=1>");
			prhtml.println("<tr> <th>&nbsp;</th> <th>Entity</th> <th>Card</th> <th>Value/Class</th> <th>Ind</th> </tr>");
		}
	}

	/**
		*  Print a HTML table row
		*/
	private void printHTMLTableRow (String phRelation, String phtitle, String phcard, String phvalue, String phindicator) {
		if (PDSOptionalFlag) {
		  prhtml.println("<tr><td><b>" + phRelation + "</b></td><td>" + phtitle + "</td><td>" + phcard + "</td><td>" + phvalue + "</td><td>" + phindicator + "</td></tr>");
		} else {
		  prhtml.println("<tr><td><b>" + phRelation + "</b></td><td>" + phtitle + "</td><td>" + phcard + "</td><td>" + phvalue + "</td><td>" + phindicator + "</td></tr>");
		}
	}
	
	/**
		*  Print a simple table row
	*/
	private void printSimpleTableRow (ArrayList <PDSObjDefn> lClassArr, String relation, boolean sortflag) {
		String phRelation = relation;
		String phtitle;
		boolean firstflag = true;
		
		// sort the classes
		TreeMap <String, PDSObjDefn> lSortClassMap= new TreeMap <String, PDSObjDefn> ();
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			lSortClassMap.put(lClass.title, lClass);
		}
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (lSortClassMap.values());

		for (Iterator <PDSObjDefn> i = lSortClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			
			phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
			printHTMLTableRow (phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			firstflag = false;
			phRelation = "&nbsp;";
			phtitle = "&nbsp;";
		}
		if (firstflag) {
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
 	} 
	
	/**
		*  Get the references for a class
		*/
	private ArrayList <PDSObjDefn> getClassReferences (String lClassId) {
		ArrayList <String> lClassIdArr = new ArrayList <String> ();
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> ();
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.valClassArr.isEmpty()) continue; 
				for (Iterator <PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {
					PDSObjDefn lCompClass = (PDSObjDefn) k.next();
					if (lCompClass.identifier.compareTo(lClassId) != 0) continue;
					if (! lClassIdArr.contains(lClass.identifier)) {
						lClassIdArr.add(lClass.identifier);
						lClassArr.add(lClass);
					}
				}
			}
			for (Iterator <AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.valClassArr.isEmpty()) continue; 
				for (Iterator <PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {
					PDSObjDefn lCompClass = (PDSObjDefn) k.next();
					if (lCompClass.identifier.compareTo(lClassId) != 0) continue;
					if (! lClassIdArr.contains(lClass.identifier)) {
						lClassIdArr.add(lClass.identifier);
						lClassArr.add(lClass);
					}
				}
			}
		}
		return lClassArr;
	}
	
/**********************************************************************************************************
		routines for printing the data dictionary
***********************************************************************************************************/

	public void printDataDict () {
		boolean pflag = false;
//	write the description heading
		prhtml.println("<dl>");

		for (Iterator <AttrDefn> i = InfoModel.getAttArrByTitleStewardClassSteward().iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass || includeAllAttrFlag) {
				printDataElement (lAttr);
				pflag = true;
			}
		}
		if (! pflag) {
			prhtml.println("<dt><b>" + "Unknown" +  "</b><dd>" +  "Unknown Description");		
		}
		prhtml.println("</dl>");		
	}
	
	/**
		*  Print a data element
		*/
	private void printDataElement (AttrDefn attr) {
		boolean fflag, altflag;		
		String phtitle, desc, altlist;
		
		// get lClassAnchorString
		PDSObjDefn lClass = attr.attrParentClass;
		String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
		String lClassHrefString = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
		String lRegistrationStatus = "";
		if (attr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = " - " + DMDocument.Literal_DEPRECATED;
		phtitle = "<a name=\"" + attr.attrAnchorString + "\"><b>" + attr.title + lRegistrationStatus + "</b> in " + lClassHrefString + "</a>";
		desc = attr.description;
		altflag = false; altlist = ""; fflag = true;
		HashMap lmap = (HashMap) attr.genAttrMap;
		if (lmap != null) {
			ArrayList <String> attraliasarr = (ArrayList) lmap.get("alias_name");
			if (attraliasarr != null) {
				for (Iterator <String> i = attraliasarr.iterator(); i.hasNext();) {
					if (fflag) {
						fflag = false;
						altflag = true;
						altlist = " {Alternatives: ";
					} else {
						altlist += ", ";
					}
					altlist += (String) i.next();
				}
				if (altflag) {
					altlist += "} ";
				}
			}
		}
		prhtml.println("<dt>" + phtitle +  "<dd><i>" + altlist + "</i>" + desc);

		printAttrType (attr);
		printAttrUnit (attr);
		printAttrMisc (attr);
		printAttrValue (attr);
		printAttrValueExtended (attr);
		printAttrSchematronRuleMsg(attr);
	}	

	
	/**
	*  Print an attributes unit
	*/

private void printAttrUnit (AttrDefn attr) {
	String lUnitOfMeasureType;
	if (attr.isAttribute && attr.unit_of_measure_type.indexOf("TBD") != 0 && attr.unit_of_measure_type.indexOf("none") != 0) {
		lUnitOfMeasureType = (String) attr.unit_of_measure_type;
		prhtml.println("<p><i>Unit of Measure Type: </i>" + lUnitOfMeasureType + "<br>");
		
		String lValueString = attr.getUnits(false);
		if (lValueString != null) {
			prhtml.println("<i>Valid Units: </i>" + lValueString + "<br>");
		}
	}
	
	String pval = "";
	if (attr.isAttribute && attr.default_unit_id.indexOf("TBD") != 0 && attr.default_unit_id.indexOf("none") != 0) {
		pval = (String) attr.default_unit_id;
		prhtml.println("<i>Specified Unit Id: </i>" + pval + "<br>");
	}
}	
	/**
	*  Print an attributes type
	*/
	private void printAttrType (AttrDefn attr) {
		String phtype;
		
		if (attr.isAttribute) {
			if (attr.valueType.indexOf("TBD") == 0) {
				return;
			}
			phtype = (String) attr.valueType;
		} else {
			phtype = "Association";
		}

		String lClassId = InfoModel.getClassIdentifier ("pds", phtype);
		PDSObjDefn lClass = InfoModel.masterMOFClassIdMap.get(lClassId);
		if (lClass != null) {
			String lAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			phtype = "<a href=\"#" + lAnchorString + "\">" + phtype + "</a>";
		} else {
			phtype = "<a href=\"#" + phtype + "\">" + phtype + "</a>";
		}		
		prhtml.println("<p><i>Type: </i>" + phtype + "<br>");
	}

	/**
	*  Print an attributes type
	*/

	private void printAttrMisc (AttrDefn attr) {
		String pval = "";
		if (! attr.isAttribute) {
			return;
		}
		
		pval = (String) attr.parentClassTitle;
		prhtml.println("<i>Class Name: </i>" + pval + "<br>");
		
		pval = attr.getMinimumCharacters2 (true, false);
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Minimum Characters: </i>" + pval + "<br>");
		}
		pval = attr.getMaximumCharacters2 (true, false);
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Maximum Characters: </i>" + pval + "<br>");
		}
		pval = attr.getMinimumValue2 (true, false);
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Minimum Value: </i>" + pval + "<br>");
		}
		pval = attr.getMaximumValue2 (true, false);
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Maximum Value: </i>" + pval + "<br>");
		}
		pval = attr.getFormat (true);
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Format: </i>" + pval + "<br>");
		}
		pval = InfoModel.unEscapeProtegeString(attr.getPattern(true));
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Pattern: </i>" + pval + "<br>");
		}
		pval = "false";
		if (attr.isNilable) {
			pval = "true";			
		}
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Nillable: </i>" + pval + "<br>");
		}
		pval = (String) attr.classConcept;
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Attribute Concept: </i>" + pval + "<br>");
		}
		pval = (String) attr.dataConcept;
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Conceptual Domain: </i>" + pval + "<br>");
		}
		pval = attr.getSteward ();
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Steward: </i>" + pval + "<br>");
		}
		pval = attr.getNameSpaceIdNC ();
		if (pval.indexOf("TBD") != 0) {
			prhtml.println("<i>Namespace Id: </i>" + pval + "<br>");
		}
	}
	
	/**
		*  Print an attributes values
		*/

	private void printAttrValue (AttrDefn lAttr) {
		String phvalue = "", del = "";
		boolean elipflag = false;

		if (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty()) return; 
		if (lAttr.permValueArr.size() > 1) {
			prhtml.println("<p><i>Values: </i><br>");
		} else {
			prhtml.println("<p><i>Value: </i><br>");
		}
		
		for (Iterator <PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
			if (lPermValueDefn.value.compareTo("...") == 0) {
				elipflag = true; 
			} else if (lPermValueDefn.value.compareTo("2147483647") == 0) {
				del = ", ";
				phvalue = "<a name=\"" + "" + "\"><b>" + "Unbounded" + "</b></a>" + " - " + "There is no bound on the maximum number of characters allowed.";
				prhtml.println(" - " + phvalue + "<br>");

			} else {
				// check if deprecated
				String lRegistrationStatus = "";
				if (lPermValueDefn.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = " - " + DMDocument.Literal_DEPRECATED;

				// check if dependent
				String lDependValue = lAttr.valueDependencyMap.get(lPermValueDefn.value);
				String lDependClause = "";
				if (lDependValue != null) lDependClause = " (" + lDependValue + ")";
				del = ", ";
				String lValueAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value).toLowerCase();				
				String lValue = replaceString (lPermValueDefn.value, "μ", "&mu;");
				String lValueMeaning = replaceString (lPermValueDefn.value_meaning, "μ", "&mu;");
				if (lAttr.title.compareTo("pattern") == 0 && lPermValueDefn.value_meaning.indexOf("TBD") == 0) {

					
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + lDependClause + lRegistrationStatus + "</b></a>";
				} else {
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lValue + lDependClause + lRegistrationStatus + "</b></a>" + " - " + lValueMeaning;
				}
				prhtml.println(" - " + phvalue + "<br>");
			}
		}
		if (elipflag) {
			phvalue = "<b>...</b>" + " - " + "The number of values exceeds the reasonable limit for this document.";
			prhtml.println(" - " + phvalue + "<br>");
		}
  }
	
	/**
	*  Print an attributes Extended values
	*/
	private void printAttrValueExtended (AttrDefn lAttr) {
		String phvalue, del = "";
		boolean elipflag = false;

		if (lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty()) {
			return;
		}
		
		for (Iterator <PermValueExtDefn> i = lAttr.permValueExtArr.iterator(); i.hasNext();) {
			PermValueExtDefn lPermValueExt = (PermValueExtDefn) i.next();	
			if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) {
				return;
			}
			
			if (lPermValueExt.permValueExtArr.size() > 1) {
				prhtml.println("<p><i>Extended Values for: " + lPermValueExt.xpath + "</i><br>");
			} else {
				prhtml.println("<p><i>Extended Value for: " + lPermValueExt.xpath + "</i><br>");
			}
		
			for (Iterator <PermValueDefn> j = lPermValueExt.permValueExtArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
				if (lPermValueDefn.value.compareTo("...") == 0) {
					elipflag = true;
				} else {
					del = ", ";
					String lValueAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value).toLowerCase();				
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + "</b></a>" + " - " + lPermValueDefn.value_meaning;
					prhtml.println(" - " + phvalue + "<br>");
				}
			}
		}
		if (elipflag) {
			phvalue = "<b>...</b>" + " - " + "The number of values exceeds the reasonable limit for this document.";
			prhtml.println(" - " + phvalue + "<br>");
		}
	}
	
	/**
	*  Print schematron rule for the attribute
	*/
	private void printAttrSchematronRuleMsg(AttrDefn lAttr) {
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleIdMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			for (Iterator <AssertDefn2> j = lRule.assertArr.iterator(); j.hasNext();) {
				AssertDefn2 lAssert = (AssertDefn2) j.next();
				if (! ((lRule.classTitle.compareTo(lAttr.parentClassTitle) == 0) && lAssert.attrTitle.compareTo(lAttr.title) == 0)) continue;
				if (lAssert.assertMsg.indexOf("TBD") == 0) continue;				
				if (lAssert.specMesg.indexOf("TBD") == 0) continue;				
				if (lAssert.assertType.compareTo("RAW") != 0) continue;
//				prhtml.println("<i>Schematron Rule: " + lAssert.assertMsg + "</i><br>");
				prhtml.println("<i>Schematron Rule: " + lAssert.specMesg + "</i><br>");
			}
		} 
	}

/**********************************************************************************************************
		routines for printing the glossary
***********************************************************************************************************/

	public void printGlossary (SectionDefn secInfo) {
		boolean pflag = false;
		
		String cid = (String) secInfo.sectionModelContentId.get(0);
		SectionContentDefn content = (SectionContentDefn) docInfo.sectionContentMap.get(cid);
		ModelDefn lModelInfo = (ModelDefn) docInfo.modelMap.get(content.modelId);
		ProtPinsGlossary lmodel = (ProtPinsGlossary) lModelInfo.objectid;			

//	write the description heading
		prhtml.println("<dl>");
		
    	Set <String> set1 = lmodel.glossTitleIdMap.keySet();
    	Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lTitle = (String) iter1.next();
			String lId = (String) lmodel.glossTitleIdMap.get(lTitle);
			AttrDefn attr =  (AttrDefn) lmodel.glossMap.get(lId);
			printTerm (attr);
			pflag = true;
		}
		
		if (! pflag) {
			prhtml.println("<dt><b>" + "Unknown" +  "</b><dd>" +  "Unknown Description");		
		}
		prhtml.println("</dl>");		
	}
	
	/**
		*  Print a data element
		*/
	private void printTerm (AttrDefn attr) {
		
		String phtitle, desc;
		
		String lTermAnchorString = ("term_" + attr.title).toLowerCase();
		String titleWithBlanks = DMDocument.replaceString (attr.title, "_", " ");
		phtitle = "<a name=\"" + lTermAnchorString + "\">" + titleWithBlanks + "</a>";
		desc = attr.description;
		prhtml.println("<dt><b>" + phtitle +  "</b><dd><i>" + "</i>" + desc);
	}	
				
/**********************************************************************************************************
		miscellaneous routines
***********************************************************************************************************/
			
	/**
	*  Get Slot Value
	*/
	public String getSlotMapValue (ArrayList <String> valarr) {
		if (! (valarr == null || valarr.isEmpty())) {
			return (String) valarr.get(0);
		}
		return null;
	}

	/**
		*  check to see if string is numeric
		*/
	public boolean myIsInteger (String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i <  sb.length (); i++) {
			if (! Character.isDigit(sb.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
		*  bump current level by one
		*/
	public void itemNumPlus (ArrayList <String> itemNum) {

		int ind = itemNum.size() - 1;
		Integer ival = new Integer ((String) itemNum.get(ind));
		int val = ival.intValue();
		val += 1;
		Integer nval = new Integer (val);
		itemNum.set(ind, nval.toString());
	}

	/**
		*  decrement current level by one
		*/
	public void itemNumMinus (ArrayList <String> itemNum) {

		int ind = itemNum.size() - 1;
		Integer ival = new Integer ((String) itemNum.get(ind));
		int val = ival.intValue();
		val -= 1;
		Integer nval = new Integer (val);
		itemNum.set(ind, nval.toString());
	}

	/**
		*  increase number of levels by one
		*/
	public void itemNumAdd (ArrayList <String> itemNum) {

		itemNum.add("0");
	}
	
	/**
		*  decrease number of levels by one
		*/
	public void itemNumRemove (ArrayList <String> itemNum) {

		int ind = itemNum.size() - 1;

		itemNum.remove(ind);
	}
	
	/**
		*  print item number
		*/
	public String printItemNum (ArrayList <String> itemNum) {

		boolean fflag = true;
		String itemNumString;

		itemNumString = "";
		for (Iterator <String> i = itemNum.iterator(); i.hasNext();) {			
			String snum = (String) i.next();
			if (fflag) {
				fflag = false;
			} else {
				itemNumString += ".";
			}
			itemNumString += snum;
		}
		return itemNumString;
	}
	
	/**
		* EMW Formula
		*/
	public String insertEMWF (String str1, ArrayList <String> formSet) {
		int p1, p2, str1len;
		char tc;
		Character ctc;
		StringBuffer sbuff1 = new StringBuffer(str1), sbuff2 = new StringBuffer();
		p1 = 0;
		p2 = p1 + 1;
		str1len = sbuff1.length();
		while (p1 < str1len) {
			tc = sbuff1.charAt(p1);
			if ((tc == '\\') && (p2 < str1len) && (sbuff1.charAt(p2) == '|')) {
				p1 += 2;
				tc = sbuff1.charAt(p1);
				ctc = new Character (tc);
				Integer Iind = new Integer (ctc.toString());
				int ind = Iind.intValue();
				sbuff2.append(formSet.get(ind));
			} else {
				sbuff2.append(tc);
			}
			p1++;
			p2 = p1 + 1;
		}
		return sbuff2.toString();
	}   

	/**
		* Replace string with string (gleaned from internet)
		*/
	
	static String replaceString (String str, String pattern, String replace) {
			int s = 0;
			int e = 0;
			StringBuffer result = new StringBuffer();
			
			while ((e = str.indexOf(pattern, s)) >= 0) {
				result.append(str.substring(s, e));
				result.append(replace);
				s = e+pattern.length();
			}
			result.append(str.substring(s));
			return result.toString();
	 }
}
