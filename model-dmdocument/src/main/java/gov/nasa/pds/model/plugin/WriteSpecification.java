package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

/** Driver for getting document
 *
 */
public class WriteSpecification extends Object {
	
	DocDefn docInfo;

	ArrayList <String> texSectionFormats;
	ArrayList <String> begSectionFormats;
	ArrayList <String> endSectionFormats;
	ArrayList <String> emwfLatex;
	ArrayList <String> emwfHTML;
//	ArrayList <String> valueList;
	
	boolean includeAllAttrFlag = false;
	boolean PDSOptionalFlag = false;
	int figNum;
	Date rTodaysDate;
	String sTodaysDate;
	ArrayList <String> itemNum;
	
	PrintWriter prlatex, prhtml, prdocbook;
	
	public WriteSpecification (DocDefn lDocInfo, boolean lflag) {
		docInfo = lDocInfo;
		PDSOptionalFlag = lflag;
		
		//	set up the Tex markers
		texSectionFormats = new ArrayList <String>();
		texSectionFormats.add("\\section");
		texSectionFormats.add("\\subsection");
		texSectionFormats.add("\\subsubsection");
		
		//	set up the HTML Headers
		begSectionFormats = new ArrayList <String>();
		begSectionFormats.add("<H2>");
		begSectionFormats.add("<H3>");
		begSectionFormats.add("<H4>");

		endSectionFormats = new ArrayList <String>();
		endSectionFormats.add("</H2>");
		endSectionFormats.add("</H3>");
		endSectionFormats.add("</H4>");
		
		emwfLatex = new ArrayList <String>();
		emwfLatex.add("\\begin{center} $\\textit{\\textbf{i}}_xAcos({\\omega}t-\\textit{\\textbf{kr}}-\\varphi)$ \\end{center}");
		emwfLatex.add("$\\textit{\\textbf{i}}_x$");
		emwfLatex.add("$\\textit{A}$");
		emwfLatex.add("${\\omega}$");
		emwfLatex.add("$\\textit{\\textbf{k}}$");
		emwfLatex.add("$\\varphi$");
				
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
	   
//    prlatex = new PrintWriter(new FileWriter("latexipt.tex", false));
//    prhtml = new PrintWriter(new FileWriter("index.html", false));
//    prdocbook = new PrintWriter(new FileWriter("PDS4docbook.xml", false));
		File targetDir = new File(DMDocument.outputDirPath);
		targetDir.mkdirs();
	    prlatex = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "/latexipt.tex", false));
	    prhtml = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "/index.html", false));
	    prdocbook = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "/PDS4docbook.xml", false));

//	Start writing the latex document                

//		System.out.println("debug - writing latex document");
		
		prlatex.println("\\documentclass[a4paper,11pt]{article}");
		prlatex.println("\\usepackage{graphicx}");
		prlatex.println("% define the title");
		prlatex.println("\\author{" + repLatexSpecChar(docInfo.author) + "}");
		prlatex.println("\\title{" + repLatexSpecChar(docInfo.title) + "}");
		
//	prlatex.println("\\documentclass{article}");
		prlatex.println("\\begin{document}");
		prlatex.println("% generates the title");
		prlatex.println("\\maketitle");
		prlatex.println("  ");		// cause a blank line for new paragraph
		prlatex.println("\\begin{center}");
		prlatex.println(repLatexSpecChar(docInfo.subTitle));
		prlatex.println("\\end{center}");			
		prlatex.println("\\begin{center}");
		prlatex.println("Version " + repLatexSpecChar(docInfo.version));
		prlatex.println("\\end{center}");			
		
		prlatex.println("\\newpage");	
		prlatex.println("\\tableofcontents");
		prlatex.println("\\newpage");	
		prlatex.println("\\listoffigures");
		prlatex.println("\\newpage");	
		prlatex.println("\\sloppy");	

//	Start writing the html document                

//		System.out.println("debug - writing html document");
		
		prhtml.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");
		prhtml.println("<html>");
		prhtml.println("<head>");
		prhtml.println("<title>" + docInfo.title + "</title>");
		prhtml.println("<p align=center>");
		prhtml.println("<h1 align=center>" + docInfo.title + "</h1><br><br>");
		prhtml.println("<h2 align=center>" + docInfo.author + "</h2><br>");
		prhtml.println("<h2 align=center>" + sTodaysDate + "</h2><br><br><br>");
		prhtml.println("<h2 align=center>" + docInfo.subTitle + "</h2><br>");
		prhtml.println("<h2 align=center>" + "Version " + docInfo.version + "</h2><br>");
		prhtml.println("</p>");
		prhtml.println("</head>");
		prhtml.println("<body bgcolor=#FFFFF0>");

//	Print the *** TABLE OF CONTENTS *** for the HTML file

		prhtml.println("<p><h2>" + "Table of Contents" + "</h2></p>");
		prhtml.println("<ul>");
		
