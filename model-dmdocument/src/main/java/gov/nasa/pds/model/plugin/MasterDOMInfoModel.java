package gov.nasa.pds.model.plugin; 
import java.util.*;

class MasterDOMInfoModel extends DOMInfoModel{ 
	static String attrNameClassWord;
	
	TreeMap <String, DOMClass> lClassHierLevelsMap = new TreeMap <String, DOMClass> ();
	ArrayList <DOMClass> lClassHierLevelsArr = new ArrayList <DOMClass> ();
	
	public MasterDOMInfoModel () {
		
		// create the USER class - the root of all classes and all namespaces
		String lTitle = DMDocument.masterUserClassName;
		String lClassNameSpaceIdNC = DMDocument.masterUserClassNamespaceIdNC;
		DOMClass lClass = new DOMClass();
		lClass.setRDFIdentifier(lTitle);
		lClass.identifier = DOMInfoModel.getClassIdentifier(lClassNameSpaceIdNC, lTitle);
		lClass.nameSpaceIdNC = lClassNameSpaceIdNC;
		lClass.nameSpaceId = lClassNameSpaceIdNC + ":";
		lClass.subClassOf = null;
		lClass.subClassOfTitle = "N/A";
		lClass.subClassOfIdentifier = "N/A";
		lClass.rootClass = "N/A";
		lClass.baseClassName = "N/A";
		lClass.definition = "The root class.";
		lClass.steward = DMDocument.masterNameSpaceIdNCLC;
		lClass.title = lTitle;
		lClass.role = "abstract";
		lClass.isAbstract = true;
		lClass.isUSERClass = true;
		lClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
		lClass.subModelId = "ROOT";
		lClass.docSecType = "USER";

		DOMInfoModel.masterDOMUserClass = lClass;
		DOMInfoModel.masterDOMClassArr.add(lClass);
		DOMInfoModel.masterDOMClassMap.put(lClass.rdfIdentifier, lClass);
		DOMInfoModel.masterDOMClassIdMap.put(lClass.identifier, lClass);
		DOMInfoModel.masterDOMClassTitleMap.put(lClass.title, lClass);		
		return;
	}

// *** General Update Routines ***
	
