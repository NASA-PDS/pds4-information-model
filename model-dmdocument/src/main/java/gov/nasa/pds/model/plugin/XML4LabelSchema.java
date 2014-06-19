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

	public XML4LabelSchema () {
		return;
	}

//	write the XML Label
	public void writeXMLSchemaFiles (SchemaFileDefn lSchemaFileDefn, ArrayList <PDSObjDefn> lInputClassArr) throws java.io.IOException {
		boolean isMaster = lSchemaFileDefn.isMaster;

		// sort the classes
		XML4LabelSortSchema (lSchemaFileDefn, lInputClassArr);	

		// get printer
		if (!DMDocument.LDDToolFlag ) {
			prXML = new PrintWriter(new FileWriter(lSchemaFileDefn.directoryPath +  lSchemaFileDefn.fileNameUC + "_" + lSchemaFileDefn.lab_version_id + "." + "xsd", false));
		} else {
			prXML = new PrintWriter(new FileWriter(lSchemaFileDefn.directoryPath +  lSchemaFileDefn.fileNameNC + "_" + lSchemaFileDefn.lab_version_id + "." + "xsd", false));
		}
//		write the XML Schema File Header
		writeXMLSchemaFileHeader (lSchemaFileDefn, prXML);
		
		// write the Class Elements for all schemas except the master schema 
		if (DMDocument.LDDToolFlag) {
			
			// write the Class Element definition statements for LDDs
//			if (DMDocument.LDDToolFlag && DMDocument.LDDClassElementFlag) {
			if (DMDocument.LDDClassElementFlag) {
				ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
				ArrayList <PDSObjDefn> lClassSubArr = new ArrayList <PDSObjDefn> ();
				for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
					PDSObjDefn lClass = (PDSObjDefn) i.next();
					if (lClass.isFromLDD) lClassSubArr.add(lClass);
				}
				writeLDDClassDefn (lSchemaFileDefn, lClassSubArr, prXML);
			}	
		} else {
			// write the Class Element definition statements for discipline namespaces
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") != 0) {
				ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (InfoModel.masterMOFClassMap.values());
				ArrayList <PDSObjDefn> lClassSubArr = new ArrayList <PDSObjDefn> ();
				for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
					PDSObjDefn lClass = (PDSObjDefn) i.next();
					if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) lClassSubArr.add(lClass);
				}
				writeLDDClassDefn (lSchemaFileDefn, lClassSubArr, prXML);
			}	
		}
		
		// write the Attribute Element definition statements
		if (DMDocument.LDDToolFlag && DMDocument.LDDAttrElementFlag) {
			writeLDDAttrDefn (lSchemaFileDefn, InfoModel.masterMOFAttrMap, prXML);
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
			
			// write the classes
			if (lClass.isAny || (lClass.title.indexOf("Mission_Area") > -1) || (lClass.title.indexOf("Discipline_Area") > -1) || (lClass.title.indexOf("Geometry") > -1) || (lClass.title.indexOf("Cartography") > -1)) {
				writeClassXSAny (lClass, prXML); 
			} else if (isBothExtensionRestriction) {
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
    	if (! DMDocument.LDDToolFlag) {
    		writeXMLDataTypes (lSchemaFileDefn, prXML);
    	}
    	
    	// write the Units Of Measure
    	if (! DMDocument.LDDToolFlag) {
    		writeXMLUnitsOfMeasure(lSchemaFileDefn, prXML);
    	}
    	
    	// write the Nil Values
    	if (! DMDocument.LDDToolFlag) {
    		writeXMLNillValues (lSchemaFileDefn, prXML);
    	}
	    	
       	// write the products
    	if (isMaster) {
    		writeProduct (classHierMap, prXML);
    	}
	    	
       	// write the deprecated items
    	if (isMaster) {
    		writeDeprecatedItems (prXML);
    	}
	    	
    	// write the footer
    	writeXMLSchemaFileFooter (prXML);
    	
//		close the file
    	prXML.close();
	}	
	
	// sort the classes and get the hierarchy map
	public void XML4LabelSortSchema (SchemaFileDefn lSchemaFileDefn, ArrayList <PDSObjDefn> lClassArr) {		
		int absLevel;
		int classCount = 0;
		
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			lClass.includeInThisSchemaFile = false;
			if ((lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) == 0) && lSchemaFileDefn.stewardArr.contains(lClass.steward)) {				
				if (! (lClass.isUSERClass || lClass.isUnitOfMeasure || lClass.isDataType || lClass.isVacuous)) {
					lClass.includeInThisSchemaFile = true;
					classCount++;
//					get sort sequence
					Integer sortInd = InfoModel.masterClassSortOrderMap.get(lClass.title);
					if (sortInd == null) {
						sortInd = InfoModel.masterClassSortOrderMap.get("Default1");
						if (sortInd == null) {
							sortInd = 99;
						}
					}
					if (lClass.isRegistryClass) {
						absLevel = 100000;
					} else if (lClass.isSchema1Class) {
						absLevel = 200000;
					} else {
						absLevel = 900000;
					}
					String sortId = absLevel + "." + MasterInfoModel.getSortId(sortInd) + "." + lClass.title;
					classHierMap.put(sortId, lClass);
				}
			}
		}
		return;
	}	
	
