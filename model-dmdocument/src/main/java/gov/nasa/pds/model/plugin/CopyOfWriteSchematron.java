package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class CopyOfWriteSchematron extends Object {	
	PrintWriter prSchematron;

	public CopyOfWriteSchematron () {
		return;
	}
	
//	write all Schematron files
	public void writeSchematronFile (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap) throws java.io.IOException {
//		System.out.println("\ndebug writeSchematronFile");

		if (! DMDocument.LDDToolFlag) {
			prSchematron = new PrintWriter(new FileWriter(lSchemaFileDefn.directoryPath + lSchemaFileDefn.fileNameUC + "_" + lSchemaFileDefn.lab_version_id + "." + "sch", false));
		} else {		
			prSchematron = new PrintWriter(new FileWriter(DMDocument.LDDToolOutputFileNameNE + "_" +  lSchemaFileDefn.fileNameNC + "_" + lSchemaFileDefn.lab_version_id +".sch", false));
		}
		writeSchematronRule(lSchemaFileDefn, lMasterMOFClassMap, prSchematron);
		prSchematron.close();	
		return;
	}
		
//	write the schematron rules
	public void writeSchematronRule (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap, PrintWriter prSchematron) {
//		System.out.println("\ndebug writeSchematronRule");
		
		// write schematron file header
		printSchematronFileHdr (lSchemaFileDefn, prSchematron); 
		printSchematronFileCmt (prSchematron);
		
		// add the enumerated value schematron rules		
		addSchematronRuleEnumerated (lSchemaFileDefn, lMasterMOFClassMap);
		
		// add the boolean (true, false) schematron rules		
		addSchematronRuleBoolean (lSchemaFileDefn, lMasterMOFClassMap);
				
		// add the deprecated item schematron rules	(Insert)
		if (! DMDocument.deprecatedAdded) {
			DMDocument.deprecatedAdded = true;
			addSchematronRuleDeprecated (lSchemaFileDefn, lMasterMOFClassMap);
		}

		// add the Science Facet schematron rules		
		addSchematronRuleDisciplineFacets (lSchemaFileDefn, InfoModel.sfDisciplineFacetDefnMap);
				
		// add the unit values schematron rules
		addSchematronRuleUnits (lSchemaFileDefn, lMasterMOFClassMap, prSchematron);
				
		// If this is an LDD run set lRule.alwaysInclude to false for all customized rules
		if (DMDocument.LDDToolFlag) {
			ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
			for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
				RuleDefn lRule = (RuleDefn) i.next();
				lRule.alwaysInclude = false;
			}
		}
		
		// write the customized class based schematron rules
//		if (! DMDocument.LDDToolFlag) {
//			writeSchematronRuleClasses (lSchemaFileDefn, prSchematron);
//		}
//		writeSchematronRuleClasses (lSchemaFileDefn, prSchematron);

		// select out the rules for this namespace
		ArrayList <RuleDefn> lSelectRuleArr = new ArrayList <RuleDefn> ();
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			if (lRule.classNameSpaceNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) == 0) lSelectRuleArr.add(lRule);
		}		
		writeSchematronRuleClasses (lSchemaFileDefn, lSelectRuleArr, prSchematron);
		
		// add the deprecated rules (no insert)
//		addSchematronRuleDeprecated (prSchematron);
		
		// write schematron file footer
		printSchematronFileFtr (prSchematron); 
	}	
	
//	add the enumerated value schematron rules
	public void addSchematronRuleEnumerated (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap) {
//		System.out.println("\naddSchematronRuleEnumerated");
		
		// add attribute enumerated values assertions
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (lMasterMOFClassMap.values());
		for (Iterator <PDSObjDefn> i = lSortClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if ((lClass == null) || (! ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) && lSchemaFileDefn.stewardArr.contains(lClass.steward)))) continue;
			if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) continue;
			if (lClass.ownedAttrAssocAssertArr == null || lClass.ownedAttrAssocAssertArr.isEmpty()) continue;
			String lDeprecatedClassIdentifier = lClass.nameSpaceIdNC + "." + lClass.title;
			if (DMDocument.deprecatedAttrValueArr.contains(lDeprecatedClassIdentifier)) continue;
