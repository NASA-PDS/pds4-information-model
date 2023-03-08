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
 * Writes the PDS4 DD content to a RDF/OWL file using DOM
 * 
 */

class WriteDOMRDFOWLFile extends Object {
  ArrayList<DOMAttr> gDOMAttrArr = new ArrayList<>();
  ArrayList<String> gClassIdArr = new ArrayList<>();
  ArrayList<String> gAttrIdArr = new ArrayList<>();
  ArrayList<String> gPermValIdArr = new ArrayList<>();
  ArrayList<String> gDataTypeIdArr = new ArrayList<>();
  ArrayList<String> gUnitIdArr = new ArrayList<>();
  ArrayList<String> adminRecUsedArr, adminRecTitleArr;
  // ArrayList <String> topLevelClassArr;
  PrintWriter prDDPins;

  public WriteDOMRDFOWLFile() {
    /*
     * topLevelClassArr = new ArrayList <String> (); topLevelClassArr.add("Data_Object");
     * topLevelClassArr.add("Tagged_Digital_Object"); topLevelClassArr.add("Tagged_Digital_Child");
     * topLevelClassArr.add("Tagged_NonDigital_Object");
     * topLevelClassArr.add("Tagged_NonDigital_Child"); topLevelClassArr.add("Data_Type");
     * topLevelClassArr.add("Unit_Of_Measure"); topLevelClassArr.add("Product");
     * topLevelClassArr.add("Product_Components");
     */
    return;
  }

