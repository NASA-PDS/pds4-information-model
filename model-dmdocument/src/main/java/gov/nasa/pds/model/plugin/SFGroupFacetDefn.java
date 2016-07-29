package gov.nasa.pds.model.plugin;

import java.util.ArrayList;

public class SFGroupFacetDefn {
	String identifier;
	String facet;
	ArrayList <String> subfacetArr;

	public SFGroupFacetDefn (String lClassName, String lFacetName) {
		identifier = lFacetName + "." + lClassName;
		facet = DMDocument.replaceString (lFacetName, "+", " ");
		subfacetArr = new ArrayList <String>();
	} 
}
