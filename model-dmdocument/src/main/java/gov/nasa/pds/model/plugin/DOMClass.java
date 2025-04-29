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
import java.util.Iterator;
import java.util.TreeMap;

public class DOMClass extends ISOClassOAIS11179 {

  String section; // section of the info model specification document for this class
  String role; // abstract or concrete
  String xPath; // class xpath
  String docSecType; // the title of the class below USER, for each class's class hierarchy
  String rootClass; // RDF identifier
  String baseClassName; // Fundamental structure class title
  String localIdentifier; // used temporarily for ingest of LDD
  String used; // used flag - Y, N, or I - Inactive
  String extrnTitleQM; // title of external class for query model

  int subClassLevel;
  boolean isUSERClass; // The class of all classes
  boolean isMasterClass; // will be included in the master class map
  boolean isSchema1Class; // will have a schema type 1 created
  boolean isRegistryClass; // will be included in registry configuration file
  boolean isUsedInModel;
  boolean isAnExtension; // This class is an extension
  boolean isARestriction; // This class is a restriction
  boolean isVacuous; // a vacuous class is empty and therefore not to be included in schemas
  boolean isUnitOfMeasure;
  boolean isDataType;
  boolean isTDO;
  // boolean isAbstract;
  boolean isChoice; // class requires xs:choice
  boolean isAny; // class requires xs:any
  boolean includeInThisSchemaFile;
  boolean isFromLDD; // has been ingested from Ingest_LDD
  boolean isReferencedFromLDD; // is a class in the master that is referenced from an LDD
  boolean isExposed; // the class is to be exposed in XML Schema - i.e., defined using xs:Element
  boolean isAssociatedExternalClass; // the class was defined using DD_Associate_External_Class
  boolean isQueryModel; // this class was defined DD_Associate_External_Class is a query model

  DOMProp hasDOMPropInverse; // the owning DOMProp of this Class
  ArrayList<DOMProtAttr> hasDOMProtAttr; // the protege attributes to be converted to DOMProp and
                                         // either DOMAttr or DOMClass
  DOMClass subClassOf;
  String subClassOfTitle; // needed after parsing Protege Pont file to find subClass
  String subClassOfIdentifier; // needed after parsing Protege Pont file to find subClass
  ArrayList<DOMClass> superClassHierArr; // super classes - does not include USER; however USER is
                                         // the value of subClassOfTitle for top level protege
                                         // classes
  ArrayList<DOMClass> subClassHierArr;

  ArrayList<DOMProp> ownedAttrArr;
  ArrayList<DOMProp> inheritedAttrArr;
  ArrayList<DOMProp> ownedAssocArr;
  ArrayList<DOMProp> inheritedAssocArr;

  ArrayList<DOMProp> allAttrAssocArr;
  ArrayList<DOMProp> ownedAttrAssocArr; // each class's owned attribute and associations in sorted
                                        // order
  ArrayList<DOMProp> ownedAttrAssocNOArr;
  ArrayList<String> ownedAttrAssocNSTitleArr; // needed during attribute/association inheritance
                                              // processing
  ArrayList<String> ownedTestedAttrAssocNSTitleArr; // needed during attribute/association
                                                    // inheritance processing
  TreeMap<String, DOMProp> ownedPropNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass
                                                // during inheritance processing
  TreeMap<String, DOMAttr> ownedAttrAssocNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass
                                                     // during inheritance processing

  ArrayList<DOMAttr> allEnumAttrArr; // all enumerated attributes, this.class and all superclasses

  String dataIdentifier; // data identifier
  String ocDataIdentifier; // object class
  String desDataIdentifier; // designation
  String defDataIdentifier; // definition
  String teDataIdentifier; // terminological entry
  String administrationRecordValue;
  String registeredByValue;
  //
  // **** move registeredByValue to ISOClassOAIS11179
  // **** deprecate registrationAuthorityIdentifierValue and use ISOClassOAIS11179.regAuthId
  // **** add both to DOMProtAttr ???
  // String registrationAuthorityIdentifierValue;

