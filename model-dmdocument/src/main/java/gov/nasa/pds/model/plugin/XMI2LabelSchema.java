package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class XMI2LabelSchema extends Object {
	
	public class XMIClass {
		String id;
		String title;
		String subClassOfTitle;
		String generalizationId;
		ArrayList <XMIProp> xmiPropArr;
		ArrayList <XMIAssoc> xmiAssocArr;
	}
	
	public class XMIProp {
		String id;
		String title;
		String cardMin;
		String cardMax;
	}
	
	public class XMIAssoc {
		String id;
		String title;
		String fromAssocPropId;
		String fromAssocClassTitle;
		String fromAssocPropMinCard;
		String fromAssocPropMaxCard;
		String toAssocPropId;
		String toAssocClassTitle;
		String toAssocPropMinCard;
		String toAssocPropMaxCard;
	}
	
	TreeMap <String, XMIClass> XMIClassMap  = new TreeMap <String, XMIClass> ();
	HashMap <String, XMIAssoc> XMIAssocMap  = new HashMap <String, XMIAssoc> ();
	HashMap <String, String> XMIClassTitleIdMap = new HashMap <String, String> ();
	HashMap <String, String> XMIAssocTitleIdMap = new HashMap <String, String> ();
		
	PrintWriter prXML;
	int uuidNum;
	
	public XMI2LabelSchema () {
		uuidNum = 1000000;
		return;
	}

//	write the XML File
	public void getXMIElements () {
		
//		System.out.println("\ndebug getXMIElements");		
			    
		// Iterate through all classes
		int count = 0;
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();	
//			if (count++ > 100) { break; };
			XMIClass lXMIClass = new XMIClass ();
//			System.out.println("\ndebug getXMIElements lClass.title:" + lClass.title);
			lXMIClass.title = lClass.title;
			lXMIClass.id = lClass.title;
			lXMIClass.generalizationId = lClass.title + getNextUUID();
			lXMIClass.subClassOfTitle = lClass.subClassOfTitle;
			lXMIClass.xmiPropArr = new ArrayList <XMIProp> ();
			lXMIClass.xmiAssocArr = new ArrayList <XMIAssoc> ();
			XMIClassMap.put(lXMIClass.id, lXMIClass);
			XMIClassTitleIdMap.put(lXMIClass.title, lXMIClass.id);
						
			// get attributes for this class
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				XMIProp lXMIProp = new XMIProp ();
				lXMIProp.id = lAttr.title + getNextUUID();
//				System.out.println("     lXMIProp.id:" + lXMIProp.id);
				lXMIProp.title = lAttr.title;
				lXMIProp.cardMin = lAttr.cardMin;
				lXMIProp.cardMax = lAttr.cardMax;
				lXMIClass.xmiPropArr.add(lXMIProp);
			}

			// get associations for this class
			for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
				AttrDefn lAssoc = (AttrDefn) j.next();
				ArrayList <String> lClassTitleArr = lAssoc.valArr; 
				for (Iterator<String> k = lClassTitleArr.iterator(); k.hasNext();) {
					String lClassTitle = k.next();
					PDSObjDefn lClass2 = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lClassTitle);
					if (lClass2 != null) {
						XMIAssoc lXMIAssoc = new XMIAssoc ();						
						lXMIAssoc.id = lAssoc.title + getNextUUID();
//						System.out.println("     lXMIAssoc.id:" + lXMIAssoc.id);
						lXMIAssoc.title = lAssoc.title;
						lXMIAssoc.fromAssocPropId = lAssoc.title + getNextUUID();
						lXMIAssoc.fromAssocClassTitle = lXMIClass.title;
						lXMIAssoc.fromAssocPropMinCard = "-9";				// do not display the cardinalities
						lXMIAssoc.fromAssocPropMaxCard = "-9";
						lXMIAssoc.toAssocPropId = "TBD_toAssocPropId";
						lXMIAssoc.toAssocClassTitle = lClass2.title; 
						lXMIAssoc.toAssocPropMinCard = lAssoc.cardMin;
						lXMIAssoc.toAssocPropMaxCard = lAssoc.cardMax;
						lXMIClass.xmiAssocArr.add(lXMIAssoc);
						if (! XMIAssocMap.containsKey(lXMIAssoc.id)) {
							XMIAssocMap.put(lXMIAssoc.id, lXMIAssoc);
							XMIAssocTitleIdMap.put(lXMIAssoc.title, lXMIAssoc.id);
						}
					}
				}
			}
		}
		
		// resolve association identifiers	
		Set <String> set1 = XMIAssocMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {

			// get association (created by the 'from' class)
			String xmiId = (String) iter1.next();
//			System.out.println("\ndebug resolve association xmiId:" + xmiId);
			XMIAssoc lXMIAssoc = XMIAssocMap.get(xmiId);
			
			// get 'to' class for association
			String lToClassTitle = lXMIAssoc.toAssocClassTitle;
//			System.out.println("    lToClassTitle:" + lToClassTitle);
			String lCId2 = XMIClassTitleIdMap.get(lToClassTitle);   // *** missing to class - Tagged_Digital_Object
//			System.out.println("    lCId2:" + lCId2);
			if (lCId2 != null) {			
				XMIClass lXMIClass2 = XMIClassMap.get(lCId2);
//				System.out.println("    lXMIClass2.id:" + lXMIClass2.id);
				if (lXMIClass2 != null) {
					// create 'to' property association
					lXMIAssoc.toAssocPropId = lXMIAssoc.title  + getNextUUID();
//					System.out.println("    lXMIAssoc.toAssocPropId:" + lXMIAssoc.toAssocPropId);
				
					// add the original association to the 'to' class
					lXMIClass2.xmiAssocArr.add(lXMIAssoc);	
				}
			}
		}
	}

