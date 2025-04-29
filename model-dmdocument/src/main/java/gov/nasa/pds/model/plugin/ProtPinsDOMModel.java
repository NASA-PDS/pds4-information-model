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
import java.util.TreeMap;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Creates the in memory protege Upper pins model (e.g. Discipline Facets)
 */
class ProtPinsDOMModel extends Object {
  ProtPins protPinsInst;
  // 444
  TreeMap<String, DOMRule> testRuleDefnMap = new TreeMap<>();
  TreeMap<String, DOMUseCase> testUseCaseDefnMap = new TreeMap<>();
  TreeMap<String, SFGroupFacetDefn> lSFGroupFacetDefnMap;

  public ProtPinsDOMModel() {

    // Parse Protege Instance File - Parse InstDefn stored as follows.
    // lInst.className - e.g. DataElement
    // + lRegAuthId + "." + lInst.className + "." + lInst.title;
    // ++ InstDefn
    DOMInfoModel.sfDisciplineFacetDefnMap = new TreeMap<>();
    lSFGroupFacetDefnMap = new TreeMap<>();
    return;
  }

  /**
   * Parses the protege Upper pins file
   */
  public void getProtPinsModel(String lRegAuthId, String fname) throws Throwable {

    // parse the PINS file
    protPinsInst = new ProtPins();
    protPinsInst.getProtInst("upperPins", "upperPins", fname);

    // iterate through the resultant instances
    ArrayList<InstDefn> lInstDefnArr = new ArrayList<>(protPinsInst.instDict.values());
    for (Iterator<InstDefn> i = lInstDefnArr.iterator(); i.hasNext();) {
      InstDefn lInst = i.next();

      if (!((lInst.className.compareTo("Discipline_Facets") == 0)
          || (lInst.className.compareTo("Group_Facet1") == 0)
          || (lInst.className.compareTo("Group_Facet2") == 0))) {
        continue;
      }

      HashMap<String, ArrayList<String>> lInstSlotMap = lInst.genSlotMap;

      if (lInst.className.compareTo("Discipline_Facets") == 0) {
        SFDisciplineFacetDefn lSFDisciplineFacetDefn =
            new SFDisciplineFacetDefn(lInst.className, lInst.title);
        DOMInfoModel.sfDisciplineFacetDefnMap.put(lSFDisciplineFacetDefn.identifier,
            lSFDisciplineFacetDefn);
        ArrayList<String> lValArr = lInstSlotMap.get("has_Group_Facet1");
        if (lValArr != null) {
          for (Iterator<String> j = lValArr.iterator(); j.hasNext();) {
            String lVal = j.next();
            String lValPlus = lVal + "." + "Group_Facet1";
            lSFDisciplineFacetDefn.groupFacet1IdArr.add(lValPlus);
          }
        }
        lValArr = lInstSlotMap.get("has_Group_Facet2");
        if (lValArr != null) {
          for (Iterator<String> j = lValArr.iterator(); j.hasNext();) {
            String lVal = j.next();
            String lValPlus = lVal + "." + "Group_Facet2";
            lSFDisciplineFacetDefn.groupFacet2IdArr.add(lValPlus);
          }
        }
      } else {
        // create new group facet
        SFGroupFacetDefn lSFGroupFacetDefn = new SFGroupFacetDefn(lInst.className, lInst.title);
        lSFGroupFacetDefnMap.put(lSFGroupFacetDefn.identifier, lSFGroupFacetDefn);

        // get subfacet1
        ArrayList<String> lValArr = lInstSlotMap.get("subfacet1");
        if (lValArr != null) {
          for (Iterator<String> j = lValArr.iterator(); j.hasNext();) {
            String lVal = j.next();
            lSFGroupFacetDefn.subfacetArr.add(lVal);
          }
        }

        // get subfacet2
        lValArr = lInstSlotMap.get("subfacet2");
        if (lValArr != null) {
          for (Iterator<String> j = lValArr.iterator(); j.hasNext();) {
            String lVal = j.next();
            lSFGroupFacetDefn.subfacetArr.add(lVal);
          }
        }

      }
    }

    // iterate through the discipline facets and resolve the group facets
    ArrayList<SFDisciplineFacetDefn> lSFDisciplineFacetDefnArr =
        new ArrayList<>(DOMInfoModel.sfDisciplineFacetDefnMap.values());
    for (Iterator<SFDisciplineFacetDefn> i = lSFDisciplineFacetDefnArr.iterator(); i.hasNext();) {
      SFDisciplineFacetDefn lSFDisciplineFacetDefn = i.next();
      for (Iterator<String> j = lSFDisciplineFacetDefn.groupFacet1IdArr.iterator(); j.hasNext();) {
        String lSFGroupFacetId = j.next();
        SFGroupFacetDefn lSFGroupFacetDefn = lSFGroupFacetDefnMap.get(lSFGroupFacetId);
        if (lSFGroupFacetDefn == null) {
          continue;
        }
        lSFDisciplineFacetDefn.groupFacet1Arr.add(lSFGroupFacetDefn);
      }
      for (Iterator<String> j = lSFDisciplineFacetDefn.groupFacet2IdArr.iterator(); j.hasNext();) {
        String lSFGroupFacetId = j.next();
        SFGroupFacetDefn lSFGroupFacetDefn = lSFGroupFacetDefnMap.get(lSFGroupFacetId);
        if (lSFGroupFacetDefn == null) {
          continue;
        }
        lSFDisciplineFacetDefn.groupFacet2Arr.add(lSFGroupFacetDefn);
      }
    }

    // iterate through the discipline facets and update the permissible values for the associated
    // attributes
    String lAttrId = DOMInfoModel.getAttrIdentifier(DMDocument.masterNameSpaceIdNCLC,
        "Discipline_Facets", DMDocument.masterNameSpaceIdNCLC, "discipline_name");
    DOMAttr lDOMAttrDiscipline = DOMInfoModel.masterDOMAttrIdMap.get(lAttrId);
    if (lDOMAttrDiscipline == null) {
      Utility.registerMessage("1>error " + "Missing discipline_name - lAttrId:" + lAttrId);
      return;
    }

    // update discpline_name values
    lDOMAttrDiscipline.isEnumerated = true;
    for (Iterator<SFDisciplineFacetDefn> i = lSFDisciplineFacetDefnArr.iterator(); i.hasNext();) {
      SFDisciplineFacetDefn lSFDisciplineFacetDefn = i.next();
      lDOMAttrDiscipline.valArr.add(lSFDisciplineFacetDefn.disciplineName);
      DOMPermValDefn lDOMPermVal = new DOMPermValDefn();
      lDOMPermVal.value = lSFDisciplineFacetDefn.disciplineName;
      DOMProp lDOMProp = new DOMProp();
      lDOMProp.initDOMPermValProp(lDOMPermVal);
      lDOMAttrDiscipline.domPermValueArr.add(lDOMProp);
      lDOMProp.domObject = lDOMPermVal;

      // update facet1 values
      for (Iterator<SFGroupFacetDefn> j = lSFDisciplineFacetDefn.groupFacet1Arr.iterator(); j
          .hasNext();) {
        SFGroupFacetDefn lSFGroupFacet = j.next();
        lAttrId = DOMInfoModel.getAttrIdentifier(DMDocument.masterNameSpaceIdNCLC, "Group_Facet1",
            DMDocument.masterNameSpaceIdNCLC, "facet1");
        DOMAttr lDOMAttrFacet1 = DOMInfoModel.masterDOMAttrIdMap.get(lAttrId);
        if (lDOMAttrFacet1 == null) {
          Utility.registerMessage("1>error " + "Missing facet2 - lAttrId:" + lAttrId);
          continue;
        }
        lDOMAttrFacet1.isEnumerated = true;
        lDOMAttrFacet1.valArr.add(lSFGroupFacet.facet);
        lDOMAttrFacet1.valueDependencyMap.put(lSFGroupFacet.facet,
            lSFDisciplineFacetDefn.disciplineName);
        DOMPermValDefn lDOMPermValFacet1 = new DOMPermValDefn();
        lDOMPermValFacet1.value = lSFGroupFacet.facet;
        DOMProp lDOMPropFacet1 = new DOMProp();
        lDOMPropFacet1.initDOMPermValProp(lDOMPermValFacet1);
        lDOMAttrFacet1.domPermValueArr.add(lDOMPropFacet1);
        lDOMPropFacet1.domObject = lDOMPermValFacet1;
      }
      // update facet2 values
      for (Iterator<SFGroupFacetDefn> j = lSFDisciplineFacetDefn.groupFacet2Arr.iterator(); j
          .hasNext();) {
        SFGroupFacetDefn lSFGroupFacet = j.next();
        lAttrId = DOMInfoModel.getAttrIdentifier(DMDocument.masterNameSpaceIdNCLC, "Group_Facet2",
            DMDocument.masterNameSpaceIdNCLC, "facet2");
        DOMAttr lDOMAttrFacet2 = DOMInfoModel.masterDOMAttrIdMap.get(lAttrId);
        if (lDOMAttrFacet2 == null) {
          Utility.registerMessage("1>error " + "Missing facet2 - lAttrId:" + lAttrId);
          continue;
        }
        lDOMAttrFacet2.isEnumerated = true;
        lDOMAttrFacet2.valArr.add(lSFGroupFacet.facet);
        lDOMAttrFacet2.valueDependencyMap.put(lSFGroupFacet.facet,
            lSFDisciplineFacetDefn.disciplineName);
        DOMPermValDefn lDOMPermValFacet2 = new DOMPermValDefn();
        lDOMPermValFacet2.value = lSFGroupFacet.facet;
        DOMProp lDOMPropFacet2 = new DOMProp();
        lDOMPropFacet2.initDOMPermValProp(lDOMPermValFacet2);
        lDOMAttrFacet2.domPermValueArr.add(lDOMPropFacet2);
        lDOMPropFacet2.domObject = lDOMPermValFacet2;
      }
    }
    return;
  }

