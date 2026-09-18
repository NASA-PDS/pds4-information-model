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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Writes the PDS4 DOM content as Markdown files for MkDocs documentation
 *
 * This writer creates a directory structure optimized for MkDocs:
 * - Individual Markdown files for each class and attribute
 * - Index pages for browsing by namespace
 * - MkDocs configuration file (mkdocs.yml)
 * - Clean, searchable documentation
 */
public class WriteMkDocsFiles extends Object {

  private static final Logger LOG = LoggerFactory.getLogger(WriteMkDocsFiles.class);

  private String baseDir;
  private SchemaFileDefn schemaFileDefn;

  // Track namespaces and their items for index generation
  private Map<String, ArrayList<DOMClass>> classesByNamespace;
  private Map<String, ArrayList<DOMAttr>> attributesByNamespace;

  public WriteMkDocsFiles() {
    this.classesByNamespace = new HashMap<>();
    this.attributesByNamespace = new HashMap<>();
  }

  /**
   * Main entry point - writes all MkDocs Markdown files
   */
  public void writeMkDocs(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
    this.schemaFileDefn = lSchemaFileDefn;
    this.baseDir = DMDocument.getOutputDirPath() + "export/docs/";

    // Create directory structure
    Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/");
    Utility.checkCreateDirectory(baseDir);
    Utility.checkCreateDirectory(baseDir + "docs/");
    Utility.checkCreateDirectory(baseDir + "docs/classes/");
    Utility.checkCreateDirectory(baseDir + "docs/attributes/");
    Utility.checkCreateDirectory(baseDir + "docs/datatypes/");
    Utility.checkCreateDirectory(baseDir + "docs/namespaces/");

    // Organize items by namespace
    organizeByNamespace();

    // Write Markdown files
    writeHomePage();
    writeClassPages();
    writeAttributePages();
    writeDataTypePages();
    writeNamespaceIndexPages();

    // Write MkDocs configuration
    writeMkDocsConfig();

    Utility.registerMessage("0>info " + "WriteMkDocsFiles - Wrote " +
        DOMInfoModel.masterDOMClassArr.size() + " classes, " +
        DOMInfoModel.masterDOMAttrArr.size() + " attributes to Markdown");
    Utility.registerMessage("0>info " + "WriteMkDocsFiles - To build docs: cd " + baseDir + " && mkdocs build");
  }

  /**
   * Organize classes and attributes by namespace for index pages
   */
  private void organizeByNamespace() {
    // Organize classes
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isInactive || lClass.title.indexOf("PDS3") > -1) {
        continue;
      }

