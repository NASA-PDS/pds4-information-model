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
import java.util.Set;
import java.util.TreeMap;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Writes the PDS4 DOM DD content to a JSON file
 * 
 */

class WriteDOMDDJSONFile extends Object {

  ArrayList<String> adminRecUsedArr, adminRecTitleArr;

  public WriteDOMDDJSONFile() {
    return;
  }

  // write the JSON file
  public void writeJSONFile() throws java.io.IOException {
    if (!DMDocument.LDDToolFlag) {
      // PDS4_PDS_JSON_1910_DOM
      String lFileName2 = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDOMModelJSON;
      PrintWriter prDDPins2 = new PrintWriter(
          new OutputStreamWriter(new FileOutputStream(new File(lFileName2)), "UTF-8"));
      printPDDPHdr(DMDocument.masterPDSSchemaFileDefn, prDDPins2);
      printPDDPBody(DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC, prDDPins2);
      printPDDPFtr(prDDPins2);
      prDDPins2.close();
    } else {
      // write the JSON file for the LDD
      String lFileName = DMDocument.masterLDDSchemaFileDefn.relativeFileSpecDOMModelJSON;
      PrintWriter prDDPins = new PrintWriter(
          new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
      printPDDPHdr(DMDocument.masterLDDSchemaFileDefn, prDDPins);
      if (!DMDocument.includeAllNamespacesFlag) {
        printPDDPBody(DMDocument.masterLDDSchemaFileDefn.nameSpaceIdNC, prDDPins);
      } else {
        printPDDPBody("all", prDDPins);
      }
      printPDDPFtr(prDDPins);
      prDDPins.close();
    }
  }

  // Print the JSON Header
  public void printPDDPHdr(SchemaFileDefn lSchemaFileDefn2, PrintWriter prDDPins) {
    prDDPins.println("[");
    prDDPins.println("  {");
    prDDPins.println("    " + formValue("dataDictionary") + ": {");
    prDDPins
        .println("      " + formValue("Title") + ": " + formValue("PDS4 Data Dictionary") + " ,");
    prDDPins.println("      " + formValue("IM Version") + ": "
        + formValue(DMDocument.masterPDSSchemaFileDefn.ont_version_id) + " ,");
    prDDPins.println("      " + formValue("LDD Version") + ": "
        + formValue(lSchemaFileDefn2.ont_version_id) + " ,");
    prDDPins.println("      " + formValue("Date") + ": "
        + formValue(DMDocument.masterTodaysDateTimeUTCFromInstant) + " ,");
    prDDPins.println("      " + formValue("Description") + ": "
        + formValue("This document is a dump of the contents of the PDS4 Data Dictionary") + " ,");
    String lNSList = formValue("pds:");
    String del = ", ";
    for (Iterator<SchemaFileDefn> i = DMDocument.LDDSchemaFileSortArr.iterator(); i.hasNext();) {
      SchemaFileDefn lSchemaFileDefn = i.next();
      lNSList += del + formValue(lSchemaFileDefn.nameSpaceId);
      // del = ", ";
    }
    prDDPins.println("      " + formValue("namespaces") + ": [" + lNSList + "] ,");
  }

  // Format the Boolean String for JSON
  public String formBooleanValue(boolean lBoolean) {
    String rString = "" + lBoolean;
    return formValue(rString);
  }

  // Format the String for JSON
  public String formValue(String lString) {
    String rString = lString;
    if (rString == null) {
      rString = "null";
    }
    if (rString.indexOf("TBD") == 0) {
      rString = "null";
    }
    rString = DOMInfoModel.escapeJSONChar(rString);
    rString = "\"" + rString + "\"";
    return rString;
  }

  // Print the JSON Footer
  public void printPDDPFtr(PrintWriter prDDPins) {
    prDDPins.println("    }");
    prDDPins.println("  }");
    prDDPins.println("]");
  }

  // print the JSON body
  public void printPDDPBody(String lNameSpaceIdNC, PrintWriter prDDPins) {
    boolean isAllFlag = false;
    if (lNameSpaceIdNC.compareTo("all") == 0) {
      isAllFlag = true;
    }

    // Write Classes
    ArrayList<DOMClass> lSelectedClassArr = new ArrayList<>();
    for (Iterator<DOMClass> j = DOMInfoModel.masterDOMClassArr.iterator(); j.hasNext();) {
      DOMClass lSelectedClass = j.next();
      if (lSelectedClass.isInactive) {
        continue;
      }
      if (isAllFlag || lNameSpaceIdNC.compareTo(lSelectedClass.nameSpaceIdNC) == 0) {
        lSelectedClassArr.add(lSelectedClass);
      }
    }

    if (lSelectedClassArr.size() > 0) {
      prDDPins.println("      " + formValue("classDictionary") + ": [");
      printClass(lSelectedClassArr, prDDPins);
      prDDPins.println("      ]");
    }

    // Properties are listed under Classes as associations

    // Write Attributes
    ArrayList<DOMAttr> lSelectedAttrArr = new ArrayList<>();
    for (Iterator<DOMAttr> j = DOMInfoModel.masterDOMAttrArr.iterator(); j.hasNext();) {
      DOMAttr lSelectedAttr = j.next();
      if (lSelectedAttr.isInactive) {
        continue;
      }
      if (isAllFlag || lNameSpaceIdNC.compareTo(lSelectedAttr.classNameSpaceIdNC) == 0) {
        lSelectedAttrArr.add(lSelectedAttr);
      }
    }
    if (lSelectedAttrArr.size() > 0) {
      prDDPins.println("    , " + formValue("attributeDictionary") + ": [");
      printAttr(lSelectedAttrArr, prDDPins);
      prDDPins.println("      ]");
    }

    // Write DataTypes
    ArrayList<DOMDataType> lSelectedDataTypeArr = new ArrayList<>();
    for (Iterator<DOMDataType> j = DOMInfoModel.masterDOMDataTypeArr.iterator(); j.hasNext();) {
      DOMDataType lSelectedDataType = j.next();
      if (lSelectedDataType.isInactive) {
        continue;
      }
      if (isAllFlag || lNameSpaceIdNC.compareTo(lSelectedDataType.nameSpaceIdNC) == 0) {
        lSelectedDataTypeArr.add(lSelectedDataType);
      }
    }
    if (lSelectedDataTypeArr.size() > 0) {
      prDDPins.println("    , " + formValue("dataTypeDictionary") + ": [");
      printDataType(lSelectedDataTypeArr, prDDPins);
      prDDPins.println("      ]");
    }

    // Write Units
    ArrayList<DOMUnit> lSelectedUnitArr = new ArrayList<>();
    for (Iterator<DOMUnit> j = DOMInfoModel.masterDOMUnitArr.iterator(); j.hasNext();) {
      DOMUnit lSelectedUnit = j.next();
      if (lSelectedUnit.isInactive) {
        continue;
      }
      if (isAllFlag || lNameSpaceIdNC.compareTo(lSelectedUnit.nameSpaceIdNC) == 0) {
        lSelectedUnitArr.add(lSelectedUnit);
      }
    }
    if (lSelectedUnitArr.size() > 0) {
      prDDPins.println("    , " + formValue("unitDictionary") + ": [");
      printUnits(lSelectedUnitArr, prDDPins);
      prDDPins.println("      ]");
    }

    // Write Property Maps
    ArrayList<PropertyMapsDefn> lSelectedPropMapArr = new ArrayList<>();
    for (Iterator<PropertyMapsDefn> j = DOMInfoModel.masterPropertyMapsArr.iterator(); j
        .hasNext();) {
      PropertyMapsDefn lSelectedPropMap = j.next();
      if (isAllFlag || lNameSpaceIdNC.compareTo(lSelectedPropMap.namespace_id) == 0) {
        lSelectedPropMapArr.add(lSelectedPropMap);
      }
    }
    if (lSelectedPropMapArr.size() > 0) {
      prDDPins.println("    , " + formValue("PropertyMapDictionary") + ": [");
      printPropertyMaps(lSelectedPropMapArr, prDDPins);
      prDDPins.println("      ]");
    }

    // Write Terminological Entry - Query Model
    ArrayList<DOMClass> lSelectedTEClassArr = new ArrayList<>();
    for (Iterator<DOMClass> j = DOMInfoModel.masterDOMClassArr.iterator(); j.hasNext();) {
      DOMClass lDOMClass = j.next();
      if (lDOMClass.isInactive || !lDOMClass.isQueryModel) {
        continue;
      }
      lSelectedTEClassArr.add(lDOMClass);
    }

    if (lSelectedTEClassArr.size() > 0) {
      getTermEntriesQM(lSelectedTEClassArr, prDDPins);
    }
  }

  // Print the classes
  public void printClass(ArrayList<DOMClass> lSelectedClassArr, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    for (Iterator<DOMClass> i = lSelectedClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.title.indexOf("PDS3") > -1) {
        continue;
      }
      prDDPins.println("      " + delimiter1 + "{");
      delimiter1 = ", ";
      prDDPins.println("          " + formValue("class") + ": {");
      prDDPins.println(
          "            " + formValue("identifier") + ": " + formValue(lClass.identifier) + " ,");
      prDDPins.println("            " + formValue("title") + ": " + formValue(lClass.title) + " ,");
      prDDPins.println("            " + formValue("registrationAuthorityId") + ": "
          + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
      prDDPins.println("            " + formValue("nameSpaceId") + ": "
          + formValue(lClass.nameSpaceIdNC) + " ,");
      prDDPins
          .println("            " + formValue("steward") + ": " + formValue(lClass.steward) + " ,");
      prDDPins.println(
          "            " + formValue("versionId") + ": " + formValue(lClass.versionId) + " ,");
      prDDPins.println("            " + formValue("isAbstract") + ": "
          + formBooleanValue(lClass.isAbstract) + " ,");
      prDDPins.println("            " + formValue("isDeprecated") + ": "
          + formBooleanValue(lClass.isDeprecated) + " ,");
      prDDPins
          .println("            " + formValue("description") + ": " + formValue(lClass.definition));
      printAssoc(lClass, prDDPins);
      prDDPins.println("          }");
      prDDPins.println("        }");
    }
  }

  // Print the Associations
  public void printAssoc(DOMClass lClass, PrintWriter prDDPins) {
    if (lClass.allAttrAssocArr.isEmpty()) {
      return;
    }

    // sort by classOrder
    TreeMap<String, DOMPropGroup> lDOMPropGroupMap = new TreeMap<>();
    DOMPropGroup lDOMPropGroup = new DOMPropGroup();
    String pGroupName = "NULL";
    for (Iterator<DOMProp> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      ISOClassOAIS11179 lDOMObject = lDOMProp.hasDOMObject;
      if (lDOMObject != null) {
        String lPropType = "C";
        if (lDOMProp.isAttribute) {
          lPropType = "A";
        }
        String lSortOrder = lPropType + lDOMProp.classOrder + "-" + lDOMObject.identifier;
        if ((lDOMProp.groupName.indexOf("TBD") == 0)
            || (lDOMProp.groupName.compareTo(pGroupName) != 0)) {
          if (lDOMProp.groupName.indexOf("TBD") == 0) {
            pGroupName = "NULL";
          } else {
            pGroupName = lDOMProp.groupName;
          }
          lDOMPropGroup = new DOMPropGroup();
          lDOMPropGroup.domProp = lDOMProp;
          lDOMPropGroup.domObjectArr.add(lDOMObject);
          lDOMPropGroupMap.put(lSortOrder, lDOMPropGroup);
        } else {
          lDOMPropGroup.domObjectArr.add(lDOMObject);
        }
      } else {
        Utility.registerMessage(
            "1>error " + "WriteDOMDDJSONFile - Failed to find DOMObject - lDOMProp.identifier: "
                + lDOMProp.identifier);
      }
    }
    ArrayList<DOMPropGroup> lDOMPropGroupArr = new ArrayList<>(lDOMPropGroupMap.values());
    prDDPins.println("              , " + formValue("associationList") + ": [");
    printSuperClassAssoc(lClass, prDDPins);
    String delimiter1 = "  ";
    for (Iterator<DOMPropGroup> i = lDOMPropGroupArr.iterator(); i.hasNext();) {
      DOMPropGroup lDOMPropGroup2 = i.next();
      DOMProp lDOMProp = lDOMPropGroup2.domProp;
      if (lDOMProp.isAttribute) {
        prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");
        delimiter1 = ", ";
        prDDPins.println("                " + formValue("identifier") + ": "
            + formValue(lDOMProp.identifier) + " ,");
        prDDPins.println(
            "                " + formValue("title") + ": " + formValue(lDOMProp.title) + " ,");
        prDDPins.println(
            "                " + formValue("assocType") + ": " + formValue("attribute_of") + " ,");
        prDDPins.println(
            "                " + formValue("isAttribute") + ": " + formBooleanValue(true) + " ,");
        prDDPins.println("                " + formValue("isChoice") + ": "
            + formBooleanValue(lDOMProp.isChoice) + " ,");
        prDDPins.println("                " + formValue("isAny") + ": "
            + formBooleanValue(lDOMProp.isAny) + " ,");
        prDDPins.println("                " + formValue("groupName") + ": "
            + formValue(lDOMProp.groupName) + " ,");
        prDDPins.println("                " + formValue("minimumCardinality") + ": "
            + formValue(lDOMProp.cardMin) + " ,");
        prDDPins.println("                " + formValue("maximumCardinality") + ": "
            + formValue(lDOMProp.cardMax) + " ,");
        prDDPins.println("                " + formValue("classOrder") + ": "
            + formValue(lDOMProp.classOrder) + " ,");
        prDDPins.println("                " + formValue("attributeId") + ": [");
        boolean isNewLine = false;
        for (Iterator<ISOClassOAIS11179> j = lDOMPropGroup2.domObjectArr.iterator(); j.hasNext();) {
          ISOClassOAIS11179 lISOClass = j.next();
          if (lISOClass instanceof DOMAttr) {
            DOMAttr lDOMAttr = (DOMAttr) lISOClass;
            if (isNewLine) {
              prDDPins.println(",");
            }
            prDDPins.print("                  " + formValue(lDOMAttr.identifier));
            isNewLine = true;
          }
        }
        prDDPins.println("");
        prDDPins.println("                 ]");
        prDDPins.println("              }");
        prDDPins.println("            }");
      } else {
        prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");
        delimiter1 = ", ";
        prDDPins.println("                " + formValue("identifier") + ": "
            + formValue(lDOMProp.identifier) + " ,");
        prDDPins.println(
            "                " + formValue("title") + ": " + formValue(lDOMProp.title) + " ,");
        prDDPins.println(
            "                " + formValue("assocType") + ": " + formValue("component_of") + " ,");
        prDDPins.println(
            "                " + formValue("isAttribute") + ": " + formBooleanValue(false) + " ,");
        prDDPins.println("                " + formValue("isChoice") + ": "
            + formBooleanValue(lDOMProp.isChoice) + " ,");
        prDDPins.println("                " + formValue("isAny") + ": "
            + formBooleanValue(lDOMProp.isAny) + " ,");
        prDDPins.println("                " + formValue("groupName") + ": "
            + formValue(lDOMProp.groupName) + " ,");
        prDDPins.println("                " + formValue("minimumCardinality") + ": "
            + formValue(lDOMProp.cardMin) + " ,");
        prDDPins.println("                " + formValue("maximumCardinality") + ": "
            + formValue(lDOMProp.cardMax) + " ,");
        prDDPins.println("                " + formValue("classOrder") + ": "
            + formValue(lDOMProp.classOrder) + " ,");
        prDDPins.println("                " + formValue("classId") + ": [");
        boolean isNewLine = false;
        for (Iterator<ISOClassOAIS11179> j = lDOMPropGroup2.domObjectArr.iterator(); j.hasNext();) {
          ISOClassOAIS11179 lISOClass = j.next();
          if (lISOClass instanceof DOMClass) {
            DOMClass lDOMAttr = (DOMClass) lISOClass;
            if (isNewLine) {
              prDDPins.println(",");
            }
            prDDPins.print("                  " + formValue(lDOMAttr.identifier));
            isNewLine = true;
          }
        }
        prDDPins.println("");
        prDDPins.println("                 ]");
        prDDPins.println("              }");
        prDDPins.println("            }");
      }
    }
    prDDPins.println("           ]");
  }

  // Print the Associations
  public void printSuperClassAssoc(DOMClass lClass, PrintWriter prDDPins) {
    if (lClass.subClassOf == null) {
      return;
    }
    DOMClass lSuperClass = lClass.subClassOf;
    if ((lSuperClass == null) || lSuperClass.isAbstract || lSuperClass.isVacuous
        || (lSuperClass.title.compareTo("USER") == 0)) {
      return;
    }
    String delimiter1 = "  ";
    prDDPins.println("           " + delimiter1 + "{" + formValue("association") + ": {");
    prDDPins.println("                " + formValue("identifier") + ": "
        + formValue(lClass.identifier + "." + lSuperClass.title + ".generalization") + " ,");
    prDDPins.println(
        "                " + formValue("title") + ": " + formValue(lSuperClass.title) + " ,");
    prDDPins.println(
        "                " + formValue("assocType") + ": " + formValue("parent_of") + " ,");
    prDDPins.println("                " + formValue("isAttribute") + ": " + "\"false\"" + " ,");
    prDDPins.println("                " + formValue("isChoice") + ": " + "\"false\"" + " ,");
    prDDPins.println("                " + formValue("isAny") + ": " + "\"false\"" + " ,");
    prDDPins.println("                " + formValue("groupName") + ": " + "\"null\"" + " ,");
    prDDPins.println("                " + formValue("minimumCardinality") + ": " + "\"1\"" + " ,");
    prDDPins.println("                " + formValue("maximumCardinality") + ": " + "\"1\"" + " ,");
    prDDPins.println("                " + formValue("classOrder") + ": " + "\"0000\"" + " ,");
    prDDPins.println("                " + formValue("attributeId") + ": [");
    prDDPins.print("");
    prDDPins.print("                  " + formValue(lSuperClass.identifier));
    prDDPins.println("");
    prDDPins.println("                 ]");
    prDDPins.println("              }");
    prDDPins.println("            } ,"); // other associations must always follow

  }

  // Print the the Protege Pins DE
  public void printAttr(ArrayList<DOMAttr> lSelectedAttrArr, PrintWriter prDDPins) {
    // print the data elements
    String delimiter1 = "  ";
    for (Iterator<DOMAttr> i = lSelectedAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (!(lAttr.isUsedInClass && lAttr.isAttribute)) {
        continue;
      }
      prDDPins.println("      " + delimiter1 + "{");
      delimiter1 = ", ";
      prDDPins.println("          " + formValue("attribute") + ": {");
      prDDPins.println(
          "            " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");
      prDDPins.println("            " + formValue("title") + ": " + formValue(lAttr.title) + " ,");
      prDDPins.println("            " + formValue("registrationAuthorityId") + ": "
          + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
      prDDPins.println(
          "            " + formValue("nameSpaceId") + ": " + formValue(lAttr.nameSpaceIdNC) + " ,");
      prDDPins
          .println("            " + formValue("steward") + ": " + formValue(lAttr.steward) + " ,");
      prDDPins.println("            " + formValue("versionId") + ": "
          + formValue(lAttr.versionIdentifierValue) + " ,");
      prDDPins.println(
          "            " + formValue("description") + ": " + formValue(lAttr.definition) + " ,");
      prDDPins.println("            " + formValue("isNillable") + ": "
          + formBooleanValue(lAttr.isNilable) + " ,");
      prDDPins.println("            " + formValue("isEnumerated") + ": "
          + formBooleanValue(lAttr.isEnumerated) + " ,");
      prDDPins.println("            " + formValue("isDeprecated") + ": "
          + formBooleanValue(lAttr.isDeprecated) + " ,");
      prDDPins.println(
          "            " + formValue("dataType") + ": " + formValue(lAttr.valueType) + " ,");
      prDDPins.println("            " + formValue("dataTypeId") + ": "
          + formValue(lAttr.getValueTypeIdentifier()) + " ,");
      prDDPins.println("            " + formValue("minimumCharacters") + ": "
          + formValue(lAttr.getMinimumCharacters(true, true)) + " ,");
      prDDPins.println("            " + formValue("maximumCharacters") + ": "
          + formValue(lAttr.getMaximumCharacters(true, true)) + " ,");
      prDDPins.println("            " + formValue("minimumValue") + ": "
          + formValue(lAttr.getMinimumValue(true, true)) + " ,");
      prDDPins.println("            " + formValue("maximumValue") + ": "
          + formValue(lAttr.getMaximumValue(true, true)) + " ,");
      prDDPins.println(
          "            " + formValue("pattern") + ": " + formValue(lAttr.getPattern(true)) + " ,");
      prDDPins.println("            " + formValue("unitOfMeasure") + ": "
          + formValue(lAttr.getUnitOfMeasure(false)) + " ,");
      prDDPins.println("            " + formValue("unitOfMeasureId") + ": "
          + formValue(lAttr.getUnitOfMeasureIdentifier()) + " ,");
      prDDPins.println(
          "            " + formValue("unitId") + ": " + formValue(lAttr.getUnits(false)) + " ,");
      prDDPins.println("            " + formValue("defaultUnitId") + ": "
          + formValue(lAttr.getDefaultUnitId(false)));
      printPermValues(lAttr, prDDPins);
      prDDPins.println("          }");
      prDDPins.println("        }");
    }
  }

  // Print the Permissible Values and Value Meanings
  public void printPermValues(DOMAttr lAttr, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    if (lAttr.domPermValueArr.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("PermissibleValueList") + ": [");
    for (Iterator<DOMProp> i = lAttr.domPermValueArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if ((lDOMProp.hasDOMObject == null) || !(lDOMProp.hasDOMObject instanceof DOMPermValDefn)) {
        continue;
      }
      DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
      prDDPins.println("            " + delimiter1 + "{" + formValue("PermissibleValue") + ": {");
      delimiter1 = ", ";
      prDDPins.println("                  " + formValue("value") + ": "
          + formValue(lDOMPermValDefn.value) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": "
          + formValue(lDOMPermValDefn.value_meaning) + " ,");
      prDDPins.println("                  " + formValue("isDeprecated") + ": "
          + formBooleanValue(lDOMPermValDefn.isDeprecated));
      prDDPins.println("                }");
      prDDPins.println("             }");
    }
    prDDPins.println("           ]");
  }

  // Print the Data Types
  public void printDataType(ArrayList<DOMDataType> lSelectedDatatypeArr, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    for (Iterator<DOMDataType> i = lSelectedDatatypeArr.iterator(); i.hasNext();) {
      DOMDataType lDataType = i.next();
      prDDPins.println("      " + delimiter1 + "{" + formValue("DataType") + ": {");
      delimiter1 = ", ";
      prDDPins.println(
          "            " + formValue("identifier") + ": " + formValue(lDataType.identifier) + " ,");
      // prDDPins.println(" " + formValue("identifier") + ": " + formValue(lDataType.pds4Identifier)
      // + " ,");
      prDDPins
          .println("            " + formValue("title") + ": " + formValue(lDataType.title) + " ,");
      prDDPins.println("            " + formValue("nameSpaceId") + ": "
          + formValue(lDataType.nameSpaceIdNC) + " ,");
      prDDPins.println("            " + formValue("registrationAuthorityId") + ": "
          + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
      prDDPins.println("            " + formValue("minimumCharacters") + ": "
          + formValue(lDataType.getMinimumCharacters(true)) + " ,");
      prDDPins.println("            " + formValue("maximumCharacters") + ": "
          + formValue(lDataType.getMaximumCharacters(true)) + " ,");
      prDDPins.println("            " + formValue("minimumValue") + ": "
          + formValue(lDataType.getMinimumValue(true)) + " ,");
      prDDPins.println("            " + formValue("maximumValue") + ": "
          + formValue(lDataType.getMaximumValue(true)));
      printPattern(lDataType, prDDPins);
      prDDPins.println("            }");
      prDDPins.println("         }");
    }
  }

  // Print the data type Pattern
  public void printPattern(DOMDataType lDataType, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    if (lDataType.pattern.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("patternList") + ": [");
    for (Iterator<String> i = lDataType.pattern.iterator(); i.hasNext();) {
      String lValue = i.next();
      prDDPins.println("            " + delimiter1 + "{" + formValue("Pattern") + ": {");
      delimiter1 = ", ";
      // prDDPins.println(" , {" + formValue("Pattern") + ": {");
      prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));
      prDDPins.println("              }");
      prDDPins.println("            }");
    }
    prDDPins.println("           ]");
  }

  // Print the Units
  public void printUnits(ArrayList<DOMUnit> lSelectedUnitArr, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    for (Iterator<DOMUnit> i = lSelectedUnitArr.iterator(); i.hasNext();) {
      DOMUnit lUnit = i.next();
      prDDPins.println("      " + delimiter1 + "{" + formValue("Unit") + ": {");
      delimiter1 = ", ";
      // prDDPins.println(" " + formValue("identifier") + ": " + formValue(lUnit.identifier) + "
      // ,");
      prDDPins.println(
          "            " + formValue("identifier") + ": " + formValue(lUnit.pds4Identifier) + " ,");
      prDDPins.println("            " + formValue("title") + ": " + formValue(lUnit.title) + " ,");
      prDDPins.println("            " + formValue("nameSpaceId") + ": "
          + formValue(DMDocument.masterNameSpaceIdNCLC) + " ,");
      prDDPins.println("            " + formValue("registrationAuthorityId") + ": "
          + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
      prDDPins.println(
          "            " + formValue("defaultUnitId") + ": " + formValue(lUnit.default_unit_id));
      printUnitId(lUnit, prDDPins);
      prDDPins.println("          }");
      prDDPins.println("        }");
    }
  }

  // Print the Unit Identifier
  public void printUnitId(DOMUnit lUnit, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    if (lUnit.unit_id.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("unitIdList") + ": [");
    for (Iterator<String> i = lUnit.unit_id.iterator(); i.hasNext();) {
      String lValue = i.next();
      prDDPins.println("            " + delimiter1 + "{" + formValue("UnitId") + ": {");
      delimiter1 = ", ";
      // prDDPins.println(" , {" + formValue("UnitId") + ": {");
      prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));
      prDDPins.println("                }");
      prDDPins.println("             }");
    }
    prDDPins.println("           ]");
  }

  // Print the Property Maps
  public void printPropertyMaps(ArrayList<PropertyMapsDefn> lSelectedPropMapsArr,
      PrintWriter prDDPins) {
    String delimiter1 = "  ";
    for (Iterator<PropertyMapsDefn> i = lSelectedPropMapsArr.iterator(); i.hasNext();) {
      PropertyMapsDefn lPropertyMaps = i.next();
      prDDPins.println("      " + delimiter1 + "{" + formValue("PropertyMaps") + ": {");
      delimiter1 = ", ";
      prDDPins.println("            " + formValue("identifier") + ": "
          + formValue(lPropertyMaps.rdfIdentifier) + " ,");
      prDDPins.println(
          "            " + formValue("title") + ": " + formValue(lPropertyMaps.title) + " ,");
      // prDDPins.println(" " + formValue("steward_id") + ": " + formValue(lPropertyMaps.steward_id)
      // + " ,");
      prDDPins.println("            " + formValue("namespace_id") + ": "
          + formValue(lPropertyMaps.namespace_id) + " ,");
      prDDPins.println("            " + formValue("description") + ": "
          + formValue(lPropertyMaps.description) + " ,");
      prDDPins.println("            " + formValue("external_property_map_id") + ": "
          + formValue(lPropertyMaps.external_property_map_id));
      printPropertyMap(lPropertyMaps, prDDPins);
      prDDPins.println("          }");
      prDDPins.println("        }");
    }
  }

  // Print the Property Map
  public void printPropertyMap(PropertyMapsDefn lPropertyMaps, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    if (lPropertyMaps.propertyMapArr.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("propertyMapList") + ": [");
    for (Iterator<PropertyMapDefn> i = lPropertyMaps.propertyMapArr.iterator(); i.hasNext();) {
      PropertyMapDefn lPropertyMap = i.next();
      prDDPins.println("            " + delimiter1 + "{" + formValue("PropertyMap") + ": {");
      delimiter1 = ", ";
      prDDPins.println("                  " + formValue("identifier") + ": "
          + formValue(lPropertyMap.identifier) + " ,");
      prDDPins.println(
          "                  " + formValue("title") + ": " + formValue(lPropertyMap.title) + " ,");
      prDDPins.println("                  " + formValue("model_object_id") + ": "
          + formValue(lPropertyMap.model_object_id) + " ,");
      prDDPins.println("                  " + formValue("model_object_type") + ": "
          + formValue(lPropertyMap.model_object_type) + " ,");
      prDDPins.println("                  " + formValue("instance_id") + ": "
          + formValue(lPropertyMap.instance_id) + " ,");
      prDDPins.println("                  " + formValue("external_namespace_id") + ": "
          + formValue(lPropertyMap.external_namespace_id) + " ,");
      prDDPins.println("                  " + formValue("description") + ": "
          + formValue(lPropertyMap.description));
      printPropertyMapEntrys(lPropertyMap, prDDPins);
      prDDPins.println("                 }");
      prDDPins.println("              }");
    }
    prDDPins.println("            ]");
  }

  // Print the Property Map Entrys
  public void printPropertyMapEntrys(PropertyMapDefn lPropertyMap, PrintWriter prDDPins) {
    String delimiter1 = "  ";
    if (lPropertyMap.propertyMapEntryArr.isEmpty()) {
      return;
    }
    prDDPins.println("                , " + formValue("propertyMapEntryList") + ": [");
    for (Iterator<PropertyMapEntryDefn> i = lPropertyMap.propertyMapEntryArr.iterator(); i
        .hasNext();) {
      PropertyMapEntryDefn lPropertyMapEntry = i.next();
      prDDPins
          .println("                  " + delimiter1 + "{" + formValue("PropertyMapEntry") + ": {");
      delimiter1 = ", ";
      prDDPins.println("                        " + formValue("property_name") + ": "
          + formValue(lPropertyMapEntry.property_name) + " ,");
      prDDPins.println("                        " + formValue("property_value") + ": "
          + formValue(lPropertyMapEntry.property_value) + " }");
      prDDPins.println("                    }");
    }
    prDDPins.println("                    ]");
  }

  // Print the Terminological Entries - Query Model
  public void getTermEntriesQM(ArrayList<DOMClass> lSelectedDOMClassArr, PrintWriter prDDPins) {

    // start the TE list
    prDDPins.println("    , " + formValue("TerminologicalEntries") + ": [");

    // scan select classes - QM classes
    boolean hasNextQMClass = true;
    for (Iterator<DOMClass> i = lSelectedDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      if (!i.hasNext()) {
        hasNextQMClass = false;
      }

      // get all QM attributes
      boolean hasNextQMAttr = true;
      for (Iterator<DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (!j.hasNext()) {
          hasNextQMAttr = false;
        }
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttrQM = (DOMAttr) lDOMProp.hasDOMObject;

          // get all QM PV for this QM attribute
          boolean hasNextQMPV = true;
          for (Iterator<DOMProp> m = lDOMAttrQM.domPermValueArr.iterator(); m.hasNext();) {
            DOMProp lDOMPropPV = m.next();
            if (!m.hasNext()) {
              hasNextQMPV = false;
            }
            if (lDOMPropPV.hasDOMObject != null
                && lDOMPropPV.hasDOMObject instanceof DOMPermValDefn) {
              DOMPermValDefn lPVQM = (DOMPermValDefn) lDOMPropPV.hasDOMObject;

              // for one PV print all QM term entrires
              ArrayList<TermEntryDefn> lPVQMTermEntryDefnArr =
                  new ArrayList<>(lPVQM.termEntryMap.values());
              printTermEntryDefnQM(lDOMClass.title, lDOMAttrQM.title, lDOMClass.extrnTitleQM,
                  lDOMAttrQM.extrnTitleQM, lPVQM.value, lPVQMTermEntryDefnArr,
                  hasNextQMClass || hasNextQMAttr || hasNextQMPV, prDDPins);
            }
          } // PV
        }
      } // Attr
    }
    // end TE list
    prDDPins.println("      ]");
  }

