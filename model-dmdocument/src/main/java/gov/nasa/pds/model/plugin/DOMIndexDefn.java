package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class DOMIndexDefn {
	String identifier;
		
	TreeMap <String, DOMAttr> identifier1Map;  // has DE
	ArrayList <String> identifier2Arr; // has CD

	public DOMIndexDefn (String id) {
		identifier = id; 
		identifier1Map = new TreeMap <String, DOMAttr> ();
		identifier2Arr = new ArrayList <String>();
	}
	 
	public  ArrayList <DOMAttr> getSortedIdentifier1Arr () {
		ArrayList <DOMAttr> lIdentifier1Arr = new ArrayList <DOMAttr> (identifier1Map.values());
		return lIdentifier1Arr;
	}
	 
	public  ArrayList <String> getSortedIdentifier2Arr () {
		Collections.sort(identifier2Arr);
		return identifier2Arr;
	}
}