  public DOMClass() {
    section = "TBD_section";
    role = "TBD_role";
    xPath = "TBD_xPath";
    docSecType = "TBD_type";
    rootClass = "TBD_root_class";
    baseClassName = "TBD_base_class_name";
    localIdentifier = "TBD_localIdentifier";
    used = "TBD_used";
    extrnTitleQM = "TBD_extrnTitleQM";
    subClassLevel = 0;
    isUSERClass = false;
    // isUsedInClass = false;
    isMasterClass = false;
    isSchema1Class = false;
    isRegistryClass = false;
    isUsedInModel = false;
    isAnExtension = false;
    isARestriction = false;
    isVacuous = false;
    isUnitOfMeasure = false;
    isDataType = false;
    isTDO = false;
    // isAbstract = false;
    isChoice = false;
    isAny = false;
    includeInThisSchemaFile = false;
    isFromLDD = false;
    isReferencedFromLDD = false;
    isAssociatedExternalClass = false; // the class was defined using DD_Associate_External_Class
    isQueryModel = false;

    hasDOMPropInverse = null;
    hasDOMProtAttr = new ArrayList<>();

    subClassOf = null;
    subClassOfTitle = "TBD_subClassOfTitle";
    subClassOfIdentifier = "TBD_subClassOfIdentifier";

    subClassHierArr = new ArrayList<>(); // all subclasses (children) of this class
    superClassHierArr = new ArrayList<>(); // the superclass (parent) hierarchy for this
                                                   // class

    ownedAttrArr = new ArrayList<>();
    inheritedAttrArr = new ArrayList<>();
    ownedAssocArr = new ArrayList<>();
    inheritedAssocArr = new ArrayList<>();

    allAttrAssocArr = new ArrayList<>();
    ownedAttrAssocArr = new ArrayList<>();
    ownedAttrAssocNOArr = new ArrayList<>();
    ownedAttrAssocNSTitleArr = new ArrayList<>();
    ownedTestedAttrAssocNSTitleArr = new ArrayList<>();
    ownedPropNSTitleMap = new TreeMap<>();
    ownedAttrAssocNSTitleMap = new TreeMap<>();
    allEnumAttrArr = new ArrayList<>();

    ocDataIdentifier = "TBD_ocDataIdentifier"; // data element
    desDataIdentifier = "TBD_desDataIdentifier"; // designation
    defDataIdentifier = "TBD_defDataIdentifier"; // definition
    teDataIdentifier = "TBD_teDataIdentifier"; // terminlogical entry
    administrationRecordValue = "TBD_administrationRecordValue";
    registeredByValue = "TBD_registeredByValue";
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    this.section = section;
  }

  /**
   * get the disposition of a class (from Protege)
   */
  public boolean getDOMClassDisposition2() {
    if (this.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
      this.nameSpaceIdNC = DMDocument.masterNameSpaceIdNCLC;
    } else {
      this.nameSpaceIdNC = "tbd"; // %3ACLIPS_TOP_LEVEL_SLOT_CLASS is not in the Common namespace
    }
    this.nameSpaceId = this.nameSpaceIdNC + ":";
    this.identifier = DOMInfoModel.getClassIdentifier(this.nameSpaceIdNC, this.title);
    set11179Attr(this.identifier);
    return true;
  }

  public void set11179Attr(String lIdentifier) {
    // object class identifiers
    dataIdentifier = lIdentifier;
    ocDataIdentifier = "OC." + lIdentifier; // data element
    desDataIdentifier = "DES." + lIdentifier; // designation
    defDataIdentifier = "DEF." + lIdentifier; // definition
    teDataIdentifier = "TE." + lIdentifier; // terminological entry
  }

  static ArrayList<DOMClass> sortDOMClass(ArrayList<DOMClass> ISOArr) {
    TreeMap<String, DOMClass> lISOMap = new TreeMap<>();
    for (Iterator<DOMClass> i = ISOArr.iterator(); i.hasNext();) {
      DOMClass lISO = i.next();
      lISOMap.put(lISO.identifier, lISO);
    }
    ArrayList<DOMClass> lISOArr = new ArrayList<>(lISOMap.values());
    return lISOArr;
  }

  static ArrayList<DOMAttr> sortDOMAttr(ArrayList<DOMAttr> ISOArr) {
    TreeMap<String, DOMAttr> lISOMap = new TreeMap<>();
    for (Iterator<DOMAttr> i = ISOArr.iterator(); i.hasNext();) {
      DOMAttr lISO = i.next();
      lISOMap.put(lISO.identifier, lISO);
    }
    ArrayList<DOMAttr> lISOArr = new ArrayList<>(lISOMap.values());
    return lISOArr;
  }

  static ArrayList<DOMProp> sortDOMProp(ArrayList<DOMProp> ISOArr) {
    TreeMap<String, DOMProp> lISOMap = new TreeMap<>();
    for (Iterator<DOMProp> i = ISOArr.iterator(); i.hasNext();) {
      DOMProp lISO = i.next();
      lISOMap.put(lISO.identifier, lISO);
    }
    ArrayList<DOMProp> lISOArr = new ArrayList<>(lISOMap.values());
    return lISOArr;
  }
  
  // set the Class and owned Properties, Attributes, and the PermissibleValue isInactive flags
  void setIsInactive(Boolean lIsInactive) {
	  
	  // set the class inactive
	  this.isInactive = lIsInactive;
		  
	  // iterate through the owned attributes - via their properties
      for (DOMProp lDOMProp : ownedAttrArr) {
    	  lDOMProp.isInactive = lIsInactive;
    	  if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
    		  DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
    		  lDOMAttr.isInactive = lIsInactive;
    		  
    		  // iterate through the permissible values - via their properties
    		  for (DOMProp lDOMProp2 : lDOMAttr.domPermValueArr) {
    			  if (lDOMProp2.domObject != null && lDOMProp2.domObject instanceof DOMPermValDefn) {
    				  DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMProp2.domObject;
    				  lDOMPermVal.isInactive = lIsInactive;
    			  }
    		  }
    	  }
      }
      // interate through the owned associations - ignore the classes since they are already set
      for (DOMProp lDOMProp : ownedAssocArr) {
        lDOMProp.isInactive = lIsInactive;
      }
  }
}
