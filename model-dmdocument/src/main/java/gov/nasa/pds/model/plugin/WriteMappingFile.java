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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Writes terminological map to the ./map directory
 * 
 */

class WriteMappingFile extends Object {

  static final String PARENT_COLUMN = "Parent";
  static final String CLASS_COLUMN = "Class";
  static final String ATTR_COLUMN = "Attr";
  static final String XPATH_FULL_COLUMN = "XPATH_FULL";
  static final String XPATH_SHORT_COLUMN = "XPATH_SHORT";
  static final String PDS3_KEYWORD_COLUMN = "PDS3_KEYWORD";
  static final String PDS3_GROUP_COLUMN = "PDS3_GROUP";
  static final String ODL_KEYWORD_COLUMN = "ODL_KEYWORD";
  static final String ODL_GROUP_COLUMN = "ODL_GROUP";
  static final String VICAR_KEYWORD_COLUMN = "VICAR_KEYWORD";
  static final String VICAR_GROUP_COLUMN = "VICAR_GROUP";
  static final String INSIGHT_NUANCE_COLUMN = "INSIGHT_NUANCE";
  static final String MSL_NUANCE_COLUMN = "MSL_NUANCE";

  static String[] column_keywords =
      {PARENT_COLUMN, CLASS_COLUMN, ATTR_COLUMN, XPATH_FULL_COLUMN, XPATH_SHORT_COLUMN,
          PDS3_KEYWORD_COLUMN, PDS3_GROUP_COLUMN, ODL_KEYWORD_COLUMN, ODL_GROUP_COLUMN,
          VICAR_KEYWORD_COLUMN, VICAR_GROUP_COLUMN, INSIGHT_NUANCE_COLUMN, MSL_NUANCE_COLUMN};

  int[] columns = new int[column_keywords.length];

  // write the mapping file
  public void writeMappingFile(String registrationAuthority, ArrayList<String> inputFileName)
      throws java.io.IOException {
    PrintWriter outFile = null;
    File outputFile = null;
    String outFileName = null;
    int processOrder = 0;
    // make sure the output map directory exists
    Map<String, String> env = System.getenv();
    String MAP_DIR = env.get("PARENT_DIR") + "/map";
    DMDocument.replaceString(MAP_DIR, "\\", "/");
    File mapDir = new File(MAP_DIR);
    if (!mapDir.isDirectory()) {
      mapDir.mkdir();
    }
    ArrayList<TermMapDefn> termMapArray = new ArrayList<>();
    ArrayList<String> modelObjectIdList = new ArrayList<>();
    // process each file in the array
    while (processOrder < inputFileName.size()) {
      // construct output file name
      String inFile = inputFileName.get(processOrder);
      processOrder++;
      int i = inFile.lastIndexOf(".");
      if (i > 0 && i < (inFile.length() - 1)) {
        outFileName = inFile.substring(0, i) + ".xml";
        System.out.println("%%%%%%%%%%% - output file name = " + outFileName);
        outputFile = new File(mapDir + "/" + outFileName);
        outFile =
            new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
      }
      // extract info from filename
      StringTokenizer strTok = new StringTokenizer(inFile, "-");
      Object discard = strTok.nextToken(); // discard "PDS4_"
      String namespaceId = strTok.nextToken();
      String stewardId = strTok.nextToken();
      String valueType = strTok.nextToken();

      try {
        BufferedReader bReader = new BufferedReader(new FileReader(inFile));

        // read the first line to see what columns are in the file
        String readLine = bReader.readLine();
        StringTokenizer strToken = new StringTokenizer(readLine, ",");
        System.out.println(readLine);
        processColumnFormat(strToken);

        while ((readLine = bReader.readLine()) != null) {
          System.out.println(readLine);
          TermMapDefn[] termEntry =
              processTermMap(registrationAuthority, namespaceId, stewardId, valueType, readLine);
          if (!modelObjectIdList.contains(termEntry[0].modelObjectId)) {
            modelObjectIdList.add(termEntry[0].modelObjectId);
            termMapArray.add(termEntry[0]);
          } else {
            System.out.println("^^^^^^^ Found existing object: " + termEntry[0].modelObjectId);
          }
          // unlikely duplicate entries for new records but check anyway in case of duplicate files
          // if (! termMapArray.contains(termEntry[1])) {
          termMapArray.add(termEntry[1]);
          // }
        }
        bReader.close();
      } catch (FileNotFoundException ex) {
        // file does not exist
        DMDocument.registerMessage("1>error " + "csv file does not exist.");
      } catch (IOException ex) {
        // I/O error
        DMDocument.registerMessage("1>error " + "input file IO Exception.");
      }

    } // end while

    Collections.sort(termMapArray, TermMapDefn.TermMapComparator);
    writeSKOSOutput(outFile, termMapArray);
    writePropertyMaps(mapDir, termMapArray);
  }

