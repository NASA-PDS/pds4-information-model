package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

//This code writes the information model to XML schema, attempting to replicate the class hierarchy.
//It is written to a single schema file (single namespace).

class XML4LabelSchema extends Object {
	PrintWriter prXML;
	ArrayList <String> allAttrTypeIdArr = new ArrayList<String> ();
	TreeMap <String, AttrDefn> allAttrTypeMap = new TreeMap <String, AttrDefn> ();
	TreeMap <String, PDSObjDefn> classHierMap = new TreeMap <String, PDSObjDefn> ();
	String extensionRestrictionDeclaration;
	String pNS = "xs:";
	int indentSpaces;
	String indentSpacesString = "                    ";
	boolean choiceBlockOpen = false;
	String lGroupName = "TBD_groupName";
	boolean xsAnyStmtWritten = false;

	public XML4LabelSchema () {
		return;
	}

//	write the XML Label
	public void writeXMLSchemaFiles (SchemaFileDefn lSchemaFileDefn, ArrayList <PDSObjDefn> lInputClassArr) throws java.io.IOException {
		// get the classes
		classHierMap = getPDS4ClassesForSchema (lSchemaFileDefn, lInputClassArr);
		String lFileName = lSchemaFileDefn.relativeFileSpecXMLSchema;
		prXML = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));		
		
//		write the XML Schema File Header
		writeXMLSchemaFileHeader (lSchemaFileDefn, prXML);
		
		// for non-LDDTool runs, write the class Elements (aka expose classes)
		if (! DMDocument.LDDToolFlag) {	
			prXML.println(" ");
			ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
			for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) i.next();
				if (! (lClass.isExposed)) continue;
				if (lClass.isAbstract) continue;
				if (! (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0)) continue;
				if (! lClass.isReferencedFromLDD) {
					prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
				} else {
					prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lClass.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
				}
			}
		}
		
		// for LDDTool runs and Class flag on, write the Class Elements for all schemas except the master schema 
		if (DMDocument.LDDToolFlag) {	
			prXML.println(" ");
			if (lSchemaFileDefn.isLDD && lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) {
				ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
				for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
					PDSObjDefn lClass = (PDSObjDefn) i.next();
					if (lClass.isAbstract) continue;
					if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
					if (! lClass.isFromLDD) continue;
					if (! lClass.isLDDElement)  continue;
					if (! lClass.isReferencedFromLDD) {
						prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
					} else {
						prXML.println("  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lClass.nameSpaceIdNC + ":" + lClass.title + "\"> </" + pNS + "element>");
					}
				}
			} 		
		}
		
		// write the Attribute Element definition statements
		if (lSchemaFileDefn.isLDD && DMDocument.LDDAttrElementFlag) {
			ArrayList <AttrDefn> lFilteredAttrArr = new ArrayList <AttrDefn> ();
			for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (! lAttr.isAttribute) continue;
				if (! lAttr.isFromLDD) continue;
				if (lAttr.attrNameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;
				if (lAttr.parentClassTitle.indexOf("TBD") == 0) continue;
				lFilteredAttrArr.add(lAttr);
			}
			writeLDDAttributeElementDefinitons(lFilteredAttrArr, prXML);			
		}
				
//		Write the classes
		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (classHierMap.values());
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();

			// skip the subclasses of Science_Facets
			if (lClass.title.compareTo("Discipline_Facets") == 0) continue;
			if (lClass.title.compareTo("Group_Facet1") == 0) continue;
			if (lClass.title.compareTo("Group_Facet2") == 0) continue;
			
			ResetIndentSpaces();
			
			// determine extension, restriction, neither
			boolean isExtension = MasterInfoModel.isExtendedClass(lClass, lClass.subClassOfInst);
			boolean isRestriction = MasterInfoModel.isRestrictedClass(lClass, lClass.subClassOfInst);
		
			if (! isExtension) {
				isRestriction = true;
			}
			if (lClass.subClassOfInst != null) {
				PDSObjDefn lParentClass = lClass.subClassOfInst;
				if (lParentClass.isVacuous || (lParentClass.title.compareTo("USER") == 0)) {
					isExtension = false;
					isRestriction = false;
				}
			}
			boolean isBothExtensionRestriction = isExtension && isRestriction;		
