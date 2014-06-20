package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

/**
 * Write the attribute definition products 
 *   
 */

class WriteDDProductClassDefinitions extends Object{
	PrintWriter prDDReg;
	String uId, uIdFileName;

	public WriteDDProductClassDefinitions () {
		return;
	}

	// write the Product Class Definition files
	public void writeDDProductClassDefnFiles (String todaysDate) throws java.io.IOException {
				
		// cycle once for each data element
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			uId = DMDocument.registrationAuthorityIdentifierValue + ":" + lClass.steward + ":" + lClass.title;
//			uIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_" + lClass.steward + "_" + lClass.title;
			uId = DMDocument.registrationAuthorityIdentifierValue + ":" + lClass.nameSpaceIdNC + ":" + lClass.title;
			uIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_" + lClass.nameSpaceIdNC + "_" + lClass.title;
//			prDDReg = new PrintWriter(new FileWriter("SchemaClassDef/" + uIdFileName + "_" + InfoModel.lab_version_id + ".xml", false));
			File targetDir = new File(DMDocument.outputDirPath + "SchemaClassDef");
			targetDir.mkdirs();
			prDDReg = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaClassDef/" + uIdFileName + "_" + InfoModel.lab_version_id + ".xml", false));
			writeDDProductClassDefnFile(prDDReg, todaysDate, uId, lClass);
			prDDReg.close();
		}
	}	
	
	// Print the class Definition Header
	public void writeDDProductClassDefnFile (PrintWriter prDDReg, String todaysDate, String uId, PDSObjDefn lClass) {
		prDDReg.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		prDDReg.println("<Product_Class_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + "\"");
		prDDReg.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + " PDS4_PDS_" + InfoModel.lab_version_id + ".xsd\">");
		prDDReg.println("    <Identification_Area>");
		prDDReg.println("        <logical_identifier>" + "urn:nasa:pds:" + uId + "</logical_identifier>");
		prDDReg.println("        <version_id>" + InfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <title>" + lClass.title + "</title>");
		prDDReg.println("        <information_model_version>" + InfoModel.ont_version_id + "</information_model_version>");
		prDDReg.println("        <product_class>Product_Class_Definition</product_class>");
		prDDReg.println("    </Identification_Area>");
//		prDDReg.println("    <Reference_List>");
//		prDDReg.println("        <Internal_Reference>");
//		prDDReg.println("            <lid_reference>" + "urn:nasa:pds:pds4_data_dictionary_" + InfoModel.lab_version_id + "</lid_reference>");
//		prDDReg.println("            <reference_type>member_of</reference_type>");
//		prDDReg.println("        </Internal_Reference>");
//		prDDReg.println("    </Reference_List>");
		prDDReg.println("    <DD_Class_Full>");
		prDDReg.println("        <name>" + lClass.title + "</name>");
		prDDReg.println("        <version_id>" + InfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <local_identifier>" + "urn:nasa:pds:" + uId + "</local_identifier>");
		prDDReg.println("        <steward_id>" + lClass.steward + "</steward_id>");
		prDDReg.println("        <type>" + "PDS4" + "</type>");
//		prDDReg.println("        <name_space_id>" + lClass.nameSpaceIdNC + "</name_space_id>");
		prDDReg.println("        <namespace_id>" + lClass.nameSpaceIdNC + "</namespace_id>");
//		prDDReg.println("        <submitter_id>" + lClass.steward + "</submitter_id>");
		prDDReg.println("        <submitter_name>" + lClass.steward + "</submitter_name>");
		prDDReg.println("        <definition>" + lClass.description + "</definition>");
		prDDReg.println("        <registered_by>" + "RA_0001_NASA_PDS_1" + "</registered_by>");
		prDDReg.println("        <registration_authority_id>" + DMDocument.registrationAuthorityIdentifierValue + "</registration_authority_id>");
		prDDReg.println("        <abstract_flag>" + lClass.isAbstract + "</abstract_flag>");
//		prDDReg.println("        <choice_flag>" + lClass.isChoice + "</choice_flag>");

		for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();			
			prDDReg.println("        <DD_Association>");
			String lUId = DMDocument.registrationAuthorityIdentifierValue + ":" + lAttr.classNameSpaceIdNC + ":" + lAttr.className + ":" + lAttr.attrNameSpaceIdNC + ":" + lAttr.title;
//			prDDReg.println("           <local_identifier>" + lAttr.title + "</local_identifier>");
			prDDReg.println("           <local_identifier>" + "urn:nasa:pds:" + lUId + "</local_identifier>");
			prDDReg.println("           <reference_type>" + "attribute_of" + "</reference_type>");
			prDDReg.println("           <minimum_occurrences>" + lAttr.cardMin + "</minimum_occurrences>");
			prDDReg.println("           <maximum_occurrences>" + lAttr.cardMax + "</maximum_occurrences>");
			prDDReg.println("        </DD_Association>");
		}
		for (Iterator<AttrDefn> j = lClass.inheritedAttribute.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();			
			prDDReg.println("        <DD_Association>");
			String lUId = DMDocument.registrationAuthorityIdentifierValue + ":" + lAttr.classNameSpaceIdNC + ":" + lAttr.className + ":" + lAttr.attrNameSpaceIdNC + ":" + lAttr.title;
//			prDDReg.println("           <local_identifier>" + lAttr.title + "</local_identifier>");
			prDDReg.println("           <local_identifier>" + "urn:nasa:pds:" + lUId + "</local_identifier>");
			prDDReg.println("           <reference_type>" + "attribute_of" + "</reference_type>");
			prDDReg.println("           <minimum_occurrences>" + lAttr.cardMin + "</minimum_occurrences>");
			prDDReg.println("           <maximum_occurrences>" + lAttr.cardMax + "</maximum_occurrences>");
			prDDReg.println("        </DD_Association>");
		}
		for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			if (lAttr.valArr.isEmpty()) {
				continue;
			}
			for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
				String lTitle = (String) k.next();
				PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
				if (lClassMember == null) {
					continue;
				}		
				String lUId = DMDocument.registrationAuthorityIdentifierValue + ":" + lClassMember.steward + ":" + lClassMember.title;
				prDDReg.println("        <DD_Association>");
				prDDReg.println("           <local_identifier>" + "urn:nasa:pds:" + lUId  + "</local_identifier>");
				prDDReg.println("           <reference_type>" + "subclass_of" + "</reference_type>");
				prDDReg.println("           <minimum_occurrences>" + lAttr.cardMin + "</minimum_occurrences>");
				prDDReg.println("           <maximum_occurrences>" + lAttr.cardMax + "</maximum_occurrences>");
				prDDReg.println("        </DD_Association>");
			}
		}
		for (Iterator<AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			if (lAttr.valArr.isEmpty()) {
				continue;
			}
			for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
				String lTitle = (String) k.next();
				PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
				if (lClassMember == null) {
					continue;
				}		
				String lUId = DMDocument.registrationAuthorityIdentifierValue + ":" + lClassMember.steward + ":" + lClassMember.title;
				prDDReg.println("        <DD_Association>");
				prDDReg.println("           <local_identifier>" + "urn:nasa:pds:" + lUId  + "</local_identifier>");
				prDDReg.println("           <reference_type>" + "subclass_of" + "</reference_type>");
				prDDReg.println("           <minimum_occurrences>" + lAttr.cardMin + "</minimum_occurrences>");
				prDDReg.println("           <maximum_occurrences>" + lAttr.cardMax + "</maximum_occurrences>");
				prDDReg.println("        </DD_Association>");
			}
		}
		prDDReg.println("        <Terminological_Entry>");
		prDDReg.println("            <name>" + lClass.title + "</name>");
		prDDReg.println("            <definition>" + lClass.description + "</definition>");
		prDDReg.println("            <language>" + "English" + "</language>");
		prDDReg.println("            <preferred_flag>" + "true" + "</preferred_flag>");
		prDDReg.println("        </Terminological_Entry>");
			
		prDDReg.println("    </DD_Class_Full>");		
		prDDReg.println("</Product_Class_Definition>");
		prDDReg.println("");
	}
}