// get each section		
		Collections.sort(docInfo.sectionArray);
		itemNumAdd(itemNum);
		for (Iterator <String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
			String secId = (String) i.next();
			SectionDefn secInfo = (SectionDefn) docInfo.sectionMap.get(secId);
//			System.out.println("\ndebug - checking TOC secId:" + secId);
			if (! secInfo.includeFlag) { continue; }
//			System.out.println("debug - processing TOC secId:" + secId);
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
		
//	Start writing the docbook document

//		System.out.println("debug - writing docbook document");
		
		prdocbook.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		prdocbook.println("<!DOCTYPE book PUBLIC \"-//OASIS//DTD DocBook XML V4.5//EN\"");
		prdocbook.println("                         \"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd\">");
		prdocbook.println("<book xmlns=\"http://docbook.org/ns/docbook\">");
		prdocbook.println("<title>" + docInfo.title + "</title>");
		prdocbook.println("<subtitle>" + docInfo.subTitle + "</subtitle>");
		prdocbook.println("<info>");
		prdocbook.println("  <date>" +  sTodaysDate 	 + "</date>");
		prdocbook.println("  <author><orgname>" + docInfo.author + "</orgname></author>");
		prdocbook.println("</info>");
		
//	Print the *** Document Contents ***		
		
//	iterate through sections and print them 	
		
		itemNum.clear();
		itemNumAdd(itemNum);
		for (Iterator <String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
			String secId = (String) i.next();
			SectionDefn secInfo = (SectionDefn) docInfo.sectionMap.get(secId);
//			System.out.println("debug - checking document secId:" + secId);
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
		prlatex.println("\\end{document}");
		prlatex.close();
		
		prhtml.println("</body>");
		prhtml.println("</html>");
		prhtml.close();
				
		prdocbook.println("</book>");
		prdocbook.close();
	}
	
/**********************************************************************************************************
		routines for printing the model
***********************************************************************************************************/

	/**
		*  Print standard section
		*/
	public void printStandardSection (SectionDefn imsec, ArrayList <String> itemNum) {
				
//	print the latex
		if (imsec.secTOCFlag) {
			prlatex.println(texSectionFormats.get(imsec.texFormatInd) + "{" + repLatexSpecChar(imsec.title) + "}");
		}
		
		prlatex.println(insertEMWF(repLatexSpecChar(imsec.description), emwfLatex));

//	insert the graphic diagram if one exists

		if (imsec.imageFlag) {
			prlatex.println("\\begin{figure}");
			prlatex.println("\\centering");
			prlatex.println("\\includegraphics[width=1.00\\textwidth]{" + repLatexSpecChar(imsec.imageFileName) + ".eps}");
			prlatex.println("\\caption{" + repLatexSpecChar(imsec.imageCaption) + "}");
			prlatex.println("\\end{figure}");
//			prlatex.println("\\newpage");	
		}
		
// print the html		
		if (imsec.secTOCFlag) {
			String phtitle = "<a name=\"" + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "</a>";
			prhtml.println(begSectionFormats.get(imsec.texFormatInd) + phtitle + endSectionFormats.get(imsec.texFormatInd) );
		}
		String phdesc = replaceString (imsec.description, "\\newline", "<p>");			
		prhtml.println("<p>" + insertEMWF(phdesc, emwfHTML) + "</p>");

//	insert the graphic diagram if one exists

		if (imsec.imageFlag) {
//			prhtml.println("<p><img align=bottom width=\"40%\" height=\"40%\" src=\"" + imsec.imageFileName + ".jpg\">");
			prhtml.println("<p><img align=bottom src=\"" + imsec.imageFileName + ".jpg\">");
			figNum++;
			prhtml.println("<p><b>Figure " + figNum + "&nbsp;&nbsp;" + imsec.imageCaption + "</b></p>");
		}
		
				
//	print the docbook
//		if (imsec.secTOCFlag) {
//			prdocbook.println(texSectionFormats.get(imsec.texFormatInd) + "{" + repLatexSpecChar(imsec.title) + "}");
//		}
		
		prdocbook.println("<para>" + imsec.description + "</para>");

//<chapter><info><title>Pictures and Figures</title></info>
//<para>.....</para></chapter>

	}
		
	/**
		*  Print Class Hierarchy
		*/
	public void printClassHierarchySection (ArrayList <String> printClassArr, SectionDefn secInfo) {
		String concatClassName;
		int numclass = 0;

		HashMap <String, String> objHierArray = new HashMap <String, String> ();
		HashMap <String, Integer> classParentCountMap = new HashMap <String, Integer> ();
		HashMap <String, String> classNameMap = new HashMap <String, String> ();
		ArrayList <String> concatNameArray = new ArrayList <String> ();
		ArrayList <String> accumClassNameArr;
		for (Iterator <String> i = printClassArr.iterator(); i.hasNext();) {
			String lTitle = (String) i.next();	
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
			if (lClass != null) {
				String lRDFId = lClass.rdfIdentifier;
				accumClassNameArr = new ArrayList <String> ();
				for (Iterator <String> j = lClass.superClasses.iterator(); j.hasNext();) {
					String locId2 = (String) j.next();
//					System.out.println("debug superClasses locId2:" + locId2);
					PDSObjDefn locClass2 = (PDSObjDefn) InfoModel.masterMOFClassMap.get(locId2);
					accumClassNameArr.add(locClass2.title);
				}
				accumClassNameArr.add(lClass.title);
				Integer numSupClass = new Integer (lClass.superClasses.size());
				classParentCountMap.put(lRDFId, numSupClass);
				classNameMap.put(lRDFId, lClass.title);
				concatClassName = "";
				for (Iterator <String> j = accumClassNameArr.iterator(); j.hasNext();) {
					String name = (String) j.next();
					concatClassName += name;
				}
				objHierArray.put(concatClassName, lRDFId);
				concatNameArray.add(concatClassName);
			}
		}
		Collections.sort(concatNameArray);
		prlatex.println("\\begin{verbatim}");
		for (Iterator <String> i = concatNameArray.iterator(); i.hasNext();) {
			concatClassName = (String) i.next();
			String lClassId = (String) objHierArray.get(concatClassName);
			numclass++;
			String ptitle = (String) classNameMap.get(lClassId);
			String indent = "+ ";
			Integer numSupClass = (Integer) classParentCountMap.get(lClassId);
			int jlmt = numSupClass.intValue();
			for (int j = 0; j < jlmt; j++) {
			  indent += "+ ";
			}

// 			get lClassAnchorString
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(ptitle);
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			
			prlatex.println(indent + ptitle);
//			prlatex.println(indent + repLatexSpecChar(ptitle));
						
//			String phtitle = "<a href=\"#" + ptitle + "\">" + ptitle + "</a>";
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + ptitle + "</a>";
			prhtml.println("<p>" + indent + phtitle + "</p>");
		}
		prlatex.println("\\end{verbatim}");
		prlatex.println("The class hierarchy above includes " + numclass + " unique classes.");	
		prhtml.println("<p>" + "The class hierarchy above includes " + numclass + " unique classes." + "</p>");	
	}

	/**
		*  Get the classes to be printed
		*/
	public ArrayList <String> getPrintClassArr (SectionDefn secInfo) {
		ArrayList <String> printClassArr = new ArrayList <String> ();
//		select out class to be included
		for (Iterator <String> i = secInfo.sectionModelContentId.iterator(); i.hasNext();) {
			String cid = (String) i.next();
			SectionContentDefn content = (SectionContentDefn) docInfo.sectionContentMap.get(cid);
//			System.out.println("\ndebug  getPrintClassArr " + "content.identifier:" + content.identifier);
			ModelDefn lModelInfo = (ModelDefn) docInfo.modelMap.get(content.modelId);
			ProtPontModel lmodel = (ProtPontModel) lModelInfo.objectid;			
//			System.out.println("debug  getPrintClassArr " + "lModelInfo.identifier:" + lModelInfo.identifier);
			Set <String> set1 = lmodel.objDict.keySet();
			Iterator <String> iter1 = set1.iterator();
			while(iter1.hasNext()) {
				String lId = (String) iter1.next();
				PDSObjDefn lClass = (PDSObjDefn) lmodel.objDict.get(lId);
				
//				*** tried adding this code but get Null Pointer ***
//				PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lId);				
				if (content.includeClassId.contains(lClass.section)) {					
					printClassArr.add(lClass.title);
				}
			}
		}		
		Collections.sort(printClassArr);
		return printClassArr;
	}
	
	/**
		*  Print a section of the table of contents
		*/
	public void printModelTOC (ArrayList <String> printClassArr, SectionDefn secInfo) {
		itemNumAdd(itemNum);
		for (Iterator <String> i = printClassArr.iterator(); i.hasNext();) {
			String lTitle = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
			if (lClass != null) {
				
				// get lClassAnchorString
				String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
				
				itemNumPlus(itemNum);
				String phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClassAnchorString + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClass.title + "</a>";
				prhtml.println("<b>&nbsp;&nbsp;" + phtitle + "</b><br>");
			}
		}
		itemNumRemove(itemNum);
	}

	/**
		*  Print a model in table format as a subsection (added values and fixed card)
		*/
	public void printModelTable (ArrayList <String> printClassArr, SectionDefn secInfo, ArrayList <String> itemNum) {
		int levelind = secInfo.texFormatInd;
		itemNumAdd(itemNum);
		for (Iterator <String> i = printClassArr.iterator(); i.hasNext();) {
			String lTitle = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
			if (lClass  != null) {
				String lRDFId = lClass.rdfIdentifier;
				itemNumPlus(itemNum);
				printClass (lRDFId, levelind, itemNum);
			}
		}
		itemNumRemove(itemNum);
		prlatex.println("\\newpage");
	}

	/**
		*  Print out a class
		*/
	private void printClass (String lClassId, int levelind, ArrayList <String> itemNum) {
//			String utitle;					
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lClassId);
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();		
//			System.out.println("\ndebug class title:" + lClass.title);
			if ((levelind + 1) < texSectionFormats.size()) {
				levelind += 1;
			}
			String lRegistrationStatus = "";
			if (lClass.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = DMDocument.Literal_DEPRECATED;
			
//			prlatex.println(texSectionFormats.get(levelind) + "{" + repLatexSpecChar(utitle) +  "} ");
			prlatex.println(texSectionFormats.get(levelind) + "{" + repLatexSpecChar(lClass.title + lRegistrationStatus) +  "} ");
			prlatex.println("\\textit{\\textbf{Root Class:}} " + repLatexSpecChar (lClass.docSecType));
			prlatex.println("\\newline");
			String prole = "Abstract";
			if (lClass.role.compareTo("concrete") == 0) {
				prole = "Concrete";
			}
			prlatex.println("\\textit{\\textbf{Role:}} " + repLatexSpecChar(prole));
			prlatex.println("\\newline");
			prlatex.println("\\textit{\\textbf{Class Description:}} " + repLatexSpecChar(lClass.description));
			prlatex.println("\\newline");
			prlatex.println("\\newline");

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
//			prhtml.println("<i><b>Class Description:</b></i>" + phdesc + "</p>");
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
			printLatexTableHdr();
			printHTMLTableHdr();

//	print attributes for this object
	 		ArrayList <String> tarr = new ArrayList <String> ();
			tarr.addAll(lClass.superClasses);
			tarr.add(lClass.rdfIdentifier);
			PrintOneClassHierarchy (tarr, "Hierarchy");
			PrintSubclasses (lClass.subClasses, "Subclass");						
			printTableRow2 (lClass.ownedAttribute, "Attribute");
			printTableRow2 (lClass.inheritedAttribute, "Inherited Attribute");
			printTableRow2 (lClass.ownedAssociation, "Association");
			printTableRow2 (lClass.inheritedAssociation, "Inherited Association");		
			
			//	find and print reference for this class
			ArrayList <String> refClassIds = getClassReferences (lClass.title);
			printSimpleTableRow (refClassIds, "Referenced from", true);
			
		  prlatex.println("\\end{tabular}");
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
	private void PrintOneClassHierarchy (ArrayList <String> classIds, String relation) {

		String plRelation = relation, phRelation = relation;
		boolean firstflag = true;
		String indent = "";

		for (Iterator <String> i = classIds.iterator(); i.hasNext();) {
			String locId = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(locId);
			
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();			
			
			String pltitle = lClass.title;
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>" ;
			printLatexTableRow (plRelation, indent + pltitle,  "   ", "   ", "   ");
			printHTMLTableRow (phRelation, indent + phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			indent = indent + ". ";
			if (firstflag) {
				firstflag = false;
				plRelation = "";
				phRelation = "&nbsp;";
			}
   	}
		if (firstflag) {
			printLatexTableRow (plRelation, "none", "   ", "   ", "   ");
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
 		prlatex.println("\\hline");
	}

	/**
		*  Print subclasses
		*/
	private void PrintSubclasses (ArrayList <String> classIds, String relation) {
		String plRelation = relation, phRelation = relation;
		boolean firstflag = true;

		Collections.sort(classIds);
		for (Iterator <String> i = classIds.iterator(); i.hasNext();) {
			String locId = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(locId);
			String pltitle = lClass.title;
			
			// get lClassAnchorString
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			
//			String phtitle = "<a href=\"#" + lClass.title + "\">" + lClass.title + "</a>" ;
			String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>" ;
			printLatexTableRow (plRelation, pltitle,  "   ", "   ", "   ");
			printHTMLTableRow (phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			if (firstflag) {
				firstflag = false;
				plRelation = "";
				phRelation = "&nbsp;";
			}
   	}
		if (firstflag) {
			printLatexTableRow (plRelation, "none", "   ", "   ", "   ");
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
 		prlatex.println("\\hline");
	}

	/**
	*  Print a table row
	*/
	
private void printTableRow2 (ArrayList <AttrDefn> lAttrArr, String relation) {
	TreeMap  <String, AttrDefn> lAttrSortMap = new TreeMap <String, AttrDefn>();
	String plRelation = relation, phRelation = relation;
	String pltitle, phtitle;
	String plcard, phcard;
	String plvalue, phvalue;		
	String plindicator = "", phindicator = "&nbsp;";		
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
		pltitle = lAttr.title + "." + lAttr.className + lRegistrationStatus;																				// get title
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
					System.out.println("debug printTableRow2 cmin>cmax title:" + lAttr.title + "  cmin:" + cmin + "  cmax:" + cmax);
				}
			} else {
				plindicator = "";
				phindicator = "&nbsp;";
			}
		}
		if (lAttr.isRestrictedInSubclass) {		// attribute is restricted in a subclass as opposed to restricted relative to the attribute in the "USER" class
			plindicator += "R";
			phindicator += "R";
		}
		if (cmin.compareTo(cmax) == 0) {
			cardval = cmin;
		}
		plcard = cardval; phcard = cardval;
		plvalue = ""; phvalue = "";
		if (lAttr.genAttrMap != null) {															// get value list from dd
			ArrayList stdvalarr = (ArrayList) lAttr.genAttrMap.get("standard_value");
			if (stdvalarr != null) {
				plvalue = "DD"; phvalue = "DD";
			}
		}
		ArrayList <String> tarr = lAttr.valArr;																// get attr value list
		if (tarr.isEmpty()) {
			tarr.add("");
		}
		for (Iterator <String> j = tarr.iterator(); j.hasNext();) {								// iterate over attr value list
			String value = (String) j.next();
			if (value.compareTo("") == 0) {
				if (plvalue.compareTo("DD") != 0) {
					plvalue = ""; phvalue = "&nbsp;";
				}
			} else {
				plvalue = InfoModel.escapeLaTEXChar(value);
				String lAnchorString = lAttr.attrAnchorString;
				if (lAttr.propType.compareTo("INSTANCE") == 0) {
					PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(value);
					if (lClass != null) {
						lAnchorString = lClass.anchorString;	
					}
//				} else if (lAttr.propType.compareTo("ATTRIBUTE") == 0) {
				} else if (lAttr.isAttribute) {
					// check for data types
					PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(value);
					if (lClass != null) {
						lAnchorString = lClass.anchorString;	
					} else {
						lAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.className + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + value).toLowerCase();				
					}
				}
//				phvalue = "<a href=\"#" + value + "\">" + value + "</a>";
				phvalue = "<a href=\"#" + lAnchorString + "\">" + value + "</a>";
			}
			if (pltitle.length() > 30) {
				pltitle = pltitle.substring(0, 27);
				pltitle += "...";
			}
			printLatexTableRow (plRelation, pltitle, plcard, plvalue, plindicator);
			printHTMLTableRow (phRelation, phtitle, phcard, phvalue, phindicator);
			firstflag = false;
			plRelation = "";
			phRelation = "&nbsp;";
			pltitle = "";
			phtitle = "&nbsp;";
			plcard = "";
			phcard = "&nbsp;";
			plindicator = "";
			phindicator = "&nbsp;";
		}
	}
	if (firstflag) {
		printLatexTableRow (plRelation, "none", "   ", "   ", "   ");
		printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
	}
		prlatex.println("\\hline");
}

	/**
		*  Print Latex table Header
		*/
	private void printLatexTableHdr () {
		if (PDSOptionalFlag) {
	   		prlatex.println("\\begin{tabular}{|l|l|l|l|l|}");
	   		prlatex.println("\\hline");
			prlatex.println("\\textbf{  }" + " & " + "\\textbf{Entity}" +  " & " + "\\textbf{Card}" +  " & " + "\\textbf{Value/Class}" +  " & " + "\\textbf{Ind}" + " \\\\");
	   		prlatex.println("\\hline");
		} else {
	   		prlatex.println("\\begin{tabular}{|l|l|l|l|l|}");
	   		prlatex.println("\\hline");
			prlatex.println("\\textbf{  }" + " & " + "\\textbf{Entity}" +  " & " + "\\textbf{Card}" +  " & " + "\\textbf{Value/Class}" +  " & " + "\\textbf{Ind}" + " \\\\");
	   		prlatex.println("\\hline");
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
		*  Print a Latex table row
		*/
	private void printLatexTableRow (String plRelation, String pltitle, String plcard, String plvalue, String plindicator) {
		if (PDSOptionalFlag) {
		  prlatex.println("\\textbf{" + repLatexSpecChar(plRelation) + "}" + " & " + repLatexSpecChar(pltitle) + " & " + plcard + " & " + repLatexSpecChar(plvalue)   + " & " + plindicator  + " \\\\");
		} else {
		  prlatex.println("\\textbf{" + repLatexSpecChar(plRelation) + "}" + " & " + repLatexSpecChar(pltitle) + " & " + plcard + " & " + repLatexSpecChar(plvalue)   + " & " + plindicator  + " \\\\");
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
	private void printSimpleTableRow (ArrayList <String> classTitles, String relation, boolean sortflag) {

		String plRelation = relation, phRelation = relation;
		String pltitle, phtitle;
		boolean firstflag = true;
		
		if (sortflag) {
			Collections.sort(classTitles);
		}
		for (Iterator <String> i = classTitles.iterator(); i.hasNext();) {
			String classTitle = (String) i.next();
			
			// get lClassAnchorString
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(classTitle);
			String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			
			pltitle = classTitle;
//			phtitle = "<a href=\"#" + classTitle + "\">" + classTitle + "</a>";
			phtitle = "<a href=\"#" + lClassAnchorString + "\">" + classTitle + "</a>";
			printLatexTableRow (plRelation, pltitle,  "   ", "   ", "   ");
			printHTMLTableRow (phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
			firstflag = false;
			plRelation = "";
			phRelation = "&nbsp;";
			pltitle = "";
			phtitle = "&nbsp;";
		}
		if (firstflag) {
			printLatexTableRow (plRelation, "none", "   ", "   ", "   ");
			printHTMLTableRow (phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
		}
 		prlatex.println("\\hline");
	} 
	
	/**
		*  Get the references for a class
		*/
	private ArrayList <String> getClassReferences (String classId) {
		ArrayList <String> refClassIds = new ArrayList <String> ();
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			if (lClass.title.compareTo("%3ACLIPS_TOP_LEVEL_SLOT_CLASS") != 0) {
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					if (lAttr != null && lAttr.valArr != null) {
						for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String val = (String) k.next();
							if (classId.compareTo(val) == 0) {
								if (! refClassIds.contains(lClass.title)) {
									refClassIds.add(lClass.title);
								}
							}
						}
					}
				}
				for (Iterator <AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
					if (lAttr != null && lAttr.valArr != null) {
						for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String val = (String) k.next();
							if (classId.compareTo(val) == 0) {
								if (! refClassIds.contains(lClass.title)) {
									refClassIds.add(lClass.title);
								}
							}
						}
					}
				}
			}
		}
		return refClassIds;
	}
	
/**********************************************************************************************************
		routines for printing the data dictionary
***********************************************************************************************************/

	public void printDataDict () {
		boolean pflag = false;
		ArrayList <String> sortArr = new ArrayList <String> ();

//	write the description heading
		prlatex.println("\\begin{description}");
		prhtml.println("<dl>");

		for (Iterator <AttrDefn> i = InfoModel.getAttArrByTitleStewardClassSteward().iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInModel || includeAllAttrFlag) {
				printDataElement (lAttr);
				pflag = true;
			}
		}
		if (! pflag) {
			prlatex.println("\\item[UNKNOWN]");		
			prhtml.println("<dt><b>" + "Unknown" +  "</b><dd>" +  "Unknown Description");		
		}
		prlatex.println("\\end{description}");		
		prhtml.println("</dl>");		
	}
	
	/**
		*  Print a data element
		*/
	private void printDataElement (AttrDefn attr) {
		boolean fflag, altflag;		
		String pltitle, phtitle, desc, altlist;
		
		// get lClassAnchorString
		PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(attr.className);
		String lClassAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
		String lClassHrefString = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
		String lRegistrationStatus = "";
		if (attr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = " - " + DMDocument.Literal_DEPRECATED;
		pltitle = attr.title + lRegistrationStatus + " in " + attr.className;
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

		prlatex.println("\\item[" + repLatexSpecChar(pltitle) +  "] " + repLatexSpecChar(altlist) + repLatexSpecChar(desc));
//		prhtml.println("<dt><b>" + phtitle +  "</b><dd><i>" + altlist + "</i>" + desc);
		prhtml.println("<dt>" + phtitle +  "<dd><i>" + altlist + "</i>" + desc);

		printAttrType (attr);
		printAttrUnit (attr);
		printAttrMisc (attr);
		printAttrValue (attr);
		printAttrValueExtended (attr);
		printAttrSchematronRuleMsg(attr);
		
//		prlatex.println("\\item[" + repLatexSpecChar(pltitle) +  "] " +  repLatexSpecChar(desc));
//		prhtml.println("<dt><b>" + phtitle +  "</b><dd>" +  desc);
	}	

	
	/**
	*  Print an attributes unit
	*/

private void printAttrUnit (AttrDefn attr) {
	String lUnitOfMeasureType;
	if (attr.isAttribute && attr.unit_of_measure_type.indexOf("TBD") != 0 && attr.unit_of_measure_type.indexOf("none") != 0) {
		lUnitOfMeasureType = (String) attr.unit_of_measure_type;
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Unit of Measure Type:} " + repLatexSpecChar (lUnitOfMeasureType));
		prlatex.println("\\end{flushleft}");
		prhtml.println("<p><i>Unit of Measure Type: </i>" + lUnitOfMeasureType + "<br>");
		
		String lValueString = attr.getUnits(false);
		if (lValueString != null) {
			prlatex.println("\\begin{flushleft}");
//			prlatex.println("\\textit{Unit:} " + repLatexSpecChar (lValueString));
			prlatex.println("\\textit{Valid Units:} " + repLatexSpecChar (lValueString));
			prlatex.println("\\end{flushleft}");
//			prhtml.println("<i>Unit: </i>" + lValueString + "<br>");
			prhtml.println("<i>Valid Units: </i>" + lValueString + "<br>");
		}
	}
	
	String pval = "";
	if (attr.isAttribute && attr.default_unit_id.indexOf("TBD") != 0 && attr.default_unit_id.indexOf("none") != 0) {
		pval = (String) attr.default_unit_id;
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Specified Unit Id:} " + repLatexSpecChar (pval));
		prlatex.println("\\end{flushleft}");
		prhtml.println("<i>Specified Unit Id: </i>" + pval + "<br>");
	}
}	
	/**
	*  Print an attributes type
	*/

	private void printAttrType (AttrDefn attr) {
		String pltype, phtype;
		
		if (attr.isAttribute) {
			if (attr.valueType.indexOf("TBD") == 0) {
				return;
			}
			pltype = (String) attr.valueType;
		} else {
			pltype = "Association";
		}

		PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(pltype);
		if (lClass != null) {
			String lAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
			phtype = "<a href=\"#" + lAnchorString + "\">" + pltype + "</a>";

		} else {
			phtype = "<a href=\"#" + pltype + "\">" + pltype + "</a>";
		}
		
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Type:} " + repLatexSpecChar (pltype));
		prlatex.println("\\end{flushleft}");

		prhtml.println("<p><i>Type: </i>" + phtype + "<br>");
	}

	/**
	*  Print an attributes type
	*/

	private void printAttrMisc (AttrDefn attr) {
		String pval = "";
		
//		if (attr.propType.compareTo("ATTRIBUTE") != 0) {
		if (! attr.isAttribute) {
			return;
		}
		
		pval = (String) attr.className;
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Class Name:} " + repLatexSpecChar (pval));
		prlatex.println("\\end{flushleft}");
		prhtml.println("<i>Class Name: </i>" + pval + "<br>");
		
		pval = attr.getMinimumCharacters (true, true);
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Minimum Characters:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Minimum Characters: </i>" + pval + "<br>");
		}
		pval = attr.getMaximumCharacters (true, true);
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Maximum Characters:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Maximum Characters: </i>" + pval + "<br>");
		}
		pval = attr.getMinimumValue (true, true);
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Minimum Value:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Minimum Value: </i>" + pval + "<br>");
		}
		pval = attr.getMaximumValue (true, true);
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Maximum Value:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Maximum Value: </i>" + pval + "<br>");
		}
		pval = attr.getFormat (true);
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Format:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Format: </i>" + pval + "<br>");
		}