//			System.out.println("debug addSchematronRuleEnumerated Class lDeprecatedClassIdentifier:" + lDeprecatedClassIdentifier);
			
			String lRuleId = lClass.nameSpaceId + lClass.title;
			RuleDefn lRule = InfoModel.schematronRuleMap.get(lRuleId);
			if (lRule == null) {
				lRule = new RuleDefn(lRuleId);		
				InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
				lRule.xpath = lRuleId;
				lRule.attrTitle = "TBD_AttrTitle";		
				lRule.attrNameSpaceNC = "TBD_attrNameSpaceNC";		
				lRule.classTitle = lClass.title;		
				lRule.classNameSpaceNC = lClass.nameSpaceIdNC;	
			}
			for (Iterator <AttrDefn> j = lClass.ownedAttrAssocAssertArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();	
				String lDeprecatedAttrIdentifier = lDeprecatedClassIdentifier + "." + lAttr.title;
//				System.out.println("debug addSchematronRuleEnumerated Attribute lDeprecatedAttrIdentifier:" + lDeprecatedAttrIdentifier);
				if (DMDocument.deprecatedAttrValueArr.contains(lDeprecatedAttrIdentifier)) continue;
				if (lAttr.valArr == null || lAttr.valArr.isEmpty()) continue;
				String assertMsgPre = " must be equal to one of the following values "; 
				if (lAttr.valArr.size() == 1) {
					assertMsgPre = " must be equal to the value "; 					
				}
				lRule.attrTitle = lAttr.title;		
				lRule.attrNameSpaceNC = lAttr.attrNameSpaceIdNC;
				String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
				if (foundAssertStmt (lAttrId, lRule.assertArr)) continue;
				AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
				lAssert.assertStmt = "if (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + ") then " + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + " = (";	
				lAssert.assertMsg =  "The attribute " + lAttrId + assertMsgPre;
				lRule.assertArr.add(lAssert);
				String lDel = "";
				for (Iterator<String> k = lAttr.valArr.iterator(); k.hasNext();) {
					String lVal = (String) k.next();
					String lDeprecatedValueIdentifier = lDeprecatedAttrIdentifier + "." + lVal;
					if (DMDocument.deprecatedAttrValueArr.contains(lDeprecatedValueIdentifier)) continue;
//					System.out.println("debug addSchematronRuleEnumerated Value lDeprecatedValueIdentifier:" + lDeprecatedValueIdentifier);
					String lValString = lDel + "'" + lVal + "'";					
					lDel = ", ";					
					lAssert.assertStmt += lValString;	
					lAssert.assertMsg += lValString;
				}
				lAssert.assertStmt += ") else true()";	
				lAssert.assertMsg +=  ".";
			}
		} 
	}

//	add the boolean schematron rules
	public void addSchematronRuleBoolean (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap) {
//		System.out.println("\naddSchematronRuleBoolean");
		
		// add attribute enumerated values assertions
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (lMasterMOFClassMap.values());
		for (Iterator <PDSObjDefn> i = lSortClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if ((lClass == null) || (! ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) && lSchemaFileDefn.stewardArr.contains(lClass.steward)))) continue;
			if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) continue;
			if (lClass.ownedAttrAssocAssertArr == null || lClass.ownedAttrAssocAssertArr.isEmpty()) continue;
