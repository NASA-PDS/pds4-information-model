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

class WriteDOM11179DDRDFFile extends Object {
  static final String NSID = "kb:";
  static final String VERSION_IDENTIFIER = "versionIdentifier";

  public WriteDOM11179DDRDFFile() {
    return;
  }

  // print the ISO 11179 DD in RDF
  public void printISO11179DDRDF(String todaysDate) throws java.io.IOException {
    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRDF;
    PrintWriter pr11179 =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
    write11179Hdr(todaysDate, pr11179);
    printAttrISODERDF(pr11179);
    printAttrISOCD(pr11179);
    printAttrISODEC(pr11179);
    pr11179.println("</rdf:RDF>");
    pr11179.close();
    return;
  }

  /**
   * Print the ISO Data Element
   */
  private void printAttrISODERDF(PrintWriter pr11179) {
    // print the data elements
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {

      DOMAttr lAttr = i.next();
      if (!(lAttr.isUsedInClass && lAttr.isAttribute)) {
        continue;
      }
      pr11179.println("<" + NSID + "DataElement rdf:about=\"&kb;" + lAttr.deDataIdentifier + "\"");
      pr11179.println("	 " + NSID + "dataIdentifier=\"" + lAttr.deDataIdentifier + "\"");
      pr11179.println("	 " + NSID + VERSION_IDENTIFIER + "=\"" + lAttr.versionIdentifierValue + "\"");
      pr11179.println("	 rdfs:label=\"" + lAttr.deDataIdentifier + "\">");
      pr11179.println(
          "	<" + NSID + "registeredBy rdf:resource=\"&kb;" + lAttr.registeredByValue + "\"/>");
      // pr11179.println(" <" + KB_NS + "typedBy rdf:resource=\"&kb;RC_ZZTEMP\"/>");
      pr11179.println(
          "	<" + NSID + "steward rdf:resource=\"&kb;" + DMDocument.stewardValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "submitter rdf:resource=\"&kb;" + DMDocument.submitterValue + "\"/>");
      pr11179.println("	<" + NSID + "terminologicalEntry rdf:resource=\"&kb;"
          + lAttr.teDataIdentifier + "\"/>");
      pr11179.println("	<" + NSID + "administrationRecord rdf:resource=\"&kb;"
          + lAttr.administrationRecordValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "expressedBy rdf:resource=\"&kb;" + "DEC_" + lAttr.classConcept + "\"/>");
      if (lAttr.isEnumerated) {
        pr11179.println(
            "	<" + NSID + "representing rdf:resource=\"&kb;" + lAttr.evdDataIdentifier + "\"/>");
      } else {
        pr11179.println(
            "	<" + NSID + "representing rdf:resource=\"&kb;" + lAttr.nevdDataIdentifier + "\"/>");
      }
      pr11179.println("	<" + NSID + "registrationAuthorityIdentifier rdf:resource=\"&kb;"
          + lAttr.registrationAuthorityIdentifierValue + "\"/>");
      pr11179.println("</" + NSID + "DataElement>");
      pr11179.println(" ");

      // print VD
      printAttrISOVD(lAttr, pr11179);
      if (lAttr.isEnumerated) {
        printAttrISOPVVM(lAttr, pr11179);
      }

      printAttrISOTermEntry(lAttr, pr11179);
    }
  }

