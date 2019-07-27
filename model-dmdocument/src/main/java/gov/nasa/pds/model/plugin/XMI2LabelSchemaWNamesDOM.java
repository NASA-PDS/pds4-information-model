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

class XMI2LabelSchemaWNamesDOM extends Object {
	
	//***************************************************************************************
	// This is the original XMI write routine. In MagicDraw, relationship names are included.
	//***************************************************************************************
	// evolution
	// XMI2LabelSchema2 -> XMI2LabelSchema2DOM -> XMI2LabelSchemaWNamesDOM
	//
	
	UMLModelClassifications lUMLModelClassifications;
	ArrayList <String> classesToWrite;
	
	public class Assoc {
		String id;
		String title;
		String fromPropId;
		String toPropId;
	}
	
	TreeMap <String, Assoc> assocMap  = new TreeMap <String, Assoc> ();
	TreeMap <String, String> assocPropMap  = new TreeMap <String, String> ();
	
	PrintWriter prXMLCMOF;
	PrintWriter prXMLXMI;
	int uuidNum;
	
	public XMI2LabelSchemaWNamesDOM () {
		uuidNum = 1000000;
		lUMLModelClassifications = new UMLModelClassifications ();
//		System.out.println("debug XMI2LabelSchemaWNamesDOM UMLModelClassifications.activeClasses:" + UMLModelClassifications.activeClasses);
		classesToWrite = new ArrayList <String> ();
		classesToWrite.add ("Information_Object");
		classesToWrite.add ("Access_Rights_Information");
		classesToWrite.add ("Content_Information");
		classesToWrite.add ("Context_Information");
		classesToWrite.add ("Fixity_Information");
		classesToWrite.add ("Identification_Information");
		classesToWrite.add ("Preservation_Description_Information");
		classesToWrite.add ("Provenance_Information");
		classesToWrite.add ("Reference_Information");
		classesToWrite.add ("Representation_Information");
		classesToWrite.add ("Registry");
		classesToWrite.add ("Repository");
		return;
	}

//	write the XML File
	public void writeXMIFile (String todaysDate) throws java.io.IOException {
		String lFileNameXMI = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecUMLXMI2;
//		lFileNameXMI = DOMInfoModel.replaceString(lFileNameXMI, "_wNames", "_DOM_wNames");
		String lFileNameCMOF = lFileNameXMI;
		lFileNameCMOF = DOMInfoModel.replaceString(lFileNameCMOF, ".xmi", ".cmof");
	    prXMLXMI = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileNameXMI)), "UTF-8"));
	    prXMLCMOF = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileNameCMOF)), "UTF-8"));
	    
		writeXMIHdrXMI (todaysDate);
		writeXMIHdrCMOF (todaysDate);
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();	
			if (classesToWrite.contains(lClass.title)) {
				writeXMIClassXMI (lClass);
				writeXMIClassCMOF (lClass);
			}
		}
		writeXMIFtrXMI();
		writeXMIFtrCMOF();
		prXMLXMI.close();
		prXMLCMOF.close();
	}
	
//	write the XMI Class
	public void  writeXMIClassCMOF (DOMClass lClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClassCMOF - Class - lClass.identifier:" + lClass.identifier);
//		System.out.println("debug                 Class - lClass.identifier:" + lClass.identifier);
	       prXMLCMOF.println("      <ownedMember xmi:type=\"cmof:Class\" xmi:id=\"" + lClass.title + "\" name=\"" + lClass.title + "\" visibility=\"public\">");
	       prXMLCMOF.println("        <superClass xmi:idref=\"" + lClass.subClassOfTitle + "\"/>");
	       
	       if (lClass.ownedAssocArr.size() > 0) {
		       for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
					DOMProp lProp = (DOMProp) j.next();
					if (lProp != null)  {		
				        if (lProp.title.indexOf("__Method") > -1) {
							if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMClass) {
								DOMClass lMethodClass = (DOMClass) lProp.hasDOMObject;
//								prXMLCMOF.println("        <ownedOperation xmi:id=\"" + lMethodClass.title + "\" name=\"" + lMethodClass.title + "\" visibility=\"public\"/>");
								prXMLCMOF.println("        <ownedOperation xmi:type=\"cmof:Operation\" xmi:id=\"" + lMethodClass.title + "\" name=\"" + lMethodClass.title + "\" visibility=\"public\"/>");
//								System.out.println("debug                 Method Class - lMethodClass.identifier:" + lMethodClass.identifier);
								for (Iterator<DOMProp> k = lMethodClass.ownedAssocArr.iterator(); k.hasNext();) {
										DOMProp lParmProp = (DOMProp) k.next();
										if (lParmProp != null)  {		
//											System.out.println("debug                 CHECK ParmProp - lParmProp.identifier:" + lParmProp.identifier);
									        if ((lParmProp.title.indexOf("__Input") > -1) || (lParmProp.title.indexOf("__Output") > -1)) {
//												System.out.println("debug                 FOUND ParmProp - lParmProp.identifier:" + lParmProp.identifier);
												if (lParmProp.hasDOMObject != null && lParmProp.hasDOMObject instanceof DOMClass) {
													DOMClass lParmClass = (DOMClass) lParmProp.hasDOMObject;
//													System.out.println("debug                 FOUND ParmClass - lParmClass.identifier:" + lParmClass.identifier);
													prXMLCMOF.println("          <ownedParameter xmi:type=\"cmof:Parameter\" xmi:id=\"" + lParmClass.title + "\" name=\"" + lParmClass.title + "\" visibility=\"public\"/>");
												}
									        }
										}
							       }
							}
				        }
					}
		       }
	 	       prXMLCMOF.println("      </ownedMember>");
	       }
	}
	
