package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Writes the PDS4 DD content to a JSON file 
 *   
 */

class WriteLODSKOSFile extends Object{
	TreeMap <String, AttrDefn> lRefAttrArr = new TreeMap <String, AttrDefn>();
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
	PrintWriter prDDPins;

	public WriteLODSKOSFile () {
		return;
	}

	// write the SKOS file
	public void writeSKOSFile (String lFileName) throws java.io.IOException {
		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the JSON Header
	public void printPDDPHdr () {
		prDDPins.println("@prefix skos: <http://www.w3.org/2004/02/skos/core#> .");
		prDDPins.println("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .");
		prDDPins.println("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .");
		prDDPins.println("@prefix owl: <http://www.w3.org/2002/07/owl#> .");
		prDDPins.println("@prefix dct: <http://purl.org/dc/terms/> .");
		prDDPins.println("@prefix foaf: <http://xmlns.com/foaf/0.1/> .");
//		prDDPins.println("@prefix ex: <http://www.example.com/> .");
//		prDDPins.println("@prefix ex1: <http://www.example.com/1/> .");
//		prDDPins.println("@prefix ex2: <http://www.example.com/2/> .");
		prDDPins.println("@prefix pds: <http://pds.jpl.nasa.gov/pds4/schema/develop/pds/skos/> .");
		
	}
	
	// Format the Boolean String for JSON
	public String formBooleanValue(boolean lBoolean) {
		String rString = "" + lBoolean;
		return formValue(rString);
	}

	// Format the String for JSON
	public String formValue(String lString) {
		String rString = lString;
		if (rString == null) rString = "null";
		if (rString.indexOf("TBD") == 0) rString = "null";
		rString = InfoModel.escapeJSONChar(rString);
		rString = "\"" + rString + "\"";
		return rString;
	}

	// Print the JSON Footer
	public  void printPDDPFtr () {
		prDDPins.println("  ");
	}
	
//	print the JSON body
	public  void printPDDPBody () {
//		prDDPins.println("");
		printClass (prDDPins);
		printAttr (prDDPins);
	}
	
	// Print the classes
	public  void printClass (PrintWriter prDDPins) {
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.title.indexOf("PDS3") > -1) continue;
			if (lClass.isDataType) continue;
			if (lClass.isUnitOfMeasure) continue;
			if (lClass.isUSERClass) continue;

			// filter out abstract classes used for organization. (leave in Table_Base, Record, etc)
			if (lClass.title.compareTo("Product_Components") == 0) continue;

			// filter out all non Common classes
			if (! (lClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0 && lClass.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0)) continue;
			
			prDDPins.println("\npds:" + lClass.title + " " + "rdf:type" + " " + "owl:Class;");	
			prDDPins.println("  " + "a" + " " + "skos:Concept;");	
			prDDPins.println("  " + "skos:prefLabel" + " " +  " \"" + InfoModel.escapeXMLChar(lClass.title) + "\"@en;");	
			prDDPins.println("  " + "skos:definition" + " " +  " \"" + InfoModel.escapeXMLChar(lClass.description) + "\"@en;");	
			
			// write the superclass
			if (! (lClass.subClassOfTitle.indexOf("TBD") == 0 || lClass.subClassOfTitle.indexOf("TNDO") == 0))  
				prDDPins.println("  " + "skos:broader" + " " +  "pds:" + lClass.subClassOfTitle + ";");	
			
			// write the subclasses
			for (Iterator<PDSObjDefn> j = lClass.subClass.iterator(); j.hasNext();) {
				PDSObjDefn lSubClass = (PDSObjDefn) j.next();
				if (lSubClass.title.compareTo("Product_Components") == 0) continue;
				prDDPins.println("  " + "skos:narrower" + " " +  "pds:" + lSubClass.title + ";");	
			}
			
/*			// write and save the member attributes
			for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (! lAttr.isAttribute) continue;
				lRefAttrArr.put(lAttr.attrNameSpaceId + lAttr.title, lAttr);
				prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lAttr.title + ";");
			} */
			
			// write and save the member attributes
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
//				if (! lAttr.isAttribute) continue;
				lRefAttrArr.put(lAttr.attrNameSpaceId + lAttr.title, lAttr);
				prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lAttr.title + ";");
			}
			
			// write and save the member class
			for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("data_object") == 0) continue;
				for (Iterator<PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {
					PDSObjDefn lCompClass = (PDSObjDefn) k.next();
					prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lCompClass.title + ";");
				}
			}
			prDDPins.println("  " + "skos:historyNote" + " " +  " \"" + "PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "  Class Version "  + lClass.versionId + "\"@en.");	
			
			

			//			prDDPins.println("");	
			
