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
import java.io.*;
import java.util.*;

/**
 * Writes the Protege .pins (protege instance) file compliant with 11179 - DOM version
 *   
 */

class WriteDOM11179DDPinsFile extends Object{
	
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
	PrintWriter prDDPins;

	public WriteDOM11179DDPinsFile () {
		return;
	}

	// write the PINS file
	public void writePINSFile (String lFileName) throws java.io.IOException {
		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));		
		
	    printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the Protege Pins Header
	public void printPDDPHdr () {
		prDDPins.println("	; Tue Jan 26 07:52:47 PST 2010");
		prDDPins.println("	; ");
		prDDPins.println("	;+ (version \"3.3.1\")");
		prDDPins.println("	;+ (build \"Build 430\")");
		prDDPins.println("");
	}
	
	// Print the Protege Pins Footer
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
		ArrayList <DOMAttr> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<DOMAttr> i = lSortedAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
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

		ArrayList <DOMAttr> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<DOMAttr> i = lSortedAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
//			System.out.println("\ndebug printPDDPVD lDOMAttr.identifier:" + lDOMAttr.identifier);
			if (lDOMAttr.isEnumerated) {
				prDDPins.println("([" + lDOMAttr.evdDataIdentifier + "] of EnumeratedValueDomain");
			} else {
				prDDPins.println("([" + lDOMAttr.nevdDataIdentifier  + "] of NonEnumeratedValueDomain");	
			}
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			
//			System.out.println("debug printPDDPVD lDOMAttr.isEnumerated:" + lDOMAttr.isEnumerated);

			if (lDOMAttr.isEnumerated) {
				if ( ! (lDOMAttr.domPermValueArr == null || lDOMAttr.domPermValueArr.isEmpty())) {
					String lfc = "";
					prDDPins.println("  (containedIn1 ");
//					System.out.println("debug printPDDPVD   - containedIn1 - lDOMAttr.isEnumerated:" + lDOMAttr.isEnumerated);

					// get the permissible value instance identifiers	
					for (Iterator<DOMProp> j = lDOMAttr.domPermValueArr.iterator(); j.hasNext();) {
						DOMProp lDOMProp = (DOMProp) j.next();
//						System.out.println("debug printPDDPVD     - domPermValueArr lDOMProp - lDOMProp.lDOMProp:" + lDOMProp.identifier);

						if ( ! (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn)) continue;
						DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
						
//						System.out.println("debug printPDDPVD     - lDOMPermValDefn - lDOMPermValDefn.identifier:" + lDOMPermValDefn.identifier);

						prDDPins.print(lfc);
						if (lDOMPermValDefn != null) {
//							System.out.println("debug printPDDPVD       - lDOMPermValDefn NOT NULL - lDOMPermValDefn.identifier:" + lDOMPermValDefn.identifier);

				 			int lHashCodeI = lDOMPermValDefn.value.hashCode();
							String lHashCodeS = Integer.toString(lHashCodeI);
							String pvIdentifier =  "pv." + lDOMAttr.dataIdentifier + "." + lHashCodeS;	// permissible value
							prDDPins.print("    [" + pvIdentifier  + "]");
//							System.out.println("debug printPDDPVD         - pvIdentifier:" + pvIdentifier);
						}
						lfc = "\n";
					}
					prDDPins.print(")\n");
				}
				prDDPins.println("  (dataIdentifier \"" + lDOMAttr.evdDataIdentifier  + "\")");
			} else {
				prDDPins.println("  (dataIdentifier \"" + lDOMAttr.nevdDataIdentifier  + "\")");				
			}
			prDDPins.println("  (datatype [" + lDOMAttr.valueType + "])");
			prDDPins.println("  (maximumCharacterQuantity \"" + lDOMAttr.maximum_characters + "\")");
			prDDPins.println("  (minimumCharacterQuantity \"" + lDOMAttr.minimum_characters + "\")");
			prDDPins.println("  (maximumValue \"" + lDOMAttr.maximum_value + "\")");
			prDDPins.println("  (minimumValue \"" + lDOMAttr.minimum_value + "\")");
			prDDPins.println("  (pattern \"" + DOMInfoModel.escapeProtegePatterns(lDOMAttr.pattern) + "\")");
			prDDPins.println("  (valueDomainFormat \"" + DOMInfoModel.escapeProtegePatterns(lDOMAttr.format) + "\")");
			prDDPins.println("  (registeredBy " + lDOMAttr.registeredByValue+ ")");
			prDDPins.println("  (registrationAuthorityIdentifier [" + lDOMAttr.registrationAuthorityIdentifierValue + "])");
			prDDPins.println("  (representedBy" + " [" + lDOMAttr.deDataIdentifier + "])");
			prDDPins.println("  (representedBy2" + " [" + "CD_" + lDOMAttr.dataConcept + "])");
			prDDPins.println("  (steward [" + "Steward_PDS" + "])");
			prDDPins.println("  (submitter [" + lDOMAttr.submitter + "])");
			prDDPins.println("  (unitOfMeasure [" + lDOMAttr.unit_of_measure_type + "])");			
			prDDPins.println("  (defaultUnitId \"" + lDOMAttr.default_unit_id + "\")");
			
			int allowedUnitIdLmt = lDOMAttr.allowedUnitId.size();
			if (allowedUnitIdLmt > 0) {
				int allowedUnitIdInd = 1;
				prDDPins.println("  (allowedUnitId");
				String lineFeed = "\n";
				for (Iterator<String> j = lDOMAttr.allowedUnitId.iterator(); j.hasNext();) {
					String lUnitId = (String) j.next();
					if (allowedUnitIdInd == allowedUnitIdLmt) lineFeed = "";
					prDDPins.print("    \"" + lUnitId + "\"" + lineFeed);
				}
				prDDPins.print(")\n");
			}
			
			prDDPins.println("  (versionIdentifier \"" + lDOMAttr.versionIdentifierValue + "\"))");
			
			if (lDOMAttr.isEnumerated) {				
				for (Iterator<DOMProp> j = lDOMAttr.domPermValueArr.iterator(); j.hasNext();) {
					DOMProp lDOMProp = (DOMProp) j.next();
					if ( ! (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn)) continue;
					DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
					if (lDOMPermValDefn != null) {
			 			int lHashCodeI = lDOMPermValDefn.value.hashCode();
						String lHashCodeS = Integer.toString(lHashCodeI);
						String pvIdentifier =  "pv." + lDOMAttr.dataIdentifier + "." + lHashCodeS;	// permissible value
						String vmIdentifier =  "vm." + lDOMAttr.dataIdentifier + "." + lHashCodeS;
						prDDPins.println("([" + pvIdentifier + "] of PermissibleValue");
						prDDPins.println("  (beginDate \"" + DMDocument.beginDatePDS4Value + "\")");
						prDDPins.println("  (containing1 [" + lDOMAttr.evdDataIdentifier + "])");
						prDDPins.println("  (endDate \"" + DMDocument.endDateValue + "\")");
						prDDPins.println("  (usedIn [" + vmIdentifier + "])");
						prDDPins.println("  (value \"" + DOMInfoModel.escapeProtegeString(lDOMPermValDefn.value) + "\"))");
// v1.3					prDDPins.println("  (value \"" + DOMInfoModel.escapeProtegeString(tPermValue) + "\"))");
						prDDPins.println(" ");
						prDDPins.println("([" + vmIdentifier + "] of ValueMeaning");
						prDDPins.println("  (beginDate \"" + DMDocument.beginDatePDS4Value + "\")");
						prDDPins.println("  (containing1 [" + lDOMAttr.evdDataIdentifier + "])");
						prDDPins.println("  (endDate \"" + DMDocument.endDateValue + "\")");
						prDDPins.println("  (description \"" + DOMInfoModel.escapeProtegeString(lDOMPermValDefn.value_meaning) + "\"))");
// v1.3						prDDPins.println("  (description \"" + DOMInfoModel.escapeProtegeString(tPermValueMeaning) + "\"))");
						prDDPins.println(" ");
					}
				}
			}
		}
	}

	// Print the the Protege Pins Properties
	public  void printPDDPPR (PrintWriter prDDPins) {
		ArrayList <DOMProp> lSortedAssocArr = new ArrayList <DOMProp> (DOMInfoModel.masterDOMPropIdMap.values());
		for (Iterator<DOMProp> i = lSortedAssocArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.isInactive) continue;
			if (lDOMProp.identifier.indexOf(DMDocument.ANAME) > -1) continue;  // %3ANAME for protege attribute name; e.g., Schematron_Rule
			String prDataIdentifier = "PR." + lDOMProp.identifier;
			prDDPins.println("([" + prDataIdentifier + "] of Property");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
			prDDPins.println("  (registeredBy [" + DMDocument.registeredByValue + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");						
			prDDPins.println("  (classOrder \"" + lDOMProp.classOrder + "\")");
			prDDPins.println("  (versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
		}
	}

	// Print the the Protege Pins CD
	public  void printPDDPCD (PrintWriter prDDPins) {
		ArrayList <DOMIndexDefn> lCDAttrArr = new ArrayList <DOMIndexDefn> (DOMInfoModel.cdDOMAttrMap.values());
		for (Iterator<DOMIndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			DOMIndexDefn lIndex = (DOMIndexDefn) i.next();
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
			for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				if (lAttr.isInactive) continue;
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
		ArrayList <DOMIndexDefn> lDECAttrArr = new ArrayList <DOMIndexDefn> (DOMInfoModel.decDOMAttrMap.values());
		for (Iterator<DOMIndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			DOMIndexDefn lIndex = (DOMIndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "DEC_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of DataElementConcept");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (expressing ");
			for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				if (lAttr.isInactive) continue;
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
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr= (DOMAttr) i.next();
			if (lDOMAttr.isInactive) continue;
			if (lDOMAttr.isUsedInClass && lDOMAttr.isAttribute) {
				// print TE section
				prDDPins.println("([" + lDOMAttr.teDataIdentifier + "] of TerminologicalEntry");
				prDDPins.println("  (administeredItemContext [NASA_PDS])");
				prDDPins.println("  (definition ["  + lDOMAttr.defDataIdentifier + "])");
				prDDPins.println("  (designation [" + lDOMAttr.desDataIdentifier + "])");
				prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");
		
				// print definition section
				prDDPins.println("([" + lDOMAttr.defDataIdentifier + "] of Definition");
				prDDPins.println("  (definitionText \"" + DOMInfoModel.escapeProtegeString(lDOMAttr.definition) + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
				
				// print designation section
				prDDPins.println("([" + lDOMAttr.desDataIdentifier + "] of Designation");
				prDDPins.println("  (designationName \"" + lDOMAttr.title + "\")");
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
		
		prDDPins.println("([" + DMDocument.registrationAuthorityIdentifierValue + "] of RegistrationAuthorityIdentifier");
		prDDPins.println("	(internationalCodeDesignator \"0001\")");
		prDDPins.println("	(opiSource \"1\")");
		prDDPins.println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
		prDDPins.println("	(organizationPartIdentifier \"Planetary Data System\"))");

		prDDPins.println("([" + DMDocument.registeredByValue + "] of RegistrationAuthority");
		prDDPins.println("	(documentationLanguageIdentifier [LI_English])");
		prDDPins.println("	(languageUsed [LI_English])");
		prDDPins.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
		prDDPins.println("	(organizationName \"NASA Planetary Data System\")");
		prDDPins.println("	(registrar [PDS_Registrar])");
		prDDPins.println("	(registrationAuthorityIdentifier_v [" + DMDocument.registrationAuthorityIdentifierValue + "]))");
	
		prDDPins.println("([NASA_PDS] of Context");
		prDDPins.println("	(dataIdentifier  \"NASA_PDS\"))");
		
		prDDPins.println("([PDS_Registrar] of  Registrar");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(registrarIdentifier \"PDS_Registrar\"))");
		
		prDDPins.println("([Steward_PDS] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
		prDDPins.println("([" + DMDocument.masterPDSSchemaFileDefn.identifier + "] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
		// ops is not included as a defined namespace in the SchemaFileDefn array
		prDDPins.println("([ops] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

// 7777
		
/*		prDDPins.println("([archive] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([bio] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([pdt] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([common] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([wrc] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
		*/
		
		
		prDDPins.println("([Submitter_PDS] of Submitter");
		prDDPins.println("	(contact [DataDesignWorkingGroup])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");
		
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
		for (Iterator<DOMUnit> i = DOMInfoModel.masterDOMUnitArr.iterator(); i.hasNext();) {
			DOMUnit lDOMUnit = (DOMUnit) i.next();		
			prDDPins.println("([" + lDOMUnit.title + "] of UnitOfMeasure");
			prDDPins.println("	(measureName \"" + lDOMUnit.title + "\")");
			prDDPins.println("	(defaultUnitId \"" + DOMInfoModel.escapeProtegeString(lDOMUnit.default_unit_id) + "\")");
			prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
			if (! lDOMUnit.unit_id.isEmpty() )
			{
				String lSpace = "";
				prDDPins.print("	(unitId ");
				// set the units
				for (Iterator<String> j = lDOMUnit.unit_id.iterator(); j.hasNext();) {
					String lVal = (String) j.next();
					prDDPins.print(lSpace + "\"" + DOMInfoModel.escapeProtegeString(lVal) + "\"");
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
		for (Iterator<DOMDataType> i = DOMInfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
			DOMDataType lDataType = (DOMDataType) i.next();	
			prDDPins.println("([" + lDataType.title + "] of DataType");
			prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
			prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
	
	/**
	*  sort attributes by class and namespaces
	*/
	public ArrayList <DOMAttr> getSortedAttrs () {
		TreeMap <String, DOMAttr> lTreeMap = new TreeMap <String, DOMAttr>();
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isInactive) continue;
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				String sortKey = lAttr.title + ":" + lAttr.steward + "." + lAttr.parentClassTitle + ":" + lAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lAttr);
			}
		}
		ArrayList <DOMAttr> lSortedAttrArr = new ArrayList <DOMAttr> (lTreeMap.values());		
		return lSortedAttrArr; 
	}
	
	
	/**
	*  sort attributes and associations by class and namespaces
	*  *** during DOM conversion, only DOMAttr was used. DOMProp might be needed.
	*  *** however getSortedAttrsAssocs is not called.
	*/
	public ArrayList <DOMAttr> getSortedAttrsAssocs () {
		TreeMap <String, DOMAttr> lTreeMap = new TreeMap <String, DOMAttr>();
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (true) {
				String sortKey = lAttr.title + ":" + lAttr.steward + "." + lAttr.parentClassTitle + ":" + lAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lAttr);
			}
		}
		ArrayList <DOMAttr> lSortedAttrArr = new ArrayList <DOMAttr> (lTreeMap.values());		
		return lSortedAttrArr; 
	}
}	