  /**
   * Print the ISO Value Domain
   */
  private void printAttrISOVD(DOMAttr lAttr, PrintWriter pr11179) {

    if (lAttr.isEnumerated) {
      pr11179.println("<" + NSID + "EnumeratedValueDomain rdf:about=\"&pdsns;"
          + lAttr.evdDataIdentifier + "\"");
    } else {
      pr11179.println("<" + NSID + "NonEnumeratedValueDomain rdf:about=\"&pdsns;"
          + lAttr.nevdDataIdentifier + "\"");
    }

    pr11179.println("  " + NSID + "dataIdentifier=\"" + lAttr.dataIdentifier + "\"");
    pr11179.println("  " + NSID + VERSION_IDENTIFIER + "=\"" + lAttr.versionIdentifierValue + "\"");

    pr11179.println("  " + NSID + "maximumCharacterQuantity=\"" + lAttr.maximum_characters + "\"");
    pr11179.println("  " + NSID + "minimumCharacterQuantity=\"" + lAttr.minimum_characters + "\"");
    pr11179.println("  " + NSID + "maximumValue=\"" + lAttr.maximum_value + "\"");
    pr11179.println("  " + NSID + "minimumValue=\"" + lAttr.minimum_value + "\"");
    pr11179.println("  " + NSID + "pattern=\"" + DOMInfoModel.escapeXMLChar(lAttr.pattern) + "\"");
    pr11179.println("  " + NSID + "valueDomainFormat=\"" + lAttr.format + "\"");
    pr11179.println("  " + NSID + "defaultUnitId=\"" + lAttr.default_unit_id + "\"");

    pr11179.println("  rdfs:label=\"" + lAttr.dataIdentifier + "\">");
    pr11179.println(
        "  <" + NSID + "representedBy1 rdf:resource=\"&pdsns;" + lAttr.deDataIdentifier + "\"/>");
    pr11179.println("  <" + NSID + "representedBy2 rdf:resource=\"&pdsns;" + "CD_"
        + lAttr.dataConcept + "\"/>");
    if (lAttr.isEnumerated) {
      for (DOMProp lProp : lAttr.domPermValueArr) {
        DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lProp.domObject;
        int lValueHashCodeI = lPermValueDefn.value.hashCode();
        String lValueHashCodeS = Integer.toString(lValueHashCodeI);
        pr11179.println("  <" + NSID + "containedIn1 rdf:resource=\"&pdsns;"
            + lAttr.pvDataIdentifier + "_" + lValueHashCodeS + "\"/>");
      }
    }
    pr11179.println("  <" + NSID + "datatype rdf:resource=\"&pdsns;" + lAttr.valueType + "\"/>");
    pr11179.println("  <" + NSID + "unitOfMeasure rdf:resource=\"&pdsns;"
        + lAttr.unit_of_measure_type + "\"/>");
    pr11179.println(
        "  <" + NSID + "registeredBy rdf:resource=\"&pdsns;" + lAttr.registeredByValue + "\"/>");
    pr11179.println("  <" + NSID + "steward rdf:resource=\"&pdsns;" + lAttr.steward + "\"/>");
    pr11179.println("  <" + NSID + "submitter rdf:resource=\"&pdsns;" + lAttr.submitter + "\"/>");
    pr11179.println("  <" + NSID + "terminologicalEntry rdf:resource=\"&pdsns;"
        + lAttr.teDataIdentifier + "\"/>");
    pr11179.println("  <" + NSID + "administrationRecord rdf:resource=\"&pdsns;"
        + lAttr.administrationRecordValue + "\"/>");
    pr11179.println("  <" + NSID + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;"
        + lAttr.registrationAuthorityIdentifierValue + "\"/>");
    if (lAttr.isEnumerated) {
      pr11179.println("</" + NSID + "EnumeratedValueDomain>");
    } else {
      pr11179.println("</" + NSID + "NonEnumeratedValueDomain>");
    }
    pr11179.println(" ");
  }