  // Print the Terminological Entries
  public void printTermEntryDefnQM(String lDOMClassQMtitle, String lDOMAttrQMtitle,
      String lDOMClassExtrnTitle, String lDOMAttrExtrnTitle, String lPVQMValue,
      ArrayList<TermEntryDefn> lTermEntryDefnArr, boolean hasNext, PrintWriter prDDPins) {
    String delOuter = "";
    if (hasNext) {
      delOuter = ",";
    }
    prDDPins.println("          {" + formValue(lDOMClassQMtitle) + ":");
    prDDPins.println("             {" + formValue(lDOMAttrQMtitle) + ":");
    prDDPins.println("                   [");
    prDDPins.println("                      {" + formValue("class") + ":"
        + formValue(lDOMClassExtrnTitle) + ",");
    prDDPins.println("                       " + formValue("attribute") + ":"
        + formValue(lDOMAttrExtrnTitle) + ",");
    prDDPins.println(
        "                       " + formValue("value") + ":" + formValue(lPVQMValue) + "},");
    printTermEntryDefnQMTermEntryList(lDOMClassQMtitle, lDOMAttrQMtitle, lTermEntryDefnArr,
        prDDPins);
    prDDPins.println("                   ]");
    prDDPins.println("             }");
    prDDPins.println("          }" + delOuter);
  }

