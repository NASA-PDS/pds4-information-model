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

class WriteDOM11179DDPinsFilePClass extends Object{
	
	PrintWriter prDDPins;

	public WriteDOM11179DDPinsFilePClass () {
		return;
	}

	// write the PINS file
	public void writePINSFile (String lFileName) throws java.io.IOException {
		String lNewFileName = DOMInfoModel.replaceString (lFileName, "dd11179_Gen", "dd11179_GenPClass");
		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lNewFileName)), "UTF-8"));		
		
	    printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the Protege Pins Header
	private void printPDDPHdr () {
		prDDPins.println("	; Tue Jan 26 07:52:47 PST 2010"); // protege write resets date
//		prDDPins.println("	; " + DMDocument.masterTodaysDateTimeUTCwT);
		prDDPins.println("	; ");
		prDDPins.println("	;+ (version \"3.3.1\")");
		prDDPins.println("	;+ (build \"Build 430\")");
		prDDPins.println("");
	}
	
	// Print the Protege Pins Footer
	private  void printPDDPFtr () {
		prDDPins.println("");
	}
	
//	print the body
	private  void printPDDPBody () {
		// print the data elements
		 printPDDPOC (prDDPins);
		 printPDDPDE (prDDPins);
		 printPDDPVD (prDDPins);
		 printPDDPPR (prDDPins);
		 printPDDPCD (prDDPins);
		 printPDDPDEC (prDDPins);
		 printPDDPTEDE (prDDPins); // TE for Data Elements
		 printPDDPTEOC (prDDPins); // TE for Object Classes
		 printPDDPMISC (prDDPins);
	}
	
	// Print the the Protege Pins Object Class (OC)
	private  void printPDDPOC (PrintWriter prDDPins) {
		for (DOMClass lDOMClass : DOMInfoModel.getMasterDOMClassArr ()) {
			if (lDOMClass.ocDataIdentifier.compareTo("TBD_ocDataIdentifier") == 0) {
				continue;
			}
			prDDPins.println("([" + lDOMClass.ocDataIdentifier + "] of ObjectClass");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("  (dataIdentifier \"" + lDOMClass.ocDataIdentifier + "\")");
			prDDPins.println("  (registeredBy [" + lDOMClass.registeredByValue + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + lDOMClass.regAuthId + "])");
			prDDPins.println("  (steward [" + lDOMClass.steward + "])");
			prDDPins.println("  (terminologicalEntry [" + lDOMClass.teDataIdentifier + "])");
			
			prDDPins.println("  (isInactive \"" + lDOMClass.isInactive + "\")");
			prDDPins.println("  (isDeprecated \"" + lDOMClass.isDeprecated + "\")");
			prDDPins.println("  (used \"" + lDOMClass.used + "\")");  // Ignore, Yes, No
			prDDPins.println("  (section \"" + lDOMClass.section + "\")");
			prDDPins.println("  (steward \"" + lDOMClass.steward + "\")");
			prDDPins.println("  (nameSpaceIdNC \"" + lDOMClass.nameSpaceIdNC + "\")");
//			prDDPins.println("  (nameSpaceId \"" + lDOMClass.nameSpaceId + "\")");
			prDDPins.println("  (isVacuous \"" + lDOMClass.isVacuous + "\")");
			prDDPins.println("  (isSchema1Class \"" + lDOMClass.isSchema1Class + "\")");
			prDDPins.println("  (isRegistryClass \"" + lDOMClass.isRegistryClass + "\")");
			prDDPins.println("  (isTDO \"" + lDOMClass.isTDO + "\")");
			prDDPins.println("  (isDataType \"" + lDOMClass.isDataType + "\")");
			prDDPins.println("  (isUnitOfMeasure \"" + lDOMClass.isUnitOfMeasure + "\")");			
			prDDPins.println("  (versionIdentifier \"" + lDOMClass.versionId + "\"))");
		}
	}
		
	// Print the the Protege Pins DE
	private  void printPDDPDE (PrintWriter prDDPins) {
		// print the data elements
		ArrayList <DOMAttr> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<DOMAttr> i = lSortedAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			prDDPins.println("([" + lAttr.deDataIdentifier + "] of DataElement");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
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
// propagated			prDDPins.println("  (isInactive \"" + lAttr.isInactive + "\")");
			prDDPins.println("  (isDeprecated \"" + lAttr.isDeprecated + "\")");
			prDDPins.println("  (isNillable \"" + lAttr.isNilable + "\")");
			prDDPins.println("  (steward [" + lAttr.steward + "])");
			prDDPins.println("  (submitter [" + lAttr.submitter + "])");
			prDDPins.println("  (terminologicalEntry [" + lAttr.teDataIdentifier + "])");
			prDDPins.println("  (versionIdentifier \"" + lAttr.versionIdentifierValue + "\"))");
		}
	}
	
	// Print the the Protege Pins VD
	private  void printPDDPVD (PrintWriter prDDPins) {
		// print the value domain

		ArrayList <DOMAttr> lSortedAttrArr = getSortedAttrs ();
		for (Iterator<DOMAttr> i = lSortedAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			if (lDOMAttr.isEnumerated) {
				prDDPins.println("([" + lDOMAttr.evdDataIdentifier + "] of EnumeratedValueDomain");
			} else {
				prDDPins.println("([" + lDOMAttr.nevdDataIdentifier  + "] of NonEnumeratedValueDomain");	
			}
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			if (lDOMAttr.isEnumerated) {
				if ( ! (lDOMAttr.domPermValueArr == null || lDOMAttr.domPermValueArr.isEmpty())) {
					boolean isNewLine = false;
					prDDPins.println("  (containedIn1 ");
					// get the permissible value instance identifiers	
					for (Iterator<DOMProp> j = lDOMAttr.domPermValueArr.iterator(); j.hasNext();) {
						DOMProp lDOMProp = (DOMProp) j.next();
						if ( ! (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn)) continue;
						DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
						if (isNewLine) prDDPins.println("");
						if (lDOMPermValDefn != null) {
				 			int lHashCodeI = lDOMPermValDefn.value.hashCode();
							String lHashCodeS = Integer.toString(lHashCodeI);
							String pvIdentifier =  "pv." + lDOMAttr.dataIdentifier + "." + lHashCodeS;	// permissible value
							prDDPins.print("    [" + pvIdentifier  + "]");
						}
						isNewLine = true;
					}
					prDDPins.println(")");
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
				boolean isNewLine = true;				
				for (Iterator<String> j = lDOMAttr.allowedUnitId.iterator(); j.hasNext();) {
					String lUnitId = (String) j.next();
					if (allowedUnitIdInd == allowedUnitIdLmt) isNewLine = false;
					if (isNewLine) prDDPins.println("    \"" + lUnitId + "\"");
					else           prDDPins.print("    \"" + lUnitId + "\"");
				}
				prDDPins.println(")");
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
// propagated			prDDPins.println("  (isInactive \"" + lDOMPermValDefn.isInactive + "\")");
						prDDPins.println("  (isDeprecated \"" + lDOMPermValDefn.isDeprecated + "\")");
						prDDPins.println("  (usedIn [" + vmIdentifier + "])");
						prDDPins.println("  (value \"" + DOMInfoModel.escapeProtegeString(lDOMPermValDefn.value) + "\"))");
						prDDPins.println(" ");
						prDDPins.println("([" + vmIdentifier + "] of ValueMeaning");
						prDDPins.println("  (beginDate \"" + DMDocument.beginDatePDS4Value + "\")");
						prDDPins.println("  (containing1 [" + lDOMAttr.evdDataIdentifier + "])");
						prDDPins.println("  (endDate \"" + DMDocument.endDateValue + "\")");
						prDDPins.println("  (description \"" + DOMInfoModel.escapeProtegeString(lDOMPermValDefn.value_meaning) + "\"))");
						prDDPins.println(" ");
					}
				}
			}
		}
	}

	// Print the the Protege Pins Properties
	private  void printPDDPPR (PrintWriter prDDPins) {
		for (DOMProp lDOMProp : DOMInfoModel.getMasterDOMPropArr()) {
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
	private  void printPDDPCD (PrintWriter prDDPins) {
		ArrayList <DOMIndexDefn> lCDAttrArr = new ArrayList <DOMIndexDefn> (DOMInfoModel.cdDOMAttrMap.values());
		for (Iterator<DOMIndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			DOMIndexDefn lIndex = (DOMIndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "CD_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of ConceptualDomain");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			boolean isNewLine = false;
			prDDPins.println("  (having ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lDEC = (String) j.next();
				if (isNewLine) prDDPins.println("");
				prDDPins.print("    [" + "DEC_" + lDEC + "]");
				isNewLine = true;
			}
			prDDPins.println(")");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");

			isNewLine = false;
			prDDPins.println("  (representing2 ");
			for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				if (isNewLine) prDDPins.println("");
				if (lAttr.isEnumerated) {
					prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
				} else {
					prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
				}
				isNewLine = true;
			}
			prDDPins.println(")");
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
	private  void printPDDPDEC (PrintWriter prDDPins) {
		ArrayList <DOMIndexDefn> lDECAttrArr = new ArrayList <DOMIndexDefn> (DOMInfoModel.decDOMAttrMap.values());
		for (Iterator<DOMIndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			DOMIndexDefn lIndex = (DOMIndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "DEC_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of DataElementConcept");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			boolean isNewLine = false;
			prDDPins.println("  (expressing ");
			for (Iterator<DOMAttr> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				if (isNewLine) prDDPins.println("");
				prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
				isNewLine = true;
			}
			prDDPins.println(")");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");
			isNewLine = false;
			prDDPins.println("  (specifying ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lCD = (String) j.next();
				if (isNewLine) prDDPins.println("");
				prDDPins.print("    [" + "CD_" + lCD + "]");
				isNewLine = true;
			}
			prDDPins.println(")");
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
	
	// Print the the Protege Pins TE for Data Elements
	private  void printPDDPTEDE (PrintWriter prDDPins) {
		
		// print the Terminological Entry
		for (DOMAttr lDOMAttr : DOMInfoModel.getMasterDOMAttrArr()) {
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
	
	// Print the the Protege Pins TE for Object Classes
	private  void printPDDPTEOC (PrintWriter prDDPins) {
		
		// print the Terminological Entry
		for (DOMClass lDOMClass : DOMInfoModel.getMasterDOMClassArr()) {
			if (lDOMClass.title.compareTo(lDOMClass.title) == 0) { // always true
				// print TE section
				prDDPins.println("([" + lDOMClass.teDataIdentifier + "] of TerminologicalEntry");
				prDDPins.println("  (administeredItemContext [NASA_PDS])");
				prDDPins.println("  (definition ["  + lDOMClass.defDataIdentifier + "])");
				prDDPins.println("  (designation [" + lDOMClass.desDataIdentifier + "])");
				prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");
		
				// print definition section
				prDDPins.println("([" + lDOMClass.defDataIdentifier + "] of Definition");
				prDDPins.println("  (definitionText \"" + DOMInfoModel.escapeProtegeString(lDOMClass.definition) + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
				
				// print designation section
				prDDPins.println("([" + lDOMClass.desDataIdentifier + "] of Designation");
				prDDPins.println("  (designationName \"" + lDOMClass.title + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
			}
		}
	}	

	// Print the the Protege Pins Misc
	private  void printPDDPMISC (PrintWriter prDDPins) {
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
		for (DOMUnit lDOMUnit : DOMInfoModel.getMasterDOMUnitArr()) {	
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
		for (DOMDataType lDOMDataType : DOMInfoModel.getMasterDOMDataTypeArr()) {
			prDDPins.println("([" + lDOMDataType.title + "] of DataType");
			prDDPins.println("  (dataTypeName \"" + lDOMDataType.title + "\")");
			prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
	
	/**
	*  sort attributes by class and namespaces
	*/
	private ArrayList <DOMAttr> getSortedAttrs () {
		TreeMap <String, DOMAttr> lTreeMap = new TreeMap <String, DOMAttr>();
		for (DOMAttr lDOMAttr : DOMInfoModel.getMasterDOMAttrArr()) {
			if (lDOMAttr.isUsedInClass && lDOMAttr.isAttribute) {
				String sortKey = lDOMAttr.title + ":" + lDOMAttr.steward + "." + lDOMAttr.parentClassTitle + ":" + lDOMAttr.classSteward;
				sortKey = sortKey.toUpperCase();
				lTreeMap.put(sortKey, lDOMAttr);
			}
		}
		ArrayList <DOMAttr> lSortedAttrArr = new ArrayList <DOMAttr> (lTreeMap.values());		
		return lSortedAttrArr; 
	}
}	
