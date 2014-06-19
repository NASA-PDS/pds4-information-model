package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DispDefn {
	String identifier;
	String title;
	String disposition;
	String section;
	String intNSId;									// namespace id - internally assigned namespace id

	
	public DispDefn (String id) {
		identifier = id; 
		title = "TBD_title";
		disposition = "TBD_disposition";
//		section = "TBD_section";
		section = "TBD_section";
		intNSId = "TBD_intNSID";
	} 
}