  // Print the Query Model Term Entry List
  public void printTermEntryDefnQMTermEntryList(String lDOMClassQMtitle, String lDOMAttrQMtitle,
      ArrayList<TermEntryDefn> lTermEntryDefnArr, PrintWriter prDDPins) {
    prDDPins.println("                      {" + formValue("TermEntryList") + ":");
    prDDPins.println("                         [");
    String delInner = ", ";
    for (Iterator<TermEntryDefn> i = lTermEntryDefnArr.iterator(); i.hasNext();) {
      TermEntryDefn lTermEntryDefn = i.next();
      if (!i.hasNext()) {
        delInner = "";
      }
      prDDPins.println("                            {" + formValue("name") + ":"
          + formValue(lTermEntryDefn.name) + ", ");
      prDDPins.println("                            " + formValue("semanticRelation") + ":"
          + formValue(lTermEntryDefn.semanticRelation) + "}" + delInner);
    }
    prDDPins.println("                         ]");
    prDDPins.println("                      }");
  }

  public void printTermEntriesList(DOMClass lSelectedDOMClass, String delimiter1,
      PrintWriter prDDPins) {
    prDDPins.println("          [");
    int count = 1;
    String delimiter2 = ",";
    Set<String> set9 = lSelectedDOMClass.termEntryMap.keySet();
    Iterator<String> iter9 = set9.iterator();
    while (iter9.hasNext()) {
      String lTitle = iter9.next();
      TermEntryDefn lTermEntryDefn = lSelectedDOMClass.termEntryMap.get(lTitle);
      if (!iter9.hasNext()) {
        delimiter2 = "";
      }
      String lObjectName = "List-" + Integer.toString(count);
      printTermEntryObject(lTermEntryDefn, delimiter2, prDDPins);
      count++;
    }
    prDDPins.println("          ]" + delimiter1);
  }

