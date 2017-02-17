package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Writes the PDS4 DD content to a RDF/OWL file using DOM
 *   
 */

class WriteDOMRDFOWLFile extends Object{
	ArrayList <DOMAttr> gDOMAttrArr = new ArrayList <DOMAttr> ();
	ArrayList <String> gClassIdArr = new ArrayList <String> ();
	ArrayList <String> gAttrIdArr = new ArrayList <String> ();
	ArrayList <String> gPermValIdArr = new ArrayList <String> ();
	ArrayList <String> gDataTypeIdArr = new ArrayList <String> ();
	ArrayList <String> gUnitIdArr = new ArrayList <String> ();
	ArrayList <String> adminRecUsedArr, adminRecTitleArr;
//	ArrayList <String> topLevelClassArr;
	PrintWriter prDDPins;

	public WriteDOMRDFOWLFile () {
/*		topLevelClassArr = new ArrayList <String> ();
		topLevelClassArr.add("Data_Object");
		topLevelClassArr.add("Tagged_Digital_Object");
		topLevelClassArr.add("Tagged_Digital_Child");
		topLevelClassArr.add("Tagged_NonDigital_Object");
		topLevelClassArr.add("Tagged_NonDigital_Child");
		topLevelClassArr.add("Data_Type");
		topLevelClassArr.add("Unit_Of_Measure");
		topLevelClassArr.add("Product");
		topLevelClassArr.add("Product_Components");	*/
		return;
	}

	// write the RDF/OWL file
	public void writeOWLFile (String lFileName) throws java.io.IOException {
		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
//		System.out.println("debug WriteDOMRDFOWLFile lFileName:" + lFileName);

		printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the RDF/OWL Header
	public void printPDDPHdr () {
		prDDPins.println("<?xml version=\"1.0\"?>");
		prDDPins.println("<rdf:RDF");
		prDDPins.println("   xmlns=\"http://pds.nasa.gov/ontologies/1700/pds/\"");
		prDDPins.println("   xml:base=\"http://pds.nasa.gov/ontologies/1700/pds/\"");
		prDDPins.println("   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
		prDDPins.println("   xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		prDDPins.println("   xmlns:owl=\"http://www.w3.org/2002/07/owl#\"");

		prDDPins.println("   xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"");
		prDDPins.println("   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"");
		prDDPins.println("   xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"");
		prDDPins.println("   xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"");
		
		prDDPins.println("   xmlns:dc=\"http://purl.org/dc/elements/1.1/\">");
//		prDDPins.println("   xmlns:pds=\"http://pds.nasa.gov/pds4/pds\">");
		prDDPins.println(" ");
		prDDPins.println("   <owl:Ontology rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/\">");
		prDDPins.println("      <dc:title>PDS4 Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:title>");
		prDDPins.println("      <dc:description>The ontology for the PDS4 Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:description>");
		prDDPins.println("   </owl:Ontology>");
		prDDPins.println(" ");
		prDDPins.println(" ");		
		
		prDDPins.println("   <!-- ");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("   //");
		prDDPins.println("   // Object Properties");
		prDDPins.println("   //");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("      -->");
		prDDPins.println(" ");
		prDDPins.println(" ");
	    
		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_a -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\">");
		prDDPins.println("      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#TransitiveProperty\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");	    
		prDDPins.println(" ");

		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_attribute -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_attribute\">");
		prDDPins.println("      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");		

		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value\">");
		prDDPins.println("      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");		

		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_class -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_class\">");
		prDDPins.println("      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");		

		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_data_type -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_data_type\">");
		prDDPins.println("      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");		

		prDDPins.println("   <!-- http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:ObjectProperty rdf:about=\"http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit\">");
		prDDPins.println("      <rdfs:subPropertyOf rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_a\"/>");
		prDDPins.println("   </owl:ObjectProperty>");
		prDDPins.println(" ");	
		
		prDDPins.println("   <!-- PDS4 Disjoints: http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.___Top Level___ -->");
		prDDPins.println("   <rdf:Description>");
		prDDPins.println("      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AllDisjointClasses\"/>");
		prDDPins.println("      <owl:members rdf:parseType=\"Collection\">");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.all.USER\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Data_Object\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Data_Type\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Product\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Product_Components\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Tagged_Digital_Child\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Tagged_Digital_Object\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Tagged_NonDigital_Child\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Tagged_NonDigital_Object\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Unit_Of_Measure\"/>");
		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Attribute\"/>");
// 777		prDDPins.println("         <rdf:Description rdf:about=\"http://pds.nasa.gov/pds4/pds#0001_NASA_PDS_1.pds.Permissible_Value\"/>");
		prDDPins.println("      </owl:members>");
		prDDPins.println("   </rdf:Description>");
		
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Attribute" + " -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Attribute" + "\">");
		prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.all.USER" + "\"/>");	
		prDDPins.println("      <dc:title>" + "Attribute" + "</dc:title>");
		prDDPins.println("      <dc:description>" + "The Attribute class is the parent of all PDS4 attributes." + "</dc:description>");
		prDDPins.println("      <rdfs:label>"  + "0001_NASA_PDS_1.pds.Attribute" +  "</rdfs:label>");
		prDDPins.println("      <rdfs:comment>The definition of class " + "0001_NASA_PDS_1.pds.Attribute" + ".</rdfs:comment>");
		prDDPins.println("   </owl:Class>");		
		
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Permissible_Value" + " -->");
		prDDPins.println(" ");
		prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Permissible_Value" + "\">");
		prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.all.USER" + "\"/>");	
		prDDPins.println("      <dc:title>" + "Permissible_Value" + "</dc:title>");
		prDDPins.println("      <dc:description>" + "The Permissible_Value class is the parent of all PDS4 Permissible Values." + "</dc:description>");
		prDDPins.println("      <rdfs:label>"  + "0001_NASA_PDS_1.pds.Permissible_Value" +  "</rdfs:label>");
		prDDPins.println("      <rdfs:comment>The definition of class " + "0001_NASA_PDS_1.pds.Permissible_Value" + ".</rdfs:comment>");
		prDDPins.println("   </owl:Class>");		
	}

	// Print the RDF/OWL Footer
	public  void printPDDPFtr () {
		prDDPins.println("</rdf:RDF>");
	}
	
