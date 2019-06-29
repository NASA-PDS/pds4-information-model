package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/** Driver for getting document
 *
 */
public class GetDOMModelDoc extends Object {
	SectionDefn docSection;
	SectionContentDefn docSectionContent;
	ModelDefn docModel;
	ModelDefn lModelInfo;
	
	ArrayList <String> texSectionFormats;
	
	boolean PDSOptionalFlag = false;
	
	public GetDOMModelDoc () {
		
		//	set up the Tex markers *** delete the following. ***
		texSectionFormats = new ArrayList <String>();
		texSectionFormats.add("\\section");
		texSectionFormats.add("\\subsection");
		texSectionFormats.add("\\subsubsection");
	}
	
/**********************************************************************************************************
	  initialize the models (document, information, data dictionaries) and master dictionaries (attribute and class)
***********************************************************************************************************/
	
	public void getModels (boolean oflag, String docFileName)  throws Throwable {

		PDSOptionalFlag = oflag;

		// get the Spec Document Information from the Protege DMDocuemt Pins file
		ProtPins protPinsInst = new ProtPins();
 		protPinsInst.getProtInst("DOC", "doc", DMDocument.dataDirPath + docFileName);
		HashMap <String, InstDefn> instMap = protPinsInst.instDict;
//		printInst(instMap);
		getDocInfo(instMap);
		getSections(instMap);
		getModels2(instMap);
		getSectionContent(instMap);
		
		Set <String> set1 = DMDocument.docInfo.modelMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String modelId = (String) iter1.next();
			lModelInfo = (ModelDefn) DMDocument.docInfo.modelMap.get(modelId);
//			System.out.println("debug modelId:" + modelId + "  ModelInfo.type:" + lModelInfo.type + "   ModelInfo.filename:" + lModelInfo.filename);
			if (lModelInfo.type.compareTo("ProtPinsGlossary") == 0) {
				lModelInfo.objectid  = new ProtPinsGlossary ();
				ProtPinsGlossary lobjectid  = (ProtPinsGlossary) lModelInfo.objectid;
				lobjectid.getProtPinsGlossary(modelId, DMDocument.dataDirPath + lModelInfo.filename);
			} else if (lModelInfo.type.compareTo("ProtPontModel") == 0) {
				lModelInfo.objectid  = new ProtPontModel ();
				ProtPontModel lobjectid  = (ProtPontModel) lModelInfo.objectid;
				lobjectid.initInfoModel();
				lobjectid.getProtModel(modelId, DMDocument.dataDirPath + lModelInfo.filename);
			}
		}
		if (DMDocument.debugFlag) System.out.println("debug GetDOMModelDoc Done 555");
	}
	
/**********************************************************************************************************
		get the document info
***********************************************************************************************************/
	
	public void getDocInfo (HashMap <String, InstDefn> instMap) throws Throwable {
		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			HashMap <String, ArrayList<String>> slotMap = (HashMap<String, ArrayList<String>>) localInst.genSlotMap;
			if (localInst.className.compareTo("Document") == 0) {
				String docId = getSlotMapValue (slotMap.get("identifier"));
				DMDocument.docInfo = new DocDefn(docId);
				DMDocument.docInfo.title = getSlotMapValue (slotMap.get("title"));
//				ArrayList valarr = (ArrayList) localInst.genSlotMap.get("subtitle");
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("subtitle");
				if (valarr != null) {
					DMDocument.docInfo.subTitle = (String) valarr.get(0);
					// blank seems more appropriate
					if (DMDocument.docInfo.subTitle.compareTo("Final") == 0) DMDocument.docInfo.subTitle = "";
				} else {
					DMDocument.docInfo.subTitle = "DRAFT";
				}
				DMDocument.docInfo.description = getSlotMapValue (slotMap.get("description"));
				DMDocument.docInfo.author = getSlotMapValue (slotMap.get("author"));
				DMDocument.docInfo.version = getSlotMapValue (slotMap.get("version"));
			}
		}
	}
	
