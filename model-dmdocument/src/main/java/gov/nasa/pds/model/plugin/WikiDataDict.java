package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class WikiDataDict extends Object {
	
	PrintWriter prIntroWiki, prDataTypesWiki;
//	ArrayList <String> DTCUsed, CDUsed, VDUsed;
	ArrayList <String> DTCUsed, VDUsed;

	
	String wikiH1, wikiH2, wikiH3;
//	String wikiBold;
	
	public WikiDataDict () {
		DTCUsed = new ArrayList <String> ();
//		CDUsed = new ArrayList <String> ();
		VDUsed = new ArrayList <String> ();
		
		wikiH1 = "h4. ";
		wikiH2 = "h5. ";
		wikiH3 = "h6. ";
//		wikiBold = "*";
		return;
	}
	
//	write the Intro to a wiki file
	public void writeIntroWikiDD () throws java.io.IOException {
//		prIntroWiki = new PrintWriter(new FileWriter("SchemaWikiDD/" + "pds4ddintro" + ".txt", false));
		prIntroWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDD/" + "pds4ddintro" + ".txt", false));
//		System.out.println("\ndebug  starting writeIntroWiki");
		prIntroWiki.println("h4. PDS4 Data Design Working Group");
		prIntroWiki.println("h5. Version " + InfoModel.ont_version_id + " - " + DMDocument.masterTodaysDate);
		prIntroWiki.println("h5. Generated from the PDS4 Information Model Version " + InfoModel.ont_version_id);
		prIntroWiki.println("h4. Table Of Contents");
		prIntroWiki.println("# h5. [Introduction|#Introduction]");
		prIntroWiki.println("# h5. [Audience|#Audience]");
		prIntroWiki.println("# h5. [Acknowledgements|#Acknowledgements]");
		prIntroWiki.println("# h5. [Scope|#Scope]");
		prIntroWiki.println("# h5. [Related Documents|#Related_Documents]");
		prIntroWiki.println("# h5. [Terminology|#Terminology]");
		prIntroWiki.println("# h5. [Attribute Definitions|#Attribute_Definitions]");
		prIntroWiki.println("# h5. [Class Definitions|#Class_Definitions]");
		prIntroWiki.println("# h5. [Data Type Definitions|#Data_Type_Definitions]");
		prIntroWiki.println("# h5. [Indices|#Main_Index]");
		prIntroWiki.println("# h6. [Attribute Index|#Attribute_Index]");
		prIntroWiki.println("# h6. [Class Index|#Class_Index]");
		prIntroWiki.close();
	}
	
//	write the Intro to a wiki file
	public void writeIntroWikiDDA3 () throws java.io.IOException {
//		prIntroWiki = new PrintWriter(new FileWriter("SchemaWikiDDA3/" + "pds4ddintro" + ".txt", false));
		prIntroWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "pds4ddintro" + ".txt", false));
//		System.out.println("\ndebug  starting writeIntroWiki");
		prIntroWiki.println("h4. PDS4 Data Design Working Group");
		prIntroWiki.println("h5. Version " + InfoModel.ont_version_id + " - " + DMDocument.masterTodaysDate);
		prIntroWiki.println("h5. Generated from the PDS4 Information Model Version " + InfoModel.ont_version_id);
		prIntroWiki.println("h4. Table Of Contents");
		prIntroWiki.println("# h5. [Introduction|#Introduction]");
		prIntroWiki.println("# h5. [Audience|#Audience]");
		prIntroWiki.println("# h5. [Acknowledgements|#Acknowledgements]");
		prIntroWiki.println("# h5. [Scope|#Scope]");
		prIntroWiki.println("# h5. [Related Documents|#Related_Documents]");
		prIntroWiki.println("# h5. [Terminology|#Terminology]");
		prIntroWiki.println("# h5. [Product/Class Definitions|#Class_Definitions]");
		prIntroWiki.println("# h5. [Attribute Definitions|#Attribute_Definitions]");
		prIntroWiki.println("# h5. [Data Type Definitions|#Data_Type_Definitions]");
		prIntroWiki.println("# h5. [Indices|#Main_Index]");
		prIntroWiki.println("# h6. [Product Index|#Product_Index]");
		prIntroWiki.println("# h6. [Class Index|#Class_Index]");
		prIntroWiki.println("# h6. [Attribute Index|#Attribute_Index]");
		prIntroWiki.close();
	}

//	write the data types to a wiki file
	public void writeDataTypesWikiFiles () throws java.io.IOException {
//		prDataTypesWiki = new PrintWriter(new FileWriter("SchemaWikiDDA3/" + "pds4datatypes" + ".txt", false));
		prDataTypesWiki = new PrintWriter(new FileWriter(DMDocument.outputDirPath + "SchemaWikiDDA3/" + "pds4datatypes" + ".txt", false));
		writeDataTypesWiki (InfoModel.masterDataTypesArr, prDataTypesWiki);
		prDataTypesWiki.close();
	}
	