//			ex:Jewellery rdf:type owl:Class;
//			a skos:Concept;
//			skos:prefLabel "Jewellery"@en;
//			skos:altLabel "Jewelry"@en;
//			skos:altLabel "Jewelery"@en;
//			skos:broader ex:Product.			
					
		}
	}		

	// Print the Attributes
	public  void printAttr (PrintWriter prDDPins) {
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (lRefAttrArr.values());
		
		// write the attributes
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			if (! lAttr.isAttribute) continue;
			PDSObjDefn lClass = lAttr.attrParentClass;
			prDDPins.println("\npds:" + lAttr.title + " " + "rdf:type" + " " + "owl:ObjectProperty;");	
			prDDPins.println("  " + "a" + " " + "skos:Concept;");	
			prDDPins.println("  " + "skos:prefLabel" + " " +  " \"" + InfoModel.escapeXMLChar(lAttr.title) + "\"@en;");	
			prDDPins.println("  " + "skos:definition" + " " +  " \"" + InfoModel.escapeXMLChar(lAttr.description) + "\"@en;");	
//			prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lClass.title + ";");	
			prDDPins.println("  " + "skos:historyNote" + " " +  " \"" + "PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "  Class Version "  + lClass.versionId + "\"@en.");	
		}
	}		
	

	// Print the Attributes
	public  void printAttrxxxyyy (PrintWriter prDDPins) {
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassIdMap.values());
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.title.indexOf("PDS3") > -1) continue;
			if (lClass.isDataType) continue;
			if (lClass.isUnitOfMeasure) continue;
			if (lClass.isUSERClass) continue;

			// filter out abstract classes used for organization. (leave in Table_Base, Record, etc)
			if (lClass.title.compareTo("Product_Components") == 0) continue;

			// filter out all non Common classes
			if (! (lClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0 && lClass.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0)) continue;

			// write the attributes
			for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (! lAttr.isAttribute) continue;
				prDDPins.println("\npds:" + lAttr.title + " " + "rdf:type" + " " + "owl:Property;");	
				prDDPins.println("  " + "a" + " " + "skos:Concept;");	
				prDDPins.println("  " + "skos:prefLabel" + " " +  " \"" + InfoModel.escapeXMLChar(lAttr.title) + "\"@en;");	
				prDDPins.println("  " + "skos:definition" + " " +  " \"" + InfoModel.escapeXMLChar(lAttr.description) + "\"@en;");	
				prDDPins.println("  " + "skos:historyNote" + " " +  " \"" + "PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "  Class Version "  + lClass.versionId + "\"@en.");	
				prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lClass.title);	
			}			
			
			//			prDDPins.println("");	
			