//			System.out.println("\ndebug addSchematronRuleBoolean - lClass.identifier:" + lClass.identifier);
			String lRuleId = lClass.nameSpaceId + lClass.title;
			RuleDefn lRule = InfoModel.schematronRuleMap.get(lRuleId);
			if (lRule == null) {
				lRule = new RuleDefn(lRuleId);
				
				// *** We might be adding the rule unnecessarily ***
				
				InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
				lRule.xpath = lRuleId;
				lRule.attrTitle = "TBD_AttrTitle";		
				lRule.attrNameSpaceNC = "TBD_attrNameSpaceNC";		
				lRule.classTitle = lClass.title;		
				lRule.classNameSpaceNC = lClass.nameSpaceIdNC;	
			}
			for (Iterator <AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();	
//				System.out.println("debug addSchematronRuleBoolean - lAttr.identifier:" + lAttr.identifier);
//				System.out.println("debug addSchematronRuleBoolean - lAttr.valueType:" + lAttr.valueType);

				if (lAttr.valueType.compareTo("ASCII_Boolean") != 0) continue;
//				System.out.println("debug addSchematronRuleBoolean FOUND Boolean - lAttr.valueType:" + lAttr.valueType);
				String assertMsgPre = " must be equal to one of the following values "; 
				lRule.attrTitle = lAttr.title;		
				lRule.attrNameSpaceNC = lAttr.attrNameSpaceIdNC;
				String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
				if (foundAssertStmt (lAttrId, lRule.assertArr)) continue;
				AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
				lAssert.assertStmt = "if (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + ") then " + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + " = (";	
				lAssert.assertMsg =  "The attribute " + lAttrId + assertMsgPre;

				lRule.assertArr.add(lAssert);
				
				lAssert.assertStmt += "'true', 'false'";	
				lAssert.assertMsg += "'true', 'false'";
				lAssert.assertStmt += ") else true()";	
				lAssert.assertMsg +=  ".";
			}
		} 
	}	
	
//	add the schematron rules for deprecation
	public void addSchematronRuleDeprecated (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap) {

		// iterate for each deprecation
		for (Iterator <DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lObject = (DeprecatedDefn) i.next();
//			System.out.println("\ndebug addSchematronRuleDeprecated - lObject.identifier:" + lObject.identifier);
			String lRuleId = lObject.context;
			RuleDefn lRule = InfoModel.schematronRuleMap.get(lRuleId);
			if (lRule == null) {
				lRule = new RuleDefn(lRuleId);
				InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
				lRule.xpath = lRuleId;
				lRule.role = " role=\"warning\"";
				lRule.attrTitle = lObject.title;		
				lRule.attrNameSpaceNC = lObject.nameSpaceIdNC;		
				lRule.classTitle = lObject.className;		
				lRule.classNameSpaceNC = lObject.nameSpaceIdNC;	
			}
			String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
			AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
			lRule.assertArr.add(lAssert);
			if (! lObject.isValue) {
				lAssert.assertStmt = "name() = '" + lObject.context + "'";	
				lAssert.assertMsg =  lObject.context + " is deprecated and should not be used.";
			} else {
				lAssert.assertStmt = lObject.nameSpaceIdNC + ":" + lObject.attrName + " != '" + lObject.value + "'";	
				lAssert.assertMsg =  "The value " + lObject.value + " for attribute " + lObject.className + "." + lObject.attrName + " is deprecated and should not be used.";
			}
		} 
	}			
	
