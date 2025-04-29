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

/**
 * Write the attribute definition products
 * 
 */

// class WriteDDProductAttrDefinitions extends ISO11179MDR{
class WriteDDProductDOMAttrDefinitions extends Object {
  ArrayList<String> adminRecUsedArr, adminRecTitleArr;
  PrintWriter prDDReg;

  public WriteDDProductDOMAttrDefinitions() {
    return;
  }

  // write the Data Element Product files
  public void writeDDDOMRegFiles(SchemaFileDefn lSchemaFileDefn, String todaysDate)
      throws java.io.IOException {
    // get the permissible values for attribute concept

    // cycle through classes to get only the used attributes
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      ArrayList<DOMProp> lDOMArr = lClass.ownedAttrArr;

      for (Iterator<DOMProp> j = lDOMArr.iterator(); j.hasNext();) {
        DOMProp lProp = j.next();
        if (lProp.domObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lProp.domObject;
          if (lDOMAttr.title.compareTo("%3ANAME") == 0) {
            continue;
          }
          String lLID = DMDocument.registrationAuthorityIdentifierValue + "."
              + lDOMAttr.classNameSpaceIdNC + "." + lDOMAttr.parentClassTitle + "."
              + lDOMAttr.getNameSpaceIdNC() + "." + lDOMAttr.title;
          lLID = "urn:nasa:pds:context:attribute:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
          lLID = lLID.toLowerCase();
          String lUIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_"
              + lDOMAttr.classNameSpaceIdNC + "_" + lDOMAttr.parentClassTitle + "_"
              + lDOMAttr.getNameSpaceIdNC() + "_" + lDOMAttr.title;
          String lFileName = lSchemaFileDefn.relativeFileSpecAttrDefn + lUIdFileName + "_"
              + lSchemaFileDefn.lab_version_id + ".xml";
          prDDReg = new PrintWriter(
              new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
          printDDRegFile(lSchemaFileDefn, prDDReg, todaysDate, lLID, lDOMAttr);
          prDDReg.close();
        }
      }
    }
  }