//	write the XML File
	public void writeXMIFile (String todaysDate) throws java.io.IOException {
		
//		System.out.println("\ndebug XMIFile");		
		
//	    prXML = new PrintWriter(new FileWriter("SchemaXMI/" + "XMI" + "_" + InfoModel.lab_version_id + "_clean.xmi", false));    
	    prXML = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaXMI/" + "XMI" + "_" + InfoModel.lab_version_id + "_clean.xmi", false));    
		writeXMIHdr (todaysDate);
		
		/**
		*  Iterate through all classes
		*/
		Set <String> set1 = XMIClassMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			XMIClass lXMIClass = XMIClassMap.get(lId);
			writeXMIClass (lXMIClass);
		}
		
		// write the Associations
		set1 = XMIAssocMap.keySet();
		iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			XMIAssoc lAssoc = XMIAssocMap.get(lId);
//			System.out.println("\ndebug writeXMIClass assocation lAssoc.id:" + lAssoc.id);
			prXML.println("        <ownedMember xmi:type=\"uml:Association\"" + " xmi:id=\"" + lAssoc.id + "\" xmi:name=\"" + lAssoc.title + "\" visibility=\"public\">");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.fromAssocPropId + "\"/>");
			prXML.println("          <memberEnd xmi:idref=\"" + lAssoc.toAssocPropId + "\"/>");
			prXML.println("        </ownedMember>");
		}
		
		writeXMIFtr();
		prXML.close();
	}

//	write the XMI Class
	public void  writeXMIClass (XMIClass lXMIClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClass lClass.title" + lClass.title);
		
		// print the class definition and generalization
		prXML.println("        <ownedMember xmi:type=\"uml:Class\" xmi:id=\"" + lXMIClass.title + "\" name=\"" + lXMIClass.title + "\" visibility=\"public\">");
		if (lXMIClass.subClassOfTitle.compareTo("USER") != 0) {
			prXML.println("          <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + lXMIClass.generalizationId + "\" general=\"" + lXMIClass.subClassOfTitle + "\"/>");
		}
		
		// get attributes for this class
		for (Iterator<XMIProp> i = lXMIClass.xmiPropArr.iterator(); i.hasNext();) {
			XMIProp lAttr = (XMIProp) i.next();
			String lCMin = lAttr.cardMin;
			String lCMinType = "uml:LiteralInteger";
			String lCMax = lAttr.cardMax;
			String lCMaxType = "uml:LiteralInteger";
			if (lCMax.compareTo("*") == 0) {
				lCMax = "-1";
				lCMaxType = "uml:LiteralUnlimitedNatural";
			}			
//			prXML.println("    printing attribute lAttr.title:" + lAttr.title);
			prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lAttr.title + "\" visibility=\"private\">");
			prXML.println("            <upperValue xmi:type=\"" + lCMaxType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + lCMax + "\"/>");
			prXML.println("            <lowerValue xmi:type=\"" + lCMinType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + lCMin + "\"/>");
			prXML.println("          </ownedAttribute>");
		}

		// write associations for this class
		for (Iterator<XMIAssoc> j = lXMIClass.xmiAssocArr.iterator(); j.hasNext();) {
			XMIAssoc lAssoc = (XMIAssoc) j.next();
			String lClassTitle = "TBD_class_title";
			String lPropId = "TBD_prop_id";
			String lCMin = "1";
			String lCMax = "1";
			
			if (lXMIClass.title.compareTo(lAssoc.toAssocClassTitle) == 0) {
				lClassTitle = lAssoc.toAssocClassTitle;
				lPropId = lAssoc.toAssocPropId;
				lCMin = lAssoc.toAssocPropMinCard;
				lCMax = lAssoc.toAssocPropMaxCard;								
			} else if (lXMIClass.title.compareTo(lAssoc.fromAssocClassTitle) == 0) {
				lClassTitle = lAssoc.fromAssocClassTitle;
				lPropId = lAssoc.fromAssocPropId;
				lCMin = lAssoc.fromAssocPropMinCard;
				lCMax = lAssoc.fromAssocPropMaxCard;				
			}
			String lCMinType = "uml:LiteralInteger";
			String lCMaxType = "uml:LiteralInteger";
			if (lCMax.compareTo("*") == 0) {
				lCMax = "-1";
				lCMaxType = "uml:LiteralUnlimitedNatural";
			}
			if (lCMin.compareTo("-9") == 0) {
				prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lPropId + "\" xmi:name=\"" + lAssoc.title + "\" visibility=\"private\" type=\"" + lClassTitle  + "\" association=\"" + lAssoc.id + "\">");
			} else {
				prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lPropId + "\" xmi:name=\"" + lAssoc.title + "\" visibility=\"private\" type=\"" + lClassTitle  + "\" association=\"" + lAssoc.id + "\">");
				prXML.println("            <upperValue xmi:type=\"" + lCMaxType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + lCMax + "\"/>");
				prXML.println("            <lowerValue xmi:type=\"" + lCMinType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + lCMin + "\"/>");
			}
			prXML.println("          </ownedAttribute>");
		}
		prXML.println("        </ownedMember>");
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
		
		prXML.println("      </ownedMember>");
		prXML.println("    </uml:Model>");
		prXML.println("  </xmi:XMI>");
	}
	
//	get the next UUID
	public String  getNextUUID () {
		uuidNum++;
		Integer ival = new Integer(uuidNum);
		return "_" + ival.toString();
	}
}
