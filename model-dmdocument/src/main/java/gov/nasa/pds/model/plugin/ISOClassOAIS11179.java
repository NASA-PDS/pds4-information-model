package gov.nasa.pds.model.plugin; 

public class ISOClassOAIS11179 extends ISOClassOAIS {
	String regAuthId;								// registration authority identifier
	String steward;									// steward
	String nameSpaceId;								// namespace id - assigned namespace id with colon
	String nameSpaceIdNC;							// namespace id - assigned namespace id No Colon

	String anchorString;							// "class_" + lClass.nameSpaceIdNC + "_" + lClass.title
	
	boolean isUsedInModel;
	boolean isAbstract;
	boolean isFromLDD;									// has been ingested from Ingest_LDD

	
	public ISOClassOAIS11179 () {
		regAuthId = "TBD_registration_authority_identifier";
		steward = "TBD_steward";
		nameSpaceId = "TBD_namespaceid";
		nameSpaceIdNC = "TBD_namespaceidNC";
		
		anchorString = "TBD_anchorString";
		
		isUsedInModel = false;
		isAbstract = false;
		isFromLDD = false;
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
}
