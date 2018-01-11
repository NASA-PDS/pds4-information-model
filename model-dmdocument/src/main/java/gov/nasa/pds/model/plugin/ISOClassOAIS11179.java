package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.TreeMap;

public class ISOClassOAIS11179 extends ISOClassOAIS {
	String regAuthId;								// registration authority identifier
	String subModelId;								// identifier of submodel within the registration authority's model.
	String steward;									// steward
	String nameSpaceId;								// namespace id - assigned namespace id with colon
	String nameSpaceIdNC;							// namespace id - assigned namespace id No Colon

	String anchorString;							// "class_" + lClass.nameSpaceIdNC + "_" + lClass.title
	
	boolean isUsedInModel;
	boolean isAbstract;
	boolean isFromLDD;									// has been ingested from Ingest_LDD

	ArrayList <ISOClassOAIS11179> hasDOMObject;		// allows more than one object (DOMProp only - OVERRIDE in DOMProp)
	TreeMap <String, TermEntryDefn> termEntryMap;

	public ISOClassOAIS11179 () {
		regAuthId = "TBD_registration_authority_identifier";
		subModelId = "TBD_subModelId";
		steward = "TBD_steward";
		nameSpaceId = "TBD_namespaceid";
		nameSpaceIdNC = "TBD_namespaceidNC";
		
		anchorString = "TBD_anchorString";
		
		isUsedInModel = false;
		isAbstract = false;
		isFromLDD = false;
		
		hasDOMObject = new ArrayList <ISOClassOAIS11179> ();
		termEntryMap = new TreeMap <String, TermEntryDefn> ();
	}
	
	public String getAnchorString() {
		return anchorString;
	}
	
	public void setAnchorString(String anchorString) {
		this.anchorString = anchorString;
	}
	
	public String getRegAuthId() {
		return regAuthId;
	}
	
	public void setRegAuthId(String regAuthId) {
		this.regAuthId = regAuthId;
	}
	
	public String getSteward() {
		return steward;
	}
	
	public void setSteward(String steward) {
		this.steward = steward;
	}
	
	public String getNameSpaceId() {
		return nameSpaceIdNC + ":";
	}
	
	public void setNameSpaceId(String nameSpaceId) {
		this.nameSpaceId = nameSpaceId;
	} 
	
	public String getNameSpaceIdNC() {
		return nameSpaceIdNC;
	}
	
	public void setNameSpaceIdNC(String nameSpaceIdNC) {
		this.nameSpaceIdNC = nameSpaceIdNC;
		this.nameSpaceId = this.nameSpaceIdNC + ":";
	}
	
	//	get the name in the indicated language; use the attribute title as a default
	public String getNameInLanguage (String lLanguage) {
		if (lLanguage == null) return this.title;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.title;
		return lTermEntry.name;
	}
	
	//	get the definition in the indicated language; use the attribute description as a default
	public String getDefinitionInLanguage (String lLanguage) {
		if (lLanguage == null) return this.definition;
		TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
		if (lTermEntry == null) return this.definition;
		return lTermEntry.definition;
	}
}
