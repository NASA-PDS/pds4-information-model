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
import java.util.Collections;
import java.util.Iterator;
import gov.nasa.pds.model.plugin.util.Utility;

public class DOMProtAttr extends ISOClassOAIS11179 {
  String sort_identifier; // lAttr.title + "_" + lAttr.steward + "_" + lAttr.className + "_" +
                          // lAttr.classSteward
  String attrAnchorString; // "attribute", lAttr.attrNameSpaceIdNC, lAttr.title,
                           // lAttr.classNameSpaceIdNC, lAttr.className
  String XMLSchemaName; // Title or Class_Title
  // String regAuthId; // registration authority identifier
  String classSteward; // steward for attribute's class
  String classNameSpaceIdNC;
  String submitter; // submitter for attribute
  String parentClassTitle; // class that this attribute is a member of
  DOMClass attrParentClass; // class instance that this attribute is a member of
                            // *** deprecate *** moved to DOMProp
  String classConcept; // for DEC
  String dataConcept; // for CD
  String classWord; // for nomenclature rules
  String lddLocalIdentifier; // LDD local identifier

  String xmlBaseDataType; // the XML base data type
  String protValType; // value type from protege model
  String propType; // Instance or Attribute
  String valueType; // Master value type
  String groupName; // the choice group name
  String referenceType;
  String cardMin;
  String cardMax;
  int cardMinI;
  int cardMaxI;

  String minimum_characters; // minimum number of characters
  String maximum_characters; // maximum number of characters
  String minimum_value; // minimum value
  String maximum_value; // maximum value
  String format; // a template for the structure of the presentation of the Value(s) e.g. YYYY-MM-DD
                 // for a date.
  String pattern; // a regular expression
  String unit_of_measure_type; //
  String default_unit_id; //
  String unit_of_measure_precision; //

  // String type;
  boolean isAttribute; // true->attribute; false->association
  boolean isOwnedAttribute; // true->attribute is owned by this class, as opposed to inherited
  boolean isPDS4; // true->PDS4 keyword used in Protege
  boolean isEnumerated;
  boolean isUsedInClass; // attribute is used in a class
  boolean isRestrictedInSubclass; // *** deprecate *** moved to DOMProp
  boolean isMeta;
  boolean hasAttributeOverride;
  boolean isNilable;
  boolean isChoice; // Association or Instance attributes that require a class choice
  boolean isAny; // Association or Instance attribute that allows a class any
  boolean isFromLDD; // attribute came from an LDD
  boolean hasRetiredValue; // at least one permissible value has been retired

  ArrayList<String> valArr;
  ArrayList<String> valArrSorted;

  public DOMProtAttr() {
    sort_identifier = "TBD_sort_identifier";
    attrAnchorString = "TBD_attrAnchorString";
    XMLSchemaName = "TBD_XMLSchemaName";
    classSteward = "TBD_classSteward";
    classNameSpaceIdNC = "TBD_classNameSpaceId";
    submitter = "TBD_submitter";
    parentClassTitle = "TBD_parentClassTitle";
    attrParentClass = null;
    classConcept = "TBD_classConcept";
    dataConcept = "TBD_dataConcept";
    classWord = "TBD_classWord";
    lddLocalIdentifier = "TBD_lddLocalIdentifier";

    xmlBaseDataType = "TBD_XML_Base_Data_Type";
    protValType = "TBD_Protege_Value_type";
    propType = "TBD_slot_type";
    valueType = "TBD_value_type";
    groupName = "TBD_groupName";
    referenceType = "TBD_referenceType";
    cardMin = "TBD_cardMin";
    cardMax = "TBD_cardMax";
    cardMinI = -99999;
    cardMaxI = -99999;

    maximum_characters = "TBD_maximum_characters";
    minimum_characters = "TBD_minimum_characters";
    maximum_value = "TBD_maximum_value";
    minimum_value = "TBD_minimum_value";
    format = "TBD_format";
    pattern = "TBD_pattern";
    unit_of_measure_type = "TBD_unit_of_measure_type";
    default_unit_id = "TBD_default_unit_id";
    unit_of_measure_precision = "TBD_unit_of_measure_precision";

    isAttribute = true;
    isOwnedAttribute = false;
    isPDS4 = false;
    isEnumerated = false;
    isUsedInClass = false;
    isRestrictedInSubclass = false;
    isMeta = false;
    hasAttributeOverride = false;
    isNilable = false;
    isChoice = false;
    isAny = false;
    isFromLDD = false;
    hasRetiredValue = false;

    valArr = new ArrayList<>();

  }