  // write Property maps
  public void writePropertyMaps(File mapDir, ArrayList<TermMapDefn> termMapArray)
      throws java.io.IOException {
    String outFileName = "PropertyMaps.xml";

    File outputFile = new File(mapDir + "/" + outFileName);
    PrintWriter propFile =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
    printPropertyFileHdr(propFile);
    for (TermMapDefn entry : termMapArray) {
      propFile.println("<Property_Map>");
      propFile.println("   <identifier>" + entry.identifier + "</identifier>");
      propFile.println("   <title>" + entry.title + "</title>");
      propFile.println("   <model_object_id>" + entry.modelObjectId + "</model_object_id>");
      propFile.println("   <model_object_type>" + entry.modelObjectType + "</model_object_type>");
      propFile.println("   <instance_id>" + entry.instanceId + "</instance_id>");
      propFile.println("   <external_namespace_id>" + "TBD" + "</external_namespace_id>");
      propFile.println("   <description>" + entry.description + "</description>");
      propFile.println("   <Property_Map_Entry>");
      propFile.println("      <property_map_type>" + "TBD" + "</Property_map_type>");
      propFile.println("      <Property_map_subtype>" + "TBD" + "</Property_map_subtype>");
      propFile.println("      <Property_name>" + entry.modelObjectType + "</Property_name>");
      propFile.println("      <Property_value>" + entry.title + "</Property_value>");
      propFile.println("   </Property_Map_Entry>");

      propFile.println("</Property_Map>");
    }
    propFile.println("</Property_Maps>");
    propFile.close();

  }

  public void writeSKOSOutput(PrintWriter pWriter, ArrayList<TermMapDefn> termMapArray) {
    printSKOSHdr(pWriter);
    for (TermMapDefn entry : termMapArray) {
      pWriter.println("\n   <Terminological_Entry_SKOS>");
      String str = "      <identifier>" + entry.identifier + "</identifier>";
      pWriter.println(str);
      str = "      <namespace_id>" + entry.namespaceId + "</namespace_id>";
      pWriter.println(str);
      str = "      <steward_id>" + entry.stewardId + "</steward_id>";
      pWriter.println(str);
      str = "      <title>" + entry.title + "</title>";
      pWriter.println(str);
      str = "      <description>" + entry.description + "</description>";
      pWriter.println(str);
      str = "      <reference_identifier>" + entry.referenceIdentifier + "</reference_identifier>";
      pWriter.println(str);
      str = "      <skos_relation_name>" + entry.skosRelationName + "</skos_relation_name>";
      pWriter.println(str);
      str = "      <model_object_id>" + entry.modelObjectId + "</model_object_id>";
      pWriter.println(str);
      str = "      <model_object_type>" + entry.modelObjectType + "</model_object_type>";
      pWriter.println(str);
      str = "      <instance_id>" + entry.instanceId + "</instance_id>";
      pWriter.println(str);
      pWriter.println("   </Terminological_Entry_SKOS>");
    }
    pWriter.println("</Term_Map_SKOS>");
    pWriter.close();
  }

