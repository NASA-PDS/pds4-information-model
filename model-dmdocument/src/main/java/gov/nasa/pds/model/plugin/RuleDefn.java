package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class RuleDefn {
	String identifier;
	String xpath;
	String role;
	String attrTitle;
	String attrNameSpaceNC;
	String classTitle;
	String classNameSpaceNC;
	boolean alwaysInclude;
	ArrayList <String> letAssignArr;
	ArrayList <AssertDefn2> assertArr;
	
	public RuleDefn (String id) {
		identifier = id; 
		xpath = "TBD_xpath";
		role = "";
		attrTitle = "TBD_attributeName";
		attrNameSpaceNC = "TBD_attNameSpaceNC";
		classTitle = "TBD_classTitle";
		classNameSpaceNC = "TBD_NameSpaceNC";	
		alwaysInclude = false;
		
		letAssignArr = new ArrayList <String>();
		assertArr = new ArrayList <AssertDefn2>();
	} 
}