  // Print the Element Definition Header
  public void printDDRegFile(SchemaFileDefn lSchemaFileDefn, PrintWriter prDDReg, String todaysDate,
      String lLID, DOMAttr lAttr) {
    prDDReg.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    // prDDReg.println("<Product_Attribute_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" +
    // DOMInfoModel.ns_version_id + "\"");
    prDDReg.println("<Product_Attribute_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v"
        + lSchemaFileDefn.ns_version_id + "\"");
    prDDReg.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    // prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" +
    // DOMInfoModel.ns_version_id + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
    prDDReg.println(
        " xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id
            + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
    prDDReg.println("    <Identification_Area>");
    prDDReg.println("        <logical_identifier>" + lLID + "</logical_identifier>");
    prDDReg.println("        <version_id>" + lAttr.versionIdentifierValue + "</version_id>");
    prDDReg.println("        <title>" + lAttr.title + "</title>");
    prDDReg.println("        <information_model_version>" + lSchemaFileDefn.ont_version_id
        + "</information_model_version>");
    prDDReg.println("        <product_class>Product_Attribute_Definition</product_class>");
    prDDReg.println("    </Identification_Area>");
    prDDReg.println("    <DD_Attribute_Full>");
    prDDReg.println("        <name>" + lAttr.title + "</name>");
    prDDReg.println("        <version_id>" + lAttr.versionIdentifierValue + "</version_id>");
    prDDReg.println("        <class_name>" + lAttr.parentClassTitle + "</class_name>");
    prDDReg.println("        <local_identifier>" + lLID + "</local_identifier>");
    prDDReg.println("        <steward_id>" + lAttr.steward + "</steward_id>");
    prDDReg.println("        <type>" + "PDS4" + "</type>");
    prDDReg.println("        <namespace_id>" + lAttr.getNameSpaceIdNC() + "</namespace_id>");
    prDDReg.println("        <nillable_flag>" + lAttr.isNilable + "</nillable_flag>");
    prDDReg.println("        <submitter_name>" + lAttr.submitter + "</submitter_name>");
    prDDReg.println("        <definition>" + lAttr.definition + "</definition>");
    prDDReg.println("        <registered_by>" + lAttr.registeredByValue + "</registered_by>");
    prDDReg.println("        <registration_authority_id>"
        + lAttr.registrationAuthorityIdentifierValue + "</registration_authority_id>");
    if (lAttr.classConcept.indexOf("TBD") != 0) {
      prDDReg.println("        <attribute_concept>" + lAttr.classConcept + "</attribute_concept>");
    }

    prDDReg.println("        <Terminological_Entry>");
    prDDReg.println("            <name>" + lAttr.title + "</name>");
    prDDReg.println("            <definition>" + lAttr.definition + "</definition>");
    prDDReg.println("            <language>" + "English" + "</language>");
    prDDReg.println("            <preferred_flag>" + "true" + "</preferred_flag>");
    prDDReg.println("        </Terminological_Entry>");

    prDDReg.println("        <DD_Value_Domain_Full>");

    // new code using attr defn methods
    String pEnumFlag = "false";
    if (lAttr.isEnumerated) {
      pEnumFlag = "true";
    }
    prDDReg.println("            <enumeration_flag>" + pEnumFlag + "</enumeration_flag>");
    prDDReg.println("            <value_data_type>" + lAttr.valueType + "</value_data_type>");
    String lFormat = lAttr.getFormat(true);
    if (lFormat.indexOf("TBD") != 0) {
      prDDReg.println("            <formation_rule>" + lFormat + "</formation_rule>");
    }
    prDDReg.println("            <minimum_characters>" + lAttr.getMinimumCharacters(true, true)
        + "</minimum_characters>");
    prDDReg.println("            <maximum_characters>" + lAttr.getMaximumCharacters(true, true)
        + "</maximum_characters>");
    prDDReg.println(
        "            <minimum_value>" + lAttr.getMinimumValue(true, true) + "</minimum_value>");
    prDDReg.println(
        "            <maximum_value>" + lAttr.getMaximumValue(true, true) + "</maximum_value>");
    // prDDReg.println(" <pattern>" + DOMInfoModel.unEscapeProtegeChar(lAttr.getPattern(true)) +
    // "</pattern>");
    String lPattern = DOMInfoModel.unEscapeProtegeString(lAttr.getPattern(true));
    if (lPattern.indexOf("TBD") != 0) {
      prDDReg.println("            <pattern>" + lPattern + "</pattern>");
    }
    prDDReg.println("            <unit_of_measure_type>" + lAttr.getUnitOfMeasure(true)
        + "</unit_of_measure_type>");
    if (lAttr.dataConcept.indexOf("TBD") != 0) {
      prDDReg
          .println("            <conceptual_domain>" + lAttr.dataConcept + "</conceptual_domain>");
    }
    prDDReg.println(
        "            <specified_unit_id>" + lAttr.getDefaultUnitId(true) + "</specified_unit_id>");

    if (lAttr.isEnumerated) {
      if (!(lAttr.domPermValueArr == null || lAttr.domPermValueArr.isEmpty())) {
        for (Iterator<DOMProp> k = lAttr.domPermValueArr.iterator(); k.hasNext();) {
          DOMProp lDOMProp = k.next();

          if ((lDOMProp.domObject == null)
              || !(lDOMProp.domObject instanceof DOMPermValDefn)) {
            continue;
          }
          DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.domObject;
          String lValue = lDOMPermVal.value;
          if (lValue != null) {
            prDDReg.println("            <DD_Permissible_Value_Full>");
            prDDReg.println("                <value>" + lValue + "</value>");

            String str = lDOMPermVal.value_meaning;
            if (str == null) {
              System.out.println("value_meaning is null" + lAttr.title + "==" + lDOMProp.title);
            } else if (str.indexOf("TBD") != 0) {
              prDDReg.println("                <value_meaning>"
                  + DOMInfoModel.escapeXMLChar(lDOMPermVal.value_meaning) + "</value_meaning>");
            }
            prDDReg.println("                <value_begin_date>" + DMDocument.beginDatePDS4Value
                + "T00:00:00Z" + "</value_begin_date>");
            prDDReg.println("                <value_end_date>" + DMDocument.endDateValue
                + "T00:00:00Z" + "</value_end_date>");
            prDDReg.println("            </DD_Permissible_Value_Full>");
          }
        }
      }
    }
    // prDDReg.println(" <conceptual_domain>" + lAttr.dataConcept + "</conceptual_domain>");

    prDDReg.println("        </DD_Value_Domain_Full>");
    prDDReg.println("    </DD_Attribute_Full>");
    prDDReg.println("</Product_Attribute_Definition>");
    prDDReg.println("");
  }

  // *** needs conversion ***
  // Print the the Protege Pins Misc
  public void printPDDPMISC(PrintWriter prDDReg) {
    // print the Miscellaneous records
    prDDReg.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
    prDDReg.println(
        "	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
    prDDReg.println("	(administrativeStatus Final)");
    prDDReg.println("	(changeDescription \"PSDD content has been merged into the model.\")");
    prDDReg.println("	(creationDate \"2010-03-10\")");
    prDDReg.println("	(effectiveDate \"2010-03-10\")");
    prDDReg.println(
        "	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
    prDDReg.println("	(lastChangeDate \"2010-03-10\")");
    prDDReg.println("	(origin \"Planetary Data System\")");
    prDDReg.println("	(registrationStatus Preferred)");
    prDDReg.println("	(unresolvedIssue \"Issues still being determined.\")");
    prDDReg.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");

    prDDReg.println("([0001_NASA_PDS_1] of RegistrationAuthorityIdentifier");
    prDDReg.println("	(internationalCodeDesignator \"0001\")");
    prDDReg.println("	(opiSource \"1\")");
    prDDReg
        .println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
    prDDReg.println("	(organizationPartIdentifier \"Planetary Data System\"))");

    prDDReg.println("([RA_0001_NASA_PDS_1] of RegistrationAuthority");
    prDDReg.println("	(documentationLanguageIdentifier [LI_English])");
    prDDReg.println("	(languageUsed [LI_English])");
    prDDReg.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
    prDDReg.println("	(organizationName \"NASA Planetary Data System\")");
    prDDReg.println("	(registrar [PDS_Registrar])");
    prDDReg.println("	(registrationAuthorityIdentifier_v [0001_NASA_PDS_1]))");

    prDDReg.println("([NASA_PDS] of Context");
    prDDReg.println("	(dataIdentifier  \"NASA_PDS\"))");

    prDDReg.println("([PDS_Registrar] of  Registrar");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(registrarIdentifier \"PDS_Registrar\"))");

    prDDReg.println("([Steward_PDS] of Steward");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([pds] of Steward");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([img] of Steward");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([rings] of Steward");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([ops] of Steward");
    prDDReg.println("	(contact [PDS_Standards_Coordinator])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([Submitter_PDS] of Submitter");
    prDDReg.println("	(contact [DataDesignWorkingGroup])");
    prDDReg.println("	(organization [RA_0001_NASA_PDS_1]))");

    prDDReg.println("([PDS_Standards_Coordinator] of Contact");
    prDDReg.println("	(contactTitle \"PDS_Standards_Coordinator\")");
    prDDReg.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
    prDDReg.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
    prDDReg.println("	(contactInformation \"Jet Propulsion Laboratory\")");
    prDDReg.println("	(contactPhone \"818.354.6135\")");
    prDDReg.println("	(contactName \"Elizabeth Rye\"))");

    prDDReg.println("([DataDesignWorkingGroup] of Contact");
    prDDReg.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
    prDDReg.println("	(contactInformation \"Jet Propulsion Laboratory\")");
    prDDReg.println("	(contactPhone \"818.354.9338\")");
    prDDReg.println("	(contactName \"J. Steven Hughes\"))");

    prDDReg.println("([LI_English] of LanguageIdentification");
    prDDReg.println("  (countryIdentifier \"USA\")");
    prDDReg.println("  (languageIdentifier \"English\"))");

    for (Iterator<DOMUnit> i = DOMInfoModel.masterDOMUnitArr.iterator(); i.hasNext();) {
      DOMUnit lUnit = i.next();
      prDDReg.println("([" + lUnit.title + "] of UnitOfMeasure");
      prDDReg.println("	(measureName \"" + lUnit.title + "\")");
      prDDReg.println("	(defaultUnitId \"" + lUnit.default_unit_id + "\"))");
      prDDReg.println("	(precision \"" + "TBD_precision" + "\"))");
      prDDReg.print("	(unitId ");
      // set the units
      for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
        String lVal = j.next();
        prDDReg.println("	\"" + lVal + "\" )");
      }
      prDDReg.println("))");
    }

    // print data types
    for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
      DOMDataType lDataType = i.next();
      prDDReg.println("([" + lDataType.title + "] of DataType");
      prDDReg.println("  (dataTypeName \"" + lDataType.title + "\")");
      prDDReg.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
    }
  }
}