//	add the Discipline schematron rules
	public void addSchematronRuleDisciplineFacets (SchemaFileDefn lSchemaFileDefn, TreeMap <String, SFDisciplineFacetDefn> lDisciplineFacetMap) {
		// add attribute enumerated values assertions
		ArrayList <SFDisciplineFacetDefn> lDisciplineFacetArr = new ArrayList <SFDisciplineFacetDefn> (lDisciplineFacetMap.values());
		ArrayList <String> lDiscNameFacet1Arr = new ArrayList <String> ();
		ArrayList <String> lDiscNameFacet2Arr = new ArrayList <String> ();
		
		// set up the Primary Result Summary rule
		String lRuleId = "pds:Primary_Result_Summary/pds:Science_Facets";
		RuleDefn lRule = InfoModel.schematronRuleMap.get(lRuleId);
		lRule = new RuleDefn(lRuleId);
		InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = lRuleId;
		lRule.attrTitle = "discipline_name";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Science_Facets";		
		lRule.classNameSpaceNC = "pds";	
		String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
		
		// create the discipline name rule
		if (lDisciplineFacetArr.size() > 0) {
			AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
			lRule.assertArr.add(lAssert);
			lAssert.assertStmt = "if (pds:discipline_name) then pds:discipline_name" + " = (";	
			lAssert.assertMsg =  "The attribute pds:discipline_name must be equal to one of the following values ";
			String lValueList = "";
			String lDelimiter = "";
			for (Iterator <SFDisciplineFacetDefn> i = lDisciplineFacetArr.iterator(); i.hasNext();) {
				SFDisciplineFacetDefn lDiscFacet = (SFDisciplineFacetDefn) i.next();
//				System.out.println("debug addSchematronRuleDisciplineFacets - lGroupFacet.identifier:" + lDiscFacet.identifier);
				lValueList += lDelimiter + "'" + lDiscFacet.disciplineName + "'";
				lDelimiter = ", ";
			}
			lAssert.assertStmt += lValueList;	
			lAssert.assertMsg += lValueList;
			lAssert.assertStmt += ") else true()";	
			lAssert.assertMsg +=  ".";
		}
		
		// create assert statement for each discipline name
		for (Iterator <SFDisciplineFacetDefn> i = lDisciplineFacetArr.iterator(); i.hasNext();) {
			SFDisciplineFacetDefn lDiscFacet = (SFDisciplineFacetDefn) i.next();
			lRule.attrTitle = lDiscFacet.disciplineName;		
			lRule.attrNameSpaceNC = "pds";
			
			// get all the facet1 assert statements
			if (lDiscFacet.groupFacet1Arr.size() > 0) {
				lDiscNameFacet1Arr.add(lDiscFacet.disciplineName); 
				AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
				lAssert.assertStmt = "if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq '" + lDiscFacet.disciplineName + "')) then " + "pds:facet1" + " = (";	
				lAssert.assertMsg =  "If the attribute pds:discipline_name equals " + lDiscFacet.disciplineName + " then if present pds:facet1 must be equal to one of the following values ";
				lRule.assertArr.add(lAssert);
				String lValueList = "";
				String lDelimiter = "";
				for (Iterator <SFGroupFacetDefn> j = lDiscFacet.groupFacet1Arr.iterator(); j.hasNext();) {
					SFGroupFacetDefn lGroupFacet = (SFGroupFacetDefn) j.next();	
//					System.out.println("debug addSchematronRuleDisciplineFacets - lGroupFacet.identifier:" + lGroupFacet.identifier);
					lValueList += lDelimiter + "'" + lGroupFacet.facet + "'";
					lDelimiter = ", ";
				}
				lAssert.assertStmt += lValueList;	
				lAssert.assertMsg += lValueList;
				lAssert.assertStmt += ") else true()";	
				lAssert.assertMsg +=  ".";
//			} else {
//				lDiscNameFacet1Arr.add(lDiscFacet.disciplineName); 
//				System.out.println("debug addSchematronRuleDisciplineFacets -1- lDiscFacet.disciplineName:" + lDiscFacet.disciplineName);
			}
			
			// get all the facet2 assert statements
			if (lDiscFacet.groupFacet2Arr.size() > 0) {
				lDiscNameFacet2Arr.add(lDiscFacet.disciplineName); 
				AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
				lAssert.assertStmt = "if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq '" + lDiscFacet.disciplineName + "')) then " + "pds:facet2" + " = (";	
				lAssert.assertMsg =  "If the attribute pds:discipline_name equals " + lDiscFacet.disciplineName + " then if present pds:facet2 must be equal to one of the following values ";
				lRule.assertArr.add(lAssert);
				String lValueList = "";
				String lDelimiter = "";
				for (Iterator <SFGroupFacetDefn> j = lDiscFacet.groupFacet2Arr.iterator(); j.hasNext();) {
					SFGroupFacetDefn lGroupFacet = (SFGroupFacetDefn) j.next();	
//					System.out.println("debug addSchematronRuleDisciplineFacets - lGroupFacet.identifier:" + lGroupFacet.identifier);
					lValueList += lDelimiter + "'" + lGroupFacet.facet + "'";
					lDelimiter = ", ";
				}
				lAssert.assertStmt += lValueList;	
				lAssert.assertMsg += lValueList;
				lAssert.assertStmt += ") else true()";	
				lAssert.assertMsg +=  ".";
			} else {
//				lDiscNameFacet2Arr.add(lDiscFacet.disciplineName); 
//				System.out.println("debug addSchematronRuleDisciplineFacets  -2- lDiscFacet.disciplineName:" + lDiscFacet.disciplineName);

			}
		} 
		
		// create the rules for disallowed facets
		int lNumDiscNames = lDisciplineFacetArr.size();
		if (lNumDiscNames > lDiscNameFacet1Arr.size()) {
			AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
			lRule.assertArr.add(lAssert);
			lAssert.assertStmt = "if (pds:discipline_name and pds:facet1) then pds:discipline_name = (";	
			lAssert.assertMsg =  "Facet1 is allowed only when pds:discipline_name is one of the following ";
			// if (pds:discipline_name and pds:facet1) then pds:discipline_name = ('Fields', 'Particles') else true()
			// Facet1 is allowed only for pds:discipline_name equals 'Fields' or 'Particles'.
			String lValueList = "";
			String lDelimiter = "";
			for (Iterator <String> i = lDiscNameFacet1Arr.iterator(); i.hasNext();) {
				String lDiscName = (String) i.next();
//				System.out.println("debug addSchematronRuleDisciplineFacets  DISALLOWED lDiscFacet:" + lDiscName);
				lValueList += lDelimiter + "'" + lDiscName + "'";
				lDelimiter = ", ";
			}
			lAssert.assertStmt += lValueList;	
			lAssert.assertMsg += lValueList;
			lAssert.assertStmt += ") else true()";	
			lAssert.assertMsg +=  ".";
		}

		if (lNumDiscNames > lDiscNameFacet2Arr.size()) {
			AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
			lRule.assertArr.add(lAssert);
			lAssert.assertStmt = "if (pds:discipline_name and pds:facet2) then pds:discipline_name = (";	
			lAssert.assertMsg =  "Facet2 is allowed only when pds:discipline_name is one of the following ";
			String lValueList = "";
			String lDelimiter = "";
			for (Iterator <String> i = lDiscNameFacet2Arr.iterator(); i.hasNext();) {
				String lDiscName = (String) i.next();
				lValueList += lDelimiter + "'" + lDiscName + "'";
				lDelimiter = ", ";
			}
			lAssert.assertStmt += lValueList;	
			lAssert.assertMsg += lValueList;
			lAssert.assertStmt += ") else true()";	
			lAssert.assertMsg +=  ".";
		}
		

		// handle the odd cases
		
/*		// did not work
		AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
		lAssert.assertStmt = "name() = 'pds:subfacet1'";
		lAssert.assertMsg =  "pds:subfacet1 should not be used. No values have been provided.";
		lRule.assertArr.add(lAssert);
		// did not work
		lAssert = new AssertDefn2 (lAttrId);
		lAssert.assertStmt = "name() = 'pds:subfacet2'";
		lAssert.assertMsg =  "pds:subfacet2 should not be used. No values have been provided.";
		lRule.assertArr.add(lAssert); */
		
		/*
		AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
		lAssert.assertStmt = "if (pds:subfacet1) then pds:facet1 else true()";
		lAssert.assertMsg =  "If the attribute pds:subfacet1 exists then pds:facet1 must also exist.";
		lRule.assertArr.add(lAssert);
		
		lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
		lAssert = new AssertDefn2 (lAttrId);
		lAssert.assertStmt = "if (pds:subfacet2) then pds:facet2 else true()";
		lAssert.assertMsg =  "If the attribute pds:subfacet2 exists then pds:facet2 must also exist.";
		lRule.assertArr.add(lAssert);
		*/
		
		// set up the Primary Result Summary rule
		lRuleId = "pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1";
		lRule = InfoModel.schematronRuleMap.get(lRuleId);
		lRule = new RuleDefn(lRuleId);
		InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = lRuleId;
		lRule.attrTitle = "subfacet1";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Science_Facets";		
		lRule.classNameSpaceNC = "pds";	
		lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
		AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
		lRule.assertArr.add(lAssert);
		lAssert.assertStmt = "name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1'";	
		lAssert.assertMsg =  "pds:subfacet1 should not be used. No values have been provided.";
		
		lRuleId = "pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2";
		lRule = InfoModel.schematronRuleMap.get(lRuleId);
		lRule = new RuleDefn(lRuleId);
		InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
		lRule.xpath = lRuleId;
		lRule.attrTitle = "subfacet2";		
		lRule.attrNameSpaceNC = "pds";		
		lRule.classTitle = "Science_Facets";		
		lRule.classNameSpaceNC = "pds";	
		lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
		lAssert = new AssertDefn2 (lAttrId);
		lRule.assertArr.add(lAssert);
		lAssert.assertStmt = "name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2'";	
		lAssert.assertMsg =  "pds:subfacet2 should not be used. No values have been provided.";		
	}	
	
