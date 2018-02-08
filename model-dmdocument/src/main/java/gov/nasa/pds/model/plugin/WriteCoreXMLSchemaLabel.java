package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class WriteCoreXMLSchemaLabel extends Object {
	PrintWriter prSchematron;

	public WriteCoreXMLSchemaLabel () {
		return;
	}
	
//	write the label for the XML schema and schematron file.
	public void writeFile (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		prSchematron = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lSchemaFileDefn.relativeFileSpecXMLLabel)), "UTF-8"));
		writeFileLines(lSchemaFileDefn, prSchematron);
		prSchematron.close();	
		return;
	}
	
//	write the schematron rules
	public void writeFileLines (SchemaFileDefn lSchemaFileDefn, PrintWriter prSchematron)  throws java.io.IOException {
		// set up the master schema identifier (namespaceid) - pds
		String lMasterFileId = DMDocument.masterPDSSchemaFileDefn.identifier;
		String lMasterFileIdUpper = lMasterFileId.toUpperCase();		
		
		// write the XML file header
		prSchematron.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		prSchematron.println("  <!-- PDS4 XML product label for " + "PDS4" + " V" + DMDocument.LDDToolSchemaVersionMapDots.get(DMDocument.masterNameSpaceIdNCLC) + "  " + DMDocument.masterTodaysDate + " -->");
		prSchematron.println("  <!-- Generated from the PDS4 Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - System Build " + DMDocument.XMLSchemaLabelBuildNum + " -->");
		prSchematron.println("  <!-- *** This PDS4 XML product label is an operational deliverable. *** -->");
		prSchematron.println("<?xml-model href=\"http://pds.nasa.gov/pds4/" +  lMasterFileId + "/v" + DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC) + "/" + "PDS4_" +  lMasterFileIdUpper + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".sch\"");		
		prSchematron.println("  schematypens=\"http://purl.oclc.org/dsdl/schematron\"?>");

		// write the product class
		prSchematron.println("<Product_XML_Schema xmlns=\"http://pds.nasa.gov/pds4/" +  lMasterFileId + "/v" + DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC) + "\"");
		prSchematron.println("    xmlns:pds=\"http://pds.nasa.gov/pds4/" +  lMasterFileId + "/v" + DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC) + "\"");
		prSchematron.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		prSchematron.println("    xsi:schemaLocation=\"http://pds.nasa.gov/pds4/" +  lMasterFileId + "/v" + DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC));		
		prSchematron.println("    http://pds.nasa.gov/pds4/" +  lMasterFileId + "/v" + DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC) + "/" + "PDS4_" +  lMasterFileIdUpper + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\">");		
		prSchematron.println("    <Identification_Area>");
		String lLID = "urn:nasa:pds:system_bundle:xml_schema:" +  lSchemaFileDefn.nameSpaceIdNCLC + "-xml_schema";
		prSchematron.println("        <logical_identifier>" + lLID.toLowerCase() + "</logical_identifier>");
		prSchematron.println("        <version_id>" + lSchemaFileDefn.labelVersionId + "</version_id>");
		prSchematron.println("        <title>PDS4 XML Schema" + " - " + lSchemaFileDefn.nameSpaceIdNCUC + " V" + lSchemaFileDefn.ont_version_id + "</title>");
		prSchematron.println("        <information_model_version>" + DMDocument.LDDToolSchemaVersionMapDots.get(DMDocument.masterNameSpaceIdNCLC) + "</information_model_version>");
		prSchematron.println("        <product_class>Product_XML_Schema</product_class>");
		prSchematron.println("        <Modification_History>");
		prSchematron.println("            <Modification_Detail>");
		prSchematron.println("                <modification_date>" + DMDocument.masterTodaysDateUTC + "</modification_date>");
		prSchematron.println("                <version_id>" + lSchemaFileDefn.labelVersionId + "</version_id>");
		prSchematron.println("                <description>This is the system generated PDS4 product label for PDS4 XML Schema and Schematron files.</description>");
		prSchematron.println("            </Modification_Detail>");
		prSchematron.println("        </Modification_History>");
		prSchematron.println("    </Identification_Area>");
		prSchematron.println("    <File_Area_XML_Schema>");
		prSchematron.println("        <File>");
		prSchematron.println("            <file_name>" + lSchemaFileDefn.relativeFileNameXMLSchema + "</file_name>");
		prSchematron.println("            <creation_date_time>" + DMDocument.masterTodaysDateTimeUTCwT + "</creation_date_time>");
		prSchematron.println("            <file_size unit=\"byte\">" + getFileSize (lSchemaFileDefn.relativeFileSpecXMLSchema) + "</file_size>");
		prSchematron.println("            <records>" + getFileNumRec (lSchemaFileDefn.relativeFileSpecXMLSchema) + "</records>");
		prSchematron.println("        </File>");
		prSchematron.println("        <XML_Schema>");
		prSchematron.println("            <name>" + lSchemaFileDefn.relativeFileNameXMLSchema + "</name>");
		prSchematron.println("            <offset unit=\"byte\">0</offset>");
		prSchematron.println("            <parsing_standard_id>XML Schema Version 1.1</parsing_standard_id>");
		prSchematron.println("            <description>This is a PDS4 XML Schema file for the declared namespace.</description>");
		prSchematron.println("        </XML_Schema>");
		prSchematron.println("    </File_Area_XML_Schema>");
		prSchematron.println("    <File_Area_XML_Schema>");
		prSchematron.println("        <File>");
		prSchematron.println("            <file_name>" + lSchemaFileDefn.relativeFileNameSchematron + "</file_name>");
		prSchematron.println("            <creation_date_time>" + DMDocument.masterTodaysDateTimeUTCwT + "</creation_date_time>");
		prSchematron.println("            <file_size unit=\"byte\">" + getFileSize (lSchemaFileDefn.relativeFileSpecSchematron) + "</file_size>");
		prSchematron.println("            <records>" + getFileNumRec (lSchemaFileDefn.relativeFileSpecSchematron) + "</records>");
		prSchematron.println("        </File>");
		prSchematron.println("        <XML_Schema>");
		prSchematron.println("            <name>" + lSchemaFileDefn.relativeFileNameSchematron + "</name>");
		prSchematron.println("            <offset unit=\"byte\">0</offset>");
		prSchematron.println("            <parsing_standard_id>Schematron ISO/IEC 19757-3:2006</parsing_standard_id>");
		prSchematron.println("            <description>This is the PDS4 Schematron file for the declared namespace. Schematron provides rule-based validation for XML Schema.</description>");
		prSchematron.println("        </XML_Schema>");
		prSchematron.println("    </File_Area_XML_Schema>");
		prSchematron.println("</Product_XML_Schema>");
	}
	
	//	get size metrics of a file
	public long getFileNumRec (String lFileSpecId) throws java.io.IOException {
//		FileInputStream stream = new FileInputStream(lFileSpecId);
		
		try {
			FileInputStream stream = new FileInputStream(lFileSpecId);
			byte[] buffer = new byte[8192];
			long count = 0;
			int n;
			while ((n = stream.read(buffer)) > 0) {
			    for (int i = 0; i < n; i++) {
			    	if (buffer[i] == '\n') count++;
			    }
			}
			stream.close();
//			System.out.println("Number of lines: " + count);	
			return count;
		} catch (Exception e) {    
			return 99999;
		}
	}
	
	//	get size metrics of a file
	public long getFileSize (String lFileSpecId) throws java.io.IOException {
		File file=new File (lFileSpecId);
		if (file.exists()) {
			return file.length();
		}
		return 99999;
	}
}
