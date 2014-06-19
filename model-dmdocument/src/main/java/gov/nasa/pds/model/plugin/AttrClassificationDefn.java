package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.TreeMap;

public class AttrClassificationDefn {
	String identifier;
	String namespaceId;
	ArrayList <AttrDefn> attrArr;
	TreeMap <String, AttrDefn> attrMap;
	
	public AttrClassificationDefn (String id) {
		identifier = id; 
		namespaceId = id;
		attrArr = new ArrayList <AttrDefn> ();
		attrMap = new TreeMap <String, AttrDefn> ();
	} 
}
