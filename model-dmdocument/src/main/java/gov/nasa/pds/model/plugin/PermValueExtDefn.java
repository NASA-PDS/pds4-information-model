package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;

public class PermValueExtDefn {
	String identifier;									// attribute name
	String xpath;										// class path to attribute
	ArrayList <PermValueDefn> permValueExtArr;			// set of permissible values
	
	public PermValueExtDefn (String lId) {
		identifier = lId; 
		xpath = "TBD_xpath";
		permValueExtArr = new ArrayList <PermValueDefn> (); 
	} 
}