  /**
   * Print the ISO Permissible Value and Value Meaning
   */
  private void printAttrISOPVVM(DOMAttr lAttr, PrintWriter pr11179) {
    for (DOMProp lProp : lAttr.domPermValueArr) {
      // for each standard value
      DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lProp.domObject;

      int lValueHashCodeI = lPermValueDefn.value.hashCode();
      String lValueHashCodeS = Integer.toString(lValueHashCodeI);
      // if (lAttr.title.compareToIgnoreCase("pattern") == 0) {
      // System.out.println("in class 4444444 "+ lAttr.parentClassTitle);
      // System.out.println("7777777777777- value_meaning is "+ lPermValueDefn.value_meaning);
      // System.out.println("8888888888- value = "+ lPermValueDefn.value + "XXX");
      // }
      String lMeaningHashCodeS = null;
      if (lPermValueDefn.value_meaning != null) {
        int lMeaningHashCodeI = lPermValueDefn.value_meaning.hashCode();
        lMeaningHashCodeS = Integer.toString(lMeaningHashCodeI);
      }

      // Permissible Values
      pr11179.println("<" + NSID + "PermissibleValue rdf:about=\"&pdsns;" + lAttr.pvDataIdentifier
          + "_" + lValueHashCodeS + "\"");
      pr11179.println("	 " + NSID + "beginDate=\"" + lPermValueDefn.value_begin_date + "\"");
      pr11179.println("	 " + NSID + "endDate=\"" + lPermValueDefn.value_end_date + "\"");
      pr11179.println("	 " + NSID + "value=\"" + lPermValueDefn.value + "\"");
      pr11179.println("	 rdfs:label=\"" + lAttr.pvDataIdentifier + "_" + lValueHashCodeS + "\">");
      pr11179.println(
          "	<" + NSID + "containing1 rdf:resource=\"&pdsns;" + lAttr.evdDataIdentifier + "\"/>");
      pr11179.println("	<" + NSID + "usedIn rdf:resource=\"&pdsns;" + lAttr.vmDataIdentifier + "_"
          + lMeaningHashCodeS + "\"/>");
      pr11179.println("</" + NSID + "PermissibleValue>");
      pr11179.println(" ");

      // Value Meanings
      pr11179.println("<" + NSID + "ValueMeaning rdf:about=\"&pdsns;" + lAttr.vmDataIdentifier + "_"
          + lMeaningHashCodeS + "\"");
      pr11179.println("	 " + NSID + "beginDate=\"" + lPermValueDefn.value_begin_date + "\"");
      pr11179.println("	 " + NSID + "description=\""
          + DOMInfoModel.escapeXMLChar(lPermValueDefn.value_meaning) + "\"");
      pr11179.println("	 " + NSID + "endDate=\"" + lPermValueDefn.value_end_date + "\"");
      pr11179.println(
          "	 rdfs:label=\"" + lAttr.vmDataIdentifier + "_" + lMeaningHashCodeS + "\"/>");
      pr11179.println(" ");
    }
  }

