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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

class MasterDOMInfoModel extends DOMInfoModel {
  static String attrNameClassWord;

  TreeMap<String, DOMClass> lClassHierLevelsMap = new TreeMap<>();
  ArrayList<DOMClass> lClassHierLevelsArr = new ArrayList<>();

  public MasterDOMInfoModel() {

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
  public void setAttrParentClass(boolean forLDD) {
    // for each PDS4 attribute set the attrParentClass
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if ((forLDD && !lAttr.isFromLDD) || (!forLDD && lAttr.isFromLDD)) {
        continue;
      }
      String lClassId =
          DOMInfoModel.getClassIdentifier(lAttr.classNameSpaceIdNC, lAttr.parentClassTitle);
      DOMClass lParentClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
      if (lParentClass != null) {
        lAttr.attrParentClass = lParentClass;
      } else {
        boolean classNotFound = true;
        for (Iterator<DOMClass> k = DOMInfoModel.masterDOMClassArr.iterator(); k.hasNext();) {
          lParentClass = k.next();
          if (lParentClass.title.compareTo(lAttr.parentClassTitle) == 0) {
            if (classNotFound) {
              classNotFound = false;
              lAttr.attrParentClass = lParentClass;
              DMDocument.registerMessage("1>warning "
                  + "set attributes parent class - lAttr.identifier:" + lAttr.identifier
                  + " - using first found - lClassMember.identifier:" + lParentClass.identifier);
            } else {
              DMDocument.registerMessage("1>warning "
                  + "set attributes parent class - lAttr.identifier:" + lAttr.identifier
                  + " - also found - lClassMember.identifier:" + lParentClass.identifier);
            }
          }
        }
        if (classNotFound) {
          lAttr.attrParentClass = DOMInfoModel.masterDOMUserClass;
          DMDocument.registerMessage(
              "1>warning " + "set attributes parent class - lAttr.identifier:" + lAttr.identifier
                  + " - parent class not found, using USER - lClassMember.identifier:"
                  + lParentClass.identifier);
        }
      }
    }
  }

