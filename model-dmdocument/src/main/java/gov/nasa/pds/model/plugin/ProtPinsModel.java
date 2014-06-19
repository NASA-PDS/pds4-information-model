package gov.nasa.pds.model.plugin;
import java.util.*;

/**
 * Creates the in memory protege Upper pins model (e.g. Discipline Facets)
 */
class ProtPinsModel extends Object {
	ProtPins protPinsInst;
	TreeMap <String, SFGroupFacetDefn> lSFGroupFacetDefnMap;
	
	public ProtPinsModel () {

		// Parse Protege Instance File - Parse InstDefn stored as follows.
		// lInst.className - e.g. DataElement
		// + lRegAuthId + "." + lInst.className + "." + lInst.title; 
		// ++ InstDefn
		InfoModel.sfDisciplineFacetDefnMap = new TreeMap <String, SFDisciplineFacetDefn> ();
		lSFGroupFacetDefnMap = new TreeMap <String, SFGroupFacetDefn> ();
		return;
	}

	/**
	 *   Parses the protege Upper pins file
	 */
	public void getProtPinsModel (String lRegAuthId, String fname) throws Throwable {

		// parse the PINS file
		protPinsInst = new ProtPins();
		protPinsInst.getProtInst("upperPins", "upperPins", fname);
		
		// iterate through the resultant instances
		ArrayList <InstDefn> lInstDefnArr = new ArrayList <InstDefn> (protPinsInst.instDict.values());
		for (Iterator <InstDefn> i = lInstDefnArr.iterator(); i.hasNext();) {
			InstDefn lInst = (InstDefn) i.next();

			if ( ! ((lInst.className.compareTo("Discipline_Facets") == 0) || (lInst.className.compareTo("Group_Facet1") == 0) || (lInst.className.compareTo("Group_Facet2") == 0))) continue;

//			System.out.println("\ndebug getProtPinsModel lInst.identifier:" + lInst.identifier);			
			HashMap <String, ArrayList<String>> lInstSlotMap = lInst.genSlotMap;

			if (lInst.className.compareTo("Discipline_Facets") == 0) {
				SFDisciplineFacetDefn lSFDisciplineFacetDefn = new SFDisciplineFacetDefn (lInst.className, lInst.title);
				InfoModel.sfDisciplineFacetDefnMap.put(lSFDisciplineFacetDefn.identifier, lSFDisciplineFacetDefn);
				ArrayList<String> lValArr = lInstSlotMap.get("has_Group_Facet1"); 
				if (lValArr != null) {
					for (Iterator <String> j = lValArr.iterator(); j.hasNext();) {
						String lVal = (String) j.next();
						String lValPlus = lVal + "." + "Group_Facet1";
						lSFDisciplineFacetDefn.groupFacet1IdArr.add(lValPlus);						
					}
				}
				lValArr = lInstSlotMap.get("has_Group_Facet2"); 
				if (lValArr != null) {
					for (Iterator <String> j = lValArr.iterator(); j.hasNext();) {
						String lVal = (String) j.next();
						String lValPlus = lVal + "." + "Group_Facet2";
						lSFDisciplineFacetDefn.groupFacet2IdArr.add(lValPlus);						
					}
				}
			} else {
				// create new group facet
				SFGroupFacetDefn lSFGroupFacetDefn = new SFGroupFacetDefn (lInst.className, lInst.title);
				lSFGroupFacetDefnMap.put(lSFGroupFacetDefn.identifier, lSFGroupFacetDefn);
				
				// get subfacet1
				ArrayList<String> lValArr = lInstSlotMap.get("subfacet1"); 
				if (lValArr != null) {
					for (Iterator <String> j = lValArr.iterator(); j.hasNext();) {
						String lVal = (String) j.next();
						lSFGroupFacetDefn.subfacetArr.add(lVal);						
					}
				}
				
				// get subfacet2
				lValArr = lInstSlotMap.get("subfacet2"); 
				if (lValArr != null) {
					for (Iterator <String> j = lValArr.iterator(); j.hasNext();) {
						String lVal = (String) j.next();
						lSFGroupFacetDefn.subfacetArr.add(lVal);						
					}
				}

			}
		}
		
		// iterate through the discipline facets and resolve the group facets
		ArrayList <SFDisciplineFacetDefn> lSFDisciplineFacetDefnArr = new ArrayList <SFDisciplineFacetDefn> (InfoModel.sfDisciplineFacetDefnMap.values());
		for (Iterator <SFDisciplineFacetDefn> i = lSFDisciplineFacetDefnArr.iterator(); i.hasNext();) {
			SFDisciplineFacetDefn lSFDisciplineFacetDefn = (SFDisciplineFacetDefn) i.next();
			
			for (Iterator <String> j = lSFDisciplineFacetDefn.groupFacet1IdArr.iterator(); j.hasNext();) {
				String lSFGroupFacetId = (String) j.next();
				SFGroupFacetDefn lSFGroupFacetDefn = lSFGroupFacetDefnMap.get(lSFGroupFacetId);
				if (lSFGroupFacetDefn == null) continue;
				lSFDisciplineFacetDefn.groupFacet1Arr.add(lSFGroupFacetDefn);
			}
			for (Iterator <String> j = lSFDisciplineFacetDefn.groupFacet2IdArr.iterator(); j.hasNext();) {
				String lSFGroupFacetId = (String) j.next();
				SFGroupFacetDefn lSFGroupFacetDefn = lSFGroupFacetDefnMap.get(lSFGroupFacetId);
				if (lSFGroupFacetDefn == null) continue;
				lSFDisciplineFacetDefn.groupFacet2Arr.add(lSFGroupFacetDefn);
			}
		}

		// iterate through the discipline facets and update the permissible values for the associated attributes
		String lAttrIdPrefix = DMDocument.registrationAuthorityIdentifierValue + "." + "pds" + ".";
		String lAttrId = lAttrIdPrefix + "Discipline_Facets" + "." + "discipline_name";
		AttrDefn lAttrDiscipline = InfoModel.masterMOFAttrIdMap.get(lAttrId);
		if (lAttrDiscipline == null) {
			System.out.println("debug getProtPinsModel ERROR Missing discipline_name - lAttrId:" + lAttrId);
			return;
		}
		
		// update discpline_name values
		lAttrDiscipline.isEnumerated = true;
		for (Iterator <SFDisciplineFacetDefn> i = lSFDisciplineFacetDefnArr.iterator(); i.hasNext();) {
			SFDisciplineFacetDefn lSFDisciplineFacetDefn = (SFDisciplineFacetDefn) i.next();
//			System.out.println("debug getProtPinsModel UPDATE DISCIPLINE lSFGroupFacetDefn.identifier:" + lSFDisciplineFacetDefn.identifier);
			lAttrDiscipline.valArr.add(lSFDisciplineFacetDefn.disciplineName);

			// update facet1 values
			for (Iterator <SFGroupFacetDefn> j = lSFDisciplineFacetDefn.groupFacet1Arr.iterator(); j.hasNext();) {
				SFGroupFacetDefn lSFGroupFacet = (SFGroupFacetDefn) j.next();
//				System.out.println("debug getProtPinsModel UPDATE FACET1 lSFGroupFacet.identifier:" + lSFGroupFacet.identifier);
				lAttrId = lAttrIdPrefix + "Group_Facet1" + "." + "facet1";
				AttrDefn lAttrFacet = InfoModel.masterMOFAttrIdMap.get(lAttrId);
//				if (lAttrDiscipline == null) {
				if (lAttrFacet == null) {
					System.out.println("debug getProtPinsModel ERROR Missing facet2 - lAttrId:" + lAttrId);
					continue;
				}
				lAttrFacet.isEnumerated = true;
				lAttrFacet.valArr.add(lSFGroupFacet.facet);
				lAttrFacet.valueDependencyMap.put(lSFGroupFacet.facet, lSFDisciplineFacetDefn.disciplineName);
			}
			// update facet2 values
			for (Iterator <SFGroupFacetDefn> j = lSFDisciplineFacetDefn.groupFacet2Arr.iterator(); j.hasNext();) {
				SFGroupFacetDefn lSFGroupFacet = (SFGroupFacetDefn) j.next();
//				System.out.println("debug getProtPinsModel UPDATE FACET2 lSFGroupFacet.identifier:" + lSFGroupFacet.identifier);
				lAttrId = lAttrIdPrefix + "Group_Facet2" + "." + "facet2";
				AttrDefn lAttrFacet = InfoModel.masterMOFAttrIdMap.get(lAttrId);
//				if (lAttrDiscipline == null) {
				if (lAttrFacet == null) {
					System.out.println("debug getProtPinsModel ERROR Missing facet2 - lAttrId:" + lAttrId);
					continue;
				}
				lAttrFacet.isEnumerated = true;
				lAttrFacet.valArr.add(lSFGroupFacet.facet);
				lAttrFacet.valueDependencyMap.put(lSFGroupFacet.facet, lSFDisciplineFacetDefn.disciplineName);
			}
		} 
		return;
	}
	