  public void getRulesPins() {
    // 444 TreeMap <String, RuleDefn> testRuleDefnMap = new TreeMap <String, RuleDefn> ();

    // extract all the rule instances from the instance dictionary
    TreeMap<String, InstDefn> ruleInstDefnMap = new TreeMap<>();
    ArrayList<InstDefn> lInstDefnArr = new ArrayList<>(protPinsInst.instDict.values());
    for (Iterator<InstDefn> i = lInstDefnArr.iterator(); i.hasNext();) {
      InstDefn lInst = i.next();
      if (!((lInst.className.compareTo("Schematron_Rule") == 0)
          || (lInst.className.compareTo("Schematron_Assert")) == 0)) {
        continue;
      }
      ruleInstDefnMap.put(lInst.title, lInst);
    }

    // process the rule instances
    ArrayList<InstDefn> ruleInstDefnArr = new ArrayList<>(ruleInstDefnMap.values());
    for (Iterator<InstDefn> i = ruleInstDefnArr.iterator(); i.hasNext();) {
      InstDefn lRuleInst = i.next();

      HashMap<String, ArrayList<String>> lInstSlotMap = lRuleInst.genSlotMap;
      if (lRuleInst.className.compareTo("Schematron_Rule") == 0) {
        // InfoModel.schematronRuleNewArr.add(lRule);

        // first singleton attributes of rule
        String lIdentifier = getValueSingleton("identifier", lInstSlotMap);
        DOMRule lRule = new DOMRule(lIdentifier);
        lRule.setRDFIdentifier();
        testRuleDefnMap.put(lRule.rdfIdentifier, lRule);
        lRule.type = getValueSingleton("type", lInstSlotMap);
        lRule.roleId = getValueSingleton("roleId", lInstSlotMap);
        lRule.xpath = getValueSingleton("xpath", lInstSlotMap);
        lRule.attrTitle = getValueSingleton("attrTitle", lInstSlotMap);
        lRule.attrNameSpaceNC = getValueSingleton("attrNameSpaceNC", lInstSlotMap);
        lRule.classTitle = getValueSingleton("classTitle", lInstSlotMap);
        lRule.classNameSpaceNC = getValueSingleton("classNameSpaceNC", lInstSlotMap);
        lRule.classSteward = getValueSingleton("classSteward", lInstSlotMap);
        String lAlwaysInclude = getValueSingleton("alwaysInclude", lInstSlotMap);
        lRule.alwaysInclude = true;
        if (lAlwaysInclude.compareTo("false") == 0) {
          lRule.alwaysInclude = false;
        }
        String lIsMissionOnly = getValueSingleton("isMissionOnly", lInstSlotMap);
        lRule.isMissionOnly = true;
        if (lIsMissionOnly.compareTo("false") == 0) {
          lRule.isMissionOnly = false;
        }
        // 444
        // lRule.letAssignArr = getValueArray ("schematronAssign", lInstSlotMap);
        ArrayList<String> lAssignArr = getValueArray("schematronAssign", lInstSlotMap);
        if (lAssignArr != null) {
          lRule.letAssignArr = lAssignArr;
        }
        ArrayList<String> lAssignPatternArr =
            getValueArray("schematronAssignPattern", lInstSlotMap);
        if (lAssignPatternArr != null) {
          lRule.letAssignPatternArr = lAssignPatternArr;
        }

        // get the Assert Statements
        ArrayList<String> lAssertIdArr = getValueArray("has_Schematron_Assert", lInstSlotMap);
        for (Iterator<String> j = lAssertIdArr.iterator(); j.hasNext();) {
          String lAssertId = j.next();
          InstDefn lAssertInst = ruleInstDefnMap.get(lAssertId);
          if (lAssertInst != null) {
            DOMAssert lAssertDefn = new DOMAssert(lAssertInst.rdfIdentifier);
            lRule.assertArr.add(lAssertDefn);
            lAssertDefn.identifier = getValueSingleton("identifier", lAssertInst.genSlotMap);
            lAssertDefn.attrTitle = getValueSingleton("attrTitle", lAssertInst.genSlotMap);
            lAssertDefn.assertType = getValueSingleton("assertType", lAssertInst.genSlotMap);
            lAssertDefn.assertMsg = getValueSingleton("assertMsg", lAssertInst.genSlotMap);
            lAssertDefn.assertStmt = getValueSingleton("assertStmt", lAssertInst.genSlotMap);
            lAssertDefn.specMesg = getValueSingleton("specMesg", lAssertInst.genSlotMap);
            // 444
            // lAssertDefn.testValArr = getValueArray ("testValArr", lAssertInst.genSlotMap);
            ArrayList<String> lValArr = getValueArray("testValArr", lAssertInst.genSlotMap);
            if (lValArr != null) {
              lAssertDefn.testValArr = lValArr;
            }
          } else {
            Utility.registerMessage(
                "1>error " + "getRulesPins - Assert Statement Not Found - lAssertId:" + lAssertId);
          }
        }
      }
    }
  }

