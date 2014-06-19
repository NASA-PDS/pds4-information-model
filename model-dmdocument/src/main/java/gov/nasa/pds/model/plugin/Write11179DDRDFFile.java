package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class Write11179DDRDFFile extends Object{
		
	public Write11179DDRDFFile () {
		return;
	}
	
//	print the ISO 11179 DD in RDF
	public void printISO11179DDRDF (String todaysDate) throws java.io.IOException {
		PrintWriter pr11179 = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "PDS4ISO11179.txt", false));
		write11179Hdr (todaysDate, pr11179);
//		printAttrISOTermEntry (pr11179);
//		printAttrISOPVVM (pr11179);
//		printAttrISOCD (pr11179);
//		printAttrISOVD (pr11179);
//		printAttrISODEC (pr11179);
		printAttrISODERDF (pr11179);
		pr11179.println("</rdf:RDF>");
		pr11179.close();
		return;
	}	
	
	/**
	* Print the ISO Terminological Entry
	*/
	private void printAttrISOTermEntry (PrintWriter pr11179) {
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lTE = (AttrDefn) i.next();
			if (! (lTE.isUsedInModel && lTE.isAttribute)) continue;
			
			//Designation			
			pr11179.println("<isons:Designation rdf:about=\"kb:" + lTE.desDataIdentifier + "\"");
			pr11179.println("	 isons:designationName=\"" + lTE.dataIdentifier + "\"");
//			pr11179.println("	 isons:isPreferred=\"" + lTE.desIsPreferred + "\"");
			pr11179.println("	 isons:isPreferred=\"" + "TRUE" + "\"");
			pr11179.println("	 rdfs:label=\"" + lTE.desDataIdentifier + "\"/>");
			pr11179.println(" ");
			
		//Definition		
			pr11179.println("<isons:Definition rdf:about=\"&pdsns;" + lTE.defDataIdentifier + "\"");
//			pr11179.println("	 isons:isPreferred=\"" + lTE.defIsPreferred + "\"");
			pr11179.println("	 isons:isPreferred=\"" + "TRUE" + "\"");
			pr11179.println("	 rdfs:label=\"" + lTE.defDataIdentifier + "\">");
			pr11179.println("	<isons:definitionText>" + lTE.description + "</isons:definitionText>");
//			pr11179.println("	<isons:definitionText>" + lTE.definition + "</isons:definitionText>");
			pr11179.println("</isons:Definition>");
			pr11179.println(" ");
				
		//Language Section			
			pr11179.println("<isons:LanguageSection rdf:about=\"&pdsns;" + lTE.lsDataIdentifier + "\"");
			pr11179.println("	 rdfs:label=\"" + lTE.lsDataIdentifier + "\">");
			pr11179.println("	<isons:definition rdf:resource=\"&pdsns;" + lTE.defDataIdentifier  + "\"/>");
			pr11179.println("	<isons:designation rdf:resource=\"&pdsns;" + lTE.desDataIdentifier + "\"/>");
//			pr11179.println("	<isons:sectionLanguage rdf:resource=\"&pdsns;" + lTE.sectionLanguage + "\"/>");
			pr11179.println("	<isons:sectionLanguage rdf:resource=\"&pdsns;" + "Englsh" + "\"/>");
			pr11179.println("</isons:LanguageSection>");
			pr11179.println(" ");
			
		//Terminological Entry		
			pr11179.println("<isons:TerminologicalEntry rdf:about=\"&pdsns;" + lTE.teDataIdentifier + "\"");
			pr11179.println("  rdfs:label=\"" + lTE.teDataIdentifier + "\">");
			pr11179.println("  <isons:partioning rdf:resource=\"&pdsns;" + lTE.lsDataIdentifier + "\"/>");
			pr11179.println("  <isons:administeredItemContext rdf:resource=\"&pdsns;NASA_PDS\"/>");
			pr11179.println("</isons:TerminologicalEntry>");	
			pr11179.println(" ");
		}
	}

	/**
	* Print the ISO Permissible Value and Value Meaning
	*/
	private void printAttrISOPVVM (PrintWriter pr11179) {

		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lVD = (AttrDefn) i.next();
			if (! (lVD.isUsedInModel && lVD.isAttribute)) continue;
			if (lVD.isEnumerated) {
				for (Iterator <PermValueDefn> j = lVD.permValueArr.iterator(); j.hasNext();) {				 // for each standard value
					PermValueDefn lVM = (PermValueDefn) j.next();
				
					// Value Meanings			
					pr11179.println("<isons:ValueMeaning rdf:about=\"&pdsns;" + "vm_" + lVM.identifier + "\"");
					pr11179.println("	 isons:beginDate=\"" + lVM.value_begin_date + "\"");
					pr11179.println("	 isons:description=\"" + lVM.value_meaning + "\"");
					pr11179.println("	 isons:endDate=\"" + lVM.value_end_date + "\"");
					pr11179.println("	 rdfs:label=\"" + "vm_" + lVM.identifier + "\"/>");
					pr11179.println(" ");

					// Permissible Values
					pr11179.println("<isons:PermissibleValue rdf:about=\"&pdsns;" + "pv_" + lVM.identifier + "\"");
					pr11179.println("	 isons:beginDate=\"" + lVM.value_begin_date + "\"");
					pr11179.println("	 isons:endDate=\"" + lVM.value_end_date + "\"");
					pr11179.println("	 isons:value=\"" + lVM.value + "\"");
					pr11179.println("	 rdfs:label=\"" + "pv_" + lVM.identifier + "\">");
// fix				pr11179.println("	<isons:containing1 rdf:resource=\"&pdsns;" + evdDataIdentifier + "\"/>");
//					pr11179.println("	<isons:containing1 rdf:resource=\"&pdsns;" + dataIdentifier + "\"/>");
					pr11179.println("	<isons:containing1 rdf:resource=\"&pdsns;" + lVD.dataIdentifier + "\"/>");
					pr11179.println("	<isons:usedIn rdf:resource=\"&pdsns;" + "vm_" + lVM.identifier + "\"/>");
					pr11179.println("</isons:PermissibleValue>");
					pr11179.println(" ");
				}
			}
		}
	}

	/**
	* Print the ISO Conceptual Domain
	*/
	private void printAttrISOCD (PrintWriter pr11179) {
		
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lCD = (AttrDefn) i.next();
			if (! (lCD.isUsedInModel && lCD.isAttribute)) continue;
			
			pr11179.println("<isons:ConceptualDomain rdf:about=\"&pdsns;" + "CD_" + lCD.dataConcept + "\"");
			pr11179.println("	 isons:dataIdentifier=\"" + "CD_" + lCD.dataConcept + "\"");
			pr11179.println("	 isons:versionIdentifier=\"" + lCD.versionIdentifierValue + "\"");
			pr11179.println("	 rdfs:label=\"" + "CD_" + lCD.dataConcept + "\">");
				
/*			for (Iterator <String> i = lCD.havingArr.iterator(); i.hasNext();) {
				String val = (String) i.next();
				pr11179.println("	<isons:having rdf:resource=\"&pdsns;" + val + "\"/>");
			}
			for (Iterator <String> i = lCD.representing2Arr.iterator(); i.hasNext();) {
				String val = (String) i.next();
				pr11179.println("	<isons:representing2 rdf:resource=\"&pdsns;" + val + "\"/>");
			} */
			
	/*		for (Iterator <String> i = lCD.containedIn2Arr.iterator(); i.hasNext();) {
				String lval = (String) i.next();
				pr11179.println("	<isons:containedIn2 rdf:resource=\"&pdsns;" + lval + "\"/>");
			} */
			pr11179.println("	<isons:administrationRecord rdf:resource=\"&pdsns;" + lCD.administrationRecordValue + "\"/>");
			pr11179.println("	<isons:registeredBy rdf:resource=\"&pdsns;" + lCD.registeredByValue + "\"/>");
			pr11179.println("	<isons:registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + lCD.registrationAuthorityIdentifierValue + "\"/>");
			pr11179.println("	<isons:steward rdf:resource=\"&pdsns;" + lCD.steward + "\"/>");
			pr11179.println("	<isons:submitter rdf:resource=\"&pdsns;" + lCD.submitter + "\"/>");
			pr11179.println("	<isons:terminologicalEntry rdf:resource=\"&pdsns;" + lCD.teDataIdentifier + "\"/>");
			if (lCD.isEnumerated) {
				pr11179.println("</isons:EnumeratedConceptualDomain>");
			} else {
				pr11179.println("</isons:NonEnumeratedConceptualDomain>");		
			}
			pr11179.println(" ");
		}
	}	

	/**
	* Print the ISO Value Domain
	*/
	private void printAttrISOVD (PrintWriter pr11179) {
		
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lVD = (AttrDefn) i.next();
			if (! (lVD.isUsedInModel && lVD.isAttribute)) continue;
			
			if (lVD.isEnumerated) {
				pr11179.println("<isons:EnumeratedValueDomain rdf:about=\"&pdsns;" + lVD.evdDataIdentifier + "\"");
			} else {
// fix				pr11179.println("<isons:NonEnumeratedValueDomain rdf:about=\"&pdsns;" + nevdDataIdentifier + "\"");
				pr11179.println("<isons:NonEnumeratedValueDomain rdf:about=\"&pdsns;" + lVD.dataIdentifier + "\"");
			}

			pr11179.println("  isons:dataIdentifier=\"" + lVD.dataIdentifier + "\"");
			pr11179.println("  isons:versionIdentifier=\"" + lVD.versionIdentifierValue + "\"");
			pr11179.println("  isons:maximumCharacterQuantity=\"255\"");
			pr11179.println("  rdfs:label=\"" + lVD.dataIdentifier + "\">");		
			for (Iterator <String> j = lVD.representedBy1Arr.iterator(); j.hasNext();) {
				String val = (String) j.next();
				pr11179.println("  <isons:representedBy1 rdf:resource=\"&pdsns;" + val + "\"/>");
			}
			
			for (Iterator <String> j = lVD.representedBy2Arr.iterator(); j.hasNext();) {
				String lval = (String) j.next();
				pr11179.println("  <isons:representedBy2 rdf:resource=\"&pdsns;" + lval  + "\"/>");
			}
//			for (Iterator <VMDefn> j = lVD.containedIn1Arr.iterator(); j.hasNext();) {
//				VMDefn lVM = (VMDefn) j.next();
//				pr11179.println("  <isons:containedIn1 rdf:resource=\"&pdsns;" + lVM.pvValue  + "\"/>");
//			}		
			pr11179.println("  <isons:datatype rdf:resource=\"&pdsns;" + lVD.valueType + "\"/>");
			pr11179.println("  <isons:unitOfMeasure rdf:resource=\"&pdsns;N_A\"/>");
			pr11179.println("  <isons:registeredBy rdf:resource=\"&pdsns;" + lVD.registeredByValue + "\"/>");
			pr11179.println("  <isons:steward rdf:resource=\"&pdsns;" + lVD.steward + "\"/>");
			pr11179.println("  <isons:submitter rdf:resource=\"&pdsns;" + lVD.submitter + "\"/>");
			pr11179.println("  <isons:terminologicalEntry rdf:resource=\"&pdsns;" + lVD.teDataIdentifier + "\"/>");
			pr11179.println("  <isons:administrationRecord rdf:resource=\"&pdsns;" + lVD.administrationRecordValue + "\"/>");
			pr11179.println("  <isons:registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + lVD.registrationAuthorityIdentifierValue + "\"/>");
			if (lVD.isEnumerated) {
				pr11179.println("</isons:EnumeratedValueDomain>");
			} else {
				pr11179.println("</isons:NonEnumeratedValueDomain>");		
			}
			pr11179.println(" ");
		}
	}	

	/**
	* Print the ISO Data Element Concept
	*/
	private void printAttrISODEC (PrintWriter pr11179) {
		
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lDEC = (AttrDefn) i.next();
			if (! (lDEC.isUsedInModel && lDEC.isAttribute)) continue;
		
			pr11179.println("<isons:DataElementConcept rdf:about=\"&pdsns;" + lDEC.decDataIdentifier + "\"");
			pr11179.println("	 isons:dataIdentifier=\"" + lDEC.decDataIdentifier + "\"");
			pr11179.println("	 isons:versionIdentifier=\"" + lDEC.versionIdentifierValue + "\"");
			pr11179.println("	 rdfs:label=\"" + lDEC.decDataIdentifier + "\">");
/*			for (Iterator <String> j = lDEC.expressingArr.iterator(); j.hasNext();) {
				String val = (String) j.next();
				pr11179.println("	<isons:expressing rdf:resource=\"&pdsns;" + val + "\"/>");
			} */
			
			pr11179.println("	<isons:registeredBy rdf:resource=\"&pdsns;" + lDEC.registeredByValue + "\"/>");
			pr11179.println("	<isons:steward rdf:resource=\"&pdsns;" + DMDocument.stewardValue + "\"/>");
			pr11179.println("	<isons:submitter rdf:resource=\"&pdsns;" + DMDocument.submitterValue + "\"/>");
			pr11179.println("	<isons:terminologicalEntry rdf:resource=\"&pdsns;" + lDEC.teDataIdentifier+ "\"/>");
			pr11179.println("	<isons:administrationRecord rdf:resource=\"&pdsns;" + lDEC.administrationRecordValue + "\"/>");
			pr11179.println("	<isons:registrationAuthorityIdentifier rdf:resource=\"&pdsns;" + lDEC.registrationAuthorityIdentifierValue + "\"/>");
/*			for (Iterator <String> j = lDEC.specifyingArr.iterator(); j.hasNext();) {
				String val = (String) j.next();
				pr11179.println("	<isons:specifying rdf:resource=\"&pdsns;" + val + "\"/>");
			} */
			pr11179.println("</isons:DataElementConcept>");
			pr11179.println(" ");
		}
	}

	/**
	* Print the ISO Data Element 
	*/
	private void printAttrISODERDF (PrintWriter pr11179) {
		// print the data elements
		ArrayList <AttrDefn> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<AttrDefn> i = lSortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			pr11179.println("<kb:DataElement rdf:about=\"&kb;" + lAttr.deDataIdentifier + "\"");
			pr11179.println("	 kb:dataIdentifier=\"" + lAttr.deDataIdentifier + "\"");
			pr11179.println("	 kb:versionIdentifier=\"" + lAttr.versionIdentifierValue + "\"");
			pr11179.println("	 rdfs:label=\"" + lAttr.deDataIdentifier + "\">");
			pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + lAttr.registeredByValue + "\"/>");
			pr11179.println("	<kb:typedBy rdf:resource=\"&kb;RC_ZZTEMP\"/>");
			pr11179.println("	<kb:steward rdf:resource=\"&kb;" + DMDocument.stewardValue + "\"/>");
			pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + DMDocument.submitterValue + "\"/>");
			pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;" + lAttr.teDataIdentifier+ "\"/>");
			pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + lAttr.administrationRecordValue + "\"/>");

