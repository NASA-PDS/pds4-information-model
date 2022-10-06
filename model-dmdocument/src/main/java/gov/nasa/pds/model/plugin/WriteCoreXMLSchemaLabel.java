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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class WriteCoreXMLSchemaLabel extends Object {
  PrintWriter prSchematron;

  public WriteCoreXMLSchemaLabel() {
    return;
  }

  // write the label for the XML schema and schematron file.
  public void writeFile(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    prSchematron = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(lSchemaFileDefn.relativeFileSpecXMLLabel)), "UTF-8"));
    writeFileLines(lSchemaFileDefn, prSchematron);
    prSchematron.close();
    return;
  }

  // write the schematron rules
  public void writeFileLines(SchemaFileDefn lSchemaFileDefn, PrintWriter prSchematron)
      throws java.io.IOException {
    // set up the master schema identifier (namespaceid) - pds
    String lMasterFileId = DMDocument.masterPDSSchemaFileDefn.identifier;
    String lMasterFileIdUpper = lMasterFileId.toUpperCase();

    // write the XML file header
    prSchematron.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    prSchematron.println("  <!-- " + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " XML product label for " + DMDocument.masterPDSSchemaFileDefn.modelShortName + " V"
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "  " + DMDocument.masterTodaysDate
        + " -->");
    prSchematron
        .println("  <!-- Generated from the " + DMDocument.masterPDSSchemaFileDefn.modelShortName
            + " Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id
            + " - System Build " + DMDocument.XMLSchemaLabelBuildNum + " -->");
    prSchematron.println("  <!-- *** This " + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " XML product label is an operational deliverable. *** -->");
    prSchematron.println("<?xml-model href=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURLs
        + lMasterFileId + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/"
        + DMDocument.masterPDSSchemaFileDefn.modelShortName + "_" + lMasterFileIdUpper + "_"
        + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch\"");
    prSchematron.println("  schematypens=\"http://purl.oclc.org/dsdl/schematron\"?>");

    // write the product class
    prSchematron
        .println("<Product_XML_Schema xmlns=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL
            + lMasterFileId + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
    prSchematron.println("    xmlns:" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNCLC + "=\""
        + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + lMasterFileId + "/v"
        + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
    prSchematron.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    prSchematron
        .println("    xsi:schemaLocation=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL
            + lMasterFileId + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id);
    prSchematron.println("    " + DMDocument.masterPDSSchemaFileDefn.nameSpaceURLs + lMasterFileId
        + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/"
        + DMDocument.masterPDSSchemaFileDefn.modelShortName + "_" + lMasterFileIdUpper + "_"
        + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\">");
    prSchematron.println("    <Identification_Area>");

    // generated and write the LID
    String lLID = lSchemaFileDefn.urnPrefix + lSchemaFileDefn.sysBundleName + ":xml_schema:"
        + lSchemaFileDefn.nameSpaceIdNCLC + "-xml_schema" + "_"
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id;
    if (DMDocument.LDDToolFlag) {
      lLID += "_" + DMDocument.masterLDDSchemaFileDefn.versionId;
    }
    prSchematron
        .println("        <logical_identifier>" + lLID.toLowerCase() + "</logical_identifier>");
    prSchematron.println("        <version_id>" + lSchemaFileDefn.labelVersionId + "</version_id>");
    prSchematron.println("        <title>" + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " XML Schema" + " - " + lSchemaFileDefn.nameSpaceIdNCUC + " V"
        + lSchemaFileDefn.ont_version_id + "</title>");
    prSchematron.println("        <information_model_version>"
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</information_model_version>");
    prSchematron.println("        <product_class>Product_XML_Schema</product_class>");
    prSchematron.println("        <Modification_History>");
    prSchematron.println("            <Modification_Detail>");
    prSchematron.println("                <modification_date>" + DMDocument.masterTodaysDateUTC
        + "</modification_date>");
    prSchematron
        .println("                <version_id>" + lSchemaFileDefn.labelVersionId + "</version_id>");
    prSchematron.println("                <description>This is the system generated "
        + DMDocument.masterPDSSchemaFileDefn.modelShortName + " product label for "
        + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " XML Schema and Schematron files.</description>");
    prSchematron.println("            </Modification_Detail>");
    prSchematron.println("        </Modification_History>");
    prSchematron.println("    </Identification_Area>");
    prSchematron.println("    <File_Area_XML_Schema>");
    prSchematron.println("        <File>");
    prSchematron.println(
        "            <file_name>" + lSchemaFileDefn.relativeFileNameXMLSchema + "</file_name>");
    prSchematron.println("            <creation_date_time>" + DMDocument.masterTodaysDateTimeUTCwT
        + "</creation_date_time>");
    prSchematron.println("            <file_size unit=\"byte\">"
        + getFileSize(lSchemaFileDefn.relativeFileSpecXMLSchema) + "</file_size>");
    prSchematron.println("            <records>"
        + getFileNumRec(lSchemaFileDefn.relativeFileSpecXMLSchema) + "</records>");
    prSchematron.println("        </File>");
    prSchematron.println("        <XML_Schema>");
    prSchematron
        .println("            <name>" + lSchemaFileDefn.relativeFileNameXMLSchema + "</name>");
    prSchematron.println("            <offset unit=\"byte\">0</offset>");
    prSchematron
        .println("            <parsing_standard_id>XML Schema Version 1.1</parsing_standard_id>");
    prSchematron.println(
        "            <description>This is a " + DMDocument.masterPDSSchemaFileDefn.modelShortName
            + " XML Schema file for the declared namespace.</description>");
    prSchematron.println("        </XML_Schema>");
    prSchematron.println("    </File_Area_XML_Schema>");
    prSchematron.println("    <File_Area_XML_Schema>");
    prSchematron.println("        <File>");
    prSchematron.println(
        "            <file_name>" + lSchemaFileDefn.relativeFileNameSchematron + "</file_name>");
    prSchematron.println("            <creation_date_time>" + DMDocument.masterTodaysDateTimeUTCwT
        + "</creation_date_time>");
    prSchematron.println("            <file_size unit=\"byte\">"
        + getFileSize(lSchemaFileDefn.relativeFileSpecSchematron) + "</file_size>");
    prSchematron.println("            <records>"
        + getFileNumRec(lSchemaFileDefn.relativeFileSpecSchematron) + "</records>");
    prSchematron.println("        </File>");
    prSchematron.println("        <XML_Schema>");
    prSchematron
        .println("            <name>" + lSchemaFileDefn.relativeFileNameSchematron + "</name>");
    prSchematron.println("            <offset unit=\"byte\">0</offset>");
    prSchematron.println(
        "            <parsing_standard_id>Schematron ISO/IEC 19757-3:2006</parsing_standard_id>");
    prSchematron.println("            <description>This is the "
        + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " Schematron file for the declared namespace. Schematron provides rule-based validation for XML Schema.</description>");
    prSchematron.println("        </XML_Schema>");
    prSchematron.println("    </File_Area_XML_Schema>");
    prSchematron.println("</Product_XML_Schema>");
  }

  // get size metrics of a file
  public long getFileNumRec(String lFileSpecId) throws java.io.IOException {
    // FileInputStream stream = new FileInputStream(lFileSpecId);

    try {
      FileInputStream stream = new FileInputStream(lFileSpecId);
      byte[] buffer = new byte[8192];
      long count = 0;
      int n;
      while ((n = stream.read(buffer)) > 0) {
        for (int i = 0; i < n; i++) {
          if (buffer[i] == '\n') {
            count++;
          }
        }
      }
      stream.close();
      // System.out.println("Number of lines: " + count);
      return count;
    } catch (Exception e) {
      return 99999;
    }
  }

  // get size metrics of a file
  public long getFileSize(String lFileSpecId) throws java.io.IOException {
    File file = new File(lFileSpecId);
    if (file.exists()) {
      return file.length();
    }
    return 99999;
  }
}