//		pval = InfoModel.unEscapeProtegeChar(attr.getPattern(true));
		pval = InfoModel.unEscapeProtegeString(attr.getPattern(true));
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Pattern:} " + repLatexSpecCharPattern (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Pattern: </i>" + pval + "<br>");
		}
		pval = "false";
		if (attr.isNilable) {
			pval = "true";			
		}
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Nillable:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Nillable: </i>" + pval + "<br>");
		}
		pval = (String) attr.classConcept;
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
//			prlatex.println("\\textit{Data Element Concept:} " + repLatexSpecChar (pval));
			prlatex.println("\\textit{Attribute Concept:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
//			prhtml.println("<i>Data Element Concept: </i>" + pval + "<br>");
			prhtml.println("<i>Attribute Concept: </i>" + pval + "<br>");
		}
		pval = (String) attr.dataConcept;
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
//			prlatex.println("\\textit{Value Concept:} " + repLatexSpecChar (pval));
			prlatex.println("\\textit{Conceptual Domain:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
//			prhtml.println("<i>Value Concept: </i>" + pval + "<br>");
			prhtml.println("<i>Conceptual Domain: </i>" + pval + "<br>");
		}
		pval = attr.getSteward ();
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Steward:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Steward: </i>" + pval + "<br>");
		}
		pval = attr.getNameSpaceId ();
		if (pval.indexOf("TBD") != 0) {
			prlatex.println("\\begin{flushleft}");
			prlatex.println("\\textit{Namespace Id:} " + repLatexSpecChar (pval));
			prlatex.println("\\end{flushleft}");
			prhtml.println("<i>Namespace Id: </i>" + pval + "<br>");
		}
	}
	
	/**
		*  Print an attributes values
		*/

	private void printAttrValue (AttrDefn lAttr) {
		String plvalue, plvalues = "", phvalue = "", del = "";
		boolean elipflag = false;
		boolean omitValueMeaning = false;

		if (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty()) return; 
		if (lAttr.permValueArr.size() > 1) {
			prhtml.println("<p><i>Values: </i><br>");
		} else {
			prhtml.println("<p><i>Value: </i><br>");
		}
		if ((lAttr.title.compareTo("pattern") == 0) || (lAttr.title.compareTo("formation_rule") == 0)) omitValueMeaning = true;
		
		for (Iterator <PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
			if (lPermValueDefn.value.compareTo("...") == 0) {
				elipflag = true; 
			} else if (lPermValueDefn.value.compareTo("2147483647") == 0) {
				plvalue = repLatexSpecChar("Unbounded");
				plvalues += del + plvalue;
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
				plvalue = repLatexSpecChar(lPermValueDefn.value + lDependClause + lRegistrationStatus);
				plvalues += del + plvalue;
				del = ", ";
				String lValueAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.className + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value).toLowerCase();
				if (! omitValueMeaning) {
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + lDependClause + lRegistrationStatus + "</b></a>" + " - " + lPermValueDefn.value_meaning;
				} else {
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + lDependClause + lRegistrationStatus + "</b></a>";					
				}
				prhtml.println(" - " + phvalue + "<br>");
			}
		}
		if (elipflag) {
			plvalues += del + "...";
			phvalue = "<b>...</b>" + " - " + "The number of values exceeds the reasonable limit for this document.";
			prhtml.println(" - " + phvalue + "<br>");
		}
		
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Value:} " + plvalues);
		prlatex.println("\\end{flushleft}");
  }
	
	/**
	*  Print an attributes Extended values
	*/
	private void printAttrValueExtended (AttrDefn lAttr) {
		String plvalue, plvalues = "", phvalue, del = "";
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
					plvalue = repLatexSpecChar(lPermValueDefn.value);
					plvalues += del + plvalue;
					del = ", ";
					String lValueAnchorString = ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.className + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value).toLowerCase();				
					phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + "</b></a>" + " - " + lPermValueDefn.value_meaning;
					prhtml.println(" - " + phvalue + "<br>");
				}
			}
		}
		if (elipflag) {
			plvalues += del + "...";
			phvalue = "<b>...</b>" + " - " + "The number of values exceeds the reasonable limit for this document.";
			prhtml.println(" - " + phvalue + "<br>");
		}
		
		prlatex.println("\\begin{flushleft}");
		prlatex.println("\\textit{Value:} " + plvalues);
		prlatex.println("\\end{flushleft}");
	}
	
	/**
	*  Print schematron rule for the attribute
	*/
	private void printAttrSchematronRuleMsg(AttrDefn lAttr) {
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			for (Iterator <AssertDefn2> j = lRule.assertArr.iterator(); j.hasNext();) {
				AssertDefn2 lAssert = (AssertDefn2) j.next();
				if (! ((lRule.classTitle.compareTo(lAttr.className) == 0) && lAssert.attrTitle.compareTo(lAttr.title) == 0)) continue;
				if (lAssert.assertMsg.indexOf("TBD") == 0) continue;				
				if (lAssert.specMesg.indexOf("TBD") == 0) continue;				
				if (lAssert.assertType.compareTo("RAW") != 0) continue;
//				prhtml.println("<i>Schematron Rule: " + lAssert.assertMsg + "</i><br>");
				prhtml.println("<i>Schematron Rule: " + lAssert.specMesg + "</i><br>");
				prlatex.println("\\begin{flushleft}");
//				prlatex.println("\\textit{Schematron Rule:} " + repLatexSpecChar(lAssert.assertMsg));
				prlatex.println("\\textit{Schematron Rule:} " + repLatexSpecChar(lAssert.specMesg));
				prlatex.println("\\end{flushleft}");
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
		prlatex.println("\\begin{description}");
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
			prlatex.println("\\item[UNKNOWN]");		
			prhtml.println("<dt><b>" + "Unknown" +  "</b><dd>" +  "Unknown Description");		
		}
		prlatex.println("\\end{description}");		
		prhtml.println("</dl>");		
	}
	
	/**
		*  Print a data element
		*/
	private void printTerm (AttrDefn attr) {
		
		String pltitle, phtitle, desc;
		
		pltitle = attr.title;
//		phtitle = "<a name=\"" + attr.title + "\">" + attr.title + "</a>";
		String lTermAnchorString = ("term_" + attr.title).toLowerCase();

//		phtitle = "<a name=\"" + attr.title + "\">" + attr.title + "</a>";
//		phtitle = "<a name=\"" + lTermAnchorString + "\">" + attr.title + "</a>";
		String titleWithBlanks = DMDocument.replaceString (attr.title, "_", " ");
		phtitle = "<a name=\"" + lTermAnchorString + "\">" + titleWithBlanks + "</a>";
		desc = attr.description;

		prlatex.println("\\item[" + repLatexSpecChar(pltitle) +  "] " + repLatexSpecChar(desc));
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
		* LaTEX replacements
		*/
	
	static String repLatexSpecChar (String aString) {
		String lString = aString;
		lString = DMDocument.replaceString (lString, "#", "\\#");
		lString = DMDocument.replaceString (lString, "$", "\\$");
		lString = DMDocument.replaceString (lString, "%", "\\%");
		lString = DMDocument.replaceString (lString, "^", "\\^");
		lString = DMDocument.replaceString (lString, "&", "\\&");
		lString = DMDocument.replaceString (lString, "_", "\\_");
		lString = DMDocument.replaceString (lString, "{", "\\{");
		lString = DMDocument.replaceString (lString, "}", "\\}");
		lString = DMDocument.replaceString (lString, "~", "\\~");
		return lString;
	}	

	/**
	* LaTEX replacements for pattern
	*/
	public String repLatexSpecCharPattern (String str1) {
		String lString = repLatexSpecChar (str1);
		lString = DMDocument.replaceString (lString, "\\s", "\\\\s");
		lString = DMDocument.replaceString (lString, "\\d", "\\\\d");

		return lString;
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