//	print the RDF/OWL body
	public  void printPDDPBody () {
		prDDPins.println(" ");
		prDDPins.println("   <!-- ");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("   //");
		prDDPins.println("   // Object Classes");
		prDDPins.println("   //");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("      -->");
		printClasses (prDDPins);

		prDDPins.println(" ");
		prDDPins.println("   <!-- ");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("   //");
		prDDPins.println("   // Object Attributes");
		prDDPins.println("   //");
		prDDPins.println("   ///////////////////////////////////////////////////////////////////////////////////////");
		prDDPins.println("      -->");

		printAttributes (prDDPins);	
		printDisjoints ("Attributes", gAttrIdArr, prDDPins);
// 777		printDisjoints ("Permanent Values", gPermValIdArr, prDDPins);
		
		printDataTypes (prDDPins);
		printUnits (prDDPins);
	}
	
	// write the PDS4 classes as OWL classes 
	public  void printClasses (PrintWriter prDDPins) {
		ArrayList <DOMClass> lDOMClassArr = new ArrayList <DOMClass> (InfoModel.masterDOMClassIdMap.values());
		for (Iterator<DOMClass> i = lDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lDOMClass = (DOMClass) i.next();	
//			System.out.println("debug WriteDOMRDFOWLFile - printClasses 1 - lDOMClass.identifier:" + lDOMClass.identifier + " - lDOMClass.nameSpaceIdNC:" + lDOMClass.nameSpaceIdNC + " - lDOMClass.steward:" + lDOMClass.steward);
			if (lDOMClass.identifier.indexOf("PDS3") > -1) continue;
//			if (lDOMClass.registrationStatus.compareTo("Retired") == 0) continue;
//			if (lDOMClass.isDataType) continue;
//			if (lDOMClass.isUnitOfMeasure) continue;
//			if (lDOMClass.isUSERClass) continue;
//			if (lDOMClass.title.compareTo("Product_Components") == 0) continue;
			if (! ((lDOMClass.nameSpaceIdNC.compareTo("pds") == 0 || lDOMClass.nameSpaceIdNC.compareTo("all") == 0) && (lDOMClass.steward.compareTo("pds") == 0 || lDOMClass.steward.compareTo("ops") == 0))) continue;			
//			System.out.println("debug WriteDOMRDFOWLFile - printClasses 2 - FILTERED - lDOMClass.identifier:" + lDOMClass.identifier + " - lDOMClass.nameSpaceIdNC:" + lDOMClass.nameSpaceIdNC + " - lDOMClass.steward:" + lDOMClass.steward);
			printClass (lDOMClass, prDDPins);
		}
	}
	
	// write a single PDS4 classes as an OWL class 
	public  void printClass (DOMClass lDOMClass, PrintWriter prDDPins) {
//		System.out.println("debug WriteDOMRDFOWLFile - printClass- lDOMClass.identifier:" + lDOMClass.identifier);
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Class: http://pds.nasa.gov/pds4/pds#" + lDOMClass.identifier + " -->");
		prDDPins.println(" ");

		// write PDS4 class as an owl:Class definition
		prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMClass.identifier + "\">");
		
		// write the superclass as a property  
		if (lDOMClass.subClassOf != null && lDOMClass.subClassOf.title.indexOf("TBD") != 0)  
			prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMClass.subClassOf.identifier + "\"/>");	
		
		prDDPins.println("      <dc:title>" + lDOMClass.title + "</dc:title>");
		prDDPins.println("      <dc:description>" + formValue(lDOMClass.definition) + "</dc:description>");
		
		prDDPins.println("      <rdfs:label>"  + lDOMClass.identifier +  "</rdfs:label>");
		prDDPins.println("      <rdfs:comment>The definition of class " + lDOMClass.identifier + ".</rdfs:comment>");
		
		// write the PDS4 attributes as "has_attribute" properties on PDS4 classes (assume PDS4 attributes are defined as classes.  
		for (Iterator<DOMProp> j = lDOMClass.ownedAttrArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
				gDOMAttrArr.add(lDOMAttr);
				gAttrIdArr.add(lDOMAttr.identifier);
				prDDPins.println("      <rdfs:subClassOf>");
				prDDPins.println("         <owl:Restriction>");
				prDDPins.println("            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_attribute\"/>");
				prDDPins.println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMAttr.identifier + "\"/>");
				prDDPins.println("         </owl:Restriction>");
				prDDPins.println("      </rdfs:subClassOf>");
			}
		}
		
		// write the PDS4 associations as "has_class" properties on PDS4 classes  
		for (Iterator<DOMProp> j = lDOMClass.ownedAssocArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
				DOMClass lDOMClass2 = (DOMClass) lDOMProp.hasDOMObject;					
				if (lDOMClass2.identifier.indexOf("PDS3") > -1) continue;
				if (! ((lDOMClass2.nameSpaceIdNC.compareTo("pds") == 0 || lDOMClass2.nameSpaceIdNC.compareTo("all") == 0) && (lDOMClass2.steward.compareTo("pds") == 0 || lDOMClass2.steward.compareTo("ops") == 0))) continue;			
				prDDPins.println("      <rdfs:subClassOf>");
				prDDPins.println("         <owl:Restriction>");
				prDDPins.println("            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_class\"/>");
				prDDPins.println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMClass2.identifier + "\"/>");
				prDDPins.println("         </owl:Restriction>");
				prDDPins.println("      </rdfs:subClassOf>");
			}
		}
		prDDPins.println("   </owl:Class>");

		// write the disjoints (subclasses) for a single class
		gClassIdArr = new ArrayList <String> ();
		if (lDOMClass.subClassHierArr.size() > 1) {
			for (Iterator<DOMClass> i = lDOMClass.subClassHierArr.iterator(); i.hasNext();) {
				DOMClass lDisjointDOMClass = (DOMClass) i.next();
				if (lDisjointDOMClass.identifier.indexOf("PDS3") > -1) continue;
				if (! ((lDisjointDOMClass.nameSpaceIdNC.compareTo("pds") == 0 || lDisjointDOMClass.nameSpaceIdNC.compareTo("all") == 0) && (lDisjointDOMClass.steward.compareTo("pds") == 0 || lDisjointDOMClass.steward.compareTo("ops") == 0))) continue;			
				gClassIdArr.add(lDisjointDOMClass.identifier);
			}
			printDisjoints (lDOMClass.identifier, gClassIdArr, prDDPins);
		}
	}
	
	// write the PDS4 attributes as OWL classes 
	public  void printAttributes (PrintWriter prDDPins) {
		for (Iterator<DOMAttr> i = gDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lDOMAttr = (DOMAttr) i.next();
//			System.out.println("debug WriteDOMRDFOWLFile - printAtributes 1 - lDOMAttr.identifier:" + lDOMAttr.identifier + " - lDOMAttr.nameSpaceIdNC:" + lDOMAttr.nameSpaceIdNC + " - lDOMAttr.steward:" + lDOMAttr.steward);
//			if (! ((lDOMAttr.nameSpaceIdNC.compareTo("pds") == 0 || lDOMAttr.nameSpaceIdNC.compareTo("all") == 0) && (lDOMAttr.steward.compareTo("pds") == 0 || lDOMAttr.steward.compareTo("ops") == 0))) continue;			
			if (lDOMAttr.title.indexOf("ANAME") > -1) continue;
			printAttr (lDOMAttr, prDDPins);
			gAttrIdArr.add(lDOMAttr.identifier);
		}
	}

	// write a single PDS4 attribute as an OWL class 
	public  void printAttr (DOMAttr lDOMAttr, PrintWriter prDDPins) {
		ArrayList <DOMPermValDefn> lDOMClassAttrPermValArr = new ArrayList <DOMPermValDefn> ();
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Attribute: http://pds.nasa.gov/pds4/pds#" + lDOMAttr.identifier + " -->");
		prDDPins.println(" ");

		// write statement to define PDS4 attribute as an owl:Class definition
		prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMAttr.identifier + "\">");
		prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Attribute" + "\"/>");
		
		prDDPins.println("      <dc:title>" + lDOMAttr.title + "</dc:title>");
		prDDPins.println("      <dc:description>" + formValue(lDOMAttr.definition) + "</dc:description>");
		
		prDDPins.println("      <rdfs:label>"  + lDOMAttr.identifier +  "</rdfs:label>");
		prDDPins.println("      <rdfs:comment>The definition of class " + lDOMAttr.identifier + ".</rdfs:comment>");
				
		// write the permissible values as properties
		for (Iterator<DOMProp> i = lDOMAttr.domPermValueArr.iterator(); i.hasNext();) {
			DOMProp lDOMProp = (DOMProp) i.next();
			if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
				DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;					
				if (lDOMPermValDefn.identifier.indexOf("PDS3") > -1) {
					if (lDOMPermValDefn.identifier.indexOf("Product") > -1) continue;
				}
				if (! ((lDOMPermValDefn.nameSpaceIdNC.compareTo("pds") == 0 || lDOMPermValDefn.nameSpaceIdNC.compareTo("all") == 0) && (lDOMPermValDefn.steward.compareTo("pds") == 0 || lDOMPermValDefn.steward.compareTo("ops") == 0))) continue;											
//				System.out.println("debug WriteDOMRDFOWLFile - permissible values 2 - FILTERED - lDOMPermValDefn.identifier:" + lDOMPermValDefn.identifier + " - lDOMPermValDefn.nameSpaceIdNC:" + lDOMPermValDefn.nameSpaceIdNC + " - lDOMPermValDefn.steward:" + lDOMPermValDefn.steward);
// 777				prDDPins.println("      <rdfs:subClassOf>");
//				prDDPins.println("         <owl:Restriction>");
//				prDDPins.println("            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_permissible_value\"/>");
//				prDDPins.println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMPermValDefn.identifier + "\"/>");
//				prDDPins.println("         </owl:Restriction>");
//				prDDPins.println("      </rdfs:subClassOf>");
				lDOMClassAttrPermValArr.add(lDOMPermValDefn);
				gPermValIdArr.add(lDOMPermValDefn.identifier);
			}
		}
		
		// write the "has_data_type properties
		String lIdentifier = InfoModel.getClassIdentifier("pds", lDOMAttr.valueType);
