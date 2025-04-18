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

import java.io.PrintWriter;
import java.util.Iterator;

class WriteDOMPropMapsSchema extends Object {

  PrintWriter prDDPins;

  public WriteDOMPropMapsSchema() {
    return;
  }

  // write the property maps
  public void writeDOMPropertyMaps(PrintWriter lPrinter) throws java.io.IOException {
    prDDPins = lPrinter;

    // write the open to the XML block to contain the JSON code
    prDDPins.println(" ");
    prDDPins.println("  <" + "xs:" + "annotation>");
    prDDPins.println("    <" + "xs:" + "documentation>");

    printPDDPHdr();
    printPDDPBody();
    printPDDPFtr();

    // write the close to the XML block to contain the JSON code
    prDDPins.println("    </" + "xs:" + "documentation>");
    prDDPins.println("  </" + "xs:" + "annotation>");
  }

  // Print the JSON Header
  public void printPDDPHdr() {
    prDDPins.println("[");
    prDDPins.println("  {");
    prDDPins.println("    " + formValue("dataDictionary") + ": {");
    prDDPins
        .println("      " + formValue("Title") + ": " + formValue("PDS4 Data Dictionary") + " ,");
    // prDDPins.println(" " + formValue("Version") + ": " + formValue(InfoModel.ont_version_id) + "
    // ,");
    prDDPins.println("      " + formValue("Version") + ": "
        + formValue(DMDocument.masterPDSSchemaFileDefn.ont_version_id) + " ,");
    prDDPins
        .println("      " + formValue("Date") + ": " + formValue(DMDocument.sTodaysDate) + " ,");
    prDDPins.println("      " + formValue("Description") + ": "
        + formValue("This document is a dump of the contents of the PDS4 Data Dictionary") + " ,");
  }

  // Format the Boolean String for JSON
  public String formBooleanValue(boolean lBoolean) {
    String rString = "" + lBoolean;
    return formValue(rString);
  }

  // Format the String for JSON
  public String formValue(String lString) {
    String rString = lString;
    if (rString == null) {
      rString = "null";
    }
    if (rString.indexOf("TBD") == 0) {
      rString = "null";
    }
    rString = DOMInfoModel.escapeJSONChar(rString);
    rString = "\"" + rString + "\"";
    return rString;
  }

  // Print the Footer
  public void printPDDPFtr() {
    prDDPins.println("    }");
    prDDPins.println("  }");
    prDDPins.println("]");
  }

  // print the body
  public void printPDDPBody() {
    prDDPins.println("      " + formValue("PropertyMapDictionary") + ": [");
    printPropertyMaps(prDDPins);
    prDDPins.println("      ]");
  }

  // Print the Property Maps
  public void printPropertyMaps(PrintWriter prDDPins) {
    boolean isFirst = true;
    for (Iterator<PropertyMapsDefn> i = DOMInfoModel.masterPropertyMapsArr.iterator(); i
        .hasNext();) {
      PropertyMapsDefn lPropertyMaps = i.next();
      if (isFirst) {
        prDDPins.println("        {" + formValue("PropertyMaps") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("      , {" + formValue("PropertyMaps") + ": {");
      }
      prDDPins.println("            " + formValue("identifier") + ": "
          + formValue(lPropertyMaps.rdfIdentifier) + " ,");
      prDDPins.println(
          "            " + formValue("title") + ": " + formValue(lPropertyMaps.title) + " ,");
      prDDPins.println("            " + formValue("namespace_id") + ": "
          + formValue(lPropertyMaps.namespace_id) + " ,");
      prDDPins.println("            " + formValue("description") + ": "
          + formValue(lPropertyMaps.description) + " ,");
      prDDPins.println("            " + formValue("external_property_map_id") + ": "
          + formValue(lPropertyMaps.external_property_map_id));
      printPropertyMap(lPropertyMaps, prDDPins);
      prDDPins.println("          }");
      prDDPins.println("        }");
    }
  }

  // Print the Property Map
  public void printPropertyMap(PropertyMapsDefn lPropertyMaps, PrintWriter prDDPins) {
    boolean isFirst = true;
    if (lPropertyMaps.propertyMapArr.isEmpty()) {
      return;
    }
    prDDPins.println("          , " + formValue("propertyMapList") + ": [");
    for (Iterator<PropertyMapDefn> i = lPropertyMaps.propertyMapArr.iterator(); i.hasNext();) {
      PropertyMapDefn lPropertyMap = i.next();
      if (isFirst) {
        prDDPins.println("              {" + formValue("PropertyMap") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("            , {" + formValue("PropertyMap") + ": {");
      }
      prDDPins.println("                  " + formValue("identifier") + ": "
          + formValue(lPropertyMap.identifier) + " ,");
      prDDPins.println(
          "                  " + formValue("title") + ": " + formValue(lPropertyMap.title) + " ,");
      prDDPins.println("                  " + formValue("model_object_id") + ": "
          + formValue(lPropertyMap.model_object_id) + " ,");
      prDDPins.println("                  " + formValue("model_object_type") + ": "
          + formValue(lPropertyMap.model_object_type) + " ,");
      prDDPins.println("                  " + formValue("instance_id") + ": "
          + formValue(lPropertyMap.instance_id) + " ,");
      prDDPins.println("                  " + formValue("external_namespace_id") + ": "
          + formValue(lPropertyMap.external_namespace_id) + " ,");
      prDDPins.println("                  " + formValue("description") + ": "
          + formValue(lPropertyMap.description));
      printPropertyMapEntrys(lPropertyMap, prDDPins);
      prDDPins.println("                 }");
      prDDPins.println("              }");
    }
    prDDPins.println("            ]");
  }

  // Print the Property Map Entrys
  public void printPropertyMapEntrys(PropertyMapDefn lPropertyMap, PrintWriter prDDPins) {
    boolean isFirst = true;
    if (lPropertyMap.propertyMapEntryArr.isEmpty()) {
      return;
    }
    prDDPins.println("                , " + formValue("propertyMapEntryList") + ": [");
    for (Iterator<PropertyMapEntryDefn> i = lPropertyMap.propertyMapEntryArr.iterator(); i
        .hasNext();) {
      PropertyMapEntryDefn lPropertyMapEntry = i.next();
      if (isFirst) {
        prDDPins.println("                    {" + formValue("PropertyMapEntry") + ": {");
        isFirst = false;
      } else {
        prDDPins.println("                  , {" + formValue("PropertyMapEntry") + ": {");
      }
      prDDPins.println("                        " + formValue("property_name") + ": "
          + formValue(lPropertyMapEntry.property_name) + " ,");
      prDDPins.println("                        " + formValue("property_value") + ": "
          + formValue(lPropertyMapEntry.property_value) + " }");
      prDDPins.println("                    }");
    }
    prDDPins.println("                    ]");
  }

}
