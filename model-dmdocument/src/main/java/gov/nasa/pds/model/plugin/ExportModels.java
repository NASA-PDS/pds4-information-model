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
import java.io.*;
import java.util.*;

/** Driver for getting document
 *
 */
public class ExportModels extends Object {
	
	public ExportModels () {
				
	}

/**********************************************************************************************************
		write the various documents and files
***********************************************************************************************************/

	public void writeAllArtifacts () throws java.io.IOException {
		
		// check if the export/ directory exists; if not create it
		DMDocument.checkCreateDirectory (DMDocument.outputDirPath + "export/");
		
	    // write the model specification
		DMDocument.dmProcessState.setRelativeFileSpecModelSpec_DOM (DMDocument.masterPDSSchemaFileDefn);
		WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification(DMDocument.docInfo);
		writeDOMSpecification.printArtifacts();
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Specification Done");
				
		//	write the label schema - new version 4		
		DMDocument.dmProcessState.setRelativeFileSpecXMLSchema (DMDocument.masterPDSSchemaFileDefn);
		XML4LabelSchemaDOM xml4LabelSchemaDOM = new XML4LabelSchemaDOM ();
		xml4LabelSchemaDOM.writeXMLSchemaFiles (DMDocument.masterPDSSchemaFileDefn, DOMInfoModel.masterDOMClassArr);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - XML Schema - lSchemaFileDefn.identifier:" + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");
		
		//  write schematron file
		DMDocument.dmProcessState.setRelativeFileSpecSchematron (DMDocument.masterPDSSchemaFileDefn);
		WriteDOMSchematron writeDOMSchematron = new WriteDOMSchematron ();
		writeDOMSchematron.writeSchematronFile(DMDocument.masterPDSSchemaFileDefn, DOMInfoModel.masterDOMClassMap);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Schematron - lSchemaFileDefn.identifier:" + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");
		
		//  write label file for XML Schema and Schematron
		DMDocument.dmProcessState.setRelativeFileSpecXMLLabel (DMDocument.masterPDSSchemaFileDefn);
		WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
		writeCoreXMLSchemaLabel.writeFile(DMDocument.masterPDSSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:" + DMDocument.masterPDSSchemaFileDefn.identifier + " - Done");
		
	    // write the Doc Book
		DMDocument.dmProcessState.setRelativeFileSpecDDDocXML (DMDocument.masterPDSSchemaFileDefn);
		WriteDOMDocBook lWriteDOMDocBook  = new WriteDOMDocBook (); 
		lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - DD DocBook Done");

//		write the custom files
//		ExportModelsCustom lExportModelsCustom = new ExportModelsCustom ();
//		lExportModelsCustom.writeArtifacts (DMDocument.LDDToolFlag, DMDocument.masterPDSSchemaFileDefn);
		
		// write the DOM RDF
		WriteDOM11179DDRDFFile writeDOM11179DDRDFFile = new WriteDOM11179DDRDFFile ();
		writeDOM11179DDRDFFile.printISO11179DDRDF (DMDocument.sTodaysDate);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - RDF Done");
		
		// write the IM to RDF
//		WriteDOMIMToRDF lWriteDOMIMToRDF = new WriteDOMIMToRDF ();
//		lWriteDOMIMToRDF.domIMToRDFWriter (DMDocument.sTodaysDate);

		// write the Lucid Ingest file
//		WriteLucidMySQLFiles WriteLucidFiles = new WriteLucidMySQLFiles ();
//		WriteLucidFiles.WriteLucidFile();
		
		// write the DOM PDS4 DD CSV file
	    WriteDOMCSVFiles writeDOMCSVFiles = new WriteDOMCSVFiles (); 
		ArrayList <DOMClass> domSortClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
		writeDOMCSVFiles.writeDOMCSVFile (domSortClassArr, DMDocument.masterPDSSchemaFileDefn, null);
        DMDocument.registerMessage ("0>info " + "writeAllArtifacts - DD CSV Done");
        
		// write the PDS4 CCSDS CSV file 
//		WriteDocCSV writeDocCSV = new WriteDocCSV ();
//		writeDocCSV.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
//		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - CCSDS CSV Done");
		
		// write the 11179 DD pins file
//		WriteDOM11179DDPinsFile lWriteDOM11179DDPinsFile = new WriteDOM11179DDPinsFile ();
//		lWriteDOM11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPins);	
//		lWriteDOM11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPinsSN);	
//		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - DD Pins File Done");

		// write the 11179 DD pins file - Plus Class Version
		WriteDOM11179DDPinsFilePClass lWriteDOM11179DDPinsFilePClass = new WriteDOM11179DDPinsFilePClass ();
		lWriteDOM11179DDPinsFilePClass.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPins);	
		lWriteDOM11179DDPinsFilePClass.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPinsSN);	
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - DD Pins *** plus class *** File Done");
		
		// write the 11179 DOM JSON file - requires DOMInfoModel to be executed
		DMDocument.dmProcessState.setRelativeFileSpecDOMModelJSON (DMDocument.masterPDSSchemaFileDefn);
		WriteDOMDDJSONFile writeDOMDDJSONFile = new WriteDOMDDJSONFile ();
		writeDOMDDJSONFile.writeJSONFile ();
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - JSON Done");
		
		// write the 11179 DD Data Element Definition XML Files
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Class Defn Done");
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Attr Defn Done");
		
		// write the 11179 DOM DD Data Element Definition XML Files
		WriteDDProductDOMAttrDefinitions writeDDProductDOMAttrDefinitions = new WriteDDProductDOMAttrDefinitions ();
		writeDDProductDOMAttrDefinitions.writeDDDOMRegFiles (DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		
		// write the 11179 DOM DD Class Definition XML Files
		WriteDDProductDOMClassDefinitions writeDDProductDOMClassDefinitions = new WriteDDProductDOMClassDefinitions ();
		writeDDProductDOMClassDefinitions.writeDDProductDOMClassDefnFiles(DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		DMDocument.registerMessage ("0>info " + "writeAllDOMArtifacts - DOM Class Defn Done");
		DMDocument.registerMessage ("0>info " + "writeAllDOMArtifacts - DOM Attr Defn Done");
		
		// write the registry configuration files *** Reg needs conversion.
		if (false) {
		RegConfig regConfig = new RegConfig ();
		regConfig.writeRegRIM(DMDocument.sTodaysDate);
		regConfig.writeRegRIM3(DMDocument.sTodaysDate);
		regConfig.writeRegRIM4(DMDocument.sTodaysDate);
		}
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Regisry Config Done");
		
		// write the standard id extract file 
		WriteDOMStandardIdExtract writeDOMStandardIdExtract = new WriteDOMStandardIdExtract ();
        writeDOMStandardIdExtract.writeExtractFile();
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Standard Id Done");
		
		// print out the histogram for the DEC concepts
/*		System.out.println("\nConcept Histogram");
		Set <String> set1 = MasterDOMInfoModel.metricConceptMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			Integer lCount = MasterDOMInfoModel.metricConceptMap.get(lId);
			System.out.println("Descriptor: " + lId + "    Count: " + lCount);
		} */	
		return;
	}

	public void writeLDDArtifacts () throws java.io.IOException {
		
		// check if the export directory exists; if not create it
		DMDocument.checkCreateDirectory (DMDocument.outputDirPath + "export/");
		
		// DOM
		ArrayList <DOMClass> lLDDDOMClassArr = new ArrayList <DOMClass> ();
		TreeMap <String, DOMClass> lLDDDOMClassMap = new TreeMap <String, DOMClass> ();
		
		// get LDD Classes
		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
		for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isFromLDD) {
				if (lClass.allAttrAssocArr.size() != 0) {
					lLDDDOMClassArr.add(lClass);
					lLDDDOMClassMap.put(lClass.title, lClass);
				}
			}
		}
		
	    // write the Doc Book - includes common and all stacked LDDs
		if (DMDocument.exportDDFileFlag) {				
			DMDocument.dmProcessState.setRelativeFileSpecDDDocXML (DMDocument.masterLDDSchemaFileDefn);
			WriteDOMDocBook lWriteDOMDocBook  = new WriteDOMDocBook (); 
			lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
			DMDocument.registerMessage ("0>info " + "writeLDDArtifacts - DD DocBook Done");
		}
		
		// write the custom files
