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

class RegConfig extends Object {

  static String reference_string = "_reference";
  static int reference_string_length = reference_string.length();

  ArrayList<String> associationsArr = new ArrayList<>();
  ArrayList<DOMAttr> allSearchAttrArr = new ArrayList<>();
  TreeMap<String, ArrayList<DOMAttr>> classAttrMap = new TreeMap<>();
  TreeMap<String, ArrayList<String>> classAssocMap = new TreeMap<>();

  public RegConfig() {
    associationsArr.add("has_browse");
    associationsArr.add("has_calibration");
    associationsArr.add("has_thumbnail");
    associationsArr.add("has_spice");
    associationsArr.add("curated_by_node");
    associationsArr.add("bibliographic_reference");
    associationsArr.add("has_document");
    associationsArr.add("collected_from");
    associationsArr.add("collected_by");
    associationsArr.add("create_by");
    associationsArr.add("cited_in");
    associationsArr.add("has_geometry");
    associationsArr.add("has_personnel");
    associationsArr.add("has_instrument_host");
    associationsArr.add("has_instrument");
    associationsArr.add("has_data");
    associationsArr.add("has_association");
    associationsArr.add("has_member");
    associationsArr.add("has_update");
    return;
  }

  // write the Registry Information Model Configuration file - JSON
  public void writeRegRIM(String todaysDate) throws java.io.IOException {
    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRIM1;
    PrintWriter prRIM1 =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    String delimiter = "";
    prRIM1.println("[");

    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isRegistryClass) {
        writeRegRIMClass(lClass, delimiter, prRIM1);
        delimiter = ", ";
      }
    }
    prRIM1.println("]");
    prRIM1.close();
  }

  public void writeRegRIMClass(DOMClass lClass, String delimiter, PrintWriter prRIM1) {
    ArrayList<DOMAttr> lAttrArr;
    ArrayList<DOMAttr> plAttrArr = new ArrayList<>();

    prRIM1.println(delimiter + "{");
    prRIM1.println("reg_object_type: " + lClass.title);

    lAttrArr =
        DOMInfoModel.getAllAttrRecurse(new ArrayList<DOMAttr>(), new ArrayList<DOMClass>(), lClass);
    if (!lAttrArr.isEmpty()) {
      for (DOMAttr lAttr : lAttrArr) {
        // System.out.println("debug writeRegRIM slot lAttr.title:" + lAttr.title);
        if (lAttr.isAttribute && DMDocument.registryAttr.contains(lAttr.title)) {
          plAttrArr.add(lAttr);
        }
      }
    }
    allSearchAttrArr.addAll(plAttrArr);
    int ind = 0;
    if (!plAttrArr.isEmpty()) {
      prRIM1.println("metadata: {");
      for (DOMAttr lAttr : plAttrArr) {
        ind++;
        prRIM1.println("           slot" + ind + ": " + lAttr.title);
      }
      prRIM1.println("          }");
    }
    prRIM1.println("data_refs: [");
    prRIM1.println(
        "             http://regrep.pds.nasa.gov/registry/entries/{logical_identifier}/{version_id}");
    if (!lAttrArr.isEmpty()) {
      for (DOMAttr lAttr : lAttrArr) {
        String lTitle = lAttr.title;
        // System.out.println("debug writeRegRIM slot lAttr.title:" + lAttr.title);
        int lIndex = lTitle.indexOf(reference_string);
        if (lIndex > -1) {
          prRIM1.println("           rel_label:" + lTitle);
          int lLength = lTitle.length();
          String lObjectType = lTitle.substring(0, (lLength - reference_string_length));
        }
      }
    }
    prRIM1.println("           ]");
    prRIM1.println("}");
  }

  // write the Registry Information Model Configuration file - JSON and XML
  public void writeRegRIM3(String todaysDate) throws java.io.IOException {
    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRIM3;
    PrintWriter prRIM =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    prRIM.println("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
    prRIM.println("<!-- $Header:-->");
    prRIM.println("<RegistryObjectList");
    prRIM.println("  xmlns=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0\"");
    prRIM.println("  xmlns:lcm=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:4.0\"");
    prRIM.println("  xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0\"");
    prRIM.println("  xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0\"");
    prRIM.println("  xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0\"");
    prRIM.println("  xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
    prRIM.println("  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    prRIM.println(
        "  xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0  file:///home/najmi/wellgeo/svn/omar/trunk/regrep-spec/src/main/resources/xsd/rim.xsd\"");
    prRIM.println("  >");
    prRIM.println(
        "  <rim:RegistryObject xsi:type=\"rim:RegisterType\" lid=\"urn:nasa:pds:profile:regrep\" id=\"urn:nasa:pds:profile:regrep\">");
    prRIM.println("    <rim:Name>");
    prRIM.println("      <rim:LocalizedString value=\"NASA PDS Profile of ebXML RegRep\"/>");
    prRIM.println("    </rim:Name>");
    prRIM.println("    <rim:Description>");
    prRIM.println(
        "      <rim:LocalizedString charset=\"UTF-8\" value=\"Register for all objects defined by the NASA PDS Profile of ebXML RegRep.\"/>");
    prRIM.println("    </rim:Description>");
    prRIM.println("    <rim:VersionInfo comment=\"1\"/>");
    prRIM.println("");
    prRIM.println("    <rim:RegistryObjectList>");

    // output objecttype definitions
    prRIM.println("\n       <!--       ObjectType Definitions       -->");
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isRegistryClass) {
        String lTitle = lClass.title;
        prRIM.println("      <rim:RegistryObject xsi:type=\"rim:ClassificationNodeType\" code=\""
            + lTitle
            + "\" parent=\"urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject\" lid=\"urn:nasa:pds:profile:regrep:ObjectType:"
            + lTitle + "\"  id=\"urn:nasa:pds:profile:regrep:ObjectType:" + lTitle + "\">");
        prRIM.println("        <rim:Name>");
        prRIM
            .println("          <rim:LocalizedString charset=\"UTF-8\" value=\"" + lTitle + "\"/>");
        prRIM.println("        </rim:Name>");
        prRIM.println("        <rim:Description>");
        prRIM.println("          <rim:LocalizedString charset=\"UTF-8\" value=\""
            + lClass.definition + "\"/>");
        prRIM.println("        </rim:Description>");
        prRIM.println("      </rim:RegistryObject>");
      }
    }

    // output associationtype definitions
    prRIM.println("\n       <!--       AssociationType Definitions       -->");
    for (Iterator<String> i = associationsArr.iterator(); i.hasNext();) {
      String lTitle = i.next();
      prRIM.println("      <rim:RegistryObject xsi:type=\"rim:ClassificationNodeType\" code=\""
          + lTitle
          + "\" parent=\"urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType\" lid=\"urn:nasa:pds:profile:regrep:AssociationType:"
          + lTitle + "\"  id=\"urn:nasa:pds:profile:regrep:AssociationType:" + lTitle + "\">");
      prRIM.println("        <rim:Name>");
      prRIM.println("          <rim:LocalizedString charset=\"UTF-8\" value=\"" + lTitle + "\"/>");
      prRIM.println("        </rim:Name>");
      prRIM.println("      </rim:RegistryObject>");
    }

    prRIM.println(
        "\n       <!--       ClassificationScheme Definitions.   Object_type (i.e., product_class) only at this time.       -->");
    String lScheme = "object_type";
    prRIM.println(
        "      <rim:RegistryObject xsi:type=\"rim:ClassificationSchemeType\" nodeType=\"urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode\" isInternal=\"true\" lid=\"urn:nasa:pds:profile:regrep:classificationScheme:"
            + lScheme + "\" id=\"urn:nasa:pds:profile:regrep:classificationScheme:" + lScheme
            + "\" >");
    prRIM.println("        <rim:Name>");
    prRIM.println("          <rim:LocalizedString value=\"" + lScheme
        + "\" charset=\"UTF-8\" xml:lang=\"en-US\"/>");
    prRIM.println("        </rim:Name>");
    prRIM.println(
        "        <ClassificationNode code=\"Standard_Product\" parent=\"urn:nasa:pds:profile:regrep:classificationScheme:"
            + lScheme + "\" lid=\"urn:nasa:pds:profile:regrep:" + lScheme
            + ":Standard_Product\" id=\"urn:nasa:pds:profile:regrep:" + lScheme
            + ":Standard_Product\">");
    prRIM.println("          <Name>");
    prRIM.println(
        "            <LocalizedString value=\"Standard_Product\" charset=\"UTF-8\" xml:lang=\"en-US\"/>");
    prRIM.println("          </Name>");
    prRIM.println("          <Description>");
    prRIM.println("            <LocalizedString value=\"\" charset=\"UTF-8\" xml:lang=\"en-US\"/>");
    prRIM.println("          </Description>");
    prRIM.println(
        "          <ClassificationNode code=\"Product_Digital\" parent=\"urn:nasa:pds:profile:regrep:"
            + lScheme + ":Standard_Product\" lid=\"urn:nasa:pds:profile:regrep:" + lScheme
            + ":Standard_Product:Digital_Product\" id=\"urn:nasa:pds:profile:regrep:" + lScheme
            + ":Standard_Product:Digital_Product\">");
    prRIM.println("            <Name>");
    prRIM.println(
        "              <LocalizedString value=\"Product_Digital\" charset=\"UTF-8\" xml:lang=\"en-US\"/>");
    prRIM.println("            </Name>");
    prRIM.println("          </ClassificationNode>");
    prRIM.println("        </ClassificationNode>");
    prRIM.println("      </rim:RegistryObject>");

    prRIM.println("\n       <!--       DataProductDiscoveryQuery.       -->");
    prRIM.println(
        "      <rim:RegistryObject xsi:type=\"rim:QueryDefinitionType\" lid=\"urn:nasa:pds:profile:regrep:query:DataProductDiscoveryQuery\" id=\"urn:nasa:pds:profile:regrep:query:DataProductDiscoveryQuery\">");
    prRIM.println("        <rim:Name>");
    prRIM.println("          <rim:LocalizedString value=\"Find PDS Data Product\"/>");
    prRIM.println("        </rim:Name>");
    prRIM.println("        <rim:Description>");
    prRIM.println(
        "          <rim:LocalizedString value=\"Find PDS Data products based upon specified search criteria.\"/>");
    prRIM.println("        </rim:Description>");
    prRIM.println(
        "        <rim:Parameter parameterName=\"matchOnAnyParameter\" dataType=\"boolean\" minOccurs=\"0\" defaultValue=\"false\">");
    prRIM.println("          <rim:Name>");
    prRIM.println(
        "            <rim:LocalizedString charset=\"UTF-8\" value=\"Match on ANY Parameter\"/>");
    prRIM.println("          </rim:Name>");
    prRIM.println("          <rim:Description>");
    prRIM.println(
        "            <rim:LocalizedString charset=\"UTF-8\" value=\"Check if you wish to match objects when any of the supplied parameters match. Do not check if you wish to match objects only when ALL supplied parameters match.\"/>");
    prRIM.println("          </rim:Description>");
    ArrayList<String> lTitleArr = new ArrayList<>();
    for (Iterator<DOMAttr> i = allSearchAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      String lTitle = lAttr.title;
      if (!lTitleArr.contains(lTitle)) {
        lTitleArr.add(lTitle);
      }
    }
    for (Iterator<String> i = lTitleArr.iterator(); i.hasNext();) {
      String lTitle = i.next();
      prRIM.println("        <rim:Parameter parameterName=\"" + lTitle
          + "\" dataType=\"string\" minOccurs=\"0\">");
      prRIM.println("          <rim:Name>");
      prRIM
          .println("            <rim:LocalizedString charset=\"UTF-8\" value=\"" + lTitle + "\"/>");
      prRIM.println("          </rim:Name>");
      prRIM.println("          <rim:Description>");
      prRIM.println(
          "            <rim:LocalizedString charset=\"UTF-8\" value=\"TBD_description. Use '%' and '_' as wildcard to match multiple and single characters respectively.\"/>");
      prRIM.println("          </rim:Description>");
      prRIM.println("        </rim:Parameter>");
    }
    prRIM.println("");
    prRIM.println(
        "        <rim:QueryExpression xsi:type=\"rim:StringQueryExpressionType\" queryLanguage=\"urn:oasis:names:tc:ebxml-regrep:QueryLanguage:EJBQL\">");
    prRIM.println("          <rim:Value>");
    prRIM.println("                Defined in QueryPlugin");
    prRIM.println("          </rim:Value>");
    prRIM.println("        </rim:QueryExpression>");
    prRIM.println("      </rim:RegistryObject>");
    prRIM.println("    </rim:RegistryObjectList>");
    prRIM.println("  </rim:RegistryObject>");

    prRIM.println("\n       <!--       RegisterType       -->");
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isRegistryClass) {
        String lTitle = lClass.title;
        prRIM.println(
            "  <rim:RegistryObject xsi:type=\"rim:RegisterType\" lid=\"urn:nasa:pds:profile:regrep:register:"
                + lTitle + "\" id=\"urn:nasa:pds:profile:regrep:register:" + lTitle + "\">");
        prRIM.println("    <rim:Name>");
        prRIM.println("      <rim:LocalizedString value=\"" + lTitle + "\"/>");
        prRIM.println("    </rim:Name>");
        prRIM.println("    <rim:Description>");
        prRIM.println("      <rim:LocalizedString charset=\"UTF-8\" value=\"Register of NASA "
            + lTitle + ".\"/>");
        prRIM.println("    </rim:Description>");
        prRIM.println("    <rim:VersionInfo comment=\"1\"/>");
        prRIM.println("  </rim:RegistryObject>");
      }
    }
    prRIM.println("</RegistryObjectList>");
    prRIM.close();
  }

  // ==========================================================================================

  // write the Registry Information Model Configuration file - XML
  public void writeRegRIM4(String todaysDate) throws java.io.IOException {

    TreeMap<String, DOMClass> lSortClassMap = new TreeMap<>();
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isRegistryClass) {
        lSortClassMap.put(lClass.title, lClass);
      }
    }
    ArrayList<DOMClass> lSortedClassArr = new ArrayList<>(lSortClassMap.values());
    String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRIM4;
    PrintWriter prRIM =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(lFileName)), "UTF-8"));

    prRIM.println("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
    prRIM.println("<PDS4Configuration xmlns=\"http://registry.pds.nasa.gov\">");

    // output objecttype definitions
    prRIM.println("\n       <!--       ObjectType definitions       -->");
    prRIM.println("");
    prRIM.println("  <registryObjectList xmlns=\"http://registry.pds.nasa.gov\">");
    prRIM.println("    <classificationNode code=\"Product\"");
    prRIM.println("        parent=\"urn:registry:ObjectType:RegistryObject:ExtrinsicObject\"");
    prRIM.println(
        "        description=\"This is the classification node for product. This represents a core registry object. In ebXML terms this is an extension of an ExtrinsicObject\" ");
    prRIM
        .println("        name=\"Product\" lid=\"urn:nasa:pds:profile:regrep:ObjectType:Product\"");
    prRIM.println("        guid=\"urn:nasa:pds:profile:regrep:ObjectType:Product\"/>");

    for (Iterator<DOMClass> i = lSortedClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      getClassAttrAndAssoc(lClass);
      writeRegRIM4regObjType(lClass, ":", prRIM);
    }
    prRIM.println("  </registryObjectList>");

    // output associationType definitions
    prRIM.println("\n       <!--       AssociationType definitions       -->");
    for (Iterator<DOMClass> i = lSortedClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      writeRegRIM4RegAssociations(lClass, ":", prRIM);
    }

    // output Slot definitions
    prRIM.println("\n       <!--       Slot definitions       -->");
    // output AssociatoinType definitions
    for (Iterator<DOMClass> i = lSortedClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      writeRegRIM4RegSlots(lClass, ":", prRIM);
    }
    prRIM.println("</PDS4Configuration>");
    prRIM.close();
  }

  public void writeRegRIM4regObjType(DOMClass lClass, String delimiter, PrintWriter prRIM) {
    String pCode = DMDocument.replaceString(lClass.title, "/", "_");
    prRIM.println("    <classificationNode code=\"" + DOMInfoModel.escapeXMLChar(pCode) + "\"");
    prRIM.println("        parent=\"urn:nasa:pds:profile:regrep:ObjectType:Product\"");
    prRIM.println("        description=\"" + DOMInfoModel.escapeXMLChar(lClass.definition) + "\"");
    prRIM.println("        name=\"" + DOMInfoModel.escapeXMLChar(lClass.title) + "\"");
    prRIM.println("        lid=\"urn:nasa:pds:profile:regrep:ObjectType:"
        + DOMInfoModel.escapeXMLChar(lClass.title) + "\"");
    prRIM.println("        guid=\"urn:nasa:pds:profile:regrep:ObjectType:"
        + DOMInfoModel.escapeXMLChar(lClass.title) + "\"/>");
  }

  public void writeRegRIM4RegAssociations(DOMClass lClass, String delimiter, PrintWriter prRIM) {
    ArrayList<DOMAttr> lAttrArr = classAttrMap.get(lClass.title);
    if (!(lAttrArr == null || lAttrArr.isEmpty())) {
      for (DOMAttr lAttr : lAttrArr) {
        if (lAttr.isAttribute && (lAttr.title.indexOf("reference_type") == 0)) {
          if (!lAttr.valArr.isEmpty()) {
            for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
              String lVal = j.next();
              if (lVal.compareTo("") != 0) {
                String pCode1 = DMDocument.replaceString(lClass.title, "/", "_");
                String pCode2 = DMDocument.replaceString(lVal, "/", "_");
                prRIM.println("    <classificationNode");
                prRIM.println("        lid=\"urn:nasa:pds:profile:regrep:AssociationType:"
                    + DOMInfoModel.escapeXMLChar(lClass.title) + "."
                    + DOMInfoModel.escapeXMLChar(lVal) + "\"");
                prRIM.println("        code=\"" + DOMInfoModel.escapeXMLChar(pCode1) + "."
                    + DOMInfoModel.escapeXMLChar(pCode2) + "\"");
                prRIM.println("        guid=\"urn:nasa:pds:profile:regrep:AssociationType:"
                    + DOMInfoModel.escapeXMLChar(lClass.title) + "."
                    + DOMInfoModel.escapeXMLChar(lVal) + "\"");
                prRIM.println(
                    "        parent=\"urn:registry:classificationScheme:AssociationType\"");
                prRIM.println("        name=\"" + DOMInfoModel.escapeXMLChar(lClass.title) + "."
                    + DOMInfoModel.escapeXMLChar(lVal) + "\"/>");
              }
            }
          }
        }
      }
    }
  }

  public void writeRegRIM4RegSlots(DOMClass lClass, String delimiter, PrintWriter prRIM) {
    ArrayList<DOMAttr> lAttrArr = classAttrMap.get(lClass.title);
    if (!(lAttrArr == null || lAttrArr.isEmpty())) {
      prRIM.println("\n    <extrinsicObject id=\"urn:nasa:pds:profile:regrep:ObjectType:"
          + DOMInfoModel.escapeXMLChar(lClass.title) + ":Definition\"");
      prRIM.println(
          "        objectType=\"urn:registry:ObjectType:RegistryObject:ExtrinsicObject:SlotDescription\">");
      for (DOMAttr lAttr : lAttrArr) {
        if (lAttr.isAttribute && DMDocument.registryAttr.contains(lAttr.title)) {
          prRIM.println("        <slot name=\"" + DOMInfoModel.escapeXMLChar(lAttr.title)
              + "\" slotType=\"string\"/>");
        }
      }
      prRIM.println(
          "\n       <!--       a way to specify an association, this is currently the approach harvest employs       -->");
      ArrayList<String> lRefTypeArr = classAssocMap.get(lClass.title);
      if (!(lRefTypeArr == null || lRefTypeArr.isEmpty())) {
        for (String lRefType : lRefTypeArr) {
          prRIM.println("        <slot name=\"urn:nasa:pds:profile:regrep:Slot:"
              + DOMInfoModel.escapeXMLChar(lRefType) + "\" slotType=\"guid\"/>");
        }
      }
      prRIM.println("    </extrinsicObject>");
    }
  }

  public void getClassAttrAndAssoc(DOMClass lClass) {
    ArrayList<DOMAttr> lAttrArr;
    ArrayList<String> lRefTypeArr;

    lAttrArr =
        DOMInfoModel.getAllAttrRecurse(new ArrayList<DOMAttr>(), new ArrayList<DOMClass>(), lClass);
    if (lAttrArr.isEmpty()) {
      return;
    }
    ArrayList<DOMAttr> tAttrArr = classAttrMap.get(lClass.title);
    if (tAttrArr != null) {
      System.out.println(
          "\n***Warning***  RegConfig - getClassAttrAndAssoc - found duplicate classAttrMap - lClass.title:"
              + lClass.title);
      return;
    }
    classAttrMap.put(lClass.title, lAttrArr);

    // get reference association types
    lRefTypeArr = DOMInfoModel.getAllRefAssocType(lAttrArr);
    if (lRefTypeArr == null) {
      return;
    }
    ArrayList<String> tRefTypeArr = classAssocMap.get(lClass.title);
    if (tRefTypeArr != null) {
      System.out.println(
          "\n***Warning***  RegConfig - getClassAttrAndAssoc - found duplicate classAssocMap - lClass.title:"
              + lClass.title);
      return;
    }
    classAssocMap.put(lClass.title, lRefTypeArr);
    return;
  }


  public void DumplRefTypeArr(int ind, ArrayList<String> lRefTypeArr) {
    System.out.println("\n ========== Dump lRefTypeArr =========== " + ind);
    if ((lRefTypeArr == null || lRefTypeArr.isEmpty())) {
      System.out.println("08>> lRefTypeArr is Empty");
    } else {
      for (String lRefType : lRefTypeArr) {
        System.out.println("    lRefType:" + lRefType);
      }

    }
  }
}