//			for (Iterator <String> i = lDE.expressedByArr.iterator(); i.hasNext();) {
//				String val = (String) i.next();
//				pr11179.println("	<kb:expressedBy rdf:resource=\"&kb;" + val + "\"/>");
//			}
//			for (Iterator <String> j = lAttr.representing1Arr.iterator(); j.hasNext();) {
//				String val = (String) j.next();
//				pr11179.println("	<kb:representing rdf:resource=\"&kb;" + val + "\"/>");
//			}
			pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + lAttr.registrationAuthorityIdentifierValue + "\"/>");
/*			if (lDE.genClassArr != null) {
				for (Iterator <String> i = lDE.genClassArr.iterator(); i.hasNext();) {
					String lval = (String) i.next();
					lval = lval.toLowerCase();
					pr11179.println("	<kb:classifiedBy rdf:resource=\"&kb;csitv1_" + lval + "\"/>");
				}	
			}
			if (lDE.sysClassArr != null) {
				for (Iterator <String> i = lDE.sysClassArr.iterator(); i.hasNext();) {
					String lval = (String) i.next();
					lval = lval.toLowerCase();
					pr11179.println("	<kb:classifiedBy rdf:resource=\"&kb;csitv2_" + lval + "\"/>");
				}
			} */
			pr11179.println("</kb:DataElement>");
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
		pr11179.println("	 <!ENTITY rdfs 'http://www.w3.org/2000/01/rdf-schema#'>");
		pr11179.println("]>");
		pr11179.println("<rdf:RDF xmlns:rdf=\"&rdf;\"");
		pr11179.println("	 xmlns:kb=\"&kb;\"");
		pr11179.println("	 xmlns:rdfs=\"&rdfs;\">");
		pr11179.println(" ");