//		System.out.println("debug printAttr datatype lIdentifier:" + lIdentifier);
		DOMDataType lDOMDataType = InfoModel.masterDOMDataTypeIdMap.get(lIdentifier);
		if (lDOMDataType != null) {
			prDDPins.println("      <rdfs:subClassOf>");
			prDDPins.println("         <owl:Restriction>");
			prDDPins.println("            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_data_type\"/>");
			prDDPins.println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMDataType.identifier + "\"/>");
			prDDPins.println("         </owl:Restriction>");
			prDDPins.println("      </rdfs:subClassOf>");
		}
		
		// write the "has_measurement_units properties
		lIdentifier = InfoModel.getClassIdentifier("pds", lDOMAttr.unit_of_measure_type);
//		System.out.println("debug printAttr unit lIdentifier:" + lIdentifier);
		DOMUnit lDOMUnit = InfoModel.masterDOMUnitIdMap.get(lIdentifier);
		if (lDOMUnit != null) {
			prDDPins.println("      <rdfs:subClassOf>");
			prDDPins.println("         <owl:Restriction>");
			prDDPins.println("            <owl:onProperty rdf:resource=\"http://pds.nasa.gov/ontologies/1700/pds/has_measurement_unit\"/>");
			prDDPins.println("            <owl:someValuesFrom rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lDOMUnit.identifier + "\"/>");
			prDDPins.println("         </owl:Restriction>");
			prDDPins.println("      </rdfs:subClassOf>");
		}
		prDDPins.println("   </owl:Class>");
		
		// write the permissible values as OWL classes