//	write the XML Schema File Header
	public void writeXMLSchemaFileHeader (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
		boolean isMasterSchema = false;
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") == 0) {
			isMasterSchema = true;
		}
		
//	Write the header statements
		prXML.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXML.println("  <!-- PDS4 XML/Schema" + " for Name Space Id:" + lSchemaFileDefn.nameSpaceIdNC + "  Version:" + lSchemaFileDefn.ont_version_id + " - " + DMDocument.masterTodaysDate + " -->");	 
		prXML.println("  <!-- Generated from the PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " - System Build 4a -->");
		prXML.println("  <!-- *** This PDS4 product schema is an operational deliverable. *** -->");
		prXML.println("  <" + pNS + "schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
		if (isMasterSchema) {
			prXML.println("    targetNamespace=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:pds=\"http://pds.nasa.gov/pds4/pds/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
		} else {
			prXML.println("    targetNamespace=\"http://pds.nasa.gov/pds4/" + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:" + lSchemaFileDefn.nameSpaceIdNC + "=\"http://pds.nasa.gov/pds4/" + lSchemaFileDefn.nameSpaceIdNC + "/v" + lSchemaFileDefn.ns_version_id + "\"");
			prXML.println("    xmlns:pds=\"http://pds.nasa.gov/pds4/pds/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\"");
		}
		prXML.println("    elementFormDefault=\"qualified\"");
		prXML.println("    attributeFormDefault=\"unqualified\"");
		prXML.println("    version=\"" + lSchemaFileDefn.sch_version_id + "\">");
//		prXML.println(" ");		
//		if (! isMasterSchema) {
		if ((! isMasterSchema) || DMDocument.LDDToolFlag) {
			prXML.println(" ");		
//			prXML.println("    <" + pNS + "import namespace=\"http://pds.nasa.gov/pds4/pds/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"PDS4_PDS_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\"/>");
			prXML.println("    <" + pNS + "import namespace=\"http://pds.nasa.gov/pds4/pds/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "\" schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + DMDocument.masterPDSSchemaFileDefn.ns_version_id + "/PDS4_PDS_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + ".xsd\"/>");
//			prXML.println(" ");		
		}
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
		
//		System.out.println("\ndebug writeXMLClassExtension lClass.title:" + lClass.title);

		prXML.println("\n" + indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.print  (indentSpacesUp() + "<" + pNS + "documentation>");
		prXML.print  (" " + lClass.description);
		prXML.println(" </" + pNS + "documentation>");
		prXML.println(indentSpacesDown() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "complexContent>");
		prXML.println(indentSpacesUp() + "<" + pNS + "extension base=\"" + lClass.nameSpaceId + lSuperClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "sequence" + ">");

		// write extension attributes
//		writeAttrAssocInClass(lClass, lClass.ownedAttrAssocNOArr, prXML);
		writeAttrAssocInClass(lClass, lClass.ownedAttrAssocNOArr, prXML);
			
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);
		
		// write assertions for enumerated values	
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") != 0 ) {		
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
		
		prXML.println("\n" + indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.print  (indentSpacesUp() + "<" + pNS + "documentation>");
		prXML.print  (" " + lClass.description);
		prXML.println(" </" + pNS + "documentation>");
		prXML.println(indentSpacesDown() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "complexContent>");
		prXML.println(indentSpacesUp() + "<" + pNS + "restriction base=\"" + lClass.nameSpaceId + lSuperClassName + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "sequence" +">");
	
		// write attributes and associations
		writeAttrAssocInClass(lClass, lClass.allAttrAssocArr, prXML);
	
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);

		// write assertions for enumerated values
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") != 0 ) {		
			writeAssertionsAttr (lClass.ownedAttrAssocAssertArr, prXML);	
		}
		prXML.println(indentSpacesDown() + "</" + pNS + "restriction>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexContent>");
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}	
	
	public void writeXMLClassNeither (SchemaFileDefn lSchemaFileDefn, PDSObjDefn lClass, ArrayList<PDSObjDefn> visitClassList, ArrayList<PDSObjDefn> recurseClassList, PrintWriter prXML) throws java.io.IOException {		
//		System.out.println("\ndebug writeXMLClassNeither lClass.identifier:" + lClass.identifier);

		prXML.println("\n" + indentSpacesUp() + "<" + pNS + "complexType name=\"" + lClass.title + "\">");
		prXML.println(indentSpacesUp() + "<" + pNS + "annotation>");
		prXML.print  (indentSpacesUp() + "<" + pNS + "documentation>");
		prXML.print  (" " + lClass.description);
		prXML.println(" </" + pNS + "documentation>");
		prXML.println(indentSpacesDown() + "</" + pNS + "annotation>");
		prXML.println(indentSpaces() + "<" + pNS + "sequence" + ">");
		
		// write attributes and associations
		writeAttrAssocInClass(lClass, lClass.allAttrAssocArr, prXML);
		if (choiceBlockOpen) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
			choiceBlockOpen = false;
		}		
			
		// write footers
		prXML.println(indentSpaces() + "</" + pNS + "sequence" + ">");
		writeVectorUnitAttribute (lClass, prXML);

		// write assertions for enumerated values
		if (DMDocument.LDDToolFlag || lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") != 0 ) {		
			writeAssertionsAttr (lClass.ownedAttrAssocAssertArr, prXML);
		}
		prXML.println(indentSpacesDown() + "</" + pNS + "complexType>");
		return;
	}
		
	public void writeVectorUnitAttribute (PDSObjDefn lClass, PrintWriter prXML) throws java.io.IOException {		
//		System.out.println("\ndebug writeVectorUnitAttribute");

		if (lClass.title.compareTo("Vector_Cartesian_3") == 0) {
			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Acceleration") == 0) {
//			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:Units_of_Acceleration\" use=\"required\" />");			
			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Position") == 0) {
//			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:Units_of_Length\" use=\"required\" />");			
			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		if (lClass.title.compareTo("Vector_Cartesian_3_Velocity") == 0) {
//			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:Units_of_Velocity\" use=\"required\" />");			
			prXML.println(indentSpaces() + "<xs:attribute name=\"unit\" type=\"pds:ASCII_Short_String_Collapsed\" use=\"required\" />");			
		}
		return;
	}	
	
//	write the attributes and associations in the class
	public void writeAttrAssocInClass (PDSObjDefn lClass, ArrayList <AttrDefn> lAttrAssocArr, PrintWriter prXML) throws java.io.IOException {
		choiceBlockOpen = false;
		upIndentSpaces();
		for (Iterator<AttrDefn> i = lAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();	
//			System.out.println("debug writeAttrAssocInClass - lAttr.identifier:" + lAttr.identifier);
//			System.out.println("      writeAttrAssocInClass - lAttr.rdfIdentifier:" + lAttr.rdfIdentifier);
//			System.out.println("      writeAttrAssocInClass - lAttr.title:" + lAttr.title);

			if (lAttr.isAttribute) {
				writeClassAttribute (lClass, lAttr, prXML);				
			} else {
				writeClassAssociation (lClass, lAttr, prXML);				
			}
		}
		downIndentSpaces();
		return;
	}	

//	write the attributes in the class
	public void writeClassAttribute (PDSObjDefn lClass, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
		// capture the data types that are used; will write only those used.
//		System.out.println("debug writeClassAttribute - lAttr.identifier:" + lAttr.identifier);
//		System.out.println("debug writeClassAttribute - lAttr.isChoice:" + lAttr.isChoice);
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

		if ((! lAttr.isChoice) && (choiceBlockOpen)) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
			choiceBlockOpen = false;
		}		
		
		if ((lAttr.isChoice) && (!choiceBlockOpen)) {
			prXML.println(indentSpaces() + "<" + pNS + "choice minOccurs=\"1\" maxOccurs=\"1\">");
			upIndentSpaces();
			choiceBlockOpen = true;
		}
		
		String minMaxOccursClause = " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"";
		if (lAttr.isChoice) {
			minMaxOccursClause = "";
		}
		
		String nilableClause = "";
		if (lAttr.isNilable) {
			nilableClause = " nillable=\"true\"";
		}
						
		if (! (lAttr.hasAttributeOverride || lAttr.isNilable)) {
			prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"pds:" + lValueType + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
		} else {
			if (! allAttrTypeIdArr.contains(lAttr.XMLSchemaName)) {
				allAttrTypeIdArr.add(lAttr.XMLSchemaName);
				allAttrTypeMap.put(lAttr.XMLSchemaName, lAttr);				
			}
			prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lAttr.attrNameSpaceId  + lAttr.XMLSchemaName + "\"" + " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"> </" + pNS + "element>");
		}
		
		// if LDDTool write out annotation with the definition
		if (DMDocument.LDDToolAnnotateDefinitionFlag) {
			prXML.println(indentSpaces() + "<xs:annotation>");
			prXML.println(indentSpaces() + "  <xs:documentation>");
			prXML.println(indentSpaces() + lAttr.description + "</xs:documentation>");
			prXML.println(indentSpaces() + "</xs:annotation>");  
		}		
		return;
	}
	