  // process termMap one line at a time
  public TermMapDefn[] processTermMap(String registrationAuthority, String fileNamespaceId,
      String stewardId, String valueType, String theLine) {

    int tokenIndex = 0;
    String classStr = null;
    String attrStr = null;
    String classNamespaceId = null;
    String classTitle = null;
    String attrNamespaceId = null;
    String attrTitle = null;
    String xpath_full = null;
    String xpath_short = null;
    String pds3_keyword = null;
    String pds3_group = null;
    String odl_keyword = null;
    String odl_group = null;
    String vicar_keyword = null;
    String vicar_group = null;
    String inSight_nuance = null;
    String msl_nuance = null;

    TermMapDefn[] returnTermMapEntry = new TermMapDefn[2];
    TermMapDefn PDS4TermMapEntry = new TermMapDefn();

    StringTokenizer strToken = new StringTokenizer(theLine, ","); // default delimiter in CSV
    while (strToken.hasMoreTokens()) {
      String tok = strToken.nextToken().trim();
      System.out.println("Token = " + tok);
      int processType = columns[tokenIndex];
      System.out.println("processType = " + processType);
      switch (processType) {
        case 0: // parent_column
          PDS4TermMapEntry.parent = tok; // ?? could it be parent.parent.parent??

          break;
        case 1: // expected class_column format <namespace-id>.<class_title>
          classStr = tok;
          StringTokenizer subTok = new StringTokenizer(tok, ".");
          classNamespaceId = subTok.nextToken();// first token is the namespace
          while (subTok.hasMoreTokens()) {
            classTitle = subTok.nextToken(); // last token is the title
          }
          System.out.println("namespace = " + classNamespaceId + " class = " + classTitle);
          break;
        case 2: // expected attr_column
                // format:<namespace_id>.<class_title>.<namespace_id>.<attribute_title>
          if (tok.isEmpty()) {
            break;
          }
          attrStr = tok;
          StringTokenizer attrTok = new StringTokenizer(tok, ".");
          classNamespaceId = attrTok.nextToken();
          classTitle = attrTok.nextToken();
          attrNamespaceId = attrTok.nextToken();
          attrTitle = attrTok.nextToken();
          System.out.println("Attr namespaceID = " + attrNamespaceId + " Attr = " + attrTitle);
          break;
        case 3: // xpath_full_column
          if (tok.isEmpty()) {
            System.out.println("xpath_full is empty!" + theLine);
            break;
          }
          xpath_full = tok;
          break;
        case 4: // xpath_short_column
          if (tok.isEmpty()) {
            System.out.println("xpath_full is empty!" + theLine);
            break;
          }
          xpath_short = tok;
          break;
        case 5: // pds3_keyword_column
          if (tok.isEmpty()) {
            System.out.println("pds3_keyword is empty!" + theLine);
            break;
          }
          pds3_keyword = tok;
          break;

        case 6: // pds3_group_column
          if (tok.isEmpty()) {
            System.out.println("pds3_group is empty!" + theLine);
            break;
          }
          pds3_keyword = tok;
          break;
        case 7: // odl_keyword_column
          if (tok.isEmpty()) {
            System.out.println("odl_keyword is empty!" + theLine);
            break;
          }
          odl_keyword = tok;
          break;

        case 8: // odl_group_column
          if (tok.isEmpty()) {
            System.out.println("odl_group is empty!" + theLine);
            break;
          }
          odl_group = tok;
          break;
        case 9: // vicar_keyword_column
          if (tok.isEmpty()) {
            System.out.println("vicar_keyword is empty!" + theLine);
            break;
          }
          vicar_keyword = tok;
          break;

        case 10: // pds3_group_column
          if (tok.isEmpty()) {
            System.out.println("vicar_group is empty!" + theLine);
            break;
          }
          vicar_group = tok;
          break;
        case 11: // InSight_nuance_column
          if (tok.isEmpty()) {
            System.out.println("InSight_nuance is empty!" + theLine);
            break;
          }
          inSight_nuance = tok;
          break;

        case 12: // msl_nuance_column
          if (tok.isEmpty()) {
            System.out.println("msl_nuance is empty!" + theLine);
            break;
          }
          msl_nuance = tok;
          break;
      } // switch
      tokenIndex++;
    } // end while
    // construct model_object_id
    String modelObjectId = registrationAuthority + "." + classStr;
    if (attrStr != null) {
      modelObjectId = modelObjectId + "." + attrStr;
      PDS4TermMapEntry.title = attrTitle;
      PDS4TermMapEntry.modelObjectType = "attribute";
      PDS4TermMapEntry.namespaceId = attrNamespaceId;
    } else {
      PDS4TermMapEntry.title = classTitle;
      PDS4TermMapEntry.modelObjectType = "class";
      PDS4TermMapEntry.namespaceId = classNamespaceId;
    }

    PDS4TermMapEntry.identifier = modelObjectId;
    PDS4TermMapEntry.modelObjectId = modelObjectId;

    PDS4TermMapEntry.stewardId = PDS4TermMapEntry.namespaceId; // default;
    PDS4TermMapEntry.instanceId =
        (classStr.replaceFirst("\\.", ":")) + "/" + (attrStr.replaceFirst("\\.", ":"));
    TermMapDefn mappedToEntry = new TermMapDefn();
    mappedToEntry.CopyFrom(PDS4TermMapEntry);

    if (xpath_full != null) {
      mappedToEntry.identifier = modelObjectId + "::" + PDS4TermMapEntry.parent + xpath_full;
      mappedToEntry.modelObjectType = "xpath";
      mappedToEntry.skosRelationName = "closeMatch";
      mappedToEntry.instanceId = xpath_full;
    } else if ((pds3_keyword != null) || (pds3_group != null)) {
      mappedToEntry.identifier = fileNamespaceId + ":" + pds3_keyword;
      mappedToEntry.namespaceId = fileNamespaceId;
      mappedToEntry.stewardId = stewardId;
      mappedToEntry.title = pds3_keyword;
      mappedToEntry.skosRelationName = "closeMatch"; // default for pds3_keywords
      mappedToEntry.modelObjectType = valueType;

    } else if ((msl_nuance != null) || (inSight_nuance != null)) {
      mappedToEntry.identifier = fileNamespaceId + ":" + msl_nuance;
      mappedToEntry.namespaceId = fileNamespaceId;
      mappedToEntry.stewardId = stewardId;
      mappedToEntry.title = msl_nuance;
      mappedToEntry.skosRelationName = "closeMatch";
      mappedToEntry.description = "TBD_NUANCE";
      mappedToEntry.modelObjectType = valueType;
    }
    // handle inSight_nuance, pds3_group, vicar_keyword, vicar_group, odl_keyword,odl_group



    returnTermMapEntry[0] = PDS4TermMapEntry;
    returnTermMapEntry[1] = mappedToEntry;
    System.out.println("entry 0 = " + PDS4TermMapEntry.toString());
    System.out.println("entry 1 = " + mappedToEntry.toString());

    return returnTermMapEntry;
  }


