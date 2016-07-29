package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

// *** This code no longer seems to be used ****

//This code writes the information model to XML schema, attempting to replicate the class hierarchy.
//This main module contains the common routines.

class AAHoldForXMLMainLabelSchema extends Object {
	static String pNS = "";
		
	public AAHoldForXMLMainLabelSchema () {
		return;
	}

//	write the XML Schema data types 
	static public void writeXMLDataTypes (PrintWriter prXML) throws java.io.IOException {
		System.out.println("\ndebug writeXMLDataTypes");		

//	Write the header statements
		prXML.println("");
	    prXML.println("    <" + pNS + "annotation>");
	    prXML.println("      <" + pNS + "documentation>This section of the schema captures the base data types for PDS4 and any constraints those types");
	    prXML.println("        may have. These types should be reused across schemas to promote compatibility. This is one");
	    prXML.println("        component of the common dictionary and thus falls into the common namespace, pds.");
	    prXML.println("      </" + pNS + "documentation>");
	    prXML.println("    </" + pNS + "annotation>");
		prXML.println("");
		
//		Write the data types			
		TreeMap <String, PDSObjDefn> sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.isDataType && lClass.subClassOfTitle.compareTo("Character_Data_Type") == 0) {
				if (! (lClass.title.compareTo("ASCII_Date_Time") == 0 || lClass.title.compareTo("ASCII_Date_Time_UTC") == 0)) {
					sortDataTypeMap.put(lClass.title, lClass);
				}
			}
		}

		Set <String> set1 = sortDataTypeMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			boolean hasPattern = false;
			boolean hasCharacterConstraint = false;
			String lId = (String) iter1.next();
			PDSObjDefn lClass = (PDSObjDefn) sortDataTypeMap.get(lId);
//			System.out.println("debug WRITING writeXMLDataTypes lClass.title:" + lClass.title);
		    prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					lVal = DMDocument.replaceString (lVal, "xsd:", "");
					if (lVal != null) {
						prXML.println("      <" + pNS + "restriction base=\"" + lVal + "\">");
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
					if (lVal != null) {
					    prXML.println("        <" + pNS + "maxLength value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("minimum_characters") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
					    prXML.println("        <" + pNS + "minLength value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("maximum_value") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
					    prXML.println("        <" + pNS + "maxInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("minimum_value") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
					    prXML.println("        <" + pNS + "minInclusive value=\"" + lVal + "\"/>");
					}
				}
				if (lAttr.title.compareTo("pattern") == 0) {
					String lVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						// if not null there there are one or more patterns
						hasPattern = true;
						for (Iterator <String> i = lAttr.valArr.iterator(); i.hasNext();) {
							String lPattern = (String) i.next();
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
		    prXML.println("");
		}
			    
	    prXML.println("    <" + pNS + "simpleType name=\"ASCII_Date_Time\">");
	    prXML.println("      <" + pNS + "union memberTypes=\"pds:ASCII_Date_Time_YMD pds:ASCII_Date_Time_DOY pds:ASCII_Date_YMD pds:ASCII_Date_DOY\"/>");
	    prXML.println("    </" + pNS + "simpleType>");
	    prXML.println("");
	    
	    prXML.println("    <" + pNS + "simpleType name=\"ASCII_Date_Time_UTC\">");
	    prXML.println("      <" + pNS + "restriction base=\"pds:ASCII_Date_Time\">");
	    prXML.println("        <" + pNS + "pattern value=\"\\S+Z\"/>");
	    prXML.println("        <" + pNS + "pattern value=\"\\S+T\\S+Z\"/>");
	    prXML.println("      </" + pNS + "restriction>");
	    prXML.println("    </" + pNS + "simpleType>");
	    prXML.println("");
	    
//		Write the unit types
		sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
//			if (lClass.subClassOfTitle.compareTo("UnitOfMeasure") == 0) {
			if (lClass.subClassOfTitle.compareTo("Unit_Of_Measure") == 0) {
				sortDataTypeMap.put(lClass.title, lClass);
			}
		}
		set1 = sortDataTypeMap.keySet();
		iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String lId = (String) iter1.next();
			PDSObjDefn lClass = (PDSObjDefn) sortDataTypeMap.get(lId);
		    prXML.println("    <" + pNS + "simpleType name=\"" + lClass.title + "\">");
			prXML.println("      <" + pNS + "restriction base=\"" + pNS + "string\">");							
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
//				if (lAttr.title.compareTo("symbol") == 0) {
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
		    prXML.println("");
		}
	}		
}
