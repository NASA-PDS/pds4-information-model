package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class GetDomClasses extends Object {

	public GetDomClasses () {
		// class structures
		InfoModel.masterDOMClassArr = new ArrayList <DOMClass> ();
		InfoModel.masterDOMClassMap = new TreeMap <String, DOMClass> ();
		InfoModel.masterDOMClassIdMap = new TreeMap <String, DOMClass> ();
		
		InfoModel.masterDOMAttrArr = new ArrayList <DOMAttr> ();
		InfoModel.masterDOMAttrMap = new TreeMap <String, DOMAttr> ();
		InfoModel.masterDOMAttrIdMap = new TreeMap <String, DOMAttr> ();
		
		InfoModel.masterDOMPropArr = new ArrayList <DOMProp> ();
		InfoModel.masterDOMPropMap = new TreeMap <String, DOMProp> ();
		InfoModel.masterDOMPropIdMap = new TreeMap <String, DOMProp> ();
		
		InfoModel.masterDOMDataTypeArr = new ArrayList <DOMDataType> ();
		InfoModel.masterDOMDataTypeMap = new TreeMap <String, DOMDataType> ();
		InfoModel.masterDOMDataTypeIdMap = new TreeMap <String, DOMDataType> ();
		
		InfoModel.masterDOMUnitArr = new ArrayList <DOMUnit> ();
		InfoModel.masterDOMUnitMap = new TreeMap <String, DOMUnit> ();
		InfoModel.masterDOMUnitIdMap = new TreeMap <String, DOMUnit> ();
		
		return;
	}
	
	public void convert () {
		// phase 1.n - for each PDSObjDefn Class, create the DOM Class - construct class and copy in singleton attributes values
		// references to other DOM classes can only be created after all DOM classes exist 
		
		// phase 1.1 - create the DOMClass object
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			System.out.println("debug GetDOMClasses convert - phase 1.1 - lClass.identifier:" + lClass.identifier);
			DOMClass lDOMClass = new DOMClass ();
			lDOMClass.createDOMClassSingletons (lClass);
			InfoModel.masterDOMClassArr.add(lDOMClass); 
			InfoModel.masterDOMClassMap.put(lDOMClass.rdfIdentifier, lDOMClass);
			InfoModel.masterDOMClassIdMap.put(lDOMClass.identifier, lDOMClass);
		}

		// phase 1.2 - copy in class references for class hierarchies
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			DOMClass lDOMClass = InfoModel.masterDOMClassMap.get(lClass.rdfIdentifier);
			if (lDOMClass != null) {
				lDOMClass.initDOMClassHierArrs (lClass, InfoModel.masterDOMClassMap);
//				System.out.println("debug GetDOMClasses.convert - phase 1.2 - init DOM Class Hierarchies - lDOMClass.identifier:" + lDOMClass.identifier);
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find new DOMClass - lClass.rdfIdentifier: " + lClass.rdfIdentifier);				
			}
		}
		
		// phase 2.n - for each AttrDefn Attribute Association create the DOMAttr and DOMPermValDefn, then DOMProps with choice groups
		// phase 2.1 - first group "choice" blocks
		ArrayList <AssocDefn> lAttrAssocArr = new ArrayList <AssocDefn> ();
		String lPrevGroupName = "FirstOfItsName";
		AssocDefn lPrevAssoc = null;
		for (Iterator<AssocDefn> i = InfoModel.masterMOFAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			if (! lAssoc.isAttribute) continue;
//			System.out.println("debug GetDOMClasses.convert - phase 2.1 - choice - lAssoc.identifier:" + lAssoc.identifier);
			AttrDefn lAttr = InfoModel.masterMOFAttrIdMap.get(lAssoc.identifier);
			if (lAttr != null) {
				if (lPrevGroupName.compareTo(lAssoc.groupName) != 0) {
					lAttrAssocArr.add(lAssoc);
					lAssoc.childAttrArr.add(lAttr); 
					lPrevGroupName = lAssoc.groupName;
					lPrevAssoc = lAssoc;
				} else {
					if (lAssoc.groupName.indexOf("TBD") == 0) {
						lAttrAssocArr.add(lAssoc);
						lAssoc.childAttrArr.add(lAttr);
					} else {
						lPrevAssoc.childAttrArr.add(lAttr);
					}
				}
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AttrDefn - lAssoc.identifier): " + lAssoc.identifier);								
			}
		}
		
		// phase 2.2 - for each AttrDefn Attribute Association create the DOMAttr and DOMPermValDefn, then DOMProps with choice groups
		
		for (Iterator <AssocDefn> i = lAttrAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
//			System.out.println("debug GetDOMClasses.convert - phase 2.2 - Association - lAssoc.identifier:" + lAssoc.identifier);
			AttrDefn lAttr1 = InfoModel.masterMOFAttrMap.get(lAssoc.rdfIdentifier);
			if (lAttr1 != null) {
//				System.out.println("debug GetDOMClasses.convert - phase 2.2 - Associated Attribute - lAttr1.identifier:" + lAttr1.identifier);
				DOMProp lDOMProp1 = new DOMProp ();
				lDOMProp1.createDOMPropSingletons (lAssoc, lAttr1);
				InfoModel.masterDOMPropArr.add(lDOMProp1); 
				InfoModel.masterDOMPropMap.put(lDOMProp1.rdfIdentifier, lDOMProp1);
				InfoModel.masterDOMPropIdMap.put(lDOMProp1.identifier, lDOMProp1);
//				System.out.println("debug GetDOMClasses.convert - phase 2.2 - create DOM Property - lDOMProp1.identifier:" + lDOMProp1.identifier);
				// phase 2.2.1 - create the DOMAttr
				for (Iterator <AttrDefn> j = lAssoc.childAttrArr.iterator(); j.hasNext();) {
					AttrDefn lAttr2 = (AttrDefn) j.next();
					if (lAttr2.isAttribute) {
//						System.out.println("debug GetDOMClasses.convert - phase 2.2.1 - create DOMAttr - lAttr2.identifier:" + lAttr2.identifier);
						DOMAttr lDOMAttr = new DOMAttr ();
						lDOMAttr.createDOMAttrSingletons (lAttr2);
						lDOMProp1.hasDOMClass.add(lDOMAttr);
						InfoModel.masterDOMAttrArr.add(lDOMAttr); 
						InfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
						InfoModel.masterDOMAttrIdMap.put(lDOMAttr.identifier, lDOMAttr);
						// phase 2.2.2 - for each permissible value create a DOMProp and DOMPermVal
//						System.out.println("debug GetDOMClasses.convert - phase 2.2.2 - create DOM Attributes - lDOMAttr.identifier:" + lDOMAttr.identifier);
						for (Iterator <PermValueDefn> k = lAttr2.permValueArr.iterator(); k.hasNext();) {
							PermValueDefn lPermValue = (PermValueDefn) k.next();
							DOMProp lDOMProp2 = new DOMProp ();
							lDOMAttr.domPermValueArr.add(lDOMProp2);
							DOMPermValDefn lDOMPermValDefn = new DOMPermValDefn ();
							lDOMProp2.hasDOMClass.add(lDOMPermValDefn);
							if (lDOMAttr.title.compareTo("pattern") == 0) lDOMPermValDefn.isPattern = true;
							lDOMPermValDefn.createDOMPermValSingletons(lPermValue, lAttr2);
		// fix					lDOMPermValDefn.setRDFIdentifier(lTitle);
		// fix					lDOMPermValDefn.setIdentifier(lNameSpaceIdNC, lTitle);
							lDOMProp2.initDOMPermValProp(lDOMPermValDefn);
						} 
					}
				}
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AttrDefn - lAssoc.rdfIdentifier): " + lAssoc.rdfIdentifier);				
			}
		}
					
		// phase 3.0 - for each AttrDefn Association DOMProp - Class associations
		// references to other DOM attributes can only be created after all DOM attributes exist 
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttr.isAttribute) {
				AssocDefn lAssoc = InfoModel.masterMOFAssocMap.get(lAttr.rdfIdentifier);
				if (lAssoc != null) {
					DOMProp lDOMProp = new DOMProp ();
					lDOMProp.createDOMPropSingletons (lAssoc, lAttr);
					InfoModel.masterDOMPropArr.add(lDOMProp); 
					InfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
					InfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
//					System.out.println("debug GetDOMClasses.convert - phase 4 - create DOM Properties - lDOMProp.identifier:" + lDOMProp.identifier);
					for (Iterator <PDSObjDefn> j = lAttr.valClassArr.iterator(); j.hasNext();) {
						PDSObjDefn lClass = (PDSObjDefn) j.next();
						DOMClass lDOMClass = InfoModel.masterDOMClassMap.get(lClass.rdfIdentifier);
						if (lDOMClass != null) {
							lDOMProp.hasDOMClass.add(lDOMClass);
//							System.out.println("debug GetDOMClasses.convert - Added- lDOMClass.identifier:" + lDOMClass.identifier);
						} else {
							System.out.println(">>error    - GetDOMClasses.convert - Failed to find new DOMClass - Assoc - lClass.rdfIdentifier: " + lClass.rdfIdentifier);				
						}
					}
				} else {
					System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AssocDefn - lAttr.rdfIdentifier): " + lAttr.rdfIdentifier);				
				}