//	write the associations in the class
	public void writeClassAssociation (PDSObjDefn lClass, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
		// get min and max cardinalities for the INSTANCE attribute
//		System.out.println("debug writeClassAssociation  lAttr.identifier:" + lAttr.identifier);
		String cmin = lAttr.cardMin;
		String cmax = lAttr.cardMax;
		if (cmax.compareTo("*") == 0) {
			cmax = "unbounded";
		}
		String minMaxOccursClause = " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"";
		
//		if (lAttr.isChoice) System.out.println("debug writeClassAssociation FOUND Choice Association lAttr.identifier:" + lAttr.identifier);
		
		if (lAttr.isChoice) {
			String choiceMinMaxOccursClause = "choice minOccurs=\"" + cmin + "\" maxOccurs=\"" + cmax + "\"";
			minMaxOccursClause = "";
			prXML.println(indentSpaces() + "<" + pNS + choiceMinMaxOccursClause+ ">");
			upIndentSpaces();
		}
		
		// get each value of the INSTANCE attribute, get the associated class
		ArrayList <String> lvalarr = lAttr.valArr;	
		if (! (lvalarr == null || lvalarr.isEmpty())) {
			for (Iterator<String> j = lvalarr.iterator(); j.hasNext();) {				
				String lctitle = (String) j.next();
				PDSObjDefn lCompClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lctitle);
				if  (lCompClass != null) {
//					String lSchemaId = lCompClass.regAuthId + ":" + lCompClass.steward.toUpperCase() + ":" + lCompClass.title;
					if (! DMDocument.omitClass.contains(lCompClass.title)) {
						if (lCompClass.title.compareTo("Discipline_Facets") == 0)
							writeXMLClassDisciplineFacet (lClass, prXML);
						else
							prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lCompClass.title + "\"" + " type=\"" + lCompClass.nameSpaceId  + lCompClass.title + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
					}							
				}
			}
		}
		
		if (lAttr.isChoice) {
			downIndentSpaces();
			prXML.println(indentSpaces() + "</" + pNS + "choice>");
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
//			System.out.println("debug writeAssertion" + "  writing assertion lAttr.identifier:" + lAttr.identifier);
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
	
//	write the mission and node areas
	public void writeClassXSAny (PDSObjDefn lClass, PrintWriter prXML) throws java.io.IOException {
		prXML.println("\n  <" + pNS + "complexType name=\"" + lClass.title + "\">");
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
//		System.out.println("\ndebug writeXMLAttributes ");	
//		Write the header statements
		prXML.println("");
		prXML.println("    <" + pNS + "annotation>");
		prXML.println("      <" + pNS + "documentation>This section contains the simpleTypes that provide more constraints");
		prXML.println("        than those at the base data type level. The simpleTypes defined here build on the base data");
		prXML.println("        types. This is another component of the common dictionary and therefore falls within the");
		prXML.println("        pds namespace.");
		prXML.println("      </" + pNS + "documentation>");
		prXML.println("    </" + pNS + "annotation>");		
		
//		Write the Standard Data Types that were used.
		writeXMLExtendedTypes (lSchemaFileDefn, allAttrTypeMap, prXML);
	}	
	
//	write the attribute types
	public void writeXMLExtendedTypes (SchemaFileDefn lSchemaFileDefn, TreeMap <String, AttrDefn> allAttrTypeMap, PrintWriter prXML) throws java.io.IOException {
		boolean hasUnits;
		Set <String> set1 = allAttrTypeMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String typeId = (String) iter1.next();
			AttrDefn lAttr = allAttrTypeMap.get(typeId);
			hasUnits = false;
			if (lAttr.unit_of_measure_type.indexOf("TBD") != 0) {
				hasUnits = true;
			}
			if (! lAttr.isNilable) {
				writeXMLExtendedRestrictedNonEnumerated (lSchemaFileDefn, typeId, hasUnits, lAttr, prXML);
// **3 nilable				
			} else {
				prXML.println("\n  <" + pNS + "complexType name=\"" + lAttr.XMLSchemaName + "\">");
				prXML.println("    <" + pNS + "simpleContent>");
				prXML.println("      <" + pNS + "extension base=\"pds:" + lAttr.valueType + "\">");
//				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"pds:Symbolic_Literals_Nil_Reason_List\" use=\"optional\" />");
				prXML.println("        <" + pNS + "attribute name=\"nilReason\" type=\"pds:nil_reason\" use=\"optional\" />");
				prXML.println("      </" + pNS + "extension>");
				prXML.println("    </" + pNS + "simpleContent>");
				prXML.println("  </" + pNS + "complexType>");
			}
		}
	}

	//	write the attribute
	public void writeXMLExtendedRestrictedNonEnumerated (SchemaFileDefn lSchemaFileDefn, String typeTitle, boolean hasUnits, AttrDefn lAttr, PrintWriter prXML) throws java.io.IOException {
//		System.out.println("debug writeXMLExtendedRestrictedNonEnumerated  lAttr.title:" + lAttr.title);
		String lValue;
		if (hasUnits) {
			prXML.println("\n  <" + pNS + "simpleType name=\"" + typeTitle + "_WO_Units\">");
		} else {
			prXML.println("\n  <" + pNS + "simpleType name=\"" + typeTitle + "\">");
		}
	
		prXML.println("    <" + pNS + "restriction base=\"pds:" + lAttr.getValueType (true) + "\">");
		
		lValue = lAttr.getFormat (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("  <!-- format=\"" + lValue + "\" -->");
		}
		
		lValue = lAttr.getPattern (true);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("    	<" + pNS + "pattern value='" + lValue + "'/>");
		}
		
		lValue = lAttr.getMinimumValue (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "minInclusive value=\"" + lValue + "\"/>");
		}
		
		lValue = lAttr.getMaximumValue (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "maxInclusive value=\"" + lValue + "\"/>");
		}
		
		lValue = lAttr.getMinimumCharacters (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "minLength value=\"" + lValue + "\"/>");
		}

		lValue = lAttr.getMaximumCharacters (true, true);
		if (! ((lValue.indexOf("TBD") == 0) || (lValue.indexOf("Unbounded") == 0))) {
			prXML.println("     <" + pNS + "maxLength value=\"" + lValue + "\"/>");
		}
		
		lValue = lAttr.getUnitOfMeasure (false);
		if (! (lValue.indexOf("TBD") == 0)) {
			prXML.println("     <!-- unit_of_measure_type=" + lValue  + " -->");
		}

		prXML.println("	   </" + pNS + "restriction>");
		prXML.println("  </" + pNS + "simpleType>");
		
		if (hasUnits) {
			prXML.println("\n  <" + pNS + "complexType name=\"" + typeTitle + "\">");
			prXML.println("    <" + pNS + "simpleContent>");
			prXML.println("      <" + pNS + "extension base=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + typeTitle + "_WO_Units\">");
//			prXML.println("        <" + pNS + "attribute name=\"unit\" type=\"pds:" + lAttr.unit_of_measure_type  + "\" use=\"required\" />");
			prXML.println("        <" + pNS + "attribute name=\"unit\" type=\"pds:" + "ASCII_Short_String_Collapsed" + "\" use=\"required\" />");
			prXML.println("        <!-- specified_unit_id=" + lAttr.default_unit_id  + " -->");
			prXML.println("      </" + pNS + "extension>");
			prXML.println("    </" + pNS + "simpleContent>");
			prXML.println("  </" + pNS + "complexType>");
		}
	}

