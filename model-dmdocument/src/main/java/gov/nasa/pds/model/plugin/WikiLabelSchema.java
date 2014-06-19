package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class WikiLabelSchema extends Object {
	PrintWriter prWikiDD;	
	String Stmt;	
		
	public WikiLabelSchema () { 			
		return;
	}

	public void writeWikiLabel (boolean expand, String ddType, PDSObjDefn lClass, String todaysDate) throws java.io.IOException  {
		boolean isUnabridged = false;
		if (ddType.compareTo("unabridged") == 0 || ddType.compareTo("unabridged2") == 0) {
			isUnabridged = true;
		}
		String classTitle = lClass.title;
		if (ddType.compareTo("unabridged") == 0) {
//			prWikiDD = new PrintWriter(new FileWriter("SchemaWikiDD/WS_" + classTitle + "_" + InfoModel.lab_version_id + ".txt", false));
			prWikiDD = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDD/WS_" + classTitle + "_" + InfoModel.lab_version_id + ".txt", false));
		} else if (ddType.compareTo("unabridged2") == 0) {
//			prWikiDD = new PrintWriter(new FileWriter("SchemaWikiDDA3/WS_" + classTitle + "_" + InfoModel.lab_version_id + ".txt", false));				
			prWikiDD = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/WS_" + classTitle + "_" + InfoModel.lab_version_id + ".txt", false));				
		}

		//	Write the root statements
		String anchorLink = lClass.title + "_" + lClass.steward;
		Stmt = "{anchor:" + anchorLink + "}";						
		prWikiDD.println(Stmt);

		//	get the classes, starting with the first		
		getClass(isUnabridged, expand, true, 0, 0, 1, 1, null, lClass, new ArrayList<PDSObjDefn>());

		prWikiDD.println("----");
		prWikiDD.close();
	}

	public void getClass (boolean isUnabridged, boolean expand, boolean isFirst, int ilevel, int ident, int classCardMin,  int classCardMax, String lFirstClass, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitedClassList) {
		if (visitedClassList.contains(lClass)) {
			return;
		} else {
			visitedClassList.add(lClass);
		}

		if (lFirstClass == null) lFirstClass = lClass.title;
		
		boolean openflag = false;

		String cctitle = lClass.title;
		String objBegSpaces = "#*";
		int curLevel = ilevel + 1;
		int curDent = ident;
		for (int j = 0; j < curDent; j++) {
			objBegSpaces += "*";
		}
		
		ArrayList <AttrDefn> lAttrArrOrdered = InfoModel.getSortedAlphaClassAssocAttrArr (lClass);
		
		if (expand) {
			if (! DMDocument.omitClass.contains(cctitle)) {
				curDent += 1;
//				System.out.println("\ndebug lClass.title:" + lClass.title);
				String poccurs = "";
				if (classCardMin == classCardMax) {
					poccurs = " occurs " + classCardMin + " times";
				} else {
					String pClassCardMax = "*";
					if (classCardMax != 9999999) {
						Integer classCardMaxI = new Integer(classCardMax);
						pClassCardMax = classCardMaxI.toString();
					}
					poccurs = " - occurs " + classCardMin + " to " + pClassCardMax + " times";					
				}

				String pBaseType = "";
//				if (lClass.baseClassName.compareTo("none") != 0 )
				if (lClass.baseClassName.indexOf("TBD") != 0) {
					pBaseType = " - Base_Class:*" + lClass.baseClassName + "*";
				}
				if (isFirst) {
					Stmt = objBegSpaces + " h4. " + "*" + cctitle + "* " + pBaseType;
				} else { 
					Stmt = objBegSpaces + " h5. " + "*" + cctitle + "* " + poccurs + pBaseType;
				}
				prWikiDD.println(Stmt);

				Stmt = "description: *" + lClass.description + "*";
				prWikiDD.println(Stmt);
				
				String pRole = "Abstract";
				if (lClass.role.compareTo("concrete") == 0) {
					pRole = "Concrete";
				}
				Stmt = "role: *" + pRole + "*";
				prWikiDD.println(Stmt);
				
				openflag = true;
				getStmt(isUnabridged, objBegSpaces, curLevel, ident, lFirstClass, lClass, lAttrArrOrdered);
			}
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
				getClass (isUnabridged, expand, false, curLevel, curDent, lAssocClass.cardMin, lAssocClass.cardMax, lFirstClass, lAssocClass.assocClass, visitedClassListCopy);
			}
		}
		if (openflag) {
			Stmt = objBegSpaces + " " + "*End* " + lClass.title + "";
			prWikiDD.println(Stmt);
		}
	}
	
