package gov.nasa.pds.model.plugin;

import java.io.*;
import java.util.*;

class WriteUML25XMIFile extends Object {
	
	//***************************************************************************************
	// This is a new version of the original XMI write routine. 
	// In MagicDraw, relationship names are included.
	//***************************************************************************************
	// evolution
	// XMI2LabelSchema2 -> XMI2LabelSchema2DOM -> XMI2LabelSchemaWNamesDOM -> WriteUML25XMIFile
	//
	
	ArrayList <String> classesToWrite;
	
	public class Assoc {
		String assocId;
		String prop1Id;
		String prop2Id;
		String title;
//		String fromClassId;
		String fromClassTitle;
//		String toClassId;
		String toClassTitle;
	}
	
	TreeMap <String, Assoc> assocMap  = new TreeMap <String, Assoc> ();
//	TreeMap <String, String> assocPropMap  = new TreeMap <String, String> ();
	
	PrintWriter prXMLXMI;
	int uuidNum;
	
	public WriteUML25XMIFile () {
		uuidNum = 1000000;
//		System.out.println("debug XMI2LabelSchemaWNamesDOM UMLModelClassifications.activeClasses:" + UMLModelClassifications.activeClasses);
		classesToWrite = new ArrayList <String> ();
		classesToWrite = new ArrayList <String> ();
		classesToWrite.add ("Access");
		classesToWrite.add ("Access_Rights_Information");
		classesToWrite.add ("Access_Service");
		classesToWrite.add ("Access_Aid");
		classesToWrite.add ("Finding_Aid");
		classesToWrite.add ("Ordering_Aid");
		classesToWrite.add ("Retrieving_Aid");
		classesToWrite.add ("Archival_Information_Package");
		classesToWrite.add ("Consumer");
		classesToWrite.add ("Content_Information");
		classesToWrite.add ("Context_Information");
		classesToWrite.add ("Dissemination_Information_Package");
		classesToWrite.add ("Fixity_Information");
		classesToWrite.add ("Information_Object");
		classesToWrite.add ("Information_Package");
		classesToWrite.add ("Interaction_Pattern");
		classesToWrite.add ("Invoke");
		classesToWrite.add ("MessagedService");
		classesToWrite.add ("receiveMessage");
		classesToWrite.add ("sendMessage");
		classesToWrite.add ("Order_Agreement");
		classesToWrite.add ("Preservation_Description_Information");
		classesToWrite.add ("Progress");
		classesToWrite.add ("Protocol");
		classesToWrite.add ("Provenance_Information");
		classesToWrite.add ("PublishSubscribe");
		classesToWrite.add ("Reference_Information");
		classesToWrite.add ("Representation_Information");
		classesToWrite.add ("Request");
		classesToWrite.add ("Send");
		classesToWrite.add ("Service");
		classesToWrite.add ("Submission_Information_Package");
		classesToWrite.add ("Submit");	
//		classesToWrite.add ("getAccessRightsInformation");
//		classesToWrite.add ("getContextInformation");
//		classesToWrite.add ("getDataObject");
//		classesToWrite.add ("getDigitalObject");
//		classesToWrite.add ("getFixityInformation");
//		classesToWrite.add ("getIdentificationInformation");
//		classesToWrite.add ("getInformationObject");
//		classesToWrite.add ("getPreservationDescriptionInformation");
//		classesToWrite.add ("getProvenanceInformation");
//		classesToWrite.add ("getReferenceInformation");
//		classesToWrite.add ("getRepresentationInformation");
//		classesToWrite.add ("queryForAIP");
//		classesToWrite.add ("queryRegistry");
//		classesToWrite.add ("retrieveDIP");

		return;
	}

//	write the XML File
	public void writeXMIFile (String todaysDate) throws java.io.IOException {
		String lFileNameXMI = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecUMLXMI2;
		lFileNameXMI = DOMInfoModel.replaceString(lFileNameXMI, "_wNames", "_wNames_new");
	    prXMLXMI = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileNameXMI)), "UTF-8"));
	    
		writeXMIHdrXMI (todaysDate);
		// write classes
		for (Iterator<String> i = classesToWrite.iterator(); i.hasNext();) {
			String lClassName = (String) i.next();
			String lClassId = DOMInfoModel.getClassIdentifier("oais", lClassName);
			DOMClass lClass = DOMInfoModel.masterDOMClassIdMap.get(lClassId);
			if (lClass != null) {
				writeXMIClassXMI (lClass);
			}
		}
		// write associations
		ArrayList <Assoc> assocArr = new ArrayList <Assoc> (assocMap.values());
		for (Iterator<Assoc> i = assocArr.iterator(); i.hasNext();) {
			Assoc lAssoc = (Assoc) i.next();
//			prXMLXMI.println("        <ownedMember xmi:type=\"uml:Association\"" + " xmi:id=\"" + lAssoc.id + " xmi:name=\"" + lAssoc.title + "\" visibility=\"public\">");
//			prXMLXMI.println("          <memberEnd xmi:idref=\"" + lAssoc.fromPropId + "\"/>");
//			prXMLXMI.println("          <memberEnd xmi:idref=\"" + lAssoc.toPropId + "\"/>");
//			prXMLXMI.println("        </ownedMember>");

			prXMLXMI.println("          <packagedElement xmi:type='uml:Association' xmi:id='" + lAssoc.assocId + "' name='" + lAssoc.title + "'>");
			prXMLXMI.println("            <memberEnd xmi:idref='" + lAssoc.prop1Id + "'/>");
			prXMLXMI.println("            <memberEnd xmi:idref='" + lAssoc.prop2Id + "'/>");
			prXMLXMI.println("            <ownedEnd xmi:type='uml:Property' xmi:id='" + lAssoc.prop2Id + "' visibility='private' type='" + lAssoc.fromClassTitle + "' association='" + lAssoc.assocId + "'/>");
			prXMLXMI.println("          </packagedElement>");

		}
		
		writeXMIFtrXMI();;
		prXMLXMI.close();
	}
	