//	add the enumerated unit schematron rules
	public void addSchematronRuleUnits (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap, PrintWriter prSchematron) {
//		System.out.println("\naddSchematronRuleUnits");		
		
		// add attribute unit values assertions
		ArrayList <PDSObjDefn> lSortClassArr = new ArrayList <PDSObjDefn> (lMasterMOFClassMap.values());
		for (Iterator <PDSObjDefn> i = lSortClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass == null) continue;
			if (! ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) && lSchemaFileDefn.stewardArr.contains(lClass.steward))) continue;
			if (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous) continue;
			if (lClass.ownedAttrAssocAssertArr == null || lClass.ownedAttrAssocAssertArr.isEmpty()) continue;
			

			for (Iterator <AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.unit_of_measure_type == null || lAttr.unit_of_measure_type.indexOf("TBD") == 0) continue;
				
//				System.out.println("addSchematronRuleUnits - lAttr.unit_of_measure_type:" + lAttr.unit_of_measure_type);
				String lUnitsValueString = lAttr.getUnits(true);
				if (lUnitsValueString == null) continue;					
				String lRuleId = lClass.nameSpaceId + lClass.title + "/" + lClass.nameSpaceId + lAttr.title;
				RuleDefn lRule = InfoModel.schematronRuleMap.get(lRuleId);
				if (lRule == null) {
					lRule = new RuleDefn(lRuleId);		
					InfoModel.schematronRuleMap.put(lRule.identifier, lRule);
					lRule.xpath = lRuleId;
					lRule.attrTitle = lAttr.title;		
					lRule.attrNameSpaceNC = lAttr.attrNameSpaceIdNC;		
					lRule.classTitle = lClass.title;		
					lRule.classNameSpaceNC = lClass.nameSpaceIdNC;	
				}
				String assertMsgPre = " must be equal to one of the following values "; 
				String lAttrId = lRule.attrNameSpaceNC + ":" + lRule.attrTitle;
				if (foundAssertStmt (lAttrId, lRule.assertArr)) continue;
				AssertDefn2 lAssert = new AssertDefn2 (lAttrId);
