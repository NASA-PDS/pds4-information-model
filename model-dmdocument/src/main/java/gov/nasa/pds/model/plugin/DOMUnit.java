package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.Iterator;

public class DOMUnit extends ISOClassOAIS11179 {
	String pds4Identifier;
	String type;
	String precision;
	String default_unit_id;
	
	ArrayList <String> unit_id; 
	
	public DOMUnit () {          
		pds4Identifier = "TBD_pds4Identifier";
		type = "TBD_type";              
		precision = "TBD precision";                        
		default_unit_id = "TBD_default_unit_id";                                         

		unit_id = new ArrayList <String>();
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getPrecision() {
		return precision;
	}
	
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getDefault_unit_id() {
		return default_unit_id;
	}
	
	public void setDefault_unit_id(String default_unit_id) {
		this.default_unit_id = default_unit_id;
	}

	public ArrayList<String> getUnit_id() {
		return unit_id;
	}
	
	public void setUnit_id(ArrayList<String> unit_id) {
		this.unit_id = unit_id;
	}
	
	public void createDOMUnitSingletons (UnitDefn lUnitDefn) {
//		rdfIdentifier = "TBD"; 							
//		identifier = "TBD"; 
		versionId = "TBD"; 
//		sequenceId= "TBD";
		
		title = lUnitDefn.title;
//		definition = lUnitDefn.description;
		
//		registrationStatus = lUnitDefn.registrationStatus; 
//		isDeprecated = lUnitDefn.isDeprecated;
		
		regAuthId = DMDocument.masterNameSpaceIdNCLC; 
		steward = DMDocument.masterNameSpaceIdNCLC; 
		nameSpaceId = DMDocument.masterNameSpaceIdNCLC; 
		nameSpaceIdNC = DMDocument.masterNameSpaceIdNCLC; 
		
		type = lUnitDefn.type;
		precision = lUnitDefn.precision;
		default_unit_id = lUnitDefn.default_unit_id;

		for (Iterator <String> i = lUnitDefn.unit_id.iterator(); i.hasNext();) {
			String lUnitId = (String) i.next();
			this.unit_id.add(lUnitId);
		}
		return;
	}
}