  public void setRDFIdentifier(String lClassTitle, String lAttrTitle) {
    this.title = lAttrTitle;
    this.sequenceId = DOMInfoModel.getNextUId();
    this.rdfIdentifier =
        DMDocument.rdfPrefix + lClassTitle + "." + lAttrTitle + "." + this.sequenceId;
  }

  public void setIdentifier(String lNameSpaceIdNC, String lTitle, String lNameSpaceIdNC2,
      String lTitle2) {
    this.identifier =
        DOMInfoModel.getAttrIdentifier(lNameSpaceIdNC, lTitle, lNameSpaceIdNC2, lTitle2);
    this.nsTitle = lNameSpaceIdNC2 + "." + lTitle2;
  }

  // copy a string array
  public void InitStringArr(ArrayList<String> lDomStrArr, ArrayList<String> lPDSStrArr) {
    if (lPDSStrArr == null) {
      return;
    }
    for (Iterator<String> i = lPDSStrArr.iterator(); i.hasNext();) {
      String lOldStr = i.next();
      if (lOldStr != null) {
        lDomStrArr.add(lOldStr);
      } else {
        Utility.registerMessage("1>error " + "InitStringArr - Null DomStr");
      }
    }
  }

  // convert a Protege Attribute to a DOM Property
  public ArrayList<DOMPropComp> convertToDOMPropCompArr() {
    ArrayList<DOMPropComp> lDomPropCompArr = new ArrayList<>();

    // sort the value array
    valArrSorted = new ArrayList<>(valArr);
    Collections.sort(valArrSorted);

    if (this.isAttribute) {
      DOMPropComp lDomPropComp = new DOMPropComp();
      lDomPropComp.domProp = convertToDOMProp();
      lDomPropComp.domComp = convertToDOMAttr();
      lDomPropCompArr.add(lDomPropComp);
    } else {
      for (Iterator<String> i = valArrSorted.iterator(); i.hasNext();) {
        String lClassTitle = i.next();
        if (lClassTitle == null) {
          continue;
        }
        // get class
        String lClassMemberIdentifier =
            DOMInfoModel.getClassIdentifier(this.nameSpaceIdNC, lClassTitle);
        DOMClass lClassMember = DOMInfoModel.masterDOMClassIdMap.get(lClassMemberIdentifier);
        if (lClassMember != null) {
          DOMPropComp lDomPropComp = new DOMPropComp();
          // get property
          DOMProp lDOMProp = convertToDOMProp();
          // make property identifiers unique
          lDOMProp.rdfIdentifier = lDOMProp.rdfIdentifier + "." + lClassTitle;
          lDOMProp.identifier = lDOMProp.identifier + "." + lClassTitle;
          lDomPropComp.domProp = lDOMProp;
          lDomPropComp.domComp = lClassMember;
          lDomPropCompArr.add(lDomPropComp);
        } else {
          Utility.registerMessage(
              "1>error " + "Could not find the class referenced in an association - identifier:"
                  + this.identifier + "   Class:" + lClassTitle);
          Utility.registerMessage(
              "1>error " + "Could not find the class referenced in an association - identifier:"
                  + this.identifier + "   lClassMemberIdentifier:" + lClassMemberIdentifier);
        }
      }
    }
    return lDomPropCompArr;
  }

  // convert a Protege Attribute to a DOM Property
  public DOMProp convertToDOMProp() {
    DOMProp lProp = new DOMProp();
    lProp.rdfIdentifier = this.rdfIdentifier;
    lProp.identifier = this.identifier;
    lProp.subModelId = this.subModelId;
    lProp.versionId = this.versionId;
    lProp.sequenceId = this.sequenceId;
    lProp.title = this.title;
    lProp.nsTitle = this.nsTitle;
    lProp.definition = this.definition;
    lProp.registrationStatus = this.registrationStatus;
    lProp.regAuthId = this.regAuthId;
    lProp.steward = this.steward;
    lProp.nameSpaceIdNC = this.nameSpaceIdNC;
    lProp.nameSpaceId = this.nameSpaceId;
    // lProp.classOrder = lOldProp.classOrder; // set from database
    // lProp.propType = this.propType; // does not seem to be needed in property
    // lProp.protValType = this.protValType; // does not seem to be needed in property
    if (this.isAttribute) {
      lProp.referenceType = "attribute_of";
    } else {
      lProp.referenceType = this.title;
    }
    lProp.cardMin = this.cardMin;
    lProp.cardMax = this.cardMax;
    lProp.cardMinI = this.cardMinI;
    lProp.cardMaxI = this.cardMaxI;
    lProp.parentClassTitle = this.parentClassTitle;
    lProp.classNameSpaceIdNC = this.classNameSpaceIdNC;
    lProp.classSteward = this.classSteward;
    lProp.groupName = this.groupName;
    lProp.isPDS4 = this.isPDS4;
    lProp.isAttribute = this.isAttribute;
    // lProp.isAttribute = false;
    // lProp.isOwnedAttribute = this.isOwnedAttribute; // does not seem to be needed in property
    // lProp.isEnumerated = this.isEnumerated; // does not seem to be needed in property
    lProp.isChoice = this.isChoice;
    return lProp;
  }

