package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Write the attribute definition products 
 *   
 */

//class WriteDDProductAttrDefinitions extends ISO11179MDR{
class WriteDDProductAttrDefinitions extends Object{
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
	PrintWriter prDDReg;
	public WriteDDProductAttrDefinitions () {
		return;
	}

	// write the Data Element Product files
	public void writeDDRegFiles (SchemaFileDefn lSchemaFileDefn, String todaysDate) throws java.io.IOException {
		// get the permissible values for attribute concept

		// cycle through classes to get only the used attributes
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("%3ANAME") == 0) continue;
				if (! lAttr.isAttribute) continue;
				String lLID = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.parentClassTitle + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.title;
				lLID = "urn:nasa:pds:" + "context:" + "attribute:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
				lLID = lLID.toLowerCase();
				String lUIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.attrNameSpaceIdNC + "_" + lAttr.title;
//				String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecAttrDefn + lUIdFileName + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xml";
				String lFileName = lSchemaFileDefn.relativeFileSpecAttrDefn + lUIdFileName + "_" + lSchemaFileDefn.lab_version_id + ".xml_MOF";
				prDDReg = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));	
				printDDRegFile(lSchemaFileDefn, prDDReg, todaysDate, lLID, lAttr);
				prDDReg.close();
			}
		}
	}	
	
	// Print the Element Definition Header
	public void printDDRegFile (SchemaFileDefn lSchemaFileDefn, PrintWriter prDDReg, String todaysDate, String lLID, AttrDefn lAttr) {
		prDDReg.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		prDDReg.println("<Product_Attribute_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + "\"");
		prDDReg.println("<Product_Attribute_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "\"");
		prDDReg.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
//		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + InfoModel.ns_version_id + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
		prDDReg.println("    <Identification_Area>");
		prDDReg.println("        <logical_identifier>" + lLID + "</logical_identifier>");
		prDDReg.println("        <version_id>" + lAttr.versionIdentifierValue + "</version_id>");
		prDDReg.println("        <title>" + lAttr.title + "</title>");
		prDDReg.println("        <information_model_version>" + lSchemaFileDefn.ont_version_id + "</information_model_version>");
		prDDReg.println("        <product_class>Product_Attribute_Definition</product_class>");
		prDDReg.println("    </Identification_Area>");
		prDDReg.println("    <DD_Attribute_Full>");
		prDDReg.println("        <name>" + lAttr.title + "</name>");
		prDDReg.println("        <version_id>" + lAttr.versionIdentifierValue + "</version_id>");
		prDDReg.println("        <class_name>" + lAttr.parentClassTitle + "</class_name>");
		prDDReg.println("        <local_identifier>" + lLID + "</local_identifier>");
		prDDReg.println("        <steward_id>" + lAttr.steward + "</steward_id>");
		prDDReg.println("        <type>" + "PDS4" + "</type>");
		prDDReg.println("        <namespace_id>" + lAttr.attrNameSpaceIdNC + "</namespace_id>");
		prDDReg.println("        <nillable_flag>" + lAttr.isNilable + "</nillable_flag>");
		prDDReg.println("        <submitter_name>" + lAttr.submitter + "</submitter_name>");
		prDDReg.println("        <definition>" + lAttr.description + "</definition>");
		prDDReg.println("        <registered_by>" + lAttr.registeredByValue + "</registered_by>");
		prDDReg.println("        <registration_authority_id>" + lAttr.registrationAuthorityIdentifierValue + "</registration_authority_id>");
		if (lAttr.classConcept.indexOf("TBD") != 0)
			prDDReg.println("        <attribute_concept>" + lAttr.classConcept + "</attribute_concept>");
		
		prDDReg.println("        <Terminological_Entry>");
		prDDReg.println("            <name>" + lAttr.title + "</name>");
		prDDReg.println("            <definition>" + lAttr.description + "</definition>");
		prDDReg.println("            <language>" + "English" + "</language>");
		prDDReg.println("            <preferred_flag>" + "true" + "</preferred_flag>");
		prDDReg.println("        </Terminological_Entry>");
		
		prDDReg.println("        <DD_Value_Domain_Full>");
		
		// new code using attr defn methods
		String pEnumFlag = "false";
		if (lAttr.isEnumerated) {
			pEnumFlag = "true";
		}
		prDDReg.println("            <enumeration_flag>" + pEnumFlag + "</enumeration_flag>");
		prDDReg.println("            <value_data_type>" + lAttr.valueType + "</value_data_type>");	
		String lFormat = lAttr.getFormat (true);
		if (lFormat.indexOf("TBD") != 0)
			prDDReg.println("            <formation_rule>" + lFormat + "</formation_rule>");
		prDDReg.println("            <minimum_characters>" + lAttr.getMinimumCharacters2 (true, true) + "</minimum_characters>");
		prDDReg.println("            <maximum_characters>" + lAttr.getMaximumCharacters2 (true, true) + "</maximum_characters>");
		prDDReg.println("            <minimum_value>" + lAttr.getMinimumValue2 (true, true)+ "</minimum_value>");
		prDDReg.println("            <maximum_value>" + lAttr.getMaximumValue2 (true, true) + "</maximum_value>");
//		prDDReg.println("            <pattern>" + InfoModel.unEscapeProtegeChar(lAttr.getPattern(true)) + "</pattern>");
		String lPattern = InfoModel.unEscapeProtegeString(lAttr.getPattern(true));
		if (lPattern.indexOf("TBD") != 0)
			prDDReg.println("            <pattern>" + lPattern + "</pattern>");
		prDDReg.println("            <unit_of_measure_type>" + lAttr.getUnitOfMeasure (true) + "</unit_of_measure_type>");
		if (lAttr.dataConcept.indexOf("TBD") != 0)
			prDDReg.println("            <conceptual_domain>" + lAttr.dataConcept + "</conceptual_domain>");
		prDDReg.println("            <specified_unit_id>" + lAttr.getDefaultUnitId (true) + "</specified_unit_id>");
		
		if (lAttr.isEnumerated) {
			for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValue = (PermValueDefn) j.next();
				if (lPermValue != null) {
					prDDReg.println("            <DD_Permissible_Value_Full>");
					prDDReg.println("                <value>" + lPermValue.value + "</value>");
					if (lPermValue.value_meaning.indexOf("TBD") != 0) {
						prDDReg.println("                <value_meaning>" + InfoModel.escapeXMLChar(lPermValue.value_meaning) + "</value_meaning>");
					}
					prDDReg.println("                <value_begin_date>" + DMDocument.beginDatePDS4Value + "T00:00:00Z" + "</value_begin_date>");
					prDDReg.println("                <value_end_date>" + DMDocument.endDateValue + "T00:00:00Z" + "</value_end_date>");
					prDDReg.println("            </DD_Permissible_Value_Full>");
				}
			}
		}
