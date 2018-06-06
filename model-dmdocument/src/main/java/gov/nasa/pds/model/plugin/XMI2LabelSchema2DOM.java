package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

class XMI2LabelSchema2DOM extends Object {
	
	//***************************************************************************************
	// This is the original XMI write routine. In MagicDraw, relationship names are included.
	//***************************************************************************************
	
	public class Assoc {
		String id;
		String title;
		String fromPropId;
		String toPropId;
	}
	
	TreeMap <String, Assoc> assocMap  = new TreeMap <String, Assoc> ();
	TreeMap <String, String> assocPropMap  = new TreeMap <String, String> ();
	
	PrintWriter prXML;
	int uuidNum;
	
	public XMI2LabelSchema2DOM () {
		uuidNum = 1000000;
		return;
	}

//	write the XML File
	public void writeXMIFile (String todaysDate) throws java.io.IOException {
		String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecUMLXMI2+"_DOM";
	    prXML = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
	    
		writeXMIHdr (todaysDate);
		/**
		*  Iterate through all classes
		*/
		int count = 0;
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();	
			writeXMIClass (lClass);
//			if (count++ > 10) { break; };
		}

/* remove - later , code is never executed
		// write the associations
		Set <String> set1 = assocMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String assocId = (String) iter1.next();
			Assoc lAssoc = (Assoc) assocMap.get(assocId);
		//	System.out.println("debug writeXMIClass assocation lAssoc.id:" + lAssoc.id);
			prXML.println("        <ownedMember xmi:type=\"uml:Association\"" + " xmi:id=\"" + lAssoc.id + " xmi:name=\"" + lAssoc.title + "\" visibility=\"public\">");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.fromPropId + "\"/>");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.toPropId + "\"/>");
			prXML.println("        </ownedMember>");
		}
*/
		writeXMIFtr();
		prXML.close();
	}
	
//	write the XMI Class
	public void  writeXMIClass (DOMClass lClass) throws java.io.IOException {
		
		// get associations for this class
        String prevTitle = "";
		for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
			
			DOMProp lProp = (DOMProp) j.next();
			if (lProp != null)  {		
		        if (!prevTitle.equals(lProp.title)){
		   	       prXML.println("        <ownedMember xmi:type=\"uml:Association\" xmi:id=\"" + lProp.title + "\" visibility=\"public\">");
			       prXML.println("          <memberEnd xmi:idref=\"" + lProp.title + "_OwnedEnd_1" + "\"/>");
			       prXML.println("          <memberEnd xmi:idref=\"" + lProp.title + "_OwnedEnd_2" + "\"/>");
			       prXML.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lProp.title + "_OwnedEnd_1" + "\" visibility=\"private\" type=\"" + lClass.title + "\" association=\"" + lProp.title + "\"/>");
		        }		    
			    String cmin = lProp.cardMin;
			    String cminType = "uml:LiteralInteger";
			    String cmax = lProp.cardMax;
			    String cmaxType = "uml:LiteralInteger";
			    if (cmax.compareTo("*") == 0) {
			  	    cmax = "-1";
				    cmaxType = "uml:LiteralUnlimitedNatural";
			    }						
	 	        DOMClass lAssocClass = (DOMClass) lProp.hasDOMObject;
			//	if (k == 1) System.out.println("Printing DOM properties");
			    prXML.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lProp.title + "_OwnedEnd_2" + "\" name=\"" + lProp.title + "\" visibility=\"private\" type=\"" + lAssocClass.title + "\" association=\"" + lProp.title + "\">");
			    prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			    prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
			    prXML.println("          </ownedEnd>");
			} 
			if (!prevTitle.equals(lProp.title))
					prXML.println("        </ownedMember>");
			prevTitle = lProp.title;
			
		}	
	   
		

		// print the class definition and generalization
		prXML.println("        <ownedMember xmi:type=\"uml:Class\" xmi:id=\"" + lClass.title + "\" name=\"" + lClass.title + "\" visibility=\"public\">");
		if (lClass.subClassOfTitle.compareTo("USER") != 0) {
			prXML.println("          <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + lClass.title + "_Generalization" + "\" general=\"" + lClass.subClassOfTitle + "\"/>");
		}
		
		// get attributes for this class
		for (Iterator<DOMProp> i = lClass.ownedAttrArr.iterator(); i.hasNext();) {
			DOMProp lProp = (DOMProp) i.next();
			DOMAttr lAttr = (DOMAttr) lProp.hasDOMObject;
			String cmin = lProp.cardMin;
			String cminType = "uml:LiteralInteger";
			String cmax = lProp.cardMax;
			String cmaxType = "uml:LiteralInteger";
			if (cmax.compareTo("*") == 0) {
				cmax = "-1";
				cmaxType = "uml:LiteralUnlimitedNatural";
			}			
			prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lProp.title + "\" visibility=\"private\">");
			prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
			prXML.println("          </ownedAttribute>");
		}
		prXML.println("        </ownedMember>	");
	}

//	write the XMI Header
	public void  writeXMIHdr (String todaysDate) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIHdr");		
		prXML.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXML.println("  <!-- Generated from the PDS4 Information Model -->");
		prXML.println("  <!-- Version: " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " -->");
		prXML.println("  <!-- Generated: " + todaysDate + " -->");
		prXML.println("  <xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.0\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">");
		prXML.println("    <xmi:Documentation xmi:Exporter=\"PDS4InfoModelExporter\" xmi:ExporterVersion=\"0.0.9\"/>");
		prXML.println("    <uml:Model name=\"PDS4_Information_Model\" xmi:id=\"PDS4_Information_Model\" visibility=\"public\">");
		prXML.println("      <ownedMember xmi:type=\"uml:Package\" xmi:id=\"PDS4_Information_Model_Common\" name=\"PDS4_Information_Model_Common\" visibility=\"public\">");
		prXML.println(" ");
	}
				
//	write the XMI Footer
	public void  writeXMIFtr () throws java.io.IOException {
//		System.out.println("\ndebug writeXMIHdr");		
		prXML.println(" ");
		
		prXML.println("    </ownedMember>");
		prXML.println("  </uml:Model>");
		prXML.println("</xmi:XMI>");
	}
//	write the XMI Footer
	public String  getNextUUID () {
		uuidNum++;
		Integer ival = new Integer(uuidNum);
		return "_" + ival.toString();
	}
}

