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

import java.util.*;

/** Driver for Customized Document Writers
 *
 */
public class ExportModelsCustom extends Object {
	
	public ExportModelsCustom () {
		// noop
	}

/**********************************************************************************************************
		write custom documents and files
***********************************************************************************************************/

	public void writeArtifacts (boolean lLDDToolFlag, SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {	 
		DMDocument.registerMessage ("0>info " + "ExportModelsCustom - Specification Done");
						
		// need to pass SchemaFileDefn to get namespace; set static for now.
		// SchemaFileDefn lSchemaFileDefn;
		
//		System.out.println("debug ExportModelsCustom lLDDToolFlag:" + lLDDToolFlag);
//		System.out.println("debug ExportModelsCustom DMDocument.exportDDFileFlag:" + DMDocument.exportDDFileFlag);
//		System.out.println("debug ExportModelsCustom DMDocument.exportCustomFileFlag:" + DMDocument.exportCustomFileFlag);
		
		// write the doc book files - one per namespace id
		if (lLDDToolFlag) {
			if (DMDocument.exportDDFileFlag) {
				DMDocument.dmProcessState.setRelativeFileSpecDDDocXML (DMDocument.masterLDDSchemaFileDefn);
				WriteDOMDocBookAnon lWriteDOMDocBooks  = new WriteDOMDocBookAnon (); 
				lWriteDOMDocBooks.writeDocBooks(DMDocument.masterPDSSchemaFileDefn);
				DMDocument.registerMessage ("0>info " + "writeLDDArtifacts - DD DocBooks - One Per LDD -  Done");
			}
		}
		
	    // write the standard id extract file
	    // WriteDOMStandardIdExtract writeDOMStandardIdExtract = new WriteDOMStandardIdExtract();
		// writeDOMStandardIdExtract.writeExtractFile();
		// DMDocument.registerMessage("0>info " + "writeAllArtifacts - Standard Id Done");
		
	    // write the registry configuration files *** Reg needs conversion.
		//   RegConfig regConfig = new RegConfig();
		//   regConfig.writeRegRIM(DMDocument.sTodaysDate);
		//   regConfig.writeRegRIM3(DMDocument.sTodaysDate);
		//   regConfig.writeRegRIM4(DMDocument.sTodaysDate);
		// DMDocument.registerMessage("0>info " + "writeAllArtifacts - Regisry Config Done");
		
	    // write the IM to RDF
	    // WriteDOMIMToRDF lWriteDOMIMToRDF = new WriteDOMIMToRDF ();
	    // lWriteDOMIMToRDF.domIMToRDFWriter (DMDocument.sTodaysDate);

	    // write the Lucid Ingest file
	    // WriteLucidMySQLFiles WriteLucidFiles = new WriteLucidMySQLFiles ();
	    // WriteLucidFiles.WriteLucidFile();

	    // write the PDS4 CCSDS CSV file
	    // WriteDocCSV writeDocCSV = new WriteDocCSV ();
	    // writeDocCSV.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
	    // DMDocument.registerMessage ("0>info " + "writeAllArtifacts - CCSDS CSV Done");

	    // print out the histogram for the DEC concepts
	    /*
	     * System.out.println("\nConcept Histogram"); Set <String> set1 =
	     * MasterDOMInfoModel.metricConceptMap.keySet(); Iterator <String> iter1 = set1.iterator();
	     * while(iter1.hasNext()) { String lId = (String) iter1.next(); Integer lCount =
	     * MasterDOMInfoModel.metricConceptMap.get(lId); System.out.println("Descriptor: " + lId +
	     * "    Count: " + lCount); }
	     */
		
		// write the new xmi file
/*		if (! lLDDToolFlag) {
			DMDocument.dmProcessState.setRelativeFileSpecUMLXMI (lSchemaFileDefn);
			WriteUML25XMIFile lWriteUML25XMIFile = new WriteUML25XMIFile ();
			lWriteUML25XMIFile.writeXMIFile (configureClassesToWriteList_PDS4_Common (), DMDocument.sTodaysDate);			
			DMDocument.registerMessage ("0>info " + "ExportModelsCustom - UML 25 XMI File - Common - Done");
		} else {
			ArrayList <DOMClass> lClassesToWrite = configureClassesToWriteList_LDD_Proc ();
			DMDocument.dmProcessState.setRelativeFileSpecUMLXMI (lSchemaFileDefn);
			WriteUML25XMIFile lWriteUML25XMIFile = new WriteUML25XMIFile ();
			lWriteUML25XMIFile.writeXMIFile (lClassesToWrite, DMDocument.sTodaysDate);
			DMDocument.registerMessage ("0>info " + "ExportModelsCustom - UML 25 XMI File - LDD - Done");
		}
		*/
		
		// **** NEO4J *** write the Neo4J file
/*		String lClassificationTypeNeo4J = "PDS4.All.Products.Class.Prop";   // default
		if (! lLDDToolFlag) {
			lClassificationTypeNeo4J = "PDS4.All.Products.Class.Prop";   // default
//			lClassificationTypeNeo4J = "TNDO_Identified";   // default
//			lClassificationTypeNeo4J = "EIMEntity";   // default
//			lClassificationTypeNeo4J = "OAISIF";   // default
			ClassAttrPropClassification lCAPC = new ClassAttrPropClassification (lClassificationTypeNeo4J);
			DMDocument.dmProcessState.setRelativeFileSpecUMLXMI (lSchemaFileDefn);
			WriteNeo4J lWriteNeo4J = new WriteNeo4J ();
			lWriteNeo4J.writeNeo4JFile (lClassificationTypeNeo4J, lCAPC, DMDocument.sTodaysDate);
			DMDocument.registerMessage ("0>info " + "ExportModelsCustom - " + lClassificationTypeNeo4J + " - Neo4J File Done");
			
		} else {
			lClassificationTypeNeo4J = ("PDS4.LDD.All");
			ClassAttrPropClassification lCAPC = new ClassAttrPropClassification (lClassificationTypeNeo4J);
			DMDocument.dmProcessState.setRelativeFileSpecUMLXMI (lSchemaFileDefn);
			WriteNeo4J lWriteNeo4J = new WriteNeo4J ();
			lWriteNeo4J.writeNeo4JFile ("ObjectClassHierarchy", lCAPC, DMDocument.sTodaysDate);
			DMDocument.registerMessage ("0>info " + "ExportModelsCustom - " + lClassificationTypeNeo4J + " - Neo4J File Done");
		} */
		
		
		// write the DOM RDF
		/*
		WriteDOM11179DDRDFFile writeDOM11179DDRDFFile = new WriteDOM11179DDRDFFile ();
		writeDOM11179DDRDFFile.printISO11179DDRDF (DMDocument.sTodaysDate);
		DMDocument.registerMessage ("0>info " + "ExportModelsCustom - RDF Done");
		*/
		
		// write the IM to RDF
		/*
		WriteDOMIMToRDF lWriteDOMIMToRDF = new WriteDOMIMToRDF ();
		lWriteDOMIMToRDF.domIMToRDFWriter (DMDocument.sTodaysDate);
		*/
		
		// write the Lucid Ingest file
		/*
		WriteLucidMySQLFiles WriteLucidFiles = new WriteLucidMySQLFiles ();
		WriteLucidFiles.WriteLucidFile();
		*/
		
		// write the terminological entry files
/*		WriteDOMTermEntryJSON writeDOMTermEntryJSON = new WriteDOMTermEntryJSON ();
		writeDOMTermEntryJSON.WriteDOMTermEntries (DMDocument.masterPDSSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "WriteDOMTermEntryJSON -  Done"); */

		
		// write the PDS4 CCSDS CSV file 
		/*
		WriteDocCSV writeDocCSV = new WriteDocCSV ();
		writeDocCSV.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "ExportModelsCustom - CCSDS CSV Done");
		*/		
				
		// write the PDS4 CCSDS CSV file  - altDefinition
/*		DMDocument.dmProcessState.setRelativeFileSpecCCSDSCSV (lSchemaFileDefn);
		WriteCCSDSDoc writeCCSDSDoc = new WriteCCSDSDoc ();
		writeCCSDSDoc.writeDocCSV (DMDocument.masterPDSSchemaFileDefn);
		DMDocument.registerMessage ("0>info " + "ExportModelsCustom - CCSDS CSV Alt Definition Done");
		
		// write the LOD SKOS file
		WriteLODSKOSFileDOM writeLODSKOSDOMFile = new WriteLODSKOSFileDOM ();
		writeLODSKOSDOMFile.writeDOMSKOSFile (DMDocument.masterPDSSchemaFileDefn.relativeFileSpecSKOSTTL_DOM);
		DMDocument.registerMessage ("0>info " + "ExportModelsCustom - SKOS Done"); */

		// write the 11179 DOM DD Class Definition XML Files
		/*
		WriteDDProductDOMClassDefinitions writeDDProductDOMClassDefinitions = new WriteDDProductDOMClassDefinitions ();
		writeDDProductDOMClassDefinitions.writeDDProductDOMClassDefnFiles(DMDocument.masterPDSSchemaFileDefn, DMDocument.sTodaysDate);
		DMDocument.registerMessage ("0>info " + "writeAllDOMArtifacts - DOM Class Defn Done");
		DMDocument.registerMessage ("0>info " + "writeAllDOMArtifacts - DOM Attr Defn Done");
		*/
			
		return;
	}
	
	// configured lists
	
	// PDS Common Dictionary --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_PDS4_Common () {
		// Dans entity list
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo("pds") == 0)) continue;
			if (lClass.identifier.indexOf("PDS3") > -1) continue;
			classesToWrite.add (lClass);
		}
		return classesToWrite;
	}
	
	// PDS Common Context --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_PDS4_Common_Contextxxx () {
		ArrayList <String> classTitlesToWrite = new ArrayList <String> ();
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();
		classTitlesToWrite.add ("Product_Context");
		classTitlesToWrite.add ("Agency");
		classTitlesToWrite.add ("Airborne");
		classTitlesToWrite.add ("Facility");
		classTitlesToWrite.add ("Instrument");
		classTitlesToWrite.add ("Instrument_Host");
		classTitlesToWrite.add ("Investigation");
		classTitlesToWrite.add ("Node");
		classTitlesToWrite.add ("Other");
		classTitlesToWrite.add ("PDS_Affiliate");
		classTitlesToWrite.add ("PDS_Guest");
		classTitlesToWrite.add ("Resource");
		classTitlesToWrite.add ("Target");
		classTitlesToWrite.add ("Telescope");
		for (Iterator<String> i = classTitlesToWrite.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("pds", lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {
				if (lClass.isInactive) continue;
				classesToWrite.add (lClass);
			}
		}
		return classesToWrite;
	}
	
	// PDS LDD -- DISP - set namespace below - replace method name above ---
	public ArrayList <DOMClass> configureClassesToWriteList_LDD_DISPxxx () {
		// the entity list
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo("disp") == 0)) continue;
			classesToWrite.add (lClass);
		}
		return classesToWrite;
	}
	
	
	// PDS LDD -- PROC - set namespace below - replace method name above ---
	public ArrayList <DOMClass> configureClassesToWriteList_LDD_Proc () {
		// the entity list
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo("proc") == 0)) continue;
			classesToWrite.add (lClass);
		}
		return classesToWrite;
	}	
	
	// PDS LDD -- Geom --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_PDS4_LDD () {
		// The entity list
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo("geom") == 0)) continue;
			classesToWrite.add (lClass);
		}
		return classesToWrite;
	}
	
	
	// EIM Classes --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_EIMxxx () {
