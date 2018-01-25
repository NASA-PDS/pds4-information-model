package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class DOMRule  extends ISOClassOAIS11179 {
	String type;
	String roleId;
	String xpath;
	String attrTitle;
	String attrNameSpaceNC;
	String classTitle;
	String classNameSpaceNC;
	String classSteward;
	boolean alwaysInclude;		// the rule is to always be included in the schematron file
	boolean isMissionOnly;		// the rule is to be included in an LDDTool generated .sch file at the mission level
	ArrayList <String> letAssignArr;
	ArrayList <String> letAssignPatternArr;
	ArrayList <DOMAssert> assertArr;
	
	public DOMRule (String id) {
		identifier = id; 
		type = "TBD_type";
		roleId = "TBD_roleId";
		xpath = "TBD_xpath";
		attrTitle = "TBD_attributeName";
		attrNameSpaceNC = "TBD_attNameSpaceNC";
		classTitle = "TBD_classTitle";
		classNameSpaceNC = "TBD_NameSpaceNC";
		classSteward = "TBD_classSteward";
		alwaysInclude = false;
		isMissionOnly = false;
		
		letAssignArr = new ArrayList <String>();
		letAssignPatternArr = new ArrayList <String>();
		assertArr = new ArrayList <DOMAssert>();
	} 
	
	public void setRDFIdentifier () {
		rdfIdentifier = DMDocument.rdfPrefix + "." + identifier + "." + InfoModel.getNextUId();
	}
}
