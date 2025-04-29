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
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import gov.nasa.pds.model.plugin.util.Utility;

class GenDOMRules extends Object {
  TreeMap <String, DeprecatedDefn> lUnitIdDeprecatedMap = new TreeMap <String, DeprecatedDefn> ();
  TreeMap<String, ParentChildrenRelation> parentChildrenRelationMap = new TreeMap<>();

  public GenDOMRules() {

    findChildrenOfAssociatedExternalClass();

    return;
  }

  // generate schematron rules
  public void genSchematronRules() throws java.io.IOException {
    // generate the schematron rules for both the common and the master LDD
    genSchematronRule(DMDocument.masterPDSSchemaFileDefn, DOMInfoModel.masterDOMClassMap);
    if (DMDocument.LDDToolFlag) {
      genSchematronRule(DMDocument.masterLDDSchemaFileDefn, DOMInfoModel.masterDOMClassMap);
    }
    return;
  }

  // write the schematron rules
  public void genSchematronRule(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap) {

    // add the enumerated value schematron rules
    addSchematronRuleEnumerated(lSchemaFileDefn, lMasterDOMClassMap);

    // add the enumerated value schematron rules for child classes of DD Associate External Class
    addSchematronRuleEnumeratedChildOfAssocExtern(lSchemaFileDefn);

    // add the boolean (true, false) schematron rules
    addSchematronRuleBoolean(lSchemaFileDefn, lMasterDOMClassMap);

    // add the deprecated item schematron rules (Insert)
    if (!DMDocument.deprecatedAddedDOM) {
      DMDocument.deprecatedAddedDOM = true;
      addSchematronRuleDeprecated(lSchemaFileDefn);
      addSchematronRuleDeprecatedUnitId(lSchemaFileDefn);
    }

    // add the Science Facet schematron rules
    addSchematronRuleDisciplineFacets(lSchemaFileDefn, DOMInfoModel.sfDisciplineFacetDefnMap);

    // add the unit values schematron rules
    addSchematronRuleUnits(lSchemaFileDefn, lMasterDOMClassMap);

    // If this is an LDD run set lRule.alwaysInclude to false for all customized rules
    if (lSchemaFileDefn.isLDD) {
      for (Iterator<DOMRule> i = DOMInfoModel.masterDOMRuleArr.iterator(); i.hasNext();) {
        DOMRule lRule = i.next();
        lRule.alwaysInclude = false;
      }
    }
  }

  // add the enumerated value schematron rules
  public void addSchematronRuleEnumerated(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap) {
    // add class attributes
    ArrayList<DOMClass> lSortClassArr = new ArrayList<>(lMasterDOMClassMap.values());
    for (DOMClass lClass : lSortClassArr) {
      if ((lClass == null)
          || (!((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0)
              && lSchemaFileDefn.stewardArr.contains(lClass.steward)))) {
        continue;
      }
      if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) {
        continue;
      }
      if (lClass.isInactive || lClass.allEnumAttrArr == null || lClass.allEnumAttrArr.isEmpty()) {
        continue;
      }

      // discipline facet and facet group schema rules are hard coded elsewhere.
      if (lClass.title.compareTo("Discipline_Facets") == 0
          || lClass.title.compareTo("Group_Facet1") == 0
          || lClass.title.compareTo("Group_Facet2") == 0) {
        continue;
      }

      // skip abstract classes defined using dd_associate_external_class
      if (lClass.isAbstract && lClass.isAssociatedExternalClass) {
        continue;
      }

      // add the rules
      addClassSchematronRuleEnumerated(lClass.nameSpaceIdNC, lClass.title, lClass.steward,
          lClass.allEnumAttrArr);
    }