//		System.out.println("\ndebug configureClassesToWriteList_EIM");

		ArrayList <String> classTitlesToWrite = new ArrayList <String> ();
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if ((lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo("eim") == 0)) continue;
			classesToWrite.add (lClass);
		}
		return classesToWrite;
	}

	
	// Selecte EIM Classes --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_EIM_Entity_Titles () {
		ArrayList <String> classTitlesToWrite = new ArrayList <String> ();
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();
		classTitlesToWrite.add ("Digital_Artifact");
		classTitlesToWrite.add ("Bill_Of_Materials");
		classTitlesToWrite.add ("Costs");
		classTitlesToWrite.add ("Data");
		classTitlesToWrite.add ("Digital_Artifact");
		classTitlesToWrite.add ("Document");
		classTitlesToWrite.add ("Domain");
		classTitlesToWrite.add ("Facility");
		classTitlesToWrite.add ("Hardware");
		classTitlesToWrite.add ("Instrument");
		classTitlesToWrite.add ("Mission");
		classTitlesToWrite.add ("Model");
		classTitlesToWrite.add ("Organization");
		classTitlesToWrite.add ("Project");
		classTitlesToWrite.add ("Research");
		classTitlesToWrite.add ("Role");
		classTitlesToWrite.add ("Task");
		classTitlesToWrite.add ("Vendor");
		classTitlesToWrite.add ("Hardware");
		classTitlesToWrite.add ("Person");
		classTitlesToWrite.add ("Service");
		classTitlesToWrite.add ("Work_Activity");
		classTitlesToWrite.add ("Program");
		classTitlesToWrite.add ("Authority");
		classTitlesToWrite.add ("Steward");
		classTitlesToWrite.add ("Instrument_Host");
		classTitlesToWrite.add ("ProjectOrWorkPackage");
		classTitlesToWrite.add ("Work_Package");
		classTitlesToWrite.add ("Spacecraft");
		
		for (Iterator<String> i = classTitlesToWrite.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("eim", lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {
				if (lClass.isInactive) continue;
				classesToWrite.add (lClass);
			}
		}
		return classesToWrite;
	}
	
	// Selecte EIM Classes --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_EIM_Titlesxxx () {
		ArrayList <String> classTitlesToWrite = new ArrayList <String> ();
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();
		classTitlesToWrite.add ("Access_Rights_Information");
		classTitlesToWrite.add ("Archival_Information_Package");
		classTitlesToWrite.add ("Artifact");
		classTitlesToWrite.add ("Artifact_Type_Area");
		classTitlesToWrite.add ("BillOfMaterials");
		classTitlesToWrite.add ("Context_Information");
		classTitlesToWrite.add ("Costs");
		classTitlesToWrite.add ("Data_Object");
		classTitlesToWrite.add ("Date_Time_Area");
		classTitlesToWrite.add ("Digital_Object");
		classTitlesToWrite.add ("Digital_Object_Handle");
		classTitlesToWrite.add ("Dissemination_Information_Package");
		classTitlesToWrite.add ("Document");
		classTitlesToWrite.add ("Domain");
		classTitlesToWrite.add ("Domain_Area");
		classTitlesToWrite.add ("Entity");
		classTitlesToWrite.add ("Facility");
		classTitlesToWrite.add ("Fixity_Information");
		classTitlesToWrite.add ("Format");
		classTitlesToWrite.add ("Format_Type_Area");
		classTitlesToWrite.add ("Hardware");
		classTitlesToWrite.add ("Identification_Information");
		classTitlesToWrite.add ("Information_Model");
		classTitlesToWrite.add ("Information_Object");
		classTitlesToWrite.add ("Information_Package");
		classTitlesToWrite.add ("Information_Package_Collection");
		classTitlesToWrite.add ("Instrument");
		classTitlesToWrite.add ("Instrument_Host");
		classTitlesToWrite.add ("Investigation");
		classTitlesToWrite.add ("Mission");
		classTitlesToWrite.add ("Model");
		classTitlesToWrite.add ("Name_Type_List_Area");
		classTitlesToWrite.add ("Native_Area");
		classTitlesToWrite.add ("Organization");
		classTitlesToWrite.add ("People");
		classTitlesToWrite.add ("Project");
		classTitlesToWrite.add ("Provenance_Information");
		classTitlesToWrite.add ("Reference_Information");
		classTitlesToWrite.add ("Reference_Information_Collection");
		classTitlesToWrite.add ("Representation_Information");
		classTitlesToWrite.add ("Research");
		classTitlesToWrite.add ("Role");
		classTitlesToWrite.add ("Science_Investigation_Area");
		classTitlesToWrite.add ("Service");
		classTitlesToWrite.add ("Submission_Information_Package");
		classTitlesToWrite.add ("Task");
		classTitlesToWrite.add ("Vendor");
		classTitlesToWrite.add ("Work_Activity");

