package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.TreeMap;

public class ClassClassificationDefnDOM {
	String identifier;
	String namespaceId;
	ArrayList <DOMClass> classArr;
	TreeMap <String, DOMClass> classMap;
	
	public ClassClassificationDefnDOM (String id) {
		identifier = id; 
		namespaceId = id;
		classArr = new ArrayList <DOMClass> ();
		classMap = new TreeMap <String, DOMClass> ();
	} 
}
