package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.TreeMap;

public class ClassDOMCCSDSClassificationDefn {
	String identifier;
	String title;
	String namespaceId;
	String order;
	String definition;
	String indent;
	ArrayList <DOMClass> classArr;
	TreeMap <String, DOMClass> classMap;
	
	public ClassDOMCCSDSClassificationDefn (String id, String lorder, String ltitle, String lIndent, String ldefinition) {
		identifier = id; 
		title = ltitle;
		namespaceId = id;
		order = lorder;
		definition = ldefinition;
		indent = lIndent;
		classArr = new ArrayList <DOMClass> ();
		classMap = new TreeMap <String, DOMClass> ();
	} 
}
