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

class WritePVLSchema extends Object {
	String PVLStmt;
	PrintWriter prPVL;
	
	public WritePVLSchema () {			
		return;
	}

//  write PVL schemas
	public void writePVLLabel (PDSObjDefn lClass, String todaysDate) throws java.io.IOException  {		
		String classTitle = lClass.title;
	    prPVL = new PrintWriter(new FileWriter(DMDocument.masterPDSSchemaFileDefn.relativeFileSpecModelPVL + classTitle + ".txt", false));

	    PVLStmt = "#PDS4#\n";
		prPVL.println(PVLStmt);
		
	    PVLStmt = "/* ******* Label Template - " + lClass.title + "_" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - " + todaysDate + " ******* */";						
		prPVL.println(PVLStmt);
		
		//	get the classes, starting with the first		
	    writePVLClass (0, 0, 1, 1, lClass, new ArrayList<PDSObjDefn>(), prPVL);

		prPVL.close();
	}

	public void writePVLClass (int ilevel, int ident, int classCardMin,  int classCardMax, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitedClassList, PrintWriter prPVL) {
		if (visitedClassList.contains(lClass)) {
			return;
		} else {
			visitedClassList.add(lClass);
		}
		boolean openflag = false;

		String cctitle = lClass.title;
		String twoSpaces = "  ";
		String objBegSpaces = "";
		int curLevel = ilevel + 1;
		int curDent = ident;
		for (int j = 0; j < curDent; j++) {
			objBegSpaces += twoSpaces;
		}
		
		ArrayList <AttrDefn> lAttrArrOrdered = InfoModel.getSortedAlphaClassAssocAttrArr (lClass);

		if (! DMDocument.omitClass.contains(cctitle)) {
			curDent += 1;
//			System.out.println("\ndebug lClass.title:" + lClass.title);
			String poccurs = "";
			if (classCardMin == classCardMax) {
				poccurs = "  /* Occurs " + classCardMin + " Times */";
			} else {
				String pClassCardMax = "*";
				if (classCardMax != 9999999) {
					Integer classCardMaxI = new Integer(classCardMax);
					pClassCardMax = classCardMaxI.toString();
				}
				poccurs = "  /* Occurs " + classCardMin + " to " + pClassCardMax + " Times */";					
			}
			PVLStmt = "\n" + objBegSpaces + "Object" + " = " + cctitle + ";" + poccurs;
			prPVL.println(PVLStmt);
			openflag = true;
			writePVLStmt(objBegSpaces, curLevel, ident, cctitle, lAttrArrOrdered, prPVL);
		}

		// get associated classes
		ArrayList <AssocClassDefn> lAssocClassArr = InfoModel.getSortedAlphaClassAssocClassArr (lClass);	
		if (lAssocClassArr != null) {
			for (Iterator<AssocClassDefn> j = lAssocClassArr.iterator(); j.hasNext();) {
				AssocClassDefn lAssocClass = (AssocClassDefn) j.next();
				ArrayList <PDSObjDefn> visitedClassListCopy = new ArrayList <PDSObjDefn> ();
				for (Iterator <PDSObjDefn> k = visitedClassList.iterator(); k.hasNext();) {
					visitedClassListCopy.add((PDSObjDefn) k.next());
				}
			    writePVLClass (curLevel, curDent, lAssocClass.cardMin, lAssocClass.cardMax, lAssocClass.assocClass, visitedClassListCopy, prPVL);

			}
		}
		if (openflag) {
			PVLStmt = objBegSpaces + "End_Object" + " = " + cctitle + ";" + "\n";
			prPVL.println(PVLStmt);
		}
	}
	
	public void writePVLStmt (String objBegSpaces, int ilevel, int ident, String cctitle,  ArrayList<AttrDefn> attrArr, PrintWriter prPVL) {
		String stmtBegSpaces = objBegSpaces + "  ";

		for (Iterator<AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			int spaceCnt = 20 - lAttr.title.length();
			if (spaceCnt < 0) { spaceCnt = 0; }
			String blankSep = "                    ".substring(0, spaceCnt);
			String pOptional = "";
			if (lAttr.cardMin.compareTo("0") == 0) {
				pOptional = "   /* Optional */";
			}
			String pcomma = "";
			String pValue = "";
			if (lAttr.valArr.isEmpty()) {
				pValue = "${" + lAttr.title + "}";
			} else {
				String tval = (String) lAttr.valArr.get(0);
				if (tval.compareTo("") == 0) {
					pValue = "${" + lAttr.title + "}";;
				} else {
					int nval = lAttr.valArr.size();
//					System.out.println("debug  nval:" + nval);
					if (nval == 1) {
						pValue = tval;
					} else {
						pValue += "{";
						for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
							String lval = (String) j.next();
							pValue += pcomma + lval;
							pcomma = ", ";
						}
						pValue += "}";
					}
				}
			}
			PVLStmt = stmtBegSpaces + lAttr.title + blankSep + " = " + pValue + ";" + "  " + pOptional;
			prPVL.println(PVLStmt);
		}
	}	
}