//	write the XML Schema data types 
	public void writeXMLDataTypes (SchemaFileDefn lSchemaFileDefn, PrintWriter prXML) throws java.io.IOException {
//		System.out.println("\ndebug writeXMLDataTypes");
		
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
	    prXML.println("      <" + pNS + "documentation>This section contains the base data types for PDS4 and any constraints those types");
	    prXML.println("        may have. These types should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace, pds.");
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
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
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
//		System.out.println("\ndebug writeXMLUnitsOfMeasure");	
	    
//		Sort the unit types
		TreeMap <String, PDSObjDefn> sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lSchemaFileDefn.nameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!(lClass.subClassOfTitle.compareTo("Unit_Of_Measure") == 0)) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}
		ArrayList <PDSObjDefn> sortDataTypeArr = new ArrayList <PDSObjDefn> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
		
//		Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section contains the base Units of Measure for PDS4.");
	    prXML.println("        These Units of Measure should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace, pds.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		
//		Write the unit types		
		for (Iterator<PDSObjDefn> i = sortDataTypeArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
		    prXML.println("");
		    prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");							
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
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
//		System.out.println("\ndebug writeXMLNillValues");	
		if (lSchemaFileDefn.nameSpaceIdNC.compareTo("pds") != 0) return;		
		// write the nil reasons
		
	    prXML.println("");
	    prXML.println("    <xs:simpleType name=\"nil_reason\" >");
	    prXML.println("      <xs:list itemType='pds:nil_reason_list'>");
	    prXML.println("      </xs:list>");
	    prXML.println("    </xs:simpleType>");
	    
	    prXML.println("    <xs:simpleType name=\"nil_reason_list\">");
	    prXML.println("      <xs:restriction base=\"xs:string\">");
	    prXML.println("        <xs:enumeration value=\"inapplicable\"></xs:enumeration>");
	    prXML.println("        <xs:enumeration value=\"anticipated\"></xs:enumeration>");
	    prXML.println("        <xs:enumeration value=\"missing\"></xs:enumeration>");
	    prXML.println("        <xs:enumeration value=\"unknown\"></xs:enumeration>");
	    prXML.println("      </xs:restriction>");
	    prXML.println("    </xs:simpleType>");
	}	

	public void writeProduct (TreeMap <String, PDSObjDefn> lClassHierMap, PrintWriter prXML) throws java.io.IOException {
//		System.out.println("\ndebug writeProduct");
		ArrayList <PDSObjDefn> lSortedClassArr = new ArrayList <PDSObjDefn> (lClassHierMap.values());
		for (Iterator<PDSObjDefn> i = lSortedClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
			if (lClass.isRegistryClass) {
				prXML.println("\n  <" + pNS + "element name=\"" + lClass.title + "\" type=\"pds:" + lClass.title + "\">");
				prXML.println("    <" + pNS + "annotation>");
				prXML.println("      <" + pNS + "documentation>");
				prXML.println("        This element is the PDS4 " + lClass.title + " XML Schema.");
		    	prXML.println("      </" + pNS + "documentation>");
		    	prXML.println("    </" + pNS + "annotation>");
		    	prXML.println("  </" + pNS + "element>");
			}
		}
	}
	
	
	public void writeDeprecatedItems (PrintWriter prXML) throws java.io.IOException {
//		System.out.println("\ndebug writeDeprecatedItems");
    	prXML.println(" ");
    	prXML.println("<!-- Deprecated Items - Begin ");
    	prXML.println(" ");
    	prXML.println("     - Classes -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (! lDeprecatedDefn.isAttribute) {
				String lValue = lDeprecatedDefn.nameSpaceIdNC + ":" + lDeprecatedDefn.className;
		    	prXML.println("     " + lValue);
//		    	prXML.println("     " + lDeprecatedDefn.title);
			}
		}
    	prXML.println(" ");
    	prXML.println("     - Attributes -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (lDeprecatedDefn.isAttribute && ! lDeprecatedDefn.isValue) {
				String lValue = lDeprecatedDefn.nameSpaceIdNC + ":" + lDeprecatedDefn.className + "/" + lDeprecatedDefn.nameSpaceIdNC + ":" + lDeprecatedDefn.attrName;
		    	prXML.println("     " + lValue);
//		    	prXML.println("     " + lDeprecatedDefn.title);
			}
		}	
    	prXML.println(" ");
    	prXML.println("     - Permissible Values -");
		for (Iterator<DeprecatedDefn> i = DMDocument.deprecatedObjects2.iterator(); i.hasNext();) {
			DeprecatedDefn lDeprecatedDefn = (DeprecatedDefn) i.next();
			if (lDeprecatedDefn.isAttribute && lDeprecatedDefn.isValue) {
				String lValue = lDeprecatedDefn.nameSpaceIdNC + ":" + lDeprecatedDefn.className + "/" + lDeprecatedDefn.nameSpaceIdNC + ":" + lDeprecatedDefn.attrName + " - " + lDeprecatedDefn.value;
		    	prXML.println("     " + lValue);
//		    	prXML.println("     " + lDeprecatedDefn.title + " : " + lDeprecatedDefn.value);
			}
		}
    	prXML.println(" ");
    	prXML.println("     Deprecated Items - End -->");
    	prXML.println(" ");
	}
		
	public void writeLDDClassDefn (SchemaFileDefn lSchemaFileDefn, ArrayList <PDSObjDefn> lClassArr, PrintWriter prXML) throws java.io.IOException {
//		System.out.println("\ndebug writeClass");
		for (Iterator<PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (! lClass.isReferencedFromLDD) {
				prXML.println("\n  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lClass.title + "\">");
			} else {
				prXML.println("\n  <" + pNS + "element name=\"" + lClass.title + "\" type=\"" + lClass.nameSpaceIdNC + ":" + lClass.title + "\">");
			}
			prXML.println("    <" + pNS + "annotation>");
			prXML.println("      <" + pNS + "documentation>");
			prXML.println("        This element is the PDS4 " + lClass.title + " XML Schema.");
	    	prXML.println("      </" + pNS + "documentation>");
	    	prXML.println("    </" + pNS + "annotation>");
	    	prXML.println("  </" + pNS + "element>");
		}
	}
	
	public void writeLDDAttrDefn (SchemaFileDefn lSchemaFileDefn, TreeMap <String, AttrDefn> lAttrHierMap, PrintWriter prXML) throws java.io.IOException {
//		System.out.println("\ndebug writeAttribute");
		ArrayList <AttrDefn> lSortedAttrArr = new ArrayList <AttrDefn> (lAttrHierMap.values());
		for (Iterator<AttrDefn> i = lSortedAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isFromLDD && lAttr.className.indexOf("TBD") != 0) { // *** check on classname is ????; can attributes exist without classes
				
				if (lAttr.hasAttributeOverride) {
//					prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"" + lAttr.attrNameSpaceId  + lAttr.XMLSchemaName + "\"" + " minOccurs=\"" + cmin + "\"" + " maxOccurs=\"" + cmax + "\"> </" + pNS + "element>");
					prXML.println("\n  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + " type=\"" + lAttr.attrNameSpaceId  + lAttr.XMLSchemaName + "\">");
				} else {
//					prXML.println(indentSpaces() + "<" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + nilableClause + " type=\"pds:" + lValueType + "\"" + minMaxOccursClause + "> </" + pNS + "element>");
					prXML.println("\n  <" + pNS + "element name=\"" + lAttr.XMLSchemaName + "\"" + " type=\"pds:" + lAttr.valueType + "\">");
				}				
//				prXML.println("\n  <" + pNS + "element name=\"" + lAttr.title + "\" type=\"" + lSchemaFileDefn.nameSpaceIdNC + ":" + lAttr.title + "\">");
				prXML.println("    <" + pNS + "annotation>");
				prXML.println("      <" + pNS + "documentation>");
				prXML.println("        This element is the PDS4 " + lAttr.title + " XML Schema.");
		    	prXML.println("      </" + pNS + "documentation>");
		    	prXML.println("    </" + pNS + "annotation>");
		    	prXML.println("  </" + pNS + "element>");
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
