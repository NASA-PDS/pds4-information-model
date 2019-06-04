package gov.nasa.pds.model.plugin;
public class DOMUseCase  extends ISOClassOAIS11179 {
	String overview;
	String type;
	String actors;
	String preconditions;
	String successfulOutcome;
	String exceptions;
	String context;
	String steps;
	String trigger;

	
	public DOMUseCase (String id) {
		identifier = id; 
		type = "TBD_type";
		actors = "TBD_actors";
		preconditions = "TBD_preconditions";
		successfulOutcome = "TBD_successfulOutcome";
		exceptions = "TBD_exceptions";
		context = "TBD_context";
		steps = "TBD_steps";
		trigger = "TBD_trigger";
	} 
	
	public void setRDFIdentifier () {
		rdfIdentifier = DMDocument.rdfPrefix + "." + identifier + "." + InfoModel.getNextUId();
	}
}
