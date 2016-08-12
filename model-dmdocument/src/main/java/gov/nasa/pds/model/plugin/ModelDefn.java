package gov.nasa.pds.model.plugin; 
public class ModelDefn extends Object {

	String identifier; // e.g.UpperPontModel 
	String type;       // e.g.ProtPontModel
	String filename;   // e.g.ProdModel.pont
	boolean ddincludeflag;
	Object objectid;

	public ModelDefn (String id) {
		identifier = id; 
		type = "TBD_type"; 
		filename = "TBD_filename"; 
		ddincludeflag = false;
		objectid = null;
	}  	
} 	