  // write the RDF/OWL file
  public void writeOWLFile(SchemaFileDefn schemaFileDefn) throws java.io.IOException {
	String lFileName = schemaFileDefn.relativeFileSpecOWLRDF_DOM;
    prDDPins =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));
    printPDDPHdr();
    printPDDPBody(schemaFileDefn);
    printPDDPFtr();
    prDDPins.close();
  }

  // Print the RDF/OWL Header
  public void printPDDPHdr() {
    prDDPins.println("<?xml version=\"1.0\"?>");
    prDDPins.println("<rdf:RDF");
    prDDPins.println("   xmlns=\"http://pds.nasa.gov/ontologies/1700/pds/\"");
    prDDPins.println("   xml:base=\"http://pds.nasa.gov/ontologies/1700/pds/\"");
    prDDPins.println("   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
    prDDPins.println("   xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
    prDDPins.println("   xmlns:owl=\"http://www.w3.org/2002/07/owl#\"");

    prDDPins.println("   xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"");
    prDDPins.println("   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"");
    prDDPins.println("   xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"");
    prDDPins.println("   xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"");

    prDDPins.println("   xmlns:dc=\"http://purl.org/dc/elements/1.1/\">");
    // prDDPins.println(" xmlns:pds=\"http://pds.nasa.gov/pds4/pds\">");
    prDDPins.println(" ");
    prDDPins.println("   <owl:Ontology rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/\">");
    prDDPins.println("      <dc:title>PDS4 Information Model V"
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:title>");
    prDDPins.println("      <dc:description>The ontology for the PDS4 Information Model V"
        + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:description>");
    prDDPins.println("   </owl:Ontology>");
    prDDPins.println(" ");
    prDDPins.println(" ");

    prDDPins.println("   <!-- ");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("   //");
    prDDPins.println("   // Object Properties");
    prDDPins.println("   //");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("      -->");
    prDDPins.println(" ");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_a -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\">");
    prDDPins.println(
        "      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#TransitiveProperty\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_attribute -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_attribute\">");
    prDDPins.println(
        "      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value\">");
    prDDPins.println(
        "      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_class -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_class\">");
    prDDPins.println(
        "      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_data_type -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_data_type\">");
    prDDPins.println(
        "      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");

    prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit -->");
    prDDPins.println(" ");
    prDDPins.println(
        "   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit\">");
    prDDPins.println(
        "      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
    prDDPins.println("   </owl:ObjectProperty>");
    prDDPins.println(" ");

    /*
     * prDDPins.println("   <!-- PDS4 Disjoints: http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.___Top Level___ -->");
     * prDDPins.println("   <rdf:Description>"); prDDPins.
     * println("      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AllDisjointClasses\"/>"
     * ); prDDPins.println("      <owl:members rdf:parseType=\"Collection\">");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".all.USER\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Data_Object\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Data_Type\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Product\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Product_Components\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Tagged_Digital_Child\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Tagged_Digital_Object\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Tagged_NonDigital_Child\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Tagged_NonDigital_Object\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Unit_Of_Measure\"/>");
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Attribute\"/>"); //
     * prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#" +
     * DMDocument.registrationAuthorityIdentifierValue + ".pds.Permissible_Value\"/>");
     * prDDPins.println("      </owl:members>"); prDDPins.println("   </rdf:Description>");
     */

    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println("   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Attribute" + " -->");
    prDDPins.println(" ");
    prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Attribute" + "\">");
    prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".all.USER" + "\"/>");
    prDDPins.println("      <dc:title>" + "Attribute" + "</dc:title>");
    prDDPins.println("      <dc:description>"
        + "The Attribute class is the parent of all PDS4 attributes." + "</dc:description>");
    prDDPins.println("      <rdfs:label>" + DMDocument.registrationAuthorityIdentifierValue
        + ".pds.Attribute" + "</rdfs:label>");
    prDDPins.println("      <rdfs:comment>The definition of class "
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Attribute" + ".</rdfs:comment>");
    prDDPins.println("   </owl:Class>");

    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println("   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Permissible_Value" + " -->");
    prDDPins.println(" ");
    prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Permissible_Value" + "\">");
    prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".all.USER" + "\"/>");
    prDDPins.println("      <dc:title>" + "Permissible_Value" + "</dc:title>");
    prDDPins.println("      <dc:description>"
        + "The Permissible_Value class is the parent of all PDS4 Permissible Values."
        + "</dc:description>");
    prDDPins.println("      <rdfs:label>" + DMDocument.registrationAuthorityIdentifierValue
        + ".pds.Permissible_Value" + "</rdfs:label>");
    prDDPins.println("      <rdfs:comment>The definition of class "
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Permissible_Value"
        + ".</rdfs:comment>");
    prDDPins.println("   </owl:Class>");
  }

  // Print the RDF/OWL Footer
  public void printPDDPFtr() {
    prDDPins.println("</rdf:RDF>");
  }

  // print the RDF/OWL body
  public void printPDDPBody(SchemaFileDefn schemaFileDefn) {
    prDDPins.println(" ");
    prDDPins.println("   <!-- ");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("   //");
    prDDPins.println("   // Object Classes");
    prDDPins.println("   //");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("      -->");
    printClasses(schemaFileDefn, prDDPins);

    prDDPins.println(" ");
    prDDPins.println("   <!-- ");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("   //");
    prDDPins.println("   // Object Attributes");
    prDDPins.println("   //");
    prDDPins.println(
        "   ///////////////////////////////////////////////////////////////////////////////////////");
    prDDPins.println("      -->");

    printAttributes(prDDPins);
    // 777 printDisjoints ("Attributes", gAttrIdArr, prDDPins);
    // printDisjoints ("Permanent Values", gPermValIdArr, prDDPins);

    printDataTypes(prDDPins);
    printUnits(prDDPins);
  }

  // write the PDS4 classes as OWL classes
  public void printClasses(SchemaFileDefn schemaFileDefn, PrintWriter prDDPins) {
    ArrayList<DOMClass> lDOMClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassIdMap.values());
    for (DOMClass lDOMClass : lDOMClassArr) {
      if (lDOMClass.identifier.indexOf("PDS3") > -1) continue;
      if (lDOMClass.nameSpaceIdNC.compareTo(schemaFileDefn.nameSpaceIdNCLC) != 0) continue;
      if (lDOMClass.steward.compareTo("ops") == 0) continue;
      printClass(lDOMClass, prDDPins);
    }
  }
  

  // write the PDS4 classes as OWL classes
/*  public void printClasses(PrintWriter prDDPins) {
    ArrayList<DOMClass> lDOMClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassIdMap.values());
    for (Iterator<DOMClass> i = lDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lDOMClass = i.next();
      // if (lDOMClass.registrationStatus.compareTo("Retired") == 0) continue;
      // if (lDOMClass.isDataType) continue;
      // if (lDOMClass.isUnitOfMeasure) continue;
      // if (lDOMClass.isUSERClass) continue;
      // if (lDOMClass.title.compareTo("Product_Components") == 0) continue;
      if ((lDOMClass.identifier.indexOf("PDS3") > -1)
          || !((lDOMClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
              || lDOMClass.nameSpaceIdNC.compareTo("all") == 0)
              && (lDOMClass.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                  || lDOMClass.steward.compareTo("ops") == 0))) {
        continue;
      }
      printClass(lDOMClass, prDDPins);
    }
  } */
  

  // write a single PDS4 classes as an OWL class
  public void printClass(DOMClass lDOMClass, PrintWriter prDDPins) {
    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println(
        "   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#" + lDOMClass.identifier + " -->");
    prDDPins.println(" ");

    // write PDS4 class as an owl:Class definition
    prDDPins.println(
        "   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMClass.identifier + "\">");

    // write the superclass as a property
    if (lDOMClass.subClassOf != null && lDOMClass.subClassOf.title.indexOf("TBD") != 0) {
      prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
          + lDOMClass.subClassOf.identifier + "\"/>");
    }

    prDDPins.println("      <dc:title>" + lDOMClass.title + "</dc:title>");
    prDDPins
        .println("      <dc:description>" + formValue(lDOMClass.definition) + "</dc:description>");

    prDDPins.println("      <rdfs:label>" + lDOMClass.identifier + "</rdfs:label>");
    prDDPins.println(
        "      <rdfs:comment>The definition of class " + lDOMClass.identifier + ".</rdfs:comment>");

    // write the PDS4 attributes as "has_attribute" properties on PDS4 classes (assume PDS4
    // attributes are defined as classes.
    for (Iterator<DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        gDOMAttrArr.add(lDOMAttr);
        gAttrIdArr.add(lDOMAttr.identifier);
        prDDPins.println("      <rdfs:subClassOf>");
        prDDPins.println("         <owl:Restriction>");
        prDDPins.println(
            "            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_attribute\"/>");
        prDDPins
            .println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
                + lDOMAttr.identifier + "\"/>");
        prDDPins.println("         </owl:Restriction>");
        prDDPins.println("      </rdfs:subClassOf>");
      }
    }

    // write the PDS4 associations as "has_class" properties on PDS4 classes
    for (Iterator<DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
        DOMClass lDOMClass2 = (DOMClass) lDOMProp.hasDOMObject;
        if ((lDOMClass2.identifier.indexOf("PDS3") > -1)
            || !((lDOMClass2.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                || lDOMClass2.nameSpaceIdNC.compareTo("all") == 0)
                && (lDOMClass2.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                    || lDOMClass2.steward.compareTo("ops") == 0))) {
          continue;
        }
        prDDPins.println("      <rdfs:subClassOf>");
        prDDPins.println("         <owl:Restriction>");
        prDDPins.println(
            "            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_class\"/>");
        prDDPins
            .println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
                + lDOMClass2.identifier + "\"/>");
        prDDPins.println("         </owl:Restriction>");
        prDDPins.println("      </rdfs:subClassOf>");
      }
    }
    prDDPins.println("   </owl:Class>");

    // write the disjoints (subclasses) for a single class
    gClassIdArr = new ArrayList<>();
    if (lDOMClass.subClassHierArr.size() > 1) {
      for (Iterator<DOMClass> i = lDOMClass.subClassHierArr.iterator(); i.hasNext();) {
        DOMClass lDisjointDOMClass = i.next();
        if ((lDisjointDOMClass.identifier.indexOf("PDS3") > -1)
            || !((lDisjointDOMClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                || lDisjointDOMClass.nameSpaceIdNC.compareTo("all") == 0)
                && (lDisjointDOMClass.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                    || lDisjointDOMClass.steward.compareTo("ops") == 0))) {
          continue;
        }
        gClassIdArr.add(lDisjointDOMClass.identifier);
      }
      // 777 printDisjoints (lDOMClass.identifier, gClassIdArr, prDDPins);
    }
  }

  // write the PDS4 attributes as OWL classes
  public void printAttributes(PrintWriter prDDPins) {
    for (Iterator<DOMAttr> i = gDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lDOMAttr = i.next();
      if (lDOMAttr.title.indexOf("ANAME") > -1) {
        continue;
      }
      printAttr(lDOMAttr, prDDPins);
      gAttrIdArr.add(lDOMAttr.identifier);
    }
  }

  // write a single PDS4 attribute as an OWL class
  public void printAttr(DOMAttr lDOMAttr, PrintWriter prDDPins) {
    ArrayList<DOMPermValDefn> lDOMClassAttrPermValArr = new ArrayList<>();
    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println(
        "   <!-- PDS4 Attribute: http://pds.nasa.gov/pds4/pds#" + lDOMAttr.identifier + " -->");
    prDDPins.println(" ");

    // write statement to define PDS4 attribute as an owl:Class definition
    prDDPins.println(
        "   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMAttr.identifier + "\">");
    prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Attribute" + "\"/>");

    prDDPins.println("      <dc:title>" + lDOMAttr.title + "</dc:title>");
    prDDPins
        .println("      <dc:description>" + formValue(lDOMAttr.definition) + "</dc:description>");

    prDDPins.println("      <rdfs:label>" + lDOMAttr.identifier + "</rdfs:label>");
    prDDPins.println(
        "      <rdfs:comment>The definition of class " + lDOMAttr.identifier + ".</rdfs:comment>");

    // write the permissible values as properties
    for (Iterator<DOMProp> i = lDOMAttr.domPermValueArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
        DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
        if (lDOMPermValDefn.identifier.indexOf("PDS3") > -1) {
          if (lDOMPermValDefn.identifier.indexOf("Product") > -1) {
            continue;
          }
        }
        if (!((lDOMPermValDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
            || lDOMPermValDefn.nameSpaceIdNC.compareTo("all") == 0)
            && (lDOMPermValDefn.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
                || lDOMPermValDefn.steward.compareTo("ops") == 0))) {
          continue;
        }
        // prDDPins.println(" <rdfs:subClassOf>");
        // prDDPins.println(" <owl:Restriction>");
        // prDDPins.println(" <owl:onProperty
        // rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value\"/>");
        // prDDPins.println(" <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" +
        // lDOMPermValDefn.identifier + "\"/>");
        // prDDPins.println(" </owl:Restriction>");
        // prDDPins.println(" </rdfs:subClassOf>");
        lDOMClassAttrPermValArr.add(lDOMPermValDefn);
        gPermValIdArr.add(lDOMPermValDefn.identifier);
      }
    }

    // write the "has_data_type properties
    String lIdentifier =
        DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lDOMAttr.valueType);
    DOMDataType lDOMDataType = DOMInfoModel.masterDOMDataTypeMap.get(lIdentifier);
    if (lDOMDataType != null) {
      prDDPins.println("      <rdfs:subClassOf>");
      prDDPins.println("         <owl:Restriction>");
      prDDPins.println(
          "            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_data_type\"/>");
      prDDPins
          .println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
              + lDOMDataType.identifier + "\"/>");
      prDDPins.println("         </owl:Restriction>");
      prDDPins.println("      </rdfs:subClassOf>");
    }

    // write the "has_measurement_units properties
    lIdentifier = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC,
        lDOMAttr.unit_of_measure_type);
    DOMUnit lDOMUnit = DOMInfoModel.masterDOMUnitMap.get(lIdentifier);
    if (lDOMUnit != null) {
      prDDPins.println("      <rdfs:subClassOf>");
      prDDPins.println("         <owl:Restriction>");
      prDDPins.println(
          "            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit\"/>");
      prDDPins
          .println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
              + lDOMUnit.identifier + "\"/>");
      prDDPins.println("         </owl:Restriction>");
      prDDPins.println("      </rdfs:subClassOf>");
    }
    prDDPins.println("   </owl:Class>");

    // write the permissible values as OWL classes
    for (Iterator<DOMPermValDefn> j = lDOMClassAttrPermValArr.iterator(); j.hasNext();) {
      DOMPermValDefn lDOMPermValDefn = j.next();
      printPermValue(lDOMPermValDefn, prDDPins);
    }
  }

  // write a single permissible value as a OWL class
  public void printPermValue(DOMPermValDefn lDOMPermValDefn, PrintWriter prDDPins) {
    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println("   <!-- PDS4 Permissible Value: http://pds.nasa.gov/pds4/pds#"
        + lDOMPermValDefn.identifier + " -->");
    prDDPins.println(" ");

    // write statement to define PDS4 permissible value as an owl:Class definition
    prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#"
        + lDOMPermValDefn.identifier + "\">");
    prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#"
        + DMDocument.registrationAuthorityIdentifierValue + ".pds.Permissible_Value" + "\"/>");
    prDDPins.println("      <dc:title>" + lDOMPermValDefn.value + "</dc:title>");
    prDDPins.println(
        "      <dc:description>" + formValue(lDOMPermValDefn.value_meaning) + "</dc:description>");
    prDDPins.println("      <rdfs:label>" + lDOMPermValDefn.identifier + "</rdfs:label>");
    prDDPins.println("      <rdfs:comment>The definition of class " + lDOMPermValDefn.identifier
        + ".</rdfs:comment>");
    prDDPins.println("   </owl:Class>");
    prDDPins.println(" ");
  }

  // write the Data Types as OWL classes
  public void printDataTypes(PrintWriter prDDPins) {
    for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
      DOMDataType lDOMDataType = i.next();
      gDataTypeIdArr.add(lDOMDataType.identifier);
      prDDPins.println(" ");
      prDDPins.println(" ");
      prDDPins.println("   <!-- PDS4 Data Type: http://pds.nasa.gov/pds4/pds#"
          + lDOMDataType.identifier + " -->");
      prDDPins.println(" ");

      // write statement to define PDS4 data type as an owl:Class definition
      prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#"
          + lDOMDataType.identifier + "\">");
      prDDPins.println("      <dc:title>" + lDOMDataType.title + "</dc:title>");
      prDDPins.println(
          "      <dc:description>" + formValue(lDOMDataType.definition) + "</dc:description>");
      prDDPins.println("      <rdfs:label>" + lDOMDataType.identifier + "</rdfs:label>");
      prDDPins.println("      <rdfs:comment>The definition of class " + lDOMDataType.identifier
          + ".</rdfs:comment>");
      prDDPins.println("   </owl:Class>");
    }
  }

  // write the Units as OWL classes
  public void printUnits(PrintWriter prDDPins) {
    for (Iterator<DOMUnit> i = DOMInfoModel.masterDOMUnitArr.iterator(); i.hasNext();) {
      DOMUnit lDOMUnit = i.next();
      gUnitIdArr.add(lDOMUnit.identifier);
      prDDPins.println(" ");
      prDDPins.println(" ");
      prDDPins.println(
          "   <!-- PDS4 Data Type: http://pds.nasa.gov/pds4/pds#" + lDOMUnit.identifier + " -->");
      prDDPins.println(" ");

      // write statement to define PDS4 data type as an owl:Class definition
      prDDPins.println(
          "   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMUnit.identifier + "\">");
      prDDPins.println("      <dc:title>" + lDOMUnit.title + "</dc:title>");
      prDDPins
          .println("      <dc:description>" + formValue(lDOMUnit.definition) + "</dc:description>");
      prDDPins.println("      <rdfs:label>" + lDOMUnit.identifier + "</rdfs:label>");
      prDDPins.println("      <rdfs:comment>The definition of class " + lDOMUnit.identifier
          + ".</rdfs:comment>");
      prDDPins.println("   </owl:Class>");
    }
  }

  // write a set of disjoints
  public void printDisjoints(String lTitle, ArrayList<String> lClassIdArr, PrintWriter prDDPins) {
    prDDPins.println(" ");
    prDDPins.println(" ");
    prDDPins.println(
        "   <!-- PDS4 Disjoints: http://pds.nasa.gov/pds4/pds#" + "___" + lTitle + "___" + " -->");
    prDDPins.println(" ");
    prDDPins.println("   <rdf:Description>");
    prDDPins.println(
        "      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AllDisjointClasses\"/>");
    prDDPins.println("      <owl:members rdf:parseType=\"Collection\">");

    // write the subclasses
    for (Iterator<String> i = lClassIdArr.iterator(); i.hasNext();) {
      String lClassId = i.next();
      prDDPins.println("         <rdf:Description rdf:about=\"" + "http://pds.nasa.gov/pds4/pds#"
          + lClassId + "\"/>");
    }
    prDDPins.println("      </owl:members>");
    prDDPins.println("   </rdf:Description>");
  }

  // Print the Associations
  public void printAssoc(DOMClass lClass, PrintWriter prDDPins) {
    boolean isFirst = true;
    if (lClass.allAttrAssocArr.isEmpty()) {
      return;
    }
    prDDPins.println("              , " + formValue("associationList") + ": [");
    for (Iterator<DOMProp> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        if (isFirst) {
          prDDPins.println("             {" + formValue("association") + ": {");
          isFirst = false;
        } else {
          prDDPins.println("           , {" + formValue("association") + ": {");
        }
        prDDPins.println("                " + formValue("identifier") + ": "
            + formValue(lDOMAttr.identifier) + " ,");
        prDDPins.println(
            "                " + formValue("title") + ": " + formValue(lDOMAttr.title) + " ,");
        prDDPins.println("                " + formValue("isAttribute") + ": "
            + formBooleanValue(lDOMAttr.isAttribute) + " ,");
        prDDPins.println("                " + formValue("minimumCardinality") + ": "
            + formValue(lDOMAttr.cardMin) + " ,");
        prDDPins.println("                " + formValue("maximumCardinality") + ": "
            + formValue(lDOMAttr.cardMax));
        prDDPins.println("              }");
        prDDPins.println("            }");
      }
    }
    prDDPins.println("           ]");
  }

  // Print the Permissible Values and Value Meanings
  public void printPermValues(DOMAttr lAttr, PrintWriter prDDPins) {
    boolean isFirst = true;
    if (lAttr.domPermValueArr.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("PermissibleValueList") + ": [");
    for (Iterator<DOMProp> i = lAttr.domPermValueArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if ((lDOMProp.hasDOMObject == null) || !(lDOMProp.hasDOMObject instanceof DOMPermValDefn)) {
        continue;
      }
      DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.hasDOMObject;

      if (isFirst) {
        prDDPins.println("              {" + formValue("PermissibleValue") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");
      }
      // prDDPins.println(" , {" + formValue("PermissibleValue") + ": {");
      prDDPins.println(
          "                  " + formValue("value") + ": " + formValue(lDOMPermVal.value) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": "
          + formValue(lDOMPermVal.value_meaning));
      prDDPins.println("                }");
      prDDPins.println("             }");
    }
    prDDPins.println("           ]");
  }

  // Print the Data Types
  public void printDataType(PrintWriter prDDPins) {
    boolean isFirst = true;
    for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
      DOMDataType lDataType = i.next();
      if (isFirst) {
        prDDPins.println("        {" + formValue("DataType") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("      , {" + formValue("DataType") + ": {");
      }
      prDDPins.println(
          "            " + formValue("identifier") + ": " + formValue(lDataType.identifier) + " ,");
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
    boolean isFirst = true;
    if (lDataType.pattern.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("patternList") + ": [");
    for (Iterator<String> i = lDataType.pattern.iterator(); i.hasNext();) {
      String lValue = i.next();
      if (isFirst) {
        prDDPins.println("              {" + formValue("Pattern") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("            , {" + formValue("Pattern") + ": {");
      }
      // prDDPins.println(" , {" + formValue("Pattern") + ": {");
      prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));
      prDDPins.println("              }");
      prDDPins.println("            }");
    }
    prDDPins.println("           ]");
  }

  // Print the Unit Identifier
  public void printUnitId(DOMUnit lUnit, PrintWriter prDDPins) {
    boolean isFirst = true;
    if (lUnit.unit_id.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("unitIdList") + ": [");
    for (Iterator<String> i = lUnit.unit_id.iterator(); i.hasNext();) {
      String lValue = i.next();
      if (isFirst) {
        prDDPins.println("              {" + formValue("UnitId") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("            , {" + formValue("UnitId") + ": {");
      }
      // prDDPins.println(" , {" + formValue("UnitId") + ": {");
      prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");
      prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));
      prDDPins.println("                }");
      prDDPins.println("             }");
    }
    prDDPins.println("           ]");

  }

  // Print the the Protege Pins Properties
  public void printPDDPPR(PrintWriter prDDPins) {
    ArrayList<DOMProp> lSortedAssocArr = new ArrayList<>(DOMInfoModel.masterDOMPropIdMap.values());
    for (Iterator<DOMProp> i = lSortedAssocArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      String prDataIdentifier = "PR." + lProp.identifier;
      prDDPins.println("([" + prDataIdentifier + "] of Property");
      prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
      prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
      prDDPins.println("  (registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println("  (registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");
      prDDPins.println("  (classOrder \"" + lProp.classOrder + "\")");
      prDDPins.println("  (versionIdentifier \"" + DOMInfoModel.identifier_version_id + "\"))");
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

      String lfc = "";
      prDDPins.println("  (having ");
      for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lDEC = j.next();
        prDDPins.print(lfc);
        prDDPins.print("    [" + "DEC_" + lDEC + "]");
        lfc = "\n";
      }
      prDDPins.print(")\n");
      prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println(" 	(registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");

      lfc = "";
      prDDPins.println("  (representing2 ");
      for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        prDDPins.print(lfc);
        if (lAttr.isEnumerated) {
          prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
        } else {
          prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
        }
        lfc = "\n";
      }
      prDDPins.print(")\n");
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

      String lfc = "";
      prDDPins.println("  (expressing ");
      for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
        DOMAttr lAttr = j.next();
        prDDPins.print(lfc);
        prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
        lfc = "\n";
      }
      prDDPins.print(")\n");
      prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue + "])");
      prDDPins.println(" 	(registrationAuthorityIdentifier ["
          + DMDocument.registrationAuthorityIdentifierValue + "])");
      lfc = "";
      prDDPins.println("  (specifying ");
      for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
        String lCD = j.next();
        prDDPins.print(lfc);
        prDDPins.print("    [" + "CD_" + lCD + "]");
        lfc = "\n";
      }
      prDDPins.print(")\n");
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

    ArrayList<DOMAttr> lAttrArr = new ArrayList<>(DOMInfoModel.masterDOMAttrIdMap.values());

    for (Iterator<DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
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
    for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
      DOMDataType lDataType = i.next();
      prDDPins.println("([" + lDataType.title + "] of DataType");
      prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
      prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
    }
  }

  // Format the Boolean String for RDF/OWL
  public String formBooleanValue(boolean lBoolean) {
    String rString = "" + lBoolean;
    return formValue(rString);
  }

  // Format the String for RDF/OWL
  public String formValue(String lString) {
    String rString = lString;
    if (rString == null) {
      rString = "null";
    }
    if (rString.indexOf("TBD") == 0) {
      rString = "null";
    }
    rString = DOMInfoModel.escapeXMLChar(rString);
    // rString = "\"" + rString + "\"";
    return rString;
  }
}