//	write the XMI Header
	public void  writeXMIHdrXMI (String todaysDate) throws java.io.IOException {
		prXMLXMI.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXMLXMI.println("  <!-- Generated from the Information Model -->");
//		prXMLXMI.println("  <!-- Version: " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " -->");
		prXMLXMI.println("  <!-- Generated: " + todaysDate + " -->");
//		prXMLXMI.println("  <xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.0\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">");
		prXMLXMI.println("  <xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.0\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">");
		prXMLXMI.println("    <xmi:Documentation xmi:Exporter=\"InfomationModelExporter\" xmi:ExporterVersion=\"0.0.9\"/>");
//		prXMLXMI.println("    <uml:Model name=\"Information_Model\" xmi:id=\"Information_Model\" visibility=\"public\">");
		prXMLXMI.println("    <uml:Model xmi:type='uml:Model' xmi:id='Information_Model' name='Information_Model'>");
//		prXMLXMI.println("      <ownedMember xmi:type=\"uml:Package\" xmi:id=\"Information_Model_Package\" name=\"Information_Model_Package\" visibility=\"public\">");
		prXMLXMI.println("      <packagedElement xmi:type='uml:Package' xmi:id='Information_Model_Package' name='Information_Model_Package'>");

/*		xmlns:StandardProfile='http://www.omg.org/spec/UML/20131001/StandardProfile'>
		<uml:Model xmi:type='uml:Model' xmi:id='Information_Model_Package' name='Information_Model_Package'>
			<packagedElement xmi:type='uml:Package' xmi:id='Information_Model' name='Information_Model'>
				<packagedElement xmi:type='uml:Class' xmi:id='Information_Object' name='Information_Object'>*/
		prXMLXMI.println(" ");
	}
				
//	write the XMI Footer
	public void  writeXMIFtrXMI () throws java.io.IOException {
		prXMLXMI.println(" ");
		prXMLXMI.println("      </packagedElement>");
		prXMLXMI.println("    </uml:Model>");
		prXMLXMI.println("  </xmi:XMI>");
	}
	
//	write the XMI Header
	public void  writeXMIHdrCMOF (String todaysDate) throws java.io.IOException {		
		prXMLCMOF.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
//		prXMLCMOF.println("  <!-- Generated from the Information Model -->");
//		prXMLCMOF.println("  <!-- Version: " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " -->");
//		prXMLCMOF.println("  <!-- Generated: " + todaysDate + " -->");
		prXMLCMOF.println("  <xmi:XMI xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\" xmi:version=\"2.1\">");
		prXMLCMOF.println("    <cmof:Package xmlns:cmof=\"http://schema.omg.org/spec/MOF/2.0/cmof.xml\" xmi:id=\"Information_Model_Package\" name=\"Information_Model_Package\" visibility=\"public\">");
		prXMLCMOF.println("      <ownedMember xmi:type=\"cmof:Package\" xmi:id=\"Information_Model\" name=\"Information_Model\" visibility=\"public\">");
		prXMLCMOF.println(" ");
	}
				
//	write the XMI Footer
	public void  writeXMIFtrCMOF () throws java.io.IOException {	
		prXMLCMOF.println(" ");
		prXMLCMOF.println("    </ownedMember>");
		prXMLCMOF.println("  </cmof:Package>");
		prXMLCMOF.println("</xmi:XMI>");
	}
	
//	write the XMI Class
	public void writeXMIClassXMI (DOMClass lClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClassXMI - Class - lClass.identifier:" + lClass.identifier);
//		System.out.println("debug                 Class - lClass.identifier:" + lClass.identifier);
		prXMLXMI.println("        <packagedElement xmi:type='uml:Class' xmi:id='" + lClass.title + "' name='" + lClass.title + "'>");
		if (classesToWrite.contains(lClass.subClassOfTitle)) {
			prXMLXMI.println("          <generalization xmi:type='uml:Generalization' xmi:idref='" + lClass.subClassOfTitle + "' general='" + lClass.subClassOfTitle + "'/>");
		}
  
		if (lClass.ownedAssocArr.size() > 0) {
			for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (lProp != null)  {		
					if (lProp.title.indexOf("__Method") > -1) {
						if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMClass) {
							DOMClass lMethodClass = (DOMClass) lProp.hasDOMObject;
							prXMLXMI.println("          <ownedOperation xmi:type='uml:Operation' xmi:id='" + lMethodClass.title + "' name='" + lMethodClass.title + "' visibility='public'>");
//							System.out.println("debug                 Method Class - lMethodClass.identifier:" + lMethodClass.identifier);
							for (Iterator<DOMProp> k = lMethodClass.ownedAssocArr.iterator(); k.hasNext();) {
								DOMProp lParmProp = (DOMProp) k.next();
								if (lParmProp != null)  {		
//								System.out.println("debug                 CHECK ParmProp - lParmProp.identifier:" + lParmProp.identifier);
									if ((lParmProp.title.indexOf("__Input") > -1) || (lParmProp.title.indexOf("__Output") > -1)) {
										String lDirection = "in";
										if ((lParmProp.title.indexOf("__Output") > -1)) lDirection = "out";
//									System.out.println("debug                 FOUND ParmProp - lParmProp.identifier:" + lParmProp.identifier);
										if (lParmProp.hasDOMObject != null && lParmProp.hasDOMObject instanceof DOMClass) {
											DOMClass lParmClass = (DOMClass) lParmProp.hasDOMObject;
//											System.out.println("debug                 FOUND ParmClass - lParmClass.identifier:" + lParmClass.identifier);
//											prXMLXMI.println("          <ownedParameter xmi:type=\"cmof:Parameter\" xmi:id=\"" + lParmClass.title + "\" name=\"" + lParmClass.title + "\" visibility=\"public\"/>");
											prXMLXMI.println("            <ownedParameter xmi:type='uml:Parameter' xmi:id='" + lParmClass.title + "' name='" + lParmClass.title + "' visibility='public' direction='" + lDirection + "'/>");
										}
									}
								}
							}
							prXMLXMI.println("          </ownedOperation>");
						}
					}
				}
			}
		}
		prXMLXMI.println("        </packagedElement>");
		prXMLXMI.println("");
		
	}
	
