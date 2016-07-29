package gov.nasa.pds.model.plugin;
import java.util.ArrayList;

public class DomUnits extends ISOClassOAIS11179 {
	String type;
	String precision;
	String default_unit_id;
	
	ArrayList <String> unit_id; 
	
	public DomUnits () {          
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
}
