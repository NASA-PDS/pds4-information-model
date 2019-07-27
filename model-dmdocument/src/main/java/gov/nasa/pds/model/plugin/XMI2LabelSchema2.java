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
		String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecUMLXMI2+"_MOF";
	    prXML = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
	    
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
		
		// get associations for this class
		for (Iterator<AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
			AttrDefn lAssoc = (AttrDefn) j.next();
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

			for (Iterator<PDSObjDefn> k = lAssoc.valClassArr.iterator(); k.hasNext();) {
				PDSObjDefn lClass2 = k.next();
				if (lClass2 != null) {
					prXML.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lAssoc.title + "_OwnedEnd_2" + "\" name=\"" + lAssoc.title + "\" visibility=\"private\" type=\"" + lClass2.title + "\" association=\"" + lAssoc.title + "\">");
					prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
					prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lAssoc.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
					prXML.println("          </ownedEnd>");
				}
			}
			prXML.println("        </ownedMember>");
		}

		// print the class definition and generalization
		prXML.println("        <ownedMember xmi:type=\"uml:Class\" xmi:id=\"" + lClass.title + "\" name=\"" + lClass.title + "\" visibility=\"public\">");
		if (lClass.subClassOfTitle.compareTo("USER") != 0) {
			prXML.println("          <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + lClass.title + "_Generalization" + "\" general=\"" + lClass.subClassOfTitle + "\"/>");
		}
		
		// get attributes for this class
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
			prXML.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lAttr.title + "\" visibility=\"private\">");
			prXML.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			prXML.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lAttr.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
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