//	write the XMI Class
	public void  writeXMIClassXMIxxx (DOMClass lClass) throws java.io.IOException {
		
		// get associations for this class
        String prevTitle = "";
		for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
			
			DOMProp lProp = (DOMProp) j.next();
			if (lProp != null)  {		
		        if (!prevTitle.equals(lProp.title)){
		   	       prXMLXMI.println("        <ownedMember xmi:type=\"uml:Association\" xmi:id=\"" + lProp.title + "\" visibility=\"public\">");
			       prXMLXMI.println("          <memberEnd xmi:idref=\"" + lProp.title + "_OwnedEnd_1" + "\"/>");
			       prXMLXMI.println("          <memberEnd xmi:idref=\"" + lProp.title + "_OwnedEnd_2" + "\"/>");
			       prXMLXMI.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lProp.title + "_OwnedEnd_1" + "\" visibility=\"private\" type=\"" + lClass.title + "\" association=\"" + lProp.title + "\"/>");
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
			    prXMLXMI.println("          <ownedEnd xmi:type=\"uml:Property\" xmi:id=\"" + lProp.title + "_OwnedEnd_2" + "\" name=\"" + lProp.title + "\" visibility=\"private\" type=\"" + lAssocClass.title + "\" association=\"" + lProp.title + "\">");
			    prXMLXMI.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			    prXMLXMI.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
			    prXMLXMI.println("          </ownedEnd>");
			} 
			if (!prevTitle.equals(lProp.title))
					prXMLXMI.println("        </ownedMember>");
			prevTitle = lProp.title;
			
		}	
	   
		// print the class definition and generalization
		prXMLXMI.println("        <ownedMember xmi:type=\"uml:Class\" xmi:id=\"" + lClass.title + "\" name=\"" + lClass.title + "\" visibility=\"public\">");
		if (lClass.subClassOfTitle.compareTo("USER") != 0) {
			prXMLXMI.println("          <generalization xmi:type=\"uml:Generalization\" xmi:id=\"" + lClass.title + "_Generalization" + "\" general=\"" + lClass.subClassOfTitle + "\"/>");
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
			prXMLXMI.println("          <ownedAttribute xmi:type=\"uml:Property\" xmi:id=\"" + lAttr.title + "\" name=\"" + lProp.title + "\" visibility=\"private\">");
			prXMLXMI.println("            <upperValue xmi:type=\"" + cmaxType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmax + "\"/>");
			prXMLXMI.println("            <lowerValue xmi:type=\"" + cminType + "\" xmi:id=\"" + lProp.title + getNextUUID()  + "\" visibility=\"public\" value=\"" + cmin + "\"/>");
			prXMLXMI.println("          </ownedAttribute>");
		}
		prXMLXMI.println("        </ownedMember>	");
	}
		
//	write the next unique identifier
	public String  getNextUUID () {
		uuidNum++;
		Integer ival = new Integer(uuidNum);
		return "_" + ival.toString();
	}
}