	// get a string value from map
	static public String getStringValue (HashMap <String, ArrayList<String>> lMap, String lMetaAttrName, String oVal) {
//		System.out.println("debug getStringValue - lMetaAttrName:" + lMetaAttrName);
		ArrayList<String> lValArr = lMap.get(lMetaAttrName); 
		if (lValArr != null) {
			int lValArrSize = lValArr.size();
			if (lValArrSize == 1) {
				String lVal = (String) lValArr.get(0);
				if (! (lVal == null || (lVal.compareTo("")) == 0)) {
					return lVal;
				}
			}
		}
		return oVal;
		// returns the original value
	}

	// get a string value from map
	static public String getStringValueUpdate (boolean isEscaped, HashMap <String, ArrayList<String>> lMap, String lMetaAttrName, String oVal) {
//		System.out.println("debug getStringValue - lMetaAttrName:" + lMetaAttrName);
		ArrayList<String> lValArr = lMap.get(lMetaAttrName); 
		if (lValArr != null) {
			int lValArrSize = lValArr.size();
			if (lValArrSize == 1) {
				String lVal = (String) lValArr.get(0);
				if (! ((lVal == null) || ((lVal.compareTo("")) == 0) || ((lVal.indexOf("TBD")) == 0))) {
					if (isEscaped) {
						lVal = InfoModel.unEscapeProtegePatterns(lVal);
					}
					if (lVal.compareTo(oVal) != 0) {
						return lVal;
					}
				}
			}
		}
		return null;
	}
}