//		prDDReg.println("            <conceptual_domain>" + lAttr.dataConcept + "</conceptual_domain>");

		prDDReg.println("        </DD_Value_Domain_Full>");
		prDDReg.println("    </DD_Attribute_Full>");		
		prDDReg.println("</Product_Attribute_Definition>");
		prDDReg.println("");
	}

	// *** needs conversion ***
	// Print the the Protege Pins Misc
	public  void printPDDPMISC (PrintWriter prDDReg) {
		// print the Miscellaneous records
		prDDReg.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
		prDDReg.println("	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
		prDDReg.println("	(administrativeStatus Final)");
		prDDReg.println("	(changeDescription \"PSDD content has been merged into the model.\")");
		prDDReg.println("	(creationDate \"2010-03-10\")");
		prDDReg.println("	(effectiveDate \"2010-03-10\")");
		prDDReg.println("	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
		prDDReg.println("	(lastChangeDate \"2010-03-10\")");
		prDDReg.println("	(origin \"Planetary Data System\")");
		prDDReg.println("	(registrationStatus Preferred)");
		prDDReg.println("	(unresolvedIssue \"Issues still being determined.\")");
		prDDReg.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");
		
		prDDReg.println("([" + DMDocument.registrationAuthorityIdentifierValue + "] of RegistrationAuthorityIdentifier");
		prDDReg.println("	(internationalCodeDesignator \"0001\")");
		prDDReg.println("	(opiSource \"1\")");
		prDDReg.println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
		prDDReg.println("	(organizationPartIdentifier \"Planetary Data System\"))");

		prDDReg.println("([" + DMDocument.registeredByValue + "] of RegistrationAuthority");
		prDDReg.println("	(documentationLanguageIdentifier [LI_English])");
		prDDReg.println("	(languageUsed [LI_English])");
		prDDReg.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
		prDDReg.println("	(organizationName \"NASA Planetary Data System\")");
		prDDReg.println("	(registrar [PDS_Registrar])");
		prDDReg.println("	(registrationAuthorityIdentifier_v [" + DMDocument.registrationAuthorityIdentifierValue + "]))");
	
		prDDReg.println("([NASA_PDS] of Context");
		prDDReg.println("	(dataIdentifier  \"NASA_PDS\"))");
		
		prDDReg.println("([PDS_Registrar] of  Registrar");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(registrarIdentifier \"PDS_Registrar\"))");
		
		prDDReg.println("([Steward_PDS] of Steward");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDReg.println("([pds] of Steward");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDReg.println("([img] of Steward");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDReg.println("([rings] of Steward");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDReg.println("([ops] of Steward");
		prDDReg.println("	(contact [PDS_Standards_Coordinator])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDReg.println("([Submitter_PDS] of Submitter");
		prDDReg.println("	(contact [DataDesignWorkingGroup])");
		prDDReg.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
		prDDReg.println("([PDS_Standards_Coordinator] of Contact");
		prDDReg.println("	(contactTitle \"PDS_Standards_Coordinator\")");
		prDDReg.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
		prDDReg.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
		prDDReg.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDReg.println("	(contactPhone \"818.354.6135\")");
		prDDReg.println("	(contactName \"Elizabeth Rye\"))");
		
		prDDReg.println("([DataDesignWorkingGroup] of Contact");
		prDDReg.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
		prDDReg.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDReg.println("	(contactPhone \"818.354.9338\")");
		prDDReg.println("	(contactName \"J. Steven Hughes\"))");
		
		prDDReg.println("([LI_English] of LanguageIdentification");
		prDDReg.println("  (countryIdentifier \"USA\")");
		prDDReg.println("  (languageIdentifier \"English\"))");
		
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();		
			prDDReg.println("([" + lUnit.title + "] of UnitOfMeasure");
			prDDReg.println("	(measureName \"" + lUnit.title + "\")");
			prDDReg.println("	(defaultUnitId \"" + lUnit.default_unit_id + "\"))");
			prDDReg.println("	(precision \"" + "TBD_precision" + "\"))");
			prDDReg.print("	(unitId ");
			// set the units
			for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				prDDReg.println("	\"" + lVal + "\" )");
			}
			prDDReg.println("))");
		}
		
		// print data types
		for (Iterator<PDSObjDefn> i = InfoModel.masterDataTypesArr.iterator(); i.hasNext();) {
			PDSObjDefn lDataType = (PDSObjDefn) i.next();	
			prDDReg.println("([" + lDataType.title + "] of DataType");
			prDDReg.println("  (dataTypeName \"" + lDataType.title + "\")");
			prDDReg.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
}