// 777		for (Iterator<DOMPermValDefn> j = lDOMClassAttrPermValArr.iterator(); j.hasNext();) {
//			DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) j.next();
//			printPermValue (lDOMPermValDefn, prDDPins);
//		}
	}	

	// write a single permissible value as a OWL class
	public  void printPermValue (DOMPermValDefn lDOMPermValDefn, PrintWriter prDDPins) {
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Permissible Value: http://pds.nasa.gov/pds4/pds#" + lDOMPermValDefn.identifier + " -->");
		prDDPins.println(" ");

		// write statement to define PDS4 permissible value as an owl:Class definition
		prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMPermValDefn.identifier + "\">");
		prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + "0001_NASA_PDS_1.pds.Permissible_Value" + "\"/>");
		prDDPins.println("      <dc:title>" + lDOMPermValDefn.value + "</dc:title>");
		prDDPins.println("      <dc:description>" + formValue(lDOMPermValDefn.value_meaning) + "</dc:description>");
		prDDPins.println("      <rdfs:label>"  + lDOMPermValDefn.identifier +  "</rdfs:label>");
		prDDPins.println("      <rdfs:comment>The definition of class " + lDOMPermValDefn.identifier + ".</rdfs:comment>");
		prDDPins.println("   </owl:Class>");
		prDDPins.println(" ");
	}	
	
	// write the Data Types as OWL classes
	public  void printDataTypes (PrintWriter prDDPins) {
		for (Iterator<DOMDataType> i = InfoModel.masterDOMDataTypeArr.iterator(); i.hasNext();) {
			DOMDataType lDOMDataType = (DOMDataType) i.next();
			gDataTypeIdArr.add(lDOMDataType.identifier);
			prDDPins.println(" ");
			prDDPins.println(" ");		    
			prDDPins.println("   <!-- PDS4 Data Type: http://pds.nasa.gov/pds4/pds#" + lDOMDataType.identifier + " -->");
			prDDPins.println(" ");

			// write statement to define PDS4 data type as an owl:Class definition
			prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMDataType.identifier + "\">");
			prDDPins.println("      <dc:title>" + lDOMDataType.title + "</dc:title>");
			prDDPins.println("      <dc:description>" + formValue(lDOMDataType.definition) + "</dc:description>");
			prDDPins.println("      <rdfs:label>"  + lDOMDataType.identifier +  "</rdfs:label>");
			prDDPins.println("      <rdfs:comment>The definition of class " + lDOMDataType.identifier + ".</rdfs:comment>");
			prDDPins.println("   </owl:Class>");
		}
	}
	
	// write the Units as OWL classes
	public  void printUnits (PrintWriter prDDPins) {
		for (Iterator<DOMUnit> i = InfoModel.masterDOMUnitArr.iterator(); i.hasNext();) {
			DOMUnit lDOMUnit = (DOMUnit) i.next();
			gUnitIdArr.add(lDOMUnit.identifier);
			prDDPins.println(" ");
			prDDPins.println(" ");		    
			prDDPins.println("   <!-- PDS4 Data Type: http://pds.nasa.gov/pds4/pds#" + lDOMUnit.identifier + " -->");
			prDDPins.println(" ");

			// write statement to define PDS4 data type as an owl:Class definition
			prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lDOMUnit.identifier + "\">");
			prDDPins.println("      <dc:title>" + lDOMUnit.title + "</dc:title>");
			prDDPins.println("      <dc:description>" + formValue(lDOMUnit.definition) + "</dc:description>");
			prDDPins.println("      <rdfs:label>"  + lDOMUnit.identifier +  "</rdfs:label>");
			prDDPins.println("      <rdfs:comment>The definition of class " + lDOMUnit.identifier + ".</rdfs:comment>");
			prDDPins.println("   </owl:Class>");
		}
	}

	// write a set of disjoints
	public  void printDisjoints (String lTitle, ArrayList <String> lClassIdArr, PrintWriter prDDPins) {		
		prDDPins.println(" ");
		prDDPins.println(" ");		    
		prDDPins.println("   <!-- PDS4 Disjoints: http://pds.nasa.gov/pds4/pds#" + "___" + lTitle + "___" + " -->");
		prDDPins.println(" ");
		prDDPins.println("   <rdf:Description>");
		prDDPins.println("      <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AllDisjointClasses\"/>");
		prDDPins.println("      <owl:members rdf:parseType=\"Collection\">");

		// write the subclasses 
		for (Iterator<String> i = lClassIdArr.iterator(); i.hasNext();) {
			String lClassId = (String) i.next();
			prDDPins.println("         <rdf:Description rdf:about=\"" + "http://pds.nasa.gov/pds4/pds#" + lClassId + "\"/>");
		}
		prDDPins.println("      </owl:members>");
		prDDPins.println("   </rdf:Description>");
	}		

	