//	write the data types to a wiki file
	public void writeDataTypesWiki (ArrayList <PDSObjDefn> lSortedDataTypeClassArr, PrintWriter prDataTypesWiki) throws java.io.IOException {
//		System.out.println("\ndebug  starting writeDataTypesWiki");
		prDataTypesWiki.println("{anchor:" + "Data_Type_Definitions" + "}");
		prDataTypesWiki.println("# " + wikiH1 + "PDS4 Data Type Definitions" + "  " + DMDocument.masterTodaysDate);
		prDataTypesWiki.println("Generated from the PDS4 Information Model Version " + InfoModel.ont_version_id);
				
		for (Iterator <PDSObjDefn> i = lSortedDataTypeClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			writeDataType (1, lClass, new ArrayList<PDSObjDefn>(), new ArrayList<PDSObjDefn>(), prDataTypesWiki);
		}
		
		for (Iterator<String> i = DTCUsed.iterator(); i.hasNext();) {
			String lcid = (String) i.next();
			PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lcid);
			String lTitle = lClass.title;
			String lDesc = lClass.description;
			prDataTypesWiki.println("");
			prDataTypesWiki.println("{anchor:" + lTitle + "}");
			prDataTypesWiki.println(wikiH2 + "data_type_concept: " + lTitle);
			prDataTypesWiki.println("description: *" + lDesc + "*");
		}
		
		for (Iterator<String> i = VDUsed.iterator(); i.hasNext();) {
			String lcid = (String) i.next();
			if (lcid != null) {
//				System.out.println("debug            WikiDataDict lcid:" + lcid);
				PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassMap.get(lcid);
				String lTitle = lClass.title;
				String lDesc = lClass.description;
				prDataTypesWiki.println("");
				prDataTypesWiki.println("{anchor:" + lTitle + "}");
				prDataTypesWiki.println(wikiH2 + "value domain: *" + lTitle + "*");
				prDataTypesWiki.println("description: *" + lDesc + "*");
			}
		}
	}

	public void writeDataType (int classCard, PDSObjDefn curClass, ArrayList<PDSObjDefn> visitClassList, ArrayList<PDSObjDefn> recurseClassList, PrintWriter prDataTypesWiki) {
		String classTitle = curClass.title;
//		prDataTypesWiki.println("");
		prDataTypesWiki.println("{anchor:" + classTitle + "}");
//		prDataTypesWiki.println("#* " + wikiH2 + "Data Type:" + classTitle);
		prDataTypesWiki.println("#* " + wikiH2 + "" + classTitle);
		prDataTypesWiki.println("description: *" + curClass.description + "*");
		
		ArrayList<AttrDefn> assocArr = new ArrayList<AttrDefn>();
		assocArr.addAll(curClass.ownedAssociation);
		assocArr.addAll(curClass.inheritedAssociation);
		
//		get attributes for this class
		ArrayList<AttrDefn> attrArr = new ArrayList<AttrDefn>();
		attrArr.addAll(curClass.ownedAttribute);
		attrArr.addAll(curClass.inheritedAttribute);
		for (Iterator<AttrDefn> i = attrArr.iterator(); i.hasNext();) {
			AttrDefn lattr = (AttrDefn) i.next();		
			String ltitle = lattr.title;
			if (ltitle.compareTo("pattern") == 0) {
				continue;
			}
			String valList = "";
			String valDel = "";
			ArrayList <String> valArr = lattr.valArr;
			for (Iterator<String> j = valArr.iterator(); j.hasNext();) {
				String lval = (String) j.next();
				valList += valDel + lval;
				valDel = ", ";
			}
			if (valList.compareTo("") != 0) {
				prDataTypesWiki.println(ltitle + ": *" + valList + "*");
			}
		}
		
		for (Iterator<AttrDefn> i = assocArr.iterator(); i.hasNext();) {
			AttrDefn lattr = (AttrDefn) i.next();
			String ltitle = lattr.title;
			ArrayList <String> valArr = lattr.valArr;
			if (ltitle.compareTo("data_type_concept") == 0) {
				for (Iterator<String> j = valArr.iterator(); j.hasNext();) {
					String lval = (String) j.next();
					PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lval);
					String lClassId = lClass.rdfIdentifier;
					if (! DTCUsed.contains(lClassId)) {
						DTCUsed.add(lClassId);
					}
					prDataTypesWiki.println("data_type_concept: [" + lval + "|#" + lval + "]");
				}
			}
			
// *** this does not seem to be used
			if (ltitle.compareTo("conceptual_domain") == 0) {
				for (Iterator<String> j = valArr.iterator(); j.hasNext();) {
//					String lval = (String) j.next();
					String lCDId = (String) j.next();
					String lCDTitle = lCDId;
					if (lCDId.length() >= 3) {
						lCDTitle = lCDId.substring(3);
					}
//					PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lval);
//					String lClassId = lClass.rdfIdentifier;
//					if (! CDUsed.contains(lClassId)) {
//						CDUsed.add(lClassId);
//					}
					prDataTypesWiki.println("conceptual_domain: [" + lCDTitle + "|#" + lCDId + "]");
				}
			}
			if (ltitle.compareTo("value_domain") == 0) {
				for (Iterator<String> j = valArr.iterator(); j.hasNext();) {
					String lval = (String) j.next();
					PDSObjDefn lClass = (PDSObjDefn) InfoModel.masterMOFClassTitleMap.get(lval);
					String lClassId = lClass.rdfIdentifier;
					if (! VDUsed.contains(lClassId)) {
						VDUsed.add(lClassId);
					}
					prDataTypesWiki.println("value_domain: [" + lval + "|#" + lval + "]");
				}
			}
		}
//		prDataTypesWiki.println("----");
//		prDataTypesWiki.println(" ");
	}
	
	/**
	*  Get Slot Value
	*/
	public String getSlotMapValue (ArrayList <String> valarr) {
		if (! (valarr == null || valarr.isEmpty())) {
			return (String) valarr.get(0);
		}
		return null;
	}	
}
