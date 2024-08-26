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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Driver for getting document
 *
 */
public class ExportModels extends Object {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExportModels.class);

  public ExportModels() {
  }

  /**********************************************************************************************************
   * write the various documents and files
   ***********************************************************************************************************/

  public void writeAllArtifacts() throws java.io.IOException {
    // check if the export/ directory exists; if not create it
    Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/");

    // write the model specification
    DMDocument.dmProcessState.setRelativeFileSpecModelSpec_DOM(DMDocument.masterPDSSchemaFileDefn);
    WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification(DMDocument.docInfo);
    writeDOMSpecification.printArtifacts();
    Utility.registerMessage("0>info " + "writeAllArtifacts - Specification Done");

    // write the label schema - new version 4
    DMDocument.dmProcessState.setRelativeFileSpecXMLSchema(DMDocument.masterPDSSchemaFileDefn);
    XML4LabelSchemaDOM xml4LabelSchemaDOM = new XML4LabelSchemaDOM();
    xml4LabelSchemaDOM.writeXMLSchemaFiles(DMDocument.masterPDSSchemaFileDefn,
        DOMInfoModel.masterDOMClassArr);
    Utility
        .registerMessage("0>info " + "writeAllArtifacts - XML Schema - lSchemaFileDefn.identifier:"
            + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");

    // write schematron file
    DMDocument.dmProcessState.setRelativeFileSpecSchematron(DMDocument.masterPDSSchemaFileDefn);
    WriteDOMSchematron writeDOMSchematron = new WriteDOMSchematron();
    writeDOMSchematron.writeSchematronFile(DMDocument.masterPDSSchemaFileDefn,
        DOMInfoModel.masterDOMClassMap);
    Utility
        .registerMessage("0>info " + "writeAllArtifacts - Schematron - lSchemaFileDefn.identifier:"
            + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");

    // write label file for XML Schema and Schematron
    DMDocument.dmProcessState.setRelativeFileSpecXMLLabel(DMDocument.masterPDSSchemaFileDefn);
    WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel();
    writeCoreXMLSchemaLabel.writeFile(DMDocument.masterPDSSchemaFileDefn);
    Utility.registerMessage(
        "0>info " + "writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:"
            + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");

    // write the Doc Book
    DMDocument.dmProcessState.setRelativeFileSpecDDDocXML(DMDocument.masterPDSSchemaFileDefn);
    WriteDOMDocBook lWriteDOMDocBook = new WriteDOMDocBook();
    lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
    Utility.registerMessage("0>info " + "writeAllArtifacts - DD DocBook Done");

    // write the custom files
    if (DMDocument.exportCustomFileFlag) {
    	ExportModelsCustom lExportModelsCustom = new ExportModelsCustom ();
    	lExportModelsCustom.writeArtifacts (DMDocument.LDDToolFlag,
    			DMDocument.masterPDSSchemaFileDefn);
    }

    // write the DOM RDF
    WriteDOM11179DDRDFFile writeDOM11179DDRDFFile = new WriteDOM11179DDRDFFile();
    writeDOM11179DDRDFFile.printISO11179DDRDF(DMDocument.sTodaysDate);
    Utility.registerMessage("0>info " + "writeAllArtifacts - RDF Done");

    // write the DOM PDS4 DD CSV file
    WriteDOMCSVFiles writeDOMCSVFiles = new WriteDOMCSVFiles();
    ArrayList<DOMClass> domSortClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassMap.values());
    writeDOMCSVFiles.writeDOMCSVFile(domSortClassArr, DMDocument.masterPDSSchemaFileDefn, null);
    Utility.registerMessage("0>info " + "writeAllArtifacts - DD CSV Done");

    // write the 11179 DD pins file - Plus Class Version
    WriteDOM11179DDPinsFilePClass lWriteDOM11179DDPinsFilePClass =
        new WriteDOM11179DDPinsFilePClass();
    lWriteDOM11179DDPinsFilePClass
        .writePINSFile(DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPins);
    lWriteDOM11179DDPinsFilePClass
        .writePINSFile(DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPinsSN);
    Utility
        .registerMessage("0>info " + "writeAllArtifacts - DD Pins *** plus class *** File Done");
    
    // write the 11179 DOM JSON file - requires DOMInfoModel to be executed
    DMDocument.dmProcessState.setRelativeFileSpecDOMModelJSON(DMDocument.masterPDSSchemaFileDefn);
    WriteDOMDDJSONFileLib writeDOMDDJSONFileLib = new WriteDOMDDJSONFileLib();
    writeDOMDDJSONFileLib.writeJSONFile(DMDocument.masterPDSSchemaFileDefn);
    Utility.registerMessage("0>info " + "writeAllArtifacts - JSON Done");
    

    // write the 11179 DD Data Element Definition XML Files
    Utility.registerMessage("0>info " + "writeAllArtifacts - Class Defn Done");
    Utility.registerMessage("0>info " + "writeAllArtifacts - Attr Defn Done");

    // write the 11179 DOM DD Data Element Definition XML Files
    WriteDDProductDOMAttrDefinitions writeDDProductDOMAttrDefinitions =
        new WriteDDProductDOMAttrDefinitions();
    writeDDProductDOMAttrDefinitions.writeDDDOMRegFiles(DMDocument.masterPDSSchemaFileDefn,
        DMDocument.sTodaysDate);

    // write the 11179 DOM DD Class Definition XML Files
    WriteDDProductDOMClassDefinitions writeDDProductDOMClassDefinitions =
        new WriteDDProductDOMClassDefinitions();
    writeDDProductDOMClassDefinitions.writeDDProductDOMClassDefnFiles(
        DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
    Utility.registerMessage("0>info " + "writeAllDOMArtifacts - DOM Class Defn Done");
    Utility.registerMessage("0>info " + "writeAllDOMArtifacts - DOM Attr Defn Done");
    
	// write the OWL/RDF/TTL output in TTL format (IM Classification)
    if (DMDocument.exportOWLRDFTTLFileFlag) {
    	String lClassificationType = "TBD_default";   // default
    	lClassificationType = "";   // default
    	lClassificationType = "PDS4.All.Products.Class.Prop"; // works
    	ClassAttrPropClassification lCAPC = new ClassAttrPropClassification (lClassificationType);
    	DMDocument.dmProcessState.setRelativeFileSpecOWLRDFTTL (DMDocument.masterPDSSchemaFileDefn);
    	WriteDOMRDFTTLFile writeDOMRDFTTLFile = new WriteDOMRDFTTLFile ();
    	writeDOMRDFTTLFile.writeDOMRDFTTLFile (lClassificationType, lCAPC);
    	Utility.registerMessage ("0>info " + "ExportModels - OWL/RDF/TTL output in TTL format (IM Classification) - Done");
    }
    
	// write the OWL/RDF output in RDF format (IM Export)
    if (DMDocument.exportOWLRDFFileFlag) {
    	DMDocument.dmProcessState.setRelativeFileSpecOWLRDFTTL (DMDocument.masterPDSSchemaFileDefn);
		WriteDOMRDFOWLFile writeDOMRDFOWLFile = new WriteDOMRDFOWLFile ();
		writeDOMRDFOWLFile.writeOWLFile (DMDocument.masterPDSSchemaFileDefn);
		Utility.registerMessage ("0>info " + "ExportModels - OWL/RDF output in RDF format (IM Export) - Done");
    }

    return;
  }

  public void writeLDDArtifacts() throws java.io.IOException {	  
    // check if the export directory exists; if not create it
    Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/");

    // DOM
    ArrayList<DOMClass> lLDDDOMClassArr = new ArrayList<>();
    TreeMap<String, DOMClass> lLDDDOMClassMap = new TreeMap<>();

    // get LDD Classes
    ArrayList<DOMClass> lClassArr = new ArrayList<>(DOMInfoModel.masterDOMClassMap.values());
    for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isFromLDD) {
        if (lClass.allAttrAssocArr.size() != 0) {
          lLDDDOMClassArr.add(lClass);
          lLDDDOMClassMap.put(lClass.title, lClass);
        }
      }
    }

    // write the Doc Book - includes common and all stacked LDDs
    if (DMDocument.exportDDFileFlag) {
      DMDocument.dmProcessState.setRelativeFileSpecDDDocXML(DMDocument.masterLDDSchemaFileDefn);
      WriteDOMDocBook lWriteDOMDocBook = new WriteDOMDocBook();
      lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
      Utility.registerMessage("0>info " + "writeLDDArtifacts - DD DocBook Done");
    }

    // write the custom files
    if (DMDocument.exportCustomFileFlag) {
    	ExportModelsCustom lExportModelsCustom = new ExportModelsCustom ();
    	lExportModelsCustom.writeArtifacts (DMDocument.LDDToolFlag,
    			DMDocument.masterLDDSchemaFileDefn);
    }

    // write the schema - new version 4
    DMDocument.dmProcessState.setRelativeFileSpecXMLSchema(DMDocument.masterLDDSchemaFileDefn);
    XML4LabelSchemaDOM xml4LabelSchemaDOM = new XML4LabelSchemaDOM();
    xml4LabelSchemaDOM.writeXMLSchemaFiles(DMDocument.masterLDDSchemaFileDefn, lLDDDOMClassArr);
    Utility
        .registerMessage("0>info " + "writeLDDArtifacts - XML Schema - lSchemaFileDefn.identifier:"
            + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");

    // write schematron file
    DMDocument.dmProcessState.setRelativeFileSpecSchematron(DMDocument.masterLDDSchemaFileDefn);
    WriteDOMSchematron writeDOMSchematron = new WriteDOMSchematron();
    writeDOMSchematron.writeSchematronFile(DMDocument.masterLDDSchemaFileDefn, lLDDDOMClassMap);
    Utility
        .registerMessage("0>info " + "writeLDDArtifacts - Schematron - lSchemaFileDefn.identifier:"
            + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");

    // write label file for XML Schema and Schematron
    DMDocument.dmProcessState.setRelativeFileSpecXMLLabel(DMDocument.masterLDDSchemaFileDefn);
    WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel();
    writeCoreXMLSchemaLabel.writeFile(DMDocument.masterLDDSchemaFileDefn);
    Utility.registerMessage(
        "0>info " + "writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:"
            + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");
    
    // write the 11179 DOM JSON file
      if (DMDocument.exportJSONFileFlag) {
    	  DMDocument.dmProcessState.setRelativeFileSpecDOMModelJSON(DMDocument.masterPDSSchemaFileDefn);
    	  WriteDOMDDJSONFileLib writeDOMDDJSONFileLib = new WriteDOMDDJSONFileLib();
    	  writeDOMDDJSONFileLib.writeJSONFile(DMDocument.masterPDSSchemaFileDefn);
    	  Utility.registerMessage("0>info " + "writeLDDArtifacts - JSON Done");
      }

    // write the Info Spec file
    if (DMDocument.exportSpecFileFlag) {
      DMDocument.dmProcessState
          .setRelativeFileSpecModelSpec_DOM(DMDocument.masterLDDSchemaFileDefn);
      WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification(DMDocument.docInfo);
      writeDOMSpecification.printArtifacts();
      Utility.registerMessage("0>info " + "writeLDDArtifacts - Info Model Spec Done");
    }
    
	// write the OWL/RDF/TTL output in TTL format (IM Classification)
	if (DMDocument.exportOWLRDFTTLFileFlag) {
		String lClassificationType = "TBD_default";   // default
		lClassificationType = "";   // default
//		lClassificationType = ("PDS4.LDD.All");
		lClassificationType = "PDS4.LDD.All";
		DMDocument.dmProcessState.setRelativeFileSpecOWLRDFTTL (DMDocument.masterLDDSchemaFileDefn);
		ClassAttrPropClassification lCAPC = new ClassAttrPropClassification (lClassificationType);
		WriteDOMRDFTTLFile writeDOMRDFTTLFile = new WriteDOMRDFTTLFile ();
		writeDOMRDFTTLFile.writeDOMRDFTTLFile (lClassificationType, lCAPC);
		Utility.registerMessage ("0>info " + "ExportModels - OWL/RDF/TTL output in TTL format (IM Classification) - Done");
	}    
	
	// write the OWL/RDF output in RDF format (IM Export)
	if (DMDocument.exportOWLRDFFileFlag) {
		DMDocument.dmProcessState.setRelativeFileSpecOWLRDFTTL (DMDocument.masterLDDSchemaFileDefn);
		WriteDOMRDFOWLFile writeDOMRDFOWLFile = new WriteDOMRDFOWLFile ();
		writeDOMRDFOWLFile.writeOWLFile (DMDocument.masterLDDSchemaFileDefn);
		Utility.registerMessage ("0>info " + "ExportModels - OWL/RDF output in RDF format (IM Export) - Done");
	}
	
	// *** To be deprecated ***
	// write the Terminological Mapping defined in the TermMap LDD to JSON
	if (DMDocument.exportTermMapFileFlag) {
		// write the terminological entry files
		WriteDOMTermEntryJSON writeDOMTermEntryJSON = new WriteDOMTermEntryJSON ();
		writeDOMTermEntryJSON.WriteDOMTermEntries (DMDocument.masterPDSSchemaFileDefn);
		Utility.registerMessage ("0>info " + "WriteDOMTermEntryJSON -  Done");
	}

    return;
  }
}
