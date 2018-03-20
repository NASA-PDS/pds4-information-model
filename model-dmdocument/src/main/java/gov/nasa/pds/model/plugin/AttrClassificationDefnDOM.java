package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.TreeMap;

public class AttrClassificationDefnDOM {
	String identifier;
	String namespaceId;
	ArrayList <DOMAttr> attrArr;
	TreeMap <String, DOMAttr> attrMap;
	
	public AttrClassificationDefnDOM (String id) {
		identifier = id; 
		namespaceId = id;
		attrArr = new ArrayList <DOMAttr> ();
                attrMap = new TreeMap <String, DOMAttr> ();
	} 
}