// 777
//			System.out.println("\ndebug writeXMLSchemaFiles lClass.identifier:" + lClass.identifier);
//			System.out.println("debug                     isExtension:" + isExtension);
//			System.out.println("debug                     isRestriction:" + isRestriction);
//			System.out.println("debug                     isBothExtensionRestriction:" + isBothExtensionRestriction);
			
			// write the classes
			xsAnyStmtWritten = false;
			if (isBothExtensionRestriction) {
				writeXMLClassExtension (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prXML);
				writeXMLClassRestriction (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prXML);
			} else if (isExtension) {
				writeXMLClassExtension (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prXML);
			} else if (isRestriction) {
				writeXMLClassRestriction (lSchemaFileDefn, isBothExtensionRestriction, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prXML);
			} else {
				writeXMLClassNeither (lSchemaFileDefn, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prXML);				
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
			if (! DMDocument.importJSONAttrFlag)
				writeXMLNillValues (lSchemaFileDefn, prXML);
    	}
		
		if (DMDocument.LDDToolFlag  && DMDocument.LDDNuanceFlag) {
			WritePropMapsSchema lWritePropMapsSchema = new WritePropMapsSchema ();
			lWritePropMapsSchema.writePropertyMaps (prXML);
		}
		
       	// write the deprecated items
    	if (lSchemaFileDefn.isMaster) {
    		writeDeprecatedItems (prXML);
    	}
    	
    	// write the footer
    	writeXMLSchemaFileFooter (prXML);
    	
