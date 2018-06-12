package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class WriteDOMStandardIdExtract extends Object {
	String PVLStmt;
	PrintWriter prPVL;
	TreeMap <String, String> lReportStatementMap = new TreeMap <String, String> ();
	int lineNum = 100000;
	Integer lineNumI;
//	String del = "\", \"", delBeg = "\"", delEnd = "\"";
	String del = "|", delBeg = "", delEnd = "";
	
	public WriteDOMStandardIdExtract () {
		return;
	}

	public void writeExtractFile ()  throws java.io.IOException  {
		writeExtractFileBegin ();
		writeExtractFileBody ();
		writeExtractFileEnd ();
	}
	
	public void writeExtractFileBegin ()  throws java.io.IOException  {
		// Write the files consisting of individual classes
		String lFileName = DMDocument.outputDirPath + "Extract/" + "StandardId" + "_" + DOMInfoModel.lab_version_id + "_DOM.txt";
	    prPVL = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));	

	    PVLStmt = delBeg + "Standard Id" + del + "Description" + del + "Class" + del + "Attribute" + delEnd;
		prPVL.println(PVLStmt);
	}

	public void writeExtractFileBody () {
		// Write the files consisting of individual classes
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isSchema1Class) {
//				System.out.println("\ndebug writeExtractFileBody lClass.identifier:" + lClass.identifier);
				getExtractFromClass (lClass, new ArrayList<DOMClass> (), prPVL);						
			}
		}
	}

	public void writeExtractFileEnd () {
		// Write the files consisting of individual classes
		ArrayList <String> lReportStatementMapTreeArr = new ArrayList <String> (lReportStatementMap.values());
		for (Iterator<String> j = lReportStatementMapTreeArr.iterator(); j.hasNext();) {
			String lReportStatement = (String) j.next();
			prPVL.println(lReportStatement);
		}
		prPVL.close();
	}

	public void getExtractFromClass (DOMClass lClass, ArrayList<DOMClass> visitedClassList, PrintWriter prPVL) {
//		System.out.println("debug getExtractFromClass lClass.identifier:" + lClass.identifier);
		if (visitedClassList.contains(lClass)) {
			return;
		} else {
			visitedClassList.add(lClass);
		}

		getExtractFromAttr(lClass, prPVL);

		// get associated classes
		ArrayList <DOMAssocClassDefn> lAssocClassArr = DOMInfoModel.getSortedAlphaClassAssocClassArr (lClass);	
		if (lAssocClassArr != null) {
			for (Iterator<DOMAssocClassDefn> j = lAssocClassArr.iterator(); j.hasNext();) {
				DOMAssocClassDefn lAssocClass = (DOMAssocClassDefn) j.next();
				ArrayList <DOMClass> visitedClassListCopy = new ArrayList <DOMClass> ();
				for (Iterator <DOMClass> k = visitedClassList.iterator(); k.hasNext();) {
					visitedClassListCopy.add((DOMClass) k.next());
				}
				getExtractFromClass (lAssocClass.assocClass, visitedClassListCopy, prPVL);
			}
		}
	}
	
	public void getExtractFromAttr (DOMClass lClass, PrintWriter prPVL) {
		if (lClass.steward.compareTo("ops") == 0) return;
		DOMAttr lAttr = null;
		for (Iterator<DOMProp> i =  lClass.ownedAttrArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			if (lProp.isAttribute)  {
				lAttr = (DOMAttr) lProp.hasDOMObject;
			} else {
				continue;
			}
			if (lAttr.title.indexOf("standard_id") < 0) continue;
			if (! lAttr.permValueArr.isEmpty()) {
				for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
					PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
//					PVLStmt = lClass.title + " - " + lAttr.title + " - " + lPermValueDefn.value + " - " + lPermValueDefn.value_meaning;
//					PVLStmt = lPermValueDefn.value + " - " + lPermValueDefn.value_meaning + " - " + lClass.title + " - " + lAttr.title;
					PVLStmt = delBeg + lPermValueDefn.value + del + lPermValueDefn.value_meaning + del + lClass.title + del + lAttr.title + delEnd;
					lineNum++;
					lineNumI = new Integer(lineNum);
//					lReportStatementMap.put(lPermValueDefn.value + "." + lineNumI, PVLStmt);
					lReportStatementMap.put(PVLStmt, PVLStmt);
//					prPVL.println(PVLStmt);
				}
			}
		}
		return;
	}	
}
