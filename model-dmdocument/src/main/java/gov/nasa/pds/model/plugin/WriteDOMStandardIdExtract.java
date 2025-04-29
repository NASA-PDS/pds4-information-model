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
import java.util.Iterator;
import java.util.TreeMap;

class WriteDOMStandardIdExtract extends Object {
  String PVLStmt;
  PrintWriter prPVL;
  TreeMap<String, String> lReportStatementMap = new TreeMap<>();
  int lineNum = 100000;
  Integer lineNumI;
  // String del = "\", \"", delBeg = "\"", delEnd = "\"";
  String del = "|", delBeg = "", delEnd = "";

  public WriteDOMStandardIdExtract() {
    return;
  }

  public void writeExtractFile() throws java.io.IOException {
    writeExtractFileBegin();
    writeExtractFileBody();
    writeExtractFileEnd();
  }

  public void writeExtractFileBegin() throws java.io.IOException {
    // Write the files consisting of individual classes
    String lFileName = DMDocument.getOutputDirPath() + "export/" + "StandardId" + "_"
        + DOMInfoModel.lab_version_id + ".txt";
    prPVL =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    PVLStmt =
        delBeg + "Standard Id" + del + "Description" + del + "Class" + del + "Attribute" + delEnd;
    prPVL.println(PVLStmt);
  }

  public void writeExtractFileBody() {
    // Write the files consisting of individual classes
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isSchema1Class) {
        // System.out.println("\ndebug writeExtractFileBody lClass.identifier:" +
        // lClass.identifier);
        getExtractFromClass(lClass, new ArrayList<DOMClass>(), prPVL);
      }
    }
  }

  public void writeExtractFileEnd() {
    // Write the files consisting of individual classes
    ArrayList<String> lReportStatementMapTreeArr = new ArrayList<>(lReportStatementMap.values());
    for (Iterator<String> j = lReportStatementMapTreeArr.iterator(); j.hasNext();) {
      String lReportStatement = j.next();
      prPVL.println(lReportStatement);
    }
    prPVL.close();
  }

  public void getExtractFromClass(DOMClass lClass, ArrayList<DOMClass> visitedClassList,
      PrintWriter prPVL) {
    // System.out.println("debug getExtractFromClass lClass.identifier:" + lClass.identifier);
    if (visitedClassList.contains(lClass)) {
      return;
    }
    visitedClassList.add(lClass);

    getExtractFromAttr(lClass, prPVL);

    // get associated classes
    ArrayList<DOMAssocClassDefn> lAssocClassArr =
        DOMInfoModel.getSortedAlphaClassAssocClassArr(lClass);
    if (lAssocClassArr != null) {
      for (Iterator<DOMAssocClassDefn> j = lAssocClassArr.iterator(); j.hasNext();) {
        DOMAssocClassDefn lAssocClass = j.next();
        ArrayList<DOMClass> visitedClassListCopy = new ArrayList<>();
        for (Iterator<DOMClass> k = visitedClassList.iterator(); k.hasNext();) {
          visitedClassListCopy.add(k.next());
        }
        getExtractFromClass(lAssocClass.assocClass, visitedClassListCopy, prPVL);
      }
    }
  }

  public void getExtractFromAttr(DOMClass lClass, PrintWriter prPVL) {
    if (lClass.steward.compareTo("ops") == 0) {
      return;
    }
    DOMAttr lAttr = null;
    for (Iterator<DOMProp> i = lClass.ownedAttrArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      if (!lProp.isAttribute) {
        continue;
      }
      lAttr = (DOMAttr) lProp.domObject;
      if (lAttr.title.indexOf("standard_id") < 0) {
        continue;
      }
      if (!lAttr.permValueArr.isEmpty()) {
        for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
          PermValueDefn lPermValueDefn = j.next();
          // PVLStmt = lClass.title + " - " + lAttr.title + " - " + lPermValueDefn.value + " - " +
          // lPermValueDefn.value_meaning;
          // PVLStmt = lPermValueDefn.value + " - " + lPermValueDefn.value_meaning + " - " +
          // lClass.title + " - " + lAttr.title;
          PVLStmt = delBeg + lPermValueDefn.value + del + lPermValueDefn.value_meaning + del
              + lClass.title + del + lAttr.title + delEnd;
          lineNum++;
          lineNumI = new Integer(lineNum);
          // lReportStatementMap.put(lPermValueDefn.value + "." + lineNumI, PVLStmt);
          lReportStatementMap.put(PVLStmt, PVLStmt);
          // prPVL.println(PVLStmt);
        }
      }
    }
    return;
  }
}