  public void printTermEntryObject(TermEntryDefn lTermEntryDefn, String delimiter2,
      PrintWriter prDDPins) {
    prDDPins.println("          {");
    prDDPins
        .println("            " + formValue("name") + ": " + formValue(lTermEntryDefn.name) + " ,");
    prDDPins.println("            " + formValue("semanticRelation") + ": "
        + formValue(lTermEntryDefn.semanticRelation) + " ,");
    String lIsPreferred = Boolean.toString(lTermEntryDefn.isPreferred);
    prDDPins
        .println("            " + formValue("isPreferred") + ": " + formValue(lIsPreferred) + " ,");
    prDDPins.println(
        "            " + formValue("definition") + ": " + formValue(lTermEntryDefn.definition));
    prDDPins.println("          }" + delimiter2);
  }

  // ===============================================================================================================

  // Print the the Protege Pins Properties
  public void printPDDPPR(PrintWriter prDDPins) {
    ArrayList<DOMProp> lSortedAssocArr = new ArrayList<>(DOMInfoModel.masterDOMPropIdMap.values());
    for (Iterator<DOMProp> i = lSortedAssocArr.iterator(); i.hasNext();) {
      DOMProp lAssoc = i.next();
      if (lAssoc.isInactive) {
        continue;
      }
      String prDataIdentifier = "PR." + lAssoc.identifier;
      prDDPins.println("([" + prDataIdentifier + "] of Property");
      prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
      prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
      prDDPins.println("  (registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println("  (registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");
      prDDPins.println("  (classOrder \"" + lAssoc.classOrder + "\")");
      prDDPins.println("  (versionIdentifier \""
          + DMDocument.masterPDSSchemaFileDefn.identifier_version_id + "\"))");
    }
  }

  // Print the the Protege Pins CD
  public void printPDDPCD(PrintWriter prDDPins) {
    ArrayList<DOMIndexDefn> lCDAttrArr = new ArrayList<>(DOMInfoModel.cdDOMAttrMap.values());
    for (Iterator<DOMIndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
      DOMIndexDefn lIndex = i.next();
      String gIdentifier = lIndex.identifier;
      String dbIdentifier = "CD_" + gIdentifier;
      prDDPins.println("([" + dbIdentifier + "] of ConceptualDomain");
      prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
      prDDPins.println("	(dataIdentifier \"" + dbIdentifier + "\")");

      prDDPins.println("  (having ");
      boolean isNewLine = false;
      for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lDEC = j.next();
        if (isNewLine) {
          prDDPins.println("");
        }
        prDDPins.print("    [" + "DEC_" + lDEC + "]");
        isNewLine = true;
      }
      prDDPins.println(")");
      prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println(" 	(registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");

      prDDPins.println("  (representing2 ");
      isNewLine = false;
      for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        if (isNewLine) {
          prDDPins.println("");
        }
        if (lAttr.isEnumerated) {
          prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
        } else {
          prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
        }
        isNewLine = true;
      }
      prDDPins.println(")");
      prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
      prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
      prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
      prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");

      // write terminological entry
      String teIdentifier = "TE." + gIdentifier;
      String defIdentifier = "DEF." + gIdentifier;
      String desIdentifier = "DES." + gIdentifier;
      prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
      prDDPins.println(" (administeredItemContext [NASA_PDS])");
      prDDPins.println(" (definition [" + defIdentifier + "])");
      prDDPins.println(" (designation [" + desIdentifier + "])");
      prDDPins.println(" (sectionLanguage [LI_English]))");
      prDDPins.println("([" + defIdentifier + "] of Definition");
      prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
      prDDPins.println(" (isPreferred \"TRUE\"))");
      prDDPins.println("([" + desIdentifier + "] of Designation");
      prDDPins.println(" (designationName \"" + gIdentifier + "\")");
      prDDPins.println(" (isPreferred \"TRUE\"))");
    }
  }