//	write the XMI Header
	public void  writeXMIHdrXMI (String todaysDate) throws java.io.IOException {
		prXMLXMI.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");						
		prXMLXMI.println("  <!-- Generated from the Information Model -->");
		prXMLXMI.println("  <!-- Version: " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " -->");
		prXMLXMI.println("  <!-- Generated: " + todaysDate + " -->");
		prXMLXMI.println("  <xmi:XMI xmi:version=\"2.1\" xmlns:uml=\"http://schema.omg.org/spec/UML/2.0\" xmlns:xmi=\"http://schema.omg.org/spec/XMI/2.1\">");
		prXMLXMI.println("    <xmi:Documentation xmi:Exporter=\"InfomationModelExporter\" xmi:ExporterVersion=\"0.0.9\"/>");
		prXMLXMI.println("    <uml:Model xmi:type='uml:Model' xmi:id='Information_Model' name='Information_Model'>");
		prXMLXMI.println("      <packagedElement xmi:type='uml:Package' xmi:id='Information_Model_Package' name='Information_Model_Package'>");
		prXMLXMI.println(" ");
	}
				
//	write the XMI Footer
	public void  writeXMIFtrXMI () throws java.io.IOException {
		prXMLXMI.println(" ");
		prXMLXMI.println("      </packagedElement>");
		prXMLXMI.println("    </uml:Model>");
		prXMLXMI.println("  </xmi:XMI>");
	}
	
//	write the XMI Class
	public void writeXMIClassXMI (DOMClass lClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClassXMI - Class - lClass.identifier:" + lClass.identifier);
//		System.out.println("debug                 Class - lClass.identifier:" + lClass.identifier);
		prXMLXMI.println("        <packagedElement xmi:type='uml:Class' xmi:id='" + lClass.title + "' name='" + lClass.title + "'>");
		if (classesToWrite.contains(lClass.subClassOfTitle)) {
//			prXMLXMI.println("          <generalization xmi:type='uml:Generalization' xmi:idref='" + lClass.subClassOfTitle + "' general='" + lClass.subClassOfTitle + "'/>");
			prXMLXMI.println("          <generalization xmi:type='uml:Generalization' xmi:id='" + lClass.subClassOfTitle + "_Generalization" + "' general='" + lClass.subClassOfTitle + "'/>");
		}
  
		if (lClass.ownedAssocArr.size() > 0) {
			for (Iterator<DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (lProp != null)  {		
					if (lProp.title.indexOf("__Method") > -1) {
						if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMClass) {
							DOMClass lMethodClass = (DOMClass) lProp.hasDOMObject;
							prXMLXMI.println("          <ownedOperation xmi:type='uml:Operation' xmi:id='" + lMethodClass.title + "' name='" + lMethodClass.title + "' visibility='public'/>");
						}
					} else if (lProp.title.indexOf("has_") > -1) {
						if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMClass) {
							DOMClass lAssocClass = (DOMClass) lProp.hasDOMObject;
							Assoc lAssoc = new Assoc();
							lAssoc.assocId = lProp.title + getNextUUID ();
							lAssoc.prop1Id = lProp.title + getNextUUID ();
							lAssoc.prop2Id = lProp.title + getNextUUID ();
							lAssoc.title = lProp.title;
//							lAssoc.fromClassId = lClass.title + getNextUUID ();
							lAssoc.fromClassTitle = lClass.title;
//							lAssoc.toClassId = lAssocClass.title + getNextUUID ();
							lAssoc.toClassTitle = lAssocClass.title;
							assocMap.put(lAssoc.assocId, lAssoc);
							prXMLXMI.println("          <ownedAttribute xmi:type='uml:Property' xmi:id='" + lAssoc.prop1Id  + "' visibility='public'" + " type='" + lAssocClass.title +"' association='" + lAssoc.assocId + "'" +  "/>");
//							                            <ownedAttribute xmi:type='uml:Property' xmi:id='_19_0_2_f470362_1577750889472_847048_5658' visibility='private'    type='Access_Service' association='_19_0_2_f470362_1577750889472_329572_5657'/>
						}
						
					}
				}
			}
		}
		prXMLXMI.println("        </packagedElement>");
		prXMLXMI.println("");
		
	}
	
	
//	write the XMI Class
	public void writeXMIClassXMIxxx (DOMClass lClass) throws java.io.IOException {
//		System.out.println("\ndebug writeXMIClassXMI - Class - lClass.identifier:" + lClass.identifier);
//		System.out.println("debug                 Class - lClass.identifier:" + lClass.identifier);
		prXMLXMI.println("        <packagedElement xmi:type='uml:Class' xmi:id='" + lClass.title + "' name='" + lClass.title + "'>");
		if (classesToWrite.contains(lClass.subClassOfTitle)) {
//			prXMLXMI.println("          <generalization xmi:type='uml:Generalization' xmi:idref='" + lClass.subClassOfTitle + "' general='" + lClass.subClassOfTitle + "'/>");
			prXMLXMI.println("          <generalization xmi:type='uml:Generalization' xmi:id='" + lClass.subClassOfTitle + "' general='" + lClass.subClassOfTitle + "'/>");
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
			
//	write the next unique identifier
	public String  getNextUUID () {
		uuidNum++;
		Integer ival = new Integer(uuidNum);
		return "_" + ival.toString();
	}
}