//				lAssert.assertStmt = lRule.attrNameSpaceNC + ":" + "@unit" + " = (";	
				lAssert.assertStmt = "@unit" + " = (";	
				lAssert.assertMsg =  "The attribute " + "@unit" + assertMsgPre;
				lRule.assertArr.add(lAssert);
				lAssert.assertStmt += lUnitsValueString + ")";	
				lAssert.assertMsg += lUnitsValueString + ".";
			}
		} 
	}		
	
//	check if assert statement already exists
	public boolean foundAssertStmt (String lAttrId, ArrayList <AssertDefn2> lAssertArr) {
		for (Iterator <AssertDefn2> i = lAssertArr.iterator(); i.hasNext();) {
			AssertDefn2 lAssert = (AssertDefn2) i.next();	
			if (lAssert.identifier.compareTo(lAttrId) == 0) {
				return true;
			}
		}
		return false;
	}
	
//	write the customized class based schematron rules
	public void writeSchematronRuleClasses (SchemaFileDefn lSchemaFileDefn, ArrayList<RuleDefn> lRuleArr, PrintWriter prSchematron) {
//		System.out.println("\ndebug writeSchematronRuleClasses");

		// write class based assertions
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
			PDSObjDefn lClass = InfoModel.masterMOFClassTitleMap.get(lRule.classTitle);
			if (lClass == null) {
				continue;
			} else if (! (lRule.alwaysInclude || (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0 && lSchemaFileDefn.stewardArr.contains(lClass.steward)))) {
				continue;
			}

			prSchematron.println("  <sch:pattern>");
			prSchematron.println("    <sch:rule context=\"" + lRule.xpath + "\"" + lRule.role + ">");
			
			for (Iterator <String> j = lRule.letAssignArr.iterator(); j.hasNext();) {
				String lLetAssign = (String) j.next();	
				prSchematron.println("      <sch:let " + lLetAssign + "/>");						
			}
			for (Iterator <AssertDefn2> j = lRule.assertArr.iterator(); j.hasNext();) {
				AssertDefn2 lAssert = (AssertDefn2) j.next();	
				if (lAssert.assertType.compareTo("RAW") == 0) {	
					if (lAssert.assertMsg.indexOf("TBD") == 0) {
						prSchematron.println("      <sch:assert test=\"" + lAssert.assertStmt + "\"/>");						
					} else {
						prSchematron.println("      <sch:assert test=\"" + lAssert.assertStmt + "\">");
						prSchematron.println("        " + lAssert.assertMsg + "</sch:assert>");
					}
				} else if (lAssert.assertType.compareTo("EVERY") == 0) {
					String lDel = "";
					prSchematron.print("      <sch:assert test=\"" + "every $ref in (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + ") satisfies $ref = (");
					String lTestValueString = "";
					for (Iterator<String> k = lAssert.testValArr.iterator(); k.hasNext();) {	
						lTestValueString += lDel + "'" + (String) k.next() + "'";
						lDel = ", ";
					}
					prSchematron.print(lTestValueString);
					prSchematron.println(")\">");

					// print message
					prSchematron.println("        The attribute " + lRule.attrTitle + lAssert.assertMsg + lTestValueString + ".</sch:assert>");
				} else if (lAssert.assertType.compareTo("IF") == 0) {
					String lDel = "";
					prSchematron.print("      <sch:assert test=\"" + "if (" + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + ") then " + lRule.attrNameSpaceNC + ":" + lRule.attrTitle + " = (");
					String lTestValueString = "";
					for (Iterator<String> k = lAssert.testValArr.iterator(); k.hasNext();) {	
						lTestValueString += lDel + "'" + (String) k.next() + "'";
						lDel = ", ";
					}
					prSchematron.print(lTestValueString);
					prSchematron.println(") else true ()\">");

					// print message
					prSchematron.println("        The attribute " + lRule.attrTitle + lAssert.assertMsg + lTestValueString + ".</sch:assert>");					
				}
			}
			prSchematron.println("    </sch:rule>");
			prSchematron.println("  </sch:pattern>");
		} 
	}	
	
