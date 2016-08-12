package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class WriteSchematronRulesPins extends Object {	
	PrintWriter prSchematron;
	TreeMap <String, AssertDefn2> assertIdMap;
	TreeMap <String, String> assignIdMap;

	public WriteSchematronRulesPins () {
		assertIdMap = new TreeMap <String, AssertDefn2> ();
		assignIdMap = new TreeMap <String, String> ();	
		return;
	}
	
//	write all Schematron files
	public void writeSchematronFile () throws java.io.IOException {
		String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelRulePins;
		prSchematron = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		printSchematronFileHdr (prSchematron); 
		writeSchematronRules(prSchematron);
		writeSchematronAssert(prSchematron);
		printSchematronFileFtr (prSchematron);
		prSchematron.close();	
		return;
	}
		
//	write the schematron rules
	public void writeSchematronRules (PrintWriter prSchematron) {
		// write rules
		ArrayList <RuleDefn> lRuleArr = new ArrayList <RuleDefn> (InfoModel.schematronRuleIdMap.values());
		for (Iterator <RuleDefn> i = lRuleArr.iterator(); i.hasNext();) {
			RuleDefn lRule = (RuleDefn) i.next();
//			System.out.println("\ndebug writeSchematronRuleClasses - lRule.identifier:" + lRule.identifier);
//			String lRuleIdPlus = lRule.identifier + "." + InfoModel.getNextUId ();
//			String lRuleIdPlus = DMDocument.registrationAuthorityIdentifierValue + "." + lRule.identifier + "." + InfoModel.getNextUId ();
//			String lRuleIdPlusEsc = escapeProtegeString2(lRuleIdPlus);
			String lRDFIdentifierEsc = escapeProtegeString2(lRule.rdfIdentifier);
			prSchematron.println("([" + lRDFIdentifierEsc + "] of Schematron_Rule");
			prSchematron.println("  (identifier \"" + escapeProtegeString2(lRule.identifier) + "\")");
			prSchematron.println("  (type \"" + lRule.type + "\")");
			prSchematron.println("  (roleId \"" + lRule.roleId + "\")");
			prSchematron.println("  (xpath \"" + escapeProtegeString2(lRule.xpath) + "\")");

			prSchematron.println("  (attrTitle \"" + lRule.attrTitle + "\")");
			prSchematron.println("  (attrNameSpaceNC \"" + lRule.attrNameSpaceNC + "\")");
			prSchematron.println("  (classTitle \"" + lRule.classTitle + "\")");
			prSchematron.println("  (classNameSpaceNC \"" + lRule.classNameSpaceNC + "\")");
			prSchematron.println("  (classSteward \"" + lRule.classSteward + "\")");

			prSchematron.println("  (alwaysInclude \"" + lRule.alwaysInclude + "\")");
			prSchematron.println("  (isMissionOnly \"" + lRule.isMissionOnly + "\")");
			
			// write let statements;
			prSchematron.print("  (schematronAssign ");
			String lSpaces = "";
			for (Iterator <String> j = lRule.letAssignArr.iterator(); j.hasNext();) {
				String lAssign = (String) j.next();
				prSchematron.println(lSpaces + "\"" + escapeProtegeString2(lAssign) + "\"");
				lSpaces = "      ";
			}
			prSchematron.println("   )");
			
			// write assert statements;
			prSchematron.print("  (has_Schematron_Assert ");
			int lNum = 100;
			lSpaces = "";
			for (Iterator <AssertDefn2> j = lRule.assertArr.iterator(); j.hasNext();) {
				AssertDefn2 lAssert = (AssertDefn2) j.next();
				lNum++;
				Integer lNumI = new Integer (lNum); 
				String lNumS = lNumI.toString();
//				String lAssertId = lRuleIdPlus + "." + lNumS;
				String lAssertId = lRule.rdfIdentifier + "." + lNumS;
				String lAssertIdEsc = escapeProtegeString2(lAssertId);

				assertIdMap.put(lAssertIdEsc, lAssert);
				prSchematron.println(lSpaces + "[" + lAssertIdEsc + "]");
				lSpaces = "      ";
			}
			prSchematron.println("   )");
			prSchematron.println(")");
			prSchematron.println(" ");
		}
	}
		
//		write the schematron assert statements
		public void writeSchematronAssert (PrintWriter prSchematron) {
			// get asserts
			Set <String> set1 = assertIdMap.keySet();
			Iterator <String> iter1 = set1.iterator();
			while(iter1.hasNext()) {
				String lAssertId = (String) iter1.next();
				AssertDefn2 lAssert = assertIdMap.get(lAssertId);

				prSchematron.println("([" + lAssertId + "] of Schematron_Assert");
//				prSchematron.println("  (identifier \"" + lAssertId + "\")");
				prSchematron.println("  (identifier \"" + escapeProtegeString2(lAssert.identifier) + "\")");
				prSchematron.println("  (attrTitle \"" + lAssert.attrTitle + "\")");
				prSchematron.println("  (assertType \"" + lAssert.assertType + "\")");
				prSchematron.println("  (assertStmt \"" + escapeProtegeString2(lAssert.assertStmt) + "\")");
				prSchematron.println("  (assertMsg \"" + escapeProtegeString2(lAssert.assertMsg) + "\")");
				prSchematron.println("  (specMesg \"" + escapeProtegeString2(lAssert.specMesg) + "\")");
				
				// write value statements;
				prSchematron.print("  (testValArr ");
				String lSpaces = "";
				for (Iterator <String> j = lAssert.testValArr.iterator(); j.hasNext();) {
					String lValue = (String) j.next();
					prSchematron.println(lSpaces + "\"" + escapeProtegeString2(lValue) + "\"");
					lSpaces = "      ";
				}
				prSchematron.println("   )");
				prSchematron.println(")");
				prSchematron.println(" ");
			}
		}		
			
//	write the schematron file header
	public void printSchematronFileHdr (PrintWriter prSchematron) {					
		prSchematron.println("	; Tue Jan 26 07:52:47 PST 2010");
		prSchematron.println("	; ");
		prSchematron.println("	;+ (version \"3.3.1\")");
		prSchematron.println("	;+ (build \"Build 430\")");
		prSchematron.println("");
	}

//		write the schematron file footer
	public void printSchematronFileFtr (PrintWriter prSchematron) {			
		prSchematron.println("");
	}
	
// 444	
	
	/**
	* escape certain characters for protege files
	*/
	static String escapeProtegeString2 (String s1) {
		String ls1 = s1;
		ls1 = InfoModel.replaceString (ls1, "\\", "\\\\"); // order is important
		ls1 = InfoModel.replaceString (ls1, "\"", "\\\"");		
		return ls1;
	}
	
	static String escapeProtegeString2xxx (String s1) {
		String ls1 = s1;
		ls1 = InfoModel.replaceString (ls1, "[", "%5B");
		ls1 = InfoModel.replaceString (ls1, "]", "%5D");
		ls1 = InfoModel.replaceString (ls1, "(", "%28");
		ls1 = InfoModel.replaceString (ls1, ")", "%29");
		ls1 = InfoModel.replaceString (ls1, "/", "%2F");
		ls1 = InfoModel.replaceString (ls1, "+", "%2B");		
		ls1 = InfoModel.replaceString (ls1, "|", "%7C");		
		ls1 = InfoModel.replaceString (ls1, "{", "%7B");		
		ls1 = InfoModel.replaceString (ls1, "}", "%7D");		
		ls1 = InfoModel.replaceString (ls1, "\"", "%42");		
		ls1 = InfoModel.replaceString (ls1, "'", "%47");		
		ls1 = InfoModel.replaceString (ls1, "\\", "%5C");		
		return ls1;
	}
	
}