  // convert a Protege Attribute to a DOM Property
  public DOMAttr convertToDOMAttr() {
    DOMAttr lAttr = new DOMAttr();
    lAttr.rdfIdentifier = this.rdfIdentifier;
    lAttr.identifier = this.identifier;
    lAttr.subModelId = this.subModelId;
    lAttr.versionId = this.versionId;
    lAttr.sequenceId = this.sequenceId;
    lAttr.title = this.title;
    lAttr.nsTitle = this.nsTitle;
    lAttr.definition = this.definition;
    lAttr.registrationStatus = this.registrationStatus;
    lAttr.regAuthId = this.regAuthId;
    lAttr.steward = this.steward;
    lAttr.nameSpaceIdNC = this.nameSpaceIdNC;
    lAttr.nameSpaceId = this.nameSpaceId;
    // lAttr.classOrder = lOldProp.classOrder; // set from database
    lAttr.propType = this.propType;
    lAttr.protValType = this.protValType;
    lAttr.cardMin = this.cardMin;
    lAttr.cardMax = this.cardMax;
    lAttr.cardMinI = this.cardMinI;
    lAttr.cardMaxI = this.cardMaxI;
    lAttr.parentClassTitle = this.parentClassTitle;
    lAttr.classNameSpaceIdNC = this.classNameSpaceIdNC;
    lAttr.classSteward = this.classSteward;
    lAttr.groupName = this.groupName;
    lAttr.isPDS4 = this.isPDS4;
    lAttr.isAttribute = this.isAttribute;
    lAttr.isOwnedAttribute = this.isOwnedAttribute;
    lAttr.isEnumerated = this.isEnumerated;
    lAttr.isChoice = this.isChoice;
    lAttr.set11179Attr(lAttr.identifier);

    createDOMPermValDefn(lAttr, this.valArr);

    InitStringArr(lAttr.valArr, this.valArr);

    return lAttr;
  }

  // convert a Protege Value Array (valArr) to a DOMPermValDefn
  public void createDOMPermValDefn(DOMAttr lDOMAttr, ArrayList<String> lValArr) {
    if (lValArr == null) {
      return;
    }

    for (Iterator<String> i = valArrSorted.iterator(); i.hasNext();) {
      String lValue = i.next();
      if (lValue != null && lValue.compareTo("") != 0) {
        DOMPermValDefn lDOMPermVal = new DOMPermValDefn();
        lDOMPermVal.value = lValue;
        DOMProp lDOMProp = createDOMProp(lDOMAttr.classNameSpaceIdNC, lDOMAttr.parentClassTitle,
            lDOMAttr.nameSpaceIdNC, lDOMAttr.title + lValue);
        lDOMAttr.domPermValueArr.add(lDOMProp);
        lDOMProp.hasDOMObject = lDOMPermVal;
      }
    }
  }

  // create a DOM Property
  public DOMProp createDOMProp(String lNameSpaceIdNC, String lTitle, String lNameSpaceIdNC2,
      String lTitle2) {
    DOMProp lDOMProp = new DOMProp();
    lDOMProp.setRDFIdentifier(lTitle);
    lDOMProp.setIdentifier(lNameSpaceIdNC, lTitle, lNameSpaceIdNC2, lTitle2);
    lDOMProp.title = lTitle;
    lDOMProp.registrationStatus = DMDocument.registrationAuthorityIdentifierValue;
    lDOMProp.nameSpaceIdNC = lNameSpaceIdNC;
    lDOMProp.nameSpaceId = lNameSpaceIdNC + ":";
    if (true) {
      lDOMProp.referenceType = "attribute_of";
    }
    lDOMProp.cardMin = "0";
    lDOMProp.cardMax = "1";
    lDOMProp.cardMinI = 0;
    lDOMProp.cardMaxI = 1;
    lDOMProp.isPDS4 = true;
    return lDOMProp;
  }
}
