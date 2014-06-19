package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class XMI2LabelSchema2 extends Object {
	
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
	
	public XMI2LabelSchema2 () {
		uuidNum = 1000000;
		return;
	}

//	write the XML File
	public void writeXMIFile (String todaysDate) throws java.io.IOException {
		
//		System.out.println("\ndebug XMIFile");		
		
//	    prXML = new PrintWriter(new FileWriter("SchemaXMI/" + "XMI" + "_" + InfoModel.lab_version_id + "_wNames.xmi", false));
	    prXML = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaXMI/" + "XMI" + "_" + InfoModel.lab_version_id + "_wNames.xmi", false));
	    
		writeXMIHdr (todaysDate);
		/**
		*  Iterate through all classes
		*/
		int count = 0;
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();	
			writeXMIClass (lClass);
//			if (count++ > 10) { break; };
		}
		// write the associations
		Set <String> set1 = assocMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String assocId = (String) iter1.next();
			Assoc lAssoc = (Assoc) assocMap.get(assocId);
			System.out.println("debug writeXMIClass assocation lAssoc.id:" + lAssoc.id);
			prXML.println("        <ownedMember xmi:type=\"uml:Association\"" + " xmi:id=\"" + lAssoc.id + " xmi:name=\"" + lAssoc.title + "\" visibility=\"public\">");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.fromPropId + "\"/>");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.toPropId + "\"/>");
			prXML.println("        </ownedMember>");
		}
		writeXMIFtr();
		prXML.close();
	}
	
//	write the XMI Class
	public void  writeXMIClass (PDSObjDefn lClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClass lClass.title" + lClass.title);
		
		// get associations for this class
//		prXML.println("*** ready to print association lClass.title:" + lClass.title);
		for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) j.next();
//			prXML.println("    printing association lAssoc.title:" + lAssoc.title);
			prXML.println("        <ownedMember xmi:type=\"uml:Association\" xmi:id=\"" + lAssoc.title + "\" visibility=\"public\">");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.title + "_OwnedEnd_1" + "\"/>");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.title + "_OwnedEnd_2" + "\"/>");
			prXML.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lAssoc.title + "_OwnedEnd_1" + "\" visibility=\"private\" type=\"" + lClass.title + "\" association=\"" + lAssoc.title + "\"/>");

			String cmin = lAssoc.cardMin;
			String cminType = "uml:LiteralInteger";
			String cmax = lAssoc.cardMax;
			String cmaxType = "uml:LiteralInteger";
			if (cmax.compareTo("*") == 0) {
				cmax = "-1";
				cmaxType = "uml:LiteralUnlimitedNatural";
			}			
			ArrayList <String> lAssocClassTitleArr = lAssoc.valArr; 
			for (Iterator<String> k = lAssocClassTitleArr.iterator(); k.hasNext();) {
				String lAssocClassTitle = k.next();
				PDSObjDefn lClass2 = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lAssocClassTitle);
				if (lClass2 != null) {
					prXML.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lAssoc.title + "_OwnedEnd_2" + "\" name=\"" + lAssoc.title + "\" visibility=\"private\" type=\"" + lClass2.title + "\" association=\"" + lAssoc.title + "\">");
//					prXML.println("            <upperValue xmi:type=\"uml:LiteralInteger\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"1\"/>");
//					prXML.println("            <lowerValue xmi:type=\"uml:LiteralInteger\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\"/>");
					prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
					prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
					prXML.println("          </ownedEnd>");
				}
			}
			prXML.println("        </ownedMember>");
		}

		// print the class definition and generalization
//		prXML.println("*** ready to print class lClass.title:" + lClass.title);
		prXML.println("        <ownedMember xmi:type=\"uml:Class\" xmi:id=\"" + lClass.title + "\" name=\"" + lClass.title + "\" visibility=\"public\">");
		if (lClass.subClassOfTitle.compareTo("USER") != 0) {
			prXML.println("          <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + lClass.title + "_Generalization" + "\" general=\"" + lClass.subClassOfTitle + "\"/>");
		}
		
		// get attributes for this class
//		prXML.println("*** ready to print attributes lClass.title:" + lClass.title);
		for (Iterator<AttrDefn> i = lClass.ownedAttribute.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			String cmin = lAttr.cardMin;
			String cminType = "uml:LiteralInteger";
			String cmax = lAttr.cardMax;
			String cmaxType = "uml:LiteralInteger";
			if (cmax.compareTo("*") == 0) {
				cmax = "-1";
				cmaxType = "uml:LiteralUnlimitedNatural";
			}			
//			prXML.println("    printing attribute lAttr.title:" + lAttr.title);
			prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lAttr.title + "\" visibility=\"private\">");
//			prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lAttr.title + "\" visibility=\"private\" type=\"_12_5_1_f470362_1265779821953_8771_145\">");
			prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
//			prXML.println("            <upperValue xmi:type=\"uml:LiteralInteger\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"1\"/>");
//			prXML.println("            <lowerValue xmi:type=\"uml:LiteralInteger\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\"/>");
			prXML.println("          </ownedAttribute>");
		}
		prXML.println("        </ownedMember>	");
	}
	
	
//	write the XMI Header
	public void  writeXMIHdr (String todaysDate) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIHdr");		
		prXML.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXML.println("  <!-- Generated from the PDS4 Information Model -->");
		prXML.println("  <!-- Version: " + InfoModel.ont_version_id + " -->");
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

