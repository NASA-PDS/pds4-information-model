package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Writes the PDS4 DD content to a RDF/OWL file 
 *   
 */

class WriteDOMRDFOWLFile extends Object{

	PrintWriter prDDPins;

	public WriteDOMRDFOWLFile () {
		return;
	}

	// write the RDF/OWL file
	public void writeOWLFile (String lFileName) throws java.io.IOException {
		prDDPins = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		printPDDPHdr();
		printPDDPBody ();
		printPDDPFtr();
		prDDPins.close();
	}	
	
	// Print the RDF/OWL Header
	public void printPDDPHdr () {

		
		prDDPins.println("<rdf:RDF");
		prDDPins.println("   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
		prDDPins.println("   xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"");
		prDDPins.println("   xmlns:owl=\"http://www.w3.org/2002/07/owl#\"");
		prDDPins.println("   xmlns:dc=\"http://purl.org/dc/elements/1.1/\">");
		prDDPins.println("   xmlns:pds=\"http://pds.nasa.gov/pds4/pds#\">");
		prDDPins.println(" ");
		prDDPins.println("   <owl:Ontology rdf:about=\"http://pds.nasa.gov/pds4/pds\">");
		prDDPins.println("      <dc:title>PDS4 Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:title>");
		prDDPins.println("      <dc:description>The ontology for the PDS4 Information Model V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</dc:description>");
		prDDPins.println("   </owl:Ontology>");
		prDDPins.println(" ");
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
		rString = InfoModel.escapeJSONChar(rString);
		rString = "\"" + rString + "\"";
		return rString;
	}

	// Print the RDF/OWL Footer
	public  void printPDDPFtr () {
		prDDPins.println("</rdf:RDF>");
	}
	
//	print the RDF/OWL body
	public  void printPDDPBody () {
		printClass (prDDPins);
//		printAttr (prDDPins);
	}
	
	// Print the classes
	public  void printClass (PrintWriter prDDPins) {
		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (DOMInfoModel.masterDOMClassIdMap.values());
		for (Iterator<DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.title.indexOf("PDS3") > -1) continue;
			if (lClass.isDataType) continue;
			if (lClass.isUnitOfMeasure) continue;
			if (lClass.isUSERClass) continue;
			if (lClass.title.compareTo("Product_Components") == 0) continue;
			if (! (lClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0 && lClass.steward.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0)) continue;
			
			prDDPins.println("   <owl:Class rdf:about=\"http://pds.nasa.gov/pds4/pds#" + lClass.title + "\">");
			
			// write the superclass
			if (! (lClass.subClassOfTitle.indexOf("TBD") == 0 || lClass.subClassOfTitle.indexOf("TNDO") == 0))  
				prDDPins.println("      <rdfs:subClassOf rdf:resource=\"http://pds.nasa.gov/pds4/pds#" + lClass.subClassOfTitle + "\"/>");	
			
			prDDPins.println("      <rdfs:label>"  + lClass.title +  "</rdfs:label>");
			prDDPins.println("      <rdfs:comment>The definition of class " + lClass.title + ".</rdfs:comment>");
			prDDPins.println("   </owl:Class>");
			prDDPins.println(" ");
			

		}
	}		
}	
