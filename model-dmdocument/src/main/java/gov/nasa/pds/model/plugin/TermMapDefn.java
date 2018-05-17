package gov.nasa.pds.model.plugin;

import java.util.ArrayList;
import java.util.Comparator;

public class TermMapDefn {
	 String identifier;
	 String namespaceId;
	 String stewardId;
	 String title;
	 String description;
	 String referenceIdentifier;
	 String skosRelationName;
	 String modelObjectId;
	 String modelObjectType;
	 String instanceId;
	 String parent;
	
	
	
	public TermMapDefn () {
		description = "";
		referenceIdentifier = null;
		instanceId = null;
		skosRelationName = "exactMatch"; // default for PDS4 obj
	}
	
	public void CopyFrom (TermMapDefn obj) {
		stewardId = obj.stewardId;
		namespaceId = obj.namespaceId;
		title = obj.title;
		parent = obj.parent;
		modelObjectId = obj.modelObjectId;
	}
	
	/*Comparator for sorting the list by modelObjectId*/
    public static Comparator<TermMapDefn> TermMapComparator = new Comparator<TermMapDefn>() {

	public int compare(TermMapDefn s1, TermMapDefn s2) {
	   String modelObjectId1 = s1.modelObjectId.toUpperCase();
	   String modelObjectId2 = s2.modelObjectId.toUpperCase();

	   //ascending order
	   return modelObjectId1.compareTo(modelObjectId2);

	   //descending order
	   //return modelObjectId2.compareTo(modelObojectId1);
    }};
    @Override
    public String toString() {
        return "[ identifier=" + identifier + "\n namespace=" + namespaceId + "\n title=" + title + "\n modelObjectId =" + modelObjectId + "]";
    }
}