    // add no-class attributes - e.g., created from DD_Associate_External_Class
    ArrayList<DOMAttr> lEnumAttrArr = new ArrayList<>();
    ArrayList<DOMAttr> lAttrArr =
        new ArrayList<>(DOMInfoModel.userSingletonDOMClassAttrIdMap.values());
    for (DOMAttr lAttr : lAttrArr) {
      if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.nameSpaceIdNC) != 0)) {
        continue;
      }
      lEnumAttrArr.add(lAttr);
    }
    if (!lEnumAttrArr.isEmpty()) {
      addClassSchematronRuleEnumerated(DMDocument.masterNameSpaceIdNCLC,
          DMDocument.LDDToolSingletonClassTitle, lSchemaFileDefn.stewardArr.get(0), lEnumAttrArr);
    }
  }

  public void addClassSchematronRuleEnumerated(String lClassNameSpaceIdNC, String lClassTitle,
      String lClassSteward, ArrayList<DOMAttr> lAttrArr) {
    for (DOMAttr lAttr : lAttrArr) {
      if ((lAttr.isAbstract && lAttr.isAssociatedExternalAttr)
          || (lAttr.valueType.compareTo("ASCII_Boolean") == 0)) {
        continue;
      }
      String lXPath;
      if (!lAttr.isAssociatedExternalAttr) {
        lXPath =
            lClassNameSpaceIdNC + ":" + lClassTitle + "/" + lAttr.nameSpaceIdNC + ":" + lAttr.title;
      } else {
        lXPath = lAttr.xPath;
      }
      DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXPath);
      if (lRule == null) {
        lRule = new DOMRule(lXPath);
        DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
        DOMInfoModel.masterDOMRuleArr.add(lRule);
        lRule.setRDFIdentifier();
        DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
        lRule.xpath = lXPath;
        lRule.attrTitle = "TBD_AttrTitle";
        lRule.attrNameSpaceNC = "TBD_attrNameSpaceNC";
        lRule.classTitle = lClassTitle;
        lRule.classNameSpaceNC = lClassNameSpaceIdNC;
        lRule.classSteward = lClassSteward;
      }
      if (lAttr.valArr == null || lAttr.valArr.isEmpty()) {
        continue;
      }
      lRule.attrTitle = lAttr.title;
      lRule.attrNameSpaceNC = lAttr.nameSpaceIdNC;
      String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
      if (foundAssertStmt(lAttrId, lRule.assertArr)) {
        continue;
      }
      DOMAssert lAssert = new DOMAssert(lAttrId);
      lRule.assertArr.add(lAssert);

      String lDel = "";
      String lDelimitedValueArr = "";

      // test for special attributes
      if (!(lAttr.title.compareTo("information_model_version") == 0)) {
        for (String lValue : lAttr.valArr) {
          String lDelimitedValue = lDel + "'" + lValue + "'";
          lDel = ", ";
          lDelimitedValueArr += lDelimitedValue;
        }

        if (lAttr.valArr.size() > 1) {
          if (!lAttr.isNilable) {
            lAssert.assertStmt = ". = (" + lDelimitedValueArr + ")";
            lAssert.assertMsg = "The attribute " + lXPath
                + " must be equal to one of the following values " + lDelimitedValueArr + ".";
          } else {
            lAssert.assertStmt = "if (not(@xsi:nil eq 'true') and (not(. = (" + lDelimitedValueArr
                + ")))) then false() else true()";
            lAssert.assertMsg = "The attribute " + lXPath
                + " must be nulled or equal to one of the following values " + lDelimitedValueArr
                + ".";
          }
        } else if (!lAttr.isNilable) {
          lAssert.assertStmt = ". = (" + lDelimitedValueArr + ")";
          lAssert.assertMsg =
              "The attribute " + lXPath + " must be equal to the value " + lDelimitedValueArr + ".";
        } else {
          lAssert.assertStmt = "if (not(@xsi:nil eq 'true') and (not(. = (" + lDelimitedValueArr
              + ")))) then false() else true()";
          lAssert.assertMsg = "The attribute " + lXPath + " must be nulled or equal to the value "
              + lDelimitedValueArr + ".";
        }

        // if attribute is information_model_version, only the current version is allowed
      } else {
        String lDelimitedValue = "'" + DMDocument.masterPDSSchemaFileDefn.versionId + "'";
        lAssert.assertStmt = ". = (" + lDelimitedValue + ")";
        if (!lAttr.isNilable) {
          lAssert.assertMsg =
              "The attribute " + lXPath + " must be equal to the value " + lDelimitedValue + ".";
        } else {
          lAssert.assertMsg = "The attribute " + lXPath + " must be nulled or equal to the value "
              + lDelimitedValue + ".";
        }
      }
    }
  }

  // add rules for child classes of a parent Associated External Class
  public void addSchematronRuleEnumeratedChildOfAssocExtern(SchemaFileDefn lSchemaFileDefn) {
    // reference the parent Associated External Class and children of the parent class
    ArrayList<ParentChildrenRelation> lParentChildrenRelationArr =
        new ArrayList<>(parentChildrenRelationMap.values());
    for (Iterator<ParentChildrenRelation> i = lParentChildrenRelationArr.iterator(); i.hasNext();) {
      ParentChildrenRelation lParentChildrenRelation = i.next();

      // get the parent class defined using Associated External Class - e.g.,
      // Body_Identification_Base
      DOMClass lParentDOMClass = lParentChildrenRelation.parentClass;

      // iterate through the DOMProps - e.g., pds:Internal_Reference
      for (Iterator<DOMProp> j = lParentDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();

        // iterate thorough the attribute identifiers - DOMAttr.identifier e.g.,
        // Body_Identification_Base.pds.Internal_Identifier.pds.reference_type
        for (Iterator<String> k = lDOMProp.valArr.iterator(); k.hasNext();) {
          String lAttrId = k.next();

          // get the attribute DOMAttr.identifier - e.g.,
          // Body_Identification_Base.pds.Internal_Identifier.pds.reference_type
          DOMAttr lInheritedDOMAttr = DOMInfoModel.userSingletonDOMClassAttrIdMap.get(lAttrId);
          if (lInheritedDOMAttr != null) {

            // get the child classes of the parent Associated External Class - e.g.,
            // geom:Central_Body_Identification
            for (Iterator<DOMClass> l = lParentChildrenRelation.childClassArr.iterator(); l
                .hasNext();) {
              DOMClass lChildDOMClass = l.next();
              addClassSchematronRuleEnumeratedChildOfAssocExtrnClass(lInheritedDOMAttr,
                  lParentDOMClass, lChildDOMClass);
            }
          }
        }
      }
    }
  }

  public void addClassSchematronRuleEnumeratedChildOfAssocExtrnClass(DOMAttr lInheritedDOMAttr,
      DOMClass lParentDOMClass, DOMClass lChildDOMClass) {
    // for each attribute, write out the rule
    String lXpath = lChildDOMClass.nameSpaceIdNC + ":" + lChildDOMClass.title + "/"
        + lInheritedDOMAttr.lddUserAttribute.classNameSpaceIdNC + ":"
        + lInheritedDOMAttr.lddUserAttribute.attrParentClass.title + "/"
        + lInheritedDOMAttr.lddUserAttribute.nameSpaceIdNC + ":"
        + lInheritedDOMAttr.lddUserAttribute.title + "";
    DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
    if (lRule == null) {
      lRule = new DOMRule(lXpath);
      DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
      DOMInfoModel.masterDOMRuleArr.add(lRule);
      lRule.setRDFIdentifier();
      DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
      lRule.xpath = lXpath;
      lRule.attrTitle = "TBD_AttrTitle";
      lRule.attrNameSpaceNC = "TBD_attrNameSpaceNC";
      lRule.classTitle = lChildDOMClass.title;
      lRule.classNameSpaceNC = lChildDOMClass.nameSpaceIdNC;
      lRule.classSteward = lChildDOMClass.steward;
      lRule.isAssociatedExternalClass = lParentDOMClass.isAssociatedExternalClass;
    }
    if (lInheritedDOMAttr.valArr == null || lInheritedDOMAttr.valArr.isEmpty()) {
      return;
    }
    lRule.attrTitle = lInheritedDOMAttr.title;
    lRule.attrNameSpaceNC = lInheritedDOMAttr.nameSpaceIdNC;
    String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
    if (foundAssertStmt(lAttrId, lRule.assertArr)) {
      return;
    }
    DOMAssert lAssert = new DOMAssert(lAttrId);
    lRule.assertArr.add(lAssert);

    String lDel = "";
    String lDelimitedValueArr = "";

    for (Iterator<String> k = lInheritedDOMAttr.valArr.iterator(); k.hasNext();) {
      String lValue = k.next();
      String lDelimitedValue = lDel + "'" + lValue + "'";
      lDel = ", ";
      lDelimitedValueArr += lDelimitedValue;
    }

    if (lInheritedDOMAttr.valArr.size() > 1) {
      if (!lInheritedDOMAttr.isNilable) {
        lAssert.assertStmt = ". = (" + lDelimitedValueArr + ")";
        lAssert.assertMsg = "The attribute " + getAssertMsg(lRule)
            + " must be equal to one of the following values " + lDelimitedValueArr + ".";
      } else {
        lAssert.assertStmt = "if (not(@xsi:nil eq 'true') and (not(. = (" + lDelimitedValueArr
            + ")))) then false() else true()";
        lAssert.assertMsg = "The attribute " + getAssertMsg(lRule)
            + " must be nulled or equal to one of the following values " + lDelimitedValueArr + ".";
      }
    } else if (!lInheritedDOMAttr.isNilable) {
      lAssert.assertStmt = ". = (" + lDelimitedValueArr + ")";
      lAssert.assertMsg = "The attribute " + getAssertMsg(lRule) + " must be equal to the value "
          + lDelimitedValueArr + ".";
    } else {
      lAssert.assertStmt = "if (not(@xsi:nil eq 'true') and (not(. = (" + lDelimitedValueArr
          + ")))) then false() else true()";
      lAssert.assertMsg = "The attribute " + getAssertMsg(lRule)
          + " must be nulled or equal to the value " + lDelimitedValueArr + ".";
    }
  }

  // get the Assert Message
  public String getAssertMsg(DOMRule lDOMRule) {
    String lAssertMsg = lDOMRule.attrNameSpaceNC + ":" + lDOMRule.attrTitle;
    if (lDOMRule.isAssociatedExternalClass) {
      lAssertMsg = lDOMRule.xpath;
    }
    return lAssertMsg;
  }

  // add the boolean schematron rules
  public void addSchematronRuleBoolean(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap) {
    // get the set of reasonable classes
    ArrayList<DOMClass> lSelectClassArr = new ArrayList<>();
    ArrayList<DOMClass> lClassArr = new ArrayList<>(lMasterDOMClassMap.values());
    for (DOMClass lClass : lClassArr) {
      if ((lClass == null)
          || (!((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0)
              && lSchemaFileDefn.stewardArr.contains(lClass.steward)))) {
        continue;
      }
      if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) {
        continue;
      }
      if (lClass.allEnumAttrArr == null || lClass.allEnumAttrArr.isEmpty()) {
        continue;
      }
      lSelectClassArr.add(lClass);
    }

    // get the set of classes with Boolean data types
    ArrayList<DOMClass> lBooleanClassArr = new ArrayList<>();
    for (DOMClass lClass : lSelectClassArr) {
      for (DOMProp lDOMProp : lClass.allAttrAssocArr) {
        if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
          if (lDOMAttr.valueType.compareTo("ASCII_Boolean") == 0) {
            lBooleanClassArr.add(lClass);
            continue;
          }
        }
      }
    }

    // create rules for the boolean attributes
    for (DOMClass lClass : lBooleanClassArr) {
      String lXpath = lClass.nameSpaceId + lClass.title;

      DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
      if (lRule == null) {
        lRule = new DOMRule(lXpath);
        DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
        DOMInfoModel.masterDOMRuleArr.add(lRule);
        lRule.setRDFIdentifier();
        DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
        lRule.xpath = lXpath;
        lRule.attrTitle = "TBD_AttrTitle";
        lRule.attrNameSpaceNC = "TBD_attrNameSpaceNC";
        lRule.classTitle = lClass.title;
        lRule.classNameSpaceNC = lClass.nameSpaceIdNC;
        lRule.classSteward = lClass.steward;
        for (DOMProp lDOMProp : lClass.allAttrAssocArr) {
          if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
            DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
            if (lDOMAttr.valueType.compareTo("ASCII_Boolean") != 0) {
              continue;
            }
            String assertMsgPre = " must be equal to one of the following values ";
            lRule.attrTitle = lDOMAttr.title;
            lRule.attrNameSpaceNC = lDOMAttr.nameSpaceIdNC;
            String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
            if (foundAssertStmt(lAttrId, lRule.assertArr)) {
              continue;
            }
            DOMAssert lAssert = new DOMAssert(lAttrId);
            lAssert.assertStmt = "if (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + ") then "
                + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + " = (";
            lAssert.assertMsg = "The attribute " + lAttrId + assertMsgPre;
            lRule.assertArr.add(lAssert);
            lAssert.assertStmt += "'true', 'false'";
            lAssert.assertMsg += "'true', 'false'";
            lAssert.assertStmt += ") else true()";
            lAssert.assertMsg += ".";
          }
        }
      }
    }
  }

  // add the schematron rules for deprecation
  public void addSchematronRuleDeprecated(SchemaFileDefn lSchemaFileDefn) {
    String roleWarning = " role=\"warning\"";

    // iterate for each deprecation
    for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
      DeprecatedDefn lObject = i.next();
      if (lObject.isUnitId && lObject.identifier.compareTo("0001_NASA_PDS_1.pds.Units_of_Map_Scale") != 0) {
        lUnitIdDeprecatedMap.put(lObject.identifier, lObject);
        continue;
      }
      String lRuleId = lObject.context + roleWarning;
      DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lRuleId);
      if (lRule == null) {
        lRule = new DOMRule(lRuleId);
        DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
        DOMInfoModel.masterDOMRuleArr.add(lRule);
        lRule.setRDFIdentifier();
        DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
        lRule.xpath = lObject.context;
        lRule.roleId = roleWarning;
        lRule.attrTitle = lObject.title;
        lRule.attrNameSpaceNC = lObject.classNameSpaceIdNC;
        lRule.classTitle = lObject.className;
        lRule.classNameSpaceNC = lObject.classNameSpaceIdNC;
        lRule.classSteward = DMDocument.masterNameSpaceIdNCLC;

      }
      String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
      DOMAssert lAssert = new DOMAssert(lAttrId);
      lRule.assertArr.add(lAssert);
      if (!lObject.isValue) {
        lAssert.assertStmt = "false()";
        lAssert.assertMsg = lObject.context + " is deprecated and should not be used.";
      } else {
        lAssert.assertStmt =
            lObject.classNameSpaceIdNC + ":" + lObject.attrName + " != '" + lObject.value + "'";
        lAssert.assertMsg = "The value " + lObject.value + " for attribute " + lObject.className
            + "." + lObject.attrName + " is deprecated and should not be used.";
      }
    }
  }
  
  // add the schematron rules for UnitId deprecation
  public void addSchematronRuleDeprecatedUnitId(SchemaFileDefn lSchemaFileDefn) {
    String roleWarning = " role=\"warning\"";

    ArrayList<DeprecatedDefn> lUnitIdDeprecatedArr2 = new ArrayList<>(lUnitIdDeprecatedMap.values());

    // special cases- these unit_ids for deprecated Units_of_Measure were simply dropped since no attributes used them (remove clutter)
    ArrayList <String> ignoreUnitsList = new ArrayList<>( Arrays.asList(
    		"0001_NASA_PDS_1.pds.Units_of_Radiance.pds.unit_id", 
    		"0001_NASA_PDS_1.pds.Units_of_Spectral_Irradiance.pds.unit_id", 
    		"0001_NASA_PDS_1.pds.Units_of_Spectral_Radiance.pds.unit_id",
    		"0001_NASA_PDS_1.pds.Units_of_Wavenumber.pds.unit_id",
    		"0001_NASA_PDS_1.pds.Units_of_Map_Scale.pds.unit_id"));
    
    // write a rule for each deprecated unit value
    for (DeprecatedDefn lObject : lUnitIdDeprecatedArr2) {
    	String lClassNameSpaceIdNC = lObject.classNameSpaceIdNC;
    	String lParentClassTitle = lObject.className;
    	String lAttrNameSpaceIdNC = lObject.attrNameSpaceIdNC;
    	String lAttrTitle = lObject.attrName;

    	if (ignoreUnitsList.contains(lObject.identifier)) continue;

    	String lContext = lClassNameSpaceIdNC + ":" + lParentClassTitle + "/"
    			+ lAttrNameSpaceIdNC + ":" + lAttrTitle;
    	String lRuleId = lContext + roleWarning;
    	DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lRuleId);
    	if (lRule == null) {
    		lRule = new DOMRule(lRuleId);
    		DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
    		DOMInfoModel.masterDOMRuleArr.add(lRule);
    		lRule.setRDFIdentifier();
    		DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);
    		lRule.xpath = lContext;
    		lRule.roleId = roleWarning;
    		lRule.attrTitle = lAttrTitle;
    		lRule.attrNameSpaceNC = lAttrNameSpaceIdNC;
    		lRule.classTitle = lParentClassTitle;
    		lRule.classNameSpaceNC = lClassNameSpaceIdNC;
    		lRule.classSteward = DMDocument.masterNameSpaceIdNCLC;
    	}
    	String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
    	DOMAssert lAssert = new DOMAssert(lAttrId);
    	lRule.assertArr.add(lAssert);
    	lAssert.assertStmt = "@unit != '" + lObject.value + "'";
    	lAssert.assertMsg =
    			"The unit value " + lObject.value + " is deprecated and should not be used.";       
    }
  }

  // add the Discipline schematron rules
  public void addSchematronRuleDisciplineFacets(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, SFDisciplineFacetDefn> lDisciplineFacetMap) {
    // add attribute enumerated values assertions
    ArrayList<SFDisciplineFacetDefn> lDisciplineFacetArr =
        new ArrayList<>(lDisciplineFacetMap.values());
    ArrayList<String> lDiscNameFacet1Arr = new ArrayList<>();
    ArrayList<String> lDiscNameFacet2Arr = new ArrayList<>();

    // set up the Primary Result Summary rule
    String lXpath = "pds:Primary_Result_Summary/pds:Science_Facets";
    DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
    lRule = new DOMRule(lXpath);
    DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
    DOMInfoModel.masterDOMRuleArr.add(lRule);
    lRule.setRDFIdentifier();
    DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);

    lRule.xpath = lXpath;
    lRule.attrTitle = "discipline_name";
    lRule.attrNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classTitle = "Science_Facets";
    lRule.classNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classSteward = DMDocument.masterNameSpaceIdNCLC;
    String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;

    // create the discipline name rule
    if (lDisciplineFacetArr.size() > 0) {
      DOMAssert lAssert = new DOMAssert(lAttrId);
      lRule.assertArr.add(lAssert);
      lAssert.assertStmt = "if (pds:discipline_name) then pds:discipline_name" + " = (";
      lAssert.assertMsg =
          "The attribute pds:discipline_name must be equal to one of the following values ";
      String lValueList = "";
      String lDelimiter = "";
      for (Iterator<SFDisciplineFacetDefn> i = lDisciplineFacetArr.iterator(); i.hasNext();) {
        SFDisciplineFacetDefn lDiscFacet = i.next();
        lValueList += lDelimiter + "'" + lDiscFacet.disciplineName + "'";
        lDelimiter = ", ";
      }
      lAssert.assertStmt += lValueList;
      lAssert.assertMsg += lValueList;
      lAssert.assertStmt += ") else true()";
      lAssert.assertMsg += ".";
    }

    // create assert statement for each discipline name
    for (Iterator<SFDisciplineFacetDefn> i = lDisciplineFacetArr.iterator(); i.hasNext();) {
      SFDisciplineFacetDefn lDiscFacet = i.next();
      lRule.attrTitle = lDiscFacet.disciplineName;
      lRule.attrNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;

      // get all the facet1 assert statements
      if (lDiscFacet.groupFacet1Arr.size() > 0) {
        lDiscNameFacet1Arr.add(lDiscFacet.disciplineName);
        DOMAssert lAssert = new DOMAssert(lAttrId);
        lAssert.assertStmt = "if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq '"
            + lDiscFacet.disciplineName + "')) then " + "pds:facet1" + " = (";
        lAssert.assertMsg =
            "If the attribute pds:discipline_name equals " + lDiscFacet.disciplineName
                + " then if present pds:facet1 must be equal to one of the following values ";
        lRule.assertArr.add(lAssert);
        String lValueList = "";
        String lDelimiter = "";
        for (Iterator<SFGroupFacetDefn> j = lDiscFacet.groupFacet1Arr.iterator(); j.hasNext();) {
          SFGroupFacetDefn lGroupFacet = j.next();
          lValueList += lDelimiter + "'" + lGroupFacet.facet + "'";
          lDelimiter = ", ";
        }
        lAssert.assertStmt += lValueList;
        lAssert.assertMsg += lValueList;
        lAssert.assertStmt += ") else true()";
        lAssert.assertMsg += ".";
      }

      // get all the facet2 assert statements
      if (lDiscFacet.groupFacet2Arr.size() > 0) {
        lDiscNameFacet2Arr.add(lDiscFacet.disciplineName);
        DOMAssert lAssert = new DOMAssert(lAttrId);
        lAssert.assertStmt = "if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq '"
            + lDiscFacet.disciplineName + "')) then " + "pds:facet2" + " = (";
        lAssert.assertMsg =
            "If the attribute pds:discipline_name equals " + lDiscFacet.disciplineName
                + " then if present pds:facet2 must be equal to one of the following values ";
        lRule.assertArr.add(lAssert);
        String lValueList = "";
        String lDelimiter = "";
        for (Iterator<SFGroupFacetDefn> j = lDiscFacet.groupFacet2Arr.iterator(); j.hasNext();) {
          SFGroupFacetDefn lGroupFacet = j.next();
          lValueList += lDelimiter + "'" + lGroupFacet.facet + "'";
          lDelimiter = ", ";
        }
        lAssert.assertStmt += lValueList;
        lAssert.assertMsg += lValueList;
        lAssert.assertStmt += ") else true()";
        lAssert.assertMsg += ".";
      }
    }

    // create the rules for disallowed facets
    int lNumDiscNames = lDisciplineFacetArr.size();
    if (lNumDiscNames > lDiscNameFacet1Arr.size()) {
      DOMAssert lAssert = new DOMAssert(lAttrId);
      lRule.assertArr.add(lAssert);
      lAssert.assertStmt = "if (pds:discipline_name and pds:facet1) then pds:discipline_name = (";
      lAssert.assertMsg =
          "Facet1 is allowed only when pds:discipline_name is one of the following ";
      // if (pds:discipline_name and pds:facet1) then pds:discipline_name = ('Fields', 'Particles')
      // else true()
      // Facet1 is allowed only for pds:discipline_name equals 'Fields' or 'Particles'.
      String lValueList = "";
      String lDelimiter = "";
      for (Iterator<String> i = lDiscNameFacet1Arr.iterator(); i.hasNext();) {
        String lDiscName = i.next();
        lValueList += lDelimiter + "'" + lDiscName + "'";
        lDelimiter = ", ";
      }
      lAssert.assertStmt += lValueList;
      lAssert.assertMsg += lValueList;
      lAssert.assertStmt += ") else true()";
      lAssert.assertMsg += ".";
    }

    if (lNumDiscNames > lDiscNameFacet2Arr.size()) {
      DOMAssert lAssert = new DOMAssert(lAttrId);
      lRule.assertArr.add(lAssert);
      lAssert.assertStmt = "if (pds:discipline_name and pds:facet2) then pds:discipline_name = (";
      lAssert.assertMsg =
          "Facet2 is allowed only when pds:discipline_name is one of the following ";
      String lValueList = "";
      String lDelimiter = "";
      for (Iterator<String> i = lDiscNameFacet2Arr.iterator(); i.hasNext();) {
        String lDiscName = i.next();
        lValueList += lDelimiter + "'" + lDiscName + "'";
        lDelimiter = ", ";
      }
      lAssert.assertStmt += lValueList;
      lAssert.assertMsg += lValueList;
      lAssert.assertStmt += ") else true()";
      lAssert.assertMsg += ".";
    }

    // set up the Primary Result Summary rule
    lXpath = "pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1";
    lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
    lRule = new DOMRule(lXpath);
    DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
    DOMInfoModel.masterDOMRuleArr.add(lRule);
    lRule.setRDFIdentifier();
    DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);

    lRule.xpath = lXpath;
    lRule.attrTitle = "subfacet1";
    lRule.attrNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classTitle = "Science_Facets";
    lRule.classNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classSteward = DMDocument.masterNameSpaceIdNCLC;
    lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
    DOMAssert lAssert = new DOMAssert(lAttrId);
    lRule.assertArr.add(lAssert);
    lAssert.assertStmt = "name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1'";
    lAssert.assertMsg = "pds:subfacet1 should not be used. No values have been provided.";

    lXpath = "pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2";
    lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
    lRule = new DOMRule(lXpath);
    DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
    DOMInfoModel.masterDOMRuleArr.add(lRule);
    lRule.setRDFIdentifier();
    DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);

    lRule.xpath = lXpath;
    lRule.attrTitle = "subfacet2";
    lRule.attrNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classTitle = "Science_Facets";
    lRule.classNameSpaceNC = DMDocument.masterNameSpaceIdNCLC;
    lRule.classSteward = DMDocument.masterNameSpaceIdNCLC;
    lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
    lAssert = new DOMAssert(lAttrId);
    lRule.assertArr.add(lAssert);
    lAssert.assertStmt = "name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2'";
    lAssert.assertMsg = "pds:subfacet2 should not be used. No values have been provided.";
  }

  // add the enumerated unit schematron rules
  public void addSchematronRuleUnits(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap) {

    // add attribute unit values assertions
    ArrayList<DOMClass> lSortClassArr = new ArrayList<>(lMasterDOMClassMap.values());
    for (Iterator<DOMClass> i = lSortClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if ((lClass == null) || !((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0)
          && lSchemaFileDefn.stewardArr.contains(lClass.steward))) {
        continue;
      }
      if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) {
        continue;
      }
      for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();

        if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
          if (lDOMAttr.unit_of_measure_type == null
              || lDOMAttr.unit_of_measure_type.indexOf("TBD") == 0) {
            continue;
          }
          String lUnitsValueString = lDOMAttr.getUnits(true);
          if (lUnitsValueString == null) {
            continue;
          }
          String lXpath =
              lClass.nameSpaceId + lClass.title + "/" + lClass.nameSpaceId + lDOMAttr.title;
          DOMRule lRule = DOMInfoModel.masterDOMRuleIdMap.get(lXpath);
          if (lRule == null) {
            lRule = new DOMRule(lXpath);
            DOMInfoModel.masterDOMRuleIdMap.put(lRule.identifier, lRule);
            DOMInfoModel.masterDOMRuleArr.add(lRule);
            lRule.setRDFIdentifier();
            DOMInfoModel.masterDOMRuleMap.put(lRule.rdfIdentifier, lRule);

            lRule.xpath = lXpath;
            lRule.attrTitle = lDOMAttr.title;
            lRule.attrNameSpaceNC = lDOMAttr.nameSpaceIdNC;
            lRule.classTitle = lClass.title;
            lRule.classNameSpaceNC = lClass.nameSpaceIdNC;
            lRule.classSteward = lClass.steward;
          }
          String assertMsgPre = " must be equal to one of the following values ";
          String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
          if (foundAssertStmt(lAttrId, lRule.assertArr)) {
            continue;
          }
          DOMAssert lAssert = new DOMAssert(lAttrId);
          lAssert.assertStmt = "@unit" + " = (";
          lAssert.assertMsg = "The attribute " + "@unit" + assertMsgPre;
          lRule.assertArr.add(lAssert);
          lAssert.assertStmt += lUnitsValueString + ")";
          lAssert.assertMsg += lUnitsValueString + ".";
        }
      }
    }
  }

  // find offending rule
  public void findOffendingRule(String lTitle, ArrayList<DOMRule> lRuleArr) {
    for (Iterator<DOMRule> i = lRuleArr.iterator(); i.hasNext();) {
      DOMRule lRule = i.next();
      if (lRule.classTitle.indexOf("Discipline_Facets") > -1
          || lRule.identifier.indexOf("Discipline_Facets") > -1) {
        System.out.println("debug findOffendingRule - lRule.identifier:" + lRule.identifier);
      }
    }
    return;
  }

  // check if assert statement already exists
  public boolean foundAssertStmt(String lAttrId, ArrayList<DOMAssert> lAssertArr) {
    for (Iterator<DOMAssert> i = lAssertArr.iterator(); i.hasNext();) {
      DOMAssert lAssert = i.next();
      if (lAssert.identifier.compareTo(lAttrId) == 0) {
        return true;
      }
    }
    return false;
  }

  private void findChildrenOfAssociatedExternalClass() {
    // find all classes with isAssociatedExternalClass = true - e.g., geom.Body_Identification_Base
    ArrayList<String> lParentClassIdArr = new ArrayList();
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (lDOMClass.isUSERClass || lDOMClass.isUnitOfMeasure || lDOMClass.isDataType
          || lDOMClass.isVacuous) {
        continue;
      }
      if (!lDOMClass.isAssociatedExternalClass) {
        continue;
      }
      ParentChildrenRelation lParentChildrenRelation =
          parentChildrenRelationMap.get(lDOMClass.identifier);
      if (lParentChildrenRelation == null) {
        lParentChildrenRelation = new ParentChildrenRelation(lDOMClass);
        parentChildrenRelationMap.put(lDOMClass.identifier, lParentChildrenRelation);
        lParentClassIdArr.add(lDOMClass.identifier);
      } else {
        Utility.registerMessage("1>error "
            + "Duplicate Found - Adding Extern Class to parentChildrenRelationMap - lDOMClass.identifier:"
            + lDOMClass.identifier);
      }
    }

    if (lParentClassIdArr == null || lParentClassIdArr.isEmpty()) {
      return;
    }

    // find all classes that are children of Associated External Class - e.g.,
    // Central_Body_Identification
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (lDOMClass.isUSERClass || lDOMClass.isUnitOfMeasure || lDOMClass.isDataType
          || lDOMClass.isVacuous) {
        continue;
      }
      if (lDOMClass.subClassOf == null) {
        continue;
      }
      if (lParentClassIdArr.contains(lDOMClass.subClassOf.identifier)) {
        ParentChildrenRelation lParentChildrenRelation =
            parentChildrenRelationMap.get(lDOMClass.subClassOf.identifier);
        if (lParentChildrenRelation != null) {
          lParentChildrenRelation.childClassArr.add(lDOMClass);
        }

      }
    }
  }

  // class for capturing Parent/Child relationships (Parent is defined using DD Associated External
  // Class)
  private class ParentChildrenRelation {
    DOMClass parentClass; // e.g., Body_Identification_Base
    ArrayList<DOMClass> childClassArr; // e.g., Central_Body_Identification

    private ParentChildrenRelation(DOMClass lParentClass) {
      parentClass = lParentClass;
      childClassArr = new ArrayList<>();
    }

    void addChildClass(DOMClass lChildClass) {
      childClassArr.add(lChildClass);
      return;
    }
  }
}