  // Print the the Protege Pins DEC
  public void printPDDPDEC(PrintWriter prDDPins) {
    ArrayList<DOMIndexDefn> lDECAttrArr = new ArrayList<>(DOMInfoModel.decDOMAttrMap.values());
    for (Iterator<DOMIndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
      DOMIndexDefn lIndex = i.next();
      String gIdentifier = lIndex.identifier;
      String dbIdentifier = "DEC_" + gIdentifier;
      prDDPins.println("([" + dbIdentifier + "] of DataElementConcept");
      prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
      prDDPins.println("	(dataIdentifier \"" + dbIdentifier + "\")");

      boolean isNewLine = false;
      prDDPins.println("  (expressing ");
      for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        if (isNewLine) {
          prDDPins.println("");
        }
        prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
        isNewLine = true;
      }
      prDDPins.println(")");
      prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println(" 	(registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");
      prDDPins.println("  (specifying ");
      isNewLine = false;
      for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lCD = j.next();
        if (isNewLine) {
          prDDPins.println("");
        }
        prDDPins.print("    [" + "CD_" + lCD + "]");
        isNewLine = true;
      }
      prDDPins.println(")");
      prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
      prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
      prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
      prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");

      // write the terminological entry

      String teIdentifier = "TE." + gIdentifier;
      String defIdentifier = "DEF." + gIdentifier;
      String desIdentifier = "DES." + gIdentifier;
      prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
      prDDPins.println(" (administeredItemContext [NASA_PDS])");
      prDDPins.println(" (definition [" + defIdentifier + "])");
      prDDPins.println(" (designation [" + desIdentifier + "])");
      prDDPins.println(" (sectionLanguage [LI_English]))");
      prDDPins.println("([" + defIdentifier + "] of Definition");
      prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
      prDDPins.println(" (isPreferred \"TRUE\"))");
      prDDPins.println("([" + desIdentifier + "] of Designation");
      prDDPins.println(" (designationName \"" + gIdentifier + "\")");
      prDDPins.println(" (isPreferred \"TRUE\"))");
    }
  }

  // Print the the Protege Pins TE
  public void printPDDPTE(PrintWriter prDDPins) {
    // print the Terminological Entry
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (lAttr.isInactive) {
        continue;
      }
      if (lAttr.isUsedInClass && lAttr.isAttribute) {
        // print TE section
        prDDPins.println("([" + lAttr.teDataIdentifier + "] of TerminologicalEntry");
        prDDPins.println("  (administeredItemContext [NASA_PDS])");
        prDDPins.println("  (definition [" + lAttr.defDataIdentifier + "])");
        prDDPins.println("  (designation [" + lAttr.desDataIdentifier + "])");
        prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");

        // print definition section
        prDDPins.println("([" + lAttr.defDataIdentifier + "] of Definition");
        prDDPins.println(
            "  (definitionText \"" + DOMInfoModel.escapeProtegeString(lAttr.definition) + "\")");
        prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");

        // print designation section
        prDDPins.println("([" + lAttr.desDataIdentifier + "] of Designation");
        prDDPins.println("  (designationName \"" + lAttr.title + "\")");
        prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
      }
    }
  }

  // Print the the Protege Pins Misc
  public void printPDDPMISC(PrintWriter prDDPins) {
    // print the Miscellaneous records
    prDDPins.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
    prDDPins.println(
        "	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
    prDDPins.println("	(administrativeStatus Final)");
    prDDPins.println("	(changeDescription \"PSDD content has been merged into the model.\")");
    prDDPins.println("	(creationDate \"2010-03-10\")");
    prDDPins.println("	(effectiveDate \"2010-03-10\")");
    prDDPins.println(
        "	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
    prDDPins.println("	(lastChangeDate \"2010-03-10\")");
    prDDPins.println("	(origin \"Planetary Data System\")");
    prDDPins.println("	(registrationStatus Preferred)");
    prDDPins.println("	(unresolvedIssue \"Issues still being determined.\")");
    prDDPins.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");

    prDDPins.println("([" + DMDocument.registrationAuthorityIdentifierValue
        + "] of RegistrationAuthorityIdentifier");
    prDDPins.println("	(internationalCodeDesignator \"0001\")");
    prDDPins.println("	(opiSource \"1\")");
    prDDPins
        .println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
    prDDPins.println("	(organizationPartIdentifier \"Planetary Data System\"))");

    prDDPins.println("([" + DMDocument.registeredByValue + "] of RegistrationAuthority");
    prDDPins.println("	(documentationLanguageIdentifier [LI_English])");
    prDDPins.println("	(languageUsed [LI_English])");
    prDDPins.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
    prDDPins.println("	(organizationName \"NASA Planetary Data System\")");
    prDDPins.println("	(registrar [PDS_Registrar])");
    prDDPins.println("	(registrationAuthorityIdentifier_v ["
        + DMDocument.registrationAuthorityIdentifierValue + "]))");

    prDDPins.println("([NASA_PDS] of Context");
    prDDPins.println("	(dataIdentifier  \"NASA_PDS\"))");

    prDDPins.println("([PDS_Registrar] of  Registrar");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(registrarIdentifier \"PDS_Registrar\"))");

    prDDPins.println("([Steward_PDS] of Steward");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([pds] of Steward");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([img] of Steward");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([rings] of Steward");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([ops] of Steward");
    prDDPins.println("	(contact [PDS_Standards_Coordinator])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([Submitter_PDS] of Submitter");
    prDDPins.println("	(contact [DataDesignWorkingGroup])");
    prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

    prDDPins.println("([PDS_Standards_Coordinator] of Contact");
    prDDPins.println("	(contactTitle \"PDS_Standards_Coordinator\")");
    prDDPins.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
    prDDPins.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
    prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
    prDDPins.println("	(contactPhone \"818.354.6135\")");
    prDDPins.println("	(contactName \"Elizabeth Rye\"))");

    prDDPins.println("([DataDesignWorkingGroup] of Contact");
    prDDPins.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
    prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
    prDDPins.println("	(contactPhone \"818.354.9338\")");
    prDDPins.println("	(contactName \"J. Steven Hughes\"))");

    prDDPins.println("([LI_English] of LanguageIdentification");
    prDDPins.println("  (countryIdentifier \"USA\")");
    prDDPins.println("  (languageIdentifier \"English\"))");

    // write the unitOfMeasure
    for (Iterator<DOMUnit> i = DOMInfoModel.masterDOMUnitArr.iterator(); i.hasNext();) {
      DOMUnit lUnit = i.next();
      if (lUnit.isInactive) {
        continue;
      }
      prDDPins.println("([" + lUnit.title + "] of UnitOfMeasure");
      prDDPins.println("	(measureName \"" + lUnit.title + "\")");
      prDDPins.println(
          "	(defaultUnitId \"" + DOMInfoModel.escapeProtegeString(lUnit.default_unit_id) + "\")");
      prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
      if (!lUnit.unit_id.isEmpty()) {
        String lSpace = "";
        prDDPins.print("	(unitId ");
        // set the units
        for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
          String lVal = j.next();
          prDDPins.print(lSpace + "\"" + DOMInfoModel.escapeProtegeString(lVal) + "\"");
          lSpace = " ";
        }
        prDDPins.println("))");
      }
    }

    // write the TBD_unitOfMeasure
    prDDPins.println("([" + "TBD_unit_of_measure_type" + "] of UnitOfMeasure");
    prDDPins.println("	(measureName \"" + "TBD_unit_of_measure_type" + "\")");
    prDDPins.println("	(defaultUnitId \"" + "defaultUnitId" + "\")");
    prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
    prDDPins.println("	(unitId \"" + "TBD_unitId" + "\"))");

    // print data types
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDataType = i.next();
      if (lDataType.isInactive || !lDataType.isDataType) {
        continue;
      }
      prDDPins.println("([" + lDataType.title + "] of DataType");
      prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
      prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
    }
  }
}
