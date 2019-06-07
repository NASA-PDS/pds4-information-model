package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

//This code writes the information model to XML schema, attempting to replicate the class hierarchy.
//It is written to a single schema file (single namespace).

class XML4LabelSchemaDOM extends Object {
	PrintWriter prXML;
	ArrayList <String> allAttrTypeIdArr = new ArrayList<String> ();
	TreeMap <String, DOMProp> allAttrTypeMap = new TreeMap <String, DOMProp> ();
	TreeMap <String, DOMClass> classHierMap = new TreeMap <String, DOMClass> ();
	String extensionRestrictionDeclaration;
	String pNS = "xs:";
	int indentSpaces;
	String indentSpacesString = "                    ";
	boolean choiceBlockOpen = false;
	String lGroupName = "TBD_groupName";
	boolean xsAnyStmtWritten = false;
 
	public XML4LabelSchemaDOM () {
		return;
	}

//	write the XML Label
	public void writeXMLSchemaFiles (SchemaFileDefn lSchemaFileDefn, ArrayList <DOMClass> lInputClassArr) throws java.io.IOException {

		// get the classes
		classHierMap = getPDS4ClassesForSchema (lSchemaFileDefn, lInputClassArr);
		
		String lFileName = lSchemaFileDefn.relativeFileSpecXMLSchema;
		String lLabelVersionId = "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id;
		String lDOMLabelVersionId = lLabelVersionId + "_DOM";
		lFileName = DMDocument.replaceString (lFileName, lLabelVersionId, lDOMLabelVersionId);
		prXML = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));		
		
//		write the XML Schema File Header
		writeXMLSchemaFileHeader (lSchemaFileDefn, prXML);
								
       	// write the Product Class Elements
    	if (lSchemaFileDefn.isMaster) {
    		// write the xs:elements for the master classes (e.g., the Product classes)
    		prXML.println(" ");
    		ArrayList <DOMClass> lSortedClassArr = new ArrayList <DOMClass> (classHierMap.values());
    		for (Iterator<DOMClass> i = lSortedClassArr.iterator(); i.hasNext();) {
    			DOMClass lClass = (DOMClass) i.next();
    			if (lClass.isRegistryClass || lClass.isExposed) {
    				prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lClass.title + "\">" + " </" + pNS + "element>");
    			}
    		}
    		
    		// write the xs:element definitions for the master attributes (e.g., local_identifier and logical_identifier)
			prXML.println(" ");
			ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrMap.values());
			for (Iterator <AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! (lAttr.isExposed)) continue;
				if (! (lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.attrNameSpaceIdNC) == 0)) continue;
				prXML.println("  <" + pNS + "element name=\"" + lAttr.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.title + "\"> </" + pNS + "element>");
			}
    	}

		// for non-LDDTool runs, write the Class Elements for all schemas except the master schema 
		if ((! lSchemaFileDefn.isLDD) && (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0)) {			
			ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
			ArrayList <DOMClass> lClassSubArr = new ArrayList <DOMClass> ();
			for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();
				if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) { 
					lClassSubArr.add(lClass);
				}
			}
			writeLDDClassElementDefinition (lSchemaFileDefn, lClassSubArr, prXML);
		}

		// for LDDTool runs and Class flag on, write the Class Elements for all schemas except the master schema 
		if (lSchemaFileDefn.isLDD && lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) {
	
			ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassMap.values());
			ArrayList <DOMClass> lClassSubArr = new ArrayList <DOMClass> ();

			for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
				DOMClass lClass = (DOMClass) i.next();
				
				if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0 && lClass.isFromLDD && lClass.isExposed) { 
					lClassSubArr.add(lClass);
				}
			}
			writeLDDClassElementDefinition (lSchemaFileDefn, lClassSubArr, prXML);
		}  	
				
		// write the Attribute Element definition statements
		if (lSchemaFileDefn.isLDD && DMDocument.LDDAttrElementFlag) {
			ArrayList <DOMAttr> lFilteredAttrArr = new ArrayList <DOMAttr> ();
			for (Iterator<DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
				DOMAttr lAttr = (DOMAttr) i.next();
				if (! lAttr.isAttribute) continue;
				if (! lAttr.isFromLDD) continue;
				if (lAttr.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;
				if (lAttr.parentClassTitle.indexOf("TBD") == 0) continue;
				lFilteredAttrArr.add(lAttr);
			}
			writeLDDAttributeElementDefinitons(lFilteredAttrArr, lSchemaFileDefn, prXML);			
		}
				
//		Write the classes
		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (classHierMap.values());
		for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			
			// skip the subclasses of Science_Facets
			if (lClass.title.compareTo("Discipline_Facets") == 0) continue;
			if (lClass.title.compareTo("Group_Facet1") == 0) continue;
			if (lClass.title.compareTo("Group_Facet2") == 0) continue;
			
			ResetIndentSpaces();
			
			// determine extension, restriction, neither
			boolean isExtension = lClass.isAnExtension;
			boolean isRestriction = lClass.isARestriction;
			
			if (! isExtension) {
				isRestriction = true;
			}
			if (lClass.subClassOf != null) {
				DOMClass lParentClass = lClass.subClassOf;
				if (lParentClass.isVacuous || (lParentClass.title.compareTo("USER") == 0)) {
					isExtension = false;
					isRestriction = false;
				}
			}
			boolean isBothExtensionRestriction = isExtension && isRestriction;
			
			// write the classes
			xsAnyStmtWritten = false;
			if (isBothExtensionRestriction) {
				writeXMLClassExtension (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<DOMClass>(), new ArrayList<DOMClass>(), prXML);
				writeXMLClassRestriction (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<DOMClass>(), new ArrayList<DOMClass>(), prXML);
			} else if (isExtension) {
				writeXMLClassExtension (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<DOMClass>(), new ArrayList<DOMClass>(), prXML);
			} else if (isRestriction) {
				writeXMLClassRestriction (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<DOMClass>(), new ArrayList<DOMClass>(), prXML);
			} else {
				writeXMLClassNeither (lSchemaFileDefn, lClass, new ArrayList<DOMClass>(), new ArrayList<DOMClass>(), prXML);				
			}			
		}

    	// write the attributes
    	writeXMLAttributes (lSchemaFileDefn, prXML);    	

    	// write the data types
		if (lSchemaFileDefn.isMaster || lSchemaFileDefn.isDiscipline || lSchemaFileDefn.isMission) {
    		writeXMLDataTypes (lSchemaFileDefn, prXML);
    	}
		
    	// write the Units Of Measure
		if (lSchemaFileDefn.isMaster || lSchemaFileDefn.isDiscipline || lSchemaFileDefn.isMission) {
    		writeXMLUnitsOfMeasure(lSchemaFileDefn, prXML);
    	}
		
    	// write the Nil Values
		if (lSchemaFileDefn.isMaster || lSchemaFileDefn.isDiscipline || lSchemaFileDefn.isMission) {
    		writeXMLNillValues (lSchemaFileDefn, prXML);
    	}
		
		if (DMDocument.LDDToolFlag  && DMDocument.LDDNuanceFlag) {
			WriteDOMPropMapsSchema lWritePropMapsSchema = new WriteDOMPropMapsSchema ();
			lWritePropMapsSchema.writeDOMPropertyMaps (prXML);
		}
		
       	// write the deprecated items
    	if (lSchemaFileDefn.isMaster) {
    		writeDeprecatedItems (prXML);
    	}
    	
    	// write the footer
    	writeXMLSchemaFileFooter (prXML);
    	
