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

	public void writeAllArtifacts () throws java.io.IOException {	    
	    // write the model specification
		WriteSpecification writeSpecification  = new WriteSpecification (DMDocument.docInfo, PDSOptionalFlag); 
		writeSpecification.printArtifacts();
		
		// Write the files consisting of individual classes
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.isSchema1Class) {
				WritePVLSchema writePVLLabel = new WritePVLSchema ();
				writePVLLabel.writePVLLabel (lClass, DMDocument.sTodaysDate);						

				//	write the WIKI schema
				WikiLabelSchema wikiLabelSchema = new WikiLabelSchema ();																
				wikiLabelSchema.writeWikiLabel (true, "unabridged2", lClass, DMDocument.sTodaysDate);				
			}
		}
		
		// get the schema file definitions
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());

		//	write the label schema - new version 4		
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			
			//	write the label schema			
			XML4LabelSchema xml4LabelSchema = new XML4LabelSchema ();
			xml4LabelSchema.writeXMLSchemaFiles (lSchemaFileDefn, InfoModel.masterMOFClassArr);
			
			//  write schematron file
			WriteSchematron writeSchematron = new WriteSchematron ();
			writeSchematron.writeSchematronFile(lSchemaFileDefn, InfoModel.masterMOFClassMap);
			
			//  write label file for XML Schema and Schematron
			WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
			writeCoreXMLSchemaLabel.writeFile(lSchemaFileDefn);
		}	
		
	    // write the Doc Book
		WriteDocBook lWriteDocBook  = new WriteDocBook (); 
		lWriteDocBook.writeDocBook(DMDocument.masterPDSSchemaFileDefn);
		
		// write the xmi file
		XMI2LabelSchema xmi2LabelSchema = new XMI2LabelSchema ();
		xmi2LabelSchema.getXMIElements ();
		xmi2LabelSchema.writeXMIFile (DMDocument.sTodaysDate);											

		// write the xmi file - original version with relationship names
		XMI2LabelSchema2 xmi2LabelSchema2 = new XMI2LabelSchema2 ();
		xmi2LabelSchema2.writeXMIFile (DMDocument.sTodaysDate);

		// write the RDF
		Write11179DDRDFFile write11179DDRDFFile = new Write11179DDRDFFile ();
		write11179DDRDFFile.printISO11179DDRDF (DMDocument.sTodaysDate);
				
		// write the Wiki files - Abridged (used to be called Unabridged2) - ws_classes excluded
		WikiDataDictAttrA3 wikiDataDictAttrA3 = new WikiDataDictAttrA3 ();
		wikiDataDictAttrA3.printISO11179DDWiki(true); // true -> abridged
		
		// write the PDS4 Attr CSV full file 
		WriteAttrCSVFiles writeAttrCSVFiles = new WriteAttrCSVFiles ();
//		writeAttrCSVFiles.printCSVFiles();
		writeAttrCSVFiles.printDDDBFile();
		
		// write the PDS4 DD CSV file 
		
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
		InfoModel.writeCSVFile (lSortClassArr, DMDocument.masterPDSSchemaFileDefn, null);
		
		// write the 11179 DD pins file
		Write11179DDPinsFile write11179DDPinsFile = new Write11179DDPinsFile ();
//		write11179DDPinsFile.writePINSFile ();	
//		write11179DDPinsFile.writePINSFile (DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179" + ".pins");	
//		write11179DDPinsFile.writePINSFile (DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_" + InfoModel.masterTodaysDateyymmdd + ".pins");	
		write11179DDPinsFile.writePINSFile (DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen" + ".pins");	
		write11179DDPinsFile.writePINSFile (DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen_" + DMDocument.masterTodaysDateyymmdd + ".pins");	
		
		// write the 11179 DD Data Element Definition XML Files
		WriteDDProductAttrDefinitions writeDDProductAttrDefinitions = new WriteDDProductAttrDefinitions ();
		writeDDProductAttrDefinitions.writeDDRegFiles (DMDocument.sTodaysDate);
		
		// write the 11179 DD Class Definition XML Files
		WriteDDProductClassDefinitions writeDDProductClassDefinitions = new WriteDDProductClassDefinitions ();
		writeDDProductClassDefinitions.writeDDProductClassDefnFiles(DMDocument.sTodaysDate);
		
		// write the registry configuration files
		RegConfig regConfig = new RegConfig ();
		regConfig.writeRegRIM(DMDocument.sTodaysDate);
		regConfig.writeRegRIM3(DMDocument.sTodaysDate);
		regConfig.writeRegRIM4(DMDocument.sTodaysDate);
		
		// write the Wiki Data Dictionary File
		WikiDataDict wikiDataDict = new WikiDataDict ();
		wikiDataDict.writeIntroWikiDDA3();
		wikiDataDict.writeDataTypesWikiFiles();
		
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
				lLDDClassArr.add(lClass);
				lLDDClassMap.put(lClass.title, lClass);
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
			
			//  write schematron file
			WriteSchematron writeSchematron = new WriteSchematron ();
			writeSchematron.writeSchematronFile(lSchemaFileDefn, lLDDClassMap);
			
			//  write label file for XML Schema and Schematron
			WriteCoreXMLSchemaLabel writeCoreXMLSchemaLabel = new WriteCoreXMLSchemaLabel ();
			writeCoreXMLSchemaLabel.writeFile(lSchemaFileDefn);

			System.out.println(">>info    - LDDTOOL Exit");
		}
		
		return;
	}	 
}