	// 002 & 009 - set the attrParentClass (attributes parent class) from the class name (temp fix)
	public void setAttrParentClass (boolean forLDD) {		
		// for each PDS4 attribute set the attrParentClass
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (forLDD && ! lAttr.isFromLDD) continue;
			if (! forLDD && lAttr.isFromLDD) continue;
			String lClassId = DOMInfoModel.getClassIdentifier (lAttr.classNameSpaceIdNC, lAttr.parentClassTitle);
			DOMClass lParentClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lParentClass != null) {
				lAttr.attrParentClass = lParentClass;
			} else {
				boolean classNotFound = true;
				for (Iterator<DOMClass> k = DOMInfoModel.masterDOMClassArr.iterator(); k.hasNext();) {
					lParentClass = (DOMClass) k.next();
					if (lParentClass.title.compareTo(lAttr.parentClassTitle) == 0) {
						if (classNotFound) {
							classNotFound = false;
							lAttr.attrParentClass = lParentClass;
							System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - using first found - lClassMember.identifier:" + lParentClass.identifier);
						} else {
							System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - also found - lClassMember.identifier:" + lParentClass.identifier);
						}
					}
				}
				if (classNotFound) {
					lAttr.attrParentClass = DOMInfoModel.masterDOMUserClass;
					System.out.println(">>warning - set attributes parent class - lAttr.identifier:" + lAttr.identifier + " - parent class not found, using USER - lClassMember.identifier:" + lParentClass.identifier);
				}
			}
		}
	}	
	
	
	// 007 - Get USER Class Attributes Id Map
	//  The attributes are not cloned, they are simply added to the DOMInfoModel.userClassAttrIdMap
	//  with the identifier for the map using "attrNameSpaceIdNC.USER" + "attrNameSpaceIdNC.title"
	//  e.g. pds.USER.pds.comment or disp.USER.disp.name	
	//  There is a many-to-one map so only one attribute survives, however all have same definition
	public void getUserClassAttrIdMap () {
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (! lClass.isMasterClass) continue;
			if (lClass.title.indexOf("PDS3") > -1) continue;   // kludge until boolean is set up
			for (Iterator <DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					
					// the namespace of the USER class for any attribute is the same as the namespace of the attribute
					String lUserAttrIdentifier = DOMInfoModel.getAttrIdentifier (DMDocument.masterUserClassNamespaceIdNC, DMDocument.masterUserClassName, lDOMAttr.nameSpaceIdNC, lDOMAttr.title);
					DOMInfoModel.userDOMClassAttrIdMap.put(lUserAttrIdentifier, lDOMAttr);
				}
			}
		}
		return;
	}
	
	// 010 - set the master unitOfMeasure
	public void setMasterUnitOfMeasure () {
		// iterate through the classes and create a sorted array
		// *** cool code
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isUnitOfMeasure) {
				if (lClass.role.compareTo("concrete") == 0) {
					DOMUnit lUnit = DOMInfoModel.masterDOMUnitMap.get(lClass.title);
					if (lUnit == null) {
						// the unit does not exist, add it
						lUnit = new DOMUnit ();
						lUnit.identifier = lClass.title;
						lUnit.pds4Identifier = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClass.title);
						lUnit.title = lUnit.identifier;
						lUnit.type = lUnit.identifier;
						lUnit.nameSpaceIdNC = lClass.nameSpaceIdNC;
						lUnit.nameSpaceId = lClass.nameSpaceId;
						lUnit.steward = lClass.steward;
						DOMInfoModel.masterDOMUnitMap.put(lUnit.title, lUnit);
// 333	-- deprecate masterDOMUnitMap, use title
						DOMInfoModel.masterDOMUnitTitleMap.put(lUnit.title, lUnit);
						// for each attribute of the class
						for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
							DOMProp lDOMProp = (DOMProp) j.next();
							if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
								DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
								
								// set the default_unit_id
								if (lDOMAttr.title.compareTo("specified_unit_id") == 0) {
									String lVal = DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr, lUnit.default_unit_id);
									if (lVal != null) {
										lUnit.default_unit_id =  lVal;
									}
								}
								// set the unit_id
								if (lDOMAttr.title.compareTo("unit_id") == 0) {
									ArrayList <String> lValArr = DOMInfoModel.getMultipleValue(lDOMAttr.valArr);
									if (lValArr != null) {
										for (Iterator<String> l = lValArr.iterator(); l.hasNext();) {
											String lVal = (String) l.next();
											lUnit.unit_id.add(lVal); 
										}
									}
								}
							}
							
							DOMAttr lAttr = (DOMAttr) lDOMProp.hasDOMObject;
							if (lAttr != null) {								// set the default_unit_id
								if (lAttr.title.compareTo("specified_unit_id") == 0) {
									String lVal = DOMInfoModel.getSingletonValueUpdate(lAttr.valArr, lUnit.default_unit_id);
									if (lVal != null) {
										lUnit.default_unit_id =  lVal;
									}
								}
								// set the unit_id
								if (lAttr.title.compareTo("unit_id") == 0) {
									ArrayList <String> lValArr = DOMInfoModel.getMultipleValue(lAttr.valArr);
									if (lValArr != null) {
										for (Iterator<String> l = lValArr.iterator(); l.hasNext();) {
											String lVal = (String) l.next();
											lUnit.unit_id.add(lVal); 
										}
									}
								}
							}
						}
						DOMInfoModel.masterDOMUnitMap.put(lUnit.title, lUnit);		
					}
				}
			}
		}
		DOMInfoModel.masterDOMUnitArr = new ArrayList <DOMUnit> (DOMInfoModel.masterDOMUnitMap.values());
	}	

	// 011.1 - get the subClassOf (superClass) identifier and instances for each class, using the title
	public void getSubClassOf () {
		// iterate through the classes and get the subClassOf			
		for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isUSERClass) continue;					
			DOMClass lSupClass = (DOMClass) masterDOMClassTitleMap.get(lClass.subClassOfTitle);
			if (lSupClass != null) {
				lClass.subClassOf = lSupClass;
				lClass.subClassOfTitle = lSupClass.title;;
				lClass.subClassOfIdentifier = lSupClass.identifier;
			} else {
				System.out.println("***error*** - missing superClass in master while trying to set subClassOf - lClass.subClassOfTitle:" + lClass.subClassOfTitle);					
			}
		}
		return;
	}
	
	// 011.2 - get each class's super class hierarchy
	public void getSuperClassHierArr () {
		// for each class get its superclasses, bottom up
		for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			// array for super classes of this class
			ArrayList <DOMClass> lSuperClassArr = new ArrayList <DOMClass> ();
			if (lClass.isUSERClass) continue;
			DOMClass lSuperClass = lClass.subClassOf;

			// recurse up the subClassOf chain, getting the super class array
			while (lSuperClass != null && ! lSuperClass.isUSERClass) {
				if (! lSuperClassArr.contains(lSuperClass)) {
					lSuperClassArr.add(lSuperClass);
				} else {
					System.out.println(">>error   - Found cycle in superclass hierarchy - SuperClass.title:" + lSuperClass.title);
					break;
				}	
				// get next superclass

				lSuperClass = lSuperClass.subClassOf;
			}
			
			// save the super class hierarchy
			lClass.superClassHierArr = lSuperClassArr;
			
			// get the class hierarchy level 
			Integer lClassHierLevels = 1000 + lSuperClassArr.size();
			String lClassHierLevelsId = lClassHierLevels.toString() + "-" + lClass.identifier;
			lClassHierLevelsMap.put(lClassHierLevelsId, lClass);

			// reverse order of superclasses; need top down
			Collections.reverse(lSuperClassArr);
			
			// miscellaneous updates
			// get the class xpath (all super classes) for class
			// get the docSecType
			String lClassXpath = "";
			String lClassXPathDel = "";
			String lDocSecType = "TBD_lDocSecType";
			if (lClass.subClassOfTitle.compareTo("USER") != 0) {
				// for each class, iterate through the super class array, from top down
				for (Iterator<DOMClass> j = lSuperClassArr.iterator(); j.hasNext();) {
					lSuperClass = (DOMClass) j.next();
					if (lDocSecType.indexOf("TBD") == 0 && ! lSuperClass.isUSERClass && lSuperClass.subClassOfTitle != null)
	 					if (lSuperClass.subClassOfTitle.compareTo("USER") == 0) {
	 						lDocSecType = lSuperClass.title;
	 					}
					if (lSuperClass.isAbstract) continue;
					lClassXpath += lClassXPathDel + lSuperClass.title;
					lClassXPathDel = "/";
				}
				lClassXpath += lClassXPathDel + lClass.title;
				lClass.xPath = lClassXpath;
				lClass.docSecType = lDocSecType;
			} else {
				// handle the top level protege classes
				lClass.xPath = lClass.title;
				lClass.docSecType = lClass.title;
			}
		}
		// init the class hierarchy levels array (classes higher in in the tree are first) 
		lClassHierLevelsArr = new ArrayList <DOMClass> (lClassHierLevelsMap.values());

		if (DMDocument.debugFlag) System.out.println("debug getAttrAssocArr Done");
	}
	
	// 012 - remove URI Attribute
	public void removeURIAttribute () {
		// BEWARE of this code to remove the attribute %3ANAME or the protege :NAME meta attribute	
		// iterate through the classes and remove %3ANAME		
		for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();			
			int ind = 0, targInd = -1;
			for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (lProp.title.compareTo("%3ANAME") == 0) {
					targInd = ind;
					break;
				}
				ind++;
			}
			if (targInd > -1) {
				lClass.ownedAttrArr.remove(targInd);
			}
		} 
	}
	
	// 013 - get the inherited attributes and class associations from the parent class
	//       uses subClassOfIdentifier

	public void getInheritedAttrClassAssocFromParent () {
		for (Iterator<DOMClass> i = lClassHierLevelsArr.iterator(); i.hasNext();) {
			DOMClass lChildClass = (DOMClass) i.next();
			DOMClass lParentClass = lChildClass.subClassOf;
			if (lParentClass != null) {

				// initialize ownedAttrAssocNSTitleArr
				//   all parent attributes are inherited, both the parent's owned and inherited (parent had no duplicate attributes)
				//   if parent attribute is also a child owned attribute
				// 	 then the parent attribute has been overridden
				//			and the parent attribute is not inherited.
				for (Iterator <DOMProp> j = lChildClass.ownedAttrArr.iterator(); j.hasNext();) {
					DOMProp lChildProp = (DOMProp) j.next();
					if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMAttr) {
						DOMAttr lChildAttr = (DOMAttr) lChildProp.hasDOMObject;
						lChildClass.ownedAttrAssocNSTitleArr.add(lChildAttr.nsTitle);
						lChildClass.ownedAttrAssocNSTitleMap.put(lChildAttr.nsTitle, lChildAttr);
						lChildClass.ownedTestedAttrAssocNSTitleArr.add(lChildAttr.nsTitle);
						lChildClass.ownedPropNSTitleMap.put(lChildProp.nsTitle, lChildProp);
					}
				}
				// ditto for associations
				for (Iterator <DOMProp> j = lChildClass.ownedAssocArr.iterator(); j.hasNext();) {
					DOMProp lChildProp = (DOMProp) j.next();
					if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMClass) {
						DOMClass lMemberChildClass = (DOMClass) lChildProp.hasDOMObject;
						lChildClass.ownedAttrAssocNSTitleArr.add(lMemberChildClass.nsTitle);
						lChildClass.ownedPropNSTitleMap.put(lChildProp.nsTitle, lChildProp);
						lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberChildClass.nsTitle);
					}
				}				
				
				// get the inherited and restricted attributes
				// add each ParentClass owned attribute as a ChildClass inherited attribute
				getOwnedAttributesFromParent (lChildClass, lParentClass);
				getInheritedAttributesFromParent  (lChildClass, lParentClass);
				getOwnedClassAssociationsFromParent  (lChildClass, lParentClass);
				getInheritedClassAssociationsFromParent   (lChildClass, lParentClass);
				
				// set the super class
