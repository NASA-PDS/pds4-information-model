package gov.nasa.pds.model.plugin; 
import java.util.*;

/**
 * Creates the in memory 11179 DD (InstDefn) from a Protege 11179 DD
 *   getProtPins11179DD - gets a parsed a pins file - 11179 data dictionary
 */
//class ProtPins11179DD extends InfoModel{
class ProtPins11179DD extends Object {
	ProtPins protPinsInst;
	
	public ProtPins11179DD () {

		// Parse Protege Instance File - Parse InstDefn stored as follows.
		// lInst.className - e.g. DataElement
		// + lRegAuthId + "." + lInst.className + "." + lInst.title; 
		// ++ InstDefn
		InfoModel.master11179DataDict = new TreeMap <String, InstDefn> ();
		return;
	}

	/**
	 *   Parses a pins file - 11179 data dictionary
	 *   Updates/Creates a data dictionary 
	 */
	public void getProtPins11179DD (String lRegAuthId, String fname) throws Throwable {

		// parse the PINS file
		protPinsInst = new ProtPins();
		protPinsInst.getProtInst("dd79", "dd79", fname);
		
		// iterate through the resultant instances and build the Master 11179 DD
		HashMap <String, InstDefn> lInstMap = protPinsInst.instDict;
		Set <String> set1 = lInstMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			InstDefn lInst = (InstDefn) lInstMap.get(lId);
	//		System.out.println("\ndebug getProtPins11179DD1 lInst.identifier:" + lInst.identifier);			
	//		System.out.println("debug getProtPins11179DD2 lInst.title:" + lInst.title);
	//		System.out.println("debug getProtPins11179DD3 lInst.rdfIdentifier:" + lInst.rdfIdentifier);
	//		System.out.println("debug getProtPins11179DD4 lInst.className:" + lInst.className);
			
			// from the instance class, get the instance (a data element with its meta-attributes)
			String lMast11179Id2 = lInst.className + "." + lInst.title; 
			InstDefn lInst3 = InfoModel.master11179DataDict.get(lMast11179Id2);
			if (lInst3 == null) {
				InfoModel.master11179DataDict.put(lMast11179Id2, lInst);
	//			System.out.println("debug getProtPins11179DataDict ++adding instance++ lMast11179Id2:" + lMast11179Id2 + "  lInst.rdfIdentifier:" + lInst.rdfIdentifier);
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
						lVal = InfoModel.unEscapeProtegeString(lVal);
					}
					if (lVal.compareTo(oVal) != 0) {
						return lVal;
					}
				}
			}
		}
		return null;
	}

	// get a string value from map
	static public String getStringValueUpdatePattern (boolean isEscaped, HashMap <String, ArrayList<String>> lMap, String lMetaAttrName, String oVal) {
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
		
	// dump master 11179 Data Dictionary
	public void dumpMaster11179DataDict () {
//		HashMap <String, ArrayList<String>> lMap = new HashMap <String, ArrayList<String>> ();

		System.out.println("\n ======= Dump 11179 Data Dictionary =======");
		Set <String> set2 = InfoModel.master11179DataDict.keySet();
		Iterator <String> iter2 = set2.iterator();	
		while (iter2.hasNext()) {
			// get the instance of the meta class - e.g. DataElement -> target_name
			String lMetaClassInstId = (String) iter2.next();
			System.out.println("\ndebug 11179 Dump -   lMetaClassInstId:" + lMetaClassInstId);
			InstDefn lInst = InfoModel.master11179DataDict.get(lMetaClassInstId);
			if (lInst != null) {
				System.out.println("                 -     Instance Name:" + lInst.title);
//				lMap = lInst.genSlotMap;						
				HashMap <String, ArrayList<String>> lMap = lInst.genSlotMap;						
				Set <String> set3 = lMap.keySet();
				Iterator <String> iter3 = set3.iterator();	
				while (iter3.hasNext()) {
					String lMetaAttrName = (String) iter3.next(); 
					// get meta attributes - e.g. DataElement -> target_name -> deIdentifier
//					System.out.println("                 -     Name:" + lMetaAttrName);
					ArrayList <String> lValArr = lMap.get(lMetaAttrName); // an ArrayList; could have one value or many.
					if (lValArr != null) {
						int lValArrSize = lValArr.size();
						if (lValArrSize == 1) {
							String lVal = (String) lValArr.get(0);
							if (! (lVal == null || (lVal.compareTo("")) == 0)) {
								System.out.println("                 -     Name:" + lMetaAttrName + "   Value:" + lVal);
							}
						} else if (lValArrSize > 1) {
							System.out.print("                 -     Name:" + lMetaAttrName + "  (Multi-valued) ");
							for (Iterator <String> i = lValArr.iterator(); i.hasNext();) {
								String lVal = (String) i.next();
								if (lVal.compareTo("") != 0) {
									System.out.print("Value:" + lVal + "; ");
								}
							}
							System.out.println("");
						}
					}
				}
			}
		}
	}
}

