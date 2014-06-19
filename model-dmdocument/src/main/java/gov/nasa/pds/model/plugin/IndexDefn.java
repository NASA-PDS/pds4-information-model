package gov.nasa.pds.model.plugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class IndexDefn {
	String identifier;
		
	TreeMap <String, AttrDefn> identifier1Map;  // has DE
	ArrayList <String> identifier2Arr; // has CD

	public IndexDefn (String id) {
		identifier = id; 
		identifier1Map = new TreeMap <String, AttrDefn> ();
		identifier2Arr = new ArrayList <String>();
	}
	 
	public  ArrayList <AttrDefn> getSortedIdentifier1Arr () {
		ArrayList <AttrDefn> lIdentifier1Arr = new ArrayList <AttrDefn> (identifier1Map.values());
		return lIdentifier1Arr;
	}
	 
	public  ArrayList <String> getSortedIdentifier2Arr () {
		Collections.sort(identifier2Arr);
		return identifier2Arr;
	}
}