//				lChildClass.superClassHierArr = lSuperClass;
				
				// get the base class
				getBaseClassNew (lChildClass, lParentClass);
			}
		}
	}	

	// 013a - get the inherited attributes - owned attributes of parent class
	//	for each ParentClass
	//		for each owned attribute of the ParentClass 
	//			add the attribute as an inherited attribute of the ChildClass
	//     		(check is limited to the atribute's nsTitle (namespace + title)
	private void getOwnedAttributesFromParent  (DOMClass lChildClass, DOMClass lParentClass) {
		//	inherit all owned attributes of the parent class
		for (Iterator <DOMProp> i = lParentClass.ownedAttrArr.iterator(); i.hasNext();) {
			DOMProp lParentProp = (DOMProp) i.next();
			if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lParentAttr = (DOMAttr) lParentProp.hasDOMObject;
				
				// is parent nsTitle a child owned nsTitle
				if (lChildClass.ownedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {
					
					// the parent owned attribute is restricted
					DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lParentAttr.nsTitle);
					if (lChildProp != null) lChildProp.isRestrictedInSubclass = true;

					DOMAttr lChildAttr = lChildClass.ownedAttrAssocNSTitleMap.get(lParentAttr.nsTitle);
					if (lChildAttr != null) lChildAttr.isRestrictedInSubclass = true;
				}
				
				// has parent attribute been checked
				if (! lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {
					lChildClass.ownedTestedAttrAssocNSTitleArr.add(lParentAttr.nsTitle);
					
					// add the parent owned attribute to the inherited attribute array.
					lChildClass.inheritedAttrArr.add(lParentProp);
				}
			}
		}
	} 
	
	// 013b - get the inherited attributes - inherited attributes of parent class
	//	for each ParentClass
	//		for each inherited attribute of the ParentClass 
	//			add the attribute as an inherited attribute of the ChildClass
	//     		(check is limited to the atribute's nsTitle (namespace + title)
	private void getInheritedAttributesFromParent (DOMClass lChildClass, DOMClass lParentClass) {
		//	inherit all inherited attributes of the parent class
		for (Iterator <DOMProp> i = lParentClass.inheritedAttrArr.iterator(); i.hasNext();) {
			DOMProp lParentProp = (DOMProp) i.next();
			if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lParentAttr = (DOMAttr) lParentProp.hasDOMObject;
				
				// is parent nsTitle also a child owned nsTitle
				if (lChildClass.ownedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {

					// the parent inherited attribute is inherited; set isRestrictedInSubclass in DOMProp
					DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lParentAttr.nsTitle);
					if (lChildProp != null) lChildProp.isRestrictedInSubclass = true;
	
					// the parent inherited attribute is inherited
					DOMAttr lChildAttr = lChildClass.ownedAttrAssocNSTitleMap.get(lParentAttr.nsTitle);
					if (lChildAttr != null) lChildAttr.isRestrictedInSubclass = true;
				}
				
				// has parent attribute been checked
				if (! lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {
					lChildClass.ownedTestedAttrAssocNSTitleArr.add(lParentAttr.nsTitle);
					
					// add the parent owned attribute to the inherited attribute array.
					lChildClass.inheritedAttrArr.add(lParentProp);
				}
			}
		}
	} 
	
	// 013c - get the inherited associations - owned associations of parent class
	//	for each ParentClass
	//		for each owned attribute of the ParentClass 
	//			add the attribute as an inherited attribute of the ChildClass
	//     		(check is limited to the atribute's nsTitle (namespace + title)
	private void getOwnedClassAssociationsFromParent (DOMClass lChildClass, DOMClass lParentClass) {
		//	inherit all owned classes of the parent class
		for (Iterator <DOMProp> i = lParentClass.ownedAssocArr.iterator(); i.hasNext();) {
			DOMProp lParentProp = (DOMProp) i.next();
			if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMClass) {
				DOMClass lMemberParentClass = (DOMClass) lParentProp.hasDOMObject;
				
				// is parent nsTitle a child owned nsTitle
				if (lChildClass.ownedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
					
					// the parent owned attribute is restricted
					DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lMemberParentClass.nsTitle);
					if (lChildProp != null) lChildProp.isRestrictedInSubclass = true;
				}
				
				// has parent attribute been checked
				if (! lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
					lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberParentClass.nsTitle);
					
					// add the parent owned attribute to the inherited array.
					lChildClass.inheritedAssocArr.add(lParentProp);
				}
			}
		}
	} 
	
	// 013d - get the inherited associations - inherited associations of parent class
	//	for each ParentClass
	//		for each inherited attribute of the ParentClass 
	//			add the attribute as an inherited attribute of the ChildClass
	//     		(check is limited to the atribute's nsTitle (namespace + title)
	private void getInheritedClassAssociationsFromParent (DOMClass lChildClass, DOMClass lParentClass) {
		//	inherit all inherited attributes of the parent class
		for (Iterator <DOMProp> i = lParentClass.inheritedAssocArr.iterator(); i.hasNext();) {
			DOMProp lParentProp = (DOMProp) i.next();
			if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMClass) {
				DOMClass lMemberParentClass = (DOMClass) lParentProp.hasDOMObject;
				
				// is parent nsTitle a child owned nsTitle
				if (lChildClass.ownedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
					
					// the parent owned attribute is restricted
					DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lMemberParentClass.nsTitle);
					if (lChildProp != null) lChildProp.isRestrictedInSubclass = true;
				}
				
				// has parent attribute been checked
				if (! lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
					lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberParentClass.nsTitle);
					
					// add the parent owned attribute to the inherited array.
					lChildClass.inheritedAssocArr.add(lParentProp);
				}
			}
		}
	} 
	
	// 013e
	/**
	*  get the Base Class
	*/
	private void getBaseClassNew (DOMClass lChildClass, DOMClass lParentClass) {
		//	get the base class
		DOMClass lSuperClass = lParentClass.subClassOf;
		if (lSuperClass != null) {
			if (lSuperClass.rdfIdentifier.indexOf(protegeRootClassRdfId) != 0) {
				lChildClass.rootClass = lSuperClass.rdfIdentifier ;
				String fundStrucName = checkForFundamentalStructure (lSuperClass.title);
				if (fundStrucName != null) {
					lChildClass.baseClassName = fundStrucName;
				}
			}
		} else {
			lChildClass.rootClass = lParentClass.rdfIdentifier;
			String fundStrucName = checkForFundamentalStructure (lParentClass.title);
			if (fundStrucName != null) {
				lChildClass.baseClassName = fundStrucName;
			}
		}
		return;
	}
	
	
	// 013.f - For each class, get all owned attributes and associations from the class	
	public void getOwnedAttrAssocArr () {
		// get the class.ownedAttrAssocArr array - sorted (owned attributes and associations)
		// 7777 why is sorted important ???
		for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			
//			System.out.println("\ndebug getOwnedAttrAssocArr lClass.identifier:" + lClass.identifier);

			// sort all owned attributes and associations (AttrDefn)
			ArrayList <DOMProp> lPropArr = new ArrayList <DOMProp> (lClass.ownedAttrArr);
			lPropArr.addAll(lClass.ownedAssocArr);
			ArrayList <DOMProp> lSortedPropArr = getSortedPropArr (lPropArr);
			lClass.ownedAttrAssocArr.addAll(lSortedPropArr);
		}
	}
	
	// 013f.1 - sort attributes or associations using MOF properties 
	static public ArrayList <DOMProp> getSortedPropArr (ArrayList<DOMProp> lPropArr) {
		TreeMap<String, DOMProp> lSortPropMap = new TreeMap <String, DOMProp> ();
		for (Iterator<DOMProp> i = lPropArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			String sortId = lProp.classOrder + "_" + lProp.identifier;
			lSortPropMap.put(sortId, lProp);
		}
		ArrayList <DOMProp> lSortAttrArr = new ArrayList <DOMProp> (lSortPropMap.values());
		return lSortAttrArr;
	}
	
	// 013g - finalize the remaining attribute and association arrays
	//	allAttrAssocArr
	public void setRemainingAttributeAssociationArrays () {
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			
			//	get all owned attributes of the class
			lClass.allAttrAssocArr.addAll(lClass.ownedAttrArr);
			
			// add all inherited attributes of the class to the owned attributes
			lClass.allAttrAssocArr.addAll(lClass.inheritedAttrArr);
			
			// before adding the associates, get all enumerated owned and inherited attributes.
			ArrayList <String> allEnumAttrIdArr = new ArrayList <String> ();
			for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					if (lDOMAttr != null && lDOMAttr.isEnumerated) {
						if (! allEnumAttrIdArr.contains(lDOMAttr.identifier)) {
							allEnumAttrIdArr.add(lDOMAttr.identifier);
							lClass.allEnumAttrArr.add(lDOMAttr);
						}
					}
				}
			}
			
			// add all owned associations of the class
			lClass.allAttrAssocArr.addAll(lClass.ownedAssocArr);
			
			// add all inherited associations of the class
			lClass.allAttrAssocArr.addAll(lClass.inheritedAssocArr);
			
			// find all owned attributes and associations that are not inherited from any super class
			//      lClass.ownedAttrAssocNOArr
			ArrayList <String> lSuperOwnedAttrAssocArr = new ArrayList <String> ();
			// iterate through the super classes
			for (Iterator<DOMClass> j = lClass.superClassHierArr.iterator(); j.hasNext();) {
				DOMClass lDOMSuperClass = (DOMClass) j.next();

				// first get the identifiers of the owned attributes of the super class
				for (Iterator<DOMProp> k = lDOMSuperClass.ownedAttrArr.iterator(); k.hasNext();) {
					DOMProp lDOMProp = (DOMProp) k.next();
					if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
						DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
						lSuperOwnedAttrAssocArr.add(lDOMAttr.nameSpaceId + lDOMAttr.title);
					}
				}
				// second get the identifiers of the owned associations of the super class
				for (Iterator<DOMProp> k = lDOMSuperClass.ownedAssocArr.iterator(); k.hasNext();) {
					DOMProp lDOMProp = (DOMProp) k.next();
					if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
						DOMClass lDOMClass = (DOMClass) lDOMProp.hasDOMObject;
						lSuperOwnedAttrAssocArr.add(lDOMClass.nameSpaceId + lDOMClass.title);
					}
				}
			}
			// finally check the owned attributes and associations against the inherited owned attr and assoc from the super classes
			for (Iterator<DOMProp> j = lClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null) {
					if (lDOMProp.hasDOMObject instanceof DOMAttr) {
						DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
						if (! lSuperOwnedAttrAssocArr.contains(lDOMAttr.nameSpaceId + lDOMAttr.title)) {
							lClass.ownedAttrAssocNOArr.add(lDOMProp);
						}
					} else if (lDOMProp.hasDOMObject instanceof DOMClass) {
						DOMClass lDOMClass = (DOMClass) lDOMProp.hasDOMObject;
						if (! lSuperOwnedAttrAssocArr.contains(lDOMClass.nameSpaceId + lDOMClass.title)) {
							lClass.ownedAttrAssocNOArr.add(lDOMProp);
						}
					}
				}
			}
		}
	}

	
	// clone an array list
	static public ArrayList <DOMClass> clonePDSObjDefnArrayList (ArrayList <DOMClass> lArrayList) {
		ArrayList <DOMClass> newArrayList = new ArrayList <DOMClass> ();
		newArrayList.addAll(lArrayList);								
		return newArrayList;
	}	
	
	
	// 014 - Get the subClass array
	public void getSubClasses () {
		for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {			// get the target class
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isUSERClass) continue;
			ArrayList <DOMClass> lSubClassHierArr = new ArrayList <DOMClass> ();
			for (Iterator<DOMClass> j = masterDOMClassArr.iterator(); j.hasNext();) {		// get class to check
				DOMClass lCandidateClass = (DOMClass) j.next();	
				if (lClass.identifier.compareTo(lCandidateClass.identifier) == 0) continue;	//	ignore self
				if (lClass.identifier.compareTo(lCandidateClass.subClassOfIdentifier) == 0) lSubClassHierArr.add(lCandidateClass);
			}
			lClass.subClassHierArr = lSubClassHierArr;
		}
	}

	// 016 - general - master attribute fixup - uses class scan 
	//		- set the isUsedInClass flag
	public void setMasterAttrisUsedInClassFlag () {		
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			for (Iterator <DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					lDOMAttr.isUsedInClass = true;
				}
			}
			for (Iterator <DOMProp> j = lClass.inheritedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					lDOMAttr.isUsedInClass = true;
				}
