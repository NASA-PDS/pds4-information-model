package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Writes the Protege .pins (protege instance) file compliant with 11179 
 *   
 */

class Write11179DDPinsFile extends Object{
	
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
	// good	PrintWriter prDDPins;
	PrintWriter prDDPins;

	public Write11179DDPinsFile () {
		return;
	}

	// write the PINS file
	public void writePINSFile (String lFileName) throws java.io.IOException {
//		FileWriter fw = new FileWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//		To specify these values yourself, construct an OutputStreamWriter on a FileOutputStream.	    
// good	    FileOutputStream specFileOutputStream = new FileOutputStream(new File(lFileName));
// good	    prDDPins = new PrintWriter(new OutputStreamWriter (specFileOutputStream, "UTF-8"));

		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));		
		
	    printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the Protege Pins Header
//	public void printPDDPHdr (PrintWriter prDDPins) {
	public void printPDDPHdr () {
		prDDPins.println("	; Tue Jan 26 07:52:47 PST 2010");
		prDDPins.println("	; ");
		prDDPins.println("	;+ (version \"3.3.1\")");
		prDDPins.println("	;+ (build \"Build 430\")");
		prDDPins.println("");
	}
	
	// Print the Protege Pins Footer
//	public  void printPDDPFtr (PrintWriter prDDPins) {
	public  void printPDDPFtr () {
		prDDPins.println("");
	}
	
