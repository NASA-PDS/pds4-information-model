package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/** Driver for getting document
 *
 */
public class ExportModels extends Object {
	boolean PDSOptionalFlag = false;
	
	public ExportModels () {
				
	}

/**********************************************************************************************************
		write the various documents and files
***********************************************************************************************************/

	public void writeAllArtifacts (boolean domFlag) throws java.io.IOException {	    
	    // write the model specification
		WriteSpecification writeSpecification  = new WriteSpecification (DMDocument.docInfo, PDSOptionalFlag); 
		writeSpecification.printArtifacts();
		
		//DOM
		if (domFlag) {
		WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification(DMDocument.docInfo, PDSOptionalFlag);
		writeDOMSpecification.printArtifacts();
		}
		
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Specification Done");
		
		if (DMDocument.exportJSONAttrFlag)  {
			WriteModelAttrJSONFile lWriteModelAttrJSONFile = new WriteModelAttrJSONFile ();
			lWriteModelAttrJSONFile.writeJSONFile (DMDocument.masterPDSSchemaFileDefn);
		}
		
		// get the schema file definitions
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());
		
		//	write the label schema - new version 4		
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			
			//	write the label schema			
			XML4LabelSchema xml4LabelSchema = new XML4LabelSchema ();
			xml4LabelSchema.writeXMLSchemaFiles (lSchemaFileDefn, InfoModel.masterMOFClassArr);
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - XML Schema - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");
			
			// DOM
			if (domFlag) {
			XML4LabelSchemaDOM xml4LabelSchemaDOM = new XML4LabelSchemaDOM ();
			xml4LabelSchemaDOM.writeXMLSchemaFiles (lSchemaFileDefn, DOMInfoModel.masterDOMClassArr);
			}
			//  write schematron file
			WriteSchematron writeSchematron = new WriteSchematron ();
			writeSchematron.writeSchematronFile(lSchemaFileDefn, InfoModel.masterMOFClassMap);
			
			//DOM
			if (domFlag) {
			WriteDOMSchematron writeDOMSchematron = new WriteDOMSchematron ();
			writeDOMSchematron.writeSchematronFile(lSchemaFileDefn, DOMInfoModel.masterDOMClassMap);
			}
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Schematron - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");
			
			//  write label file for XML Schema and Schematron
			WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
			writeCoreXMLSchemaLabel.writeFile(lSchemaFileDefn);
//			writeCoreXMLSchemaLabel.writeFile(DMDocument.masterPDSSchemaFileDefn);
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");
		}	
		
	    // write the Doc Book
		WriteDocBook lWriteDocBook  = new WriteDocBook (); 
		lWriteDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
		
		if (domFlag) {
		WriteDOMDocBook lWriteDOMDocBook  = new WriteDOMDocBook (); 
		lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - DD DocBook Done");

		// write the xmi file
		XMI2LabelSchema xmi2LabelSchema = new XMI2LabelSchema ();
		xmi2LabelSchema.getXMIElements ();
		xmi2LabelSchema.writeXMIFile (DMDocument.sTodaysDate);
		
		//DOM
		if (domFlag) {
		XMI2LabelSchemaDOM xmi2LabelSchemaDOM = new XMI2LabelSchemaDOM ();
		xmi2LabelSchemaDOM.getXMIElements ();
		xmi2LabelSchemaDOM.writeXMIFile (DMDocument.sTodaysDate);	
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - XMI1 Done");

		// write the xmi file - original version with relationship names
		XMI2LabelSchema2 xmi2LabelSchema2 = new XMI2LabelSchema2 ();
		xmi2LabelSchema2.writeXMIFile (DMDocument.sTodaysDate);
		
		// DOM  write the xmi file - original version with relationship names
		if (domFlag) {
		XMI2LabelSchema2DOM xmi2LabelSchema2DOM = new XMI2LabelSchema2DOM ();
		xmi2LabelSchema2DOM.writeXMIFile (DMDocument.sTodaysDate);
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - XMI2 Done");
		
		// write the RDF
		Write11179DDRDFFile write11179DDRDFFile = new Write11179DDRDFFile ();
		write11179DDRDFFile.printISO11179DDRDF (DMDocument.sTodaysDate);
		
		// write the DOM RDF
		if (domFlag) {
		WriteDOM11179DDRDFFile writeDOM11179DDRDFFile = new WriteDOM11179DDRDFFile ();
		writeDOM11179DDRDFFile.printISO11179DDRDF (DMDocument.sTodaysDate);
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - RDF Done");
		
/*		// write the IM to RDF
		WriteDOMIMToRDF lWriteDOMIMToRDF = new WriteDOMIMToRDF ();
		lWriteDOMIMToRDF.domIMToRDFWriter (DMDocument.sTodaysDate);

		// write the Lucid Ingest file  file 
		WriteLucidFiles WriteLucidFiles = new WriteLucidFiles ();
		WriteLucidFiles.WriteLucidFile(); */
		
        // write the DOM PDS4 DD CSV file 
        ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
		WriteCSVFiles writeCSVFiles = new WriteCSVFiles ();
		writeCSVFiles.writeCSVFile (lSortClassArr, DMDocument.masterPDSSchemaFileDefn, null);
        
    	if (domFlag) {
	    WriteDOMCSVFiles writeDOMCSVFiles = new WriteDOMCSVFiles (); 
		ArrayList <DOMClass> domSortClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
		writeDOMCSVFiles.writeDOMCSVFile (domSortClassArr, DMDocument.masterPDSSchemaFileDefn, null);
    	}
        if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - DD CSV Done");
        
		// write the PDS4 CCSDS CSV file 
		WriteDocCSV writeDocCSV = new WriteDocCSV ();
		writeDocCSV.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - CCSDS CSV Done");
		
		//DOM
		if (domFlag) {
		WriteDOMDocCSV writeDOMDocCSV = new WriteDOMDocCSV ();
		writeDOMDocCSV.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
		}
		
		//DOM
		if (domFlag) {
		WriteDOM11179DDPinsFile lWriteDOM11179DDPinsFile = new WriteDOM11179DDPinsFile ();
		lWriteDOM11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPins);	
		lWriteDOM11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPinsSN);	
		}
		
		// write the 11179 DD pins file
		Write11179DDPinsFile write11179DDPinsFile = new Write11179DDPinsFile ();
		write11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPins);	
		write11179DDPinsFile.writePINSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecDDProtPinsSN);	
		
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - DD Pins File Done");