  /*
   * public void getUseCasesPins () { // extract all the use instances from the instance dictionary
   * TreeMap <String, InstDefn> UseCaseInstDefnMap = new TreeMap <String, InstDefn> (); ArrayList
   * <InstDefn> lInstDefnArr = new ArrayList <InstDefn> (protPinsInst.instDict.values()); for
   * (Iterator <InstDefn> i = lInstDefnArr.iterator(); i.hasNext();) { InstDefn lInst = (InstDefn)
   * i.next(); if ( ! ((lInst.className.compareTo("UseCase") == 0))) continue;
   * UseCaseInstDefnMap.put(lInst.title, lInst); }
   * 
   * // process the rule instances ArrayList <InstDefn> UseCaseInstDefnArr = new ArrayList
   * <InstDefn> (UseCaseInstDefnMap.values()); for (Iterator <InstDefn> i =
   * UseCaseInstDefnArr.iterator(); i.hasNext();) { InstDefn lUseCaseInst = (InstDefn) i.next();
   * 
   * HashMap <String, ArrayList<String>> lInstSlotMap = lUseCaseInst.genSlotMap; if
   * (lUseCaseInst.className.compareTo("UseCase") == 0) {
   * 
   * // first singleton attributes of rule String lIdentifier = getValueSingleton ("identifier",
   * lInstSlotMap); DOMUseCase lUseCase = new DOMUseCase (lIdentifier); lUseCase.setRDFIdentifier
   * (); testUseCaseDefnMap.put(lUseCase.rdfIdentifier, lUseCase); lUseCase.overview =
   * getValueSingleton ("overview", lInstSlotMap); lUseCase.type = getValueSingleton ("type",
   * lInstSlotMap); lUseCase.actors = getValueSingleton ("actors", lInstSlotMap);
   * lUseCase.preconditions = getValueSingleton ("preconditions", lInstSlotMap);
   * lUseCase.successfulOutcome = getValueSingleton ("successfulOutcome", lInstSlotMap);
   * lUseCase.exceptions = getValueSingleton ("exceptions", lInstSlotMap); lUseCase.context =
   * getValueSingleton ("context", lInstSlotMap); lUseCase.steps = getValueSingleton ("steps",
   * lInstSlotMap); lUseCase.trigger = getValueSingleton ("trigger", lInstSlotMap); } } }
   */

  private String getValueSingleton(String lKey, HashMap<String, ArrayList<String>> lInstSlotMap) {
    ArrayList<String> lValArr = lInstSlotMap.get(lKey);
    if (lValArr == null || lValArr.isEmpty()) {
      return "TBD_" + lKey;
    }
    if (lValArr.size() > 1) {
      Utility.registerMessage("1>error " + "getSingletonValue - ProtPinsModel - lKey:" + lKey);
    }
    return lValArr.get(0);
  }

  private ArrayList<String> getValueArray(String lKey,
      HashMap<String, ArrayList<String>> lInstSlotMap) {
    ArrayList<String> lValArr = lInstSlotMap.get(lKey);
    if (lValArr == null || lValArr.isEmpty()) {
      return null;
    }
    return lValArr;
  }

  // get a string value from map
  static public String getStringValue(HashMap<String, ArrayList<String>> lMap, String lMetaAttrName,
      String oVal) {
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
}

