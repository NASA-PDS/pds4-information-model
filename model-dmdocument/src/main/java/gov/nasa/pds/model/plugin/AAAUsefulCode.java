package gov.nasa.pds.model.plugin;
public class AAAUsefulCode {
	
	
	/*	

	System.out.println("debug xxx yyy.identifier:" + yyy.identifier);

	InfoModel.printObjectDebug (1, InfoModel.masterMOFClassIdMap.get("0001_NASA_PDS_1.rtqbb.Band_Bin_Set_RTQBB130614"));


	// list Units keySet		
	ArrayList <String> lUnitKeyArr = new ArrayList <String> (InfoModel.masterUnitOfMeasureMap.keySet());
	for (Iterator <String> i = lUnitKeyArr.iterator(); i.hasNext();) {
		String lUnit = (String) i.next();
		System.out.println("debug - Unit Key Dump - lUnit:" + lUnit);
	}
	
	// list Units		
	 ArrayList <UnitDefn> lUnitArr = new ArrayList <UnitDefn> (InfoModel.masterUnitOfMeasureMap.values());
	for (Iterator <UnitDefn> i = lUnitArr.iterator(); i.hasNext();) {
		UnitDefn lUnit = (UnitDefn) i.next();
		System.out.println("debug - Unit Dump - lUnit.title:" + lUnit.title);
	}
	
//  debug routine - print number of associations
	public void debugPrintNumberAssociations (int lLocation) {
		System.out.println("\ndebug - debugPrintNumberAssociations - location:" + lLocation);						
		// iterate through the classes	
		for (Iterator<PDSObjDefn> i = masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (! lClass.isFromLDD) continue;
			System.out.println("debug - debugPrintNumberAssociations - " + lClass.allAttrAssocArr.size());					
		}
		return;
	}
*/
	
	/*
	// print out the Class Order Map
	System.out.println("\nClass Order Map");
	Set <String> set9 = lddClassOrderArr.keySet();
	Iterator <String> iter9 = set9.iterator();
	while(iter9.hasNext()) {
		String lTitle = (String) iter9.next();
		Integer lOrder = lddClassOrderArr.get(lTitle);
		System.out.println("debug name: " + lTitle + "   order: " + lOrder);
	} */
	
	/*	public TreeMap <String, AssocClassDefn> getClassxxx (boolean isFirst, int lLevel, int classCardMin,  int classCardMax, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitedClassList, TreeMap <String, AssocClassDefn> allNonProductReferenceClassesMap, PrintWriter prDocBook) {
	if (visitedClassList.contains(lClass)) return allNonProductReferenceClassesMap;
	else visitedClassList.add(lClass);
	
	if (DMDocument.omitClass.contains(lClass.title)) return allNonProductReferenceClassesMap;
	
	System.out.println("debug getClass lClass.identifier:" + lClass.identifier);

	// get associated classes
	ArrayList <AssocClassDefn> lAssocClassArr = InfoModel.getSortedAlphaClassAssocClassArr (lClass);
	if (lAssocClassArr != null) {
		for (Iterator<AssocClassDefn> j = lAssocClassArr.iterator(); j.hasNext();) {
			AssocClassDefn lAssocClass = (AssocClassDefn) j.next();

			// save all classes that are referenced but not products
			if (allNonProductReferenceClassesMap.get(lAssocClass.assocClass.identifier) == null)
				allNonProductReferenceClassesMap.put(lAssocClass.assocClass.identifier, lAssocClass);
			
			ArrayList <PDSObjDefn> visitedClassListCopy = new ArrayList <PDSObjDefn> ();
			for (Iterator <PDSObjDefn> k = visitedClassList.iterator(); k.hasNext();) {
				visitedClassListCopy.add((PDSObjDefn) k.next());
			}
			allNonProductReferenceClassesMap = getClassxxx (false, lLevel + 1, lAssocClass.cardMin, lAssocClass.cardMax, lAssocClass.assocClass, visitedClassListCopy, allNonProductReferenceClassesMap, prDocBook);
		}
	}
	return allNonProductReferenceClassesMap;
}
 */
	
	
	
}
