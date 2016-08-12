package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class Write11179DDRDFFile extends Object{
	String kbId = "kb:";
		
	public Write11179DDRDFFile () {
		return;
	}
	
//	print the ISO 11179 DD in RDF
	public void printISO11179DDRDF (String todaysDate) throws java.io.IOException {
		String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRDF;
		PrintWriter pr11179 = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		write11179Hdr (todaysDate, pr11179);
		printAttrISODERDF (pr11179);
		printAttrISOCD (pr11179);
		printAttrISODEC (pr11179);
		pr11179.println("</rdf:RDF>");
		pr11179.close();
		return;
	}	

	/**
	* Print the ISO Data Element 
	*/
	private void printAttrISODERDF (PrintWriter pr11179) {
		// print the data elements
///		ArrayList <AttrDefn> lSortedAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
///		for (Iterator<AttrDefn> i = lSortedAttrArr.iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! (lAttr.isUsedInClass && lAttr.isAttribute)) continue;
			pr11179.println("<" + kbId + "DataElement rdf:about=\"&kb;" + lAttr.deDataIdentifier + "\"");
			pr11179.println("	 " + kbId + "dataIdentifier=\"" + lAttr.deDataIdentifier + "\"");
			pr11179.println("	 " + kbId + "versionIdentifier=\"" + lAttr.versionIdentifierValue + "\"");
			pr11179.println("	 rdfs:label=\"" + lAttr.deDataIdentifier + "\">");
			pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + lAttr.registeredByValue + "\"/>");
//			pr11179.println("	<" + kbId + "typedBy rdf:resource=\"&kb;RC_ZZTEMP\"/>");
			pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + DMDocument.stewardValue + "\"/>");
			pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + DMDocument.submitterValue + "\"/>");
			pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;" + lAttr.teDataIdentifier+ "\"/>");
			pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + lAttr.administrationRecordValue + "\"/>");
			pr11179.println("	<" + kbId + "expressedBy rdf:resource=\"&kb;" + "DEC_" + lAttr.classConcept + "\"/>");
			if (lAttr.isEnumerated)
				pr11179.println("	<" + kbId + "representing rdf:resource=\"&kb;" + lAttr.evdDataIdentifier + "\"/>");
			else
				pr11179.println("	<" + kbId + "representing rdf:resource=\"&kb;" + lAttr.nevdDataIdentifier + "\"/>");
			pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + lAttr.registrationAuthorityIdentifierValue + "\"/>");
			pr11179.println("</" + kbId + "DataElement>");
			pr11179.println(" ");
			
			// print VD
			printAttrISOVD (lAttr, pr11179);
			if (lAttr.isEnumerated) printAttrISOPVVM (lAttr, pr11179);
			
			printAttrISOTermEntry (lAttr, pr11179);
		}
	}
	
	/**
	* Print the ISO Value Domain
	*/
	private void printAttrISOVD (AttrDefn lAttr, PrintWriter pr11179) {
			
			if (lAttr.isEnumerated)
				pr11179.println("<" + kbId + "EnumeratedValueDomain rdf:about=\"&pdsns;" + lAttr.evdDataIdentifier + "\"");
			else
				pr11179.println("<" + kbId + "NonEnumeratedValueDomain rdf:about=\"&pdsns;" + lAttr.nevdDataIdentifier + "\"");

			pr11179.println("  " + kbId + "dataIdentifier=\"" + lAttr.dataIdentifier + "\"");
			pr11179.println("  " + kbId + "versionIdentifier=\"" + lAttr.versionIdentifierValue + "\"");
//			pr11179.println("  " + kbId + "maximumCharacterQuantity=\"255\"");

			pr11179.println("  " + kbId + "maximumCharacterQuantity=\"" + lAttr.maximum_characters + "\"");
			pr11179.println("  " + kbId + "minimumCharacterQuantity=\"" + lAttr.minimum_characters + "\"");
			pr11179.println("  " + kbId + "maximumValue=\"" + lAttr.maximum_value + "\"");
			pr11179.println("  " + kbId + "minimumValue=\"" + lAttr.minimum_value + "\"");
			pr11179.println("  " + kbId + "pattern=\"" + InfoModel.escapeXMLChar(lAttr.pattern) + "\"");
			pr11179.println("  " + kbId + "valueDomainFormat=\"" + lAttr.format + "\"");
//			pr11179.println("  " + kbId + "unitOfMeasure=\"" + lAttr.unit_of_measure_type + "\"");			
			pr11179.println("  " + kbId + "defaultUnitId=\"" + lAttr.default_unit_id + "\"");		
			
			pr11179.println("  rdfs:label=\"" + lAttr.dataIdentifier + "\">");		
			pr11179.println("  <" + kbId + "representedBy1 rdf:resource=\"&pdsns;" + lAttr.deDataIdentifier + "\"/>");
			pr11179.println("  <" + kbId + "representedBy2 rdf:resource=\"&pdsns;" + "CD_" + lAttr.dataConcept  + "\"/>");		
			if (lAttr.isEnumerated) {
				for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
					PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
		 			int lValueHashCodeI = lPermValueDefn.value.hashCode();
					String lValueHashCodeS = Integer.toString(lValueHashCodeI);
					pr11179.println("  <" + kbId + "containedIn1 rdf:resource=\"&pdsns;" + lAttr.pvDataIdentifier + "_" + lValueHashCodeS  + "\"/>");
				}
			}
			pr11179.println("  <" + kbId + "datatype rdf:resource=\"&pdsns;" + lAttr.valueType + "\"/>");
			pr11179.println("  <" + kbId + "unitOfMeasure rdf:resource=\"&pdsns;" + lAttr.unit_of_measure_type + "\"/>");
			pr11179.println("  <" + kbId + "registeredBy rdf:resource=\"&pdsns;" + lAttr.registeredByValue + "\"/>");
			pr11179.println("  <" + kbId + "steward rdf:resource=\"&pdsns;" + lAttr.steward + "\"/>");
			pr11179.println("  <" + kbId + "submitter rdf:resource=\"&pdsns;" + lAttr.submitter + "\"/>");
			pr11179.println("  <" + kbId + "terminologicalEntry rdf:resource=\"&pdsns;" + lAttr.teDataIdentifier + "\"/>");
			pr11179.println("  <" + kbId + "administrationRecord rdf:resource=\"&pdsns;" + lAttr.administrationRecordValue + "\"/>");
			pr11179.println("  <" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + lAttr.registrationAuthorityIdentifierValue + "\"/>");
			if (lAttr.isEnumerated) 
				pr11179.println("</" + kbId + "EnumeratedValueDomain>");
			else
				pr11179.println("</" + kbId + "NonEnumeratedValueDomain>");
			pr11179.println(" ");
	}	

	/**
	* Print the ISO Permissible Value and Value Meaning
	*/
	private void printAttrISOPVVM (AttrDefn lAttr, PrintWriter pr11179) {
		for (Iterator <PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {				 // for each standard value
			PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
			
 			int lValueHashCodeI = lPermValueDefn.value.hashCode();
			String lValueHashCodeS = Integer.toString(lValueHashCodeI);

			int lMeaningHashCodeI = lPermValueDefn.value_meaning.hashCode();
			String lMeaningHashCodeS = Integer.toString(lMeaningHashCodeI);

			// Permissible Values
			pr11179.println("<" + kbId + "PermissibleValue rdf:about=\"&pdsns;" + lAttr.pvDataIdentifier + "_" + lValueHashCodeS + "\"");
			pr11179.println("	 " + kbId + "beginDate=\"" + lPermValueDefn.value_begin_date + "\"");
			pr11179.println("	 " + kbId + "endDate=\"" + lPermValueDefn.value_end_date + "\"");
			pr11179.println("	 " + kbId + "value=\"" + lPermValueDefn.value + "\"");
			pr11179.println("	 rdfs:label=\"" + lAttr.pvDataIdentifier + "_" + lValueHashCodeS + "\">");
			pr11179.println("	<" + kbId + "containing1 rdf:resource=\"&pdsns;" + lAttr.evdDataIdentifier + "\"/>");
			pr11179.println("	<" + kbId + "usedIn rdf:resource=\"&pdsns;" + lAttr.vmDataIdentifier + "_" + lMeaningHashCodeS + "\"/>");
			pr11179.println("</" + kbId + "PermissibleValue>");
			pr11179.println(" ");
						
			// Value Meanings			
			pr11179.println("<" + kbId + "ValueMeaning rdf:about=\"&pdsns;" + lAttr.vmDataIdentifier + "_" + lMeaningHashCodeS + "\"");
			pr11179.println("	 " + kbId + "beginDate=\"" + lPermValueDefn.value_begin_date + "\"");
			pr11179.println("	 " + kbId + "description=\"" + InfoModel.escapeXMLChar(lPermValueDefn.value_meaning) + "\"");
			pr11179.println("	 " + kbId + "endDate=\"" + lPermValueDefn.value_end_date + "\"");
			pr11179.println("	 rdfs:label=\"" + lAttr.vmDataIdentifier + "_" + lMeaningHashCodeS + "\"/>");
			pr11179.println(" ");
		}
	}
	
	/**
	* Print the ISO Terminological Entry
	*/
	private void printAttrISOTermEntry (AttrDefn lAttr, PrintWriter pr11179) {

		//Designation			
		pr11179.println("<" + kbId + "Designation rdf:about=\"" + kbId + "" + lAttr.desDataIdentifier + "\"");
		pr11179.println("	 " + kbId + "designationName=\"" + lAttr.dataIdentifier + "\"");
//		pr11179.println("	 " + kbId + "isPreferred=\"" + lAttr.desIsPreferred + "\"");
		pr11179.println("	 " + kbId + "isPreferred=\"" + "TRUE" + "\"");
		pr11179.println("	 rdfs:label=\"" + lAttr.desDataIdentifier + "\"/>");
		pr11179.println(" ");
		
	//Definition		
		pr11179.println("<" + kbId + "Definition rdf:about=\"&pdsns;" + lAttr.defDataIdentifier + "\"");
//		pr11179.println("	 " + kbId + "isPreferred=\"" + lTE.defIsPreferred + "\"");
		pr11179.println("	 " + kbId + "isPreferred=\"" + "TRUE" + "\"");
		pr11179.println("	 rdfs:label=\"" + lAttr.defDataIdentifier + "\">");
		pr11179.println("	<" + kbId + "definitionText>" + InfoModel.escapeXMLChar(lAttr.description) + "</" + kbId + "definitionText>");
		pr11179.println("</" + kbId + "Definition>");
		pr11179.println(" ");
			
	//Language Section			
		pr11179.println("<" + kbId + "LanguageSection rdf:about=\"&pdsns;" + lAttr.lsDataIdentifier + "\"");
		pr11179.println("	 rdfs:label=\"" + lAttr.lsDataIdentifier + "\">");
		pr11179.println("	<" + kbId + "definition rdf:resource=\"&pdsns;" + lAttr.defDataIdentifier  + "\"/>");
		pr11179.println("	<" + kbId + "designation rdf:resource=\"&pdsns;" + lAttr.desDataIdentifier + "\"/>");
		pr11179.println("	<" + kbId + "sectionLanguage rdf:resource=\"&pdsns;" + "Englsh" + "\"/>");
		pr11179.println("</" + kbId + "LanguageSection>");
		pr11179.println(" ");
		
	//Terminological Entry		
		pr11179.println("<" + kbId + "TerminologicalEntry rdf:about=\"&pdsns;" + lAttr.teDataIdentifier + "\"");
		pr11179.println("  rdfs:label=\"" + lAttr.teDataIdentifier + "\">");
		pr11179.println("  <" + kbId + "partioning rdf:resource=\"&pdsns;" + lAttr.lsDataIdentifier + "\"/>");
		pr11179.println("  <" + kbId + "administeredItemContext rdf:resource=\"&pdsns;NASA_PDS\"/>");
		pr11179.println("</" + kbId + "TerminologicalEntry>");	
		pr11179.println(" ");
	}
	
	/**
	* Print the ISO Conceptual Domains
	*/
	private void printAttrISOCD (PrintWriter pr11179) {
		ArrayList <IndexDefn> lCDAttrArr = new ArrayList <IndexDefn> (InfoModel.cdAttrMap.values());
		for (Iterator <IndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndexDefn = (IndexDefn) i.next();
 
			String lCDId = "CD" + "." + lIndexDefn.identifier;
			pr11179.println("<" + kbId + "ConceptualDomain rdf:about=\"&pdsns;" + lCDId + "\"");
			pr11179.println("	 " + kbId + "dataIdentifier=\"" + lIndexDefn.identifier + "\"");
//			pr11179.println("	 " + kbId + "versionIdentifier=\"" + lConceptualDomainAttr.versionIdentifierValue + "\"");
			pr11179.println("	 " + kbId + "versionIdentifier=\"" + "1.0.0.0" + "\"");
			pr11179.println("	 rdfs:label=\"" + lIndexDefn.identifier + "\">");
				
			for (Iterator<String> j = lIndexDefn.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				pr11179.println("	<" + kbId + "having rdf:resource=\"&pdsns;" + "DEC."  + lVal + "\"/>");
			}
			for (Iterator<AttrDefn> j = lIndexDefn.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.isEnumerated) {
					pr11179.println("	<" + kbId + "representing2 rdf:resource=\"&pdsns;" + lAttr.evdDataIdentifier + "\"/>");
				} else {
					pr11179.println("	<" + kbId + "representing2 rdf:resource=\"&pdsns;" + lAttr.nevdDataIdentifier + "\"/>");
				}
			}
			
	/*		for (Iterator <String> i = lCD.containedIn2Arr.iterator(); i.hasNext();) {
				String lval = (String) i.next();
				pr11179.println("	<" + kbId + "containedIn2 rdf:resource=\"&pdsns;" + lval + "\"/>");
			} */
			pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&pdsns;" + DMDocument.administrationRecordValue + "\"/>");
			pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&pdsns;" + DMDocument.registeredByValue + "\"/>");
			pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + DMDocument.registrationAuthorityIdentifierValue + "\"/>");
			pr11179.println("	<" + kbId + "steward rdf:resource=\"&pdsns;" + DMDocument.stewardValue + "\"/>");
			pr11179.println("	<" + kbId + "submitter rdf:resource=\"&pdsns;" + DMDocument.submitterValue + "\"/>");
			pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&pdsns;" + "TE." + lIndexDefn.identifier + "\"/>");
			pr11179.println("</" + kbId + "ConceptualDomain>");
			pr11179.println(" ");
		}
	}	
	
	/**
	* Print the ISO Data Element Concepts
	*/
	private void printAttrISODEC (PrintWriter pr11179) {
		ArrayList <IndexDefn> lDECAttrArr = new ArrayList <IndexDefn> (InfoModel.decAttrMap.values());
		for (Iterator <IndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndexDefn = (IndexDefn) i.next();

			String lDECId = "DEC" + "." + lIndexDefn.identifier;
			pr11179.println("<" + kbId + "DataElementConcept rdf:about=\"&pdsns;" + lDECId + "\"");
			pr11179.println("	 " + kbId + "dataIdentifier=\"" + lIndexDefn.identifier + "\"");
//			pr11179.println("	 " + kbId + "versionIdentifier=\"" + lConceptualDomainAttr.versionIdentifierValue + "\"");
			pr11179.println("	 " + kbId + "versionIdentifier=\"" + "1.0.0.0" + "\"");
			pr11179.println("	 rdfs:label=\"" + lIndexDefn.identifier + "\">");
				
			for (Iterator<AttrDefn> j = lIndexDefn.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				pr11179.println("	<" + kbId + "expressing rdf:resource=\"&pdsns;" + lAttr.deDataIdentifier + "\"/>");
			}
			
			for (Iterator<String> j = lIndexDefn.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				pr11179.println("	<" + kbId + "specifying rdf:resource=\"&pdsns;" + "CD."  + lVal + "\"/>");
			}

	/*		for (Iterator <String> i = lCD.containedIn2Arr.iterator(); i.hasNext();) {
				String lval = (String) i.next();
				pr11179.println("	<" + kbId + "containedIn2 rdf:resource=\"&pdsns;" + lval + "\"/>");
			} */
			pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&pdsns;" + DMDocument.administrationRecordValue + "\"/>");
			pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&pdsns;" + DMDocument.registeredByValue + "\"/>");
			pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + DMDocument.registrationAuthorityIdentifierValue + "\"/>");
			pr11179.println("	<" + kbId + "steward rdf:resource=\"&pdsns;" + DMDocument.stewardValue + "\"/>");
			pr11179.println("	<" + kbId + "submitter rdf:resource=\"&pdsns;" + DMDocument.submitterValue + "\"/>");
			pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&pdsns;" + "TE." + lIndexDefn.identifier + "\"/>");
			pr11179.println("</" + kbId + "DataElementConcept>");
			pr11179.println(" ");
		}
	}	
			
	// write the ISO/IEC 11179 Header
	public void write11179Hdr (String todaysDate, PrintWriter pr11179) {
		pr11179.println("<?xml version='1.0' encoding='UTF-8'?>");
		pr11179.println("<!-- PDS4 Planetary Science Data Dictionary - Master Model Dump - " + todaysDate + " -->");
		pr11179.println("<!DOCTYPE rdf:RDF [");
		pr11179.println("	 <!ENTITY rdf 'http://www.w3.org/1999/02/22-rdf-syntax-ns#'>");
		pr11179.println("	 <!ENTITY kb 'http://protege.stanford.edu/kb#'>");
		pr11179.println("	 <!ENTITY pdsns 'http://pds.nasa.gov/pds4/pds/v1#'>");
		pr11179.println("	 <!ENTITY rdfs 'http://www.w3.org/2000/01/rdf-schema#'>");
		pr11179.println("]>");
		pr11179.println("<rdf:RDF xmlns:rdf=\"&rdf;\"");
		pr11179.println("	 xmlns:kb=\"&kb;\"");
		pr11179.println("	 xmlns:rdfs=\"&rdfs;\">");
		pr11179.println(" ");
/*		pr11179.println("<&kb;AdministrationRecord rdf:about=\"&kb;" + administrationRecordValue + "\"");
		pr11179.println("	 " + kbId + "administrativeNote=\"Test load from PDS4 Master Model\"");
		pr11179.println("	 " + kbId + "administrativeStatus=\"Final\"");
		pr11179.println("	 " + kbId + "changeDescription=\"No changes have been made to PSDD content.\"");
		pr11179.println("	 " + kbId + "creationDate=\"" + creationDateValue + "\"");
		pr11179.println("	 " + kbId + "effectiveDate=\"" + creationDateValue + "\"");
		pr11179.println("	 " + kbId + "lastChangeDate=\"" + creationDateValue + "\"");
		pr11179.println("	 " + kbId + "origin=\"Planetary Data System\"");
		pr11179.println("	 " + kbId + "registrationStatus=\"Preferred\"");
		pr11179.println("	 " + kbId + "unresolvedIssue=\"Issues still being determined.\"");
		pr11179.println("	 " + kbId + "untilDate=\"" + futureDateValue + "\"");
		pr11179.println("	 rdfs:label=\"" + administrationRecordValue + "\">");
		pr11179.println("	<" + kbId + "explanatoryComment>This is a test load of a ISO/IEC 11179 Data Dictionary from the PDS4 Master model.</" + kbId + "explanatoryComment>");
		pr11179.println("</" + kbId + "AdministrationRecord>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "Steward rdf:about=\"&kb;" + stewardValue + "\"");
		pr11179.println("	 rdfs:label=\"" + stewardValue + "\">");
		pr11179.println("	<" + kbId + "contact rdf:resource=\"&kb;Elizabeth_Rye\"/>");
		pr11179.println("	<" + kbId + "organization rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("</" + kbId + "Steward>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "Submitter rdf:about=\"&kb;" + submitterValue + "\"");
		pr11179.println("	 rdfs:label=\"" + submitterValue + "\">");
		pr11179.println("	<" + kbId + "contact rdf:resource=\"&kb;Elizabeth_Rye\"/>");
		pr11179.println("	<" + kbId + "organization rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("</" + kbId + "Submitter>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "ClassificationScheme rdf:about=\"&kb;System_Classification_Id\"");
		pr11179.println("	 " + kbId + "classification_scheme_type_name=\"Keyword\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"System_Classification_Id\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"System_Classification_Id\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TE_System_Classification_Id\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_common\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_isis\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_jpl-ammos\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_mars-observer\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-atmos\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-cn\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-geo-mgn\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-img-gll\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-mer-ops\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-ppi\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-rings\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_pds-sbn\"/>");
		pr11179.println("	<" + kbId + "containing3 rdf:resource=\"&kb;csitv2_spice\"/>");
		pr11179.println("</" + kbId + "ClassificationScheme>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RegistrationAuthority rdf:about=\"&kb;" + registeredByValue + "\"");
		pr11179.println("	 " + kbId + "organizationMailingAddress=\"4800 Oak Grove Drive\"");
		pr11179.println("	 " + kbId + "organizationName=\"NASA Planetary Data System\"");
		pr11179.println("	 rdfs:label=\"" + registeredByValue + "\">");
		pr11179.println("	<" + kbId + "documentationLanguageIdentifier rdf:resource=\"&kb;LI_English\"/>");
		pr11179.println("	<" + kbId + "languageUsed rdf:resource=\"&kb;LI_English\"/>");
		pr11179.println("	<" + kbId + "registrar rdf:resource=\"&kb;PDS_Registrar\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier_v rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RegistrationAuthority>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_COUNT\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_COUNT\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_COUNT\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_COUNT\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_IDENTIFIER\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_IDENTIFIER\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_IDENTIFIER\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_IDENTIFIER\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_MASK\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_MASK\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_MASK\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_MASK\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_NAME\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_NAME\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_NAME\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_NAME\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_TEXT\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_TEXT\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_TEXT\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_TEXT\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_UTC_DATE\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_UTC_DATE\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_DATE\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_UTC_DATE\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_UTC_DATE_TIME\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_UTC_DATE_TIME\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_DATE_TIME\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_UTC_DATE_TIME\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_UTC_TIME\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_UTC_TIME\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_TIME\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_UTC_TIME\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<" + kbId + "RepresentationClass rdf:about=\"&kb;RC_ZZTEMP\"");
		pr11179.println("	 " + kbId + "dataIdentifier=\"RC_ZZTEMP\"");
		pr11179.println("	 " + kbId + "versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_ZZTEMP\">");
		pr11179.println("	<" + kbId + "registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<" + kbId + "steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<" + kbId + "submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<" + kbId + "terminologicalEntry rdf:resource=\"&kb;TERC_ZZTEMP\"/>");
		pr11179.println("	<" + kbId + "administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<" + kbId + "registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</" + kbId + "RepresentationClass>"); */
		pr11179.println(" ");
	}
}
