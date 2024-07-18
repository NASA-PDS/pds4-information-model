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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Driver for getting document
 *
 */
public class GetDOMModelDoc extends Object {
  SectionDefn docSection;
  SectionContentDefn docSectionContent;
  ModelDefn docModel;
  ModelDefn lModelInfo;

  ArrayList<String> texSectionFormats;

  public GetDOMModelDoc() {

    // set up the Tex markers *** delete the following. ***
    texSectionFormats = new ArrayList<>();
    texSectionFormats.add("\\section");
    texSectionFormats.add("\\subsection");
    texSectionFormats.add("\\subsubsection");
  }

  /**********************************************************************************************************
   * initialize the models (document, information, data dictionaries) and master dictionaries
   * (attribute and class)
   ***********************************************************************************************************/

  public void getModels(String docFileName) throws Throwable {

    // get the Spec Document Information from the Protege DMDocuemt Pins file
    ProtPins protPinsInst = new ProtPins();
    protPinsInst.getProtInst("DOC", "doc", DMDocument.dataDirPath + docFileName);
    HashMap<String, InstDefn> instMap = protPinsInst.instDict;
    // printInst(instMap);
    getDocInfo(instMap);
    getSections(instMap);
    getModels2(instMap);
    getSectionContent(instMap);

    Set<String> set1 = DMDocument.docInfo.modelMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String modelId = iter1.next();
      lModelInfo = DMDocument.docInfo.modelMap.get(modelId);
      // System.out.println("debug modelId:" + modelId + " ModelInfo.type:" + lModelInfo.type + "
      // ModelInfo.filename:" + lModelInfo.filename);
      if (lModelInfo.type.compareTo("ProtPinsGlossary") == 0) {
        lModelInfo.objectid = new ProtPinsGlossary();
        ProtPinsGlossary lobjectid = (ProtPinsGlossary) lModelInfo.objectid;
        lobjectid.getProtPinsGlossary(modelId, DMDocument.dataDirPath + lModelInfo.filename);
      }
    }
    DMDocument.registerMessage("0>info " + "GetDOMModelDoc Done");
  }

  /**********************************************************************************************************
   * get the document info
   ***********************************************************************************************************/

  public void getDocInfo(HashMap<String, InstDefn> instMap) throws Throwable {
    ArrayList<InstDefn> lInstArr = new ArrayList<>(instMap.values());
    for (Iterator<InstDefn> i = lInstArr.iterator(); i.hasNext();) {
      InstDefn localInst = i.next();
      HashMap<String, ArrayList<String>> slotMap = localInst.genSlotMap;
      if (localInst.className.compareTo("Document") == 0) {
        String docId = getSlotMapValue(slotMap.get("identifier"));
        DMDocument.docInfo = new DocDefn(docId);
        DMDocument.docInfo.title = getSlotMapValue(slotMap.get("title"));
        // ArrayList valarr = (ArrayList) localInst.genSlotMap.get("subtitle");
        ArrayList<String> valarr = localInst.genSlotMap.get("subtitle");
        if (valarr != null) {
          DMDocument.docInfo.subTitle = valarr.get(0);
          // blank seems more appropriate
          if (DMDocument.docInfo.subTitle.compareTo("Final") == 0) {
            DMDocument.docInfo.subTitle = "";
          }
        } else {
          DMDocument.docInfo.subTitle = "DRAFT";
        }
        DMDocument.docInfo.description = getSlotMapValue(slotMap.get("description"));
        DMDocument.docInfo.author = getSlotMapValue(slotMap.get("author"));
        DMDocument.docInfo.version = getSlotMapValue(slotMap.get("version"));
      }
    }
  }

  /**********************************************************************************************************
   * get the document sections
   ***********************************************************************************************************/

  public void getSections(HashMap<String, InstDefn> instMap) throws Throwable {

    String tflag;

    ArrayList<InstDefn> lInstArr = new ArrayList<>(instMap.values());
    for (Iterator<InstDefn> i = lInstArr.iterator(); i.hasNext();) {
      InstDefn localInst = i.next();
      if (localInst.className.compareTo("Section") == 0) {
        ArrayList<String> valarr = localInst.genSlotMap.get("identifier");
        String sectionId = valarr.get(0);
        docSection = new SectionDefn(sectionId);
        DMDocument.docInfo.sectionMap.put(sectionId, docSection);
        DMDocument.docInfo.sectionArray.add(sectionId);
        valarr = localInst.genSlotMap.get("title");
        docSection.title = valarr.get(0);
        valarr = localInst.genSlotMap.get("description");
        docSection.description = valarr.get(0);
        valarr = localInst.genSlotMap.get("secType");
        docSection.secType = valarr.get(0);
        valarr = localInst.genSlotMap.get("secSubType");
        docSection.secSubType = valarr.get(0);
        valarr = localInst.genSlotMap.get("selectConstraint");
        docSection.selectConstraint = valarr.get(0);
        valarr = localInst.genSlotMap.get("latexFormat");
        int Iind = Integer.parseInt(valarr.get(0));
        int ind = Iind;
        if (ind == 0) {
          docSection.texFormatInd = 0;
        } else {
          ind = ind - 1;
          if (ind < 0 || ind >= texSectionFormats.size()) {
            docSection.texFormatInd = 0;
          } else {
            docSection.texFormatInd = ind;
          }
        }
        valarr = localInst.genSlotMap.get("secTOCFlag");
        tflag = valarr.get(0);
        if (tflag.compareTo("true") == 0) {
          docSection.secTOCFlag = true;
        } else {
          docSection.secTOCFlag = false;
        }
        valarr = localInst.genSlotMap.get("subSecTOCFlag");
        tflag = valarr.get(0);
        if (tflag.compareTo("true") == 0) {
          docSection.subSecTOCFlag = true;
        } else {
          docSection.subSecTOCFlag = false;
        }
        valarr = localInst.genSlotMap.get("includeFlag");
        tflag = valarr.get(0);
        if (tflag.compareTo("true") == 0) {
          docSection.includeFlag = true;
        } else {
          docSection.includeFlag = false;
        }
        valarr = localInst.genSlotMap.get("imageFileName");
        docSection.imageFileName = valarr.get(0);
        if (docSection.imageFileName.compareTo("none") == 0) {
          docSection.imageFlag = false;
        } else {
          docSection.imageFlag = true;
          valarr = localInst.genSlotMap.get("imageCaption");
          docSection.imageCaption = valarr.get(0);
        }
        valarr = localInst.genSlotMap.get("Section_Model_Content_Id");
        if (valarr != null) {
          for (Iterator<String> j = valarr.iterator(); j.hasNext();) {
            String val = j.next();
            docSection.sectionModelContentId.add(val);
          }
        }
      }
    }
  }

  /**********************************************************************************************************
   * get the document models
   ***********************************************************************************************************/

  public void getModels2(HashMap<String, InstDefn> instMap) throws Throwable {

    ArrayList<InstDefn> lInstArr = new ArrayList<>(instMap.values());
    for (Iterator<InstDefn> i = lInstArr.iterator(); i.hasNext();) {
      InstDefn localInst = i.next();
      if (localInst.className.compareTo("Model") == 0) {
        ArrayList<String> valarr = localInst.genSlotMap.get("identifier");
        String modelId = valarr.get(0);
        docModel = new ModelDefn(modelId);
        DMDocument.docInfo.modelMap.put(modelId, docModel);
        valarr = localInst.genSlotMap.get("type");
        docModel.type = valarr.get(0);
        valarr = localInst.genSlotMap.get("filename");
        docModel.filename = valarr.get(0);
        valarr = localInst.genSlotMap.get("DDIncludeFlag");
        String tflag = valarr.get(0);
        if (tflag.compareTo("true") == 0) {
          docModel.ddincludeflag = true;
        } else {
          docModel.ddincludeflag = false;
        }
      }
    }
  }

  /**********************************************************************************************************
   * get the document section contents
   ***********************************************************************************************************/

  public void getSectionContent(HashMap<String, InstDefn> instMap) throws Throwable {

    ArrayList<InstDefn> lInstArr = new ArrayList<>(instMap.values());
    for (Iterator<InstDefn> i = lInstArr.iterator(); i.hasNext();) {
      InstDefn localInst = i.next();

      // System.out.println("debug getSectionContent instRDFId:" + instRDFId);
      if (localInst.className.compareTo("Section_Model_Content") == 0) {
        ArrayList<String> valarr = localInst.genSlotMap.get("identifier");
        String contentId = valarr.get(0);
        docSectionContent = new SectionContentDefn(contentId);
        DMDocument.docInfo.sectionContentMap.put(contentId, docSectionContent);
        valarr = localInst.genSlotMap.get("Model_Identifier");
        docSectionContent.modelId = valarr.get(0);
        valarr = localInst.genSlotMap.get("Include_Class_Type");
        if (valarr != null) {
          for (Iterator<String> j = valarr.iterator(); j.hasNext();) {
            String val = j.next();
            docSectionContent.includeClassType.add(val);
          }
        }
        valarr = localInst.genSlotMap.get("Include_Class_Id");
        if (valarr != null) {
          for (Iterator<String> j = valarr.iterator(); j.hasNext();) {
            String val = j.next();
            docSectionContent.includeClassId.add(val);
          }
        }
        valarr = localInst.genSlotMap.get("Exclude_Class_Id");
        if (valarr != null) {
          for (Iterator<String> j = valarr.iterator(); j.hasNext();) {
            String val = j.next();
            docSectionContent.excludeClassId.add(val);
          }
        }
      }
    }
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

  public void printInst(HashMap<String, InstDefn> instMap) throws Throwable {
    Set<String> set1 = instMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String instRDFId = iter1.next();
      System.out.println("\ndebug instRDFId:" + instRDFId);
      InstDefn localInst = instMap.get(instRDFId);
      System.out.println("      rdfIdentifier:" + localInst.rdfIdentifier);
      System.out.println("      identifier:" + localInst.identifier);
      System.out.println("      title:" + localInst.title);
      // System.out.println(" classifiedIdentifier:" + localInst.classifiedIdentifier);
      System.out.println("      className:" + localInst.className);
      System.out.println("      description:" + localInst.description);
      Set<String> set2 = localInst.genSlotMap.keySet();
      Iterator<String> iter2 = set2.iterator();
      while (iter2.hasNext()) {
        String aname = iter2.next();
        System.out.println("      attribute:" + aname);
        ArrayList<String> valarr = localInst.genSlotMap.get(aname);
        if (valarr != null) {
          for (Iterator<String> i = valarr.iterator(); i.hasNext();) {
            String val = i.next();
            System.out.println("        val:" + val);
          }
        }
      }
    }
  }
}
