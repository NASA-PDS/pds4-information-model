package gov.nasa.pds.model.plugin;
import java.util.*;

class ProtPinsGlossary extends Object{
	TreeMap <String, AttrDefn> glossMap;
	TreeMap <String, String> glossTitleIdMap;
	
	ProtPins protPinsInst;   
	
	public ProtPinsGlossary () {
		glossMap = new TreeMap <String, AttrDefn> ();
		glossTitleIdMap = new TreeMap <String, String> ();
		return;
	}
	
	public void getProtPinsGlossary (String subModelId, String fname) throws Throwable {

		protPinsInst = new ProtPins();
		protPinsInst.getProtInst("PDS3", "pds3", fname);
		
		HashMap <String, InstDefn> tDict = protPinsInst.instDict;
		Set <String> set1 = tDict.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String instRDFId = (String) iter1.next();		
			InstDefn localInst = (InstDefn) tDict.get(instRDFId);
			AttrDefn attrClass = new AttrDefn(localInst.rdfIdentifier);
			attrClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
			attrClass.subModelId = subModelId;
			attrClass.title = InfoModel.unEscapeProtegeString(localInst.title);
			attrClass.genAttrMap = localInst.genSlotMap;
			ArrayList attrdescarr = (ArrayList) attrClass.genAttrMap.get("column_desc");
			String lDescription = (String) attrdescarr.get(0);
			lDescription =  InfoModel.unEscapeProtegeString(lDescription);
			attrClass.description = lDescription;
			
			glossMap.put(attrClass.rdfIdentifier, attrClass);
			glossTitleIdMap.put(attrClass.title, attrClass.rdfIdentifier);
		}
		return;
	}
}