/**********************************************************************************************************
		get the document sections
***********************************************************************************************************/
	
	public void getSections (HashMap <String, InstDefn> instMap) throws Throwable {

		String tflag;

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			if (localInst.className.compareTo("Section") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier");
				String sectionId = (String) valarr.get(0);
				docSection = new SectionDefn(sectionId);
				DMDocument.docInfo.sectionMap.put(sectionId, docSection);
				DMDocument.docInfo.sectionArray.add(sectionId);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("title"); docSection.title = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("description"); docSection.description  = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secType"); docSection.secType = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secSubType"); docSection.secSubType = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("selectConstraint"); docSection.selectConstraint = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("latexFormat"); Integer Iind = new Integer ((String) valarr.get(0));
				int ind = Iind.intValue();
				if (ind == 0) {
					docSection.texFormatInd = 0;
				} else {
					ind = ind - 1;
					if (ind < 0 || ind >= texSectionFormats.size()) {
						docSection.texFormatInd = 0;
					} else {
						docSection.texFormatInd = ind;
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("secTOCFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.secTOCFlag = true;
				} else {
					docSection.secTOCFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("subSecTOCFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.subSecTOCFlag = true;
				} else {
					docSection.subSecTOCFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("includeFlag"); tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docSection.includeFlag = true;
				} else {
					docSection.includeFlag = false;
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("imageFileName"); docSection.imageFileName = (String) valarr.get(0);
				if (docSection.imageFileName.compareTo("none") == 0) {
					docSection.imageFlag = false;
				} else {
					docSection.imageFlag = true;
					valarr = (ArrayList <String>) localInst.genSlotMap.get("imageCaption"); docSection.imageCaption = (String) valarr.get(0);
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Section_Model_Content_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSection.sectionModelContentId.add(val);
					}
				}
			}
		}
	}

/**********************************************************************************************************
		get the document models
***********************************************************************************************************/
	
	public void getModels2 (HashMap <String, InstDefn> instMap) throws Throwable {

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();
			if (localInst.className.compareTo("Model") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier");
				String modelId = (String) valarr.get(0);
				docModel = new ModelDefn(modelId);
				DMDocument.docInfo.modelMap.put(modelId, docModel);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("type"); docModel.type = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("filename"); docModel.filename  = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("DDIncludeFlag"); String tflag =  (String) valarr.get(0);
				if (tflag.compareTo("true") == 0) {
					docModel.ddincludeflag = true;
				} else {
					docModel.ddincludeflag = false;
				}
			}
		}
	}	

/**********************************************************************************************************
		get the document section contents
***********************************************************************************************************/
	
	public void getSectionContent (HashMap <String, InstDefn> instMap) throws Throwable {

		ArrayList <InstDefn> lInstArr = new ArrayList <InstDefn> (instMap.values());
		for (Iterator <InstDefn> i = lInstArr.iterator(); i.hasNext();) {
			InstDefn localInst = (InstDefn) i.next();

//			System.out.println("debug getSectionContent instRDFId:" + instRDFId);
			if (localInst.className.compareTo("Section_Model_Content") == 0) {
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get("identifier"); 
				String contentId = (String) valarr.get(0);
				docSectionContent = new SectionContentDefn(contentId);
				DMDocument.docInfo.sectionContentMap.put(contentId, docSectionContent);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Model_Identifier"); docSectionContent.modelId = (String) valarr.get(0);
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Include_Class_Type");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.includeClassType.add(val);
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Include_Class_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.includeClassId.add(val);
					}
				}
				valarr = (ArrayList <String>) localInst.genSlotMap.get("Exclude_Class_Id");
				if (valarr != null) {
					for (Iterator <String> j = valarr.iterator(); j.hasNext();) {
						String val = (String) j.next();
						docSectionContent.excludeClassId.add(val);
					}
				}
			}
		}
	}
				
/**********************************************************************************************************
		miscellaneous routines
***********************************************************************************************************/
		
	/**
	*  Get Slot Value
	*/
	public String getSlotMapValue (ArrayList <String> valarr) {
		if (! (valarr == null || valarr.isEmpty())) {
			return (String) valarr.get(0);
		}
		return null;
	}

	/**
		* Replace string with string (gleaned from internet)
		*/
	
	static String replaceString (String str, String pattern, String replace) {
			int s = 0;
			int e = 0;
			StringBuffer result = new StringBuffer();
			
			while ((e = str.indexOf(pattern, s)) >= 0) {
				result.append(str.substring(s, e));
				result.append(replace);
				s = e+pattern.length();
			}
			result.append(str.substring(s));
			return result.toString();
	 }
	
	/**
	*  check to see if string is numeric
	*/
	static public boolean isInteger (String s) {
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i <  sb.length (); i++) {
			if (! Character.isDigit(sb.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public void printInst (HashMap <String, InstDefn> instMap) throws Throwable {
		Set <String> set1 = instMap.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String instRDFId = (String) iter1.next();
			System.out.println("\ndebug instRDFId:" + instRDFId);
			InstDefn localInst = (InstDefn) instMap.get(instRDFId);
			System.out.println("      rdfIdentifier:" + localInst.rdfIdentifier);
			System.out.println("      identifier:" + localInst.identifier);
			System.out.println("      title:" + localInst.title);
//			System.out.println("      classifiedIdentifier:" + localInst.classifiedIdentifier);
			System.out.println("      className:" + localInst.className);
			System.out.println("      description:" + localInst.description);
			Set <String> set2 = localInst.genSlotMap.keySet();
			Iterator <String> iter2 = set2.iterator();
			while(iter2.hasNext()) {
				String aname = (String) iter2.next();
				System.out.println("      attribute:" + aname);
				ArrayList <String> valarr = (ArrayList <String>) localInst.genSlotMap.get(aname);
				if (valarr != null) {
					for (Iterator <String> i = valarr.iterator(); i.hasNext();) {
						String val = (String) i.next();
						System.out.println("        val:" + val);
					}
				}
			}
		}
	}	
}
