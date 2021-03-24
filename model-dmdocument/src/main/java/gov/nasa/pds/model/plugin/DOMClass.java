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
	
	String section;									// section of the info model specification document for  this class
	String role;									// abstract or concrete
	String xPath;									// class xpath
	String docSecType;								// the title of the class below USER, for each class's class hierarchy
	String rootClass;								// RDF identifier
	String baseClassName;							// Fundamental structure class title
	String localIdentifier;							// used temporarily for ingest of LDD
	String used;									// MDPTNConfig used flag - Y, N, or I - Inactive
	
	int subClassLevel;
	boolean isUSERClass;							// The class of all classes
	boolean isMasterClass;							// will be included in the master class map
	boolean isSchema1Class;							// will have a schema type 1 created
	boolean isRegistryClass;						// will be included in registry configuration file
	boolean isUsedInModel;
	boolean isAnExtension;							// This class is an extension
	boolean isARestriction;							// This class is a restriction
	boolean isVacuous;								// a vacuous class is empty and therefore not to be included in schemas
	boolean isUnitOfMeasure;
	boolean isDataType;
	boolean isTDO;
//	boolean isAbstract;
	boolean isChoice;								// class requires xs:choice
	boolean isAny;									// class requires xs:any
	boolean includeInThisSchemaFile;
	boolean isFromLDD;								// has been ingested from Ingest_LDD
	boolean isReferencedFromLDD;				// is a class in the master that is referenced from an LDD
	boolean isExposed;							// the class is to be exposed in XML Schema - i.e., defined using xs:Element
	boolean isAssociatedExternalClass;			// the class was defined using DD_Associate_External_Class

	DOMProp hasDOMPropInverse;					// the owning DOMProp of this Class 
	ArrayList <DOMProtAttr> hasDOMProtAttr;		// the protege attributes to be converted to DOMProp and either DOMAttr or DOMClass
	DOMClass subClassOf; 
	String subClassOfTitle; 					// needed after parsing Protege Pont file to find subClass
	String subClassOfIdentifier; 				// needed after parsing Protege Pont file to find subClass
	ArrayList <DOMClass> superClassHierArr; 	// super classes - does not include USER; however USER is the value of subClassOfTitle for top level protege classes
	ArrayList <DOMClass> subClassHierArr; 
	
	ArrayList <DOMProp> ownedAttrArr; 
	ArrayList <DOMProp> inheritedAttrArr; 
	ArrayList <DOMProp> ownedAssocArr; 
	ArrayList <DOMProp> inheritedAssocArr; 
	
	ArrayList <DOMProp> allAttrAssocArr; 
	ArrayList <DOMProp> ownedAttrAssocArr;					// each class's owned attribute and associations in sorted order
	ArrayList <DOMProp> ownedAttrAssocNOArr;
	ArrayList <String> ownedAttrAssocNSTitleArr; 		// needed during attribute/association inheritance processing
	ArrayList <String> ownedTestedAttrAssocNSTitleArr; 		// needed during attribute/association inheritance processing
	TreeMap <String, DOMProp> ownedPropNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass during inheritance processing
	TreeMap <String, DOMAttr> ownedAttrAssocNSTitleMap; // needed to set DOMAttr.isRestrictedInSubclass during inheritance processing

	ArrayList <DOMAttr> allEnumAttrArr;						// all enumerated attributes, this.class and all superclasses
	
	public DOMClass () {
		section = "TBD_section";
		role = "TBD_role";
		xPath = "TBD_xPath";
		docSecType = "TBD_type"; 
		rootClass = "TBD_root_class";
		baseClassName = "TBD_base_class_name";
		localIdentifier = "TBD_localIdentifier";
		used = "TBD_used";
		subClassLevel = 0;
		isUSERClass = false;
//		isUsedInClass = false;
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
//		isAbstract = false;
		isChoice = false;
		isAny = false;
		includeInThisSchemaFile = false;
		isFromLDD = false;
		isReferencedFromLDD = false;
		isAssociatedExternalClass = false;			// the class was defined using DD_Associate_External_Class
		
		hasDOMPropInverse = null;
		hasDOMProtAttr = new ArrayList <DOMProtAttr> ();
		
		subClassOf = null;
		subClassOfTitle = "TBD_subClassOfTitle"; 					
		subClassOfIdentifier = "TBD_subClassOfIdentifier";
		
		subClassHierArr = new ArrayList <DOMClass> ();				// all subclasses (children) of this class
		superClassHierArr = new ArrayList <DOMClass> ();  			// the superclass (parent) hierarchy for this class
		
		ownedAttrArr = new ArrayList <DOMProp> (); 
		inheritedAttrArr = new ArrayList <DOMProp> (); 
		ownedAssocArr = new ArrayList <DOMProp> (); 
		inheritedAssocArr = new ArrayList <DOMProp> (); 
		
		allAttrAssocArr = new ArrayList <DOMProp> (); 
		ownedAttrAssocArr = new ArrayList <DOMProp> ();
		ownedAttrAssocNOArr = new ArrayList <DOMProp> ();
		ownedAttrAssocNSTitleArr = new ArrayList <String> (); 
		ownedTestedAttrAssocNSTitleArr = new ArrayList <String> (); 
		ownedPropNSTitleMap = new TreeMap <String, DOMProp> ();
		ownedAttrAssocNSTitleMap = new TreeMap <String, DOMAttr> ();
		allEnumAttrArr = new ArrayList <DOMAttr> ();
	}
	
	public String getSection() {
		return section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	/**
	 *   get the disposition of a class (from Protege)
	 */
	public boolean getDOMClassDisposition (boolean isFromProtege) {
		// get disposition identifier - if isFromProtege, then the identifier is set else it is not since it is from an LDD.
		String lDispId = this.subModelId + "." + DMDocument.registrationAuthorityIdentifierValue + "." + this.title;
		if (! isFromProtege) lDispId = "LDD_" + lDispId;
		DispDefn lDispDefn = DMDocument.masterClassDispoMap2.get(lDispId);
		if (lDispDefn != null) {
			this.used = lDispDefn.used;
			this.section = lDispDefn.section;
			String lDisp = lDispDefn.disposition;
			this.steward = lDispDefn.intSteward;
			String lClassNameSpaceIdNC = lDispDefn.intNSId;
			this.nameSpaceIdNC = lClassNameSpaceIdNC;
			this.nameSpaceId = lClassNameSpaceIdNC + ":";
			
			// if from protege, the identifier needs to be set; if from LDD it cannot be set here.
			if (isFromProtege) this.identifier = DOMInfoModel.getClassIdentifier(lClassNameSpaceIdNC, this.title);
			this.isMasterClass = true;
			if (lDisp.indexOf("V") > -1) {
				this.isVacuous = true;
			}
			if (lDisp.indexOf("S") > -1) {
				this.isSchema1Class = true;
			}
			if (lDisp.indexOf("R") > -1) {
				this.isRegistryClass = true;
			}
			if (lDisp.indexOf("T") > -1) {
				this.isTDO = true;
			}
			if (lDisp.indexOf("d") > -1) {
				this.isDataType = true;
			}
			if (lDisp.indexOf("u") > -1) {
				this.isUnitOfMeasure = true;
			}
			return true;
		}
		return false;
	}
}