  /**
   * Print the ISO Terminological Entry
   */
  private void printAttrISOTermEntry(DOMAttr lAttr, PrintWriter pr11179) {

    // Designation
    pr11179.println(
        "<" + NSID + "Designation rdf:about=\"" + NSID + "" + lAttr.desDataIdentifier + "\"");
    pr11179.println("	 " + NSID + "designationName=\"" + lAttr.dataIdentifier + "\"");
    // pr11179.println(" " + KB_NS + "isPreferred=\"" + lAttr.desIsPreferred + "\"");
    pr11179.println("	 " + NSID + "isPreferred=\"" + "TRUE" + "\"");
    pr11179.println("	 rdfs:label=\"" + lAttr.desDataIdentifier + "\"/>");
    pr11179.println(" ");

    // Definition
    pr11179.println("<" + NSID + "Definition rdf:about=\"&pdsns;" + lAttr.defDataIdentifier + "\"");
    // pr11179.println(" " + KB_NS + "isPreferred=\"" + lTE.defIsPreferred + "\"");
    pr11179.println("	 " + NSID + "isPreferred=\"" + "TRUE" + "\"");
    pr11179.println("	 rdfs:label=\"" + lAttr.defDataIdentifier + "\">");
    pr11179.println("	<" + NSID + "definitionText>" + DOMInfoModel.escapeXMLChar(lAttr.definition)
        + "</" + NSID + "definitionText>");
    pr11179.println("</" + NSID + "Definition>");
    pr11179.println(" ");

    // Language Section
    pr11179.println(
        "<" + NSID + "LanguageSection rdf:about=\"&pdsns;" + lAttr.lsDataIdentifier + "\"");
    pr11179.println("	 rdfs:label=\"" + lAttr.lsDataIdentifier + "\">");
    pr11179.println(
        "	<" + NSID + "definition rdf:resource=\"&pdsns;" + lAttr.defDataIdentifier + "\"/>");
    pr11179.println(
        "	<" + NSID + "designation rdf:resource=\"&pdsns;" + lAttr.desDataIdentifier + "\"/>");
    pr11179.println("	<" + NSID + "sectionLanguage rdf:resource=\"&pdsns;" + "Englsh" + "\"/>");
    pr11179.println("</" + NSID + "LanguageSection>");
    pr11179.println(" ");

    // Terminological Entry
    pr11179.println(
        "<" + NSID + "TerminologicalEntry rdf:about=\"&pdsns;" + lAttr.teDataIdentifier + "\"");
    pr11179.println("  rdfs:label=\"" + lAttr.teDataIdentifier + "\">");
    pr11179.println(
        "  <" + NSID + "partioning rdf:resource=\"&pdsns;" + lAttr.lsDataIdentifier + "\"/>");
    pr11179.println("  <" + NSID + "administeredItemContext rdf:resource=\"&pdsns;NASA_PDS\"/>");
    pr11179.println("</" + NSID + "TerminologicalEntry>");
    pr11179.println(" ");
  }

  /**
   * Print the ISO Conceptual Domains
   */
  private void printAttrISOCD(PrintWriter pr11179) {
    ArrayList<DOMIndexDefn> lCDAttrArr = new ArrayList<>(DOMInfoModel.cdDOMAttrMap.values());
    for (Iterator<DOMIndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
      DOMIndexDefn lIndexDefn = i.next();

      String lCDId = "CD" + "." + lIndexDefn.identifier;
      pr11179.println("<" + NSID + "ConceptualDomain rdf:about=\"&pdsns;" + lCDId + "\"");
      pr11179.println("	 " + NSID + "dataIdentifier=\"" + lIndexDefn.identifier + "\"");
      pr11179.println("	 " + NSID + VERSION_IDENTIFIER + "=\"" + DMDocument.VERSION_ID_DEFAULT + "\"");
      pr11179.println("	 rdfs:label=\"" + lIndexDefn.identifier + "\">");

      for (Iterator<String> j = lIndexDefn.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lVal = j.next();
        pr11179.println("	<" + NSID + "having rdf:resource=\"&pdsns;" + "DEC." + lVal + "\"/>");
      }
      for (Iterator<DOMAttr> j = lIndexDefn.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        if (lAttr.isEnumerated) {
          pr11179.println("	<" + NSID + "representing2 rdf:resource=\"&pdsns;"
              + lAttr.evdDataIdentifier + "\"/>");
        } else {
          pr11179.println("	<" + NSID + "representing2 rdf:resource=\"&pdsns;"
              + lAttr.nevdDataIdentifier + "\"/>");
        }
      }

      pr11179.println("	<" + NSID + "administrationRecord rdf:resource=\"&pdsns;"
          + DMDocument.administrationRecordValue + "\"/>");
      pr11179.println("	<" + NSID + "registeredBy rdf:resource=\"&pdsns;"
          + DMDocument.registeredByValue + "\"/>");
      pr11179.println("	<" + NSID + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;"
          + DMDocument.registrationAuthorityIdentifierValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "steward rdf:resource=\"&pdsns;" + DMDocument.stewardValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "submitter rdf:resource=\"&pdsns;" + DMDocument.submitterValue + "\"/>");
      pr11179.println("	<" + NSID + "terminologicalEntry rdf:resource=\"&pdsns;" + "TE."
          + lIndexDefn.identifier + "\"/>");
      pr11179.println("</" + NSID + "ConceptualDomain>");
      pr11179.println(" ");
    }
  }