//		close the file
    	prXML.close();
    	
	
	}	
	
	// get the PDS4 classes (Products ?)
	public TreeMap <String, DOMClass> getPDS4ClassesForSchema (SchemaFileDefn lSchemaFileDefn, ArrayList <DOMClass> lClassArr) {	
		TreeMap <String, DOMClass> lClassMap = new TreeMap <String, DOMClass> ();
		int classCount = 0;
		for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			lClass.includeInThisSchemaFile = false;
			if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) && lSchemaFileDefn.stewardArr.contains(lClass.steward)) {				
				if (! (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) {
					lClass.includeInThisSchemaFile = true;
					classCount++;
					lClassMap.put(lClass.identifier, lClass);
				}
			}
		}
//		System.out.println("\ndebug getPDS4ClassesForSchema classCount:" + classCount);
		return lClassMap;
	}
	
//	write the XML Schema File Header
	public void writeXMLSchemaFileHeader (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		//	Write the header statements
		prXML.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXML.println("  <!-- " + DMDocument.masterPDSSchemaFileDefn.modelShortName + " XML/Schema" + " for Name Space Id:" + lSchemaFileDefn.nameSpaceIdNC + "  Version:" + lSchemaFileDefn.ont_version_id + " - " + DMDocument.masterTodaysDate + " -->");	 
		prXML.println("  <!-- Generated from the " + DMDocument.masterPDSSchemaFileDefn.modelShortName + " Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - System Build " + DMDocument.XMLSchemaLabelBuildNum + " -->");
		prXML.println("  <!-- *** This " + DMDocument.masterPDSSchemaFileDefn.modelShortName + " product schema is an operational deliverable. *** -->");
		if (DMDocument.LDDToolFlag) {
			prXML.println("  <!--                                                                           -->");
			prXML.println("  <!--               Dictionary Stack                                            -->");
			String lEntry = DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - " + DMDocument.masterPDSSchemaFileDefn.nameSpaceId + " - " + DMDocument.masterPDSSchemaFileDefn.lddName + " - " + DMDocument.masterPDSSchemaFileDefn.sourceFileName + "                                   ";
			lEntry = lEntry.substring(0, 73);
			prXML.println("  <!-- " + lEntry + " -->");

			for (Iterator <SchemaFileDefn> i = DMDocument.LDDSchemaFileSortArr.iterator(); i.hasNext();) {
				SchemaFileDefn lFileInfo = (SchemaFileDefn) i.next();
//				lEntry = lFileInfo.sourceFileName + " - " + lFileInfo.ont_version_id;
				lEntry = lFileInfo.ont_version_id + " - " + lFileInfo.nameSpaceId + " - " + lFileInfo.lddName + " - " + lFileInfo.sourceFileName + "                                   ";
				lEntry = lEntry.substring(0, 73);
				prXML.println("  <!-- " + lEntry + " -->");
			}
			prXML.println("  <!--                                                                           -->");
		}
		prXML.println("  <" + pNS + "schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
		
		// write namespace statements
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0) {
			// namespaces required: pds - latest version
			prXML.println("    targetNamespace=\"" + lSchemaFileDefn.nameSpaceURL + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:" + lSchemaFileDefn.nameSpaceIdNC + "=\"" + lSchemaFileDefn.nameSpaceURL + lSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
		} else {
			// namespaces required: ldd - latest version
			String governanceDirectory = "";
			if (DMDocument.LDDToolMissionGovernanceFlag) governanceDirectory = DMDocument.governanceLevel.toLowerCase() +  "/";
			prXML.println("    targetNamespace=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + governanceDirectory + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:" + lSchemaFileDefn.nameSpaceIdNC + "=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + governanceDirectory + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");

			// namespaces required: pds - latest version
			prXML.println("    xmlns:" + lSchemaFileDefn.nameSpaceIdNC + "=\"" + lSchemaFileDefn.nameSpaceURL + lSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");

			// namespaces required: all other discipline levels referenced; no mission level allowed - prior versions
			if (DMDocument.LDDToolFlag) {
				for (Iterator<String> i = DMDocument.LDDImportNameSpaceIdNCArr.iterator(); i.hasNext();) {
					String lNameSpaceIdNC = (String) i.next();
					String lVersionNSId = (DMDocument.masterSchemaFileSortMap.get(lNameSpaceIdNC)).ns_version_id;
					if (lVersionNSId == null) lVersionNSId = DMDocument.masterPDSSchemaFileDefn.ns_version_id;
					prXML.println("    xmlns:" + lNameSpaceIdNC + "=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + lNameSpaceIdNC + "/v" + lVersionNSId + "\"");
				}
			}
		}
		prXML.println("    elementFormDefault=\"qualified\"");
		prXML.println("    attributeFormDefault=\"unqualified\"");
		prXML.println("    version=\"" + lSchemaFileDefn.sch_version_id + "\">");

		// write import statements for all but pds (common)
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) {
			// imports required: pds - latest version
			prXML.println(" ");		
			prXML.println("    <" + pNS + "import namespace=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURLs + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id +                                      "/PDS4_PDS_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\"/>");	

			// imports required: all other LDD discipline levels referenced; no mission level allowed
			for (Iterator<String> i = DMDocument.LDDImportNameSpaceIdNCArr.iterator(); i.hasNext();) {
				String lNameSpaceIdNC = (String) i.next();
				SchemaFileDefn lLDDSchemaFileDefn = DMDocument.masterAllSchemaFileSortMap.get(lNameSpaceIdNC);
				String lSchemaLocationFileName2 = "TBD_lSchemaLocation";
				if (lLDDSchemaFileDefn != null) lSchemaLocationFileName2 = lLDDSchemaFileDefn.relativeFileNameXMLSchema2;
				prXML.println("    <" + pNS + "import namespace=\"" + lLDDSchemaFileDefn.nameSpaceURL + lNameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"" + lLDDSchemaFileDefn.nameSpaceURLs + lNameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/" + lSchemaLocationFileName2 + "\"/>");	
			}
		}
		
		prXML.println(" ");		
		prXML.println("  <" + pNS + "annotation>");		
		if (! DMDocument.LDDToolFlag) {
//			prXML.println(DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lSchemaFileDefn.comment + "</" + pNS + "documentation>", (indentSpaces + 2) * 2, 72));		
			String lLine = "<" + pNS + "documentation>" + lSchemaFileDefn.comment + "</" + pNS + "documentation>";		
			DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, (indentSpaces + 2) * 2, 72 ), prXML);
		} else {
			// change made to allow Mitch's comments to wrap properly.
			prXML.println("    <" + pNS + "documentation>");		
			prXML.println(lSchemaFileDefn.comment);		
			prXML.println("    </" + pNS + "documentation>");
		}
		prXML.println("  </" + pNS + "annotation>");
	}
	