  public void processColumnFormat(StringTokenizer tokenizer) {
    int total_column = 0;
    while (tokenizer.hasMoreTokens()) {
      String tok = tokenizer.nextToken();
      for (int i = 0; i < column_keywords.length; i++) {
        if (tok.equalsIgnoreCase(column_keywords[i])) {
          columns[total_column] = i;
          total_column++;
        }
      }
    }

  }


  // Print the SKOS Header
  public void printSKOSHdr(PrintWriter pWriter) {
    pWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    pWriter.println("<Term_Map_SKOS xmlns=\"http://pds.nasa.gov/pds4/pds/v1\"");
    pWriter.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    pWriter.println(
        "xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v1 file:/C:/AA7Ontologies/A01PDS4/Document/SchemaXML4/PDS4_PDS_1A10_TermMapSKOS.xsd\">");
  }

  // Print the SKOS Header
  public void printPropertyFileHdr(PrintWriter pWriter) {
    pWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    pWriter.println("<Property_Maps xmlns=\"http://pds.nasa.gov/pds4/pds/v1\"");
    pWriter.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    pWriter.println(
        "xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v1 file:/C:/AA7Ontologies/A01PDS4/Document/SchemaXML4/PDS4_PDS_1A10_TermMapSKOS.xsd\">");

    pWriter.println("<identifier>identifier0</identifier>");
    pWriter.println("<title>title0</title>");
    pWriter.println("<namespace_id>namespace_id0</namespace_id>");
    pWriter.println("<description>description0</description>");
    pWriter
        .println("<external_property_map_id>external_property_map_id0</external_property_map_id>");

  }

} // end WriteMappingFile