      String ns = lClass.nameSpaceIdNC;
      if (!classesByNamespace.containsKey(ns)) {
        classesByNamespace.put(ns, new ArrayList<>());
      }
      classesByNamespace.get(ns).add(lClass);
    }

    // Organize attributes
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (lAttr.isInactive || !(lAttr.isUsedInClass && lAttr.isAttribute)) {
        continue;
      }

      String ns = lAttr.nameSpaceIdNC;
      if (!attributesByNamespace.containsKey(ns)) {
        attributesByNamespace.put(ns, new ArrayList<>());
      }
      attributesByNamespace.get(ns).add(lAttr);
    }
  }

  /**
   * Write home page (index.md)
   */
  private void writeHomePage() throws IOException {
    String fileName = baseDir + "docs/index.md";
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    pw.println("# PDS4 Information Model - Data Dictionary");
    pw.println();
    pw.println("Version: " + DMDocument.masterPDSSchemaFileDefn.ont_version_id);
    pw.println();
    pw.println("Generated: " + DMDocument.masterTodaysDateTimeUTCFromInstant);
    pw.println();
    pw.println("## Overview");
    pw.println();
    pw.println("The Planetary Data System (PDS) Information Model defines the structure and semantics");
    pw.println("of all PDS4 data products. This data dictionary allows you to explore classes,");
    pw.println("attributes, and data types that make up the model.");
    pw.println();
    pw.println("## Statistics");
    pw.println();
    pw.println("- **Classes**: " + DOMInfoModel.masterDOMClassArr.size());
    pw.println("- **Attributes**: " + DOMInfoModel.masterDOMAttrArr.size());
    pw.println("- **Data Types**: " + DOMInfoModel.masterDOMDataTypeArr.size());
    pw.println("- **Units**: " + DOMInfoModel.masterDOMUnitArr.size());
    pw.println();
    pw.println("## Browse by Namespace");
    pw.println();

    // Categorize namespaces
    ArrayList<String> commonNS = new ArrayList<>();
    ArrayList<String> disciplineNS = new ArrayList<>();
    ArrayList<String> missionNS = new ArrayList<>();
    categorizeNamespaces(commonNS, disciplineNS, missionNS);

    // Common namespaces
    if (!commonNS.isEmpty()) {
      pw.println("### Common");
      pw.println();
      for (String ns : commonNS) {
        int classCount = classesByNamespace.containsKey(ns) ? classesByNamespace.get(ns).size() : 0;
        int attrCount = attributesByNamespace.containsKey(ns) ? attributesByNamespace.get(ns).size() : 0;
        pw.println("- [" + ns + "](namespaces/" + ns + ".md) - " +
            classCount + " classes, " + attrCount + " attributes");
      }
      pw.println();
    }

    // Discipline LDDs
    if (!disciplineNS.isEmpty()) {
      pw.println("### Discipline LDDs");
      pw.println();
      for (String ns : disciplineNS) {
        int classCount = classesByNamespace.containsKey(ns) ? classesByNamespace.get(ns).size() : 0;
        int attrCount = attributesByNamespace.containsKey(ns) ? attributesByNamespace.get(ns).size() : 0;
        pw.println("- [" + ns + "](namespaces/" + ns + ".md) - " +
            classCount + " classes, " + attrCount + " attributes");
      }
      pw.println();
    }

    // Mission LDDs
    if (!missionNS.isEmpty()) {
      pw.println("### Mission LDDs");
      pw.println();
      for (String ns : missionNS) {
        int classCount = classesByNamespace.containsKey(ns) ? classesByNamespace.get(ns).size() : 0;
        int attrCount = attributesByNamespace.containsKey(ns) ? attributesByNamespace.get(ns).size() : 0;
        pw.println("- [" + ns + "](namespaces/" + ns + ".md) - " +
            classCount + " classes, " + attrCount + " attributes");
      }
      pw.println();
    }

    pw.println();
    pw.println("## Getting Started");
    pw.println();
    pw.println("- Use the search bar at the top to find classes, attributes, or data types by name");
    pw.println("- Browse **Classes** to see object definitions and their associations");
    pw.println("- Explore **Attributes** to understand properties and their constraints");
    pw.println("- Check **Data Types** for valid value formats and constraints");
    pw.println();
    pw.println("## More Information");
    pw.println();
    pw.println("Visit the [Planetary Data System](https://pds.nasa.gov/) website for more information");
    pw.println("about PDS4 and the Information Model.");

    pw.close();
  }

  /**
   * Write individual Markdown file for each class
   */
  private void writeClassPages() throws IOException {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isInactive || lClass.title.indexOf("PDS3") > -1) {
        continue;
      }

      writeClassPage(lClass);
    }
  }

  /**
   * Write a single class page
   */
  private void writeClassPage(DOMClass lClass) throws IOException {
    String safeName = lClass.title.replaceAll("[^a-zA-Z0-9_-]", "_");
    String fileName = baseDir + "docs/classes/" + safeName + ".md";

    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    // Title with badges
    pw.println("# " + escapeMarkdown(lClass.title));
    pw.println();

    // Badges
    if (lClass.isAbstract) pw.print("![Abstract](https://img.shields.io/badge/Abstract-gray) ");
    if (lClass.isDeprecated) pw.print("![Deprecated](https://img.shields.io/badge/Deprecated-red) ");
    pw.println();
    pw.println();

    // Basic information table
    pw.println("## Basic Information");
    pw.println();
    pw.println("| Property | Value |");
    pw.println("|----------|-------|");
    pw.println("| **Identifier** | `" + lClass.identifier + "` |");
    pw.println("| **Version** | " + lClass.versionId + " |");
    pw.println("| **Namespace** | [" + lClass.nameSpaceIdNC + "](../namespaces/" + lClass.nameSpaceIdNC + ".md) |");
    pw.println("| **Steward** | " + lClass.steward + " |");
    if (lClass.subClassOf != null && !lClass.subClassOf.isAbstract &&
        !lClass.subClassOf.isVacuous && !lClass.subClassOf.title.equals("USER")) {
      String superSafeName = lClass.subClassOf.title.replaceAll("[^a-zA-Z0-9_-]", "_");
      pw.println("| **Superclass** | [" + lClass.subClassOf.title + "](" + superSafeName + ".md) |");
    }
    pw.println();

    // Definition
    pw.println("## Definition");
    pw.println();
    pw.println(escapeMarkdown(lClass.definition));
    pw.println();

    // Associations
    if (!lClass.allAttrAssocArr.isEmpty()) {
      pw.println("## Associations");
      pw.println();
      writeClassAssociations(lClass, pw);
    }

    pw.close();
  }

  /**
   * Write associations table for a class
   */
  private void writeClassAssociations(DOMClass lClass, PrintWriter pw) throws IOException {
    // Sort by classOrder
    TreeMap<String, DOMPropGroup> lDOMPropGroupMap = new TreeMap<>();
    DOMPropGroup lDOMPropGroup = new DOMPropGroup();
    String pGroupName = "NULL";

    for (Iterator<DOMProp> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      ISOClassOAIS11179 lDOMObject = lDOMProp.domObject;
      if (lDOMObject != null) {
        String lPropType = lDOMProp.isAttribute ? "A" : "C";
        String lSortOrder = lPropType + lDOMProp.classOrder + "-" + lDOMObject.identifier;

        if ((lDOMProp.groupName.indexOf("TBD") == 0) ||
            (lDOMProp.groupName.compareTo(pGroupName) != 0)) {
          pGroupName = lDOMProp.groupName.indexOf("TBD") == 0 ? "NULL" : lDOMProp.groupName;
          lDOMPropGroup = new DOMPropGroup();
          lDOMPropGroup.domProp = lDOMProp;
          lDOMPropGroup.domObjectArr.add(lDOMObject);
          lDOMPropGroupMap.put(lSortOrder, lDOMPropGroup);
        } else {
          lDOMPropGroup.domObjectArr.add(lDOMObject);
        }
      }
    }

    ArrayList<DOMPropGroup> lDOMPropGroupArr = new ArrayList<>(lDOMPropGroupMap.values());

    pw.println("| Name | Type | Cardinality | Reference |");
    pw.println("|------|------|-------------|-----------|");

    for (Iterator<DOMPropGroup> i = lDOMPropGroupArr.iterator(); i.hasNext();) {
      DOMPropGroup lDOMPropGroup2 = i.next();
      DOMProp lDOMProp = lDOMPropGroup2.domProp;

      String cardMin = lDOMProp.cardMin != null ? lDOMProp.cardMin : "0";
      String cardMax = lDOMProp.cardMax != null ? lDOMProp.cardMax : "*";
      String cardinality = cardMin + ".." + cardMax;

      String type = lDOMProp.isAttribute ? "Attribute" : "Class";

      // Build reference links
      StringBuilder refs = new StringBuilder();
      boolean first = true;
      for (Iterator<ISOClassOAIS11179> j = lDOMPropGroup2.domObjectArr.iterator(); j.hasNext();) {
        ISOClassOAIS11179 lISOClass = j.next();
        if (!first) refs.append("<br>");

        if (lDOMProp.isAttribute && lISOClass instanceof DOMAttr) {
          DOMAttr refAttr = (DOMAttr) lISOClass;
          String refSafeName = refAttr.title.replaceAll("[^a-zA-Z0-9_-]", "_");
          refs.append("[").append(refAttr.title).append("](../attributes/").append(refSafeName).append(".md)");
        } else if (!lDOMProp.isAttribute && lISOClass instanceof DOMClass) {
          DOMClass refClass = (DOMClass) lISOClass;
          String refSafeName = refClass.title.replaceAll("[^a-zA-Z0-9_-]", "_");
          refs.append("[").append(refClass.title).append("](").append(refSafeName).append(".md)");
        } else {
          refs.append("`").append(lISOClass.identifier).append("`");
        }

        first = false;
      }

      pw.println("| **" + escapeMarkdown(lDOMProp.title) + "** | " + type + " | `" + cardinality + "` | " + refs.toString() + " |");
    }
    pw.println();
  }

  /**
   * Write individual Markdown file for each attribute
   */
  private void writeAttributePages() throws IOException {
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (lAttr.isInactive || !(lAttr.isUsedInClass && lAttr.isAttribute)) {
        continue;
      }

      writeAttributePage(lAttr);
    }
  }

  /**
   * Write a single attribute page
   */
  private void writeAttributePage(DOMAttr lAttr) throws IOException {
    String safeName = lAttr.title.replaceAll("[^a-zA-Z0-9_-]", "_");
    String fileName = baseDir + "docs/attributes/" + safeName + ".md";

    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    // Title with badges
    pw.println("# " + escapeMarkdown(lAttr.title));
    pw.println();

    // Badges
    if (lAttr.isEnumerated) pw.print("![Enumerated](https://img.shields.io/badge/Enumerated-blue) ");
    if (lAttr.isDeprecated) pw.print("![Deprecated](https://img.shields.io/badge/Deprecated-red) ");
    if (lAttr.isNilable) pw.print("![Nillable](https://img.shields.io/badge/Nillable-gray) ");
    pw.println();
    pw.println();

    // Basic information table
    pw.println("## Basic Information");
    pw.println();
    pw.println("| Property | Value |");
    pw.println("|----------|-------|");
    pw.println("| **Identifier** | `" + lAttr.identifier + "` |");
    pw.println("| **Version** | " + lAttr.versionIdentifierValue + " |");
    pw.println("| **Namespace** | [" + lAttr.nameSpaceIdNC + "](../namespaces/" + lAttr.nameSpaceIdNC + ".md) |");
    pw.println("| **Steward** | " + lAttr.steward + " |");
    pw.println();

    // Definition
    pw.println("## Definition");
    pw.println();
    pw.println(escapeMarkdown(lAttr.definition));
    pw.println();

    // Data type and constraints
    pw.println("## Data Type & Constraints");
    pw.println();
    pw.println("| Property | Value |");
    pw.println("|----------|-------|");
    pw.println("| **Data Type** | `" + lAttr.valueType + "` |");

    String minChars = lAttr.getMinimumCharacters(true, true);
    if (minChars != null && !minChars.equals("null") && !minChars.equals("TBD")) {
      pw.println("| **Minimum Characters** | " + minChars + " |");
    }

    String maxChars = lAttr.getMaximumCharacters(true, true);
    if (maxChars != null && !maxChars.equals("null") && !maxChars.equals("TBD")) {
      pw.println("| **Maximum Characters** | " + maxChars + " |");
    }

    String minVal = lAttr.getMinimumValue(true, true);
    if (minVal != null && !minVal.equals("null") && !minVal.equals("TBD")) {
      pw.println("| **Minimum Value** | " + minVal + " |");
    }

    String maxVal = lAttr.getMaximumValue(true, true);
    if (maxVal != null && !maxVal.equals("null") && !maxVal.equals("TBD")) {
      pw.println("| **Maximum Value** | " + maxVal + " |");
    }

    String pattern = lAttr.getPattern(true);
    if (pattern != null && !pattern.equals("null") && !pattern.equals("TBD")) {
      pw.println("| **Pattern** | `" + escapeMarkdown(pattern) + "` |");
    }

    String unit = lAttr.getUnitOfMeasure(false);
    if (unit != null && !unit.equals("null") && !unit.equals("TBD")) {
      pw.println("| **Unit of Measure** | " + unit + " |");
    }

    pw.println();

    // Permissible values
    if (!lAttr.domPermValueArr.isEmpty()) {
      pw.println("## Permissible Values");
      pw.println();
      pw.println("| Value | Meaning | Deprecated |");
      pw.println("|-------|---------|------------|");

      for (Iterator<DOMProp> i = lAttr.domPermValueArr.iterator(); i.hasNext();) {
        DOMProp lDOMProp = i.next();
        if ((lDOMProp.domObject == null) || !(lDOMProp.domObject instanceof DOMPermValDefn)) {
          continue;
        }

        DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.domObject;
        String deprecated = lDOMPermValDefn.isDeprecated ? "Yes" : "No";
        pw.println("| `" + escapeMarkdown(lDOMPermValDefn.value) + "` | " +
            escapeMarkdown(lDOMPermValDefn.value_meaning) + " | " + deprecated + " |");
      }
      pw.println();
    }

    pw.close();
  }

  /**
   * Write data type pages
   */
  private void writeDataTypePages() throws IOException {
    String fileName = baseDir + "docs/datatypes/index.md";
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    pw.println("# Data Types");
    pw.println();
    pw.println("This page lists all data types defined in the PDS4 Information Model.");
    pw.println();
    pw.println("| Data Type | Namespace | Identifier |");
    pw.println("|-----------|-----------|------------|");

    for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
      DOMDataType lDataType = i.next();
      if (lDataType.isInactive) {
        continue;
      }

      pw.println("| **" + escapeMarkdown(lDataType.title) + "** | " +
          lDataType.nameSpaceIdNC + " | `" + lDataType.identifier + "` |");
    }

    pw.close();
  }

  /**
   * Write namespace index pages
   */
  private void writeNamespaceIndexPages() throws IOException {
    ArrayList<String> namespaces = new ArrayList<>(classesByNamespace.keySet());
    java.util.Collections.sort(namespaces);

    for (String ns : namespaces) {
      writeNamespaceIndexPage(ns);
    }
  }

  /**
   * Write index page for a single namespace
   */
  private void writeNamespaceIndexPage(String namespace) throws IOException {
    String fileName = baseDir + "docs/namespaces/" + namespace + ".md";
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    pw.println("# Namespace: " + namespace);
    pw.println();

    // Classes
    if (classesByNamespace.containsKey(namespace)) {
      ArrayList<DOMClass> classes = classesByNamespace.get(namespace);
      pw.println("## Classes (" + classes.size() + ")");
      pw.println();

      for (DOMClass lClass : classes) {
        String safeName = lClass.title.replaceAll("[^a-zA-Z0-9_-]", "_");
        pw.print("- [" + lClass.title + "](../classes/" + safeName + ".md)");
        if (lClass.isAbstract) pw.print(" ![Abstract](https://img.shields.io/badge/Abstract-gray)");
        if (lClass.isDeprecated) pw.print(" ![Deprecated](https://img.shields.io/badge/Deprecated-red)");
        pw.println();
      }
      pw.println();
    }

    // Attributes
    if (attributesByNamespace.containsKey(namespace)) {
      ArrayList<DOMAttr> attributes = attributesByNamespace.get(namespace);
      pw.println("## Attributes (" + attributes.size() + ")");
      pw.println();

      for (DOMAttr lAttr : attributes) {
        String safeName = lAttr.title.replaceAll("[^a-zA-Z0-9_-]", "_");
        pw.print("- [" + lAttr.title + "](../attributes/" + safeName + ".md)");
        if (lAttr.isEnumerated) pw.print(" ![Enumerated](https://img.shields.io/badge/Enumerated-blue)");
        if (lAttr.isDeprecated) pw.print(" ![Deprecated](https://img.shields.io/badge/Deprecated-red)");
        pw.println(" - " + lAttr.valueType);
      }
      pw.println();
    }

    pw.close();
  }

  /**
   * Write mkdocs.yml configuration file
   */
  private void writeMkDocsConfig() throws IOException {
    String fileName = baseDir + "mkdocs.yml";
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(
        new FileOutputStream(new File(fileName)), "UTF-8"));

    pw.println("site_name: PDS4 Information Model - Data Dictionary");
    pw.println("site_description: NASA Planetary Data System PDS4 Information Model Reference");
    pw.println("site_author: NASA Planetary Data System");
    pw.println("site_url: https://pds.nasa.gov/");
    pw.println();
    pw.println("theme:");
    pw.println("  name: material");
    pw.println("  palette:");
    pw.println("    - scheme: default");
    pw.println("      primary: indigo");
    pw.println("      accent: indigo");
    pw.println("  features:");
    pw.println("    - navigation.tabs");
    pw.println("    - navigation.sections");
    pw.println("    - navigation.indexes");
    pw.println("    - navigation.top");
    pw.println("    - search.suggest");
    pw.println("    - search.highlight");
    pw.println("    - content.code.copy");
    pw.println();
    pw.println("plugins:");
    pw.println("  - search:");
    pw.println("      separator: '[\\s\\-\\.]+'");
    pw.println();
    pw.println("markdown_extensions:");
    pw.println("  - admonition");
    pw.println("  - codehilite");
    pw.println("  - pymdownx.superfences");
    pw.println("  - pymdownx.tabbed");
    pw.println("  - tables");
    pw.println("  - toc:");
    pw.println("      permalink: true");
    pw.println();
    pw.println("nav:");
    pw.println("  - Home: index.md");
    pw.println("  - Classes:");

    // Organize namespaces by type (Common, Discipline, Mission)
    organizeNavByNamespaceType(pw, true);

    pw.println("  - Attributes:");
    organizeNavByNamespaceType(pw, false);

    pw.println("  - Data Types: datatypes/index.md");
    pw.println("  - Namespaces:");

    // Organize namespace links by type
    ArrayList<String> commonNS = new ArrayList<>();
    ArrayList<String> disciplineNS = new ArrayList<>();
    ArrayList<String> missionNS = new ArrayList<>();

    categorizeNamespaces(commonNS, disciplineNS, missionNS);

    if (!commonNS.isEmpty()) {
      pw.println("    - Common:");
      for (String ns : commonNS) {
        pw.println("      - " + ns + ": namespaces/" + ns + ".md");
      }
    }

    if (!disciplineNS.isEmpty()) {
      pw.println("    - Discipline LDDs:");
      for (String ns : disciplineNS) {
        pw.println("      - " + ns + ": namespaces/" + ns + ".md");
      }
    }

    if (!missionNS.isEmpty()) {
      pw.println("    - Mission LDDs:");
      for (String ns : missionNS) {
        pw.println("      - " + ns + ": namespaces/" + ns + ".md");
      }
    }

    pw.close();
  }

  /**
   * Organize navigation by namespace type (Common/Discipline/Mission)
   */
  private void organizeNavByNamespaceType(PrintWriter pw, boolean isClasses) {
    ArrayList<String> commonNS = new ArrayList<>();
    ArrayList<String> disciplineNS = new ArrayList<>();
    ArrayList<String> missionNS = new ArrayList<>();

    categorizeNamespaces(commonNS, disciplineNS, missionNS);

    // Common namespaces section
    if (!commonNS.isEmpty()) {
      pw.println("    - Common:");
      for (String ns : commonNS) {
        if (isClasses) {
          if (!classesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMClass> items = classesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, true);
        } else {
          if (!attributesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMAttr> items = attributesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, false);
        }
      }
    }

    // Discipline LDDs section
    if (!disciplineNS.isEmpty()) {
      pw.println("    - Discipline LDDs:");
      for (String ns : disciplineNS) {
        if (isClasses) {
          if (!classesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMClass> items = classesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, true);
        } else {
          if (!attributesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMAttr> items = attributesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, false);
        }
      }
    }

    // Mission LDDs section
    if (!missionNS.isEmpty()) {
      pw.println("    - Mission LDDs:");
      for (String ns : missionNS) {
        if (isClasses) {
          if (!classesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMClass> items = classesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, true);
        } else {
          if (!attributesByNamespace.containsKey(ns)) continue;
          ArrayList<DOMAttr> items = attributesByNamespace.get(ns);
          pw.println("      - " + ns + ":");
          writeNavItems(pw, ns, items, false);
        }
      }
    }
  }

  /**
   * Categorize namespaces into Common, Discipline, and Mission
   *
   * Logic:
   * - if pds -> Common
   * - if isDiscipline flag -> Discipline
   * - else -> Mission
   */
  private void categorizeNamespaces(ArrayList<String> commonNS, ArrayList<String> disciplineNS, ArrayList<String> missionNS) {
    ArrayList<SchemaFileDefn> nsArr = new ArrayList<>(DMDocument.masterAllSchemaFileSortMap.values());

    for (SchemaFileDefn ns : nsArr) {
      String nsId = ns.nameSpaceIdNC;

      // Skip if not in our lists
      if (!classesByNamespace.containsKey(nsId) && !attributesByNamespace.containsKey(nsId)) {
        continue;
      }

      String category;

      if (nsId.equals("pds")) {
        // pds namespace is always Common
        commonNS.add(nsId);
        category = "Common";
      } else if (ns.isDiscipline) {
        // Trust the isDiscipline flag for Discipline LDDs
        disciplineNS.add(nsId);
        category = "Discipline";
      } else {
        // Everything else is Mission
        missionNS.add(nsId);
        category = "Mission";
      }

      LOG.info("WriteMkDocsFiles - Namespace '{}' categorized as: {}", nsId, category);
    }

    // Sort each list
    java.util.Collections.sort(commonNS);
    java.util.Collections.sort(disciplineNS);
    java.util.Collections.sort(missionNS);

    // Print summary
    LOG.info("WriteMkDocsFiles - Categorization summary:");
    LOG.info("  Common: {}", commonNS);
    LOG.info("  Discipline: {}", disciplineNS);
    LOG.info("  Mission: {}", missionNS);
  }

  /**
   * Write navigation items (classes or attributes) with limit
   */
  private void writeNavItems(PrintWriter pw, String ns, ArrayList<?> items, boolean isClasses) {
    int count = 0;
    for (Object item : items) {
      if (count >= 10) {
        pw.println("        - '... (see namespace page)': namespaces/" + ns + ".md");
        break;
      }

      if (isClasses && item instanceof DOMClass) {
        DOMClass lClass = (DOMClass) item;
        String safeName = lClass.title.replaceAll("[^a-zA-Z0-9_-]", "_");
        pw.println("        - " + lClass.title + ": classes/" + safeName + ".md");
      } else if (!isClasses && item instanceof DOMAttr) {
        DOMAttr lAttr = (DOMAttr) item;
        String safeName = lAttr.title.replaceAll("[^a-zA-Z0-9_-]", "_");
        pw.println("        - " + lAttr.title + ": attributes/" + safeName + ".md");
      }

      count++;
    }
  }

  /**
   * Escape special Markdown characters
   */
  private String escapeMarkdown(String text) {
    if (text == null) return "";
    // Escape pipe characters in tables
    return text.replace("|", "\\|");
  }
}
