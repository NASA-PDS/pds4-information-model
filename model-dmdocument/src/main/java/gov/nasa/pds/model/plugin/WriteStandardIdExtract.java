package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class WriteStandardIdExtract extends Object {
	String PVLStmt;
	PrintWriter prPVL;
	TreeMap <String, String> lReportStatementMap = new TreeMap <String, String> ();
	int lineNum = 100000;
	Integer lineNumI;
//	String del = "\", \"", delBeg = "\"", delEnd = "\"";
	String del = "|", delBeg = "", delEnd = "";
	
	public WriteStandardIdExtract () {
		return;
	}

	public void writeExtractFile ()  throws java.io.IOException  {
		writeExtractFileBegin ();
		writeExtractFileBody ();
		writeExtractFileEnd ();
	}
	
	public void writeExtractFileBegin ()  throws java.io.IOException  {
		// Write the files consisting of individual classes
		String lFileName = DMDocument.outputDirPath + "Extract/" + "StandardId" + "_" + InfoModel.lab_version_id + ".txt";
	    prPVL = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));	

	    PVLStmt = delBeg + "Standard Id" + del + "Description" + del + "Class" + del + "Attribute" + delEnd;
		prPVL.println(PVLStmt);
	}

	public void writeExtractFileBody () {
		// Write the files consisting of individual classes
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.isSchema1Class) {
//				System.out.println("\ndebug writeExtractFileBody lClass.identifier:" + lClass.identifier);
				getExtractFromClass (lClass, new ArrayList<PDSObjDefn> (), prPVL);						
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

	public void getExtractFromClass (PDSObjDefn lClass, ArrayList<PDSObjDefn> visitedClassList, PrintWriter prPVL) {
//		System.out.println("debug getExtractFromClass lClass.identifier:" + lClass.identifier);
		if (visitedClassList.contains(lClass)) {
			return;
		} else {
			visitedClassList.add(lClass);
		}

		getExtractFromAttr(lClass, prPVL);

		// get associated classes
		ArrayList <AssocClassDefn> lAssocClassArr = InfoModel.getSortedAlphaClassAssocClassArr (lClass);	
		if (lAssocClassArr != null) {
			for (Iterator<AssocClassDefn> j = lAssocClassArr.iterator(); j.hasNext();) {
				AssocClassDefn lAssocClass = (AssocClassDefn) j.next();
				ArrayList <PDSObjDefn> visitedClassListCopy = new ArrayList <PDSObjDefn> ();
				for (Iterator <PDSObjDefn> k = visitedClassList.iterator(); k.hasNext();) {
					visitedClassListCopy.add((PDSObjDefn) k.next());
				}
				getExtractFromClass (lAssocClass.assocClass, visitedClassListCopy, prPVL);
			}
		}
	}
	
	public void getExtractFromAttr (PDSObjDefn lClass, PrintWriter prPVL) {
		if (lClass.steward.compareTo("ops") == 0) return;
		for (Iterator<AttrDefn> i =  lClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
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