//				System.out.println("debug GetDOMClasses.convert - phase 3 - create DOM Attributes - lDOMAttr.identifier:" + lDOMAttr.identifier);
			}
		}
		
		// phase 4 - for each AssocDefn attribute, create a DOM Property - construct property and copy singleton values
/*		for (Iterator <AssocDefn> i = InfoModel.masterMOFAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			DOMProp lDOMProp = new DOMProp ();
			AttrDefn lAttr = InfoModel.masterMOFAttrMap.get(lAssoc.rdfIdentifier);
			if (lAttr != null) {
				lDOMProp.createDOMPropSingletons (lAssoc, lAttr);
				InfoModel.masterDOMPropArr.add(lDOMProp); 
				InfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
				InfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
//				System.out.println("debug GetDOMClasses.convert - phase 4 - create DOM Properties - lDOMProp.identifier:" + lDOMProp.identifier);
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AttrDefn - lAssoc.rdfIdentifier): " + lAssoc.rdfIdentifier);				
			}
		} */
		
		// phase 5 - for each DOM class, copy in references to DOM Properties/DOM Attributes 
		for (Iterator <DOMClass> i = InfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			PDSObjDefn lClass = InfoModel.masterMOFClassMap.get(lDOMClass.rdfIdentifier);
			if (lClass != null) {
				lDOMClass.initDOMClassAttrArrs (lClass, InfoModel.masterDOMPropMap, InfoModel.masterDOMAttrMap);
//				System.out.println("debug GetDOMClasses.convert - phase 5 - lDOMClass.identifier:" + lDOMClass.identifier);
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old PDS4ObjDefn Class - lDOMClass.rdfIdentifier: " + lDOMClass.rdfIdentifier);				
			}
		}
	
		// phase 6 - for each PDSObjDefn Datatype, create the DOM DataType
		for (Iterator <DataTypeDefn> i = InfoModel.masterDataTypesArr2.iterator(); i.hasNext();) {
			DataTypeDefn lDataTypeDefn = (DataTypeDefn) i.next();
//			System.out.println("debug GetDOMClasses convert - phase 1 - lClass.identifier:" + lClass.identifier);
			DOMDataType lDOMDataType= new DOMDataType ();
			lDOMDataType.createDOMDataTypeSingletons (lDataTypeDefn);
			lDOMDataType.setRDFIdentifier (lDataTypeDefn.title);
			lDOMDataType.setIdentifier ("pds", lDataTypeDefn.title);
			InfoModel.masterDOMDataTypeArr.add(lDOMDataType); 
			InfoModel.masterDOMDataTypeMap.put(lDOMDataType.rdfIdentifier, lDOMDataType);
			InfoModel.masterDOMDataTypeIdMap.put(lDOMDataType.identifier, lDOMDataType);
		}		
		
		
		// phase 7 - for each PDSObjDefn Unit, create the DOM Unit
		for (Iterator <UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();
//			System.out.println("debug GetDOMClasses convert - phase 1 - lClass.identifier:" + lClass.identifier);
			DOMUnit lDOMUnit= new DOMUnit ();
			lDOMUnit.createDOMUnitSingletons (lUnit);
			lDOMUnit.setRDFIdentifier (lUnit.title);
			lDOMUnit.setIdentifier ("pds", lUnit.title);
			InfoModel.masterDOMUnitArr.add(lDOMUnit); 
			InfoModel.masterDOMUnitMap.put(lDOMUnit.rdfIdentifier, lDOMUnit);
			InfoModel.masterDOMUnitIdMap.put(lDOMUnit.identifier, lDOMUnit);
		}		
		
	// phase 6 - for each DOM Attribute, write a DOM Value class 
//	for (Iterator <DOMClass> i = InfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
//		DOMClass lDOMClass = (DOMClass) i.next();
//		PDSObjDefn lClass = InfoModel.masterMOFClassMap.get(lDOMClass.rdfIdentifier);
//		if (lClass != null) {
//			System.out.println("debug GetDOMClasses convert - phase 5 - lDOMClass.identifier:" + lDOMClass.identifier);
//			lDOMClass.initDOMClassAttrArrs (lClass, InfoModel.masterDOMAttrMap);
//			System.out.println("debug GetDOMClasses.convert - phase 5 - lDOMClass.identifier:" + lDOMClass.identifier);
//		} else {
//			System.out.println(">>error    - GetDOMClasses.convert - Failed to find old PDS4ObjDefn Class - lDOMClass.rdfIdentifier: " + lDOMClass.rdfIdentifier);				
//		}
//	}
}

	public void writeAttrSection (String lNameSpaceId, PrintWriter prDocBook) {
		// get the attribute classification maps
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
//			System.out.println("\ndebug writeAttrSection lAttr.identifier:" + lAttr.identifier);
//			System.out.println("debug writeAttrSection lAttr.attrNameSpaceIdNC:" + lAttr.attrNameSpaceIdNC);
		}
	}
}