//	write the XML Schema File Footer
	public void writeXMLSchemaFileFooter (PrintWriter prXML) throws java.io.IOException {
		prXML.println("</" + pNS + "schema>");		
	}
	
	public void writeXMLClassExtension (SchemaFileDefn lSchemaFileDefn, boolean isBothExtensionRestriction, DOMClass lClass, ArrayList<DOMClass> visitClassList, ArrayList<DOMClass> recurseClassList, PrintWriter prXML) throws java.io.IOException {
		// get proper names		
		String lClassName = lClass.title;
		String lSuperClassName = lClass.subClassOfTitle ;
		if (isBothExtensionRestriction) {
			lClassName = lClass.subClassOfTitle + "-" + lClass.title;
		}
		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
//		prXML.println (DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
		String lLine = "<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>";		
		DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, (indentSpaces + 1) * 2, 72 ), prXML);
		prXML.println(indentSpaces() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "complexContent>");
		prXML.println(indentSpacesUp() + "<" + pNS + "extension base=\"" + lClass.nameSpaceId + lSuperClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "sequence" + ">");

		// write extension attributes
		writeAttrAssocInClass(lClass, lClass.ownedAttrAssocNOArr, prXML);
					
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);
		
		// write assertions for enumerated values	
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0 ) {		
			if (! isBothExtensionRestriction) {
				writeAssertionsAttr (lClass.allEnumAttrArr, prXML);	
			}
		}	
		
		prXML.println(indentSpacesDown() + "</" + pNS + "extension>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexContent>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}
	
	public void writeXMLClassRestriction (SchemaFileDefn lSchemaFileDefn, boolean isBothExtensionRestriction, DOMClass lClass, ArrayList<DOMClass> visitClassList, ArrayList<DOMClass> recurseClassList, PrintWriter prXML) throws java.io.IOException {		
		// get proper names
		String lClassName = lClass.title;
		String lSuperClassName = lClass.subClassOfTitle ;
		if (isBothExtensionRestriction) {
			lSuperClassName = lSuperClassName + "-" + lClass.title;
		}		
		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
//		prXML.println (DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
		String lLine = "<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>";		
		DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, (indentSpaces + 1) * 2, 72 ), prXML);
		prXML.println(indentSpaces() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "complexContent>");
		prXML.println(indentSpacesUp() + "<" + pNS + "restriction base=\"" + lClass.nameSpaceId + lSuperClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "sequence" +">");
	
		// write attributes and associations
		writeAttrAssocInClass(lClass, lClass.allAttrAssocArr, prXML);
	
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);

		// write assertions for enumerated values
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0 ) {		
			writeAssertionsAttr (lClass.allEnumAttrArr, prXML);	
		}
		
		prXML.println(indentSpacesDown() + "</" + pNS + "restriction>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexContent>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}	
	
	public void writeXMLClassNeither (SchemaFileDefn lSchemaFileDefn, DOMClass lClass, ArrayList<DOMClass> visitClassList, ArrayList<DOMClass> recurseClassList, PrintWriter prXML) throws java.io.IOException {		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClass.title + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
//		prXML.println (DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
		String lLine = "<" + pNS + "documentation>" + lClass.definition + "</" + pNS + "documentation>";		
		DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, (indentSpaces + 1) * 2, 72 ), prXML);
		prXML.println(indentSpaces() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "sequence" + ">");
		
		// write attributes and associations
		writeAttrAssocInClass(lClass, lClass.allAttrAssocArr, prXML);
		
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);

		// write assertions for enumerated values
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0 ) {		
			writeAssertionsAttr (lClass.allEnumAttrArr, prXML);
		}
		
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}
	
