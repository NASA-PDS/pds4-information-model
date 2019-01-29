package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DispDefn {
	String identifier;
	String used;									// used flag, Y, N, or I - Inactive
	String title;
	String disposition;
	String section;
	String intNSId;									// namespace id - internally assigned namespace id
	String intSteward;
	
	public DispDefn (String id) {
		identifier = id; 
		used = "TBD_used";
		title = "TBD_title";
		disposition = "TBD_disposition";
		section = "TBD_section";
		intNSId = "TBD_intNSID";
		intSteward = "TBD_intSteward";
	} 
}
