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

class ISO11179DOMMDR extends Object {
  TreeMap<String, String> hashCodedPersmissibleValueMap;
  TreeMap<String, String> hashCodedValueMeaningMap;
  TreeMap<String, String> hashCodedIsDeprecatedMap;
  TreeMap<String, String> valueChangeMap;

  public ISO11179DOMMDR() {
    hashCodedPersmissibleValueMap = new TreeMap<>();
    hashCodedValueMeaningMap = new TreeMap<>();
    hashCodedIsDeprecatedMap = new TreeMap<>();
    valueChangeMap = new TreeMap<>();

    // set up permissible value / value meaning map; need identifier from map
    ArrayList<InstDefn> lMaster11179DataDictArr =
        new ArrayList<>(DOMInfoModel.master11179DataDict.values());
    for (Iterator<InstDefn> i = lMaster11179DataDictArr.iterator(); i.hasNext();) {
      InstDefn lInstDefn = i.next();
      if (lInstDefn.className.compareTo("PermissibleValue") == 0) {

        // get value
        ArrayList<String> lValArr = lInstDefn.genSlotMap.get("value");
        if (lValArr != null) {
          String lValue = lValArr.get(0);
          String lKey = lInstDefn.title;
          int lOffset = lInstDefn.title.lastIndexOf(".");
          if (lOffset > -1) {
            lKey = lInstDefn.title.substring(0, lOffset);
          }
          lValArr = lInstDefn.genSlotMap.get("usedIn");
          if (lValArr != null) {
            String lVMId = lValArr.get(0);
            String lVMIdExt = "ValueMeaning" + "." + lVMId;
            InstDefn lVMInst = DOMInfoModel.master11179DataDict.get(lVMIdExt);
            if (lVMInst != null) {
              lValArr = lVMInst.genSlotMap.get("description");
              if (lValArr != null) {
                String lDescription = lValArr.get(0);
                lKey += "." + lValue;
                hashCodedValueMeaningMap.put(lKey, lDescription);
              }
            }
          }
        }
        // get isDeprecated
        String lSlotIsDeprecated = lInstDefn.getSlotValueSingleton("isDeprecated");
        if (lSlotIsDeprecated != null && lSlotIsDeprecated.compareTo("true") == 0) {
          String lSlotValue = lInstDefn.getSlotValueSingleton("value");
          if (lSlotValue != null) {
            String lKey = lInstDefn.title;
            int lOffset = lInstDefn.title.lastIndexOf(".");
            if (lOffset > -1) {
              lKey = lInstDefn.title.substring(0, lOffset);
            }
            lKey += "." + lSlotValue;
            hashCodedIsDeprecatedMap.put(lKey, lSlotIsDeprecated);
          }
        }
      }
    }
    return;
  }

  // set up the 11179 elements
  public void ISO11179MDRSetup() {
    for (Iterator<DOMAttr> i = DOMInfoModel.getAttArrByTitleStewardClassSteward().iterator(); i
        .hasNext();) {
      DOMAttr lAttr = i.next();
      if (lAttr.isUsedInClass && lAttr.isAttribute) {
        getAttrISOAttr(lAttr);
      }
    }
  }

  // Get the ISO components of a data element
  private void getAttrISOAttr(DOMAttr lattr) {
    // add the namespace to the above
    lattr.administrationRecordValue = DMDocument.administrationRecordValue;
    lattr.versionIdentifierValue = DMDocument.versionIdentifierValue;
    lattr.submitter = DMDocument.submitterValue;
    lattr.registeredByValue = DMDocument.registeredByValue;
    lattr.registrationAuthorityIdentifierValue = DMDocument.registrationAuthorityIdentifierValue;
  }