/*		classTitlesToWrite.add ("Entify");
		classTitlesToWrite.add ("Artifact");
		classTitlesToWrite.add ("People");
		classTitlesToWrite.add ("Mission");
		classTitlesToWrite.add ("Instrument");
		classTitlesToWrite.add ("Project");
		classTitlesToWrite.add ("Data");
		classTitlesToWrite.add ("Model");
		classTitlesToWrite.add ("Document");
		classTitlesToWrite.add ("Service");
		classTitlesToWrite.add ("Facility");
		classTitlesToWrite.add ("Costs");
		classTitlesToWrite.add ("Organization");
		classTitlesToWrite.add ("Vendor");
		classTitlesToWrite.add ("Hardware");
		classTitlesToWrite.add ("BillOfMaterials");
		classTitlesToWrite.add ("Research");
		classTitlesToWrite.add ("Concepts");
		classTitlesToWrite.add ("Domain");
		classTitlesToWrite.add ("Role");
		classTitlesToWrite.add ("Steward"); */
		
		for (Iterator<String> i = classTitlesToWrite.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("eim", lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {
				if (lClass.isInactive) continue;
				classesToWrite.add (lClass);
			}
		}
		return classesToWrite;
	}
	
	// OAISIF selected classes --- set namespace below ---
	public ArrayList <DOMClass> configureClassesToWriteList_OAISIFxxx () {
		ArrayList <String> classTitlesToWrite = new ArrayList <String> ();
		ArrayList <DOMClass> classesToWrite = new ArrayList <DOMClass> ();

		classTitlesToWrite.add ("Abstraction_Layer");
		classTitlesToWrite.add ("Abstraction_Layer_Mapping");
		classTitlesToWrite.add ("Abstraction_Layer_OAIS_DIP");
		classTitlesToWrite.add ("Abstraction_Layer_OAIS_SIP");
		classTitlesToWrite.add ("Abstraction_Layer_PDS4");

		classTitlesToWrite.add ("Access");
		classTitlesToWrite.add ("Access_Rights_Information");
		classTitlesToWrite.add ("Access_Service");
		classTitlesToWrite.add ("Access_Aid");
		classTitlesToWrite.add ("Archival_Storage");
		classTitlesToWrite.add ("Finding_Aid");
		classTitlesToWrite.add ("Ordering_Aid");
		classTitlesToWrite.add ("Retrieving_Aid");
		classTitlesToWrite.add ("Archival_Information_Package");
		classTitlesToWrite.add ("Consumer");
		classTitlesToWrite.add ("Content_Information");
		classTitlesToWrite.add ("Context_Information");
		classTitlesToWrite.add ("Dissemination_Information_Package");
		classTitlesToWrite.add ("External_Object");
		classTitlesToWrite.add ("Fixity_Information");
		classTitlesToWrite.add ("Functional_Entity");
		classTitlesToWrite.add ("Information_Object");
		classTitlesToWrite.add ("Information_Package");
		classTitlesToWrite.add ("Ingest");
		classTitlesToWrite.add ("Interaction_Pattern");
		classTitlesToWrite.add ("Invoke");
		classTitlesToWrite.add ("MessagedService");
		classTitlesToWrite.add ("receiveMessage");
		classTitlesToWrite.add ("sendMessage");
		classTitlesToWrite.add ("Order_Agreement");
		classTitlesToWrite.add ("Preservation_Description_Information");
		classTitlesToWrite.add ("Producer");
		classTitlesToWrite.add ("Progress");
		classTitlesToWrite.add ("Protocol");
		classTitlesToWrite.add ("Provenance_Information");
		classTitlesToWrite.add ("PublishSubscribe");
		classTitlesToWrite.add ("Reference_Information");
		classTitlesToWrite.add ("Representation_Information");
		classTitlesToWrite.add ("Request");
		classTitlesToWrite.add ("Send");
		classTitlesToWrite.add ("Service");
		classTitlesToWrite.add ("Submission_Information_Package");
		classTitlesToWrite.add ("Submit");	
//		classTitlesToWrite.add ("putAccessRightsInformation");
//		classTitlesToWrite.add ("putContextInformation");
//		classTitlesToWrite.add ("putIdentificationInformation");
//		classTitlesToWrite.add ("putFixityInformation");
//		classTitlesToWrite.add ("putProvenanceInformation");
//		classTitlesToWrite.add ("putPreservationDescriptionInformation");
//		classTitlesToWrite.add ("putRepresentationInformation");
//		classTitlesToWrite.add ("putReferenceInformation");
//		classTitlesToWrite.add ("setPID");
//		classTitlesToWrite.add ("setDataObject");
//		classTitlesToWrite.add ("getDataObject");
//		classTitlesToWrite.add ("getRepresentationInformation");
//		classTitlesToWrite.add ("getReferenceInformation");
//		classTitlesToWrite.add ("getPID");
//		classTitlesToWrite.add ("getPreservationDescriptionInformation");
//		classTitlesToWrite.add ("getProvenanceInformation");
//		classTitlesToWrite.add ("getIdentificationInformation");
//		classTitlesToWrite.add ("getFixityInformation");
//		classTitlesToWrite.add ("getContextInformation");
//		classTitlesToWrite.add ("getAccessRightsInformation");
		
		for (Iterator<String> i = classTitlesToWrite.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("oais", lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {
				if (lClass.isInactive) continue;
				classesToWrite.add (lClass);
			}
		}
		return classesToWrite;
	}
}