//	write the attributes and associations as element definitions, components of the element that represents the class
	public void writeAttrAssocInClass (DOMClass lClass, ArrayList <DOMProp> lAttrAssocArr, PrintWriter prXML) throws java.io.IOException {
		choiceBlockOpen = false;
		lGroupName = "TBD_groupName";

		upIndentSpaces();
//		if ((lClass.title.indexOf("Mission_Area") > -1) || (lClass.title.indexOf("Discipline_Area") > -1) || (lClass.title.indexOf(DMDocument.LDDToolGeometry) > -1)) {
		if ((lClass.title.indexOf("Mission_Area") > -1) || (lClass.title.indexOf("Discipline_Area") > -1)) {
			writeClassXSAnyStmts (prXML);
			downIndentSpaces();
			return;
		}
		if ((lClass.title.indexOf("Type_List_Area") > -1)) {
			writeClassXSAnyTypeList (prXML);
			downIndentSpaces();
			return;
		}
		for (Iterator<DOMProp> i = lAttrAssocArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();			
			if (lProp.isAny) {
				writeClassXSAnyStmts (prXML);
			}
			if (lProp.isAttribute) {
				writeClassAttribute (lClass, lProp, prXML);
			} else {
				writeClassAssociation (lClass, lProp, prXML);
			}
		}
		downIndentSpaces();
		
		// if the choice block is open, close it
		if (choiceBlockOpen) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
			choiceBlockOpen = false;
		}	
		return;
	}	

//	write the attribute as an element definition, a component of the element that represents the class
	public void writeClassAttribute (DOMClass lClass, DOMProp lProp, PrintWriter prXML) throws java.io.IOException {
		// capture the data types that are used; will write only those used.
		ISOClassOAIS11179 lDOMObject = lProp.hasDOMObject;
		DOMAttr lAttr = (DOMAttr) lDOMObject;
		String lValueType = lAttr.valueType;
		if (lValueType == null) {
			lValueType = "ASCII_Short_String_Collapsed";
//			System.out.println("debug writeClassAttribute - FOUND NULL ValueType - lAttr.identifier:" + lAttr.identifier);
		}
			
		// convert the max cardinality for XML Schema
		String cmin = lAttr.cardMin;
		String cmax = lAttr.cardMax;
		if (cmax.compareTo("*") == 0) {
			cmax = "unbounded";
		}
		
		// create the unique attribute identifier
//		String lSchemaId = lAttr.regAuthId + ":" + lAttr.steward.toUpperCase() + ":" + lAttr.className + ":" + lAttr.title;			
	
		if ((choiceBlockOpen) && (! lProp.isChoice || lProp.groupName.compareTo(lGroupName) != 0) ) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
			choiceBlockOpen = false;
			lGroupName = "TBD_groupName";
		}	
		
		if ((lProp.isChoice) && (!choiceBlockOpen)) {
			// set the cardinalities of Master Choice block attributes to (1,1)
			if (lProp.isChoice && ! lProp.isFromLDD) {
				cmin = "1";
				cmax = "1";
			}
			prXML.println(indentSpaces() + "<" + pNS + "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\">");
			upIndentSpaces();
			choiceBlockOpen = true;
			lGroupName = lProp.groupName;
		}
		String minMaxOccursClause = " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"";
		if (choiceBlockOpen) {
			minMaxOccursClause = "";
		}
		
		String nilableClause = "";
		if (lAttr.isNilable) {
			nilableClause = " nillable=\"true\"";
		}

		// write the XML schema statement
		if (! DMDocument.LDDToolFlag) {
			prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lAttr.nameSpaceId  + lAttr.XMLSchemaName + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
		} else {
			if (! (lAttr.isExposed || lAttr.title.compareTo("local_identifier") == 0)) 
				prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + " type=\"" + lAttr.nameSpaceId  + lAttr.XMLSchemaName + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
			else
				prXML.println(indentSpaces() + "<" + pNS + "element ref=\"" + lAttr.nameSpaceId + lAttr.XMLSchemaName + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
		}
		
		// save the attribute's schema name for writing the simpleType statements
		if (! allAttrTypeIdArr.contains(lAttr.XMLSchemaName)) {
			allAttrTypeIdArr.add(lAttr.XMLSchemaName);
			allAttrTypeMap.put(lAttr.XMLSchemaName, lProp);				
		}
		
		// if LDDTool write out annotation with the definition
		if (DMDocument.LDDToolAnnotateDefinitionFlag) {
			prXML.println(indentSpaces() + "<" + pNS + "annotation>");
			prXML.println(indentSpaces() + "  <" + pNS + "documentation>");
			prXML.println(indentSpaces() + lProp.definition + "</" + pNS + "documentation>");
			prXML.println(indentSpaces() + "</" + pNS + "annotation>");  
		}		
		return;
	}
	