// the following is here only for historical reference, the sort is buggy - how did the inherited attributes sort correctly
/*	public ArrayList <AttrDefn> getAttrOrdered (PDSObjDefn lClass) {
		// get attributes and order
//		System.out.println("\ndebug getAttrOrdered lClass.identifier:" + lClass.identifier);
		ArrayList<AttrDefn> lAttrArr = new ArrayList<AttrDefn>();
		lAttrArr.addAll(lClass.ownedAttribute);
		lAttrArr.addAll(lClass.inheritedAttribute);
		//	get associated subclasses
		TreeMap <String, AttrDefn> lAttrMap = new TreeMap <String, AttrDefn> ();
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("debug lAttr.identifier:" + lAttr.identifier);
			String lOrder = getSeq(99);
			Integer lOrderInt = InfoModel.masterAttrSortOrderMap.get(lAttr.title);
			if (lOrderInt != null) {
				lOrder = getSeq(lOrderInt);
			}
			String lId = lOrder + ":" + lAttr.title;
			lAttrMap.put(lId, lAttr);
		}
		ArrayList <AttrDefn> lAttrArrOrdered = new ArrayList <AttrDefn> (lAttrMap.values());
		return lAttrArrOrdered;
	}*/
	
// Here for historical reasons - see attribute search above.	
/*	public ArrayList <AssocClassDefn> getAssocClassOrdered (PDSObjDefn lClass) {
		// get associated classes
//		System.out.println("\ndebug lClass.identifier:" + lClass.identifier);
		
		ArrayList <AttrDefn> lAssocArr  = new ArrayList <AttrDefn> ();
		lAssocArr.addAll(lClass.ownedAssociation);
		lAssocArr.addAll(lClass.inheritedAssociation);

		//	get associated subclasses
		TreeMap <String, AssocClassDefn> lAssocClassMap = new TreeMap <String, AssocClassDefn> ();
		for (Iterator<AttrDefn> i = lAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("debug lAttr.identifier:" + lAttr.identifier);
			if (lAttr.valArr.isEmpty()) {
				continue;
			}
			for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
				String lTitle = (String) j.next();
				PDSObjDefn lClassMember = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lTitle);
				if (lClassMember == null) {
					continue;
				}
//				System.out.println("debug lClassMember.identifier:" + lClassMember.identifier);
				String lOrder = getSeq(99);
				Integer lOrderInt = InfoModel.masterClassSortOrderMap.get(lTitle);
				if (lOrderInt != null) {
					lOrder = getSeq(lOrderInt);
				}
				String lId = lOrder + ":" + lClassMember.title;
				AssocClassDefn lAssocClass = new AssocClassDefn (lId, lAttr.cardMinI, lAttr.cardMaxI, lClassMember);
				lAssocClassMap.put(lAssocClass.identifier, lAssocClass);
			}
		}
		ArrayList <AssocClassDefn> lAssocClassArr = new ArrayList <AssocClassDefn> (lAssocClassMap.values());
		return lAssocClassArr;
	} */
	
/*	public String getSeq (Integer iseq) {	
		String seq = iseq.toString();
		if (seq.length() < 2) {
			seq = "0" + seq;
		} else if (seq.length() > 2) {
			seq = "99";
		}
		return (seq);
	} */
	
	public void getStmt (Boolean isUnabridged, String objBegSpaces, int ilevel, int ident, String lFirstClass, PDSObjDefn lClass, ArrayList<AttrDefn> attrArr) {
		boolean isProductClass = false;
		for (Iterator<AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();

			isProductClass = false;
			if (lAttr.title.compareTo("product_class") == 0) isProductClass = true;
			int spaceCnt = 20 - lAttr.title.length();
			if (spaceCnt < 0) { spaceCnt = 0; }
			String pOptional = "";
			if (lAttr.cardMin.compareTo("0") == 0) {
				pOptional = " ^Optional^ ";
			}
			String del = "";
//			String pValue = "_value_";
			String pValue = "";
			if (! isProductClass) {
				if (! lAttr.valArr.isEmpty()) {
					for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
						String lval = (String) j.next();
						pValue += del + lval;
						del = ", ";
					}
				}
			} else {
				pValue = lFirstClass;
			}

			if (pValue.compareTo("") != 0) pValue = "value: *" + pValue + "*  ";
			
			String anchorLink = lAttr.title + "_" + lAttr.steward + "_" + lAttr.className + "_" + lAttr.classSteward;
			if (! isUnabridged) anchorLink = lAttr.title + "_" + lAttr.steward;
			Stmt = "attribute: [*" + lAttr.title + "*|#" + anchorLink + "] " + pValue + pOptional;
			prWikiDD.println(Stmt);
		}
	}
}
