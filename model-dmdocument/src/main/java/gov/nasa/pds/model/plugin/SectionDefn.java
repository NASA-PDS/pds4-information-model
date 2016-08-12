package gov.nasa.pds.model.plugin; 
import java.util.*;

public class SectionDefn extends Object {

	String identifier; 
	String title; 
	String description;
	String type;
	String secType;
	String secSubType;
	boolean secTOCFlag;
	boolean subSecTOCFlag;
	int texFormatInd;
	boolean includeFlag, imageFlag, TOCFlag;
	String imageFileName;
	String imageCaption;
	String selectConstraint;
	ArrayList <String> sectionModelContentId;


	public SectionDefn (String id) {
		identifier = id; 
		title = id; 
		description = "TBD_description"; 
		type = "TBD_type"; 
		secType = "text";
		secSubType = "content";
		secTOCFlag = false;
		subSecTOCFlag = false;
		texFormatInd = 0;
		includeFlag = false;
		imageFlag = false;
		imageFileName = "none";
		imageCaption = "none";
		selectConstraint = "none";
		sectionModelContentId = new ArrayList <String> ();		
		
	}  	
} 	