//	write the associations in the class
	public void writeClassAssociation (DOMClass lClass, DOMProp lProp, PrintWriter prXML) throws java.io.IOException {
		// get min and max cardinalities for the INSTANCE attribute
		String cmin = lProp.cardMin;
		String cmax = lProp.cardMax;
		if (cmax.compareTo("*") == 0) {
			cmax = "unbounded";
		}
		String minMaxOccursClause = " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"";
		if (lProp.isChoice){
			if (!choiceBlockOpen) {
				String choiceMinMaxOccursClause = "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\"";
				prXML.println(indentSpaces() + "<" + pNS + choiceMinMaxOccursClause+ ">");
				upIndentSpaces();
				choiceBlockOpen = true;
				minMaxOccursClause = "";
			} else {
				minMaxOccursClause = "";
			}
		} else {
			if (choiceBlockOpen) {
				downIndentSpaces();
				prXML.println(indentSpaces() + "</" + pNS + "choice>");
				choiceBlockOpen = false;
			}
		}

		// get each associated class
		DOMClass lAssocClass = (DOMClass) lProp.hasDOMObject;
					
		if (! DMDocument.omitClass.contains(lAssocClass.title)) {
			if (lAssocClass.title.compareTo("Discipline_Facets") == 0) {
				writeXMLClassDisciplineFacet (lClass, prXML);
			} else if (! lAssocClass.isExposed) {
				prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAssocClass.title + "\"" + " type=\"" + lAssocClass.nameSpaceId  + lAssocClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
			} else {
				prXML.println(indentSpaces() + "<" + pNS + "element ref=\"" + lAssocClass.nameSpaceId + lAssocClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
			}
		}
		return;
	}
	
	public void writeVectorUnitAttribute (DOMClass lClass, PrintWriter prXML) throws java.io.IOException {		
//		System.out.println("\ndebug writeVectorUnitAttribute");

		if (lClass.title.compareTo("Vector_Cartesian_3") == 0) {
			prXML.println(indentSpaces() + "<" + pNS + "attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Acceleration") == 0) {
			prXML.println(indentSpaces() + "<" + pNS + "attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Position") == 0) {
			prXML.println(indentSpaces() + "<" + pNS + "attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Velocity") == 0) {
			prXML.println(indentSpaces() + "<" + pNS + "attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		return;
	}	
		
	public void writeXMLClassDisciplineFacet (DOMClass lClass, PrintWriter prXML) throws java.io.IOException {		
//		write attributes
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "discipline_name" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "1" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "facet1" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "subfacet1" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "unbounded" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "facet2" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "subfacet2" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "unbounded" + "\"> </" + pNS + "element>");
		return;
	}	
	
//	write the assertions - simple attribute enumerations
	public void writeAssertionsAttr (ArrayList<DOMAttr> assertArr, PrintWriter prXML) throws java.io.IOException {
		if (assertArr == null || assertArr.size() < 1) return;
		
		String commentPrefix = "<!-- ";
		String commentSuffix = " -->";
		prXML.println(indentSpaces() + commentPrefix + "Begin assert statements for schematron - Enumerated Values" + commentSuffix);

		for (Iterator<DOMAttr> i = assertArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();	
			prXML.print(indentSpaces() + commentPrefix + "<" + pNS + "assert test=\"");
			boolean isFirst = true;
			
			for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				if (isFirst) {
					isFirst = false;
					prXML.print(lAttr.nameSpaceId + lAttr.title + " = ('" + lVal + "'");
				} else {
					prXML.print(", '" + lVal + "'");							
				}
			}
			prXML.println(")\"/>" + commentSuffix);
		}
		prXML.println(indentSpaces() + commentPrefix + "End assert statements for schematron - Enumerated Values" + commentSuffix);
		return;
	}

//	write the class XSAny statements
	public void writeClassXSAnyStmts (PrintWriter prXML) throws java.io.IOException {
		if (xsAnyStmtWritten) return;
		prXML.println("      <!-- When creating a specific XML schema, remove the '" + pNS + "any' element. You may insert any described nondigital object, one or more times. -->");
//		prXML.println("      <" + pNS + "any namespace=\"##other\" processContents=\"lax\" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
		prXML.println("      <" + pNS + "any namespace=\"##other\" processContents=\"strict\" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
		prXML.println("      <!-- <" + pNS + "element name=\"Any_NonDigital_Object\" type=\"pds:Any_NonDigital_Object\" minOccurs=\"0\" maxOccurs=\"unbounded\"> </" + pNS + "element> -->");
		xsAnyStmtWritten = true;
		return;
	}

//	write the Type List XSAny statements
	public void writeClassXSAnyTypeList (PrintWriter prXML) throws java.io.IOException {
		if (xsAnyStmtWritten) return;
		prXML.println("      <!-- When creating a context product you must insert a type list. -->");
		prXML.println("      <" + pNS + "any namespace=\"##other\" processContents=\"strict\" minOccurs=\"1\" maxOccurs=\"1\" />");
		prXML.println("      <!-- <" + pNS + "element name=\"Type_List\" type=\"ctli:Type_List\" minOccurs=\"1\" maxOccurs=\"1\"> </" + pNS + "element> -->");
		xsAnyStmtWritten = true;
		return;
	}
	
//	write the mission and node areas
	public void writeClassXSAny (DOMClass lClass, PrintWriter prXML) throws java.io.IOException {
		prXML.println("");
		prXML.println("  <" + pNS + "complexType name=\"" + lClass.title + "\">");
		prXML.println("    <" + pNS + "annotation>");
		prXML.print  ("      <" + pNS + "documentation>");
		prXML.print  (" " + lClass.definition);
		prXML.println(" </" + pNS + "documentation>");
		prXML.println("    </" + pNS + "annotation>");
		prXML.println("    <" + pNS + "sequence>");
		prXML.println("      <!-- When creating a specific XML schema, remove the '" + pNS + "any' element. You may insert any described nondigital object, one or more times. -->");
//		prXML.println("      <" + pNS + "any namespace=\"##other\" processContents=\"lax\" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
		prXML.println("      <" + pNS + "any namespace=\"##other\" processContents=\"strict\" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
		prXML.println("      <!-- <" + pNS + "element name=\"Any_NonDigital_Object\" type=\"pds:Any_NonDigital_Object\" minOccurs=\"0\" maxOccurs=\"unbounded\"> </" + pNS + "element> -->");
		prXML.println("    </" + pNS + "sequence>");
		prXML.println("  </" + pNS + "complexType>");		
		return;
	}
	
//	write the attributes
	public void writeXMLAttributes (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {	
//		Write the header statements
		prXML.println("");
		prXML.println("    <" + pNS + "annotation>");
		prXML.println("      <" + pNS + "documentation>This section contains the simpleTypes that provide more constraints");
		prXML.println("        than those at the base data type level. The simpleTypes defined here build on the base data");
		prXML.println("        types. This is another component of the common dictionary and therefore falls within the");
		prXML.println("        " + lSchemaFileDefn.nameSpaceIdNC + " namespace.");
		prXML.println("      </" + pNS + "documentation>");
		prXML.println("    </" + pNS + "annotation>");		
		
//		Write the Standard Data Types that were used.
		
		// first add in any LDD attributes that are not owned by a class
		if (lSchemaFileDefn.isLDD) {
			ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> (DOMInfoModel.userSingletonDOMClassAttrIdMap.values());
			for (Iterator<DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
				DOMAttr lAttr = (DOMAttr) i.next();
				if (lAttr.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;
				DOMProp lDOMProp = new DOMProp();
				lDOMProp.createDOMPropSingletonsNoAssoc(lAttr);
				allAttrTypeMap.put(lAttr.XMLSchemaName, lDOMProp);	
			}
		}
		writeXMLExtendedTypes (lSchemaFileDefn, allAttrTypeMap, prXML);
	}	
			
//	write the attribute types
	public void writeXMLExtendedTypes (SchemaFileDefn lSchemaFileDefn, TreeMap <String, DOMProp> allAttrTypeMap, PrintWriter prXML) throws java.io.IOException {
		boolean hasUnits;
		ArrayList <DOMProp> lPropArr = new ArrayList <DOMProp> (allAttrTypeMap.values());
		for (Iterator<DOMProp> i = lPropArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
		    DOMAttr lAttr = (DOMAttr)lProp.hasDOMObject;

		
			if (lAttr.nameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;
			hasUnits = false;
			
			if (lAttr.unit_of_measure_type.indexOf("TBD") != 0) hasUnits = true;
			if (! lAttr.isNilable || hasUnits) {
				writeXMLExtendedRestrictedNonEnumerated (lSchemaFileDefn, hasUnits, lAttr, prXML);			
			} else {
				prXML.println("");
				prXML.println("  <" + pNS + "complexType name=\"" + lAttr.XMLSchemaName + "\">");
				prXML.println("    <" + pNS + "annotation>");		
//				prXML.println(DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>", 6, 72));		
				String lLine = "<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>";		
				DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, 6, 72 ), prXML);
				prXML.println("    </" + pNS + "annotation>");		
				prXML.println("    <" + pNS + "simpleContent>");
				prXML.println("      <" + pNS + "extension base=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.valueType + "\">");
				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"" + DMDocument.masterNameSpaceIdNCLC + ":nil_reason\" use=\"optional\" />");
				prXML.println("      </" + pNS + "extension>");
				prXML.println("    </" + pNS + "simpleContent>");
				prXML.println("  </" + pNS + "complexType>");
			}
		}
	}

	//	write the attribute
	public void writeXMLExtendedRestrictedNonEnumerated (SchemaFileDefn lSchemaFileDefn, boolean hasUnits, DOMAttr lAttr, PrintWriter prXML) throws java.io.IOException {
		String lValue;
		
		if (hasUnits) {
			prXML.println("");
			prXML.println("  <" + pNS + "simpleType name=\"" + lAttr.XMLSchemaName + "_WO_Units\">");
		} else {
			prXML.println("");
			prXML.println("  <" + pNS + "simpleType name=\"" + lAttr.XMLSchemaName + "\">");
			prXML.println("    <" + pNS + "annotation>");		
//			prXML.println(DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>", 6, 72));		
			String lLine = "<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>";		
			DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, 6, 72 ), prXML);
			prXML.println("    </" + pNS + "annotation>");
		}
	
		prXML.println("    <" + pNS + "restriction base=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.getValueType (true) + "\">");
		
		lValue = lAttr.getFormat (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("  <!-- format=\"" + lValue + "\" -->");
		}
		
		lValue = lAttr.getPattern (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("    	<" + pNS + "pattern value='" + lValue + "'/>");
		}
		
		// if valueType is non-character then write min/max value, otherwise write min/max lengths.
		if (! lAttr.isCharDataType) {
			lValue = lAttr.getMinimumValue(true, true);
			if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
				prXML.println("     <" + pNS + "minInclusive value=\"" + lValue + "\"/>");
			}
			
			lValue = lAttr.getMaximumValue(true, true);
			if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
				prXML.println("     <" + pNS + "maxInclusive value=\"" + lValue + "\"/>");
			}
		} else {
			lValue = lAttr.getMinimumCharacters(true, true);
			if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
				prXML.println("     <" + pNS + "minLength value=\"" + lValue + "\"/>");
			}

			lValue = lAttr.getMaximumCharacters(true, true);
			if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
				prXML.println("     <" + pNS + "maxLength value=\"" + lValue + "\"/>");
			}
		}

		prXML.println("	   </" + pNS + "restriction>");
		prXML.println("  </" + pNS + "simpleType>");
		
		if (hasUnits) {
			prXML.println("");
			prXML.println("  <" + pNS + "complexType name=\"" + lAttr.XMLSchemaName + "\">");
			prXML.println("    <" + pNS + "annotation>");		
//			prXML.println(DOMInfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>", 6, 72));		
			String lLine = "<" + pNS + "documentation>" + lAttr.definition + "</" + pNS + "documentation>";		
			DOMInfoModel.printWrappedTextArr (DOMInfoModel.wrapTextNew(lLine, 6, 72 ), prXML);
			prXML.println("    </" + pNS + "annotation>");		
			prXML.println("    <" + pNS + "simpleContent>");
			prXML.println("      <" + pNS + "extension base=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.XMLSchemaName + "_WO_Units\">");
			prXML.println("        <" + pNS + "attribute name=\"unit\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.unit_of_measure_type  + "\" use=\"required\" />");
			if (lAttr.isNilable) {
				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"" + DMDocument.masterNameSpaceIdNCLC + ":nil_reason\" use=\"optional\" />");
			}
			prXML.println("      </" + pNS + "extension>");
			prXML.println("    </" + pNS + "simpleContent>");
			prXML.println("  </" + pNS + "complexType>");
		}
	}