// = = = = = = = = 	
	
	// Print the Attributes
	public  void printAttrxxxx (PrintWriter prDDPins) {
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> ();
		
		// write the attributes
		for (Iterator<AttrDefn> j = lAttrArr.iterator(); j.hasNext();) {
			AttrDefn lAttr = (AttrDefn) j.next();
			if (! lAttr.isAttribute) continue;
			PDSObjDefn lClass = lAttr.attrParentClass;
			prDDPins.println("\npds:" + lAttr.title + " " + "rdf:type" + " " + "owl:ObjectProperty;");	
			prDDPins.println("  " + "a" + " " + "skos:Concept;");	
			prDDPins.println("  " + "skos:prefLabel" + " " +  " \"" + formValue(lAttr.title) + "\"@en;");	
			prDDPins.println("  " + "skos:definition" + " " +  " \"" + formValue(lAttr.description) + "\"@en;");	
//			prDDPins.println("  " + "skos:semanticRelation" + " " +  "pds:" + lClass.title + ";");	
			prDDPins.println("  " + "skos:historyNote" + " " +  " \"" + "PDS4 Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "  Class Version "  + lClass.versionId + "\"@en.");	
		}
	}		
	
	// Print the Associations
	public  void printAssoc (PDSObjDefn lClass, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lClass.allAttrAssocArr.isEmpty()) return;
		prDDPins.println("              , " + formValue("associationList") + ": [");				
		for (Iterator<AttrDefn> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (isFirst) {
				prDDPins.println("             {" + formValue("association") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("           , {" + formValue("association") + ": {");				
			}				
			prDDPins.println("                " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");	
			prDDPins.println("                " + formValue("title") + ": " + formValue(lAttr.title) + " ,");	
			prDDPins.println("                " + formValue("isAttribute") + ": " + formBooleanValue(lAttr.isAttribute) + " ,");	
			prDDPins.println("                " + formValue("minimumCardinality") + ": " + formValue(lAttr.cardMin) + " ,");	
			prDDPins.println("                " + formValue("maximumCardinality") + ": " + formValue(lAttr.cardMax));	
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		prDDPins.println("           ]");
	}
	
	// Print the the Protege Pins DE
	public  void printAttrxxx (PrintWriter prDDPins) {
		// print the data elements
		boolean isFirst = true;
		ArrayList <AttrDefn> lAttrArr = new ArrayList (InfoModel.masterMOFAttrIdMap.values());
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (! (lAttr.isUsedInClass && lAttr.isAttribute)) continue;
			if (isFirst) {
				prDDPins.println("        {");
				isFirst = false;
			} else {
				prDDPins.println("      , {");				
			}
			prDDPins.println("          " + formValue("attribute") + ": {");	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lAttr.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lAttr.title) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lAttr.attrNameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("steward") + ": " + formValue(lAttr.steward) + " ,");	
			prDDPins.println("            " + formValue("versionId") + ": " + formValue(lAttr.versionIdentifierValue) + " ,");	
			prDDPins.println("            " + formValue("description") + ": " + formValue(lAttr.description) + " ,");	
			prDDPins.println("            " + formValue("isNillable") + ": " + formBooleanValue(lAttr.isNilable) + " ,");	
			prDDPins.println("            " + formValue("isEnumerated") + ": " + formBooleanValue(lAttr.isEnumerated) + " ,");	
			prDDPins.println("            " + formValue("dataType") + ": " + formValue(lAttr.valueType) + " ,");	
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lAttr.getMinimumCharacters2(true,true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lAttr.getMaximumCharacters2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lAttr.getMinimumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lAttr.getMaximumValue2(true,true)) + " ,");	
			prDDPins.println("            " + formValue("pattern") + ": " + formValue(lAttr.getPattern(true)) + " ,");	
			prDDPins.println("            " + formValue("unitOfMeasure") + ": " + formValue(lAttr.getUnitOfMeasure(false)) + " ,");	
			prDDPins.println("            " + formValue("unitId") + ": " + formValue(lAttr.getUnits(false)) + " ,");	
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lAttr.getDefaultUnitId(false)));	
			printPermValues (lAttr, prDDPins);
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}

	// Print the Permissible Values and Value Meanings
	public  void printPermValues (AttrDefn lAttr, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lAttr.permValueArr.isEmpty()) return;
		prDDPins.println("          , " + formValue("PermissibleValueList") + ": [");				
		for (Iterator<PermValueDefn> i = lAttr.permValueArr.iterator(); i.hasNext();) {
			PermValueDefn lPermValueDefn = (PermValueDefn) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("PermissibleValue") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("PermissibleValue") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lPermValueDefn.value) + " ,");	
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue(lPermValueDefn.value_meaning));	
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");
	}
	
	// Print the Data Types
	public  void printDataType (PrintWriter prDDPins) {
		boolean isFirst = true;
		for (Iterator<DataTypeDefn> i = InfoModel.masterDataTypesArr2.iterator(); i.hasNext();) {
			DataTypeDefn lDataType = (DataTypeDefn) i.next();
			if (isFirst) {
				prDDPins.println("        {" + formValue("DataType") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("      , {" + formValue("DataType") + ": {");			
			}	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lDataType.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lDataType.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue(lDataType.nameSpaceIdNC) + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("minimumCharacters") + ": " + formValue(lDataType.getMinimumCharacters2(true)) + " ,");			
			prDDPins.println("            " + formValue("maximumCharacters") + ": " + formValue(lDataType.getMaximumCharacters2(true)) + " ,");	
			prDDPins.println("            " + formValue("minimumValue") + ": " + formValue(lDataType.getMinimumValue2(true)) + " ,");	
			prDDPins.println("            " + formValue("maximumValue") + ": " + formValue(lDataType.getMaximumValue2(true)));	
			printPattern(lDataType, prDDPins);
			prDDPins.println("            }");
			prDDPins.println("         }");
		}
	}	
	
	// Print the data type Pattern
	public  void printPattern (DataTypeDefn lDataType, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lDataType.pattern.isEmpty()) return;
		prDDPins.println("          , " + formValue("patternList") + ": [");	
		for (Iterator<String> i = lDataType.pattern.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("Pattern") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("Pattern") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("Pattern") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("              }");
			prDDPins.println("            }");
		}
		prDDPins.println("           ]");
	}

	// Print the Units
	public  void printUnitsxxx (PrintWriter prDDPins) {
		boolean isFirst = true;
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();
			if (isFirst) {
				prDDPins.println("        {" + formValue("Unit") + ": {");
				isFirst = false;
			} else {
				prDDPins.println("      , {" + formValue("Unit") + ": {");			
			}	
			prDDPins.println("            " + formValue("identifier") + ": " + formValue(lUnit.identifier) + " ,");	
			prDDPins.println("            " + formValue("title") + ": " + formValue(lUnit.title) + " ,");	
			prDDPins.println("            " + formValue("nameSpaceId") + ": " + formValue("pds") + " ,");	
			prDDPins.println("            " + formValue("registrationAuthorityId") + ": " + formValue(DMDocument.registrationAuthorityIdentifierValue) + " ,");
			prDDPins.println("            " + formValue("defaultUnitId") + ": " + formValue(lUnit.default_unit_id));	
			printUnitId (lUnit, prDDPins); 
			prDDPins.println("          }");
			prDDPins.println("        }");
		}
	}	

	// Print the Unit Identifier
	public  void printUnitId (UnitDefn lUnit, PrintWriter prDDPins) {
		boolean isFirst = true;
		if (lUnit.unit_id.isEmpty()) return;
		prDDPins.println("          , " + formValue("unitIdList") + ": [");	
		for (Iterator<String> i = lUnit.unit_id.iterator(); i.hasNext();) {
			String lValue = (String) i.next();
			if (isFirst) {
				prDDPins.println("              {" + formValue("UnitId") + ": {");	
				isFirst = false;
			} else {
				prDDPins.println("            , {" + formValue("UnitId") + ": {");					
			}
//			prDDPins.println("            , {" + formValue("UnitId") + ": {");					
			prDDPins.println("                  " + formValue("value") + ": " + formValue(lValue) + " ,");		
			prDDPins.println("                  " + formValue("valueMeaning") + ": " + formValue("TBD"));		
			prDDPins.println("                }");
			prDDPins.println("             }");
		}
		prDDPins.println("           ]");

	}
						
	// Print the the Protege Pins Properties
	public  void printPDDPPR (PrintWriter prDDPins) {
//		System.out.println("debug printPDDPPR");
		ArrayList <AssocDefn> lSortedAssocArr = new ArrayList <AssocDefn> (InfoModel.masterMOFAssocIdMap.values());
		for (Iterator<AssocDefn> i = lSortedAssocArr.iterator(); i.hasNext();) {
			AssocDefn lAssoc = (AssocDefn) i.next();
			String prDataIdentifier = "PR." + lAssoc.identifier;
//			System.out.println("debug printPDDPPR - prDataIdentifier:" + prDataIdentifier);
			prDDPins.println("([" + prDataIdentifier + "] of Property");
			prDDPins.println("  (administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("  (dataIdentifier \"" + prDataIdentifier + "\")");
			prDDPins.println("  (registeredBy [" + "RA_0001_NASA_PDS_1" + "])");
			prDDPins.println("  (registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");						
			prDDPins.println("  (classOrder \"" + lAssoc.classOrder + "\")");
			prDDPins.println("  (versionIdentifier \"" + InfoModel.identifier_version_id + "\"))");
		}
	}

	// Print the the Protege Pins CD
	public  void printPDDPCD (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lCDAttrArr = new ArrayList <IndexDefn> (InfoModel.cdAttrMap.values());
		for (Iterator<IndexDefn> i = lCDAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "CD_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of ConceptualDomain");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (having ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lDEC = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "DEC_" + lDEC + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");

			lfc = "";
			prDDPins.println("  (representing2 ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				if (lAttr.isEnumerated) {
					prDDPins.print("    [" + lAttr.evdDataIdentifier + "]");
				} else {
					prDDPins.print("    [" + lAttr.nevdDataIdentifier + "]");
				}
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
		
			// write terminological entry
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins DEC
	public  void printPDDPDEC (PrintWriter prDDPins) {
		ArrayList <IndexDefn> lDECAttrArr = new ArrayList <IndexDefn> (InfoModel.decAttrMap.values());
		for (Iterator<IndexDefn> i = lDECAttrArr.iterator(); i.hasNext();) {
			IndexDefn lIndex = (IndexDefn) i.next();
			String gIdentifier = lIndex.identifier;
			String dbIdentifier = "DEC_" + gIdentifier;
			prDDPins.println("([" + dbIdentifier  + "] of DataElementConcept");
			prDDPins.println("	(administrationRecord [" + DMDocument.administrationRecordValue + "])");
			prDDPins.println("	(dataIdentifier \"" + dbIdentifier  + "\")");

			String lfc = "";
			prDDPins.println("  (expressing ");
//			for (Iterator<AttrDefn> j = lIndex.identifier1Arr.iterator(); j.hasNext();) {
			for (Iterator<AttrDefn> j = lIndex.getSortedIdentifier1Arr().iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + lAttr.deDataIdentifier + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(registeredBy [" + DMDocument.registeredByValue+ "])");
			prDDPins.println(" 	(registrationAuthorityIdentifier [" + DMDocument.registrationAuthorityIdentifierValue + "])");
			lfc = "";
			prDDPins.println("  (specifying ");
			for (Iterator<String> j = lIndex.getSortedIdentifier2Arr().iterator(); j.hasNext();) {
				String lCD = (String) j.next();
				prDDPins.print(lfc);
				prDDPins.print("    [" + "CD_" + lCD + "]");
				lfc = "\n";
			}
			prDDPins.print(")\n");
			prDDPins.println(" 	(steward [" + DMDocument.stewardValue + "])");
			prDDPins.println(" 	(submitter [" + DMDocument.submitterValue + "])");
			prDDPins.println(" 	(terminologicalEntry [" + "TE." + gIdentifier + "])");
			prDDPins.println(" 	(versionIdentifier \"" + DMDocument.versionIdentifierValue + "\"))");
			
			// write the terminological entry
			
			String teIdentifier = "TE." + gIdentifier;
			String defIdentifier = "DEF." + gIdentifier;
			String desIdentifier = "DES." + gIdentifier;
			prDDPins.println("([" + teIdentifier + "] of TerminologicalEntry");
			prDDPins.println(" (administeredItemContext [NASA_PDS])");
			prDDPins.println(" (definition [" + defIdentifier + "])");
			prDDPins.println(" (designation [" + desIdentifier + "])");
			prDDPins.println(" (sectionLanguage [LI_English]))");
			prDDPins.println("([" + defIdentifier + "] of Definition");
			prDDPins.println(" (definitionText \"TBD_DEC_Definition\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
			prDDPins.println("([" + desIdentifier + "] of Designation");
			prDDPins.println(" (designationName \"" + gIdentifier + "\")");
			prDDPins.println(" (isPreferred \"TRUE\"))");
		}
	}
	
	// Print the the Protege Pins TE
	public  void printPDDPTE (PrintWriter prDDPins) {
		// print the Terminological Entry
//		for (Iterator<AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> (InfoModel.masterMOFAttrIdMap.values());
//		for (Iterator<AttrDefn> i = InfoModel.getMasterMOFAttrIdMap().iterator(); i.hasNext();) {
		for (Iterator<AttrDefn> i = lAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			if (lAttr.isUsedInClass && lAttr.isAttribute) {
				// print TE section
				prDDPins.println("([" + lAttr.teDataIdentifier + "] of TerminologicalEntry");
				prDDPins.println("  (administeredItemContext [NASA_PDS])");
				prDDPins.println("  (definition ["  + lAttr.defDataIdentifier + "])");
				prDDPins.println("  (designation [" + lAttr.desDataIdentifier + "])");
				prDDPins.println("  (sectionLanguage [" + "LI_English" + "]))");
		
				// print definition section
				prDDPins.println("([" + lAttr.defDataIdentifier + "] of Definition");
				prDDPins.println("  (definitionText \"" + InfoModel.escapeProtegeString(lAttr.description) + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
				
				// print designation section
				prDDPins.println("([" + lAttr.desDataIdentifier + "] of Designation");
				prDDPins.println("  (designationName \"" + lAttr.title + "\")");
				prDDPins.println("  (isPreferred \"" + "TRUE" + "\"))");
			}
		}
	}	

	// Print the the Protege Pins Misc
	public  void printPDDPMISC (PrintWriter prDDPins) {
		// print the Miscellaneous records
		prDDPins.println("([" + DMDocument.administrationRecordValue + "] of AdministrationRecord");
		prDDPins.println("	(administrativeNote \"This is a test load of the PDS4 Data Dictionary from the PDS4 Information Model.\")");
		prDDPins.println("	(administrativeStatus Final)");
		prDDPins.println("	(changeDescription \"PSDD content has been merged into the model.\")");
		prDDPins.println("	(creationDate \"2010-03-10\")");
		prDDPins.println("	(effectiveDate \"2010-03-10\")");
		prDDPins.println("	(explanatoryComment \"This test load is a merge of the PDS4 Information Model and the Planetary Science Data Dictionary (PSDD).\")");
		prDDPins.println("	(lastChangeDate \"2010-03-10\")");
		prDDPins.println("	(origin \"Planetary Data System\")");
		prDDPins.println("	(registrationStatus Preferred)");
		prDDPins.println("	(unresolvedIssue \"Issues still being determined.\")");
		prDDPins.println("	(untilDate \"" + DMDocument.endDateValue + "\"))");
		
		prDDPins.println("([0001_NASA_PDS_1] of RegistrationAuthorityIdentifier");
		prDDPins.println("	(internationalCodeDesignator \"0001\")");
		prDDPins.println("	(opiSource \"1\")");
		prDDPins.println("	(organizationIdentifier \"National Aeronautics and Space Administration\")");
		prDDPins.println("	(organizationPartIdentifier \"Planetary Data System\"))");

		prDDPins.println("([RA_0001_NASA_PDS_1] of RegistrationAuthority");
		prDDPins.println("	(documentationLanguageIdentifier [LI_English])");
		prDDPins.println("	(languageUsed [LI_English])");
		prDDPins.println("	(organizationMailingAddress \"4800 Oak Grove Drive\")");
		prDDPins.println("	(organizationName \"NASA Planetary Data System\")");
		prDDPins.println("	(registrar [PDS_Registrar])");
		prDDPins.println("	(registrationAuthorityIdentifier_v [0001_NASA_PDS_1]))");
	
		prDDPins.println("([NASA_PDS] of Context");
		prDDPins.println("	(dataIdentifier  \"NASA_PDS\"))");
		
		prDDPins.println("([PDS_Registrar] of  Registrar");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(registrarIdentifier \"PDS_Registrar\"))");
		
		prDDPins.println("([Steward_PDS] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([pds] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([img] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([rings] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([ops] of Steward");
		prDDPins.println("	(contact [PDS_Standards_Coordinator])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");

		prDDPins.println("([Submitter_PDS] of Submitter");
		prDDPins.println("	(contact [DataDesignWorkingGroup])");
		prDDPins.println("	(organization [RA_0001_NASA_PDS_1]))");
		
		prDDPins.println("([PDS_Standards_Coordinator] of Contact");
		prDDPins.println("	(contactTitle \"PDS_Standards_Coordinator\")");
		prDDPins.println("	(contactMailingAddress \"4800 Oak Grove Dr, Pasadena, CA 91109\")");
		prDDPins.println("	(contactEmailAddress \"Elizabeth.Rye@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.6135\")");
		prDDPins.println("	(contactName \"Elizabeth Rye\"))");
		
		prDDPins.println("([DataDesignWorkingGroup] of Contact");
		prDDPins.println("	(contactEmailAddress \"Steve.Hughes@jpl.nasa.gov\")");
		prDDPins.println("	(contactInformation \"Jet Propulsion Laboratory\")");
		prDDPins.println("	(contactPhone \"818.354.9338\")");
		prDDPins.println("	(contactName \"J. Steven Hughes\"))");
		
		prDDPins.println("([LI_English] of LanguageIdentification");
		prDDPins.println("  (countryIdentifier \"USA\")");
		prDDPins.println("  (languageIdentifier \"English\"))");
			
		// write the unitOfMeasure
		for (Iterator<UnitDefn> i = InfoModel.masterUnitOfMeasureArr.iterator(); i.hasNext();) {
			UnitDefn lUnit = (UnitDefn) i.next();		
			prDDPins.println("([" + lUnit.title + "] of UnitOfMeasure");
			prDDPins.println("	(measureName \"" + lUnit.title + "\")");
			prDDPins.println("	(defaultUnitId \"" + InfoModel.escapeProtegeString(lUnit.default_unit_id) + "\")");
			prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
			if (! lUnit.unit_id.isEmpty() )
			{
				String lSpace = "";
				prDDPins.print("	(unitId ");
				// set the units
				for (Iterator<String> j = lUnit.unit_id.iterator(); j.hasNext();) {
					String lVal = (String) j.next();
					prDDPins.print(lSpace + "\"" + InfoModel.escapeProtegeString(lVal) + "\"");
					lSpace = " ";
				}
				prDDPins.println("))");
			}
		}
		
		// write the TBD_unitOfMeasure		
		prDDPins.println("([" + "TBD_unit_of_measure_type" + "] of UnitOfMeasure");
		prDDPins.println("	(measureName \"" + "TBD_unit_of_measure_type" + "\")");
		prDDPins.println("	(defaultUnitId \"" + "defaultUnitId" + "\")");
		prDDPins.println("	(precision \"" + "TBD_precision" + "\")");
		prDDPins.println("	(unitId \"" + "TBD_unitId" + "\"))");
		
		// print data types
		for (Iterator<PDSObjDefn> i = InfoModel.masterDataTypesArr.iterator(); i.hasNext();) {
			PDSObjDefn lDataType = (PDSObjDefn) i.next();	
			prDDPins.println("([" + lDataType.title + "] of DataType");
			prDDPins.println("  (dataTypeName \"" + lDataType.title + "\")");
			prDDPins.println("  (dataTypeSchemaReference \"TBD_dataTypeSchemaReference\"))");
		}
	}
	
	// Format the Boolean String for RDF/OWL
	public String formBooleanValue(boolean lBoolean) {
		String rString = "" + lBoolean;
		return formValue(rString);
	}

	// Format the String for RDF/OWL
	public String formValue(String lString) {
		String rString = lString;
		if (rString == null) rString = "null";
		if (rString.indexOf("TBD") == 0) rString = "null";
		rString = InfoModel.escapeXMLChar(rString);
//		rString = "\"" + rString + "\"";
		return rString;
	}
}	