  /**
   * Print the ISO Data Element Concepts
   */
  private void printAttrISODEC(PrintWriter pr11179) {
    ArrayList<DOMIndexDefn> lDECAttrArr = new ArrayList<>(DOMInfoModel.decDOMAttrMap.values());
    for (Iterator<DOMIndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
      DOMIndexDefn lIndexDefn = i.next();

      String lDECId = "DEC" + "." + lIndexDefn.identifier;
      pr11179.println("<" + NSID + "DataElementConcept rdf:about=\"&pdsns;" + lDECId + "\"");
      pr11179.println("	 " + NSID + "dataIdentifier=\"" + lIndexDefn.identifier + "\"");
      pr11179.println("	 " + NSID + VERSION_IDENTIFIER + "=\"" + DMDocument.VERSION_ID_DEFAULT + "\"");
      pr11179.println("	 rdfs:label=\"" + lIndexDefn.identifier + "\">");

      for (Iterator<DOMAttr> j = lIndexDefn.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        pr11179.println(
            "	<" + NSID + "expressing rdf:resource=\"&pdsns;" + lAttr.deDataIdentifier + "\"/>");
      }

      for (Iterator<String> j = lIndexDefn.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lVal = j.next();
        pr11179
            .println("	<" + NSID + "specifying rdf:resource=\"&pdsns;" + "CD." + lVal + "\"/>");
      }

      /*
       * for (Iterator <String> i = lCD.containedIn2Arr.iterator(); i.hasNext();) { String lval =
       * (String) i.next(); pr11179.println("	<" + KB_NS + "containedIn2 rdf:resource=\"&pdsns;" +
       * lval + "\"/>"); }
       */
      pr11179.println("	<" + NSID + "administrationRecord rdf:resource=\"&pdsns;"
          + DMDocument.administrationRecordValue + "\"/>");
      pr11179.println("	<" + NSID + "registeredBy rdf:resource=\"&pdsns;"
          + DMDocument.registeredByValue + "\"/>");
      pr11179.println("	<" + NSID + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;"
          + DMDocument.registrationAuthorityIdentifierValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "steward rdf:resource=\"&pdsns;" + DMDocument.stewardValue + "\"/>");
      pr11179.println(
          "	<" + NSID + "submitter rdf:resource=\"&pdsns;" + DMDocument.submitterValue + "\"/>");
      pr11179.println("	<" + NSID + "terminologicalEntry rdf:resource=\"&pdsns;" + "TE."
          + lIndexDefn.identifier + "\"/>");
      pr11179.println("</" + NSID + "DataElementConcept>");
      pr11179.println(" ");
    }
  }

  // write the ISO/IEC 11179 Header
  public void write11179Hdr(String todaysDate, PrintWriter pr11179) {
    pr11179.println("<?xml version='1.0' encoding='UTF-8'?>");
    pr11179.println(
        "<!-- PDS4 Planetary Science Data Dictionary - Master Model Dump - " + todaysDate + " -->");
    pr11179.println("<!DOCTYPE rdf:RDF [");
    pr11179.println("	 <!ENTITY rdf 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'>");
    pr11179.println("	 <!ENTITY kb 'http://protege.stanford.edu/kb#'>");
    pr11179.println("	 <!ENTITY pdsns 'http://pds.nasa.gov/pds4/pds/v1#'>");
    pr11179.println("	 <!ENTITY rdfs 'http://www.w3.org/2000/01/rdf-schema#'>");
    pr11179.println("]>");
    pr11179.println("<rdf:RDF xmlns:rdf=\"&rdf;\"");
    pr11179.println("	 xmlns:kb=\"&kb;\"");
    pr11179.println("	 xmlns:rdfs=\"&rdfs;\">");
    pr11179.println(" ");

    pr11179.println(" ");
  }
}