//	write the XML Schema data types 
	public void writeXMLDataTypes (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
//		Sort the data types			
		TreeMap <String, DOMClass> sortDataTypeMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!(lClass.isDataType && lClass.subClassOfTitle.compareTo("Character_Data_Type") == 0)) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}	
		ArrayList <DOMClass> sortDataTypeArr = new ArrayList <DOMClass> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
		
		
//		Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section contains the base data types for " + lSchemaFileDefn.modelShortName + " and any constraints those types");
	    prXML.println("        may have. These types should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		
//		Write the data types
		for (Iterator<DOMClass> i = sortDataTypeArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			boolean hasPattern = false;
			boolean hasCharacterConstraint = false;

			prXML.println("");
			prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (lProp.title.compareTo("xml_schema_base_type") == 0) {
					DOMAttr lAttr = (DOMAttr)lProp.hasDOMObject;
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					lVal = DMDocument.replaceString (lVal, "xsd:", "");
					if (lVal != null) {
						prXML.println("      <" + pNS + "restriction base=\"" + pNS + lVal + "\">");
					} else {
						prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");							
					}
				}
			}
//			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
			// use attribute list sorted by classOrder
			for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				DOMAttr lAttr = (DOMAttr)lProp.hasDOMObject;
				if (lProp.title.compareTo("character_constraint") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						hasCharacterConstraint = true;
					}
				}

				if (lProp.title.compareTo("minimum_characters") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("-2147483648") == 0)) {
					    prXML.println("        <" + pNS + "minLength value=\"" + lVal + "\"/>");
					}
				}
				if (lProp.title.compareTo("maximum_characters") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("2147483647") == 0)) {
					    prXML.println("        <" + pNS + "maxLength value=\"" + lVal + "\"/>");
					}
				}
				if (lProp.title.compareTo("minimum_value") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("-2147483648") == 0)) {
					    prXML.println("        <" + pNS + "minInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lProp.title.compareTo("maximum_value") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("2147483647") == 0)) {
					    prXML.println("        <" + pNS + "maxInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lProp.title.compareTo("pattern") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						// if not null there there are one or more patterns
						hasPattern = true;
						for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String lPattern = (String) k.next();
							lPattern = DOMInfoModel.escapeXMLChar(lPattern);
							prXML.println("        <" + pNS + "pattern value='" + lPattern + "'/>");
						}
					}
				}
			}
			if (hasCharacterConstraint && (! hasPattern)) {
			    prXML.println("        <" + pNS + "pattern value='" + "\\p{IsBasicLatin}*" + "'/>");
			}
		    prXML.println("      </" + pNS + "restriction>");
		    prXML.println("    </" + pNS + "simpleType>");
		}
	}
	
