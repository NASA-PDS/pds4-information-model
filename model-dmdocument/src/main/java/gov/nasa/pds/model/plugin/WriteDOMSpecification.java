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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Driver for getting document
 *
 */
public class WriteDOMSpecification extends Object {

  DocDefn docInfo;

  ArrayList<String> begSectionFormats;
  ArrayList<String> endSectionFormats;
  ArrayList<String> emwfHTML;

  boolean includeAllAttrFlag = false;
  boolean lDebugPrint = false;
  int figNum;
  Date rTodaysDate;
  String sTodaysDate;
  ArrayList<String> itemNum;

  PrintWriter prhtml;

  public WriteDOMSpecification(DocDefn lDocInfo) {
    docInfo = lDocInfo;

    // set up the HTML Headers
    begSectionFormats = new ArrayList<>();
    begSectionFormats.add("<H2>");
    begSectionFormats.add("<H3>");
    begSectionFormats.add("<H4>");

    endSectionFormats = new ArrayList<>();
    endSectionFormats.add("</H2>");
    endSectionFormats.add("</H3>");
    endSectionFormats.add("</H4>");

    emwfHTML = new ArrayList<>();
    emwfHTML.add("<center> <i><b>i<sub>x</sub>A</b>cos(&omega;t-<b>kr</b>-&phi;</i>) </center>");
    emwfHTML.add("<i><b>i<sub>x</sub></b></i>");
    emwfHTML.add("<i>A</i>");
    emwfHTML.add("<i>&omega;</i>");
    emwfHTML.add("<i><b>k</b></i>");
    emwfHTML.add("<i>&phi;</i>");

    figNum = 0;
    rTodaysDate = new Date();
    sTodaysDate = rTodaysDate.toString();
    itemNum = new ArrayList<>();
  }

  /**********************************************************************************************************
   * print the various artifacts
   ***********************************************************************************************************/