/*		pr11179.println("<&kb;AdministrationRecord rdf:about=\"&kb;" + administrationRecordValue + "\"");
		pr11179.println("	 kb:administrativeNote=\"Test load from PDS4 Master Model\"");
		pr11179.println("	 kb:administrativeStatus=\"Final\"");
		pr11179.println("	 kb:changeDescription=\"No changes have been made to PSDD content.\"");
		pr11179.println("	 kb:creationDate=\"" + creationDateValue + "\"");
		pr11179.println("	 kb:effectiveDate=\"" + creationDateValue + "\"");
		pr11179.println("	 kb:lastChangeDate=\"" + creationDateValue + "\"");
		pr11179.println("	 kb:origin=\"Planetary Data System\"");
		pr11179.println("	 kb:registrationStatus=\"Preferred\"");
		pr11179.println("	 kb:unresolvedIssue=\"Issues still being determined.\"");
		pr11179.println("	 kb:untilDate=\"" + futureDateValue + "\"");
		pr11179.println("	 rdfs:label=\"" + administrationRecordValue + "\">");
		pr11179.println("	<kb:explanatoryComment>This is a test load of a ISO/IEC 11179 Data Dictionary from the PDS4 Master model.</kb:explanatoryComment>");
		pr11179.println("</kb:AdministrationRecord>");
		pr11179.println(" ");
		pr11179.println("<kb:Steward rdf:about=\"&kb;" + stewardValue + "\"");
		pr11179.println("	 rdfs:label=\"" + stewardValue + "\">");
		pr11179.println("	<kb:contact rdf:resource=\"&kb;Elizabeth_Rye\"/>");
		pr11179.println("	<kb:organization rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("</kb:Steward>");
		pr11179.println(" ");
		pr11179.println("<kb:Submitter rdf:about=\"&kb;" + submitterValue + "\"");
		pr11179.println("	 rdfs:label=\"" + submitterValue + "\">");
		pr11179.println("	<kb:contact rdf:resource=\"&kb;Elizabeth_Rye\"/>");
		pr11179.println("	<kb:organization rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("</kb:Submitter>");
		pr11179.println(" ");
		pr11179.println("<kb:ClassificationScheme rdf:about=\"&kb;System_Classification_Id\"");
		pr11179.println("	 kb:classification_scheme_type_name=\"Keyword\"");
		pr11179.println("	 kb:dataIdentifier=\"System_Classification_Id\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"System_Classification_Id\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TE_System_Classification_Id\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_common\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_isis\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_jpl-ammos\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_mars-observer\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-atmos\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-cn\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-geo-mgn\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-img-gll\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-mer-ops\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-ppi\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-rings\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_pds-sbn\"/>");
		pr11179.println("	<kb:containing3 rdf:resource=\"&kb;csitv2_spice\"/>");
		pr11179.println("</kb:ClassificationScheme>");
		pr11179.println(" ");
		pr11179.println("<kb:RegistrationAuthority rdf:about=\"&kb;" + registeredByValue + "\"");
		pr11179.println("	 kb:organizationMailingAddress=\"4800 Oak Grove Drive\"");
		pr11179.println("	 kb:organizationName=\"NASA Planetary Data System\"");
		pr11179.println("	 rdfs:label=\"" + registeredByValue + "\">");
		pr11179.println("	<kb:documentationLanguageIdentifier rdf:resource=\"&kb;LI_English\"/>");
		pr11179.println("	<kb:languageUsed rdf:resource=\"&kb;LI_English\"/>");
		pr11179.println("	<kb:registrar rdf:resource=\"&kb;PDS_Registrar\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier_v rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RegistrationAuthority>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_COUNT\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_COUNT\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_COUNT\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_COUNT\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_IDENTIFIER\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_IDENTIFIER\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_IDENTIFIER\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_IDENTIFIER\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_MASK\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_MASK\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_MASK\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_MASK\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_NAME\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_NAME\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_NAME\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_NAME\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_TEXT\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_TEXT\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_TEXT\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_TEXT\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_UTC_DATE\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_UTC_DATE\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_DATE\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_UTC_DATE\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_UTC_DATE_TIME\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_UTC_DATE_TIME\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_DATE_TIME\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_UTC_DATE_TIME\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_UTC_TIME\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_UTC_TIME\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_UTC_TIME\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_UTC_TIME\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>");
		pr11179.println(" ");
		pr11179.println("<kb:RepresentationClass rdf:about=\"&kb;RC_ZZTEMP\"");
		pr11179.println("	 kb:dataIdentifier=\"RC_ZZTEMP\"");
		pr11179.println("	 kb:versionIdentifier=\"" + versionIdentifierValue + "\"");
		pr11179.println("	 rdfs:label=\"RC_ZZTEMP\">");
		pr11179.println("	<kb:registeredBy rdf:resource=\"&kb;" + registeredByValue + "\"/>");
		pr11179.println("	<kb:steward rdf:resource=\"&kb;" + stewardValue + "\"/>");
		pr11179.println("	<kb:submitter rdf:resource=\"&kb;" + submitterValue + "\"/>");
		pr11179.println("	<kb:terminologicalEntry rdf:resource=\"&kb;TERC_ZZTEMP\"/>");
		pr11179.println("	<kb:administrationRecord rdf:resource=\"&kb;" + administrationRecordValue + "\"/>");
		pr11179.println("	<kb:registrationAuthorityIdentifier rdf:resource=\"&kb;" + registrationAuthorityIdentifierValue + "\"/>");
		pr11179.println("</kb:RepresentationClass>"); */
		pr11179.println(" ");
	}

//	=======================  Utilities ================================================================
	
	/**
	*  sort attributes by class and namespaces
	*/
	public ArrayList <AttrDefn> getSortedAttrs () {
		TreeMap <String, AttrDefn> lTreeMap = new TreeMap <String, AttrDefn>();
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInModel && lAttr.propType.compareTo("ATTRIBUTE") == 0) {
				String sortKey = lAttr.title + ":" + lAttr.steward + "." + lAttr.className + ":" + lAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lAttr);
			}
		}
		ArrayList <AttrDefn> lSortedAttrArr = new ArrayList <AttrDefn> (lTreeMap.values());		
		return lSortedAttrArr; 
	}	
	
	
}