//	write the XML Schema Units Of Measure
	public void writeXMLUnitsOfMeasure(SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
//		Sort the unit types
		TreeMap <String, DOMClass> sortDataTypeMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!(lClass.subClassOfTitle.compareTo("Unit_Of_Measure") == 0)) continue;
//  222 needs work to omit unit_of_measure			if (!(lClass.isUnitOfMeasure)) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}
		ArrayList <DOMClass> sortDataTypeArr = new ArrayList <DOMClass> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
		
//		Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section contains the base Units of Measure for " + lSchemaFileDefn.modelShortName + ".");
	    prXML.println("        These Units of Measure should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		
//		Write the unit types		
		for (Iterator<DOMClass> i = sortDataTypeArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			prXML.println("");
			prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");							
			// use attribute list sorted by classOrder
			for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lProp.hasDOMObject;
					if (lDOMAttr != null) {
						if (lDOMAttr.title.compareTo("unit_id") == 0) {
						ArrayList <String> lValArr = DOMInfoModel.getMultipleValue(lDOMAttr.valArr);
							if (lValArr != null) {
								for (Iterator<String> k = lValArr.iterator(); k.hasNext();) {
									String lVal = (String) k.next();
									prXML.println("        <" + pNS + "enumeration value=\"" + lVal + "\"></" + pNS + "enumeration>");
								}
							}
						}
					}
				}
			}
			prXML.println("      </" + pNS + "restriction>");
			prXML.println("    </" + pNS + "simpleType>");
		}
	}	
	
