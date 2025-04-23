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
import gov.nasa.pds.model.plugin.util.Utility;

class WriteDOMSchematron extends Object {
  PrintWriter prSchematron;

  public WriteDOMSchematron() {
    return;
  }

  // write all Schematron files
  public void writeSchematronFile(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap) throws java.io.IOException {
    String lFileName = lSchemaFileDefn.relativeFileSpecSchematron;
    prSchematron =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
    writeSchematronRule(lSchemaFileDefn, lMasterDOMClassMap, prSchematron);
    prSchematron.close();
    return;
  }

  // write the schematron rules
  public void writeSchematronRule(SchemaFileDefn lSchemaFileDefn,
      TreeMap<String, DOMClass> lMasterDOMClassMap, PrintWriter prSchematron) {
    // write schematron file header
    printSchematronFileHdr(lSchemaFileDefn, prSchematron);
    printSchematronFileCmt(prSchematron);

    // select out the rules for this namespace
    ArrayList<DOMRule> lSelectRuleArr = new ArrayList<>();
    ArrayList<DOMRule> lRuleArr = new ArrayList<>(DOMInfoModel.masterDOMRuleMap.values()); // use unique rdfIdentifier
    for (DOMRule lRule : lRuleArr) {
      if (lSchemaFileDefn.isMaster) {
        if (lRule.isMissionOnly || !(lRule.alwaysInclude
            || (lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
                && lSchemaFileDefn.stewardArr.contains(lRule.classSteward)))) continue;
      } else if (lSchemaFileDefn.isDiscipline) {
        if (lRule.isMissionOnly
            || !(((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
                && lSchemaFileDefn.stewardArr.contains(lRule.classSteward))
                || lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.attrNameSpaceNC) == 0))) continue;
      } else if (lSchemaFileDefn.isMission) {
        if (lRule.isMissionOnly
            || !(((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
                && lSchemaFileDefn.stewardArr.contains(lRule.classSteward))
                || lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.attrNameSpaceNC) == 0))) continue;
      } else if (lSchemaFileDefn.isLDD) {
        // write an LDD schemtron
        Utility.registerMessage(
            "2>info " + "Found LDD - lSchemaFileDefn.isMission:" + lSchemaFileDefn.isMission);
        if (!(lRule.isMissionOnly && lSchemaFileDefn.isMission)
            || !((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
                && lSchemaFileDefn.stewardArr.contains(lRule.classSteward))
                || (lRule.classTitle.compareTo(DMDocument.LDDToolSingletonClassTitle) == 0))) continue;
      } else {
        Utility.registerMessage("1>warning "
            + "Write Schematron - Invalid governance in SchemaFileDefn  - lSchemaFileDefn.identifier:"
            + lSchemaFileDefn.identifier);
      }
      lSelectRuleArr.add(lRule);
    }
    writeSchematronRuleClasses(lSchemaFileDefn, lSelectRuleArr, prSchematron);

    // write schematron file footer
    printSchematronFileFtr(prSchematron);
  }

  // check if assert statement already exists
  public boolean foundAssertStmt(String lAttrId, ArrayList<DOMAssert> lAssertArr) {
    for (DOMAssert lAssert : lAssertArr) {
      if (lAssert.identifier.compareTo(lAttrId) == 0) {
        return true;
      }
    }
    return false;
  }

  // write the schematron rules
  // 555 TBD clean up assertMsg
  public void writeSchematronRuleClasses(SchemaFileDefn lSchemaFileDefn,
      ArrayList<DOMRule> lRuleArr, PrintWriter prSchematron) {
    // write class based assertions

    for (DOMRule lRule : lRuleArr) {
      prSchematron.println("  <sch:pattern>");

      // write pattern level LETs
      for (String lLetAssignPattern : lRule.letAssignPatternArr) {
        prSchematron.println("    <sch:let " + lLetAssignPattern + "/>");
      }

      String lRole = "";
      if (lRule.roleId.compareTo(" role=\"warning\"") == 0) {
        lRole = " role=\"warning\"";
      }
      if (lRule.roleId.compareTo("warning") == 0) {
        lRole = " role=\"warning\"";
      }
      prSchematron.println("    <sch:rule context=\"" + lRule.xpath + "\"" + lRole + ">");

      // write rule level LETs
      for (String lLetAssign : lRule.letAssignArr) {
        prSchematron.println("      <sch:let " + lLetAssign + "/>");
      }
      for (DOMAssert lAssert : lRule.assertArr) {
       	String warningStr = "";
      	if (lAssert.assertType.indexOf("WARN") > 0) warningStr = " role=\"WARN\"";
        if (lAssert.assertType.indexOf("RAW") == 0) {
          if (lAssert.assertMsg.indexOf("TBD") == 0) {
            prSchematron.println("      <sch:assert test=\"" + lAssert.assertStmt + "\"" + warningStr + "/>");
            prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          } else {
            prSchematron.println("      <sch:assert test=\"" + lAssert.assertStmt + "\"" + warningStr + ">");
            prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          }
          prSchematron.println("        " + lAssert.assertMsg + "</sch:assert>");
        } else if (lAssert.assertType.compareTo("REPORT") == 0) {
          if (lAssert.assertMsg.indexOf("TBD") == 0) {
            prSchematron.println("      <sch:report test=\"" + lAssert.assertStmt + "\"/>");
            prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          } else {
            prSchematron.println("      <sch:report test=\"" + lAssert.assertStmt + "\">");
            prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          }
          prSchematron.println("        " + lAssert.assertMsg + "</sch:report>");
        } else if (lAssert.assertType.compareTo("EVERY") == 0) {
          String lDel = "";
          prSchematron.print("      <sch:assert test=\"" + "every $ref in (" + lRule.attrNameSpaceNC
              + ":" + lRule.attrTitle + ") satisfies $ref = (");
          String lTestValueString = "";
          for (Iterator<String> k = lAssert.testValArr.iterator(); k.hasNext();) {
            lTestValueString += lDel + "'" + k.next() + "'";
            lDel = ", ";
          }
          prSchematron.print(lTestValueString);
          prSchematron.println(")\">");
          prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          // print message
          prSchematron.println("        The attribute " + lRule.attrTitle + lAssert.assertMsg
              + lTestValueString + ".</sch:assert>");
        } else if (lAssert.assertType.compareTo("IF") == 0) {
          String lDel = "";
          prSchematron.print(
              "      <sch:assert test=\"" + "if (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle
                  + ") then " + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + " = (");
          String lTestValueString = "";
          for (Iterator<String> k = lAssert.testValArr.iterator(); k.hasNext();) {
            lTestValueString += lDel + "'" + k.next() + "'";
            lDel = ", ";
          }
          prSchematron.print(lTestValueString);
          prSchematron.println(") else true ()\">");
          prSchematron.println("        <title>" + getRuleTitle(lRule, lAssert) + "</title>");
          // print message
          prSchematron.println("        The attribute " + lRule.attrTitle + lAssert.assertMsg
              + lTestValueString + ".</sch:assert>");
        }
      }
      prSchematron.println("    </sch:rule>");
      prSchematron.println("  </sch:pattern>");
    }
  }

  // get the rule title
  public String getRuleTitle(DOMRule lRule, DOMAssert lAssert) {
    String lTitle = lRule.identifier + "/" + lAssert.identifier;
    if (lRule.isAssociatedExternalClass) {
      lTitle = lRule.identifier;
    }
    return lTitle;
  }

  // write the schematron file header
  public void printSchematronFileHdr(SchemaFileDefn lSchemaFileDefn, PrintWriter prSchematron) {
    prSchematron.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    prSchematron.println("  <!-- " + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " Schematron" + " for Name Space Id:" + lSchemaFileDefn.nameSpaceIdNC + "  Version:"
        + lSchemaFileDefn.ont_version_id + " - " + DMDocument.masterTodaysDate + " -->");
    prSchematron
        .println("  <!-- Generated from the " + DMDocument.masterPDSSchemaFileDefn.modelShortName
            + " Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id
            + " - System Build " + DMDocument.XMLSchemaLabelBuildNum + " -->");
    prSchematron.println("  <!-- *** This " + DMDocument.masterPDSSchemaFileDefn.modelShortName
        + " schematron file is an operational deliverable. *** -->");
    prSchematron.println(
        "<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">");
    prSchematron.println("");
    prSchematron.println("  <sch:title>Schematron using XPath 2.0</sch:title>");
    prSchematron.println("");
    prSchematron.println(
        "  <sch:ns uri=\"http://www.w3.org/2001/XMLSchema-instance\"" + " prefix=\"xsi\"/>");
    if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0) {
      // namespaces required: pds - latest version
      prSchematron.println("  <sch:ns uri=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL
          + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v"
          + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" prefix=\""
          + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "\"/>");
    } else {
      // namespaces required: pds
      prSchematron.println("  <sch:ns uri=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL
          + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v"
          + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" prefix=\""
          + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "\"/>");
      // namespaces required: ldd
      String governanceDirectory = "";
      if (lSchemaFileDefn.isMission && !DMDocument.disciplineMissionFlag) {
        governanceDirectory = lSchemaFileDefn.governanceLevel.toLowerCase() + "/";
      }
      prSchematron.println("  <sch:ns uri=\"" + lSchemaFileDefn.nameSpaceURL + governanceDirectory
          + lSchemaFileDefn.nameSpaceIdNCDir + "/v" + lSchemaFileDefn.ns_version_id + "\" prefix=\""
          + lSchemaFileDefn.nameSpaceIdNC + "\"/>");
      // namespaces required: all other LDD discipline levels referenced; no mission level allowed
      for (String lNameSpaceIdNC : DMDocument.LDDImportNameSpaceIdNCArr) {
        String lVersionNSId = "TBD_lVersionNSId";
        String lNameSpaceURL = "TBD_lNameSpaceURL";
        String lNameSpaceIdNCDIR = "TBD_lNameSpaceIdNCDIR";
        // omit this LDD schema file's namespace; namespace used as targetNamespace above
        if (lNameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) == 0) continue;

        // get info for XML schema namespace declaration; first try LDD
        SchemaFileDefn lSchemaFileDefnExternal =
            DMDocument.LDDSchemaFileSortMap.get(lNameSpaceIdNC);
        if (lSchemaFileDefnExternal != null) {
          lVersionNSId = lSchemaFileDefnExternal.ns_version_id;
          lNameSpaceURL = lSchemaFileDefnExternal.nameSpaceURL;
          lNameSpaceIdNCDIR = lSchemaFileDefnExternal.nameSpaceIdNCDir;
        } else {

          // next try config.properties
          lSchemaFileDefnExternal = DMDocument.masterAllSchemaFileSortMap.get(lNameSpaceIdNC);
          if (lSchemaFileDefnExternal != null) {
            lVersionNSId = lSchemaFileDefnExternal.ns_version_id;
            lNameSpaceURL = lSchemaFileDefnExternal.nameSpaceURL;
            lNameSpaceIdNCDIR = lSchemaFileDefnExternal.nameSpaceIdNCDir;
          } else {
            lVersionNSId = DMDocument.masterPDSSchemaFileDefn.ns_version_id;
            lNameSpaceURL = DMDocument.masterPDSSchemaFileDefn.nameSpaceURL;
            lNameSpaceIdNCDIR = DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNCDir;
            Utility.registerMessage("1>warning "
                + "config.properties file entry is missing for namespace id:" + lNameSpaceIdNC);
          }
        }
        prSchematron.println("  <sch:ns uri=\"" + lNameSpaceURL + lNameSpaceIdNCDIR + "/v"
            + lVersionNSId + "\" prefix=\"" + lNameSpaceIdNC + "\"/>");
      }
    }
  }

  // write the schematron file header
  public void printSchematronFileCmt(PrintWriter prSchematron) {
    prSchematron.println("");
    prSchematron.println("		   <!-- ================================================ -->");
    prSchematron.println("		   <!-- NOTE:  There are two types of schematron rules.  -->");
    prSchematron.println("		   <!--        One type includes rules written for       -->");
    prSchematron.println("		   <!--        specific situations. The other type are   -->");
    prSchematron.println("		   <!--        generated to validate enumerated value    -->");
    prSchematron.println("		   <!--        lists. These two types of rules have been -->");
    prSchematron.println("		   <!--        merged together in the rules below.       -->");
    prSchematron.println("		   <!-- ================================================ -->");
  }

  // write the schematron file footer
  public void printSchematronFileFtr(PrintWriter prSchematron) {
    prSchematron.println("</sch:schema>");
  }
}
