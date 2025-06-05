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

// Write the PDS4 Data Dictionary DocBook file.

class WriteDOMDocBook extends Object {
  // class structures
  TreeMap<String, ClassClassificationDefnDOM> classClassificationMap;
  ArrayList<ClassClassificationDefnDOM> classClassificationArr;

  // namespaces to process
  ArrayList<SchemaFileDefn> lSchemaFileDefnToWriteArr;

  // attribute structures
  TreeMap<String, AttrClassificationDefnDOM> attrClassificationMap;
  ArrayList<AttrClassificationDefnDOM> attrClassificationArr;

  // Miscellaneous
  String writtenNamespaceIds = DMDocument.getMasterPDSSchemaFileDefn().identifier + " v"
      + DMDocument.getMasterPDSSchemaFileDefn().versionId;

  // Insert zero-width space characters (&#x200B;) in text strings; form break points for the lines.

  public WriteDOMDocBook() {
    // class structures
    classClassificationMap = new TreeMap<>();
    attrClassificationMap = new TreeMap<>();

    // get the current namespaces and initialize classification maps
    if (!DMDocument.LDDToolFlag) {
      // only the common dictionary (pds)
      lSchemaFileDefnToWriteArr = new ArrayList<>();
      lSchemaFileDefnToWriteArr.add(DMDocument.getMasterPDSSchemaFileDefn());
    } else {
      // intialize array to capture namespaces to be written
      ArrayList<String> lDDNamespaceArr = new ArrayList<>();
      lSchemaFileDefnToWriteArr = new ArrayList<>();

      // first get the common dictionary
      ArrayList<SchemaFileDefn> tempSchemaFileDefnToWriteArr = new ArrayList<>();
      tempSchemaFileDefnToWriteArr.add(DMDocument.getMasterPDSSchemaFileDefn());
      lSchemaFileDefnToWriteArr.add(DMDocument.getMasterPDSSchemaFileDefn());
      lDDNamespaceArr.add(DMDocument.getMasterPDSSchemaFileDefn().nameSpaceIdNC);

      // now add all the LDDs that are stacked for this run
      tempSchemaFileDefnToWriteArr = new ArrayList<>(DMDocument.LDDSchemaFileSortMap.values());
      for (SchemaFileDefn lSchemaFileDefn : tempSchemaFileDefnToWriteArr) {
        if (!lDDNamespaceArr.contains(lSchemaFileDefn.nameSpaceIdNC)) {
          lSchemaFileDefnToWriteArr.add(lSchemaFileDefn);
          lDDNamespaceArr.add(lSchemaFileDefn.nameSpaceIdNC);
        }
      }
    }

    // System.out.println("");
    for (SchemaFileDefn lSchemaFileDefn: lSchemaFileDefnToWriteArr) {
      classClassificationMap.put(lSchemaFileDefn.nameSpaceIdNC,
          new ClassClassificationDefnDOM(lSchemaFileDefn.nameSpaceIdNC));
      attrClassificationMap.put(lSchemaFileDefn.nameSpaceIdNC,
          new AttrClassificationDefnDOM(lSchemaFileDefn.nameSpaceIdNC));
    }

    classClassificationMap.put("pds.product", new ClassClassificationDefnDOM("pds.product"));
    if (DMDocument.pds4ModelFlag) {
      classClassificationMap.put("pds.ops", new ClassClassificationDefnDOM("pds.ops"));
    }
    classClassificationMap.put("pds.support", new ClassClassificationDefnDOM("pds.support"));
    classClassificationMap.put("pds.other", new ClassClassificationDefnDOM("pds.other"));

    classClassificationMap.put("other", new ClassClassificationDefnDOM("other"));
    classClassificationArr = new ArrayList<>(classClassificationMap.values());
    attrClassificationMap.put("other", new AttrClassificationDefnDOM("other"));
    attrClassificationArr = new ArrayList<>(attrClassificationMap.values());

    // classify attributes and classes
    // get the class classification maps
    for (DOMClass lClass: DOMInfoModel.getMasterDOMClassArr()) {
      if (lClass.isInactive) {
        continue;
      }
      getClassClassification(lClass);
    }

    // get the classification arrays
    for (ClassClassificationDefnDOM lClassClassificationDefn: classClassificationArr) {
      lClassClassificationDefn.classArr =
          new ArrayList<>(lClassClassificationDefn.classMap.values());
    }

    // get the attribute classification maps
    for (DOMAttr lAttr: DOMInfoModel.getMasterDOMAttrArr()) {
      if (lAttr.isInactive) {
        continue;
      }
      getAttrClassification(lAttr);
    }

    // get the classification arrays
    for (AttrClassificationDefnDOM lAttrClassificationDefn: attrClassificationArr) {
      lAttrClassificationDefn.attrArr = new ArrayList<>(lAttrClassificationDefn.attrMap.values());
    }

    // print out the class and attribute counts
    Utility.registerMessage("0>info " + "DD DocBook Class Counts");
    Set<String> set9 = classClassificationMap.keySet();
    Iterator<String> iter9 = set9.iterator();
    while (iter9.hasNext()) {
      String lId = iter9.next();
      ClassClassificationDefnDOM lClassClassificationDefnDOM = classClassificationMap.get(lId);
      if (lClassClassificationDefnDOM != null) {
        Utility.registerMessage("0>info " + " - namespace: " + lId + "   size: "
            + lClassClassificationDefnDOM.classArr.size());
        if (lClassClassificationDefnDOM.classArr.size() > 0) {
          if (!(lId.compareTo(DMDocument.getMasterNameSpaceIdNCLC()) == 0
              || lId.compareTo("pds.product") == 0 || lId.compareTo("pds.ops") == 0
              || lId.compareTo("pds.support") == 0 || lId.compareTo("pds.other") == 0
              || lId.compareTo("other") == 0)) {
            writtenNamespaceIds += ", " + lId;
            SchemaFileDefn lSchemaFileDefn = DMDocument.LDDSchemaFileSortMap.get(lId);
            if (lSchemaFileDefn != null) {
              writtenNamespaceIds += " v" + lSchemaFileDefn.versionId;
            }
          }
        }
      }
    }

    Utility.registerMessage("0>info " + "DD DocBook Attribute Counts");
    Set<String> set92 = classClassificationMap.keySet();
    Iterator<String> iter92 = set92.iterator();
    while (iter92.hasNext()) {
      String lId = iter92.next();
      AttrClassificationDefnDOM lAttrClassificationDefnDOM = attrClassificationMap.get(lId);
      if (lAttrClassificationDefnDOM != null) {
        Utility.registerMessage("0>info " + " - namespace: " + lId + "   size: "
            + lAttrClassificationDefnDOM.attrArr.size());
      }
    }
    return;
  }