// debug print the master list of rules
//		System.out.println("\n ================================ Master Rule Array Dump ========================================");
//		ArrayList <RuleDefn> dumpRuleDefnArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
//		System.out.println("<<<RuleDump - ExportModels - model update complete - start writing artifacts>>>");
//		InfoModel.printRulesAllDebug (222, dumpRuleDefnArr);
//		System.out.println("<<<RuleDump End - ExportModels>>>");
		
		// write the 11179 JSON file
		Write11179DDJSONFile write11179DDJSONFile = new Write11179DDJSONFile ();
		write11179DDJSONFile.writeJSONFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelJSON);
		
		if (! DMDocument.LDDToolFlag) {
			WriteDOMDDJSONFile writeDOMDDJSONFile = new WriteDOMDDJSONFile ();
			writeDOMDDJSONFile.writeJSONFile ();	
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - JSON Done");
		
		// write the LOD SKOS file
		WriteLODSKOSFile writeLODSKOSFile = new WriteLODSKOSFile ();
		writeLODSKOSFile.writeSKOSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecSKOSTTL);	
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - SKOS Done");
		
		//DOM
		if (domFlag) {
		WriteLODSKOSFileDOM writeLODSKOSDOMFile = new WriteLODSKOSFileDOM ();
		writeLODSKOSDOMFile.writeDOMSKOSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecSKOSTTL_DOM);
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - SKOS Done");

		// write the RDF/OWL file
		if (! DMDocument.LDDToolFlag) {
			WriteRDFOWLFile writeRDFOWLFile = new WriteRDFOWLFile ();
			writeRDFOWLFile.writeOWLFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecOWLRDF);	
			//DOM
			if (domFlag) {
				WriteDOMRDFOWLFile writeDOMRDFOWLFile = new WriteDOMRDFOWLFile ();
				writeDOMRDFOWLFile.writeOWLFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecOWLRDF_DOM);	
			}
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - RDF/OWL Done");
		
		// write the 11179 DD Data Element Definition XML Files
		WriteDDProductAttrDefinitions writeDDProductAttrDefinitions = new WriteDDProductAttrDefinitions ();
		writeDDProductAttrDefinitions.writeDDRegFiles (DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Attr Defn Done");
		
		// write the 11179 DD Class Definition XML Files
		WriteDDProductClassDefinitions writeDDProductClassDefinitions = new WriteDDProductClassDefinitions ();
		writeDDProductClassDefinitions.writeDDProductClassDefnFiles(DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Class Defn Done");

		//DOM
		// write the 11179 DOM DD Data Element Definition XML Files
		if (domFlag) {
		WriteDDProductDOMAttrDefinitions writeDDProductDOMAttrDefinitions = new WriteDDProductDOMAttrDefinitions ();
		writeDDProductDOMAttrDefinitions.writeDDDOMRegFiles (DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		if (DMDocument.debugFlag) System.out.println("debug writeAllDOMArtifacts - DOM Attr Defn Done");
		
		// write the 11179 DOM DD Class Definition XML Files
		WriteDDProductDOMClassDefinitions writeDDProductDOMClassDefinitions = new WriteDDProductDOMClassDefinitions ();
		writeDDProductDOMClassDefinitions.writeDDProductDOMClassDefnFiles(DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		if (DMDocument.debugFlag) System.out.println("debug writeAllDOMArtifacts - DOM Class Defn Done");
		}
		
		// write the registry configuration files
		RegConfig regConfig = new RegConfig ();
		regConfig.writeRegRIM(DMDocument.sTodaysDate);
		regConfig.writeRegRIM3(DMDocument.sTodaysDate);
		regConfig.writeRegRIM4(DMDocument.sTodaysDate);
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Regisry Config Done");
		
		// write the standard id extract file 
		WriteStandardIdExtract writeStandardIdExtract = new WriteStandardIdExtract ();
//		writeStandardIdExtract.writeExtractFile(DMDocument.sTodaysDate);
		writeStandardIdExtract.writeExtractFile();
		// DOM
		if (domFlag) {
		WriteDOMStandardIdExtract writeDOMStandardIdExtract = new WriteDOMStandardIdExtract ();
        writeDOMStandardIdExtract.writeExtractFile();
		}
		if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Standard Id Done");
		
		// print out the histogram for the DEC concepts
/*		System.out.println("\nConcept Histogram");
		Set <String> set1 = MasterInfoModel.metricConceptMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			Integer lCount = MasterInfoModel.metricConceptMap.get(lId);
			System.out.println("Descriptor: " + lId + "    Count: " + lCount);
		} */	
		return;
	}

	public void writeLDDArtifacts () throws java.io.IOException {	    
		ArrayList <PDSObjDefn> lLDDClassArr = new ArrayList <PDSObjDefn> ();
		TreeMap <String, PDSObjDefn> lLDDClassMap = new TreeMap <String, PDSObjDefn> ();
		
		// get LDD Classes
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.isFromLDD) {
				if (lClass.allAttrAssocArr.size() != 0) {
					lLDDClassArr.add(lClass);
					lLDDClassMap.put(lClass.title, lClass);
				}
			}
		}
		
		// get the LDD SchemaFileDefn - should be just one; but the Master must be skipped
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			
			// skip the master for LDD runs
			if (lSchemaFileDefn.isMaster) continue;
			
			//	write the schema - new version 4	
			XML4LabelSchema xml4LabelSchema = new XML4LabelSchema ();
			xml4LabelSchema.writeXMLSchemaFiles (lSchemaFileDefn, lLDDClassArr);
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - XML Schema - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");
			
			//  write schematron file
			WriteSchematron writeSchematron = new WriteSchematron ();
			writeSchematron.writeSchematronFile(lSchemaFileDefn, lLDDClassMap);
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Schematron - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");

			//  write label file for XML Schema and Schematron
			WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
			writeCoreXMLSchemaLabel.writeFile(lSchemaFileDefn);
			if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - Schema Label - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier + " - Done");

			// write the 11179 JSON file
			if (DMDocument.exportJSONFileFlag) {
				String lFileName = lSchemaFileDefn.relativeFileSpecModelJSON;	
				Write11179DDJSONFile write11179DDJSONFile = new Write11179DDJSONFile ();
				write11179DDJSONFile.writeJSONFile (lFileName);	
				
				WriteDOMDDJSONFile writeDOMDDJSONFile = new WriteDOMDDJSONFile ();
				writeDOMDDJSONFile.writeJSONFile ();	
				if (DMDocument.debugFlag) System.out.println("debug writeAllArtifacts - JSON Done");
			}

			// write the Info Spec file 
			if (DMDocument.exportSpecFileFlag) {
				WriteSpecification writeSpecification = new WriteSpecification (DMDocument.docInfo, PDSOptionalFlag); 
				writeSpecification.printArtifacts();
				if (DMDocument.debugFlag) System.out.println("debug writeLDDArtifacts - Info Model Spec Done");
			}
			//DOM
/*			if (DMDocument.exportSpecFileFlag) {
				WriteDOMSpecification writeDOMSpecification = new WriteDOMSpecification (DMDocument.docInfo, PDSOptionalFlag); 
				writeDOMSpecification.printArtifacts();
				if (DMDocument.debugFlag) System.out.println("debug writeDOMSpecification Done");
			} */
		    // write the Doc Book
			if (DMDocument.exportDDFileFlag) {
				WriteDocBook lWriteDocBook  = new WriteDocBook (); 
				lWriteDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
				
				WriteDOMDocBook lWriteDOMDocBook  = new WriteDOMDocBook (); 
				lWriteDOMDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);				
				if (DMDocument.debugFlag) System.out.println("debug writeLDDArtifacts - DD DocBook Done");
			}

			System.out.println(">>info    - LDDTOOL Exit");
		}
		return;
	}	 
}