  // scan through the master attribute list and overwrite with the latest from the 11179 DD
  // database.
  public void OverwriteFrom11179DataDict() {
    HashMap<String, ArrayList<String>> lInstMap = new HashMap<>();
    String lSuffix;
    String lInstId;
    String lVal;
    boolean isEnumerated = false;
    boolean lDebugFlag = false;

    // iterate through the master attribute array
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (!lAttr.isAttribute || lAttr.isFromLDD) {
        continue;
      }
      DOMClass lParentClass = lAttr.attrParentClass;
      if (lParentClass == null) {
        continue;
      }
      if (lParentClass.isFromLDD) {
        continue;
      }

      lSuffix = DOMInfoModel.getAttrIdentifier(lAttr.classNameSpaceIdNC, lAttr.parentClassTitle,
          lAttr.nameSpaceIdNC, lAttr.title);

      lInstId = "DataElement" + "." + "DE" + "." + lSuffix;
      InstDefn lDEInst = DOMInfoModel.master11179DataDict.get(lInstId);
      if (lDEInst != null) {
        lInstMap = lDEInst.genSlotMap;

        // update isDeprecated
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isDeprecated", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.isDeprecated:" + lAttr.isDeprecated + "  - with lVal:" + lVal);
          }
          lAttr.isDeprecated = true;
        }

        // update steward
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "steward", lAttr.steward);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.steward:" + lAttr.steward + "  - with lVal:" + lVal);
          }
          lAttr.steward = lVal;
        }

        // update classConcept
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "expressedBy",
            lAttr.classConcept);
        if (lVal != null) {
          lVal = lVal.substring(4, lVal.length());
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.classConcept:" + lAttr.classConcept + "  - with lVal:" + lVal);
          }
          lAttr.classConcept = lVal;
        }

        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isNillable", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.isNilable:" + lAttr.isNilable + "  - with lVal:" + lVal);
          }
          lAttr.isNilable = true;
        }

      } else if (!(lAttr.title.compareTo("%3ANAME") == 0)) {
        DMDocument.registerMessage("1>error "
            + "11179 data dictionary attribute is missing for overwrite - Identifier:" + lInstId);
      }
      boolean hasVD = false;
      isEnumerated = false;
      lInstId = "EnumeratedValueDomain" + "." + "EVD" + "." + lSuffix;
      InstDefn lVDInst = DOMInfoModel.master11179DataDict.get(lInstId);
      if (lAttr.isEnumerated && lVDInst != null) {
        hasVD = true;
        isEnumerated = true;
      } else {
        lInstId = "NonEnumeratedValueDomain" + "." + "NEVD" + "." + lSuffix;
        lVDInst = DOMInfoModel.master11179DataDict.get(lInstId);
        if (lVDInst != null) {
          hasVD = true;
        }
      }
      if (hasVD) {
        lInstMap = lVDInst.genSlotMap;
        // dataType
        lVal =
            ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "datatype", lAttr.valueType);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.valueType:" + lAttr.valueType + "  - with lVal:" + lVal);
          }
          lAttr.valueType = lVal;
        }

        // dataConcept
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "representedBy2",
            lAttr.dataConcept);
        if (lVal != null) {
          lVal = lVal.substring(3, lVal.length());
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.dataConcept:" + lAttr.dataConcept + "  - with lVal:" + lVal);
          }
          lAttr.dataConcept = lVal;
        }

        // Minimum Value
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "minimumValue",
            lAttr.minimum_value);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.minimum_value:" + lAttr.minimum_value + "  - with lVal:"
                + lVal);
          }
          lAttr.minimum_value = lVal;
        }

        // Maximum Value
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "maximumValue",
            lAttr.maximum_value);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.maximum_value:" + lAttr.maximum_value + "  - with lVal:"
                + lVal);
          }
          lAttr.maximum_value = lVal;
        }

        // Minimum Characters
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "minimumCharacterQuantity",
            lAttr.minimum_characters);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.minimum_characters:" + lAttr.minimum_characters
                + "  - with lVal:" + lVal);
          }
          lAttr.minimum_characters = lVal;
        }

        // Maximum Characters
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "maximumCharacterQuantity",
            lAttr.maximum_characters);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.maximum_characters:" + lAttr.maximum_characters
                + "  - with lVal:" + lVal);
          }
          lAttr.maximum_characters = lVal;
        }

        // Pattern
        lVal = ProtPinsDOM11179DD.getStringValueUpdatePattern(true, lInstMap, "pattern",
            lAttr.pattern);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.pattern:" + lAttr.pattern + "  - with lVal:" + lVal);
          }
          lAttr.pattern = lVal;
        }

        // Unit_of_measure_type
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "unitOfMeasure",
            lAttr.unit_of_measure_type);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.unit_of_measure_type:" + lAttr.unit_of_measure_type
                + "  - with lVal:" + lVal);
          }
          lAttr.unit_of_measure_type = lVal;
        }

        // default_unit_id
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "defaultUnitId",
            lAttr.default_unit_id);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite with 11179 - got lInstId:" + lInstId
                + " - updating lAttr.default_unit_id:" + lAttr.default_unit_id + "  - with lVal:"
                + lVal);
          }
          lAttr.default_unit_id = lVal;
        }

        // get the permissible value meanings
        if (lAttr.domPermValueArr != null && lAttr.domPermValueArr.size() > 0) {
          for (Iterator<DOMProp> j = lAttr.domPermValueArr.iterator(); j.hasNext();) {
            DOMProp lDOMProp = j.next();
            if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
              DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.hasDOMObject;

              // title:vm.0001_NASA_PDS_1.pds.DD_Class_Full.steward_id.111209
              String lKey =
                  "pv."
                      + DOMInfoModel.getAttrIdentifier(lAttr.classNameSpaceIdNC,
                          lAttr.parentClassTitle, lAttr.nameSpaceIdNC, lAttr.title)
                      + "." + lDOMPermVal.value;
              String lValueMeaning = hashCodedValueMeaningMap.get(lKey);
              if (lValueMeaning != null) {
                lDOMPermVal.value_meaning = lValueMeaning;
              }

              // update isDeprecated
              String lIsDeprecatedKey = "pv" + "." + lSuffix + "." + lDOMPermVal.value;
              String lIsDeprecated = hashCodedIsDeprecatedMap.get(lIsDeprecatedKey);
              if (lIsDeprecated != null) {
                lDOMPermVal.isDeprecated = true;
              }
            }
          }
        }
      }
    }
  }

  // scan through the master class list and overwrite with the latest from the 11179 DD database.
  public void OverwriteClassFrom11179DataDict() {
    HashMap<String, ArrayList<String>> lInstMap = new HashMap<>();
    String lSuffix;
    String lInstId;
    String lVal;
    boolean lDebugFlag = false;

    // iterate through the master class array
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (lDOMClass.isFromLDD) {
        continue;
      }
      lSuffix = DOMInfoModel.getClassIdentifier(lDOMClass.nameSpaceIdNC, lDOMClass.title);
      lInstId = "ObjectClass" + "." + "OC" + "." + lSuffix;
      InstDefn lOCInst = DOMInfoModel.master11179DataDict.get(lInstId);
      if (lOCInst != null) {
        lInstMap = lOCInst.genSlotMap;

        // update isDeprecated
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isDeprecated", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isDeprecated:" + lDOMClass.isDeprecated + "  - with lVal:"
                + lVal);
          }
          lDOMClass.isDeprecated = true;
        }

        // update used
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "used", lDOMClass.used);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.used:" + lDOMClass.used + "  - with lVal:" + lVal);
          }
          lDOMClass.used = lVal;
        }

        // update section
        lVal =
            ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "section", lDOMClass.section);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.section:" + lDOMClass.section + "  - with lVal:" + lVal);
          }
          lDOMClass.section = lVal;
        }

        // update steward
        lVal =
            ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "steward", lDOMClass.steward);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.steward:" + lDOMClass.steward + "  - with lVal:" + lVal);
          }
          lDOMClass.steward = lVal;
        }

        // update nameSpaceIdNC
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "nameSpaceIdNC",
            lDOMClass.nameSpaceIdNC);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.nameSpaceIdNC:" + lDOMClass.nameSpaceIdNC + "  - with lVal:"
                + lVal);
          }
          lDOMClass.nameSpaceIdNC = lVal;
        }

        // update isVacuous
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isVacuous", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isVacuous:" + lDOMClass.isVacuous + "  - with lVal:" + lVal);
          }
          lDOMClass.isVacuous = true;
        }

        // update isSchema1Class
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isSchema1Class", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isSchema1Class:" + lDOMClass.isSchema1Class + "  - with lVal:"
                + lVal);
          }
          lDOMClass.isSchema1Class = true;
        }

        // update isRegistryClass
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isRegistryClass", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isRegistryClass:" + lDOMClass.isRegistryClass
                + "  - with lVal:" + lVal);
          }
          lDOMClass.isRegistryClass = true;
        }

        // update isTDO
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isTDO", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isTDO:" + lDOMClass.isTDO + "  - with lVal:" + lVal);
          }
          lDOMClass.isTDO = true;
        }

        // update isDataType
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isDataType", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isDataType:" + lDOMClass.isDataType + "  - with lVal:" + lVal);
          }
          lDOMClass.isDataType = true;
        }

        // update isUnitOfMeasure
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "isUnitOfMeasure", "false");
        if (lVal != null && lVal.compareTo("true") == 0) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.isUnitOfMeasure:" + lDOMClass.isUnitOfMeasure
                + "  - with lVal:" + lVal);
          }
          lDOMClass.isUnitOfMeasure = true;
        }

        // update versionIdentifier
        lVal = ProtPinsDOM11179DD.getStringValueUpdate(false, lInstMap, "versionIdentifier",
            lDOMClass.versionId);
        if (lVal != null) {
          if (lDebugFlag) {
            System.out.println("debug Overwrite Class with 11179 - got lInstId:" + lInstId
                + " - updating Class.versionId:" + lDOMClass.versionId + "  - with lVal:" + lVal);
          }
          lDOMClass.versionId = lVal;
        }

      } else // DMDocument.registerMessage ("1>error " + "11179 data dictionary class is missing for
             // overwrite - Identifier:" + lInstId);
      if (lDOMClass.title.compareTo("USER") != 0) {
        DMDocument.registerMessage("1>error "
            + "11179 data dictionary class is missing for overwrite - Identifier:" + lInstId);
      }
    }
  }

  // Get class order
  public void getClassOrder() {
    // iterate through the associations
    for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      // String lSuffix = DOMInfoModel.getAttrIdentifier (lProp.classNameSpaceIdNC,
      // lProp.parentClassTitle, lProp.nameSpaceIdNC, lProp.title);
      // String lInstId = "Property" + "." + "PR" + "." + lSuffix;
      String lInstId = "Property" + "." + "PR" + "." + lProp.identifier;
      InstDefn lPRInst = DOMInfoModel.master11179DataDict.get(lInstId);
      if (lPRInst == null) {
        continue;
      }
      HashMap<String, ArrayList<String>> lInstMap = lPRInst.genSlotMap;
      if (lInstMap == null) {
        continue;
      }
      ArrayList<String> lValArr = lInstMap.get("classOrder");
      if (lValArr == null) {
        continue;
      }
      String lClassOrder = lValArr.get(0);
      if (lClassOrder.compareTo("") == 0) {
        continue;
      }
      lProp.classOrder = lClassOrder;
    }
    return;
  }
}
