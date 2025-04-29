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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

class WriteDOMCSVFiles extends Object {
  private static final String DELM_BEGIN = "\"", DELM_MID = "\",\"", DELM_END = "\"";

  public WriteDOMCSVFiles() {
    return;
  }

  // write the CSV File
  public void writeDOMCSVFile(ArrayList<DOMClass> lClassArr, SchemaFileDefn lSchemaFileDefn,
      String lOtherLanguage) throws java.io.IOException {
    String pIdentifier;
    String blanks = "                              ";
    String padding;
    int padLength;

    String lFileName = lSchemaFileDefn.relativeFileSpecDDCSV;
    if (lOtherLanguage != null) {
      lFileName = lSchemaFileDefn.relativeFileSpecDDCSV + "_" + lOtherLanguage;
    }
    lFileName += ".csv";

    FileOutputStream lFileOutputStream = new FileOutputStream(lFileName);
    BufferedWriter prCSVAttr =
        new BufferedWriter(new OutputStreamWriter(lFileOutputStream, "UTF8"));
    prCSVAttr.write(DELM_BEGIN + "Sort Key" + DELM_MID + "Type" + DELM_MID + "Name" + DELM_MID
        + "Version" + DELM_MID + "Name Space Id" + DELM_MID + "Description" + DELM_MID + "Steward"
        + DELM_MID + "Value Type" + DELM_MID + "Minimum Cardinality" + DELM_MID
        + "Maximum Cardinality" + DELM_MID + "Minimum Value" + DELM_MID + "Maximum Value" + DELM_MID
        + "Minimum Characters" + DELM_MID + "Maximum Characters" + DELM_MID + "Unit of Measure Type"
        + DELM_MID + "Specified Unit Id" + DELM_MID + "Attribute Concept" + DELM_MID
        + "Conceptual Domain" + DELM_END);
	prCSVAttr.newLine();
    for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) {
        continue;
      }
      if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
        padLength = 30 - lClass.title.length();
        if (padLength < 0) {
          padLength = 0;
        }
        padding = blanks.substring(0, padLength);
        String classSortField = lClass.nameSpaceId + lClass.title + ":1" + padding;
        pIdentifier = classSortField;
        prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Class" + DELM_MID
            + lClass.getNameInLanguage(lOtherLanguage) + DELM_MID + lClass.versionId + DELM_MID
            + lClass.nameSpaceIdNC + DELM_MID + lClass.getDefinitionInLanguage(lOtherLanguage)
            + DELM_MID + lClass.steward + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID
            + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + "" + DELM_MID + ""
            + DELM_MID + "" + DELM_MID + "" + DELM_END + "\r\n");
        ArrayList<DOMProp> allAttr = new ArrayList<>();
        allAttr.addAll(lClass.ownedAttrArr);
        allAttr.addAll(lClass.inheritedAttrArr);
        if (!allAttr.isEmpty()) {
          writeCSVFileLine(lClass.title, lClass.nameSpaceId, allAttr, lOtherLanguage, prCSVAttr);
        }
      }
    }
    // write any singleton attributes
    ArrayList<DOMProp> lEnumAttrArr = new ArrayList<>();
    ArrayList<DOMAttr> lAttrArr =
        new ArrayList<>(DOMInfoModel.userSingletonDOMClassAttrIdMap.values());
    for (Iterator<DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.nameSpaceIdNC) != 0)) {
        continue;
      }
      DOMProp lDOMProp = new DOMProp();
      lDOMProp.createDOMPropSingletonsNoAssoc(lAttr);
      lEnumAttrArr.add(lDOMProp);
    }
    if (!lEnumAttrArr.isEmpty()) {
      writeCSVFileLine(DMDocument.LDDToolSingletonClassTitle, lSchemaFileDefn.nameSpaceIdNC,
          lEnumAttrArr, lOtherLanguage, prCSVAttr);
    }
    prCSVAttr.close();
  }

  // write an attribute, one line
  public void writeCSVFileLine(String lClassTitle, String lClassNameSpaceId,
      ArrayList<DOMProp> allAttr, String lOtherLanguage, BufferedWriter prCSVAttr)
      throws java.io.IOException {
    String pIdentifier;
    String blanks = "                              ";
    String padding;
    int padLength;
    String classSortField, attrSortField, valueSortField;
    padLength = 30 - lClassTitle.length();
    if (padLength < 0) {
      padLength = 0;
    }
    padding = blanks.substring(0, padLength);
    classSortField = lClassNameSpaceId + lClassTitle + ":1" + padding;
    attrSortField = lClassNameSpaceId + lClassTitle + ":2" + padding;
    pIdentifier = classSortField;
    for (Iterator<DOMProp> j = allAttr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
        String pMinVal = lDOMAttr.getMinimumValue(true, true);
        String pMaxVal = lDOMAttr.getMaximumValue(true, true);
        String pMinChar = lDOMAttr.getMinimumCharacters(true, true);
        String pMaxChar = lDOMAttr.getMaximumCharacters(true, true);
        padLength = 30 - lDOMAttr.getTitle().length();
        if (padLength < 0) {
          padLength = 0;
        }
        padding = blanks.substring(0, padLength);
        pIdentifier =
            attrSortField + " " + lDOMAttr.getNameSpaceId() + lDOMAttr.getTitle() + ":1" + padding;
        valueSortField =
            attrSortField + " " + lDOMAttr.getNameSpaceId() + lDOMAttr.getTitle() + ":2" + padding;
        String lTextString = lDOMAttr.getDefinitionInLanguage(lOtherLanguage);
        lTextString = DOMInfoModel.replaceString(lTextString, "\"", "\"\"");
        prCSVAttr.write(DELM_BEGIN + pIdentifier + DELM_MID + "Attribute" + DELM_MID
            + lDOMAttr.getNameInLanguage(lOtherLanguage) + DELM_MID + "n/a" + DELM_MID
            + lDOMProp.getNameSpaceIdNC() + DELM_MID + lTextString + DELM_MID
            + lDOMAttr.getSteward() + DELM_MID + lDOMAttr.valueType + DELM_MID + lDOMProp.cardMin
            + DELM_MID + lDOMProp.cardMax + DELM_MID + pMinVal + DELM_MID + pMaxVal + DELM_MID
            + pMinChar + DELM_MID + pMaxChar + DELM_MID + lDOMAttr.getUnitOfMeasure(true) + DELM_MID
            + lDOMAttr.getDefaultUnitId(true) + DELM_MID + lDOMAttr.classConcept + DELM_MID
            + lDOMAttr.dataConcept + DELM_END + "\r\n");

        for (Iterator<DOMProp> i = lDOMAttr.domPermValueArr.iterator(); i.hasNext();) {
          DOMProp lDOMProp2 = i.next();
          if ((lDOMProp2.domObject == null)
              || !(lDOMProp2.domObject instanceof DOMPermValDefn)) {
            continue;
          }
          DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp2.domObject;
          String lValue = lDOMPermValDefn.value;
          if (lValue.length() > 20) {
            lValue = lValue.substring(0, 20);
          }
          pIdentifier = valueSortField + " Value:" + lValue;
          String lTextString2 = lDOMPermValDefn.value_meaning;
          lTextString2 = DOMInfoModel.replaceString(lTextString2, "\"", "\"\"");
          prCSVAttr.write(
              DELM_BEGIN + pIdentifier + DELM_MID + "Value" + DELM_MID + lDOMPermValDefn.value
                  + DELM_MID + "" + DELM_MID + "" + DELM_MID + lTextString2 + DELM_END + "\r\n");
        }
      }
    }
  }
}
