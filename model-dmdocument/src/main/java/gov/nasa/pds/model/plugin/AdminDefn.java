package gov.nasa.pds.model.plugin; 
public class AdminDefn {
	String identifier;
	String title;
	String administrativeNote; 
	String administrativeStatus;
	String changeDescription;
	String creationDate;
	String effectiveDate;
	String explanatoryComment;
	String lastChangeDate;
	String origin;
	String registrationStatus;
	String unresolvedIssue;
	String untilDate;

	public AdminDefn (String id) {
		identifier = id; 
		title = "TBD_title";
		administrativeNote = "TBD_administrativeNote";
		administrativeStatus = "TBD_administrativeStatus";
		changeDescription = "TBD_changeDescription";
		creationDate = "TBD_creationDate";
		effectiveDate = "TBD_effectiveDate";
		explanatoryComment = "TBD_explanatoryComment";
		lastChangeDate = "TBD_lastChangeDate";
		origin = "TBD_origin";
		registrationStatus = "TBD_registrationStatus";
		unresolvedIssue = "TBD_unresolvedIssue";
		untilDate = "TBD_untilDate";
	} 
}