//		close the file
    	prXML.close();
    	
		if (DMDocument.debugFlag) System.out.println("debug writeXMLSchemaFiles Done");
	}	
	
	// get the PDS4 classes (Products ?)
	public TreeMap <String, PDSObjDefn> getPDS4ClassesForSchema (SchemaFileDefn lSchemaFileDefn, ArrayList <PDSObjDefn> lClassArr) {	
		TreeMap <String, PDSObjDefn> lClassMap = new TreeMap <String, PDSObjDefn> ();
		int classCount = 0;
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
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
			prXML.println("    targetNamespace=\"" + lSchemaFileDefn.nameSpaceURL + governanceDirectory + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:" + lSchemaFileDefn.nameSpaceIdNC + "=\"" + lSchemaFileDefn.nameSpaceURL + governanceDirectory + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");

			// namespaces required: pds - latest version
			prXML.println("    xmlns:" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");

			// namespaces required: all other discipline levels referenced; no mission level allowed - prior versions
			if (DMDocument.LDDToolFlag) {
				for (Iterator<String> i = DMDocument.LDDImportNameSpaceIdNCArr.iterator(); i.hasNext();) {
					String lNameSpaceIdNC = (String) i.next();
					prXML.println("    xmlns:" + lNameSpaceIdNC + "=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + lNameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
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
			prXML.println("    <" + pNS + "import namespace=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURLs + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/PDS4_PDS_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\"/>");	
			// imports required: all other LDD discipline levels referenced; no mission level allowed
			for (Iterator<String> i = DMDocument.LDDImportNameSpaceIdNCArr.iterator(); i.hasNext();) {
				String lNameSpaceIdNC = (String) i.next();
				prXML.println("    <" + pNS + "import namespace=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + lNameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceURL + lNameSpaceIdNC + "/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/PDS4_" + lNameSpaceIdNC.toUpperCase() + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\"/>");	
			}
		}
		
		prXML.println(" ");		
		prXML.println("  <" + pNS + "annotation>");		
		if (! DMDocument.LDDToolFlag) {
			prXML.println(InfoModel.wrapText ("<" + pNS + "documentation>" + lSchemaFileDefn.comment + "</" + pNS + "documentation>", (indentSpaces + 2) * 2, 72));		
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
	
	public void writeXMLClassExtension (SchemaFileDefn lSchemaFileDefn, boolean isBothExtensionRestriction, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitClassList, ArrayList<PDSObjDefn> recurseClassList, PrintWriter prXML) throws java.io.IOException {
		// get proper names		
		String lClassName = lClass.title;
		String lSuperClassName = lClass.subClassOfTitle ;
		if (isBothExtensionRestriction) {
			lClassName = lClass.subClassOfTitle + "-" + lClass.title;
		}
		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.println (InfoModel.wrapText ("<" + pNS + "documentation>" + lClass.description + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
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
				writeAssertionsAttr (lClass.ownedAttrAssocAssertArr, prXML);	
			}
		}	
		prXML.println(indentSpacesDown() + "</" + pNS + "extension>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexContent>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}
		
	public void writeXMLClassRestriction (SchemaFileDefn lSchemaFileDefn, boolean isBothExtensionRestriction, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitClassList, ArrayList<PDSObjDefn> recurseClassList, PrintWriter prXML) throws java.io.IOException {		
		// get proper names
		String lClassName = lClass.title;
		String lSuperClassName = lClass.subClassOfTitle ;
		if (isBothExtensionRestriction) {
			lSuperClassName = lSuperClassName + "-" + lClass.title;
		}		
		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.println (InfoModel.wrapText ("<" + pNS + "documentation>" + lClass.description + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
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
			writeAssertionsAttr (lClass.ownedAttrAssocAssertArr, prXML);	
		}
		prXML.println(indentSpacesDown() + "</" + pNS + "restriction>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexContent>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}	
	
	public void writeXMLClassNeither (SchemaFileDefn lSchemaFileDefn, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitClassList, ArrayList<PDSObjDefn> recurseClassList, PrintWriter prXML) throws java.io.IOException {		
		prXML.println("");
		prXML.println(indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClass.title + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.println (InfoModel.wrapText ("<" + pNS + "documentation>" + lClass.description + "</" + pNS + "documentation>", (indentSpaces + 1) * 2, 72));		
		prXML.println(indentSpaces() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "sequence" + ">");
		
		// write attributes and associations
		writeAttrAssocInClass(lClass, lClass.allAttrAssocArr, prXML);
		
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);

		// write assertions for enumerated values
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0 ) {		
			writeAssertionsAttr (lClass.ownedAttrAssocAssertArr, prXML);
		}
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}
	
//	write the attributes and associations as element definitions, components of the element that represents the class
	public void writeAttrAssocInClass (PDSObjDefn lClass, ArrayList <AttrDefn> lAttrAssocArr, PrintWriter prXML) throws java.io.IOException {
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
		for (Iterator<AttrDefn> i = lAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isAny) writeClassXSAnyStmts (prXML);
			if (lAttr.isAttribute) {
				writeClassAttribute (lClass, lAttr, prXML);
				
			// if non PDS run then do not process selected relations
			} else if (! (DMDocument.importJSONAttrFlag && ( (lAttr.title.indexOf("_has_")  > -1) || (lAttr.title.indexOf("_to_")  > -1 ) ) ) ) {
				
				// if the choice block is open, close it
				if (choiceBlockOpen) {
					downIndentSpaces();
					prXML.println(indentSpaces() + "</" + pNS + "choice>");
					choiceBlockOpen = false;
				}
				writeClassAssociation (lClass, lAttr, prXML);
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
	public void writeClassAttribute (PDSObjDefn lClass, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
		// capture the data types that are used; will write only those used.
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
	
		if ((choiceBlockOpen) && (! lAttr.isChoice || lAttr.groupName.compareTo(lGroupName) != 0) ) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
			choiceBlockOpen = false;
			lGroupName = "TBD_groupName";
		}	
		
		if ((lAttr.isChoice) && (!choiceBlockOpen)) {
			// set the cardinalities of Master Choice block attributes to (1,1)
//			if (lAttr.isChoice && ! lAttr.isFromLDD) {
//				cmin = "1";
//				cmax = "1";
//			}
			prXML.println(indentSpaces() + "<" + pNS + "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\">");
			upIndentSpaces();
			choiceBlockOpen = true;
			lGroupName = lAttr.groupName;
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
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lAttr.attrNameSpaceId  + lAttr.XMLSchemaName + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
		
		// save the attribute's schema name for writing the simpleType statements
		if (! allAttrTypeIdArr.contains(lAttr.XMLSchemaName)) {
			allAttrTypeIdArr.add(lAttr.XMLSchemaName);
			allAttrTypeMap.put(lAttr.XMLSchemaName, lAttr);				
		}
		
		// if LDDTool write out annotation with the definition
		if (DMDocument.LDDToolAnnotateDefinitionFlag) {
			prXML.println(indentSpaces() + "<" + pNS + "annotation>");
			prXML.println(indentSpaces() + "  <" + pNS + "documentation>");
			prXML.println(indentSpaces() + lAttr.description + "</" + pNS + "documentation>");
			prXML.println(indentSpaces() + "</" + pNS + "annotation>");  
		}		
		return;
	}
	
//	write the associations in the class
	public void writeClassAssociation (PDSObjDefn lClass, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
		// get min and max cardinalities for the INSTANCE attribute
		String cmin = lAttr.cardMin;
		String cmax = lAttr.cardMax;
		if (cmax.compareTo("*") == 0) {
			cmax = "unbounded";
		}
		String minMaxOccursClause = " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"";			

		if (lAttr.isChoice) {
			minMaxOccursClause = "";
//			String choiceMinMaxOccursClause = "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\"";
			prXML.println(indentSpaces() + "<" + pNS + "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\"" + ">");
			upIndentSpaces();
		}
		
		// get each associated class
		if (! (lAttr.valClassArr == null || lAttr.valClassArr.isEmpty())) {
			for (Iterator<PDSObjDefn> j = lAttr.valClassArr.iterator(); j.hasNext();) {				
				PDSObjDefn lCompClass = (PDSObjDefn) j.next();				
				if (! DMDocument.omitClass.contains(lCompClass.title)) {
					if (lCompClass.title.compareTo("Discipline_Facets") == 0)
						writeXMLClassDisciplineFacet (lClass, prXML);
					else
//						prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lCompClass.title + "\"" + " type=\"" + lCompClass.nameSpaceId  + lCompClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
						if (! lCompClass.isExposed)
							prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lCompClass.title + "\"" + " type=\"" + lCompClass.nameSpaceId  + lCompClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
						else
							prXML.println(indentSpaces() + "<" + pNS + "element ref=\"" + lCompClass.nameSpaceId + lCompClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
				}							
			}
		}
		
		if (lAttr.isChoice) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
		}		
		return;
	}
	
	public void writeVectorUnitAttribute (PDSObjDefn lClass, PrintWriter prXML) throws java.io.IOException {		
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
		
	public void writeXMLClassDisciplineFacet (PDSObjDefn lClass, PrintWriter prXML) throws java.io.IOException {		
//		write attributes
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "discipline_name" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "1" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "facet1" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "subfacet1" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "unbounded" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "facet2" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "1" + "\"> </" + pNS + "element>");
		prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + "subfacet2" + "\"" + "" + " type=\"" + lClass.nameSpaceId  + "ASCII_Short_String_Collapsed" + "\"" + " minOccurs=\"" + "0" + "\"" + " maxOccurs=\"" + "unbounded" + "\"> </" + pNS + "element>");
		return;
	}	
	
//	write the assertions - simple attribute enumerations
	public void writeAssertionsAttr (ArrayList<AttrDefn> assertArr, PrintWriter prXML) throws java.io.IOException {
		if (assertArr == null || assertArr.size() < 1) return;
		
		String commentPrefix = "<!-- ";
		String commentSuffix = " -->";
		prXML.println(indentSpaces() + commentPrefix + "Begin assert statements for schematron - Enumerated Values" + commentSuffix);

		for (Iterator<AttrDefn> i = assertArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();	
			prXML.print(indentSpaces() + commentPrefix + "<" + pNS + "assert test=\"");
			boolean isFirst = true;
			for (Iterator<String> j = lAttr.valArr.iterator(); j.hasNext();) {
				String lVal = (String) j.next();
				if (isFirst) {
					isFirst = false;
					prXML.print(lAttr.attrNameSpaceId + lAttr.title + " = ('" + lVal + "'");
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
	public void writeClassXSAny (PDSObjDefn lClass, PrintWriter prXML) throws java.io.IOException {
		prXML.println("");
		prXML.println("  <" + pNS + "complexType name=\"" + lClass.title + "\">");
		prXML.println("    <" + pNS + "annotation>");
		prXML.print  ("      <" + pNS + "documentation>");
		prXML.print  (" " + lClass.description);
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
		prXML.println("        common namespace.");
		prXML.println("      </" + pNS + "documentation>");
		prXML.println("    </" + pNS + "annotation>");		
		
//		Write the Standard Data Types that were used.
		
		// first add in any LDD attributes that are not owned by a class
		if (lSchemaFileDefn.isLDD) {
			ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.userSingletonClassAttrIdMap.values());
			for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (lAttr.attrNameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;;
				allAttrTypeMap.put(lAttr.XMLSchemaName, lAttr);	
			}
		}
		writeXMLExtendedTypes (lSchemaFileDefn, allAttrTypeMap, prXML);
	}	
			
//	write the attribute types
	public void writeXMLExtendedTypes (SchemaFileDefn lSchemaFileDefn, TreeMap <String, AttrDefn> allAttrTypeMap, PrintWriter prXML) throws java.io.IOException {
		boolean hasUnits;
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (allAttrTypeMap.values());
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.attrNameSpaceIdNC.compareTo(lSchemaFileDefn.nameSpaceIdNC) != 0) continue;
			hasUnits = false;
			if (lAttr.unit_of_measure_type.indexOf("TBD") != 0) hasUnits = true;
			if (! lAttr.isNilable || hasUnits) {
				writeXMLExtendedRestrictedNonEnumerated (lSchemaFileDefn, hasUnits, lAttr, prXML);			
			} else {
				prXML.println("");
				prXML.println("  <" + pNS + "complexType name=\"" + lAttr.XMLSchemaName + "\">");
				prXML.println("    <" + pNS + "annotation>");		
				prXML.println(InfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.description + "</" + pNS + "documentation>", 6, 72));		
				prXML.println("    </" + pNS + "annotation>");		
				prXML.println("    <" + pNS + "simpleContent>");
				prXML.println("      <" + pNS + "extension base=\"pds:" + lAttr.valueType + "\">");
				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"pds:nil_reason\" use=\"optional\" />");
				prXML.println("      </" + pNS + "extension>");
				prXML.println("    </" + pNS + "simpleContent>");
				prXML.println("  </" + pNS + "complexType>");
			}
		}
	}

	//	write the attribute
	public void writeXMLExtendedRestrictedNonEnumerated (SchemaFileDefn lSchemaFileDefn, boolean hasUnits, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
		String lValue;
		if (hasUnits) {
			prXML.println("");
			prXML.println("  <" + pNS + "simpleType name=\"" + lAttr.XMLSchemaName + "_WO_Units\">");
		} else {
			prXML.println("");
			prXML.println("  <" + pNS + "simpleType name=\"" + lAttr.XMLSchemaName + "\">");
			prXML.println("    <" + pNS + "annotation>");		
			prXML.println(InfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.description + "</" + pNS + "documentation>", 6, 72));		
			prXML.println("    </" + pNS + "annotation>");
		}
		
		prXML.println("    <" + pNS + "restriction base=\"" + DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNCLC + ":" + lAttr.getValueType (true) + "\">");
		
		lValue = lAttr.getFormat (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("  <!-- format=\"" + lValue + "\" -->");
		}
		
		lValue = lAttr.getPattern (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("    	<" + pNS + "pattern value='" + lValue + "'/>");
		}
		
		lValue = lAttr.getMinimumValue2 (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "minInclusive value=\"" + lValue + "\"/>");
		}
		
		lValue = lAttr.getMaximumValue2 (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "maxInclusive value=\"" + lValue + "\"/>");
		}
		
		lValue = lAttr.getMinimumCharacters2 (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "minLength value=\"" + lValue + "\"/>");
		}

		lValue = lAttr.getMaximumCharacters2 (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "maxLength value=\"" + lValue + "\"/>");
		}

		prXML.println("	   </" + pNS + "restriction>");
		prXML.println("  </" + pNS + "simpleType>");
		
		if (hasUnits) {
			prXML.println("");
			prXML.println("  <" + pNS + "complexType name=\"" + lAttr.XMLSchemaName + "\">");
			prXML.println("    <" + pNS + "annotation>");		
			prXML.println(InfoModel.wrapText ("<" + pNS + "documentation>" + lAttr.description + "</" + pNS + "documentation>", 6, 72));		
			prXML.println("    </" + pNS + "annotation>");		
			prXML.println("    <" + pNS + "simpleContent>");
			prXML.println("      <" + pNS + "extension base=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.XMLSchemaName + "_WO_Units\">");
			prXML.println("        <" + pNS + "attribute name=\"unit\" type=\"pds:" + lAttr.unit_of_measure_type  + "\" use=\"required\" />");
			if (lAttr.isNilable) {
				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"pds:nil_reason\" use=\"optional\" />");
			}
			prXML.println("      </" + pNS + "extension>");
			prXML.println("    </" + pNS + "simpleContent>");
			prXML.println("  </" + pNS + "complexType>");
		}
	}

//	write the XML Schema data types 
	public void writeXMLDataTypes (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
//		Sort the data types			
		TreeMap <String, PDSObjDefn> sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!(lClass.isDataType && lClass.subClassOfTitle.compareTo("Character_Data_Type") == 0)) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}	
		ArrayList <PDSObjDefn> sortDataTypeArr = new ArrayList <PDSObjDefn> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
		
		
//		Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section contains the base data types and any constraints those types");
	    prXML.println("        may have. These types should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		
//		Write the data types
		for (Iterator<PDSObjDefn> i = sortDataTypeArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			boolean hasPattern = false;
			boolean hasCharacterConstraint = false;

			prXML.println("");
			prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
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
			for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("character_constraint") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						hasCharacterConstraint = true;
					}
				}

				if (lAttr.title.compareTo("maximum_characters") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("2147483647") == 0)) {
					    prXML.println("        <" + pNS + "maxLength value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("minimum_characters") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("-2147483648") == 0)) {
					    prXML.println("        <" + pNS + "minLength value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("maximum_value") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("2147483647") == 0)) {
					    prXML.println("        <" + pNS + "maxInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("minimum_value") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ( ! (lVal == null || lVal.compareTo("-2147483648") == 0)) {
					    prXML.println("        <" + pNS + "minInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("pattern") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						// if not null there there are one or more patterns
						hasPattern = true;
						for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String lPattern = (String) k.next();
							lPattern = InfoModel.escapeXMLChar(lPattern);
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
		TreeMap <String, PDSObjDefn> sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!(lClass.subClassOfTitle.compareTo("Unit_Of_Measure") == 0)) continue;
//  222 needs work to omit unit_of_measure			if (!(lClass.isUnitOfMeasure)) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}
		ArrayList <PDSObjDefn> sortDataTypeArr = new ArrayList <PDSObjDefn> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
		
//		Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section contains the base Units of Measure.");
	    prXML.println("        These Units of Measure should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		
//		Write the unit types		
		for (Iterator<PDSObjDefn> i = sortDataTypeArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
		    prXML.println("");
		    prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");							
//			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
			// use attribute list sorted by classOrder
			for (Iterator<AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("unit_id") == 0) {
					ArrayList <String> lValArr = InfoModel.getMultipleValue(lAttr.valArr);
					if (lValArr != null) {
						for (Iterator<String> k = lValArr.iterator(); k.hasNext();) {
							String lVal = (String) k.next();
							prXML.println("        <" + pNS + "enumeration value=\"" + lVal + "\"></" + pNS + "enumeration>");
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
	    prXML.println("      <" + pNS + "list itemType='pds:nil_reason_list'>");
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
	
	public void writeUserAttributeElementDefinitons (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		prXML.println("");
		
		// get all Owned variables for any class that is a child of the USER class
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.userClassAttrIdMap.values());
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			
			// only write the attributes in this schema's namespace
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lAttr.attrNameSpaceIdNC) != 0) continue;
			
			// kludge for common only; fix for LDD
//			if (lAttr.attrNameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) continue;
			if (! (lAttr.isUsedInClass && lAttr.isAttribute && lAttr.isPDS4)) continue;
			
			String nilableClause = "";
			if (lAttr.isNilable) {
				nilableClause = " nillable=\"true\"";
			}
			
			if (lAttr.hasAttributeOverride) {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"pds:" + lAttr.XMLSchemaName + "\">" + " </" + pNS + "element>");
			} else {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"pds:" + lAttr.valueType     + "\">" + " </" + pNS + "element>");
			}
		}
	}
	
	public void writeLDDAttributeElementDefinitons (ArrayList <AttrDefn> lAttrArr, PrintWriter prXML) throws java.io.IOException {
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.hasAttributeOverride) {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + " type=\"" + lAttr.attrNameSpaceId  + lAttr.XMLSchemaName + "\"> </" + pNS + "element>");
			} else {
				prXML.println("  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + " type=\"pds:" + lAttr.valueType + "\"> </" + pNS + "element>");
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
