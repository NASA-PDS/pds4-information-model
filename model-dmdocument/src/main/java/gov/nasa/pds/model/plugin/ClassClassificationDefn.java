package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.TreeMap;

public class ClassClassificationDefn {
	String identifier;
	String namespaceId;
	ArrayList <PDSObjDefn> classArr;
	TreeMap <String, PDSObjDefn> classMap;
	
	public ClassClassificationDefn (String id) {
		identifier = id; 
		namespaceId = id;
		classArr = new ArrayList <PDSObjDefn> ();
		classMap = new TreeMap <String, PDSObjDefn> ();
	} 
}