//			ex:Jewellery rdf:type owl:Class;
//			a skos:Concept;
//			skos:prefLabel "Jewellery"@en;
//			skos:altLabel "Jewelry"@en;
//			skos:altLabel "Jewelery"@en;
//			skos:broader ex:Product.						
		}
	}		
	
	// Print the Associations
	public  void printAssoc (PDSObjDefn lClass, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lClass.allAttrAssocArr.isEmpty()) return;
		prDDPins.println("              , " + formValue("associationList") + ": [");				
		for (Iterator<AttrDefn> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (isFirst) {
				prDDPins.println("             {" + formValue("association") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("           , {" + formValue("association") + ": {");				
			}				
			prDDPins.println("                " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");	
			prDDPins.println("                " + formValue("title") + ": " + formValue(lAttr.title) + " ,");	
			prDDPins.println("                " + formValue("isAttribute") + ": " + formBooleanValue(lAttr.isAttribute) + " ,");	
			prDDPins.println("                " + formValue("minimumCardinality") + ": " + formValue(lAttr.cardMin) + " ,");	
			prDDPins.println("                " + formValue("maximumCardinality") + ": " + formValue(lAttr.cardMax));	
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		prDDPins.println("           ]");
	}
	
	// Print the the Protege Pins DE
	public  void printAttrxxx (PrintWriter prDDPins) {
		// print the data elements
		boolean isFirst = true;
		ArrayList <AttrDefn> lAttrArr = new ArrayList (InfoModel.masterMOFAttrIdMap.values());
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! (lAttr.isUsedInClass && lAttr.isAttribute)) continue;
			if (isFirst) {
				prDDPins.println("        {");
				isFirst = false;
			} else {
				prDDPins.println("      , {");				
			}
			prDDPins.println("          " + formValue("attribute") + ": {");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lAttr.title) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lAttr.attrNameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("steward") + ": " + formValue(lAttr.steward) + " ,");	
			prDDPins.println("            " + formValue("versionId") + ": " + formValue(lAttr.versionIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("description") + ": " + formValue(lAttr.description) + " ,");	
			prDDPins.println("            " + formValue("isNillable") + ": " + formBooleanValue(lAttr.isNilable) + " ,");	
			prDDPins.println("            " + formValue("isEnumerated") + ": " + formBooleanValue(lAttr.isEnumerated) + " ,");	
			prDDPins.println("            " + formValue("dataType") + ": " + formValue(lAttr.valueType) + " ,");	
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lAttr.getMinimumCharacters2(true,true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lAttr.getMaximumCharacters2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lAttr.getMinimumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lAttr.getMaximumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("pattern") + ": " + formValue(lAttr.getPattern(true)) + " ,");	
			prDDPins.println("            " + formValue("unitOfMeasure") + ": " + formValue(lAttr.getUnitOfMeasure(false)) + " ,");	
			prDDPins.println("            " + formValue("unitId") + ": " + formValue(lAttr.getUnits(false)) + " ,");	
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lAttr.getDefaultUnitId(false)));	
			printPermValues (lAttr, prDDPins);
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}

	// Print the Permissible Values and Value Meanings
	public  void printPermValues (AttrDefn lAttr, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lAttr.permValueArr.isEmpty()) return;
		prDDPins.println("          , " + formValue("PermissibleValueList") + ": [");				
		for (Iterator<PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("PermissibleValue") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lPermValueDefn.value) + " ,");	
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue(lPermValueDefn.value_meaning));	
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");
	}
	
	// Print the Data Types
	public  void printDataType (PrintWriter prDDPins) {
		boolean isFirst = true;
		for (Iterator<DataTypeDefn> i = InfoModel.masterDataTypesArr2.iterator(); i.hasNext();) {
			DataTypeDefn lDataType = (DataTypeDefn) i.next();
			if (isFirst) {
				prDDPins.println("        {" + formValue("DataType") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("      , {" + formValue("DataType") + ": {");			
			}	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lDataType.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lDataType.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lDataType.nameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lDataType.getMinimumCharacters2(true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lDataType.getMaximumCharacters2(true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lDataType.getMinimumValue2(true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lDataType.getMaximumValue2(true)));	
			printPattern(lDataType, prDDPins);
			prDDPins.println("            }");
			prDDPins.println("         }");
		}
	}	
	
	// Print the data type Pattern
	public  void printPattern (DataTypeDefn lDataType, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lDataType.pattern.isEmpty()) return;
		prDDPins.println("          , " + formValue("patternList") + ": [");	
		for (Iterator<String> i = lDataType.pattern.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("Pattern") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("Pattern") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("Pattern") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		prDDPins.println("           ]");
	}

	// Print the Units
	public  void printUnits (PrintWriter prDDPins) {
		boolean isFirst = true;
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();
			if (isFirst) {
				prDDPins.println("        {" + formValue("Unit") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("      , {" + formValue("Unit") + ": {");			
			}	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lUnit.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lUnit.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(DMDocument.masterNameSpaceIdNCLC) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lUnit.default_unit_id));	
			printUnitId (lUnit, prDDPins); 
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}	

	// Print the Unit Identifier
	public  void printUnitId (UnitDefn lUnit, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lUnit.unit_id.isEmpty()) return;
		prDDPins.println("          , " + formValue("unitIdList") + ": [");	
		for (Iterator<String> i = lUnit.unit_id.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("UnitId") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("UnitId") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("UnitId") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");

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
			prDDPins.println("  (registeredBy [" + DMDocument.registeredByValue + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");						
			prDDPins.println("  (classOrder \"" + lAssoc.classOrder + "\")");
//			prDDPins.println("  (versionIdentifier \"" + InfoModel.identifier_version_id + "\"))");
			prDDPins.println("  (versionIdentifier \"" + DMDocument.masterPDSSchemaFileDefn.identifier_version_id + "\"))");
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
//		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
//		for (Iterator<AttrDefn> i = InfoModel.getMasterMOFAttrIdMap().iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
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

		prDDPins.println("([pds] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([img] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

		prDDPins.println("([rings] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [" + DMDocument.registeredByValue + "]))");

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
}	