  public void getClassClassification(DOMClass lClass) {
    if (lClass.isDataType || lClass.isUnitOfMeasure) {
      return;
    }

    // classify the class by namespace and other criteria
    ClassClassificationDefnDOM lClassClassificationDefn =
        classClassificationMap.get(lClass.nameSpaceIdNC);
    if (lClassClassificationDefn != null) {
      if (lClass.nameSpaceIdNC.compareTo(DMDocument.getMasterNameSpaceIdNCLC()) == 0) {
        // i.e., the Common directory, pds
        if (lClass.steward.compareTo("ops") == 0) {
          lClassClassificationDefn = classClassificationMap.get("pds.ops");
          lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
        } else if (lClass.isRegistryClass) {
          lClassClassificationDefn = classClassificationMap.get("pds.product");
          lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
        } else {
          lClassClassificationDefn = classClassificationMap.get("pds.support");
          lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
        }
      } else {
        // all LDD namespaces
        lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
      }
    } else {
      lClassClassificationDefn = classClassificationMap.get("other");
      lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
    }
    return;
  }

  public void getAttrClassification(DOMAttr lAttr) {
    if (!(lAttr.isUsedInClass && lAttr.isAttribute)) {
      return;
    }

    // classify the class by namespace and other criteria
    String lAttrId = lAttr.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.attrParentClass.title
        + "." + lAttr.classNameSpaceIdNC + "." + DMDocument.registrationAuthorityIdentifierValue;
    AttrClassificationDefnDOM lAttrClassificationDefn =
        attrClassificationMap.get(lAttr.nameSpaceIdNC);
    if (lAttrClassificationDefn != null) {
      lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);
    } else {
      lAttrClassificationDefn = attrClassificationMap.get("other");
      lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);
    }
    return;
  }

  // print DocBook File
  public void writeDocBook(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    String lFileName = lSchemaFileDefn.relativeFileSpecDDDocXML;
    String lLabelVersionId = "_" + DMDocument.getMasterPDSSchemaFileDefn().lab_version_id;
    String lDOMLabelVersionId = lLabelVersionId;
    lFileName = DMDocument.replaceString(lFileName, lLabelVersionId, lDOMLabelVersionId);
    PrintWriter prDocBook =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
    writeHeader(prDocBook);

    // write the Common dictionary
    writeClassSection(DMDocument.getMasterNameSpaceIdNCLC(), prDocBook);
    writeAttrSection(DMDocument.getMasterNameSpaceIdNCLC(), prDocBook);

    // write the LDDs
    for (SchemaFileDefn lSchemaFileDefnToWrite: lSchemaFileDefnToWriteArr) {
      if (lSchemaFileDefnToWrite.nameSpaceIdNCLC.compareTo(DMDocument.getMasterNameSpaceIdNCLC()) == 0) {
        continue; // skip master namespace
      }
      ClassClassificationDefnDOM lClassClassificationDefn =
          classClassificationMap.get(lSchemaFileDefnToWrite.nameSpaceIdNCLC);
      if (lClassClassificationDefn != null) {
        if (lClassClassificationDefn.classArr.size() > 0) {
          writeClassSection(lClassClassificationDefn, prDocBook);
        }
      }
      AttrClassificationDefnDOM lAttrClassificationDefn =
          attrClassificationMap.get(lSchemaFileDefnToWrite.nameSpaceIdNCLC);
      if (lAttrClassificationDefn != null) {
        if (lAttrClassificationDefn.attrArr.size() > 0) {
          writeAttrSection(lAttrClassificationDefn, prDocBook);
        }
      }
    }

    // write the Data Types and Units
    writeDataTypeSection(DMDocument.getMasterNameSpaceIdNCLC(), prDocBook);
    writeUnitsSection(DMDocument.getMasterNameSpaceIdNCLC(), prDocBook);
    writeFooter(prDocBook);
    prDocBook.close();
    return;
  }

  private void writeClassSection(String lNameSpaceId, PrintWriter prDocBook) {
    prDocBook.println("");
    prDocBook.println("      <!-- =====================Part2 Begin=========================== -->");
    prDocBook.println("");

    ClassClassificationDefnDOM lClassClassificationDefn = classClassificationMap.get("pds.product");
    if (lClassClassificationDefn != null) {
      prDocBook.println("        <chapter>");
      prDocBook.println("           <title>Product Classes in the common namespace.</title>");
      prDocBook.println("           <para>These classes define the products.</para>");
      for (DOMClass lClass: lClassClassificationDefn.classArr) {
        writeClass(lClass, prDocBook);
      }
      prDocBook.println("        </chapter>");
      prDocBook.println("");
    }

    lClassClassificationDefn = classClassificationMap.get("pds.support");
    if (lClassClassificationDefn != null) {
      prDocBook.println("        <chapter>");
      prDocBook.println("           <title>Support classes in the common namespace.</title>");
      prDocBook.println(
          "           <para>The classes in this section are used by the product classes.</para>");
      for (DOMClass lClass: lClassClassificationDefn.classArr) {
        writeClass(lClass, prDocBook);
      }
      prDocBook.println("        </chapter>");
      prDocBook.println("");

    }

    if (!DMDocument.importJSONAttrFlag) { // if a JSON run, dont write the following
      lClassClassificationDefn = classClassificationMap.get("pds.ops");
      if (lClassClassificationDefn != null) {
        prDocBook.println("        <chapter>");
        prDocBook.println("           <title>OPS catalog classes in the common namespace.</title>");
        prDocBook.println("           <para>These classes support operations. </para>");
        for (DOMClass lClass: lClassClassificationDefn.classArr) {
          writeClass(lClass, prDocBook);
        }
        prDocBook.println("        </chapter>");
        prDocBook.println("");
      }
    }

    prDocBook.println("      <!-- =====================Part2 End=========================== -->");
    prDocBook.println("");
  }

  private void writeClassSection(ClassClassificationDefnDOM lClassClassificationDefn,
      PrintWriter prDocBook) {
    prDocBook.println("");
    prDocBook.println(
        "      <!-- ===================== LDD Class Begin =========================== -->");
    prDocBook.println("");

    prDocBook.println("        <chapter>");
    prDocBook.println("           <title>Classes in the " + lClassClassificationDefn.namespaceId
        + " namespace.</title>");
    prDocBook.println("           <para>These classes comprise the namespace.</para>");
    for (DOMClass lClass: lClassClassificationDefn.classArr) {
      writeClass(lClass, prDocBook);
    }
    prDocBook.println("        </chapter>");
    prDocBook.println("");

    prDocBook
        .println("      <!-- ===================== LDD Class End =========================== -->");
    prDocBook.println("");
  }

  private void writeAttrSection(AttrClassificationDefnDOM lAttrClassificationDefn,
      PrintWriter prDocBook) {
    prDocBook.println("");
    prDocBook.println(
        "      <!-- ===================== LDD Attribute Begin =========================== -->");
    prDocBook.println("");

    prDocBook.println("        <chapter>");
    prDocBook.println("           <title>Attributes in the " + lAttrClassificationDefn.namespaceId
        + " namespace.</title>");
    prDocBook.println("           <para>These attributes are used by the classes in the "
        + lAttrClassificationDefn.namespaceId + " namespace. </para>");
    for (DOMAttr lAttr: lAttrClassificationDefn.attrArr) {
      writeAttr(lAttr, prDocBook);
    }
    prDocBook.println("        </chapter>");
    prDocBook.println("");

    prDocBook.println(
        "      <!-- ===================== LDD Attribute Begin =========================== -->");
    prDocBook.println("");
  }

  private void writeClass(DOMClass lClass, PrintWriter prDocBook) {
    String lValueString = "";
    String lValueDel = "";
    String lRegistrationStatus = "Active";
    String lRegistrationStatusInsert = "";
    if (lClass.registrationStatus.compareTo("Retired") == 0) {
      lRegistrationStatus = "Deprecated";
      lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
    }
    prDocBook.println("<sect1>");
    prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title)
        + lRegistrationStatusInsert + "</title>");
    prDocBook.println("");
    prDocBook.println("<para>");
    prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
    prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
    prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <thead>");
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">"
        + getPrompt("Name: ") + getValue(lClass.title) + lRegistrationStatusInsert + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Version Id: ")
        + getValue(lClass.versionId) + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("            </thead>");
    prDocBook.println("            <tbody>");
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
        + getPrompt("Description:") + getValue(lClass.definition) + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry>" + getPrompt("Namespace Id: ")
        + getValue(lClass.nameSpaceIdNC) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Steward: ")
        + getValue(lClass.steward) + "</entry>");
    prDocBook.println(
        "                    <entry>" + getPrompt("Role: ") + getValue(lClass.role) + "</entry>");
    prDocBook.println(
        "                    <entry>" + getPrompt("Status: ") + lRegistrationStatus + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("");

    // write hierarchy
    ArrayList<DOMClass> lClassArr = new ArrayList<>(lClass.superClassHierArr);
    lClassArr.add(lClass);
    lValueString = "";
    lValueDel = "";
    for (DOMClass lHierClass: lClassArr) {
      lValueString += lValueDel + getClassLink(lHierClass);
      lValueDel = " :: ";
    }
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
        + getPrompt("Class Hierarchy: ") + lValueString + "</entry>");
    prDocBook.println("                </row>");

    // determine the type and number of class members (attributes and classes)
    int attrCount = 0, assocCount = 0;
    if (lClass.allAttrAssocArr != null) {
      for (DOMProp lProp: lClass.allAttrAssocArr) {
        if (lProp.isAttribute) {
          attrCount++;
        } else {
          assocCount++;
        }
      }
    }

    // write the attributes
    if (attrCount == 0) {
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("No Attributes") + "</entry>");
      prDocBook.println(
          "                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\"></entry>");
      prDocBook.println("                </row>");
    } else {
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("Attribute(s)") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Name") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Cardinality") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
      prDocBook.println("                </row>");

      for (DOMProp lProp: lClass.allAttrAssocArr) {
        if (!lProp.isAttribute) {
          continue;
        }
        DOMAttr lAttr = (DOMAttr) lProp.domObject;
        lValueString = "None";
        lValueDel = "";
        if (!(lAttr.domPermValueArr == null || lAttr.domPermValueArr.size() == 0)) {
          lValueString = "";
          for (DOMProp lDOMProp: lAttr.domPermValueArr) {
            if (!(lDOMProp.domObject instanceof DOMPermValDefn)) {
              continue;
            }
            DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lDOMProp.domObject;
            lValueString += lValueDel + getValueLink(lAttr, getValue(lPermValueDefn.value));
            lValueDel = ", ";
          }
        }
        String choiceIndicator = "";
        if (lProp.isChoice) choiceIndicator = " - Choice";
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry></entry>");
        prDocBook.println("                    <entry>" + getAttrLink(lAttr) + "</entry>");
        prDocBook.println("                    <entry>"
            + getValue(getCardinality(lAttr.cardMinI, lAttr.cardMaxI)) + choiceIndicator + "</entry>");
        prDocBook.println("                    <entry>" + lValueString + "</entry>");
        prDocBook.println("                </row>");
      }
    }

    // write the associations
    if (assocCount == 0) {
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("No Associations") + "</entry>");
      prDocBook.println(
          "                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\"></entry>");
      prDocBook.println("                </row>");
    } else {

      // write the associations
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("Association(s)") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Name") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Cardinality") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Class") + "</entry>");
      prDocBook.println("                </row>");

      // first get array of member classes for this association
      TreeMap<String, String> lPropOrderMap = new TreeMap<>();
      TreeMap<String, String> lPropCardMap = new TreeMap<>();
      TreeMap<String, TreeMap<String, DOMClass>> lPropMemberClassMap = new TreeMap<>();

      int lSeqNumI = 1000;
      for (DOMProp lDOMProp: lClass.allAttrAssocArr) {
        if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMClass) {
          DOMClass lMemberDOMClass = (DOMClass) lDOMProp.domObject;
          TreeMap<String, DOMClass> lMemberClassMap = lPropMemberClassMap.get(lDOMProp.title);
          if (lMemberClassMap != null) {
            lMemberClassMap.put(lMemberDOMClass.title, lMemberDOMClass);
          } else {
            lSeqNumI++;
            String lSeqNum = Integer.toString(lSeqNumI);
            lPropOrderMap.put(lSeqNum, lDOMProp.title);
            lMemberClassMap = new TreeMap<>();
            lMemberClassMap.put(lMemberDOMClass.title, lMemberDOMClass);
            lPropMemberClassMap.put(lDOMProp.title, lMemberClassMap);
            String choiceIndicator = "";
            if (lDOMProp.isChoice) choiceIndicator = " - Choice";
            lPropCardMap.put(lDOMProp.title,
                getValue((getCardinality(lDOMProp.cardMinI, lDOMProp.cardMaxI)) + choiceIndicator));
          }
        }
      }

      // write the member classes
      ArrayList<String> lPropOrderArr = new ArrayList<>(lPropOrderMap.values());
      for (String lDOMPropTitle: lPropOrderArr) {
        TreeMap<String, DOMClass> lMemberClassMap = lPropMemberClassMap.get(lDOMPropTitle);
        String lCardString = lPropCardMap.get(lDOMPropTitle);
        if (lCardString != null && lMemberClassMap != null) {
          ArrayList<DOMClass> lMemberClassArr = new ArrayList<>(lMemberClassMap.values());
          lValueString = "";
          lValueDel = "";
          for (DOMClass lMemberClass: lMemberClassArr) {
            lValueString += lValueDel + getClassLink(lMemberClass);
            lValueDel = ", ";
          }

          prDocBook.println("                <row>");
          prDocBook.println("                    <entry></entry>");
          prDocBook
              .println("                    <entry>" + getValueBreak(lDOMPropTitle) + "</entry>");
          prDocBook.println("                    <entry>" + lCardString + "</entry>");
          prDocBook.println("                    <entry>" + lValueString + "</entry>");
          prDocBook.println("                </row>");
        }
      }
    }

    // write the references
    ArrayList<DOMClass> lRefClassArr = getClassReferences(lClass);
    lValueString = "";
    lValueDel = "";
    if (!(lRefClassArr == null || lRefClassArr.isEmpty())) {
      for (DOMClass lRefClass: lRefClassArr) {
        lValueString += lValueDel + getClassLink(lRefClass);
        lValueDel = ", ";
      }
    } else {
      lValueString = "None";
    }
    prDocBook.println("                <row>");
    // prDocBook.println(" <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" +
    // getPrompt("Referenced from: ") + getValue(lValueString) + "</entry>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
        + getPrompt("Referenced from: ") + lValueString + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("            </tbody>");
    prDocBook.println("        </tgroup>");
    prDocBook.println("        </informaltable>");
    prDocBook.println("</para>");
    prDocBook.println("</sect1> ");
    return;
  }

  private void writeAttrSection(String lNameSpaceId, PrintWriter prDocBook) {
    prDocBook.println("");
    prDocBook.println("      <!-- =====================Part3 Begin=========================== -->");
    prDocBook.println("");

    AttrClassificationDefnDOM lAttrClassificationDefn =
        attrClassificationMap.get(DMDocument.getMasterNameSpaceIdNCLC());
    if (lAttrClassificationDefn != null) {
      prDocBook.println("        <chapter>");
      prDocBook.println("           <title>Attributes in the common namespace.</title>");
      prDocBook.println(
          "           <para>These attributes are used by the classes in the common namespace. </para>");
      for (DOMAttr lAttr: lAttrClassificationDefn.attrArr) {
        writeAttr(lAttr, prDocBook);
      }
      prDocBook.println("        </chapter>");
      prDocBook.println("");
    }

    prDocBook.println("      <!-- =====================Part3 End=========================== -->");
    prDocBook.println("");
  }

  private void writeAttr(DOMAttr lAttr, PrintWriter prDocBook) {
    String lRegistrationStatus = "Active";
    String lRegistrationStatusInsert = "";
    if (lAttr.registrationStatus.compareTo("Retired") == 0) {
      lRegistrationStatus = "Deprecated";
      lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
    }
    prDocBook.println("<sect1>");

    prDocBook.println("    <title>" + getValue(lAttr.title) + "  in  "
        + getClassLink(lAttr.attrParentClass) + lRegistrationStatusInsert + "</title>");
    prDocBook.println("");
    prDocBook.println("<para>");
    prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
    prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
    prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
    prDocBook.println("            <thead>");
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">"
        + getPrompt("Name: ") + getAttrAnchor(lAttr) + getValue(lAttr.title)
        + lRegistrationStatusInsert + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Version Id: ")
        + getValue("1.0.0.0") + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Version Id: ") + getValue(lAttr.versionId) +
    // "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("            </thead>");
    prDocBook.println("            <tbody>");
    prDocBook.println("                <row>");
    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
        + getPrompt("Description: ") + getValue(lAttr.definition) + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("                <row>");
    // prDocBook.println(" <entry>" + getPrompt("Namespace Id: ") +
    // getValue(lAttr.attrNameSpaceIdNC) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Namespace Id: ")
        + getValue(lAttr.getNameSpaceIdNC()) + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Steward: ") + getValue(lAttr.steward) +
    // "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Steward: ")
        + getValue(lAttr.getSteward()) + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Class Name: ") + getValueBreak(lAttr.className) +
    // "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Class Name: ") + getClassLink(lParentClass) +
    // "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Class Name: ")
        + getClassLink(lAttr.attrParentClass) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Type: ")
        + getDataTypeLink(lAttr.valueType) + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("                <row>");
    // prDocBook.println(" <entry>" + getPrompt("Minimum Value: ") +
    // getValueReplaceTBDWithNone(lAttr.minimum_value) + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Maximum Value: ") +
    // getValueReplaceTBDWithNone(lAttr.maximum_value) + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Minimum Characters: ") +
    // getValueReplaceTBDWithNone(lAttr.minimum_characters) + "</entry>");
    // prDocBook.println(" <entry>" + getPrompt("Maximum Characters: ") +
    // getValueReplaceTBDWithNone(lAttr.maximum_characters) + "</entry>");

    prDocBook.println("                    <entry>" + getPrompt("Minimum Value: ")
        + getValueReplaceTBDWithNone(lAttr.getMinimumValue(true, false)) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Maximum Value: ")
        + getValueReplaceTBDWithNone(lAttr.getMaximumValue(true, false)) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Minimum Characters: ")
        + getValueReplaceTBDWithNone(lAttr.getMinimumCharacters(true, false)) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Maximum Characters: ")
        + getValueReplaceTBDWithNone(lAttr.getMaximumCharacters(true, false)) + "</entry>");
    prDocBook.println("                </row>");


    prDocBook.println("                <row>");
    prDocBook.println("                    <entry>" + getPrompt("Unit of Measure Type: ")
        + getUnitIdLink(lAttr.unit_of_measure_type) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Default Unit Id: ")
        + getValueReplaceTBDWithNone(lAttr.default_unit_id) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Attribute Concept: ")
        + getValueReplaceTBDWithNone(lAttr.classConcept) + "</entry>");
    prDocBook.println("                    <entry>" + getPrompt("Conceptual Domain: ")
        + getValueReplaceTBDWithNone(lAttr.dataConcept) + "</entry>");
    prDocBook.println("                </row>");

    prDocBook.println("                <row>");
    prDocBook.println(
        "                    <entry>" + getPrompt("Status: ") + lRegistrationStatus + "</entry>");
    prDocBook.println(
        "                    <entry>" + getPrompt("Nillable: ") + lAttr.isNilable + "</entry>");
    // prDocBook.println(" <entry namest=\"c3\" nameend=\"c4\" >" + getPrompt("Pattern: ") +
    // getValueReplaceTBDWithNone(lAttr.pattern) + "</entry>");
    prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" >"
        + getPrompt("Pattern: ") + getValueReplaceTBDWithNone(lAttr.getPattern(true)) + "</entry>");
    prDocBook.println("                </row>");
    prDocBook.println("");

    if (lAttr.domPermValueArr == null || lAttr.domPermValueArr.size() == 0) {
      prDocBook.println("                <row>");
      prDocBook
          .println("                    <entry>" + getPrompt("Permissible Value(s)") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("No Values") + "</entry>");
      prDocBook.println(
          "                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\"></entry>");
      prDocBook.println("                </row>");
    } else {
      prDocBook.println("                <row>");
      prDocBook
          .println("                    <entry>" + getPrompt("Permissible Value(s)") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
      prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
          + getPrompt("Value Meaning") + "</entry>");
      prDocBook.println("                </row>");

      for (DOMProp lProp: lAttr.domPermValueArr) {
        if (!(lProp.domObject instanceof DOMPermValDefn)) {
          continue;
        }
        DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lProp.domObject;
        if (lPermValueDefn.value.compareTo("...") == 0) {
          continue;
        }
        lRegistrationStatusInsert = "";
        if (lPermValueDefn.registrationStatus.compareTo("Retired") == 0) {
          lRegistrationStatusInsert = " - " + DMDocument.Literal_DEPRECATED;
        }

        String lDependValue = lAttr.valueDependencyMap.get(lPermValueDefn.value);
        String lDependClause = "";
        if (lDependValue != null) {
          lDependClause = " (" + lDependValue + ")";
        }

        String lValueMeaning = lPermValueDefn.value_meaning;
        if (lValueMeaning == null) {
          lValueMeaning = "TBD_value_meaning";
        }

        if (lAttr.title.compareTo("pattern") == 0) {
          if ((lValueMeaning == null) || (lValueMeaning.indexOf("TBD") == 0)) {
            lValueMeaning = "";
          }
        }
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry></entry>");
        prDocBook.println(
            "                    <entry>" + getValueAnchor(lAttr, getValue(lPermValueDefn.value))
                + getValueBreak(getValue(lPermValueDefn.value)) + getValue(lDependClause)
                + lRegistrationStatusInsert + "</entry>");
        prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
            + getValue(lValueMeaning) + "</entry>");
        prDocBook.println("                </row>");
      }
    }
    if (!(lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty())) {

      for (PermValueExtDefn lPermValueExt: lAttr.permValueExtArr) {
        if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) {
          continue;
        }
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry>"
            + getPrompt("Extended Value(s) for: " + getValueBreak(lPermValueExt.xpath))
            + "</entry>");
        prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
        prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
            + getPrompt("Value Meaning") + "</entry>");
        prDocBook.println("                </row>");

        for (PermValueDefn lPermValueDefn: lPermValueExt.permValueExtArr) {
          prDocBook.println("                <row>");
          prDocBook.println("                    <entry></entry>");
          prDocBook.println(
              "                    <entry>" + getValueBreak(lPermValueDefn.value) + "</entry>");
          // prDocBook.println(" <entry>" + getValueAnchor(lAttr, lPermValueDefn.value) +
          // getValueBreak (lPermValueDefn.value) + "</entry>");
          prDocBook
              .println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
                  + getValue(lPermValueDefn.value_meaning) + "</entry>");
          prDocBook.println("                </row>");
        }
      }
    }

    prDocBook.println("            </tbody>");
    prDocBook.println("        </tgroup>");
    prDocBook.println("        </informaltable>");
    prDocBook.println("</para>");
    prDocBook.println("</sect1> ");
    prDocBook.println("");
  }

  private void writeDataTypeSection(String lNameSpaceIdNC, PrintWriter prDocBook) {
    ArrayList<String> lPatternArr = new ArrayList<>();
    prDocBook.println("");
    prDocBook
        .println("      <!-- =====================Part4a Begin=========================== -->");
    prDocBook.println("");
    // prDocBook.println(" <chapter>");
    // prDocBook.println(" <title>Data Types in the common namespace.</title>");
    // prDocBook.println(" <para>These classes define the data types. </para>");

    // Sort the data types
    TreeMap<String, DOMClass> sortDataTypeMap = new TreeMap<>();
    for (DOMClass lClass: DOMInfoModel.getMasterDOMClassArr()) {
      if (lClass.isInactive || (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0)
          || !lClass.isDataType) {
        continue;
      }
      sortDataTypeMap.put(lClass.title, lClass);
    }
    ArrayList<DOMClass> sortDataTypeArr = new ArrayList<>(sortDataTypeMap.values());
    if (sortDataTypeArr.size() <= 0) {
      prDocBook.println("");
      prDocBook
          .println("      <!-- ===================== Part4a End=========================== -->");
      return;
    }

    prDocBook.println("    <chapter>");
    prDocBook.println("       <title>Data Types in the common namespace.</title>");
    prDocBook.println("       <para>These classes define the data types. </para>");

    // Write the data types
    String lSchemaBaseType = "None", lMinChar = "None", lMaxChar = "None", lMinVal = "None",
        lMaxVal = "None";
    for (DOMClass lClass: sortDataTypeArr) {
      lSchemaBaseType = "None";
      lMinChar = "None";
      lMaxChar = "None";
      lMinVal = "None";
      lMaxVal = "None";
      lPatternArr = new ArrayList<>();

      for (DOMProp lProp : lClass.ownedAttrArr) {
        DOMAttr lAttr = (DOMAttr) lProp.domObject;
        if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
          lSchemaBaseType = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if (lSchemaBaseType == null) {
            lSchemaBaseType = "None";
          }
          lSchemaBaseType = DMDocument.replaceString(lSchemaBaseType, "xsd:", "");
        }

        if (lAttr.title.compareTo("maximum_characters") == 0) {
          lMaxChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if ((lMaxChar == null || lMaxChar.compareTo("2147483647") == 0)) {
            lMaxChar = "None";
          }
        }

        if (lAttr.title.compareTo("minimum_characters") == 0) {
          lMinChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if ((lMinChar == null || lMinChar.compareTo("-2147483648") == 0)) {
            lMinChar = "None";
          }

        }
        if (lAttr.title.compareTo("maximum_value") == 0) {
          lMaxVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if ((lMaxVal == null || lMaxVal.compareTo("2147483647") == 0)) {
            lMaxVal = "None";
          }

        }
        if (lAttr.title.compareTo("minimum_value") == 0) {
          lMinVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if ((lMinVal == null || lMinVal.compareTo("-2147483648") == 0)) {
            lMinVal = "None";
          }

        }
        if (lProp.title.compareTo("pattern") == 0) {
          String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
          if (lVal != null) {
            // if not null there there are one or more patterns
            for (String lPattern: lAttr.valArr) {
              lPatternArr.add(lPattern);
            }
          }
        }
      }

      prDocBook.println("<sect1>");
      prDocBook
          .println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
      prDocBook.println("");
      prDocBook.println("<para>");
      prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
      prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
      prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <thead>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">"
          + getPrompt("Name: ") + getValue(lClass.title) + "</entry>");
      // prDocBook.println(" <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") +
      // "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Version Id: ")
          + getValue(lClass.versionId) + "</entry>");
      prDocBook.println("                </row>");
      prDocBook.println("            </thead>");
      prDocBook.println("            <tbody>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
          + getPrompt("Description ") + getValue(lClass.definition) + "</entry>");
      prDocBook.println("                </row>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("Schema Base Type:  ")
          + getValueReplaceTBDWithNone(lSchemaBaseType) + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
      prDocBook.println("                </row>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + getPrompt("Minimum Value: ")
          + getValueReplaceTBDWithNone(lMinVal) + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Maximum Value: ")
          + getValueReplaceTBDWithNone(lMaxVal) + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Minimum Characters: ")
          + getValueReplaceTBDWithNone(lMinChar) + "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Maximum Characters: ")
          + getValueReplaceTBDWithNone(lMaxChar) + "</entry>");
      prDocBook.println("                </row>");

      if (lPatternArr == null || lPatternArr.isEmpty()) {
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry>" + "</entry>");
        prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">"
            + getPrompt("No Pattern") + "</entry>");
        prDocBook.println("                </row>");
      } else {
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry>" + "</entry>");
        prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">"
            + getPrompt("Pattern") + "</entry>");
        prDocBook.println("                </row>");
        for (String lPattern: lPatternArr) {
          prDocBook.println("                <row>");
          prDocBook.println("                    <entry>" + "</entry>");
          prDocBook
              .println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">"
                  + getValue(lPattern) + "</entry>");
          prDocBook.println("                </row>");
        }
      }

      prDocBook.println("");
      prDocBook.println("            </tbody>");
      prDocBook.println("        </tgroup>");
      prDocBook.println("        </informaltable>");
      prDocBook.println("</para>");
      prDocBook.println("</sect1> ");
    }
    prDocBook.println("    </chapter>");
    prDocBook.println("");
    prDocBook.println("      <!-- ===================== Part4a End=========================== -->");
    prDocBook.println("");
  }

  private void writeUnitsSection(String lNameSpaceIdNC, PrintWriter prDocBook) {
    ArrayList<String> lPatternArr = new ArrayList<>();
    prDocBook.println("");
    prDocBook
        .println("      <!-- =====================Part4b Begin=========================== -->");
    prDocBook.println("");
    // prDocBook.println(" <chapter>");
    // prDocBook.println(" <title>Units of Measure in the common namespace.</title>");
    // prDocBook.println(" <para>These classes define the units of measure. </para>");

    // get the units
    ArrayList<DOMProp> lDOMPermValueArr;
    TreeMap<String, DOMClass> sortUnitsMap = new TreeMap<>();
    for (DOMClass lClass: DOMInfoModel.getMasterDOMClassArr()) {
      if (lClass.isInactive || (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0)
          || !lClass.isUnitOfMeasure) {
        continue;
      }
      sortUnitsMap.put(lClass.title, lClass);
    }
    ArrayList<DOMClass> sortUnitsArr = new ArrayList<>(sortUnitsMap.values());
    if (sortUnitsArr.size() <= 0) {
      prDocBook
          .println("      <!-- ===================== Part4b End=========================== -->");
      return;
    }

    prDocBook.println("    <chapter>");
    prDocBook.println("       <title>Units of Measure in the common namespace.</title>");
    prDocBook.println("       <para>These classes define the units of measure. </para>");

    for (DOMClass lClass: sortUnitsArr) {
      lDOMPermValueArr = new ArrayList<>();
      for (DOMProp lProp : lClass.allAttrAssocArr) {
        DOMAttr lDOMAttr = (DOMAttr) lProp.domObject;
        if (lDOMAttr.title.compareTo("unit_id") == 0) {
          if (lDOMAttr.domPermValueArr != null) {
            lDOMPermValueArr = lDOMAttr.domPermValueArr;
          }
        }
      }

      prDocBook.println("<sect1>");
      prDocBook
          .println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
      prDocBook.println("");
      prDocBook.println("<para>");
      prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
      prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
      prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
      prDocBook.println("            <thead>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">"
          + getPrompt("Name:  ") + getValue(lClass.title) + "</entry>");
      // prDocBook.println(" <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") +
      // "</entry>");
      prDocBook.println("                    <entry>" + getPrompt("Version Id:  ")
          + getValue(lClass.versionId) + "</entry>");
      prDocBook.println("                </row>");
      prDocBook.println("            </thead>");
      prDocBook.println("            <tbody>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">"
          + getPrompt("Description: ") + getValue(lClass.definition) + "</entry>");
      prDocBook.println("                </row>");
      prDocBook.println("                <row>");
      prDocBook.println("                    <entry>" + "</entry>");
      prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">"
          + getPrompt("Unit Id") + "</entry>");
      prDocBook.println("                </row>");

      if (!lDOMPermValueArr.isEmpty()) {
        for (DOMProp lDOMProp: lDOMPermValueArr) {
          if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMPermValDefn) {
            DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.domObject;
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + "</entry>");
            prDocBook.println(
                "                    <entry>" + getValue(lDOMPermValDefn.value) + "</entry>");
            String lValueMeaning = lDOMPermValDefn.value_meaning;
            if (lValueMeaning == null) {
              lValueMeaning = "TBD_value_meaning";
            }
            prDocBook
                .println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
                    + getValue(lValueMeaning) + "</entry>");
            prDocBook.println("                </row>");
          }
        }
      } else {
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry>" + "</entry>");
        prDocBook.println("                    <entry>" + "None" + "</entry>");
        prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">"
            + getValue("") + "</entry>");
        prDocBook.println("                </row>");
      }

      prDocBook.println("");
      prDocBook.println("            </tbody>");
      prDocBook.println("        </tgroup>");
      prDocBook.println("        </informaltable>");
      prDocBook.println("</para>");
      prDocBook.println("</sect1> ");
    }

    // finalize Part 4
    prDocBook.println("         </chapter>");
    prDocBook.println("");
    prDocBook.println("      <!-- ===================== Part4b End=========================== -->");
    prDocBook.println("");
  }

  private void writeHeader(PrintWriter prDocBook) {
    prDocBook.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    prDocBook.println(
        "<?xml-model href=\"http://www.oasis-open.org/docbook/xml/5.0/rng/docbook.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>");
    prDocBook.println("<book xmlns=\"http://docbook.org/ns/docbook\"");
    prDocBook.println("    xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"5.0\">");
    prDocBook.println("    <info>");
    prDocBook.println("        <title>" + DMDocument.ddDocTitle + "</title>");
    prDocBook.println("        <subtitle>Abridged - Version "
        + DMDocument.getMasterPDSSchemaFileDefn().ont_version_id + "</subtitle>");
    prDocBook.println("        <author>");
    prDocBook.println("            <orgname>" + DMDocument.ddDocTeam + "</orgname>");
    prDocBook.println("        </author>");
    prDocBook.println("        <releaseinfo>Generated from Information Model Version "
        + DMDocument.getMasterPDSSchemaFileDefn().ont_version_id + " on " + DMDocument.sTodaysDate
        + "</releaseinfo>");
    prDocBook.println(
        "        <releaseinfo>Contains namespace ids: " + writtenNamespaceIds + "</releaseinfo>");
    prDocBook.println("        <date>" + DMDocument.sTodaysDate + "</date>");
    prDocBook.println("    </info>");
    prDocBook.println("        ");
    prDocBook.println("        <chapter>");
    prDocBook.println("            <title>Introduction</title>");
    prDocBook.println(
        "            <para>The Data Dictionary defines the organization and components of product labels. Components of a product label include classes and their attributes.</para>");
    prDocBook.println("            <para>");
    prDocBook.println("            </para>");
    prDocBook.println("            <sect1>");
    prDocBook.println("                <title>Audience</title>");
    prDocBook.println(
        "                <para>The Data Dictionary - Abridged - has been abstracted from the unabridged version with the needs of data providers and data end users in mind. It contains full definitions but not all the fine detail or repetition necessary to support the underlying Information Model.</para>");
    prDocBook.println("                <para>");
    prDocBook.println("                </para>");
    prDocBook.println("            </sect1>");
    prDocBook.println("            <sect1>");
    prDocBook.println("                <title>Acknowledgements</title>");
    prDocBook.println(
        "                <para>The Data Dictionary and the Information Model is a joint effort involving discipline experts functioning as a data design working group.</para>");
    prDocBook.println("                <para>");
    prDocBook.println("                </para>");
    prDocBook.println("            </sect1>");
    prDocBook.println("            <sect1>");
    prDocBook.println("                <title>Scope</title>");
    prDocBook.println(
        "                <para>The Data Dictionary defines the common and discipline level classes and attributes used to create product labels. It also defines the meta-attributes (i.e. attributes about attributes) used to define attributes. This abridged version includes only one entry for each attribute where the unabridge version includes an entry for each use of an attribute in a class.</para>");
    prDocBook.println("                <para>");
    prDocBook.println("                </para>");
    prDocBook.println("            </sect1>");
    prDocBook.println("            <sect1>");
    prDocBook.println("                <title>Applicable Documents</title>");
    prDocBook.println("                <para>");
    prDocBook
        .println("                    <emphasis role=\"bold\">Controlling Documents</emphasis>");
    prDocBook.println("");
    prDocBook.println("                    <itemizedlist>");
    prDocBook.println("                        <listitem>");
    prDocBook.println("                            <para>");
    prDocBook.println(
        "                                Information Model Specification - The Information Model is used as the source for class, attribute, and data type definitions. The model is presented in document format as the Information Model Specification.");
    prDocBook.println("                            </para>");
    prDocBook.println("                        </listitem>");
    prDocBook.println("                        <listitem>");
    prDocBook.println("                            <para>");
    prDocBook.println(
        "                                ISO/IEC 11179:3 Registry Metamodel and Basic Attributes Specification, 2003. - The ISO/IEC 11179 specification provides the schema for the data dictionary.");
    prDocBook.println("                            </para>");
    prDocBook.println("                        </listitem>");
    prDocBook.println("                    </itemizedlist>");
    if (!DMDocument.importJSONAttrFlag) {
      prDocBook
          .println("                    <emphasis role=\"bold\">Reference Documents</emphasis>");
      prDocBook.println("                    <itemizedlist>");
      prDocBook.println("                        <listitem>");
      prDocBook.println("                            <para>");
      prDocBook.println(
          "                                Planetary Science Data Dictionary - The online version of the PDS3 data dictionary was used as the source for a few data elements being carried over from the PDS3 data standards.");
      prDocBook.println("                            </para>");
      prDocBook.println("                        </listitem>");
      prDocBook.println("                        ");
      prDocBook.println("                    </itemizedlist>");
    }
    prDocBook.println("                </para>");
    prDocBook.println("            </sect1>");
    prDocBook.println("            <sect1>");
    prDocBook.println("                <title>Terminology</title>");
    prDocBook.println(
        "                <para>This document uses very specific engineering terminology to describe the various structures involved.  It is particularly important that readers who have absorbed the Standards Reference bear in mind that terms which are familiar in that context can have very different meanings in the present document. </para>");
    prDocBook.println(
        "                <para>Following are some definitions of essential terms used throughout this document.</para>");
    prDocBook.println("                <itemizedlist>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>An <emphasis role=\"italic\">attribute</emphasis> is a property or characteristic that provides a unit of information. For example, 'color' and 'length' are possible attributes. </para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">class</emphasis> is a set of attributes (including a name) which defines a family.  A class is generic - a template from which individual members of the family may be constructed.");
    prDocBook.println("                        </para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">conceptual object</emphasis> is an object which is intangible (and, because it is intangible, does not fit into a digital archive).  Examples of 'conceptual objects' include the Cassini mission and NASA's strategic plan for solar system exploration.  Note that a PDF describing the Cassini mission is a digital object, not a conceptual object (nor a component of a conceptual object). </para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">data element</emphasis> is a unit of data for which the definition, identification, representation and <emphasis role=\"italic\">permissible values</emphasis> are specified by means of a set of attributes. For example, the concept of a <emphasis role=\"italic\">calibration_lamp_state_flag</emphasis> is used to indicate whether the lamp used for onboard camera calibration was turned on or off during the capture of an image. The <emphasis role=\"italic\"> data element</emphasis> aspect of this concept is the named attribute (or data element)  <emphasis role=\"italic\">calibration_lamp_state_flag</emphasis>.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">data object</emphasis> is a physical, conceptual, or digital object.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">digital object</emphasis> is an object which is real data - for example, a binary image of a redwood tree or an ASCII table of atmospheric composition versus altitude.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para><emphasis role=\"italic\">Formal</emphasis> as used in the definition of attributes that are names indicates that an established procedure was involved in creating the name.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">unique identifier</emphasis> is a special type of identifier used to provide a reference number which is unique in a context.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para><emphasis role=\"italic\">Local</emphasis> refers to the context within a single label.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para><emphasis role=\"italic\">Logical</emphasis> as used in the definition of logical identifier indicates that the identifier logically groups a set of objects. </para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">physical object</emphasis> is an object which is physical or tangible (and, therefore, does not itself fit into a digital archive).  Examples of 'physical objects' include the planet Saturn and the Venus Express magnetometer.  Note that an ASCII file describing Saturn is a digital object, not a physical object (nor a component of a physical object).  </para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                    <listitem>");
    prDocBook.println(
        "                        <para>A <emphasis role=\"italic\">resource</emphasis> is the target (referent) of any Uniform Resource Identifier; the thing to which a URI points.</para>");
    prDocBook.println("                    </listitem>");
    prDocBook.println("                </itemizedlist>");
    prDocBook.println("");
    prDocBook.println("                <para>");
    prDocBook.println("                </para>");
    prDocBook.println("            </sect1>");
    prDocBook.println("        </chapter>");
    // prDocBook.println(" </part>");
    prDocBook.println("");
    return;
  }

  private void writeFooter(PrintWriter prDocBook) {
    prDocBook.println("");
    prDocBook.println("	</book>");
  }

  private String getPrompt(String lPrompt) {
    // return "<emphasis role=\"italic\">" + "<emphasis role=\"bold\">" + lPrompt + "</emphasis>" +
    // "</emphasis>";
    // return "<emphasis role=\"italic\">" + lPrompt + "</emphasis>";
    return "<emphasis>" + lPrompt + "</emphasis>";
  }

  private String getValue(String lValue) {
    return DOMInfoModel.escapeXMLChar(lValue);
  }

  private String getValueBreak(String lValue) {
    String lValueBreak = DMDocument.replaceString(lValue, "_", "_&#x200B;");
    return lValueBreak;
  }

  private String getValueReplaceTBDWithNone(String lValue) {
    if (lValue.indexOf("TBD") == 0) {
      return "None";
    }
    return DOMInfoModel.escapeXMLChar(lValue);
  }

  private String getValueAnchor(DOMAttr lAttr, String lValue) {
    // String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." +
    // lAttr.attrNameSpaceIdNC + "." + lAttr.parentClassTitle + "." + lAttr.attrNameSpaceIdNC + "."
    // + lAttr.title + "." + lValue;
    // String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." +
    // lAttr.attrNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC +
    // "." + lAttr.title + "." + lValue;
    String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "."
        + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC
        + "." + lAttr.title + "." + lValue;
    int lAnchorI = lAnchor.hashCode();
    lAnchor = "N" + Integer.toString(lAnchorI);
    return "<anchor xml:id=\"" + lAnchor + "\"/>";
  }

  private String getValueLink(DOMAttr lAttr, String lValue) {
    String lAttrParentClassTitle = "TBD_lAttrParentClassTitle";
    if (lAttr.attrParentClass != null) {
      lAttrParentClassTitle = lAttr.attrParentClass.title;
    }
    String lLink =
        DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "."
            + lAttrParentClassTitle + "." + lAttr.nameSpaceIdNC + "." + lAttr.title + "." + lValue;
    int lLinkI = lLink.hashCode();
    lLink = "N" + Integer.toString(lLinkI);
    return "<link linkend=\"" + lLink + "\">" + getValueBreak(lValue) + "</link>";
  }

  private String getAttrAnchor(DOMAttr lAttr) {
    // String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." +
    // lAttr.attrNameSpaceIdNC + "." + lAttr.parentClassTitle + "." + lAttr.attrNameSpaceIdNC + "."
    // + lAttr.title;
    // String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." +
    // lAttr.attrNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC +
    // "." + lAttr.title;
    String lAnchor =
        DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "."
            + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
    int lAnchorI = lAnchor.hashCode();
    lAnchor = "N" + Integer.toString(lAnchorI);
    return "<anchor xml:id=\"" + lAnchor + "\"/>";
  }

  private String getAttrLink(DOMAttr lAttr) {
    String lAttrParentClassTitle = "TBD_lAttrParentClassTitle";
    if (lAttr.attrParentClass != null) {
      lAttrParentClassTitle = lAttr.attrParentClass.title;
    }
    String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC
        + "." + lAttrParentClassTitle + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
    int lLinkI = lLink.hashCode();
    lLink = "N" + Integer.toString(lLinkI);
    String lRegistrationStatusInsert = "";
    if (lAttr.registrationStatus.compareTo("Retired") == 0) {
      lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
    }
    return "<link linkend=\"" + lLink + "\">" + getValueBreak(lAttr.title)
        + lRegistrationStatusInsert + "</link>";
  }

  private String getClassAnchor(DOMClass lClass) {
    String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC
        + "." + lClass.title;
    int lAnchorI = lAnchor.hashCode();
    lAnchor = "N" + Integer.toString(lAnchorI);
    return "<anchor xml:id=\"" + lAnchor + "\"/>";
  }

  private String getClassLink(DOMClass lClass) {
    String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC
        + "." + lClass.title;
    int lLinkI = lLink.hashCode();
    lLink = "N" + Integer.toString(lLinkI);
    String lRegistrationStatusInsert = "";
    if (lClass.registrationStatus.compareTo("Retired") == 0) {
      lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
    }
    return "<link linkend=\"" + lLink + "\">" + getValueBreak(lClass.title)
        + lRegistrationStatusInsert + "</link>";
  }

  private String getDataTypeLink(String lDataType) {
    String lLink = DMDocument.registrationAuthorityIdentifierValue + "."
        + DMDocument.getMasterNameSpaceIdNCLC() + "." + lDataType;
    int lLinkI = lLink.hashCode();
    lLink = "N" + Integer.toString(lLinkI);
    // String lDataTypeWrap = DMDocument.replaceString(lDataType, "_", "_&#x200B;");
    return "<link linkend=\"" + lLink + "\">" + getValueBreak(lDataType) + "</link>";
  }

  private String getUnitIdLink(String lUnitId) {
    if (lUnitId.indexOf("TBD") == 0) {
      return "None";
    }
    String lLink = DMDocument.registrationAuthorityIdentifierValue + "."
        + DMDocument.getMasterNameSpaceIdNCLC() + "." + lUnitId;
    int lLinkI = lLink.hashCode();
    lLink = "N" + Integer.toString(lLinkI);
    return "<link linkend=\"" + lLink + "\">" + lUnitId + "</link>";
  }

  private String getCardinality(int lCardMin, int lCardMax) {
    String pCardMax = "Unbounded";
    if (lCardMax != 9999999) {
      pCardMax = Integer.toString(lCardMax);
    }
    String pCardMin = Integer.toString(lCardMin);
    return pCardMin + ".." + pCardMax;
  }

  // return all classes that reference this class
  private ArrayList<DOMClass> getClassReferences(DOMClass lTargetClass) {
    ArrayList<DOMClass> refClassArr = new ArrayList<>();
    for (DOMClass lClass: DOMInfoModel.getMasterDOMClassArr()) {
      if (lClass.isInactive) {
        continue;
      }
      if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
        for (DOMProp lProp : lClass.ownedAssocArr) {
          DOMClass lCompClass = (DOMClass) lProp.domObject;

          if (lTargetClass == lCompClass) {
            if (!refClassArr.contains(lClass)) {
              refClassArr.add(lClass);
            }
          }
        }
        for (DOMProp lProp : lClass.inheritedAssocArr) {
          DOMClass lCompClass = (DOMClass) lProp.domObject;

          if (lTargetClass == lCompClass) {
            if (!refClassArr.contains(lClass)) {
              refClassArr.add(lClass);
            }
          }
        }
      }
    }


    return refClassArr;
  }

  /**
   * escape certain characters for DocBook
   */
  String escapeDocBookChar(String aString) {
    String lString = aString;
    // lString = replaceString (lString, "\\", "\\\\");
    return lString;
  }

  // locally defined classes for classifying classes and attributes

  public class ClassClassificationDefnDOM {
    String identifier;
    String namespaceId;
    ArrayList<DOMClass> classArr;
    TreeMap<String, DOMClass> classMap;

    public ClassClassificationDefnDOM(String id) {
      identifier = id;
      namespaceId = id;
      classArr = new ArrayList<>();
      classMap = new TreeMap<>();
    }
  }

  public class AttrClassificationDefnDOM {
    String identifier;
    String namespaceId;
    ArrayList<DOMAttr> attrArr;
    TreeMap<String, DOMAttr> attrMap;

    public AttrClassificationDefnDOM(String id) {
      identifier = id;
      namespaceId = id;
      attrArr = new ArrayList<>();
      attrMap = new TreeMap<>();
    }
  }
}