  public void printArtifacts() throws java.io.IOException {
    String phtitle;

    // FileWriter fw = new FileWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    // To specify these values yourself, construct an OutputStreamWriter on a FileOutputStream.

    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelSpec_DOM;
    prhtml =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    // Start writing the html document

    prhtml.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">");
    prhtml.println("<html>");
    prhtml.println("<head>");
    prhtml.println("<title>" + DMDocument.imSpecDocTitle + "</title>");
    prhtml.println("<p align=center>");
    prhtml.println("<h1 align=center>" + DMDocument.imSpecDocTitle + "</h1><br><br>");
    prhtml.println("<h2 align=center>" + DMDocument.imSpecDocAuthor + "</h2><br>");
    prhtml.println("<h2 align=center>" + sTodaysDate + "</h2><br><br><br>");
    prhtml.println("<h2 align=center>" + DMDocument.imSpecDocSubTitle + "</h2><br>");
    prhtml.println("<h2 align=center>" + "Version "
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</h2><br>");
    prhtml.println("</p>");
    prhtml.println("</head>");
    prhtml.println("<body bgcolor=#FFFFF0>");

    // Print the *** TABLE OF CONTENTS *** for the HTML file
    prhtml.println("<p><h2>" + "Table of Contents" + "</h2></p>");
    prhtml.println("<ul>");

    // get each section
    Collections.sort(docInfo.sectionArray);
    itemNumAdd(itemNum);
    for (Iterator<String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
      String secId = i.next();
      SectionDefn secInfo = docInfo.sectionMap.get(secId);
      if (!secInfo.includeFlag) {
        continue;
      }
      if (secInfo.secTOCFlag) {
        itemNumPlus(itemNum);
      }
      if (secInfo.secType.compareTo("text") == 0) {
        if (secInfo.secTOCFlag) {
          phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "\">"
              + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "</a>";
          prhtml.println("<b>" + phtitle + "</b><br>");
        }
      } else if (secInfo.secType.compareTo("model") == 0) {
        if (secInfo.secTOCFlag) {
          phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "\">"
              + printItemNum(itemNum) + "&nbsp;&nbsp;" + secInfo.title + "</a>";
          prhtml.println("<b>" + phtitle + "</b><br>");
        }
        if (secInfo.secSubType.compareTo("table") == 0) {
          if (secInfo.subSecTOCFlag) {
            printModelTOC(getPrintClassArr(secInfo), secInfo);
          }
        }
      }
    }
    itemNumRemove(itemNum);

    // Print the *** Document Contents ***
    // iterate through sections and print them
    itemNum.clear();
    itemNumAdd(itemNum);
    for (Iterator<String> i = docInfo.sectionArray.iterator(); i.hasNext();) {
      String secId = i.next();
      SectionDefn secInfo = docInfo.sectionMap.get(secId);
      if (!secInfo.includeFlag) {
        continue;
      }
      if (secInfo.secTOCFlag) {
        itemNumPlus(itemNum);
      }
      if (secInfo.secType.compareTo("text") == 0) {
        printStandardSection(secInfo, itemNum);
      } else if (secInfo.secType.compareTo("model") == 0) {
        printStandardSection(secInfo, itemNum);
        if (secInfo.secSubType.compareTo("table") == 0) {
          printModelTable(getPrintClassArr(secInfo), secInfo, itemNum);
        } else if (secInfo.secSubType.compareTo("hierarchy") == 0) {
          printClassHierarchySection(getPrintClassArr(secInfo), secInfo);
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
   * routines for printing the model
   ***********************************************************************************************************/

  /**
   * Print standard section
   */
  public void printStandardSection(SectionDefn imsec, ArrayList<String> itemNum) {
    // print the html
    if (imsec.secTOCFlag) {
      String phtitle = "<a name=\"" + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "\">"
          + printItemNum(itemNum) + "&nbsp;&nbsp;" + imsec.title + "</a>";
      prhtml.println(begSectionFormats.get(imsec.texFormatInd) + phtitle
          + endSectionFormats.get(imsec.texFormatInd));
    }
    String phdesc = replaceString(imsec.description, "\\newline", "<p>");
    prhtml.println("<p>" + insertEMWF(phdesc, emwfHTML) + "</p>");

    // insert the graphic diagram if one exists
    if (imsec.imageFlag) {
      prhtml.println("<p><img align=bottom src=\"" + imsec.imageFileName + ".jpg\">");
      figNum++;
      prhtml.println("<p><b>Figure " + figNum + "&nbsp;&nbsp;" + imsec.imageCaption + "</b></p>");
    }
  }

  /**
   * Print Class Hierarchy
   */

  public void printClassHierarchySection(ArrayList<DOMClass> printClassArr, SectionDefn secInfo) {
    String concatClassName;
    int numclass = 0;

    HashMap<String, String> objHierArray = new HashMap<>();
    HashMap<String, Integer> classParentCountMap = new HashMap<>();
    ArrayList<String> concatNameArray = new ArrayList<>();
    ArrayList<String> accumClassNameArr;
    for (Iterator<DOMClass> i = printClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      accumClassNameArr = new ArrayList<>();
      for (Iterator<DOMClass> j = lClass.superClassHierArr.iterator(); j.hasNext();) {
        DOMClass locClass2 = j.next();
        accumClassNameArr.add(locClass2.title);
      }
      accumClassNameArr.add(lClass.title);
      Integer numSupClass = lClass.superClassHierArr.size();
      classParentCountMap.put(lClass.rdfIdentifier, numSupClass);
      concatClassName = "";
      for (Iterator<String> j = accumClassNameArr.iterator(); j.hasNext();) {
        String name = j.next();
        concatClassName += name;
      }
      objHierArray.put(concatClassName, lClass.rdfIdentifier);
      concatNameArray.add(concatClassName);
    }
    Collections.sort(concatNameArray);
    for (Iterator<String> i = concatNameArray.iterator(); i.hasNext();) {
      concatClassName = i.next();
      String lClassId = objHierArray.get(concatClassName);
      numclass++;
      String indent = "+ ";
      Integer numSupClass = classParentCountMap.get(lClassId);
      int jlmt = numSupClass.intValue();
      for (int j = 0; j < jlmt; j++) {
        indent += "+ ";
      }

      // get lClassAnchorString
      DOMClass lClass = DOMInfoModel.masterDOMClassMap.get(lClassId);
      if (lClass == null) {
        continue;
      }
      String lClassAnchorString =
          ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
      String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
      prhtml.println("<p>" + indent + phtitle + "</p>");
    }
    prhtml.println(
        "<p>" + "The class hierarchy above includes " + numclass + " unique classes." + "</p>");
  }

  /**
   * Get the classes to be printed
   */
  public ArrayList<DOMClass> getPrintClassArr(SectionDefn secInfo) {
    TreeMap<String, DOMClass> lClassSortMap = new TreeMap<>();
    for (Iterator<String> i = secInfo.sectionModelContentId.iterator(); i.hasNext();) {
      String cid = i.next();
      SectionContentDefn content = docInfo.sectionContentMap.get(cid);
      Collection<DOMClass> classArr = DOMInfoModel.masterDOMClassIdMap.values();
      for (Iterator<DOMClass> j = classArr.iterator(); j.hasNext();) {
        DOMClass lClass = j.next();
        if (lClass.isInactive) {
          continue;
        }
        if (content.includeClassId.contains(lClass.section)) {
          lClassSortMap.put(lClass.title, lClass);
        }
      }
    }
    return new ArrayList<>(lClassSortMap.values());
  }

  /**
   * Print a section of the table of contents
   */

  public void printModelTOC(ArrayList<DOMClass> printClassArr, SectionDefn secInfo) {
    itemNumAdd(itemNum);
    for (Iterator<DOMClass> i = printClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get lClassAnchorString
      String lClassAnchorString =
          ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
      itemNumPlus(itemNum);
      String phtitle = "<a href=\"#" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClassAnchorString
          + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;" + lClass.title + "</a>";
      prhtml.println("<b>&nbsp;&nbsp;" + phtitle + "</b><br>");
    }
    itemNumRemove(itemNum);
  }

  /**
   * Print a model in table format as a subsection (added values and fixed card)
   */
  public void printModelTable(ArrayList<DOMClass> printClassArr, SectionDefn secInfo,
      ArrayList<String> itemNum) {
    int levelind = secInfo.texFormatInd;
    itemNumAdd(itemNum);
    for (Iterator<DOMClass> i = printClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      itemNumPlus(itemNum);
      printClass(lClass.rdfIdentifier, levelind, itemNum);
    }
    itemNumRemove(itemNum);
  }

  /**
   * Print out a class
   */

  private void printClass(String lClassId, int levelind, ArrayList<String> itemNum) {
    DOMClass lClass = DOMInfoModel.masterDOMClassMap.get(lClassId);
    if (lClass == null) {
      return;
    }
    String lClassAnchorString =
        ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
    if ((levelind + 1) < begSectionFormats.size()) {
      levelind += 1;
    }
    String lRegistrationStatus = "";
    if (lClass.registrationStatus.compareTo("Retired") == 0) {
      lRegistrationStatus = DMDocument.Literal_DEPRECATED;
    }

    String prole = "Abstract";
    if (! lClass.isAbstract) prole = "Concrete";
    prhtml.println("<a name=\"" + lClassAnchorString + "\">" + "&nbsp;&nbsp;" + "</a>"); // needed
                                                                                         // to link
                                                                                         // from
                                                                                         // within
                                                                                         // objects
    prhtml.println(begSectionFormats.get(levelind) + "<a name=\"" + printItemNum(itemNum)
        + "&nbsp;&nbsp;" + lClassAnchorString + "\">" + printItemNum(itemNum) + "&nbsp;&nbsp;"
        + lClass.title + lRegistrationStatus + "</a>" + endSectionFormats.get(levelind));
    prhtml.println("<p><i><b>Root Class:</b></i>" + lClass.docSecType + "<br>");
    prhtml.println("<i><b>Role:</b></i>" + prole + "<br>");
    String phdesc = lClass.definition;
    int s = phdesc.indexOf("[CHG:");
    if (s >= 0) {
      int e = phdesc.indexOf(']', s);
      String change = phdesc.substring(s + 5, e);
      String phchange = "<a href=\"#" + change + "\">" + change + "</a>";
      phdesc = replaceString(phdesc, change, phchange);
    }
    s = phdesc.indexOf("[ANO:");
    if (s >= 0) {
      int e = phdesc.indexOf(']', s);
      String change = phdesc.substring(s + 5, e);
      String phchange = "<a href=\"#" + change + "\">" + change + "</a>";
      phdesc = replaceString(phdesc, change, phchange);
    }
    prhtml.println("<i><b>Class Description:</b></i>" + phdesc + "<br>");
    prhtml.println("<i><b>Steward:</b></i>" + lClass.steward + "<br>");
    prhtml.println("<i><b>Namespace Id:</b></i>" + lClass.nameSpaceIdNC + "<br>");
    prhtml.println("<i><b>Version Id:</b></i>" + lClass.versionId + "<br>");
    String lUnitsOf = getClassUnits(lClass);
    if (lUnitsOf == null) {
      prhtml.println("</p>");
    } else {
      prhtml.println("<i><b>Unit of Measure Type:</b></i>" + lUnitsOf + "</p>");
    }
    printHTMLTableHdr();

    // print attributes for this object
    ArrayList<DOMClass> lClassArr = new ArrayList<>(lClass.superClassHierArr);
    lClassArr.add(lClass);
    PrintOneClassHierarchy(lClassArr, "Hierarchy");
    PrintSubclasses(lClass.subClassHierArr, "Subclass");
    printTableRow(lClass.ownedAttrArr, "Attribute");
    printTableRow(lClass.inheritedAttrArr, "Inherited Attribute");
    printAssoc(lClass.ownedAssocArr, "Association");
    printAssoc(lClass.inheritedAssocArr, "Inherited Association");
    
    // find and print reference for this class
    ArrayList<DOMClass> refClasses = getClassReferences(lClass.identifier);
    printSimpleTableRow(refClasses, "Referenced from", true);
    prhtml.println("</table>");
  }

  public String getClassUnits(DOMClass lClass) {
    if (lClass.title.compareTo("Vector_Cartesian_3_Acceleration") == 0) {
      return "Units_of_Acceleration";
    }
    if (lClass.title.compareTo("Vector_Cartesian_3_Position") == 0) {
      return "Units_of_Length";
    }
    if (lClass.title.compareTo("Vector_Cartesian_3_Velocity") == 0) {
      return "Units_of_Velocity";
    }
    return null;
  }

  /**
   * Print hierarchy
   */
  private void PrintOneClassHierarchy(ArrayList<DOMClass> lClassArr, String relation) {
    String phRelation = relation;
    boolean firstflag = true;
    String indent = "";

    for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get lClassAnchorString
      String lClassAnchorString =
          ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();

      String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
      printHTMLTableRow(phRelation, indent + phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
      indent = indent + ". ";
      if (firstflag) {
        firstflag = false;
        phRelation = "&nbsp;";
      }
    }
    if (firstflag) {
      printHTMLTableRow(phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
    }
  }

  /**
   * Print subclasses
   */
  private void PrintSubclasses(ArrayList<DOMClass> subClasses, String relation) {
    String phRelation = relation;
    boolean firstflag = true;

    TreeMap<String, DOMClass> lDOMClassMap = new TreeMap<>();
    for (DOMClass lClass : subClasses) {
      if (lClass.isInactive) continue;
      String className = lClass.title;
      lDOMClassMap.put(className, lClass);
    }
    ArrayList<DOMClass> lSortClassArr = new ArrayList<>(lDOMClassMap.values());

    for (Iterator<DOMClass> i = lSortClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get lClassAnchorString
      String lClassAnchorString =
          ("class_" + lClass.nameSpaceIdNC + "_" + lClass.getTitle()).toLowerCase();

      String phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.getTitle() + "</a>";
      printHTMLTableRow(phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
      if (firstflag) {
        firstflag = false;
        phRelation = "&nbsp;";
      }
    }
    if (firstflag) {
      printHTMLTableRow(phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
    }
  }

  /**
   * Print a table row
   *
   */
  private void printTableRow(ArrayList<DOMProp> lPropArr, String relation) {
    TreeMap<String, DOMProp> lPropSortMap = new TreeMap<>();
    String phRelation = relation;
    String phtitle;
    String phindicator = "&nbsp;";
    boolean firstflag = true;

    // sort the local attributes
    for (DOMProp lProp: lPropArr) {
      lPropSortMap.put(lProp.domObject.rdfIdentifier, lProp);
    }
    
    // Process each sorted DOMProp
    ArrayList<DOMProp> lSortPropArr = new ArrayList<>(lPropSortMap.values());
    for (DOMProp lProp: lSortPropArr) {
      String lRegistrationStatus = "";
      if (lProp.registrationStatus.compareTo("Retired") == 0) lRegistrationStatus = DMDocument.Literal_DEPRECATED;

      DOMAttr lDOMAttr = (DOMAttr) lProp.domObject;
      phtitle = "<a href=\"#" + lDOMAttr.anchorString + "\">" + lDOMAttr.getTitle()
          + lRegistrationStatus + "</a>";
      String cmin = lDOMAttr.cardMin;
      String cmax = lDOMAttr.cardMax;
      String phcard = cmin.equals(cmax) ? cmin : (cmin + ".." + cmax);
      if (phcard.equals("1..0")) phcard = "none";
      
      // attribute is restricted in a subclass as opposed to
      // restricted relative to the attribute in the "USER" class
      if (lProp.isRestrictedInSubclass) phindicator += "R";
      
	  ArrayList<DOMProp> lValClassArr = new ArrayList<>(lDOMAttr.domPermValueArr);
      if (lValClassArr.isEmpty()) {
        String lClassRdfIdentifier = DMDocument.rdfPrefix + "." + "UNK" + "." + "DUMMY";
        DOMProp lDummyClass = new DOMProp();
        lDummyClass.rdfIdentifier = lClassRdfIdentifier;
        lDummyClass.title = "dummy";
        lValClassArr.add(lDummyClass);
      }
      
      for (DOMProp lDOMProp : lValClassArr) {
          String phvalue = printPermissibleValues(lDOMAttr, lProp, lDOMProp);
          printHTMLTableRow(phRelation, phtitle, phcard, phvalue, phindicator);
          firstflag = false;
          phRelation = phtitle = phcard = phindicator = phvalue = "&nbsp;";
      } 
    }
    if (firstflag) {
      printHTMLTableRow(phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
    }
  }
  
  private String printPermissibleValues(DOMAttr attr, DOMProp prop, DOMProp valueProp) {
	    if (!(valueProp.domObject instanceof DOMPermValDefn)) {
	        return "&nbsp;";
	    }

	    DOMPermValDefn permVal = (DOMPermValDefn) valueProp.domObject;
	    String value = permVal.value;
	    if (value.isEmpty()) {
	        return "&nbsp;";
	    }

	    String anchorString = attr.anchorString;

	    // Special handling for known attribute types
	    if ("data_type".equals(attr.title) || 
	        "value_data_type".equals(attr.getTitle()) || 
	        "unit_of_measure_type".equals(attr.getTitle()) || 
	        "product_class".equals(attr.getTitle())) {
	        
	        String classId = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, value);
	        DOMClass domClass = DOMInfoModel.masterDOMClassIdMap.get(classId);
	        if (domClass != null) {
	            anchorString = domClass.anchorString;
	        } else {
	            Utility.registerMessage("1>error printTableRow - Component Class is missing - lClassId:" + classId);
	        }
	    } else {
	        anchorString = ("value_" + attr.classNameSpaceIdNC + "_" + attr.parentClassTitle + "_" 
	                      + prop.nameSpaceIdNC + "_" + attr.getTitle() + "_" + value).toLowerCase();
	    }

	    return "<a href=\"#" + anchorString + "\">" + replaceString(value, "μ", "&mu;") + "</a>";
	}

  private void printAssoc(ArrayList<DOMProp> lPropArr, String relation) {
    String phRelation = relation;
    String phtitle;
    String phcard = "&nbsp;";
    String phvalue = "&nbsp;";
    String phindicator = "&nbsp;";
    boolean firstflag = true;

    TreeMap<String, DOMProp> lPropSortMap = new TreeMap<>();

    for (Iterator<DOMProp> i = lPropArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      lPropSortMap.put(lProp.rdfIdentifier, lProp);
    }
    ArrayList<DOMProp> lSortPropArr = new ArrayList<>(lPropSortMap.values());
    String lastProp = "&nbsp;";
    for (Iterator<DOMProp> i = lSortPropArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      DOMClass lDOMClass = (DOMClass) lProp.domObject;
      phtitle = "&nbsp;";
      phcard = "&nbsp;";
      phindicator = "&nbsp;";
      if (lProp.isRestrictedInSubclass) { // attribute is restricted in a subclass as opposed to
                                          // restricted relative to the attribute in the "USER"
                                          // class
        phindicator += "R";
      }
      // get lClassAnchorString
      String lClassAnchorString = lDOMClass.anchorString;
      // set attributes anchor string
      if (lProp.attrParentClass == null) {
        lProp.anchorString = "attribute_";
      } else {
        lProp.anchorString =
            ("attribute_" + lProp.classNameSpaceIdNC + "_" + lProp.attrParentClass.title + "_"
                + lDOMClass.nameSpaceIdNC + "_" + lProp.title).toLowerCase();
      }
      if (lastProp.compareTo(lProp.title) != 0) {
        phtitle = "<a href=\"#" + lProp.anchorString + "\">" + lProp.title + "</a>";
      }
      phvalue = "<a href=\"#" + lClassAnchorString + "\">" + lDOMClass.title + "</a>";
      String cmin = lProp.cardMin; // get min card
      String cmax = lProp.cardMax; // get max card
      String cardval = cmin + ".." + cmax;
      if (cardval.compareTo("1..0") == 0) {
        cardval = "none";
      }
      if (cmin.compareTo(cmax) == 0) {
        cardval = cmin;
      }

      if (phtitle.compareTo("&nbsp;") != 0) {
        phcard = cardval;
      }
      lastProp = lProp.title;

      printHTMLTableRow(phRelation, phtitle, phcard, phvalue, phindicator);
      firstflag = false;
      phRelation = "&nbsp;";

    }

    if (firstflag) {
      printHTMLTableRow(phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
    }
  }

  /**
   * Print HTML table Header
   */
  private void printHTMLTableHdr() {
    prhtml.println("<table border=1>");
    prhtml.println(
        "<tr> <th>&nbsp;</th> <th>Entity</th> <th>Card</th> <th>Value/Class</th> <th>Ind</th> </tr>");
  }

  /**
   * Print a HTML table row
   */
  private void printHTMLTableRow(String phRelation, String phtitle, String phcard, String phvalue,
      String phindicator) {
    prhtml.println("<tr><td><b>" + phRelation + "</b></td><td>" + phtitle + "</td><td>" + phcard
        + "</td><td>" + phvalue + "</td><td>" + phindicator + "</td></tr>");
  }

  /**
   * Print a simple table row
   */
  private void printSimpleTableRow(ArrayList<DOMClass> lClassArr, String relation,
      boolean sortflag) {
    String phRelation = relation;
    String phtitle;
    boolean firstflag = true;

    // sort the classes
    TreeMap<String, DOMClass> lSortClassMap = new TreeMap<>();
    for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      lSortClassMap.put(lClass.title, lClass);
    }
    ArrayList<DOMClass> lSortClassArr = new ArrayList<>(lSortClassMap.values());

    for (Iterator<DOMClass> i = lSortClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get lClassAnchorString
      String lClassAnchorString =
          ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();

      phtitle = "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
      printHTMLTableRow(phRelation, phtitle, "&nbsp;", "&nbsp;", "&nbsp;");
      firstflag = false;
      phRelation = "&nbsp;";
      phtitle = "&nbsp;";
    }
    if (firstflag) {
      printHTMLTableRow(phRelation, "none", "&nbsp;", "&nbsp;", "&nbsp;");
    }
  }

  /**
   * Get the references for a class
   */
  private ArrayList<DOMClass> getClassReferences(String lClassId) {
    ArrayList<String> lClassIdArr = new ArrayList<>();
    ArrayList<DOMClass> lClassArr = new ArrayList<>();
    ArrayList<DOMClass> lDOMClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassIdMap.values());
    for (Iterator<DOMClass> i = lDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if ((lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) || lClass.isInactive) {
        continue;
      }
      for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
        DOMProp lProp = j.next();

        ISOClassOAIS11179 lISOClass = lProp.domObject;

        DOMClass lDOMClass = (DOMClass) lISOClass;
        if (lDOMClass.identifier.compareTo(lClassId) != 0) {
          continue;
        }
        if (!lClassIdArr.contains(lClass.identifier)) {
          lClassIdArr.add(lClass.identifier);
          lClassArr.add(lClass);
        }
      }
      for (Iterator<DOMProp> j = lClass.inheritedAssocArr.iterator(); j.hasNext();) {
        DOMProp lProp = j.next();
        ISOClassOAIS11179 lISOClass = lProp.domObject;
        DOMClass lDOMClass = (DOMClass) lISOClass;
        if (lDOMClass.identifier.compareTo(lClassId) != 0) {
          continue;
        }
        if (!lClassIdArr.contains(lClass.identifier)) {
          lClassIdArr.add(lClass.identifier);
          lClassArr.add(lClass);
        }

      }
    }
    return lClassArr;
  }

  /**********************************************************************************************************
   * routines for printing the data dictionary
   ***********************************************************************************************************/

  public void printDataDict() {
    boolean pflag = false;
    // write the description heading
    prhtml.println("<dl>");
    // get property array sorted by "attribute", namespace, attr title, namespace, class title
    ArrayList<DOMProp> sortedList = DOMInfoModel.getPropArrByTitleStewardClassSteward();
    for (Iterator<DOMProp> i = sortedList.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      pflag = true;
      printDataElement(lProp);
    }
    if (!pflag) {
      prhtml.println("<dt><b>" + "Unknown" + "</b><dd>" + "Unknown Description");
    }
    prhtml.println("</dl>");
  }

  /**
   * Print a data element
   */
  private void printDataElement(DOMProp lProp) {
    boolean fflag, altflag;
    String phtitle, desc, altlist;

    if (lProp.domObject != null) {
      if (lProp.domObject instanceof DOMAttr) {
        DOMAttr lAttr = (DOMAttr) lProp.domObject;
        if (!lAttr.isInactive) {
          if (lAttr.isUsedInClass || includeAllAttrFlag) {

            // get lClassAnchorString
            DOMClass lClass = lAttr.attrParentClass;
            String lClassAnchorString =
                ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
            String lClassHrefString =
                "<a href=\"#" + lClassAnchorString + "\">" + lClass.title + "</a>";
            String lRegistrationStatus = "";
            if (lAttr.registrationStatus.compareTo("Retired") == 0) {
              lRegistrationStatus = " - " + DMDocument.Literal_DEPRECATED;
            }
            phtitle = "<a name=\"" + lAttr.anchorString + "\"><b>" + lAttr.title
                + lRegistrationStatus + "</b> in " + lClassHrefString + "</a>";
            desc = lAttr.definition;
            altflag = false;
            altlist = "";
            fflag = true;
            HashMap lmap = lAttr.genAttrMap;
            if (lmap != null) {
              ArrayList<String> attraliasarr = (ArrayList) lmap.get("alias_name");
              if (attraliasarr != null) {
                for (Iterator<String> i = attraliasarr.iterator(); i.hasNext();) {
                  if (fflag) {
                    fflag = false;
                    altflag = true;
                    altlist = " {Alternatives: ";
                  } else {
                    altlist += ", ";
                  }
                  altlist += i.next();
                }
                if (altflag) {
                  altlist += "} ";
                }
              }
            }
            prhtml.println("<dt>" + phtitle + "<dd><i>" + altlist + "</i>" + desc);
          }

          printAttrType(lProp);
          printAttrUnit(lAttr);
          printAttrMisc(lAttr);
          printAttrValue(lAttr);
          printAttrValueExtended(lAttr);
          printAttrSchematronRuleMsg(lAttr);
        }
      } else {
        DOMClass lDOMClass = lProp.attrParentClass;
        if (!lDOMClass.isInactive) {
          String lClassAnchorString = ("attribute_" + lDOMClass.nameSpaceIdNC + "_"
              + lDOMClass.title + "_" + lDOMClass.nameSpaceIdNC + "_" + lProp.title).toLowerCase();
          String lClassHrefString = "<a href=\"#" + "class_" + lDOMClass.nameSpaceIdNC + "_"
              + lDOMClass.title.toLowerCase() + "\">" + lDOMClass.title + "</a>";
          phtitle = "<a name=\"" + lClassAnchorString + "\"><b>" + lProp.title + "</b> in "
              + lClassHrefString + "</a>";
          desc = lProp.definition;
          altlist = "";
          prhtml.println("<dt>" + phtitle + "<dd><i>" + altlist + "</i>" + desc);
          printAttrType(lProp);
        }
      }
    }
  }

  /**
   * Print an attributes unit
   */

  private void printAttrUnit(DOMAttr attr) {
    String lUnitOfMeasureType;
    if (attr.isAttribute && attr.unit_of_measure_type.indexOf("TBD") != 0
        && attr.unit_of_measure_type.indexOf("none") != 0) {
      lUnitOfMeasureType = attr.unit_of_measure_type;
      prhtml.println("<p><i>Unit of Measure Type: </i>" + lUnitOfMeasureType + "<br>");

      String lValueString = attr.getUnits(false);
      if (lValueString != null) {
        prhtml.println("<i>Valid Units: </i>" + lValueString + "<br>");
      }
    }

    String pval = "";
    if (attr.isAttribute && attr.default_unit_id.indexOf("TBD") != 0
        && attr.default_unit_id.indexOf("none") != 0) {
      pval = attr.default_unit_id;
      prhtml.println("<i>Specified Unit Id: </i>" + pval + "<br>");
    }
  }

  /**
   * Print an attributes type
   */
  private void printAttrType(DOMProp prop) {
    String phtype;

    if (prop.isAttribute) {
      DOMAttr lAttr = (DOMAttr) prop.domObject;
      if (lAttr.valueType.indexOf("TBD") == 0) {
        return;
      }
      phtype = lAttr.valueType;
    } else {
      phtype = "Association";
    }

    String lClassId = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, phtype);
    DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
    if (lClass != null) {
      String lAnchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
      phtype = "<a href=\"#" + lAnchorString + "\">" + phtype + "</a>";
    } else {
      phtype = "<a href=\"#" + phtype + "\">" + phtype + "</a>";
    }
    prhtml.println("<p><i>Type: </i>" + phtype + "<br>");
  }

  /**
  */

  private void printAttrMisc(DOMAttr attr) {
    String pval = "";
    if (!attr.isAttribute) {
      return;
    }

    pval = attr.parentClassTitle;
    prhtml.println("<i>Class Name: </i>" + pval + "<br>");

    pval = attr.getMinimumCharacters(true, false);
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Minimum Characters: </i>" + pval + "<br>");
    }
    pval = attr.getMaximumCharacters(true, false);
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Maximum Characters: </i>" + pval + "<br>");
    }
    pval = attr.getMinimumValue(true, false);
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Minimum Value: </i>" + pval + "<br>");
    }
    pval = attr.getMaximumValue(true, false);
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Maximum Value: </i>" + pval + "<br>");
    }
    pval = attr.getFormat(true);
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Format: </i>" + pval + "<br>");
    }
    pval = DOMInfoModel.unEscapeProtegeString(attr.getPattern(true));
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
    pval = attr.classConcept;
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Attribute Concept: </i>" + pval + "<br>");
    }
    pval = attr.dataConcept;
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Conceptual Domain: </i>" + pval + "<br>");
    }
    pval = attr.getSteward();
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Steward: </i>" + pval + "<br>");
    }
    pval = attr.getNameSpaceIdNC();
    if (pval.indexOf("TBD") != 0) {
      prhtml.println("<i>Namespace Id: </i>" + pval + "<br>");
    }
  }

  /**
   * Print an attributes values
   */

  private void printAttrValue(DOMAttr lAttr) {
    String phvalue = "";
    boolean elipflag = false;
    ArrayList<DOMProp> lValClassArr = lAttr.domPermValueArr;

    if (lValClassArr.size() > 1) {
      prhtml.println("<p><i>Values: </i><br>");
    } else if (lValClassArr.size() == 1) { // determine if need to print the Value label
      prhtml.println("<p><i>Value: </i><br>");
    }

    for (Iterator<DOMProp> i = lValClassArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();

      if (!(lDOMProp.domObject instanceof DOMPermValDefn)) {
        phvalue = "&nbsp;";

      } else {
        DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lDOMProp.domObject;
        if (lPermValueDefn.value.compareTo("...") == 0) {
          elipflag = true;
        } else if (lPermValueDefn.value.compareTo("2147483647") == 0) {
          phvalue = "<a name=\"" + "" + "\"><b>" + "Unbounded" + "</b></a>" + " - "
              + "There is no bound on the maximum number of characters allowed.";
          prhtml.println(" - " + phvalue + "<br>");

        } else {
          // check if deprecated
          String lRegistrationStatus = "";
          if (lPermValueDefn.registrationStatus.compareTo("Retired") == 0) {
            lRegistrationStatus = " - " + DMDocument.Literal_DEPRECATED;
          }

          // check if dependent
          String lDependValue = lAttr.valueDependencyMap.get(lPermValueDefn.value);
          String lDependClause = "";
          if (lDependValue != null) {
            lDependClause = " (" + lDependValue + ")";
          }
          String lValueAnchorString =
              ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_"
                  + lAttr.nameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value)
                      .toLowerCase();
          String lValue = replaceString(lPermValueDefn.value, "μ", "&mu;");
          String lValueMeaning = replaceString(lPermValueDefn.value_meaning, "μ", "&mu;");
          if (lAttr.title.compareTo("pattern") == 0
              && lPermValueDefn.value_meaning.indexOf("TBD") == 0) {


            phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value
                + lDependClause + lRegistrationStatus + "</b></a>";
          } else {
            phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lValue + lDependClause
                + lRegistrationStatus + "</b></a>" + " - " + lValueMeaning;
          }
          prhtml.println(" - " + phvalue + "<br>");
        }
      }
      if (elipflag) {
        phvalue = "<b>...</b>" + " - "
            + "The number of values exceeds the reasonable limit for this document.";
        prhtml.println(" - " + phvalue + "<br>");
      }
    }
  }


  /**
   * Print an attributes Extended values
   */
  private void printAttrValueExtended(DOMAttr lAttr) {
    String phvalue;
    boolean elipflag = false;

    if (lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty()) {
      return;
    }

    for (Iterator<PermValueExtDefn> i = lAttr.permValueExtArr.iterator(); i.hasNext();) {
      PermValueExtDefn lPermValueExt = i.next();
      if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) {
        return;
      }

      if (lPermValueExt.permValueExtArr.size() > 1) {
        prhtml.println("<p><i>Extended Values for: " + lPermValueExt.xpath + "</i><br>");
      } else {
        prhtml.println("<p><i>Extended Value for: " + lPermValueExt.xpath + "</i><br>");
      }

      // sort the permissible values
      TreeMap<String, PermValueDefn> sortPermValueDefnMap = new TreeMap<>();
      for (PermValueDefn lPermValueDefn : lPermValueExt.permValueExtArr) {
        sortPermValueDefnMap.put(lPermValueDefn.identifier + lPermValueDefn.value, lPermValueDefn);
      }
      ArrayList<PermValueDefn> sortPermValueDefnArr =
          new ArrayList<>(sortPermValueDefnMap.values());
      for (Iterator<PermValueDefn> j = sortPermValueDefnArr.iterator(); j.hasNext();) {
        PermValueDefn lPermValueDefn = j.next();
        if (lPermValueDefn.value.compareTo("...") == 0) {
          elipflag = true;
        } else {
          String lValueAnchorString =
              ("value_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_"
                  + lAttr.nameSpaceIdNC + "_" + lAttr.title + "_" + lPermValueDefn.value)
                      .toLowerCase();
          phvalue = "<a name=\"" + lValueAnchorString + "\"><b>" + lPermValueDefn.value + "</b></a>"
              + " - " + lPermValueDefn.value_meaning;
          prhtml.println(" - " + phvalue + "<br>");
        }
      }
    }
    if (elipflag) {
      phvalue = "<b>...</b>" + " - "
          + "The number of values exceeds the reasonable limit for this document.";
      prhtml.println(" - " + phvalue + "<br>");
    }
  }

  /**
   * Print schematron rule for the attribute
   */
  private void printAttrSchematronRuleMsg(DOMAttr lAttr) {
    ArrayList<DOMRule> lRuleArr = new ArrayList<>(DOMInfoModel.masterDOMRuleMap.values()); // use unique rdfIdentifier
    for (Iterator<DOMRule> i = lRuleArr.iterator(); i.hasNext();) {
      DOMRule lRule = i.next();
      for (Iterator<DOMAssert> j = lRule.assertArr.iterator(); j.hasNext();) {
        DOMAssert lAssert = j.next();
        if (!((lRule.classTitle.compareTo(lAttr.parentClassTitle) == 0)
            && lAssert.attrTitle.compareTo(lAttr.title) == 0)
            || (lAssert.assertMsg.indexOf("TBD") == 0) || (lAssert.specMesg.indexOf("TBD") == 0)
            || (lAssert.assertType.indexOf("RAW") != 0)) {
          continue;
        }

        prhtml.println("<i>Schematron Rule: " + lAssert.specMesg + "</i><br>");
      }
    }
  }

  /**********************************************************************************************************
   * routines for printing the glossary
   ***********************************************************************************************************/

  public void printGlossary(SectionDefn secInfo) {
    boolean pflag = false;

    String cid = secInfo.sectionModelContentId.get(0);
    SectionContentDefn content = docInfo.sectionContentMap.get(cid);
    ModelDefn lModelInfo = docInfo.modelMap.get(content.modelId);
    ProtPinsGlossary lmodel = (ProtPinsGlossary) lModelInfo.objectid;

    // write the description heading
    prhtml.println("<dl>");

    Set<String> set1 = lmodel.glossTitleIdMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String lTitle = iter1.next();
      String lId = lmodel.glossTitleIdMap.get(lTitle);
      DOMAttr lDOMAttr = lmodel.glossMap.get(lId);
      printTerm(lDOMAttr);
      pflag = true;
    }

    if (!pflag) {
      prhtml.println("<dt><b>" + "Unknown" + "</b><dd>" + "Unknown Description");
    }
    prhtml.println("</dl>");
  }

  /**
   * Print a data element
   */
  private void printTerm(DOMAttr lDOMAttr) {

    String phtitle, desc;

    String lTermAnchorString = ("term_" + lDOMAttr.title).toLowerCase();
    String titleWithBlanks = DMDocument.replaceString(lDOMAttr.title, "_", " ");
    phtitle = "<a name=\"" + lTermAnchorString + "\">" + titleWithBlanks + "</a>";
    desc = lDOMAttr.definition;
    prhtml.println("<dt><b>" + phtitle + "</b><dd><i>" + "</i>" + desc);
  }

  /**********************************************************************************************************
   * miscellaneous routines
   ***********************************************************************************************************/

  /**
   * Get Slot Value
   */
  public String getSlotMapValue(ArrayList<String> valarr) {
    if (!(valarr == null || valarr.isEmpty())) {
      return valarr.get(0);
    }
    return null;
  }

  /**
   * bump current level by one
   */
  public void itemNumPlus(ArrayList<String> itemNum) {
    int ind = itemNum.size() - 1;
    Integer ival = Integer.parseInt(itemNum.get(ind));
    ival += 1;
    itemNum.set(ind, ival.toString());
  }

  /**
   * decrement current level by one
   */
  public void itemNumMinus(ArrayList<String> itemNum) {
    int ind = itemNum.size() - 1;
    Integer ival = Integer.parseInt(itemNum.get(ind));
    ival -= 1;
    itemNum.set(ind, ival.toString());
  }

  /**
   * increase number of levels by one
   */
  public void itemNumAdd(ArrayList<String> itemNum) {

    itemNum.add("0");
  }

  /**
   * decrease number of levels by one
   */
  public void itemNumRemove(ArrayList<String> itemNum) {

    int ind = itemNum.size() - 1;

    itemNum.remove(ind);
  }

  /**
   * print item number
   */
  public String printItemNum(ArrayList<String> itemNum) {

    boolean fflag = true;
    String itemNumString;

    itemNumString = "";
    for (Iterator<String> i = itemNum.iterator(); i.hasNext();) {
      String snum = i.next();
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
  public String insertEMWF(String str1, ArrayList<String> formSet) {
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
        ctc = sbuff1.charAt(p1);
        int ind = Integer.parseInt(ctc.toString());
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

  static String replaceString(String str, String pattern, String replace) {
    int s = 0;
    int e = 0;
    StringBuffer result = new StringBuffer();

    while ((e = str.indexOf(pattern, s)) >= 0) {
      result.append(str.substring(s, e));
      result.append(replace);
      s = e + pattern.length();
    }
    result.append(str.substring(s));
    return result.toString();
  }
}