//	write the XML Schema Nill Values 
	public void writeXMLNillValues(SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) return;		
		// write the nil reasons
		
	    prXML.println("");
	    prXML.println("    <" + pNS + "simpleType name=\"nil_reason\" >");
	    prXML.println("      <" + pNS + "list itemType='" + DMDocument.masterNameSpaceIdNCLC + ":nil_reason_list'>");
	    prXML.println("      </" + pNS + "list>");
	    prXML.println("    </" + pNS + "simpleType>");
	    
	    prXML.println("    <" + pNS + "simpleType name=\"nil_reason_list\">");
	    prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");
	    prXML.println("        <" + pNS + "enumeration value=\"inapplicable\"></" + pNS + "enumeration>");
	    prXML.println("        <" + pNS + "enumeration value=\"anticipated\"></" + pNS + "enumeration>");
	    prXML.println("        <" + pNS + "enumeration value=\"missing\"></" + pNS + "enumeration>");
	    prXML.println("        <" + pNS + "enumeration value=\"unknown\"></" + pNS + "enumeration>");
	    prXML.println("      </" + pNS + "restriction>");
	    prXML.println("    </" + pNS + "simpleType>");
	}	

	public void writeDeprecatedItems (PrintWriter prXML) throws java.io.IOException {
		if (DMDocument.importJSONAttrFlag) return;
		prXML.println(" ");
		prXML.println("<!-- Deprecated Items - Begin ");
		prXML.println(" ");
		prXML.println("     - Classes -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (! lDeprecatedDefn.isAttribute) {
				String lValue = lDeprecatedDefn.classNameSpaceIdNC + ":" + lDeprecatedDefn.className;
				prXML.println("     " + lValue);
			}
		}
		prXML.println(" ");
		prXML.println("     - Attributes -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (lDeprecatedDefn.isAttribute && ! lDeprecatedDefn.isValue) {
				String lValue = lDeprecatedDefn.classNameSpaceIdNC + ":" + lDeprecatedDefn.className + "/" + lDeprecatedDefn.classNameSpaceIdNC + ":" + lDeprecatedDefn.attrName;
		    	prXML.println("     " + lValue);
			}
		}	
    	prXML.println(" ");
    	prXML.println("     - Permissible Values -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (lDeprecatedDefn.isAttribute && lDeprecatedDefn.isValue) {
				String lValue = lDeprecatedDefn.classNameSpaceIdNC + ":" + lDeprecatedDefn.className + "/" + lDeprecatedDefn.classNameSpaceIdNC + ":" + lDeprecatedDefn.attrName + " - " + lDeprecatedDefn.value;
		    	prXML.println("     " + lValue);
			}
		}
    	prXML.println(" ");
    	prXML.println("     Deprecated Items - End -->");
    	prXML.println(" ");
	}
	
	public void writeLDDClassElementDefinition (SchemaFileDefn lSchemaFileDefn, ArrayList <DOMClass> lClassArr, PrintWriter prXML) throws java.io.IOException {
		prXML.println(" ");
		for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isAbstract) continue;
			if (! lClass.isReferencedFromLDD) {
				prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
			} else {
				prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lClass.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
			}
		}
	}
	
	public void writeUserAttributeElementDefinitons (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		prXML.println("");
		
		// get all Owned variables for any class that is a child of the USER class
		ArrayList <DOMAttr> lAttrArr = new ArrayList <DOMAttr> (DOMInfoModel.userClassAttrIdMap.values());
		for (Iterator<DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
			
			// only write the attributes in this schema's namespace
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lDOMAttr.nameSpaceIdNC) != 0) continue;
			
			// kludge for common only; fix for LDD
//			if (lAttr.attrNameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) continue;
			if (! (lDOMAttr.isUsedInClass && lDOMAttr.isAttribute && lDOMAttr.isPDS4)) continue;
			
			String nilableClause = "";
			if (lDOMAttr.isNilable) {
				nilableClause = " nillable=\"true\"";
			}
			
			if (lDOMAttr.hasAttributeOverride) {
				prXML.println("  <" + pNS + "element name=\"" + lDOMAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lDOMAttr.XMLSchemaName + "\">" + " </" + pNS + "element>");
			} else {
				prXML.println("  <" + pNS + "element name=\"" + lDOMAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lDOMAttr.valueType     + "\">" + " </" + pNS + "element>");
			}
		}
	}
	
	public void writeLDDAttributeElementDefinitons (ArrayList <DOMAttr> lAttrArr, SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		for (Iterator<DOMAttr> i = lAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.hasAttributeOverride) {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\" type=\"" + lAttr.nameSpaceId  + lAttr.XMLSchemaName + "\"> </" + pNS + "element>");
			} else {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.valueType + "\"> </" + pNS + "element>");
			}
		}
	}	
	
	public void ResetIndentSpaces () {
		indentSpaces = 0;
	}
	
	public void upIndentSpaces () {
		indentSpaces++;
		if (indentSpaces > 10) {
			indentSpaces = 10;
		}
		return;
	}
	
	public void downIndentSpaces () {
		indentSpaces--;
		if (indentSpaces <= 0) {
			indentSpaces = 0;
		}
		return;
	}
	
	public String indentSpaces () {
		return indentSpacesString.substring(0, (indentSpaces * 2));
	}
		
	public String indentSpacesUp () {
		indentSpaces++;
		if (indentSpaces > 10) {
			indentSpaces = 10;
		}
		return indentSpacesString.substring(0, (indentSpaces * 2));
	}
	
	public String indentSpacesDown () {
		indentSpaces--;
		if (indentSpaces <= 0) {
			indentSpaces = 0;
			return "";
		}
		return indentSpacesString.substring(0, (indentSpaces * 2));
	}
}
