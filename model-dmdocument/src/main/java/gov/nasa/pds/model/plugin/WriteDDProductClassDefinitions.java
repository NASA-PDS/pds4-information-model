package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Write the attribute definition products 
 *   
 */

class WriteDDProductClassDefinitions extends Object{
	PrintWriter prDDReg;
// 1.4	String uId, uIdFileName;

	public WriteDDProductClassDefinitions () {
		return;
	}

	// write the Product Class Definition files
	public void writeDDProductClassDefnFiles (SchemaFileDefn lSchemaFileDefn, String todaysDate) throws java.io.IOException {
				
		// cycle once for each data element
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			String lLID = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
//			lLID = "urn:nasa:pds:" + "context:" + "class:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
			lLID = "urn:nasa:pds:" + "context:" + "class:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
			lLID = lLID.toLowerCase();
			String lUIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_" + lClass.nameSpaceIdNC + "_" + lClass.title;
			String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecClassDefn + lUIdFileName + "_" + lSchemaFileDefn.lab_version_id + ".xml";
			prDDReg = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));

			writeDDProductClassDefnFile(lSchemaFileDefn, prDDReg, todaysDate, lLID, lClass);
			prDDReg.close();
		}
	}	
	
	// Print the class Definition Header
	public void writeDDProductClassDefnFile (SchemaFileDefn lSchemaFileDefn, PrintWriter prDDReg, String todaysDate, String lLID, PDSObjDefn lClass) {
		prDDReg.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		prDDReg.println("<Product_Class_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + "\"");
		prDDReg.println("<Product_Class_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "\"");
		prDDReg.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
//		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + "/PDS4_PDS_" + InfoModel.lab_version_id + ".xsd\">");
		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
		prDDReg.println("    <Identification_Area>");
		prDDReg.println("        <logical_identifier>" + lLID + "</logical_identifier>");
//		prDDReg.println("        <version_id>" + InfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <version_id>" + lSchemaFileDefn.identifier_version_id + "</version_id>");
		prDDReg.println("        <title>" + lClass.title + "</title>");
		prDDReg.println("        <information_model_version>" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</information_model_version>");
		prDDReg.println("        <product_class>Product_Class_Definition</product_class>");
		prDDReg.println("    </Identification_Area>");
		prDDReg.println("    <DD_Class_Full>");
		prDDReg.println("        <name>" + lClass.title + "</name>");
//		prDDReg.println("        <version_id>" + InfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <version_id>" + lSchemaFileDefn.identifier_version_id + "</version_id>");
		prDDReg.println("        <local_identifier>" + lLID + "</local_identifier>");
		prDDReg.println("        <steward_id>" + lClass.steward + "</steward_id>");
		prDDReg.println("        <type>" + "PDS4" + "</type>");
		prDDReg.println("        <namespace_id>" + lClass.nameSpaceIdNC + "</namespace_id>");
		prDDReg.println("        <submitter_name>" + lClass.steward + "</submitter_name>");
		prDDReg.println("        <definition>" + lClass.description + "</definition>");
		prDDReg.println("        <registered_by>" + "RA_0001_NASA_PDS_1" + "</registered_by>");
		prDDReg.println("        <registration_authority_id>" + DMDocument.registrationAuthorityIdentifierValue + "</registration_authority_id>");
		prDDReg.println("        <abstract_flag>" + lClass.isAbstract + "</abstract_flag>");

		for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();		
			if (! lAttr.isAttribute) continue;
			prDDReg.println("        <DD_Association>");
			String lAttrLID = DMDocument.registrationAuthorityIdentifierValue + ":" + lAttr.classNameSpaceIdNC + ":" + lAttr.parentClassTitle + ":" + lAttr.attrNameSpaceIdNC + ":" + lAttr.title;
			lAttrLID = "urn:nasa:pds:" + "context:" + "attribute:" + lAttrLID; 
			lAttrLID = lAttrLID.toLowerCase();
			prDDReg.println("           <local_identifier>" + "urn:nasa:pds:" + lAttrLID + "</local_identifier>");
			prDDReg.println("           <reference_type>" + "attribute_of" + "</reference_type>");
			prDDReg.println("           <minimum_occurrences>" + lAttr.cardMin + "</minimum_occurrences>");
			prDDReg.println("           <maximum_occurrences>" + lAttr.cardMax + "</maximum_occurrences>");
			prDDReg.println("        </DD_Association>");
		}

		for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			if (lAttr.isAttribute) continue;
			for (Iterator<PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {
				PDSObjDefn lClassMember = (PDSObjDefn) k.next();	
				String lClassLID = DMDocument.registrationAuthorityIdentifierValue + ":" + lClassMember.nameSpaceIdNC + ":" + lClassMember.title;
				lClassLID = "urn:nasa:pds:" + "context:" + "class:" + lClassLID; 
				lClassLID = lClassLID.toLowerCase();
				prDDReg.println("        <DD_Association>");
				prDDReg.println("           <local_identifier>" + lClassLID + "</local_identifier>");
				prDDReg.println("           <reference_type>" + "component_of" + "</reference_type>");
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
