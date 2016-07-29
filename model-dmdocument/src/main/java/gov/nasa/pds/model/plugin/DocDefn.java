package gov.nasa.pds.model.plugin;
import java.util.*;

public class DocDefn extends Object {

	String identifier; 
	String title; 
	String subTitle; 
	String description;
	String author;
	String version;
	
	SectionDefn docSection;	
	ModelDefn docModel;	
	
	HashMap <String, SectionDefn> sectionMap;
	HashMap <String, ModelDefn> modelMap;
	HashMap <String, SectionContentDefn> sectionContentMap;
	
	ArrayList <String> sectionArray;

	public DocDefn (String id) {
		identifier = id; 
		title = id; 
		subTitle = "TBD_subtitle"; 
		description = "TBD_description"; 
		author = "TBD_author"; 
		version = "TBD_version"; 

		sectionMap = new HashMap <String, SectionDefn> ();
		modelMap = new HashMap <String, ModelDefn> ();
		sectionContentMap = new HashMap <String, SectionContentDefn>  ();
		
		sectionArray = new ArrayList <String> ();		
	}
} 	
