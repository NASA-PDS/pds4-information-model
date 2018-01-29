package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class WriteSchematron extends Object {	
	PrintWriter prSchematron;

	public WriteSchematron () {
		return;
	}
	
//	write all Schematron files
	public void writeSchematronFile (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap) throws java.io.IOException {				
		String lFileName = lSchemaFileDefn.relativeFileSpecSchematron;
		prSchematron = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));		
		writeSchematronRule(lSchemaFileDefn, lMasterMOFClassMap, prSchematron);
		prSchematron.close();	
		return;
	}
		
//	write the schematron rules
	public void writeSchematronRule (SchemaFileDefn lSchemaFileDefn, TreeMap <String, PDSObjDefn> lMasterMOFClassMap, PrintWriter prSchematron) {
		// write schematron file header
		printSchematronFileHdr (lSchemaFileDefn, prSchematron); 
		printSchematronFileCmt (prSchematron);

		// select out the rules for this namespace
		ArrayList <RuleDefn> lSelectRuleArr = new ArrayList <RuleDefn> ();
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleIdMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
						
			if (lSchemaFileDefn.isMaster) {
				if (lRule.isMissionOnly) continue;
				if (! (lRule.alwaysInclude
						|| (lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
								&& lSchemaFileDefn.stewardArr.contains(lRule.classSteward)))) continue;
			} else if (lSchemaFileDefn.isDiscipline) {
				if (lRule.isMissionOnly) continue;
				if (! ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
								&& lSchemaFileDefn.stewardArr.contains(lRule.classSteward)))) continue;
			} else if (lSchemaFileDefn.isMission) {
				if (lRule.isMissionOnly) continue;
				if (! ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
								&& lSchemaFileDefn.stewardArr.contains(lRule.classSteward)))) continue;
			} else if (lSchemaFileDefn.isLDD) {
				// write an LDD schemtron
				if (! (lRule.isMissionOnly && DMDocument.LDDToolMissionGovernanceFlag)) continue;
				if (!((lSchemaFileDefn.nameSpaceIdNC.compareTo(lRule.classNameSpaceNC) == 0
						&& lSchemaFileDefn.stewardArr.contains(lRule.classSteward))
						|| (lRule.classTitle.compareTo(DMDocument.LDDToolSingletonClassTitle) == 0 ))) continue;
			} else {
				System.out.println(">>warning - Write Schematron - Invalid governance in SchemaFileDefn  - lSchemaFileDefn.identifier:" + lSchemaFileDefn.identifier); 			
			}
//			if (lRule.classNameSpaceNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) == 0) lSelectRuleArr.add(lRule);
			lSelectRuleArr.add(lRule);
		}		
		writeSchematronRuleClasses (lSchemaFileDefn, lSelectRuleArr, prSchematron);
		
		// write schematron file footer
		printSchematronFileFtr (prSchematron); 
	}
		
//	find offending rule
	public void findOffendingRule (String lTitle, ArrayList <RuleDefn> lRuleArr) {
		System.out.println("\ndebug findOffendingRule - " + lTitle);
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();	
			if (lRule.classTitle.indexOf("Discipline_Facets") > -1 || lRule.identifier.indexOf("Discipline_Facets") > -1) {
				System.out.println("debug findOffendingRule - lRule.identifier:" + lRule.identifier);
			}
		}
		return;
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
	
//	write the schematron rules
	public void writeSchematronRuleClasses (SchemaFileDefn lSchemaFileDefn, ArrayList<RuleDefn> lRuleArr, PrintWriter prSchematron) {
		// write class based assertions
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();	
						
			prSchematron.println("  <sch:pattern>");
			
			// write pattern level LETs
			for (Iterator <String> j = lRule.letAssignPatternArr.iterator(); j.hasNext();) {
				String lLetAssignPattern = (String) j.next();	
				prSchematron.println("    <sch:let " + lLetAssignPattern + "/>");						
			}
			
			String lRole = "";
			if (lRule.roleId.compareTo(" role=\"warning\"") == 0) lRole = " role=\"warning\"";
			prSchematron.println("    <sch:rule context=\"" + lRule.xpath + "\"" + lRole + ">");

			// write rule level LETs
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
		prSchematron.println("  <!-- Generated from the PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - System Build " + DMDocument.XMLSchemaLabelBuildNum + " -->");
		prSchematron.println("  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->");
		prSchematron.println("<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">");
		prSchematron.println("");		   
		prSchematron.println("  <sch:title>Schematron using XPath 2.0</sch:title>");
		prSchematron.println("");
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0) {
			// namespaces required: pds - latest version
			prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" prefix=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "\"/>");
		} else {
			// namespaces required: pds
			prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" prefix=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "\"/>");
			// namespaces required: ldd
			String governanceDirectory = "";
			if (DMDocument.LDDToolMissionGovernanceFlag) governanceDirectory = DMDocument.governanceLevel.toLowerCase() +  "/";
			prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/" + governanceDirectory + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\" prefix=\"" + lSchemaFileDefn.nameSpaceIdNC + "\"/>");
			// namespaces required: all other LDD discipline levels referenced; no mission level allowed
			for (Iterator<String> i = DMDocument.LDDImportNameSpaceIdNCArr.iterator(); i.hasNext();) {
				String lNameSpaceIdNC = (String) i.next();
				String lVersionNSId = DMDocument.LDDToolSchemaVersionNSMap.get(lNameSpaceIdNC);
				if (lVersionNSId == null) lVersionNSId = DMDocument.LDDToolSchemaVersionNSMap.get(DMDocument.masterNameSpaceIdNCLC);
				prSchematron.println("  <sch:ns uri=\"http://pds.nasa.gov/pds4/" + lNameSpaceIdNC + "/v" + lVersionNSId + "\" prefix=\"" + lNameSpaceIdNC + "\"/>");
			}
		}
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
