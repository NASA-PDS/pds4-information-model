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
import java.util.TreeMap;

/**
 * Creates the in memory 11179 DD (InstDefn) from a Protege 11179 DD getProtPins11179DD - gets a
 * parsed a pins file - 11179 data dictionary
 */

class ProtPinsDOM11179DD extends Object {
  ProtPins protPinsInst;

  public ProtPinsDOM11179DD() {

    // Parse Protege Instance File - Parse InstDefn stored as follows.
    // lInst.className - e.g. DataElement
    // + lRegAuthId + "." + lInst.className + "." + lInst.title;
    // ++ InstDefn
    DOMInfoModel.master11179DataDict = new TreeMap<>();
    return;
  }

  /**
   * Parses a pins file - 11179 data dictionary Updates/Creates a data dictionary
   */
  public void getProtPins11179DD(String lRegAuthId, String fname) throws Throwable {

    // parse the PINS file
    protPinsInst = new ProtPins();
    protPinsInst.getProtInst("dd79", "dd79", fname);

    // iterate through the resultant instances and build the Master 11179 DD
    HashMap<String, InstDefn> lInstMap = protPinsInst.instDict;
    Set<String> set1 = lInstMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String lId = iter1.next();
      InstDefn lInst = lInstMap.get(lId);
      // System.out.println("\ndebug getProtPins11179DD1 lInst.identifier:" + lInst.identifier);
      // System.out.println("debug getProtPins11179DD2 lInst.title:" + lInst.title);
      // System.out.println("debug getProtPins11179DD3 lInst.rdfIdentifier:" + lInst.rdfIdentifier);
      // System.out.println("debug getProtPins11179DD4 lInst.className:" + lInst.className);

      // from the instance class, get the instance (a data element with its meta-attributes)
      String lMast11179Id2 = lInst.className + "." + lInst.title;
      InstDefn lInst3 = DOMInfoModel.master11179DataDict.get(lMast11179Id2);
      if (lInst3 == null) {
        DOMInfoModel.master11179DataDict.put(lMast11179Id2, lInst);
        // System.out.println("debug getProtPins11179DataDict ++adding instance++ lMast11179Id2:" +
        // lMast11179Id2 + " lInst.rdfIdentifier:" + lInst.rdfIdentifier);
      }
    }
    return;
  }

  // get a string value from map
  static public String getStringValue(HashMap<String, ArrayList<String>> lMap, String lMetaAttrName,
      String oVal) {
    // System.out.println("debug getStringValue - lMetaAttrName:" + lMetaAttrName);
    ArrayList<String> lValArr = lMap.get(lMetaAttrName);
    if (lValArr != null) {
      int lValArrSize = lValArr.size();
      if (lValArrSize == 1) {
        String lVal = lValArr.get(0);
        if (!(lVal == null || (lVal.compareTo("")) == 0)) {
          return lVal;
        }
      }
    }
    return oVal;
    // returns the original value
  }

  // get a string value from map
  static public String getStringValueUpdate(boolean isEscaped,
      HashMap<String, ArrayList<String>> lMap, String lMetaAttrName, String oVal) {
    // System.out.println("debug getStringValue - lMetaAttrName:" + lMetaAttrName);
    ArrayList<String> lValArr = lMap.get(lMetaAttrName);
    if (lValArr != null) {
      int lValArrSize = lValArr.size();
      if (lValArrSize == 1) {
        String lVal = lValArr.get(0);
        if (!((lVal == null) || ((lVal.compareTo("")) == 0) || ((lVal.indexOf("TBD")) == 0))) {
          if (isEscaped) {
            lVal = DOMInfoModel.unEscapeProtegeString(lVal);
          }
          if (lVal.compareTo(oVal) != 0) {
            return lVal;
          }
        }
      }
    }
    return null;
  }

  // get a string value from map
  static public String getStringValueUpdatePattern(boolean isEscaped,
      HashMap<String, ArrayList<String>> lMap, String lMetaAttrName, String oVal) {
    // System.out.println("debug getStringValue - lMetaAttrName:" + lMetaAttrName);
    ArrayList<String> lValArr = lMap.get(lMetaAttrName);
    if (lValArr != null) {
      int lValArrSize = lValArr.size();
      if (lValArrSize == 1) {
        String lVal = lValArr.get(0);
        if (!((lVal == null) || ((lVal.compareTo("")) == 0) || ((lVal.indexOf("TBD")) == 0))) {
          if (isEscaped) {
            lVal = DOMInfoModel.unEscapeProtegePatterns(lVal);
          }
          if (lVal.compareTo(oVal) != 0) {
            return lVal;
          }
        }
      }
    }
    return null;
  }

  // dump master 11179 Data Dictionary
  public void dumpMaster11179DataDict() {
    // HashMap <String, ArrayList<String>> lMap = new HashMap <String, ArrayList<String>> ();

    System.out.println("\n ======= Dump 11179 Data Dictionary =======");
    Set<String> set2 = DOMInfoModel.master11179DataDict.keySet();
    Iterator<String> iter2 = set2.iterator();
    while (iter2.hasNext()) {
      // get the instance of the meta class - e.g. DataElement -> target_name
      String lMetaClassInstId = iter2.next();
      System.out.println("\ndebug 11179 Dump -   lMetaClassInstId:" + lMetaClassInstId);
      InstDefn lInst = DOMInfoModel.master11179DataDict.get(lMetaClassInstId);
      if (lInst != null) {
        System.out.println("                 -     Instance Name:" + lInst.title);
        // lMap = lInst.genSlotMap;
        HashMap<String, ArrayList<String>> lMap = lInst.genSlotMap;
        Set<String> set3 = lMap.keySet();
        Iterator<String> iter3 = set3.iterator();
        while (iter3.hasNext()) {
          String lMetaAttrName = iter3.next();
          // get meta attributes - e.g. DataElement -> target_name -> deIdentifier
          // System.out.println(" - Name:" + lMetaAttrName);
          ArrayList<String> lValArr = lMap.get(lMetaAttrName); // an ArrayList; could have one value
                                                               // or many.
          if (lValArr != null) {
            int lValArrSize = lValArr.size();
            if (lValArrSize == 1) {
              String lVal = lValArr.get(0);
              if (!(lVal == null || (lVal.compareTo("")) == 0)) {
                System.out
                    .println("                 -     Name:" + lMetaAttrName + "   Value:" + lVal);
              }
            } else if (lValArrSize > 1) {
              System.out
                  .print("                 -     Name:" + lMetaAttrName + "  (Multi-valued) ");
              for (Iterator<String> i = lValArr.iterator(); i.hasNext();) {
                String lVal = i.next();
                if (lVal.compareTo("") != 0) {
                  System.out.print("Value:" + lVal + "; ");
                }
              }
              System.out.println("");
            }
          }
        }
      }
    }
  }
}