//				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
//				if (lDOMAttr != null) {
//					lDOMAttr.isUsedInClass = true;
//				}
			}
			
			// set the xpath
			for (Iterator <DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					lDOMAttr.xPath = lClass.xPath + "/" + lDOMAttr.title;
				}
			}
			
//			for (Iterator <DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
//				DOMProp lDOMProp = (DOMProp) j.next();
//				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
//					DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
//					lChildClass.isUsedInClass = true;
//				}
//				DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
//				if (lChildClass != null) {
//					lChildClass.isUsedInClass = true;
//				}
//			}
//			for (Iterator <DOMProp> j = lClass.inheritedAssocArr.iterator(); j.hasNext();) {
//				DOMProp lDOMProp = (DOMProp) j.next();
//				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
//					DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
//					lChildClass.isUsedInClass = true;
//				}
//				DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
//				if (lChildClass != null) {
//					lChildClass.isUsedInClass = true;
//				}
//			}
		}
		return;
	}
	

	// 019 - general master attribute fixup
	// anchorString; sort_identifier; sorts valArr; get DEC
	// requires final attribute and class namespaces; final valArr;
	public void setMasterAttrGeneral () {
		String lBlanks = "                              ";
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			
			// set attributes anchor string
//			lAttr.attrAnchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.nameSpaceIdNC + "_"  + lAttr.title).toLowerCase();
			lAttr.anchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle + "_" + lAttr.nameSpaceIdNC + "_"  + lAttr.title).toLowerCase();

			// set attributes sort identifier
			int lLength = lAttr.title.length();
			if (lLength >= 30) lLength = 30;
			String lPaddedAttrTitle = lAttr.title + lBlanks.substring(0, 30 - lLength);
			
			lLength = lAttr.parentClassTitle.length();
			if (lLength >= 30) lLength = 30;
			String lPaddedClassTitle = lAttr.parentClassTitle + lBlanks.substring(0, 30 - lLength);
			lAttr.sort_identifier = lPaddedAttrTitle + "_" + lAttr.steward + "_" + lPaddedClassTitle + "_" + lAttr.classSteward;

			// sort attribute.valarr
			Collections.sort(lAttr.valArr);
		}	
		return;
	}
	
	// 020 - set up anchor strings for classes; requires final class namespace.
	public void setMasterClassAnchorString () {
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			lClass.anchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
		}	
		return;
	}	
	
	// 021 - get the permissible values from schematron statements (for example reference_type)
	public void getAttributePermValuesExtended () {
		// get an array of the schematron pattern statements
		ArrayList <DOMRule> lRuleArr = new ArrayList <DOMRule> (masterDOMRuleMap.values());				
		for (Iterator<DOMRule> i = lRuleArr.iterator(); i.hasNext();) {
			DOMRule lRule = (DOMRule) i.next();
			
//			System.out.println("\ndebug getAttributePermValuesExtended lRule.identifier:" + lRule.identifier);
			
			// get the affected attribute
			String lAttrId = DOMInfoModel.getAttrIdentifier (lRule.classNameSpaceNC, lRule.classTitle, lRule.attrNameSpaceNC, lRule.attrTitle);			
			DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(lAttrId);
			if (lAttr != null) {
					
			// for each rule get assert statement
				for (Iterator<DOMAssert> k = lRule.assertArr.iterator(); k.hasNext();) {
					DOMAssert lAssert = (DOMAssert) k.next();
					
					if (lAssert.assertType.compareTo("EVERY") == 0 || lAssert.assertType.compareTo("IF") == 0) {		
						// create a new permissible value entry extended for the attribute
						PermValueExtDefn lPermValueExt = new PermValueExtDefn (lAttr.title);
						lPermValueExt.xpath = lRule.xpath;
						lAttr.permValueExtArr.add(lPermValueExt);
												
						// create a new permissible value entry for each value
						for (Iterator<String> l = lAssert.testValArr.iterator(); l.hasNext();) {
							String lVal = (String) l.next();
							String lValueMeaning = getValueMeaning (lRule, lVal);
							PermValueDefn lPermValue = new PermValueDefn (lAssert.identifier, lVal, lValueMeaning);
							lPermValueExt.permValueExtArr.add(lPermValue);
						}
					}
				}
			}
		}	
		return;
	}	
	
	// 022 - set Registration Status
	public void setRegistrationStatus () {
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			// is it a value
			if (lDeprecatedDefn.value.compareTo("") != 0) {
				String lId = DOMInfoModel.getAttrIdentifier (lDeprecatedDefn.classNameSpaceIdNC, lDeprecatedDefn.className, lDeprecatedDefn.attrNameSpaceIdNC, lDeprecatedDefn.attrName);
				DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(lId);
				if (lAttr != null) {
					lAttr.hasRetiredValue = true;
					for (Iterator<PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
						PermValueDefn lPermValue = (PermValueDefn) j.next();
						if (lPermValue.value.compareTo(lDeprecatedDefn.value) == 0) {
							lPermValue.registrationStatus = "Retired";
						}
					}
					for (Iterator<DOMProp> j = lAttr.domPermValueArr.iterator(); j.hasNext();) {
						DOMProp lDOMProp = (DOMProp) j.next();
						if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
							DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
							if (lDOMPermValDefn.value.compareTo(lDeprecatedDefn.value) == 0) {
								lDOMPermValDefn.registrationStatus = "Retired";
							}
						}
					}
				}
			} else {
				// is it a class
				DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lDeprecatedDefn.identifier);
				if (lClass != null) {
					lClass.registrationStatus = "Retired";
					// update property
					if (lClass.hasDOMProp != null) lClass.hasDOMProp.registrationStatus = lClass.registrationStatus;
				} else {
					// is it an attribute
					DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(lDeprecatedDefn.identifier);
					if (lAttr != null) {
						lAttr.registrationStatus = "Retired";
						// update property
						if (lAttr.hasDOMProp != null) lAttr.hasDOMProp.registrationStatus = lAttr.registrationStatus;
					}
				}				
			}
		}
	}

	// 024 - get the data type definitions
		public void setMasterDataType2 () {
			
			// Find DOMClasses that define DataTypes
			TreeMap <String, DOMClass> lDOMDataTypeClassTitleMap = new TreeMap <String, DOMClass> ();
			
			// iterate through the classes and create a sorted array
			for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();
				if (lClass.title.indexOf("Data_Type") > -1 || lClass.rootClass.indexOf("Data_Type") > -1) {
					if (lClass.subClassHierArr.isEmpty()) {
						lDOMDataTypeClassTitleMap.put(lClass.title, lClass);
					}
				}
			}
			ArrayList <DOMClass> lDOMDataTypeClassArr = new ArrayList <DOMClass> (lDOMDataTypeClassTitleMap.values());	
			
			//Create DOMDataTypes
			for (Iterator<DOMClass> i = lDOMDataTypeClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();				
				DOMDataType lDataType = new DOMDataType ();
				lDataType.setRDFIdentifier(lClass.title);
				if (DOMInfoModel.masterDOMDataTypeMap.get(lDataType.rdfIdentifier) == null) {
					// the data type does not exist, add it
					
					lDataType.setDataTypeAttrs (lClass);
					DOMInfoModel.masterDOMDataTypeMap.put(lDataType.rdfIdentifier, lDataType);
					DOMInfoModel.masterDOMDataTypeTitleMap.put(lDataType.title, lDataType);
				}
			}
			DOMInfoModel.masterDOMDataTypeArr = new ArrayList <DOMDataType> (DOMInfoModel.masterDOMDataTypeMap.values());
		}
		
	// 025 - Set the DEC and CD for each attribute.
	public void GetMasterDECMaps () {		
		//set the DEC maps
		DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get("0001_NASA_PDS_1.pds.DD_Attribute_Full.pds.attribute_concept");

		if (lAttr == null) {
			System.out.println("***error*** system attribute - attribute_concept - MISSING");
			return;
		}
		if (lAttr.valArr == null) {
			System.out.println("***error*** - system attribute - attribute_concept - NO PERMISSIBLE VALUES");
			return;
		}
		if (lAttr.valArr.size() < 1) {
			System.out.println("***error*** - system attribute - attribute_concept - NO PERMISSIBLE VALUES");
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lDECTitle = (String) i.next();
			String lDECId = "DEC_" + lDECTitle;
			DOMInfoModel.cdID2CDTitleMap.put(lDECId, lDECTitle);
			DOMInfoModel.cdTitle2CDIDMap.put(lDECTitle, lDECId);
		}
	}

	// 026 - set up master conceptual domain array
	public void GetMasterCDMaps () {		
		//set the CD maps
		DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get("0001_NASA_PDS_1.pds.DD_Value_Domain_Full.pds.conceptual_domain");

		if (lAttr == null) {
			System.out.println("***error*** system attribute - conceptual_domain - MISSING");
			return;
		}
		if (lAttr.valArr == null) {
			System.out.println("***error*** - system attribute - conceptual_domain - NO PERMISSIBLE VALUES");
			return;
		}
		if (lAttr.valArr.size() < 1) {
			System.out.println("***error*** - system attribute - conceptual_domain - NO PERMISSIBLE VALUES");
			return;
		}
		for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
			String lCDTitle = (String) i.next();
			String lCDId = "CD_" + lCDTitle;
			DOMInfoModel.cdID2CDTitleMap.put(lCDId, lCDTitle);
			DOMInfoModel.cdTitle2CDIDMap.put(lCDTitle, lCDId);
		}
	}

	
	// 027 - Set Master Attribute XML Base Data Type From the Data Type	
	public void SetMasterAttrXMLBaseDataTypeFromDataType () {
		// for each PDS4 attribute set the xml_schema_base_type from the data type
 
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isPDS4) {
				DOMDataType lDataType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lAttr.valueType);
				if (lDataType != null) {
					lAttr.xmlBaseDataType = lDataType.xml_schema_base_type;
				} else {
					System.out.println(">>error   - SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing - lAttr.protValType:" + lAttr.protValType);
				}
			}
		}
	}			
	
	// 029.1 - find attributes that have no overrides; compare to data type
	public void sethasAttributeOverride1 (ArrayList <DOMAttr> lMasterDOMAttrArr) {
		for (Iterator<DOMAttr> i = lMasterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isAttribute) {
//				DOMDataType lValueType = DOMInfoModel.masterDOMDataTypeMap.get(lAttr.valueType);
				DOMDataType lValueType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lAttr.valueType);
				if (lValueType == null) {
					System.out.println(">>error   - Could not find a value type for this attribute while checking for attribute overrides - Name:" + lAttr.title + "   Value Type:" + lAttr.valueType);
				} else {
					boolean hasOverride = false;
					if (!((lAttr.minimum_value.indexOf("TBD") == 0) || (lAttr.minimum_value.compareTo(lValueType.minimum_value) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.maximum_value.indexOf("TBD") == 0) || (lAttr.maximum_value.compareTo(lValueType.maximum_value) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.minimum_characters.indexOf("TBD") == 0) || (lAttr.minimum_characters.compareTo(lValueType.minimum_characters) == 0))) {
						hasOverride = true;
					}
					if (!((lAttr.maximum_characters.indexOf("TBD") == 0) || (lAttr.maximum_characters.compareTo(lValueType.maximum_characters) == 0))) {
						hasOverride = true;
					}
					if (!(lAttr.unit_of_measure_type.indexOf("TBD") == 0)) {
						hasOverride = true;
					}					
					if (lAttr.pattern.indexOf("TBD") != 0) {
						hasOverride = true;
					}					
					if (hasOverride) {
						lAttr.hasAttributeOverride = true;
					} else {
						// if the attribute does not have an override, then it gets to use it own name in the schema
						lAttr.XMLSchemaName = lAttr.title;
					}
				}
			}
		}
		return;
	}	

	// 029.1 - for attributes with overrides, the first gets to use its own name in the schema
	//         the others are either equivalent or are forced to use className_attributeName
	public void sethasAttributeOverride2 (ArrayList <DOMAttr> lMasterDOMAttrArr) {
		TreeMap <String, DOMAttr> lSortAttrMap = new TreeMap <String, DOMAttr> ();
		TreeMap <String, ArrayList<DOMAttr>> lTitleAttrsMap = new TreeMap <String, ArrayList<DOMAttr>> ();
		
		// *** Need to add steward to partition attributes ***
		
		// put attributes in sorted order; attr.title_class.title
		for (Iterator<DOMAttr> i = lMasterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if ((lAttr.XMLSchemaName.indexOf("TBD") == 0) && lAttr.isAttribute) {
				// all remaining attributes have overrides, i.e. different constraints than those in the data type
				lSortAttrMap.put(lAttr.title + "_" + lAttr.parentClassTitle + lAttr.nameSpaceId, lAttr);
			}
		}

		// for each attribute title, get the array of all attributes with that title
		ArrayList <String> lTitleArr = new ArrayList <String> ();
		ArrayList <DOMAttr> lSortAttrArr = new ArrayList <DOMAttr> (lSortAttrMap.values());
		ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> ();
		for (Iterator<DOMAttr> i = lSortAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if(lTitleArr.contains(lAttr.title)) {
				lAttrArr.add(lAttr);
			} else {
				lTitleArr.add(lAttr.title);
				lAttrArr = new ArrayList <DOMAttr> ();
				lAttrArr.add(lAttr);
				lTitleAttrsMap.put(lAttr.title, lAttrArr);
			}
		}

		// iterate through the title/attribute array map
		Set <String> set1 = lTitleAttrsMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lTitle = (String) iter1.next();
			ArrayList <DOMAttr> lAttrArr2 = (ArrayList <DOMAttr>) lTitleAttrsMap.get(lTitle);		

			// iterate through this attribute array 
			boolean isFirst = true;
			for (Iterator<DOMAttr> i = lAttrArr2.iterator(); i.hasNext();) {
				DOMAttr lAttr1 = (DOMAttr) i.next();			

				// if this is  the first attribute, it gets to use its own name
				if (isFirst) {
					lAttr1.XMLSchemaName = lAttr1.title;
					isFirst = false;					
				} else {

					// nested iteration through this attribute array to find the duplicates
					boolean isEquivalentAll = true;
					for (Iterator<DOMAttr> j = lAttrArr2.iterator(); j.hasNext();) {
						DOMAttr lAttr2 = (DOMAttr) j.next();

						// omit checking an attribute against itself
						if (lAttr1.rdfIdentifier.compareTo(lAttr2.rdfIdentifier) == 0) { continue; }
						
						// set one XMLSchemaName at a time 
						if (lAttr2.XMLSchemaName.indexOf("TBD") == 0) {
							break;
						}
						boolean isEquivalent = true;
						if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
							isEquivalent = false;
							System.out.println(">>error   - hasAttributeOverride2 - valueType is not equivalent - attribute identifier:" + lAttr2.identifier);
						}
						if (lAttr1.minimum_value.compareTo(lAttr2.minimum_value) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.maximum_value.compareTo(lAttr2.maximum_value) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.minimum_characters.compareTo(lAttr2.minimum_characters) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.maximum_characters.compareTo(lAttr2.maximum_characters) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.pattern.compareTo(lAttr2.pattern) != 0) {
							isEquivalent = false;
						}
						if (lAttr1.unit_of_measure_type.compareTo(lAttr2.unit_of_measure_type) != 0) {
							isEquivalent = false;
						}
						if (isEquivalent) {
							lAttr1.XMLSchemaName = lAttr2.XMLSchemaName;
							isEquivalentAll = true;
							break;
						} else {
							System.out.println(">>warning - sethasAttributeOverride - attributes are not equivalent:" + lAttr1.identifier + " - " + lAttr2.identifier);
						}
						isEquivalentAll = isEquivalentAll && isEquivalent;
					}
					if (! isEquivalentAll) {
						System.out.println(">>error   - sethasAttributeOverride - attribute is not equivalent - Setting unique name - attribute identifier:" + lAttr1.identifier);
						lAttr1.XMLSchemaName = lAttr1.parentClassTitle + "_" + lAttr1.title;
					}
				}
			}
		}
		return;
	}	
	
	// 030 - get the hasDOMObject from the valArr for each association (AttrDefn)
	public void getValClassArr () {		
		// for each PDS4 attribute set the meta attribute from the data type
		for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			if (lProp.isAttribute) continue;		// ignore, not a class
			if (lProp.valArr == null || lProp.valArr.isEmpty()) continue;			
			for (Iterator<String> j = lProp.valArr.iterator(); j.hasNext();) {
				String lTitle = (String) j.next();
				String lClassMemberIdentifier = DOMInfoModel.getClassIdentifier(lProp.nameSpaceIdNC, lTitle);
				DOMClass lClassMember = (DOMClass) DOMInfoModel.masterDOMClassIdMap.get(lClassMemberIdentifier);
				if (lClassMember != null) {				
					lProp.hasDOMObject = (DOMClass) lClassMember;
				} else {
					DOMClass firstClassFound = null;
					for (Iterator<DOMClass> k = DOMInfoModel.masterDOMClassArr.iterator(); k.hasNext();) {
						lClassMember = (DOMClass) k.next();
						if (lClassMember.title.compareTo(lTitle) == 0) {
							if (firstClassFound == null) {
								firstClassFound = lClassMember;
								System.out.println(">>warning - get class using attribute value array - lAttr.identifier:" + lProp.identifier + " - using first found - lClassMember.identifier:" + lClassMember.identifier);
							} else {
								System.out.println(">>warning - get class using attribute value array - lAttr.identifier:" + lProp.identifier + " - also found - lClassMember.identifier:" + lClassMember.identifier);
							}
						}
					}					
					if (firstClassFound != null) lProp.hasDOMObject = (DOMClass) firstClassFound;
				}
			}
		}
	}
	
	// 031 - Get the attribute's CD and DEC values
	//       get all CD and DEC values for each attribute; used to print a CD or DEC 11179 definition. i.e. all DECS for a CD.
	public void getCDDECIndexes () {
		// get Attribute for the VD info
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				
				// update the CD (dataConcept) Index
				String sortKey = lAttr.dataConcept;
				DOMIndexDefn lIndex = cdDOMAttrMap.get(sortKey);
				if (lIndex == null) {
					lIndex = new DOMIndexDefn(sortKey);
					cdDOMAttrMap.put(sortKey, lIndex);
				}
				lIndex.identifier1Map.put(lAttr.identifier, lAttr);
				if (! lIndex.identifier2Arr.contains(lAttr.classConcept)) {
					lIndex.identifier2Arr.add(lAttr.classConcept);
				}

				// update the DEC (classConcept) Index
				sortKey = lAttr.classConcept;
				lIndex = decDOMAttrMap.get(sortKey);
				if (lIndex == null) {
					lIndex = new DOMIndexDefn(sortKey);
					decDOMAttrMap.put(sortKey, lIndex);
				}
				lIndex.identifier1Map.put(lAttr.identifier, lAttr);
				if (! lIndex.identifier2Arr.contains(lAttr.dataConcept)) {
					lIndex.identifier2Arr.add(lAttr.dataConcept);
				}
			}
		}
		return;
	}	
	
	// 032 - Get the attribute's DEC values
	// null
	
	// 033 - get the USER attributes (not owned attributes)
	//	Get User Class Attributes Id Map (not owned)	
	public void getUserSingletonClassAttrIdMap () {
		for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (! lAttr.isAttribute) continue;
			if (lAttr.isUsedInClass) continue;
			if (lAttr.title.compareTo("%3ANAME") == 0) continue;
			DOMInfoModel.userSingletonDOMClassAttrIdMap.put(lAttr.identifier, lAttr);	
		}
		return;
	}
	
	// 034 - get the LDDToolSingletonClass, the class for LDD singleton attributes (Discipline or Mission)
	// currently defined in GetDOMModel
	
	// 038 - set the class version identifiers (stop gap until class are stored in OWL	
	public void setClassVersionIds () {
		Set <String> set = DMDocument.classVersionId.keySet();
		Iterator <String> iter = set.iterator();
		while(iter.hasNext()) {
			String lClassName = (String) iter.next();
			String lClassId = DOMInfoModel.getClassIdentifier(DMDocument.masterNameSpaceIdNCLC, lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {		
				String lClassVersionId = DMDocument.classVersionId.get(lClassName);;		
				lClass.versionId = lClassVersionId;
			}
		}
	}
	
	// 039 - set exposed flag
	// currently defined in GetDOMModel
	
// ===============================================================================================	
	
	// get the value meaning
	public String getValueMeaning (DOMRule lRule, String lVal) {
		String lId = lRule.xpath + "." + lRule.attrTitle + "." + lVal;
		PermValueDefn lPermVal = GetValueMeanings.masterValueMeaningKeyMap.get(lId);
		if (lPermVal != null) {
			return lPermVal.value_meaning;
		}
		return "TBD_value_meaning";
	}

//	CheckDataTypes	
	public void CheckDataTypes () {
		System.out.println("\ndebug CheckDataTypes");
		TreeMap <String, DOMAttr> lTreeMap = new TreeMap <String, DOMAttr>();
		for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			String rId = lAttr.title + "_" + lAttr.rdfIdentifier;
			lTreeMap.put(rId, lAttr);
		}
		Collection <DOMAttr> values = lTreeMap.values();		
		ArrayList <DOMAttr> lAttrArr = (new ArrayList <DOMAttr> ( values ));
		String pTitle = "", pDataType = "", pRDFId = "";
		for (Iterator<DOMAttr> j = lAttrArr.iterator(); j.hasNext();) {
			DOMAttr lAttr = (DOMAttr) j.next();
			System.out.println("\ndebug CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
			if (lAttr.title.compareTo(pTitle) == 0) {
				if (lAttr.valueType.compareTo(pDataType) == 0) {
					continue;
				} else {
					System.out.println("debug CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
					System.out.println("debug CheckDataTypes data types not equal - pRDFId:" + pRDFId);
					System.out.println("debug CheckDataTypes data types not equal - pDataType:" + pDataType);
					System.out.println("debug CheckDataTypes data types not equal - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
					System.out.println("debug CheckDataTypes data types not equal - lAttr.valueType:" + lAttr.valueType);
				}
			} else {
				pTitle = lAttr.title;
				pDataType = lAttr.valueType;
				pRDFId = lAttr.rdfIdentifier;
			}
		}
	}

	/**********************************************************************************************************
		Routines for Finalizing the Ontology
	***********************************************************************************************************/ 
			
			// The hierarchy of classes is then processed downward
			// one level at a time, where each class at the level again processes back
			// up the hierarchy, this time capturing inherited attributes.

		
		/**
		*  Fixup name spaces and base class name - final cleanup
		*/
		public void fixNameSpaces () {
	
	//		iterate through the classes and get the namespace
			for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();
				if (lClass.baseClassName.indexOf("TBD") == 0) {
					lClass.baseClassName = "none";
				}
			}
			return;
		}	

		/**
		*  Check whether class is an extension
		*/
		static public boolean isExtendedClass (PDSObjDefn lClass, PDSObjDefn lSuperClass) {
			if (lSuperClass == null) {
				return false;
			}

			//	check if all owned and inherited attributes are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lSuperClass.ownedAttrNSTitle.contains(lAttr.nsTitle) || lSuperClass.inheritedAttrNSTitle.contains(lAttr.nsTitle))) {
					return true;
				}
			}
			
			//	check if all owned and inherited associations are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lSuperClass.ownedAssocNSTitle.contains(lAttr.nsTitle) || lSuperClass.inheritedAssocNSTitle.contains(lAttr.nsTitle))) {
					return true;
				}
			}
			return false;
		}
				
		static public boolean isRestrictedClass (PDSObjDefn lClass, PDSObjDefn lSuperClass) {
			//	check if any owned attributes is in the super class (owned or inherited)
			if (lSuperClass == null) {
				return false;
			}
			for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (isRestrictedAttribute (true, lAttr, (getPossibleRestrictedAttribute (lAttr, lSuperClass)))) {
					return true;
				}
			}
			//	check if all owned and inherited associations are in the super class
			for (Iterator<AttrDefn> i = lClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (isRestrictedAttribute (false, lAttr, (getPossibleRestrictedAssociation (lAttr, lSuperClass)))) {
					return true;
				}
			}
			return false;
		}
		
		static public AttrDefn getPossibleRestrictedAttribute (AttrDefn lAttr, PDSObjDefn lSuperClass) {
			//	find the attribute by title in the super class.

			for (Iterator<AttrDefn> i = lSuperClass.ownedAttribute.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			for (Iterator<AttrDefn> i = lSuperClass.inheritedAttribute.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			return null;
		}
		
		static public AttrDefn getPossibleRestrictedAssociation (AttrDefn lAttr, PDSObjDefn lSuperClass) {
			//	find the attribute by title in the super class.

			for (Iterator<AttrDefn> i = lSuperClass.ownedAssociation.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			for (Iterator<AttrDefn> i = lSuperClass.inheritedAssociation.iterator(); i.hasNext();) {
				AttrDefn lSuperAttr = (AttrDefn) i.next();
				if ((lSuperAttr.title.compareTo(lAttr.title) == 0)) {
					return lSuperAttr;
				}
			}
			return null;
		}
		
		/**
		*  Check whether attribute is restricted
		*/
		static public boolean isRestrictedAttribute (Boolean isAttribute, AttrDefn lAttr, AttrDefn lSuperAttr) {
			// check if the attribute has different meta-attributes
			// for the xml schemas, having a different enumerated list does not count as being different
			
			if (lSuperAttr == null) {
				return false;
			}
			
//			System.out.println("\ndebug isRestrictedAttribute lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
//			System.out.println("      isRestrictedAttribute lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
			
			if (lAttr.valueType.compareTo(lSuperAttr.valueType) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.valueType:" + lSuperAttr.valueType + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.cardMin.compareTo(lSuperAttr.cardMin) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.cardMin:" + lSuperAttr.cardMin + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.cardMax.compareTo(lSuperAttr.cardMax) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.cardMax:" + lSuperAttr.cardMax + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.description.compareTo(lSuperAttr.description) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.description:" + lSuperAttr.description + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.minimum_characters.compareTo(lSuperAttr.minimum_characters) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.minimum_characters:" + lSuperAttr.minimum_characters + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.maximum_characters.compareTo(lSuperAttr.maximum_characters) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.maximum_characters:" + lSuperAttr.maximum_characters + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.minimum_value.compareTo(lSuperAttr.minimum_value) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.minimum_value:" + lSuperAttr.minimum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.maximum_value.compareTo(lSuperAttr.maximum_value) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.maximum_value:" + lSuperAttr.maximum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.attrNameSpaceIdNC.compareTo(lSuperAttr.attrNameSpaceIdNC) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.attrNameSpaceIdNC:" + lSuperAttr.attrNameSpaceIdNC + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.pattern.compareTo(lSuperAttr.pattern) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.pattern:" + lSuperAttr.pattern + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.default_unit_id.compareTo(lSuperAttr.default_unit_id) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.default_unit_id:" + lSuperAttr.default_unit_id + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (lAttr.format.compareTo(lSuperAttr.format) != 0) {
				System.out.println(">>warning - isRestrictedAttribute lSuperAttr.format:" + lSuperAttr.format + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
				return true;
			}
			if (! isAttribute) { // if association we need  to check the standard values.
				if (lAttr.valArr.size() != lSuperAttr.valArr.size()) {
					System.out.println(">>warning - isRestrictedAttribute lAttr.valArr.size():" + lAttr.valArr.size() + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
					return true;
				}
				for (Iterator<String> i = lAttr.valArr.iterator(); i.hasNext();) {
					String lVal = (String) i.next();
					if (! lSuperAttr.valArr.contains(lVal)) {
						System.out.println(">>warning - isRestrictedAttribute lAttr.lVal" + lVal + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
						return true;
					}
				}
			}
			return false;
		}

		public void dumpClassVersionIds () {
			//	set the class version identifiers
			 ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassIdMap.values());
			for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();
				System.out.println("debug dumpClassVersionIds  lClass.identifier:" + lClass.identifier);		
				System.out.println("debug dumpClassVersionIds  lClass.versionId:" + lClass.versionId);		
			}
		}
		
	/**********************************************************************************************************
		miscellaneous routines
	***********************************************************************************************************/
		
		/**
		*  Check for attributes with the same ns:title but different constraints.
		*  (attributes with the same ns:title can be used in two or more classes
		*   this check ensures that they all have the same values for the meta-attributes
		*   Description alone is allowed to be different
		*   *** Different constraints would cause multiple simpleType definitions ***
		*/		
		static public void checkSameNameOverRide () {
			System.out.println(">>info    - Checking for attribute consistency - checkSameNameOverRide");
		
			// sort the attributes
			TreeMap <String, DOMAttr> lAttrMap = new TreeMap <String, DOMAttr> ();
			for (Iterator<DOMAttr> i = masterDOMAttrArr.iterator(); i.hasNext();) {
				DOMAttr lAttr = (DOMAttr) i.next();	
				// *** temporary fix - needs to be thought out ***
				if (lAttr.isFromLDD) continue;
				lAttrMap.put(lAttr.title + "-" + lAttr.identifier, lAttr);
			}
			ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> (lAttrMap.values());
			
			// check the attributes against all other attributes
			for (int i = 0; i < lAttrArr.size(); i++) {
				DOMAttr lAttr1 = lAttrArr.get(i);
				if (! lAttr1.isAttribute) continue;
				for (int j = i + 1; j < lAttrArr.size(); j++) {
					DOMAttr lAttr2 = lAttrArr.get(j);
					if (! lAttr2.isAttribute) continue;
					
					// only check attributes with the same titles
					if (! (lAttr1.nsTitle.compareTo(lAttr2.nsTitle) == 0)) continue;
					
					// don't check the meta-attributes
					if (lAttr1.title.compareTo("minimum_characters") == 0) continue;
					if (lAttr1.title.compareTo("maximum_characters") == 0) continue;
					if (lAttr1.title.compareTo("minimum_value") == 0) continue;
					if (lAttr1.title.compareTo("maximum_value") == 0) continue;
					if (lAttr1.title.compareTo("xml_schema_base_type") == 0) continue;
					if (lAttr1.title.compareTo("formation_rule") == 0) continue;
					if (lAttr1.title.compareTo("character_constraint") == 0) continue;
					if (lAttr1.title.compareTo("value") == 0) continue;
					if (lAttr1.title.compareTo("unit_id") == 0) continue;
					if (lAttr1.title.compareTo("pattern") == 0) continue;
					
					// check what is left
					checkForOverRideDetail (lAttr1, lAttr2);
				}
			}	
		}
		
		static public void checkForOverRideDetail (DOMAttr lAttr1, DOMAttr lAttr2) {
			boolean isFound = false;
			
			if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.valueType:" + lAttr1.valueType + "   lAttr2.valueType:" + lAttr2.valueType);
			}
			if (lAttr1.minimum_characters.compareTo(lAttr2.minimum_characters) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.minimum_characters:" + lAttr1.minimum_characters + "   lAttr2.minimum_characters:" + lAttr2.minimum_characters);
			}
			if (lAttr1.maximum_characters.compareTo(lAttr2.maximum_characters) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.maximum_characters:" + lAttr1.maximum_characters + "   lAttr2.maximum_characters:" + lAttr2.maximum_characters);
			}
			if (lAttr1.minimum_value.compareTo(lAttr2.minimum_value) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.minimum_value:" + lAttr1.minimum_value + "   lAttr2.minimum_value:" + lAttr2.minimum_value);
			}
			if (lAttr1.maximum_value.compareTo(lAttr2.maximum_value) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.maximum_value:" + lAttr1.maximum_value + "   lAttr2.maximum_value:" + lAttr2.maximum_value);
			}
			if (lAttr1.pattern.compareTo(lAttr2.pattern) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.pattern:" + lAttr1.pattern + "   lAttr2.pattern:" + lAttr2.pattern);
			}
			if (lAttr1.default_unit_id.compareTo(lAttr2.default_unit_id) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.default_unit_id:" + lAttr1.default_unit_id + "   lAttr2.default_unit_id:" + lAttr2.default_unit_id);
			}
			if (lAttr1.format.compareTo(lAttr2.format) != 0) {
				isFound = true;
				if (DMDocument.debugFlag) System.out.println(">>warning - checkForOverRideDetail lAttr1.format:" + lAttr1.format + "   lAttr2.format:" + lAttr2.format);
			}
			if (isFound) {
				if (DMDocument.debugFlag) System.out.println("debug checkForOverRideDetail lAttr1.identifier:" + lAttr1.identifier);
				if (DMDocument.debugFlag) System.out.println("debug checkForOverRideDetail lAttr2.identifier:" + lAttr2.identifier);
				if (DMDocument.debugFlag) System.out.println(" ");
			}
			return;
		}		
	}