  // 007 - Get USER Class Attributes Id Map
  // The attributes are not cloned, they are simply added to the DOMInfoModel.userClassAttrIdMap
  // with the identifier for the map using "attrNameSpaceIdNC.USER" + "attrNameSpaceIdNC.title"
  // e.g. pds.USER.pds.comment or disp.USER.disp.name
  // There is a many-to-one map so only one attribute survives, however all have same definition
  public void getUserClassAttrIdMap() {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (!lClass.isMasterClass || (lClass.title.indexOf("PDS3") > -1)) {
        continue; // kludge until boolean is set up
      }
      for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();

        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;

          // the namespace of the USER class for any attribute is the same as the namespace of the
          // attribute
          String lUserAttrIdentifier =
              DOMInfoModel.getAttrIdentifier(DMDocument.masterUserClassNamespaceIdNC,
                  DMDocument.masterUserClassName, lDOMAttr.nameSpaceIdNC, lDOMAttr.title);
          DOMInfoModel.userDOMClassAttrIdMap.put(lUserAttrIdentifier, lDOMAttr);
        }
      }
    }
    return;
  }

  // 010 - set the master unitOfMeasure
  public void setMasterUnitOfMeasure() {
    // iterate through the classes and create a sorted array
    // *** cool code
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isUnitOfMeasure) {
        if (lClass.role.compareTo("concrete") == 0) {
          DOMUnit lUnit = DOMInfoModel.masterDOMUnitMap.get(lClass.title);
          if (lUnit == null) {
            // the unit does not exist, add it
            lUnit = new DOMUnit();
            lUnit.identifier = lClass.title;
            lUnit.createDOMUnitSingletons(lClass.title, lClass);
            DOMInfoModel.masterDOMUnitMap.put(lUnit.title, lUnit);
            DOMInfoModel.masterDOMUnitTitleMap.put(lUnit.title, lUnit);
            // for each attribute of the class
            for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
              DOMProp lDOMProp = j.next();
              if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
                DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
                if (lDOMAttr != null) {
                  // set the default_unit_id
                  if (lDOMAttr.title.compareTo("specified_unit_id") == 0) {
                    String lVal = DOMInfoModel.getSingletonValueUpdate(lDOMAttr.valArr,
                        lUnit.default_unit_id);
                    if (lVal != null) {
                      lUnit.default_unit_id = lVal;
                    }
                  }
                  // set the unit_id
                  if (lDOMAttr.title.compareTo("unit_id") == 0) {
                    ArrayList<String> lValArr = DOMInfoModel.getMultipleValue(lDOMAttr.valArr);
                    if (lValArr != null) {
                      for (Iterator<String> l = lValArr.iterator(); l.hasNext();) {
                        String lVal = l.next();
                        lUnit.unit_id.add(lVal);
                      }
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
    DOMInfoModel.masterDOMUnitArr = new ArrayList<>(DOMInfoModel.masterDOMUnitMap.values());
  }

  // 011.1 - get the subClassOf (superClass) identifier and instances for each class, using the
  // title
  public void getSubClassOf() {
    // iterate through the classes and get the subClassOf
    for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.isUSERClass) {
        continue;
      }
      DOMClass lSupClass = masterDOMClassTitleMap.get(lClass.subClassOfTitle);
      if (lSupClass != null) {
        lClass.subClassOf = lSupClass;
        lClass.subClassOfTitle = lSupClass.title;
        lClass.subClassOfIdentifier = lSupClass.identifier;
      } else {
        DMDocument.registerMessage("1>error "
            + "missing superClass in master while trying to set subClassOf - lClass.identifier:"
            + lClass.identifier + " -  lClass.subClassOfTitle:" + lClass.subClassOfTitle);
      }
    }
    return;
  }

  // 011.2 - get each class's super class hierarchy
  public void getSuperClassHierArr() {
    // for each class get its superclasses, bottom up
    for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      // array for super classes of this class
      ArrayList<DOMClass> lSuperClassArr = new ArrayList<>();
      if (lClass.isUSERClass) {
        continue;
      }
      DOMClass lSuperClass = lClass.subClassOf;

      // recurse up the subClassOf chain, getting the super class array
      while (lSuperClass != null && !lSuperClass.isUSERClass) {
        if (lSuperClassArr.contains(lSuperClass)) {
          DMDocument.registerMessage("1>error "
              + "Found cycle in superclass hierarchy - SuperClass.title:" + lSuperClass.title);
          break;
        }
        // get next superclass
        lSuperClassArr.add(lSuperClass);

        lSuperClass = lSuperClass.subClassOf;
      }

      // save the super class hierarchy
      lClass.superClassHierArr = lSuperClassArr;

      // get the class hierarchy level
      int lClassHierLevels = 1000 + lSuperClassArr.size();
      String lClassHierLevelsId = Integer.toString(lClassHierLevels) + "-" + lClass.identifier;
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
          lSuperClass = j.next();
          if (lDocSecType.indexOf("TBD") == 0 && !lSuperClass.isUSERClass
              && lSuperClass.subClassOfTitle != null) {
            if (lSuperClass.subClassOfTitle.compareTo("USER") == 0) {
              lDocSecType = lSuperClass.title;
            }
          }
          if (lSuperClass.isAbstract) {
            continue;
          }
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
    lClassHierLevelsArr = new ArrayList<>(lClassHierLevelsMap.values());

    DMDocument.registerMessage("0>info " + "getAttrAssocArr Done");
  }

  // 012 - remove URI Attribute
  public void removeURIAttribute() {
    // iterate through the attributes and make %3ANAME inactive
    for (DOMAttr lDOMAttr : masterDOMAttrArr) {
      if (lDOMAttr.title.compareTo("%3ANAME") == 0) {
        lDOMAttr.isInactive = true;
      }
    }
  }

  // 013 - get the inherited attributes and class associations from the parent class
  // uses subClassOfIdentifier

  public void getInheritedAttrClassAssocFromParent() {
    for (Iterator<DOMClass> i = lClassHierLevelsArr.iterator(); i.hasNext();) {
      DOMClass lChildClass = i.next();
      DOMClass lParentClass = lChildClass.subClassOf;
      if (lParentClass != null) {

        // initialize ownedAttrAssocNSTitleArr
        // all parent attributes are inherited, both the parent's owned and inherited (parent had no
        // duplicate attributes)
        // if parent attribute is also a child owned attribute
        // then the parent attribute has been overridden
        // and the parent attribute is not inherited.
        for (Iterator<DOMProp> j = lChildClass.ownedAttrArr.iterator(); j.hasNext();) {
          DOMProp lChildProp = j.next();
          if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMAttr) {
            DOMAttr lChildAttr = (DOMAttr) lChildProp.hasDOMObject;
            lChildClass.ownedAttrAssocNSTitleArr.add(lChildAttr.nsTitle);
            lChildClass.ownedAttrAssocNSTitleMap.put(lChildAttr.nsTitle, lChildAttr);
            lChildClass.ownedTestedAttrAssocNSTitleArr.add(lChildAttr.nsTitle);
            lChildClass.ownedPropNSTitleMap.put(lChildAttr.nsTitle, lChildProp);
          }
        }
        // ditto for associations
        for (Iterator<DOMProp> j = lChildClass.ownedAssocArr.iterator(); j.hasNext();) {
          DOMProp lChildProp = j.next();
          if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMClass) {
            DOMClass lMemberChildClass = (DOMClass) lChildProp.hasDOMObject;
            lChildClass.ownedAttrAssocNSTitleArr.add(lMemberChildClass.nsTitle);
            lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberChildClass.nsTitle);
            lChildClass.ownedPropNSTitleMap.put(lMemberChildClass.nsTitle, lChildProp);
          }
        }

        // get the inherited and restricted attributes
        // add each ParentClass owned attribute as a ChildClass inherited attribute
        getOwnedAttributesFromParent(lChildClass, lParentClass);
        getInheritedAttributesFromParent(lChildClass, lParentClass);
        getOwnedClassAssociationsFromParent(lChildClass, lParentClass);
        getInheritedClassAssociationsFromParent(lChildClass, lParentClass);

        // set the super class
        // lChildClass.superClassHierArr = lSuperClass;

        // get the base class
        getBaseClassNew(lChildClass, lParentClass);
      }
    }
  }

  // 013a - get the inherited attributes - owned attributes of parent class
  // for each ParentClass
  // for each owned attribute of the ParentClass
  // add the attribute as an inherited attribute of the ChildClass
  // (check is limited to the atribute's nsTitle (namespace + title)
  private void getOwnedAttributesFromParent(DOMClass lChildClass, DOMClass lParentClass) {
    // inherit all owned attributes of the parent class
    for (Iterator<DOMProp> i = lParentClass.ownedAttrArr.iterator(); i.hasNext();) {
      DOMProp lParentProp = i.next();
      if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lParentAttr = (DOMAttr) lParentProp.hasDOMObject;

        // has parent attribute been checked
        if (!lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {
          lChildClass.ownedTestedAttrAssocNSTitleArr.add(lParentAttr.nsTitle);

          // add the parent owned attribute to the inherited attribute array.
          lChildClass.inheritedAttrArr.add(lParentProp);
        }

        // is parent nsTitle a child owned nsTitle
        if (lChildClass.ownedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {

          // the parent owned attribute is restricted
          DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lParentAttr.nsTitle);
          if (lChildProp != null) {
            lChildProp.isRestrictedInSubclass = true;
          }

          DOMAttr lChildAttr = lChildClass.ownedAttrAssocNSTitleMap.get(lParentAttr.nsTitle);
          if (lChildAttr != null) {
            lChildAttr.isRestrictedInSubclass = true;
          }
        }
      }
    }
  }

  // 013b - get the inherited attributes - inherited attributes of parent class
  // for each ParentClass
  // for each inherited attribute of the ParentClass
  // add the attribute as an inherited attribute of the ChildClass
  // (check is limited to the atribute's nsTitle (namespace + title)
  private void getInheritedAttributesFromParent(DOMClass lChildClass, DOMClass lParentClass) {
    // inherit all inherited attributes of the parent class
    for (Iterator<DOMProp> i = lParentClass.inheritedAttrArr.iterator(); i.hasNext();) {
      DOMProp lParentProp = i.next();
      if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lParentAttr = (DOMAttr) lParentProp.hasDOMObject;

        // has parent attribute been checked
        if (!lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {
          lChildClass.ownedTestedAttrAssocNSTitleArr.add(lParentAttr.nsTitle);

          // add the parent owned attribute to the inherited attribute array.
          lChildClass.inheritedAttrArr.add(lParentProp);
        }

        // is parent nsTitle also a child owned nsTitle
        if (lChildClass.ownedAttrAssocNSTitleArr.contains(lParentAttr.nsTitle)) {

          // the parent inherited attribute is inherited; set isRestrictedInSubclass in DOMProp
          DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lParentAttr.nsTitle);
          if (lChildProp != null) {
            lChildProp.isRestrictedInSubclass = true;
          }

          // the parent inherited attribute is inherited
          DOMAttr lChildAttr = lChildClass.ownedAttrAssocNSTitleMap.get(lParentAttr.nsTitle);
          if (lChildAttr != null) {
            lChildAttr.isRestrictedInSubclass = true;
          }
        }
      }
    }
  }

  // 013c - get the inherited associations - owned associations of parent class
  // for each ParentClass
  // for each owned attribute of the ParentClass
  // add the attribute as an inherited attribute of the ChildClass
  // (check is limited to the atribute's nsTitle (namespace + title)
  private void getOwnedClassAssociationsFromParent(DOMClass lChildClass, DOMClass lParentClass) {
    // inherit all owned classes of the parent class
    for (Iterator<DOMProp> i = lParentClass.ownedAssocArr.iterator(); i.hasNext();) {
      DOMProp lParentProp = i.next();
      if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMClass) {
        DOMClass lMemberParentClass = (DOMClass) lParentProp.hasDOMObject;

        // has parent attribute been checked
        if (!lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
          lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberParentClass.nsTitle);

          // add the parent owned attribute to the inherited array.
          lChildClass.inheritedAssocArr.add(lParentProp);
        }

        // is parent nsTitle a child owned nsTitle
        if (lChildClass.ownedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {

          // the parent owned attribute is restricted
          DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lMemberParentClass.nsTitle);
          if (lChildProp != null) {
            lChildProp.isRestrictedInSubclass = true;
          }
        }
      }
    }
  }

  // 013d - get the inherited associations - inherited associations of parent class
  // for each ParentClass
  // for each inherited attribute of the ParentClass
  // add the attribute as an inherited attribute of the ChildClass
  // (check is limited to the attribute's nsTitle (namespace + title)
  private void getInheritedClassAssociationsFromParent(DOMClass lChildClass,
      DOMClass lParentClass) {
    // inherit all inherited attributes of the parent class
    for (Iterator<DOMProp> i = lParentClass.inheritedAssocArr.iterator(); i.hasNext();) {
      DOMProp lParentProp = i.next();
      if (lParentProp.hasDOMObject != null && lParentProp.hasDOMObject instanceof DOMClass) {
        DOMClass lMemberParentClass = (DOMClass) lParentProp.hasDOMObject;

        // has parent attribute been checked
        if (!lChildClass.ownedTestedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {
          lChildClass.ownedTestedAttrAssocNSTitleArr.add(lMemberParentClass.nsTitle);

          // add the parent owned attribute to the inherited array.
          lChildClass.inheritedAssocArr.add(lParentProp);
        }

        // is parent nsTitle a child owned nsTitle
        if (lChildClass.ownedAttrAssocNSTitleArr.contains(lMemberParentClass.nsTitle)) {

          // the parent owned attribute is restricted
          DOMProp lChildProp = lChildClass.ownedPropNSTitleMap.get(lMemberParentClass.nsTitle);
          if (lChildProp != null) {
            lChildProp.isRestrictedInSubclass = true;
          }
        }
      }
    }
  }

  // 013e
  /**
   * get the Base Class
   */
  private void getBaseClassNew(DOMClass lChildClass, DOMClass lParentClass) {
    // get the base class
    DOMClass lSuperClass = lParentClass.subClassOf;
    if (lSuperClass != null) {
      if (lSuperClass.rdfIdentifier.indexOf(protegeRootClassRdfId) != 0) {
        lChildClass.rootClass = lSuperClass.rdfIdentifier;
        String fundStrucName = checkForFundamentalStructure(lSuperClass.title);
        if (fundStrucName != null) {
          lChildClass.baseClassName = fundStrucName;
        }
      }
    } else {
      lChildClass.rootClass = lParentClass.rdfIdentifier;
      String fundStrucName = checkForFundamentalStructure(lParentClass.title);
      if (fundStrucName != null) {
        lChildClass.baseClassName = fundStrucName;
      }
    }
    return;
  }


  // 013.f - For each class, get all owned attributes and associations from the class
  public void getOwnedAttrAssocArr() {
    // get the class.ownedAttrAssocArr array - sorted (owned attributes and associations)
    for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      // sort all owned attributes and associations (AttrDefn)
      ArrayList<DOMProp> lPropArr = new ArrayList<>(lClass.ownedAttrArr);
      lPropArr.addAll(lClass.ownedAssocArr);
      ArrayList<DOMProp> lSortedPropArr = getSortedPropArr(lPropArr);
      lClass.ownedAttrAssocArr.addAll(lSortedPropArr);
    }
  }

  // 013f.1 - sort attributes or associations using MOF properties
  static public ArrayList<DOMProp> getSortedPropArr(ArrayList<DOMProp> lPropArr) {
    TreeMap<String, DOMProp> lSortPropMap = new TreeMap<>();
    for (Iterator<DOMProp> i = lPropArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      String sortId = lProp.classOrder + "_" + lProp.identifier;
      lSortPropMap.put(sortId, lProp);
    }
    ArrayList<DOMProp> lSortAttrArr = new ArrayList<>(lSortPropMap.values());
    return lSortAttrArr;
  }

  // 013g1 - set allAttrAssocArr
  public void setAllAttrAssocArr() {

    // get the class hierarchy, including this class
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get the class hierarchy, including this class
      ArrayList<DOMClass> lClassHierarchyTopDownArr = new ArrayList<>();
      lClassHierarchyTopDownArr.addAll(lClass.superClassHierArr);
      lClassHierarchyTopDownArr.add(lClass);
      ArrayList<DOMClass> lClassHierarchyBottomUp = new ArrayList<>(lClassHierarchyTopDownArr);
      Collections.reverse(lClassHierarchyBottomUp);

      // extract the namespace and title of all owned Attributes and Associations; top down
      ArrayList<String> lMemberAttrAssocIDTopDownArr = new ArrayList<>();
      for (Iterator<DOMClass> j = lClassHierarchyTopDownArr.iterator(); j.hasNext();) {
        DOMClass lDOMClass = j.next();
        for (Iterator<DOMProp> k = lDOMClass.ownedAttrAssocArr.iterator(); k.hasNext();) {
          DOMProp lDOMProp = k.next();
          String lIdentifier;
          if (lDOMProp.hasDOMObject != null) {
            if (lDOMProp.hasDOMObject instanceof DOMAttr) {
              DOMAttr lMemDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
              lIdentifier = lMemDOMAttr.nameSpaceId + lMemDOMAttr.title;
            } else {
              DOMClass lMemDOMClass = (DOMClass) lDOMProp.hasDOMObject;
              lIdentifier = lMemDOMClass.nameSpaceId + lMemDOMClass.title;
            }
            if (!lMemberAttrAssocIDTopDownArr.contains(lIdentifier)) {
              lMemberAttrAssocIDTopDownArr.add(lIdentifier);
            }
          }
        }
      }

      // extracted all the owned Attributes and Associations; bottom up
      ArrayList<String> lMemberAttrAssocIdBottomUpArr = new ArrayList<>();
      TreeMap<String, DOMProp> lMemberAttrAssocIdBottomUpMap = new TreeMap<>();
      for (Iterator<DOMClass> j = lClassHierarchyBottomUp.iterator(); j.hasNext();) {
        DOMClass lDOMClass = j.next();
        for (Iterator<DOMProp> k = lDOMClass.ownedAttrAssocArr.iterator(); k.hasNext();) {
          DOMProp lDOMProp = k.next();
          String lIdentifier;
          if (lDOMProp.hasDOMObject != null) {
            if (lDOMProp.hasDOMObject instanceof DOMAttr) {
              DOMAttr lMemDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
              lIdentifier = lMemDOMAttr.nameSpaceId + lMemDOMAttr.title;
            } else {
              DOMClass lMemDOMClass = (DOMClass) lDOMProp.hasDOMObject;
              lIdentifier = lMemDOMClass.nameSpaceId + lMemDOMClass.title;
            }
            if (!lMemberAttrAssocIdBottomUpArr.contains(lIdentifier)) {
              lMemberAttrAssocIdBottomUpArr.add(lIdentifier);
              lMemberAttrAssocIdBottomUpMap.put(lIdentifier, lDOMProp);
            }
          }
        }
      }

      // create allAttrAssocArr; using id from top down, get the bottom up owned Attributes or
      // Associations
      for (Iterator<String> j = lMemberAttrAssocIDTopDownArr.iterator(); j.hasNext();) {
        String lIdentfier = j.next();
        DOMProp lMemberDOMProp = lMemberAttrAssocIdBottomUpMap.get(lIdentfier);
        if (lMemberDOMProp != null) {
          lClass.allAttrAssocArr.add(lMemberDOMProp);
        }
      }
    }
  }

  // 013g - finalize the remaining attribute and association arrays
  // allEnumAttrIdArr
  // ownedAttrAssocNOArr
  public void setRemainingAttributeAssociationArrays() {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();

      // get all enumerated owned and inherited attributes
      ArrayList<String> allEnumAttrIdArr = new ArrayList<>();
      ArrayList<DOMProp> lOwnedInheritedAttributes = new ArrayList<>();
      lOwnedInheritedAttributes.addAll(lClass.ownedAttrArr);
      lOwnedInheritedAttributes.addAll(lClass.inheritedAttrArr);
      for (Iterator<DOMProp> j = lOwnedInheritedAttributes.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
          if (lDOMAttr != null && lDOMAttr.isEnumerated) {
            if (!allEnumAttrIdArr.contains(lDOMAttr.identifier)) {
              allEnumAttrIdArr.add(lDOMAttr.identifier);
              lClass.allEnumAttrArr.add(lDOMAttr);
            }
          }
        }
      }

      // find all owned attributes and associations that are not inherited from any super class
      // lClass.ownedAttrAssocNOArr
      ArrayList<String> lSuperOwnedAttrAssocArr = new ArrayList<>();
      // iterate through the super classes
      for (Iterator<DOMClass> j = lClass.superClassHierArr.iterator(); j.hasNext();) {
        DOMClass lDOMSuperClass = j.next();

        // first get the identifiers of the owned attributes of the super class
        for (Iterator<DOMProp> k = lDOMSuperClass.ownedAttrArr.iterator(); k.hasNext();) {
          DOMProp lDOMProp = k.next();
          if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
            DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
            lSuperOwnedAttrAssocArr.add(lDOMAttr.nameSpaceId + lDOMAttr.title);
          }
        }
        // second get the identifiers of the owned associations of the super class
        for (Iterator<DOMProp> k = lDOMSuperClass.ownedAssocArr.iterator(); k.hasNext();) {
          DOMProp lDOMProp = k.next();
          if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
            DOMClass lDOMClass = (DOMClass) lDOMProp.hasDOMObject;
            lSuperOwnedAttrAssocArr.add(lDOMClass.nameSpaceId + lDOMClass.title);
          }
        }
      }
      // finally check the owned attributes and associations against the inherited owned attr and
      // assoc from the super classes
      for (Iterator<DOMProp> j = lClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (lDOMProp.hasDOMObject != null) {
          if (lDOMProp.hasDOMObject instanceof DOMAttr) {
            DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
            if (!lSuperOwnedAttrAssocArr.contains(lDOMAttr.nameSpaceId + lDOMAttr.title)) {
              lClass.ownedAttrAssocNOArr.add(lDOMProp);
            }
          } else if (lDOMProp.hasDOMObject instanceof DOMClass) {
            DOMClass lDOMClass = (DOMClass) lDOMProp.hasDOMObject;
            if (!lSuperOwnedAttrAssocArr.contains(lDOMClass.nameSpaceId + lDOMClass.title)) {
              lClass.ownedAttrAssocNOArr.add(lDOMProp);
            }
          }
        }
      }
    }
  }

  // 013h - set IsAnExtension And IsARestriction
  public void setIsAnExtensionAndIsARestriction() {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      // DMDocument.registerMessage ("0>info " + "Extension-Restriction lClass.identifier:" +
      // lClass.identifier);
      // DMDocument.registerMessage ("0>info " + "Extension-Restriction lClass.ownedAttrTitleArr:" +
      // getAttrTitles (lClass));

      DOMClass lParentClass = lClass.subClassOf;
      if (lParentClass != null) {
        // DMDocument.registerMessage ("0>info " + "Extension-Restriction lParentClass.identifier:"
        // + lParentClass.identifier);
        // DMDocument.registerMessage ("0>info " + "Extension-Restriction
        // lParentClass.ownedAttrTitleArr:" + getAttrTitles (lParentClass));
        // DMDocument.registerMessage ("0>info " + "Extension-Restriction
        // lParentClass.ownedTestedAttrAssocNSTitleArr:" +
        // lParentClass.ownedTestedAttrAssocNSTitleArr);
        // DMDocument.registerMessage ("0>info " + "Extension-Restriction
        // lParentClass.ownedAttrAssocNSTitleArr:" + lParentClass.ownedAttrAssocNSTitleArr);

        // the class is an extension if any owned attribute is inherited from its parent class
        for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
          DOMProp lChildProp = j.next();
          if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMAttr) {
            DOMAttr lChildAttr = (DOMAttr) lChildProp.hasDOMObject;

            // is the owned attribute (nsTitle) inherited from its parent class
            if (!lParentClass.ownedTestedAttrAssocNSTitleArr.contains(lChildAttr.nsTitle)) {

              // the owned attribute not inherited; this is an extension
              lClass.isAnExtension = true;
            }
            // is the owned attribute (nsTitle) inherited from its parent class
            if (lParentClass.ownedAttrAssocNSTitleArr.contains(lChildAttr.nsTitle)) {

              // the owned attribute is inherited; this is probably a restriction; test the
              // meta-attributes
              boolean testTrue = false;
              for (Iterator<DOMProp> k = lParentClass.ownedAttrArr.iterator(); k.hasNext();) {
                DOMProp lParentProp = k.next();
                if (lParentProp.hasDOMObject != null
                    && lParentProp.hasDOMObject instanceof DOMAttr) {
                  DOMAttr lParentAttr = (DOMAttr) lChildProp.hasDOMObject;
                  testTrue = isRestrictedAttribute(true, lChildAttr, lParentAttr);
                }
              }
              if (testTrue) {
                lClass.isARestriction = true;
              }
            }
          }
        }

        // the child class is an extension if any association owned by the child is inherited
        for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
          DOMProp lChildProp = j.next();
          if (lChildProp.hasDOMObject != null && lChildProp.hasDOMObject instanceof DOMClass) {
            DOMClass lMemberChildClass = (DOMClass) lChildProp.hasDOMObject;
            // is child nsTitle inherited from the parent
            if (!lParentClass.ownedTestedAttrAssocNSTitleArr.contains(lMemberChildClass.nsTitle)) {
              // the child class is restricted since an owned attribute in this class is not
              // inherited
              lClass.isAnExtension = true;
            }
            // is child nsTitle a parent owned nsTitle
            if (lParentClass.ownedAttrAssocNSTitleArr.contains(lMemberChildClass.nsTitle)) {
              // the child class is restricted since an inherited attribute is also an owned
              // attribute in this class
              boolean testTrue = false;
              for (Iterator<DOMProp> k = lParentClass.ownedAssocArr.iterator(); k.hasNext();) {
                DOMProp lParentProp = k.next();
                testTrue = isRestrictedProp(false, lChildProp, lParentProp);
              }
              if (testTrue) {
                lClass.isARestriction = true;
              }
            }
          }
        }
      }
    }
  }

  public ArrayList<String> getAttrTitles(DOMClass iClass) {
    ArrayList<String> lAttrTitleArr = new ArrayList<>();
    for (Iterator<DOMProp> j = iClass.ownedAttrArr.iterator(); j.hasNext();) {
      DOMProp lDOMProp = j.next();
      if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
        DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        lAttrTitleArr.add(lDOMAttr.title);
      }
    }
    return lAttrTitleArr;
  }

  /**
   * Check whether attribute is restricted
   */
  private boolean isRestrictedAttribute(boolean isAttribute, DOMAttr lAttr, DOMAttr lSuperAttr) {
    // check if the attribute has different meta-attributes
    // for the xml schemas, having a different enumerated list does not count as being different

    if (lSuperAttr == null) {
      return false;
    }

    if (lAttr.valueType.compareTo(lSuperAttr.valueType) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.valueType:"
          + lSuperAttr.valueType + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.cardMin.compareTo(lSuperAttr.cardMin) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.cardMin:"
          + lSuperAttr.cardMin + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.cardMax.compareTo(lSuperAttr.cardMax) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.cardMax:"
          + lSuperAttr.cardMax + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.definition.compareTo(lSuperAttr.definition) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.description:"
          + lSuperAttr.definition + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.minimum_characters.compareTo(lSuperAttr.minimum_characters) != 0) {
      DMDocument.registerMessage("1>warning "
          + "isRestrictedAttribute lSuperAttr.minimum_characters:" + lSuperAttr.minimum_characters
          + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.maximum_characters.compareTo(lSuperAttr.maximum_characters) != 0) {
      DMDocument.registerMessage("1>warning "
          + "isRestrictedAttribute lSuperAttr.maximum_characters:" + lSuperAttr.maximum_characters
          + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.minimum_value.compareTo(lSuperAttr.minimum_value) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.minimum_value:"
          + lSuperAttr.minimum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.maximum_value.compareTo(lSuperAttr.maximum_value) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.maximum_value:"
          + lSuperAttr.maximum_value + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.nameSpaceIdNC.compareTo(lSuperAttr.nameSpaceIdNC) != 0) {
      DMDocument.registerMessage("1>warning "
          + "isRestrictedAttribute lSuperAttr.attrNameSpaceIdNC:" + lSuperAttr.nameSpaceIdNC
          + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.pattern.compareTo(lSuperAttr.pattern) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.pattern:"
          + lSuperAttr.pattern + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.default_unit_id.compareTo(lSuperAttr.default_unit_id) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.default_unit_id:"
          + lSuperAttr.default_unit_id + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (lAttr.format.compareTo(lSuperAttr.format) != 0) {
      DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lSuperAttr.format:"
          + lSuperAttr.format + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
      return true;
    }
    if (!isAttribute) { // if association we need to check the standard values.
      if (lAttr.valArr.size() != lSuperAttr.valArr.size()) {
        DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lAttr.valArr.size():"
            + lAttr.valArr.size() + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
        return true;
      }
      for (Iterator<String> i = lAttr.valArr.iterator(); i.hasNext();) {
        String lVal = i.next();
        if (!lSuperAttr.valArr.contains(lVal)) {
          DMDocument.registerMessage("1>warning " + "isRestrictedAttribute lAttr.lVal" + lVal
              + "   lSuperAttr.rdfIdentifier:" + lSuperAttr.rdfIdentifier);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Check whether attribute is restricted
   */
  private boolean isRestrictedProp(boolean isAttribute, DOMProp lProp, DOMProp lSuperProp) {
    // check if the attribute has different meta-attributes
    // for the xml schemas, having a different enumerated list does not count as being different

    if (lSuperProp == null) {
      return false;
    }

    // 7777 ***** this need to be resolved; this check and the errors do not occur in the MOF
    // version, only in the DOM, why??? *****
    // 7777 error message commented out for v1.12.0.0 to reduce error messages for both IMTool and
    // LDDTool runs
    if ((lProp.cardMin.compareTo(lSuperProp.cardMin) != 0)
        || (lProp.cardMax.compareTo(lSuperProp.cardMax) != 0)
        || (lProp.nameSpaceIdNC.compareTo(lSuperProp.nameSpaceIdNC) != 0)) {
      // DMDocument.registerMessage ("1>warning " + "isRestrictedProperty
      // lSuperProp.attrNameSpaceIdNC:" + lSuperProp.nameSpaceIdNC + " lSuperProp.rdfIdentifier:" +
      // lSuperProp.rdfIdentifier);
      return true;
    }
    return false;
  }

  // 014 - Get the subClass array
  public void getSubClasses() {
    for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) { // get the target
                                                                              // class
      DOMClass lClass = i.next();
      if (lClass.isUSERClass || lClass.isInactive) {
        continue;
      }
      ArrayList<DOMClass> lSubClassHierArr = new ArrayList<>();
      for (Iterator<DOMClass> j = masterDOMClassArr.iterator(); j.hasNext();) { // get class to
                                                                                // check
        DOMClass lCandidateClass = j.next();
        if (lCandidateClass.isInactive) {
          continue;
        }
        if (lClass.identifier.compareTo(lCandidateClass.identifier) == 0) {
          continue; // ignore self
        }
        if (lClass.identifier.compareTo(lCandidateClass.subClassOfIdentifier) == 0) {
          lSubClassHierArr.add(lCandidateClass);
        }
      }
      lClass.subClassHierArr = lSubClassHierArr;
    }
  }

  // 016 - general - master attribute fixup - uses class scan
  // - set the isUsedInClass flag
  public void setMasterAttrisUsedInClassFlag() {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
          lDOMAttr.isUsedInClass = true;
        }
      }
      for (Iterator<DOMProp> j = lClass.inheritedAttrArr.iterator(); j.hasNext();) {
        DOMProp lDOMProp = j.next();
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
          lDOMAttr.isUsedInClass = true;
        }
        // DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
        // if (lDOMAttr != null) {
        // lDOMAttr.isUsedInClass = true;
        // }
      }

      // for (Iterator <DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
      // DOMProp lDOMProp = (DOMProp) j.next();
      // if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
      // DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
      // lChildClass.isUsedInClass = true;
      // }
      // DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
      // if (lChildClass != null) {
      // lChildClass.isUsedInClass = true;
      // }
      // }
      // for (Iterator <DOMProp> j = lClass.inheritedAssocArr.iterator(); j.hasNext();) {
      // DOMProp lDOMProp = (DOMProp) j.next();
      // if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
      // DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
      // lChildClass.isUsedInClass = true;
      // }
      // DOMClass lChildClass = (DOMClass) lDOMProp.hasDOMObject;
      // if (lChildClass != null) {
      // lChildClass.isUsedInClass = true;
      // }
      // }
    }
    return;
  }

  // 018.5 - propagate the inactive flag
  public void setInactiveFlag() {
    // iterate through the classes
    for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {

      // iterate through the owned attribute properties and the attributes
      for (DOMProp lDOMProp : lDOMClass.ownedAttrArr) {
        lDOMProp.isInactive = lDOMClass.isInactive;
        if (lDOMProp.hasDOMObject != null) {
          if (lDOMProp.hasDOMObject instanceof DOMAttr) {
            DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
            lDOMAttr.isInactive = lDOMClass.isInactive;
          }
        }
      }

      // interate through the owned associations - ignore the classes since they are already set
      for (DOMProp lDOMProp : lDOMClass.ownedAssocArr) {
        lDOMProp.isInactive = lDOMClass.isInactive;
      }
    }
  }

  // 018.7 - get the Deprecated Objects array
  public ArrayList<DeprecatedDefn> getDeprecatedObjectsArr() {
    ArrayList<DeprecatedDefn> deprecatedObjectsArr = new ArrayList<>();

    // iterate through the classes for class deprecation
    for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {
      if (lDOMClass.isInactive || !lDOMClass.isDeprecated) {
        continue;
      }
      String deprecatedClassTitle = lDOMClass.title;
      DeprecatedDefn deprecatedDefn = new DeprecatedDefn(deprecatedClassTitle,
          lDOMClass.nameSpaceIdNC, lDOMClass.title, "", "", "", lDOMClass.isUnitOfMeasure);
      deprecatedObjectsArr.add(deprecatedDefn);
    }

    // iterate through the attributes for attribute deprecation
    for (DOMAttr lDOMAttr : DOMInfoModel.masterDOMAttrArr) {
      if (lDOMAttr.isInactive || !lDOMAttr.isDeprecated) {
        continue;
      }
      DOMClass lDOMClass = lDOMAttr.attrParentClass;
      String deprecatedAttrTitle = lDOMClass.title + "." + lDOMAttr.title;

      DeprecatedDefn deprecatedDefn = new DeprecatedDefn(deprecatedAttrTitle,
          lDOMAttr.classNameSpaceIdNC, lDOMAttr.parentClassTitle, lDOMAttr.nameSpaceIdNC,
          lDOMAttr.title, "", lDOMClass.isUnitOfMeasure);
      deprecatedObjectsArr.add(deprecatedDefn);
    }

    // iterate through the attributes for permissible value deprecation
    for (DOMAttr lDOMAttr : DOMInfoModel.masterDOMAttrArr) {
      if (lDOMAttr.isInactive) {
        continue;
      }
      DOMClass lDOMClass = lDOMAttr.attrParentClass;
      String deprecatedAttrTitle = lDOMClass.title + "." + lDOMAttr.title;

      if (lDOMAttr.domPermValueArr != null && lDOMAttr.domPermValueArr.size() > 0) {
        for (DOMProp lDOMProp : lDOMAttr.domPermValueArr) {
          if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
            DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp.hasDOMObject;
            if (lDOMPermVal.isInactive || !lDOMPermVal.isDeprecated) {
              continue;
            }
            // System.out.println("debug getDeprecatedObjectsArr lDOMPermVal.value:" +
            // lDOMPermVal.value + " lDOMPermVal.isInactive:" + lDOMPermVal.isInactive + "
            // lDOMPermVal.isDeprecated:" + lDOMPermVal.isDeprecated + "
            // lDOMPermVal.registrationStatus:" + lDOMPermVal.registrationStatus + "
            // lDOMPermVal.value_meaning:" + lDOMPermVal.value_meaning);
            // 3331 String deprecatedValueTitle = deprecatedAttrTitle + "." + lDOMPermVal.value;
            String deprecatedValueTitle = deprecatedAttrTitle;
            DeprecatedDefn deprecatedDefn2 = new DeprecatedDefn(deprecatedValueTitle,
                lDOMAttr.classNameSpaceIdNC, lDOMAttr.parentClassTitle, lDOMAttr.nameSpaceIdNC,
                lDOMAttr.title, lDOMPermVal.value, lDOMClass.isUnitOfMeasure);
            deprecatedObjectsArr.add(deprecatedDefn2);
          }
        }
      }
    }

    return deprecatedObjectsArr;
  }

  // 019 - general master attribute fixup
  // anchorString; sort_identifier; sorts valArr; get DEC
  // requires final attribute and class namespaces; final valArr;
  public void setMasterAttrGeneral() {
    String lBlanks = "                              ";
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();

      // set attributes anchor string
      // lAttr.attrAnchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" +
      // lAttr.parentClassTitle + "_" + lAttr.nameSpaceIdNC + "_" + lAttr.title).toLowerCase();
      lAttr.anchorString = ("attribute_" + lAttr.classNameSpaceIdNC + "_" + lAttr.parentClassTitle
          + "_" + lAttr.nameSpaceIdNC + "_" + lAttr.title).toLowerCase();

      // set attributes sort identifier
      int lLength = lAttr.title.length();
      if (lLength >= 30) {
        lLength = 30;
      }
      String lPaddedAttrTitle = lAttr.title + lBlanks.substring(0, 30 - lLength);

      lLength = lAttr.parentClassTitle.length();
      if (lLength >= 30) {
        lLength = 30;
      }
      String lPaddedClassTitle = lAttr.parentClassTitle + lBlanks.substring(0, 30 - lLength);
      lAttr.sort_identifier = lPaddedAttrTitle + "_" + lAttr.steward + "_" + lPaddedClassTitle + "_"
          + lAttr.classSteward;

      // sort attribute.valarr
      Collections.sort(lAttr.valArr);
      lAttr.sortPermissibleValues();
      lAttr.setIsCharDataType();
    }
    // set IM Spec Sort Key
    for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lDOMProp = i.next();
      if (lDOMProp.identifier.indexOf("TBD") != 0) {
        lDOMProp.setSortKey();
      }
    }
    return;
  }

  // 020 - set up anchor strings for classes; requires final class namespace.
  public void setMasterClassAnchorString() {
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      lClass.anchorString = ("class_" + lClass.nameSpaceIdNC + "_" + lClass.title).toLowerCase();
    }
    return;
  }

  // 021 - get the permissible values from schematron statements (for example reference_type)
  public void getAttributePermValuesExtended() {
    // get an array of the schematron pattern statements
    ArrayList<DOMRule> lRuleArr = new ArrayList<>(masterDOMRuleMap.values());
    for (Iterator<DOMRule> i = lRuleArr.iterator(); i.hasNext();) {
      DOMRule lRule = i.next();

      // get the affected attribute
      String lAttrId = DOMInfoModel.getAttrIdentifier(lRule.classNameSpaceNC, lRule.classTitle,
          lRule.attrNameSpaceNC, lRule.attrTitle);
      DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(lAttrId);
      if (lAttr != null) {

        // for each rule get assert statement
        for (Iterator<DOMAssert> k = lRule.assertArr.iterator(); k.hasNext();) {
          DOMAssert lAssert = k.next();

          if (lAssert.assertType.compareTo("EVERY") == 0
              || lAssert.assertType.compareTo("IF") == 0) {
            // create a new permissible value entry extended for the attribute
            PermValueExtDefn lPermValueExt = new PermValueExtDefn(lAttr.title);
            lPermValueExt.xpath = lRule.xpath;
            lAttr.permValueExtArr.add(lPermValueExt);

            // create a new permissible value entry for each value
            for (Iterator<String> l = lAssert.testValArr.iterator(); l.hasNext();) {
              String lVal = l.next();
              String lValueMeaning = getValueMeaning(lRule, lVal);
              PermValueDefn lPermValue = new PermValueDefn(lAssert.identifier, lVal, lValueMeaning);
              lPermValueExt.permValueExtArr.add(lPermValue);
            }
          }
        }
      }
    }
    return;
  }

  // 022 - set Registration Status
  public void setRegistrationStatus() {

    // scan all classes and set registration status
    for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {
      if (lDOMClass.isDeprecated) {
        lDOMClass.registrationStatus = "Retired";

        // update property
        if (lDOMClass.hasDOMPropInverse != null) {
          lDOMClass.hasDOMPropInverse.registrationStatus = lDOMClass.registrationStatus;
          lDOMClass.hasDOMPropInverse.isDeprecated = lDOMClass.isDeprecated;
        }
      }
    }

    // scan all attributes and set registration status
    for (DOMAttr lDOMAttr : DOMInfoModel.masterDOMAttrArr) {
      if (lDOMAttr.isDeprecated) {
        lDOMAttr.registrationStatus = "Retired";

        // update property
        if (lDOMAttr.hasDOMPropInverse != null) {
          lDOMAttr.hasDOMPropInverse.registrationStatus = lDOMAttr.registrationStatus;
          lDOMAttr.hasDOMPropInverse.isDeprecated = lDOMAttr.isDeprecated;
        }
      }

      for (PermValueDefn lPermValue : lDOMAttr.permValueArr) {
        if (lPermValue.isDeprecated) {
          lPermValue.registrationStatus = "Retired";
          lDOMAttr.hasRetiredValue = true;
        }
      }

      for (DOMProp lDOMProp : lDOMAttr.domPermValueArr) {
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
          DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
          if (lDOMPermValDefn.isDeprecated) {
            lDOMPermValDefn.registrationStatus = "Retired";
            lDOMAttr.hasRetiredValue = true;
          }
        }
      }
    }
  }

  // 024 - get the data type definitions
  public void setMasterDataType2() {

    // Find DOMClasses that define DataTypes
    TreeMap<String, DOMClass> lDOMDataTypeClassTitleMap = new TreeMap<>();

    // iterate through the classes and create a sorted array
    for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.title.indexOf("Data_Type") > -1 || lClass.rootClass.indexOf("Data_Type") > -1) {
        if (lClass.subClassHierArr.isEmpty()) {
          lDOMDataTypeClassTitleMap.put(lClass.title, lClass);
        }
      }
    }
    ArrayList<DOMClass> lDOMDataTypeClassArr = new ArrayList<>(lDOMDataTypeClassTitleMap.values());

    // Create DOMDataTypes
    for (Iterator<DOMClass> i = lDOMDataTypeClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      DOMDataType lDataType = new DOMDataType();
      lDataType.setRDFIdentifier(lClass.title);
      if (DOMInfoModel.masterDOMDataTypeMap.get(lDataType.rdfIdentifier) == null) {
        // the data type does not exist, add it

        lDataType.setDataTypeAttrs(lClass);
        DOMInfoModel.masterDOMDataTypeMap.put(lDataType.rdfIdentifier, lDataType);
        DOMInfoModel.masterDOMDataTypeTitleMap.put(lDataType.title, lDataType);
      }
    }
    DOMInfoModel.masterDOMDataTypeArr = new ArrayList<>(DOMInfoModel.masterDOMDataTypeMap.values());
  }

  // 025 - Set the DEC and CD for each attribute.
  public void GetMasterDECMaps() {
    // set the DEC maps
    DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(DMDocument.masterPDSSchemaFileDefn.regAuthId
        + "." + DMDocument.masterPDSSchemaFileDefn.identifier + "."
        + "DD_Attribute_Full.pds.attribute_concept");

    if (lAttr == null) {
      DMDocument.registerMessage("1>error " + "system attribute - attribute_concept - MISSING");
      return;
    }
    if ((lAttr.valArr == null) || (lAttr.valArr.size() < 1)) {
      DMDocument.registerMessage(
          "1>error " + "system attribute - attribute_concept - NO PERMISSIBLE VALUES");
      return;
    }
    for (Iterator<String> i = lAttr.valArr.iterator(); i.hasNext();) {
      String lDECTitle = i.next();
      String lDECId = "DEC_" + lDECTitle;
      DOMInfoModel.cdID2CDTitleMap.put(lDECId, lDECTitle);
      DOMInfoModel.cdTitle2CDIDMap.put(lDECTitle, lDECId);
    }
  }

  // 026 - set up master conceptual domain array
  public void GetMasterCDMaps() {
    // set the CD maps
    DOMAttr lAttr = DOMInfoModel.masterDOMAttrIdMap.get(DMDocument.masterPDSSchemaFileDefn.regAuthId
        + "." + DMDocument.masterPDSSchemaFileDefn.identifier + "."
        + "DD_Value_Domain_Full.pds.conceptual_domain");

    if (lAttr == null) {
      DMDocument.registerMessage("1>error " + "system attribute - conceptual_domain - MISSING");
      return;
    }
    if ((lAttr.valArr == null) || (lAttr.valArr.size() < 1)) {
      DMDocument.registerMessage(
          "1>error " + "system attribute - conceptual_domain - NO PERMISSIBLE VALUES");
      return;
    }
    for (Iterator<String> i = lAttr.valArr.iterator(); i.hasNext();) {
      String lCDTitle = i.next();
      String lCDId = "CD_" + lCDTitle;
      DOMInfoModel.cdID2CDTitleMap.put(lCDId, lCDTitle);
      DOMInfoModel.cdTitle2CDIDMap.put(lCDTitle, lCDId);
    }
  }

  // 027 - Set Master Attribute XML Base Data Type From the Data Type
  public void SetMasterAttrXMLBaseDataTypeFromDataType() {
    // for each PDS4 attribute set the xml_schema_base_type from the data type

    for (DOMAttr lAttr : DOMInfoModel.masterDOMAttrArr) {
      if (lAttr.isInactive) continue;
      if (lAttr.isExtendedAttribute) continue;
      if (lAttr.isPDS4) {
        DOMDataType lDataType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lAttr.valueType);
        if (lDataType != null) {
          lAttr.xmlBaseDataType = lDataType.xml_schema_base_type;
        } else {
          DMDocument.registerMessage("1>error "
              + "SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing - lAttr.identifier:"
              + lAttr.identifier);
          DMDocument.registerMessage("1>error "
              + "SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing - lAttr.valueType:"
              + lAttr.valueType);
        }
      }
    }
  }

  // 029.1 - find attributes that have no overrides; compare to data type
  public void sethasAttributeOverride1(ArrayList<DOMAttr> lMasterDOMAttrArr) {
    for (DOMAttr lAttr : lMasterDOMAttrArr) {
      if (lAttr.isInactive) continue;
      if (lAttr.isExtendedAttribute) continue;
      if (lAttr.isAttribute) {
        DOMDataType lValueType = DOMInfoModel.masterDOMDataTypeTitleMap.get(lAttr.valueType);
        if (lValueType == null) {
          DMDocument.registerMessage("1>error "
              + "Could not find a value type for this attribute while checking for attribute overrides - Name:"
              + lAttr.identifier + "   Value Type:" + lAttr.valueType);
        } else {
          boolean hasOverride = false;
          if (!((lAttr.minimum_value.indexOf("TBD") == 0)
              || (lAttr.minimum_value.compareTo(lValueType.minimum_value) == 0))) {
            hasOverride = true;
          }
          if (!((lAttr.maximum_value.indexOf("TBD") == 0)
              || (lAttr.maximum_value.compareTo(lValueType.maximum_value) == 0))) {
            hasOverride = true;
          }
          if (!((lAttr.minimum_characters.indexOf("TBD") == 0)
              || (lAttr.minimum_characters.compareTo(lValueType.minimum_characters) == 0))) {
            hasOverride = true;
          }
          if (!((lAttr.maximum_characters.indexOf("TBD") == 0)
              || (lAttr.maximum_characters.compareTo(lValueType.maximum_characters) == 0))) {
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
            // if the attribute does not have an override, then it gets to use it own name in the
            // schema
            lAttr.XMLSchemaName = lAttr.title;
          }
        }
      }
    }
    return;
  }

  // 029.1 - for attributes with overrides, the first gets to use its own name in the schema
  // the others are either equivalent or are forced to use className_attributeName
  public void sethasAttributeOverride2(ArrayList<DOMAttr> lMasterDOMAttrArr) {
    TreeMap<String, DOMAttr> lSortAttrMap = new TreeMap<>();
    TreeMap<String, ArrayList<DOMAttr>> lTitleAttrsMap = new TreeMap<>();
    
	System.out.println("\n\ndebug MasterDOMInfoModel -sethasAttributeOverride2");


    // *** Need to add steward to partition attributes ***

    // put attributes in sorted order; attr.title_class.title
    for (Iterator<DOMAttr> i = lMasterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if ((lAttr.XMLSchemaName.indexOf("TBD") == 0) && lAttr.isAttribute) {
        // all remaining attributes have overrides, i.e. different constraints than those in the
        // data type
        lSortAttrMap.put(lAttr.title + "_" + lAttr.parentClassTitle + lAttr.nameSpaceId, lAttr);
      }
    }

    // for each attribute title, get the array of all attributes with that title
    ArrayList<String> lTitleArr = new ArrayList<>();
    ArrayList<DOMAttr> lSortAttrArr = new ArrayList<>(lSortAttrMap.values());
    ArrayList<DOMAttr> lAttrArr = new ArrayList<>();
    for (Iterator<DOMAttr> i = lSortAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      
      if (lAttr.title.compareTo("archive_status_note") == 0) {
			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr.identifier:" + lAttr.identifier);
			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr.parentClassTitle:" + lAttr.parentClassTitle);
			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr.title:" + lAttr.title);
      }
      
      if (lTitleArr.contains(lAttr.title)) {
        lAttrArr.add(lAttr);
      } else {
        lTitleArr.add(lAttr.title);
        lAttrArr = new ArrayList<>();
        lAttrArr.add(lAttr);
        lTitleAttrsMap.put(lAttr.title, lAttrArr);
      }
    }

    // iterate through the title/attribute array map
    Set<String> set1 = lTitleAttrsMap.keySet();
    Iterator<String> iter1 = set1.iterator();
    while (iter1.hasNext()) {
      String lTitle = iter1.next();
      ArrayList<DOMAttr> lAttrArr2 = lTitleAttrsMap.get(lTitle);

      // iterate through this attribute array
      boolean isFirst = true;
      for (Iterator<DOMAttr> i = lAttrArr2.iterator(); i.hasNext();) {
        DOMAttr lAttr1 = i.next();
        
        if (lAttr1.title.compareTo("archive_status_note") == 0) {
  			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr1.identifier:" + lAttr1.identifier);
  			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr1.parentClassTitle:" + lAttr1.parentClassTitle);
  			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr1.title:" + lAttr1.title);
        }

        // if this is the first attribute, it gets to use its own name
        if (isFirst) {
          lAttr1.XMLSchemaName = lAttr1.title;
          isFirst = false;
        } else {

          // nested iteration through this attribute array to find the duplicates
          boolean isEquivalentAll = true;
          for (Iterator<DOMAttr> j = lAttrArr2.iterator(); j.hasNext();) {
            DOMAttr lAttr2 = j.next();
            
            if (lAttr1.title.compareTo("archive_status_note") == 0) {
      			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr2.identifier:" + lAttr2.identifier);
      			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr2.parentClassTitle:" + lAttr2.parentClassTitle);
      			System.out.println("debug MasterDOMInfoModel -sethasAttributeOverride2 - lAttr2.title:" + lAttr2.title);
            }            

            // omit checking an attribute against itself
            if (lAttr1.rdfIdentifier.compareTo(lAttr2.rdfIdentifier) == 0) {
              continue;
            }

            // set one XMLSchemaName at a time
            if (lAttr2.XMLSchemaName.indexOf("TBD") == 0) {
              break;
            }
            boolean isEquivalent = true;
            if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
              isEquivalent = false;
              DMDocument.registerMessage("1>error "
                  + "hasAttributeOverride2 - valueType is not equivalent - attribute identifier:"
                  + lAttr2.identifier);
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
            }
            DMDocument.registerMessage(
                "1>warning " + "sethasAttributeOverride - attributes are not equivalent:"
                    + lAttr1.identifier + " - " + lAttr2.identifier);
            isEquivalentAll = isEquivalentAll && isEquivalent;
          }
          if (!isEquivalentAll) {
            DMDocument.registerMessage("1>error "
                + "sethasAttributeOverride - attribute is not equivalent - Setting unique name - attribute identifier:"
                + lAttr1.identifier);
            lAttr1.XMLSchemaName = lAttr1.parentClassTitle + "_" + lAttr1.title;
          }
        }
      }
    }
    return;
  }

  // 030 - get the hasDOMObject from the valArr for each association (AttrDefn)
  public void getValClassArr() {
    // for each PDS4 attribute set the meta attribute from the data type
    for (Iterator<DOMProp> i = DOMInfoModel.masterDOMPropArr.iterator(); i.hasNext();) {
      DOMProp lProp = i.next();
      if (lProp.isAttribute || lProp.valArr == null || lProp.valArr.isEmpty()) {
        continue;
      }
      for (Iterator<String> j = lProp.valArr.iterator(); j.hasNext();) {
        String lTitle = j.next();
        String lClassMemberIdentifier =
            DOMInfoModel.getClassIdentifier(lProp.nameSpaceIdNC, lTitle);
        DOMClass lClassMember = DOMInfoModel.masterDOMClassIdMap.get(lClassMemberIdentifier);
        if (lClassMember != null) {
          lProp.hasDOMObject = lClassMember;
        } else {
          DOMClass firstClassFound = null;
          for (Iterator<DOMClass> k = DOMInfoModel.masterDOMClassArr.iterator(); k.hasNext();) {
            lClassMember = k.next();
            if (lClassMember.title.compareTo(lTitle) == 0) {
              if (firstClassFound == null) {
                firstClassFound = lClassMember;
                DMDocument.registerMessage("1>warning "
                    + "get class using attribute value array - lAttr.identifier:" + lProp.identifier
                    + " - using first found - lClassMember.identifier:" + lClassMember.identifier);
              } else {
                DMDocument.registerMessage("1>warning "
                    + "get class using attribute value array - lAttr.identifier:" + lProp.identifier
                    + " - also found - lClassMember.identifier:" + lClassMember.identifier);
              }
            }
          }
          if (firstClassFound != null) {
            lProp.hasDOMObject = firstClassFound;
          }
        }
      }
    }
  }

  // 031 - Get the attribute's CD and DEC values
  // get all CD and DEC values for each attribute; used to print a CD or DEC 11179 definition. i.e.
  // all DECS for a CD.
  public void getCDDECIndexes() {
    // get Attribute for the VD info
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (lAttr.isUsedInClass && lAttr.isAttribute) {

        // update the CD (dataConcept) Index
        String sortKey = lAttr.dataConcept;
        DOMIndexDefn lIndex = cdDOMAttrMap.get(sortKey);
        if (lIndex == null) {
          lIndex = new DOMIndexDefn(sortKey);
          cdDOMAttrMap.put(sortKey, lIndex);
        }
        lIndex.identifier1Map.put(lAttr.identifier, lAttr);
        if (!lIndex.identifier2Arr.contains(lAttr.classConcept)) {
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
        if (!lIndex.identifier2Arr.contains(lAttr.dataConcept)) {
          lIndex.identifier2Arr.add(lAttr.dataConcept);
        }
      }
    }
    return;
  }

  // 032 - Get the attribute's DEC values
  // null

  // 033 - get the USER attributes (not owned attributes)
  // Get User Class Attributes Id Map (not owned)
  public void getUserSingletonClassAttrIdMap() {
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      if (!lAttr.isAttribute || lAttr.isUsedInClass || (lAttr.title.compareTo("%3ANAME") == 0)) {
        continue;
      }
      DOMInfoModel.userSingletonDOMClassAttrIdMap.put(lAttr.identifier, lAttr);
    }
    return;
  }

  // 034 - get the LDDToolSingletonClass, the class for LDD singleton attributes (Discipline or
  // Mission)
  // currently defined in GetDOMModel

  // 039 - set exposed flag
  // currently defined in GetDOMModel

  // ===============================================================================================

  // get the value meaning
  public String getValueMeaning(DOMRule lRule, String lVal) {
    String lId = lRule.xpath + "." + lRule.attrTitle + "." + lVal;
    PermValueDefn lPermVal = GetValueMeanings.masterValueMeaningKeyMap.get(lId);
    if (lPermVal != null) {
      return lPermVal.value_meaning;
    }
    return "TBD_value_meaning";
  }

  // CheckDataTypes
  public void CheckDataTypes() {
    DMDocument.registerMessage("0>info " + "CheckDataTypes");
    TreeMap<String, DOMAttr> lTreeMap = new TreeMap<>();
    for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      String rId = lAttr.title + "_" + lAttr.rdfIdentifier;
      lTreeMap.put(rId, lAttr);
    }
    Collection<DOMAttr> values = lTreeMap.values();
    ArrayList<DOMAttr> lAttrArr = (new ArrayList<>(values));
    String pTitle = "", pDataType = "", pRDFId = "";
    for (Iterator<DOMAttr> j = lAttrArr.iterator(); j.hasNext();) {
      DOMAttr lAttr = j.next();
      DMDocument.registerMessage(
          "0>info " + "CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
      if (lAttr.title.compareTo(pTitle) == 0) {
        if (lAttr.valueType.compareTo(pDataType) == 0) {
          continue;
        }
        DMDocument.registerMessage(
            "0>info " + "CheckDataTypes data types not equal - lAttr.title:" + lAttr.title);
        DMDocument
            .registerMessage("0>info " + "CheckDataTypes data types not equal - pRDFId:" + pRDFId);
        DMDocument.registerMessage(
            "0>info " + "CheckDataTypes data types not equal - pDataType:" + pDataType);
        DMDocument.registerMessage("0>info "
            + "CheckDataTypes data types not equal - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
        DMDocument.registerMessage(
            "0>info " + "CheckDataTypes data types not equal - lAttr.valueType:" + lAttr.valueType);
      } else {
        pTitle = lAttr.title;
        pDataType = lAttr.valueType;
        pRDFId = lAttr.rdfIdentifier;
      }
    }
  }

  /**********************************************************************************************************
   * Routines for Finalizing the Ontology
   ***********************************************************************************************************/

  // The hierarchy of classes is then processed downward
  // one level at a time, where each class at the level again processes back
  // up the hierarchy, this time capturing inherited attributes.


  /**
   * Fixup name spaces and base class name - final cleanup
   */
  public void fixNameSpaces() {

    // iterate through the classes and get the namespace
    for (Iterator<DOMClass> i = masterDOMClassArr.iterator(); i.hasNext();) {
      DOMClass lClass = i.next();
      if (lClass.baseClassName.indexOf("TBD") == 0) {
        lClass.baseClassName = "none";
      }
    }
    return;
  }

  /**********************************************************************************************************
   * miscellaneous routines
   ***********************************************************************************************************/

  /**
   * Check for attributes with the same ns:title but different constraints. (attributes with the
   * same ns:title can be used in two or more classes this check ensures that they all have the same
   * values for the meta-attributes Description alone is allowed to be different *** Different
   * constraints would cause multiple simpleType definitions ***
   */
  static public void checkSameNameOverRide() {
    DMDocument
        .registerMessage("0>info " + "Checking for attribute consistency - checkSameNameOverRide");

    // sort the attributes
    TreeMap<String, DOMAttr> lAttrMap = new TreeMap<>();
    for (Iterator<DOMAttr> i = masterDOMAttrArr.iterator(); i.hasNext();) {
      DOMAttr lAttr = i.next();
      // *** temporary fix - needs to be thought out ***
      if (lAttr.isFromLDD) {
        continue;
      }
      lAttrMap.put(lAttr.title + "-" + lAttr.identifier, lAttr);
    }
    ArrayList<DOMAttr> lAttrArr = new ArrayList<>(lAttrMap.values());

    // check the attributes against all other attributes
    for (int i = 0; i < lAttrArr.size(); i++) {
      DOMAttr lAttr1 = lAttrArr.get(i);
      if (!lAttr1.isAttribute) {
        continue;
      }
      for (int j = i + 1; j < lAttrArr.size(); j++) {
        DOMAttr lAttr2 = lAttrArr.get(j);


        // only check attributes with the same titles


        // don't check the meta-attributes
        if (!lAttr2.isAttribute || !(lAttr1.nsTitle.compareTo(lAttr2.nsTitle) == 0)
            || (lAttr1.title.compareTo("minimum_characters") == 0)
            || (lAttr1.title.compareTo("maximum_characters") == 0)) {
          continue;
        }
        if (lAttr1.title.compareTo("minimum_value") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("maximum_value") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("xml_schema_base_type") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("formation_rule") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("character_constraint") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("value") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("unit_id") == 0) {
          continue;
        }
        if (lAttr1.title.compareTo("pattern") == 0) {
          continue;
        }

        // check what is left
        checkForOverRideDetail(lAttr1, lAttr2);
      }
    }
  }

  static public void checkForOverRideDetail(DOMAttr lAttr1, DOMAttr lAttr2) {
    boolean isFound = false;

    if (lAttr1.valueType.compareTo(lAttr2.valueType) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.valueType:"
          + lAttr1.valueType + "   lAttr2.valueType:" + lAttr2.valueType);
    }
    if (lAttr1.minimum_characters.compareTo(lAttr2.minimum_characters) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.minimum_characters:"
          + lAttr1.minimum_characters + "   lAttr2.minimum_characters:"
          + lAttr2.minimum_characters);
    }
    if (lAttr1.maximum_characters.compareTo(lAttr2.maximum_characters) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.maximum_characters:"
          + lAttr1.maximum_characters + "   lAttr2.maximum_characters:"
          + lAttr2.maximum_characters);
    }
    if (lAttr1.minimum_value.compareTo(lAttr2.minimum_value) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.minimum_value:"
          + lAttr1.minimum_value + "   lAttr2.minimum_value:" + lAttr2.minimum_value);
    }
    if (lAttr1.maximum_value.compareTo(lAttr2.maximum_value) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.maximum_value:"
          + lAttr1.maximum_value + "   lAttr2.maximum_value:" + lAttr2.maximum_value);
    }
    if (lAttr1.pattern.compareTo(lAttr2.pattern) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.pattern:"
          + lAttr1.pattern + "   lAttr2.pattern:" + lAttr2.pattern);
    }
    if (lAttr1.default_unit_id.compareTo(lAttr2.default_unit_id) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.default_unit_id:"
          + lAttr1.default_unit_id + "   lAttr2.default_unit_id:" + lAttr2.default_unit_id);
    }
    if (lAttr1.format.compareTo(lAttr2.format) != 0) {
      isFound = true;
      DMDocument.registerMessage("0>warning " + "checkForOverRideDetail lAttr1.format:"
          + lAttr1.format + "   lAttr2.format:" + lAttr2.format);
    }
    if (isFound) {
      DMDocument.registerMessage(
          "0>info " + "checkForOverRideDetail lAttr1.identifier:" + lAttr1.identifier);
      DMDocument.registerMessage(
          "0>info " + "checkForOverRideDetail lAttr2.identifier:" + lAttr2.identifier);
      DMDocument.registerMessage("0>info ");
    }
    return;
  }

  static public void printOwnedAttrAssocArr(DOMClass lClass, String lTitle) {
    DMDocument.registerMessage("0>info " + "ownedAttrAssocArr - " + lTitle + " - "
        + "lClass.identifier:" + lClass.identifier);
    for (Iterator<DOMProp> j = lClass.ownedAttrAssocArr.iterator(); j.hasNext();) {
      DOMProp lProp = j.next();
      DMDocument.registerMessage("0>info " + "  - lProp.classOrder:" + lProp.classOrder);
    }
  }

  static public void printAllAttrAssocArr(DOMClass lClass, String lTitle) {
    DMDocument.registerMessage("0>info ");
    DMDocument.registerMessage("0>info " + "allAttrAssocArr - " + lTitle + " - "
        + "lClass.identifier:" + lClass.identifier);
    for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
      DOMProp lProp = j.next();
      DMDocument.registerMessage("0>info " + " - lProp.identifier:" + lProp.identifier);
      DMDocument.registerMessage("0>info " + " - lProp.classOrder:" + lProp.classOrder);
    }
  }
}
