package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class GetDomClasses extends Object {

	public GetDomClasses () { 		// this is a temporary bridge - DOM classes are created from PDSObjDefn classes
		// class structures
		InfoModel.masterDOMClassArr = new ArrayList <DOMClass> ();
		InfoModel.masterDOMClassMap = new TreeMap <String, DOMClass> ();
		InfoModel.masterDOMClassIdMap = new TreeMap <String, DOMClass> ();
		
		InfoModel.masterDOMAttrArr = new ArrayList <DOMAttr> ();
		InfoModel.masterDOMAttrMap = new TreeMap <String, DOMAttr> ();
		InfoModel.masterDOMAttrIdMap = new TreeMap <String, DOMAttr> ();

		InfoModel.userDOMClassAttrIdMap = new TreeMap <String, DOMAttr> ();			
		InfoModel.userSingletonDOMClassAttrIdMap = new TreeMap <String, DOMAttr> (); 
		
		InfoModel.masterDOMPropArr = new ArrayList <DOMProp> ();
		InfoModel.masterDOMPropMap = new TreeMap <String, DOMProp> ();
		InfoModel.masterDOMPropIdMap = new TreeMap <String, DOMProp> ();
		InfoModel.convertAssocDOMPropIdMap = new TreeMap <String, ArrayList<ISOClassOAIS11179>> ();
		
		InfoModel.masterDOMDataTypeArr = new ArrayList <DOMDataType> ();
		InfoModel.masterDOMDataTypeMap = new TreeMap <String, DOMDataType> ();
		InfoModel.masterDOMDataTypeIdMap = new TreeMap <String, DOMDataType> ();

		InfoModel.masterDOMDataTypeClassArr = new ArrayList <DOMClass> ();
		InfoModel.masterDOMDataTypeClassMap = new TreeMap <String, DOMClass> ();
		
		InfoModel.masterDOMUnitArr = new ArrayList <DOMUnit> ();
		InfoModel.masterDOMUnitMap = new TreeMap <String, DOMUnit> ();
		InfoModel.masterDOMUnitIdMap = new TreeMap <String, DOMUnit> ();
		
		return;
	}
	
//	public void convert () {
	public void domConvert () throws Throwable {
		// phase 1.n - for each PDSObjDefn Class, create the DOM Class - construct class and copy in singleton attributes values
		// references to other DOM classes can only be created after all DOM classes exist 
		
		// phase 1.1 - create the DOMClass object
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
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
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find new DOMClass - lClass.rdfIdentifier: " + lClass.rdfIdentifier);				
			}
		}
		
		// phase 2.0 - for each AttrDefn Attribute create the DOMProp, DOMAttr, and DOMPermValDefn - carry over choice groups
		ArrayList <ISOClassOAIS11179> lDOMPropArr = new ArrayList <ISOClassOAIS11179> ();	
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! lAttr.isAttribute) continue;
			AssocDefn lAssoc = InfoModel.masterMOFAssocIdMap.get(lAttr.identifier);
			if (lAssoc != null) {
				lDOMPropArr = new ArrayList <ISOClassOAIS11179> (); // initalize the DOMProp array for this association
				DOMProp lDOMProp = new DOMProp ();									// create the DOMProp for the DOMAttr
				lDOMProp.createDOMPropAttrSingletons (lAssoc, lAttr);
				lDOMProp.initPropParentClass (lAttr, InfoModel.masterDOMClassIdMap);
				InfoModel.masterDOMPropArr.add(lDOMProp); 
				InfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
				InfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
				DOMAttr lDOMAttr = new DOMAttr ();									// create the DOMAttr
				lDOMAttr.createDOMAttrSingletons (lAttr);
				lDOMAttr.initAttrParentClass (lAttr, InfoModel.masterDOMClassIdMap);
				lDOMProp.hasDOMObject = lDOMAttr;									// assign the one DOMAttr to the DOMProp
				lDOMPropArr.add(lDOMProp);
				InfoModel.masterDOMAttrArr.add(lDOMAttr); 
				InfoModel.masterDOMAttrMap.put(lDOMAttr.rdfIdentifier, lDOMAttr);
				InfoModel.masterDOMAttrIdMap.put(lDOMAttr.identifier, lDOMAttr);
				InfoModel.convertAssocDOMPropIdMap.put(lAttr.identifier, lDOMPropArr);
						
				// phase 2.2.2 - for each permissible value create a DOMProp and DOMPermVal
				for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
					PermValueDefn lPermValue = (PermValueDefn) k.next();
					DOMProp lDOMProp2 = new DOMProp ();								// create the DOMProp for the Permissible Value
					lDOMAttr.domPermValueArr.add(lDOMProp2);
					lDOMAttr.hasDOMObject.add(lDOMProp2);							// add the DOMProp to the DOMattr
					DOMPermValDefn lDOMPermValDefn = new DOMPermValDefn ();			// create the DOMPermValDefn
					lDOMProp2.hasDOMClass.add(lDOMPermValDefn);						
					lDOMProp2.hasDOMObject = lDOMPermValDefn;						// assign the one DOMPermValDefn to the DOMProp
					if (lDOMAttr.title.compareTo("pattern") == 0) lDOMPermValDefn.isPattern = true;
					lDOMPermValDefn.createDOMPermValSingletons(lPermValue, lAttr);
// fix					lDOMPermValDefn.setRDFIdentifier(lTitle);
// fix					lDOMPermValDefn.setIdentifier(lNameSpaceIdNC, lTitle);
					lDOMProp2.initDOMPermValProp(lDOMPermValDefn);
				}
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AttrDefn - lAttr.rdfIdentifier): " + lAttr.rdfIdentifier);
			}
		}
		
		// phase 3.0 - for each AttrDefn Association create the DOMProp and associate the existing classes
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isAttribute) continue;
			AssocDefn lAssoc = InfoModel.masterMOFAssocIdMap.get(lAttr.identifier);
			if (lAssoc != null) {			
				lDOMPropArr = new ArrayList <ISOClassOAIS11179> (); // initalize the DOMProp array for this association
				for (Iterator <PDSObjDefn> j = lAttr.valClassArr.iterator(); j.hasNext();) {
					PDSObjDefn lClass = (PDSObjDefn) j.next();
					DOMClass lDOMClass = InfoModel.masterDOMClassIdMap.get(lClass.identifier); // get the associated DOMClass
					if (lDOMClass != null) {
						DOMProp lDOMProp = new DOMProp ();										// create the DOMProp for the DOMClass association
						lDOMProp.createDOMPropClassSingletons (lAssoc, lAttr, lClass);
						lDOMProp.initPropParentClass (lAttr, InfoModel.masterDOMClassIdMap);
						InfoModel.masterDOMPropArr.add(lDOMProp); 
						InfoModel.masterDOMPropMap.put(lDOMProp.rdfIdentifier, lDOMProp);
						InfoModel.masterDOMPropIdMap.put(lDOMProp.identifier, lDOMProp);
						lDOMPropArr.add(lDOMProp);
						lDOMProp.hasDOMObject = lDOMClass;										// assign the DOMClass to the DOMProp
					} else {
						System.out.println(">>error    - GetDOMClasses.convert - Failed to find new DOMClass - Assoc - lClass.rdfIdentifier: " + lClass.rdfIdentifier);				
					}
				}
				InfoModel.convertAssocDOMPropIdMap.put(lAttr.identifier, lDOMPropArr);
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old AssocDefn - lAttr.rdfIdentifier): " + lAttr.rdfIdentifier);				
			}
		}
		
		// phase 4 - for each DOM class, copy in references to DOM Properties/DOM Attributes 
		for (Iterator <DOMClass> i = InfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();
			PDSObjDefn lClass = InfoModel.masterMOFClassMap.get(lDOMClass.rdfIdentifier);
			if (lClass != null) {
				lDOMClass.initDOMClassAttrArrs (lClass, InfoModel.masterDOMPropMap, InfoModel.masterDOMAttrMap);
			} else {
				System.out.println(">>error    - GetDOMClasses.convert - Failed to find old PDS4ObjDefn Class - lDOMClass.rdfIdentifier: " + lDOMClass.rdfIdentifier);				
			}
		}
		
		// phase 5 - for each PDSObjDefn Datatype, create the DOM DataType
		for (Iterator <DataTypeDefn> i = InfoModel.masterDataTypesArr2.iterator(); i.hasNext();) {
			DataTypeDefn lDataTypeDefn = (DataTypeDefn) i.next();
			DOMDataType lDOMDataType= new DOMDataType ();
			lDOMDataType.createDOMDataTypeSingletons (lDataTypeDefn);
			lDOMDataType.setRDFIdentifier (lDataTypeDefn.title);
			lDOMDataType.setIdentifier (DMDocument.masterNameSpaceIdNCLC, lDataTypeDefn.title);
			InfoModel.masterDOMDataTypeArr.add(lDOMDataType); 
			InfoModel.masterDOMDataTypeMap.put(lDOMDataType.rdfIdentifier, lDOMDataType);
			InfoModel.masterDOMDataTypeIdMap.put(lDOMDataType.identifier, lDOMDataType);
		}		
		
		// phase 6 - for each PDSObjDefn Unit, create the DOM Unit
		for (Iterator <UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();
			DOMUnit lDOMUnit= new DOMUnit ();
			lDOMUnit.createDOMUnitSingletons (lUnit);
			lDOMUnit.setRDFIdentifier (lUnit.title);
			lDOMUnit.setIdentifier (DMDocument.masterNameSpaceIdNCLC, lUnit.title);
			InfoModel.masterDOMUnitArr.add(lDOMUnit); 
			InfoModel.masterDOMUnitMap.put(lDOMUnit.rdfIdentifier, lDOMUnit);
			InfoModel.masterDOMUnitIdMap.put(lDOMUnit.identifier, lDOMUnit);
		}
	}
}