//		ExportModelsCustom lExportModelsCustom = new ExportModelsCustom ();
//		lExportModelsCustom.writeArtifacts (DMDocument.LDDToolFlag, DMDocument.masterLDDSchemaFileDefn);
		
		//	write the schema - new version 4
		DMDocument.dmProcessState.setRelativeFileSpecXMLSchema (DMDocument.masterLDDSchemaFileDefn);
		XML4LabelSchemaDOM xml4LabelSchemaDOM = new XML4LabelSchemaDOM ();
		xml4LabelSchemaDOM.writeXMLSchemaFiles (DMDocument.masterLDDSchemaFileDefn, lLDDDOMClassArr);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - XML Schema - lSchemaFileDefn.identifier:" + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");
		
		//  write schematron file
		DMDocument.dmProcessState.setRelativeFileSpecSchematron (DMDocument.masterLDDSchemaFileDefn);
		WriteDOMSchematron writeDOMSchematron = new WriteDOMSchematron ();
		writeDOMSchematron.writeSchematronFile(DMDocument.masterLDDSchemaFileDefn, lLDDDOMClassMap);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Schematron - lSchemaFileDefn.identifier:" + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");

		//  write label file for XML Schema and Schematron
		DMDocument.dmProcessState.setRelativeFileSpecXMLLabel (DMDocument.masterLDDSchemaFileDefn);
		WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
		writeCoreXMLSchemaLabel.writeFile(DMDocument.masterLDDSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:" + DMDocument.masterLDDSchemaFileDefn.identifier + " - Done");

		// write the 11179 JSON file
		if (DMDocument.exportJSONFileFlag || DMDocument.exportJSONFileAllFlag) {
			DMDocument.dmProcessState.setRelativeFileSpecDOMModelJSON (DMDocument.masterLDDSchemaFileDefn);
			WriteDOMDDJSONFile writeDOMDDJSONFile = new WriteDOMDDJSONFile ();
			writeDOMDDJSONFile.writeJSONFile ();
			DMDocument.registerMessage ("0>info " + "writeAllArtifacts - JSON Done");
		}

		// write the Info Spec file 
		if (DMDocument.exportSpecFileFlag) {
			DMDocument.dmProcessState.setRelativeFileSpecModelSpec_DOM (DMDocument.masterLDDSchemaFileDefn);
			WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification (DMDocument.docInfo); 
			writeDOMSpecification.printArtifacts();
			DMDocument.registerMessage ("0>info " + "writeLDDArtifacts - Info Model Spec Done");
		}
		return;
	}
}
