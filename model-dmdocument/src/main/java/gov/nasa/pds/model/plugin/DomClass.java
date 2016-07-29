package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class DomClass extends ISOClassOAIS11179 {
	
	String section;									// section of the info model specification document for  this class
	String subModelId;								// identifier of submodel within the registration authority's model.
	String role;									// abstract or concrete
	String docSecType;								// class type = title
	String rootClass;								// RDF identifier
	String baseClassName;							// Fundamental structure class title
	String localIdentifier;							// used temporarily for ingest of LDD
	
	int subClassLevel;
	boolean isUSERClass;							// The class of all classes
	boolean isMasterClass;							// will be included in the master class map
	boolean isSchema1Class;							// will have a schema type 1 created
	boolean isRegistryClass;						// will be included in registry configuration file
	boolean isUsedInModel;
	boolean isVacuous;								// a vacuous class is empty and therefore not to be included in schemas
	boolean isUnitOfMeasure;
	boolean isDataType;
	boolean isTDO;
	boolean isAbstract;
	boolean isChoice;								// class requires xs:choice
	boolean isAny;									// class requires xs:any
	boolean includeInThisSchemaFile;
	boolean isFromLDD;									// has been ingested from Ingest_LDD
	boolean isReferencedFromLDD;						// is a class in the master that is referenced from an LDD
	
	DomClass subClassOf; 
	ArrayList <DomClass> superClassHierArr; 
	ArrayList <DomClass> subClassHierArr; 
	
	ArrayList <DomProp> ownedAttrArr; 
	ArrayList <DomProp> inheritedAttrArr; 
	ArrayList <DomProp> ownedAssocArr; 
	ArrayList <DomProp> inheritedAssocArr; 
	
	ArrayList <DomProp> allAttrAssocArr; 
	ArrayList <DomProp> ownedAttrAssocNOArr;
	ArrayList <DomProp> ownedAttrAssocArr;				// each class's owned attribute and associations in sorted order
	ArrayList <DomProp> ownedAttrAssocAssertArr;		// all enumerated attributes, from this.class through to all superclasses.
	ArrayList <DomProp> ownedAttrAssocAssertTitleArr;	// all enumerated attributes, required to eliminate duplicates
	
	public DomClass () {
		section = "TBD_section";
		subModelId = "TBD_submodel_identifier";
		role = "TBD_role";
		docSecType = "TBD_type"; 
		rootClass = "TBD_root_class";
		baseClassName = "TBD_base_class_name";
		localIdentifier = "TBD_localIdentifier";
		subClassLevel = 0;
		isUSERClass = false;
		isMasterClass = false;
		isSchema1Class = false;
		isRegistryClass = false;
		isUsedInModel = false;
		isVacuous = false;
		isUnitOfMeasure = false;
		isDataType = false;
		isTDO = false;
		isAbstract = false;
		isChoice = false;
		isAny = false;
		includeInThisSchemaFile = false;
		isFromLDD = false;
		isReferencedFromLDD = false;
		
		subClassOf = null;
		subClassHierArr = new ArrayList <DomClass> ();  
		superClassHierArr = new ArrayList <DomClass> ();  
		
		ownedAttrArr = new ArrayList <DomProp> (); 
		inheritedAttrArr = new ArrayList <DomProp> (); 
		ownedAssocArr = new ArrayList <DomProp> (); 
		inheritedAssocArr = new ArrayList <DomProp> (); 
		
		allAttrAssocArr = new ArrayList <DomProp> (); 
		ownedAttrAssocNOArr = new ArrayList <DomProp> ();
		ownedAttrAssocArr = new ArrayList <DomProp> ();
		ownedAttrAssocAssertArr = new ArrayList <DomProp> ();
		ownedAttrAssocAssertTitleArr = new ArrayList <DomProp> ();
	}
	
	public String getSection() {
		return section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public void initDomClass (PDSObjDefn lOldClass,  TreeMap <String, DomClass> lDomClassMap, TreeMap <String, AttrDefn> lDomAttrMap) {
		initDomClassSingletons (lOldClass);
		initDomClassArrs (lOldClass, lDomClassMap);
//		initDomAttrArrs (lOldClass, lDomAttrMap);
	}
	
	public void initDomClassSingletons (PDSObjDefn lOldClass) {
		section = lOldClass.section;
		subModelId = lOldClass.subModelId;
		role = lOldClass.role;
		docSecType = lOldClass.docSecType;
		rootClass = lOldClass.rootClass;
		baseClassName = lOldClass.baseClassName;
		localIdentifier = lOldClass.localIdentifier;
		subClassLevel = lOldClass.subClassLevel;
		isUSERClass = lOldClass.isUSERClass;
		isMasterClass = lOldClass.isMasterClass;
		isSchema1Class = lOldClass.isSchema1Class;
		isRegistryClass = lOldClass.isRegistryClass;
		isUsedInModel = lOldClass.isUsedInModel;
		isVacuous = lOldClass.isVacuous;
		isUnitOfMeasure = lOldClass.isUnitOfMeasure;
		isDataType = lOldClass.isDataType;
		isTDO = lOldClass.isTDO;
		isAbstract = lOldClass.isAbstract;
		isChoice = lOldClass.isChoice;
		isAny = lOldClass.isAny;
		includeInThisSchemaFile = lOldClass.includeInThisSchemaFile;
		isFromLDD = lOldClass.isFromLDD;
		isReferencedFromLDD = lOldClass.isReferencedFromLDD;
		return;
	}
	
	public void initDomClassArrs (PDSObjDefn lOldClass, TreeMap <String, DomClass> lDomClassMap) {
		InitClassArr (subClassHierArr, lOldClass.subClass, lDomClassMap);
		InitClassArr (superClassHierArr, lOldClass.superClass, lDomClassMap);
//		subClassOf = lOldClass.subClassOfInst;
		return;
	}
	
	// copy a class array
	public void InitClassArr (ArrayList <DomClass> lDomClassArr, ArrayList <PDSObjDefn> lPDSClassArr, TreeMap <String, DomClass> lDomClassMap) {
		for (Iterator <PDSObjDefn> i = lPDSClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lOldClass = (PDSObjDefn) i.next();
			// using the RDFIdentifier of the original class, get the new "equivalent" Dom class.
			DomClass lDomClass = lDomClassMap.get(lOldClass.rdfIdentifier);
			if (lDomClass != null)
				lDomClassArr.add(lDomClass);
			else
				System.out.println(">>error    - InitClassArr - Failed to find new DomClass - lOldClass.rdfIdentifier: " + lOldClass.rdfIdentifier);
		}
	}

/*
	public void initDomAttrArrs (PDSObjDefn lOldClass, TreeMap <String, AttrDefn> lDomAttrMap) {
		InitAttrArr (ownedAttrArr, lOldClass.ownedAttribute);

		InitAttrArr (ownedAttrArr, lOldClass.ownedAttribute, lDomAttrMap);
		InitAttrArr (inheritedAttrArr, lOldClass.inheritedAttribute, lDomAttrMap);
		InitAttrArr (ownedAssocArr, lOldClass.ownedAssociation, lDomAttrMap);
		InitAttrArr (inheritedAssocArr, lOldClass.inheritedAssociation, lDomAttrMap);
		InitAttrArr (allAttrAssocArr, lOldClass.allAttrAssocArr, lDomAttrMap);
		InitAttrArr (ownedAttrAssocNOArr, lOldClass.ownedAttrAssocNOArr, lDomAttrMap);
		InitAttrArr (ownedAttrAssocArr, lOldClass.ownedAttrAssocArr, lDomAttrMap);
		InitAttrArr (ownedAttrAssocAssertArr, lOldClass.ownedAttrAssocAssertArr, lDomAttrMap);
		InitAttrArr (ownedAttrAssocAssertTitleArr, lOldClass.ownedAttrAssocAssertTitleArr, lDomAttrMap);
		return;
	}*/
	
	// copy an attr array
	public void InitAttrArr (ArrayList <DomAttr> lDomAttrArr, ArrayList <AttrDefn> lOldAttrArr) {
		for (Iterator <AttrDefn> i = lOldAttrArr.iterator(); i.hasNext();) {
			AttrDefn lOldAttr = (AttrDefn) i.next();
			DomAttr lDomAttr = new DomAttr ();
			lDomAttr.initDomAttr(lOldAttr);
			lDomAttrArr.add(lDomAttr);
		}
	}
}