//	print the body
	public  void printPDDPBody () {
		// print the data elements
		 printPDDPDE (prDDPins);
		 printPDDPVD (prDDPins);
		 printPDDPPR (prDDPins);
		 printPDDPCD (prDDPins);
		 printPDDPDEC (prDDPins);
		 printPDDPTE (prDDPins);
		 printPDDPMISC (prDDPins);
	}
		
	// Print the the Protege Pins DE
	public  void printPDDPDE (PrintWriter prDDPins) {
		// print the data elements
		ArrayList <AttrDefn> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<AttrDefn> i = lSortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			prDDPins.println("([" + lAttr.deDataIdentifier + "] of DataElement");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
//			prDDPins.println("  (classifiedBy");
//			prDDPins.println("    [TBD_Classification_Scheme])");
			prDDPins.println("  (dataIdentifier \"" + lAttr.deDataIdentifier + "\")");
			prDDPins.println("  (expressedBy ");
			prDDPins.println("    [DEC_" + lAttr.classConcept + "])");
			prDDPins.println("  (registeredBy [" + lAttr.registeredByValue+ "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + lAttr.registrationAuthorityIdentifierValue + "])");
			prDDPins.println("  (representing ");
			if (lAttr.isEnumerated) {
				prDDPins.println("    [" + lAttr.evdDataIdentifier + "])");
			} else {
				prDDPins.println("    [" + lAttr.nevdDataIdentifier + "])");				
			}
			prDDPins.println("  (isNillable \"" + lAttr.isNilable + "\")");
			prDDPins.println("  (steward [" + lAttr.steward + "])");
			prDDPins.println("  (submitter [" + lAttr.submitter + "])");
			prDDPins.println("  (terminologicalEntry [" + lAttr.teDataIdentifier + "])");
			prDDPins.println("  (versionIdentifier \"" + lAttr.versionIdentifierValue + "\"))");
		}
	}
	
	// Print the the Protege Pins VD
	public  void printPDDPVD (PrintWriter prDDPins) {
		// print the value domain

		ArrayList <AttrDefn> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<AttrDefn> i = lSortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			if (lAttr.isEnumerated) {
				prDDPins.println("([" + lAttr.evdDataIdentifier + "] of EnumeratedValueDomain");
			} else {
				prDDPins.println("([" + lAttr.nevdDataIdentifier  + "] of NonEnumeratedValueDomain");	
			}
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");

			if (lAttr.isEnumerated) {
				if ( ! (lAttr.permValueArr == null || lAttr.permValueArr.isEmpty())) {
					String lfc = "";
					prDDPins.println("  (containedIn1 ");
					
					// get the permissible value instance identifiers, for some reason the class is called value meaning.
					for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
						PermValueDefn lPermValue = (PermValueDefn) j.next();
						prDDPins.print(lfc);
						if (lPermValue != null) {
				 			int lHashCodeI = lPermValue.value.hashCode();
							String lHashCodeS = Integer.toString(lHashCodeI);
							
							
// v1.3	- fix pattern for data types.		
/*							if (lAttr.title.compareTo("pattern") != 0) {
								String tPermValue = lPermValue.value;
								if (tPermValue.indexOf("[0001-9999]") > -1) {
									tPermValue = InfoModel.replaceString(tPermValue, "[0001-9999]", "[0-9]{4}");
						 			lHashCodeI = tPermValue.hashCode();
									lHashCodeS = Integer.toString(lHashCodeI);
								}
							}							
							*/
							
// v1.3
/*							String tPermValue = "Pattern";
							if (lAttr.title.compareTo("pattern") != 0) {
								tPermValue = InfoModel.replaceString(lPermValue.value, " ", "_");
							} */
							String pvIdentifier =  "pv." + lAttr.dataIdentifier + "." + lHashCodeS;	// permissible value
							prDDPins.print("    [" + pvIdentifier + "]");
						}
						lfc = "\n";
					}
					prDDPins.print(")\n");
				}
				prDDPins.println("  (dataIdentifier \"" + lAttr.evdDataIdentifier  + "\")");
			} else {
				prDDPins.println("  (dataIdentifier \"" + lAttr.nevdDataIdentifier  + "\")");				
			}
			prDDPins.println("  (datatype [" + lAttr.valueType + "])");
			prDDPins.println("  (maximumCharacterQuantity \"" + lAttr.maximum_characters + "\")");
			prDDPins.println("  (minimumCharacterQuantity \"" + lAttr.minimum_characters + "\")");
			prDDPins.println("  (maximumValue \"" + lAttr.maximum_value + "\")");
			prDDPins.println("  (minimumValue \"" + lAttr.minimum_value + "\")");
			prDDPins.println("  (pattern \"" + InfoModel.escapeProtegePatterns(lAttr.pattern) + "\")");
			prDDPins.println("  (valueDomainFormat \"" + lAttr.format + "\")");
			prDDPins.println("  (registeredBy " + lAttr.registeredByValue+ ")");
			prDDPins.println("  (registrationAuthorityIdentifier [" + lAttr.registrationAuthorityIdentifierValue + "])");
			prDDPins.println("  (representedBy" + " [" + lAttr.deDataIdentifier + "])");
			prDDPins.println("  (representedBy2" + " [" + "CD_" + lAttr.dataConcept + "])");
			prDDPins.println("  (steward [" + "Steward_PDS" + "])");
			prDDPins.println("  (submitter [" + lAttr.submitter + "])");
			prDDPins.println("  (unitOfMeasure [" + lAttr.unit_of_measure_type + "])");			
			prDDPins.println("  (defaultUnitId \"" + lAttr.default_unit_id + "\")");
			
			int allowedUnitIdLmt = lAttr.allowedUnitId.size();
			if (allowedUnitIdLmt > 0) {
				int allowedUnitIdInd = 1;
				prDDPins.println("  (allowedUnitId");
				String lineFeed = "\n";
				for (Iterator<String> j = lAttr.allowedUnitId.iterator(); j.hasNext();) {
					String lUnitId = (String) j.next();
					if (allowedUnitIdInd == allowedUnitIdLmt) lineFeed = "";
					prDDPins.print("    \"" + lUnitId + "\"" + lineFeed);
				}
				prDDPins.print(")\n");
			}
			
			prDDPins.println("  (versionIdentifier \"" + lAttr.versionIdentifierValue + "\"))");
			
			if (lAttr.isEnumerated) {				
				for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
					PermValueDefn lPermValue = (PermValueDefn) j.next();
					if (lPermValue != null) {
			 			int lHashCodeI = lPermValue.value.hashCode();
						String lHashCodeS = Integer.toString(lHashCodeI);
						
// v1.3	- fix pattern for data types.
						/*
						String tPermValue = lPermValue.value;
						String tPermValueMeaning = lPermValue.value_meaning;

						if (lAttr.title.compareTo("pattern") != 0) {
							if (tPermValue.indexOf("[0001-9999]") > -1) {
								tPermValue = InfoModel.replaceString(tPermValue, "[0001-9999]", "[0-9]{4}");
								tPermValueMeaning = InfoModel.replaceString(tPermValueMeaning, "[0001-9999]", "[0-9]{4}");
					 			lHashCodeI = tPermValue.hashCode();
								lHashCodeS = Integer.toString(lHashCodeI);
							}
						}
						*/
						
// v1.3						
/*						String tPermValue = "Pattern";
						if (lAttr.title.compareTo("pattern") != 0) {
							tPermValue = InfoModel.replaceString(lPermValue.value, " ", "_");
						} */
						String pvIdentifier =  "pv." + lAttr.dataIdentifier + "." + lHashCodeS;	// permissible value
						String vmIdentifier =  "vm." + lAttr.dataIdentifier + "." + lHashCodeS;
						prDDPins.println("([" + pvIdentifier + "] of PermissibleValue");
						prDDPins.println("  (beginDate \"" + DMDocument.beginDatePDS4Value + "\")");
						prDDPins.println("  (containing1 [" + lAttr.evdDataIdentifier + "])");
						prDDPins.println("  (endDate \"" + DMDocument.endDateValue + "\")");
						prDDPins.println("  (usedIn [" + vmIdentifier + "])");
						prDDPins.println("  (value \"" + InfoModel.escapeProtegeString(lPermValue.value) + "\"))");
// v1.3					prDDPins.println("  (value \"" + InfoModel.escapeProtegeString(tPermValue) + "\"))");
						prDDPins.println(" ");
						prDDPins.println("([" + vmIdentifier + "] of ValueMeaning");
						prDDPins.println("  (beginDate \"" + DMDocument.beginDatePDS4Value + "\")");
						prDDPins.println("  (containing1 [" + lAttr.evdDataIdentifier + "])");
						prDDPins.println("  (endDate \"" + DMDocument.endDateValue + "\")");
						prDDPins.println("  (description \"" + InfoModel.escapeProtegeString(lPermValue.value_meaning) + "\"))");
// v1.3						prDDPins.println("  (description \"" + InfoModel.escapeProtegeString(tPermValueMeaning) + "\"))");
						prDDPins.println(" ");
					}
				}
			}
		}
	}

	// Print the the Protege Pins Properties
	public  void printPDDPPR (PrintWriter prDDPins) {
//		System.out.println("debug printPDDPPR");
		ArrayList <AssocDefn> lSortedAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());
		for (Iterator<AssocDefn> i = lSortedAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			String prDataIdentifier = "PR." + lAssoc.identifier;
//			System.out.println("debug printPDDPPR - prDataIdentifier:" + prDataIdentifier);
			prDDPins.println("([" + prDataIdentifier + "] of Property");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
			prDDPins.println("  (registeredBy [" + "RA_0001_NASA_PDS_1" + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");						
			prDDPins.println("  (classOrder \"" + lAssoc.classOrder + "\")");
			prDDPins.println("  (versionIdentifier \"" + InfoModel.identifier_version_id + "\"))");
		}
	}

	// Print the the Protege Pins CD
	public  void printPDDPCD (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lCDAttrArr = new ArrayList <IndexDefn> (InfoModel.cdAttrMap.values());
		for (Iterator<IndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "CD_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of ConceptualDomain");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (having ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lDEC = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "DEC_" + lDEC + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");

			lfc = "";
			prDDPins.println("  (representing2 ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				if (lAttr.isEnumerated) {
					prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
				} else {
					prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
				}
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
		
			// write terminological entry
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins DEC
	public  void printPDDPDEC (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lDECAttrArr = new ArrayList <IndexDefn> (InfoModel.decAttrMap.values());
		for (Iterator<IndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "DEC_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of DataElementConcept");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (expressing ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");
			lfc = "";
			prDDPins.println("  (specifying ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lCD = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "CD_" + lCD + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
			
			// write the terminological entry
			
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins TE
	public  void printPDDPTE (PrintWriter prDDPins) {
		// print the Terminological Entry
///		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
///		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				// print TE section
				prDDPins.println("([" + lAttr.teDataIdentifier + "] of TerminologicalEntry");
				prDDPins.println("  (administeredItemContext [NASA_PDS])");
				prDDPins.println("  (definition ["  + lAttr.defDataIdentifier + "])");
				prDDPins.println("  (designation [" + lAttr.desDataIdentifier + "])");
				prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");
		
				// print definition section
				prDDPins.println("([" + lAttr.defDataIdentifier + "] of Definition");
				prDDPins.println("  (definitionText \"" + InfoModel.escapeProtegeString(lAttr.description) + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
				
				// print designation section
				prDDPins.println("([" + lAttr.desDataIdentifier + "] of Designation");
				prDDPins.println("  (designationName \"" + lAttr.title + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
			}
		}
	}	

	// Print the the Protege Pins Misc
	public  void printPDDPMISC (PrintWriter prDDPins) {
		// print the Miscellaneous records
		prDDPins.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
		prDDPins.println("	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
		prDDPins.println("	(administrativeStatus Final)");
		prDDPins.println("	(changeDescription \"PSDD content has been merged into the model.\")");
		prDDPins.println("	(creationDate \"2010-03-10\")");
		prDDPins.println("	(effectiveDate \"2010-03-10\")");
		prDDPins.println("	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
		prDDPins.println("	(lastChangeDate \"2010-03-10\")");
		prDDPins.println("	(origin \"Planetary Data System\")");
		prDDPins.println("	(registrationStatus Preferred)");
		prDDPins.println("	(unresolvedIssue \"Issues still being determined.\")");
		prDDPins.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");
		
		prDDPins.println("([0001_NASA_PDS_1] of RegistrationAuthorityIdentifier");
		prDDPins.println("	(internationalCodeDesignator \"0001\")");
		prDDPins.println("	(opiSource \"1\")");
		prDDPins.println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
		prDDPins.println("	(organizationPartIdentifier \"Planetary Data System\"))");

		prDDPins.println("([RA_0001_NASA_PDS_1] of RegistrationAuthority");
		prDDPins.println("	(documentationLanguageIdentifier [LI_English])");
		prDDPins.println("	(languageUsed [LI_English])");
		prDDPins.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
		prDDPins.println("	(organizationName \"NASA Planetary Data System\")");
		prDDPins.println("	(registrar [PDS_Registrar])");
		prDDPins.println("	(registrationAuthorityIdentifier_v [0001_NASA_PDS_1]))");
	
		prDDPins.println("([NASA_PDS] of Context");
		prDDPins.println("	(dataIdentifier  \"NASA_PDS\"))");
		
		prDDPins.println("([PDS_Registrar] of  Registrar");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(registrarIdentifier \"PDS_Registrar\"))");
		
		prDDPins.println("([Steward_PDS] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");
		
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());		
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			prDDPins.println("([" + lSchemaFileDefn.identifier + "] of Steward");
			prDDPins.println("	(contact [PDS_Standards_Coordinator])");
			prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		}
		
		// ops is not included as a defined namespace in the SchemaFileDefn array
		prDDPins.println("([ops] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([Submitter_PDS] of Submitter");
		prDDPins.println("	(contact [DataDesignWorkingGroup])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");
		
		prDDPins.println("([PDS_Standards_Coordinator] of Contact");
		prDDPins.println("	(contactTitle \"PDS_Standards_Coordinator\")");
		prDDPins.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
		prDDPins.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.6135\")");
		prDDPins.println("	(contactName \"Elizabeth Rye\"))");
		
		prDDPins.println("([DataDesignWorkingGroup] of Contact");
		prDDPins.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.9338\")");
		prDDPins.println("	(contactName \"J. Steven Hughes\"))");
		
		prDDPins.println("([LI_English] of LanguageIdentification");
		prDDPins.println("  (countryIdentifier \"USA\")");
		prDDPins.println("  (languageIdentifier \"English\"))");
			
		// write the unitOfMeasure
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();		
			prDDPins.println("([" + lUnit.title + "] of UnitOfMeasure");
			prDDPins.println("	(measureName \"" + lUnit.title + "\")");
			prDDPins.println("	(defaultUnitId \"" + InfoModel.escapeProtegeString(lUnit.default_unit_id) + "\")");
			prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
			if (! lUnit.unit_id.isEmpty() )
			{
				String lSpace = "";
				prDDPins.print("	(unitId ");
				// set the units
				for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
					String lVal = (String) j.next();
					prDDPins.print(lSpace + "\"" + InfoModel.escapeProtegeString(lVal) + "\"");
					lSpace = " ";
				}
				prDDPins.println("))");
			}
		}
		
		// write the TBD_unitOfMeasure		
		prDDPins.println("([" + "TBD_unit_of_measure_type" + "] of UnitOfMeasure");
		prDDPins.println("	(measureName \"" + "TBD_unit_of_measure_type" + "\")");
		prDDPins.println("	(defaultUnitId \"" + "defaultUnitId" + "\")");
		prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
		prDDPins.println("	(unitId \"" + "TBD_unitId" + "\"))");
		
		// print data types
		for (Iterator<PDSObjDefn> i = InfoModel.masterDataTypesArr.iterator(); i.hasNext();) {
			PDSObjDefn lDataType = (PDSObjDefn) i.next();	
			prDDPins.println("([" + lDataType.title + "] of DataType");
			prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
			prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
	
	/**
	*  sort attributes by class and namespaces
	*/
	public ArrayList <AttrDefn> getSortedAttrs () {
		TreeMap <String, AttrDefn> lTreeMap = new TreeMap <String, AttrDefn>();
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				String sortKey = lAttr.title + ":" + lAttr.steward + "." + lAttr.parentClassTitle + ":" + lAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lAttr);
			}
		}
		ArrayList <AttrDefn> lSortedAttrArr = new ArrayList <AttrDefn> (lTreeMap.values());		
		return lSortedAttrArr; 
	}
	
	
	/**
	*  sort attributes and associations by class and namespaces
	*/
	public ArrayList <AttrDefn> getSortedAttrsAssocs () {
		TreeMap <String, AttrDefn> lTreeMap = new TreeMap <String, AttrDefn>();
		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			if (lAttr.isUsedInClass) {
			if (true) {
				String sortKey = lAttr.title + ":" + lAttr.steward + "." + lAttr.parentClassTitle + ":" + lAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lAttr);
			}
		}
		ArrayList <AttrDefn> lSortedAttrArr = new ArrayList <AttrDefn> (lTreeMap.values());		
		return lSortedAttrArr; 
	}
}	