//	write the schematron file header
	public void printSchematronFileHdr (SchemaFileDefn lSchemaFileDefn, PrintWriter prSchematron) {
		prSchematron.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");					
		prSchematron.println("  <!-- PDS4 Schematron" + " for Name Space Id:" + lSchemaFileDefn.nameSpaceIdNC + "  Version:" + lSchemaFileDefn.ont_version_id  + " - " + DMDocument.masterTodaysDate + " -->");
		prSchematron.println("  <!-- Generated from the PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - System Build 4a -->");
		prSchematron.println("  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->");
		prSchematron.println("<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">");
		prSchematron.println("");		   
		prSchematron.println("  <sch:title>Schematron using XPath 2.0</sch:title>");
		prSchematron.println("");
//		prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "\" prefix=\"" + lSchemaFileDefn.nameSpaceIdNC + "\"/>");
		prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/" + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\" prefix=\"" + lSchemaFileDefn.nameSpaceIdNC + "\"/>");
	}

//	write the schematron file header
	public void printSchematronFileCmt (PrintWriter prSchematron) {
		prSchematron.println("");
		prSchematron.println("		   <!-- ================================================ -->");
		prSchematron.println("		   <!-- NOTE:  There are two types of schematron rules.  -->");
		prSchematron.println("		   <!--        One type includes rules written for       -->");
		prSchematron.println("		   <!--        specific situations. The other type are   -->");
		prSchematron.println("		   <!--        generated to validate enumerated value    -->");
		prSchematron.println("		   <!--        lists. These two types of rules have been -->");
		prSchematron.println("		   <!--        merged together in the rules below.       -->");
		prSchematron.println("		   <!-- ================================================ -->");
	}
	
//		write the schematron file footer
	public void printSchematronFileFtr (PrintWriter prSchematron) {			
		prSchematron.println("</sch:schema>");
	}
}
